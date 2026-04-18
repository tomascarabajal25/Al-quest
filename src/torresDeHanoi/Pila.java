package torresDeHanoi;

/*
 * Clase pila, gestion de tipo FIFO
 * metodos: push,pop y peek
 * atributos: contnodo y cabeza
 */
public class Pila {
//CONSTANTES-------------------------------------------------------------------
//ATRIBUTOS DE LA CLASE---------------------------------------------------------
//ATRIBUTOS----------------------------------------------------------------------
	private int contNodo;
	private Nodo cabeza;					
//CONSTRUCTORES-----------------------------------------------------------------
	public Pila() {
		this.setContNodo(0);
		this.setCabeza(null);
	}
//METODOS DE CLASES-------------------------------------------------------------
//METODOS GENERALES------------------------------------------------------------

//METODOS DE COMPORTAMIENTO------------------------------------------------------
	/*
	 * @param:nodo que se agregara a la pila
	 * agrega un nodo a la pila 
	 */
	public void push (Nodo nuevoNodo) {
		contNodo++;
		if (cabeza==null) {
			cabeza=nuevoNodo;
		}else {
			nuevoNodo.setAbajo(cabeza);
			cabeza.setArriba(nuevoNodo);
			cabeza=nuevoNodo;
		}
	
	}
	/*
	 * elimina el nodo cabeza de la pila
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
	 * @return: devuelve el nodo cabeza de la pila
	 */
	public String peek() {
		return cabeza.getDato();
	}
	
//GETTER SIMPLES-----------------------------------------------------------------
	public int getContNodo() {
		return contNodo;
	}
	public Nodo getCabeza() {
		return cabeza;
	}
//SETTERS SIMPLES---------------------------------------------------------------
	public void setContNodo(int contNodo) {
		this.contNodo = contNodo;
	}
	
	public void setCabeza(Nodo cabeza) {
		this.cabeza = cabeza;
	}
	
}
