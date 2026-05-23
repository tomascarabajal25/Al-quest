package material.estructuras.vector;

import java.util.Iterator;
import java.util.NoSuchElementException;

import material.utiles.ValidacionesUtiles;

public class Vector<T> implements Iterable<T> {
//ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
//ATRIBUTOS -----------------------------------------------------------------------------------------------

	private T[] datos = null;
	private T datoInicial;

//CONSTRUCTORES -------------------------------------------------------------------------------------------
	
	/**
	 * pre: 
	 * @param longitud: entero mayor a 0, determina la cantiadad de elementos del vector
	 * @param datoInicial: valor inicial para las posiciones del vector
	 * @throws Exception: da error si la longitud es invalida
	 * post: inicializa el vector de longitud de largo y todos los valores inicializados
	 */
	public Vector(int longitud, T datoInicial) {
		if (longitud < 1) {
			throw new RuntimeException("La longitud debe ser mayor o igual a 1");
		}
		this.datos = crearVector(longitud);
		this.datoInicial = datoInicial;
		for(int i = 0; i < this.getLongitud(); i++){
			this.datos[i] = datoInicial;
		}
	}
	
//METODOS DE CLASE ----------------------------------------------------------------------------------------
//METODOS GENERALES ---------------------------------------------------------------------------------------
//METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

	public Vector(Vector<T> vector) {
		this(vector.getLongitud(), vector.datoInicial);
		for(int i = 0; i < vector.getLongitud(); i++){
			this.datos[i] = vector.datos[i];
		}
	}

	/**
	 * pre:
	 * @param posicion: valor entre 1 y el largo del vector (no redimensiona)
	 * @param dato: -
	 * @throws Exception: da error si la posicion no esta en rango
	 * post: guarda la el dato en la posicion dada 
	 */
	public void agregar(int posicion, T dato) {
		ValidacionesUtiles.validarRangoNumerico(posicion, 1, this.getLongitud(), "posicion");
		this.datos[posicion - 1] = dato;
	}

	/**
	 * pre: -
	 * @param posicion: valor entre 1 y el largo del vector
	 * @return devuelve el valor en esa posicion
	 * @throws Exception: da error si la posicion no esta en rango
	 */
	public T obtener(int posicion) {
		ValidacionesUtiles.validarRangoNumerico(posicion, 1, this.getLongitud(), "posicion");
		return this.datos[posicion - 1];
	}

	/**
	 * pre: -
	 * @param posicion: valor entre 1 y el largo del vector
	 * @throws Exception: da error si la posicion no esta en rango
	 * post: remueve el valor en la posicion y deja el valor inicial
	 */
	public void remover(int posicion) throws Exception {
		if ((posicion < 1) ||
				(posicion > this.getLongitud())) {
			throw new Exception("La " + posicion + " no esta en el rango 1 y " + this.getLongitud() + " inclusive");
		}
		this.datos[posicion - 1] = this.datoInicial;
	}
	
	/**
	 * Remueve un dato del vector 
	 * @param posicion
	 * @throws Exception
	 */
	public void remove(T dato) {
		ValidacionesUtiles.esDistintoDeNull(dato, "Dato");
		for(int i = 0; i < getLongitud(); i++){
			if ((this.datos[i] != null) &&
			     this.datos[i].equals(dato)) {
				this.datos[i] = null;
			}
		}		
	}
	
	/**
	 * Remueve la primera aparicion
	 * @param dato
	 */
	public void removeFirst(T dato) {
		ValidacionesUtiles.esDistintoDeNull(dato, "Dato");
		for(int i = 0; i < getLongitud(); i++){
			if ((this.datos[i] != null) &&
			     this.datos[i].equals(dato)) {
				this.datos[i] = null;
				return;
			}
		}		
	}
	

	/**
	 * pre: 
	 * @param dato: valor a guardar
	 * @return devuelve la posicion en que se guardo
	 * @throws Exception
	 * post: guarda el dato en la siguiente posicion vacia
	 */
	public int agregar(T dato) {
		//validar dato;
		for(int i = 0; i < this.getLongitud(); i++) {
			if (this.datos[i] == this.datoInicial) {
				this.datos[i] = dato;
				return i + 1;
			}
		}		
		T[] temp = crearVector(this.getLongitud() * 2);
		for(int i = 0; i < this.getLongitud(); i++) {
			temp[i] = this.datos[i];
		}
		int posicion = this.getLongitud();		
		this.datos = temp;
		this.datos[posicion] = dato;		
		for(int i = posicion +1; i < this.getLongitud(); i++) {
			this.datos[i] = this.datoInicial;	
		}
		return posicion + 1;
	}

	/**
	 * pre: 
	 * @param longitud: -
	 * @return devuelve un vector del tipo y longitud deseado
	 * @throws Exception 
	 */
	
	@SuppressWarnings("unchecked")
	private T[] crearVector(int longitud) {
		if (longitud <= 0) {
			throw new RuntimeException("La longitud debe ser mayor o igual a 1");
		}
		return (T[]) new Object[longitud];
	}
	
	/**
	 * Retorna un iterador sobre los elementos de este vector.
	 * @return Un Iterator<T>.
	 */
	@Override
	public Iterator<T> iterator() {
		return new VectorIterator();
	}

	/**
	 * Clase interna que implementa el Iterator<T> para Vector<T>.
	 */
	private class VectorIterator implements Iterator<T> {
		private int currentIndex = 0;

		@Override
		public boolean hasNext() {
			// Comprueba si hay más elementos en el array 'datos'
			return currentIndex < getLongitud();
		}

		@Override
		public T next() {
			// Si no hay siguiente, lanza una excepción estándar
			if (!hasNext()) {
				throw new NoSuchElementException();
			}
			// Retorna el dato actual y avanza el índice
			return datos[currentIndex++];
		}

		// Opcionalmente, puedes no implementar remove()
		// o lanzar UnsupportedOperationException si el iterador no debe modificar el vector.
		@Override
		public void remove() {
			throw new UnsupportedOperationException("El método remove() no está soportado por este iterador.");
		}
	}
	
//GETTERS SIMPLES -----------------------------------------------------------------------------------------
	
	public int getLongitud() {
		return this.datos.length;
	}

	/**
	 * Devuelve verdadero si contiene el dato.
	 * @param dato
	 * @return
	 */
	public boolean contains(T dato) {
		ValidacionesUtiles.esDistintoDeNull(dato, "Dato");
		for(int i = 0; i < this.getLongitud(); i++) {
			if (dato.equals(this.datos[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Devuelve la cantidad de valores distintos del valor inicial
	 * @return
	 */
	public int getCantidadDeDatos() {
		int cantidadDeDatos = 0;
		for(T dato: this.datos) {
			if (dato != this.datoInicial) {
				cantidadDeDatos++;
			}
		}
		return cantidadDeDatos;
	}
	
//SETTERS SIMPLES -----------------------------------------------------------------------------------------	
}