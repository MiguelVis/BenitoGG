package com.app.floppysoftware.benitogg;

import com.app.floppysoftware.benitogg.models.Objeto;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test JUnit4 para la clase Objeto.
 */
public class ObjetoTest {

    // Valores para testeo
    private static final String OBJETO_ID = "Id";             // Id
    private static final String OBJETO_NOMBRE = "Nombre";     // Nombre
    private static final String OBJETO_LUGAR = "Lugar";       // Lugar
    private static final String OBJETO_ESTADO = "Estado";     // Estado
    private static final String OBJETO_LUGAR_2 = "Lugar_2";   // Lugar alternativo
    private static final String OBJETO_ESTADO_2 = "Estado_2"; // Estado alternativo

    // Objeto
    private Objeto objeto;

    /**
     * Este método se ejecuta antes de cada método de test. Inicializar
     * la activity y el entorno.
     *
     * @throws Exception  excepción
     */
    @Before
    public void setUp() throws Exception {

        // Crear objeto
        objeto = new Objeto(OBJETO_ID, OBJETO_NOMBRE, OBJETO_LUGAR, OBJETO_ESTADO);

        // Comprobar resultado correcto
        assertNotNull(objeto);

    }

    /**
     * Comprobar getters.
     *
     * @throws Exception  excepción
     */
    @Test
    public void testGetters() throws Exception {

        // Getters
        assertEquals(OBJETO_ID, objeto.getId());            // Id
        assertEquals(OBJETO_NOMBRE, objeto.getNombre());    // Nombre
        assertEquals(OBJETO_LUGAR, objeto.getLugar());      // Lugar
        assertEquals(OBJETO_ESTADO, objeto.getEstado());    // Estado
    }

    /**
     * Comprobar setters.
     *
     * @throws Exception  excepción
     */
    @Test
    public void testSetters() throws Exception {

        // Cambiar el lugar
        objeto.setLugar(OBJETO_LUGAR_2);

        // Comprobar getter de lugar
        assertEquals(OBJETO_LUGAR_2, objeto.getLugar());

        // Cambiar el estado
        objeto.setEstado(OBJETO_ESTADO_2);

        // Comprobar getter de estado
        assertEquals(OBJETO_ESTADO_2, objeto.getEstado());
    }
}