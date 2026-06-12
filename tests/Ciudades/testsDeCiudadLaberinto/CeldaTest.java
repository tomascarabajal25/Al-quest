package ciudades.testsDeCiudadLaberinto;

import juego.ciudades.ciudad_3_laberinto.src.Celda;
import juego.ciudades.ciudad_3_laberinto.src.EstadoCelda;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;


/**
 * Tests unitarios para la clase Celda.
 */
public class CeldaTest {

    private Celda celdaLibre;
    private Celda celdaPared;
    private Celda celdaInicio;

    /**
     * Inicializa las celdas de prueba antes de cada test.
     */
    @Before
    public void setUp() {
        celdaLibre  = new Celda(1, 1, EstadoCelda.LIBRE);
        celdaPared  = new Celda(0, 0, EstadoCelda.PARED);
        celdaInicio = new Celda(2, 3, EstadoCelda.INICIO);
    }

    /** Verifica que la posicion de la celda es correcta */
    @Test
    public void testPosicion() {
        assertEquals(1, celdaLibre.getFila());
        assertEquals(1, celdaLibre.getColumna());
    }

    /** Verifica que esPared retorna true solo para celdas PARED */
    @Test
    public void testEsPared() {
        assertTrue(celdaPared.esPared());
        assertFalse(celdaLibre.esPared());
        assertFalse(celdaInicio.esPared());
    }

    /** Verifica que esTransitable retorna true para LIBRE, INICIO y FIN */
    @Test
    public void testEsTransitable() {
        assertTrue(celdaLibre.esTransitable());
        assertTrue(celdaInicio.esTransitable());
        assertTrue(new Celda(0, 0, EstadoCelda.FIN).esTransitable());
        assertFalse(celdaPared.esTransitable());
        assertFalse(new Celda(0, 0, EstadoCelda.EN_CAMINO).esTransitable());
        assertFalse(new Celda(0, 0, EstadoCelda.DESCARTADA).esTransitable());
    }

    /** Verifica que setEstadoCelda cambia el estado correctamente */
    @Test
    public void testCambiarEstado() {
        celdaLibre.setEstadoCelda(EstadoCelda.EN_CAMINO);
        assertEquals(EstadoCelda.EN_CAMINO, celdaLibre.getEstadoCelda());
    }
}