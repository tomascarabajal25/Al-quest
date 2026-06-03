package com.aiquest.juego.ciudades.ordenamientos;

import com.aiquest.modelos.Elemento;
import com.aiquest.utils.ValidacionesUtiles;

/**
 * Clase caja, elemento q sera utilizado para comparar en la ciudad de ordenamiento
 * se compara en base al tamaño
 */
public class Caja extends Elemento implements Comparable<Caja>{
	//ATRIBUTOS----------------------------------------------------------------------
	private int tamaño;
	//CONSTRUCTORES-----------------------------------------------------------------
	/**
	 * pre:
	 * @param nombre diferente de null, almenos len de 2
	 * @param tamaño mayor a 0
	 * post: crea la caja con el tamaño y nombre ingresados
	 */
	public Caja(String nombre, int tamaño) {
		super(nombre);
		this.setTamaño(tamaño);
	}
	
	//METODOS DE COMPORTAMIENTO------------------------------------------------------
	
	//METODOS DE CLASES-------------------------------------------------------------
	//METODOS GENERALES------------------------------------------------------------
	@Override
    public int compareTo(Caja otra) {
        return Integer.compare(this.tamaño, otra.getTamaño());
    }
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Caja other = (Caja) obj;
		return tamaño==other.getTamaño();
	}

	//GETTER SIMPLES-----------------------------------------------------------------
	/**
	 * devuelve el tamaño
	 * @return
	 */
	public int getTamaño() {
		return tamaño;
	}
	
	
	//SETTERS SIMPLES---------------------------------------------------------------
	/**
	 * modifica el tamaño
	 * @param tamaño, debe ser mayor a 0
	 */
	private void setTamaño(int tamaño) {
		ValidacionesUtiles.validarMayorACero(tamaño, "El tamaño no puede ser menor a 0");
		this.tamaño=tamaño;
	}
	
	
}
