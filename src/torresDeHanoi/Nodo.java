package torresDeHanoi;

/**
 * Representa un nodo genérico dentro de una estructura enlazada.
 * 
 * Este TDA encapsula un dato y referencias a nodos adyacentes
 * (arriba y abajo), permitiendo modelar estructuras como pilas.
 * 
 * @param <T> tipo de dato almacenado en el nodo
 */
public class Nodo<T> {
//CONSTANTES-------------------------------------------------------------------
//ATRIBUTOS DE LA CLASE---------------------------------------------------------
//ATRIBUTOS----------------------------------------------------------------------
	private T dato;
	private Nodo<T> arriba;
	private Nodo<T> abajo;					
//CONSTRUCTORES-----------------------------------------------------------------
	/**
     * Crea un nodo con un dato inicial.
     * 
     * PRE:
     * - datoNuevo != null
     * 
     * POST:
     * - El nodo contiene el dato especificado.
     * - Las referencias arriba y abajo son null.
     * 
     * @param datoNuevo dato a almacenar
     */
	public Nodo(T datoNuevo) {
		this.setDato(datoNuevo);
	}
//METODOS DE CLASES-------------------------------------------------------------
//METODOS GENERALES------------------------------------------------------------

//METODOS DE COMPORTAMIENTO------------------------------------------------------

//GETTER SIMPLES-----------------------------------------------------------------
	/**
     * Devuelve el dato almacenado en el nodo.
     */
	public T getDato() {
		return dato;
	}
	 /**
     * Devuelve el nodo superior.
     */
	public Nodo<T> getArriba() {
		return arriba;
	}	
	 /**
     * Devuelve el nodo inferior.
     */
	public Nodo<T> getAbajo() {
		return abajo;
	}
//SETTERS SIMPLES---------------------------------------------------------------
	/**
     * Establece el dato del nodo.
     * 
     * PRE:
     * - dato != null
     * 
     * POST:
     * - El nodo contiene el nuevo dato.
     */
	public void setDato(T dato) {
		this.dato = dato;
	}
	/**
     * Establece la referencia al nodo superior.
     * 
     * PRE:
     * - arriba puede ser null o un nodo válido
     * 
     * POST:
     * - Se actualiza la referencia al nodo superior.
     */
	public void setArriba(Nodo<T> arriba) {
		this.arriba = arriba;
	}
	/**
     * Establece la referencia al nodo inferior.
     * 
     * PRE:
     * - abajo puede ser null o un nodo válido
     * 
     * POST:
     * - Se actualiza la referencia al nodo inferior.
     */
	public void setAbajo(Nodo<T> abajo) {
		this.abajo = abajo;
	}
	
	
}

