package juego.ciudades.batalla.view.models;

public class Vikingo extends Enemy {
	public Vikingo(int dificultad) {
		super("VIKINGO", 110, 55, 45, 60, "/juego/ciudades/batalla/view/sprites/vikingo" + dificultad + ".png");
	}

	@Override
	public String useSpecialMove(Enemy target) {
		int damage = (int)(this.attack * 2.5); // High critical damage
		target.takeDamage(damage);
		return name + " unleashed an unsheathed IAIDO SLASH dealing " + damage + " critical damage!";
	}
}
