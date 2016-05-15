package com.app.floppysoftware.benitogg.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
//import android.util.Log;

import com.app.floppysoftware.benitogg.fragments.PreferenciasFragment;
import com.app.floppysoftware.benitogg.utils.Mensaje;
import com.app.floppysoftware.benitogg.utils.Preferencias;
import com.app.floppysoftware.benitogg.R;
import com.app.floppysoftware.benitogg.database.BaseDatos;
import com.app.floppysoftware.benitogg.fragments.BienvenidaFragment;
import com.app.floppysoftware.benitogg.fragments.InfoFragment;
import com.app.floppysoftware.benitogg.fragments.MenuFragment;

/**
 * Clase que implementa el menú principal.
 *
 * @author   Miguel I. García López
 * @version  1.1
 * @since    08 Mar 2016
 */
public class PrincipalActivity extends Activity implements
        MenuFragment.OnMenuListener,
        PreferenciasFragment.OnPreferenciasListener {

    // Tag para el log
    //private final static String TAG = "Principal";

    // Parámetro extra para indicar que la activity se está
    // recreando, debido a un cambio de orientación.
    private final static String EXTRA_VERTICAL = "ExtraVertical";

    // Tags para los fragments
    private final static String TAG_FRAG_MENU = "FrMenu";
    private final static String TAG_FRAG_BIENVENIDA = "FrBienvenida";
    private final static String TAG_FRAG_PREFERENCIAS = "FrPreferencias";
    private final static String TAG_FRAG_INFO = "FrInfo";

    // Fragments
    private MenuFragment menuFragment;
    private BienvenidaFragment bienvenidaFragment;
    private PreferenciasFragment preferenciasFragment;
    private InfoFragment infoFragment;

    // Variable que indica si el dispositivo es una tablet,
    // con orientación horizontal.
    private boolean esTabletHorizontal = false;

    // --------------------------------------------------
    // Implementación de la interfaz del fragment de menú
    // --------------------------------------------------

    /**
     * Ejecutar la opción seleccionada.
     *
     * @param opcion  Id de la opción
     */
    public void onOpcionSeleccionada(int opcion) {

        // Ejecutar la opción
        switch(opcion) {
            case MenuFragment.MENU_OPCION_JUGAR :

                // Lanzar el juego
                new Jugar().execute();

                break;
            case MenuFragment.MENU_OPCION_PREFERENCIAS:

                // Actuar según sea tablet con orientación horizontal,
                // o no.
                if(esTabletHorizontal) {

                    // Tablet con orientación horizontal
                    ponFragmentArea(getPreferenciasFragment(), TAG_FRAG_PREFERENCIAS);
                } else {

                    // Móvil, o tablet con orientación vertical
                    ponFragmentMovil(getPreferenciasFragment(), TAG_FRAG_PREFERENCIAS);
                }

                break;
            case MenuFragment.MENU_OPCION_INFO :

                // Actuar según sea tablet con orientación horizontal,
                // o no.
                if(esTabletHorizontal) {

                    // Tablet con orientación horizontal
                    ponFragmentArea(getInfoFragment(), TAG_FRAG_INFO);
                } else {

                    // Móvil, o tablet con orientación vertical
                    ponFragmentMovil(getInfoFragment(), TAG_FRAG_INFO);
                }
                break;
            default :

                // Opción desconocida (no debería ocurrir...)
                break;
        }
    }

    // ----------------------------------------------------------
    // Implementación de la interfaz del fragment de preferencias
    // ----------------------------------------------------------

    /**
     * Cambiar al modo diestro o zurdo.
     */
    public void onCambioZurdo() {

        // Recrear la activity; de esta forma
        // se pondrá cada fragment (preferencias y menú) en su lugar.
        recreate();
    }

    /**
     * Cambiar al modo vertical u horizontal.
     */
    public void onCambioVertical() {

        // Crear un nuevo intent y lanzar la activity; de esta
        // forma, se inicializará con la orientación que
        // corresponda.
        Intent intent = getIntent();

        // Poner extra para indicar el cambio
        intent.putExtra(EXTRA_VERTICAL, true);

        // Finalizar esta activity
        finish();

        // Lanzar el intent
        startActivity(intent);

        // Nota: No se utiliza el método recreate() como en onCambioZurdo(),
        // porque Android restauraría los fragments, dando lugar
        // a errores e inconsistencias.
    }

    /**
     * Activar / desactivar el sonido.
     */
    public void onCambioSonido() {

        // Nada de momento
    }

    /**
     * Resetear juego.
     */
    public void onResetJuego() {

        // Listener para el botón SI
        DialogInterface.OnClickListener onClickSi = new DialogInterface.OnClickListener() {

            /**
             * Método llamado al pulsar el botón SI.
             *
             * @param dialog Diálogo
             * @param which  Botón pulsado
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Señalar en las preferencias que se ha de
                // reinicializar la base de datos.
                Preferencias.setReset(PrincipalActivity.this, true);

                // Cuadro de diálogo, para indicar al usuario
                // que el juego ha sido reinicializado.
                Mensaje.continuar(PrincipalActivity.this,
                        R.drawable.ic_done,
                        getString(R.string.dialogo_reset_titulo),
                        getString(R.string.dialogo_reset_hecho),
                        null);
            }
        };

        // Cuadro de diálogo, para preguntar al usuario
        // si quiere proceder con el reset o no.
        Mensaje.preguntarSiCancelar(this,
                R.drawable.ic_question,
                getString(R.string.dialogo_reset_titulo),
                getString(R.string.dialogo_reset_texto),
                onClickSi,
                null);
    }

    /**
     * Método llamado al crear la activity.
     *
     * @param savedInstanceState  Estado previamente guardado
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Llamar a la superclase
        super.onCreate(savedInstanceState);

        // Averiguar si el dispositivo es una tablet
        // con orientación horizontal.
        esTabletHorizontal = getResources().getBoolean(R.bool.isTablet) && !Preferencias.getVertical(this);

        // Cambiar la orientación de la pantalla y poner el layout. Esto puede ocasionar que la
        // activity se reinicie, si la orientación actual no es la solicitada.
        if(esTabletHorizontal) {

            // Tablet con orientación horizontal

            if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            // Poner layout, dependiendo de la preferencia diestro / zurdo.
            setContentView(Preferencias.getZurdo(this) ? R.layout.activity_principal_tablet_zurdo : R.layout.activity_principal_tablet);
        } else {

            // Móvil, o tablet con orientación vertical

            if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            // Poner layout
            setContentView(R.layout.activity_principal);
        }

        // Poner los fragments iniciales

        // Tomar referencia del FragmentManager
        FragmentManager fm = getFragmentManager();

        // Si es la 1ª vez que se ejecuta la activity...
        if(savedInstanceState == null) {

            // Log
            //Log.d(TAG, "onCreate - primera vez");

            // Comenzar transacción
            FragmentTransaction ft = fm.beginTransaction();

            // Añadir el fragment del menú
            ft.add(R.id.frameLayoutMenu, getMenuFragment(), TAG_FRAG_MENU);

            // Si es una tablet con orientación horizontal, añadir
            // el fragment inicial de bienvenida.
            if (esTabletHorizontal) {
                ft.add(R.id.frameLayoutArea, getBienvenidaFragment(), TAG_FRAG_BIENVENIDA);
            }

            // Activar los cambios
            ft.commit();
        } else {

            // Restaurando el estado de la activity

            // Log
            //Log.d(TAG, "onCreate - restaurar");

            // Tomar referencias de los fragments
            menuFragment = (MenuFragment) fm.findFragmentByTag(TAG_FRAG_MENU);
            bienvenidaFragment = (BienvenidaFragment) fm.findFragmentByTag(TAG_FRAG_BIENVENIDA);
            preferenciasFragment = (PreferenciasFragment) fm.findFragmentByTag(TAG_FRAG_PREFERENCIAS);
            infoFragment = (InfoFragment) fm.findFragmentByTag(TAG_FRAG_INFO);

            // Log
            //Log.d(TAG, "menuFragment = " + menuFragment);
            //Log.d(TAG, "bienvenidaFragment = " + bienvenidaFragment);
            //Log.d(TAG, "preferenciasFragment = " + preferenciasFragment);
            //Log.d(TAG, "infoFragment = " + infoFragment);
        }

        // Si se está restaurando la activity debido a un cambio
        // de orientación...
        if(getIntent().getBooleanExtra(EXTRA_VERTICAL, false)) {

            // Quitar el extra
            getIntent().removeExtra(EXTRA_VERTICAL);

            // Forzar la activación de la opción de preferencias
            menuFragment.forzarOpcion(MenuFragment.MENU_OPCION_PREFERENCIAS);
        }
    }

    /**
     * Reemplazar el fragment del area (tablets con
     * orientación horizontal).
     *
     * @param fragment  Fragment
     * @param tag       Tag
     */
    private void ponFragmentArea(Fragment fragment, String tag) {

        // Iniciar transacción
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        // Reemplazar fragment actual, por el nuevo
        ft.replace(R.id.frameLayoutArea, fragment, tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        // Activar los cambios
        ft.commit();
    }

    /**
     * Reemplazar el fragment del menú (móvil, y tablets
     * con orientación vertical).
     *
     * @param fragment  Fragment
     * @param tag       Tag
     */
    private void ponFragmentMovil(Fragment fragment, String tag) {

        // Iniciar transacción
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        // Reemplazar el fragment actual, por el nuevo
        ft.replace(R.id.frameLayoutMenu, fragment, tag);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

        // Ponerlo en el BackStack, para que se pueda volver
        // a la disposición anterior.
        ft.addToBackStack(null);

        // Activar los cambios
        ft.commit();
    }

    /**
     * Devolver fragment de preferencias, creando uno nuevo
     * si fuera necesario.
     *
     * @return  fragment
     */
    private PreferenciasFragment getPreferenciasFragment() {

        return (preferenciasFragment = (preferenciasFragment != null ? preferenciasFragment : new PreferenciasFragment()));
    }

    /**
     * Devolver fragment de información, creando uno nuevo
     * si fuera necesario.
     *
     * @return  fragment
     */
    private InfoFragment getInfoFragment() {

        return (infoFragment = (infoFragment != null ? infoFragment : new InfoFragment()));
    }

    /**
     * Devolver fragment del menú, creando uno nuevo
     * si fuera necesario.
     *
     * @return  fragment
     */
    private MenuFragment getMenuFragment() {

        return (menuFragment = (menuFragment != null ? menuFragment : new MenuFragment()));
    }

    /**
     * Devolver fragment de bienvenida, creando uno nuevo
     * si fuera necesario.
     *
     * @return  fragment
     */
    private BienvenidaFragment getBienvenidaFragment() {

        return (bienvenidaFragment = (bienvenidaFragment != null ? bienvenidaFragment : new BienvenidaFragment()));
    }

    /**
     * Clase interna para iniciar el juego, reinicializando
     * la base de datos si es necesario.
     */
    private class Jugar extends AsyncTask<Void, Void, Void> {

        // True si se ha de reinicializar la base de datos
        boolean reset;

        // ProgressDialog a mostrar durante la reinicialización
        // de la base de datos.
        ProgressDialog progressDialog;

        /**
         * Tarea a ejecutar en el UI, antes de doInBackground().
         */
        @Override
        protected void onPreExecute() {

            if(esTabletHorizontal) {

                // Tablet con orientación horizontal

                // Poner primero el fragment de bienvenida en el área,
                // para que se pueda volver a él.
                ponFragmentArea(getBienvenidaFragment(), TAG_FRAG_BIENVENIDA);
            }

            // ¿Se ha de reinicializar el juego?
            reset = Preferencias.getReset(PrincipalActivity.this);

            // Si se ha de reinicializar el juego...
            if(reset) {

                // La próxima vez no se reinicializará
                Preferencias.setReset(PrincipalActivity.this, false);

                // Mostrar ProgressDialog durante el proceso de
                // reinicializado.
                progressDialog = new ProgressDialog(PrincipalActivity.this);

                // Configurar el ProgressDialog
                progressDialog.setIcon(R.drawable.ic_wait);
                progressDialog.setTitle(R.string.dialogo_carga_titulo);
                progressDialog.setMessage(getString(R.string.dialogo_carga_texto));
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);

                // Mostrar el ProgressDialog
                progressDialog.show();
            }
        }

        /**
         * Tarea a realizar en background, fuera del thread del UI. Reinicializa
         * la base de datos si es necesario.
         *
         * @param params  nada
         * @return  nada
         */
        @Override
        protected Void doInBackground(Void... params) {

            // Si se ha de reinicializar el juego...
            if(reset) {
                // Abrir base de datos
                BaseDatos bd = new BaseDatos(PrincipalActivity.this, true);

                // Cerrar base de datos
                bd.cerrar();
            }

            // Retornar
            return null;
        }

        /**
         * Tarea a realizar en el thread del UI, una vez finalizada la tarea
         * en background. Pone cada fragment en su sitio, e inicia el juego.
         */
        @Override
        protected void onPostExecute(Void param) {

            // Quitar el ProgressDialog de reinicialización de la
            // base de datos, si está activo.
            if(progressDialog != null) {
                progressDialog.dismiss();
            }

            // Lanzar la activity del juego
            startActivity(new Intent(PrincipalActivity.this, JuegoActivity.class));
        }
    }
}
