package juego.ciudades.batalla.model.acciones;

import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.view.accion.ActionUi;
import juego.ciudades.batalla.view.accion.HabilidadUi;

public class CortePreciso extends Accion {
	private int danio;

	public CortePreciso(Combatiente combatiente, Combatiente objetivo) {
		super(combatiente, objetivo, TipoAccion.HABILIDAD_ESPECIAL);
	}

	@Override
	public void ejecutar() {
		danio = combatiente.getFuerza();
		objetivo.setVida(Math.max(0, objetivo.getVida() - danio));
	}

	@Override
	public ActionUi getUi() {
		return new HabilidadUi(combatiente.getNombre(), "Corte Preciso");
	}
}
