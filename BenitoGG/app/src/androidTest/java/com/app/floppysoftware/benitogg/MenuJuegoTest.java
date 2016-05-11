package com.app.floppysoftware.benitogg;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;

import com.app.floppysoftware.benitogg.activities.JuegoActivity;
import com.app.floppysoftware.benitogg.database.BaseDatos;
import com.app.floppysoftware.benitogg.models.Actor;

/**
 * Tests para el apartado 'Jugar' del menú.
 */
public class MenuJuegoTest extends ActivityInstrumentationTestCase2<JuegoActivity> {

    // Referencia de la activity
    private JuegoActivity activity;

    /**
     * Constructor.
     */
    public MenuJuegoTest() {

        // Llamar a la superclase
        super(JuegoActivity.class);
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

        //
        // activity.onAccionSeleccionada(-1, -1);
    }

    /**
     * Comprobar el funcionamiento de las acciones.
     */
    public void testAcciones() {

        // Referencias de los botones de direcciones
        Button btnNorte = (Button) activity.findViewById(R.id.buttonNorte);
        Button btnSur = (Button) activity.findViewById(R.id.buttonSur);
        Button btnEste = (Button) activity.findViewById(R.id.buttonEste);
        Button btnOeste = (Button) activity.findViewById(R.id.buttonOeste);

        // Comprobar que las referencias son válidas
        assertNotNull(btnNorte);
        assertNotNull(btnSur);
        assertNotNull(btnEste);
        assertNotNull(btnOeste);

        // Comprobar el funcionamiento de las direcciones

        // Comprobar el estado de los ImageView para el Lugar Centro
        assertTrue(btnNorte.isEnabled());
        assertTrue(btnSur.isEnabled());
        assertTrue(btnEste.isEnabled());
        assertTrue(btnOeste.isEnabled());

        // Ir al Lugar Norte
        TouchUtils.clickView(this, btnNorte);

        // Comprobar el estado de los ImageView para el Lugar Norte
        assertFalse(btnNorte.isEnabled());
        assertTrue(btnSur.isEnabled());
        assertFalse(btnEste.isEnabled());
        assertFalse(btnOeste.isEnabled());

        // Volver al Lugar Centro
        TouchUtils.clickView(this, btnSur);

        // Comprobar el estado de los ImageView para el Lugar Centro
        assertTrue(btnNorte.isEnabled());
        assertTrue(btnSur.isEnabled());
        assertTrue(btnEste.isEnabled());
        assertTrue(btnOeste.isEnabled());

        // Ir al Lugar Sur
        TouchUtils.clickView(this, btnSur);

        // Comprobar el estado de los ImageView para el Lugar Sur
        assertTrue(btnNorte.isEnabled());
        assertFalse(btnSur.isEnabled());
        assertFalse(btnEste.isEnabled());
        assertFalse(btnOeste.isEnabled());

        // Volver al Lugar Centro
        TouchUtils.clickView(this, btnNorte);

        // Comprobar el estado de los ImageView para el Lugar Centro
        assertTrue(btnNorte.isEnabled());
        assertTrue(btnSur.isEnabled());
        assertTrue(btnEste.isEnabled());
        assertTrue(btnOeste.isEnabled());

        // Ir al Lugar Este
        TouchUtils.clickView(this, btnEste);

        // Comprobar el estado de los ImageView para el Lugar Este
        assertFalse(btnNorte.isEnabled());
        assertFalse(btnSur.isEnabled());
        assertFalse(btnEste.isEnabled());
        assertTrue(btnOeste.isEnabled());

        // Volver al Lugar Centro
        TouchUtils.clickView(this, btnOeste);

        // Comprobar el estado de los ImageView para el Lugar Centro
        assertTrue(btnNorte.isEnabled());
        assertTrue(btnSur.isEnabled());
        assertTrue(btnEste.isEnabled());
        assertTrue(btnOeste.isEnabled());

        // Ir al Lugar Oeste
        TouchUtils.clickView(this, btnOeste);

        // Comprobar el estado de los ImageView para el Lugar Oeste
        assertFalse(btnNorte.isEnabled());
        assertFalse(btnSur.isEnabled());
        assertTrue(btnEste.isEnabled());
        assertFalse(btnOeste.isEnabled());

        // Volver al Lugar Centro
        TouchUtils.clickView(this, btnEste);

        // Comprobar el estado de los ImageView para el Lugar Centro
        assertTrue(btnNorte.isEnabled());
        assertTrue(btnSur.isEnabled());
        assertTrue(btnEste.isEnabled());
        assertTrue(btnOeste.isEnabled());
    }
}
