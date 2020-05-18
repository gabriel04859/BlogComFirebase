package com.e.blog_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.e.blog_firebase.Model.Comentario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetalhesPostActivity extends AppCompatActivity {
    private ImageView imgPost, btnEdt;
    private TextView txtTituloDet, txtDescricaoDet,txtDataDet;
    private CircleImageView imgPerfilDet;
    private EditText edtComentario;
    private ImageButton btnComentario;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReferenceComent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_post);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();

        iniciaComponentes();


        String titulo = getIntent().getStringExtra("titulo");
        String descricao = getIntent().getStringExtra("descricao");
        String postImage = getIntent().getStringExtra("postImagem");
        String idPost = getIntent().getStringExtra("idPost");
       // String imgUser = firebaseUser.getPhotoUrl().toString();
        configFirebase(idPost);
        txtTituloDet.setText(titulo);
        txtDescricaoDet.setText(descricao);
        Picasso.get().load(postImage).into(imgPost);

        btnComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComentario();
            }
        });


    }

    private void addComentario() {
        if (edtComentario.getText() == null){
            return;
        }
        String comentario = edtComentario.getText().toString();
        Comentario comentario1 = new Comentario();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        String idUser = firebaseUser.getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference databaseReference1 = databaseReference.child(idUser).child("nome");
        String nomeUser = databaseReference1.toString();
        comentario1.setComentario(comentario);
        comentario1.setUid(idUser);
        comentario1.setUname(nomeUser);

        databaseReferenceComent.setValue(comentario1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), "Sucesso", Toast.LENGTH_LONG).show();
                edtComentario.setText("");
            }
        });
    }

    private void iniciaComponentes(){
        imgPost = findViewById(R.id.imageViewPost);
        btnEdt = findViewById(R.id.btnEditar);
        txtTituloDet = findViewById(R.id.txtTituloDet);
        txtDescricaoDet = findViewById(R.id.txtDescricaoDet);
        txtDataDet = findViewById(R.id.txtDataDet);
        imgPerfilDet = findViewById(R.id.imageViewPerfilDet);
        edtComentario = findViewById(R.id.edtComentario);
        btnComentario = findViewById(R.id.btnComentario);

    }

    private void configFirebase(String idPost){
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReferenceComent = FirebaseDatabase.getInstance().getReference().child("Comentarios").child(idPost).push();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
