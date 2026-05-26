package Juego.ordenamientos;

import java.util.ArrayList;
import java.util.List;

public class PasoOrdenamiento<T> {
	//ATRIBUTOS----------------------------------------------------------------------
    private List<T> estadoCopia; 
    private int indice1;
    private int indice2;
    private String mensaje;
    
    //CONSTRUCTORES-----------------------------------------------------------------
    public PasoOrdenamiento(List<T> estado, int i1, int i2, String mensaje) {
        setEstadoCopia(new ArrayList<>(estado)); 
        setIndice1(i1);
        setIndice2(i2);
        setMensaje(mensaje);
    }
    
  //GETTER SIMPLES-----------------------------------------------------------------
	/**
     * post: devuelve como se ven los elementos en este paso
     */
    public List<T> getCopiasEnEstePaso() {
        return estadoCopia;
    }
    /**
     * post: devuelve el indice 1
     */
    public int getIndice1() {
    	return indice1; 
    	}
    
    /**
     * post: devuelve el indice 2
     */
    public int getIndice2() {
    	return indice2;
    	}
    /**
     * post: devuelve la accion que se esta ejecutando entre los indices
     */
    public String getAccion() { 
    	return mensaje; 
    	}
    //SETTERS SIMPLES---------------------------------------------------------------
	
  	
    private void setMensaje(String mensaje2) {
		this.mensaje=mensaje2;
		
	}

	private void setIndice2(int i2) {
		this.indice2=i2;
	}

	private void setIndice1(int i1) {
		this.indice1=i1;
		
	}

	private void setEstadoCopia(List<T> estadoCopia) {
		this.estadoCopia=estadoCopia;
	}
}