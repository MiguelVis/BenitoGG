package com.app.floppysoftware.benitogg;

import com.app.floppysoftware.benitogg.models.Lugar;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests JUnit4 para la clase Lugar.
 */
public class LugarTest {

    // Valores para testeo
    private static final String LUGAR_ID = "Id";            // Id
    private static final String LUGAR_TITULO = "Título";    // Título
    private static final String LUGAR_DETALLE = "Detalle";  // Detalle
    private static final String LUGAR_SONIDO = "Sonido";    // Sonido
    private static final int LUGAR_X = 99;                  // Posición X en mapa
    private static final int LUGAR_Y = 99;                  // Posición Y en mapa
    private static final String LUGAR_NORTE = "LugarNorte"; // Lugar Norte
    private static final String LUGAR_SUR = "LugarSur";     // Lugar Sur
    private static final String LUGAR_ESTE = "LugarEste";   // Lugar Este
    private static final String LUGAR_OESTE = "LugarOeste"; // Lugar Oeste

    // Lugar
    private Lugar lugar;

    /**
     * Este método se ejecuta antes de cada método de test. Inicializar
     * la activity y el entorno.
     *
     * @throws Exception  excepción
     */
    @Before
    public void setUp() throws Exception {

        // Crear lugar con valores conocidos
        lugar = new Lugar(LUGAR_ID, LUGAR_TITULO, LUGAR_DETALLE, LUGAR_SONIDO,
                LUGAR_X, LUGAR_Y,
                LUGAR_NORTE, LUGAR_SUR, LUGAR_ESTE, LUGAR_OESTE);

        // Comprobar resultado correcto
        assertNotNull(lugar);

    }

    /**
     * Comprobar getters.
     *
     * @throws Exception  excepción
     */
    @Test
    public void testGetters() throws Exception {

        // Getters
        assertEquals(LUGAR_ID, lugar.getId());              // Id
        assertEquals(LUGAR_TITULO, lugar.getTitulo());      // Título
        assertEquals(LUGAR_DETALLE, lugar.getDetalle());    // Detalle
        assertEquals(LUGAR_SONIDO, lugar.getSonido());      // Sonido
        assertEquals((long) LUGAR_X, (long) lugar.getX());  // Pos. X en mapa
        assertEquals((long) LUGAR_Y, (long) lugar.getY());  // Pos. Y en mapa
        assertEquals(LUGAR_NORTE, lugar.getLugarNorte());   // Lugar Norte
        assertEquals(LUGAR_SUR, lugar.getLugarSur());       // Lugar Sur
        assertEquals(LUGAR_ESTE, lugar.getLugarEste());     // Lugar Este
        assertEquals(LUGAR_OESTE, lugar.getLugarOeste());   // Lugar Oeste
    }
}