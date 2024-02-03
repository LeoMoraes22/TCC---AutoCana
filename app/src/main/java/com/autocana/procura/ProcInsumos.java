package com.autocana.procura;

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

import com.autocana.R;
import com.autocana.cadastros.CadEstimativas;
import com.autocana.listas.AdapterProcInsumos;
import com.autocana.listas.UserListColheita;
import com.autocana.listas.UserListProcInsumos;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProcInsumos extends AppCompatActivity {

    ImageButton btn_voltar;

    RecyclerView rcvLista;
    ArrayList<UserListProcInsumos> userListArrayList;
    AdapterProcInsumos adapter;
    FirebaseFirestore db;

    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_proc_insumos);

        btn_voltar = (ImageButton) findViewById(R.id.btn_voltar);
        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProcInsumos.this, CadEstimativas.class);
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
        userListArrayList = new ArrayList<UserListProcInsumos>();
        adapter = new AdapterProcInsumos(ProcInsumos.this, userListArrayList);

        rcvLista.setAdapter(adapter);

        EventChangeListener();
    }

    public void EventChangeListener(){
        db.collection("insumo").orderBy("nomeInsumo", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.e("Firebase error", error.getMessage());
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                userListArrayList.add(dc.getDocument().toObject(UserListProcInsumos.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void filterList(String text){
        ArrayList<UserListProcInsumos> listaFiltrada = new ArrayList<>();
        for(UserListProcInsumos item : userListArrayList){
            if(item.getNomeInsumo().toLowerCase().contains(text.toLowerCase())){
                listaFiltrada.add(item);
            }
        }

        if(listaFiltrada.isEmpty()){
            Toast.makeText(this, "Insumo não encontrada!", Toast.LENGTH_SHORT).show();
        }else{
            adapter.setListaFiltrada(listaFiltrada);
        }
    }
}