package juego.ciudades.batalla.model.estados;

import juego.ciudades.batalla.model.Combatiente;
import juego.ciudades.batalla.model.EstadoActivo;
import juego.ciudades.batalla.model.EstadoCombatiente;
import juego.ciudades.batalla.view.estado.StateUi;
import juego.ciudades.batalla.view.estado.PotenciadoUi;

public class Potenciado extends EstadoActivo {
	private final int fuerzaOriginal;

	public Potenciado(Combatiente combatiente) {
		super(EstadoCombatiente.POTENCIADO, combatiente, combatiente, 2);
		this.fuerzaOriginal = combatiente.getFuerza();
		combatiente.setFuerza((int) (fuerzaOriginal * 1.5));
	}

	@Override
	public void remover() {
		getDestino().setFuerza(fuerzaOriginal);
	}

	@Override
	public StateUi getUi() {
		return new PotenciadoUi();
	}
}
