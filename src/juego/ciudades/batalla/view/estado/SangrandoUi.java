package juego.ciudades.batalla.view.estado;

import java.awt.Color;
import juego.ciudades.batalla.model.EstadoActivo;

public class SangrandoUi implements StateUi {
	@Override
	public String getBadgeText() { return "SND"; }

	@Override
	public Color getBadgeColor() { return new Color(139, 0, 0); }

	@Override
	public String getDescripcion(EstadoActivo estado) {
		return estado.getOrigen().getNombre() + " está sangrando! (-2 ❤️)";
	}
}
