package modelos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MapaTest {

    @Test
    void constructorMapaCorrecto() {
        Mapa mapa = new Mapa(3, 4);

        assertEquals(3, mapa.getAncho());
        assertEquals(4, mapa.getAlto());
        assertEquals(12, mapa.getCantidadCeldas());
    }

    @Test
    void constructorInicializaCeldasEnNull() {
        Mapa mapa = new Mapa(2, 2);

        for (int i = 0; i < mapa.getAncho(); i++) {
            for (int j = 0; j < mapa.getAlto(); j++) {
                assertNotNull(mapa.getCeldaConPosicion(i, j));
                assertNull(mapa.getCeldaConPosicion(i, j).getContenido());
            }
        }
    }

    @Test
    void constructorAnchoNegativo() {
        assertThrows(RuntimeException.class, () -> {new Mapa(-1, 2);});
    }

    @Test
    void constructorAltoNegativo() {
        assertThrows(RuntimeException.class, () -> {new Mapa(2, -1);});
    }

    @Test
    void ocuparCeldaCorrectamente() {
        Mapa mapa = new Mapa(3, 3);
        mapa.ocuparCelda("Hola", 1, 1);

        assertEquals("Hola", mapa.getCeldaConPosicion(1, 1).getContenido());
    }

    @Test
    void ocuparCeldaConNull() {
        Mapa mapa = new Mapa(3, 3);
        assertThrows(RuntimeException.class, () -> {mapa.ocuparCelda(null, 1, 1);});
    }

    @Test
    void ocuparCeldaFueraDeRango() {
        Mapa mapa = new Mapa(3, 3);
        assertThrows(RuntimeException.class, () -> {mapa.ocuparCelda("Hola", 10, 10);});
    }

    @Test
    void vaciarCeldaCorrectamente() {
        Mapa mapa = new Mapa(3, 3);
        mapa.ocuparCelda("Hola", 1, 1);
        mapa.vaciarCelda(1, 1);

        assertNull(mapa.getCeldaConPosicion(1, 1).getContenido());
    }

    @Test
    void vaciarCeldaFueraDeRango() {
        Mapa mapa = new Mapa(3, 3);
        assertThrows(RuntimeException.class, () -> {mapa.vaciarCelda(10, 10);});
    }

    @Test
    void getCeldaConPosicionCorrecta() {
        Mapa mapa = new Mapa(3, 3);
        mapa.ocuparCelda("Dato", 2, 1);

        assertEquals("Dato", mapa.getCeldaConPosicion(2, 1).getContenido());
    }

    @Test
    void getCeldaConPosicionFueraDeRango() {
        Mapa mapa = new Mapa(3, 3);
        assertThrows(RuntimeException.class, () -> {mapa.getCeldaConPosicion(5, 5);});
    }

    @Test
    void getCeldaConContenidoExistente() {
        Mapa mapa = new Mapa(3, 3);
        mapa.ocuparCelda("Hola", 1, 2);

        Celda celda = mapa.getCeldaConContenido("Hola");

        assertNotNull(celda);
        assertEquals("Hola", celda.getContenido());
    }

    @Test
    void getCeldaConContenidoInexistente() {
        Mapa mapa = new Mapa(3, 3);
        Celda celda = mapa.getCeldaConContenido("NoExiste");

        assertNull(celda);
    }

    @Test
    void getCeldaConContenidoNull() {
        Mapa mapa = new Mapa(3, 3);
        assertThrows(RuntimeException.class, () -> {mapa.getCeldaConContenido(null);});
    }

    @Test
    void validarFueraDeRangoCorrecto() {
        Mapa mapa = new Mapa(3, 3);
        assertDoesNotThrow(() -> {mapa.validarFueraDeRango(1, 1);});
    }

    @Test
    void validarFueraDeRangoIncorrecto() {
        Mapa mapa = new Mapa(3, 3);
        assertThrows(RuntimeException.class, () -> {mapa.validarFueraDeRango(100, 100);});
    }

    @Test
    void equalsMapasIguales() {
        Mapa mapa1 = new Mapa(2, 2);
        Mapa mapa2 = new Mapa(2, 2);

        mapa1.ocuparCelda("A", 0, 0);
        mapa2.ocuparCelda("A", 0, 0);

        assertEquals(mapa1, mapa2);
    }

    @Test
    void equalsMapasDistintos() {
        Mapa mapa1 = new Mapa(2, 2);
        Mapa mapa2 = new Mapa(2, 2);

        mapa1.ocuparCelda("A", 0, 0);
        mapa2.ocuparCelda("B", 0, 0);

        assertNotEquals(mapa1, mapa2);
    }

    @Test
    void equalsConNull() {
        Mapa mapa = new Mapa(2, 2);
        assertNotEquals(null, mapa);
    }

    @Test
    void equalsConOtroTipo() {
        Mapa mapa = new Mapa(2, 2);
        assertNotEquals("Hola", mapa);
    }

    @Test
    void hashCodeMapasIguales() {
        Mapa mapa1 = new Mapa(2, 2);
        Mapa mapa2 = new Mapa(2, 2);

        mapa1.ocuparCelda("A", 0, 0);
        mapa2.ocuparCelda("A", 0, 0);

        assertEquals(mapa1.hashCode(), mapa2.hashCode());
    }

    @Test
    void getVecinosCentro() {
        Mapa mapa = new Mapa(5, 5);
        Celda<?>[][] vecinos = mapa.getCeldasVecinasRespectoPosicion(2, 2, 1);

        assertEquals(3, vecinos.length);
        assertEquals(3, vecinos[0].length);
    }

    @Test
    void getVecinosBorde() {
        Mapa mapa = new Mapa(5, 5);
        Celda<?>[][] vecinos = mapa.getCeldasVecinasRespectoPosicion(0, 0, 1);

        assertEquals(3, vecinos.length);
        assertEquals(3, vecinos[0].length);
    }

    @Test
    void getVecinosCantInvalida() {
        Mapa mapa = new Mapa(5, 5);
        assertThrows(RuntimeException.class, () -> {
            mapa.getCeldasVecinasRespectoPosicion(1, 1, 0);
        });
    }
}
