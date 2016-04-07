package com.app.floppysoftware.benitogg;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Fragment de bienvenida. Su único propósito es servir de fragment
 * inicial cuando el dispositivo es una tablet.
 */
public class BienvenidaFragment extends Fragment {

    /**
     * Constructor.
     */
    public BienvenidaFragment() {

        // Nada de momento
    }


    /**
     * Método para inflar el layout del fragment.
     *
     * @param inflater   Inflater
     * @param container  Contenedor
     * @param savedInstanceState  Estado previamente guardado
     * @return  Layout inflado
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflar el layout del fragment
        return inflater.inflate(R.layout.fragment_bienvenida, container, false);
    }
}
