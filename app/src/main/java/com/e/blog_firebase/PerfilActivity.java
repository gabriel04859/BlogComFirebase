package com.e.blog_firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.e.blog_firebase.Adapter.PostagensPerfilAdatper;
import com.e.blog_firebase.Model.PostUser;
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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilActivity extends AppCompatActivity {
    private CircleImageView circleImageViewPerfil;
    private TextView txtNomePerfil, txtEmailPerfil;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerUsuario;

    private ArrayList<PostUser> postUsers;

    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerViewPostagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        circleImageViewPerfil = findViewById(R.id.imagePerfil);
        txtNomePerfil = findViewById(R.id.textViewNomePerfil);
        txtEmailPerfil = findViewById(R.id.textViewEmailPerfil);
        recyclerViewPostagens = findViewById(R.id.recyclerPostagensPerfil);
        layoutManager = new GridLayoutManager(this,3);
        recyclerViewPostagens.setHasFixedSize(true);
        recyclerViewPostagens.setLayoutManager(layoutManager);
        postUsers = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");





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

    private void obtemPostagensUser(){
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        final String idUser = firebaseUser.getUid();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        DatabaseReference mDatabaseReference = databaseReference.child(idUser).child("userPosts");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    PostUser postUser = ds.getValue(PostUser.class);
                    postUsers.add(postUser);
                }
                PostagensPerfilAdatper postagensPerfilAdatper = new PostagensPerfilAdatper(PerfilActivity.this, postUsers);
                recyclerViewPostagens.setAdapter(postagensPerfilAdatper);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_perfil, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.icSair:
                try{
                    firebaseAuth.signOut();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));


                }catch (Exception e){
                    e.printStackTrace();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperaDadosUser();
        obtemPostagensUser();
    }
}
