package juego.ciudades.batalla.model;

import juego.ciudades.batalla.view.accion.ActionUi;

public abstract class Accion {
	protected Combatiente combatiente;
	protected Combatiente objetivo;
	protected TipoAccion tipo;

	public Accion(Combatiente jugador, Combatiente objetivo, TipoAccion tipo) {
		this.combatiente = jugador;
		this.objetivo = objetivo;
		this.tipo = tipo;
	}

	public TipoAccion getTipo() { return this.tipo; }

	public Combatiente getCombatiente() {
		return combatiente;
	}

	public Combatiente getObjetivo() {
		return objetivo;
	}

	public abstract void ejecutar();

	public abstract ActionUi getUi();
}
