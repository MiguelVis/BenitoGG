package com.app.floppysoftware.benitogg;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Clase que implementa el apartado de opciones / configuración del menú.
 *
 * @author   Miguel I. García López
 * @version  1.0
 * @since    12 Mar 2016
 */
public class OpcionesFragment extends Fragment {

    // Views
    private EditText editTextNombre;    // Nombre del usuario
    private CheckBox checkBoxSonido;    // Preferencia de sonido
    private CheckBox checkBoxZurdo;     // Preferencia de modo zurdo (sólo tablet en modo horizontal)
    private CheckBox checkBoxVertical;  // Preferencia de modo vertical (sólo tablet)

    // Lístener que ha de implementar la activity
    private OnOpcionesInteractionListener mListener;

    /**
     * Interfaz que ha de implementar la activity que utilice
     * este fragment.
     */
    public interface OnOpcionesInteractionListener {

        // Métodos a implementar

        public void onCambioVertical();  // Cambio de modo vertical / horizontal
        public void onCambioZurdo();     // Cambio de modo diestro / zurdo
        public void onCambioSonido();    // Cambio de preferencia de sonido
        public void onResetJuego();      // Resetear la base de datos
    }

    /**
     * Constructor.
     */
    public OpcionesFragment() {

        // Nada, de momento
    }

    /**
     * Método llamado cuando se ha de crear la vista del fragment.
     *
     * @param inflater   inflater
     * @param container  contenedor
     * @param savedInstanceState  estado previamente guardado
     *
     * @return  layout inflado
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflar el layout del fragment
        View v =  inflater.inflate(R.layout.fragment_opciones, container, false);

        // Averiguar si el dispositivo es una tablet
        boolean esTablet = getResources().getBoolean(R.bool.isTablet);

        // View del nombre de usuario
        editTextNombre = (EditText) v.findViewById(R.id.editTextNombre);

        // View de la preferencia de sonido
        checkBoxSonido = (CheckBox) v.findViewById(R.id.checkBoxSonido);

        // View de la preferencia del modo zurdo / diestro
        checkBoxZurdo = (CheckBox) v.findViewById(R.id.checkBoxZurdo);

        // View de la preferencia del modo vertical / horizontal
        checkBoxVertical = (CheckBox) v.findViewById(R.id.checkBoxVertical);

        // View para reiniciar la base de datos
        Button buttonReset = (Button) v.findViewById(R.id.buttonReset);

        // Listener para el CheckBox del sonido
        checkBoxSonido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Cambiar la preferencia de sonido, según el estado
                // del CheckBox.
                Preferencias.setSonido(getActivity(), checkBoxSonido.isChecked());

                // Informar a la activity
                mListener.onCambioSonido();
            }
        });

        // Listener para el CheckBox del modo zurdo / diestro,
        // sólo si el dispositivo es una tablet, y está activado el
        // modo horizontal.
        if(esTablet && !Preferencias.getVertical(getActivity())) {

            // Establecer listener
            checkBoxZurdo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Cambiar la preferencia de modo zurdo / diestro
                    Preferencias.setZurdo(getActivity(), checkBoxZurdo.isChecked());

                    // Informar a la activity
                    mListener.onCambioZurdo();
                }
            });

        } else {

            // No es una tablet, o está activado el
            // modo vertical: inhabilitar el CheckBox.
            checkBoxZurdo.setEnabled(false);
        }

        // Listener para el CheckBox de modo vertical / horizontal,
        // sólo si el dispositivo es una tablet.
        if(esTablet) {

            // Establecer listener
            checkBoxVertical.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Cambiar la preferencia de modo vertical / horizontal
                    Preferencias.setVertical(getActivity(), checkBoxVertical.isChecked());

                    // Informar a la activity
                    mListener.onCambioVertical();
                }
            });

        } else {

            // No es una tablet: inhabilitar el CheckBox
            checkBoxVertical.setEnabled(false);
        }

        // Listener para el botón de reset de la base de datos
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Informar a la activity
                mListener.onResetJuego();
            }
        });

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

        // Indicar el nombre del usuario
        editTextNombre.setText(Preferencias.getNombre(getActivity()));

        // Indicar la preferencia de sonido
        checkBoxSonido.setChecked(Preferencias.getSonido(getActivity()));

        // Indicar la preferencia de modo zurdo / diestro
        checkBoxZurdo.setChecked(Preferencias.getZurdo(getActivity()));

        // Indicar la preferencia de modo vertical / horizontal
        checkBoxVertical.setChecked(Preferencias.getVertical(getActivity()));
    }

    /**
     * Método llamado cuando el fragment es enlazado a la activity.
     *
     * @param activity  activity
     */
    @Override
    public void onAttach(Activity activity) {

        // Llamar a la superclase
        super.onAttach(activity);

        // Comprobar que la activity ha implementado
        // el listener para comunicarse.
        try {

            // Tomar la referencia del listener
            mListener = (OnOpcionesInteractionListener) activity;

        } catch (ClassCastException e) {

            // No ha sido implementado
            throw new ClassCastException(activity.toString()
                    + " must implement OnOpcionesInteractionListener");
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
     * Método llamado cuando el fragment entra en pausa.
     */
    @Override
    public void onPause() {

        // Llamar a la superclase
        super.onPause();

        // Grabar en las preferencias el nombre del usuario (el resto de preferencias
        // se guarda en el listener correspondiente).
        Preferencias.setNombre(getActivity(), editTextNombre.getText().toString());
    }
}
