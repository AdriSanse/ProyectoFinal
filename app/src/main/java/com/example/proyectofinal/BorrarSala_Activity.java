package com.example.proyectofinal;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.proyectofinal.Objects.Sala;
import com.example.proyectofinal.providers.Autentificacion;
import com.example.proyectofinal.providers.SalaProviders;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class BorrarSala_Activity extends AppCompatActivity implements View.OnClickListener{

    Button btnVerDatos;
    String idUsuario;
    ArrayList<String> misIdSala;
    Spinner salasCombo;
    FirebaseFirestore db;
    SalaProviders salaProviders;
    Autentificacion mAuth;
    private ArrayAdapter<Sala> salaAdapter;
    Sala miSala;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar_sala_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        guardarLocale();

        getSupportActionBar().setTitle(getString(R.string.title_activity_borrar_sala_));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseFirestore.getInstance();
        mAuth = new Autentificacion();
        salaProviders = new SalaProviders();

        btnVerDatos = findViewById(R.id.btnVerDatos);
        btnVerDatos.setOnClickListener(this);
        salasCombo = findViewById(R.id.comboSalasUnidos2);
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

    public void cargarDatos(){
        salaAdapter = new ArrayAdapter<>(BorrarSala_Activity.this,R.layout.spinner_item_eleccion,new ArrayList<Sala>());
        salasCombo.setAdapter(salaAdapter);
        for(String a: misIdSala){
            DocumentReference miColeccionSala = db.collection("Salas").document(a);
            miColeccionSala.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        miSala = new Sala(task.getResult().get("id").toString(), task.getResult().get("nombreSala").toString());
                        salaAdapter.add(miSala);
                        salaAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnVerDatos){
            Sala miIdSala = (Sala) salasCombo.getSelectedItem();
            if(salaAdapter.isEmpty()){
                Toast.makeText(this, getString(R.string.noSeleccionastes), Toast.LENGTH_SHORT).show();
            }else {
                db.collection("Salas").document(miIdSala.getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            Sala miSalaRemove = task.getResult().toObject(Sala.class);
                            String creador1 = task.getResult().get("nombreCreador").toString();
                            if(creador1.equals(idUsuario)) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(BorrarSala_Activity.this);
                                builder.setMessage(getString(R.string.eresCreadorBorrarSala)).setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        db.collection("Salas").document(miIdSala.getId()).collection("Sucesos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                for(DocumentSnapshot a : task.getResult().getDocuments()){
                                                    a.getReference().delete();
                                                }
                                                db.collection("Salas").document(miIdSala.getId()).delete();
                                                Toast.makeText(BorrarSala_Activity.this, R.string.borroConExito, Toast.LENGTH_SHORT).show();
                                                salaAdapter.remove(miSalaRemove);
                                                salaAdapter.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                }).setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                            }
                            if(!creador1.equals(idUsuario)){
                                AlertDialog.Builder builder2 = new AlertDialog.Builder(BorrarSala_Activity.this);
                                builder2.setMessage(getString(R.string.eresInvitado)).setPositiveButton(getString(R.string.aceptar), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        salaProviders.getSala(miIdSala.getId()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isSuccessful()){
                                                    ArrayList<String> miGrupo = (ArrayList<String>) task.getResult().get("grupo");
                                                    for(String a: miGrupo){
                                                        if(a.equals(idUsuario)){
                                                            db.collection("Salas").document(miIdSala.getId()).update("grupo",FieldValue.arrayRemove(a)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if(task.isSuccessful()){
                                                                        Toast.makeText(BorrarSala_Activity.this, R.string.borroConExito, Toast.LENGTH_SHORT).show();
                                                                        salaAdapter.remove(miSalaRemove);
                                                                        salaAdapter.notifyDataSetChanged();
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    }
                                }).setNegativeButton(getString(R.string.cancelar), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                            }
                        }
                    }
                });
            }
        }
    }
}