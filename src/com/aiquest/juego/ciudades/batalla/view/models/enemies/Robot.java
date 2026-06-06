package com.aiquest.juego.ciudades.batalla.view.models.enemies;

public class Robot extends Enemy {
	private boolean isCharged = false;

	public Robot() {
		super("ROBOT", 120, 50, 60, 35, "/com/aiquest/juego/ciudades/batalla/view/sprites/robot.png");
	}

	@Override
	public String useSpecialMove(Enemy target) {
		if (!isCharged) {
			isCharged = true;
			return name + " is charging up its arm cannon! Mega Buster power maximizing...";
		} else {
			isCharged = false;
			int damage = this.attack * 4; // Devastating move that requires a turn to setup
			target.takeDamage(damage);
			return name + " fired its fully charged MEGA BUSTER for " + damage + " absolute damage!";
		}
	}
}