package juego.ciudades.batalla.model;

import juego.ciudades.batalla.view.estado.StateUi;

public abstract class EstadoActivo {

	public abstract StateUi getUi();
	private final EstadoCombatiente estado;
	int turnosRestantes;
	private final Combatiente origen;
	private final Combatiente destino;

	public EstadoActivo(EstadoCombatiente estado, Combatiente origen, Combatiente destino,  int turnosRestantes) {
		this.estado = estado;
		this.origen = origen;
		this.destino = destino;
		this.turnosRestantes = turnosRestantes;
	}

	public Combatiente getOrigen() {
		return this.origen;
	}

	public Combatiente getDestino() {
		return this.destino;
	}

	public EstadoCombatiente getEstado() {
		return this.estado;
	}

	public int getTurnosRestantes() {
		return this.turnosRestantes;
	}

	public void aplicar() {};

	public void apilar(EstadoActivo nuevoEstado) {
		this.turnosRestantes += nuevoEstado.getTurnosRestantes();
	}

	public void defendido() {}

	public void usado() {
		this.turnosRestantes--;
	}

	public boolean terminado() {
		return this.turnosRestantes <= 0;
	}
}
