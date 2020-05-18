package com.e.blog_firebase.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.e.blog_firebase.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddFragment extends Fragment {
    private ImageView imgPost;
    private EditText txtTitulo, txtDescricao;
    private CircleImageView btnAdd;
    private ProgressBar progressBar;

    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;

    private String userId;

    private final int REQUEST_CODE = 1;
    private Uri imgUri;


    public AddFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iniciaComponentes(view);
        iniciaFirebase();

        imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });


    }

    private void abrirGaleria() {
        Intent intentAbrirGaleria = new Intent();
        intentAbrirGaleria.setType("image/*");
        intentAbrirGaleria.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentAbrirGaleria,REQUEST_CODE );
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        if (requestCode == REQUEST_CODE && intent.getData() != null){
            imgUri = intent.getData();
            imgPost.setImageURI(imgUri);
        }
    }





    private void iniciaComponentes(View view) {
        imgPost = view.findViewById(R.id.imageViewAdd);
        txtTitulo = view.findViewById(R.id.edtTituloAdd);
        txtDescricao = view.findViewById(R.id.edtDescricaoAdd);
        btnAdd = view.findViewById(R.id.btnAdd);
        progressBar = view.findViewById(R.id.progressBarAdd);

    }

    private void iniciaFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        assert firebaseUser != null;
        userId = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Post");

        storageReference = FirebaseStorage.getInstance().getReference().child("postagem_imagens/");
        storageReference.child(firebaseUser.getUid());

    }
}
