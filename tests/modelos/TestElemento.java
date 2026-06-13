package modelos;

import juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestElemento {

    /**
     * Implementación mínima de Elemento
     * utilizada únicamente para testing.
     */
    private static class ElementoMock extends Elemento {

        public ElementoMock(String nombre) {
            super(nombre);
        }

        @Override
        public void aplicarEfecto(CiudadRecoleccion juego) {
            // No hace nada para el test
        }
    }

    /**
     * Segunda implementación mínima de Elemento para testear
     * que equals retorna false entre subclases distintas.
     */
    private static class ElementoMock2 extends Elemento {

        public ElementoMock2(String nombre) {
            super(nombre);
        }

        @Override
        public void aplicarEfecto(CiudadRecoleccion juego) {
            // No hace nada para el test
        }
    }

    private Elemento elemento;

    @BeforeEach
    void setUp() {
        elemento = new ElementoMock("Carta");
    }

    // ── Construcción ────────────────────────────────────────────────────────────

    @Test
    void constructorValido() {
        assertEquals("Carta", elemento.getNombre());
    }

    @Test
    void constructorNombreNull() {
        assertThrows(RuntimeException.class, () -> new ElementoMock(null));
    }

    // ── equals ──────────────────────────────────────────────────────────────────

    @Test
    void equalsMismoObjeto() {
        assertEquals(elemento, elemento);
    }

    @Test
    void equalsObjetosIguales() {
        Elemento elemento2 = new ElementoMock("Carta");
        assertEquals(elemento, elemento2);
    }

    @Test
    void equalsObjetosDistintos() {
        Elemento elemento2 = new ElementoMock("Carta2");
        assertNotEquals(elemento, elemento2);
    }

    @Test
    void equalsConNull() {
        assertNotEquals(null, elemento);
    }

    @Test
    void equalsConOtroTipo() {
        // Comparar con un objeto de clase completamente distinta
        assertFalse(elemento.equals("Carta"));
        assertFalse(elemento.equals(42));
    }

    @Test
    void equalsEntreSubclasesDistintas() {
        // Dos subclases distintas con el mismo nombre no deben ser iguales
        // porque getClass() difiere
        Elemento mock2 = new ElementoMock2("Carta");
        assertNotEquals(elemento, mock2);
    }

    // ── hashCode ────────────────────────────────────────────────────────────────

    @Test
    void hashCodeIguales() {
        Elemento elemento2 = new ElementoMock("Carta");
        assertEquals(elemento.hashCode(), elemento2.hashCode());
    }

    @Test
    void hashCodeDistintos() {
        Elemento elemento2 = new ElementoMock("Otra");
        assertNotEquals(elemento.hashCode(), elemento2.hashCode());
    }

    @Test
    void hashCodeConsistencia() {
        // El mismo objeto debe retornar siempre el mismo hashCode
        assertEquals(elemento.hashCode(), elemento.hashCode());
    }

    // ── toString ────────────────────────────────────────────────────────────────

    @Test
    void toStringValido() {
        assertEquals("Elemento{nombre='Carta'}", elemento.toString());
    }

    @Test
    void toStringContieneNombre() {
        // Verificación más flexible: el nombre debe aparecer en el string
        assertTrue(elemento.toString().contains("Carta"));
    }

    // ── getNombre ───────────────────────────────────────────────────────────────

    @Test
    void getNombreValido() {
        assertEquals("Carta", elemento.getNombre());
    }
}
