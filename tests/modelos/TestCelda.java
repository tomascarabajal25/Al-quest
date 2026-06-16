package modelos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCelda {

    private Celda<Integer> celda;
    private Celda<String> celda2;

    @BeforeEach
    public void setUp() {
        celda  = new Celda<>(10);
        celda2 = new Celda<>("Hola");
    }

    @Test
    public void testCrearCelda() {
        assertNotNull(celda);
        assertNotNull(celda2);
    }

    @Test
    public void testEquals() {
        Celda<Integer> celda3 = new Celda<>(10);
        Celda<String>  celda4 = new Celda<>("Hola");

        assertTrue(celda.equals(celda3));
        assertFalse(celda.equals(celda4));
    }

    @Test
    public void testNotEquals() {
        Celda<Integer> celda3 = new Celda<>(20);
        Celda<String>  celda4 = new Celda<>("Adios");

        assertFalse(celda.equals(celda3));
        assertFalse(celda.equals(celda4));
    }

    @Test
    public void testEqualsConNull() {
        // El contrato de equals exige retornar false ante null
        assertFalse(celda.equals(null));
    }

    @Test
    public void testEqualsConOtroTipo() {
        // Comparar con un objeto de clase distinta debe retornar false
        assertFalse(celda.equals("no soy una Celda"));
        assertFalse(celda.equals(10));
    }

    @Test
    public void testEqualsMismaInstancia() {
        // Reflexividad: x.equals(x) siempre debe ser true
        assertTrue(celda.equals(celda));
    }

    @Test
    public void testHashCode() {
        Celda<Integer> celda3 = new Celda<>(10);
        Celda<String>  celda4 = new Celda<>("Adios");

        assertEquals(celda.hashCode(), celda3.hashCode());
        assertNotEquals(celda.hashCode(), celda4.hashCode());
    }

    @Test
    public void testHashCodeConsistencia() {
        // El mismo objeto debe retornar siempre el mismo hashCode
        int hash1 = celda.hashCode();
        int hash2 = celda.hashCode();
        assertEquals(hash1, hash2);
    }

    @Test
    public void testToString() {
        assertTrue(celda.toString().contains("10"));
        assertTrue(celda2.toString().contains("Hola"));
    }

    @Test
    public void testGetContenido() {
        assertEquals(10,     celda.getContenido());
        assertEquals("Hola", celda2.getContenido());
    }
}
