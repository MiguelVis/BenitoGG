package com.app.floppysoftware.benitogg;

import android.app.Instrumentation;
import android.content.Intent;
import android.content.IntentFilter;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;

import com.app.floppysoftware.benitogg.activities.PrincipalActivity;

/**
 * Tests para el apartado 'Info.' del menú.
 */
public class MenuInfoTest extends ActivityInstrumentationTestCase2<PrincipalActivity> {

    // Referencia de la activity
    private PrincipalActivity activity;

    /**
     * Constructor.
     */
    public MenuInfoTest() {

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

        // Tomar la referencia del botón de info.
        Button botonInfo = (Button) activity.findViewById(R.id.buttonInfo);

        // Comprobar que la referencia es válida
        assertNotNull(botonInfo);

        // Clickar el botón de info.
        TouchUtils.clickView(this, botonInfo);
    }

    /**
     * Comprobar el funcionamiento del botón 'Visitar Web'.
     */
    public void testWeb() {

        // Tomar la referencia del botón
        Button botonWeb = (Button) activity.findViewById(R.id.buttonWeb);

        // Comprobar que la referencia es válida
        assertNotNull(botonWeb);

        // Comprobar que se abre la web al pulsar el botón -- FIXME!!!! lo de abajo funciona, pero hay que saber por qué


        // http://lixiantao.googlecode.com/svn/trunk/3500_Code_Android_Application_Testing_Guide/3500_03_source/MyFirstProjectTest/src/com/example/aatg/myfirstproject/test/MyFirstProjectActivityTests.java


        //
        final Instrumentation inst = getInstrumentation();
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_VIEW);
        intentFilter.addDataScheme("http");
        intentFilter.addCategory(Intent.CATEGORY_BROWSABLE);
        Instrumentation.ActivityMonitor monitor = inst.addMonitor(intentFilter, null, false);
        assertEquals(0, monitor.getHits());


        // Clickar el botón
        TouchUtils.clickView(this, botonWeb);


        //
        monitor.waitForActivityWithTimeout(5000);
        assertEquals(1, monitor.getHits());
        inst.removeMonitor(monitor);

    }
}
