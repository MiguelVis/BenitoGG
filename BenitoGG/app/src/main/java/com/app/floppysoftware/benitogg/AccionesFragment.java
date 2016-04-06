package com.app.floppysoftware.benitogg;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.ArrayList;

/**
 * Fragmento que implementa la lista de acciones de una escena.
 */
public class AccionesFragment extends Fragment {

    // Log
    private static final String TAG = "AccionesFragment";

    //
    public static final int ACTION_TYPE_GO = 0;
    public static final int ACTION_TYPE_PICK = 1;
    public static final int ACTION_TYPE_DROP = 2;
    public static final int ACTION_TYPE_OTHER = 3;
    public static final int ACTION_TYPE_MAP = 4;
    public static final int ACTION_TYPE_CASOS = 5;

    public static final int ACTION_TYPE_GO_NORTH = 0;
    public static final int ACTION_TYPE_GO_SOUTH = 1;
    public static final int ACTION_TYPE_GO_EAST = 2;
    public static final int ACTION_TYPE_GO_WEST = 3;

    private ListView listViewAcciones;

    private ImageView imageViewNorte;
    private ImageView imageViewSur;
    private ImageView imageViewEste;
    private ImageView imageViewOeste;

    private boolean norte, sur, este, oeste;

    private Button buttonTomarObjeto;
    private Button buttonDejarObjeto;
    private Button buttonOtrasAcciones;
    private Button buttonMapa;
    private Button buttonCasos;

    private PopupMenu popupMenuTomarObjeto;
    private PopupMenu popupMenuDejarObjeto;
    private PopupMenu popupMenuOtrasAcciones;

    private ArrayList<String> arrayListObjetosLugar;
    private ArrayList<String> arrayListObjetosBolsillo;
    private ArrayList<String> arrayListOtrasAcciones;

    private boolean isReady;
    private boolean refreshPending;


    // Lístener que ha de implementar la activity, para
    // comunicarle la acción seleccionada
    private OnActionSelectedListener mListener;

    /**
     * Interfaz que ha de implementar la activity que utilice
     * este fragment.
     */
    public interface OnActionSelectedListener {

        public void onActionSelected(int actionType, int actionNumber);
    }

    public AccionesFragment() {
        // Required empty public constructor
    }

    /**
     * Fijar la lista de acciones.
     *
     * @param otrasAcciones  lista de acciones
     */
    public void setAcciones(boolean norte, boolean sur, boolean este, boolean oeste,
                            ArrayList<String> objetosLugar,
                            ArrayList<String> objetosBolsillo,
                            ArrayList<String> otrasAcciones) {

        // Salidas
        this.norte = norte;
        this.sur = sur;
        this.este = este;
        this.oeste = oeste;

        //imageViewNorte.setVisibility(norte ? View.VISIBLE : View.GONE);
        //imageViewSur.setVisibility(sur ? View.VISIBLE : View.GONE);
        //imageViewEste.setVisibility(este ? View.VISIBLE : View.GONE);
        //imageViewOeste.setVisibility(oeste ? View.VISIBLE : View.GONE);

        //imageViewNorte.setImageResource(norte ? R.drawable.norte : R.drawable.dir_ninguna);
        //imageViewNorte.setClickable(norte);

        //imageViewSur.setImageResource(sur ? R.drawable.sur : R.drawable.dir_ninguna);
        //imageViewSur.setClickable(sur);

        //imageViewEste.setImageResource(este ? R.drawable.este : R.drawable.dir_ninguna);
        //imageViewEste.setClickable(este);

        //imageViewOeste.setImageResource(oeste ? R.drawable.oeste : R.drawable.dir_ninguna);
        //imageViewOeste.setClickable(oeste);

        // Objetos del lugar
        arrayListObjetosLugar = objetosLugar;

        //
        //popupMenuTomarObjeto = creaPopupMenu(buttonTomarObjeto, objetosLugar, ACTION_TYPE_PICK);

        // Objetos del bolsillo
        arrayListObjetosBolsillo = objetosBolsillo;

        //
        //popupMenuDejarObjeto = creaPopupMenu(buttonDejarObjeto, objetosBolsillo, ACTION_TYPE_DROP);

        // Cambiar la lista de otras acciones
        arrayListOtrasAcciones = otrasAcciones;

        //popupMenuOtrasAcciones = creaPopupMenu(buttonOtrasAcciones, otrasAcciones, ACTION_TYPE_OTHER);

        Log.i(TAG, "Datos recibidos");

        refreshPending = true;

        //
        refresh();
    }


    private void refresh() {

        Log.i(TAG, "refresh - isReady: " + isReady + " refreshPending: " + refreshPending);

        if(isReady && refreshPending) {

            Log.i(TAG, "refresh - Refrescando");

            imageViewNorte.setEnabled(norte);
            //imageViewNorte.setImageResource(norte ? R.drawable.norte : R.drawable.dir_ninguna);
            //imageViewNorte.setClickable(norte);

            imageViewSur.setEnabled(sur);
            //imageViewSur.setImageResource(sur ? R.drawable.sur : R.drawable.dir_ninguna);
            //imageViewSur.setClickable(sur);

            imageViewEste.setEnabled(este);
            //imageViewEste.setImageResource(este ? R.drawable.este : R.drawable.dir_ninguna);
            //imageViewEste.setClickable(este);

            imageViewOeste.setEnabled(oeste);
            //imageViewOeste.setImageResource(oeste ? R.drawable.oeste : R.drawable.dir_ninguna);
            //imageViewOeste.setClickable(oeste);

            popupMenuTomarObjeto = creaPopupMenu(buttonTomarObjeto, arrayListObjetosLugar, ACTION_TYPE_PICK);
            //
            popupMenuDejarObjeto = creaPopupMenu(buttonDejarObjeto, arrayListObjetosBolsillo, ACTION_TYPE_DROP);

            popupMenuOtrasAcciones = creaPopupMenu(buttonOtrasAcciones, arrayListOtrasAcciones, ACTION_TYPE_OTHER);

            //
            refreshPending = false;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Tomar la lista de acciones desde los argumentos de entrada,
        // o crear una lista vacía
        if (getArguments() != null) {
            // Tomar la lista de acciones
            //arrayListOtrasAcciones = getArguments().getStringArrayList(EXTRA_IN_OPCIONES);
        } else {
            // Crear lista vacía
            //arrayListOtrasAcciones = new ArrayList<String>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflar el layout del fragment
        View v = inflater.inflate(R.layout.fragment_acciones, container, false);

        imageViewNorte = (ImageView) v.findViewById(R.id.imageViewNorte);
        imageViewSur = (ImageView) v.findViewById(R.id.imageViewSur);
        imageViewEste = (ImageView) v.findViewById(R.id.imageViewEste);
        imageViewOeste = (ImageView) v.findViewById(R.id.imageViewOeste);

        imageViewNorte.setOnClickListener(onClickRumbo);
        imageViewSur.setOnClickListener(onClickRumbo);
        imageViewEste.setOnClickListener(onClickRumbo);
        imageViewOeste.setOnClickListener(onClickRumbo);

        //
        buttonTomarObjeto = (Button) v.findViewById(R.id.buttonTomarObjeto);
        buttonDejarObjeto = (Button) v.findViewById(R.id.buttonDejarObjeto);
        buttonOtrasAcciones = (Button) v.findViewById(R.id.buttonOtrasAcciones);

        //
        buttonMapa = (Button) v.findViewById(R.id.buttonMapa);
        buttonMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                mListener.onActionSelected(ACTION_TYPE_MAP, -1);
            }
        });

        //
        buttonCasos = (Button) v.findViewById(R.id.buttonCasos);
        buttonCasos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                mListener.onActionSelected(ACTION_TYPE_CASOS, -1);
            }
        });

        // Devolver layout inflado
        return v;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnActionSelectedListener) activity;
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

    @Override
    public void onResume() {
        super.onResume();

        isReady = true;
        Log.i(TAG, "onResume");
        refresh();
    }

    @Override
    public void onPause() {
        super.onPause();

        isReady = false;
    }


    View.OnClickListener onClickRumbo = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int actionType;
            if(v == imageViewNorte)
                actionType = ACTION_TYPE_GO_NORTH;
            else if(v == imageViewSur)
                actionType = ACTION_TYPE_GO_SOUTH;
            else if(v == imageViewEste)
                actionType = ACTION_TYPE_GO_EAST;
            else
                actionType = ACTION_TYPE_GO_WEST;

            mListener.onActionSelected(ACTION_TYPE_GO, actionType);
        }
    };

    private class BotonListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

            if (v == buttonTomarObjeto) {
                popupMenuTomarObjeto.show();
            } else if (v == buttonDejarObjeto) {
                popupMenuDejarObjeto.show();
            } else if (v == buttonOtrasAcciones) {
                popupMenuOtrasAcciones.show();
            }
        }
    };

    private PopupMenu creaPopupMenu(Button button, ArrayList<String> items, int actionType) {

        if(items.isEmpty()) {

            button.setEnabled(false);
            button.setOnClickListener(null);

            return null;
        }

        //
        button.setEnabled(true);
        button.setOnClickListener(new BotonListener());

        PopupMenu popupMenu = new PopupMenu(getActivity(), button);

        for(int i = 0; i < items.size(); ++i)
            popupMenu.getMenu().add(actionType + 1, i + 1, Menu.NONE, items.get(i));

        popupMenu.setOnMenuItemClickListener(new PopupMenuListener());

        return popupMenu;
    }


    private class PopupMenuListener implements PopupMenu.OnMenuItemClickListener {
        public boolean onMenuItemClick(MenuItem item) {
            //
            int actionType = item.getGroupId() - 1;
            int pos = 0;

            switch(actionType) {
                case ACTION_TYPE_PICK :
                case ACTION_TYPE_DROP :
                case ACTION_TYPE_OTHER :
                    pos = item.getItemId() - 1;
                    break;
                default :
                    return false;
            }

            mListener.onActionSelected(actionType, pos);

            return true;
        }
    }


}
