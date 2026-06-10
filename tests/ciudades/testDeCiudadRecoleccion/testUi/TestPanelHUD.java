package ciudades.testDeCiudadRecoleccion.testUi;

import Juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelos.Jugador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TestPanelHUD {

    @Test
    void constructorInicializaCorrectamente() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 10, jugador);
        PanelHUD hud = new PanelHUD(juego);

        assertNotNull(hud.getJuego());
        assertEquals(juego, hud.getJuego());
    }

    @Test
    void constructorConJuegoNullLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> new PanelHUD(null));
    }

    @Test
    void getJuegoDevuelveJuegoCorrecto() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 10, jugador);
        PanelHUD hud = new PanelHUD(juego);

        assertSame(juego, hud.getJuego());
    }

    @Test
    void equalsMismoObjeto() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 10, jugador);
        PanelHUD hud = new PanelHUD(juego);

        assertEquals(hud, hud);
    }

    @Test
    void equalsConNull() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 10, jugador);
        PanelHUD hud = new PanelHUD(juego);

        assertNotEquals(null, hud);
    }

    @Test
    void equalsConOtraClase() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 10, jugador);
        PanelHUD hud = new PanelHUD(juego);

        assertNotEquals("hola", hud);
    }

    @Test
    void equalsConMismoJuego() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 10, jugador);
        PanelHUD hud1 = new PanelHUD(juego);
        PanelHUD hud2 = new PanelHUD(juego);

        assertEquals(hud1, hud2);
    }

    @Test
    void hashCodeEsConsistente() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 10, jugador);
        PanelHUD hud = new PanelHUD(juego);
        int hash1 = hud.hashCode();
        int hash2 = hud.hashCode();

        assertEquals(hash1, hash2);
    }

    @Test
    void panelesIgualesTienenMismoHashCode() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 10, jugador);
        PanelHUD hud1 = new PanelHUD(juego);
        PanelHUD hud2 = new PanelHUD(juego);

        assertEquals(hud1.hashCode(), hud2.hashCode());
    }

    @Test
    void toStringNoEsNull() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 10, jugador);
        PanelHUD hud = new PanelHUD(juego);

        assertNotNull(hud.toString());
    }

    @Test
    void toStringNoEstaVacio() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 10, jugador);
        PanelHUD hud = new PanelHUD(juego);

        assertFalse(hud.toString().isEmpty());
    }

    @Test
    void panelCreadoEsOpaque() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion juego = new CiudadRecoleccion(20, 35, 3, 10, jugador);
        PanelHUD hud = new PanelHUD(juego);

        assertTrue(hud.isOpaque());
    }
}
