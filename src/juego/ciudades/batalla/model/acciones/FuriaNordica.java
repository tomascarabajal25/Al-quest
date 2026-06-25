package juego.ciudades.batalla.model.acciones;

import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.model.estados.Potenciado;
import juego.ciudades.batalla.view.accion.ActionUi;
import juego.ciudades.batalla.view.accion.HabilidadUi;

public class FuriaNordica extends Accion {

	public FuriaNordica(Combatiente combatiente) {
		super(combatiente, combatiente, TipoAccion.HABILIDAD_ESPECIAL);
	}

	@Override
	public void ejecutar() {
		combatiente.setEstado(new Potenciado(combatiente));
	}

	@Override
	public ActionUi getUi() {
		return new HabilidadUi(combatiente.getNombre(), "Furia Nórdica");
	}
}
