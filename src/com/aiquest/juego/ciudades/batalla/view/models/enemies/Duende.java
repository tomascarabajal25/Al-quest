package com.aiquest.juego.ciudades.batalla.view.models.enemies;

public class Duende extends Enemy {
	public Duende() {
		super("DUENDE", 70, 30, 25, 85, "/com/aiquest/juego/ciudades/batalla/view/sprites/duende.png");
	}

	@Override
	public String useSpecialMove(Enemy target) {
		// Saps health from target
		int damage = this.attack + 15;
		target.takeDamage(damage);
		this.hp += (damage / 2);
		if (this.hp > maxHp) this.hp = maxHp;
		return name + " used Gold Pilfer! Dealt " + damage + " damage and drained health!";
	}
}