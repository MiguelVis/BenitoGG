package com.app.floppysoftware.benitogg.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.app.floppysoftware.benitogg.R;

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

    // Último AlertDialog construido
    private static AlertDialog alertDialog;

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
    public static AlertDialog continuar(Context contexto, int icono, String titulo, String texto,
                                        DialogInterface.OnClickListener onClickContinuar) {

        // Crear un AlertDialog
        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(contexto);

        // Mostrar AlertDialog según parámetros de entrada.
        alertDialog =  dlgBuilder.setTitle(titulo)
                .setIcon(icono)
                .setMessage(texto)
                .setPositiveButton(R.string.dialogo_continuar, onClickContinuar)
                .setCancelable(false)
                .show();

        // Devolverlo
        return alertDialog;
    }

    public static AlertDialog preguntarSiCancelar(Context contexto, int icono, String titulo, String texto,
                                                  DialogInterface.OnClickListener onClickSi,
                                                  DialogInterface.OnClickListener onClickCancelar) {

        // Crear un AlertDialog
        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(contexto);

        // Mostrar AlertDialog según parámetros de entrada.
        alertDialog =  dlgBuilder.setTitle(titulo)
                .setIcon(icono)
                .setMessage(texto)
                .setPositiveButton(R.string.dialogo_si, onClickSi)
                .setNegativeButton(R.string.dialogo_cancelar, onClickCancelar)
                .setCancelable(false)
                .show();

        // Devolverlo
        return alertDialog;
    }

    public static AlertDialog seleccionar(Context contexto, int icono, String titulo, CharSequence[] items,
                                             DialogInterface.OnClickListener onClick) {

        // Crear un AlertDialog
        AlertDialog.Builder dlgBuilder = new AlertDialog.Builder(contexto);

        // Mostrar AlertDialog según parámetros de entrada.
        alertDialog =  dlgBuilder.setTitle(titulo)
                .setIcon(icono)
                .setItems(items, onClick)
                .setNegativeButton(R.string.dialogo_cancelar, null)
                .setCancelable(false)
                .show();

        // Devolverlo
        return alertDialog;
    }

    /**
     * Devolver referencia al último AlertDialog construido.
     *
     * @return  último AlertDialog creado, o null si no se ha creado ninguno
     */
    public static AlertDialog getAlertDialog() {

        return alertDialog;
    }
}
