package juego.ciudades.batalla.model.estados;

import juego.ciudades.batalla.model.Combatiente;
import juego.ciudades.batalla.model.EstadoActivo;
import juego.ciudades.batalla.model.EstadoCombatiente;
import juego.ciudades.batalla.view.estado.StateUi;
import juego.ciudades.batalla.view.estado.SangrandoUi;

public class Sangrando extends EstadoActivo {
	private static final int DANIO_POR_TURNO = 2;

	public Sangrando(Combatiente combatiente) {
		super(EstadoCombatiente.SANGRANDO, combatiente, combatiente, 4);
	}

	@Override
	public void aplicar() {
		Combatiente destino = getDestino();
		destino.setVida(Math.max(0, destino.getVida() - DANIO_POR_TURNO));
	}

	@Override
	public StateUi getUi() {
		return new SangrandoUi();
	}
}
