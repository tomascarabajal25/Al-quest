package com.aiquest.estructuras.arboles;

class NodoAVL<T> extends NodoDeArbol<T> {
    private int altura;

    public NodoAVL(T valor) {
        super(valor);
        this.altura = 1; // La altura inicial de un nodo recién creado es 1
    }

	public int getAltura() {
		return altura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}
    
    
}