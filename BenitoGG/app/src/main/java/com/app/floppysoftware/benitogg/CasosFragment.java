package com.app.floppysoftware.benitogg;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Fragment para visualizar la lista de nombres de casos resueltos, y un
 * texto informativo.
 */
public class CasosFragment extends Fragment {

    // Views del layout
    private ListView listViewCasos;  // Lista de casos
    private TextView textViewCasos;  // Texto informativo

    /**
     * Constructor.
     */
    public CasosFragment() {
        // Nada
    }

    /**
     * Método llamado al crear el UI por 1ª vez.
     *
     * @param inflater    inflater
     * @param container   container
     * @param savedInstanceState   estado previamente guardado
     *
     * @return  view del layout inflado
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar el layout del fragment
        View v = inflater.inflate(R.layout.fragment_casos, container, false);

        // Tomar la referencia de los views
        listViewCasos = (ListView) v.findViewById(R.id.listViewCasos);
        textViewCasos = (TextView) v.findViewById(R.id.textViewCasos);

        // Devolver layout inflado
        return v;
    }

    /**
     * Método llamado cuando la activity ha sido creada.
     *
     * @param savedInstanceState  estado previamente guardado
     */
    @Override
    public void onActivityCreated (Bundle savedInstanceState) {

        // Llamar a la superclase
        super.onActivityCreated(savedInstanceState);

        // Mostrar lista de casos resueltos
        new MostrarCasosResueltos().execute();
    }

    /**
     * Clase interna, para mostrar la lista de casos. Extiende de AsyncTask, para
     * no saturar el thread del UI.
     */
    private class MostrarCasosResueltos extends AsyncTask<Void, Void, ArrayList<Caso>> {

        // Número total de casos
        private int numTotalCasos;

        /**
         * Tarea a realizar en background, fuera del thread del UI. Devuelve
         * la lista de casos.
         *
         * @param params  nada
         * @return  casos
         */
        @Override
        protected ArrayList<Caso> doInBackground(Void... params) {
            // Abrir base de datos
            BaseDatos bd = new BaseDatos(getActivity(), false);

            // Tomar lista de casos
            ArrayList<Caso> casos = bd.getCasos(null);

            // Cerrar base de datos
            bd.cerrar();

            // Ofuscar el nombre de los casos no resueltos
            for(Caso caso: casos) {
                if(!caso.getResuelto()) {
                    caso.setNombre(ofuscarTexto(caso.getNombre()));
                }
            }

            // Devolver lista
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
            //ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listaCasosResueltos);
            CasosAdapter adapter = new CasosAdapter(casos);

            // Asignar el adapter al ListView
            listViewCasos.setAdapter(adapter);

            // Texto informativo sobre los casos resueltos.
            int textoId = R.string.casos_resueltos_todos;

            for(Caso caso : casos) {
                if(!caso.getResuelto()) {
                    textoId = R.string.casos_resueltos_quedan;
                    break;
                }
            }

            // Mostrar el texto
            textViewCasos.setText(textoId);
        }
    }

    /**
     * Ofuscar un texto, sustituyendo los caracteres en posiciones impares,
     * que no sean espacios, por el caracter '?'.
     *
     * @param texto   texto a ofuscar
     * @return        texto resultante
     */
    private String ofuscarTexto(String texto) {

        // Texto resultante
        String resultado = "";

        // Recorrer el texto
        for(int i = 0; i < texto.length(); ++i) {

            // Caracter en la posición actual
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

    private class CasosAdapter extends ArrayAdapter<Caso> {

        private ArrayList<Caso> datos;

        public CasosAdapter(ArrayList<Caso> datos) {

            super(getActivity(), 0, datos);

            this.datos = datos;
        }

        //
        public View getView(int position, View convertView, ViewGroup parent) {
            // position del view
            // converView -> view a utilizar
            // parent -> padre

            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(convertView == null) {
                convertView = inflater.inflate(R.layout.adapter_casos, parent, false);
            }

            // Tomar los views del layout
            ImageView imageViewTick = (ImageView) convertView.findViewById(R.id.imageViewAdapterCasos);
            TextView textViewCaso = (TextView) convertView.findViewById(R.id.textViewAdapterCasos);

            // Tomar el caso que corresponde
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
