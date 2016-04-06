package com.app.floppysoftware.benitogg;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MenuFragment extends Fragment {

    //
    public static final int MENU_OPCION_JUGAR = 1;
    public static final int MENU_OPCION_OPCIONES = 2;
    public static final int MENU_OPCION_INFO = 3;

    //
    private static final String KEY_JUGAR = "tagJugar";
    private static final String KEY_OPCIONES = "tagOpciones";
    private static final String KEY_INFO = "tagInfo";

    //
    private Button buttonJugar;
    private Button buttonOpciones;
    private Button buttonInfo;

    // Variable que indica si el dispositivo es una tablet
    private boolean esTablet = false;

    //
    private int forcedOption = -1;

    private OnFragmentInteractionListener mListener;

    /**
     * Interfaz que ha de implementar la activity que utilice
     * este fragment.
     */
    public interface OnFragmentInteractionListener {

        public void onOptionSelected(int opcion);
    }

    /**
     *
     * @param opcion
     */
    public void forceOpcion(int opcion) {

        if(buttonJugar == null || buttonOpciones == null || buttonInfo == null) {

            forcedOption = opcion;

            return;
        }

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

        forcedOption = -1;

    }

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Averiguar si el dispositivo es una tablet o móvil
        esTablet = getResources().getBoolean(R.bool.isTablet) && !Preferencias.getVertical(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        //
        buttonJugar = (Button) v.findViewById(R.id.buttonJugar);
        buttonOpciones = (Button) v.findViewById(R.id.buttonOpciones);
        buttonInfo = (Button) v.findViewById(R.id.buttonInfo);

        //
        buttonJugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOpcion(MENU_OPCION_JUGAR);
            }
        });

        //
        buttonOpciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickOpcion(MENU_OPCION_OPCIONES);
            }
        });

        //
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               clickOpcion(MENU_OPCION_INFO);
            }
        });

        //
        if(savedInstanceState != null) {

            buttonJugar.setEnabled(savedInstanceState.getBoolean(KEY_JUGAR, true));
            buttonOpciones.setEnabled(savedInstanceState.getBoolean(KEY_OPCIONES, true));
            buttonInfo.setEnabled(savedInstanceState.getBoolean(KEY_INFO, true));
        }

        //
        if(forcedOption != -1) {
            forceOpcion(forcedOption);
        }

        //
        return v;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //
        outState.putBoolean(KEY_JUGAR, buttonJugar.isEnabled());
        outState.putBoolean(KEY_OPCIONES, buttonOpciones.isEnabled());
        outState.putBoolean(KEY_INFO, buttonInfo.isEnabled());
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void clickOpcion(int opcionId) {

        //
        if(esTablet) {
            buttonJugar.setEnabled(!(opcionId == MENU_OPCION_JUGAR));
            buttonOpciones.setEnabled(!(opcionId == MENU_OPCION_OPCIONES));
            buttonInfo.setEnabled(!(opcionId == MENU_OPCION_INFO));
        }

        //
        mListener.onOptionSelected(opcionId);
    }

}
