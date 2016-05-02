package com.app.floppysoftware.benitogg.activities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.floppysoftware.benitogg.utils.Preferencias;
import com.app.floppysoftware.benitogg.R;
import com.app.floppysoftware.benitogg.database.BaseDatos;
import com.app.floppysoftware.benitogg.models.Caso;

import java.util.ArrayList;

/**
 * Activity para mostrar la lista de casos, así como el estado en el que se
 * encuentran (resuelto / no resuelto), y un texto informativo.
 *
 * @author   Miguel I. García López
 * @version  2.0
 * @since    16 Apr 2016
 */
public class CasosActivity extends Activity {

    // Views del layout
    private ListView listViewCasos;  // Lista de casos
    private TextView textViewCasos;  // Texto informativo

    /**
     * Método llamado al crear la activity.
     *
     * @param savedInstanceState  Estado previamente guardado
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Llamar a la superclase
        super.onCreate(savedInstanceState);

        // Averiguar si el dispositivo es una tablet con preferencia de orientación horizontal
        boolean esTabletHorizontal = getResources().getBoolean(R.bool.isTablet) && !Preferencias.getVertical(this);

        // Cambiar la orientación de la pantalla. Esto puede ocasionar que la
        // activity se reinicie, si la orientación actual no es la solicitada.
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
        setContentView(R.layout.activity_casos);

        // Tomar la referencia de los views
        listViewCasos = (ListView) findViewById(R.id.listViewCasos);  // Lista de casos
        textViewCasos = (TextView) findViewById(R.id.textViewCasos);  // Texto adicional

        // Mostrar lista de casos
        new MostrarCasosResueltos().execute();
    }

    /**
     * Clase interna, para mostrar la lista de casos. Extiende de AsyncTask, para
     * no saturar el thread del UI.
     */
    private class MostrarCasosResueltos extends AsyncTask<Void, Void, ArrayList<Caso>> {

        /**
         * Tarea a realizar en background, fuera del thread del UI. Devuelve
         * la lista de casos.
         *
         * @param params  Nada
         * @return        Lista de casos
         */
        @Override
        protected ArrayList<Caso> doInBackground(Void... params) {

            // Abrir base de datos
            BaseDatos bd = new BaseDatos(CasosActivity.this, false);

            // Tomar lista de casos
            ArrayList<Caso> casos = bd.getCasos();

            // Cerrar base de datos
            bd.cerrar();

            // Alterar el nombre de los casos no resueltos
            for(Caso caso: casos) {
                if(!caso.getResuelto()) {
                    caso.setNombre(alterarTexto(caso.getNombre()));
                }
            }

            // Devolver lista de casos
            return casos;
        }

        /**
         * Tarea a realizar en el thread del UI, una vez finalizada la tarea
         * en background. Muestra la lista de casos.
         *
         * @param casos  Lista de casos
         */
        @Override
        protected void onPostExecute(ArrayList<Caso> casos) {

            // Crear adapter con la lista de casos para el ListView
            CasosAdapter adapter = new CasosAdapter(casos);

            // Asignar el adapter al ListView
            listViewCasos.setAdapter(adapter);

            // Texto informativo sobre los casos resueltos (por
            // defecto: todos resueltos).
            int textoId = R.string.casos_resueltos_todos;

            // Cambiar el texto informativo si hay casos pendientes
            // de resolver
            for(Caso caso : casos) {

                // Si hay al menos un caso no resuelto,
                // cambiar el texto.
                if(!caso.getResuelto()) {

                    // Cambiar el texto; quedan casos por resolver
                    textoId = R.string.casos_resueltos_quedan;
                    break;
                }
            }

            // Mostrar el texto
            textViewCasos.setText(textoId);
        }
    }

    /**
     * Alterar un texto, sustituyendo los caracteres en posiciones impares,
     * que no sean espacios, por el caracter '?'.
     *
     * @param texto   Texto a alterar
     * @return        Texto resultante
     */
    private String alterarTexto(String texto) {

        // Texto resultante
        String resultado = "";

        // Recorrer el texto
        for(int i = 0; i < texto.length(); ++i) {

            // Tomar caracter que está en la posición actual
            char ch = texto.charAt(i);

            // Si la posición es impar, y el caracter no
            // es un espacio, poner el caracter '?'.
            if(ch != ' ' && (i % 2) == 1) {
                ch = '?';
            }

            // Añadir caracter al resultado
            resultado = resultado + ch;
        }

        // Devolver resultado
        return resultado;
    }

    /**
     * Adapter para el ListView.
     */
    private class CasosAdapter extends ArrayAdapter<Caso> {

        // Datos de la lista
        private ArrayList<Caso> datos;

        /**
         * Constructor.
         *
         * @param datos  Datos de la lista
         */
        public CasosAdapter(ArrayList<Caso> datos) {

            // Llamar a la superclase
            super(CasosActivity.this, 0, datos);

            // Tomar lista de datos
            this.datos = datos;
        }

        /**
         * Devolver el view correspondiente a un caso de la lista.
         *
         * @param position     Posición del caso en la lista
         * @param convertView  View a tratar
         * @param parent       View padre
         * @return             View tratado
         */
        public View getView(int position, View convertView, ViewGroup parent) {

            // Inflar el layout del view si no está inflado ya
            if(convertView == null) {

                // Inflater
                LayoutInflater inflater = (LayoutInflater) CasosActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                // Inflar el layout del view
                convertView = inflater.inflate(R.layout.adapter_casos, parent, false);
            }

            // Tomar los views del layout
            ImageView imageViewTick = (ImageView) convertView.findViewById(R.id.imageViewAdapterCasos);  // Tick de estado
            TextView  textViewCaso  = (TextView) convertView.findViewById(R.id.textViewAdapterCasos);    // Nombre del caso

            // Tomar el caso correspondiente
            Caso caso = datos.get(position);

            // Fijar imagen del tick según el estado del caso
            imageViewTick.setImageResource(caso.getResuelto() ? R.drawable.tick_si : R.drawable.tick_no);

            // Fijar texto con el nombre del caso
            textViewCaso.setText(caso.getNombre());

            // Devolver el view
            return convertView;
        }
    }
}
