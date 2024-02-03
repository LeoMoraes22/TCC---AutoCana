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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.autocana.Plantacoes;
import com.autocana.R;
import com.autocana.mapa.Mapa;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CadPlantacoes extends AppCompatActivity {

    ImageButton btn_voltar, btn_areaPlantada;
    private AppCompatButton btn_Aidiconar;

    private EditText edtNomePlantacao, edtCidade, edtArea, edtDescricao;
    AutoCompleteTextView autoEstado;
    ArrayAdapter<String> adapterItems;
    String[] itens = {"AC","AL","AP","AM","BA","CE","DF","ES","GO","MA","MT","MS","MG","PA","PB","PR","PE","PI","RJ","RN","RS","RO","RR","SC","SP","SE","TO"};
    String id, nomePlantacaoSalvo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_cad_plantacoes);

        btn_voltar = (ImageButton) findViewById(R.id.btn_voltar);
        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CadPlantacoes.this, Plantacoes.class);
                startActivity(i);
                finish();
            }
        });



        btn_areaPlantada = (ImageButton) findViewById(R.id.btn_areaPlantada);
        btn_areaPlantada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CadPlantacoes.this, Mapa.class);
                startActivity(i);
                finish();
            }
        });


        //faz o dropdown menu
        autoEstado = (AutoCompleteTextView) findViewById(R.id.autoEstado);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, itens);
        autoEstado.setAdapter(adapterItems);
        autoEstado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long id){
                String item = adapterView.getItemAtPosition(i).toString();
            }
        });

        edtNomePlantacao = findViewById(R.id.edtNomePlantacao);
        edtCidade = findViewById(R.id.edtCidade);
        edtArea = findViewById(R.id.edtArea);
        edtDescricao = findViewById(R.id.edtDescricao);
        btn_Aidiconar = (AppCompatButton) findViewById(R.id.btn_Adicionar);

        btn_Aidiconar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomePlantacao = edtNomePlantacao.getText().toString();
                String cidade = edtCidade.getText().toString();
                String area = edtArea.getText().toString();
                String descricao = edtDescricao.getText().toString();
                String estado = autoEstado.getText().toString();

                if (!TextUtils.isEmpty(nomePlantacao) || !TextUtils.isEmpty(cidade) || !TextUtils.isEmpty(area) || !TextUtils.isEmpty(estado)){
                    salvarDados();

                    Toast.makeText(CadPlantacoes.this, "Plantação cadastrada com sucesso!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(CadPlantacoes.this, Plantacoes.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(CadPlantacoes.this, "Preencha os campos para realizar o cadastro!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        trasValores();
    }

    private void salvarDados(){
        String nomePlantacao = edtNomePlantacao.getText().toString().trim();
        String cidade = edtCidade.getText().toString().trim();
        String area = edtArea.getText().toString().trim();
        String descricao = edtDescricao.getText().toString().trim();
        String estado = autoEstado.getText().toString().trim();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object>plantacoes = new HashMap<>();
        plantacoes.put("nome", nomePlantacao);
        plantacoes.put("cidade", cidade);
        plantacoes.put("estado", estado);
        plantacoes.put("area", area);
        plantacoes.put("descricao", descricao);

        id = nomePlantacao;

        DocumentReference documentReference = db.collection("plantacoes").document(id);
        documentReference.set(plantacoes).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        String nome = i.getStringExtra("nome");
        String cidade = i.getStringExtra("cidade");
        String estado = i.getStringExtra("estado");
        String area = i.getStringExtra( "area");
        String descricao = i.getStringExtra("descricao");

        edtNomePlantacao.setText(nome);
        edtCidade.setText(cidade);
        edtArea.setText(area);
        edtDescricao.setText(descricao);
        autoEstado.setText(estado);
    }
}