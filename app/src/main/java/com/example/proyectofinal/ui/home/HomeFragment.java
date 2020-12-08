package com.example.proyectofinal.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectofinal.BorrarSala_Activity;
import com.example.proyectofinal.CrearSala_Activity;
import com.example.proyectofinal.EleccionSala_Activity;
import com.example.proyectofinal.MainActivity;
import com.example.proyectofinal.R;
import com.example.proyectofinal.SalaPersonal_Activity;
import com.example.proyectofinal.Unirse_Sala_Activity;
import com.example.proyectofinal.providers.Autentificacion;
import com.example.proyectofinal.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment implements View.OnClickListener{

    TextView nombreUsuario;
    Button btnCrear, btnActividad, btnBorrar, btnUnirse;
    Autentificacion mAuth;
    UsuariosProvider usersProvider;
    FirebaseFirestore db;
    String idUsuario ="";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        guardarLocale();

        mAuth = new Autentificacion();
        usersProvider = new UsuariosProvider();
        db = FirebaseFirestore.getInstance();

        btnCrear = root.findViewById(R.id.btnPrueba);
        btnCrear.setOnClickListener(this);
        btnBorrar = root.findViewById(R.id.btnBorrarSalas);
        btnBorrar.setOnClickListener(this);
        btnActividad = root.findViewById(R.id.btnActividad);
        btnActividad.setOnClickListener(this);
        btnUnirse = root.findViewById(R.id.btnHomeUnirseSala);
        btnUnirse.setOnClickListener(this);
        idUsuario = mAuth.getIdUser();
        nombreUsuario = root.findViewById(R.id.nombreUsuarioPaginaPrincipal);
        sacarUsuario();
        return root;
    }

    public void sacarUsuario(){
        usersProvider.getUsuario(mAuth.getIdUser()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                nombreUsuario.setText(documentSnapshot.get("nombre")+"");
            }
        });

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

    public void guardarSalas(Class<? extends AppCompatActivity> miActivity){
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
                    Intent miIntento3 = new Intent(getActivity(),miActivity);
                    miIntento3.putExtra("id",misSalas);
                    startActivity(miIntento3);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnActividad){
            guardarSalas(EleccionSala_Activity.class);
        }
        if(view.getId()==R.id.btnPrueba){
            Intent miIntentoActividad = new Intent(getActivity(), CrearSala_Activity.class);
            startActivity(miIntentoActividad);
        }
        if(view.getId()==R.id.btnBorrarSalas){
            guardarSalas(BorrarSala_Activity.class);
        }
        if(view.getId()==R.id.btnHomeUnirseSala){
            Intent miIntentoUnirse = new Intent(getActivity(), Unirse_Sala_Activity.class);
            startActivity(miIntentoUnirse);
        }
    }
}