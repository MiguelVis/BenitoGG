package com.app.floppysoftware.benitogg;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests JUnit4 para la clase Actor.
 */
public class ActorTest {

    // Valores para testeo
    private static final String ACTOR_ID = "Id";            // Id del actor
    private static final String ACTOR_LUGAR = "Lugar";      // Lugar
    private static final String ACTOR_LUGAR_2 = "Lugar_2";  // Lugar alternativo

    // Actor
    private Actor actor;

    /**
     * Este método se ejecuta antes de cada método de test. Inicializar
     * la activity y el entorno.
     *
     * @throws Exception  excepción
     */
    @Before
    public void setUp() throws Exception {

        // Crear actor con Id y Lugar conocidos
        actor = new Actor(ACTOR_ID, ACTOR_LUGAR);

        // Comprobar resultado correcto
        assertNotNull(actor);
    }

    /**
     * Comprobar getters.
     *
     * @throws Exception  excepción
     */
    @Test
    public void testGetters() throws Exception {

        // Getters
        assertEquals(ACTOR_ID, actor.getId());          // Id
        assertEquals(ACTOR_LUGAR, actor.getLugar());    // Lugar
    }

    /**
     * Comprobar setters.
     *
     * @throws Exception  excepción
     */
    @Test
    public void testSetters() throws Exception {

        // Cambiar el lugar
        actor.setLugar(ACTOR_LUGAR_2);

        // Comprobar getter de lugar
        assertEquals(ACTOR_LUGAR_2, actor.getLugar());
    }
}