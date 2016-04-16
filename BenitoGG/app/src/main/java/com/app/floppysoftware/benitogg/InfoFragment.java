package com.app.floppysoftware.benitogg;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

                // URL de la página web
                String url = Zeta.WEBSITE;

                // Nombre del jugador, eliminando posibles espacios a derecha e izquierda
                String nombre = Preferencias.getNombre(getActivity()).trim();

                // Añadir el nombre como argumento a la URL
                if(!nombre.isEmpty()) {

                    // Proceder, controlando posibles excepciones
                    try {

                        // Construir URL completa con el argumento
                        url = url.concat("?nombre=" + URLEncoder.encode(nombre, "UTF-8"));

                    } catch (UnsupportedEncodingException e) {

                        // Excepción
                        e.printStackTrace(); // -- FIXME
                    }
                }

                // Lanzar el navegador con la URL completa, controlando
                // posibles excepciones
                try {

                    // Lanzar activity
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                } catch (ActivityNotFoundException e) {

                    // Excepción
                    e.printStackTrace();  // -- FIXME
                }
            }
        });

        // Devolver layout inflado
        return v;
    }
}
