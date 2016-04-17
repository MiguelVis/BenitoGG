package com.app.floppysoftware.benitogg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.util.ArrayList;

/**
 * Clase que implementa la base de datos del juego.
 *
 * @author   Miguel I. García López
 * @version  1.0
 * @since    16 Mar 2016
 */
public class BaseDatos {

    // Conversión para insertar nueva línea en la descripción de un lugar
    private static final String NUEVA_LINEA_IN = "<br>";   // Origen desde la base de datos
    private static final String NUEVA_LINEA_OUT = "\n\n";  // Conversión para el View

    // Helper
    private DbHelper db_helper = null;

    // Base de datos
    private SQLiteDatabase db_database = null;

    /**
     * Constructor. Abre (y opcionalmente reinicializa) la base de datos.
     *
     * @param context  Contexto
     * @param reset    True para reinicializar la base de datos,
     *                 false sólo para abrir.
     */
    public BaseDatos(Context context, boolean reset) {

        // Crear helper
        db_helper = new DbHelper(context, reset);

        // Obtener base de datos
        try {
            db_database = db_helper.getWritableDatabase();
        } catch(SQLiteException ex) {
            // Datos de la excepción
            ex.printStackTrace();

            // Cerrar helper
            db_helper.close();
        }
    }

    /**
     * Cerrar la base de datos.
     */
    public void cerrar() {

        // Cerrar base de datos
        db_database.close();

        // Cerrar helper
        db_helper.close();
    }

    /**
     * Devolver todos los lugares.
     *
     * @return  Lugares
     */
    public ArrayList<Lugar> getLugares() {

        // Realizar la petición, obteniendo un cursor
        Cursor cur = db_database.query(Contract.Lugares.TABLE_NAME,
                null,
                null,
                null, null, null, null);

        // Crear array para los lugares
        ArrayList<Lugar> lugares = new ArrayList<Lugar>();

        // Rellenar el array con cada lugar devuelto
        if(cur != null) {

            // Siguiente registro
            while(cur.moveToNext()) {

                // Tomar datos
                String id = cur.getString(cur.getColumnIndex(Contract.Lugares.ID_NAME));
                String titulo = cur.getString(cur.getColumnIndex(Contract.Lugares.TITULO_NAME));
                String detalle = cur.getString(cur.getColumnIndex(Contract.Lugares.DETALLE_NAME)).replace(NUEVA_LINEA_IN, NUEVA_LINEA_OUT);
                int x = cur.getInt(cur.getColumnIndex(Contract.Lugares.X_NAME));
                int y = cur.getInt(cur.getColumnIndex(Contract.Lugares.Y_NAME));
                String lugarNorte = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_NORTE_NAME));
                String lugarSur = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_SUR_NAME));
                String lugarEste = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_ESTE_NAME));
                String lugarOeste = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_OESTE_NAME));

                // Añadir lugar al array
                lugares.add(new Lugar(id, titulo, detalle, x, y, lugarNorte, lugarSur, lugarEste, lugarOeste));
            }

            // Cerrar cursor
            cur.close();
        }

        // Devolver array
        return lugares;
    }

    /**
     * Devolver lugar.
     *
     * @param lugarId  Id del lugar
     * @return         Lugar, o null si no existe
     */
    public Lugar getLugar(String lugarId) {

        // Realizar la petición, obteniendo un cursor
        Cursor cur = db_database.query(Contract.Lugares.TABLE_NAME,
                null,
                Contract.Lugares.ID_NAME + "='" + lugarId + "'",  // WHERE
                null, null, null, null);

        // Lugar a devolver
        Lugar lugar = null;

        // Obtener datos, si se han recibido
        if(cur != null) {

            // Primer registro
            if(cur.moveToFirst()) {

                // Obtener datos del lugar
                String titulo = cur.getString(cur.getColumnIndex(Contract.Lugares.TITULO_NAME));
                String detalle = cur.getString(cur.getColumnIndex(Contract.Lugares.DETALLE_NAME)).replace(NUEVA_LINEA_IN, NUEVA_LINEA_OUT);
                int x = cur.getInt(cur.getColumnIndex(Contract.Lugares.X_NAME));
                int y = cur.getInt(cur.getColumnIndex(Contract.Lugares.Y_NAME));
                String lugarNorte = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_NORTE_NAME));
                String lugarSur = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_SUR_NAME));
                String lugarEste = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_ESTE_NAME));
                String lugarOeste = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_OESTE_NAME));

                // Crear lugar con los datos
                lugar = new Lugar(lugarId, titulo, detalle, x, y, lugarNorte, lugarSur, lugarEste, lugarOeste);
            }

            // Cerrar el cursor
            cur.close();
        }

        // Devolver el lugar
        return lugar;
    }

    /**
     * Devolver actor.
     *
     * @param actorId  Id del actor
     * @return         Actor, o null si no existe
     */
    public Actor getActor(String actorId) {

        // Realizar la petición y obtener un cursor
        Cursor cur = db_database.query(Contract.Actores.TABLE_NAME,
                null,
                Contract.Actores.ID_NAME + "='" + actorId + "'",  // WHERE
                null, null, null, null);

        // Actor a devolver
        Actor actor = null;

        // Tomar datos del actor, si existe
        if(cur != null) {

            // Primer registro
            if(cur.moveToFirst()) {

                // Tomar datos del actor
                String lugarId = cur.getString(cur.getColumnIndex(Contract.Actores.LUGAR_ID_NAME));

                // Crear actor
                actor = new Actor(actorId, lugarId);
            }

            // Cerrar cursor
            cur.close();
        }

        // Devolver actor
        return actor;
    }

    /**
     * Actualizar actor. Únicamente se actualizará la id
     * del lugar en que se encuentra el actor.
     *
     * @param actor  Actor
     */
    public void updateActor(Actor actor) {

        // Valores a cambiar
        ContentValues values = new ContentValues();

        // Cambiar sólo el lugar
        values.put(Contract.Actores.LUGAR_ID_NAME, actor.getLugar());

        // Indicar el actor a actualizar
        String selection = Contract.Actores.ID_NAME + " = ?";
        String[] selectionArgs = { actor.getId() };

        // Actualizar actor
        db_database.update(Contract.Actores.TABLE_NAME, values, selection, selectionArgs);
    }

    /**
     * Devolver los objetos que están en un lugar.
     *
     * @param lugarId  Id del lugar
     * @return         Objetos, o null si no hay ninguno
     */
    public ArrayList<Objeto> getObjetos(String lugarId) {

        // Realizar petición y obtener el cursor
        Cursor cur = db_database.query(Contract.Objetos.TABLE_NAME,
                null,
                Contract.Objetos.LUGAR_ID_NAME + "='" + lugarId + "'",  // WHERE
                null, null, null, null);

        // Lista de objetos a devolver
        ArrayList<Objeto> objetos = null;

        // Tomar datos, si los hay
        if(cur != null) {

            // Crear
            objetos = new ArrayList<Objeto>();

            // Siguiente registro
            while(cur.moveToNext()) {

                // Tomar datos del objeto
                String idObjeto = cur.getString(cur.getColumnIndex(Contract.Objetos.ID_NAME));
                String nombreObjeto = cur.getString(cur.getColumnIndex(Contract.Objetos.NOMBRE_NAME));
                String lugarObjeto = cur.getString(cur.getColumnIndex(Contract.Objetos.LUGAR_ID_NAME));
                String estadoObjeto = cur.getString(cur.getColumnIndex(Contract.Objetos.ESTADO_NAME));

                // Crear objeto y añadirlo a la lista
                objetos.add(new Objeto(idObjeto, nombreObjeto, lugarObjeto, estadoObjeto));
            }

            // Cerrar cursor
            cur.close();
        }

        // Devolver objetos
        return objetos;
    }

    /**
     * Devolver un objeto.
     *
     * @param objetoId  Id del objeto
     * @return          Objeto, o null si no existe
     */
    public Objeto getObjeto(String objetoId) {

        // Realizar petición y obtener un cursor
        Cursor cur = db_database.query(Contract.Objetos.TABLE_NAME,
                null,
                Contract.Objetos.ID_NAME + "='" + objetoId + "'",  // WHERE
                null, null, null, null);

        // Objeto a devolver
        Objeto objeto = null;

        // Obtener datos, si los hay
        if(cur != null) {

            // Primer registro
            if(cur.moveToFirst()) {

                // Obtener datos del objeto
                String nombre = cur.getString(cur.getColumnIndex(Contract.Objetos.NOMBRE_NAME));
                String lugar = cur.getString(cur.getColumnIndex(Contract.Objetos.LUGAR_ID_NAME));
                String estado = cur.getString(cur.getColumnIndex(Contract.Objetos.ESTADO_NAME));

                // Crear objeto
                objeto = new Objeto(objetoId, nombre, lugar, estado);
            }

            // Cerrar cursor
            cur.close();
        }

        // Devolver objeto
        return objeto;
    }

    /**
     * Actualizar objeto. Sólo se actualizarán la id del lugar en que
     * se encuentra, y el estado del objeto.
     *
     * @param objeto  Objeto
     */
    public void updateObjeto(Objeto objeto) {

        // Valores a actualizar
        ContentValues values = new ContentValues();

        // Actualizar id del lugar y estado del objeto
        values.put(Contract.Objetos.LUGAR_ID_NAME, objeto.getLugar());
        values.put(Contract.Objetos.ESTADO_NAME, objeto.getEstado());

        // Indicar el objeto a actualizar
        String selection = Contract.Objetos.ID_NAME + " = ?";
        String[] selectionArgs = { objeto.getId() };

        // Actualizar objeto
        db_database.update(Contract.Objetos.TABLE_NAME, values, selection, selectionArgs);
    }

    /**
     * Devolver lista de casos.
     *
     * @return  Lista de casos
     */
    public ArrayList<Caso> getCasos() {

        // Realizar petición y obtener el cursor
        Cursor cur = db_database.query(Contract.Casos.TABLE_NAME,
                null,
                null,
                null, null, null, null);

        // Lista de casos a devolver
        ArrayList<Caso> casos = new ArrayList<Caso>();;

        // Tomar datos leídos
        if(cur != null) {

            // Siguiente registro
            while(cur.moveToNext()) {

                // Tomar datos del caso
                int idCaso = cur.getInt(cur.getColumnIndex(Contract.Casos.ID_NAME));
                String nombreCaso = cur.getString(cur.getColumnIndex(Contract.Casos.NOMBRE_NAME));
                boolean resueltoCaso = cur.getInt(cur.getColumnIndex(Contract.Casos.RESUELTO_NAME)) > 0;

                // Crear caso y añadirlo a la lista
                casos.add(new Caso(idCaso, nombreCaso, resueltoCaso));
            }

            // Cerrar el cursor
            cur.close();
        }

        // Devolver lista de casos
        return casos;
    }

    /**
     * Actualizar un caso. Únicamente se actualiza el estado
     * del caso.
     *
     * @param caso  Caso
     */
    public void updateCaso(Caso caso) {

        // Valores
        ContentValues values = new ContentValues();

        // Indicar que el caso está resuelto
        values.put(Contract.Casos.RESUELTO_NAME, caso.getResuelto());

        // Indicar el caso a actualizar
        String selection = Contract.Casos.ID_NAME + " = ?";
        String[] selectionArgs = { "" + caso.getId() };

        // Actualizar caso
        db_database.update(Contract.Casos.TABLE_NAME, values, selection, selectionArgs);
    }

    /**
     * Devolver un caso.
     *
     * @param casoId  Id del caso
     * @return        Caso, o null si no existe
     */
    public Caso getCaso(int casoId) {

        // Realizar petición y obtener el cursor
        Cursor cur = db_database.query(Contract.Casos.TABLE_NAME,
                null,
                Contract.Casos.ID_NAME + "=" + casoId, // WHERE
                null, null, null, null);

        // Caso a devolver
        Caso caso = null;

        // Leer datos del caso
        if(cur != null) {

            // Primer registro
            if(cur.moveToFirst()) {

                // Leer datos del caso
                String nombre = cur.getString(cur.getColumnIndex(Contract.Casos.NOMBRE_NAME));
                boolean resuelto = cur.getInt(cur.getColumnIndex(Contract.Casos.RESUELTO_NAME)) > 0;

                // Crear caso
                caso = new Caso(casoId, nombre, resuelto);
            }

            // Cerrar cursor
            cur.close();
        }

        // Devolver caso
        return caso;
    }
}
