package juego.ciudades.batalla.model.acciones;

import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.model.estados.Sangrando;
import juego.ciudades.batalla.view.accion.ActionUi;
import juego.ciudades.batalla.view.accion.HabilidadUi;

public class TrucoSucio extends Accion {

	public TrucoSucio(Combatiente combatiente, Combatiente objetivo) {
		super(combatiente, objetivo, TipoAccion.HABILIDAD_ESPECIAL);
	}

	@Override
	public void ejecutar() {
		objetivo.setEstado(new Sangrando(objetivo));
	}

	@Override
	public ActionUi getUi() {
		return new HabilidadUi(combatiente.getNombre(), "Truco Sucio");
	}
}
