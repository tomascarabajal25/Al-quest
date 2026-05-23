package modelos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JugadorTest {
    private Jugador jugador;

    @BeforeEach
    public void setUp(){
        jugador = new Jugador("Jugador");
    }

    @Test
    public void testCrearJugador(){
        assertNotNull(jugador);
    }

    @Test
    public void testEquals(){
        Jugador jugador2 = new Jugador("Jugador");
        assertTrue(jugador.equals(jugador2));
    }

    @Test
    public void testNotEquals(){
        Jugador jugador2 = new Jugador("Jugador2");
        assertFalse(jugador.equals(jugador2));
    }

    @Test
    public void testHashCode(){
        Jugador jugador2 = new Jugador("Jugador");
        assertEquals(jugador.hashCode(), jugador2.hashCode());
    }

    @Test
    public void testToString(){
        assertTrue(jugador.toString().contains("Jugador"));
    }

    @Test
    public void testGetNombre(){
        assertEquals("Jugador", jugador.getNombre());
    }
}
