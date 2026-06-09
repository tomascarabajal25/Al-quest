package Juego.ciudades.recoleccionEnMatriz.ui;

import modelosVista.KeyHandler;

import java.awt.event.KeyEvent;

/**
 * KeyHandler extendido para la ciudad de recolección.
 * Agrega las teclas E (recoger carta) y P (mochila)
 * sobre el movimiento base que ya maneja KeyHandler.
 *
 * Uso desde PartidaDeRecoleccion:
 *   KeyHandlerRecoleccion key = new KeyHandlerRecoleccion();
 *   Vista vista = new Vista(rutaMundo, jugador, spawnCol, spawnFila, sprites, key);
 */
public class KeyHandlerRecoleccion extends KeyHandler {

    public boolean ePressed = false;   // recoger carta
    public boolean pPressed = false;   // abrir/cerrar mochila
    public int cartaPresionada = 0;
    // Agregar más teclas específicas acá si se necesitan

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_E) ePressed = true;
        if (code == KeyEvent.VK_P) pPressed = true;
        if (code == KeyEvent.VK_1) cartaPresionada = 1;
        if (code == KeyEvent.VK_2) cartaPresionada = 2;
        if (code == KeyEvent.VK_3) cartaPresionada = 3;
    }

    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_E) ePressed = false;
        if (code == KeyEvent.VK_P) pPressed = false;
        if (code == KeyEvent.VK_1 || code == KeyEvent.VK_2 || code == KeyEvent.VK_3)
            cartaPresionada = 0;
    }
}