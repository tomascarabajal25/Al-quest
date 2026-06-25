package juego.ciudades.batalla.view.estado;

import java.awt.Color;
import juego.ciudades.batalla.model.EstadoActivo;

public class PotenciadoUi implements StateUi {
	@Override
	public String getBadgeText() { return "POT"; }

	@Override
	public Color getBadgeColor() { return new Color(255, 215, 0); }

	@Override
	public String getDescripcion(EstadoActivo estado) {
		return estado.getOrigen().getNombre() + " está potenciado! (+50% fuerza)";
	}
}
