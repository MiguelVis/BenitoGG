package com.app.floppysoftware.benitogg;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class BaseDatos {

    private static final String NUEVA_LINEA_IN = "<br>";
    private static final String NUEVA_LINEA_OUT = "\n\n";

    private DbHelper db_helper = null;
    private SQLiteDatabase db_database = null;

    public BaseDatos(Context context, boolean reset) {

        db_helper = new DbHelper(context, reset);
        db_database = db_helper.getWritableDatabase();
    }

    public void cerrar() {
        db_database.close();
        db_helper.close();

    }

    public ArrayList<Lugar> getLugares() {

        Cursor cur = db_database.query(Contract.Lugares.TABLE_NAME,
                null,
                null,
                null, null, null, null);

        ArrayList<Lugar> lugares = new ArrayList<Lugar>();

        if(cur != null) {
            while(cur.moveToNext()) {

                String id = cur.getString(cur.getColumnIndex(Contract.Lugares.ID_NAME));
                String titulo = cur.getString(cur.getColumnIndex(Contract.Lugares.TITULO_NAME));
                String detalle = cur.getString(cur.getColumnIndex(Contract.Lugares.DETALLE_NAME)).replace(NUEVA_LINEA_IN, NUEVA_LINEA_OUT);
                int x = cur.getInt(cur.getColumnIndex(Contract.Lugares.X_NAME));
                int y = cur.getInt(cur.getColumnIndex(Contract.Lugares.Y_NAME));
                String lugarNorte = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_NORTE_NAME));
                String lugarSur = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_SUR_NAME));
                String lugarEste = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_ESTE_NAME));
                String lugarOeste = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_OESTE_NAME));

                lugares.add(new Lugar(id, titulo, detalle, x, y, lugarNorte, lugarSur, lugarEste, lugarOeste));
            }

            cur.close();
        }

        return lugares;
    }

    public Lugar getLugar(String id) {

        Cursor cur = db_database.query(Contract.Lugares.TABLE_NAME,
                null,
                Contract.Lugares.ID_NAME + "='" + id + "'", // WHERE
                null, null, null, null);

        Lugar lugar = null;

        if(cur != null) {
            if(cur.moveToFirst()) {

                String titulo = cur.getString(cur.getColumnIndex(Contract.Lugares.TITULO_NAME));
                String detalle = cur.getString(cur.getColumnIndex(Contract.Lugares.DETALLE_NAME)).replace(NUEVA_LINEA_IN, NUEVA_LINEA_OUT);
                int x = cur.getInt(cur.getColumnIndex(Contract.Lugares.X_NAME));
                int y = cur.getInt(cur.getColumnIndex(Contract.Lugares.Y_NAME));
                String lugarNorte = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_NORTE_NAME));
                String lugarSur = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_SUR_NAME));
                String lugarEste = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_ESTE_NAME));
                String lugarOeste = cur.getString(cur.getColumnIndex(Contract.Lugares.LUGAR_OESTE_NAME));

                lugar = new Lugar(id, titulo, detalle, x, y, lugarNorte, lugarSur, lugarEste, lugarOeste);
            }

            cur.close();
        }

        return lugar;
    }



    public ArrayList<Actor> getActores(String lugar) {
        String where = null;

        if(lugar != null) {
            where = Contract.Actores.LUGAR_ID_NAME + "='" + lugar + "'";
        }

        Cursor cur = db_database.query(Contract.Actores.TABLE_NAME,
                null,
                where,
                null, null, null, null);

        ArrayList<Actor> actores = null;

        if(cur != null) {

            actores = new ArrayList<Actor>();

            while(cur.moveToNext()) {

                String idActor = cur.getString(cur.getColumnIndex(Contract.Actores.ID_NAME));
                String lugarActor = cur.getString(cur.getColumnIndex(Contract.Actores.LUGAR_ID_NAME));

                actores.add(new Actor(idActor, lugarActor));
            }

            cur.close();
        }

        return actores;
    }

    /******************************
    String columns[] = new String[]{ColumnQuotes.BODY_QUOTES};
    String selection = ColumnQuotes.AUTHOR_QUOTES + " = ? ";//WHERE author = ?
    String selectionArgs[] = new String[]{"John D. Rockefeller"};

    Cursor c = db.query(
            "Quotes",
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
    );
    ******************************/
    public Actor getActor(String id) {
        Cursor cur = db_database.query(Contract.Actores.TABLE_NAME,
                null,
                Contract.Actores.ID_NAME + "='" + id + "'", // WHERE
                null, null, null, null);

       Actor actor = null;

        if(cur != null) {

            if(cur.moveToFirst()) {

                String lugar = cur.getString(cur.getColumnIndex(Contract.Actores.LUGAR_ID_NAME));

                actor = new Actor(id, lugar);
            }

            cur.close();
        }

        return actor;
    }

    // Cambia un actor de lugar
    public void updateActor(Actor actor) {
        ContentValues values = new ContentValues();

        values.put(Contract.Actores.LUGAR_ID_NAME, actor.getLugar());

        String selection = Contract.Actores.ID_NAME + " = ?";
        String[] selectionArgs = { actor.getId() };

        db_database.update(Contract.Actores.TABLE_NAME, values, selection, selectionArgs);
    }

    public ArrayList<Objeto> getObjetos(String lugar) {
        String where = null;

        if(lugar != null) {
            where = Contract.Objetos.LUGAR_ID_NAME + "='" + lugar + "'";
        }
        Cursor cur = db_database.query(Contract.Objetos.TABLE_NAME,
                null,
                where,
                null, null, null, null);

        ArrayList<Objeto> objetos = null;

        if(cur != null) {

            objetos = new ArrayList<Objeto>();

            while(cur.moveToNext()) {

                String idObjeto = cur.getString(cur.getColumnIndex(Contract.Objetos.ID_NAME));
                String nombreObjeto = cur.getString(cur.getColumnIndex(Contract.Objetos.NOMBRE_NAME));
                String lugarObjeto = cur.getString(cur.getColumnIndex(Contract.Objetos.LUGAR_ID_NAME));
                String estadoObjeto = cur.getString(cur.getColumnIndex(Contract.Objetos.ESTADO_NAME));

                objetos.add(new Objeto(idObjeto, nombreObjeto, lugarObjeto, estadoObjeto));
            }

            cur.close();
        }

        return objetos;
    }

    public Objeto getObjeto(String id) {
        Cursor cur = db_database.query(Contract.Objetos.TABLE_NAME,
                null,
                Contract.Objetos.ID_NAME + "='" + id + "'", // WHERE
                null, null, null, null);

        Objeto objeto = null;

        if(cur != null) {

            if(cur.moveToFirst()) {

                String nombre = cur.getString(cur.getColumnIndex(Contract.Objetos.NOMBRE_NAME));
                String lugar = cur.getString(cur.getColumnIndex(Contract.Objetos.LUGAR_ID_NAME));
                String estado = cur.getString(cur.getColumnIndex(Contract.Objetos.ESTADO_NAME));

                objeto = new Objeto(id, nombre, lugar, estado);
            }

            cur.close();
        }

        return objeto;
    }

    // Cambia objeto de lugar y estado
    public void updateObjeto(Objeto objeto) {
        ContentValues values = new ContentValues();

        values.put(Contract.Objetos.LUGAR_ID_NAME, objeto.getLugar());
        values.put(Contract.Objetos.ESTADO_NAME, objeto.getEstado());

        String selection = Contract.Objetos.ID_NAME + " = ?";
        String[] selectionArgs = { objeto.getId() };

        db_database.update(Contract.Objetos.TABLE_NAME, values, selection, selectionArgs);
    }

    /************************
    public Dicho getDicho(String actorId, String lugarId) {
        Cursor cur = db_database.query(Contract.Dichos.TABLE_NAME,
                null,
                Contract.Dichos.ACTOR_ID_NAME + "='" + actorId + "' and " + Contract.Dichos.LUGAR_ID_NAME + "='" + lugarId + "'", // WHERE
                null, null, null, null);

        Dicho dicho = null;

        if(cur != null) {

            if(cur.moveToFirst()) {

                String actor = cur.getString(cur.getColumnIndex(Contract.Dichos.ACTOR_ID_NAME));
                String lugar = cur.getString(cur.getColumnIndex(Contract.Dichos.LUGAR_ID_NAME));
                String detalle = cur.getString(cur.getColumnIndex(Contract.Dichos.DETALLE_NAME)).replace('<', '\n');

                dicho = new Dicho(actor, lugar, detalle);
            }

            cur.close();
        }

        return dicho;
    }
    *****************/

    public ArrayList<Caso> getCasos(Boolean resuelto) {
        String where = null;

        if(resuelto != null) {
            where = Contract.Casos.RESUELTO_NAME + "=" + (resuelto ? '1' : '0');
        }

        Cursor cur = db_database.query(Contract.Casos.TABLE_NAME,
                null,
                where,
                null, null, null, null);

        ArrayList<Caso> casos = null;

        if(cur != null) {

            casos = new ArrayList<Caso>();

            while(cur.moveToNext()) {

                int idCaso = cur.getInt(cur.getColumnIndex(Contract.Casos.ID_NAME));
                String nombreCaso = cur.getString(cur.getColumnIndex(Contract.Casos.NOMBRE_NAME));
                boolean resueltoCaso = cur.getInt(cur.getColumnIndex(Contract.Casos.RESUELTO_NAME)) > 0;

                casos.add(new Caso(idCaso, nombreCaso, resueltoCaso));
            }

            cur.close();
        }

        return casos;
    }

    public int getNumeroDeCasos(Boolean resuelto) {

        String where = null;

        if(resuelto != null) {
            where = Contract.Casos.RESUELTO_NAME + "=" + (resuelto ? '1' : '0');
        }

        return (int) DatabaseUtils.queryNumEntries(db_database, Contract.Casos.TABLE_NAME, where);
    }

    public void resuelveCaso(int id) {
        ContentValues values = new ContentValues();

        values.put(Contract.Casos.RESUELTO_NAME, 1);

        String selection = Contract.Casos.ID_NAME + " = ?";
        String[] selectionArgs = { "" + id };

        db_database.update(Contract.Casos.TABLE_NAME, values, selection, selectionArgs);
    }

    public Caso getCaso(int id) {
        Cursor cur = db_database.query(Contract.Casos.TABLE_NAME,
                null,
                Contract.Casos.ID_NAME + "=" + id, // WHERE
                null, null, null, null);

        Caso caso = null;

        if(cur != null) {

            if(cur.moveToFirst()) {

                String nombre = cur.getString(cur.getColumnIndex(Contract.Casos.NOMBRE_NAME));
                boolean resuelto = cur.getInt(cur.getColumnIndex(Contract.Casos.RESUELTO_NAME)) > 0;

                caso = new Caso(id, nombre, resuelto);
            }

            cur.close();
        }

        return caso;
    }


}
