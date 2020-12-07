package com.example.proyectofinal;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinal.providers.Autentificacion;
import com.example.proyectofinal.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class PaginaPrincipal_Activity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    Autentificacion mauth;
    UsuariosProvider usersProvider;
    FirebaseFirestore db;
    TextView nombreUsuario, correoUsuario;
    DrawerLayout drawer;
    private ListenerRegistration listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagina_principal);
        guardarLocale();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_perfil, R.id.ajustesFragment, R.id.ayudaFragment, R.id.idiomasFragment, R.id.sobreNosotros_Fragment)
                .setDrawerLayout(drawer)
                .build();
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                drawerView.findViewById(R.id.compartir).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(PaginaPrincipal_Activity.this, "HOOOLA", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        mauth = new Autentificacion();
        usersProvider = new UsuariosProvider();
        db = FirebaseFirestore.getInstance();

        //cargarDatos();
    }
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //guardar datos de preferencia
        SharedPreferences.Editor editor = getSharedPreferences("Ajustes",MODE_PRIVATE).edit();
        editor.putString("Mi idioma", lang);
        editor.apply();
    }
    public void guardarLocale (){
        SharedPreferences preferences = getSharedPreferences("Ajustes", MODE_PRIVATE);
        String idioma = preferences.getString("Mi idioma", "");
        setLocale(idioma);
    }

//    @Override
//    protected void onStart() {
//        if(listener==null){
//            cargarDatos();
//        }
//        super.onStart();
//    }
//
//    @Override
//    protected void onStop() {
//        listener.remove();
//        super.onStop();
//    }
//
//    public void cargarDatos(){
//        listener =  usersProvider.getUsuario(mauth.getIdUser()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                nombreUsuario = drawer.findViewById(R.id.nombreUsuarioDrawer);
//                correoUsuario = drawer.findViewById(R.id.correoUsuarioDrawer);
//                nombreUsuario.setText(value.get("nombre").toString());
//                correoUsuario.setText(value.get("email").toString());
//            }
//        });
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pagina_principal_, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}