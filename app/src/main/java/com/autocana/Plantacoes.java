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


import com.autocana.cadastros.CadPlantacoes;
import com.autocana.listas.AdapterPlantacoes;
import com.autocana.listas.UserListPlantacoes;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Plantacoes extends AppCompatActivity {

    private ImageButton btn_voltar, btn_adicionar;
    RecyclerView rcvLista;
    ArrayList<UserListPlantacoes> userListArrayList;
    AdapterPlantacoes adapter;
    FirebaseFirestore db;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_plantacoes);

        btn_voltar = (ImageButton) findViewById(R.id.btn_voltar);
        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Plantacoes.this, Dashboard.class);
                startActivity(i);
                finish();
            }
        });

        btn_adicionar = (ImageButton) findViewById(R.id.btn_adicionar);
        btn_adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Plantacoes.this, CadPlantacoes.class);
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
        userListArrayList = new ArrayList<UserListPlantacoes>();
        adapter = new AdapterPlantacoes(Plantacoes.this, userListArrayList);

        rcvLista.setAdapter(adapter);

        EventChangeListener();
    }

    private void EventChangeListener() {
        db.collection("plantacoes")
                .orderBy("nome", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){

                            if(dc.getType() == DocumentChange.Type.ADDED){
                                userListArrayList.add(dc.getDocument().toObject(UserListPlantacoes.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void filterList(String text){
        ArrayList<UserListPlantacoes> listaFiltrada = new ArrayList<>();
        for(UserListPlantacoes item : userListArrayList){
            if(item.getNome().toLowerCase().contains(text.toLowerCase())){
                listaFiltrada.add(item);
            }
        }

        if(listaFiltrada.isEmpty()){
            Toast.makeText(this, "Plantação não encontrada!", Toast.LENGTH_SHORT).show();
        }else{
            adapter.setListaFiltrada(listaFiltrada);
        }
    }
}