package com.autocana;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.autocana.cadastros.CadColheita;
import com.autocana.listas.AdapterColheita;
import com.autocana.listas.UserListColheita;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class Colheita extends AppCompatActivity {

    private ImageButton btn_voltar, btn_adicionar, btn_pdf;
    RecyclerView rcvLista;
    ArrayList<UserListColheita> userListArrayList;
    AdapterColheita adapter;
    FirebaseFirestore db;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_colheita);

        btn_voltar = (ImageButton) findViewById(R.id.btn_voltar);
        btn_adicionar= (ImageButton) findViewById(R.id.btn_adicionar);
        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Colheita.this, Dashboard.class);
                startActivity(i);
                finish();
            }
        });

        btn_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Colheita.this, CadColheita.class);
                startActivity(i);
                finish();
            }
        });

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        rcvLista = findViewById(R.id.rcvLista);
        rcvLista.setHasFixedSize(true);
        rcvLista.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        userListArrayList = new ArrayList<UserListColheita>();
        adapter = new AdapterColheita(Colheita.this, userListArrayList);

        rcvLista.setAdapter(adapter);

        EventChangeListener();
    }

    private void EventChangeListener() {

        db.collection("colheita")
                .orderBy("plantacao", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){

                            if(dc.getType() == DocumentChange.Type.ADDED){
                                userListArrayList.add(dc.getDocument().toObject(UserListColheita.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void filterList(String text){
        ArrayList<UserListColheita> listaFiltrada = new ArrayList<>();
        for(UserListColheita item : userListArrayList){
            if(item.getPlantacao().toLowerCase().contains(text.toLowerCase())){
                listaFiltrada.add(item);
            }
        }

        if(listaFiltrada.isEmpty()){
            Toast.makeText(this, "Colheita não encontrada!", Toast.LENGTH_SHORT).show();
        }else{
            adapter.setListaFiltrada(listaFiltrada);
        }
    }
}