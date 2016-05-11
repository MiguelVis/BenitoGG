package com.app.floppysoftware.benitogg;

import com.app.floppysoftware.benitogg.models.Accion;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests JUnit4 para la clase Accion.
 */
public class AccionTest {

    // Valores para testeo
    private static final int ACCION_ID = 1;            // Id
    private static final int ACCION_STRING_ID = 32;    // Id del recurso de string

    // Accion
    private Accion accion;

    /**
     * Este método se ejecuta antes de cada método de test. Inicializar
     * la activity y el entorno.
     *
     * @throws Exception  excepción
     */
    @Before
    public void setUp() throws Exception {

        // Crear acción con valores conocidos
        accion = new Accion(ACCION_ID, ACCION_STRING_ID);

        // Comprobar resultado correcto
        assertNotNull(accion);
    }

    /**
     * Comprobar getters.
     *
     * @throws Exception  excepción
     */
    @Test
    public void testGetters() throws Exception {

        // Getters
        assertEquals(ACCION_ID, accion.getId());                // Id
        assertEquals(ACCION_STRING_ID, accion.getStringId());   // Id del recurso de string
    }
}