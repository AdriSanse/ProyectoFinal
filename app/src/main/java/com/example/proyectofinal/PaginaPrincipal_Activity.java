package com.example.proyectofinal;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinal.providers.Autentificacion;
import com.example.proyectofinal.providers.UsuariosProvider;
import com.example.proyectofinal.ui.Ajustes.AjustesFragment;
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
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
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
    View header;
    TextView nombreUsuario, correoUsuario;
    DrawerLayout drawer;
    private ListenerRegistration listener;

    @SuppressLint("ResourceType")
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
                R.id.nav_home, R.id.nav_perfil, R.id.ajustesFragment, R.id.ayudaFragment, R.id.idiomasFragment, R.id.sobreNosotros_Fragment, R.id.compartirAppFragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                if(item.getItemId()==R.id.ajustesFragment){
//                    Log.e("own", "onDestinationChanged: entro " );
//                    Bundle bundle=new Bundle();
//                    bundle.putString("key","value");
//
//                    return true;
//                }
//
//                return false;
//            }
//        });


        header = navigationView.getHeaderView(0);
        nombreUsuario = header.findViewById(R.id.nombreUsuarioDrawer);
        correoUsuario = header.findViewById(R.id.correoUsuarioDrawer);

        mauth = new Autentificacion();
        usersProvider = new UsuariosProvider();
        db = FirebaseFirestore.getInstance();
    }

    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //guardar datos de preferencia
        SharedPreferences.Editor editor = getSharedPreferences("Ajustes",MODE_PRIVATE).edit();
        editor.putString("idioma", lang);
        editor.apply();
    }
    public void guardarLocale (){
        SharedPreferences preferences = getSharedPreferences("Ajustes", MODE_PRIVATE);
        String idioma = preferences.getString("idioma", "");
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
//        System.out.println("OnStop");
//        super.onStop();
//    }

//    public void cargarDatos(){
//        usersProvider.getUsuario(mauth.getIdUser()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
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