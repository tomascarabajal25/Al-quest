package juego.ciudades.batalla.view.accion;

import juego.ciudades.batalla.model.Combatiente;
import juego.ciudades.batalla.view.animacion.Animacion;
import juego.ciudades.batalla.view.animacion.AnimacionCompuesta;
import juego.ciudades.batalla.view.animacion.FlashAnimacion;
import juego.ciudades.batalla.view.animacion.ShakeAnimacion;

public class HabilidadUi implements ActionUi {
	private final String nombre;
	private final String nombreHabilidad;

	public HabilidadUi(String nombre, String nombreHabilidad) {
		this.nombre = nombre;
		this.nombreHabilidad = nombreHabilidad;
	}

	@Override
	public String getMensaje() {
		return nombre + " usa " + nombreHabilidad + "!";
	}

	@Override
	public Animacion crearAnimacion(Combatiente actor, Combatiente objetivo) {
		return new AnimacionCompuesta(
			new FlashAnimacion(),
			new ShakeAnimacion(false)
		);
	}
}
