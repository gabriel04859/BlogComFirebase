package com.e.blog_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.e.blog_firebase.Model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class CadastrarActivity extends AppCompatActivity {
    private CircleImageView imageView;
    private final static int REQUEST_CODE =  1;
    private Uri imgUri;

    private EditText edtNome, edtEmail, edtSenha, edtSenhaConf;
    private Button btnCadastrar;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        iniciaComponentes();
        sharedPreferences = this.getSharedPreferences("USER", 0);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference().child("user_images/");

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtemValores();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    abrirGaleria();

            }
        });

    }

    private void obtemValores() {
        final String nome = edtNome.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        final String senha = edtSenha.getText().toString().trim();
        final String senhaConf = edtSenhaConf.getText().toString().trim();

        if (nome.isEmpty()){
            edtNome.setError("Campo Vazio");
            edtNome.requestFocus();
            return;
        }

        if (email.isEmpty()){
            edtEmail.setError("Campo Vazio");
            edtEmail.requestFocus();
            return;
        }

        if (senha.isEmpty()){
            edtSenha.setError("Campo Vazio");
            edtSenha.requestFocus();
            return;
        }

        if (senhaConf.isEmpty()){
            edtSenhaConf.setError("Campo Vazio");
            edtSenhaConf.requestFocus();
            return;
        }

        if (!senha.equals(senhaConf)){
            edtSenha.setError("As senhas devem ser indênticas.");
            edtSenha.requestFocus();
            edtSenhaConf.setError("As senhas devem ser idênticas.");
            edtSenhaConf.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        btnCadastrar.setVisibility(View.GONE);
        firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    criaUser(nome,email,senha);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                btnCadastrar.setVisibility(View.VISIBLE);
            }
        });
    }


    private void criaUser(final String nome, final String email, final String senha){
        final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getException(imgUri));
        reference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        final String id = firebaseUser.getUid();
                        Usuario usuario = new Usuario();
                        usuario.setId(id);
                        usuario.setNome(nome);
                        usuario.setEmail(email);
                        usuario.setSenha(senha);
                        usuario.setImgUri(uri.toString());



                        databaseReference.child("Users").child(id).setValue(usuario);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    private String getException(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }






    private void abrirGaleria() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data.getData() != null){
            imgUri = data.getData();
            imageView.setImageURI(imgUri);

        }
    }

    private void iniciaComponentes() {
        edtNome = findViewById(R.id.edtNomeCadastrar);
        edtEmail = findViewById(R.id.edtEmailCadastrar);
        edtSenha = findViewById(R.id.edtSenhaCadastrar);
        edtSenhaConf = findViewById(R.id.edtSenhaConfCadastrar);
        btnCadastrar = findViewById(R.id.btnCadastrar);
        imageView = findViewById(R.id.imageViewCadastrar);
        progressBar = findViewById(R.id.progressBarCadastrar);

    }
}
