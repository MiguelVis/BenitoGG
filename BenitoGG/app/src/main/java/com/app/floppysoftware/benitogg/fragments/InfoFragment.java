package com.app.floppysoftware.benitogg.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.floppysoftware.benitogg.database.BaseDatos;
import com.app.floppysoftware.benitogg.utils.Preferencias;
import com.app.floppysoftware.benitogg.R;
import com.app.floppysoftware.benitogg.Zeta;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Clase que implementa el fragment de información de la aplicación.
 *
 * @author   Miguel I. García López
 * @version  1.0
 * @since    20 Mar 2016
 */
public class InfoFragment extends Fragment {

    /**
     * Constructor.
     */
    public InfoFragment() {

        // Nada
    }

    /**
     * Método llamado para inflar el layout del fragment.
     *
     * @param inflater   inflater
     * @param container  contenedor
     * @param savedInstanceState  estado previamente guardado
     * @return  layout inflado
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflar el layout del fragment
        View v =  inflater.inflate(R.layout.fragment_info, container, false);

        // Tomar la referencia del botón para visitar la web
        Button buttonVisitarWeb = (Button) v.findViewById(R.id.buttonWeb);

        // Fijar listener del botón
        buttonVisitarWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Lanzar la página web
                new LanzarWeb().execute();
            }
        });

        // Devolver layout inflado
        return v;
    }

    /**
     * Clase interna, para lanzar el navegador. Extiende de AsyncTask, para
     * no saturar el thread del UI, mientras se accede a la base de datos.
     */
    private class LanzarWeb extends AsyncTask<Void, Void, Integer> {

        /**
         * Tarea a realizar en background, fuera del thread del UI. Devuelve
         * la escala de casos resueltos: 0..4.
         *
         * @param params  Nada
         * @return        Escala
         */
        @Override
        protected Integer doInBackground(Void... params) {

            // Abrir base de datos
            BaseDatos bd = new BaseDatos(getActivity(), false);

            // Tomar nº de casos resueltos
            int numCasosResueltos = bd.getNumCasos(true);

            // Tomar nº de casos totales
            int numCasosTotales = numCasosResueltos + bd.getNumCasos(false);

            // Devolver escala
            return numCasosResueltos * 4 / numCasosTotales;
        }

        /**
         * Tarea a realizar en el thread del UI, una vez finalizada la tarea
         * en background. Compone la URL con los parámetros, y lanza el navegador.
         *
         * @param escala  Escala de casos resueltos
         */
        @Override
        protected void onPostExecute(Integer escala) {

            // URL de la página web
            String url = Zeta.WEBSITE;

            // Añadir los parámetros a la URL
            try {

                // Escala de casos resueltos
                url = url.concat("?escala=" + URLEncoder.encode(escala.toString(), "UTF-8"));

                // Nombre del jugador, eliminando posibles espacios a derecha e izquierda
                String nombre = Preferencias.getNombre(getActivity()).trim();

                // Añadir el nombre como argumento a la URL
                if(!nombre.isEmpty()) {
                    url = url.concat("&nombre=" + URLEncoder.encode(nombre, "UTF-8"));
                }

                // ¿Usuario zurdo?
                url = url.concat("&zurdo=" + URLEncoder.encode(Preferencias.getZurdo(getActivity()) ? "1" : "0", "UTF-8"));

            } catch (UnsupportedEncodingException e) {

                // Excepción
                e.printStackTrace();
            }

            // Lanzar el navegador con la URL completa
            try {

                // Lanzar activity
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

            } catch (ActivityNotFoundException e) {

                // Excepción
                e.printStackTrace();
            }
        }
    }
}
