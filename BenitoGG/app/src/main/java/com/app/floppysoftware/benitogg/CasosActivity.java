package com.app.floppysoftware.benitogg;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity para mostrar la lista de nombres de casos, así como el
 * estado en que se encuentran, y un texto informativo.
 *
 * Utiliza el fragment CasosFragment.
 *
 * Esta activity es utilizada únicamente para la versión tablet.
 */
public class CasosActivity extends Activity {

    /**
     * Método llamado al crear la activity.
     *
     * @param savedInstanceState  Estado previamente guardado
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Llamar a la superclase
        super.onCreate(savedInstanceState);

        // Fijar layout
        setContentView(R.layout.activity_casos);
    }
}
