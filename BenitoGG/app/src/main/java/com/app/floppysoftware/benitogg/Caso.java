package com.app.floppysoftware.benitogg;

/**
 * Clase que implementa un caso o misterio a resolver.
 *
 * @author   Miguel I. García López
 * @version  1.0
 * @since    07 Mar 2016
 */
public class Caso {

    // Atributos de un caso
    private int id;             // Id
    private String nombre;      // Nombre
    private boolean resuelto;   // Resuelto (true en caso afirmativo, false en caso contrario)

    /**
     * Constructor.
     *
     * @param id        Id
     * @param nombre    Nombre
     * @param resuelto  Resuelto
     */
    public Caso(int id, String nombre, boolean resuelto) {

        // Asignar valores según parámetros de entrada
        this.id = id;
        this.nombre = nombre;
        this.resuelto = resuelto;
    }

    /**
     * Devolver id. del caso.
     *
     * @return  Id
     */
    public int getId() {
        return id;
    }

    /**
     * Devolver nombre del caso.
     *
     * @return  Nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devolver estado de resolución del caso.
     *
     * @return  True si está resuelto, false en caso contrario
     */
    public boolean getResuelto() {
        return resuelto;
    }

    /**
     * Cambiar nombre del caso.
     *
     * @param nombre   nuevo nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Cambiar estado de resolución del caso.
     *
     * @param resuelto  True para resuelto, false en caso contrario
     */
    public void setResuelto(boolean resuelto) {
        this.resuelto = resuelto;
    }
}
