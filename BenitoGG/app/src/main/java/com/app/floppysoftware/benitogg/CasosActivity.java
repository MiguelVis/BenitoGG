package com.app.floppysoftware.benitogg;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity para mostrar una lista de nombres de casos resueltos, y
 * un texto informativo. Utiliza el fragment CasosFragment. Es utilizada
 * únicamente para la versión tablet.
 */
public class CasosActivity extends Activity {

    /**
     * Método llamado al crear la activity.
     *
     * @param savedInstanceState  estado previamente guardado
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Llamar a la superclase
        super.onCreate(savedInstanceState);

        // Fijar layout
        setContentView(R.layout.activity_casos);
    }
}
