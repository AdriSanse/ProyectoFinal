package com.example.proyectofinal.ui.Ajustes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinal.Objects.User;
import com.example.proyectofinal.R;
import com.example.proyectofinal.providers.Autentificacion;
import com.example.proyectofinal.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Locale;

public class VerPerfil_Activity extends AppCompatActivity implements View.OnClickListener{

    TextView textoUsuarioAjustes, textoTelefonoAjustes;
    TextInputLayout textoUsuarioLayoutAjustes, textoTelefonoLayoutAjustes;
    Button btnGuardarCambiosAjustes;
    UsuariosProvider usersProvider;
    Autentificacion mAuth;
    String id ="";
    String correo ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);
        getSupportActionBar().setTitle(getString(R.string.ver_perfil));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        guardarLocale();

        mAuth = new Autentificacion();
        usersProvider = new UsuariosProvider();

        textoUsuarioAjustes = findViewById(R.id.textUserPerfilAjustes);
        textoTelefonoAjustes = findViewById(R.id.textTelefonoPerfilAjustes);

        textoUsuarioLayoutAjustes = findViewById(R.id.usuarioLayoutVerPerfilAjustes);
        textoTelefonoLayoutAjustes = findViewById(R.id.telefonoLayoutVerPerfilAjustes);

        btnGuardarCambiosAjustes = findViewById(R.id.btnGuardarCambiosPerfil);
        btnGuardarCambiosAjustes.setOnClickListener(this);

        textoUsuarioAjustes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(textoUsuarioAjustes.getText().toString())){
                    textoUsuarioLayoutAjustes.setError(getString(R.string.nombreFallo));
                }else{
                    textoUsuarioLayoutAjustes.setError(null);
                }
            }
        });
        textoTelefonoAjustes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(textoTelefonoAjustes.getText().toString())){
                    textoTelefonoLayoutAjustes.setError(getString(R.string.telefonoFallo));
                }else{
                    textoTelefonoLayoutAjustes.setError(null);
                }
            }
        });
        cargarDatosPerfilActivity();
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
        SharedPreferences preferences =  getSharedPreferences("Ajustes", MODE_PRIVATE);
        String idioma = preferences.getString("idioma", "");
        setLocale(idioma);
    }

    public void cargarDatosPerfilActivity(){
        usersProvider.getUsuario(mAuth.getIdUser()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                id=documentSnapshot.get("id").toString();
                textoUsuarioAjustes.setText(documentSnapshot.get("nombre").toString());
                correo = documentSnapshot.get("email").toString();
                textoTelefonoAjustes.setText(documentSnapshot.get("telefono").toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(VerPerfil_Activity.this, "Algo ha fallodo", Toast.LENGTH_SHORT).show();

            }
        });
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
        finish();
    }

    public void guardarCambios(){
        String telefono = textoTelefonoAjustes.getText().toString();
        String usuario = textoUsuarioAjustes.getText().toString();

        if(TextUtils.isEmpty(usuario) || TextUtils.isEmpty(telefono)){
            if(TextUtils.isEmpty(usuario)){
                textoUsuarioLayoutAjustes.setError(getString(R.string.nombreFallo));
            }
            if(TextUtils.isEmpty(telefono)){
                textoTelefonoLayoutAjustes.setError(getString(R.string.telefonoFallo));
            }
            return;
        }

        User miUsuario = new User(id,usuario,correo,telefono);
        usersProvider.updateUser(mAuth.getIdUser(), miUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(VerPerfil_Activity.this, "Se actualizo correcamente tu perfil", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VerPerfil_Activity.this, "Fallo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnGuardarCambiosPerfil){
            guardarCambios();
        }
    }
}