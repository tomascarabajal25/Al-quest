package juego.ciudades.batalla.model.acciones;

import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.model.estados.Quemado;
import juego.ciudades.batalla.view.accion.ActionUi;
import juego.ciudades.batalla.view.accion.HabilidadUi;

public class Sobrecarga extends Accion {

	public Sobrecarga(Combatiente combatiente, Combatiente objetivo) {
		super(combatiente, objetivo, TipoAccion.HABILIDAD_ESPECIAL);
	}

	@Override
	public void ejecutar() {
		objetivo.setEstado(new Quemado(objetivo));
	}

	@Override
	public ActionUi getUi() {
		return new HabilidadUi(combatiente.getNombre(), "Sobrecarga");
	}
}
