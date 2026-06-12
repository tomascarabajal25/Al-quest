package ciudades.testsDeCiudadLaberinto;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import juego.ciudades.ciudad_3_laberinto.src.*;

/**
 * Tests unitarios para el algoritmo BacktrackingLaberinto.
 */
public class BacktrackingLaberintoTest {

    private Laberinto laberintoConSolucion;
    private Laberinto laberintoSinSolucion;

    /**
     * Laberinto con solucion:     Laberinto sin solucion:
     * # # #                       # # #
     * I . F                       I # F
     * # # #                       # # #
     */
    @Before
    public void setUp() {
        // Con solucion
        Celda[][] conSolucion = new Celda[3][3];
        conSolucion[0][0] = new Celda(0, 0, EstadoCelda.PARED);
        conSolucion[0][1] = new Celda(0, 1, EstadoCelda.PARED);
        conSolucion[0][2] = new Celda(0, 2, EstadoCelda.PARED);
        conSolucion[1][0] = new Celda(1, 0, EstadoCelda.INICIO);
        conSolucion[1][1] = new Celda(1, 1, EstadoCelda.LIBRE);
        conSolucion[1][2] = new Celda(1, 2, EstadoCelda.FIN);
        conSolucion[2][0] = new Celda(2, 0, EstadoCelda.PARED);
        conSolucion[2][1] = new Celda(2, 1, EstadoCelda.PARED);
        conSolucion[2][2] = new Celda(2, 2, EstadoCelda.PARED);
        laberintoConSolucion = new Laberinto(
            conSolucion, 3, 3, conSolucion[1][0], conSolucion[1][2]
        );

        // Sin solucion
        Celda[][] sinSolucion = new Celda[3][3];
        sinSolucion[0][0] = new Celda(0, 0, EstadoCelda.PARED);
        sinSolucion[0][1] = new Celda(0, 1, EstadoCelda.PARED);
        sinSolucion[0][2] = new Celda(0, 2, EstadoCelda.PARED);
        sinSolucion[1][0] = new Celda(1, 0, EstadoCelda.INICIO);
        sinSolucion[1][1] = new Celda(1, 1, EstadoCelda.PARED);
        sinSolucion[1][2] = new Celda(1, 2, EstadoCelda.FIN);
        sinSolucion[2][0] = new Celda(2, 0, EstadoCelda.PARED);
        sinSolucion[2][1] = new Celda(2, 1, EstadoCelda.PARED);
        sinSolucion[2][2] = new Celda(2, 2, EstadoCelda.PARED);
        laberintoSinSolucion = new Laberinto(
            sinSolucion, 3, 3, sinSolucion[1][0], sinSolucion[1][2]
        );
    }

    /** Verifica que el algoritmo encuentra la solucion en un laberinto simple */
    @Test
    public void testEncuentraSolucion() {
        BacktrackingLaberinto bt = new BacktrackingLaberinto(laberintoConSolucion);
        ResultadoPaso resultado = ResultadoPaso.EN_PROGRESO;

        while (resultado == ResultadoPaso.EN_PROGRESO) {
            resultado = bt.avanzarPaso();
        }

        assertEquals(ResultadoPaso.SOLUCION_ENCONTRADA, resultado);
    }

    /** Verifica que el algoritmo detecta cuando no hay solucion */
    @Test
    public void testDetectaSinSolucion() {
        BacktrackingLaberinto bt = new BacktrackingLaberinto(laberintoSinSolucion);
        ResultadoPaso resultado = ResultadoPaso.EN_PROGRESO;

        while (resultado == ResultadoPaso.EN_PROGRESO) {
            resultado = bt.avanzarPaso();
        }

        assertEquals(ResultadoPaso.SIN_SOLUCION, resultado);
    }

    /** Verifica que isTerminado es true cuando el algoritmo finaliza */
    @Test
    public void testIsTerminado() {
        BacktrackingLaberinto bt = new BacktrackingLaberinto(laberintoConSolucion);

        assertFalse(bt.isTerminado());

        while (!bt.isTerminado()) {
            bt.avanzarPaso();
        }

        assertTrue(bt.isTerminado());
    }

    /** Verifica que llamar avanzarPaso despues de terminar no cambia el resultado */
    @Test
    public void testAvanzarDespuesDeTerminar() {
        BacktrackingLaberinto bt = new BacktrackingLaberinto(laberintoConSolucion);

        while (!bt.isTerminado()) {
            bt.avanzarPaso();
        }

        ResultadoPaso resultadoFinal = bt.getResultado();
        bt.avanzarPaso();
        bt.avanzarPaso();

        assertEquals(resultadoFinal, bt.getResultado());
    }
}