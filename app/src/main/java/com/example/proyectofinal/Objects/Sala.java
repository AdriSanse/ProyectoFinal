package com.example.proyectofinal.Objects;

import java.util.List;

public class Sala {
    String id, nombreCreador, nombreSala, contrasena;
    String dinero;
    List<String> grupo;

    public Sala(String id, String nombreSala) {
        this.id = id;
        this.nombreSala = nombreSala;
    }
    public Sala() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreCreador() {
        return nombreCreador;
    }

    public void setNombreCreador(String nombreCreador) {
        this.nombreCreador = nombreCreador;
    }

    public String getNombreSala() {
        return nombreSala;
    }

    public void setNombreSala(String nombreSala) {
        this.nombreSala = nombreSala;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getDinero() {
        return dinero;
    }

    public void setDinero(String dinero) {
        this.dinero = dinero;
    }

    public List<String> getGrupo() {
        return grupo;
    }

    public void setGrupo(List<String> grupo) {
        this.grupo = grupo;
    }

    @Override
    public String toString() {
        return nombreSala;
    }
}
