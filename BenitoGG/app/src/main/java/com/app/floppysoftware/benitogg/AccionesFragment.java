package com.app.floppysoftware.benitogg;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

/**
 * Fragmento que implementa la lista de acciones de una escena.
 *
 * @author   Miguel I. García López
 * @version  2.0
 * @since    18 Mar 2016
 */
public class AccionesFragment extends Fragment {

    // Log
    private static final String TAG = "AccionesFragment";

    // Códigos de las acciones
    public static final int ACCION_NORTE = 0;  // Ir hayNorte
    public static final int ACCION_SUR   = 1;  // Ir haySur
    public static final int ACCION_ESTE  = 2;  // Ir hayEste
    public static final int ACCION_OESTE = 3;  // Ir hayOeste
    public static final int ACCION_TOMAR = 4;  // Tomar objeto
    public static final int ACCION_DEJAR = 5;  // Dejar objeto
    public static final int ACCION_OTRAS = 6;  // Otras acciones
    public static final int ACCION_MAPA  = 7;  // Mostrar mapa
    public static final int ACCION_CASOS = 8;  // Mostrar casos

    // Botones de dirección
    private Button buttonNorte;
    private Button buttonSur;
    private Button buttonEste;
    private Button buttonOeste;

    // True si hay salida en la dirección indicada
    private boolean hayNorte, haySur, hayEste, hayOeste;

    // Botones
    private Button buttonTomarObjeto;      // Tomar objeto
    private Button buttonDejarObjeto;      // Dejar objeto
    private Button buttonOtrasAcciones;    // Otras acciones
    private Button buttonInventario;       // Inventario

    // ArrayList de los objetos y acciones
    private ArrayList<Objeto> arrayListObjetosLugar;      // Tomar objeto: objetos en el lugar
    private ArrayList<Objeto> arrayListObjetosBolsillo;   // Dejar objeto: objetos que lleva el protagonista
    private ArrayList<Accion> arrayListOtrasAcciones;     // Otras acciones: lista de acciones posibles

    // Flags que indican el estado de visualización de las acciones
    private boolean fragmentOk;         // True si el fragment está listo para refrescarse
    private boolean refrescoPendiente;  // True si el fragment está pendiente de refresco

    // Lístener que ha de implementar la activity, para
    // comunicarle la acción seleccionada
    private OnAccionesListener mListener;

    /**
     * Interfaz que ha de implementar la activity que utilice
     * este fragment.
     */
    public interface OnAccionesListener {

        // Acción seleccionada
        public void onAccionSeleccionada(int accionId, int param);

        // Emitir un sonido
        public void emiteSonido(int resId);
    }

    /**
     * Constructor.
     */
    public AccionesFragment() {

        // Nada
    }

    /**
     * Fijar las acciones disponibles.
     *
     * @param norte  True si hay salida por el norte
     * @param sur    True si hay salida por el sur
     * @param este   True si hay salida por el este
     * @param oeste  True si hay salida por el oeste
     * @param objetosLugar     Objetos en el lugar
     * @param objetosBolsillo  Objetos que lleva el protagonista
     * @param otrasAcciones    Otras acciones
     */
    public void setAcciones(boolean norte, boolean sur, boolean este, boolean oeste,
                            ArrayList<Objeto> objetosLugar,
                            ArrayList<Objeto> objetosBolsillo,
                            ArrayList<Accion> otrasAcciones) {

        // Salidas
        hayNorte = norte;
        haySur = sur;
        hayEste = este;
        hayOeste = oeste;

        // Objetos del lugar
        arrayListObjetosLugar = objetosLugar;

        // Objetos que lleva el protagonista
        arrayListObjetosBolsillo = objetosBolsillo;

        // Otras acciones
        arrayListOtrasAcciones = otrasAcciones;

        // Indicar que el fragment está pendiente de refresco
        refrescoPendiente = true;

        // Log
        Log.d(TAG, "Datos recibidos; refresco pendiente");

        // Refrescar
        refrescar();
    }

    /**
     * Método llamado al crear el fragment.
     *
     * @param savedInstanceState   Estado previamente guardado
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Llamar a la superclase
        super.onCreate(savedInstanceState);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflar el layout del fragment
        View v = inflater.inflate(R.layout.fragment_acciones, container, false);

        // Tomar las referencias de los botones de direcciones
        buttonNorte = (Button) v.findViewById(R.id.buttonNorte);
        buttonSur   = (Button) v.findViewById(R.id.buttonSur);
        buttonOeste = (Button) v.findViewById(R.id.buttonOeste);
        buttonEste  = (Button) v.findViewById(R.id.buttonEste);

        // Fijar listeners
        buttonNorte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAccionSeleccionada(ACCION_NORTE, -1);
            }
        });

        buttonSur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAccionSeleccionada(ACCION_SUR, -1);
            }
        });

        buttonEste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAccionSeleccionada(ACCION_ESTE, -1);
            }
        });

        buttonOeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAccionSeleccionada(ACCION_OESTE, -1);
            }
        });

        // Botones de las acciones
        buttonTomarObjeto   = (Button) v.findViewById(R.id.buttonTomarObjeto);
        buttonDejarObjeto   = (Button) v.findViewById(R.id.buttonDejarObjeto);
        buttonOtrasAcciones = (Button) v.findViewById(R.id.buttonOtrasAcciones);

        // Fijar listeners
        buttonTomarObjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Tomar objeto
                tomarObjeto();
            }
        });

        buttonDejarObjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Dejar objeto
                dejarObjeto();
            }
        });

        buttonOtrasAcciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Realizar acción
                otraAccion();
            }
        });

        // Botón para el inventario
        buttonInventario = (Button) v.findViewById(R.id.buttonInventario);

        // Listener para el inventario
        buttonInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Mostrar el inventario
                verInventario();
            }
        });

        // Botón para el mapa
        Button buttonMapa = (Button) v.findViewById(R.id.buttonMapa);

        // Listener para el mapa
        buttonMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Mostrar el mapa
                mListener.onAccionSeleccionada(ACCION_MAPA, -1);
            }
        });

        // Botón para mostrar la lista de casos
        Button buttonCasos = (Button) v.findViewById(R.id.buttonCasos);

        // Listener para el botón de la lista de casos
        buttonCasos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Mostrar lista de casos
                mListener.onAccionSeleccionada(ACCION_CASOS, -1);
            }
        });

        // Devolver layout inflado
        return v;
    }

    /**
     * Método llamado cuando el fragment es enlazado a la activity
     *
     * @param activity  Activity
     */
    @Override
    public void onAttach(Activity activity) {

        // Llamar a la superclase
        super.onAttach(activity);

        // Comprobar que la activity ha implementado el listener
        try {
            mListener = (OnAccionesListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnAccionesListener");
        }
    }

    /**
     * Método llamado cuando el fragment es desenlazado de la activity
     */
    @Override
    public void onDetach() {

        // Llamar a la superclase
        super.onDetach();

        // Invalidar listener
        mListener = null;
    }

    /**
     * Método llamado al reanudar el fragment.
     */
    @Override
    public void onResume() {

        // Llamar a la superclase
        super.onResume();

        // Indicar que el fragment está listo
        fragmentOk = true;

        // Log
        Log.d(TAG, "onResume; fragmento ok");

        // Refrescar
        refrescar();
    }

    /**
     * Método llamado cuando el fragment entra en pausa.
     */
    @Override
    public void onPause() {

        // Llamar a la superclase
        super.onPause();

        // Indicar que el fragment no está listo
        fragmentOk = false;
    }

    /**
     * Refrescar el fragment.
     */
    private void refrescar() {

        // Log
        Log.d(TAG, "fragmentOk: " + fragmentOk + "; refrescoPendiente: " + refrescoPendiente);

        // Refrescar el fragment, si está listo y el refresco está pendiente
        if(fragmentOk && refrescoPendiente) {

            // Log
            Log.d(TAG, "Refrescando");

            // Habilitar / deshabilitar los botones de las direcciones.
            buttonNorte.setEnabled(hayNorte);
            buttonSur.setEnabled(haySur);
            buttonEste.setEnabled(hayEste);
            buttonOeste.setEnabled(hayOeste);

            // Habilitar / deshabilitar los botones de acción.
            buttonTomarObjeto.setEnabled(!arrayListObjetosLugar.isEmpty());
            buttonDejarObjeto.setEnabled(!arrayListObjetosBolsillo.isEmpty());
            buttonOtrasAcciones.setEnabled(!arrayListOtrasAcciones.isEmpty());

            // Habilitar / deshabilitar el botón de inventario.
            buttonInventario.setEnabled(!arrayListObjetosBolsillo.isEmpty());

            // Indicar que se ha refrescado el fragment
            refrescoPendiente = false;
        }
    }

    /**
     * Tomar un objeto.
     */
    private void tomarObjeto() {

        // Generar una lista de nombres de objetos
        ArrayList<String> nombreObjetosLugar = new ArrayList<>();

        for(Objeto obj: arrayListObjetosLugar) {
            nombreObjetosLugar.add(obj.getNombre().concat("."));
        }

        // Listener de selección de un objeto de la lista
        DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener() {

            /**
             * Método que se llamará cuando se seleccione un
             * elemento de la lista.
             *
             * @param dialog  Diálogo
             * @param which   Nº de elemento
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Comprobar que el objeto puede ser tomado
                int errId = Zeta.puedeTomarObjeto(arrayListObjetosLugar.get(which), arrayListObjetosBolsillo);

                // Mostrar mensaje de error, si no puede ser tomado
                if(Zeta.puedeTomarObjeto(arrayListObjetosLugar.get(which), arrayListObjetosBolsillo) > 0) {

                    // Sonido
                    mListener.emiteSonido(R.raw.error);

                    // Mostrar mensaje de error
                    Mensaje.continuar(getActivity(), R.drawable.ic_forbidden,
                            getString(R.string.acciones_titulo_tomar_objeto),
                            getString(errId),
                            null);
                } else {

                    // El objeto puede ser tomado

                    // Indicar a la activity la acción y el elemento
                    mListener.onAccionSeleccionada(ACCION_TOMAR, which);
                }
            }
        };

        // Mostrar el cuadro de diálogo de selección del objeto
        Mensaje.seleccionar(getActivity(),
                R.drawable.ic_add,
                getString(R.string.acciones_titulo_tomar_objeto),
                nombreObjetosLugar.toArray(new CharSequence[nombreObjetosLugar.size()]),
                onClick);
    }

    /**
     * Dejar un objeto.
     */
    private void dejarObjeto() {

        // Generar la lista de nombres de objetos
        ArrayList<String> nombreObjetosBolsillo = new ArrayList<>();

        for(Objeto obj: arrayListObjetosBolsillo) {
            nombreObjetosBolsillo.add(obj.getNombre().concat("."));
        }

        // Listener de selección de un objeto de la lista
        DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener() {

            /**
             * Método que se llamará cuando se seleccione un
             * elemento de la lista.
             *
             * @param dialog  Diálogo
             * @param which   Nº de elemento
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    // Indicar a la activity la acción y el elemento
                    mListener.onAccionSeleccionada(ACCION_DEJAR, which);
            }
        };

        // Mostrar el cuadro de diálogo de selección del objeto
        Mensaje.seleccionar(getActivity(),
                R.drawable.ic_remove,
                getString(R.string.acciones_titulo_dejar_objeto),
                nombreObjetosBolsillo.toArray(new CharSequence[nombreObjetosBolsillo.size()]),
                onClick);
    }

    /**
     * Realizar una accion.
     */
    private void otraAccion() {

        // Generar lista de nombres de objetos
        ArrayList<String> nombreOtrasAcciones = new ArrayList<>();

        for(Accion acc: arrayListOtrasAcciones) {
            nombreOtrasAcciones.add(getString(acc.getStringId()).concat("."));
        }

        // Listener de selección de un objeto de la lista
        DialogInterface.OnClickListener onClick = new DialogInterface.OnClickListener() {

            /**
             * Método que se llamará cuando se seleccione un
             * elemento de la lista.
             *
             * @param dialog  Diálogo
             * @param which   Nº de elemento
             */
            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Indicar a la activity la acción y el elemento
                mListener.onAccionSeleccionada(ACCION_OTRAS, which);
            }
        };

        // Mostrar el cuadro de diálogo de selección del objeto
        Mensaje.seleccionar(getActivity(),
                R.drawable.ic_action,
                getString(R.string.acciones_titulo_otras_acciones),
                nombreOtrasAcciones.toArray(new CharSequence[nombreOtrasAcciones.size()]),
                onClick);
    }

    /**
     * Mostrar la lista de cosas que lleva el protagonista.
     */
    private void verInventario() {

        // Cadena de texto con la lista de objetos que lleva el protagonista
        String inventario = "";

        // Construir la cadena de texto
        for(Objeto cosa : arrayListObjetosBolsillo) {

            // Añadir una línea por objeto
            inventario = inventario.concat(cosa.getNombre().concat(".\n"));
        }

        // Mostrar mensaje con el inventario
        Mensaje.continuar(getActivity(), R.drawable.ic_bag, getString(R.string.acciones_titulo_inventario), inventario, null);
    }
}
