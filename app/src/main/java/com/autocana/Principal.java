package com.autocana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class Principal extends AppCompatActivity {


    private AppCompatButton btnCadastrarse, btnLogin;
    private EditText edtEmail, edtSenha;
    private TextView tvRecuperarSenha;
    private CheckBox chkMostarSenha;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_principal);

        // Deixar texto sublinhado
        tvRecuperarSenha = (TextView) findViewById(R.id.tvRecuperaSenha);
        String texto = "Esqueceu sua senha?";
        SpannableString spannableString = new SpannableString(texto);
        spannableString.setSpan(new UnderlineSpan(), 0, texto.length(), 0);
        tvRecuperarSenha.setText(spannableString);

        //Redefine a senha
        tvRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Principal.this, RecuperarSenha.class);
                startActivity(i);
                finish();
            }
        });

        //Entrar na tela de cadastro
        btnCadastrarse = (AppCompatButton) findViewById(R.id.btnCadastrarse);
        btnCadastrarse.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               Intent i = new Intent(Principal.this, Cadastro.class);
               startActivity(i);
               finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtSenha = (EditText) findViewById(R.id.edtSenha);
        chkMostarSenha = (CheckBox) findViewById(R.id.chkMostarSenha);

        //Realiza login e entra na dashboard
        btnLogin = (AppCompatButton) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmail = edtEmail.getText().toString();
                String loginSenha = edtSenha.getText().toString();

                if(!TextUtils.isEmpty(loginEmail) || !TextUtils.isEmpty(loginSenha)){
                    mAuth.signInWithEmailAndPassword(loginEmail, loginSenha)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent i = new Intent(Principal.this, Dashboard.class);
                                startActivity(i);
                                finish();
                            }else {
                                String erro;

                                try {
                                    throw task.getException();
                                }catch (Exception e){
                                    erro = "Erro ao realizar o login!"+e.getMessage();
                                    e.printStackTrace();
                                }
                                Toast.makeText(Principal.this, erro , Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        //Exibe a senha digitada
        chkMostarSenha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    edtSenha.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    edtSenha.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            Intent i = new Intent(Principal.this, Dashboard.class);
            startActivity(i);
            finish();
        }
    }
    
}