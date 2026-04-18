package torresDeHanoi;

public class Nodo {
//CONSTANTES-------------------------------------------------------------------
//ATRIBUTOS DE LA CLASE---------------------------------------------------------
//ATRIBUTOS----------------------------------------------------------------------
	private String dato;
	private Nodo arriba;
	private Nodo abajo;					
//CONSTRUCTORES-----------------------------------------------------------------
	public Nodo(String datoNuevo) {
		this.setDato(datoNuevo);
	}
//METODOS DE CLASES-------------------------------------------------------------
//METODOS GENERALES------------------------------------------------------------

//METODOS DE COMPORTAMIENTO------------------------------------------------------

//GETTER SIMPLES-----------------------------------------------------------------
	public String getDato() {
		return dato;
	}
	public Nodo getArriba() {
		return arriba;
	}	
	public Nodo getAbajo() {
		return abajo;
	}
//SETTERS SIMPLES---------------------------------------------------------------
	
	public void setDato(String dato) {
		this.dato = dato;
	}
	
	public void setArriba(Nodo arriba) {
		this.arriba = arriba;
	}
	
	public void setAbajo(Nodo abajo) {
		this.abajo = abajo;
	}
	
	
}

