package com.e.blog_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.e.blog_firebase.Fragment.HomeFragment;
import com.e.blog_firebase.Fragment.PerfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Home");
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomMain);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frameMain, new HomeFragment()).commit();
        }



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragmentSelecionada = null;
            switch (menuItem.getItemId()){
                case R.id.ic_home:
                    fragmentSelecionada = new HomeFragment();
                    getSupportActionBar().setTitle("Home");
                    break;

                case R.id.ic_perfil:
                    fragmentSelecionada = new PerfilFragment();
                    getSupportActionBar().setTitle("Perfil");
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frameMain, fragmentSelecionada).commit();

            return true;
        }
    };
}
