package ciudades.testDeCiudadRecoleccion;

import Juego.ciudades.recoleccionEnMatriz.PartidaDeRecoleccion;
import modelos.Jugador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPartidaDeRecoleccion {

    @Test
    public void crearPartidaValida() {

        Jugador jugador = new Jugador("Tomas");

        PartidaDeRecoleccion partida =
                new PartidaDeRecoleccion(
                        "Partida",
                        jugador,
                        10,
                        10,
                        3,
                        20
                );

        assertEquals("Partida", partida.getNombre());
        assertEquals(jugador, partida.getJugador());
        assertEquals(0, partida.getPuntajeActual());
        assertNotNull(partida.getJuego());
    }

    @Test
    public void getJuegoDevuelveJuegoInicializado() {

        Jugador jugador = new Jugador("Tomas");

        PartidaDeRecoleccion partida =
                new PartidaDeRecoleccion(
                        "Partida",
                        jugador,
                        10,
                        10,
                        3,
                        20
                );

        assertNotNull(partida.getJuego());
    }

    @Test
    public void crearPartidaConFilasCeroLanzaExcepcion() {

        Jugador jugador = new Jugador("Tomas");

        assertThrows(RuntimeException.class,
                () -> new PartidaDeRecoleccion(
                        "Partida",
                        jugador,
                        0,
                        10,
                        3,
                        20
                ));
    }

    @Test
    public void crearPartidaConColumnasCeroLanzaExcepcion() {

        Jugador jugador = new Jugador("Tomas");

        assertThrows(RuntimeException.class,
                () -> new PartidaDeRecoleccion(
                        "Partida",
                        jugador,
                        10,
                        0,
                        3,
                        20
                ));
    }

    @Test
    public void crearPartidaConNivelesCeroLanzaExcepcion() {

        Jugador jugador = new Jugador("Tomas");

        assertThrows(RuntimeException.class,
                () -> new PartidaDeRecoleccion(
                        "Partida",
                        jugador,
                        10,
                        10,
                        0,
                        20
                ));
    }

    @Test
    public void crearPartidaConCapacidadDeMochilaCeroLanzaExcepcion() {

        Jugador jugador = new Jugador("Tomas");

        assertThrows(RuntimeException.class,
                () -> new PartidaDeRecoleccion(
                        "Partida",
                        jugador,
                        10,
                        10,
                        3,
                        0
                ));
    }

    @Test
    public void crearPartidaConJugadorNuloLanzaExcepcion() {

        assertThrows(RuntimeException.class,
                () -> new PartidaDeRecoleccion(
                        "Partida",
                        null,
                        10,
                        10,
                        3,
                        20
                ));
    }

    @Test
    public void dosPartidasConLosMismosDatosSonIguales() {

        Jugador jugador = new Jugador("Tomas");

        PartidaDeRecoleccion partida1 =
                new PartidaDeRecoleccion(
                        "Partida",
                        jugador,
                        10,
                        10,
                        3,
                        20
                );

        PartidaDeRecoleccion partida2 =
                new PartidaDeRecoleccion(
                        "Partida",
                        jugador,
                        10,
                        10,
                        3,
                        20
                );

        assertEquals(partida1, partida2);
    }

    @Test
    public void dosPartidasIgualesTienenMismoHashCode() {

        Jugador jugador = new Jugador("Tomas");

        PartidaDeRecoleccion partida1 =
                new PartidaDeRecoleccion(
                        "Partida",
                        jugador,
                        10,
                        10,
                        3,
                        20
                );

        PartidaDeRecoleccion partida2 =
                new PartidaDeRecoleccion(
                        "Partida",
                        jugador,
                        10,
                        10,
                        3,
                        20
                );

        assertEquals(partida1.hashCode(), partida2.hashCode());
    }

    @Test
    public void dosPartidasConDatosDistintosNoSonIguales() {

        Jugador jugador = new Jugador("Tomas");

        PartidaDeRecoleccion partida1 =
                new PartidaDeRecoleccion(
                        "Partida1",
                        jugador,
                        10,
                        10,
                        3,
                        20
                );

        PartidaDeRecoleccion partida2 =
                new PartidaDeRecoleccion(
                        "Partida2",
                        jugador,
                        10,
                        10,
                        3,
                        20
                );

        assertNotEquals(partida1, partida2);
    }

    @Test
    public void toStringNoDevuelveNull() {

        Jugador jugador = new Jugador("Tomas");

        PartidaDeRecoleccion partida =
                new PartidaDeRecoleccion(
                        "Partida",
                        jugador,
                        10,
                        10,
                        3,
                        20
                );

        assertNotNull(partida.toString());
    }
}