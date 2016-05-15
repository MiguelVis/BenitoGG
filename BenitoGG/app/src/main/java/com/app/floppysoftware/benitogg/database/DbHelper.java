package com.app.floppysoftware.benitogg.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Clase que implementa el helper de la base de datos.
 *
 * @author   Miguel I. García López
 * @version  1.0
 * @since    16 Mar 2016
 */
public class DbHelper extends SQLiteOpenHelper {

    // Las líneas que comiencen por el caracter de comentario
    // serán ignoradas.
    private static final String COMENTARIO = "#";

    // Contexto
    private Context context;

    // Flag de reset
    private boolean reset;

    /**
     * Constructor.
     *
     * @param context  Contexto
     * @param reset    True si la base de datos ha de ser reinicializada
     */
    public DbHelper(Context context, boolean reset) {

        // Llamar a la superclase
        super(context, Contract.DATABASE_NAME, null, Contract.DATABASE_VERSION);

        // Fijar atributos según parámetros de entrada
        this.context = context;
        this.reset = reset;
    }

    /**
     * Método llamado cuando se tenga que crear la base de datos.
     *
     * @param db  Base de datos
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Crear base de datos, e insertar datos
        // en las tablas.
        creaBaseDatos(db, true);
    }

    /**
     * Método llamado cuando se tenga que abrir la base de datos.
     *
     * @param db  Base de datos
     */
    @Override
    public void onOpen(SQLiteDatabase db) {

        // Si se ha de reinicializar la base de datos,
        // eliminar las tablas.
        if(reset) {

            // Eliminar las tablas
            try {
                //db.execSQL(Contract.Lugares.SQL_DROP_TABLE);
                db.execSQL(Contract.Actores.SQL_DROP_TABLE);
                db.execSQL(Contract.Objetos.SQL_DROP_TABLE);
                db.execSQL(Contract.Casos.SQL_DROP_TABLE);
            } catch(SQLException ex) {
                // Hubo una excepción
                ex.printStackTrace();
            }

            // Crear base de datos, e insertar datos
            // en las tablas.
            creaBaseDatos(db, false);
        }
    }

    /**
     * Método llamado al actualizar la base de datos.
     *
     * @param db          Base de datos
     * @param oldVersion  Versión antigua
     * @param newVersion  Versión nueva
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Nada
    }

    /**
     * Crear base de datos, e insertar datos en las tablas.
     *
     * @param db       Base de datos
     * @param lugares  True para crear la tabla de Lugares, false en caso contrario
     */
    private void creaBaseDatos(SQLiteDatabase db, boolean lugares) {

        // Crear tablas
        try {
            if(lugares) {
                db.execSQL(Contract.Lugares.SQL_CREATE_TABLE);
            }
            db.execSQL(Contract.Actores.SQL_CREATE_TABLE);
            db.execSQL(Contract.Objetos.SQL_CREATE_TABLE);
            db.execSQL(Contract.Casos.SQL_CREATE_TABLE);
        } catch(SQLException ex) {
            // Hubo una excepción
            ex.printStackTrace();
        }

        // Insertar datos en las tablas
        if(lugares) {
            insertarDatos(db, Contract.FICHERO_LUGARES, Contract.Lugares.TABLE_NAME);
        }
        insertarDatos(db, Contract.FICHERO_ACTORES, Contract.Actores.TABLE_NAME);
        insertarDatos(db, Contract.FICHERO_OBJETOS, Contract.Objetos.TABLE_NAME);
        insertarDatos(db, Contract.FICHERO_CASOS, Contract.Casos.TABLE_NAME);
    }

    /**
     * Insertar datos iniciales en una tabla.
     *
     * @param db             Base de datos
     * @param nombreFichero  Nombre del fichero con los datos iniciales
     * @param nombreTabla    Nombre de la tabla
     */
    private void insertarDatos(SQLiteDatabase db, String nombreFichero, String nombreTabla) {

        // Variables para el fichero con los datos iniciales
        InputStream is = null;         // InputStream
        InputStreamReader isr = null;  // InputStreamReader
        BufferedReader br = null;      // BufferedReader

        // Datos
        String line;          // Línea leída
        String values = null; // Valores a insertar

        // Leer los datos desde el fichero
        // e insertarlos en la tabla.
        try {

            // InputStream; abrir el fichero con los datos iniciales
            is = context.getAssets().open("zeta_" + nombreFichero + ".txt");

            // InputStreamReader
            isr = new InputStreamReader(is);

            // BufferedReader
            br = new BufferedReader(isr);

            // Leer los datos del fichero,
            // línea a línea.
            do {

                // Leer una línea
                line = br.readLine();

                // Un registro finaliza con el fin del fichero,
                // una línea en blanco,
                // o un comentario.
                if(line == null || line.length() == 0 || line.startsWith(COMENTARIO)) {

                    // Insertar registro si hay datos leídos,
                    // pendientes de escribir.
                    if(values != null) {

                        // Insertar registro en la tabla
                        db.execSQL("insert into " + nombreTabla + " values (" + values + ");");

                        // Invalidar datos
                        values = null;
                    }
                } else {
                    // Línea con datos

                    // Entrecomillar el dato leído si no es un número o la cadena 'null' (se supone que
                    // es una cadena de texto válida).
                    if(!Character.isDigit(line.charAt(0)) && !line.equals("null")) {
                        line = "'" + line + "'";
                    }

                    // Añadir a los datos existentes
                    if(values == null) {

                        // No hay datos todavía, inicializarlos con el dato leído
                        values = line;
                    } else {

                        // Añadir dato a los existentes
                        values = values.concat("," + line);
                    }
                }

              // Seguir leyendo hasta el final
              // del fichero.
            } while (line != null);

        }
        catch (SQLException | IOException ex) {

            // Hubo una excepción
            ex.printStackTrace();
        }

        // Cerrar BufferedReader
        if(br != null) {
            try {
                br.close();
            } catch (IOException ex) {

                // Excepción
                ex.printStackTrace();
            }
        }

        // Cerrar InputStreamReader
        if(isr != null) {
            try {
                isr.close();
            } catch (IOException ex) {

                // Excepción
                ex.printStackTrace();
            }
        }

        // Cerrar InputStream
        if(is != null) {
            try {
                is.close();
            } catch (IOException ex) {

                // Excepción
                ex.printStackTrace();
            }
        }
    }
}
