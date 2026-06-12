package juego.ciudades.batalla.view.models;

public class Vikingo extends Enemy {
	public Vikingo() {
		super("VIKINGO", 140, 65, 30, 40, "/juego/ciudades/batalla/view/sprites/vikingo.png");
	}

	@Override
	public String useSpecialMove(Enemy target) {
		// High risk, high reward mechanic
		int damage = this.attack * 3;
		target.takeDamage(damage);
		this.hp -= 15; // Recoil damage
		return name + " went into a BERSERK RAGE! Hits for " + damage + " damage, but took 15 recoil damage!";
	}
}