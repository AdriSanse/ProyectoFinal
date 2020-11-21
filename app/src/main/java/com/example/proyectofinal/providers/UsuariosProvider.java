package com.example.proyectofinal.providers;


import com.example.proyectofinal.Objects.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class UsuariosProvider {

    private CollectionReference databaseUser;

    public UsuariosProvider() {
        databaseUser = FirebaseFirestore.getInstance().collection("User");
    }

    public Task<Void> createUser(User miUser) {
        DocumentReference inter = databaseUser.document(miUser.getId());
        return inter.set(miUser);
    }

    public Task<DocumentSnapshot> getUsuario(String id){
        return databaseUser.document(id).get();
    }

    public Task<Void> updateUser(String id, User user){

        Map<String, Object> usuarioActu = new HashMap<>();
        usuarioActu.put("id",user.getId());
        usuarioActu.put("nombre",user.getNombre());
        usuarioActu.put("contrasena",user.getContrasena());
        usuarioActu.put("correo",user.getEmail());
        usuarioActu.put("telefono",user.getTelefono());
        return databaseUser.document(id).update(usuarioActu);
    }
}
