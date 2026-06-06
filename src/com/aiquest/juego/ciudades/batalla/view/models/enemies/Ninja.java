package com.aiquest.juego.ciudades.batalla.view.models.enemies;

public class Ninja extends Enemy {
	public Ninja() {
		// Name, HP, Atk, Def, Spd, SpritePath
		super("NINJA", 80, 45, 20, 95, "/com/aiquest/juego/ciudades/batalla/view/sprites/ninja.png");
	}

	@Override
	public String useSpecialMove(Enemy target) {
		// In a true implementation, this could flag an evasion buff or strike twice
		int damage = this.attack * 2;
		target.takeDamage(damage);
		return name + " vanished into the shadows and used Shadow Strike for " + damage + " damage!";
	}
}
