package com.app.floppysoftware.benitogg.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
//import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.floppysoftware.benitogg.R;
import com.app.floppysoftware.benitogg.Zeta;
import com.app.floppysoftware.benitogg.database.BaseDatos;
import com.app.floppysoftware.benitogg.models.Accion;
import com.app.floppysoftware.benitogg.models.Actor;
import com.app.floppysoftware.benitogg.models.Caso;
import com.app.floppysoftware.benitogg.models.Lugar;
import com.app.floppysoftware.benitogg.models.Objeto;
import com.app.floppysoftware.benitogg.utils.Preferencias;
import com.app.floppysoftware.benitogg.utils.Mensaje;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EscenaFragment extends Fragment {

    // Log
    //private static final String TAG ="EscenaFragment";

    // Claves para el bundle
    private static final String KEY_ID_LUGAR = "key_id_lugar";  // Id del lugar actual, utilizado para detectar cambios

    // Id del último lugar visitado
    private String ultimoLugarId = null;

    // Lugar actual
    private Lugar lugarActual = null;

    // Objetos del lugar actual
    private ArrayList<Objeto> lugarActualObjetos = null;

    // Acciones disponibles
    private ArrayList<Accion> otrasAcciones = null;

    // Actor protagonista
    private Actor prota = null;

    // Objetos que lleva el protagonista
    private ArrayList<Objeto> protaObjetos = null;

    // TextView del título del lugar
    private TextView textViewTitulo;

    // ImageView de la imagen del lugar
    private ImageView imageViewImagen;

    // TextView del detalle del lugar
    private TextView textViewDetalle;

    // Lístener que ha de implementar la activity
    private OnEscenaListener mListener;

    /**
     * Interfaz que ha de implementar la activity que utilice
     * este fragment.
     */
    public interface OnEscenaListener {

        // Fijar acciones disponibles
        void setAcciones(boolean norte, boolean sur, boolean este, boolean oeste,
                        ArrayList<Objeto> objetosLugar,
                        ArrayList<Objeto> objetosBolsillo,
                        ArrayList<Accion> otrasAcciones);

        // Emitir sonido
        void emiteSonido(int resId);
    }

    /**
     * Constructor.
     */
    public EscenaFragment() {

        // Nada
    }

    /**
     * Método llamado cuando se infla el layout por primera vez.
     *
     * @param inflater             Inflater
     * @param container            Contenedor
     * @param savedInstanceState   Estado previamente guardado
     *
     * @return                     Layout inflado
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Si el fragment se está recreando,
        // averiguar el lugar que se estaba mostrando.
        if(savedInstanceState != null) {
            ultimoLugarId = savedInstanceState.getString(KEY_ID_LUGAR);
        }

        // Averiguar si el dispositivo es una tablet o móvil
        boolean esTablet = getResources().getBoolean(R.bool.isTablet)  && !Preferencias.getVertical(getActivity());

        // Inflar el layout
        View v = inflater.inflate(esTablet ? R.layout.fragment_escena_tablet : R.layout.fragment_escena, container, false);

        // Tomar referencias de los Views
        textViewTitulo = (TextView) v.findViewById(R.id.textViewTitulo);
        imageViewImagen = (ImageView) v.findViewById(R.id.imageViewImagen);
        textViewDetalle = (TextView) v.findViewById(R.id.textViewDetalle);

        // Mostrar la escena
        mostrarEscena();

        // Devolver layout inflado
        return v;
    }

    /**
     * Método llamado cuando el fragment es enlazado a la activity.
     *
     * @param activity  Activity
     */
    @Override
    public void onAttach(Activity activity) {

        // Llamar a la superclase
        super.onAttach(activity);

        // Comprobar que la activity ha implementado el listener
        try {
            mListener = (OnEscenaListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAccionesListener");
        }
    }

    /**
     * Método llamado cuando el fragment es desenlazado de la activity.
     */
    @Override
    public void onDetach() {

        // Llamar a la superclase
        super.onDetach();

        // Invalidar listener
        mListener = null;
    }

    /**
     * Método llamado cuando se precise guardar el estado del
     * fragment.
     *
     * @param outState  Estado
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        // Llamar a la superclase
        super.onSaveInstanceState(outState);

        // Guardar id del lugar actual
        if(lugarActual != null) {
            outState.putString(KEY_ID_LUGAR, lugarActual.getId());
        }
    }

    /**
     * Actualizar las acciones disponibles.
     */
    private void setAcciones() {

        // Actualizar acciones disponibles
        mListener.setAcciones(lugarActual.getLugarNorte() != null,
                lugarActual.getLugarSur() != null,
                lugarActual.getLugarEste() != null,
                lugarActual.getLugarOeste() != null,
                lugarActualObjetos,
                protaObjetos,
                otrasAcciones);
    }

    /**
     * Mostrar la escena actual.
     */
    private void mostrarEscena() {

        // Mostrar la escena
        new MostrarEscena().execute();
    }

    /**
     * Clase interna, para recargar los datos de la escena, y mostrarla. También
     * recarga las acciones disponibles. Extiende de AsyncTask,
     * para no saturar el thread del UI.
     */
    private class MostrarEscena extends AsyncTask<Void, Void, String> {

        /**
         * Tarea a ejecutar en el UI, antes de doInBackground().
         */
        @Override
        protected void onPreExecute() {

            // Nada
        }

        /**
         * Tarea a realizar en background, fuera del thread del UI. Lee
         * la base de datos.
         *
         * @param params  nada
         * @return  nada
         */
        @Override
        protected String doInBackground(Void... params) {

            // Abrir base de datos
            BaseDatos bd = new BaseDatos(getActivity(), false);

            // Leer protagonista
            prota = bd.getActor(Actor.PROTAGONISTA);

            //Log.i(TAG, "prota = " + prota);
            //Log.i(TAG, "prota.getLugar() = " + prota.getLugar());

            // Leer lugar actual
            lugarActual = bd.getLugar(prota.getLugar());

            //Log.i(TAG, "lugarActual = " + lugarActual);

            // Leer los títulos de las salidas
            String tituloNorte = lugarActual.getLugarNorte() != null ? bd.getLugar(lugarActual.getLugarNorte()).getTitulo() : null;
            String tituloSur = lugarActual.getLugarSur() != null ? bd.getLugar(lugarActual.getLugarSur()).getTitulo() : null;
            String tituloEste = lugarActual.getLugarEste() != null ? bd.getLugar(lugarActual.getLugarEste()).getTitulo() : null;
            String tituloOeste = lugarActual.getLugarOeste() != null ? bd.getLugar(lugarActual.getLugarOeste()).getTitulo() : null;

            // Leer objetos que lleva el protagonista
            protaObjetos = bd.getObjetos(Lugar.BOLSILLO);

            //Log.i(TAG, "protaObjetos = " + protaObjetos);

            // Leer objetos en el lugar
            lugarActualObjetos = bd.getObjetos(lugarActual.getId());

            // Construir la descripción completa del lugar
            ArrayList<String> lugarLineas = new ArrayList<>();

            // Tomar descripción por defecto
            lugarLineas.add(lugarActual.getDetalle());

            // ArrayList para el parcheo del detalle
            ArrayList<Integer> parcheDetalle = new ArrayList<Integer>();

            // ArrayList para el parcheo de las acciones disponibles
            ArrayList<Accion> parcheAcciones = new ArrayList<Accion>();

            // Parchear escena
            Zeta.parcheEscena(bd, parcheDetalle, parcheAcciones);

            // Cerrar base de datos
            bd.cerrar();

            // Añadir líneas de parcheo al detalle
            for (int strId : parcheDetalle) {

                // Añadir línea en blanco de separación
                lugarLineas.add("");

                // Añadir línea
                lugarLineas.add(getString(strId));
            }

            // Acciones disponibles
            otrasAcciones = parcheAcciones;

            // Indicar los objetos que hay en el lugar
            if(lugarActualObjetos != null && !lugarActualObjetos.isEmpty()) {

                // Comenzar la línea
                lugarLineas.add("");

                // Mostrar un mensaje personalizado, según el nº de objetos que haya
                if (lugarActualObjetos.size() == 1) {
                    // Un objeto
                    lugarLineas.add(getString(R.string.escena_hay_1_objeto));
                } else {
                    // Más de un objeto
                    lugarLineas.add(getString(R.string.escena_hay_varios_objetos));
                }

                // Indicar los objetos que hay en el lugar
                for (int i = 0; i < lugarActualObjetos.size(); ++i) {
                    Objeto obj = lugarActualObjetos.get(i);
                    lugarLineas.add("   " + obj.getNombre() + ".");
                }
            }

            // Indicar las salidas disponibles

            // Añadir línea en blanco de separación
            lugarLineas.add("");

            // Añadir título
            lugarLineas.add(getString(R.string.escena_salidas));

            // Salida por el norte
            if(tituloNorte != null) {
                lugarLineas.add("   " + getString(R.string.escena_norte) + " " + tituloNorte + ".");
            }

            // Salida por el sur
            if(tituloSur!= null) {
                lugarLineas.add("   " + getString(R.string.escena_sur) + " " + tituloSur + ".");
            }

            // Salida por el oeste
            if(tituloOeste != null) {
                lugarLineas.add("   " + getString(R.string.escena_oeste) + " " + tituloOeste + ".");
            }

            // Salida por el este
            if(tituloEste != null) {
                lugarLineas.add("   " + getString(R.string.escena_este) + " " + tituloEste + ".");
            }

            // Construir el detalle final de la escena
            String escenaDetalle = "";

            // Añadir nueva línea a todas las líneas de detalle
            for(String str : lugarLineas) {
                escenaDetalle = escenaDetalle.concat(str + "\n");
            }

            // Devolver texto descriptivo de la escena
            return escenaDetalle;
        }

        /**
         * Tarea a realizar en el thread del UI, una vez finalizada la tarea
         * en background. Muestra la escena. Además, muestra las acciones
         * disponibles en versión tablet.
         *
         * @param  detalle  texto descriptivo de la escena
         */
        @Override
        protected void onPostExecute(String detalle) {

            // Si el fragment no está enlazado a la activity, no hacer nada
            if(!isAdded()) {

                // Cancelar
                return;
            }

            // Mostrar título
            textViewTitulo.setText(lugarActual.getTitulo());

            // Mostrar imagen
            imageViewImagen.setImageResource(getResources().getIdentifier("zeta_lugar_" + lugarActual.getId(), "drawable", getActivity().getPackageName()));

            // Mostrar detalle
            textViewDetalle.setText(detalle);

            // Actualizar las acciones disponibles
            setAcciones();

            // Comprobar si se ha cambiado de lugar
            if(!lugarActual.getId().equals(ultimoLugarId)) {

                // Indicar el cambio de lugar, para futuras comprobaciones
                ultimoLugarId = lugarActual.getId();

                // Emitir sonido, si el lugar tiene uno asignado
                if(lugarActual.getSonido() != null) {

                    // Emitir sonido
                    mListener.emiteSonido(getResources().getIdentifier("zeta_" + lugarActual.getSonido(), "raw", getActivity().getPackageName()));
                }
            }
        }
    }

    /**
     * Realizar una acción.
     *
     * @param actionType    Tipo de acción
     * @param actionNumber  Número de acción
     */
    public void realizaAccion(int actionType, int actionNumber) {

        // Realizar la acción
        new RealizaAccion().execute(actionType, actionNumber);
    }

    /**
     * Clase interna, para realizar algunas acciones. Extiende de AsyncTask,
     * para no saturar el thread del UI.
     */
    private class RealizaAccion extends AsyncTask<Integer, Void, Void> {

        // Indica el caso que ha sido resuelto con esta acción, o null
        private String casoResuelto = null;

        /**
         * Tarea a realizar en background, fuera del thread del UI. Realiza
         * la acción, y devuelve el nombre del caso resuelto, o null.
         *
         * @param params   actionType, actionNumber
         */
        @Override
        protected Void doInBackground(Integer... params) {

            // Tomar parámetros de entrada
            int actionType = params[0];
            int actionNumber = params[1];

            // Caso resuelto, por defecto: ninguno
            int casoResueltoId = 0;

            // Abrir base de datos
            BaseDatos bd = new BaseDatos(getActivity(), false);

            // Ejecutar la acción
            switch(actionType) {
                case AccionesFragment.ACCION_NORTE:
                    cambiaLugar(bd, lugarActual.getLugarNorte());
                    break;
                case AccionesFragment.ACCION_SUR:
                    cambiaLugar(bd, lugarActual.getLugarSur());
                    break;
                case AccionesFragment.ACCION_ESTE:
                    cambiaLugar(bd, lugarActual.getLugarEste());
                    break;
                case AccionesFragment.ACCION_OESTE:
                    cambiaLugar(bd, lugarActual.getLugarOeste());
                    break;
                case AccionesFragment.ACCION_TOMAR:

                    // Averiguar objeto a tomar
                    Objeto objLugar = lugarActualObjetos.get(actionNumber);

                    // Ponerlo en el bolsillo
                    objLugar.setLugar(Lugar.BOLSILLO);
                    bd.updateObjeto(objLugar);

                    // Comprobar si se ha resuelto un caso con esta acción
                    casoResueltoId = Zeta.objetoTomado(bd, objLugar);
                    break;
                case AccionesFragment.ACCION_DEJAR:

                    // Averiguar objeto a dejar
                    Objeto objBolsillo = protaObjetos.get(actionNumber);

                    // Dejar el objeto en el lugar actual
                    objBolsillo.setLugar(lugarActual.getId());
                    bd.updateObjeto(objBolsillo);

                    // Comprobar si se ha resuelto un caso con esta acción
                    casoResueltoId = Zeta.objetoDejado(bd, objBolsillo);
                    break;
                case AccionesFragment.ACCION_OTRAS:

                    // Realizar la acción, y comprobar si se ha
                    // resuelto un caso con ella.
                    casoResueltoId = Zeta.doAccion(bd, otrasAcciones.get(actionNumber).getId());
                    break;
                default :

                    // Nada
                    break;
            }

            // Comprobar si se ha resuelto un caso tras realizar la acción
            if(casoResueltoId != 0) {

                // Averiguar qué caso se ha resuelto
                Caso caso = bd.getCaso(casoResueltoId);

                // Indicar que se ha resuelto
                caso.setResuelto(true);
                bd.updateCaso(caso);

                // Tomar el nombre del caso resuelto
                casoResuelto = caso.getNombre();
            }

            // Cerrar la base de datos
            bd.cerrar();

            // Retornar
            return null;
        }

        /**
         * Tarea a realizar en el thread del UI, una vez finalizada la tarea
         * en background. Si se ha resuelto un caso, muestra un cuadro
         * de diálogo. Refresca la escena.
         */
        @Override
        protected void onPostExecute(Void param) {

            // Refrescar la escena
            mostrarEscena();

            // Comprobar si se ha resuelto un caso tras realizar la acción
            if(casoResuelto != null) {

                // Emitir un sonido
                mListener.emiteSonido(R.raw.caso_resuelto);

                // Indicar al usuario la resolución del caso
                Mensaje.continuar(getActivity(), R.drawable.ic_solved,
                        getString(R.string.dialogo_caso_resuelto_titulo),
                        getString(R.string.dialogo_caso_resuelto_texto) + " '" + casoResuelto + "'!",
                        null);
            }
        }
    }

    /**
     * Cambiar de lugar al protagonista.
     *
     * @param bd       Base de datos
     * @param lugarId  Lugar destino
     */
    private void cambiaLugar(BaseDatos bd, String lugarId) {

        // Tomar id del lugar origen
        String lugarOrigenId = lugarActual.getId();

        // Cambiar al protagonista de lugar
        prota.setLugar(lugarId);
        bd.updateActor(prota);

        // Indicar el cambio de lugar
        Zeta.protaCambiaLugar(bd, lugarOrigenId, prota.getLugar());
    }
}
