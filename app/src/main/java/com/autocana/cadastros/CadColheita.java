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

import com.autocana.Colheita;
import com.autocana.R;
import com.autocana.procura.ProcPlantColheita;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CadColheita extends AppCompatActivity {

    private FirebaseAuth mAuth;

    ImageButton btn_voltar, btn_procPlantacoes;

    private AppCompatButton btn_Adicionar;

    private EditText edtPlantacao, edtDataColheita, edtQuantidade, edtPlaca, edtAnotacoes;

    AutoCompleteTextView autoCompleteText;
    ArrayAdapter<String> adapterItems;

    String id;
    String[] itens = {"Kg", "Ton"};
    String ultimoDigito = "";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_cad_colheita);

        //faz o dropdown menu
        autoCompleteText = (AutoCompleteTextView) findViewById(R.id.autoUniMedida);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, itens);
        autoCompleteText.setAdapter(adapterItems);
        autoCompleteText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id){
                String item = adapterView.getItemAtPosition(i).toString();
            }
        });

        btn_voltar = (ImageButton) findViewById(R.id.btn_voltar);
        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CadColheita.this, Colheita.class);
                startActivity(i);
                finish();
            }
        });

        //Cria mascara para data
        edtDataColheita = findViewById(R.id.edtDataColheita);
        edtDataColheita.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Integer digitos = edtDataColheita.getText().toString().length();
                if (digitos > 1){
                    ultimoDigito = edtDataColheita.getText().toString().substring(digitos-1);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer digitos = edtDataColheita.getText().toString().length();

                if (digitos == 2){
                    if (!ultimoDigito.equals("/")){
                        edtDataColheita.append("/");
                    }else{
                        edtDataColheita.getText().delete(digitos-1, digitos);
                    }
                }else if(digitos == 5){
                    if (!ultimoDigito.equals("/")){
                        edtDataColheita.append("/");
                    }else{
                        edtDataColheita.getText().delete(digitos-1, digitos);
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtAnotacoes = findViewById(R.id.edtAnotacoes);
        edtPlaca = findViewById(R.id.edtPlaca);
        edtPlantacao = findViewById(R.id.edtPlantacao);
        edtQuantidade = findViewById(R.id.edtQuantidade);
        btn_Adicionar = (AppCompatButton) findViewById(R.id.btn_Adicionar);
        btn_procPlantacoes = (ImageButton) findViewById(R.id.btn_procPlantacao);

        btn_Adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String anotacoes = edtAnotacoes.getText().toString();
                String placa = edtPlaca.getText().toString();
                String plantacao = edtPlantacao.getText().toString();
                String quantidade = edtQuantidade.getText().toString();
                String unidade = autoCompleteText.getText().toString();
                String dataColheita = edtDataColheita.getText().toString();

                if(!TextUtils.isEmpty(plantacao) || !TextUtils.isEmpty(quantidade) || !TextUtils.isEmpty(placa)){
                    salvarDados();

                    Toast.makeText(CadColheita.this, "Colheita cadastrada com sucesso!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(CadColheita.this, Colheita.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(CadColheita.this, "Preencha os campos para realizar o cadastro!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_procPlantacoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CadColheita.this, ProcPlantColheita.class);
                startActivity(i);
                finish();
            }
        });

        trasValores();
    }

    private void salvarDados(){
        String anotacoes = edtAnotacoes.getText().toString().trim();
        String placa = edtPlaca.getText().toString().trim();
        String plantacao = edtPlantacao.getText().toString().trim();
        String quantidade = edtQuantidade.getText().toString().trim();
        String unidade = autoCompleteText.getText().toString().trim();
        String dataColheita = edtDataColheita.getText().toString().trim();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object>colheita = new HashMap<>();
        colheita.put("plantacao", plantacao);
        colheita.put("quantidade", quantidade);
        colheita.put("unidade", unidade);
        colheita.put("placa", placa);
        colheita.put("anotacoes", anotacoes);
        colheita.put("dataColheita", dataColheita);


        id =plantacao;

        DocumentReference documentReference = db.collection("colheita").document(id);
        documentReference.set(colheita).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        String plantacao = i.getStringExtra("plantacao");
        String placa = i.getStringExtra("placa");
        String unidade = i.getStringExtra("unidade");
        String quantidade = i.getStringExtra( "quantidade");
        String anotacoes = i.getStringExtra("anotacoes");
        String dataColheita = i.getStringExtra("dataColheita");
        String nomePlantacao = i.getStringExtra("nome");

        edtPlantacao.setText(plantacao);
        edtPlaca.setText(placa);
        edtAnotacoes.setText(anotacoes);
        edtQuantidade.setText(String.valueOf(quantidade));
        edtDataColheita.setText(dataColheita);
        autoCompleteText.setText(unidade);

        if(!TextUtils.isEmpty(nomePlantacao)){
            edtPlantacao.setText(nomePlantacao);
        }
    }
}