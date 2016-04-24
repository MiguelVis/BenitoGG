package com.app.floppysoftware.benitogg;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AhoraFragment extends Fragment {

    private static final String TAG ="AhoraFragment";

    // Claves para el bundle
    private static final String KEY_ID_LUGAR = "key_id_lugar";  // Id del lugar actual, utilizado para detectar cambios

    private String ultimoLugarId = null;

    private Lugar lugarActual = null;
    private ArrayList<Objeto> lugarActualObjetos = null;

    private ArrayList<Accion> otrasAcciones = null;

    private Actor prota = null;
    private ArrayList<Objeto> protaObjetos = null;

    private TextView textViewTitulo;
    private ImageView imageViewImagen;
    private TextView textViewDetalle;
    //private Button buttonAcciones;

    // Botones para la versión móvil
    private ImageView imageViewNorte;
    private ImageView imageViewSur;
    private ImageView imageViewEste;
    private ImageView imageViewOeste;
    private ImageView imageViewMas;

    // Variable que indica si el dispositivo es una tablet
    private boolean esTablet = false;

    // Lístener que ha de implementar la activity
    private AhoraFragmentInteractionListener mListener;

    public interface AhoraFragmentInteractionListener {

        public void setAcciones(boolean norte, boolean sur, boolean este, boolean oeste,
                                ArrayList<Objeto> objetosLugar,
                                ArrayList<Objeto> objetosBolsillo,
                                ArrayList<Accion> otrasAcciones);

        public void emiteSonido(int resId);
    }

    public AhoraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //
        if(savedInstanceState != null) {
            ultimoLugarId = savedInstanceState.getString(KEY_ID_LUGAR);
        }
        // Averiguar si el dispositivo es una tablet o móvil
        esTablet = getResources().getBoolean(R.bool.isTablet)  && !Preferencias.getVertical(getActivity());

        // Inflate the layout for this fragment
        View v = inflater.inflate(esTablet ? R.layout.fragment_ahora_tablet : R.layout.fragment_ahora, container, false);


        textViewTitulo = (TextView) v.findViewById(R.id.textViewTitulo);
        imageViewImagen = (ImageView) v.findViewById(R.id.imageViewImagen);
        textViewDetalle = (TextView) v.findViewById(R.id.textViewDetalle);

        // Botones para la versión móvil
        if(!esTablet) {

            // Botones de dirección
            imageViewNorte = (ImageView) v.findViewById(R.id.imageViewNorte);
            imageViewSur = (ImageView) v.findViewById(R.id.imageViewSur);
            imageViewOeste = (ImageView) v.findViewById(R.id.imageViewOeste);
            imageViewEste = (ImageView) v.findViewById(R.id.imageViewEste);

            // Botón de acciones
            imageViewMas = (ImageView) v.findViewById(R.id.imageViewMas);

            // Inhabilitar los botones de dirección
            imageViewNorte.setEnabled(false);
            imageViewSur.setEnabled(false);
            imageViewOeste.setEnabled(false);
            imageViewEste.setEnabled(false);

            // Inhabilitar el botón de acciones
            imageViewMas.setEnabled(false);

            // Lísteners
            imageViewNorte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realizaAccion(AccionesFragment.ACCION_NORTE, 0);
                }
            });

            imageViewSur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realizaAccion(AccionesFragment.ACCION_SUR, 0);
                }
            });

            imageViewEste.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realizaAccion(AccionesFragment.ACCION_ESTE, 0);
                }
            });

            imageViewOeste.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realizaAccion(AccionesFragment.ACCION_OESTE, 0);
                }
            });

            imageViewMas.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setAcciones();
                }
            });
        }

        //
        mostrarEscena();

        // Devolver layout inflado
        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (AhoraFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAccionesListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        outState.putString(KEY_ID_LUGAR, lugarActual.getId());
    }



    /**
     * En versión móvil, lanza el fragment de acciones. En versión tablet, actualiza
     * el fragment de las acciones disponibles.
     */
    private void setAcciones() {
        mListener.setAcciones(lugarActual.getLugarNorte() != null,
                lugarActual.getLugarSur() != null,
                lugarActual.getLugarEste() != null,
                lugarActual.getLugarOeste() != null,
                lugarActualObjetos,
                protaObjetos,
                otrasAcciones);
    }

    private void mostrarEscena() {

        new MostrarEscena().execute();
    }

    /**
     * Clase interna, para recargar los datos de la escena, y mostrarla. También
     * recarga las acciones disponibles. Extiende de AsyncTask,
     * para no saturar el thread del UI.
     */
    private class MostrarEscena extends AsyncTask<Void, Void, String> {

        // True si se ha de reinicializar la base de datos
        boolean reset;

        // ProgressDialog a mostrar durante la reinicialización
        // de la base de datos.
        ProgressDialog progressDialog;

        /**
         * Tarea a ejecutar en el UI, antes de doInBackground().
         */
        @Override
        protected void onPreExecute() {

            // ¿Se ha de reinicializar el juego?
            reset = Preferencias.getReset(getActivity());

            // Si se ha de reinicializar el juego...
            if(reset) {

                // Mostrar imagen del reloj
                imageViewImagen.setImageResource(R.drawable.reloj);

                // Mostrar ProgressDialog durante el proceso de
                // reinicializado.
                progressDialog = ProgressDialog.show(getActivity(),
                        getString(R.string.dialogo_carga_titulo),
                        getString(R.string.dialogo_carga_texto),
                        true,
                        false);

                // La próxima vez no se reinicializará
                Preferencias.setReset(getActivity(), false);
            }
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
            BaseDatos bd = new BaseDatos(getActivity(), reset);

            // Leer protagonista
            prota = bd.getActor(Actor.PROTAGONISTA);

            Log.i(TAG, "prota = " + prota);
            Log.i(TAG, "prota.getLugar() = " + prota.getLugar());

            // Leer lugar actual
            lugarActual = bd.getLugar(prota.getLugar());

            Log.i(TAG, "lugarActual = " + lugarActual);

            // Leer los títulos de las salidas

            String tituloNorte = lugarActual.getLugarNorte() != null ? bd.getLugar(lugarActual.getLugarNorte()).getTitulo() : null;
            String tituloSur = lugarActual.getLugarSur() != null ? bd.getLugar(lugarActual.getLugarSur()).getTitulo() : null;
            String tituloEste = lugarActual.getLugarEste() != null ? bd.getLugar(lugarActual.getLugarEste()).getTitulo() : null;
            String tituloOeste = lugarActual.getLugarOeste() != null ? bd.getLugar(lugarActual.getLugarOeste()).getTitulo() : null;

            // Leer objetos que lleva el protagonista
            protaObjetos = bd.getObjetos(Lugar.BOLSILLO);

            Log.i(TAG, "protaObjetos = " + protaObjetos);

            // Leer objetos en el lugar
            lugarActualObjetos = bd.getObjetos(lugarActual.getId());


            // Leer los dichos de los actores que hay en el lugar
            //lugarActualDichos = new ArrayList<>();

            //for(Actor actor : lugarActualActores) {
            //    Dicho dicho = bd.getDicho(actor.getId(), lugarActual.getId());
            //    lugarActualDichos.add(dicho);
            //}

            // Cerrar base de datos
            //bd.cerrar();

            // Título del lugar
            //escenaTitulo = lugarActual.getTitulo();

            // Imagen del lugar
            //escenaImagen = R.drawable.portada;

            //if(lugarActual.getImagen() != null) {
            //    escenaImagen = getResources().getIdentifier("lugar_" + lugarActual.getImagen(), "drawable", getActivity().getPackageName());
            //}

            // Construir la descripción completa del lugar
            ArrayList<String> lugarLineas = new ArrayList<>();

            // Tomar descripción por defecto
            lugarLineas.add(lugarActual.getDetalle());

            // Indicar los dichos de los actores que hay en el lugar, a excepción del protagonista
            //for(Dicho dicho : lugarActualDichos) {
            //    if(dicho != null && !dicho.getActorId().equals(Actor.PROTAGONISTA)) {
            //        lugarLineas.add(dicho.getDetalle());
            //    }
            //}

            // Parcheo detalle
            //ArrayList<Integer> parcheDetalle = Zeta.getDetalle(lugarActual, lugarActualActores, lugarActualObjetos, protaObjetos);

            ArrayList<Integer> parcheDetalle = new ArrayList<Integer>();
            ArrayList<Accion> parcheAcciones = new ArrayList<Accion>();

            Zeta.parcheEscena(bd, parcheDetalle, parcheAcciones);

            // Cerrar base de datos
            bd.cerrar();

            /*****************
             if(!parcheDetalle.isEmpty()) {

             lugarLineas.add("");

             for (int strId : parcheDetalle) {
             lugarLineas.add(getString(strId));
             }
             }
             ******************/

            for (int strId : parcheDetalle) {

                //
                lugarLineas.add("");

                //
                lugarLineas.add(getString(strId));
            }

            // Acciones
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

            // Indicar las salidas
            lugarLineas.add("");
            lugarLineas.add(getString(R.string.escena_salidas));

            if(tituloNorte != null) {
                lugarLineas.add("   " + getString(R.string.escena_norte) + " " + tituloNorte + ".");
            }

            if(tituloSur!= null) {
                lugarLineas.add("   " + getString(R.string.escena_sur) + " " + tituloSur + ".");
            }

            if(tituloOeste != null) {
                lugarLineas.add("   " + getString(R.string.escena_oeste) + " " + tituloOeste + ".");
            }

            if(tituloEste != null) {
                lugarLineas.add("   " + getString(R.string.escena_este) + " " + tituloEste + ".");
            }

            String escenaDetalle = "";

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

            // Quitar el ProgressDialog de reinicialización de la
            // base de datos, si está activo.
            if(progressDialog != null) {
                progressDialog.dismiss();
            }

            // Mostrar la descripción completa del lugar
            textViewTitulo.setText(lugarActual.getTitulo());
            imageViewImagen.setImageResource(getResources().getIdentifier("zeta_lugar_" + lugarActual.getId(), "drawable", getActivity().getPackageName()));
            textViewDetalle.setText(detalle);

            // Actualizar las acciones disponibles, si es una tablet
            //if(buttonAcciones == null) {
            //    setAcciones();
            //}
            if(esTablet) {
                setAcciones();
            } else {
                imageViewNorte.setEnabled(lugarActual.getLugarNorte() != null);
                imageViewSur.setEnabled(lugarActual.getLugarSur() != null);
                imageViewEste.setEnabled(lugarActual.getLugarEste() != null);
                imageViewOeste.setEnabled(lugarActual.getLugarOeste() != null);
                imageViewMas.setEnabled(true);
            }

            if(!lugarActual.getId().equals(ultimoLugarId)) {

                ultimoLugarId = lugarActual.getId();

                if(lugarActual.getSonido() != null) {
                    mListener.emiteSonido(getResources().getIdentifier("zeta_" + lugarActual.getSonido(), "raw", getActivity().getPackageName()));
                }
            }
            ///mListener.emiteSonido(getResources().getIdentifier("zeta_sonido_" + "prueba", "raw", getActivity().getPackageName()));
        }
    }

    public void realizaAccion(int actionType, int actionNumber) {

        new RealizaAccion().execute(actionType, actionNumber);
    }

    /**
     * Clase interna, para realizar algunas acciones. Extiende de AsyncTask,
     * para no saturar el thread del UI.
     */
    private class RealizaAccion extends AsyncTask<Integer, Void, Void> {

        private String casoResuelto = null;

        /**
         * Tarea a realizar en background, fuera del thread del UI. Realiza
         * la acción, y devuelve el nombre del caso resuelto, o null.
         *
         * @param params  actionType, actionNumber
         * @return  nada   nombre del caso resuelto, o null
         */
        @Override
        protected Void doInBackground(Integer... params) {
            // Tomar parámetros
            int actionType = params[0];
            int actionNumber = params[1];

            // Caso resuelto, por defecto: ninguno
            int casoResueltoId = 0;

            // Abrir base de datos
            BaseDatos bd = new BaseDatos(getActivity(), false);

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

                    Objeto objLugar = lugarActualObjetos.get(actionNumber);

                    //
                    objLugar.setLugar(Lugar.BOLSILLO);
                    bd.updateObjeto(objLugar);

                    //
                    casoResueltoId = Zeta.objetoTomado(bd, objLugar);
                    break;
                case AccionesFragment.ACCION_DEJAR:
                    Objeto objBolsillo = protaObjetos.get(actionNumber);
                    objBolsillo.setLugar(lugarActual.getId());
                    bd.updateObjeto(objBolsillo);

                    //
                    casoResueltoId = Zeta.objetoDejado(bd, objBolsillo);
                    break;
                case AccionesFragment.ACCION_OTRAS:

                    casoResueltoId = Zeta.doAccion(bd, otrasAcciones.get(actionNumber).getId());

                    break;
                default :
                    break;
            }

            //
            if(casoResueltoId != 0) {

                Caso caso = bd.getCaso(casoResueltoId);

                caso.setResuelto(true);

                bd.updateCaso(caso);

                casoResuelto = caso.getNombre();
            }

            //
            bd.cerrar();

            //
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

            if(casoResuelto != null) {

                // Mostrar mensaje, si se ha resuelto un caso

                //mensaje(getString(R.string.dialogo_caso_resuelto_titulo),
                //        getString(R.string.dialogo_caso_resuelto_texto) + "\n\n" + nombreCasoResuelto + ".");

                //mListener.casoResuelto(casoResuelto);

                mListener.emiteSonido(R.raw.caso_resuelto);

                Mensaje.continuar(getActivity(), R.drawable.ic_check,
                        getString(R.string.dialogo_caso_resuelto_titulo),
                        getString(R.string.dialogo_caso_resuelto_texto) + " '" + casoResuelto + "'!",
                        null);
            }
        }
    }



    private void cambiaLugar(BaseDatos bd, String lugarId) {

        String lugarOrigenId = lugarActual.getId();

        prota.setLugar(lugarId);

        bd.updateActor(prota);

        Zeta.protaCambiaLugar(bd, lugarOrigenId, prota.getLugar());
    }
}
