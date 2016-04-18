package com.app.floppysoftware.benitogg;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Clase que implementa el envío de mensajes al usuario,
 * mediante AlertDialog.
 *
 * Su propósito es que la apariencia de todos los AlertDialog
 * sea la misma en toda la app.
 *
 * @author   Miguel I. García López
 * @version  1.0
 * @since    18 Apr 2016
 */
public class Mensaje {

    /**
     * Mostrar AlertDialog con mensaje para el usuario, y
     * el botón 'Continuar'.
     *
     * Devuelve el AlertDialog creado, para poder ser
     * utilizado en tests instrumentales.
     *
     * @param contexto  Contexto
     * @param icono     Icono
     * @param titulo    Título del mensaje
     * @param texto     Texto del mensaje
     *
     * @return          AlertDialog
     */
    public static AlertDialog continuar(Context contexto, int icono, String titulo, String texto) {

        // Crear un AlertDialog
        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(contexto);

        // Mostrar AlertDialog según parámetros de entrada,
        // y devolverlo.
        return dlgBuilder.setTitle(titulo)
                .setIcon(icono)
                .setMessage(texto)
                .setPositiveButton(R.string.dialogo_continuar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nada
                    }
                })
                .show();
    }
}
