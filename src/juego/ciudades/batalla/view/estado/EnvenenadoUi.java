package juego.ciudades.batalla.view.estado;

import java.awt.Color;
import juego.ciudades.batalla.model.EstadoActivo;

public class EnvenenadoUi implements StateUi {
	@Override
	public String getBadgeText() { return "PSN"; }

	@Override
	public Color getBadgeColor() { return new Color(160, 50, 200); }

	@Override
	public String getDescripcion(EstadoActivo estado) {
		return estado.getOrigen().getNombre() + " está envenenado! (-2 ❤️)";
	}
}
