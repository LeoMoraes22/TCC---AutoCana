package com.autocana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class RecuperarSenha extends AppCompatActivity {

    EditText edtEmail;
    ImageButton btn_voltar;

    AppCompatButton btn_recuperaSenha;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_recuperar_senha);

        mAuth = FirebaseAuth.getInstance();
        edtEmail = findViewById(R.id.edtEmail);
        btn_recuperaSenha = (AppCompatButton) findViewById(R.id.btn_recuperaSenha);
        btn_voltar = (ImageButton) findViewById(R.id.btn_voltar);

        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecuperarSenha.this, Principal.class);
                startActivity(i);
                finish();
            }
        });

        btn_recuperaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redefineSenha();
            }
        });

    }

    private void redefineSenha(){
        String email = edtEmail.getText().toString().trim();

        if (email.isEmpty()){
            Toast.makeText(getBaseContext(), "É necessário informar o e-mail para realizar a recuperação de senha", Toast.LENGTH_SHORT).show();
        }else {
            mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getBaseContext(), "Acesse o link enviado ao seu e-mail para redefinir ssua senha!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    String erro = e.toString();

                    if (erro.contentEquals("The email address is badly formatted")) {
                        Toast.makeText(getBaseContext(), "E-mail inválido!", Toast.LENGTH_SHORT).show();
                    } else if (erro.contentEquals("EMAIL_NOT_FOUND")) {
                        Toast.makeText(getBaseContext(), "E-mail não cadastrado!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}