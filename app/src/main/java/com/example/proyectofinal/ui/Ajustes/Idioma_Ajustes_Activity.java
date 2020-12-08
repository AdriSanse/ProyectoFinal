package com.example.proyectofinal.ui.Ajustes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.proyectofinal.MainActivity;
import com.example.proyectofinal.PaginaPrincipal_Activity;
import com.example.proyectofinal.R;

import java.util.Locale;

public class Idioma_Ajustes_Activity extends AppCompatActivity implements View.OnClickListener{

    Button btnIdioma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idioma__ajustes);
        getSupportActionBar().setTitle(getString(R.string.idiomasTitulo));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        guardarLocale();
        btnIdioma = findViewById(R.id.btnCambiarIdiomaAjustes);
        btnIdioma.setOnClickListener(this);
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

    public void verIdiomas(){
        String[] listaItems = {getString(R.string.francia), getString(R.string.ingles),getString(R.string.aleman), getString(R.string.espanol)};
        AlertDialog.Builder builder = new AlertDialog.Builder(Idioma_Ajustes_Activity.this);
        builder.setTitle("Elija idioma");
        builder.setSingleChoiceItems(listaItems, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i == 0){
                    //Frances
                    setLocale("fr");
                    recreate();
                    finish();
                    startActivity(new Intent(Idioma_Ajustes_Activity.this, PaginaPrincipal_Activity.class));
                }
                else if(i == 1){
                    //Ingles
                    setLocale("en");
                    recreate();
                    finish();
                    startActivity(new Intent(Idioma_Ajustes_Activity.this,PaginaPrincipal_Activity.class));
                }
                else if(i == 2){
                    //Aleman
                    setLocale("de");
                    recreate();
                    finish();
                    startActivity(new Intent(Idioma_Ajustes_Activity.this,PaginaPrincipal_Activity.class));
                }
                else if(i == 3){
                    //Español
                    setLocale("esp");
                    recreate();
                    finish();
                    startActivity(new Intent(Idioma_Ajustes_Activity.this,PaginaPrincipal_Activity.class));
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
        SharedPreferences preferences =  getSharedPreferences("Ajustes", MODE_PRIVATE);
        String idioma = preferences.getString("idioma", "");
        setLocale(idioma);
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnCambiarIdiomaAjustes){
            verIdiomas();

        }
    }
}