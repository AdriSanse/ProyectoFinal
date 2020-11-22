package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyectofinal.Adaptador.AdaptadorSucesos;
import com.example.proyectofinal.Objects.Sucesos;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;

public class Sucesos_Activity extends AppCompatActivity {

    ArrayList<Sucesos> misSucesos;
    RecyclerView miLista;
    AdaptadorSucesos adaptador;
    String id;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sucesos_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();

        misSucesos = new ArrayList<>();
        miLista = findViewById(R.id.miListaRecycler);
        miLista.setLayoutManager(new LinearLayoutManager(this));

        Intent miIntento = getIntent();
        id = (String) miIntento.getSerializableExtra("id");
        cargarAdaptador();
    }
    public void cargarAdaptador(){
        Query query = db.collection("Salas").document(id).collection("Sucesos");
        FirestoreRecyclerOptions<Sucesos> firestoreSucesos = new FirestoreRecyclerOptions.Builder<Sucesos>()
                .setQuery(query, Sucesos.class).build();

        adaptador = new AdaptadorSucesos(firestoreSucesos);
        miLista.setAdapter(adaptador);
        adaptador.startListening();

    }
}