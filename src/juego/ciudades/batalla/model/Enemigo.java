package juego.ciudades.batalla.model;

public class Enemigo extends Combatiente {
	//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
	private final TipoEnemigo tipo;

	//CONSTRUCTORES -------------------------------------------------------------------------------------------
	public Enemigo(
			String nombre,
			TipoEnemigo tipo,
			int vida,
			int fuerza,
			int armadura
	) {
		super(nombre, vida, fuerza, armadura);
		this.tipo = tipo;
	}

	//GETTERS SIMPLES -----------------------------------------------------------------------------------------
	public TipoEnemigo getTipo() {
		return tipo;
	}
}
