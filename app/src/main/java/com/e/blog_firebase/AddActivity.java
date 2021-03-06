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

import com.e.blog_firebase.Model.PostUser;
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

import de.hdodenhof.circleimageview.CircleImageView;

public class AddActivity extends AppCompatActivity {
    private ImageView imgPost;
    private EditText txtTitulo, txtDescricao;
    private CircleImageView btnAdd;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;

    private final int REQUEST_CODE = 1;
    private Uri imgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        iniciaComponentes();
        firebaseAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference().child("postagem_imagens/");
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtemDados();
            }
        });

        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

    }

    private void obtemDados(){
        btnAdd.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        String titulo = txtTitulo.getText().toString();
        String descricao = txtDescricao.getText().toString();
        if (titulo.isEmpty()){
            txtTitulo.setError("Título não pode estar vazio.");
            txtTitulo.requestFocus();
            return;
        }
        updatePostagem(titulo, descricao);
    }

    private void criaPostagem(String titulo, String descricao, final Uri uriImg){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String idUser = user.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mDatabaseReference = databaseReference.child("Post");
        final Postagem postagem = new Postagem();
        postagem.setTitulo(titulo);
        postagem.setImage(uriImg.toString());
        postagem.setUserPhoto(user.getPhotoUrl().toString());
        postagem.setDescricao(descricao);
        postagem.setIdUsuario(idUser);


        String keyPost = UUID.randomUUID().toString();
        postagem.setId(keyPost);

        Toast.makeText(getApplicationContext(), keyPost, Toast.LENGTH_LONG).show();



        mDatabaseReference.push().setValue(postagem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                PostUser postUser = new PostUser(uriImg.toString());
                postDoUser(postUser);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                btnAdd.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void postDoUser(final PostUser postUser){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        String idUser = user.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference mDataUser = databaseReference.child("Users").child(idUser).child("userPosts");

        mDataUser.push().setValue(postUser).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    private void updatePostagem(final String titulo, final String descricao) {
        final StorageReference reference = storageReference.child(System.currentTimeMillis() + "." + getException(imgUri));
        reference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        criaPostagem(titulo, descricao, uri);
                    }
                    //fail do getDownloadUrl
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("TESTE", e.getMessage());
                    }
                });
            }
            //fail do put
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("TESTE", e.getMessage());
            }
        });
    }

    private String getException(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void abrirGaleria() {
        Intent intentAbrirGaleria = new Intent();
        intentAbrirGaleria.setType("image/*");
        intentAbrirGaleria.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentAbrirGaleria,REQUEST_CODE );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data.getData() != null){
            imgUri = data.getData();
            imgPost.setImageURI(imgUri);
        }

    }

    private void iniciaComponentes() {
        imgPost = findViewById(R.id.imageViewAdd);
        txtTitulo = findViewById(R.id.edtTituloAdd);
        txtDescricao = findViewById(R.id.edtDescricaoAdd);
        btnAdd = findViewById(R.id.btnAdd);
        progressBar = findViewById(R.id.progressBarAdd);



    }

}
