package com.example.proyectofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.proyectofinal.providers.Autentificacion;
import com.example.proyectofinal.providers.SalaProviders;
import com.example.proyectofinal.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
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

import java.util.Locale;

public class Unirse_Sala_Activity extends AppCompatActivity implements View.OnClickListener{

    TextView textoUnirse;
    TextInputLayout layoutUnirse;
    Button btnUnirse;
    String idUsuario="";

    SalaProviders salaProviders;
    UsuariosProvider usersProvider;
    FirebaseFirestore db;
    Autentificacion mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unirse__sala);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        guardarLocale();
        getSupportActionBar().setTitle(getString(R.string.title_activity_unirse__sala_));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        salaProviders = new SalaProviders();
        usersProvider = new UsuariosProvider();
        db = FirebaseFirestore.getInstance();
        mAuth = new Autentificacion();

        layoutUnirse = findViewById(R.id.unirseSalaPersonalLayout);
        textoUnirse = findViewById(R.id.unirseSalaPersonal);
        //textoUnirse.setText("biv1gNVi3LTK2JnttR5R");

        btnUnirse = findViewById(R.id.btnUnirseSala);
        btnUnirse.setOnClickListener(this);
        idUsuario = mAuth.getIdUser();
        textoUnirse.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(textoUnirse.getText().toString())){
                    layoutUnirse.setError(getString(R.string.rellenarCampo));
                }else {
                    layoutUnirse.setError(null);
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
    public void onClick(View view) {
        if(view.getId()==R.id.btnUnirseSala){
            if(TextUtils.isEmpty(textoUnirse.getText().toString())){
                layoutUnirse.setError(getString(R.string.rellenarCampo));
                return;
            }else{
                CollectionReference busquedaId = db.collection("Salas");
                Query query = busquedaId.whereEqualTo("id",textoUnirse.getText().toString());
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                DocumentReference washingtonRef = db.collection("Salas").document(document.getId());
                                washingtonRef.update("grupo", FieldValue.arrayUnion(idUsuario)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Unirse_Sala_Activity.this, "Se unio a la sala", Toast.LENGTH_SHORT).show();
                                        Intent miIntentoUnirse = new Intent(Unirse_Sala_Activity.this,SalaPersonal_Activity.class);
                                        miIntentoUnirse.putExtra("id",textoUnirse.getText().toString());
                                        startActivity(miIntentoUnirse);
                                    }
                                });
                            }
                        }else{
                            layoutUnirse.setError(getString(R.string.actualizar));
                            return;
                        }
                    }
                });
            }
        }
    }
}