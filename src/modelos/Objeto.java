package modelos;



public class Objeto extends Entidad {
	private boolean colision=false;
	
	
	public Objeto(String nombre, boolean colision) {
		super(nombre);
		setColision(colision);
		
	}
	
	
	public boolean isColision() {
		return colision;
	}
	
	private void setColision(boolean colision) {
		this.colision = colision;
	}
	


}
