package com.e.blog_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.e.blog_firebase.Adapter.ComentarioAdapter;
import com.e.blog_firebase.Model.Comentario;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetalhesPostActivity extends AppCompatActivity {
    private ImageView imgPost, btnEdt;
    private TextView txtTituloDet, txtDescricaoDet,txtDataDet;
    private CircleImageView imgPerfilDet;
    private EditText edtComentario;
    private ImageButton btnComentario;
    private RecyclerView recyclerViewComentario;
    private ArrayList<Comentario> comentarioArrayList;
    private ComentarioAdapter comentarioAdapter;

    private FirebaseAuth firebaseAuth;

    static String COMENT_KEY ="Comentarios";
    private String keyPost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_post);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getSupportActionBar().hide();

        iniciaComponentes();
        keyPost = getIntent().getStringExtra("keyPost");

        obtemDadosdaIntent();
        firebaseAuth = FirebaseAuth.getInstance();
        recyclerViewComentario.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewComentario.setHasFixedSize(true);

        btnComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addComentario();
            }
        });
    }


    private void obtemDadosdaIntent() {
        String titulo = getIntent().getStringExtra("titulo");
        String descricao = getIntent().getStringExtra("descricao");
        String postImage = getIntent().getStringExtra("postImagem");
        String userImg = getIntent().getStringExtra("userPhoto");
        txtTituloDet.setText(titulo);
        txtDescricaoDet.setText(descricao);
        Picasso.get().load(postImage).into(imgPost);
        Picasso.get().load(userImg).into(imgPerfilDet);


    }

    private void addComentario() {
        final String comentarioString = edtComentario.getText().toString();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String idUser = firebaseUser.getUid();
        String userImage = firebaseUser.getPhotoUrl().toString();
        String userName = firebaseUser.getDisplayName();
        Comentario comentario = new Comentario(comentarioString, idUser, userImage, userName);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cometarioDatabase = databaseReference.child(COMENT_KEY).child(keyPost);
        cometarioDatabase.push().setValue(comentario).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
             edtComentario.setText("");
            }
        });

    }
    private void exibeComentarios( ){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference cometarioDatabase = databaseReference.child(COMENT_KEY).child(keyPost);
        cometarioDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comentarioArrayList = new ArrayList<>();
                for(DataSnapshot comentarioDs : dataSnapshot.getChildren()){
                    Comentario comentario = comentarioDs.getValue(Comentario.class);
                    comentarioArrayList.add(comentario);
                }
                ComentarioAdapter comentarioAdapter = new ComentarioAdapter(getApplicationContext(),comentarioArrayList);
                recyclerViewComentario.setAdapter(comentarioAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        recyclerViewComentario = findViewById(R.id.recyclerViewComentarios);
        comentarioArrayList = new ArrayList<>();

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
        exibeComentarios();

    }
}
