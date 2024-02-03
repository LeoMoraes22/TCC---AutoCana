package com.autocana.cadastros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.autocana.Producoes;
import com.autocana.R;
import com.autocana.procura.ProcPlantProducao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CadProducoes extends AppCompatActivity {

    ImageButton btn_voltar, btn_procPlantacoes;

    private EditText edtNomePlantacao, edtDataInicial, edtDataFinal, edtDescricao;

    private AppCompatButton btn_Adicionar;

    AutoCompleteTextView autoColhido;
    ArrayAdapter<String> adapterItems;

    String id;
    String[] itens = {"Sim", "Não"};

    String ultimoDigito = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_cad_producoes);

        btn_voltar = (ImageButton) findViewById(R.id.btn_voltar);
        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CadProducoes.this, Producoes.class);
                startActivity(i);
                finish();
            }
        });

        //faz o dropdown menu
        autoColhido = (AutoCompleteTextView) findViewById(R.id.autoColhido);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, itens);
        autoColhido.setAdapter(adapterItems);
        autoColhido.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id){
                String item = adapterView.getItemAtPosition(i).toString();
            }
        });


        //Cria mascara para data inicial
        edtDataInicial = findViewById(R.id.edtDataInicial);
        edtDataInicial.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Integer digitos = edtDataInicial.getText().toString().length();
                if (digitos > 1){
                    ultimoDigito = edtDataInicial.getText().toString().substring(digitos-1);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer digitos = edtDataInicial.getText().toString().length();

                if (digitos == 2){
                    if (!ultimoDigito.equals("/")){
                        edtDataInicial.append("/");
                    }else{
                        edtDataInicial.getText().delete(digitos-1, digitos);
                    }
                }else if(digitos == 5){
                    if (!ultimoDigito.equals("/")){
                        edtDataInicial.append("/");
                    }else{
                        edtDataInicial.getText().delete(digitos-1, digitos);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        //Cria mascara para data final
        edtDataFinal = findViewById(R.id.edtDataFinal);
        edtDataFinal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Integer digitos = edtDataFinal.getText().toString().length();
                if (digitos > 1){
                    ultimoDigito = edtDataFinal.getText().toString().substring(digitos-1);
                }
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer digitos = edtDataFinal.getText().toString().length();

                if (digitos == 2){
                    if (!ultimoDigito.equals("/")){
                        edtDataFinal.append("/");
                    }else{
                        edtDataFinal.getText().delete(digitos-1, digitos);
                    }
                }else if(digitos == 5){
                    if (!ultimoDigito.equals("/")){
                        edtDataFinal.append("/");
                    }else{
                        edtDataFinal.getText().delete(digitos-1, digitos);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtDescricao = findViewById(R.id.edtDescricao);
        edtNomePlantacao = findViewById(R.id.edtNomePlantacao);
        btn_Adicionar = (AppCompatButton) findViewById(R.id.btn_Adicionar);

        btn_Adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomePlantacao = edtNomePlantacao.getText().toString();
                String dataInicial = edtDataFinal.getText().toString();
                String dataFinal = edtDataFinal.getText().toString();
                String colhido = autoColhido.getText().toString();
                String descricao = edtDescricao.getText().toString();

                if (!TextUtils.isEmpty(nomePlantacao)||!TextUtils.isEmpty(dataFinal)||!TextUtils.isEmpty(dataInicial)){
                    salvarDados();

                    Toast.makeText(CadProducoes.this, "Procução cadastrada com sucesso!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(CadProducoes.this, Producoes.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(CadProducoes.this, "Preencha os campos para realizar o cadastro.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_procPlantacoes = (ImageButton) findViewById(R.id.btn_procPlantacao);
        btn_procPlantacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CadProducoes.this, ProcPlantProducao.class);
                startActivity(i);
                finish();
            }
        });

        trasValores();
    }

    private void salvarDados(){
        String nomePlantacao = edtNomePlantacao.getText().toString().trim();
        String dataInicial = edtDataFinal.getText().toString().trim();
        String dataFinal = edtDataFinal.getText().toString().trim();
        String colhido = autoColhido.getText().toString().trim();
        String descricao = edtDescricao.getText().toString().trim();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object>producoes = new HashMap<>();
        producoes.put("nomePlantacao", nomePlantacao);
        producoes.put("dataInicial", dataInicial);
        producoes.put("dataFinal", dataFinal);
        producoes.put("colhido", colhido);
        producoes.put("descricao", descricao);

        id = nomePlantacao;

        DocumentReference documentReference = db.collection("producoes").document(id);
        documentReference.set(producoes).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("db", "Sucesso ao salvar os dados");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("db_error", "Erro ao salvar os dados" + e.toString());
            }
        });
    }

    public void trasValores(){
        // tras valores quando clicar em alterar os dados
        Intent i = getIntent();
        String nomePlantacao = i.getStringExtra("nomePlantacao");
        String dataInicial = i.getStringExtra("dataInicial");
        String dataFinal = i.getStringExtra("dataFinal");
        String colhido = i.getStringExtra( "colhido");
        String descricao = i.getStringExtra("descricao");
        String nome = i.getStringExtra("nome");

        edtNomePlantacao.setText(nomePlantacao);
        edtDataInicial.setText(dataInicial);
        edtDataFinal.setText(dataFinal);
        edtDescricao.setText(descricao);
        autoColhido.setText(colhido);

        if(!TextUtils.isEmpty(nome)){
            edtNomePlantacao.setText(nome);
        }
    }
}