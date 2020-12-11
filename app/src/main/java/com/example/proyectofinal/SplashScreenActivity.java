package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.proyectofinal.MainActivity;
import com.example.proyectofinal.PaginaPrincipal_Activity;
import com.example.proyectofinal.R;
import com.example.proyectofinal.providers.Autentificacion;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.Authentication;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.util.Locale;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        guardarLocale();

        tryToGetDynamicLink();

    }

    public void tryToGetDynamicLink() {
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            if (deepLink.getQueryParameter("idSala") != null) {
                                String id = deepLink.getQueryParameter("idSala");
                                Intent intent=new Intent(SplashScreenActivity.this,MainActivity.class);
                                intent.putExtra("idSalaUnirse",id);
                                System.out.println(id);
                                startActivity(intent);
                            }
                        } else {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            if (auth.getCurrentUser() != null){
                                Intent miIntento = new Intent(SplashScreenActivity.this, PaginaPrincipal_Activity.class);
                                miIntento.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(miIntento);
                            }
                            else{
                                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                            }
                        }
                        finishAffinity();///destroy all activities until this time

                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        if (auth.getCurrentUser() != null){
                            Intent miIntento = new Intent(SplashScreenActivity.this, PaginaPrincipal_Activity.class);
                            miIntento.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(miIntento);
                        }
                        else{
                            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                        }
                        finishAffinity();///destroy all activities until this time

                    }
                });
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