package com.app.floppysoftware.benitogg;

import com.app.floppysoftware.benitogg.database.BaseDatos;
import com.app.floppysoftware.benitogg.models.Accion;
import com.app.floppysoftware.benitogg.models.Actor;
import com.app.floppysoftware.benitogg.models.Lugar;
import com.app.floppysoftware.benitogg.models.Objeto;

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
    public static final String WEBSITE = "http://benitogg.floppysoftware.es";

    // Máximo número de objetos que puede llevar el protagonista
    private static final int MAX_OBJETOS_BOLSILLO = 3;

    // Prefijos de las localizaciones exteriores
    private static final String PREFIJO_CALLE = "cl_";
    private static final String PREFIJO_AVENIDA = "av_";
    private static final String PREFIJO_PLAZA = "pl_";
    private static final String PREFIJO_PARQUE = "pq_";

    // IDs de los lugares
    private static final String LUGAR_QUIOSCO = "quiosco";
    private static final String LUGAR_BIBLIOTECA = "biblioteca";
    private static final String LUGAR_OFICINA = "oficina";
    private static final String LUGAR_PIZZERIA = "pizzeria";
    private static final String LUGAR_PENSION = "pension";
    private static final String LUGAR_GIMNASIO = "gimnasio";
    private static final String LUGAR_PARQUE_VERDOR = "pq_verdor";
    private static final String LUGAR_TALLER = "taller";
    private static final String LUGAR_CONSULTORIO = "consultorio";

    // IDs de los lugares especiales
    private static final String LUGAR_LIMBO = "limbo";

    // IDs de los actores
    private static final String ACTOR_PERIQUITO = "periquito";
    private static final String ACTOR_GATO = "gato";
    private static final String ACTOR_PERRO = "perro";

    // IDs de los objetos
    private static final String OBJETO_PAN = "pan";
    private static final String OBJETO_LIBRO_RECETAS = "libro_recetas";
    private static final String OBJETO_LIBRO_HISTORIA = "libro_historia";
    private static final String OBJETO_PENDRIVE = "pendrive";
    private static final String OBJETO_LATA_SARDINAS = "lata_sardinas";
    private static final String OBJETO_ABRELATAS = "abrelatas";
    private static final String OBJETO_PEDRUSCO = "pedrusco";
    private static final String OBJETO_CARRETILLA = "carretilla";
    private static final String OBJETO_MARTILLO = "martillo";
    private static final String OBJETO_HUCHA = "hucha";
    private static final String OBJETO_MONEDA = "moneda";
    private static final String OBJETO_CHUCHE = "chuche";
    private static final String OBJETO_HUESO = "hueso";

    // IDs del estado de los objetos
    private static final String ESTADO_ENCHUFADO = "enchufado";
    private static final String ESTADO_DESENCHUFADO = "desenchufado";
    private static final String ESTADO_ABIERTO = "abierto";
    private static final String ESTADO_CERRADO = "cerrado";
    private static final String ESTADO_ROTO = "roto";

    // IDs de los casos
    private static final int CASO_PERIQUITO_EXTRAVIADO = 1;
    private static final int CASO_LIBROS_CAMBIADOS = 2;
    private static final int CASO_GATO_COLORAO = 3;
    private static final int CASO_PEDRUSCO = 4;
    private static final int CASO_CHUCHE = 5;
    private static final int CASO_MARTILLO = 6;
    private static final int CASO_ANALISIS = 7;
    private static final int CASO_PERRO_CANSINO = 8;

    // IDs de las acciones
    private static final int ACCION_ATRAPAR_PERIQUITO = 0;
    private static final int ACCION_ENCHUFAR_PENDRIVE = 1;
    private static final int ACCION_ABRIR_LATA = 2;
    private static final int ACCION_ATRAPAR_GATO = 3;
    private static final int ACCION_ROMPER_HUCHA = 4;
    private static final int ACCION_COMPRAR_CHUCHE = 5;

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

        if(bd.getCaso(CASO_PERIQUITO_EXTRAVIADO).getResuelto()) {

            // Caso Resuelto

            if(lugarId.equals(LUGAR_QUIOSCO)) {

                // El periquito está en la jaula
                detalle.add(R.string.periquito_enjaulado);
            }
        } else {

            // Caso Pendiente

            // Si el protagonista está en el quiosco...
            if(lugarId.equals(LUGAR_QUIOSCO)) {

                // El periquito se ha escapado
                detalle.add(R.string.periquito_escapado);

            } else if(bd.getActor(ACTOR_PERIQUITO).getLugar().equals(lugarId)) {

                // El periquito está en el lugar actual

                // Si también está aquí el pan...
                if(bd.getObjeto(OBJETO_PAN).getLugar().equals(lugarId)) {

                    // El periquito está picoteando el pan
                    detalle.add(R.string.periquito_picoteando);

                    // Acción: Atrapar el periquito
                    acciones.add(new Accion(ACCION_ATRAPAR_PERIQUITO, R.string.accion_atrapar_periquito));
                } else {

                    // El periquito está revoloteando por aquí
                    detalle.add(R.string.periquito_revoloteando);
                }
            }
        }

        // -------------------------------
        // El caso de los libros cambiados
        // -------------------------------

        if(bd.getCaso(CASO_LIBROS_CAMBIADOS).getResuelto()) {

            // Caso Resuelto

            // Si el protagonista está en la biblioteca...
            if(lugarId.equals(LUGAR_BIBLIOTECA)) {

                // El libro de historia está en la biblioteca
                detalle.add(R.string.libros_encontrado_historia);

            } else if(lugarId.equals(LUGAR_PIZZERIA)) {

                // El protagonista está en la pizzería

                // El libro de recetas está en la pizzería
                detalle.add(R.string.libros_encontrado_recetas);
            }
        } else {

            // Caso Pendiente

            // Averiguar dónde están los libros
            String lugarLibroHistoria = bd.getObjeto(OBJETO_LIBRO_HISTORIA).getLugar();
            String lugarLibroRecetas = bd.getObjeto(OBJETO_LIBRO_RECETAS).getLugar();

            // Si el protagonista está en la biblioteca...
            if(lugarId.equals(LUGAR_BIBLIOTECA)) {

                // Si el libro de historia también está en la biblioteca...
                if(lugarLibroHistoria.equals(LUGAR_BIBLIOTECA)) {

                    // El libro de historia está en la biblioteca
                    detalle.add(R.string.libros_encontrado_historia);
                } else {

                    // El libro de historia se ha extraviado
                    detalle.add(R.string.libros_extraviado_historia);
                }
            } else if(lugarId.equals(LUGAR_PIZZERIA)) {

                // El protagonista está en la pizzería

                // Si el libro de recetas también está en la pizzería...
                if(lugarLibroRecetas.equals(LUGAR_PIZZERIA)) {

                    // El libro de recetas está en la pizzería
                    detalle.add(R.string.libros_encontrado_recetas);
                } else {

                    // El libro de recetas se ha extraviado
                    detalle.add(R.string.libros_extraviado_recetas);
                }
            }
        }

        // ------------------------
        // El caso del gato colorao
        // ------------------------

        if(bd.getCaso(CASO_GATO_COLORAO).getResuelto()) {

            // Caso Resuelto

            // Si el protagonista está en la pensión...
            if(lugarId.equals(LUGAR_PENSION)) {

                // El gato está en casa
                detalle.add(R.string.gato_casa);
            }
        } else {

            // Caso Pendiente

            // Tomar el objeto Lata de Sardinas
            Objeto lataSardinas = bd.getObjeto(OBJETO_LATA_SARDINAS);

            // Si el protagonista está en la pensión...
            if(lugarId.equals(LUGAR_PENSION)) {

                // El gato se ha escapado
                detalle.add(R.string.gato_escapado);

            } else if(bd.getActor(ACTOR_GATO).getLugar().equals(lugarId)){

                // El gato está en el mismo lugar que el protagonista

                // Si la lata de sardinas está abierta y aquí...
                if(lataSardinas.getLugar().equals(lugarId) && lataSardinas.getEstado().equals(ESTADO_ABIERTO)) {

                    // El gato está comiendo
                    detalle.add(R.string.gato_comiendo);

                    // Acción: Atrapar el gato
                    acciones.add(new Accion(ACCION_ATRAPAR_GATO, R.string.accion_atrapar_gato));
                } else {

                    // El gato está por aquí
                    detalle.add(R.string.gato_aqui);
                }
            }

            // Si la lata de sardinas la tiene el protagonista, está cerrada,
            // y también tiene el abrelatas...
            if(lataSardinas.getLugar().equals(Lugar.BOLSILLO) && lataSardinas.getEstado().equals(ESTADO_CERRADO)
                    && bd.getObjeto(OBJETO_ABRELATAS).getLugar().equals(Lugar.BOLSILLO)) {

                // Acción: Abrir la lata con el abrelatas
                acciones.add(new Accion(ACCION_ABRIR_LATA, R.string.accion_abrir_lata));
            }
        }

        // ----------------------------
        // El caso del pedrusco birlado
        // ----------------------------

        if(bd.getCaso(CASO_PEDRUSCO).getResuelto()) {

            // Caso Resuelto

            // Si el protagonista está en el gimnasio...
            if(lugarId.equals(LUGAR_GIMNASIO)) {

                // El pedrusco está en el gimnasio
                detalle.add(R.string.pedrusco_encontrado);
            }
        } else {

            // Caso Pendiente

            // Si el protagonista está en el gimnasio...
            if(lugarId.equals(LUGAR_GIMNASIO)) {

                // Han birlado el pedrusco
                detalle.add(R.string.pedrusco_birlado);
            }
        }

        // --------------------------------
        // El caso de la chuche engorrinada
        // --------------------------------

        if(bd.getCaso(CASO_CHUCHE).getResuelto()) {

            // Caso Resuelto

            // Si el protagonista está en el parque El Verdor...
            if(lugarId.equals(LUGAR_PARQUE_VERDOR)) {

                // Andresito está devorando la chuche
                detalle.add(R.string.chuche_devorando);
            }
        } else {

            // Caso Pendiente

            // Si el protagonista está en el parque El Verdor...
            if(lugarId.equals(LUGAR_PARQUE_VERDOR)) {

                // Andresito está llorando, porque la chuche se le ha engorrinado
                detalle.add(R.string.chuche_engorrinada);
            }

            // Si el protagonista está en el quiosco,
            // y tiene la moneda en el bolsillo...
            if(lugarId.equals(LUGAR_QUIOSCO) && bd.getObjeto(OBJETO_MONEDA).getLugar().equals(Lugar.BOLSILLO)) {

                // Acción: Comprar una chuche
                acciones.add(new Accion(ACCION_COMPRAR_CHUCHE, R.string.accion_comprar_chuche));
            }

            // Tomar objetos martillo y hucha
            Objeto martillo = bd.getObjeto(OBJETO_MARTILLO);
            Objeto hucha = bd.getObjeto(OBJETO_HUCHA);

            // Si el protagonista tiene el martillo
            // y la hucha sin romper...
            if(martillo.getLugar().equals(Lugar.BOLSILLO) && hucha.getLugar().equals(Lugar.BOLSILLO) &&
                    !hucha.getEstado().equals(ESTADO_ROTO)) {

                // Acción: Romper la hucha con el martillo
                acciones.add(new Accion(ACCION_ROMPER_HUCHA, R.string.accion_romper_hucha));
            }
        }

        // ----------------------------
        // El caso del martillo rarillo
        // ----------------------------

        if(bd.getCaso(CASO_MARTILLO).getResuelto()) {

            // Caso Resuelto

            // Si el protagonista está en el taller...
            if(lugarId.equals(LUGAR_TALLER)) {

                // Añadir detalle, dependiendo de que el martillo esté o no
                // en el taller (cuando se resuelve el caso, no se le puede
                // enviar al limbo, pues también es necesario para resolver el caso
                // de la chuche engorrinada).
                if(bd.getObjeto(OBJETO_MARTILLO).getLugar().equals(LUGAR_TALLER)) {

                    // El martillo está en el taller
                    detalle.add(R.string.martillo_taller);
                } else {
                    // El martillo no está en el taller
                    detalle.add(R.string.martillo_fregona);
                }
            }
        } else {

            // Caso Pendiente

            // Si el protagonista está en el taller...
            if(lugarId.equals(LUGAR_TALLER)) {

                // El martillo ha desaparecido
                detalle.add(R.string.martillo_desaparecido);
            }
        }

        // ----------------------------
        // El caso del análisis perdido
        // ----------------------------

        if(bd.getCaso(CASO_ANALISIS).getResuelto()) {

            // Caso resuelto

            // Si el protagonista está en el consultorio...
            if(lugarId.equals(LUGAR_CONSULTORIO)) {

                // El análisis ha sido encontrado
                detalle.add(R.string.analisis_encontrado);
            }
        } else {

            // Caso pendiente

            // Si el protagonista está en el consultorio...
            if(lugarId.equals(LUGAR_CONSULTORIO)) {

                // El análisis se ha perdido
                detalle.add(R.string.analisis_perdido);

            } else if(lugarId.equals(LUGAR_OFICINA)) {

                // El protagonista está en la oficina

                // Tomar objeto Pendrive
                Objeto pendrive = bd.getObjeto(OBJETO_PENDRIVE);

                // Si el protagonista tiene el pendrive...
                if(pendrive.getLugar().equals(Lugar.BOLSILLO)) {

                    // Acción: Enchufar el pendrive en el ordenador
                    acciones.add(new Accion(ACCION_ENCHUFAR_PENDRIVE, R.string.accion_enchufar_pendrive));

                } else if(pendrive.getLugar().equals(LUGAR_OFICINA) && pendrive.getEstado().equals(ESTADO_ENCHUFADO)) {

                    // El pendrive está enchufado
                    detalle.add(R.string.analisis_pendrive);
                }
            }
        }

        // -------------------------
        // El caso del perro cansino
        // -------------------------

        // Si el perro está en el mismo lugar que el protagonista...
        if(bd.getActor(ACTOR_PERRO).getLugar().equals(lugarId)) {

            // El perro está aquí

            if(bd.getCaso(CASO_PERRO_CANSINO).getResuelto()) {

                // El caso está resuelto

                // El perro está mordisqueando el hueso
                detalle.add(R.string.perro_hueso);
            } else {

                // El perro está ladrando
                detalle.add(R.string.perro_ladrando);
            }
        }
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

        if(!bd.getCaso(CASO_PERRO_CANSINO).getResuelto()) {

            // Tomar actor Perro
            Actor perro = bd.getActor(ACTOR_PERRO);

            // Si el perro está en el mismo lugar que el protagonista,
            // y el lugar destino es exterior, cambiar al perro
            // a dicho lugar (el perro persigue al protagonista).

            if(perro.getLugar().equals(idLugarOrigen) &&
                    (idLugarDestino.startsWith(PREFIJO_CALLE) ||
                            idLugarDestino.startsWith(PREFIJO_AVENIDA) ||
                            idLugarDestino.startsWith(PREFIJO_PLAZA) ||
                            idLugarDestino.startsWith(PREFIJO_PARQUE))) {

                // Mover al perro al lugar destino
                perro.setLugar(idLugarDestino);
                bd.updateActor(perro);
            }
        }
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
            case ACCION_ATRAPAR_PERIQUITO:
                act = bd.getActor(ACTOR_PERIQUITO);
                act.setLugar(LUGAR_QUIOSCO);
                bd.updateActor(act);
                return CASO_PERIQUITO_EXTRAVIADO;
            case ACCION_ATRAPAR_GATO:
                act = bd.getActor(ACTOR_GATO);
                act.setLugar(LUGAR_PENSION);
                bd.updateActor(act);
                return CASO_GATO_COLORAO;
            case ACCION_ABRIR_LATA:
                obj = bd.getObjeto(OBJETO_LATA_SARDINAS);
                obj.setNombre(obj.getNombre() + " abierta");
                obj.setEstado(ESTADO_ABIERTO);
                bd.updateObjeto(obj);
                break;
            case ACCION_ROMPER_HUCHA:
                obj = bd.getObjeto(OBJETO_HUCHA);
                obj.setNombre(obj.getNombre() + " rota");
                obj.setEstado(ESTADO_ROTO);
                bd.updateObjeto(obj);
                obj = bd.getObjeto(OBJETO_MONEDA);
                obj.setLugar(lugarId);
                bd.updateObjeto(obj);
                break;
            case ACCION_COMPRAR_CHUCHE:
                obj = bd.getObjeto(OBJETO_CHUCHE);
                obj.setLugar(Lugar.BOLSILLO);
                bd.updateObjeto(obj);
                obj = bd.getObjeto(OBJETO_MONEDA);
                obj.setLugar(LUGAR_LIMBO);
                bd.updateObjeto(obj);
                break;
            case ACCION_ENCHUFAR_PENDRIVE:
                obj = bd.getObjeto(OBJETO_PENDRIVE);
                obj.setLugar(lugarId);
                obj.setEstado(ESTADO_ENCHUFADO);
                bd.updateObjeto(obj);
                break;
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

        // El pedrusco sólo se puede coger si ya tiene la carretilla
        if(objeto.getId().equals(OBJETO_PEDRUSCO)) {

            // Comprobar si ya tiene la carretilla
            for(Objeto obj : bolsillo) {
                if(obj.getId().equals(OBJETO_CARRETILLA)) {

                    // Tiene la carretilla;
                    // puede tomar el pedrusco.
                    return 0;
                }
            }

            // No tiene la carretilla
            return R.string.pedrusco_falta_carretilla;
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

        // Si el objeto es el pendrive y está enchufado,
        // desenchufarlo.
        if(objeto.getId().equals(OBJETO_PENDRIVE)) {
            if(objeto.getEstado().equals(ESTADO_ENCHUFADO)) {

                // Desenchufar el pendrive
                objeto.setEstado(ESTADO_DESENCHUFADO);
                bd.updateObjeto(objeto);
            }
        }

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

        // --------------------------------
        // El caso del periquito extraviado
        // --------------------------------

        // Nada

        // -------------------------------
        // El caso de los libros cambiados
        // -------------------------------

        // Si el libro de historia está en la biblioteca, y
        // el libro de recetas está en la pizzería, el caso está resuelto.

        if(!bd.getCaso(CASO_LIBROS_CAMBIADOS).getResuelto() &&
                (objeto.getId().equals(OBJETO_LIBRO_HISTORIA) || objeto.getId().equals(OBJETO_LIBRO_RECETAS))) {

            // Comprobar si los 2 libros están en su sitio
            Objeto libroHistoria = bd.getObjeto(OBJETO_LIBRO_HISTORIA);
            Objeto libroRecetas = bd.getObjeto(OBJETO_LIBRO_RECETAS);

            // Si los 2 libros están en su sitio, caso resuelto
            if(libroHistoria.getLugar().equals(LUGAR_BIBLIOTECA) && libroRecetas.getLugar().equals(LUGAR_PIZZERIA)) {

                // Poner los libros en el limbo, para que no puedan ser vueltos a tomar
                libroHistoria.setLugar(LUGAR_LIMBO); bd.updateObjeto(libroHistoria);
                libroRecetas.setLugar(LUGAR_LIMBO); bd.updateObjeto(libroRecetas);

                // Caso resuelto
                return CASO_LIBROS_CAMBIADOS;
            }
        }

        // ------------------------
        // El caso del gato colorao
        // ------------------------

        // Nada

        // ----------------------------
        // El caso del pedrusco birlado
        // ----------------------------

        // Si se ha dejado el pedrusco en el gimnasio, el caso está resuelto

        if(!bd.getCaso(CASO_PEDRUSCO).getResuelto() &&
                (objeto.getId().equals(OBJETO_CARRETILLA) || objeto.getId().equals(OBJETO_PEDRUSCO))) {

            // Si el pedrusco sigue teniéndolo el protagonista, significa que se ha
            // dejado la carretilla; dejar también el pedrusco.

            // Tomar objeto Pedrusco
            Objeto pedrusco = bd.getObjeto(OBJETO_PEDRUSCO);

            // Si el pedrusco lo tiene el protagonista todavía...
            if(pedrusco.getLugar().equals(Lugar.BOLSILLO)) {

                // Dejar el pedrusco también
                pedrusco.setLugar(objeto.getLugar());
                bd.updateObjeto(pedrusco);
            }

            // Si se ha dejado el pedrusco en el gimnasio, el caso está resuelto
            if(pedrusco.getLugar().equals(LUGAR_GIMNASIO)) {

                // Dejar pedrusco en el limbo, para que no se pueda volver a tomar
                pedrusco.setLugar(LUGAR_LIMBO);
                bd.updateObjeto(pedrusco);

                // Caso resuelto
                return CASO_PEDRUSCO;
            }
        }

        // --------------------------------
        // El caso de la chuche engorrinada
        // --------------------------------

        // Si ha dejado la chuche en el Parque Verdor, el caso está resuelto

        if(!bd.getCaso(CASO_CHUCHE).getResuelto() &&
                objeto.getId().equals(OBJETO_CHUCHE) &&
                objeto.getLugar().equals(LUGAR_PARQUE_VERDOR)) {

            // Dejar la chuche en el limbo
            objeto.setLugar(LUGAR_LIMBO);
            bd.updateObjeto(objeto);

            // Caso resuelto
            return CASO_CHUCHE;
        }

        // ----------------------------
        // El caso del martillo rarillo
        // ----------------------------

        // Si ha dejado el martillo en el taller, el caso está resuelto

        if(!bd.getCaso(CASO_MARTILLO).getResuelto() &&
                objeto.getId().equals(OBJETO_MARTILLO) &&
                objeto.getLugar().equals(LUGAR_TALLER)) {

            // No se puede dejar el martillo en el limbo,
            // porque se necesita también para el caso de la chuche engorrinada.

            // Caso resuelto
            return CASO_MARTILLO;
        }

        // ----------------------------
        // El caso del análisis perdido
        // ----------------------------

        // Si ha dejado el pendrive en el consultorio, el caso está resuelto

        if(!bd.getCaso(CASO_ANALISIS).getResuelto() &&
                objeto.getId().equals(OBJETO_PENDRIVE) &&
                objeto.getLugar().equals(LUGAR_CONSULTORIO)) {

            // Dejar el pendrive en el limbo
            objeto.setLugar(LUGAR_LIMBO);
            bd.updateObjeto(objeto);

            // Caso resuelto
            return CASO_ANALISIS;
        }

        // -------------------------
        // El caso del perro cansino
        // -------------------------

        // Si ha dejado el hueso donde está el perro, caso resuelto

        if(!bd.getCaso(CASO_PERRO_CANSINO).getResuelto() &&
                objeto.getId().equals(OBJETO_HUESO) &&
                bd.getActor(ACTOR_PERRO).getLugar().equals(bd.getActor(Actor.PROTAGONISTA).getLugar())) {

            // Dejar el hueso en el limbo
            objeto.setLugar(LUGAR_LIMBO);
            bd.updateObjeto(objeto);

            // Caso resuelto
            return CASO_PERRO_CANSINO;
        }

        // No se ha resuelto ningún caso
        return 0;
    }
}
