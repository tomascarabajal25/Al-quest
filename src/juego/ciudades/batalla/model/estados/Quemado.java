package juego.ciudades.batalla.model.estados;

import juego.ciudades.batalla.model.Combatiente;
import juego.ciudades.batalla.model.EstadoActivo;
import juego.ciudades.batalla.model.EstadoCombatiente;
import juego.ciudades.batalla.view.estado.StateUi;
import juego.ciudades.batalla.view.estado.QuemadoUi;

public class Quemado extends EstadoActivo {
	private static final int DANIO_POR_TURNO = 5;

	public Quemado(Combatiente combatiente) {
		super(EstadoCombatiente.QUEMADO, combatiente, combatiente, 2);
	}

	@Override
	public void aplicar() {
		Combatiente destino = getDestino();
		destino.setVida(Math.max(0, destino.getVida() - DANIO_POR_TURNO));
	}

	@Override
	public StateUi getUi() {
		return new QuemadoUi();
	}
}
