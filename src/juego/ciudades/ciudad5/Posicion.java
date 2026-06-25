package juego.ciudades.ciudad5;

import java.util.Objects;

import utils.ValidacionesUtiles;

public class Posicion {
	//atributos----------------------------------------------------------------------
	private int linea;
	private int indice;
	
	//constructor------------------------------------------------------------------------
	/*
	 * Pre:
	 * linea y columna enteros mayor o igual a 0
	 * post:
	 * crea la posicion 
	 */
	public Posicion(int linea, int columna){
		setLinea(linea);
		setColumna(columna);
	}
	// metodos generales----------------------------------------------------------------------
	@Override
	public int hashCode() {
		return Objects.hash(indice, linea);
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Posicion other = (Posicion) obj;
		return indice == other.indice && linea == other.linea;
	}

	@Override
	public String toString() {
		return "Posicion [linea=" + linea + ", columna=" + indice + "]";
	}
	//getters-------------------------------------------------------------------------
	//devuelve la linea de la posicion
	public int getLinea() {
		return linea;
	}

	//devuelve la columna de la posicion
	public int getIndice() {
		return indice;
	}
	
	//setters-------------------------------------------------------------------------
	/*
	 * Pre: 
	 * el numero debe ser mayor o igual a 0
	 * Post:
	 * modifica la columna de la posicion
	 */
	private void setColumna(int columna2) {
		ValidacionesUtiles.validarMayorOIgualACero(columna2, "la columna no puede ser negativa");
		indice=columna2;
	}
	/*
	 * Pre: 
	 * el numero debe ser mayor o igual a 0
	 * Post:
	 * modifica la linea de la posicion
	 */
	private void setLinea(int linea2) {
		ValidacionesUtiles.validarMayorOIgualACero(linea2, "la linea debe no puede ser negativa");
		linea=linea2;
	}

}
