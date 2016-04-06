package com.app.floppysoftware.benitogg;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DbHelper extends SQLiteOpenHelper {

    private static final String COMENTARIO = "#";

    private Context context;

    private boolean reset;

    /**
     * Constructor
     *
     * @param context
     * @param reset
     */
    public DbHelper(Context context, boolean reset) {

        super(context, Contract.DATABASE_NAME, null, Contract.DATABASE_VERSION);

        this.context = context;
        this.reset = reset;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //db.execSQL(BdContract.SQL_DROP_TABLE);     // Drop table
        //db.execSQL(BdContract.SQL_CREATE_TABLE);   // Create table
        //db.execSQL(BdContract.SQL_INSERT_DEFAULT); // Insert default rows

        createAndPopulate(db, true);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        if(reset) {
            db.execSQL(Contract.Actores.SQL_DROP_TABLE);
            db.execSQL(Contract.Objetos.SQL_DROP_TABLE);
            //db.execSQL(Contract.Dichos.SQL_DROP_TABLE);
            db.execSQL(Contract.Casos.SQL_DROP_TABLE);

            createAndPopulate(db, false);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Nothing yet
    }

    private void createAndPopulate(SQLiteDatabase db, boolean todo) {
        // FIXME: Exceptions

        if(todo) {
            db.execSQL(Contract.Lugares.SQL_CREATE_TABLE);


            Log.i("DbHelper", Contract.Lugares.SQL_CREATE_TABLE);
        }

        db.execSQL(Contract.Actores.SQL_CREATE_TABLE);
        db.execSQL(Contract.Objetos.SQL_CREATE_TABLE);
        //db.execSQL(Contract.Dichos.SQL_CREATE_TABLE);
        db.execSQL(Contract.Casos.SQL_CREATE_TABLE);

        if(todo) {
            populate(db, Contract.FICHERO_LUGARES, Contract.Lugares.TABLE_NAME);
        }

        populate(db, Contract.FICHERO_ACTORES, Contract.Actores.TABLE_NAME);
        populate(db, Contract.FICHERO_OBJETOS, Contract.Objetos.TABLE_NAME);
        //populate(db, "zeta_dichos_no_vale.txt", Contract.Dichos.TABLE_NAME);
        populate(db, Contract.FICHERO_CASOS, Contract.Casos.TABLE_NAME);

    }

    private void populate(SQLiteDatabase db, String fileName, String tableName) {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader r = null;
        String line = null;
        String values = null;

        try {
            is = context.getAssets().open("zeta_" + fileName + ".txt");
            // Lee y convierte de ANSI (windows-1252) a Unicode -- nota: SQLite utiliza UTF8. -- grabando los txt como UTF8, da error en cur.moveToFirst();
            // isr = new InputStreamReader(is, "windows-1252");
            isr = new InputStreamReader(is);
            r = new BufferedReader(isr);

            do {

                // Leer línea
                line = r.readLine();

                // Un registro finaliza con el fin del fichero, una línea en blanco, o un comentario
                if(line == null || line.length() == 0 || line.startsWith(COMENTARIO)) {

                    // Insertar registro si hay datos leídos, pendientes de escribir
                    if(values != null) {

                        // Escribir registro
                        db.execSQL("insert into " + tableName + " values (" + values + ");");

                        // Invalidar datos
                        values = null;
                    }
                } else {
                    // Línea con datos

                    // Entrecomillar el valor, si no es un número o null (se supone que
                    // es una cadena de texto).
                    if(!Character.isDigit(line.charAt(0)) && !line.equals("null")) {
                        line = "'" + line + "'";
                    }

                    // Añadir datos
                    if(values == null) {
                        values = line;
                    } else {
                        values = values.concat("," + line);
                    }
                }
            } while (line != null);

        }
        catch (SQLException | IOException e) {
            e.printStackTrace();
        }

        if(r != null) {
            try {
                r.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(isr != null) {
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(isr != null) {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
