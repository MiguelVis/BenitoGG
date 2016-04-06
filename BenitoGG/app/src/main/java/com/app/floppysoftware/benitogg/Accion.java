package com.app.floppysoftware.benitogg;

/**
 * Clase que implementa una acción a realizar por el protagonista.
 *
 * @author   Miguel I. García López
 * @version  1.0
 * @since    07 Mar 2016
 */
public class Accion {

    // Atributos de una acción
    private int id;         // Id
    private int stringId;   // Descripción (id de recurso)

    /**
     * Constructor.
     *
     * @param id        Id
     * @param stringId  Descripción (id de recurso)
     */
    public Accion(int id, int stringId) {

        // Asignar valores según parámetros de entrada
        this.id = id;
        this.stringId = stringId;
    }

    /**
     * Devolver id de la acción.
     *
     * @return  Id
     */
    int getId() {
        return id;
    }

    /**
     * Devolver descripción de la acción.
     *
     * @return  Id del recurso
     */
    int getStringId() {
        return stringId;
    }
}
