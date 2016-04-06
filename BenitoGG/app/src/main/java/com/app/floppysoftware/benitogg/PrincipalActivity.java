package com.app.floppysoftware.benitogg;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class PrincipalActivity extends Activity implements
        MenuFragment.OnFragmentInteractionListener,
        AccionesFragment.OnActionSelectedListener,
        AhoraFragment.AhoraFragmentInteractionListener,
        OpcionesFragment.OnOpcionesInteractionListener {

    //
    private final static String TAG = "PrincipalActivity";

    //
    private final static String EXTRA_VERTICAL = "ExtraVertical";

    //
    private final static String TAG_FRAG_MENU = "FrMenu";
    private final static String TAG_FRAG_BIENVENIDA = "FrBienvenida";
    private final static String TAG_FRAG_OPCIONES = "FrOpciones";
    private final static String TAG_FRAG_INFO = "FrInfo";
    private final static String TAG_FRAG_AHORA = "FrAhora";
    private final static String TAG_FRAG_ACCIONES = "FrAcciones";
    private final static String TAG_FRAG_CASOS = "FrCasos";

    // Fragment para el menú de opciones
    private MenuFragment menuFragment;

    //
    private BienvenidaFragment bienvenidaFragment;
    private OpcionesFragment opcionesFragment;
    private InfoFragment infoFragment;
    private AhoraFragment ahoraFragment;
    private AccionesFragment accionesFragment;
    private CasosFragment casosFragment;

    // Variable que indica si el dispositivo es una tablet
    private boolean esTablet = false;

    // Gestion del sonido
    private AudioManager audioManager;
    private SoundPool soundPool;
    private boolean puedoSonar = false;
    private boolean sonidoCargado = false;
    private int sonidoId;

    // Último Alert Dialog activo (utilizado en depuración)
    private AlertDialog alertDialog;


    /****************************************************/

    public void setAcciones(boolean norte, boolean sur, boolean este, boolean oeste,
                            ArrayList<String> objetosLugar,
                            ArrayList<String> objetosBolsillo,
                            ArrayList<String> otrasAcciones) {

        // Si es una tablet, el fragment de acciones está visible. Si es un móvil, no.

        if(!esTablet) {

            ponFragmentMovil(getAccionesFragment(), TAG_FRAG_ACCIONES);
        }

        // Enviar datos al fragment de acciones
        accionesFragment.setAcciones(norte, sur, este, oeste, objetosLugar, objetosBolsillo, otrasAcciones);
    }

    public void emiteSonido(int resId) {

        emiteUnSonido(resId);
    }




    /**************************************************/


    public void onActionSelected(int actionType, int actionNumber) {

        // Si es móvil, hay que quitar el fragment de acciones,
        // y poner el anterior.
        if(!esTablet) {
            //
            getFragmentManager().popBackStack();
        }

        //
        switch(actionType) {
            case AccionesFragment.ACTION_TYPE_MAP :
                 startActivity(new Intent(this, MapaActivity.class));
                break;
            case AccionesFragment.ACTION_TYPE_CASOS :
                if(esTablet) {
                    startActivity(new Intent(this, CasosActivity.class));
                } else {
                    ponFragmentMovil(getCasosFragment(), TAG_FRAG_CASOS);
                }
                break;
            default :
                ahoraFragment.realizaAccion(actionType, actionNumber);
                break;
        }
    }

    /**
     *
     * @param opcion
     */
    public void onOptionSelected(int opcion) {
/*****************
        if(esTablet) {
            // Habilitar todas las opciones del menú
            menuFragment.enableOpcion(MenuFragment.MENU_OPCION_JUGAR, true);
            menuFragment.enableOpcion(MenuFragment.MENU_OPCION_OPCIONES, true);
            menuFragment.enableOpcion(MenuFragment.MENU_OPCION_INFO, true);

            // Desactivar la opción seleccionada
            menuFragment.enableOpcion(opcion, false);
        }
*************/
        //


        switch(opcion) {

            case MenuFragment.MENU_OPCION_JUGAR :

                //
                if(esTablet) {

                    //
                    ponFragmentArea(getBienvenidaFragment(), TAG_FRAG_BIENVENIDA);

                    //
                    FragmentManager fm = getFragmentManager();

                    //
                    FragmentTransaction ft = fm.beginTransaction();

                    //
                    ft.replace(R.id.frameLayoutArea, getAhoraFragment(), TAG_FRAG_AHORA);
                    //
                    ft.replace(R.id.frameLayoutMenu, getAccionesFragment(), TAG_FRAG_MENU);

                    //
                    ft.addToBackStack(null);

                    //
                    ft.commit();
                } else {

                    ponFragmentMovil(getAhoraFragment(), TAG_FRAG_AHORA);
                }

                break;
            case MenuFragment.MENU_OPCION_OPCIONES :

                //
                if(esTablet) {
                    ponFragmentArea(getOpcionesFragment(), TAG_FRAG_OPCIONES);
                } else {
                    ponFragmentMovil(getOpcionesFragment(), TAG_FRAG_OPCIONES);
                }

                break;
            case MenuFragment.MENU_OPCION_INFO :
                //
                if(esTablet) {
                    ponFragmentArea(getInfoFragment(), TAG_FRAG_INFO);
                } else {
                    ponFragmentMovil(getInfoFragment(), TAG_FRAG_INFO);
                }
                break;
            default :
                break;
        }
    }

    /*************************************/

    public void onCambioZurdo() {

        recreate();  // >= API 11

        //Intent intent = getIntent();
        //finish();
        //startActivity(intent);
    }

    public void onCambioVertical() {

        Intent intent = getIntent();
        intent.putExtra(EXTRA_VERTICAL, true); // FIXME?? duplica el extra?
        finish();
        startActivity(intent);

    }

    public void onCambioSonido() {

        if(Preferencias.getSonido(this)) {
            holaSonido();
        } else {
            adiosSonido();
        }
    }

    public void onResetJuego() {

        //
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);

        //
        alertDialog = dlg.setTitle(R.string.dialogo_reset_titulo)
                .setIcon(R.drawable.ic_question)
                .setMessage(R.string.dialogo_reset_texto)
                .setPositiveButton(R.string.dialogo_si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dejar la BD al estado inicial
                        //new ReinicializaJuego().execute();
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected Void doInBackground(Void... params) {
                                // Dejar la BD al estado inicial
                                BaseDatos bd = new BaseDatos(PrincipalActivity.this, true);
                                bd.cerrar();
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void result) {
                                AlertDialog.Builder dlg = new AlertDialog.Builder(PrincipalActivity.this);

                                alertDialog = dlg.setTitle(R.string.dialogo_reset_titulo)
                                        .setIcon(R.drawable.ic_information)
                                        .setMessage(R.string.dialogo_reset_hecho)
                                        .setPositiveButton(R.string.dialogo_continuar, null)
                                        .show();
                            }
                        }.execute();

                    }
                })
                .setNegativeButton(R.string.dialogo_cancelar, null)
                .show();
    }


    /*****************************************/

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //
        super.onCreate(savedInstanceState);

        // Averiguar si el dispositivo es una tablet o móvil
        esTablet = getResources().getBoolean(R.bool.isTablet) && !Preferencias.getVertical(this);

        // Cambiar la orientación de la pantalla. Esto puede ocasionar que la
        // activity se reinicie, si la orientación actual no es la solicitada.
        if(esTablet) {
            if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }

            setContentView(Preferencias.getZurdo(this) ? R.layout.activity_principal_tablet_zurdo : R.layout.activity_principal_tablet);
        } else {

            if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            setContentView(R.layout.activity_principal);
        }

        FragmentManager fm = getFragmentManager();

        if(savedInstanceState == null) {

            //
            Log.i(TAG, "onCreate - 1st run");

            //
            FragmentTransaction ft = fm.beginTransaction();

            //
            ft.add(R.id.frameLayoutMenu, getMenuFragment(), TAG_FRAG_MENU);

            //
            if (esTablet) {

                ft.add(R.id.frameLayoutArea, getBienvenidaFragment(), TAG_FRAG_BIENVENIDA);
            }

            //
            ft.commit();
        } else {

            //
            Log.i(TAG, "onCreate - Restoring");

            menuFragment = (MenuFragment) fm.findFragmentByTag(TAG_FRAG_MENU);
            bienvenidaFragment = (BienvenidaFragment) fm.findFragmentByTag(TAG_FRAG_BIENVENIDA);
            opcionesFragment = (OpcionesFragment) fm.findFragmentByTag(TAG_FRAG_OPCIONES);
            infoFragment = (InfoFragment) fm.findFragmentByTag(TAG_FRAG_INFO);
            ahoraFragment = (AhoraFragment) fm.findFragmentByTag(TAG_FRAG_AHORA);
            accionesFragment = (AccionesFragment) fm.findFragmentByTag(TAG_FRAG_ACCIONES);
            casosFragment = (CasosFragment) fm.findFragmentByTag(TAG_FRAG_CASOS);

            //
            Log.i(TAG, "menuFragment = " + menuFragment);
            Log.i(TAG, "bienvenidaFragment = " + bienvenidaFragment);
            Log.i(TAG, "opcionesFragment = " + opcionesFragment);
            Log.i(TAG, "infoFragment = " + infoFragment);
            Log.i(TAG, "ahoraFragment = " + ahoraFragment);
            Log.i(TAG, "accionesFragment = " + accionesFragment);
            Log.i(TAG, "casosFragment = " + casosFragment);


        }

        //
        if(getIntent().getBooleanExtra(EXTRA_VERTICAL, false)) {

            getIntent().removeExtra(EXTRA_VERTICAL);

            //onOptionSelected(MenuFragment.MENU_OPCION_OPCIONES);

            //if(esTablet) {
                //menuFragment.enableOpcion(MenuFragment.MENU_OPCION_OPCIONES, false);
            //}

            menuFragment.forceOpcion(MenuFragment.MENU_OPCION_OPCIONES);
        }
    }

    /**
     * Método llamado tras onCreate() y tras onStop() > onRestart().
     */
    @Override
    protected void onStart() {

        // Llamar a la superclase
        super.onStart();

        // Inicializar gestión del sonido
        if(Preferencias.getSonido(this)) {
            holaSonido();
        }

        // Log
        Log.i(TAG, "onStart()");
    }

    /**
     * Método llamado tras onPause() y onStart().
     */
    @Override
    protected void onResume() {

        // Llamar a la superclase
        super.onResume();

        // Log
        Log.i(TAG, "onResume()");

        // Reanudar sonido
        if(puedoSonar) {
            reanudarSonido();
        }
    }

    /**
     * Método llamado al pausar la activity.
     */
    @Override
    protected void onPause() {

        // Llamar a la superclase
        super.onPause();

        // Log
        Log.i(TAG, "onPause()");

        // Pausar sonido
        if(puedoSonar) {
            pausarSonido();
        }
    }

    /**
     * Método llamado al detener la activity.
     */
    @Override
    protected void onStop() {

        // Llamar a la superclase
        super.onStop();

        // Log
        Log.i(TAG, "onStop()");

        // Finalizar gestión del sonido
        if(puedoSonar) {
            adiosSonido();
        }
    }

    private void ponFragmentArea(Fragment fragment, String tag) {

        //
        FragmentManager fm = getFragmentManager();

        //
        FragmentTransaction ft = fm.beginTransaction();

        //
        ft.replace(R.id.frameLayoutArea, fragment, tag);

        //
        ft.commit();
    }


    private void ponFragmentMovil(Fragment fragment, String tag) {

        //
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        //

        ft.replace(R.id.frameLayoutMenu, fragment, tag);
        ft.addToBackStack(null);

        //
        ft.commit();
    }

    private AccionesFragment getAccionesFragment() {

        if(accionesFragment == null) {
            accionesFragment = new AccionesFragment();
        }

        return accionesFragment;
    }

    private AhoraFragment getAhoraFragment() {

        if(ahoraFragment == null) {
            ahoraFragment = new AhoraFragment();
        }

        return ahoraFragment;
    }

    private OpcionesFragment getOpcionesFragment() {

        if(opcionesFragment == null) {
            opcionesFragment = new OpcionesFragment();
        }

        return opcionesFragment;
    }

    private InfoFragment getInfoFragment() {

        if(infoFragment == null) {
            infoFragment = new InfoFragment();
        }

        return infoFragment;
    }

    private MenuFragment getMenuFragment() {

        if(menuFragment == null) {
            menuFragment = new MenuFragment();
        }

        return menuFragment;
    }

    private BienvenidaFragment getBienvenidaFragment() {

        if(bienvenidaFragment == null) {
            bienvenidaFragment = new BienvenidaFragment();
        }

        return bienvenidaFragment;
    }

    private CasosFragment getCasosFragment() {
        if(casosFragment == null) {
            casosFragment = new CasosFragment();
        }

        return casosFragment;
    }


    /*********************
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id) {
            case R.id.action_reset :
                // Dejar la BD al estado inicial
                BaseDatos bd = new BaseDatos(this, true);
                bd.cerrar();
                return true;
            case R.id.action_settings :
                Intent intent = new Intent(this, PreferenciasActivity.class);
                //intent.putExtra(EscenaActivity.EXTRA_IN_RESET, false);
                // Con éste método, la activity llamada, no nos puede
                // devolver datos:
                startActivity(intent);
                return true;
            default :
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    ***********/

    private void emiteUnSonido(int resId) {
        if(puedoSonar) {
            //soundPool.play(soundId, 1.0f, 1.0f, 0, 0, 1.0f);

            if(sonidoCargado) {

                soundPool.unload(sonidoId);

                sonidoCargado = false;
            }

            sonidoId = soundPool.load(this, resId, 1);

        }
    }

        /**
     * Inicializar la gestión del sonido
     * y cargar los recursos necesarios.
     */
    private void holaSonido() {

        // Por defecto, no podemos utilizar el sonido
        puedoSonar = false;

        // Capturar servicio de audio
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        //
        int result = audioManager.requestAudioFocus(audioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        // Fijar resultado
        puedoSonar = (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Creamos el manejador del sonido
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        //
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

                if(sampleId == sonidoId && status == 0) {

                    soundPool.play(sampleId, 1.0f, 1.0f, 0, 0, 1.0f);

                    sonidoCargado = true;
                }
            }
        });
    }

    /**
     * Finalizar la gestión del sonido, parar la emisión de música
     * y descargar los recursos.
     */
    private void adiosSonido() {

        //
        if(soundPool != null) {
            // Descargar las canciones
            // soundPool.unload(soundId);

            soundPool.release();

            soundPool = null;
        }

        // AudioPlayer. No hacer nada, si no fue inicializado.
        if(audioManager != null) {

            // Abandonar el foco de audio
            audioManager.abandonAudioFocus(audioFocusChangeListener);

            // Inhabilitar AudioManager
            audioManager = null;
        }

        // Liberar el teclado del dispositivo del control de volumen
        setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);

        // Inhabilitar sonido
        puedoSonar = false;
    }

    /**
     * Pausar la música.
     */
    private void pausarSonido() {

        soundPool.autoPause();
    }

    /**
     * Reanudar / comenzar la música.
     */
    private void reanudarSonido() {

        soundPool.autoResume();
    }



    /**
     * Lístener para el foco de audio.
     */
    AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {

        /**
         * Método llamado cuando cambia el foco de audio.
         *
         * @param focusChange   tipo de cambio de foco de audio
         */
        public void onAudioFocusChange(int focusChange) {

            // Actuar según el tipo de cambio de foco de audio
            switch(focusChange) {

                // Pérdida de foco de audio
                case AudioManager.AUDIOFOCUS_LOSS :
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT :

                    // Pausar sonido
                    pausarSonido();
                    break;

                // Ganancia de foco de audio
                case AudioManager.AUDIOFOCUS_GAIN :
                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT :

                    // Reanudar sonido
                    reanudarSonido();
                    break;
            }
        }
    };


    public AlertDialog getAlertDialog() {
        return alertDialog;
    }

}
