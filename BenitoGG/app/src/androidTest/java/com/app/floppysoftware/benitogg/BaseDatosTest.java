package com.app.floppysoftware.benitogg;


import android.test.ActivityInstrumentationTestCase2;

import com.app.floppysoftware.benitogg.activities.PrincipalActivity;
import com.app.floppysoftware.benitogg.database.BaseDatos;
import com.app.floppysoftware.benitogg.models.Actor;
import com.app.floppysoftware.benitogg.models.Lugar;

import java.util.ArrayList;

public class BaseDatosTest extends ActivityInstrumentationTestCase2<PrincipalActivity> {

    /**
     * Constructor.
     */
    public BaseDatosTest() {

        // Llamar a la superclase
        super(PrincipalActivity.class);
    }

    /**
     * Este método se ejecuta antes de cada método de test. Inicializar
     * la activity y el entorno.
     *
     * @throws Exception excepción
     */
    @Override
    public void setUp() throws Exception {

        // Llamar a la superclase
        super.setUp();

        // Comprobar que la activity ha sido creada
        assertNotNull(getActivity());
    }

    public void testLugares() {

        // Resetear base de datos
        BaseDatos bd = new BaseDatos(getActivity(), true);

        // Comprobar que se ha abierto correctamente
        assertNotNull(bd);

        // Leer todos los lugares
        ArrayList<Lugar> lugares = bd.getLugares();

        //
        String nombreLugarDestino;
        Lugar lugarDestino;

        // Iterar todos los lugares
        for(Lugar lugar : lugares) {

            // Norte
            nombreLugarDestino = lugar.getLugarNorte();

            if(nombreLugarDestino != null) {
                if((lugarDestino = buscaLugar(lugares, nombreLugarDestino)) != null) {

                    compruebaNombreLugares("Sur", lugarDestino.getId(), lugar.getId(), lugarDestino.getLugarSur());
                }
            }

            // Sur
            nombreLugarDestino = lugar.getLugarSur();

            if(nombreLugarDestino != null) {
                if((lugarDestino = buscaLugar(lugares, nombreLugarDestino)) != null) {

                    compruebaNombreLugares("Norte", lugarDestino.getId(), lugar.getId(), lugarDestino.getLugarNorte());
                }
            }

            // Este
            nombreLugarDestino = lugar.getLugarEste();

            if(nombreLugarDestino != null) {
                if((lugarDestino = buscaLugar(lugares, nombreLugarDestino)) != null) {

                    compruebaNombreLugares("Oeste", lugarDestino.getId(), lugar.getId(), lugarDestino.getLugarOeste());
                }
            }

            // Oeste
            nombreLugarDestino = lugar.getLugarOeste();

            if(nombreLugarDestino != null) {
                if((lugarDestino = buscaLugar(lugares, nombreLugarDestino)) != null) {

                    compruebaNombreLugares("Este", lugarDestino.getId(), lugar.getId(), lugarDestino.getLugarEste());
                }
            }

        }

        //
        // Leer actor protagonista
        Actor prota = bd.getActor(Actor.PROTAGONISTA);

        // Comprobar que se ha leído correctamente
        assertNotNull(prota);

        // Cambiarlo al Lugar Centro
        prota.setLugar("test_lugar_centro");

        // Actualizar el actor en la base de datos
        bd.updateActor(prota);

        // Cerrar base de datos
        bd.cerrar();
    }

    private void compruebaNombreLugares(String direccion, String enLugar, String nombreEsperado, String nombreEncontrado) {

        assertEquals("Conexión " + direccion +
                " errónea en lugar '" + enLugar +
                "';",
                nombreEsperado,
                nombreEncontrado);
    }

    private Lugar buscaLugar(ArrayList<Lugar> lugares, String nombreLugar) {

        for(Lugar lugar : lugares) {
            if(lugar.getId().equals(nombreLugar)) {
                return lugar;
            }
        }

        return null;
    }
}
