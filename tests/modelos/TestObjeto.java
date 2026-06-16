package modelos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestObjeto {

    @Test
    void constructorValido() {

        Objeto objeto = new Objeto("Pared", true);

        assertEquals("Pared", objeto.getNombre());
        assertTrue(objeto.getColision());
    }

    @Test
    void constructorSinColision() {

        Objeto objeto = new Objeto("Suelo", false);

        assertEquals("Suelo", objeto.getNombre());
        assertFalse(objeto.getColision());
    }

    @Test
    void constructorNombreNull() {

        assertThrows(RuntimeException.class, () -> {
            new Objeto(null, true);
        });
    }

    @Test
    void equalsMismoObjeto() {

        Objeto objeto = new Objeto("Pared", true);

        assertEquals(objeto, objeto);
    }

    @Test
    void equalsObjetosIguales() {

        Objeto objeto1 = new Objeto("Pared", true);
        Objeto objeto2 = new Objeto("Pared", false);

        assertEquals(objeto1, objeto2);
    }

    @Test
    void equalsObjetosDistintos() {

        Objeto objeto1 = new Objeto("Pared", true);
        Objeto objeto2 = new Objeto("Suelo", true);

        assertNotEquals(objeto1, objeto2);
    }

    @Test
    void equalsConNull() {

        Objeto objeto = new Objeto("Pared", true);

        assertNotEquals(null, objeto);
    }

    @Test
    void equalsConOtroTipo() {

        Objeto objeto = new Objeto("Pared", true);

        assertFalse(objeto.equals("Pared"));
        assertFalse(objeto.equals(42));
    }

    @Test
    void hashCodeIguales() {

        Objeto objeto1 = new Objeto("Pared", true);
        Objeto objeto2 = new Objeto("Pared", false);

        assertEquals(
                objeto1.hashCode(),
                objeto2.hashCode()
        );
    }

    @Test
    void hashCodeDistintos() {

        Objeto objeto1 = new Objeto("Pared", true);
        Objeto objeto2 = new Objeto("Suelo", true);

        assertNotEquals(
                objeto1.hashCode(),
                objeto2.hashCode()
        );
    }

    @Test
    void hashCodeConsistencia() {

        Objeto objeto = new Objeto("Pared", true);

        assertEquals(objeto.hashCode(), objeto.hashCode());
    }

    @Test
    void toStringContieneNombre() {

        Objeto objeto = new Objeto("Pared", true);

        assertTrue(objeto.toString().contains("Pared"));
    }

    @Test
    void getNombreValido() {

        Objeto objeto = new Objeto("Pared", true);

        assertEquals("Pared", objeto.getNombre());
    }

    @Test
    void getColisionVerdadero() {

        Objeto objeto = new Objeto("Pared", true);

        assertTrue(objeto.getColision());
    }

    @Test
    void getColisionFalso() {

        Objeto objeto = new Objeto("Suelo", false);

        assertFalse(objeto.getColision());
    }
}
