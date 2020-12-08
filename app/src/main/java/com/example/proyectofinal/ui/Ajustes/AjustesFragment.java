package com.example.proyectofinal.ui.Ajustes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.proyectofinal.MainActivity;
import com.example.proyectofinal.RecuperarContrasena_Activity;
import com.example.proyectofinal.R;
import com.example.proyectofinal.providers.Autentificacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class AjustesFragment extends Fragment implements View.OnClickListener{

    View mView;
    CardView cerrarSesion, prueba, verPerfil, cambiarContrasena, idioma;
    Autentificacion mAuth;

    public AjustesFragment() {
        // Required empty public constructor
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_ajustes, container, false);
        mAuth = new Autentificacion();
        cerrarSesion = mView.findViewById(R.id.cerrarSesionAjustes);
        cerrarSesion.setOnClickListener(this);
        prueba = mView.findViewById(R.id.ayudaAjustes);
        prueba.setOnClickListener(this);
        verPerfil = mView.findViewById(R.id.verPerfilAjustes);
        verPerfil.setOnClickListener(this);
        cambiarContrasena = mView.findViewById(R.id.cambiarContrasenaAjustes);
        cambiarContrasena.setOnClickListener(this);
        idioma = mView.findViewById(R.id.cambiarIdiomaAjustes);
        idioma.setOnClickListener(this);
        guardarLocale();
        return mView;
    }
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        getContext().getResources().updateConfiguration(config, getContext().getResources().getDisplayMetrics());
        //guardar datos de preferencia
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Ajustes", MODE_PRIVATE).edit();
        editor.putString("idioma", lang);
        editor.apply();
    }
    public void guardarLocale (){
        SharedPreferences preferences = getActivity().getSharedPreferences("Ajustes", MODE_PRIVATE);
        String idioma = preferences.getString("idioma", "");
        setLocale(idioma);
    }

    @Override
    public void onClick(View view) {
        if(R.id.cerrarSesionAjustes==view.getId()){
            Intent miIntentoLogin = new Intent(getActivity(), MainActivity.class);
            miIntentoLogin.putExtra("cerrarSesion",true);
            miIntentoLogin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(miIntentoLogin);
        }
        if(R.id.verPerfilAjustes==view.getId()){
            Intent miIntentoPerfil = new Intent(getActivity(), VerPerfil_Activity.class);
            startActivity(miIntentoPerfil);
        }
        if(R.id.cambiarContrasenaAjustes==view.getId()){
            Intent miIntentoContrasena = new Intent(getActivity(), CambiarRecuperarContrasena_Activity.class);
            startActivity(miIntentoContrasena);
        }
        if(R.id.cambiarIdiomaAjustes==view.getId()){
            Intent miIntentoIdioma = new Intent(getActivity(), Idioma_Ajustes_Activity.class);
            startActivity(miIntentoIdioma);
        }
    }
}