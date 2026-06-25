package juego.ciudades.batalla.model.estados;

import juego.ciudades.batalla.model.Combatiente;
import juego.ciudades.batalla.model.EstadoActivo;
import juego.ciudades.batalla.model.EstadoCombatiente;
import juego.ciudades.batalla.view.estado.StateUi;
import juego.ciudades.batalla.view.estado.DefendiendoUi;

public class Defendiendo extends EstadoActivo {
	public Defendiendo(Combatiente combatiente) {
		super(EstadoCombatiente.DEFENDIENDO, combatiente, combatiente, 1);
	}

	public Defendiendo(Combatiente combatiente, int turnos) {
		super(EstadoCombatiente.DEFENDIENDO, combatiente, combatiente, turnos);
	}

	@Override
	public StateUi getUi() {
		return new DefendiendoUi();
	}

	public void defendido() {
		this.usado();
	}
}
