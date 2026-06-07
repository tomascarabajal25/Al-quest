package modelos;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class EntidadVista extends Entidad {
	public EntidadVista(String nombre) {
		super(nombre);
		
	}

	private BufferedImage up1;
	private BufferedImage up2;
	private BufferedImage down1;
	private BufferedImage down2;
	private BufferedImage right1;
	private BufferedImage right2;
	private BufferedImage left1;
	private BufferedImage left2;
	private int worldX;
	private int worldY;
	private int velocidad=4;
	private int spriteNum= 1;
	private int spriteCounter= 0;
	private Rectangle areaSolida;
	private boolean colisionOn=false;
	private Direccion direccion;
	public BufferedImage getUp1() {
		return up1;
	}
	
	public BufferedImage getUp2() {
		return up2;
	}
	
	public BufferedImage getDown1() {
		return down1;
	}
	
	public BufferedImage getDown2() {
		return down2;
	}
	
	public BufferedImage getRight1() {
		return right1;
	}
	
	public BufferedImage getRight2() {
		return right2;
	}
	
	public BufferedImage getLeft1() {
		return left1;
	}
	
	public BufferedImage getLeft2() {
		return left2;
	}
	
	public int getWorldX() {
		return worldX;
	}
	
	public int getWorldY() {
		return worldY;
	}
	
	public int getVelocidad() {
		return velocidad;
	}
	
	public int getSpriteNum() {
		return spriteNum;
	}
	
	public int getSpriteCounter() {
		return spriteCounter;
	}
	
	public Rectangle getAreaSolida() {
		return areaSolida;
	}
	
	public boolean isColisionOn() {
		return colisionOn;
	}
	
	public Direccion getDireccion() {
		return direccion;
	}
	
	
	public void setUp1(BufferedImage up1) {
		this.up1 = up1;
	}
	public void setUp2(BufferedImage up2) {
		this.up2 = up2;
	}
	public void setRight1(BufferedImage right1) {
		this.right1 = right1;
	}
	public void setDown1(BufferedImage down1) {
		this.down1 = down1;
	}
	public void setDown2(BufferedImage down2) {
		this.down2 = down2;
	}
	public void setRight2(BufferedImage right2) {
		this.right2 = right2;
	}
	
	public void setLeft1(BufferedImage left1) {
		this.left1 = left1;
	}
	public void setLeft2(BufferedImage left2) {
		this.left2 = left2;
	}
	public void setWorldX(int worldX) {
		this.worldX = worldX;
	}
	public void setVelocidad(int velocidad) {
		this.velocidad = velocidad;
	}
	public void setWorldY(int worldY) {
		this.worldY = worldY;
	}
	public void setSpriteNum(int spriteNum) {
		this.spriteNum = spriteNum;
	}
	public void setSpriteCounter(int spriteCounter) {
		this.spriteCounter = spriteCounter;
	}
	public void setAreaSolida(Rectangle areaSolida) {
		this.areaSolida = areaSolida;
	}
	
	public void setColisionOn(boolean colisionOn) {
		this.colisionOn = colisionOn;
	}
	
	public void setDireccion(Direccion direccion) {
		this.direccion = direccion;
	}
	
	
	
	
	
	
	
}
