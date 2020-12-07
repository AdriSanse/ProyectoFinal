package com.example.proyectofinal.providers;


import com.example.proyectofinal.Objects.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Autentificacion {

    private FirebaseAuth auth;

    public Autentificacion() {
        auth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> crearUsuario(User miUsuario){
        return auth.createUserWithEmailAndPassword(miUsuario.getEmail(),miUsuario.getContrasena());
    }
    public String getIdUser() {
        return auth.getCurrentUser().getUid();
    }

    public Task<AuthResult> iniciarSesion(String correo, String contrasena){
        return auth.signInWithEmailAndPassword(correo,contrasena);
    }
    public void logOut(){
        auth.signOut();
    }

    public Task<Void> changePassword(String email){
        return auth.sendPasswordResetEmail(email);
    }

}
