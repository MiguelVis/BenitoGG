package com.app.floppysoftware.benitogg;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests JUnit4 para la clase Caso.
 */
public class CasoTest {

    // Valores para testeo
    private static final int CASO_ID = 1;                   // Id
    private static final String CASO_NOMBRE = "Caso";       // Nombre
    private static final boolean CASO_RESUELTO = false;     // Resuelto

    // Caso
    private Caso caso;

    /**
     * Este método se ejecuta antes de cada método de test. Inicializar
     * la activity y el entorno.
     *
     * @throws Exception  excepción
     */
    @Before
    public void setUp() throws Exception {

        // Crear caso con valores conocidos
        caso = new Caso(CASO_ID, CASO_NOMBRE, CASO_RESUELTO);

        // Comprobar resultado correcto
        assertNotNull(caso);
    }

    /**
     * Comprobar getters.
     *
     * @throws Exception  excepción
     */
    @Test
    public void testGetters() throws Exception {

        // Getters
        assertEquals(CASO_ID, caso.getId());                // Id
        assertEquals(CASO_NOMBRE, caso.getNombre());        // Nombre
        assertEquals(CASO_RESUELTO, caso.getResuelto());    // Resuelto
    }

    /**
     * Comprobar setters.
     *
     * @throws Exception  excepción
     */
    @Test
    public void testSetters() throws Exception {

        // Fijar caso resuelto a true
        caso.setResuelto(true);

        // Comprobar getter
        assertTrue(caso.getResuelto());

        // Fijar caso resuelto a false
        caso.setResuelto(false);

        // Comprobar getter
        assertFalse(caso.getResuelto());
    }
}