package modelos;

import static org.junit.jupiter.api.Assertions.*;

import juego.ciudades.ordenamientos.EstadoDePartida;
import org.junit.jupiter.api.Test;

public class TestPartidaGeneral {

    @Test
    public void constructorValido() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida = new PartidaGeneral(jugador);

        assertEquals("Al-Quest — Mapa Mundial", partida.getNombre());
        assertEquals(jugador, partida.getJugador());
        assertEquals(0, partida.getPuntajeTotal());
        assertEquals(EstadoDePartida.Creado, partida.getEstado());
    }

    @Test
    public void constructorJugadorNuloLanzaExcepcion() {

        assertThrows(RuntimeException.class, () -> {
            new PartidaGeneral(null);
        });
    }

    @Test
    public void constructorInicializaSkinPorDefecto() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida = new PartidaGeneral(jugador);

        assertNotNull(partida.getSkinActual());
        assertTrue(partida.getSkinActual().contains("boy"));
    }

    @Test
    public void constructorInicializaSkinsDesbloqueadas() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida = new PartidaGeneral(jugador);

        assertNotNull(partida.getSkinsDesbloqueadas());
        assertFalse(partida.getSkinsDesbloqueadas().isEmpty());
        assertTrue(partida.getSkinsDesbloqueadas().contains(partida.getSkinActual()));
    }

    @Test
    public void constructorCiudadActualEsNull() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida = new PartidaGeneral(jugador);

        assertNull(partida.getCiudadActual());
    }

    @Test
    public void constructorMapaMundiNoEsNull() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida = new PartidaGeneral(jugador);

        assertNotNull(partida.getMapaMundi());
    }

    @Test
    public void partidaRecienCreadaNoEstaIniciada() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida = new PartidaGeneral(jugador);

        assertFalse(partida.estaIniciada());
    }

    @Test
    public void comprarSkinConPuntajeInsuficiente() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida = new PartidaGeneral(jugador);

        boolean resultado = partida.comprarSkin("/assets/jugador/girl", 100);

        assertFalse(resultado);
        assertFalse(partida.getSkinsDesbloqueadas().contains("/assets/jugador/girl"));
    }

    @Test
    public void comprarSkinNulaLanzaExcepcion() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida = new PartidaGeneral(jugador);

        assertThrows(RuntimeException.class, () -> {
            partida.comprarSkin(null, 50);
        });
    }

    @Test
    public void comprarSkinCostoNegativoLanzaExcepcion() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida = new PartidaGeneral(jugador);

        assertThrows(RuntimeException.class, () -> {
            partida.comprarSkin("/assets/jugador/girl", -1);
        });
    }

    @Test
    public void comprarSkinCostoCeroEsValido() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida = new PartidaGeneral(jugador);

        boolean resultado = partida.comprarSkin("/assets/jugador/girl", 0);

        assertTrue(resultado);
        assertTrue(partida.getSkinsDesbloqueadas().contains("/assets/jugador/girl"));
    }

    @Test
    public void toStringContieneAtributos() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida = new PartidaGeneral(jugador);

        assertTrue(partida.toString().contains("Al-Quest"));
        assertTrue(partida.toString().contains("Tomas"));
    }

    @Test
    public void equalsMismaInstancia() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida = new PartidaGeneral(jugador);

        assertEquals(partida, partida);
    }

    @Test
    public void equalsPartidasIguales() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida1 = new PartidaGeneral(jugador);
        PartidaGeneral partida2 = new PartidaGeneral(jugador);

        assertEquals(partida1, partida2);
    }

    @Test
    public void equalsPartidasDistintos() {

        Jugador jugador1 = new Jugador("Tomas");
        Jugador jugador2 = new Jugador("Juan");

        PartidaGeneral partida1 = new PartidaGeneral(jugador1);
        PartidaGeneral partida2 = new PartidaGeneral(jugador2);

        assertNotEquals(partida1, partida2);
    }

    @Test
    public void equalsConNull() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida = new PartidaGeneral(jugador);

        assertNotEquals(null, partida);
    }

    @Test
    public void equalsConOtroTipo() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida = new PartidaGeneral(jugador);

        assertFalse(partida.equals("Al-Quest"));
        assertFalse(partida.equals(42));
    }

    @Test
    public void hashCodeIguales() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida1 = new PartidaGeneral(jugador);
        PartidaGeneral partida2 = new PartidaGeneral(jugador);

        assertEquals(
                partida1.hashCode(),
                partida2.hashCode()
        );
    }

    @Test
    public void hashCodeDistintos() {

        Jugador jugador1 = new Jugador("Tomas");
        Jugador jugador2 = new Jugador("Juan");

        PartidaGeneral partida1 = new PartidaGeneral(jugador1);
        PartidaGeneral partida2 = new PartidaGeneral(jugador2);

        assertNotEquals(
                partida1.hashCode(),
                partida2.hashCode()
        );
    }

    @Test
    public void hashCodeConsistencia() {

        Jugador jugador = new Jugador("Tomas");

        PartidaGeneral partida = new PartidaGeneral(jugador);

        assertEquals(partida.hashCode(), partida.hashCode());
    }
}
