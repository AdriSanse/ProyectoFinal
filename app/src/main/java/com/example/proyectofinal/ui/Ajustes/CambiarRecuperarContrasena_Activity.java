package com.example.proyectofinal.ui.Ajustes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

import com.example.proyectofinal.R;
import com.example.proyectofinal.providers.Autentificacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class CambiarRecuperarContrasena_Activity extends AppCompatActivity implements View.OnClickListener{

    TextView correoCambio;
    TextInputLayout cambioCorreoLayout;
    Button btnCambioContrasena;
    String correoC;
    Autentificacion mauth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_recuperar_contrasena);
        getSupportActionBar().setTitle(getString(R.string.cambioContra));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        guardarLocale();


        mauth = new Autentificacion();
        progressDialog = new ProgressDialog(this);

        correoCambio = findViewById(R.id.correoRecuperarCambiar);
        cambioCorreoLayout = findViewById(R.id.correoCambioVisible2);
        btnCambioContrasena = findViewById(R.id.btnRecuperacion2);
        btnCambioContrasena.setOnClickListener(this);
        correoCambio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(correoCambio.getText().toString())){
                    cambioCorreoLayout.setError(getString(R.string.emailFallo));
                }else {
                    cambioCorreoLayout.setError(null);
                }
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

    public void cambioPass(){
        mauth.changePassword(correoC).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressDialog.dismiss();
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnRecuperacion2){
            correoC = correoCambio.getText().toString();
            if(!correoC.isEmpty()){
                progressDialog.setMessage(getString(R.string.cambioContrasenaEspera));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                cambioPass();
            }else{
                cambioCorreoLayout.setError(getString(R.string.emailFallo));
            }
        }
    }
}