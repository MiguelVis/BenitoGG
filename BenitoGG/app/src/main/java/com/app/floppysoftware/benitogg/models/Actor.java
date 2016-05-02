package com.app.floppysoftware.benitogg.models;

/**
 * Clase que implementa un actor.
 *
 * @author   Miguel I. García López
 * @version  1.0
 * @since    07 Mar 2016
 */
public class Actor {

    // IDs de actores comunes a todas las aventuras
    public static final String PROTAGONISTA = "prota";  // Protagonista

    // Atributos del actor
    private String id;      // Id
    private String lugar;   // Lugar donde está

    /**
     * Constructor.
     *
     * @param id        Id
     * @param lugar     Lugar donde está
     */
    public Actor(String id, String lugar) {

        // Asignar valores según parámetros de entrada
        this.id = id;
        this.lugar = lugar;
    }

    /**
     * Devolver id. del actor.
     *
     * @return  Id
     */
    public String getId() {
        return id;
    }

    /**
     * Devolver lugar donde está el actor.
     *
     * @return  Lugar
     */
    public String getLugar() {
        return lugar;
    }

    /**
     * Cambiar de lugar al actor.
     *
     * @param lugar  Lugar
     */
    public void setLugar(String lugar) {
        this.lugar = lugar;
    }
}
