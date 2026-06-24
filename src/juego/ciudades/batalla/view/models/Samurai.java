package juego.ciudades.batalla.view.models;

public class Samurai extends Enemy {
	public Samurai(int dificultad) {
		super("SAMURAI", 110, 55, 45, 60, "/juego/ciudades/batalla/view/sprites/samurai" + dificultad + ".png");
	}

	@Override
	public String useSpecialMove(Enemy target) {
		int damage = (int)(this.attack * 2.5); // High critical damage
		target.takeDamage(damage);
		return name + " unleashed an unsheathed IAIDO SLASH dealing " + damage + " critical damage!";
	}
}
