package com.autocana.procura;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.autocana.R;
import com.autocana.cadastros.CadColheita;
import com.autocana.cadastros.CadProducoes;
import com.autocana.listas.AdapterProcPlantacoesColheita;
import com.autocana.listas.AdapterProcPlantacoesProducoes;
import com.autocana.listas.UserListProcPlantacoesColheita;
import com.autocana.listas.UserListProcPlantacoesProducoes;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProcPlantProducao extends AppCompatActivity {

    ImageButton btn_voltar;

    RecyclerView rcvLista;

    ArrayList<UserListProcPlantacoesProducoes> userListArrayList;
    AdapterProcPlantacoesProducoes adapter;
    FirebaseFirestore db;
    SearchView searchView;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proc_plant_producao);

        btn_voltar = (ImageButton) findViewById(R.id.btn_voltar);
        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProcPlantProducao.this, CadProducoes.class);
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
        userListArrayList = new ArrayList<UserListProcPlantacoesProducoes>();
        adapter = new AdapterProcPlantacoesProducoes(ProcPlantProducao.this, userListArrayList);

        rcvLista.setAdapter(adapter);

        EventChangeListener();
    }

    public void EventChangeListener(){
        db.collection("plantacoes").orderBy("nome", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.e("Firestore error", error.getMessage());
                        }

                        for (DocumentChange dc : value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                userListArrayList.add(dc.getDocument().toObject(UserListProcPlantacoesProducoes.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void filterList(String text){
        ArrayList<UserListProcPlantacoesProducoes> listaFiltrada = new ArrayList<>();
        for(UserListProcPlantacoesProducoes item : userListArrayList){
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