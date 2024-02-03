package com.autocana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Cadastro extends AppCompatActivity {

    private ImageButton btn_voltar;
    private AppCompatButton btn_Cadastrar;
    private CheckBox chkMostarSenha;
    private EditText edtNome, edtTelefone, edtEmail, edtSenha, edtConfirmaSenha;
    private FirebaseAuth mAuth;
    String usuarioID;
    String ultimoDigito = "";

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_cadastro);

        // Volta para página de login
        btn_voltar = (ImageButton) findViewById(R.id.btn_voltar);
        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Cadastro.this, Principal.class);
                startActivity(i);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        edtNome = findViewById(R.id.edtNome);
        edtTelefone = findViewById(R.id.edtTelefone);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfirmaSenha = findViewById(R.id.edtConfirmaSenha);
        btn_Cadastrar = (AppCompatButton) findViewById(R.id.btn_Cadastrar);
        chkMostarSenha = findViewById(R.id.chkMostarSenha);

        chkMostarSenha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    edtSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edtConfirmaSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    edtSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edtConfirmaSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //Realiza o cadastro do usuário novo, faz o tratamento dos erros e gravas os dados do usuário
        btn_Cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String senha = edtSenha.getText().toString();
                String confirmaSenha = edtConfirmaSenha.getText().toString();

                if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(senha) || !TextUtils.isEmpty(confirmaSenha)){
                    if(senha.equals(confirmaSenha)){
                        mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    SalvarDadosUsuario();

                                    Toast.makeText(Cadastro.this, "Usuário cadastrado o sucesso!", Toast.LENGTH_LONG).show();

                                    Intent i = new Intent(Cadastro.this, Principal.class);
                                    startActivity(i);
                                    finish();
                                }else{
                                    String erro;

                                    try {
                                        throw task.getException();
                                    }catch (FirebaseAuthInvalidCredentialsException e){
                                        erro = "Digite um e-mail válido!";
                                    }catch (FirebaseAuthUserCollisionException e){
                                        erro = "Este e-mail já está sendo utilizado!";
                                    }catch (Exception e){
                                        erro = "Erro ao cadastrar usuário!"+e.getMessage();
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(Cadastro.this, erro , Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(Cadastro.this, "A senha deve ser a mesma em ambos os campos!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        //mascara de celular
        edtTelefone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Integer digitos = edtTelefone.getText().toString().length();
                if (digitos > 1){
                    ultimoDigito = edtTelefone.getText().toString().substring(digitos-1);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Integer digitos = edtTelefone.getText().toString().length();
                if (digitos == 2){
                    if (!ultimoDigito.equals(" ")){
                        edtTelefone.append(" ");
                    }else{
                        edtTelefone.getText().delete(digitos-1, digitos);
                    }
                }else if(digitos == 8){
                    if (!ultimoDigito.equals("-")){
                        edtTelefone.append("-");
                    }else{
                        edtTelefone.getText().delete(digitos-1, digitos);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private void SalvarDadosUsuario(){
        String nome = edtNome.getText().toString().trim();
        String telefone = edtTelefone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> usuarios = new HashMap<>();
        usuarios.put("nome", nome);
        usuarios.put("telefone", telefone);
        usuarios.put("e-mail", email);

        usuarioID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DocumentReference documentReference = db.collection("Usuarios").document(usuarioID);
        documentReference.set(usuarios).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("db", "Sucesso ao salvar os dados");
            }
        }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {
              Log.d("db_error", "Erro ao salvar os dados" + e.toString());
          }
        });
    }


}