package com.app.floppysoftware.benitogg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * Tests para el apartado 'Opciones' del menú.
 */
public class MenuOpcionesTest extends ActivityInstrumentationTestCase2<PrincipalActivity> {

    // Referencia de la activity
    private PrincipalActivity activity;

    // True si es una tablet, false si es un móvil
    private boolean esTablet;

    /**
     * Constructor.
     */
    public MenuOpcionesTest() {

        // Llamar a la superclase
        super(PrincipalActivity.class);
    }

    /**
     * Este método se ejecuta antes de cada método de test. Inicializar
     * la activity y el entorno.
     *
     * @throws Exception  excepción
     */
    @Override
    public void setUp() throws Exception{

        // Llamar a la superclase
        super.setUp();

        // Tomar referencia de la activity
        activity = getActivity();

        // Comprobar que la activity ha sido creada
        assertNotNull(activity);

        // Averiguar si es tablet o móvil
        esTablet = activity.getResources().getBoolean(R.bool.isTablet); // FIXME!! excepción

        // Tomar la referencia del botón de opciones
        Button botonOpciones = (Button) activity.findViewById(R.id.buttonOpciones);

        // Comprobar que la referencia es válida
        assertNotNull(botonOpciones);

        // Clickar el botón de opciones
        TouchUtils.clickView(this, botonOpciones);
    }

    /**
     * Comprobar que el valor de las preferencias de sonido y el CheckBox que lo cambia
     * están coordinados.
     */
    public void testSonido() {

        // Tomar la referencia del CheckBox del sonido
        CheckBox checkBoxSonido = (CheckBox) activity.findViewById(R.id.checkBoxSonido);

        // Comprobar que la referencia es válida
        assertNotNull(checkBoxSonido);

        // Comprobar que coincide el valor de las preferencias y el estado del CheckBox
        assertEquals(Preferencias.getSonido(activity), checkBoxSonido.isChecked());

        // Clickar el CheckBox para invertir su estado
        TouchUtils.clickView(this, checkBoxSonido);

        // Comprobar que coincide el valor de las preferencias y el estado del CheckBox
        assertEquals(Preferencias.getSonido(activity), checkBoxSonido.isChecked());

        // Clickar el CheckBox para invertir su estado (lo deja en su estado inicial)
        TouchUtils.clickView(this, checkBoxSonido);

        // Comprobar que coincide el valor de las preferencias y el estado del CheckBox
        assertEquals(Preferencias.getSonido(activity), checkBoxSonido.isChecked());
    }

    /**
     * Comprobar que el valor de las preferencias de layout para zurdos y el CheckBox
     * que lo cambia están coordinados. Sólo tablets en orientación horizontal.
     */
    public void testZurdos() {

        // Tomar referencia del CheckBox
        CheckBox checkBoxZurdo = (CheckBox) activity.findViewById(R.id.checkBoxZurdo);

        // Comprobar que la referencia es válida
        assertNotNull(checkBoxZurdo);

        // Comprobar que el valor de las preferencias y el estado del CheckBox coinciden
        assertEquals(Preferencias.getZurdo(activity), checkBoxZurdo.isChecked());

        // Comprobaciones adicionales
        if(esTablet && !Preferencias.getVertical(activity)) {

            // Tablet, orientación horizontal

            // Comprobar que el CheckBox está habilitado
            assertTrue(checkBoxZurdo.isEnabled());

            // Clickar el CheckBox para invertir su estado
            TouchUtils.clickView(this, checkBoxZurdo);

            // Comprobar que el valor de las preferencias y el estado del CheckBox coinciden
            assertEquals(Preferencias.getZurdo(activity), checkBoxZurdo.isChecked());

            /******************************
            // Tomar referencia de la activity, ya que ha sido recreada al cambiar el layout
            activity = getActivity();

            // Clickar el CheckBox para invertir su estado (lo deja en su estado inicial)
            TouchUtils.clickView(this, checkBoxZurdo);

            // Tomar referencia de la activity, ya que ha sido recreada al cambiar el layout
            activity = getActivity();

            // Comprobar que el valor de las preferencias y el estado del CheckBox coinciden
            assertEquals(Preferencias.getZurdo(activity), checkBoxZurdo.isChecked());
             **********************/

        } else {

            // Comprobar que el CheckBox no está habilitado
            assertFalse(checkBoxZurdo.isEnabled());
        }
    }

    /**
     * Comprobar el buen funcionamiento del reset de la base de datos.
     */
    public void testReset() {

        // Referencias de los botones
        Button buttonReset = (Button) activity.findViewById(R.id.buttonReset);

        // Comprobar que la referencia del botón de reset es válida
        assertNotNull(buttonReset);

        // Referencia del cuadro de diálogo
        AlertDialog alertDialog;

        // Referencia del botón de continuar
        Button buttonContinuar;

        // Referencia del botón de cancelar
        Button buttonCancelar;

        // Desactivar el reset
        Preferencias.setReset(activity, false);

        // Comprobar que se ha cambiado
        assertFalse(Preferencias.getReset(activity));

        // ------------------------------------
        // Comprobación de cancelación de reset
        // ------------------------------------

        // Clickar el botón de reset
        TouchUtils.clickView(this, buttonReset);

        // Tomar cuadro de diálogo
        alertDialog = Mensaje.getAlertDialog();

        // Comprobar que no es nulo
        assertNotNull(alertDialog);

        // Tomar botón de cancelar
        buttonCancelar = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);

        // Comprobar que no es nulo
        assertNotNull(buttonCancelar);

        // Clickar el botón de cancelar
        TouchUtils.clickView(this, buttonCancelar);

        // Comprobar que no ha cambiado
        assertFalse(Preferencias.getReset(activity));

        // -------------------------------------
        // Comprobación de confirmación de reset
        // -------------------------------------

        // Clickar el botón de reset
        TouchUtils.clickView(this, buttonReset);

        // Tomar cuadro de diálogo
        alertDialog = Mensaje.getAlertDialog();

        // Comprobar que no es nulo
        assertNotNull(alertDialog);

        // Tomar botón de confirmación
        buttonContinuar = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

        // Comprobar que no es nulo
        assertNotNull(buttonContinuar);

        // Clickar el botón de confirmación
        TouchUtils.clickView(this, buttonContinuar);

        // Tomar cuadro de diálogo tras el reset
        alertDialog = Mensaje.getAlertDialog();

        // Comprobar que no es nulo
        assertNotNull(alertDialog);

        // Tomar botón de aceptación
        buttonContinuar = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);

        // Comprobar que no es nulo
        assertNotNull(buttonContinuar);

        // Clickar el botón de aceptación
        TouchUtils.clickView(this, buttonContinuar);

        // Comprobar que sí ha cambiado
        assertTrue(Preferencias.getReset(activity));
    }
}
