package com.e.blog_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetalhesPostActivity extends AppCompatActivity {
    private ImageView imgPost, btnEdt;
    private EditText edtTituloDet, edtDescricaoDet;
    private CircleImageView imgPerfilDet;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_post);

        imgPost = findViewById(R.id.imageViewPost);
        btnEdt = findViewById(R.id.btnEditar);
        edtTituloDet = findViewById(R.id.editTextTituloDet);
        edtDescricaoDet = findViewById(R.id.editTextDescricaoDet);
        imgPerfilDet = findViewById(R.id.imageViewPerfilDet);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        String imgPerfil = firebaseUser.getPhotoUrl().toString();
        Picasso.get().load(imgPerfil).into(imgPerfilDet);




    }
}
