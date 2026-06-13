package modelosVista;

import org.junit.jupiter.api.Test;

import java.awt.event.KeyEvent;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class TestKeyHandler {
    @Test
    public void constructorExisteYEsPublico() throws Exception {
        var c = KeyHandler.class.getConstructor();
        assertTrue(Modifier.isPublic(c.getModifiers()));
    }

    @Test
    public void estadoInicialUpPressedEsFalse() {
        KeyHandler kh = new KeyHandler();
        assertFalse(kh.getUpPressed());
    }

    @Test
    public void estadoInicialDownPressedEsFalse() {
        KeyHandler kh = new KeyHandler();
        assertFalse(kh.getDownPressed());
    }

    @Test
    public void estadoInicialLeftPressedEsFalse() {
        KeyHandler kh = new KeyHandler();
        assertFalse(kh.getLeftPressed());
    }

    @Test
    public void estadoInicialRightPressedEsFalse() {
        KeyHandler kh = new KeyHandler();
        assertFalse(kh.getRightPressed());
    }

    @Test
    public void estadoInicialEnterPresionadoEsFalse() {
        KeyHandler kh = new KeyHandler();
        assertFalse(kh.enterPresionado);
    }

    @Test
    public void keyTypedConEventoNuloLanzaExcepcion() {
        KeyHandler kh = new KeyHandler();
        assertThrows(RuntimeException.class, () -> kh.keyTyped(null));
    }

    @Test
    public void keyPressedConEventoNuloLanzaExcepcion() {
        KeyHandler kh = new KeyHandler();
        assertThrows(RuntimeException.class, () -> kh.keyPressed(null));
    }

    @Test
    public void keyPressedVK_W_ActivaUpPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_W));
        assertTrue(kh.getUpPressed());
    }

    @Test
    public void keyPressedVK_UP_ActivaUpPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_UP));
        assertTrue(kh.getUpPressed());
    }

    @Test
    public void keyPressedVK_S_ActivaDownPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_S));
        assertTrue(kh.getDownPressed());
    }

    @Test
    public void keyPressedVK_DOWN_ActivaDownPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_DOWN));
        assertTrue(kh.getDownPressed());
    }

    @Test
    public void keyPressedVK_A_ActivaLeftPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_A));
        assertTrue(kh.getLeftPressed());
    }

    @Test
    public void keyPressedVK_LEFT_ActivaLeftPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_LEFT));
        assertTrue(kh.getLeftPressed());
    }

    @Test
    public void keyPressedVK_D_ActivaRightPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_D));
        assertTrue(kh.getRightPressed());
    }

    @Test
    public void keyPressedVK_RIGHT_ActivaRightPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_RIGHT));
        assertTrue(kh.getRightPressed());
    }

    @Test
    public void keyPressedVK_ENTER_ActivaEnterPresionado() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_ENTER));
        assertTrue(kh.enterPresionado);
    }

    @Test
    public void keyPressedNoAfectaOtrasFlags() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_W));
        assertFalse(kh.getDownPressed());
        assertFalse(kh.getLeftPressed());
        assertFalse(kh.getRightPressed());
        assertFalse(kh.enterPresionado);
    }

    @Test
    public void keyReleasedConEventoNuloLanzaExcepcion() {
        KeyHandler kh = new KeyHandler();
        assertThrows(RuntimeException.class, () -> kh.keyReleased(null));
    }

    @Test
    public void keyReleasedVK_W_DesactivaUpPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_W));
        kh.keyReleased(simularTecla(KeyEvent.VK_W));
        assertFalse(kh.getUpPressed());
    }

    @Test
    public void keyReleasedVK_UP_DesactivaUpPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_UP));
        kh.keyReleased(simularTecla(KeyEvent.VK_UP));
        assertFalse(kh.getUpPressed());
    }

    @Test
    public void keyReleasedVK_S_DesactivaDownPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_S));
        kh.keyReleased(simularTecla(KeyEvent.VK_S));
        assertFalse(kh.getDownPressed());
    }

    @Test
    public void keyReleasedVK_DOWN_DesactivaDownPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_DOWN));
        kh.keyReleased(simularTecla(KeyEvent.VK_DOWN));
        assertFalse(kh.getDownPressed());
    }

    @Test
    public void keyReleasedVK_A_DesactivaLeftPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_A));
        kh.keyReleased(simularTecla(KeyEvent.VK_A));
        assertFalse(kh.getLeftPressed());
    }

    @Test
    public void keyReleasedVK_LEFT_DesactivaLeftPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_LEFT));
        kh.keyReleased(simularTecla(KeyEvent.VK_LEFT));
        assertFalse(kh.getLeftPressed());
    }

    @Test
    public void keyReleasedVK_D_DesactivaRightPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_D));
        kh.keyReleased(simularTecla(KeyEvent.VK_D));
        assertFalse(kh.getRightPressed());
    }

    @Test
    public void keyReleasedVK_RIGHT_DesactivaRightPressed() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_RIGHT));
        kh.keyReleased(simularTecla(KeyEvent.VK_RIGHT));
        assertFalse(kh.getRightPressed());
    }

    @Test
    public void keyReleasedVK_ENTER_DesactivaEnterPresionado() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_ENTER));
        kh.keyReleased(simularTecla(KeyEvent.VK_ENTER));
        assertFalse(kh.enterPresionado);
    }

    @Test
    public void teclaIrreconocidaNoAlteraNingunFlag() {
        KeyHandler kh = new KeyHandler();
        kh.keyPressed(simularTecla(KeyEvent.VK_ESCAPE));
        assertFalse(kh.getUpPressed());
        assertFalse(kh.getDownPressed());
        assertFalse(kh.getLeftPressed());
        assertFalse(kh.getRightPressed());
        assertFalse(kh.enterPresionado);
    }

    @Test
    public void setUpPressedEstableceValorTrue() {
        KeyHandler kh = new KeyHandler();
        kh.setUpPressed(true);
        assertTrue(kh.getUpPressed());
    }

    @Test
    public void setUpPressedEstableceValorFalse() {
        KeyHandler kh = new KeyHandler();
        kh.setUpPressed(true);
        kh.setUpPressed(false);
        assertFalse(kh.getUpPressed());
    }

    @Test
    public void setDownPressedEstableceValorTrue() {
        KeyHandler kh = new KeyHandler();
        kh.setDownPressed(true);
        assertTrue(kh.getDownPressed());
    }

    @Test
    public void setDownPressedEstableceValorFalse() {
        KeyHandler kh = new KeyHandler();
        kh.setDownPressed(true);
        kh.setDownPressed(false);
        assertFalse(kh.getDownPressed());
    }

    @Test
    public void setLeftPressedEstableceValorTrue() {
        KeyHandler kh = new KeyHandler();
        kh.setLeftPressed(true);
        assertTrue(kh.getLeftPressed());
    }

    @Test
    public void setLeftPressedEstableceValorFalse() {
        KeyHandler kh = new KeyHandler();
        kh.setLeftPressed(true);
        kh.setLeftPressed(false);
        assertFalse(kh.getLeftPressed());
    }

    @Test
    public void setRightPressedEstableceValorTrue() {
        KeyHandler kh = new KeyHandler();
        kh.setRightPressed(true);
        assertTrue(kh.getRightPressed());
    }

    @Test
    public void setRightPressedEstableceValorFalse() {
        KeyHandler kh = new KeyHandler();
        kh.setRightPressed(true);
        kh.setRightPressed(false);
        assertFalse(kh.getRightPressed());
    }

    @Test
    public void getUpPressedEsPublico() throws Exception {
        var m = KeyHandler.class.getMethod("getUpPressed");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void getDownPressedEsPublico() throws Exception {
        var m = KeyHandler.class.getMethod("getDownPressed");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void getLeftPressedEsPublico() throws Exception {
        var m = KeyHandler.class.getMethod("getLeftPressed");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void getRightPressedEsPublico() throws Exception {
        var m = KeyHandler.class.getMethod("getRightPressed");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void setUpPressedEsPublico() throws Exception {
        var m = KeyHandler.class.getMethod("setUpPressed", boolean.class);
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void setDownPressedEsPublico() throws Exception {
        var m = KeyHandler.class.getMethod("setDownPressed", boolean.class);
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void setLeftPressedEsPublico() throws Exception {
        var m = KeyHandler.class.getMethod("setLeftPressed", boolean.class);
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void setRightPressedEsPublico() throws Exception {
        var m = KeyHandler.class.getMethod("setRightPressed", boolean.class);
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void keyHandlerImplementaKeyListener() {
        KeyHandler kh = new KeyHandler();
        assertTrue(kh instanceof java.awt.event.KeyListener);
    }

    private KeyEvent simularTecla(int keyCode) {
        return new KeyEvent(
            new java.awt.Component() {},
            KeyEvent.KEY_PRESSED,
            System.currentTimeMillis(),
            0,
            keyCode,
            KeyEvent.CHAR_UNDEFINED
        );
    }
}
