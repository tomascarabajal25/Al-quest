package ciudades.testsDeCiudadLaberinto;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.List;
import juego.ciudades.ciudad_3_laberinto.src.*;

/**
 * Tests unitarios para la clase Laberinto.
 */
public class LaberintoTest {

    private Laberinto laberinto;

    /**
     * Construye un laberinto simple de 3x3 para pruebas:
     * # # #
     * I . F
     * # # #
     */
    @Before
    public void setUp() {
        Celda[][] grilla = new Celda[3][3];
        grilla[0][0] = new Celda(0, 0, EstadoCelda.PARED);
        grilla[0][1] = new Celda(0, 1, EstadoCelda.PARED);
        grilla[0][2] = new Celda(0, 2, EstadoCelda.PARED);
        grilla[1][0] = new Celda(1, 0, EstadoCelda.INICIO);
        grilla[1][1] = new Celda(1, 1, EstadoCelda.LIBRE);
        grilla[1][2] = new Celda(1, 2, EstadoCelda.FIN);
        grilla[2][0] = new Celda(2, 0, EstadoCelda.PARED);
        grilla[2][1] = new Celda(2, 1, EstadoCelda.PARED);
        grilla[2][2] = new Celda(2, 2, EstadoCelda.PARED);

        laberinto = new Laberinto(grilla, 3, 3, grilla[1][0], grilla[1][2]);
    }

    /** Verifica que las dimensiones del laberinto son correctas */
    @Test
    public void testDimensiones() {
        assertEquals(3, laberinto.getFilas());
        assertEquals(3, laberinto.getColumnas());
    }

    /** Verifica que posicionValida funciona en los bordes */
    @Test
    public void testPosicionValida() {
        assertTrue(laberinto.posicionValida(0, 0));
        assertTrue(laberinto.posicionValida(2, 2));
        assertFalse(laberinto.posicionValida(-1, 0));
        assertFalse(laberinto.posicionValida(0, -1));
        assertFalse(laberinto.posicionValida(3, 0));
        assertFalse(laberinto.posicionValida(0, 3));
    }

    /** Verifica que getCeldaInicio y getCeldaFin retornan las celdas correctas */
    @Test
    public void testInicioYFin() {
        assertEquals(EstadoCelda.INICIO, laberinto.getCeldaInicio().getEstadoCelda());
        assertEquals(EstadoCelda.FIN, laberinto.getCeldaFin().getEstadoCelda());
    }

    /** Verifica que obtenerCeldasTransitables retorna solo las celdas correctas */
    @Test
    public void testVecinosTransitables() {
        Celda inicio = laberinto.getCeldaInicio();
        List<Celda> vecinos = laberinto.obtenerCeldasTransitables(inicio);
        assertEquals(1, vecinos.size());
        assertEquals(EstadoCelda.LIBRE, vecinos.get(0).getEstadoCelda());
    }

    /** Verifica que reiniciarLaberinto vuelve las celdas a su estado original */
    @Test
    public void testReiniciar() {
        laberinto.getCelda(1, 1).setEstadoCelda(EstadoCelda.EN_CAMINO);
        laberinto.reiniciarLaberinto();
        assertEquals(EstadoCelda.LIBRE, laberinto.getCelda(1, 1).getEstadoCelda());
        assertEquals(EstadoCelda.INICIO, laberinto.getCeldaInicio().getEstadoCelda());
        assertEquals(EstadoCelda.FIN, laberinto.getCeldaFin().getEstadoCelda());
    }
}
