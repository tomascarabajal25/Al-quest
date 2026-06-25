package juego.ciudades.batalla.model.acciones;

import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.model.estados.Defendiendo;
import juego.ciudades.batalla.view.accion.ActionUi;
import juego.ciudades.batalla.view.accion.HabilidadUi;

public class EscudoSagrado extends Accion {

	public EscudoSagrado(Combatiente combatiente) {
		super(combatiente, combatiente, TipoAccion.HABILIDAD_ESPECIAL);
	}

	@Override
	public void ejecutar() {
		combatiente.setEstado(new Defendiendo(combatiente, 2));
	}

	@Override
	public ActionUi getUi() {
		return new HabilidadUi(combatiente.getNombre(), "Escudo Sagrado");
	}
}
