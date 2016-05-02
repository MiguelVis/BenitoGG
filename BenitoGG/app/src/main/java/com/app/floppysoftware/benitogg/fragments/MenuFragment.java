package com.app.floppysoftware.benitogg.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.floppysoftware.benitogg.utils.Preferencias;
import com.app.floppysoftware.benitogg.R;

/**
 * Clase que implementa el fragment del menú.
 *
 * @author   Miguel I. García López
 * @version  1.0
 * @since    12 Mar 2016
 */
public class MenuFragment extends Fragment {

    // Id de las opciones del menú
    public static final int MENU_OPCION_JUGAR = 1;    // Jugar
    public static final int MENU_OPCION_OPCIONES = 2; // Opciones / configuración
    public static final int MENU_OPCION_INFO = 3;     // Información

    // Claves para guardar el estado
    // de las opciones del menú, para recrear
    // el fragment.
    private static final String KEY_JUGAR = "tagJugar";       // Opción Jugar
    private static final String KEY_OPCIONES = "tagOpciones"; // Opción Opciones
    private static final String KEY_INFO = "tagInfo";         // Opción Info

    // Botones de las opciones
    private Button buttonJugar;
    private Button buttonOpciones;
    private Button buttonInfo;

    // Variable que indica si el dispositivo es una tablet,
    // con orientación horizontal.
    private boolean esTabletHorizontal = false;

    // Opción a forzar
    private int opcionForzada = -1;

    // Listener para la interfaz que ha de implementar
    // la activity.
    private OnMenuListener mListener;

    /**
     * Interfaz que ha de implementar la activity que utilice
     * este fragment.
     */
    public interface OnMenuListener {

        // Indicar opción seleccionada
        public void onOpcionSeleccionada(int opcion);
    }

    /**
     * Forzar la selección de una opción. Este método
     * puede ser llamado desde fuera del fragment.
     *
     * @param opcion  Opción
     */
    public void forzarOpcion(int opcion) {

        // Si algún botón de opción no está disponible,
        // dejarlo para después.
        if(buttonJugar == null || buttonOpciones == null || buttonInfo == null) {

            // Indicar la opción a forzar
            opcionForzada = opcion;

            // Finalizar
            return;
        }

        // Forzar la selección de la opción
        switch(opcion) {
            case MENU_OPCION_JUGAR :
                buttonJugar.performClick();
                break;
            case MENU_OPCION_OPCIONES :
                buttonOpciones.performClick();
                break;
            case MENU_OPCION_INFO :
                buttonInfo.performClick();
                break;
            default :
                 break;
        }

        // Invalidar
        opcionForzada = -1;
    }

    /**
     * Constructor.
     */
    public MenuFragment() {

        // Nada
    }

    /**
     * Método llamado al crear el fragment.
     *
     * @param savedInstanceState  Estado previamente guardado
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        // Llamar a la superclase
        super.onCreate(savedInstanceState);

        // Averiguar si el dispositivo es una tablet
        // con orientación horizontal.
        esTabletHorizontal = getResources().getBoolean(R.bool.isTablet) && !Preferencias.getVertical(getActivity());
    }

    /**
     * Método llamado para inflar el layout del fragment.
     *
     * @param inflater            Inflater
     * @param container           Contenedor
     * @param savedInstanceState  Estado previamente guardado
     * @return                    Layout inflado
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflar el layout del fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        // Referencias de los botones
        buttonJugar = (Button) v.findViewById(R.id.buttonJugar);
        buttonOpciones = (Button) v.findViewById(R.id.buttonOpciones);
        buttonInfo = (Button) v.findViewById(R.id.buttonInfo);

        // Listener del botón Jugar
        buttonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Seleccionar opción
                clickOpcion(MENU_OPCION_JUGAR);
            }
        });

        // Listener del botón Opciones
        buttonOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Seleccionar opción
                clickOpcion(MENU_OPCION_OPCIONES);
            }
        });

        // Listener del botón Info
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Seleccionar opción
                clickOpcion(MENU_OPCION_INFO);
            }
        });

        // Recuperar estado previo de los botones
        if(savedInstanceState != null) {

            // Fijar estado
            buttonJugar.setEnabled(savedInstanceState.getBoolean(KEY_JUGAR, true));
            buttonOpciones.setEnabled(savedInstanceState.getBoolean(KEY_OPCIONES, true));
            buttonInfo.setEnabled(savedInstanceState.getBoolean(KEY_INFO, true));
        }

        // Forzar selección de una opción
        if(opcionForzada != -1) {

            // Forzar la opción
            forzarOpcion(opcionForzada);
        }

        // Devolver layout
        return v;
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

        // Guardar estado de los botones
        outState.putBoolean(KEY_JUGAR, buttonJugar.isEnabled());
        outState.putBoolean(KEY_OPCIONES, buttonOpciones.isEnabled());
        outState.putBoolean(KEY_INFO, buttonInfo.isEnabled());
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

        // Comprobar que la activity ha implementado
        // el listener.
        try {

            // Tomar la referencia del listener
            mListener = (OnMenuListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnMenuListener");
        }
    }

    /**
     * Método llamado cuando el fragment es desenlazado
     * de la activity.
     */
    @Override
    public void onDetach() {

        // Llamar a la superclase
        super.onDetach();

        // Invalidar listener
        mListener = null;
    }

    /**
     * Hacer click en una opción.
     *
     * @param opcionId  Id de la opción
     */
    private void clickOpcion(int opcionId) {

        // Si la orientación es horizontal, inhabilitar el botón
        // de la opción seleccionada, habilitando los demás.
        if(esTabletHorizontal) {
            //buttonJugar.setEnabled(!(opcionId == MENU_OPCION_JUGAR));
            buttonOpciones.setEnabled(!(opcionId == MENU_OPCION_OPCIONES));
            buttonInfo.setEnabled(!(opcionId == MENU_OPCION_INFO));
        }

        // Indicarlo a la activity
        mListener.onOpcionSeleccionada(opcionId);
    }
}
