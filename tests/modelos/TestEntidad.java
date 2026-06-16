package modelos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestEntidad {

    /**
     * Implementación mínima de Entidad
     * utilizada únicamente para testing.
     */
    private static class EntidadMock extends Entidad {

        public EntidadMock(String nombre) {
            super(nombre);
        }
    }

    /**
     * Segunda implementación mínima de Entidad para testear
     * que equals retorna false entre subclases distintas.
     */
    private static class EntidadMock2 extends Entidad {

        public EntidadMock2(String nombre) {
            super(nombre);
        }
    }

    @Test
    void constructorValido() {

        Entidad entidad = new EntidadMock("Jugador");

        assertEquals("Jugador", entidad.getNombre());
    }

    @Test
    void constructorNombreNull() {

        assertThrows(RuntimeException.class, () -> {
            new EntidadMock(null);
        });
    }

    @Test
    void equalsMismoObjeto() {

        Entidad entidad = new EntidadMock("Jugador");

        assertEquals(entidad, entidad);
    }

    @Test
    void equalsObjetosIguales() {

        Entidad entidad1 = new EntidadMock("Jugador");
        Entidad entidad2 = new EntidadMock("Jugador");

        assertEquals(entidad1, entidad2);
    }

    @Test
    void equalsObjetosDistintos() {

        Entidad entidad1 = new EntidadMock("Jugador1");
        Entidad entidad2 = new EntidadMock("Jugador2");

        assertNotEquals(entidad1, entidad2);
    }

    @Test
    void equalsConNull() {

        Entidad entidad = new EntidadMock("Jugador");

        assertNotEquals(null, entidad);
    }

    @Test
    void equalsConOtroTipo() {

        Entidad entidad = new EntidadMock("Jugador");

        assertFalse(entidad.equals("Jugador"));
        assertFalse(entidad.equals(42));
    }

    @Test
    void equalsEntreSubclasesDistintas() {

        Entidad mock1 = new EntidadMock("Jugador");
        Entidad mock2 = new EntidadMock2("Jugador");

        assertNotEquals(mock1, mock2);
    }

    @Test
    void hashCodeIguales() {

        Entidad entidad1 = new EntidadMock("Jugador");
        Entidad entidad2 = new EntidadMock("Jugador");

        assertEquals(
                entidad1.hashCode(),
                entidad2.hashCode()
        );
    }

    @Test
    void hashCodeDistintos() {

        Entidad entidad1 = new EntidadMock("Jugador1");
        Entidad entidad2 = new EntidadMock("Jugador2");

        assertNotEquals(
                entidad1.hashCode(),
                entidad2.hashCode()
        );
    }

    @Test
    void hashCodeConsistencia() {

        Entidad entidad = new EntidadMock("Jugador");

        assertEquals(entidad.hashCode(), entidad.hashCode());
    }

    @Test
    void toStringValido() {

        Entidad entidad = new EntidadMock("Jugador");

        String esperado = "Entidad [nombre=Jugador]";

        assertEquals(esperado, entidad.toString());
    }

    @Test
    void toStringContieneNombre() {

        Entidad entidad = new EntidadMock("Jugador");

        assertTrue(entidad.toString().contains("Jugador"));
    }

    @Test
    void getNombreValido() {

        Entidad entidad = new EntidadMock("Jugador");

        assertEquals("Jugador", entidad.getNombre());
    }
}
