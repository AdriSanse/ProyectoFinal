package com.example.proyectofinal.ui.EditarPerfil;

import android.os.Bundle;
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
import com.google.firebase.firestore.DocumentSnapshot;

public class GalleryFragment extends Fragment implements View.OnClickListener{

    TextView textoUsuario, textoCorreo, textoTelefono;
    Button btnGuardarCambios;
    UsuariosProvider usersProvider;
    Autentificacion mAuth;
    String id ="";
    String contrasena ="";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editar_perfil, container, false);

        mAuth = new Autentificacion();
        usersProvider = new UsuariosProvider();

        textoUsuario = root.findViewById(R.id.textUserPerfil);
        textoCorreo = root.findViewById(R.id.textCorreoPerfil);
        textoTelefono = root.findViewById(R.id.textTelefonoPerfil);
        btnGuardarCambios = root.findViewById(R.id.btnGuardarCambiosPerfil);
        btnGuardarCambios.setOnClickListener(this);
        cargarDatosPerfil();
        return root;
    }

    public void cargarDatosPerfil(){
        Task<DocumentSnapshot> task = usersProvider.getUsuario(mAuth.getIdUser()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                id=documentSnapshot.get("id").toString();
                contrasena=documentSnapshot.get("contrasena").toString();
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
        User miUsuario = new User(id,usuario,contrasena,correo,telefono);
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