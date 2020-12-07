package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinal.providers.Autentificacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    TextView correo, contrasena, passOlvidada;
    TextInputLayout contrasenaLayout, correoLayout;
    Button btnRegistro, btnLogin, btnCambioIdioma;
    Autentificacion mauth;
    @SuppressLint("VisibleForTests")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Carga el idioma seleccionado
        System.out.println();
        guardarLocale();

        mauth = new Autentificacion();

        correo = findViewById(R.id.correoLogin);
        contrasena = findViewById(R.id.passLogin);
        passOlvidada = findViewById(R.id.passOlvidada);
        passOlvidada.setOnClickListener(this);
        contrasenaLayout = findViewById(R.id.contrasenaLayoutInicioSesion);
        correoLayout = findViewById(R.id.correoInicioSesionLayout);

        btnCambioIdioma = findViewById(R.id.btnCambioIdiomaInicioSesion);
        btnCambioIdioma.setOnClickListener(this);
        btnRegistro = findViewById(R.id.btnRegistroLogin);
        btnRegistro.setOnClickListener(this);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        correo.setText("adrian15sanse@gmail.com");
        contrasena.setText("123123");
        boolean result = getIntent().getBooleanExtra("cerrarSesion",false);
        if(result){
            System.out.println("Cerrar Sesion");
            mauth.logOut();
        }

        contrasena.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(contrasena.getText().toString())){
                    contrasenaLayout.setError(getString(R.string.contrasenaFallo));
                }else{
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
    }

    public void verIdiomas(){
        String[] listaItems = {getString(R.string.francia), getString(R.string.ingles),getString(R.string.aleman), getString(R.string.espanol)};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(getString(R.string.seleccionaIdioma));
        builder.setSingleChoiceItems(listaItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    //Frances
                    setLocale("fr");
                    recreate();
                }
                else if(i == 1){
                    //Ingles
                    setLocale("en");
                    recreate();
                }
                else if(i == 2){
                    //Aleman
                    setLocale("de");
                    recreate();
                }
                else if(i == 3){
                    //Español
                    setLocale("esp");
                    recreate();
                }

                dialogInterface.dismiss();
            }
        }).show();
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
        SharedPreferences preferences = getSharedPreferences("Ajustes", Activity.MODE_PRIVATE);
        String idioma = preferences.getString("idioma", "");
        setLocale(idioma);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.passOlvidada){
            Intent miIntento = new Intent(this, RecuperarContrasena_Activity.class);
            startActivity(miIntento);
        }
        if(view.getId()==R.id.btnRegistroLogin){
            Intent miIntento = new Intent(this,Registro_Activity.class);
            startActivity(miIntento);
        }
        if(view.getId()==R.id.btnLogin){

            String scorreo = correo.getText().toString();
            String scontrasena = contrasena.getText().toString();
            if(TextUtils.isEmpty(scorreo) || TextUtils.isEmpty(scontrasena)){
                if(TextUtils.isEmpty(scorreo)){
                    correoLayout.setError(getString(R.string.emailFallo));
                }
                if(TextUtils.isEmpty(scontrasena)){
                    contrasenaLayout.setError(getString(R.string.contrasenaFallo));
                }
                return;
            }
            mauth.iniciarSesion(scorreo,scontrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent miIntentoLogin = new Intent(MainActivity.this,PaginaPrincipal_Activity.class);
                        startActivity(miIntentoLogin);
                    }else{
                        Toast.makeText(MainActivity.this, "No pusistes bien los datos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(view.getId()==R.id.btnCambioIdiomaInicioSesion){
            verIdiomas();
        }
    }
}