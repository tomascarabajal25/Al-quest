package ordenamientos;

import java.util.Objects;

/*
 * Clase Caja, la cual va a ser ordenada para el juego de memoria
 * atributos:ancho, profundo y alto
 */
public class Caja {
	private double ancho;
	private double alto;
	private double profundo;
	//CONSTRUCTORES-----------------------------------------------------------------
	public Caja(double ancho, double alto, double profundo) {
		super();
		this.setAncho(ancho); 
		this.setAlto(alto); 
		this.setProfundo(profundo); 
	}
	//METODOS DE COMPORTAMIENTO------------------------------------------------------
	
	//METODOS DE CLASES-------------------------------------------------------------
	//METODOS GENERALES------------------------------------------------------------
	@Override
	public String toString() {
		return "Caja [ancho=" + ancho + ", alto=" + alto + ", profundo=" + profundo + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(alto, ancho, profundo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Caja other = (Caja) obj;
		return Double.doubleToLongBits(alto) == Double.doubleToLongBits(other.alto)
				&& Double.doubleToLongBits(ancho) == Double.doubleToLongBits(other.ancho)
				&& Double.doubleToLongBits(profundo) == Double.doubleToLongBits(other.profundo);
	}

	//GETTER SIMPLES-----------------------------------------------------------------
	public double getVolumen(){
		return this.getAncho()*this.getAlto()*this.getProfundo();
	}

	public double getAncho() {
		return ancho;
	}

	public double getAlto() {
		return alto;
	}

	public double getProfundo() {
		return profundo;
	}
	
	//SETTERS SIMPLES---------------------------------------------------------------
	private void setProfundo(double profundo) {
		this.profundo = profundo;
	}
	
	private void setAncho(double ancho) {
		this.ancho = ancho;
	}
	
	private void setAlto(double alto) {
		this.alto = alto;
	}
}
