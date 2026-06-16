package juego.ciudades.hashing.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * Manejador de teclado del minijuego de hashing.
 * Misma logica que KeyHandlerOrdenamiento.
 * 
 * Teclas:
 * -ESPACIO >>> Confirmar accion sobre el slot apuntado (insertar / buscar), el clasico interactuar
 * -R       >>> Activar/Pausar el resolvedor automatico.
 * -ESCAPE  >>> Cancelar/Detener el resolver.
 */
public class KeyHandlerHashing implements KeyListener{

    public boolean espacioPresionado = false;
    public boolean resolverPresionado = false;
    public boolean escapePresionado = false;

    @Override
    public void keyTyped(KeyEvent e){}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_SPACE) {
            espacioPresionado = true;
        }

        if (code == KeyEvent.VK_R){
            resolverPresionado = true;
        }

        if (code == KeyEvent.VK_ESCAPE) {
            escapePresionado = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_SPACE){
            espacioPresionado = false;
        }

        if (code == KeyEvent.VK_R) {
            resolverPresionado = false;
        }

        if (code == KeyEvent.VK_ESCAPE) {
            escapePresionado = false;
        }
    }

    
}
