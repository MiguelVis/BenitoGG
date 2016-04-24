package com.app.floppysoftware.benitogg;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

/**
 * Clase principal del juego. Implementa el menú, así como
 * el juego.
 *
 * @author   Miguel I. García López
 * @version  1.1
 * @since    08 Mar 2016
 */
public class PrincipalActivity extends Activity implements
        MenuFragment.OnMenuListener,
        AccionesFragment.OnAccionesListener,
        EscenaFragment.OnEscenaListener,
        OpcionesFragment.OnOpcionesListener {

    // Tag para el log
    private final static String TAG = "Principal";

    // Parámetro extra para indicar que la activity se está
    // recreando, debido a un cambio de orientación.
    private final static String EXTRA_VERTICAL = "ExtraVertical";

    // Tags para los fragments
    private final static String TAG_FRAG_MENU = "FrMenu";
    private final static String TAG_FRAG_BIENVENIDA = "FrBienvenida";
    private final static String TAG_FRAG_OPCIONES = "FrOpciones";
    private final static String TAG_FRAG_INFO = "FrInfo";
    private final static String TAG_FRAG_ESCENA = "FrEscena";
    private final static String TAG_FRAG_ACCIONES = "FrAcciones";

    // Nº máximo de sonidos que pueden estar activos
    // simultáneamente.
    private final static int MAX_SONIDOS = 3;

    // Fragments
    private MenuFragment menuFragment;
    private BienvenidaFragment bienvenidaFragment;
    private OpcionesFragment opcionesFragment;
    private InfoFragment infoFragment;
    private EscenaFragment escenaFragment;
    private AccionesFragment accionesFragment;

    // Variable que indica si el dispositivo es una tablet,
    // con orientación horizontal.
    private boolean esTabletHorizontal = false;

    // Gestion del sonido
    private AudioManager audioManager;             // AudioManager
    private SoundPool soundPool;                   // SoundPool
    private boolean puedoSonar = false;            // True si se puede emitir sonidos, false en caso contrario
    private int[] sonidos = new int[MAX_SONIDOS];  // Array para los sonidos
    private int indiceSonido;                      // Índice actual en el array

    // ----------------------------------------------------
    // Implementación de la interfaz del fragment de escena
    // ----------------------------------------------------

    /**
     * Actualizar las acciones disponibles.
     *
     * @param norte            True si hay salida al norte, false en caso contrario
     * @param sur              True si hay salida al sur, false en caso contrario
     * @param este             True si hay salida al este, false en caso contrario
     * @param oeste            True si hay salida al oeste, false en caso contrario
     * @param objetosLugar     Objetos que hay en el lugar
     * @param objetosBolsillo  Objetos que lleva el protagonista
     * @param otrasAcciones    Otras acciones disponibles
     */
    public void setAcciones(boolean norte, boolean sur, boolean este, boolean oeste,
                            ArrayList<Objeto> objetosLugar,
                            ArrayList<Objeto> objetosBolsillo,
                            ArrayList<Accion> otrasAcciones) {

        // Si es una tablet con orientación horizontal, el fragment de acciones está visible;
        // si es un móvil, no.
        if(!esTabletHorizontal) {

            // Móvil, o tablet con orientación vertical: poner el fragment
            ponFragmentMovil(getAccionesFragment(), TAG_FRAG_ACCIONES);
        }

        // Enviar datos al fragment de acciones
        accionesFragment.setAcciones(norte, sur, este, oeste, objetosLugar, objetosBolsillo, otrasAcciones);
    }

    /**
     * Emitir un sonido. Este método también está definido en
     * la interfaz del fragment de acciones, por lo que está
     * compartido.
     *
     * @param resId   Id del recurso de sonido
     */
    public void emiteSonido(int resId) {

        // Si se puede emitir el sonido...
        if(puedoSonar) {

            // Si el array de sonidos está al completo,
            // inicializar el índice al primero.
            if(indiceSonido >= MAX_SONIDOS) {
                indiceSonido = 0;
            }

            // Descargar sonido correspondiente
            // al índice.
            if(sonidos[indiceSonido] != -1) {
                soundPool.unload(sonidos[indiceSonido]);
            }

            // Cargar el sonido
            sonidos[indiceSonido] = soundPool.load(this, resId, 1);

            // Actualizar el índice
            ++indiceSonido;
        }
    }

    // ------------------------------------------------------
    // Implementación de la interfaz del fragment de acciones
    // ------------------------------------------------------

    /**
     * Ejecutar la acción seleccionada.
     *
     * @param accionId  Id de la acción
     * @param param     Parámetro auxiliar
     */
    public void onAccionSeleccionada(int accionId, int param) {

        // Ejecutar la acción correspondiente
        switch(accionId) {
            case AccionesFragment.ACCION_MAPA:
                // Mostrar mapa
                startActivity(new Intent(this, MapaActivity.class));
                break;
            case AccionesFragment.ACCION_CASOS:
                // Mostrar lista de casos
                startActivity(new Intent(this, CasosActivity.class));
                break;
            default:
                // Si el dispositivo no es una tablet en modo horizontal,
                // hay que quitar el fragment de acciones,
                // y poner el anterior.
                if (!esTabletHorizontal) {
                    // Quitar el fragment actual (acciones),
                    // y volver al anterior.
                    getFragmentManager().popBackStack();
                }

                // Realizar acción
                escenaFragment.realizaAccion(accionId, param);
                break;
        }
    }

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

                // Actuar según sea tablet con orientación horizontal,
                // o no.
                if(esTabletHorizontal) {

                    // Tablet con orientación horizontal

                    // Poner primero el fragment de bienvenida en el área,
                    // para que se pueda volver a él.
                    ponFragmentArea(getBienvenidaFragment(), TAG_FRAG_BIENVENIDA);

                    // Comenzar transacción
                    FragmentTransaction ft = getFragmentManager().beginTransaction();

                    // Reemplazar el fragment actual en el área, por el de la escena
                    ft.replace(R.id.frameLayoutArea, getEscenaFragment(), TAG_FRAG_ESCENA);

                    // Reemplazar el fragment actual en el menú, por el de las acciones
                    ft.replace(R.id.frameLayoutMenu, getAccionesFragment(), TAG_FRAG_MENU);

                    // Añadirlo al BackStack, para que se pueda restaurar
                    // al estado previo (fragment de bienvenida + fragment de menú).
                    ft.addToBackStack(null);

                    // Activar los cambios
                    ft.commit();
                } else {

                    // Móvil, o tablet con orientación vertical

                    // Poner el fragment de la escena
                    ponFragmentMovil(getEscenaFragment(), TAG_FRAG_ESCENA);
                }
                break;
            case MenuFragment.MENU_OPCION_OPCIONES :

                // Actuar según sea tablet con orientación horizontal,
                // o no.
                if(esTabletHorizontal) {

                    // Tablet con orientación horizontal
                    ponFragmentArea(getOpcionesFragment(), TAG_FRAG_OPCIONES);
                } else {

                    // Móvil, o tablet con orientación vertical
                    ponFragmentMovil(getOpcionesFragment(), TAG_FRAG_OPCIONES);
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

    // ------------------------------------------------------
    // Implementación de la interfaz del fragment de opciones
    // ------------------------------------------------------

    /**
     * Cambiar al modo diestro o zurdo.
     */
    public void onCambioZurdo() {

        // Recrear la activity; de esta forma
        // se pondrá cada fragment (opciones y menú) en su lugar.
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

        // Activar o desactivar el sonido,
        // según la preferencia actual.
        if(Preferencias.getSonido(this)) {

            // Activar sonido
            holaSonido();
        } else {

            // Desactivar sonido
            adiosSonido();
        }
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
                        R.drawable.ic_information,
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
            Log.d(TAG, "onCreate - primera vez");

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
            Log.d(TAG, "onCreate - restaurar");

            // Tomar referencias de los fragments
            menuFragment = (MenuFragment) fm.findFragmentByTag(TAG_FRAG_MENU);
            bienvenidaFragment = (BienvenidaFragment) fm.findFragmentByTag(TAG_FRAG_BIENVENIDA);
            opcionesFragment = (OpcionesFragment) fm.findFragmentByTag(TAG_FRAG_OPCIONES);
            infoFragment = (InfoFragment) fm.findFragmentByTag(TAG_FRAG_INFO);
            escenaFragment = (EscenaFragment) fm.findFragmentByTag(TAG_FRAG_ESCENA);
            accionesFragment = (AccionesFragment) fm.findFragmentByTag(TAG_FRAG_ACCIONES);

            // Log
            Log.d(TAG, "menuFragment = " + menuFragment);
            Log.d(TAG, "bienvenidaFragment = " + bienvenidaFragment);
            Log.d(TAG, "opcionesFragment = " + opcionesFragment);
            Log.d(TAG, "infoFragment = " + infoFragment);
            Log.d(TAG, "escenaFragment = " + escenaFragment);
            Log.d(TAG, "accionesFragment = " + accionesFragment);
        }

        // Si se está restaurando la activity debido a un cambio
        // de orientación...
        if(getIntent().getBooleanExtra(EXTRA_VERTICAL, false)) {

            // Quitar el extra
            getIntent().removeExtra(EXTRA_VERTICAL);

            // Forzar la activación de las opciones
            menuFragment.forzarOpcion(MenuFragment.MENU_OPCION_OPCIONES);
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
        Log.d(TAG, "onStart()");
    }

    /**
     * Método llamado tras onPause() y onStart().
     */
    @Override
    protected void onResume() {

        // Llamar a la superclase
        super.onResume();

        // Log
        Log.d(TAG, "onResume()");

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
        Log.d(TAG, "onPause()");

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
        Log.d(TAG, "onStop()");

        // Finalizar gestión del sonido
        if(puedoSonar) {
            adiosSonido();
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

        // Ponerlo en el BackStack, para que se pueda volver
        // a la disposición anterior.
        ft.addToBackStack(null);

        // Activar los cambios
        ft.commit();
    }

    /**
     * Devolver fragment de acciones, creando uno nuevo
     * si fuera necesario.
     *
     * @return  fragment
     */
    private AccionesFragment getAccionesFragment() {

        return (accionesFragment = (accionesFragment != null ? accionesFragment : new AccionesFragment()));
    }

    /**
     * Devolver fragment de la escena, creando uno nuevo
     * si fuera necesario.
     *
     * @return  fragment
     */
    private EscenaFragment getEscenaFragment() {

        return (escenaFragment = (escenaFragment != null ? escenaFragment : new EscenaFragment()));
    }

    /**
     * Devolver fragment de opciones, creando uno nuevo
     * si fuera necesario.
     *
     * @return  fragment
     */
    private OpcionesFragment getOpcionesFragment() {

        return (opcionesFragment = (opcionesFragment != null ? opcionesFragment : new OpcionesFragment()));
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

    // ------------------
    // Gestión del sonido
    // ------------------

    /**
     * Inicializar la gestión del sonido
     * y cargar los recursos necesarios.
     */
    private void holaSonido() {

        // Por defecto, no se puede utilizar el sonido
        puedoSonar = false;

        // No hay sonidos cargados en el array
        indiceSonido = 0;

        // Inicializar array de sonidos
        for(int i = 0; i < MAX_SONIDOS; ++i) {
            sonidos[i] = -1;
        }

        // Capturar el servicio de audio
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Solicitar el foco de audio
        int result = audioManager.requestAudioFocus(audioFocusChangeListener,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

        // Tomar resultado
        puedoSonar = (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED);

        // Listener para gestionar la carga de sonidos
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // Crear el manejador del sonido -- nota: deprecated en API 21, pero
        // la app ha de correr también en dispositivos con APIs anteriores.
        soundPool = new SoundPool(MAX_SONIDOS, AudioManager.STREAM_MUSIC, 0);

        // Listener para gestionar la carga de sonidos
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {

            /**
             * Método que será llamado cuando un sonido sea cargado.
             *
             * @param soundPool  SoundPool
             * @param sampleId   Id del sonido
             * @param status     Estado
             */
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

                // ¡Que empiece la música!
                if(sampleId == sonidos[indiceSonido - 1] && status == 0) {
                    soundPool.play(sampleId, 1.0f, 1.0f, 0, 0, 1.0f);
                }
            }
        });
    }

    /**
     * Finalizar la gestión del sonido, parar la emisión de música
     * y descargar los recursos.
     */
    private void adiosSonido() {

        // Proceder con el SoundPool
        if(soundPool != null) {

            // Liberar recursos del SoundPool
            soundPool.release();

            // Inhabilitar la referencia del SoundPool
            soundPool = null;
        }

        // Proceder con el AudioManager
        if(audioManager != null) {

            // Abandonar el foco de audio
            audioManager.abandonAudioFocus(audioFocusChangeListener);

            // Inhabilitar referencia del AudioManager
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
}
