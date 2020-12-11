package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinal.Objects.Sala;
import com.example.proyectofinal.Objects.User;
import com.example.proyectofinal.providers.Autentificacion;
import com.example.proyectofinal.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;

import java.util.Locale;

public class Registro_Activity extends AppCompatActivity implements View.OnClickListener{

    TextView nombre,contrasena,correo,telefono;
    TextInputLayout contrasenaLayout, nombreLayout, correoLayout, telefonoLayout;
    Button btnRegistrar;
    Autentificacion auth;
    UsuariosProvider usuarioProvider;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_);
        guardarLocale();
        getSupportActionBar().setTitle(getString(R.string.registroTitulo));

        auth = new Autentificacion();
        usuarioProvider = new UsuariosProvider();
        progressDialog = new ProgressDialog(this);

        nombre = findViewById(R.id.nombreUsuarioRegistro);
        contrasena = findViewById(R.id.contrasenaRegistro);
        contrasenaLayout = findViewById(R.id.contrasenaLayoutRegistro);
        nombreLayout = findViewById(R.id.nombreLayoutRegistro);
        correoLayout = findViewById(R.id.emailLayoutRegistro);
        telefonoLayout = findViewById(R.id.telefonoLayoutRegistro);
        correo = findViewById(R.id.emailRegistro);
        telefono = findViewById(R.id.telefonoRegistro);
        btnRegistrar = findViewById(R.id.btnRegistro);
        btnRegistrar.setOnClickListener(this);

        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(nombre.getText().toString())){
                    nombreLayout.setError(getString(R.string.nombreFallo));
                }else{
                    nombreLayout.setError(null);
                }

            }
        });
        contrasena.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(contrasena.getText().toString()) && contrasena.getText().toString().length() < 6){
                    contrasenaLayout.setError(getString(R.string.contrasenaFalloLongitud)+" "+getString(R.string.contrasenaFallo));
                }
                else if(contrasena.getText().toString().length() < 6){
                    contrasenaLayout.setError(getString(R.string.contrasenaFalloLongitud));
                }
                else  if(TextUtils.isEmpty(contrasena.getText().toString())){
                    contrasenaLayout.setError(getString(R.string.contrasenaFallo));
                }
                else{
                    contrasenaLayout.setError(null);
                }
            }
        });
        correo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(correo.getText().toString())){
                    correoLayout.setError(getString(R.string.emailFallo));
                }else{
                    correoLayout.setError(null);
                }
            }
        });
        telefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(telefono.getText().toString())){
                    telefonoLayout.setError(getString(R.string.telefonoFallo));
                }else{
                    telefonoLayout.setError(null);
                }
            }
        });
    }

    public boolean isEmailValid(TextInputLayout inputLayoutEmail) {
        String email = inputLayoutEmail.getEditText().getText().toString().trim();
        if (Patterns.EMAIL_ADDRESS.matcher(email).find()) {
            inputLayoutEmail.setError(null);
            return true;
        } else {
            inputLayoutEmail.setError(getString(R.string.emailFallo));
            return false;
        }
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
        if(view.getId()==R.id.btnRegistro){

            String scorreo = correo.getText().toString();
            String spass = contrasena.getText().toString();
            final String snombre = nombre.getText().toString();
            final String stelefono = telefono.getText().toString();
            if(!isEmailValid(correoLayout) || TextUtils.isEmpty(spass) || TextUtils.isEmpty(snombre) || TextUtils.isEmpty(stelefono)){
                Toast.makeText(this, "Debes rellenar todos los datos", Toast.LENGTH_SHORT).show();
                if(!isEmailValid(correoLayout)){
                    correoLayout.setError(getString(R.string.emailFallo));
                }
                if(TextUtils.isEmpty(spass)){
                    contrasenaLayout.setError(getString(R.string.contrasenaFallo));
                }
                if(spass.length() < 6){
                    contrasenaLayout.setError(getString(R.string.contrasenaFalloLongitud));
                }
                if(TextUtils.isEmpty(snombre)){
                    nombreLayout.setError(getString(R.string.nombreFallo));
                }
                if(TextUtils.isEmpty(stelefono)){
                    telefonoLayout.setError(getString(R.string.telefonoFallo));
                }
                return;
            }
            final User miUsuario = new User(spass,scorreo);

            auth.crearUsuario(miUsuario).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        User miUsuarioRegistrado = new User(auth.getIdUser(),snombre,scorreo,stelefono);
                        usuarioProvider.createUser(miUsuarioRegistrado).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.setMessage(getString(R.string.registroEspera));
                                progressDialog.setCanceledOnTouchOutside(false);
                                progressDialog.show();
                                if(task.isSuccessful()){
                                    progressDialog.dismiss();
                                }
                            }
                        });
                        Intent miRegistro = new Intent(Registro_Activity.this,MainActivity.class);
                        startActivity(miRegistro);
                    }else{
                        Toast.makeText(Registro_Activity.this, "No relleno todos los datos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}