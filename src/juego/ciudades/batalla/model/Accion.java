package juego.ciudades.batalla.model;

public abstract class Accion {
	private Combatiente combatiente;
	private Combatiente objetivo;
	private TipoAccion tipo;

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
}
