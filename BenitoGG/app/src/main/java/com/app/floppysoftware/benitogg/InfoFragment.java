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
 * A simple {@link Fragment} subclass.
 */
public class InfoFragment extends Fragment {

    private Button buttonVisitarWeb;

    public InfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_info, container, false);

        buttonVisitarWeb = (Button) v.findViewById(R.id.buttonWeb);

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
                        e.printStackTrace();
                    }
                }

                // Lanzar el navegador con la URL completa, controlando
                // posibles excepciones
                try {

                    // Lanzar activity
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        return v;
    }




}
