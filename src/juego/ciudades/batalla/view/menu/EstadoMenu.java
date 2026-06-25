package juego.ciudades.batalla.view.menu;

import java.awt.Graphics2D;
import juego.ciudades.batalla.model.Accion;

public interface EstadoMenu {
    void dibujar(Graphics2D g, int w, int h);
    Accion onClick(int mx, int my);
    void onEnter();
    void onExit();
}
