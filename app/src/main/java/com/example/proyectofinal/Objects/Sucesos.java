package com.example.proyectofinal.Objects;

public class Sucesos {
    String fecha, dinero, usuario, asunto, gastoIngreso;

    public Sucesos() {
    }

    public Sucesos(String fecha, String dinero, String usuario, String asunto, String gastoIngreso) {
        this.fecha = fecha;
        this.dinero = dinero;
        this.usuario = usuario;
        this.asunto = asunto;
        this.gastoIngreso = gastoIngreso;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDinero() {
        return dinero;
    }

    public void setDinero(String dinero) {
        this.dinero = dinero;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getGastoIngreso() {
        return gastoIngreso;
    }

    public void setGastoIngreso(String gastoIngreso) {
        this.gastoIngreso = gastoIngreso;
    }
}
