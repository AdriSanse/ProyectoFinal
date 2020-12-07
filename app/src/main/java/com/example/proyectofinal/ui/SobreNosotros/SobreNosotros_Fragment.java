package com.example.proyectofinal.ui.SobreNosotros;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectofinal.R;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class SobreNosotros_Fragment extends Fragment {

    View mView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_sobre_nosotros, container, false);
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
        editor.putString("Mi idioma", lang);
        editor.apply();
    }
    public void guardarLocale (){
        SharedPreferences preferences = getActivity().getSharedPreferences("Ajustes", MODE_PRIVATE);
        String idioma = preferences.getString("Mi idioma", "");
        setLocale(idioma);
    }
}