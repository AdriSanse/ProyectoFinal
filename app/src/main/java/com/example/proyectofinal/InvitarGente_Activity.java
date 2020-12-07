package com.example.proyectofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import com.example.proyectofinal.providers.Autentificacion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class InvitarGente_Activity extends AppCompatActivity implements View.OnClickListener{

    TextView correo;
    Button btnInvitarGente;
    String idSala="", idUsuario="";
    Autentificacion mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitar_gente_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        guardarLocale();

        getSupportActionBar().setTitle(getString(R.string.title_activity_invitar_gente_));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = new Autentificacion();

        correo = findViewById(R.id.textCorreoInvitarGente);
        btnInvitarGente = findViewById(R.id.btnInvitarGenteInvitar);
        btnInvitarGente.setOnClickListener(this);

        Intent miIntento = getIntent();
        idSala = (String) miIntento.getSerializableExtra("id");
        idUsuario = mAuth.getIdUser();
    }
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //guardar datos de preferencia
        SharedPreferences.Editor editor = getSharedPreferences("Ajustes",MODE_PRIVATE).edit();
        editor.putString("Mi idioma", lang);
        editor.apply();
    }
    public void guardarLocale (){
        SharedPreferences preferences = getSharedPreferences("Ajustes", MODE_PRIVATE);
        String idioma = preferences.getString("Mi idioma", "");
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
        finish();
    }

    public void sendEmail() {
        String sCorreo = correo.getText().toString();
        String[] TO = {sCorreo};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.invitarGenteGmail));
        emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mensajeGmail)+" "+idSala);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(InvitarGente_Activity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnInvitarGenteInvitar){
            sendEmail();
        }
    }
}