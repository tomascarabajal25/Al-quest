package ciudades.testsDeCiudadReinas;

import juego.ciudades.reinas.Tablero;
import juego.ciudades.reinas.SolverReinas;
import juego.ciudades.reinas.Paso;
import juego.ciudades.reinas.Accion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class SolverReinasTest {

    private SolverReinas solver;
    private Tablero tablero;

    @BeforeEach
    public void setUp() {
        solver = new SolverReinas();
        tablero = new Tablero();
        tablero.setTamanio(8);
    }

    // --- resolver ---

    @Test
    public void testResolverTableroVacioEncuentraSolucion() {
        assertTrue(solver.resolver(tablero, 0), "Debe encontrar solución en tablero vacío");
    }

    @Test
    public void testResolverRespetaReinaPrevia() {
        tablero.colocarReina(0, 0);
        assertTrue(solver.resolver(tablero, 0));
        assertEquals(0, tablero.getReinas(0), "La reina inicial no debe moverse");
    }

    // --- obtenerSolucion ---

    @Test
    public void testObtenerSolucionNoModificaOriginal() {
        tablero.colocarReina(0, 3);
        int[] antes = tablero.getTodasLasReinas().clone();
        solver.obtenerSolucion(tablero);
        assertArrayEquals(antes, tablero.getTodasLasReinas(), "El tablero original no debe modificarse");
    }

    @Test
    public void testObtenerSolucionDevuelveArregloValido() {
        tablero.colocarReina(0, 0);
        int[] solucion = solver.obtenerSolucion(tablero);
        assertNotNull(solucion);
        assertEquals(8, solucion.length);
        // verificar que ninguna fila quede vacía
        for (int col : solucion) {
            assertNotEquals(-1, col, "Todas las filas deben tener reina en la solución");
        }
    }

    @Test
    public void testObtenerSolucionDevuelveNullSinSolucion() {
        // tablero 4x4 con posiciones que no tienen solución
        tablero.setTamanio(4);
        tablero.colocarReina(0, 0);
        tablero.colocarReina(1, 2);
        tablero.colocarReina(2, 0);
        assertNull(solver.obtenerSolucion(tablero));
    }

    // --- grabarPasos ---

    @Test
    public void testGrabarPasosNoEstaVacio() {
        tablero.colocarReina(0, 0);
        List<Paso> pasos = solver.grabarPasos(tablero);
        assertFalse(pasos.isEmpty(), "Debe haber al menos un paso");
    }

    @Test
    public void testGrabarPasosContieneColocarYQuitar() {
        tablero.colocarReina(0, 0);
        List<Paso> pasos = solver.grabarPasos(tablero);

        boolean tieneColocar = pasos.stream().anyMatch(p -> p.getAccion() == Accion.COLOCAR);
        boolean tieneQuitar = pasos.stream().anyMatch(p -> p.getAccion() == Accion.QUITAR);

        assertTrue(tieneColocar, "Debe haber pasos COLOCAR");
        assertTrue(tieneQuitar, "Debe haber pasos QUITAR por el backtracking");
    }

    @Test
    public void testGrabarPasosNoTocaFilaConReinaPrevia() {
        tablero.colocarReina(3, 2);
        List<Paso> pasos = solver.grabarPasos(tablero);

        boolean tocaFila3 = pasos.stream().anyMatch(p -> p.getFila() == 3);
        assertFalse(tocaFila3, "El solver no debe tocar la fila de la reina inicial");
    }

    @Test
    public void testUltimoPasoEsColocar() {
        tablero.colocarReina(0, 0);
        List<Paso> pasos = solver.grabarPasos(tablero);
        Paso ultimo = pasos.get(pasos.size() - 1);
        assertEquals(Accion.COLOCAR, ultimo.getAccion(), "El último paso debe ser COLOCAR");
    }
}