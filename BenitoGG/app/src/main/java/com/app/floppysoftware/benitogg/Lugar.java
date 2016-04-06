package com.app.floppysoftware.benitogg;

/**
 * Clase que implementa un lugar del mapa.
 *
 * @author   Miguel I. García López
 * @version  1.0
 * @since    08 Mar 2016
 */
public class Lugar {

    // IDs de lugares
    public static final String BOLSILLO = "Bolsillo";

    private String id;
    private String titulo;
    private String detalle;
    private int x;
    private int y;
    private String lugarNorte;
    private String lugarSur;
    private String lugarEste;
    private String lugarOeste;

    public Lugar(String id, String titulo, String detalle, int x, int y, String lugarNorte, String lugarSur, String lugarEste, String lugarOeste) {
        this.id = id;
        this.titulo = titulo;
        this.detalle = detalle;
        this.x = x;
        this.y = y;
        this.lugarNorte = lugarNorte;
        this.lugarSur = lugarSur;
        this.lugarEste = lugarEste;
        this.lugarOeste = lugarOeste;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDetalle() {
        return detalle;
    }

    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }

    public String getLugarNorte() {
        return lugarNorte;
    }

    public String getLugarSur() {
        return lugarSur;
    }

    public String getLugarEste() {
        return lugarEste;
    }

    public String getLugarOeste() {
        return lugarOeste;
    }
}
