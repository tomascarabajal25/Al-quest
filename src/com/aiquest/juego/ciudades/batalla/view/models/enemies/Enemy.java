package com.aiquest.juego.ciudades.batalla.view.models.enemies;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class Enemy {
	protected String name;
	protected int hp;
	protected int maxHp;
	protected int attack;
	protected int defense;
	protected int speed;
	protected BufferedImage sprite;

	public Enemy(String name, int maxHp, int attack, int defense, int speed, String spritePath) {
		this.name = name;
		this.maxHp = maxHp;
		this.hp = maxHp;
		this.attack = attack;
		this.defense = defense;
		this.speed = speed;
		this.sprite = loadSprite(spritePath);
	}

	private BufferedImage loadSprite(String path) {
		try {
			var stream = getClass().getResourceAsStream(path);
			if (stream == null) {
				System.err.println("Could not load sprite for " + name + " at " + path + ". Creating a blank placeholder.");
				return createBlankPlaceholder();
			}
			return ImageIO.read(stream);
		} catch (IOException e) {
			System.err.println("Could not load sprite for " + name + " at " + path + ". Creating a blank placeholder.");
			return createBlankPlaceholder();
		}
	}

	private BufferedImage createBlankPlaceholder() {
		return new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
	}

	// Nearest-neighbor scaling rendering for sharp pixel art
	public void draw(Graphics2D g2d, int x, int y, int scale) {
		if (sprite != null) {
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			int scaledSize = 64 * scale;
			g2d.drawImage(sprite, x, y, scaledSize, scaledSize, null);
		}
	}

	public abstract String useSpecialMove(Enemy target);

	public void takeDamage(int rawDamage) {
		int damageCalculated = rawDamage - (this.defense / 2);
		if (damageCalculated < 1) damageCalculated = 1;
		this.hp -= damageCalculated;
		if (this.hp < 0) this.hp = 0;
	}

	public boolean isFainted() { return this.hp <= 0; }
	public String getName() { return name; }
	public int getHp() { return hp; }
	public int getMaxHp() { return maxHp; }
	public int getAttack() { return attack; }
	public int getDefense() { return defense; }
	public int getSpeed() { return speed; }
	public void setHp(int hp) { this.hp = Math.max(0, hp); }
	public void setMaxHp(int maxHp) { this.maxHp = maxHp; }
}
