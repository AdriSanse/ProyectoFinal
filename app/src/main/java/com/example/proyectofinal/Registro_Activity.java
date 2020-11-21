package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinal.Objects.User;
import com.example.proyectofinal.providers.Autentificacion;
import com.example.proyectofinal.providers.UsuariosProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class Registro_Activity extends AppCompatActivity implements View.OnClickListener{

    TextView nombre,contrasena,correo,telefono;
    Button btnRegistrar;
    Autentificacion auth;
    UsuariosProvider usuarioProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_);

        auth = new Autentificacion();

        usuarioProvider = new UsuariosProvider();
        nombre = findViewById(R.id.nombreUsuarioRegistro);
        contrasena = findViewById(R.id.contrasenaRegistro);
        correo = findViewById(R.id.emailRegistro);
        telefono = findViewById(R.id.telefonoRegistro);
        btnRegistrar = findViewById(R.id.btnRegistro);
        btnRegistrar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnRegistro){
            String scorreo = correo.getText().toString();
            String spass = contrasena.getText().toString();
            final String snombre = nombre.getText().toString();
            final String stelefono = telefono.getText().toString();
            final User miUsuario = new User(spass,scorreo);
            auth.crearUsuario(miUsuario).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        miUsuario.setId(auth.getIdUser());
                        miUsuario.setNombre(snombre);
                        miUsuario.setTelefono(stelefono);
                        usuarioProvider.createUser(miUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(Registro_Activity.this, "Se unio al Provider", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(Registro_Activity.this, "No se unio", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        Intent miRegistro = new Intent(Registro_Activity.this,MainActivity.class);
                        startActivity(miRegistro);
                    }else{
                        Toast.makeText(Registro_Activity.this, "No se registro", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}