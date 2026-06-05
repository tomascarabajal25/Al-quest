package ciudades.testDeCiudadRecoleccion;

import Juego.Constantes;
import Juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelos.Jugador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCiudadRecoleccion {

    @Test
    public void crearCiudadValida() {

        Jugador jugador = new Jugador("Tomas");

        CiudadRecoleccion ciudad =
                new CiudadRecoleccion(
                        40,
                        40,
                        3,
                        10,
                        jugador
                );

        assertNotNull(ciudad);
    }

    @Test
    public void crearCiudadConFilasCeroLanzaExcepcion() {

        Jugador jugador = new Jugador("Tomas");

        assertThrows(
                RuntimeException.class,
                () -> new CiudadRecoleccion(
                        0,
                        40,
                        3,
                        10,
                        jugador
                )
        );
    }

    @Test
    public void crearCiudadConColumnasCeroLanzaExcepcion() {

        Jugador jugador = new Jugador("Tomas");

        assertThrows(
                RuntimeException.class,
                () -> new CiudadRecoleccion(
                        40,
                        0,
                        3,
                        10,
                        jugador
                )
        );
    }

    @Test
    public void crearCiudadConNivelesCeroLanzaExcepcion() {

        Jugador jugador = new Jugador("Tomas");

        assertThrows(
                RuntimeException.class,
                () -> new CiudadRecoleccion(
                        40,
                        40,
                        0,
                        10,
                        jugador
                )
        );
    }

    @Test
    public void crearCiudadConCapacidadMochilaCeroLanzaExcepcion() {

        Jugador jugador = new Jugador("Tomas");

        assertThrows(
                RuntimeException.class,
                () -> new CiudadRecoleccion(
                        40,
                        40,
                        3,
                        0,
                        jugador
                )
        );
    }

    @Test
    public void crearCiudadConJugadorNuloLanzaExcepcion() {

        assertThrows(
                RuntimeException.class,
                () -> new CiudadRecoleccion(
                        40,
                        40,
                        3,
                        10,
                        null
                )
        );
    }

    @Test
    public void ciudadSeInicializaConValoresCorrectos() {

        Jugador jugador = new Jugador("Tomas");

        CiudadRecoleccion ciudad =
                new CiudadRecoleccion(
                        40,
                        40,
                        3,
                        10,
                        jugador
                );

        assertEquals(
                Constantes.DESPLAZAMIENTO_INICIAL,
                ciudad.getDesplazamiento()
        );

        assertEquals(
                Constantes.VISIBILIDAD_INICIAL,
                ciudad.getVisibilidad()
        );

        assertEquals(
                Constantes.PUNTOS_INICIALES_PARTIDA,
                ciudad.getPuntos()
        );

        assertFalse(ciudad.estaFinalizado());
    }

    @Test
    public void jugadorComienzaEnLaPosicionUnoUnoUno() {

        Jugador jugador = new Jugador("Tomas");

        CiudadRecoleccion ciudad =
                new CiudadRecoleccion(
                        40,
                        40,
                        3,
                        10,
                        jugador
                );

        int[] posicion = ciudad.getPosicionJugador();

        assertEquals(1, posicion[0]);
        assertEquals(1, posicion[1]);
        assertEquals(1, posicion[2]);
    }

    @Test
    public void moverJugadorHaciaAbajo() {

        Jugador jugador = new Jugador("Tomas");

        CiudadRecoleccion ciudad =
                new CiudadRecoleccion(
                        40,
                        40,
                        3,
                        10,
                        jugador
                );

        ciudad.moverJugador('S');

        int[] posicion = ciudad.getPosicionJugador();

        assertEquals(2, posicion[0]);
        assertEquals(1, posicion[1]);
        assertEquals(1, posicion[2]);
    }

    @Test
    public void moverJugadorHaciaDerecha() {

        Jugador jugador = new Jugador("Tomas");

        CiudadRecoleccion ciudad =
                new CiudadRecoleccion(
                        40,
                        40,
                        3,
                        10,
                        jugador
                );

        ciudad.moverJugador('D');

        int[] posicion = ciudad.getPosicionJugador();

        assertEquals(1, posicion[0]);
        assertEquals(2, posicion[1]);
        assertEquals(1, posicion[2]);
    }

    @Test
    public void aumentarVisionIncrementaLaVisibilidad() {

        Jugador jugador = new Jugador("Tomas");

        CiudadRecoleccion ciudad =
                new CiudadRecoleccion(
                        40,
                        40,
                        3,
                        10,
                        jugador
                );

        int visibilidadInicial = ciudad.getVisibilidad();

        ciudad.aumentarVision();

        assertEquals(
                visibilidadInicial +
                        Constantes.CANTIDAD_AUMENTO_VISIBILIDAD,
                ciudad.getVisibilidad()
        );
    }

    @Test
    public void aumentarDesplazamientoIncrementaElDesplazamiento() {

        Jugador jugador = new Jugador("Tomas");

        CiudadRecoleccion ciudad =
                new CiudadRecoleccion(
                        40,
                        40,
                        3,
                        10,
                        jugador
                );

        int desplazamientoInicial = ciudad.getDesplazamiento();

        ciudad.aumentardesplazamiento();

        assertEquals(
                desplazamientoInicial +
                        Constantes.CANTIDAD_AUMENTO_DESPLAZAMIENTO,
                ciudad.getDesplazamiento()
        );
    }

    @Test
    public void finalizarDevuelveLosPuntosActuales() {

        Jugador jugador = new Jugador("Tomas");

        CiudadRecoleccion ciudad =
                new CiudadRecoleccion(
                        40,
                        40,
                        3,
                        10,
                        jugador
                );

        assertEquals(
                ciudad.getPuntos(),
                ciudad.finalizar()
        );
    }

    @Test
    public void limpiarUltimoMensajeDejaElMensajeEnNull() {

        Jugador jugador = new Jugador("Tomas");

        CiudadRecoleccion ciudad =
                new CiudadRecoleccion(
                        40,
                        40,
                        3,
                        10,
                        jugador
                );

        ciudad.limpiarUltimoMensaje();

        assertNull(ciudad.getUltimoMensaje());
    }

    @Test
    public void dosCiudadesIgualesTienenMismoHashCode() {

        Jugador jugador = new Jugador("Tomas");

        CiudadRecoleccion ciudad1 =
                new CiudadRecoleccion(
                        40,
                        40,
                        3,
                        10,
                        jugador
                );

        CiudadRecoleccion ciudad2 =
                new CiudadRecoleccion(
                        40,
                        40,
                        3,
                        10,
                        jugador
                );

        assertEquals(
                ciudad1.hashCode(),
                ciudad2.hashCode()
        );
    }

    @Test
    public void toStringNoDevuelveNull() {

        Jugador jugador = new Jugador("Tomas");

        CiudadRecoleccion ciudad =
                new CiudadRecoleccion(
                        40,
                        40,
                        3,
                        10,
                        jugador
                );

        assertNotNull(ciudad.toString());
    }
}