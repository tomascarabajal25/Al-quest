package com.aiquest.juego.ciudades.batalla.view.models.enemies;

import java.util.Random;

public class Bufon extends Enemy {
	private Random random = new Random();

	public Bufon() {
		super("BUFON", 95, 40, 35, 75, "/com/aiquest/juego/ciudades/batalla/view/sprites/bufon.png");
	}

	@Override
	public String useSpecialMove(Enemy target) {
		// Random effect / Wildcard mechanics
		int choice = random.nextInt(2);
		if (choice == 0) {
			int damage = random.nextInt(60) + 10;
			target.takeDamage(damage);
			return name + " threw exploding wild daggers dealing " + damage + " damage!";
		} else {
			this.hp += 30;
			if (this.hp > maxHp) this.hp = maxHp;
			return name + " cackled wildly and tricked the opponent, recovering 30 HP!";
		}
	}
}
