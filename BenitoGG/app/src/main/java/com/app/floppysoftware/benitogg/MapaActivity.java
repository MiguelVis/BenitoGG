package com.app.floppysoftware.benitogg;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

/**
 * Clase que implementa la activity del mapa.
 *
 * @author   Miguel I. García López
 * @version  1.0
 * @since    25 Mar 2016
 */
public class MapaActivity extends Activity {

    // ImageView del mapa
    private ImageView imageViewMapa;

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
        setContentView(R.layout.activity_mapa);

        // Tomar referencia del ImageView
        imageViewMapa = (ImageView) findViewById(R.id.imageViewMapa);

        // Posicionar la imagen del protagonista en el mapa
        new MostrarProta().execute();
    }

    /**
     * Clase interna, para mostrar la posición del protagonista. Extiende de AsyncTask,
     * para no saturar el thread del UI.
     */
    private class MostrarProta extends AsyncTask<Void, Void, Bitmap> {

        /**
         * Tarea a realizar en background, fuera del thread del UI. Devuelve
         * el bitmap a mostrar.
         *
         * @param params  Nada
         * @return        Bitmap
         */
        @Override
        protected Bitmap doInBackground(Void... params) {

            // Abrir base de datos
            BaseDatos bd = new BaseDatos(MapaActivity.this, false);

            // Tomar lugar en el que está el protagonista
            Lugar lugar = bd.getLugar(bd.getActor(Actor.PROTAGONISTA).getLugar());

            // Cerrar base de datos
            bd.cerrar();

            // Opciones para el Bitmap del mapa
            BitmapFactory.Options bmpMapaOpts = new BitmapFactory.Options();
            bmpMapaOpts.inMutable = true;  // Se va a modificar
            bmpMapaOpts.inScaled = false;  // No escalado, tamaño original

            // Devolver objeto bitmap del mapa
            Bitmap bmpMapa = BitmapFactory.decodeResource(getResources(), R.drawable.zeta_mapa, bmpMapaOpts);

            // Opciones para el Bitmap del protagonista
            BitmapFactory.Options bmpProtaOpts = new BitmapFactory.Options();
            bmpProtaOpts.inScaled = false; // No escalado, tamaño original

            // Devolver objeto bitmap del protagonista
            Bitmap bmpProta = BitmapFactory.decodeResource(getResources(), R.drawable.zeta_mapa_prota, bmpProtaOpts);

            // Obtener posición referida al bitmap del mapa,
            // donde irá el bitmap del protagonista.
            int x = lugar.getX() - (bmpProta.getWidth() + 1) / 2;  // Posición X
            int y = lugar.getY() - bmpProta.getHeight();           // Posición Y

            // Ajustar posición X, si el bitmap del protagonista está
            // fuera del mapa.
            if(x < 0) {
                x = 0;
            } else if(x + bmpProta.getWidth() > bmpMapa.getWidth()) {
                x = bmpMapa.getWidth() - bmpProta.getWidth();
            }

            // Ajustar posición Y, si el bitmap del protagonista está
            // fuera del mapa.
            if(y < 0) {
                y = 0;
            } else if(y + bmpProta.getHeight() > bmpMapa.getHeight()) {
                y = bmpMapa.getHeight() - bmpProta.getHeight();
            }

            // Canvas para dibujar sobre el mapa
            Canvas canvas = new Canvas(bmpMapa);

            // Dibujar el bitmap del protagonista en el mapa
            canvas.drawBitmap(bmpProta, (float) x, (float) y, null);

            // Devolver bitmap modificado del mapa
            return bmpMapa;
        }

        /**
         * Tarea a realizar en el thread del UI, una vez finalizada la tarea
         * en background. Muestra el mapa modificado, con la imagen del
         * protagonista.
         *
         * @param bmpMapa  Bitmap del mapa
         */
        @Override
        protected void onPostExecute(Bitmap bmpMapa) {

            // Asignar el bitmap del mapa al ImageView
            imageViewMapa.setImageBitmap(bmpMapa);
        }
    }
}
