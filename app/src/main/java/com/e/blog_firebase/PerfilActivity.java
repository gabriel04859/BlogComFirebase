package com.e.blog_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.e.blog_firebase.Model.Postagem;
import com.e.blog_firebase.Model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {
    private CircleImageView circleImageViewPerfil;
    private TextView txtNomePerfil, txtEmailPerfil;
    private Button btnEditar, btnSair;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        circleImageViewPerfil = findViewById(R.id.imagePerfil);
        txtNomePerfil = findViewById(R.id.textViewNomePerfil);
        txtEmailPerfil = findViewById(R.id.textViewEmailPerfil);
        btnEditar = findViewById(R.id.btnEditarPerfil);
        btnSair = findViewById(R.id.btnSingOUt);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");


        assert firebaseUser != null;
        final String idUser = firebaseUser.getUid();

        databaseReference.child(idUser);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Usuario usuario = dataSnapshot1.getValue(Usuario.class);
                    assert usuario != null;
                    String nome = usuario.getNome();
                    String email = usuario.getEmail();
                    String imgUrl = usuario.getImgUri();
                    txtNomePerfil.setText(nome);
                    txtEmailPerfil.setText(email);
                    Picasso.get().load(imgUrl).into(circleImageViewPerfil);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("TESTE", databaseError.getMessage());

            }
        });



        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });



    }

}
