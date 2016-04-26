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

    // True si es una tablet con orientacion horizontal, false si es un móvil
    private boolean esTabletHorizontal;

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
        esTabletHorizontal = activity.getResources().getBoolean(R.bool.isTablet);  // FIXME!! excepción

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

        // Desactivar el reset
        Preferencias.setReset(activity, false);

        // Comprobar que se ha cambiado
        assertFalse(Preferencias.getReset(activity));

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

        // Referencias de los botones de direcciones
        Button btnNote;
        Button btnSur;
        Button btnEste;
        Button btnOeste;

        // Tomar la referencia del botón de Más Acciones (...)
        Button btnMas = getBtnMas();

        // Comprobaciones
        if(esTabletHorizontal) {

            // Comprobar que la referencia al ImageView de Más Acciones es null
            assertNull(btnMas);
        } else {
            // Comprobar que la referencia al ImageView de Más Acciones es válida
            assertNotNull(btnMas);

            // Tomar las referencias de los botones de direcciones
            btnNote = getBtnNorte();
            btnSur = getBtnSur();
            btnEste = getBtnEste();
            btnOeste = getBtnOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(btnNote.toString(), btnNote);
            assertNotNull(btnSur.toString(), btnSur);
            assertNotNull(btnEste.toString(), btnEste);
            assertNotNull(btnOeste.toString(), btnOeste);

            // Comprobar el funcionamiento de las direcciones

            // Comprobar el estado de los ImageView para el Lugar Centro
            assertTrue(btnNote.isEnabled());
            assertTrue(btnSur.isEnabled());
            assertTrue(btnEste.isEnabled());
            assertTrue(btnOeste.isEnabled());

            // Ir al Lugar Norte
            TouchUtils.clickView(this, btnNote);

            // Comprobar el estado de los ImageView para el Lugar Norte
            assertFalse(btnNote.isEnabled());
            assertTrue(btnSur.isEnabled());
            assertFalse(btnEste.isEnabled());
            assertFalse(btnOeste.isEnabled());

            // Volver al Lugar Centro
            TouchUtils.clickView(this, btnSur);

            // Comprobar el estado de los ImageView para el Lugar Centro
            assertTrue(btnNote.isEnabled());
            assertTrue(btnSur.isEnabled());
            assertTrue(btnEste.isEnabled());
            assertTrue(btnOeste.isEnabled());

            // Ir al Lugar Sur
            TouchUtils.clickView(this, btnSur);

            // Comprobar el estado de los ImageView para el Lugar Sur
            assertTrue(btnNote.isEnabled());
            assertFalse(btnSur.isEnabled());
            assertFalse(btnEste.isEnabled());
            assertFalse(btnOeste.isEnabled());

            // Volver al Lugar Centro
            TouchUtils.clickView(this, btnNote);

            // Comprobar el estado de los ImageView para el Lugar Centro
            assertTrue(btnNote.isEnabled());
            assertTrue(btnSur.isEnabled());
            assertTrue(btnEste.isEnabled());
            assertTrue(btnOeste.isEnabled());

            // Ir al Lugar Este
            TouchUtils.clickView(this, btnEste);

            // Comprobar el estado de los ImageView para el Lugar Este
            assertFalse(btnNote.isEnabled());
            assertFalse(btnSur.isEnabled());
            assertFalse(btnEste.isEnabled());
            assertTrue(btnOeste.isEnabled());

            // Volver al Lugar Centro
            TouchUtils.clickView(this, btnOeste);

            // Comprobar el estado de los ImageView para el Lugar Centro
            assertTrue(btnNote.isEnabled());
            assertTrue(btnSur.isEnabled());
            assertTrue(btnEste.isEnabled());
            assertTrue(btnOeste.isEnabled());

            // Ir al Lugar Oeste
            TouchUtils.clickView(this, btnOeste);

            // Comprobar el estado de los ImageView para el Lugar Oeste
            assertFalse(btnNote.isEnabled());
            assertFalse(btnSur.isEnabled());
            assertTrue(btnEste.isEnabled());
            assertFalse(btnOeste.isEnabled());

            // Volver al Lugar Centro
            TouchUtils.clickView(this, btnEste);

            // Comprobar el estado de los ImageView para el Lugar Centro
            assertTrue(btnNote.isEnabled());
            assertTrue(btnSur.isEnabled());
            assertTrue(btnEste.isEnabled());
            assertTrue(btnOeste.isEnabled());

            // Clickar para que aparezcan todas las acciones
            TouchUtils.clickView(this, btnMas);
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
        btnNote = getBtnNorte();
        btnSur = getBtnSur();
        btnEste = getBtnEste();
        btnOeste = getBtnOeste();

        // Comprobar que las referencias son válidas
        assertNotNull(btnNote);
        assertNotNull(btnSur);
        assertNotNull(btnEste);
        assertNotNull(btnOeste);

        // Comprobar el estado de los ImageView para el Lugar Centro
        assertTrue(btnNote.isEnabled());
        assertTrue(btnSur.isEnabled());
        assertTrue(btnEste.isEnabled());
        assertTrue(btnOeste.isEnabled());

        // Ir al Lugar Norte
        TouchUtils.clickView(this, btnNote);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTabletHorizontal) {

            // Tomar la referencia del ImageView de Más Acciones
            btnMas = getBtnMas();

            // Comprobar que la referencia es válida
            assertNotNull(btnMas);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, btnMas);

            // Tomar las referencias de los ImageView de direcciones
            btnNote = getBtnNorte();
            btnSur = getBtnSur();
            btnEste = getBtnEste();
            btnOeste = getBtnOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(btnNote);
            assertNotNull(btnSur);
            assertNotNull(btnEste);
            assertNotNull(btnOeste);
        }

        // Comprobar el estado de los ImageView para el Lugar Norte
        assertFalse(btnNote.isEnabled());
        assertTrue(btnSur.isEnabled());
        assertFalse(btnEste.isEnabled());
        assertFalse(btnOeste.isEnabled());

        // Volver al Lugar Centro
        TouchUtils.clickView(this, btnSur);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTabletHorizontal) {

            // Tomar la referencia del ImageView de Más Acciones
            btnMas = getBtnMas();

            // Comprobar que la referencia es válida
            assertNotNull(btnMas);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, btnMas);

            // Tomar las referencias de los ImageView de direcciones
            btnNote = getBtnNorte();
            btnSur = getBtnSur();
            btnEste = getBtnEste();
            btnOeste = getBtnOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(btnNote);
            assertNotNull(btnSur);
            assertNotNull(btnEste);
            assertNotNull(btnOeste);
        }

        // Comprobar que las referencias son válidas
        assertNotNull(btnNote);
        assertNotNull(btnSur);
        assertNotNull(btnEste);
        assertNotNull(btnOeste);

        // Comprobar el estado de los ImageView para el Lugar Centro
        assertTrue(btnNote.isEnabled());
        assertTrue(btnSur.isEnabled());
        assertTrue(btnEste.isEnabled());
        assertTrue(btnOeste.isEnabled());

        // Ir al Lugar Sur
        TouchUtils.clickView(this, btnSur);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTabletHorizontal) {

            // Tomar la referencia del ImageView de Más Acciones
            btnMas = getBtnMas();

            // Comprobar que la referencia es válida
            assertNotNull(btnMas);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, btnMas);

            // Tomar las referencias de los ImageView de direcciones
            btnNote = getBtnNorte();
            btnSur = getBtnSur();
            btnEste = getBtnEste();
            btnOeste = getBtnOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(btnNote);
            assertNotNull(btnSur);
            assertNotNull(btnEste);
            assertNotNull(btnOeste);
        }

        // Comprobar el estado de los ImageView para el Lugar Sur
        assertTrue(btnNote.isEnabled());
        assertFalse(btnSur.isEnabled());
        assertFalse(btnEste.isEnabled());
        assertFalse(btnOeste.isEnabled());

        // Volver al Lugar Centro
        TouchUtils.clickView(this, btnNote);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTabletHorizontal) {

            // Tomar la referencia del ImageView de Más Acciones
            btnMas = getBtnMas();

            // Comprobar que la referencia es válida
            assertNotNull(btnMas);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, btnMas);

            // Tomar las referencias de los ImageView de direcciones
            btnNote = getBtnNorte();
            btnSur = getBtnSur();
            btnEste = getBtnEste();
            btnOeste = getBtnOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(btnNote);
            assertNotNull(btnSur);
            assertNotNull(btnEste);
            assertNotNull(btnOeste);
        }

        // Comprobar que las referencias son válidas
        assertNotNull(btnNote);
        assertNotNull(btnSur);
        assertNotNull(btnEste);
        assertNotNull(btnOeste);

        // Comprobar el estado de los ImageView para el Lugar Centro
        assertTrue(btnNote.isEnabled());
        assertTrue(btnSur.isEnabled());
        assertTrue(btnEste.isEnabled());
        assertTrue(btnOeste.isEnabled());

        // Ir al Lugar Este
        TouchUtils.clickView(this, btnEste);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTabletHorizontal) {

            // Tomar la referencia del ImageView de Más Acciones
            btnMas = getBtnMas();

            // Comprobar que la referencia es válida
            assertNotNull(btnMas);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, btnMas);

            // Tomar las referencias de los ImageView de direcciones
            btnNote = getBtnNorte();
            btnSur = getBtnSur();
            btnEste = getBtnEste();
            btnOeste = getBtnOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(btnNote);
            assertNotNull(btnSur);
            assertNotNull(btnEste);
            assertNotNull(btnOeste);
        }

        // Comprobar el estado de los ImageView para el Lugar Este
        assertFalse(btnNote.isEnabled());
        assertFalse(btnSur.isEnabled());
        assertFalse(btnEste.isEnabled());
        assertTrue(btnOeste.isEnabled());

        // Volver al Lugar Centro
        TouchUtils.clickView(this, btnOeste);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTabletHorizontal) {

            // Tomar la referencia del ImageView de Más Acciones
            btnMas = getBtnMas();

            // Comprobar que la referencia es válida
            assertNotNull(btnMas);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, btnMas);

            // Tomar las referencias de los ImageView de direcciones
            btnNote = getBtnNorte();
            btnSur = getBtnSur();
            btnEste = getBtnEste();
            btnOeste = getBtnOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(btnNote);
            assertNotNull(btnSur);
            assertNotNull(btnEste);
            assertNotNull(btnOeste);
        }

        // Comprobar que las referencias son válidas
        assertNotNull(btnNote);
        assertNotNull(btnSur);
        assertNotNull(btnEste);
        assertNotNull(btnOeste);

        // Comprobar el estado de los ImageView para el Lugar Centro
        assertTrue(btnNote.isEnabled());
        assertTrue(btnSur.isEnabled());
        assertTrue(btnEste.isEnabled());
        assertTrue(btnOeste.isEnabled());

        // Ir al Lugar Oeste
        TouchUtils.clickView(this, btnOeste);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTabletHorizontal) {

            // Tomar la referencia del ImageView de Más Acciones
            btnMas = getBtnMas();

            // Comprobar que la referencia es válida
            assertNotNull(btnMas);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, btnMas);

            // Tomar las referencias de los ImageView de direcciones
            btnNote = getBtnNorte();
            btnSur = getBtnSur();
            btnEste = getBtnEste();
            btnOeste = getBtnOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(btnNote);
            assertNotNull(btnSur);
            assertNotNull(btnEste);
            assertNotNull(btnOeste);
        }

        // Comprobar el estado de los ImageView para el Lugar Oeste
        assertFalse(btnNote.isEnabled());
        assertFalse(btnSur.isEnabled());
        assertTrue(btnEste.isEnabled());
        assertFalse(btnOeste.isEnabled());

        // Volver al Lugar Centro
        TouchUtils.clickView(this, btnEste);

        // Móvil: Clickar para que aparezcan todas las acciones
        if(!esTabletHorizontal) {

            // Tomar la referencia del ImageView de Más Acciones
            btnMas = getBtnMas();

            // Comprobar que la referencia es válida
            assertNotNull(btnMas);

            // Clickar en el ImageView de Más Acciones
            TouchUtils.clickView(this, btnMas);

            // Tomar las referencias de los ImageView de direcciones
            btnNote = getBtnNorte();
            btnSur = getBtnSur();
            btnEste = getBtnEste();
            btnOeste = getBtnOeste();

            // Comprobar que las referencias son válidas
            assertNotNull(btnNote);
            assertNotNull(btnSur);
            assertNotNull(btnEste);
            assertNotNull(btnOeste);
        }

        // Comprobar que las referencias son válidas
        assertNotNull(btnNote);
        assertNotNull(btnSur);
        assertNotNull(btnEste);
        assertNotNull(btnOeste);

        // Comprobar el estado de los ImageView para el Lugar Centro
        assertTrue(btnNote.isEnabled());
        assertTrue(btnSur.isEnabled());
        assertTrue(btnEste.isEnabled());
        assertTrue(btnOeste.isEnabled());
    }

    private Button getBtnNorte() {
        return (Button) activity.findViewById(R.id.buttonNorte);
    }

    private Button getBtnSur() {
        return (Button) activity.findViewById(R.id.buttonSur);
    }

    private Button getBtnEste() {
        return (Button) activity.findViewById(R.id.buttonEste);
    }

    private Button getBtnOeste() {
        return (Button) activity.findViewById(R.id.buttonOeste);
    }

    private Button getBtnMas() {
        return (Button) activity.findViewById(R.id.buttonMas);
    }



}
