package ciudades.testsDeCiudadReinas;

import juego.ciudades.reinas.Tablero;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TableroTest {

    private Tablero tablero;

    @BeforeEach
    public void setUp() {
        tablero = new Tablero();
        tablero.setTamanio(8);
    }

    // --- setTamanio ---

    @Test
    public void testSetTamanioInicializaVacio() {
        for (int i = 0; i < tablero.getTamanio(); i++) {
            assertEquals(-1, tablero.getReinas(i), "Todas las filas deben arrancar vacías");
        }
    }

    @Test
    public void testSetTamanioInvalidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> tablero.setTamanio(0));
        assertThrows(IllegalArgumentException.class, () -> tablero.setTamanio(-1));
    }

    // --- colocarReina / quitarReina ---

    @Test
    public void testColocarReinaGuardaColumna() {
        tablero.colocarReina(0, 3);
        assertEquals(3, tablero.getReinas(0));
    }

    @Test
    public void testQuitarReinaDejaFilaVacia() {
        tablero.colocarReina(0, 3);
        tablero.quitarReina(0);
        assertEquals(-1, tablero.getReinas(0));
    }

    // --- esValido(fila, columna) ---

    @Test
    public void testEsValidoRechazaMismaColumna() {
        tablero.colocarReina(0, 3);
        assertFalse(tablero.esValido(1, 3), "Misma columna debe ser inválida");
    }

    @Test
    public void testEsValidoRechazaDiagonal() {
        tablero.colocarReina(0, 0);
        assertFalse(tablero.esValido(1, 1), "Diagonal debe ser inválida");
        assertFalse(tablero.esValido(2, 2), "Diagonal extendida debe ser inválida");
    }

    @Test
    public void testEsValidoAceptaPosicionLibre() {
        tablero.colocarReina(0, 0);
        assertTrue(tablero.esValido(1, 2), "Posición libre debe ser válida");
    }

    @Test
    public void testEsValidoFueraDeRangoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> tablero.esValido(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> tablero.esValido(0, 8));
    }

    // --- copiar ---

    @Test
    public void testCopiarPreservaReinas() {
        tablero.colocarReina(0, 0);
        tablero.colocarReina(1, 4);
        Tablero copia = tablero.copiar();

        assertEquals(0, copia.getReinas(0));
        assertEquals(4, copia.getReinas(1));
        assertEquals(-1, copia.getReinas(2));
    }

    @Test
    public void testCopiarEsIndependiente() {
        tablero.colocarReina(0, 2);
        Tablero copia = tablero.copiar();

        copia.colocarReina(1, 4);
        assertEquals(-1, tablero.getReinas(1), "Modificar la copia no debe afectar el original");
    }

    // --- getTodasLasReinas ---

    @Test
    public void testGetTodasLasReinasDevuelveCopia() {
        tablero.colocarReina(0, 1);
        int[] reinas = tablero.getTodasLasReinas();
        reinas[0] = 99;
        assertEquals(1, tablero.getReinas(0), "Modificar el arreglo devuelto no debe afectar el tablero");
    }
}
