package com.autocana;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import com.autocana.listas.AdapterProducoes;
import com.autocana.listas.UserListProducoes;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

import com.autocana.cadastros.CadProducoes;

public class Producoes extends AppCompatActivity {

    private ImageButton btn_voltar, btn_adicionar;
    RecyclerView rcvLista;
    ArrayList<UserListProducoes> userListArrayList;
    AdapterProducoes adapter;
    FirebaseFirestore db;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_producoes);

        btn_voltar = (ImageButton) findViewById(R.id.btn_voltar);
        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Producoes.this, Dashboard.class);
                startActivity(i);
                finish();
            }
        });

        btn_adicionar = (ImageButton) findViewById(R.id.btn_adicionar);
        btn_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Producoes.this, CadProducoes.class);
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
        userListArrayList = new ArrayList<UserListProducoes>();
        adapter = new AdapterProducoes(Producoes.this, userListArrayList);

        rcvLista.setAdapter(adapter);

        EventChangeListener();

    }

    private void EventChangeListener() {

        db.collection("producoes").orderBy("nomePlantacao", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){

                            if(dc.getType() == DocumentChange.Type.ADDED){
                                userListArrayList.add(dc.getDocument().toObject(UserListProducoes.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void filterList(String text){
        ArrayList<UserListProducoes> listaFiltrada = new ArrayList<>();
        for(UserListProducoes item : userListArrayList){
            if(item.getNomePlantacao().toLowerCase().contains(text.toLowerCase())){
                listaFiltrada.add(item);
            }
        }

        if(listaFiltrada.isEmpty()){
            Toast.makeText(this, "Produção não encontrada!", Toast.LENGTH_SHORT).show();
        }else{
            adapter.setListaFiltrada(listaFiltrada);
        }
    }

}