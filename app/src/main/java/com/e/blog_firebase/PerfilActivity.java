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
    private ValueEventListener valueEventListenerUsuario;

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


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    firebaseAuth.signOut();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));


                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



    }

    private void recuperaDadosUser(){
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        final String idUser = firebaseUser.getUid();
        databaseReference.child(idUser);
         valueEventListenerUsuario =   databaseReference.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 for (DataSnapshot ds: dataSnapshot.getChildren()){
                     Usuario usuario = ds.getValue(Usuario.class);
                     String nome = String.valueOf( usuario.getNome() );
                     String email = String.valueOf(usuario.getEmail());
                     String imgUrl = String.valueOf(usuario.getImgUri());

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaDadosUser();
    }
}
