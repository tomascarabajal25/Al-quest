package modelosVista;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import modelos.Objeto;

public class ObjetoVista extends Objeto {
	private BufferedImage imagen;
	private int worldX;
	private int worldY;
	
	public ObjetoVista(int x, int y,String nombre, boolean colision, BufferedImage imagen) {
		super(nombre, colision);
		setWorldX(x);
		setWorldY(y);
		setImagen(imagen);
	}
	
	public void draw(Graphics2D g2, Vista vista) {
        if (!estaEnPantalla(vista)) {
        	return;
        }
        if (imagen == null) {
        	return;
        }
 
        g2.drawImage(imagen,
                getScreenX(vista), getScreenY(vista),
                vista.tamaño, vista.tamaño, null);
    }
	/**
     * post: devuelve true si el objeto está dentro del área visible
     */
    protected boolean estaEnPantalla(Vista vista) {
        int jx = vista.jugadorVista.getWorldX();
        int jy = vista.jugadorVista.getWorldY();
        int sx = vista.jugadorVista.getScreenX();
        int sy = vista.jugadorVista.getScreenY();
 
        return worldX + vista.tamaño > jx - sx &&
               worldX - vista.tamaño < jx + sx &&
               worldY + vista.tamaño > jy - sy &&
               worldY - vista.tamaño < jy + sy;
    }
 
	
	
	public BufferedImage getImagen() {
		return imagen;
	}
	

	public int getWorldX() {
		return worldX;
	}
	public int getWorldY() {
		return worldY;
	}
	/**
     * post: posición X en pantalla relativa al jugador
     */
    protected int getScreenX(Vista vista) {
        return worldX - vista.jugadorVista.getWorldX()
                      + vista.jugadorVista.getScreenX();
    }
 
    /**
     * post: posición Y en pantalla relativa al jugador
     */
    protected int getScreenY(Vista vista) {
        return worldY - vista.jugadorVista.getWorldY()
                      + vista.jugadorVista.getScreenY();
    }
	public void setWorldY(int worldY) {
		this.worldY = worldY;
	}
	public void setWorldX(int worldX) {
		this.worldX = worldX;
	}

	protected void setImagen(BufferedImage imagen) {
		this.imagen = imagen;
	}

}
