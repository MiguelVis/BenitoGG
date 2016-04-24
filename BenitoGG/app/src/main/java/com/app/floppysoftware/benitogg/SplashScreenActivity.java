package com.app.floppysoftware.benitogg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

/**
 * Clase que implementa la pantalla de inicio (Splash Screen).
 *
 * @author   Miguel I. García López
 * @version  1.0
 * @since    08 Mar 2016
 */
public class SplashScreenActivity extends Activity {

    // Tag para log
    private final static String TAG = "SplashScreenActivity";

    // Valores
    private final static int NUM_PASOS = 9;      // 6 pasos, más 3
    private final static long MILIS_PASO = 500;  // Milisegundos entre pasos

    // Timer
    private CountDownTimer timer;

    // Número de pasos dados
    private int pasosDados = 0;

    // Gestion del sonido
    private AudioManager audioManager;
    private SoundPool soundPool;
    private boolean puedoSonar = false;
    private boolean sonidoCargado = false;
    private int sonidoId;

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
        // y la preferencia es orientación horizontal.
        boolean esTabletHorizontal = getResources().getBoolean(R.bool.isTablet) && !Preferencias.getVertical(this);

        // Cambiar la orientación de la pantalla; esto puede ocasionar un
        // reinicio de la activity.
        if(esTabletHorizontal) {
            if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else {
            if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }

        // Fijar layout
        setContentView(R.layout.activity_splash_screen);
    }

    /**
     * Método llamado al pausar la activity.
     */
    @Override
    protected void onPause() {

        // Llamar a la superclase
        super.onPause();

        // Cancelar timer
        timer.cancel();

        // Pausar sonido
        if(puedoSonar) {
            pausarSonido();
        }//
    }

    /**
     * Método llamado al reanudar la activity.
     */
    @Override
    protected void onResume() {

        // Llamar a la superclase
        super.onResume();

        // Reanudar sonido
        if(puedoSonar) {
            reanudarSonido();
        }

        // Timer para que se muestre durante cierto tiempo la Splash Screen
        timer = new CountDownTimer(MILIS_PASO * (NUM_PASOS - pasosDados), MILIS_PASO) {

            /**
             * Método llamado en cada paso.
             *
             * @param millisUntilFinished  Tiempo restante
             */
            @Override
            public void onTick(long millisUntilFinished) {

                // Log
                Log.d(TAG, "Paso = " + pasosDados);

                // Mostrar imagen correspondiente al
                // número de paso.
                switch(pasosDados++) {
                    case 2 :
                        ((ImageView) findViewById(R.id.imageViewPie1)).setImageResource(R.drawable.splash_pie_izquierdo);
                        emiteSonido();
                        break;
                    case 3 :
                        ((ImageView) findViewById(R.id.imageViewPie2)).setImageResource(R.drawable.splash_pie_derecho);
                        emiteSonido();
                        break;
                    case 4 :
                        ((ImageView) findViewById(R.id.imageViewPie3)).setImageResource(R.drawable.splash_pie_izquierdo);
                        emiteSonido();
                        break;
                    case 5 :
                        ((ImageView) findViewById(R.id.imageViewPie4)).setImageResource(R.drawable.splash_pie_derecho);
                        emiteSonido();
                        break;
                    case 6 :
                        ((ImageView) findViewById(R.id.imageViewPie5)).setImageResource(R.drawable.splash_pie_izquierdo);
                        emiteSonido();
                        break;
                    case 7 :
                        ((ImageView) findViewById(R.id.imageViewPie6)).setImageResource(R.drawable.splash_pie_derecho);
                        emiteSonido();
                        break;
                    default :
                        break;
                }
            }

            /**
             * Método llamado al finalizar la cuenta atrás.
             */
            @Override
            public void onFinish() {

                // Crear intent para ejecutar la activity principal
                Intent intent = new Intent(SplashScreenActivity.this, PrincipalActivity.class);

                // Ejecutar la activity
                startActivity(intent);

                // Finalizar esta activity; de esta forma, no se podrá volver a ella
                // desde la aplicación iniciada con el intent.
                finish();
            }
        }.start();
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

    private void emiteSonido() {
        if(puedoSonar && sonidoCargado) {

            soundPool.play(sonidoId, 1.0f, 1.0f, 0, 0, 1.0f);


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

                if (sampleId == sonidoId && status == 0) {

                    //soundPool.play(sampleId, 1.0f, 1.0f, 0, 0, 1.0f);

                    sonidoCargado = true;
                }
            }
        });

        //
        sonidoId = soundPool.load(this, R.raw.paso, 1);
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
            if(sonidoCargado) {
                soundPool.unload(sonidoId);
                sonidoCargado = false;
            }
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




}
