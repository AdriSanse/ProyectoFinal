package com.example.proyectofinal.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectofinal.CrearSala_Activity;
import com.example.proyectofinal.EleccionSala_Activity;
import com.example.proyectofinal.R;
import com.example.proyectofinal.SalaPersonal_Activity;
import com.example.proyectofinal.providers.Autentificacion;
import com.example.proyectofinal.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements View.OnClickListener{

    TextView nombreUsuario;
    Button btnCrear, btnActividad;
    Autentificacion mauth;
    UsuariosProvider usersProvider;
    FirebaseFirestore db;
    String idUsuario ="";
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        mauth = new Autentificacion();
        usersProvider = new UsuariosProvider();
        db = FirebaseFirestore.getInstance();

        btnCrear = root.findViewById(R.id.btnPrueba);
        btnCrear.setOnClickListener(this);
        btnActividad = root.findViewById(R.id.btnActividad);
        btnActividad.setOnClickListener(this);
        nombreUsuario = root.findViewById(R.id.nombreUsuarioPaginaPrincipal);
        cargarDatos();
        return root;
    }

    public void cargarDatos(){
        Task<DocumentSnapshot> task = usersProvider.getUsuario(mauth.getIdUser()).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                nombreUsuario.setText(documentSnapshot.get("nombre").toString());
                idUsuario = documentSnapshot.getId();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Algo ha fallodo", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnActividad){
            CollectionReference busquedaSala = db.collection("Salas");
            busquedaSala.whereArrayContains("grupo", idUsuario).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        ArrayList<String> misSalas = new ArrayList<>();
                        String id="";
                        for(DocumentSnapshot document : task.getResult()){
                            id =(String) document.get("id");
                            misSalas.add(id);
                        }
                        Intent miIntento3 = new Intent(getActivity(),EleccionSala_Activity.class);
                        miIntento3.putExtra("id",misSalas);
                        miIntento3.putExtra("idUsuario",idUsuario);
                        startActivity(miIntento3);
                        Toast.makeText(getActivity(), ""+id, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        if(view.getId()==R.id.btnPrueba){
            Intent miIntentoActividad = new Intent(getActivity(), CrearSala_Activity.class);
            startActivity(miIntentoActividad);
        }
    }
}