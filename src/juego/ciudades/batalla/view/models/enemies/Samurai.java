package juego.ciudades.batalla.view.models.enemies;

public class Samurai extends Enemy {
	public Samurai() {
		super("SAMURAI", 110, 55, 45, 60, "/com/aiquest/juego/ciudades/batalla/view/sprites/samurai.png");
	}

	@Override
	public String useSpecialMove(Enemy target) {
		int damage = (int)(this.attack * 2.5); // High critical damage
		target.takeDamage(damage);
		return name + " unleashed an unsheathed IAIDO SLASH dealing " + damage + " critical damage!";
	}
}
