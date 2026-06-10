package ciudades.testDeCiudadRecoleccion.testUi;

import juego.ciudades.recoleccionEnMatriz.ui.KeyHandlerRecoleccion;
import org.junit.jupiter.api.Test;
import javax.swing.*;
import java.awt.event.KeyEvent;
import static org.junit.jupiter.api.Assertions.*;

class TestKeyHandlerRecoleccion {

    private KeyEvent crearEvento(int keyCode) {
        return new KeyEvent(
                new JPanel(),
                KeyEvent.KEY_PRESSED,
                System.currentTimeMillis(),
                0,
                keyCode,
                KeyEvent.CHAR_UNDEFINED
        );
    }

    @Test
    void alPresionarESeActivaEPressed() {
        KeyHandlerRecoleccion handler = new KeyHandlerRecoleccion();

        handler.keyPressed(crearEvento(KeyEvent.VK_E));

        assertTrue(handler.getEPressed());
    }

    @Test
    void alLiberarESeDesactivaEPressed() {
        KeyHandlerRecoleccion handler = new KeyHandlerRecoleccion();

        handler.keyPressed(crearEvento(KeyEvent.VK_E));

        KeyEvent release = new KeyEvent(
                new JPanel(),
                KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_E,
                KeyEvent.CHAR_UNDEFINED
        );

        handler.keyReleased(release);

        assertFalse(handler.getEPressed());
    }

    @Test
    void alPresionarPSeActivaPPressed() {
        KeyHandlerRecoleccion handler = new KeyHandlerRecoleccion();

        handler.keyPressed(crearEvento(KeyEvent.VK_P));

        assertTrue(handler.getPPressed());
    }

    @Test
    void alLiberarPSeDesactivaPPressed() {
        KeyHandlerRecoleccion handler = new KeyHandlerRecoleccion();

        handler.keyPressed(crearEvento(KeyEvent.VK_P));

        KeyEvent release = new KeyEvent(
                new JPanel(),
                KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_P,
                KeyEvent.CHAR_UNDEFINED
        );

        handler.keyReleased(release);

        assertFalse(handler.getPPressed());
    }

    @Test
    void alPresionarUnoSeGuardaCartaUno() {
        KeyHandlerRecoleccion handler = new KeyHandlerRecoleccion();

        handler.keyPressed(crearEvento(KeyEvent.VK_1));

        assertEquals(1, handler.getCartaPresionada());
    }

    @Test
    void alPresionarDosSeGuardaCartaDos() {
        KeyHandlerRecoleccion handler = new KeyHandlerRecoleccion();

        handler.keyPressed(crearEvento(KeyEvent.VK_2));

        assertEquals(2, handler.getCartaPresionada());
    }

    @Test
    void alPresionarTresSeGuardaCartaTres() {
        KeyHandlerRecoleccion handler = new KeyHandlerRecoleccion();

        handler.keyPressed(crearEvento(KeyEvent.VK_3));

        assertEquals(3, handler.getCartaPresionada());
    }

    @Test
    void alLiberarCartaSeRestableceACero() {
        KeyHandlerRecoleccion handler = new KeyHandlerRecoleccion();

        handler.keyPressed(crearEvento(KeyEvent.VK_1));

        KeyEvent release = new KeyEvent(
                new JPanel(),
                KeyEvent.KEY_RELEASED,
                System.currentTimeMillis(),
                0,
                KeyEvent.VK_1,
                KeyEvent.CHAR_UNDEFINED
        );

        handler.keyReleased(release);

        assertEquals(0, handler.getCartaPresionada());
    }

    @Test
    void restablecerCartaPresionadaLaVuelveACero() {
        KeyHandlerRecoleccion handler = new KeyHandlerRecoleccion();

        handler.keyPressed(crearEvento(KeyEvent.VK_2));

        handler.restablecerCartaPresionada();

        assertEquals(0, handler.getCartaPresionada());
    }
}