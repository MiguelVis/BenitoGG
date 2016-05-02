package com.app.floppysoftware.benitogg.models;

/**
 * Clase que implementa un lugar del mapa.
 *
 * @author   Miguel I. García López
 * @version  1.1
 * @since    08 Mar 2016
 */
public class Lugar {

    // IDs de lugares independientes de la aventura
    public static final String BOLSILLO = "Bolsillo";

    // Atributos de un lugar
    private String id;           // Id
    private String titulo;       // Título
    private String detalle;      // Detalle
    private String sonido;       // Sonido
    private int x;               // Posición X en el mapa
    private int y;               // Posición Y en el mapa
    private String lugarNorte;   // Lugar al que se llega saliendo por el norte (o null, si no hay salida)
    private String lugarSur;     // Lugar al que se llega saliendo por el sur   (o null, si no hay salida)
    private String lugarEste;    // Lugar al que se llega saliendo por el este  (o null, si no hay salida)
    private String lugarOeste;   // Lugar al que se llega saliendo por el oeste (o null, si no hay salida)

    /**
     * Constructor.
     *
     * @param id          Id
     * @param titulo      Título
     * @param detalle     Detalle
     * @param sonido      Sonido
     * @param x           Posición X en el mapa
     * @param y           Posición Y en el mapa
     * @param lugarNorte  Lugar al que se llega saliendo por el norte (o null, si no hay salida)
     * @param lugarSur    Lugar al que se llega saliendo por el sur   (o null, si no hay salida)
     * @param lugarEste   Lugar al que se llega saliendo por el este  (o null, si no hay salida)
     * @param lugarOeste  Lugar al que se llega saliendo por el oeste (o null, si no hay salida)
     */
    public Lugar(String id, String titulo, String detalle, String sonido, int x, int y,
                 String lugarNorte, String lugarSur, String lugarEste, String lugarOeste) {

        // Asignar atributos según parámetros de entrada
        this.id = id;
        this.titulo = titulo;
        this.detalle = detalle;
        this.sonido = sonido;
        this.x = x;
        this.y = y;
        this.lugarNorte = lugarNorte;
        this.lugarSur = lugarSur;
        this.lugarEste = lugarEste;
        this.lugarOeste = lugarOeste;
    }

    /**
     * Devolver id del lugar.
     *
     * @return  Id
     */
    public String getId() {
        return id;
    }

    /**
     * Devolver título del lugar.
     *
     * @return  Título
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Devolver detalle del lugar.
     *
     * @return  Detalle
     */
    public String getDetalle() {
        return detalle;
    }

    /**
     * Devolver sonido del lugar.
     *
     * @return  Sonido
     */
    public String getSonido() {
        return sonido;
    }

    /**
     * Devolver posición X del lugar en el mapa.
     *
     * @return  X
     */
    public int getX() {
        return x;
    }

    /**
     * Devolver posición Y del lugar en el mapa.
     *
     * @return  Y
     */
    public int getY() {
        return y;
    }

    /**
     * Devolver lugar al que se llega saliendo por el norte, o null si no hay salida.
     *
     * @return  Lugar
     */
    public String getLugarNorte() {
        return lugarNorte;
    }

    /**
     * Devolver lugar al que se llega saliendo por el sur, o null si no hay salida.
     *
     * @return  Lugar
     */
    public String getLugarSur() {
        return lugarSur;
    }

    /**
     * Devolver lugar al que se llega saliendo por el este, o null si no hay salida.
     *
     * @return  Lugar
     */
    public String getLugarEste() {
        return lugarEste;
    }

    /**
     * Devolver lugar al que se llega saliendo por el oeste, o null si no hay salida.
     *
     * @return  Lugar
     */
    public String getLugarOeste() {
        return lugarOeste;
    }
}
