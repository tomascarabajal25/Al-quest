package modelos;

import juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMochila {

    /**
     * Implementación mínima de Elemento
     * para testing.
     */
    private static class ElementoMock extends Elemento {

        public ElementoMock(String nombre) {
            super(nombre);
        }

        @Override
        public void aplicarEfecto(CiudadRecoleccion juego) {
            // Vacío para testing
        }
    }

    @Test
    public void testConstructorValido() {

        Mochila mochila = new Mochila(3);

        assertEquals(0, mochila.getCantidadElementos());
        assertEquals(3, mochila.getCantidadMaxima());
    }

    @Test
    public void testConstructorInvalido() {

        assertThrows(RuntimeException.class, () -> {
            new Mochila(0);
        });

        assertThrows(RuntimeException.class, () -> {
            new Mochila(-1);
        });
    }

    @Test
    public void testAgregarElemento() {

        Mochila mochila = new Mochila(3);

        Elemento espada =
                new ElementoMock("Espada");

        mochila.agregarElemento(espada);

        assertEquals(1,
                mochila.getCantidadElementos());

        assertEquals(
                espada,
                mochila.getElementoPorNombre("Espada")
        );
    }

    @Test
    public void testAgregarElementoNull() {

        Mochila mochila = new Mochila(3);

        assertThrows(RuntimeException.class, () -> {
            mochila.agregarElemento(null);
        });
    }

    @Test
    public void testAgregarElementoMochilaLlena() {

        Mochila mochila = new Mochila(2);

        mochila.agregarElemento(
                new ElementoMock("Espada"));

        mochila.agregarElemento(
                new ElementoMock("Arco"));

        assertThrows(RuntimeException.class, () -> {

            mochila.agregarElemento(
                    new ElementoMock("Pocion"));
        });
    }

    @Test
    public void testEliminarElemento() {

        Mochila mochila = new Mochila(3);

        Elemento espada =
                new ElementoMock("Espada");

        mochila.agregarElemento(espada);

        mochila.eliminarElemento(espada);

        assertEquals(
                0,
                mochila.getCantidadElementos()
        );

        assertNull(
                mochila.getElementoPorNombre("Espada")
        );
    }

    @Test
    public void testEliminarElementoNull() {

        Mochila mochila = new Mochila(3);

        assertThrows(RuntimeException.class, () -> {
            mochila.eliminarElemento(null);
        });
    }

    @Test
    public void testEliminarElementoInexistente() {

        Mochila mochila = new Mochila(3);

        Elemento espada =
                new ElementoMock("Espada");

        assertThrows(RuntimeException.class, () -> {
            mochila.eliminarElemento(espada);
        });
    }

    @Test
    public void testVaciarMochila() {

        Mochila mochila = new Mochila(5);

        mochila.agregarElemento(
                new ElementoMock("Espada"));

        mochila.agregarElemento(
                new ElementoMock("Arco"));

        mochila.agregarElemento(
                new ElementoMock("Pocion"));

        mochila.vaciarMochila();

        assertEquals(
                0,
                mochila.getCantidadElementos()
        );
    }

    @Test
    public void testGetElementos() {

        Mochila mochila = new Mochila(3);

        Elemento espada = new ElementoMock("Espada");
        Elemento arco   = new ElementoMock("Arco");

        mochila.agregarElemento(espada);
        mochila.agregarElemento(arco);

        assertNotNull(mochila.getElementos());
        assertEquals(2, mochila.getElementos().size());
    }

    @Test
    public void testGetElementoPorNombreExistente() {

        Mochila mochila = new Mochila(3);

        Elemento espada =
                new ElementoMock("Espada");

        mochila.agregarElemento(espada);

        Elemento encontrado =
                mochila.getElementoPorNombre("Espada");

        assertEquals(espada, encontrado);
    }

    @Test
    public void testGetElementoPorNombreInexistente() {

        Mochila mochila = new Mochila(3);

        mochila.agregarElemento(
                new ElementoMock("Espada"));

        assertNull(
                mochila.getElementoPorNombre("Arco")
        );
    }

    @Test
    public void testGetElementoPorPosicionValido() {

        Mochila mochila = new Mochila(3);

        Elemento espada = new ElementoMock("Espada");
        Elemento arco   = new ElementoMock("Arco");

        mochila.agregarElemento(espada);
        mochila.agregarElemento(arco);

        assertEquals(espada, mochila.getElementoPorPosicion(1));
        assertEquals(arco,   mochila.getElementoPorPosicion(2));
    }

    @Test
    public void testGetElementoPorPosicionInvalido() {

        Mochila mochila = new Mochila(3);

        assertThrows(RuntimeException.class, () -> {
            mochila.getElementoPorPosicion(0);
        });

        assertThrows(RuntimeException.class, () -> {
            mochila.getElementoPorPosicion(-1);
        });
    }

    @Test
    public void testEqualsMismaInstancia() {

        Mochila mochila = new Mochila(3);

        assertEquals(mochila, mochila);
    }

    @Test
    public void testEquals() {

        Mochila mochila1 = new Mochila(3);
        Mochila mochila2 = new Mochila(3);

        Elemento espada =
                new ElementoMock("Espada");

        mochila1.agregarElemento(espada);
        mochila2.agregarElemento(espada);

        assertEquals(mochila1, mochila2);
    }

    @Test
    public void testNotEquals() {

        Mochila mochila1 = new Mochila(3);
        Mochila mochila2 = new Mochila(3);

        mochila1.agregarElemento(
                new ElementoMock("Espada"));

        mochila2.agregarElemento(
                new ElementoMock("Arco"));

        assertNotEquals(mochila1, mochila2);
    }

    @Test
    public void testEqualsConNull() {

        Mochila mochila = new Mochila(3);

        assertNotEquals(null, mochila);
    }

    @Test
    public void testEqualsConOtroTipo() {

        Mochila mochila = new Mochila(3);

        assertFalse(mochila.equals("Mochila"));
        assertFalse(mochila.equals(42));
    }

    @Test
    public void testHashCode() {

        Mochila mochila1 = new Mochila(3);
        Mochila mochila2 = new Mochila(3);

        Elemento espada =
                new ElementoMock("Espada");

        mochila1.agregarElemento(espada);
        mochila2.agregarElemento(espada);

        assertEquals(
                mochila1.hashCode(),
                mochila2.hashCode()
        );
    }

    @Test
    public void testHashCodeDistintos() {

        Mochila mochila1 = new Mochila(3);
        Mochila mochila2 = new Mochila(3);

        mochila1.agregarElemento(
                new ElementoMock("Espada"));

        mochila2.agregarElemento(
                new ElementoMock("Arco"));

        assertNotEquals(
                mochila1.hashCode(),
                mochila2.hashCode()
        );
    }

    @Test
    public void testHashCodeConsistencia() {

        Mochila mochila = new Mochila(3);

        assertEquals(
                mochila.hashCode(),
                mochila.hashCode()
        );
    }

    @Test
    public void testToString() {

        Mochila mochila = new Mochila(3);

        mochila.agregarElemento(
                new ElementoMock("Espada"));

        assertNotNull(mochila.toString());

        assertTrue(
                mochila.toString().contains("Mochila")
        );

        assertTrue(
                mochila.toString().contains("elementos")
        );
    }
}
