package com.app.floppysoftware.benitogg;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

public class MapaActivity extends Activity {

    private ImageView imageViewMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //
        super.onCreate(savedInstanceState);

        //
        setContentView(R.layout.activity_mapa);

        //
        imageViewMapa = (ImageView) findViewById(R.id.imageViewMapa);

        //
        new MostrarProta().execute();
    }

    /**
     * Clase interna, para mostrar la posición del protagonista. Extiende de AsyncTask, para
     * no saturar el thread del UI.
     */
    private class MostrarProta extends AsyncTask<Void, Void, Bitmap> {

        /**
         * Tarea a realizar en background, fuera del thread del UI. Devuelve
         * el bitmap a mostrar.
         *
         * @param params  nada
         * @return  bitmap
         */
        @Override
        protected Bitmap doInBackground(Void... params) {
            // Abrir base de datos
            BaseDatos bd = new BaseDatos(MapaActivity.this, false);

            // Tomar lugar en el que está el protagonista
            Lugar lugar = bd.getLugar(bd.getActor(Actor.PROTAGONISTA).getLugar());

            // Cerrar base de datos
            bd.cerrar();

            //
            BitmapFactory.Options bmpMapaOpts = new BitmapFactory.Options();
            bmpMapaOpts.inMutable = true;
            bmpMapaOpts.inScaled = false;

            //
            Bitmap bmpMapa = BitmapFactory.decodeResource(getResources(), R.drawable.zeta_mapa, bmpMapaOpts);

            //
            BitmapFactory.Options bmpProtaOpts = new BitmapFactory.Options();
            bmpProtaOpts.inScaled = false;

            //
            Bitmap bmpProta = BitmapFactory.decodeResource(getResources(), R.drawable.zeta_mapa_prota, bmpProtaOpts);

            //
            int x = lugar.getX() - (bmpProta.getWidth() + 1) / 2;
            //int y = lugar.getY() - (bmpProta.getHeight() + 1) / 2;
            int y = lugar.getY() - bmpProta.getHeight();

            //
            if(x < 0) {
                x = 0;
            } else if(x + bmpProta.getWidth() > bmpMapa.getWidth()) {
                x = bmpMapa.getWidth() - bmpProta.getWidth();
            }

            //
            if(y < 0) {
                y = 0;
            } else if(y + bmpProta.getHeight() > bmpMapa.getHeight()) {
                y = bmpMapa.getHeight() - bmpProta.getHeight();
            }

            //
            Canvas canvas = new Canvas(bmpMapa);

            //
            canvas.drawBitmap(bmpProta, (float) x, (float) y, null);

            //
            return bmpMapa;
        }

        /**
         * Tarea a realizar en el thread del UI, una vez finalizada la tarea
         * en background. Muestra el mapa.
         *
         * @param bmpMapa  Bitmap del mapa
         */
        @Override
        protected void onPostExecute(Bitmap bmpMapa) {

            //
            imageViewMapa.setImageBitmap(bmpMapa);
        }
    }


}
