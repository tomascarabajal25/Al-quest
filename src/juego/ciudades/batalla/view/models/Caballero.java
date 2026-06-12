package juego.ciudades.batalla.view.models;

public class Caballero extends Enemy {
	private boolean ironDefenseActive = false;

	public Caballero() {
		super("CABALLERO", 160, 35, 80, 25, "/juego/ciudades/batalla/view/sprites/caballero.png");
	}

	@Override
	public String useSpecialMove(Enemy target) {
		this.defense += 30; // Direct stat buff
		return name + " raised their massive Tower Shield! Defense rose significantly!";
	}
}
