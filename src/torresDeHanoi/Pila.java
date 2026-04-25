package torresDeHanoi;

import materialesUtiles.ValidacionesUtiles;

/*
 * Clase pila, gestion de tipo LIFO
 * metodos: push,pop y peek
 * atributos: contnodo y cabeza
 */
public class Pila {
//CONSTANTES-------------------------------------------------------------------
//ATRIBUTOS DE LA CLASE---------------------------------------------------------
//ATRIBUTOS----------------------------------------------------------------------
	private int contNodo;
	private Nodo<String> cabeza;					
//CONSTRUCTORES-----------------------------------------------------------------
	/*
	 * post: inicializa la pila vacia para su uso
	 */
	public Pila() {
		this.setContNodo(0);
		this.setCabeza(null);
	}
//METODOS DE CLASES-------------------------------------------------------------
//METODOS GENERALES------------------------------------------------------------

//METODOS DE COMPORTAMIENTO------------------------------------------------------
	/*pre: nuevo nodo distinto de null
	 * @param:nodo que se agregara a la pila
	 * post:agrega un nodo a la pila 
	 */
	public void push (Nodo<String> nuevoNodo) {
		ValidacionesUtiles.esDistintoDeNull(nuevoNodo, "no se puede insertar un nodo nulo");
		if (cabeza==null) {
			cabeza=nuevoNodo;
		}else {
			nuevoNodo.setAbajo(cabeza);
			cabeza.setArriba(nuevoNodo);
			cabeza=nuevoNodo;
		}
		this.setContNodo(++contNodo);
	}
	/*
	 * post:elimina el nodo cabeza de la pila
	 */
	public void pop() {
		if (contNodo>0) {
			contNodo--;
			cabeza=cabeza.getAbajo();
		}
		if (cabeza != null) {
            cabeza.setArriba(null);
        }
	}
	/*
	 * @return: devuelve el dato del nodo cabeza de la pila
	 */
	public String peek() {
		return cabeza.getDato();
	}
	
//GETTER SIMPLES-----------------------------------------------------------------
	/*
	 *  @return: devuelve la cantidad de nodos
	 */
	public int getContNodo() {
		return contNodo;
	}
	/*
	 *  @return: devuelve el nodo cabeza de la pila
	 */
	public Nodo<String> getCabeza() {
		return cabeza;
	}
//SETTERS SIMPLES---------------------------------------------------------------
	/*
	 *post: cambia la cantidad de nodos
	 */
	private void setContNodo(int contNodo) {
		this.contNodo = contNodo;
	}
	/*
	 *post: cambia el nodo cabeza
	 */
	private void setCabeza(Nodo<String> cabeza) {
		this.cabeza = cabeza;
	}
	
}
