package com.example.proyectofinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proyectofinal.providers.Autentificacion;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    TextView correo,contrasena;
    Button btnRegistro, btnLogin;
    Autentificacion mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mauth = new Autentificacion();

        correo = findViewById(R.id.correoLogin);
        contrasena = findViewById(R.id.passLogin);

        btnRegistro = findViewById(R.id.btnRegistroLogin);
        btnRegistro.setOnClickListener(this);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.btnRegistroLogin){
            Intent miIntento = new Intent(this,Registro_Activity.class);
            startActivity(miIntento);
        }
        if(view.getId()==R.id.btnLogin){

            String scorreo = correo.getText().toString();
            String scontrasena = contrasena.getText().toString();
            mauth.iniciarSesion(scorreo,scontrasena).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent miIntentoLogin = new Intent(MainActivity.this,PaginaPrincipal_Activity.class);
                        startActivity(miIntentoLogin);
                    }else{
                        Toast.makeText(MainActivity.this, "No pusistes bien los datos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}