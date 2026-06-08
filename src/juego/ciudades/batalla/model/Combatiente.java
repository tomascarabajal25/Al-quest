package juego.ciudades.batalla.model;

public abstract class Combatiente {
//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	private final String nombre;
	private int vida;
	private int fuerza;
	private int armadura;
	private final HabilidadEspecial habilidad;

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
	}

//GETTERS SIMPLES -----------------------------------------------------------------------------------------
	public int getVida() { return vida; }

	public int getFuerza() { return fuerza; }

	public int getArmadura() { return armadura; }

	public String getNombre() { return nombre; }

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
}
