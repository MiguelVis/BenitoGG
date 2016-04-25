package com.app.floppysoftware.benitogg;

import com.app.floppysoftware.benitogg.Accion;
import com.app.floppysoftware.benitogg.Actor;
import com.app.floppysoftware.benitogg.BaseDatos;
import com.app.floppysoftware.benitogg.Lugar;
import com.app.floppysoftware.benitogg.Objeto;
import com.app.floppysoftware.benitogg.R;

import java.util.ArrayList;

/**
 * Esta clase es la única dependiente de la aventura.
 *
 * Los métodos de esta clase no pueden acceder al UI o a la base de datos,
 * salvo que se indique lo contrario. Por lo general, los
 * métodos de esta clase se ejecutarán fuera del hilo del UI.
 *
 * @author   Miguel I. García López
 * @version  1.0
 * @since    18 Mar 2016
 */
public final class Zeta {

    // Website
    public static final String WEBSITE = "http://www.floppysoftware.es/gg";

    // Máximo número de objetos que puede llevar el protagonista
    private static final int MAX_OBJETOS_BOLSILLO = 3;

    // Prefijos de las localizaciones exteriores
    private static final String PREFIJO_CALLE = "cl_";
    private static final String PREFIJO_AVENIDA = "av_";
    private static final String PREFIJO_PLAZA = "pl_";
    private static final String PREFIJO_PARQUE = "pq_";

    // IDs de los lugares
    //private static final String LUGAR_QUIOSCO = "quiosco"

    // IDs de los lugares especiales
    private static final String LUGAR_LIMBO = "limbo";

    // IDs de los actores
    // static final String ACTOR_PERIQUITO = "periquito";

    // IDs de los objetos
    //private static final String OBJETO_PAN = "pan";

    // IDs del estado de los objetos
    //private static final String ESTADO_ENCHUFADO = "enchufado";

    // IDs de los casos
    //private static final int CASO_PERIQUITO_EXTRAVIADO = 1;

    // IDs de las acciones
    //private static final int ACCION_ATRAPAR_PERIQUITO = 0;

    /**
     * Parchear la escena, según el estado actual del juego.
     *
     * El parcheo consiste en la adición de líneas de detalle, así como
     * de acciones posibles.
     *
     * @param bd        Base de datos
     * @param detalle   Detalle
     * @param acciones  Acciones
     */
    public static void parcheEscena(BaseDatos bd, ArrayList<Integer> detalle, ArrayList<Accion> acciones) {

        // Tomar la id del lugar en el que se encuentra el protagonista
        String lugarId = bd.getActor(Actor.PROTAGONISTA).getLugar();

        // Parchear caso por caso, según el estado actual del juego

        // --------------------------------
        // El caso del periquito extraviado
        // --------------------------------
    }

    /**
     * Realizar cualquier cosa necesaria, después de que el protagonista ha
     * cambiado de lugar.
     *
     * @param bd              Base de datos
     * @param idLugarOrigen   Lugar anterior
     * @param idLugarDestino  Lugar actual
     */
    public static void protaCambiaLugar(BaseDatos bd, String idLugarOrigen, String idLugarDestino) {

        // -------------------------
        // El caso del perro cansino
        // -------------------------

    }

    /**
     * Realizar acción.
     *
     * @param bd        Base de datos
     * @param accionId  Acción
     *
     * @return          Id del caso resuelto con la acción,
     *                  o 0 si no ha sido resuelto ninguno.
     */
    public static int doAccion(BaseDatos bd, int accionId) {

        // Id del lugar en el que está el protagonista
        String lugarId = bd.getActor(Actor.PROTAGONISTA).getLugar();

        // Objetos auxiliares
        Actor act;
        Objeto obj;

        // Realizar acción
        switch(accionId) {

            default :
                break;
        }

        // Ningún caso resuelto
        return 0;
    }

    /**
     * Comprobar si el protagonista puede tomar un objeto.
     *
     * @param objeto    Objeto a tomar
     * @param bolsillo  Objetos que tiene el protagonista actualmente
     * @return          Id de una String con un mensaje de error,
     *                  o 0 si puede tomar el objeto.
     */
    public static int puedeTomarObjeto(Objeto objeto, ArrayList<Objeto> bolsillo) {

        // Comprobar si puede tomar más objetos
        if(bolsillo.size() >= MAX_OBJETOS_BOLSILLO) {

            // No; demasiados objetos
            return R.string.tomar_demasiados_objetos;
        }

        // Ok, puede tomar el objeto
        return 0;
    }

    /**
     * Realizar cualquier cosa necesaria, después de que el
     * protagonista ha tomado un objeto.
     *
     * @param bd         Base de datos
     * @param objeto     Objeto tomado
     * @return           Id del caso resuelto al tomar el objeto,
     *                   o 0 si no se ha resuelto ninguno.
     */
    public static int objetoTomado(BaseDatos bd, Objeto objeto) {

        // Ningún caso resuelto
        return 0;
    }

    /**
     * Realizar cualquier cosa necesaria, después de que
     * el protagonista haya dejado un objeto.
     *
     * @param bd        Base de datos
     * @param objeto    Objeto dejado
     * @return          Id del caso resuelto al dejar el objeto,
     *                  o 0 si no se ha resuelto ninguno.
     */
    public static int objetoDejado(BaseDatos bd, Objeto objeto) {

        // Ver caso por caso

        // No se ha resuelto ningún caso
        return 0;
    }
}
