package com.example.proyectofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.proyectofinal.Objects.Sala;
import com.example.proyectofinal.providers.Autentificacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class EleccionSala_Activity extends AppCompatActivity implements View.OnClickListener{

    Button btnSalaPersonal, btnSucesos;
    String idUsuario;
    ArrayList<String> misIdSala;
    Spinner salasCombo;
    FirebaseFirestore db;
    Autentificacion mAuth;
    private ArrayAdapter<Sala> salaAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eleccion_sala_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        guardarLocale();

        getSupportActionBar().setTitle(getString(R.string.title_activity_eleccion_sala_));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        mAuth = new Autentificacion();

        btnSalaPersonal = findViewById(R.id.btnActualizarDinero);
        btnSalaPersonal.setOnClickListener(this);
        btnSucesos = findViewById(R.id.btnActividadSala);
        btnSucesos.setOnClickListener(this);
        salasCombo = findViewById(R.id.comboSalasUnidos);
        Intent miIntento = getIntent();
        misIdSala = (ArrayList<String>) miIntento.getSerializableExtra("id");
        idUsuario = mAuth.getIdUser();

        cargarDatos();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void cargarDatos(){
        salaAdapter = new ArrayAdapter<>(EleccionSala_Activity.this,R.layout.spinner_item_eleccion,new ArrayList<Sala>());
        salasCombo.setAdapter(salaAdapter);
        for(String a: misIdSala){
            DocumentReference miColeccionSala = db.collection("Salas").document(a);
            miColeccionSala.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        Sala miSala = new Sala(task.getResult().get("id").toString(), task.getResult().get("nombreSala").toString());
                        salaAdapter.add(miSala);
                        salaAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        Sala miIdSala = (Sala) salasCombo.getSelectedItem();
        if(R.id.btnActividadSala==view.getId()){
            if(salaAdapter.isEmpty()){
                Toast.makeText(this, "No hay ninguna sala seleccionada", Toast.LENGTH_SHORT).show();
            }else {
                Intent miIntentoSucesos = new Intent(this, Sucesos_Activity.class);
                miIntentoSucesos.putExtra("id",miIdSala.getId());
                startActivity(miIntentoSucesos);
            }

        }
        if(R.id.btnActualizarDinero==view.getId()){
            if(salaAdapter.isEmpty()){
                Toast.makeText(this, "No hay ninguna sala seleccionada", Toast.LENGTH_SHORT).show();
            }else{
                Intent miIntentoSala = new Intent(this, SalaPersonal_Activity.class);
                miIntentoSala.putExtra("id",miIdSala.getId());
                startActivity(miIntentoSala);
            }
        }
    }
}