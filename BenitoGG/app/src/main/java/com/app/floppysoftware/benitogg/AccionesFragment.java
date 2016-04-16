package com.app.floppysoftware.benitogg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;

import java.util.ArrayList;

/**
 * Fragmento que implementa la lista de acciones de una escena.
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

    // ImageViews de los botones de dirección
    private ImageView imageViewNorte;
    private ImageView imageViewSur;
    private ImageView imageViewEste;
    private ImageView imageViewOeste;

    // True si hay salida en la dirección indicada
    private boolean hayNorte, haySur, hayEste, hayOeste;

    // Botones
    private Button buttonTomarObjeto;      // Tomar objeto
    private Button buttonDejarObjeto;      // Dejar objeto
    private Button buttonOtrasAcciones;    // Otras acciones
    private Button buttonInventario;       // Inventario

    // Popups para los botones de acción
    private PopupMenu popupMenuTomarObjeto;    // Tomar objeto
    private PopupMenu popupMenuDejarObjeto;    // Dejar objetos
    private PopupMenu popupMenuOtrasAcciones;  // Otra acciones

    // ArrayList para las acciones
    private ArrayList<String> arrayListObjetosLugar;      // Tomar objeto: objetos en el lugar
    private ArrayList<String> arrayListObjetosBolsillo;   // Dejar objeto: objetos que lleva el protagonista
    private ArrayList<String> arrayListOtrasAcciones;     // Otras acciones: lista de acciones posibles

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
                            ArrayList<String> objetosLugar,
                            ArrayList<String> objetosBolsillo,
                            ArrayList<String> otrasAcciones) {

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

        // Tomar las referencias de los ImageView de direcciones
        imageViewNorte = (ImageView) v.findViewById(R.id.imageViewNorte);
        imageViewSur = (ImageView) v.findViewById(R.id.imageViewSur);
        imageViewEste = (ImageView) v.findViewById(R.id.imageViewEste);
        imageViewOeste = (ImageView) v.findViewById(R.id.imageViewOeste);

        // Fijar listener para Norte
        imageViewNorte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAccionSeleccionada(ACCION_NORTE, 0);
            }
        });

        // Fijar listener para Sur
        imageViewSur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAccionSeleccionada(ACCION_SUR, 0);
            }
        });

        // Fijar listener para Este
        imageViewEste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAccionSeleccionada(ACCION_ESTE, 0);
            }
        });

        // Fijar listener para Oeste
        imageViewOeste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAccionSeleccionada(ACCION_OESTE, 0);
            }
        });

        // Botones de las acciones
        buttonTomarObjeto   = (Button) v.findViewById(R.id.buttonTomarObjeto);
        buttonDejarObjeto   = (Button) v.findViewById(R.id.buttonDejarObjeto);
        buttonOtrasAcciones = (Button) v.findViewById(R.id.buttonOtrasAcciones);

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
                startActivity(new Intent(getActivity(), MapaActivity.class));
            }
        });

        // Botón para mostrar la lista de casos
        Button buttonCasos = (Button) v.findViewById(R.id.buttonCasos);

        // Listener para el botón de la lista de casos
        buttonCasos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Mostrar lista de casos
                startActivity(new Intent(getActivity(), CasosActivity.class));
            }
        });

        // Devolver layout inflado
        return v;
    }

    /**
     * Método llamadao cuando el fragment es enlazado a la activity
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

            // Habilitar / deshabilitar los imageView de las direcciones,
            // según hayan o no salidas.
            imageViewNorte.setEnabled(hayNorte);
            imageViewSur.setEnabled(haySur);
            imageViewEste.setEnabled(hayEste);
            imageViewOeste.setEnabled(hayOeste);

            // Actualizar el Popup para tomar objetos
            popupMenuTomarObjeto = creaPopupMenu(buttonTomarObjeto, arrayListObjetosLugar, ACCION_TOMAR);

            // Actualizar el Popup para dejar objetos
            popupMenuDejarObjeto = creaPopupMenu(buttonDejarObjeto, arrayListObjetosBolsillo, ACCION_DEJAR);

            // Actualizar el Popup de otras acciones
            popupMenuOtrasAcciones = creaPopupMenu(buttonOtrasAcciones, arrayListOtrasAcciones, ACCION_OTRAS);

            // Habilitar / deshabilitar el botón de inventario,
            // según lleve objetos o no el protagonista.
            buttonInventario.setEnabled(!arrayListObjetosBolsillo.isEmpty());

            // Indicar que se ha refrescado el fragment
            refrescoPendiente = false;
        }
    }

    /**
     * Listener para los botones de acción que tienen un Popup asociado.
     */
    private class BotonListener implements View.OnClickListener {

        /**
         * Método llamado al clickar el botón.
         *
         * @param v  View del botón
         */
        @Override
        public void onClick(View v) {

            // Mostrar el Popup correspondiente
            if (v == buttonTomarObjeto) {
                popupMenuTomarObjeto.show();
            } else if (v == buttonDejarObjeto) {
                popupMenuDejarObjeto.show();
            } else if (v == buttonOtrasAcciones) {
                popupMenuOtrasAcciones.show();
            }
        }
    };

    /**
     * Crear Popup asociado a un botón. En el caso de que la lista de items
     * del Popup esté vacía, deshabilitará el botón, e invalidará el listener
     * del mismo.
     *
     * @param button      Botón
     * @param items       Elementos del Popup
     * @param accionId    Id de la acción asociada al botón
     *
     * @return            Popup creado, o null si la lista de items está vacía
     */
    private PopupMenu creaPopupMenu(Button button, ArrayList<String> items, int accionId) {

        // Comprobar si la lista de items está vacía
        if(items.isEmpty()) {

            // Está vacía

            // Deshabilitar el botón
            button.setEnabled(false);

            // Invalidar el listener del botón
            button.setOnClickListener(null);

            // Devolver null
            return null;
        }

        // La lista contiene items

        // Habilitar el botón
        button.setEnabled(true);

        // Fijar listener del botón
        button.setOnClickListener(new BotonListener());

        // Crear Popup asociado al botón, con la lista de items
        PopupMenu popupMenu = new PopupMenu(getActivity(), button);

        // Añadir tantos elementos al Popup, como items
        // haya en la lista.
        for(int i = 0; i < items.size(); ++i) {

            // Añadir elemento
            popupMenu.getMenu().add(accionId + 1, i + 1, Menu.NONE, items.get(i));
        }

        // Fijar listener del Popup
        popupMenu.setOnMenuItemClickListener(new PopupMenuListener());

        // Devolver referencia al Popup
        return popupMenu;
    }


    /**
     * Listener para los Popup asociados a botones de acción.
     */
    private class PopupMenuListener implements PopupMenu.OnMenuItemClickListener {

        /**
         * Método llamado cuando se seleccione un elemento.
         *
         * @param item   Número de elemento seleccionado
         * @return       True si se ha tratado el evento
         */
        public boolean onMenuItemClick(MenuItem item) {

            // Id de la acción que corresponde al Popup
            int accionId = item.getGroupId() - 1;

            // Posición del elemento en la lista
            int pos = 0;

            // Filtrar las acciones posibles, y
            // obtener la posición del elemento seleccionado.
            switch(accionId) {

                case ACCION_TOMAR:  // Tomar objeto
                case ACCION_DEJAR:  // Dejar objeto
                case ACCION_OTRAS:  // Otros objetos

                    // Tomar posición del elemento seleccionado
                    pos = item.getItemId() - 1;
                    break;
                default :

                    // Indicar que no ha sido tratado el evento
                    return false;
            }

            // Indicar a la activity la acción y elemento seleccionados
            mListener.onAccionSeleccionada(accionId, pos);

            // Indicar que ha sido tratado el evento
            return true;
        }
    }

    /**
     * Mostrar el inventario (lista de cosas que lleva el protagonista).
     */
    private void verInventario() {

        // Cadena de texto que contendrá la lista de objetos
        // que lleva el protagonista.
        String inventario = "";

        // Construir la cadena de texto
        for(String cosa : arrayListObjetosBolsillo) {

            // Añadir una línea por objeto
            inventario = inventario.concat(cosa.concat("\n"));
        }

        // Crear un AlertDialog para la lisa de objetos
        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());

        // Mostrar la lista de objetos mediante un AlertDialog
        dlg.setTitle(R.string.dialogo_inventario_titulo)
                .setIcon(R.drawable.ic_information)
                .setMessage(inventario)
                .setPositiveButton(R.string.dialogo_continuar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Nada
                    }
                })
                .show();
    }
}
