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

public class CadastrarActivity extends AppCompatActivity {
    private ImageView imageView;
    static int PReqCode = 1;
    private final static int REQUEST_CODE =  1;

    private Uri imgUri;

    private EditText edtNome, edtEmail, edtSenha, edtSenhaConf;
    private Button btnCadastrar;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private StorageTask storageTask;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cadastrar);

        iniciaComponentes();

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtemValores();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 22) {
                    verificaPermissao();
                } else {
                    abrirGaleria();
                }
            }
        });

    }

    private void obtemValores() {
        final String nome = edtNome.getText().toString().trim();
        final String email = edtEmail.getText().toString().trim();
        final String senha = edtSenha.getText().toString().trim();
        final String senhaConf = edtSenhaConf.getText().toString().trim();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() ||  senhaConf.isEmpty() ){
            mostrarToast("Todos os campos devem ser preenchidos.");
        }else  if(!senha.equals(senhaConf)){
            mostrarToast("As senhas devem ser identicas.");
        }else{
            criarUsuario(email, senha, nome);
        }
    }

    private void criarUsuario(final String email, final String senha, final String nome){
        btnCadastrar.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    updateUsuario(email, senha, nome);
                    updateImagemUsuario();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();


                }else {
                    mostrarToast("Houve um erro.");
                    btnCadastrar.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void updateUsuario(String email, String senha, String nome){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        final String id = firebaseUser.getUid();
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setNome(nome);
        usuario.setSenha(senha);
        usuario.setId(id);
        databaseReference.child(id).setValue(usuario);





    }

    private void updateImagemUsuario() {
        StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+ getException(imgUri));

        reference.putFile(imgUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Log.d("UPLOAD TASK", "SUCCECIFUL");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }


    private void verificaPermissao() {
        if (ContextCompat.checkSelfPermission(CadastrarActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(CadastrarActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Toast.makeText(CadastrarActivity.this,"Please accept for required permission",Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(CadastrarActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }

        }else{
            abrirGaleria();
        }
    }

    private String getException(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }



    private void abrirGaleria() {
        Intent intentAbriGaleria = new Intent();
        intentAbriGaleria.setType("image/*");
        intentAbriGaleria.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentAbriGaleria,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data.getData() != null){
            imgUri = data.getData();
            imageView.setImageURI(imgUri);
        }
    }

    private void iniciaComponentes(){
        imageView = findViewById(R.id.imageViewCadastrar);
        edtNome= findViewById(R.id.edtNomeCadastrar);
        edtEmail= findViewById(R.id.edtEmailCadastrar);
        edtSenha= findViewById(R.id.edtSenhaCadastrar);
        edtSenhaConf= findViewById(R.id.edtSenhaConfCadastrar);
        btnCadastrar= findViewById(R.id.btnCadastrar);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Usuarios");
        storageReference = FirebaseStorage.getInstance().getReference().child("user_imagens/");
        progressBar = findViewById(R.id.progressBarCadastrar);
    }

    private void mostrarToast(String msg){
        Toast.makeText(getApplicationContext(),msg, Toast.LENGTH_LONG).show();
    }

}
