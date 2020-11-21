package com.example.proyectofinal;

import android.content.Intent;
import android.os.Bundle;

import com.example.proyectofinal.Objects.Sala;
import com.example.proyectofinal.providers.Autentificacion;
import com.example.proyectofinal.providers.SalaProviders;
import com.example.proyectofinal.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CrearSala_Activity extends AppCompatActivity implements View.OnClickListener{

    TextView nombreSala, dineroSala, invitacion;
    Button btnCrear, btnInvitar;
    SalaProviders salaProviders;
    UsuariosProvider usersProvider;
    Autentificacion mAuth;
    FirebaseFirestore db;
    String id = "";
    String nombre = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_sala_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        salaProviders = new SalaProviders();
        usersProvider = new UsuariosProvider();
        mAuth = new Autentificacion();
        db = FirebaseFirestore.getInstance();

        btnCrear = findViewById(R.id.btnCrearSalaPersonal);
        btnCrear.setOnClickListener(this);
        btnInvitar = findViewById(R.id.btnInvitar);
        btnInvitar.setOnClickListener(this);
        nombreSala = findViewById(R.id.textNombreSalaCrearSala);
        dineroSala = findViewById(R.id.textDineroCrearSala);
        invitacion = findViewById(R.id.correoInvitacionAmigo);
        cargarDatos();
    }
    public void cargarDatos(){
        Task<DocumentSnapshot> task = usersProvider.getUsuario(mAuth.getIdUser()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                id = documentSnapshot.get("id").toString();
                nombre = documentSnapshot.get("nombre").toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CrearSala_Activity.this, "Algo ha fallodo", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnCrearSalaPersonal){
            Sala miSala = new Sala();
                miSala.setDinero(dineroSala.getText().toString());
                miSala.setNombreCreador(nombre);
                miSala.setNombreSala(nombreSala.getText().toString());
                ArrayList<String> miGrupo = new ArrayList<>();
                miGrupo.add(id);
                miSala.setGrupo(miGrupo);
                salaProviders.createSala(miSala).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            CollectionReference idSala = db.collection("Salas");
                            Query query = idSala.whereEqualTo("nombreSala",nombreSala.getText().toString());
                            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if(task.isSuccessful()){
                                        for (QueryDocumentSnapshot document : task.getResult()){
                                            String id = document.getId();
                                            Intent miIntento = new Intent(CrearSala_Activity.this,SalaPersonal_Activity.class);
                                            miIntento.putExtra("id",id);
                                            startActivity(miIntento);
                                        }
                                    }
                                }
                            });
                        }else{
                            Toast.makeText(CrearSala_Activity.this, "No se unio", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else{
                Toast.makeText(this, "No es la misma Contrasena", Toast.LENGTH_SHORT).show();
            }
    }
}