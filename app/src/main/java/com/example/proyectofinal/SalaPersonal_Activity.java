package com.example.proyectofinal;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.proyectofinal.Objects.Sucesos;
import com.example.proyectofinal.providers.Autentificacion;
import com.example.proyectofinal.providers.SalaProviders;
import com.example.proyectofinal.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

public class SalaPersonal_Activity extends AppCompatActivity implements View.OnClickListener{

    Button btnCalendario, btnGenerarSuceso, btnInvitarGente;
    TextView textoCalendario, textoDinero, textoDineroCambio, textAsunto;
    String id = "", idUsuario = "", usuario = "", fecha = "", dineroSala = "";
    RadioButton radioGasto, radioIngreso;
    RadioGroup grupoRadio;
    TextInputLayout dineroLayout, asuntoLayout, calendarioLayout;
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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        guardarLocale();

        getSupportActionBar().setTitle(getString(R.string.title_activity_sala_personal_));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        grupoRadio = findViewById(R.id.radioGrupo);

        dineroLayout = findViewById(R.id.dineroSalaPersonalLayout);
        asuntoLayout = findViewById(R.id.AsuntoLayout);
        calendarioLayout = findViewById(R.id.fechaSalaPersonalLayout);

        btnInvitarGente = findViewById(R.id.btnInvitarGente);
        btnInvitarGente.setOnClickListener(this);
        btnGenerarSuceso = findViewById(R.id.btnCrearSuceso);
        btnGenerarSuceso.setOnClickListener(this);
        btnCalendario = findViewById(R.id.btnCalendario);
        btnCalendario.setOnClickListener(this);
        mCalendar = Calendar.getInstance();

        Intent miDinero = getIntent();
        id = (String) miDinero.getSerializableExtra("id");
        idUsuario = auth.getIdUser();
        cargarDatos();
        sacarUsuario();
        textAsunto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(textAsunto.getText().toString())){
                    asuntoLayout.setError(null);
                }
            }
        });
        textoDineroCambio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(textoDineroCambio.getText().toString())){
                    dineroLayout.setError(null);
                }
            }
        });
        textoCalendario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!TextUtils.isEmpty(textoCalendario.getText().toString())){
                    calendarioLayout.setError(null);
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
        editor.putString("idioma", lang);
        editor.apply();
    }
    public void guardarLocale (){
        SharedPreferences preferences = getSharedPreferences("Ajustes", MODE_PRIVATE);
        String idioma = preferences.getString("idioma", "");
        setLocale(idioma);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void cargarDatos() {
        CollectionReference busquedaDinero = db.collection("Salas");
        Query query = busquedaDinero.whereEqualTo("id", id);
        miEscuchador = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null && !value.isEmpty()) {
                    dineroSala = value.getDocuments().get(0).getString("dinero");
                    textoDinero.setText(dineroSala);
                }
            }
        });
    }

    public void sacarUsuario(){
        DocumentReference dcoNombre = db.collection("User").document(idUsuario);
        dcoNombre.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                  usuario = (String) task.getResult().get("nombre");
                }
            }
        });
    }

    public static String roundToHalf(double d) {
        //double result = Math.round(d * 2) / 2.0;
        DecimalFormat formato = new DecimalFormat("####0.00");
        return formato.format(d);
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

            if(TextUtils.isEmpty(sAsunto) || TextUtils.isEmpty(textoCalendario.getText().toString()) || TextUtils.isEmpty(textoDineroCambio.getText().toString())){
                Toast.makeText(this, getString(R.string.datosObligatorios) , Toast.LENGTH_SHORT).show();
                if(TextUtils.isEmpty(sAsunto)){
                    asuntoLayout.setError(getString(R.string.asuntoFallo));
                }
                if(TextUtils.isEmpty(textoDineroCambio.getText().toString())){
                    dineroLayout.setError(getString(R.string.dineroFallo));
                }
                if(TextUtils.isEmpty(fecha)){
                    calendarioLayout.setError(getString(R.string.fechaFallo));
                }
                return;
            }

            final double dineroDouble = Double.parseDouble(textoDineroCambio.getText().toString());

            if(radioGasto.isChecked()){
                CollectionReference busquedaId = db.collection("Salas");
                Query query = busquedaId.whereEqualTo("id",id);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot document : task.getResult()) {
                                double resultadoGasto = Double.parseDouble(dineroSala) - dineroDouble;

                                Sucesos miSuceso = new Sucesos();
                                miSuceso.setDinero(""+dineroDouble);
                                miSuceso.setAsunto(sAsunto);
                                miSuceso.setFecha(fecha);
                                miSuceso.setUsuario(usuario);
                                miSuceso.setGastoIngreso(getString(R.string.gastos));
                                DocumentReference actuDinero = db.collection("Salas").document(document.getId()).collection("Sucesos").document();
                                actuDinero.set(miSuceso).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            DocumentReference actuDineroSala = db.collection("Salas").document(id);
                                            actuDineroSala.update("dinero",resultadoGasto+"").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(SalaPersonal_Activity.this, getString(R.string.actualizacionDinero), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else{
                                            Toast.makeText(SalaPersonal_Activity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
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
                                double resultadoIngreso = Double.parseDouble(dineroSala) + dineroDouble;
                                Toast.makeText(SalaPersonal_Activity.this, ""+resultadoIngreso, Toast.LENGTH_SHORT).show();

                                Sucesos miSuceso = new Sucesos();
                                miSuceso.setDinero(""+dineroDouble);
                                miSuceso.setAsunto(sAsunto);
                                miSuceso.setFecha(fecha);
                                miSuceso.setUsuario(usuario);
                                miSuceso.setGastoIngreso(getString(R.string.ingresos));

                                DocumentReference actuDinero = db.collection("Salas").document(document.getId()).collection("Sucesos").document();
                                actuDinero.set(miSuceso).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            DocumentReference actuDineroSala = db.collection("Salas").document(id);
                                            actuDineroSala.update("dinero",resultadoIngreso+"").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(SalaPersonal_Activity.this, getString(R.string.actualizacionDinero), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }else{
                                            Toast.makeText(SalaPersonal_Activity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
            }
            if(!radioIngreso.isChecked() && !radioGasto.isChecked()){
                Toast.makeText(this, getString(R.string.marcarOpcion), Toast.LENGTH_SHORT).show();
            }else {
                grupoRadio.clearCheck();
                textAsunto.setText("");
                textoCalendario.setText("");
                textoDineroCambio.setText("");
            }
        }
        if(view.getId()==R.id.btnInvitarGente){
            Intent miIntentoInvitar = new Intent(this, InvitarGente_Activity.class);
            miIntentoInvitar.putExtra("id",id);
            startActivity(miIntentoInvitar);
        }
    }
}
