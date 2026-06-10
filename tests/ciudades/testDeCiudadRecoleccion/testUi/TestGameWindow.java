package ciudades.testDeCiudadRecoleccion.testUi;

import modelos.Jugador;
import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

class TestGameWindow {

    @Test
    void constructorInicializaCorrectamente() {
        Jugador jugador = new Jugador("Tomas");
        GameWindow ventana = new GameWindow(jugador, 20, 35, 3, 10);

        assertNotNull(ventana.getJuego());
        assertNotNull(ventana.getPanelJuego());
        assertFalse(ventana.isMochilaVisible());
        ventana.dispose();
    }

    @Test
    void equalsMismoObjeto() {
        Jugador jugador = new Jugador("Tomas");
        GameWindow ventana = new GameWindow(jugador, 20, 35, 3, 10);

        assertEquals(ventana, ventana);
        ventana.dispose();

    }

    @Test
    void equalsNull() {
        Jugador jugador = new Jugador("Tomas");
        GameWindow ventana = new GameWindow(jugador, 20, 35, 3, 10);

        assertNotEquals(null, ventana);

        ventana.dispose();
    }

    @Test
    void equalsDistintaClase() {
        Jugador jugador = new Jugador("Tomas");
        GameWindow ventana = new GameWindow(jugador, 20, 35, 3, 10);

        assertNotEquals("hola", ventana);

        ventana.dispose();
    }

    @Test
    void hashCodeConsistente() {
        Jugador jugador = new Jugador("Tomas");
        GameWindow ventana = new GameWindow(jugador, 20, 35, 3, 10);
        int hash1 = ventana.hashCode();
        int hash2 = ventana.hashCode();

        assertEquals(hash1, hash2);

        ventana.dispose();
    }

    @Test
    void toStringNoEsNull() {
        Jugador jugador = new Jugador("Tomas");
        GameWindow ventana = new GameWindow(jugador, 20, 35, 3, 1);

        assertNotNull(ventana.toString());
        assertFalse(ventana.toString().isEmpty());

        ventana.dispose();
    }

    @Test
    void constructorFilasInvalidas() {
        Jugador jugador = new Jugador("Tomas");
        assertThrows(RuntimeException.class, () -> new GameWindow(jugador, 0, 35, 3, 10));
    }

    @Test
    void constructorColumnasInvalidas() {
        Jugador jugador = new Jugador("Tomas");
        assertThrows(RuntimeException.class, () -> new GameWindow(jugador, 20, 0, 3, 10));
    }

    @Test
    void teclaPAbreMochila() {
        Jugador jugador = new Jugador("Tomas");
        GameWindow ventana = new GameWindow(jugador, 20, 35, 3, 10);
        KeyEvent evento = new KeyEvent(ventana, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_P, 'P');

        ventana.keyPressed(evento);

        assertTrue(ventana.isMochilaVisible());

        ventana.dispose();
    }

    @Test
    void teclaPCierraMochila() {
        Jugador jugador = new Jugador("Tomas");
        GameWindow ventana = new GameWindow(jugador, 20, 35, 3, 10);
        KeyEvent evento = new KeyEvent(ventana, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_P, 'P');

        ventana.keyPressed(evento);
        ventana.keyPressed(evento);

        assertFalse(ventana.isMochilaVisible());

        ventana.dispose();
    }
}
