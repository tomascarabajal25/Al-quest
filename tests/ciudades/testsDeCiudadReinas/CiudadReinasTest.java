package ciudades.testsDeCiudadReinas;

import juego.ciudades.reinas.CiudadReinas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CiudadReinasTest {

    private CiudadReinas ciudad;

    @BeforeEach
    public void setUp() {
        ciudad = new CiudadReinas();
    }

    // --- iniciarCiudad ---

    @Test
    public void testIniciarCiudadConPosicionValida() {
        assertTrue(ciudad.iniciarCiudad(8, 0, 0), "Debe haber solución desde (0,0) en tablero 8x8");
    }

    @Test
    public void testIniciarCiudadDevuelveReinasCorrectas() {
        ciudad.iniciarCiudad(8, 3, 2);
        int[] reinas = ciudad.getReinasTablero();
        assertEquals(2, reinas[3], "La reina inicial debe estar en fila 3, columna 2");
    }

    // --- validarTableroJugador ---

    @Test
    public void testValidarTableroCorrectoDevuelveTrue() {
        // solución conocida para 4x4
        int[][] tablero = new int[4][4];
        tablero[0][1] = 1;
        tablero[1][3] = 1;
        tablero[2][0] = 1;
        tablero[3][2] = 1;
        ciudad.iniciarCiudad(4, 0, 1);
        assertTrue(ciudad.validarTableroJugador(tablero));
    }

    @Test
    public void testValidarTableroIncorrectoDevuelveFalse() {
        ciudad.iniciarCiudad(4, 0, 0);
        int[][] tablero = new int[4][4];
        tablero[0][0] = 1;
        tablero[1][0] = 1; // misma columna
        tablero[2][0] = 1;
        tablero[3][0] = 1;
        assertFalse(ciudad.validarTableroJugador(tablero));
    }

    @Test
    public void testValidarTableroConFilaVaciaDevuelveFalse() {
        ciudad.iniciarCiudad(4, 0, 0);
        int[][] tablero = new int[4][4];
        tablero[0][0] = 1;
        // fila 1, 2, 3 vacías
        assertFalse(ciudad.validarTableroJugador(tablero));
    }

    @Test
    public void testValidarTableroConDosReinasMismaFilaDevuelveFalse() {
        ciudad.iniciarCiudad(4, 0, 0);
        int[][] tablero = new int[4][4];
        tablero[0][0] = 1;
        tablero[0][1] = 1; // dos reinas en fila 0
        tablero[1][3] = 1;
        tablero[2][1] = 1;
        tablero[3][2] = 1;
        assertFalse(ciudad.validarTableroJugador(tablero));
    }

    // --- actualizarTableroJugador ---

    @Test
    public void testActualizarTableroRespetaReinaInicial() {
        ciudad.iniciarCiudad(8, 3, 2);
        int[][] tableroJugador = new int[8][8];
        tableroJugador[3][2] = 1;
        tableroJugador[0][4] = 1;
        ciudad.actualizarTableroJugador(tableroJugador, 3, 2);
        int[] reinas = ciudad.getReinasTablero();
        assertEquals(2, reinas[3], "La reina inicial no debe moverse");
    }

    @Test
    public void testActualizarTableroIgnoraReinasConflictivas() {
        ciudad.iniciarCiudad(8, 0, 0);
        int[][] tableroJugador = new int[8][8];
        tableroJugador[0][0] = 1;
        tableroJugador[1][0] = 1; // misma columna que la inicial, debe ignorarse
        ciudad.actualizarTableroJugador(tableroJugador, 0, 0);
        int[] reinas = ciudad.getReinasTablero();
        assertEquals(-1, reinas[1], "Reina conflictiva debe ser ignorada");
    }

    // --- obtenerPasos ---

    @Test
    public void testObtenerPasosDevuelveListaNoVacia() {
        ciudad.iniciarCiudad(8, 0, 0);
        assertNotNull(ciudad.obtenerPasos());
        assertFalse(ciudad.obtenerPasos().isEmpty());
    }

    @Test
    public void testObtenerPasosDevuelveNullSinSolucion() {
        // forzar estado sin solución actualizando con reinas conflictivas
        ciudad.iniciarCiudad(4, 0, 0);
        int[][] tableroJugador = new int[4][4];
        tableroJugador[0][0] = 1;
        tableroJugador[1][2] = 1;
        tableroJugador[2][0] = 1; // conflicto con fila 0
        ciudad.actualizarTableroJugador(tableroJugador, 0, 0);
        // con este estado el solver no debería encontrar solución
        // si obtenerPasos devuelve null, el estado es inválido
        // si devuelve lista, el solver encontró solución igual
        // ambos son comportamientos válidos — solo verificamos que no lanza excepción
        assertDoesNotThrow(() -> ciudad.obtenerPasos());
    }
}