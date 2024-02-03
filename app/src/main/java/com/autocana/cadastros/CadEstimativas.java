package com.autocana.cadastros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.autocana.Estimativas;
import com.autocana.R;
import com.autocana.procura.ProcInsumos;
import com.autocana.procura.ProcPlantColheita;
import com.autocana.procura.ProcPlantacoes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CadEstimativas extends AppCompatActivity {

    ImageButton btn_voltar, btn_procPlantacoes, btn_procInsumos;
    TextView tvQuantInsumos, tvQuantProducao, tvAlqueires, tvHectares;

    EditText edtNomePlantacacao, edtInsumo;

    AppCompatButton btn_Adicionar;

    String id, nomePlantacao, nomeInsumo, area, concentracao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_cad_estimativas);

        btn_Adicionar = (AppCompatButton) findViewById(R.id.btn_Adicionar);
        btn_procInsumos = (ImageButton) findViewById(R.id.btn_procInsumo);
        btn_procPlantacoes = (ImageButton) findViewById(R.id.btn_procPlantacao);
        btn_voltar = (ImageButton) findViewById(R.id.btn_voltar);
        edtNomePlantacacao = findViewById(R.id.edtNomePlantacao);
        edtInsumo = findViewById(R.id.edtNomeInsumo);
        tvAlqueires = findViewById(R.id.tvAlqueires);
        tvHectares = findViewById(R.id.tvHectares);
        tvQuantInsumos = findViewById(R.id.tvQuantInsumos);
        tvQuantProducao = findViewById(R.id.tvQuantProducao);
        btn_procPlantacoes = (ImageButton) findViewById(R.id.btn_procPlantacao);

        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CadEstimativas.this, Estimativas.class);
                startActivity(i);
                finish();
            }
        });

        btn_procPlantacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("chaveGeral", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("chaveNomeInsumo", nomeInsumo);
                editor.putString("chaveConcentracao", concentracao);
                editor.commit();
                Intent i = new Intent(CadEstimativas.this, ProcPlantacoes.class);
                startActivity(i);
                finish();
            }
        });

        btn_procInsumos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("chaveGeral2", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("chaveNomePlantacao", nomePlantacao);
                editor.putString("chaveArea", area);
                editor.commit();
                Intent i = new Intent(CadEstimativas.this, ProcInsumos.class);
                startActivity(i);
                finish();
            }
        });


        btn_Adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomePlantacao = edtNomePlantacacao.getText().toString();
                String nomeInsumo = edtInsumo.getText().toString();

                if(!TextUtils.isEmpty(nomePlantacao) ||!TextUtils.isEmpty(nomeInsumo)){
                    salvarDados();

                    Toast.makeText(CadEstimativas.this, "Estimativa salva com sucesso!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(CadEstimativas.this, Estimativas.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(CadEstimativas.this, "Preencha os campos para realizar o cadastro!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        trasValores();
    }

    public void salvarDados(){
        String nomePlantacao = edtNomePlantacacao.getText().toString().trim();
        String nomeInsumo = edtInsumo.getText().toString().trim();
        String alqueires = tvAlqueires.getText().toString().trim();
        String hectares = tvHectares.getText().toString().trim();
        String quantInsumo = tvQuantInsumos.getText().toString().trim();
        String quantProducao = tvQuantProducao.getText().toString().trim();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object>estimativa = new HashMap<>();
        estimativa.put("nomePlantacao", nomePlantacao);
        estimativa.put("nomeInsumo", nomeInsumo);
        estimativa.put("alqueires", alqueires);
        estimativa.put("hectares", hectares);
        estimativa.put("quantInsumo", quantInsumo);
        estimativa.put("quantProducao", quantProducao);

        id = nomePlantacao;

        DocumentReference documentReference = db.collection("estimativa").document(id);
        documentReference.set(estimativa).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("db", "Sucesso ao salvar os dados");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db", "Erro ao salvar os dados" + e.toString());
            }
        });
    }

    public void trasValores(){
        Intent i = getIntent();

        //insumo
        nomeInsumo = i.getStringExtra("nomeInsumo");
        concentracao = i.getStringExtra("concentracao");

        //plantação
        nomePlantacao = i.getStringExtra("nome");
        area = i.getStringExtra("area");


        if(i != null && i.getExtras() !=null){
            if(nomeInsumo == null || concentracao == null){
                SharedPreferences prefs = getSharedPreferences("chaveGeral", MODE_PRIVATE);
                nomeInsumo=prefs.getString("chaveNomeInsumo", "");
                concentracao=prefs.getString("chaveConcentracao", "");
            }

            if(nomePlantacao == null || area == null){
                SharedPreferences prefs = getSharedPreferences("chaveGeral2", MODE_PRIVATE);
                nomePlantacao=prefs.getString("chaveNomePlantacao", "");
                area=prefs.getString("chaveArea", "");
            }
        }

        edtNomePlantacacao.setText(nomePlantacao);
        edtInsumo.setText(nomeInsumo);


        if(area != null && area != ""){
            float Area = Float.parseFloat(area);
            float QuantidadeDeProducao = (float) (Area * 6.9355);
            float QuantidaDeHectares = (float) (Area /10000);
            float QuantidaDeAlqueires = (float) (Area/24200);
            String quantDeHectares = String.valueOf(QuantidaDeHectares);
            String quantDeAlqueires = String.valueOf(QuantidaDeAlqueires);
            String quantDeProducao = String.valueOf(QuantidadeDeProducao);

            tvQuantProducao.setText(quantDeProducao);
            tvHectares.setText(quantDeHectares);
            tvAlqueires.setText(quantDeAlqueires);
        }

        if(concentracao != null && concentracao != ""){
            float Concentracao = Float.parseFloat(concentracao);
            float QuantidadeDeInsumo = (float) (Concentracao/10);
            String quantidadeDeInsumo = String.valueOf(QuantidadeDeInsumo);

            tvQuantInsumos.setText(quantidadeDeInsumo);
        }
    }
}