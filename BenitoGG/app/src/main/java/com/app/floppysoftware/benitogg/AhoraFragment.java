package com.app.floppysoftware.benitogg;


import android.app.Activity;
import android.app.AlertDialog;
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

    private Lugar lugarActual = null;
    private ArrayList<Objeto> lugarActualObjetos = null;
    private ArrayList<Actor> lugarActualActores = null;

    //private ArrayList<Dicho> lugarActualDichos = null;

    private ArrayList<String> lugarActualNombreObjetosLugar;

    private ArrayList<String> lugarActualNombreObjetosBolsillo;

    private ArrayList<String> accionesTitulos = null;
    private ArrayList<Integer> accionesIds;

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
            ArrayList<String> objetosLugar,
            ArrayList<String> objetosBolsillo,
            ArrayList<String> otrasAcciones);

        public void emiteSonido(int resId);
    }

    public AhoraFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Averiguar si el dispositivo es una tablet o móvil
        esTablet = getResources().getBoolean(R.bool.isTablet)  && !Preferencias.getVertical(getActivity());

        // Inflate the layout for this fragment
        View v = inflater.inflate(esTablet ? R.layout.fragment_ahora_tablet : R.layout.fragment_ahora, container, false);


        textViewTitulo = (TextView) v.findViewById(R.id.textViewTitulo);
        imageViewImagen = (ImageView) v.findViewById(R.id.imageViewImagen);
        textViewDetalle = (TextView) v.findViewById(R.id.textViewDetalle);

        // Botones para la versión móvil
        if(!esTablet) {
            imageViewNorte = (ImageView) v.findViewById(R.id.imageViewNorte);
            imageViewSur = (ImageView) v.findViewById(R.id.imageViewSur);
            imageViewEste = (ImageView) v.findViewById(R.id.imageViewEste);
            imageViewOeste = (ImageView) v.findViewById(R.id.imageViewOeste);
            imageViewMas = (ImageView) v.findViewById(R.id.imageViewMas);

            // Lísteners
            imageViewNorte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realizaAccion(AccionesFragment.ACTION_TYPE_GO, AccionesFragment.ACTION_TYPE_GO_NORTH);
                }
            });

            imageViewSur.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realizaAccion(AccionesFragment.ACTION_TYPE_GO, AccionesFragment.ACTION_TYPE_GO_SOUTH);
                }
            });

            imageViewEste.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realizaAccion(AccionesFragment.ACTION_TYPE_GO, AccionesFragment.ACTION_TYPE_GO_EAST);
                }
            });

            imageViewOeste.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    realizaAccion(AccionesFragment.ACTION_TYPE_GO, AccionesFragment.ACTION_TYPE_GO_WEST);
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
                    + " must implement OnActionSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;


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
                lugarActualNombreObjetosLugar,
                lugarActualNombreObjetosBolsillo,
                accionesTitulos);
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

            // Leer actores en el lugar
            lugarActualActores = bd.getActores(lugarActual.getId());

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
            accionesTitulos = new ArrayList<>();
            accionesIds = new ArrayList<>();

            // Parcheo acciones
            //ArrayList<Accion> parcheAcciones = Zeta.getAcciones(lugarActual, lugarActualActores, lugarActualObjetos, protaObjetos);

            for(Accion ac : parcheAcciones) {
                otraAccion(ac.getId(), ac.getStringId());
            }

            // Lista de nombres de objetos que hay en el lugar
            lugarActualNombreObjetosLugar = new ArrayList<>();

            for(Objeto objLugar : lugarActualObjetos) {
                lugarActualNombreObjetosLugar.add(objLugar.getNombre());
            }

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

            if(tituloEste != null) {
                lugarLineas.add("   " + getString(R.string.escena_este) + " " + tituloEste + ".");
            }

            if(tituloOeste != null) {
                lugarLineas.add("   " + getString(R.string.escena_oeste) + " " + tituloOeste + ".");
            }

            // Lista de nombres de objetos que hay en el bolsillo
            lugarActualNombreObjetosBolsillo = new ArrayList<>();

            for(Objeto objBolsillo : protaObjetos) {
                lugarActualNombreObjetosBolsillo.add(objBolsillo.getNombre());
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
            }

            ///mListener.emiteSonido(getResources().getIdentifier("zeta_sonido_" + "prueba", "raw", getActivity().getPackageName()));
        }
    }

    private void otraAccion(int id, String titulo) {
        accionesTitulos.add(titulo);
        accionesIds.add(id);
    }

    private void otraAccion(int id, int tituloId) {
        otraAccion(id, getString(tituloId));
    }

    public void realizaAccion(int actionType, int actionNumber) {

        new RealizaAccion().execute(actionType, actionNumber);
    }

    /**
     * Clase interna, para realizar algunas acciones. Extiende de AsyncTask,
     * para no saturar el thread del UI.
     */
    private class RealizaAccion extends AsyncTask<Integer, Void, Void> {

        private int tomarObjetoError = 0;
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
                case AccionesFragment.ACTION_TYPE_GO :

                    // Lugar actual (será el lugar de origen)
                    String idLugarOrigen = lugarActual.getId();

                    switch (actionNumber) {
                        case AccionesFragment.ACTION_TYPE_GO_NORTH :
                            prota.setLugar(lugarActual.getLugarNorte());
                            break;
                        case AccionesFragment.ACTION_TYPE_GO_SOUTH :
                            prota.setLugar(lugarActual.getLugarSur());
                            break;
                        case AccionesFragment.ACTION_TYPE_GO_EAST :
                            prota.setLugar(lugarActual.getLugarEste());
                            break;
                        case AccionesFragment.ACTION_TYPE_GO_WEST :
                            prota.setLugar(lugarActual.getLugarOeste());
                            break;
                    }

                    //
                    bd.updateActor(prota);

                    // Notificar el cambio de lugar
                    Zeta.protaCambiaLugar(bd, idLugarOrigen, prota.getLugar());
                    break;
                case AccionesFragment.ACTION_TYPE_PICK:

                    Objeto objLugar = lugarActualObjetos.get(actionNumber);

                    if((tomarObjetoError = Zeta.puedeTomarObjeto(bd, objLugar, protaObjetos)) == 0) {

                        //
                        objLugar.setLugar(Lugar.BOLSILLO);
                        bd.updateObjeto(objLugar);

                        //
                        casoResueltoId = Zeta.objetoTomado(bd, objLugar, protaObjetos);
                    }
                    break;
                case AccionesFragment.ACTION_TYPE_DROP :
                    Objeto objBolsillo = protaObjetos.get(actionNumber);
                    objBolsillo.setLugar(lugarActual.getId());
                    bd.updateObjeto(objBolsillo);

                    //
                    casoResueltoId = Zeta.objetoDejado(bd, objBolsillo, protaObjetos);
                    break;
                case AccionesFragment.ACTION_TYPE_OTHER :

                    casoResueltoId = Zeta.doAccion(bd, accionesIds.get(actionNumber));

                    break;
                default :
                    break;
            }

            //
            if(casoResueltoId != 0) {

                Caso caso = bd.getCaso(casoResueltoId);

                if(!caso.getResuelto()) {
                    bd.resuelveCaso(casoResueltoId);
                    casoResuelto = caso.getNombre();
                }
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

            if(tomarObjetoError > 0) {

                mensajeContinuar(R.drawable.ic_information, getString(R.string.dialogo_tomar_objeto_titulo), getString(tomarObjetoError));

            } else if(casoResuelto != null) {

                // Mostrar mensaje, si se ha resuelto un caso

                //mensaje(getString(R.string.dialogo_caso_resuelto_titulo),
                //        getString(R.string.dialogo_caso_resuelto_texto) + "\n\n" + nombreCasoResuelto + ".");

                //mListener.casoResuelto(casoResuelto);

                mensajeContinuar(R.drawable.ic_check, getString(R.string.dialogo_caso_resuelto_titulo),
                        getString(R.string.dialogo_caso_resuelto_texto) + "\n\n'" + casoResuelto + "'!");
            }
        }
    }

    private void mensajeContinuar(int icon, String titulo, String texto) {
        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
        dlg.setTitle(titulo)
                .setIcon(icon)
                .setMessage(texto)
                .setPositiveButton(R.string.dialogo_continuar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nada
                    }
                })
                .show();
    }


}
