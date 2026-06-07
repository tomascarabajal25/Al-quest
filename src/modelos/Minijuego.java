package modelos;

import java.awt.Graphics2D;

public interface Minijuego {

	void actualizar(JugadorVista jugadorVista);

	void draw(Graphics2D g2, JugadorVista jugadorVista);

}
