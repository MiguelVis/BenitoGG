package com.app.floppysoftware.benitogg;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Tests para el apartado 'Jugar' del menú.
 */
public class MenuJuegoTest extends ActivityInstrumentationTestCase2<PrincipalActivity> {

    // Referencia de la activity
    private PrincipalActivity activity;

    // True si es una tablet, false si es un móvil
    private boolean esTablet;

    /**
     * Constructor.
     */
    public MenuJuegoTest() {

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

        // Tomar referencia de la activity
        activity = getActivity();

        // Comprobar que la activity ha sido creada
        assertNotNull(activity);

        // Averiguar si es tablet o móvil
        esTablet = activity.getResources().getBoolean(R.bool.isTablet);  // FIXME!! excepción

        // Abrir base de datos
        BaseDatos bd = new BaseDatos(activity, false);

        // Comprobar que se ha abierto correctamente
        assertNotNull(bd);

        // Leer actor protagonista
        Actor prota = bd.getActor(Actor.PROTAGONISTA);

        // Comprobar que se ha leído correctamente
        assertNotNull(prota);

        // Cambiarlo al Lugar Centro
        prota.setLugar("testLugarCentro");

        // Actualizar el actor en la base de datos
        bd.updateActor(prota);

        // Cerrar base de datos
        bd.cerrar();

        // Tomar la referencia del botón de juego
        Button botonJugar = (Button) activity.findViewById(R.id.buttonJugar);

        // Comprobar que la referencia es válida
        assertNotNull(botonJugar);

        // Clickar el botón
        TouchUtils.clickView(this, botonJugar);
    }

    /**
     * Comprobar el funcionamiento de las acciones.
     */
    public void testAcciones() {

        // Referencias de las ImageView de direcciones
        ImageView imViNorte;
        ImageView imViSur;
        ImageView imViEste;
        ImageView imViOeste;

        // Tomar la referencia del ImageView de Más Acciones (...)
        ImageView imViMasAcciones = getImViMas();

        // Comprobaciones
        if(esTablet) {

            // Comprobar que la referencia al ImageView de Más Acciones es null
            assertNull(imViMasAcciones);
        } else {
            // Comprobar que la referencia al ImageView de Más Acciones es válida
            assertNotNull(imViMasAcciones);

            // Tomar las referencias de los ImageView de direcciones
            imViNorte = getImViNorte();
            imViSur = getImViSur();
            imViEste = getImViEste();
            imViOeste = getImViOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(imViNorte);
            assertNotNull(imViSur);
            assertNotNull(imViEste);
            assertNotNull(imViOeste);

            // Comprobar el funcionamiento de las direcciones

            // Comprobar el estado de los ImageView para el Lugar Centro
            assertTrue(imViNorte.isEnabled());
            assertTrue(imViSur.isEnabled());
            assertTrue(imViEste.isEnabled());
            assertTrue(imViOeste.isEnabled());

            // Ir al Lugar Norte
            TouchUtils.clickView(this, imViNorte);

            // Comprobar el estado de los ImageView para el Lugar Norte
            assertFalse(imViNorte.isEnabled());
            assertTrue(imViSur.isEnabled());
            assertFalse(imViEste.isEnabled());
            assertFalse(imViOeste.isEnabled());

            // Volver al Lugar Centro
            TouchUtils.clickView(this, imViSur);

            // Comprobar el estado de los ImageView para el Lugar Centro
            assertTrue(imViNorte.isEnabled());
            assertTrue(imViSur.isEnabled());
            assertTrue(imViEste.isEnabled());
            assertTrue(imViOeste.isEnabled());

            // Ir al Lugar Sur
            TouchUtils.clickView(this, imViSur);

            // Comprobar el estado de los ImageView para el Lugar Sur
            assertTrue(imViNorte.isEnabled());
            assertFalse(imViSur.isEnabled());
            assertFalse(imViEste.isEnabled());
            assertFalse(imViOeste.isEnabled());

            // Volver al Lugar Centro
            TouchUtils.clickView(this, imViNorte);

            // Comprobar el estado de los ImageView para el Lugar Centro
            assertTrue(imViNorte.isEnabled());
            assertTrue(imViSur.isEnabled());
            assertTrue(imViEste.isEnabled());
            assertTrue(imViOeste.isEnabled());

            // Ir al Lugar Este
            TouchUtils.clickView(this, imViEste);

            // Comprobar el estado de los ImageView para el Lugar Este
            assertFalse(imViNorte.isEnabled());
            assertFalse(imViSur.isEnabled());
            assertFalse(imViEste.isEnabled());
            assertTrue(imViOeste.isEnabled());

            // Volver al Lugar Centro
            TouchUtils.clickView(this, imViOeste);

            // Comprobar el estado de los ImageView para el Lugar Centro
            assertTrue(imViNorte.isEnabled());
            assertTrue(imViSur.isEnabled());
            assertTrue(imViEste.isEnabled());
            assertTrue(imViOeste.isEnabled());

            // Ir al Lugar Oeste
            TouchUtils.clickView(this, imViOeste);

            // Comprobar el estado de los ImageView para el Lugar Oeste
            assertFalse(imViNorte.isEnabled());
            assertFalse(imViSur.isEnabled());
            assertTrue(imViEste.isEnabled());
            assertFalse(imViOeste.isEnabled());

            // Volver al Lugar Centro
            TouchUtils.clickView(this, imViEste);

            // Comprobar el estado de los ImageView para el Lugar Centro
            assertTrue(imViNorte.isEnabled());
            assertTrue(imViSur.isEnabled());
            assertTrue(imViEste.isEnabled());
            assertTrue(imViOeste.isEnabled());

            // Clickar para que aparezcan todas las acciones
            TouchUtils.clickView(this, imViMasAcciones);
        }

        // Comprobaciones sobre el fragment de acciones

        // En la version móvil, cada vez que se clicka el botón de Más Acciones (...),
        // aparece el fragment con todas las acciones.
        //
        // Una vez seleccionada la acción, se restaura el fragment de la escena, por
        // lo que cada vez que aparezca el fragment con las acciones,
        // hay que regenerar las referencias a los ImageViews, etc.
        //
        // No es conveniente utilizar funciones helper que contengan aserciones, ya que si
        // alguna de éstas falla, no se sabría en qué punto exacto del flujo ha fallado.
        //
        // De ahí que se repitan ciertos bloques con el mismo código.
        //
        // Este problema no existe en la versión tablet, ya que el fragment de las
        // acciones está permanentemente en pantalla.

        // Comprobar direcciones

        // Tomar las referencias de los ImageView de direcciones
        imViNorte = getImViNorte();
        imViSur = getImViSur();
        imViEste = getImViEste();
        imViOeste = getImViOeste();

        // Comprobar que las referencias son válidas
        assertNotNull(imViNorte);
        assertNotNull(imViSur);
        assertNotNull(imViEste);
        assertNotNull(imViOeste);

        // Comprobar el estado de los ImageView para el Lugar Centro
        assertTrue(imViNorte.isEnabled());
        assertTrue(imViSur.isEnabled());
        assertTrue(imViEste.isEnabled());
        assertTrue(imViOeste.isEnabled());

        // Ir al Lugar Norte
        TouchUtils.clickView(this, imViNorte);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTablet) {

            // Tomar la referencia del ImageView de Más Acciones
            imViMasAcciones = getImViMas();

            // Comprobar que la referencia es válida
            assertNotNull(imViMasAcciones);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, imViMasAcciones);

            // Tomar las referencias de los ImageView de direcciones
            imViNorte = getImViNorte();
            imViSur = getImViSur();
            imViEste = getImViEste();
            imViOeste = getImViOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(imViNorte);
            assertNotNull(imViSur);
            assertNotNull(imViEste);
            assertNotNull(imViOeste);
        }

        // Comprobar el estado de los ImageView para el Lugar Norte
        assertFalse(imViNorte.isEnabled());
        assertTrue(imViSur.isEnabled());
        assertFalse(imViEste.isEnabled());
        assertFalse(imViOeste.isEnabled());

        // Volver al Lugar Centro
        TouchUtils.clickView(this, imViSur);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTablet) {

            // Tomar la referencia del ImageView de Más Acciones
            imViMasAcciones = getImViMas();

            // Comprobar que la referencia es válida
            assertNotNull(imViMasAcciones);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, imViMasAcciones);

            // Tomar las referencias de los ImageView de direcciones
            imViNorte = getImViNorte();
            imViSur = getImViSur();
            imViEste = getImViEste();
            imViOeste = getImViOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(imViNorte);
            assertNotNull(imViSur);
            assertNotNull(imViEste);
            assertNotNull(imViOeste);
        }

        // Comprobar que las referencias son válidas
        assertNotNull(imViNorte);
        assertNotNull(imViSur);
        assertNotNull(imViEste);
        assertNotNull(imViOeste);

        // Comprobar el estado de los ImageView para el Lugar Centro
        assertTrue(imViNorte.isEnabled());
        assertTrue(imViSur.isEnabled());
        assertTrue(imViEste.isEnabled());
        assertTrue(imViOeste.isEnabled());

        // Ir al Lugar Sur
        TouchUtils.clickView(this, imViSur);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTablet) {

            // Tomar la referencia del ImageView de Más Acciones
            imViMasAcciones = getImViMas();

            // Comprobar que la referencia es válida
            assertNotNull(imViMasAcciones);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, imViMasAcciones);

            // Tomar las referencias de los ImageView de direcciones
            imViNorte = getImViNorte();
            imViSur = getImViSur();
            imViEste = getImViEste();
            imViOeste = getImViOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(imViNorte);
            assertNotNull(imViSur);
            assertNotNull(imViEste);
            assertNotNull(imViOeste);
        }

        // Comprobar el estado de los ImageView para el Lugar Sur
        assertTrue(imViNorte.isEnabled());
        assertFalse(imViSur.isEnabled());
        assertFalse(imViEste.isEnabled());
        assertFalse(imViOeste.isEnabled());

        // Volver al Lugar Centro
        TouchUtils.clickView(this, imViNorte);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTablet) {

            // Tomar la referencia del ImageView de Más Acciones
            imViMasAcciones = getImViMas();

            // Comprobar que la referencia es válida
            assertNotNull(imViMasAcciones);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, imViMasAcciones);

            // Tomar las referencias de los ImageView de direcciones
            imViNorte = getImViNorte();
            imViSur = getImViSur();
            imViEste = getImViEste();
            imViOeste = getImViOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(imViNorte);
            assertNotNull(imViSur);
            assertNotNull(imViEste);
            assertNotNull(imViOeste);
        }

        // Comprobar que las referencias son válidas
        assertNotNull(imViNorte);
        assertNotNull(imViSur);
        assertNotNull(imViEste);
        assertNotNull(imViOeste);

        // Comprobar el estado de los ImageView para el Lugar Centro
        assertTrue(imViNorte.isEnabled());
        assertTrue(imViSur.isEnabled());
        assertTrue(imViEste.isEnabled());
        assertTrue(imViOeste.isEnabled());

        // Ir al Lugar Este
        TouchUtils.clickView(this, imViEste);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTablet) {

            // Tomar la referencia del ImageView de Más Acciones
            imViMasAcciones = getImViMas();

            // Comprobar que la referencia es válida
            assertNotNull(imViMasAcciones);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, imViMasAcciones);

            // Tomar las referencias de los ImageView de direcciones
            imViNorte = getImViNorte();
            imViSur = getImViSur();
            imViEste = getImViEste();
            imViOeste = getImViOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(imViNorte);
            assertNotNull(imViSur);
            assertNotNull(imViEste);
            assertNotNull(imViOeste);
        }

        // Comprobar el estado de los ImageView para el Lugar Este
        assertFalse(imViNorte.isEnabled());
        assertFalse(imViSur.isEnabled());
        assertFalse(imViEste.isEnabled());
        assertTrue(imViOeste.isEnabled());

        // Volver al Lugar Centro
        TouchUtils.clickView(this, imViOeste);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTablet) {

            // Tomar la referencia del ImageView de Más Acciones
            imViMasAcciones = getImViMas();

            // Comprobar que la referencia es válida
            assertNotNull(imViMasAcciones);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, imViMasAcciones);

            // Tomar las referencias de los ImageView de direcciones
            imViNorte = getImViNorte();
            imViSur = getImViSur();
            imViEste = getImViEste();
            imViOeste = getImViOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(imViNorte);
            assertNotNull(imViSur);
            assertNotNull(imViEste);
            assertNotNull(imViOeste);
        }

        // Comprobar que las referencias son válidas
        assertNotNull(imViNorte);
        assertNotNull(imViSur);
        assertNotNull(imViEste);
        assertNotNull(imViOeste);

        // Comprobar el estado de los ImageView para el Lugar Centro
        assertTrue(imViNorte.isEnabled());
        assertTrue(imViSur.isEnabled());
        assertTrue(imViEste.isEnabled());
        assertTrue(imViOeste.isEnabled());

        // Ir al Lugar Oeste
        TouchUtils.clickView(this, imViOeste);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTablet) {

            // Tomar la referencia del ImageView de Más Acciones
            imViMasAcciones = getImViMas();

            // Comprobar que la referencia es válida
            assertNotNull(imViMasAcciones);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, imViMasAcciones);

            // Tomar las referencias de los ImageView de direcciones
            imViNorte = getImViNorte();
            imViSur = getImViSur();
            imViEste = getImViEste();
            imViOeste = getImViOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(imViNorte);
            assertNotNull(imViSur);
            assertNotNull(imViEste);
            assertNotNull(imViOeste);
        }

        // Comprobar el estado de los ImageView para el Lugar Oeste
        assertFalse(imViNorte.isEnabled());
        assertFalse(imViSur.isEnabled());
        assertTrue(imViEste.isEnabled());
        assertFalse(imViOeste.isEnabled());

        // Volver al Lugar Centro
        TouchUtils.clickView(this, imViEste);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTablet) {

            // Tomar la referencia del ImageView de Más Acciones
            imViMasAcciones = getImViMas();

            // Comprobar que la referencia es válida
            assertNotNull(imViMasAcciones);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, imViMasAcciones);

            // Tomar las referencias de los ImageView de direcciones
            imViNorte = getImViNorte();
            imViSur = getImViSur();
            imViEste = getImViEste();
            imViOeste = getImViOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(imViNorte);
            assertNotNull(imViSur);
            assertNotNull(imViEste);
            assertNotNull(imViOeste);
        }

        // Comprobar que las referencias son válidas
        assertNotNull(imViNorte);
        assertNotNull(imViSur);
        assertNotNull(imViEste);
        assertNotNull(imViOeste);

        // Comprobar el estado de los ImageView para el Lugar Centro
        assertTrue(imViNorte.isEnabled());
        assertTrue(imViSur.isEnabled());
        assertTrue(imViEste.isEnabled());
        assertTrue(imViOeste.isEnabled());
    }

    private ImageView getImViNorte() {
        return (ImageView) activity.findViewById(R.id.imageViewNorte);
    }

    private ImageView getImViSur() {
        return (ImageView) activity.findViewById(R.id.imageViewSur);
    }

    private ImageView getImViEste() {
        return (ImageView) activity.findViewById(R.id.imageViewEste);
    }

    private ImageView getImViOeste() {
        return (ImageView) activity.findViewById(R.id.imageViewOeste);
    }

    private ImageView getImViMas() {
        return (ImageView) activity.findViewById(R.id.imageViewMas);
    }



}
