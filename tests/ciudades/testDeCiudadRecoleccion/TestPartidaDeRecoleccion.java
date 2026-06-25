package ciudades.testDeCiudadRecoleccion;

import juego.ciudades.recoleccionEnMatriz.PartidaDeRecoleccion;
import modelos.Jugador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPartidaDeRecoleccion {

    @Test
    public void crearPartidaValida() {
        Jugador jugador = new Jugador("Tomas");
        PartidaDeRecoleccion partida = new PartidaDeRecoleccion("Partida", jugador, null);

        assertEquals("Partida", partida.getNombre());
        assertEquals(jugador, partida.getJugador());
        assertEquals(0, partida.getPuntaje());
        assertNotNull(partida.getJuego());
    }

    @Test
    public void getJuegoDevuelveJuegoInicializado() {

        Jugador jugador = new Jugador("Tomas");
        PartidaDeRecoleccion partida = new PartidaDeRecoleccion("Partida", jugador, null);
        assertNotNull(partida.getJuego());
    }

    @Test
    public void crearPartidaConFilasCeroLanzaExcepcion() {
        Jugador jugador = new Jugador("Tomas");
        assertThrows(RuntimeException.class, () -> new PartidaDeRecoleccion("Partida", jugador, null));
    }

    @Test
    public void crearPartidaConColumnasCeroLanzaExcepcion() {
        Jugador jugador = new Jugador("Tomas");
        assertThrows(RuntimeException.class, () -> new PartidaDeRecoleccion("Partida", jugador, null));
    }

    @Test
    public void crearPartidaConNivelesCeroLanzaExcepcion() {
        Jugador jugador = new Jugador("Tomas");
        assertThrows(RuntimeException.class, () -> new PartidaDeRecoleccion("Partida", jugador, null));
    }

    @Test
    public void crearPartidaConCapacidadDeMochilaCeroLanzaExcepcion() {
        Jugador jugador = new Jugador("Tomas");
        assertThrows(RuntimeException.class, () -> new PartidaDeRecoleccion("Partida", jugador, null));
    }

    @Test
    public void crearPartidaConJugadorNuloLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> new PartidaDeRecoleccion("Partida", null, null));
    }

    @Test
    public void dosPartidasConLosMismosDatosSonIguales() {
        Jugador jugador = new Jugador("Tomas");
        PartidaDeRecoleccion partida2 = new PartidaDeRecoleccion("Partida", jugador, null);
        PartidaDeRecoleccion partida1 = new PartidaDeRecoleccion("Partida", jugador, null);
        assertEquals(partida1, partida2);
    }

    @Test
    public void dosPartidasIgualesTienenMismoHashCode() {
        Jugador jugador = new Jugador("Tomas");
        PartidaDeRecoleccion partida1 = new PartidaDeRecoleccion("Partida", jugador, null);
        PartidaDeRecoleccion partida2 = new PartidaDeRecoleccion("Partida", jugador, null);
        assertEquals(partida1.hashCode(), partida2.hashCode());
    }

    @Test
    public void dosPartidasConDatosDistintosNoSonIguales() {
        Jugador jugador = new Jugador("Tomas");
        PartidaDeRecoleccion partida1 = new PartidaDeRecoleccion("Partida1", jugador, null);
        PartidaDeRecoleccion partida2 = new PartidaDeRecoleccion("Partida2", jugador, null);
        assertNotEquals(partida1, partida2);
    }

    @Test
    public void toStringNoDevuelveNull() {
        Jugador jugador = new Jugador("Tomas");
        PartidaDeRecoleccion partida = new PartidaDeRecoleccion("Partida", jugador, null);
        assertNotNull(partida.toString());
    }
}