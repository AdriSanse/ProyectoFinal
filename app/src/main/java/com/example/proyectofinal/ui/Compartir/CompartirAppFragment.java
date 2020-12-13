package com.example.proyectofinal.ui.Compartir;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyectofinal.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


public class CompartirAppFragment extends Fragment implements View.OnClickListener{

    View mView;
    Button btnCompartir;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_compartir_app, container, false);
        btnCompartir = mView.findViewById(R.id.btnCompartirApp);
        btnCompartir.setOnClickListener(this);
        guardarLocale();
        return mView;
    }

    public void generateDynamicLink() {

        Task<ShortDynamicLink> dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://proyectofinalgrado.page.link"))
                .setDomainUriPrefix("https://proyectofinalgrado.page.link")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("com.example.proyectofinal")
                                .setMinimumVersion(1)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder("com.example.proyectofinal")
                                .setAppStoreId("whatever")
                                .setMinimumVersion("1.0.1")
                                .build())
                .setSocialMetaTagParameters(
                        new DynamicLink.SocialMetaTagParameters.Builder()
                                .setTitle("PROYECTO FIN DE GRADO")
                                .setDescription(getString(R.string.descargateLaApp))
                                .setImageUrl(Uri.parse("https://www.android.com/static/images/logos/andy-lg.png"))
                                .build())
                .buildShortDynamicLink()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Intent sendIntent = new Intent();
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.descargateLaApp) + ": " + shortLink.toString());
                            sendIntent.setType("text/plain");

                            Intent shareIntent = Intent.createChooser(sendIntent, null);
                            startActivity(shareIntent);
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.error), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        getContext().getResources().updateConfiguration(config, getContext().getResources().getDisplayMetrics());
        //guardar datos de preferencia
        SharedPreferences.Editor editor = getContext().getSharedPreferences("Ajustes",MODE_PRIVATE).edit();
        editor.putString("idioma", lang);
        editor.apply();
    }
    public void guardarLocale (){
        SharedPreferences preferences = getContext().getSharedPreferences("Ajustes", MODE_PRIVATE);
        String idioma = preferences.getString("idioma", "");
        setLocale(idioma);
    }
    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnCompartirApp){
            generateDynamicLink();
        }
    }
}