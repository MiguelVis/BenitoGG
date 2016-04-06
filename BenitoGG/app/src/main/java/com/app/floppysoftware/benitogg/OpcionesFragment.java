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
 * A simple {@link Fragment} subclass.
 */
public class OpcionesFragment extends Fragment {

    private EditText editTextNombre;
    private CheckBox checkBoxSonido;
    private CheckBox checkBoxZurdo;
    private CheckBox checkBoxVertical;
    private Button buttonReset;

    //
    private boolean esTablet = false;

    // Lístener que ha de implementar la activity
    private OnOpcionesInteractionListener mListener;

    /**
     * Interfaz que ha de implementar la activity que utilice
     * este fragment.
     */
    public interface OnOpcionesInteractionListener {

        public void onCambioVertical();
        public void onCambioZurdo();
        public void onCambioSonido();
        public void onResetJuego();
    }


    public OpcionesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_opciones, container, false);

        // Averiguar si el dispositivo es una tablet o móvil
        esTablet = getResources().getBoolean(R.bool.isTablet);

        //
        editTextNombre = (EditText) v.findViewById(R.id.editTextNombre);

        //
        checkBoxSonido = (CheckBox) v.findViewById(R.id.checkBoxSonido);
        checkBoxZurdo = (CheckBox) v.findViewById(R.id.checkBoxZurdo);
        checkBoxVertical = (CheckBox) v.findViewById(R.id.checkBoxVertical);

        //
        buttonReset = (Button) v.findViewById(R.id.buttonReset);


        //
        checkBoxSonido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Preferencias.setSonido(getActivity(), checkBoxSonido.isChecked());

                //
                mListener.onCambioSonido();
            }
        });

        //
        if(esTablet && !Preferencias.getVertical(getActivity())) {

            checkBoxZurdo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //
                    Preferencias.setZurdo(getActivity(), checkBoxZurdo.isChecked());

                    //
                    mListener.onCambioZurdo();
                }
            });

        } else {
            checkBoxZurdo.setEnabled(false);
        }

        //
        if(esTablet) {

            checkBoxVertical.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //
                    Preferencias.setVertical(getActivity(), checkBoxVertical.isChecked());

                    //
                    mListener.onCambioVertical();
                }
            });
        } else {
            checkBoxVertical.setEnabled(false);
        }

        //
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                //opcionResetBd();
                mListener.onResetJuego();
            }
        });

        //
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

        //
        editTextNombre.setText(Preferencias.getNombre(getActivity()));

        //
        checkBoxSonido.setChecked(Preferencias.getSonido(getActivity()));

        //
        checkBoxZurdo.setChecked(Preferencias.getZurdo(getActivity()));

        //
        checkBoxVertical.setChecked(Preferencias.getVertical(getActivity()));

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnOpcionesInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnOpcionesInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;


    }

    @Override
    public void onPause() {
        super.onPause();

        Preferencias.setNombre(getActivity(), editTextNombre.getText().toString());
    }

    /**********************************
    private void opcionResetBd() {

        //
        AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
        dlg.setTitle(R.string.dialogo_reset_titulo)
                .setMessage(R.string.dialogo_reset_texto)
                .setPositiveButton(R.string.dialogo_si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dejar la BD al estado inicial
                        new ReinicializaJuego().execute();
                    }
                })
                .setNegativeButton(R.string.dialogo_cancelar, null)
                .show();
    }

    private class ReinicializaJuego extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // Dejar la BD al estado inicial
            BaseDatos bd = new BaseDatos(getActivity(), true);
            bd.cerrar();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            AlertDialog.Builder dlg = new AlertDialog.Builder(getActivity());
            dlg.setTitle(R.string.dialogo_reset_titulo)
                    .setMessage(R.string.dialogo_reset_hecho)
                    .setPositiveButton(R.string.dialogo_continuar, null)
                    .show();
        }
    }
     *******************/


}
