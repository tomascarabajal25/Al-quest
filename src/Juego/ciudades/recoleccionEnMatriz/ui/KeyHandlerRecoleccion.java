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
    // Agregar más teclas específicas acá si se necesitan

    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e); // delega W/A/S/D al padre

        int code = e.getKeyCode();
        if (code == KeyEvent.VK_E) {
        	ePressed = true;
        }
        if (code == KeyEvent.VK_P) {
        	pPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e); // delega W/A/S/D al padre

        int code = e.getKeyCode();
        if (code == KeyEvent.VK_E) {
        	ePressed = false;
        }
        if (code == KeyEvent.VK_P) {
        	pPressed = false;
        }
    }
}