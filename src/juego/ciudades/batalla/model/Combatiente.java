package juego.ciudades.batalla.model;

import estructuras.cola.Cola;

import java.util.HashMap;
import java.util.Map;

public abstract class Combatiente {
//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	private final String nombre;
	private int vida;
	private int fuerza;
	private int armadura;
	private final HabilidadEspecial habilidad;
	private final Map<EstadoCombatiente, EstadoActivo> estados;

//CONSTRUCTORES -------------------------------------------------------------------------------------------
	public Combatiente(
			String nombre,
			int vida,
			int fuerza,
			int armadura,
			HabilidadEspecial habilidad
	) {
		this.nombre = nombre;
		setVida(vida);
		setFuerza(fuerza);
		setArmadura(armadura);
		this.habilidad = habilidad;
		this.estados = new HashMap<>();
	}

//GETTERS SIMPLES -----------------------------------------------------------------------------------------
	public int getVida() { return vida; }

	public int getFuerza() { return fuerza; }

	public int getArmadura() { return armadura; }

	public String getNombre() { return nombre; }

	public Map<EstadoCombatiente, EstadoActivo> getEstados() { return estados; }

	//SETTERS SIMPLES -----------------------------------------------------------------------------------------
	public void setVida(int vida) {
		this.vida = vida;
	}

	public void setFuerza(int fuerza) {
		this.fuerza = fuerza;
	}

	public void setArmadura(int armadura) {
		this.armadura = armadura;
	}

	public void setEstado(EstadoActivo estado) {
		if (!this.estados.containsKey(estado.getEstado())) {
			this.estados.put(estado.getEstado(), estado);
			return;
		}
		this.estados.get(estado.getEstado()).apilar(estado);
	}

//METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
	public void usarHabilidadEspecial(Combatiente enemigo) {
		this.habilidad.activar(this, enemigo);
	}
//METODOS DE CLASE ----------------------------------------------------------------------------------------

//METODOS GENERALES ---------------------------------------------------------------------------------------
//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
	@Override
	public String toString() {
		return String.format("%s | ❤️ %d, 🗡 %d, 🛡 %d |",
				this.getNombre(),
				this.getVida(),
				this.getFuerza(),
				this.getArmadura()
		);
	}

//METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
	public boolean estaVivo() {
		return this.vida > 0;
	}

	public boolean estaDefendiendo() {
		return
				estados.containsKey(EstadoCombatiente.DEFENDIENDO) &&
				!estados.get(EstadoCombatiente.DEFENDIENDO).terminado();
	}

	public void defendido() {
		estados.get(EstadoCombatiente.DEFENDIENDO).defendido();
	}
}
