package juego.ciudades.batalla.model.acciones;

import juego.ciudades.batalla.model.Accion;
import juego.ciudades.batalla.model.Combatiente;
import juego.ciudades.batalla.model.TipoAccion;
import juego.ciudades.batalla.view.accion.ActionUi;
import juego.ciudades.batalla.view.accion.HabilidadUi;

import java.util.function.BiConsumer;

public class HabilidadAccion extends Accion {
	private final String nombre;
	private final BiConsumer<Combatiente, Combatiente> efecto;

	public HabilidadAccion(Combatiente actor, Combatiente objetivo,
	                       String nombre, BiConsumer<Combatiente, Combatiente> efecto) {
		super(actor, objetivo, TipoAccion.HABILIDAD_ESPECIAL);
		this.nombre = nombre;
		this.efecto = efecto;
	}

	@Override
	public void ejecutar() {
		efecto.accept(combatiente, objetivo);
	}

	@Override
	public ActionUi getUi() {
		return new HabilidadUi(combatiente.getNombre(), nombre);
	}
}
