package com.example.proyectofinal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.proyectofinal.Objects.Sucesos;
import com.example.proyectofinal.providers.Autentificacion;
import com.example.proyectofinal.providers.SalaProviders;
import com.example.proyectofinal.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;

public class SalaPersonal_Activity extends AppCompatActivity implements View.OnClickListener{

    Button btnCalendario, btnGenerarSuceso;
    TextView textoCalendario, textoDinero, textoDineroCambio, textAsunto;
    String id = "", idUsuario = "", usuario = "", fecha = "";
    RadioButton radioGasto, radioIngreso;
    Calendar mCalendar;
    Autentificacion auth;
    SalaProviders salaProviders;
    UsuariosProvider userProviders;
    FirebaseFirestore db;
    private ListenerRegistration miEscuchador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sala_personal_);

        auth = new Autentificacion();
        salaProviders = new SalaProviders();
        userProviders = new UsuariosProvider();
        db = FirebaseFirestore.getInstance();

        textoCalendario = findViewById(R.id.textFechaSuceso);
        textoDinero = findViewById(R.id.dineroTotal);
        textoDineroCambio = findViewById(R.id.textDineroSalaPersonal);
        textAsunto = findViewById(R.id.textAsunto);
        radioIngreso = findViewById(R.id.radioIngreso);
        radioGasto = findViewById(R.id.radioGastos);
        btnGenerarSuceso = findViewById(R.id.btnCrearSuceso);
        btnGenerarSuceso.setOnClickListener(this);
        btnCalendario = findViewById(R.id.btnCalendario);
        btnCalendario.setOnClickListener(this);
        mCalendar = Calendar.getInstance();

        Intent miDinero = getIntent();
        id = (String) miDinero.getSerializableExtra("id");
        idUsuario = (String) miDinero.getSerializableExtra("idUsuario");
        cargarDatos();
        sacarUsuario();
    }
    public void cargarDatos() {
        CollectionReference busquedaDinero = db.collection("Salas");
        Query query = busquedaDinero.whereEqualTo("id", id);
        miEscuchador = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && !value.isEmpty()) {
                    textoDinero.setText("" + value.getDocuments().get(0).get("dinero"));
                }
            }
        });
        Toast.makeText(this, "Eres " + idUsuario + " con " + textoDinero.getText().toString(), Toast.LENGTH_SHORT).show();
    }

    public void sacarUsuario(){
        Task<DocumentSnapshot> task = userProviders.getUsuario(auth.getIdUser()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                //Toast.makeText(SalaPersonal_Activity.this, ""+documentSnapshot.get("nombre").toString(), Toast.LENGTH_SHORT).show();
                usuario = documentSnapshot.get("nombre").toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SalaPersonal_Activity.this, "Algo ha fallodo", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    protected void onStop() {
        if(miEscuchador!=null){
            miEscuchador.remove();
        }
        super.onStop();
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnCalendario){
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mCalendar.set(Calendar.YEAR, year);
                    mCalendar.set(Calendar.MONTH, monthOfYear);
                    mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    fecha = dayOfMonth+"/"+monthOfYear+"/"+year;
                    textoCalendario.setText(fecha);
                }
            }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }

        if(view.getId() == R.id.btnCrearSuceso){
            final String sAsunto = textAsunto.getText().toString();
            final int dineroInt = Integer.parseInt(textoDineroCambio.getText().toString());
            if(radioGasto.isChecked()){
                CollectionReference busquedaId = db.collection("Salas");
                Query query = busquedaId.whereEqualTo("id",id);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document : task.getResult()) {
                                int dinero = Integer.parseInt("" + document.get("dinero"));
                                int resultado = dinero - dineroInt;
                                final String sResultado = resultado + "";

                                Sucesos miSuceso = new Sucesos();
                                miSuceso.setDinero(""+dineroInt);
                                miSuceso.setAsunto(sAsunto);
                                miSuceso.setFecha(fecha);
                                miSuceso.setUsuario(usuario);
                                miSuceso.setGastoIngreso("Gasto");
                                DocumentReference actuDinero = db.collection("Salas").document(document.getId()).collection("Sucesos").document();
                                actuDinero.set(miSuceso).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(SalaPersonal_Activity.this, "Funciona Crear Sucesos Dentro de la Sala", Toast.LENGTH_SHORT).show();
                                            DocumentReference actuDineroSala = db.collection("Salas").document(id);
                                            actuDineroSala.update("dinero",sResultado).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(SalaPersonal_Activity.this, "Se actualizo el dinero de la sala", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else{
                                            Toast.makeText(SalaPersonal_Activity.this, "No funciono la creacion", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }

            if(radioIngreso.isChecked()){
                CollectionReference busquedaId = db.collection("Salas");
                Query query = busquedaId.whereEqualTo("id",id);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){
                            for(DocumentSnapshot document : task.getResult()) {
                                int dinero = Integer.parseInt("" + document.get("dinero"));
                                int resultado = dinero + dineroInt;
                                final String sResultado = resultado + "";

                                Sucesos miSuceso = new Sucesos();
                                miSuceso.setDinero(""+dineroInt);
                                miSuceso.setAsunto(sAsunto);
                                miSuceso.setFecha(fecha);
                                miSuceso.setUsuario(usuario);
                                miSuceso.setGastoIngreso("Ingreso");
                                DocumentReference actuDinero = db.collection("Salas").document(document.getId()).collection("Sucesos").document();
                                actuDinero.set(miSuceso).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(SalaPersonal_Activity.this, "Funciona Crear Sucesos Dentro de la Sala", Toast.LENGTH_SHORT).show();
                                            DocumentReference actuDineroSala = db.collection("Salas").document(id);
                                            actuDineroSala.update("dinero",sResultado).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(SalaPersonal_Activity.this, "Se actualizo el dinero de la sala", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else{
                                            Toast.makeText(SalaPersonal_Activity.this, "No funciono la creacion", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
            textAsunto.setText("");
            textoCalendario.setText("");
            textoDineroCambio.setText("");

        }
    }
}
