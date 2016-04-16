package com.app.floppysoftware.benitogg;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;


public class SplashScreenActivity extends Activity {

    private final static String TAG = "SplashScreenActivity";

    private final static int NUM_PASOS = 8;  // 6 pasos, mas 2
    private final static long MILIS_PASO = 500;  // Milisegundos entre pasos

    private CountDownTimer timer;

    private int pasosDados = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Llamar a la superclase
        super.onCreate(savedInstanceState);

        // Averiguar si el dispositivo es una tablet en orientación horizontal
        boolean esTabletHorizontal = getResources().getBoolean(R.bool.isTablet) && !Preferencias.getVertical(this);

        // Cambiar la orientación de la pantalla, dependiendo del tipo de dispositivo
        if(esTabletHorizontal) {
            // Tablet
            if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        } else {
            // Móvil
            if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }

        // Fijar layout
        setContentView(R.layout.activity_splash_screen);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Cancelar timer
        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //
        //if(pasosDados >= NUM_PASOS) {
        //    return;
        //}

        // Timer para que se muestre durante ese tiempo la Splash Screen.
        timer = new CountDownTimer(MILIS_PASO * (NUM_PASOS - pasosDados), MILIS_PASO) {
            @Override
            public void onTick(long millisUntilFinished) {

                //
                Log.i(TAG, "Paso = " + pasosDados);
                //
                switch(pasosDados++) {
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
                    case 6 :
                        ((ImageView) findViewById(R.id.imageViewPie6)).setImageResource(R.drawable.splash_pie_derecho);
                        break;
                    default :
                        break;
                }

            }

            @Override
            public void onFinish() {
                // Crear intent para ejecutar la otra activity
                Intent intent = new Intent(SplashScreenActivity.this, PrincipalActivity.class);

                // Ejecutar la activity
                startActivity(intent);

                // Finalizar esta activity, de esta forma, no se podrá volver a ella
                // desde la aplicación iniciada con el intent.
                finish();
            }
        }.start();
    }
}
