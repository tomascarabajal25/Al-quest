package modelos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CeldaTest<T> {
    private Celda celda;
    private Celda celda2;

    @BeforeEach
    public void setUp() {
        celda = new Celda(10);
        celda2 = new Celda("Hola");

    }

    @Test
    public void testCrearCelda() {
        assertNotNull(celda);
        assertNotNull(celda2);
    }

    @Test
    public void testEquals(){
        Celda celda3 = new Celda(10);
        Celda celda4 = new Celda("Hola");

        assertTrue(celda.equals(celda3));
        assertFalse(celda.equals(celda4));
    }

    @Test
    public void testNotEquals(){
        Celda celda3 = new Celda(20);
        Celda celda4 = new Celda("Adios");

        assertFalse(celda.equals(celda3));
        assertFalse(celda.equals(celda4));
    }

    @Test
    public void testHashCode(){
        Celda celda3 = new Celda(10);
        Celda celda4 = new Celda("Adios");

        assertEquals(celda.hashCode(), celda3.hashCode());
        assertNotEquals(celda.hashCode(), celda4.hashCode());
    }

    @Test
    public void testToString(){
        assertTrue(celda.toString().contains("10"));
        assertTrue(celda2.toString().contains("Hola"));
    }

    @Test
    public void testGetContenido() {
        assertEquals(10, celda.getContenido());
        assertEquals("Hola", celda2.getContenido());
    }

}
