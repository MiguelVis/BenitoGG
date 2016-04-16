package com.app.floppysoftware.benitogg;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
    private final static int NUM_PASOS = 8;      // 6 pasos, más 2
    private final static long MILIS_PASO = 500;  // Milisegundos entre pasos

    // Timer
    private CountDownTimer timer;

    // Número de pasos dados
    private int pasosDados = 0;

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
    }

    /**
     * Método llamado al reanudar la activity.
     */
    @Override
    protected void onResume() {

        // Llamar a la superclase
        super.onResume();

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
                    case 0 :
                        break;
                    case 1 :
                        ((ImageView) findViewById(R.id.imageViewPie1)).setImageResource(R.drawable.splash_pie_izquierdo);
                        break;
                    case 2 :
                        ((ImageView) findViewById(R.id.imageViewPie2)).setImageResource(R.drawable.splash_pie_derecho);
                        break;
                    case 3 :
                        ((ImageView) findViewById(R.id.imageViewPie3)).setImageResource(R.drawable.splash_pie_izquierdo);
                        break;
                    case 4 :
                        ((ImageView) findViewById(R.id.imageViewPie4)).setImageResource(R.drawable.splash_pie_derecho);
                        break;
                    case 5 :
                        ((ImageView) findViewById(R.id.imageViewPie5)).setImageResource(R.drawable.splash_pie_izquierdo);
                        break;
                    default :
                        ((ImageView) findViewById(R.id.imageViewPie6)).setImageResource(R.drawable.splash_pie_derecho);
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
}
