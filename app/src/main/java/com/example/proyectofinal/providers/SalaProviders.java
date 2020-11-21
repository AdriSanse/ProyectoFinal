package com.example.proyectofinal.providers;

import com.example.proyectofinal.Objects.Sala;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SalaProviders {

    private CollectionReference databaseSala;

    public SalaProviders() {
        this.databaseSala = FirebaseFirestore.getInstance().collection("Salas");
    }

    public Task<Void> createSala(Sala miSala) {
        DocumentReference inter = databaseSala.document();
        miSala.setId(inter.getId());
        return inter.set(miSala);
    }

    public Task<DocumentSnapshot> getSala(String nombreSala){
        return databaseSala.document(nombreSala).get();
    }
    public Task<DocumentSnapshot> getDinero(String dinero){
        return databaseSala.document(dinero).get();
    }

    public Task<Void> updateSala(String id, Sala sala){

        Map<String, Object> usuarioActu = new HashMap<>();
        usuarioActu.put("id",sala.getId());
        usuarioActu.put("nombreCreador",sala.getNombreCreador());
        usuarioActu.put("nombreSala",sala.getNombreSala());
        usuarioActu.put("contrasena",sala.getContrasena());
        //usuarioActu.put("grupo",sala.getGrupo());
        usuarioActu.put("dinero",sala.getDinero());
        return databaseSala.document(id).update(usuarioActu);
    }

}
