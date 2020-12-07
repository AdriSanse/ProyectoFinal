package com.example.proyectofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.proyectofinal.Objects.Sala;
import com.example.proyectofinal.providers.Autentificacion;
import com.example.proyectofinal.providers.SalaProviders;
import com.example.proyectofinal.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class CrearSala_Activity extends AppCompatActivity implements View.OnClickListener{

    TextView nombreSala, dineroSala;
    TextInputLayout dineroLayout, nombreSalaLayout;
    Button btnCrear;
    SalaProviders salaProviders;
    UsuariosProvider usersProvider;
    Autentificacion mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_sala_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        guardarLocale();
        getSupportActionBar().setTitle(getString(R.string.title_activity_crear_sala_));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        salaProviders = new SalaProviders();
        usersProvider = new UsuariosProvider();
        mAuth = new Autentificacion();
        db = FirebaseFirestore.getInstance();

        btnCrear = findViewById(R.id.btnCrearSalaPersonal);
        btnCrear.setOnClickListener(this);

        nombreSala = findViewById(R.id.textNombreCrearSala);
        dineroSala = findViewById(R.id.textDineroCrearSala);

        nombreSalaLayout = findViewById(R.id.nombreCrearSalaLayout);
        dineroLayout = findViewById(R.id.dineroCrearSalaLayout);

        nombreSala.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(nombreSala.getText().toString())){
                    nombreSalaLayout.setError(getString(R.string.nombreFallo));
                }else{
                    nombreSalaLayout.setError(null);
                }
            }
        });
        dineroSala.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(dineroSala.getText().toString())){
                    dineroLayout.setError(getString(R.string.dineroFallo));
                }else {
                    dineroLayout.setError(null);
                }
            }
        });

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

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnCrearSalaPersonal) {

            if(TextUtils.isEmpty(nombreSala.getText().toString()) || TextUtils.isEmpty(dineroSala.getText().toString())){
                Toast.makeText(this, "No pusistes los datos obligatorios", Toast.LENGTH_SHORT).show();
                if(TextUtils.isEmpty(nombreSala.getText().toString())){
                    nombreSalaLayout.setError(getString(R.string.nombreFallo));
                }
                if(TextUtils.isEmpty(dineroSala.getText().toString())){
                    dineroLayout.setError(getString(R.string.dineroFallo));
                }
                return;
            }
            

            Sala miSala = new Sala();
            miSala.setDinero(dineroSala.getText().toString());
            miSala.setNombreCreador(mAuth.getIdUser());
            miSala.setNombreSala(nombreSala.getText().toString());
            ArrayList<String> miGrupo = new ArrayList<>();
            miGrupo.add(mAuth.getIdUser());
            miSala.setGrupo(miGrupo);

            salaProviders.createSala(miSala).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        CollectionReference idSala = db.collection("Salas");
                        Query query = idSala.whereEqualTo("nombreSala", nombreSala.getText().toString());
                        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        String id = document.getId();
                                        Intent miIntento = new Intent(CrearSala_Activity.this, SalaPersonal_Activity.class);
                                        miIntento.putExtra("id", id);
                                        startActivity(miIntento);
                                    }
                                }
                            }
                        });
                    } else {
                        Toast.makeText(CrearSala_Activity.this, "No se unio", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}