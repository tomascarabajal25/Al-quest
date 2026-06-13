package modelosVista;

import modelos.Jugador;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestVista {
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
    public void testConstructorNombreNull(){
        assertThrows(RuntimeException.class, () -> {
            new Jugador(null);
        });
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
    public void testEqualsConNull(){
        assertFalse(jugador.equals(null));
    }

    @Test
    public void testEqualsConOtroTipo(){
        assertFalse(jugador.equals("Jugador"));
        assertFalse(jugador.equals(42));
    }

    @Test
    public void testEqualsMismaInstancia(){
        assertTrue(jugador.equals(jugador));
    }

    @Test
    public void testHashCode(){
        Jugador jugador2 = new Jugador("Jugador");
        assertEquals(jugador.hashCode(), jugador2.hashCode());
    }

    @Test
    public void testHashCodeDistintos(){
        Jugador jugador2 = new Jugador("Jugador2");
        assertNotEquals(jugador.hashCode(), jugador2.hashCode());
    }

    @Test
    public void testHashCodeConsistencia(){
        assertEquals(jugador.hashCode(), jugador.hashCode());
    }

    @Test
    public void testToString(){
        String esperado = "Jugador{nombre='Jugador'}";
        assertEquals(esperado, jugador.toString());
    }

    @Test
    public void testToStringContieneNombre(){
        assertTrue(jugador.toString().contains("Jugador"));
    }

    @Test
    public void testGetNombre(){
        assertEquals("Jugador", jugador.getNombre());
    }
}
