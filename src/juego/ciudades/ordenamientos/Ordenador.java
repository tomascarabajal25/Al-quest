package juego.ciudades.ordenamientos;
import java.util.List;
import java.util.Objects;

import utils.ValidacionesUtiles;

public abstract class Ordenador<T extends Comparable<T>> {
	//ATRIBUTOS----------------------------------------------------------------------
    private String nombre;
    
    //CONSTRUCTORES-----------------------------------------------------------------
	public Ordenador(String nombre) {
    	setNombre(nombre);
    }
    
    //METODOS DE CLASES-------------------------------------------------------------
    //METODOS GENERALES------------------------------------------------------------
    
    @Override
	public int hashCode() {
		return Objects.hash(nombre);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ordenador<?> other = (Ordenador<?>) obj;
		return Objects.equals(nombre, other.nombre);
	}

	@Override
	public String toString() {
		return "Ordenador [nombre=" + nombre + "]";
	}

	
    
	//METODOS DE COMPORTAMIENTO------------------------------------------------------
    protected abstract void ordenar(List<T> elementos);
    
    public abstract void ordenar(List<T> elementos, AdministradorDePasos<T> historialDePasos);
    
    //GETTER SIMPLES-----------------------------------------------------------------
    public String getNombre() {
    	return nombre;
    }
    
    // SETTERS SIMPLES---------------------------------------------------------------------------------------
     private void setNombre(String nombre) {
    	ValidacionesUtiles.esDistintoDeNull(nombre, nombre);
    	this.nombre=nombre;
    }
     
}