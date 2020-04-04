package com.e.blog_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.e.blog_firebase.Model.Postagem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;
import java.util.UUID;

public class AddActivity extends AppCompatActivity {
    private ImageView imgAdd;
    private EditText edtTitulo, edtDescricao;
    private Button btnAdd;
    private ProgressBar progressBar;

    private final int REQUEST_CODE = 1;
    private Uri imgUriAdd;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        getSupportActionBar().hide();

        iniciaComponentes();

        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              updateImagemPostagem();
            }
        });
    }

    private void iniciaComponentes() {
        imgAdd = findViewById(R.id.imageViewAdd);
        edtTitulo = findViewById(R.id.edtTituloAdd);
        edtDescricao = findViewById(R.id.edtDescricaoAdd);
        btnAdd = findViewById(R.id.btnAdd);
        progressBar = findViewById(R.id.progressBarAdd);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Postagens");
        storageReference = FirebaseStorage.getInstance().getReference().child("postagem_imagens/");
    }

    private void addPostagem(String imagem){
        String titulo = edtTitulo.getText().toString().trim();
        String descricao = edtTitulo.getText().toString().trim();
        final String id = UUID.randomUUID().toString();

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        final String idUser = firebaseUser.getUid();

        final Postagem postagem = new Postagem();
        postagem.setDescricao(descricao);
        postagem.setTitulo(titulo);
        postagem.setId(id);
        postagem.setIdUsuario(idUser);
        postagem.setImage(imagem);
        databaseReference.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                databaseReference.child(id +" - " + idUser).setValue(postagem);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
    private void abrirGaleria(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data.getData() != null){
            imgUriAdd = data.getData();
            imgAdd.setImageURI(imgUriAdd);

        }
    }

    private String getException(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void updateImagemPostagem() {
        StorageReference reference = storageReference.child(System.currentTimeMillis()+"."+ getException(imgUriAdd));
        String id = reference.toString();
        addPostagem(id);

        reference.putFile(imgUriAdd)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                    }
                });
    }
}
