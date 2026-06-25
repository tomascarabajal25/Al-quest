package juego.ciudades.batalla.view.estado;

import java.awt.Color;
import juego.ciudades.batalla.model.EstadoActivo;

public class QuemadoUi implements StateUi {
	@Override
	public String getBadgeText() { return "QMD"; }

	@Override
	public Color getBadgeColor() { return new Color(255, 140, 0); }

	@Override
	public String getDescripcion(EstadoActivo estado) {
		return estado.getOrigen().getNombre() + " está quemado! (-5 ❤️)";
	}
}
