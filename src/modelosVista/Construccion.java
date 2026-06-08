package modelosVista;

import java.awt.image.BufferedImage;



public class Construccion  {
	private BufferedImage imagen;
	//proximo enum
	private boolean colision=false;
	
	
	public BufferedImage getImagen() {
		return imagen;
	}
	protected void setImagen(BufferedImage imagen) {
		this.imagen = imagen;
	}
	public boolean isColision() {
		return colision;
	}
	protected void setColision(boolean colision) {
		this.colision = colision;
	}
	protected boolean getColision() {
		return colision;
	}
	
	
	
}
