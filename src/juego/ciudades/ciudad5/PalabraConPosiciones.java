package juego.ciudades.ciudad5;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import utils.ValidacionesUtiles;

public class PalabraConPosiciones implements Comparable<PalabraConPosiciones> {
	//atributos--------------------------------------------------
	private String palabra;
	private List<Posicion> posiciones;
	
	//constructores--------------------------------------------------
	/**
	 * pre:
	 * @param palabra no nula
	 * @param fila mayor o igual a 0
	 * @param columna mayor o igual a 0
	 * post:
	 * se crea la palabra con posicion
	 */
	public PalabraConPosiciones(String palabra, int fila, int columna) {
		setPalabra(palabra);
		setPosiciones(new ArrayList<Posicion>());
		agregarPosicion(fila, columna);
	}
	// Metodos generales--------------------------------------------------
	@Override
	public int compareTo(PalabraConPosiciones otra) {
		return this.palabra.compareTo(otra.palabra);
	}

	@Override
	public int hashCode() {
		return Objects.hash(palabra);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PalabraConPosiciones other = (PalabraConPosiciones) obj;
		return Objects.equals(palabra, other.palabra);
	}

	@Override
	public String toString() {
		return "PalabraConPosiciones [palabra=" + palabra + ", posiciones=" + posiciones + "]";
	}
	//metodos--------------------------------------------------
	/**
	 * Pre:
	 * @param fila mayor o igual a 0
	 * @param columna mayor o igual a 0
	 * post:
	 * se agrega una posicion a la palabra
	 */
	public void agregarPosicion(int fila, int columna){
		posiciones.add(new Posicion(fila, columna));
	}
	//getters--------------------------------------------------
	// devuelve las posiciones de la palabra
	public List<Posicion> getPosiciones() {
		return new ArrayList<Posicion>(posiciones);
	}
	//devuelve la palabra
	public String getPalabra() {
		return palabra;
	}
	
	
	//setters--------------------------------------------------
	/**
	 * pre:
	 * @param posiciones2 distinto de null
	 * post:
	 * modifica la lista de posiciones
	 */
	private void setPosiciones(List<Posicion> posiciones2) {
		ValidacionesUtiles.esDistintoDeNull(posiciones2, "posiciones no puede ser nulo");
		posiciones=posiciones2;
	}
	
	/**
	 * pre:
	 * @param palabra2 distinto de null y con largo mayor a 0
	 * post:
	 * modifica la palabra
	 */
	private void setPalabra(String palabra2) {
		ValidacionesUtiles.esDistintoDeNull(palabra2, "la palabra no puede ser nula");
		ValidacionesUtiles.validarMayorACero(palabra2.length(), "largo de palabra invalido");
		palabra=palabra2;
	}

	

	
}

