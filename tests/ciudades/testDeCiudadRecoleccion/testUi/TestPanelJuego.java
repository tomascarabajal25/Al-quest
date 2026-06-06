package ciudades.testDeCiudadRecoleccion.testUi;

import Juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import Juego.ciudades.recoleccionEnMatriz.ui.PanelHUD;
import Juego.ciudades.recoleccionEnMatriz.ui.PanelJuego;
import modelos.Jugador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestPanelJuego {

    @Test
    void constructorInicializaCorrectamente() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);
        PanelJuego panel = new PanelJuego(juego, 5, 5);

        assertEquals(juego, panel.getJuego());
        assertEquals(5, panel.getFilas());
        assertEquals(5, panel.getColumnas());
        assertNotNull(panel.getTileCache());
        assertFalse(panel.isMostrarMochila());
        assertNull(panel.getMensajeTemp());
    }

    @Test
    void constructorJuegoNullLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> new PanelJuego(null, 5, 5));
    }

    @Test
    void constructorFilasInvalidasLanzaExcepcion() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);

        assertThrows(RuntimeException.class, () -> new PanelJuego(juego, 0, 5));
    }

    @Test
    void constructorColumnasInvalidasLanzaExcepcion() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);

        assertThrows(RuntimeException.class, () -> new PanelJuego(juego, 5, 0));
    }

    @Test
    void getJuegoDevuelveJuegoCorrecto() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);
        PanelJuego panel = new PanelJuego(juego, 5, 5);

        assertSame(juego, panel.getJuego());
    }

    @Test
    void getTileCacheNoEstaVacio() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);
        PanelJuego panel = new PanelJuego(juego, 5, 5);

        assertFalse(panel.getTileCache().isEmpty());
    }

    @Test
    void setMostrarMochilaActualizaEstado() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);
        PanelJuego panel = new PanelJuego(juego, 5, 5);
        panel.setMostrarMochila(true);

        assertTrue(panel.isMostrarMochila());
    }

    @Test
    void mostrarMensajeGuardaMensaje() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);
        PanelJuego panel = new PanelJuego(juego, 5, 5);
        panel.mostrarMensaje("Hola");

        assertEquals("Hola", panel.getMensajeTemp());
    }

    @Test
    void mostrarMensajeNullLanzaExcepcion() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);
        PanelJuego panel = new PanelJuego(juego, 5, 5);

        assertThrows(RuntimeException.class, () -> panel.mostrarMensaje(null));
    }

    @Test
    void setHUDAsignaHudCorrectamente() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);
        PanelJuego panel = new PanelJuego(juego, 5, 5);
        PanelHUD hud = new PanelHUD(juego);
        panel.setHUD(hud);

        assertEquals(hud, panel.getHUD());
    }

    @Test
    void setHUDNullLanzaExcepcion() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);
        PanelJuego panel = new PanelJuego(juego, 5, 5);

        assertThrows(RuntimeException.class, () -> panel.setHUD(null));
    }

    @Test
    void equalsMismoObjeto() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);
        PanelJuego panel = new PanelJuego(juego, 5, 5);

        assertEquals(panel, panel);
    }

    @Test
    void equalsConNull() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);
        PanelJuego panel = new PanelJuego(juego, 5, 5);

        assertNotEquals(null, panel);
    }

    @Test
    void equalsConOtraClase() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);
        PanelJuego panel = new PanelJuego(juego, 5, 5);

        assertNotEquals("texto", panel);
    }

    @Test
    void hashCodeEsConsistente() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);
        PanelJuego panel = new PanelJuego(juego, 5, 5);
        int hash1 = panel.hashCode();
        int hash2 = panel.hashCode();

        assertEquals(hash1, hash2);
    }

    @Test
    void toStringNoEsNull() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);
        PanelJuego panel = new PanelJuego(juego, 5, 5);

        assertNotNull(panel.toString());
    }

    @Test
    void toStringNoEstaVacio() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 3, jugador);
        PanelJuego panel = new PanelJuego(juego, 5, 5);

        assertFalse(panel.toString().isEmpty());
    }
}
