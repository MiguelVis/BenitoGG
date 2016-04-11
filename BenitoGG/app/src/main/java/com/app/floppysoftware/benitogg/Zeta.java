package com.app.floppysoftware.benitogg;

import java.util.ArrayList;

/**
 * Esta clase es dependiente de la aventura. Los métodos de esta clase no pueden acceder
 * al UI o a la base de datos, salvo que se indique lo contrario. Por lo general, los
 * métodos de esta clase se ejecutarán fuera del hilo del UI.
 */
public final class Zeta {

    // Website
    public static final String WEBSITE = "http://www.floppysoftware.es/gg"; // FIXME!!!!!!

    // Máximo número de objetos en el bolsillo
    private static final int MAX_OBJETOS_BOLSILLO = 3;

    // Prefijos de las localizaciones exteriores
    private static final String PREFIJO_CALLE = "cl_";
    private static final String PREFIJO_AVENIDA = "av_";
    private static final String PREFIJO_PLAZA = "pl_";
    private static final String PREFIJO_PARQUE = "pr_";

    // IDs de los lugares
    private static final String ID_QUIOSCO = "quiosco";
    private static final String ID_BIBLIOTECA = "biblioteca";
    private static final String ID_OFICINA = "oficina";
    private static final String ID_PIZZERIA = "pizzeria";
    private static final String ID_PENSION = "pension";
    private static final String ID_GIMNASIO = "gimnasio";
    private static final String ID_PARQUE_VERDOR = "pr_verdor";
    private static final String ID_TALLER = "taller";
    private static final String ID_CONSULTORIO = "consultorio";

    // IDs de los lugares especiales
    private static final String ID_LIMBO = "limbo";

    // IDs de los actores
    private static final String ID_PERIQUITO = "periquito";
    private static final String ID_GATO = "gato";
    private static final String ID_PERRO = "perro";

    // IDs de los objetos
    private static final String ID_PAN = "pan";
    private static final String ID_LIBRO_RECETAS = "libro_recetas";
    private static final String ID_LIBRO_HISTORIA = "libro_historia";
    private static final String ID_PENDRIVE = "pendrive";
    private static final String ID_LATA_SARDINAS = "lata_sardinas";
    private static final String ID_ABRELATAS = "abrelatas";
    private static final String ID_PEDRUSCO = "pedrusco";
    private static final String ID_CARRETILLA = "carretilla";
    private static final String ID_MARTILLO = "martillo";
    private static final String ID_HUCHA = "hucha";
    private static final String ID_MONEDA = "moneda";
    private static final String ID_CHUCHE = "chuche";
    private static final String ID_HUESO = "hueso";

    // IDs del estado de los objetos
    private static final String ID_ENCHUFADO = "enchufado";
    private static final String ID_DESENCHUFADO = "desenchufado";
    private static final String ID_ABIERTO = "abierto";
    private static final String ID_CERRADO = "cerrado";
    private static final String ID_ROTO = "roto";

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
    private static final int ID_ATRAPAR_PERIQUITO = 0;
    private static final int ID_ENCHUFAR_PENDRIVE = 1;
    private static final int ID_ABRIR_LATA = 2;
    private static final int ID_ATRAPAR_GATO = 3;
    private static final int ID_ROMPER_HUCHA = 4;
    private static final int ID_COMPRAR_CHUCHE = 5;

    public static void parcheEscena(BaseDatos bd, ArrayList<Integer> detalle, ArrayList<Accion> acciones) {

        String lugarId = bd.getActor(Actor.PROTAGONISTA).getLugar();

        // --------------------------------
        // El caso del periquito extraviado
        // --------------------------------

        if(bd.getCaso(CASO_PERIQUITO_EXTRAVIADO).getResuelto()) {

            // Caso Resuelto

            if(lugarId.equals(ID_QUIOSCO)) {

                // El periquito está en la jaula
                detalle.add(R.string.periquito_enjaulado);
            }

        } else {

            // Caso Pendiente

            if(lugarId.equals(ID_QUIOSCO)) {

                // El periquito ha escapado
                detalle.add(R.string.periquito_escapado);

            } else if(bd.getActor(ID_PERIQUITO).getLugar().equals(lugarId)) {

                if(bd.getObjeto(ID_PAN).getLugar().equals(lugarId)) {

                    // El periquito está picoteando el pan
                    detalle.add(R.string.periquito_picoteando);

                    // Acción: Atrapar el periquito
                    acciones.add(new Accion(ID_ATRAPAR_PERIQUITO, R.string.accion_atrapar_periquito));
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

            if(lugarId.equals(ID_BIBLIOTECA)) {

                // El libro de historia está en la biblioteca
                detalle.add(R.string.libros_encontrado_historia);

            } else if(lugarId.equals(ID_PIZZERIA)) {

                // El libro de recetas está en la pizzería
                detalle.add(R.string.libros_encontrado_recetas);
            }
        } else {

            // Caso Pendiente

            String lugarLibroHistoria = bd.getObjeto(ID_LIBRO_HISTORIA).getLugar();
            String lugarLibroRecetas = bd.getObjeto(ID_LIBRO_RECETAS).getLugar();

            if(lugarId.equals(ID_BIBLIOTECA)) {

                if(lugarLibroHistoria.equals(ID_BIBLIOTECA)) {

                    // El libro de historia está en la biblioteca
                    detalle.add(R.string.libros_encontrado_historia);
                } else {

                    // El libro de historia se ha extraviado
                    detalle.add(R.string.libros_extraviado_historia);
                }


            } else if(lugarId.equals(ID_PIZZERIA)) {

                if(lugarLibroRecetas.equals(ID_PIZZERIA)) {

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

            if(lugarId.equals(ID_PENSION)) {

                // El gato está en casa
                detalle.add(R.string.gato_casa);
            }
        } else {

            // Caso Pendiente

            Objeto lataSardinas = bd.getObjeto(ID_LATA_SARDINAS);

            if(lugarId.equals(ID_PENSION)) {

                // El gato se ha escapado
                detalle.add(R.string.gato_escapado);

            } else if(bd.getActor(ID_GATO).getLugar().equals(lugarId)){

                if(lataSardinas.getLugar().equals(lugarId) && lataSardinas.getEstado().equals(ID_ABIERTO)) {

                    // El gato está comiendo
                    detalle.add(R.string.gato_comiendo);

                    // Acción: Atrapar el gato
                    acciones.add(new Accion(ID_ATRAPAR_GATO, R.string.accion_atrapar_gato));
                } else {

                    // El gato está por aqí
                    detalle.add(R.string.gato_aqui);
                }
            }

            //
            if(lataSardinas.getLugar().equals(Lugar.BOLSILLO) && lataSardinas.getEstado().equals(ID_CERRADO)
                    && bd.getObjeto(ID_ABRELATAS).getLugar().equals(Lugar.BOLSILLO)) {

                // Acción: Abrir la lata con el abrelatas
                acciones.add(new Accion(ID_ABRIR_LATA, R.string.accion_abrir_lata));
            }
        }

        // ----------------------------
        // El caso del pedrusco birlado
        // ----------------------------

        if(bd.getCaso(CASO_PEDRUSCO).getResuelto()) {

            // Caso Resuelto

            if(lugarId.equals(ID_GIMNASIO)) {

                // El pedrusco está en el gimnasio
                detalle.add(R.string.pedrusco_encontrado);
            }
        } else {

            // Caso Pendiente

            if(lugarId.equals(ID_GIMNASIO)) {

                // Han birlado el pedrusco
                detalle.add(R.string.pedrusco_birlado);
            }
        }

        // --------------------------------
        // El caso de la chuche engorrinada
        // --------------------------------

        if(bd.getCaso(CASO_CHUCHE).getResuelto()) {

            // Caso Resuelto

            if(lugarId.equals(ID_PARQUE_VERDOR)) {

                // Andresito está devorando la chuche
                detalle.add(R.string.chuche_devorando);
            }
        } else {

            // Caso Pendiente

            if(lugarId.equals(ID_PARQUE_VERDOR)) {

                // Andresito está llorando, porque la chuche se le ha engorrinado
                detalle.add(R.string.chuche_engorrinada);
            }

            //
            if(lugarId.equals(ID_QUIOSCO) && bd.getObjeto(ID_MONEDA).getLugar().equals(Lugar.BOLSILLO)) {

                // Acción: Comprar una chuche
                acciones.add(new Accion(ID_COMPRAR_CHUCHE, R.string.accion_comprar_chuche));
            }

            //
            Objeto martillo = bd.getObjeto(ID_MARTILLO);
            Objeto hucha = bd.getObjeto(ID_HUCHA);

            //
            if(martillo.getLugar().equals(Lugar.BOLSILLO) && hucha.getLugar().equals(Lugar.BOLSILLO) &&
                    !hucha.getEstado().equals(ID_ROTO)) {

                // Acción: Romper la hucha con el martillo
                acciones.add(new Accion(ID_ROMPER_HUCHA, R.string.accion_romper_hucha));
            }
        }

        // ----------------------------
        // El caso del martillo rarillo
        // ----------------------------

        if(bd.getCaso(CASO_MARTILLO).getResuelto()) {

            // Caso Resuelto

            if(lugarId.equals(ID_TALLER)) {

                // Añadir detalle, dependiendo de que el martillo esté o no
                // en el taller (cuando se resuelve el caso, no se le puede
                // enviar al limbo, pues también es necesario para resolver el caso
                // de la chuche engorrinada).
                if(bd.getObjeto(ID_MARTILLO).getLugar().equals(ID_TALLER)) {

                    // El martillo está en el taller
                    detalle.add(R.string.martillo_taller);
                } else {
                    // El martillo no está en el taller
                    detalle.add(R.string.martillo_fregona);
                }
            }
        } else {

            // Caso Pendiente

            if(lugarId.equals(ID_TALLER)) {

                // El martillo ha desaparecido
                detalle.add(R.string.martillo_desaparecido);
            }
        }

        // ----------------------------
        // El caso del análisis perdido
        // ----------------------------

        if(bd.getCaso(CASO_ANALISIS).getResuelto()) {

            // Caso resuelto

            if(lugarId.equals(ID_CONSULTORIO)) {

                // El análisis ha sido encontrado
                detalle.add(R.string.analisis_encontrado);
            }
        } else {

            // Caso pendiente

            if(lugarId.equals(ID_CONSULTORIO)) {

                // El análisis se ha perdido
                detalle.add(R.string.analisis_perdido);

            } else if(lugarId.equals(ID_OFICINA)) {

                //
                Objeto pendrive = bd.getObjeto(ID_PENDRIVE);

                //
                if(pendrive.getLugar().equals(Lugar.BOLSILLO)) {

                    acciones.add(new Accion(ID_ENCHUFAR_PENDRIVE, R.string.accion_enchufar_pendrive));

                } else if(pendrive.getLugar().equals(ID_OFICINA) && pendrive.getEstado().equals(ID_ENCHUFADO)) {

                    detalle.add(R.string.analisis_pendrive);
                }
            }
        }

        // -------------------------
        // El caso del perro cansino
        // -------------------------

        if(bd.getActor(ID_PERRO).getLugar().equals(lugarId)) {

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

    public static void protaCambiaLugar(BaseDatos bd, String idLugarOrigen, String idLugarDestino) {

        // -------------------------
        // El caso del perro cansino
        // -------------------------

        if(!bd.getCaso(CASO_PERRO_CANSINO).getResuelto()) {

            Actor perro = bd.getActor(ID_PERRO);

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
     * Puede acceder a la base de datos.
     *
     * @param bd
     * @param accionId
     *
     * @return
     */
    public static int doAccion(BaseDatos bd, int accionId) {

        String lugarId = bd.getActor(Actor.PROTAGONISTA).getLugar();

        Actor act;
        Objeto obj;

        switch(accionId) {
            case ID_ATRAPAR_PERIQUITO :
                act = bd.getActor(ID_PERIQUITO);
                act.setLugar(ID_QUIOSCO);
                bd.updateActor(act);
                return CASO_PERIQUITO_EXTRAVIADO;
            case ID_ATRAPAR_GATO :
                act = bd.getActor(ID_GATO);
                act.setLugar(ID_PENSION);
                bd.updateActor(act);
                return CASO_GATO_COLORAO;
            case ID_ABRIR_LATA :
                obj = bd.getObjeto(ID_LATA_SARDINAS);
                obj.setEstado(ID_ABIERTO);
                bd.updateObjeto(obj);
                return 0;
            case ID_ROMPER_HUCHA :
                obj = bd.getObjeto(ID_HUCHA);
                obj.setEstado(ID_ROTO);
                bd.updateObjeto(obj);
                obj = bd.getObjeto(ID_MONEDA);
                obj.setLugar(lugarId);
                bd.updateObjeto(obj);
                return 0;
            case ID_COMPRAR_CHUCHE :
                obj = bd.getObjeto(ID_CHUCHE);
                obj.setLugar(Lugar.BOLSILLO);
                bd.updateObjeto(obj);
                obj = bd.getObjeto(ID_MONEDA);
                obj.setLugar(ID_LIMBO);
                bd.updateObjeto(obj);
                return 0;
            case ID_ENCHUFAR_PENDRIVE:
                obj = bd.getObjeto(ID_PENDRIVE);
                obj.setLugar(lugarId);
                obj.setEstado(ID_ENCHUFADO);
                bd.updateObjeto(obj);
                return 0;
            default :
                return 0;
        }
    }

    public static int puedeTomarObjeto(BaseDatos bd, Objeto objeto, ArrayList<Objeto> bolsillo) {

        // Comprobar si puede cargar más objetos
        if(bolsillo.size() >= MAX_OBJETOS_BOLSILLO) {

            return R.string.tomar_demasiados_objetos;
        }

        // El pedrusco sólo se puede coger si ya tenemos la carretilla
        if(objeto.getId().equals(ID_PEDRUSCO) && !bd.getObjeto(ID_CARRETILLA).getLugar().equals(Lugar.BOLSILLO)) {

            return R.string.pedrusco_falta_carretilla;
        }

        // Ok, puede tomar el objeto
        return 0;
    }

    public static int objetoTomado(BaseDatos bd, Objeto objeto, ArrayList<Objeto> bolsillo) {

        if(objeto.getId().equals(ID_PENDRIVE)) {
            if(objeto.getEstado().equals(ID_ENCHUFADO)) {
                objeto.setEstado(ID_DESENCHUFADO);
                bd.updateObjeto(objeto);
            }
        }

        return 0;
    }

    public static int objetoDejado(BaseDatos bd, Objeto objeto, ArrayList<Objeto> bolsillo) {

        // --------------------------------
        // El caso del periquito extraviado
        // --------------------------------

        // Nada

        // -------------------------------
        // El caso de los libros cambiados
        // -------------------------------

        // Si el libro de historia está en la biblioteca, y
        // el libro de recetas está en la pizzería, el caso está resuelto

        if(!bd.getCaso(CASO_LIBROS_CAMBIADOS).getResuelto() &&
                (objeto.getId().equals(ID_LIBRO_HISTORIA) || objeto.getId().equals(ID_LIBRO_RECETAS))) {

            // Comprobar si los 2 libros están en su sitio
            Objeto libroHistoria = bd.getObjeto(ID_LIBRO_HISTORIA);
            Objeto libroRecetas = bd.getObjeto(ID_LIBRO_RECETAS);

            // Si los 2 libros están en su sitio, caso resuelto
            if(libroHistoria.getLugar().equals(ID_BIBLIOTECA) && libroRecetas.getLugar().equals(ID_PIZZERIA)) {

                // Poner los libros en el limbo, para que no puedan ser vueltos a tomar
                libroHistoria.setLugar(ID_LIMBO); bd.updateObjeto(libroHistoria);
                libroRecetas.setLugar(ID_LIMBO); bd.updateObjeto(libroRecetas);

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
                (objeto.getId().equals(ID_CARRETILLA) || objeto.getId().equals(ID_PEDRUSCO))) {

            // Si el pedrusco continúa en el bolsillo, significa que se ha
            // dejado la carretilla; dejar también el pedrusco.
            Objeto pedrusco = bd.getObjeto(ID_PEDRUSCO);

            if(pedrusco.getLugar().equals(Lugar.BOLSILLO)) {

                // Dejar el pedrusco también
                pedrusco.setLugar(objeto.getLugar());
                bd.updateObjeto(pedrusco);
            }

            // Si se ha dejado el pedrusco en el gimnasio, el caso está resuelto
            if(pedrusco.getLugar().equals(ID_GIMNASIO)) {

                // Dejar pedrusco en el limbo, para que no se pueda volver a tomar
                pedrusco.setLugar(ID_LIMBO);
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
                objeto.getId().equals(ID_CHUCHE) &&
                objeto.getLugar().equals(ID_PARQUE_VERDOR)) {

            // Dejar la chuche en el limbo
            objeto.setLugar(ID_LIMBO);
            bd.updateObjeto(objeto);

            // Caso resuelto
            return CASO_CHUCHE;
        }

        // ----------------------------
        // El caso del martillo rarillo
        // ----------------------------

        // Si ha dejado el martillo en el taller, el caso está resuelto

        if(!bd.getCaso(CASO_MARTILLO).getResuelto() &&
                objeto.getId().equals(ID_MARTILLO) &&
                objeto.getLugar().equals(ID_TALLER)) {

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
                objeto.getId().equals(ID_PENDRIVE) &&
                objeto.getLugar().equals(ID_CONSULTORIO)) {

            // Dejar el pendrive en el limbo
            objeto.setLugar(ID_LIMBO);
            bd.updateObjeto(objeto);

            // Caso resuelto
            return CASO_ANALISIS;
        }

        // -------------------------
        // El caso del perro cansino
        // -------------------------

        // Si ha dejado el hueso donde está el perro, caso resuelto

        if(!bd.getCaso(CASO_PERRO_CANSINO).getResuelto() &&
                objeto.getId().equals(ID_HUESO) &&
                bd.getActor(ID_PERRO).getLugar().equals(bd.getActor(Actor.PROTAGONISTA).getLugar())) {

            // Dejar el hueso en el limbo
            objeto.setLugar(ID_LIMBO);
            bd.updateObjeto(objeto);

            // Caso resuelto
            return CASO_PERRO_CANSINO;
        }

        // No se ha resuelto ningún caso
        return 0;
    }
}
