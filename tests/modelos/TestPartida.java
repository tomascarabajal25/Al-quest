package modelos;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import juego.ciudades.ordenamientos.EstadoDePartida;

public class TestPartida {

    private static class PartidaDummy extends Partida {

        public PartidaDummy(String nombre, Jugador jugador) {
            super(nombre, jugador);
        }

        @Override
        public void iniciar() {
            setEstado(EstadoDePartida.Iniciado);
        }

        @Override
        public void finalizar() {
            setEstado(EstadoDePartida.Creado);
        }
    }

    @Test
    public void crearPartidaValida() {
        Jugador jugador = new Jugador("Tomas");

        Partida partida = new PartidaDummy("Partida 1", jugador);

        assertEquals("Partida 1", partida.getNombre());
        assertEquals(jugador, partida.getJugador());
        assertEquals(0, partida.getPuntaje());
        assertEquals(EstadoDePartida.Creado, partida.getEstado());
    }

    @Test
    public void partidaRecienCreadaNoEstaIniciada() {
        Jugador jugador = new Jugador("Tomas");

        Partida partida = new PartidaDummy("Partida 1", jugador);

        assertFalse(partida.estaIniciada());
    }

    @Test
    public void iniciarPartidaLaMarcaComoIniciada() {
        Jugador jugador = new Jugador("Tomas");

        Partida partida = new PartidaDummy("Partida 1", jugador);

        partida.iniciar();

        assertTrue(partida.estaIniciada());
        assertEquals(EstadoDePartida.Iniciado, partida.getEstado());
    }

    @Test
    public void finalizarPartidaCambiaSuEstado() {
        Jugador jugador = new Jugador("Tomas");

        Partida partida = new PartidaDummy("Partida 1", jugador);

        partida.finalizar();

        assertEquals(EstadoDePartida.Creado, partida.getEstado());
    }

    @Test
    public void crearPartidaConNombreNuloLanzaExcepcion() {
        Jugador jugador = new Jugador("Tomas");

        assertThrows(RuntimeException.class,
                () -> new PartidaDummy(null, jugador));
    }

    @Test
    public void crearPartidaConJugadorNuloLanzaExcepcion() {
        assertThrows(RuntimeException.class,
                () -> new PartidaDummy("Partida 1", null));
    }

    @Test
    public void dosPartidasIgualesSonEquals() {
        Jugador jugador = new Jugador("Tomas");

        Partida partida1 = new PartidaDummy("Partida 1", jugador);
        Partida partida2 = new PartidaDummy("Partida 1", jugador);

        assertEquals(partida1, partida2);
        assertEquals(partida1.hashCode(), partida2.hashCode());
    }

    @Test
    public void dosPartidasDistintasNoSonEquals() {
        Jugador jugador = new Jugador("Tomas");

        Partida partida1 = new PartidaDummy("Partida 1", jugador);
        Partida partida2 = new PartidaDummy("Partida 2", jugador);

        assertNotEquals(partida1, partida2);
    }
}