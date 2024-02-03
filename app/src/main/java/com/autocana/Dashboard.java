package com.autocana;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Dashboard extends AppCompatActivity {

    private ImageButton btn_logout, btn_plantacoes, btn_producoes, btn_insumos, btn_colheita, btn_estimativas;

    private TextView tvNome;
    private FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String usuarioID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_dashboard);

        mAuth = FirebaseAuth.getInstance();
        btn_colheita = (ImageButton) findViewById(R.id.btn_colheita);
        btn_plantacoes = (ImageButton) findViewById(R.id.btn_plantacoes);
        btn_producoes = (ImageButton) findViewById(R.id.btn_producoes);
        btn_insumos = (ImageButton) findViewById(R.id.btn_insumos);
        btn_estimativas = (ImageButton) findViewById(R.id.btn_estimativas);

        tvNome = findViewById(R.id.tvNome);



        btn_logout = (ImageButton) findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deslogar();
            }
        });

        btn_insumos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insumos();
            }
        });

        btn_colheita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colheita();
            }
        });

        btn_plantacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                plantacoes();
            }
        });

        btn_producoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                producoes();
            }
        });

        btn_estimativas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                estimativas();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(documentSnapshot != null){
                    tvNome.setText(documentSnapshot.getString("nome"));
                }
            }
        });
    }

    private void deslogar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Deseja realmente sair ?");

        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();

                Intent i = new Intent(Dashboard.this, Principal.class);
                startActivity(i);
                finish();
            }
        });
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void plantacoes(){
        Intent i = new Intent(Dashboard.this, Plantacoes.class);
        startActivity(i);
        finish();
    }

    private void producoes(){
        Intent i = new Intent(Dashboard.this, Producoes.class);
        startActivity(i);
        finish();
    }

    private void insumos(){
        Intent i = new Intent(Dashboard.this, Insumos.class);
        startActivity(i);
        finish();
    }

    private void colheita(){
        Intent i = new Intent(Dashboard.this, Colheita.class);
        startActivity(i);
        finish();
    }

    private void estimativas(){
        Intent i = new Intent(Dashboard.this, Estimativas.class);
        startActivity(i);
        finish();
    }
}