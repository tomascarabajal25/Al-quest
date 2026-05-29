package modelos;

import Juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelos.Elemento;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ElementoTest {

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

    @Test
    void constructorValido() {

        Elemento elemento = new ElementoMock("Carta");

        assertEquals("Carta", elemento.getNombre());
    }

    @Test
    void constructorNombreNull() {

        assertThrows(RuntimeException.class, () -> {
            new ElementoMock(null);
        });
    }

    @Test
    void equalsMismoObjeto() {

        Elemento elemento = new ElementoMock("Carta");

        assertEquals(elemento, elemento);
    }

    @Test
    void equalsObjetosIguales() {

        Elemento elemento1 = new ElementoMock("Carta");
        Elemento elemento2 = new ElementoMock("Carta");

        assertEquals(elemento1, elemento2);
    }

    @Test
    void equalsObjetosDistintos() {

        Elemento elemento1 = new ElementoMock("Carta1");
        Elemento elemento2 = new ElementoMock("Carta2");

        assertNotEquals(elemento1, elemento2);
    }

    @Test
    void equalsConNull() {

        Elemento elemento = new ElementoMock("Carta");

        assertNotEquals(null, elemento);
    }

    @Test
    void hashCodeIguales() {

        Elemento elemento1 = new ElementoMock("Carta");
        Elemento elemento2 = new ElementoMock("Carta");

        assertEquals(
                elemento1.hashCode(),
                elemento2.hashCode()
        );
    }

    @Test
    void toStringValido() {

        Elemento elemento = new ElementoMock("Carta");

        String esperado =
                "Elemento{nombre='Carta'}";

        assertEquals(esperado, elemento.toString());
    }
}