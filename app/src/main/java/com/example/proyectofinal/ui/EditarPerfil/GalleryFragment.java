package com.example.proyectofinal.ui.EditarPerfil;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectofinal.Objects.User;
import com.example.proyectofinal.R;
import com.example.proyectofinal.providers.Autentificacion;
import com.example.proyectofinal.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;

import org.w3c.dom.Text;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class GalleryFragment extends Fragment implements View.OnClickListener{

    TextView textoUsuario, textoCorreo, textoTelefono;
    TextInputLayout textoUsuarioLayout, textoCorreoLayout, textoTelefonoLayout;
    Button btnGuardarCambios;
    UsuariosProvider usersProvider;
    Autentificacion mAuth;
    String id ="";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editar_perfil, container, false);

        guardarLocale();
        mAuth = new Autentificacion();
        usersProvider = new UsuariosProvider();

        textoUsuario = root.findViewById(R.id.textUserPerfil);
        textoCorreo = root.findViewById(R.id.textCorreoPerfil);
        textoTelefono = root.findViewById(R.id.textTelefonoPerfil);

        textoUsuarioLayout = root.findViewById(R.id.usuarioLayoutVerPerfil);
        textoCorreoLayout = root.findViewById(R.id.correoVerPerfilLayout);
        textoTelefonoLayout = root.findViewById(R.id.telefonoLayoutVerPerfil);

        btnGuardarCambios = root.findViewById(R.id.btnGuardarCambiosPerfil);
        btnGuardarCambios.setOnClickListener(this);
        textoUsuario.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(textoUsuario.getText().toString())){
                    textoUsuarioLayout.setError(getString(R.string.nombreFallo));
                }else{
                    textoUsuarioLayout.setError(null);
                }
            }
        });
        textoTelefono.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(TextUtils.isEmpty(textoTelefono.getText().toString())){
                    textoTelefonoLayout.setError(getString(R.string.telefonoFallo));
                }else {
                    textoTelefonoLayout.setError(null);
                }
            }
        });
        cargarDatosPerfil();
        return root;
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

    public void cargarDatosPerfil(){
        usersProvider.getUsuario(mAuth.getIdUser()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                id=documentSnapshot.get("id").toString();
                textoUsuario.setText(documentSnapshot.get("nombre").toString());
                textoCorreo.setText(documentSnapshot.get("email").toString());
                textoTelefono.setText(documentSnapshot.get("telefono").toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Algo ha fallodo", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void guardarCambios(){
        String telefono = textoTelefono.getText().toString();
        String usuario = textoUsuario.getText().toString();
        String correo = textoCorreo.getText().toString();
        if(TextUtils.isEmpty(usuario) || TextUtils.isEmpty(telefono)){
            if(TextUtils.isEmpty(usuario)){
                textoUsuarioLayout.setError(getString(R.string.nombreFallo));
            }
            if(TextUtils.isEmpty(telefono)){
                textoTelefonoLayout.setError(getString(R.string.telefonoFallo));
            }
            return;
        }


        User miUsuario = new User(id,usuario,correo,telefono);
        usersProvider.updateUser(mAuth.getIdUser(), miUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Se actualizo correcamente tu perfil", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Fallo", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnGuardarCambiosPerfil){
            guardarCambios();
        }
    }
}