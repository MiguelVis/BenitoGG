package com.app.floppysoftware.benitogg;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Clase que implementa las preferencias del usuario.
 *
 * @author   Miguel I. García López
 * @version  1.0
 * @since    09 Mar 2016
 */
public final class Preferencias {

    // Nombre del fichero de preferencias
    private static final String FICHERO_PREFERENCIAS = "prefs";

    // Claves para las preferencias
    private static final String KEY_NOMBRE = "nombre";       // Nombre del usuario
    private static final String KEY_SONIDO = "sonido";       // Sonido
    private static final String KEY_ZURDO = "zurdo";         // Modo zurdo (para tablets en horizontal)
    private static final String KEY_VERTICAL = "vertical";   // Modo vertical (para tablets)

    /**
     * Constructor que impide crear objetos de esta clase.
     */
    private Preferencias() {

        // Nada
    }

    /**
     * Devolver manejador de las preferencias.
     *
     * @param context  Contexto
     * @return  Manejador
     */
    private static SharedPreferences getShPrefs(Context context) {

        // Devolver manejador de las preferencias
        return context.getSharedPreferences(FICHERO_PREFERENCIAS, Activity.MODE_PRIVATE);
    }

    /**
     * Devolver nombre del usuario.
     *
     * @param context  Contexto
     * @return  Nombre (o cadena vacía, si no ha sido fijado)
     */
    public static String getNombre(Context context) {

        // Devolver nombre del usuario, o cadena vacía si no ha sido fijado
        return getShPrefs(context).getString(KEY_NOMBRE, "");
    }

    /**
     * Cambiar nombre del usuario. Si nombre es null, se eliminará el dato.
     *
     * @param context  Contexto
     * @param nombre  Nombre
     */
    public static void setNombre(Context context, String nombre) {

        // Cambiar nombre del usuario. Si nombre es null, se eliminará el dato.
        getShPrefs(context).edit().putString(KEY_NOMBRE, nombre).commit();
    }

    /**
     * Devolver preferencia del sonido.
     *
     * @param context  Contexto
     * @return  Preferencia del sonido (true si se desea, false en caso contrario)
     */
    public static boolean getSonido(Context context) {

        // Devolver preferencia del sonido, o false si no ha sido fijado
        return getShPrefs(context).getBoolean(KEY_SONIDO, false);
    }

    /**
     * Cambiar la preferencia del sonido.
     *
     * @param context  Contexto
     * @param onoff  True para activarlo, false para desactivarlo
     */
    public static void setSonido(Context context, boolean onoff) {

        // Cambiar la preferencia del sonido
        getShPrefs(context).edit().putBoolean(KEY_SONIDO, onoff).commit();
    }

    /**
     * Devolver preferencia del modo zurdos.
     *
     * @param context  Contexto
     * @return  Preferencia del modo zurdos (true si se desea, false en caso contrario)
     */
    public static boolean getZurdo(Context context) {

        // Devolver preferencia del modo zurdos, o false si no ha sido fijado
        return getShPrefs(context).getBoolean(KEY_ZURDO, false);
    }

    /**
     * Cambiar la preferencia del modo zurdos.
     *
     * @param context  Contexto
     * @param onoff  True para activarlo, false para desactivarlo
     */
    public static void setZurdo(Context context, boolean onoff) {

        // Cambiar la preferencia del modo zurdos
        getShPrefs(context).edit().putBoolean(KEY_ZURDO, onoff).commit();
    }

    /**
     * Devolver la preferencia del modo vertical.
     *
     * @param context  Contexto
     * @return  Preferencia del modo vertical (true si se desea, false en caso contrario)
     */
    public static boolean getVertical(Context context) {

        // Devolver la preferencia del modo vertical, o false si no ha sido fijado
        return getShPrefs(context).getBoolean(KEY_VERTICAL, false);
    }

    /**
     * Cambiar la preferencia del modo vertical.
     *
     * @param context  Contexto
     * @param onoff  True para activarlo, false para desactivarlo
     */
    public static void setVertical(Context context, boolean onoff) {

        // Cambiar la preferencia del modo vertical
        getShPrefs(context).edit().putBoolean(KEY_VERTICAL, onoff).commit();
    }
}
