package juego.ciudades.batalla.model.acciones;

import juego.ciudades.batalla.model.Accion;
import juego.ciudades.batalla.model.Combatiente;
import juego.ciudades.batalla.model.EstadoActivo;
import juego.ciudades.batalla.model.TipoAccion;
import juego.ciudades.batalla.model.estados.Defendiendo;
import juego.ciudades.batalla.view.accion.ActionUi;
import juego.ciudades.batalla.view.accion.DefenderUi;

public class Defender extends Accion {

	public Defender(Combatiente combatiente, Combatiente objetivo) {
		super(combatiente, objetivo, TipoAccion.DEFENSA);
	}

	@Override
	public ActionUi getUi() {
		return new DefenderUi(combatiente.getNombre());
	}

	@Override
	public void ejecutar() {
		Combatiente combatiente = getCombatiente();
		EstadoActivo defensa = new Defendiendo(combatiente);
		combatiente.setEstado(defensa);
		System.out.println(combatiente.getNombre() + " se defiende!");
	}
}
