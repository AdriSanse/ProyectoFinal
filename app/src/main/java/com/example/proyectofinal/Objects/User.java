package com.example.proyectofinal.Objects;

public class User {
    String id;
    String nombre;
    String contrasena;
    String email;
    String telefono;

    public User(String contrasena, String email) {
        this.contrasena = contrasena;
        this.email = email;
    }

    public User(String id, String nombre, String contrasena, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.contrasena = contrasena;
        this.email = email;
        this.telefono = telefono;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
