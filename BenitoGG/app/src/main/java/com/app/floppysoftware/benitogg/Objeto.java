package com.app.floppysoftware.benitogg;


public class Objeto {

    private String id;
    private String nombre;
    private String lugar;
    private String estado;

    public Objeto(String id, String nombre, String lugar, String estado) {
        //
        this.id = id;
        this.nombre = nombre;
        this.lugar = lugar;
        this.estado = estado;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getLugar() {
        return lugar;
    }

    public String getEstado() {
        return estado;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
