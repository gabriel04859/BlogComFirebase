package com.e.blog_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText edtEmail, edtSenha;
    private TextView txtCadastrar;
    private Button btnLogin;

    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        iniciaComponentes();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
               }
        });
        txtCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CadastrarActivity.class));
            }
        });
    }

    private void loginUser() {
        final String email = edtEmail.getText().toString().trim();
        final String senha = edtSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()){
            mostrarToast("Preencha todos os campos");
        }else{
            btnLogin.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }else {
                        mostrarToast("Ocorreu um erro, tente novamente.");
                        btnLogin.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                }
            });
        }
    }

    private void mostrarToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

    }

    private void iniciaComponentes() {
        edtEmail= findViewById(R.id.edtEmailLogin);
        edtSenha= findViewById(R.id.edtSenhaLogin);
        btnLogin= findViewById(R.id.btnLogin);
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBarLogin);
        txtCadastrar = findViewById(R.id.txtCadastrar);

    }
}
