package estructuras.arboles;

class NodoDeArbol<T> {
    private T valor;
    private NodoDeArbol<T> izquierdo = null;
    private NodoDeArbol<T> derecho = null;

    public NodoDeArbol(T valor) {
        this.valor = valor;
        this.izquierdo = null; 
        this.derecho = null;
    }

	public T getValor() {
		return valor;
	}

	public NodoDeArbol<T> getIzquierdo() {
		return izquierdo;
	}

	public NodoDeArbol<T> getDerecho() {
		return derecho;
	}

	public void setValor(T valor) {
		this.valor = valor;
	}

	public void setIzquierdo(NodoDeArbol<T> izquierdo) {
		this.izquierdo = izquierdo;
	}

	public void setDerecho(NodoDeArbol<T> derecho) {
		this.derecho = derecho;
	}

	/**
	 * Devuelve verdadero si alguno de los dos hijos es distintos de null
	 * @return
	 */
	public boolean tieneHijos() {
		return (this.derecho != null) ||
			   (this.izquierdo != null);
	}
	
	public boolean tieneUnHijo() {
		return (((this.derecho != null) &&
				 (this.izquierdo == null)) ||
				((this.derecho == null) &&
				 (this.izquierdo != null)));				
	}
    
    
}