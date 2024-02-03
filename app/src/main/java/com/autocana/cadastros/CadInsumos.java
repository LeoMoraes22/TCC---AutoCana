package com.autocana.cadastros;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.autocana.Insumos;
import com.autocana.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CadInsumos extends AppCompatActivity {

    private ImageButton btn_voltar;
    private AppCompatButton btnAdiconarInsumo;
    private EditText edtNomeInsumo, edtQuantidade, edtDescricao;
    AutoCompleteTextView autoCompleteText;
    ArrayAdapter<String> adapterItems;
    String[] itens = {"Adubo", "Abudo Granulado"};
    String id;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_cad_insumos);

        btn_voltar = (ImageButton) findViewById(R.id.btn_voltar);
        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CadInsumos.this, Insumos.class);
                startActivity(i);
                finish();
            }
        });

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

        edtQuantidade = findViewById(R.id.edtQuantidade);
        edtDescricao = findViewById(R.id.edtDescricao);
        edtNomeInsumo = findViewById(R.id.edtNomeInsumo);

        btnAdiconarInsumo = (AppCompatButton) findViewById(R.id.btnAdicionarInsumo);
        btnAdiconarInsumo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeInsumo = edtNomeInsumo.getText().toString();
                String descricao = edtDescricao.getText().toString();
                String concentracao = edtQuantidade.getText().toString();
                String unidade = autoCompleteText.getText().toString();

                if(!TextUtils.isEmpty(nomeInsumo) || !TextUtils.isEmpty(concentracao) || !TextUtils.isEmpty(unidade)){
                     salvarDados();

                    Toast.makeText(CadInsumos.this, "Insumo cadastrados com sucesso!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(CadInsumos.this, Insumos.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(CadInsumos.this, "Preencha os campos para realizar o cadastro!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        trasValores();
    }

    private void salvarDados(){
        String nomeInsumo = edtNomeInsumo.getText().toString().trim();
        String descricao = edtDescricao.getText().toString().trim();
        String concentracao = edtQuantidade.getText().toString().trim();
        String unidade = autoCompleteText.getText().toString().trim();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object>insumo = new HashMap<>();
        insumo.put("nomeInsumo", nomeInsumo);
        insumo.put("descricao", descricao);
        insumo.put("concentracao", concentracao);
        insumo.put("unidade", unidade);

        id = nomeInsumo;

        DocumentReference documentReference = db.collection("insumo").document(id);
        documentReference.set(insumo).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        String nomeInsumo = i.getStringExtra("nomeInsumo");
        String concentracao = i.getStringExtra("concentracao");
        String unidade = i.getStringExtra("unidade");
        String descricao = i.getStringExtra("descricao");

        edtNomeInsumo.setText(nomeInsumo);
        edtQuantidade.setText(concentracao);
        edtDescricao.setText(descricao);
        autoCompleteText.setText(unidade);
    }
}