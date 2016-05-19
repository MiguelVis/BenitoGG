package com.app.floppysoftware.benitogg.models;

/**
 * Clase que implementa un objeto del juego.
 *
 * @author   Miguel I. García López
 * @version  1.0
 * @since    08 Mar 2016
 */
public class Objeto {

    // Atributos de un objeto
    private String id;      // Id
    private String nombre;  // Nombre
    private String lugar;   // Lugar en el que se encuentra
    private String estado;  // Estado

    /**
     * Constructor.
     *
     * @param id      Id
     * @param nombre  Nombre
     * @param lugar   Lugar
     * @param estado  Estado
     */
    public Objeto(String id, String nombre, String lugar, String estado) {

        // Fijar atributos, según parámetros de entrada
        this.id = id;
        this.nombre = nombre;
        this.lugar = lugar;
        this.estado = estado;
    }

    /**
     * Devolver id del objeto.
     *
     * @return  Id
     */
    public String getId() {
        return id;
    }

    /**
     * Devolver nombre del objeto.
     *
     * @return  Nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devolver lugar en el que está el objeto.
     *
     * @return  Lugar
     */
    public String getLugar() {
        return lugar;
    }

    /**
     * Devolver estado del objeto.
     *
     * @return  Estado
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Cambiar de nombre el objeto.
     *
     * @param nombre  Nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Cambiar de lugar el objeto.
     *
     * @param lugar  Lugar
     */
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    /**
     * Cambiar el estado del objeto.
     *
     * @param estado  Estado
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
}
