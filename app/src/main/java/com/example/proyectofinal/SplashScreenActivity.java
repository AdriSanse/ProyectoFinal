package com.example.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import com.example.proyectofinal.MainActivity;
import com.example.proyectofinal.PaginaPrincipal_Activity;
import com.example.proyectofinal.R;
import com.example.proyectofinal.providers.Autentificacion;
import com.google.api.Authentication;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        guardarLocale();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null)
            startActivity(new Intent(this, PaginaPrincipal_Activity.class));
        else
            startActivity(new Intent(this, MainActivity.class));

    }

    private void setLocale(String lang) {
        Log.e("own", "onCreate: " + lang);

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        //guardar datos de preferencia
        SharedPreferences.Editor editor = getSharedPreferences("Ajustes", MODE_PRIVATE).edit();
        editor.putString("idioma", lang);
        editor.apply();

    }

    public void guardarLocale() {
        SharedPreferences preferences = getSharedPreferences("Ajustes", Activity.MODE_PRIVATE);
        String idioma = preferences.getString("idioma", "en");
        setLocale(idioma);
    }
}