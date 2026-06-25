package juego.ciudades.batalla.model.acciones;

import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.view.accion.ActionUi;
import juego.ciudades.batalla.view.accion.HabilidadUi;

public class RoboDeVida extends Accion {

	public RoboDeVida(Combatiente combatiente, Combatiente objetivo) {
		super(combatiente, objetivo, TipoAccion.HABILIDAD_ESPECIAL);
	}

	@Override
	public void ejecutar() {
		objetivo.setVida(Math.max(0, objetivo.getVida() - 4));
		combatiente.setVida(combatiente.getVida() + 4);
	}

	@Override
	public ActionUi getUi() {
		return new HabilidadUi(combatiente.getNombre(), "Robo de Vida");
	}
}
