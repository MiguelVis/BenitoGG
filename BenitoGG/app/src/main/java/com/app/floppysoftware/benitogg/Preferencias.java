package com.app.floppysoftware.benitogg;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public final class Preferencias {

    // Fichero de preferencias
    private static final String FICHERO_PREFERENCIAS = "benito_gg";

    // Claves para preferencias
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_SONIDO = "sonido";
    private static final String KEY_ZURDO = "zurdo";
    private static final String KEY_VERTICAL = "vertical";

    /**
     * Constructor que impide crear objetos con esta clase.
     */
    private Preferencias() {

        //
    }

    private static SharedPreferences getShPrefs(Context context) {

        return context.getSharedPreferences(FICHERO_PREFERENCIAS, Activity.MODE_PRIVATE);
    }

    public static String getNombre(Context context) {

        return getShPrefs(context).getString(KEY_NOMBRE, "");
    }

    public static void setNombre(Context context, String nombre) {

        getShPrefs(context).edit().putString(KEY_NOMBRE, nombre).commit();
    }

    public static boolean getSonido(Context context) {

        return getShPrefs(context).getBoolean(KEY_SONIDO, false);
    }

    public static void setSonido(Context context, boolean onoff) {

        getShPrefs(context).edit().putBoolean(KEY_SONIDO, onoff).commit();
    }

    public static boolean getZurdo(Context context) {
        return getShPrefs(context).getBoolean(KEY_ZURDO, false);

    }

    public static void setZurdo(Context context, boolean onoff) {

        getShPrefs(context).edit().putBoolean(KEY_ZURDO, onoff).commit();
    }

    public static boolean getVertical(Context context) {
        return getShPrefs(context).getBoolean(KEY_VERTICAL, false);

    }

    public static void setVertical(Context context, boolean onoff) {

        getShPrefs(context).edit().putBoolean(KEY_VERTICAL, onoff).commit();
    }

}
