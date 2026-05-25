package tests.modelos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelos.Elemento;

import static org.junit.jupiter.api.Assertions.*;

public class ElementoTest {
    private Elemento elemento;

    @BeforeEach
    public void setUp(){
        elemento = new Elemento("Elemento");
    }

    @Test
    public void testCrearJugador(){
        assertNotNull(elemento);
    }

    @Test
    public void testEquals(){
        Elemento jugador2 = new Elemento("Jugador");
        assertTrue(elemento.equals(jugador2));
    }

    @Test
    public void testNotEquals(){
        Elemento jugador2 = new Elemento("Elemento2");
        assertFalse(elemento.equals(jugador2));
    }

    @Test
    public void testHashCode(){
        Elemento jugador2 = new Elemento("Elemento");
        assertEquals(elemento.hashCode(), jugador2.hashCode());
    }

    @Test
    public void testToString(){
        assertTrue(elemento.toString().contains("Elemento"));
    }

    @Test
    public void testGetNombre(){
        assertEquals("Elemento", elemento.getNombre());
    }
}
