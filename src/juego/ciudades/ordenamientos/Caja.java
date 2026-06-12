package juego.ciudades.ordenamientos;


import modelos.Objeto;
import utils.ValidacionesUtiles;

/**
 * Clase caja, elemento q sera utilizado para comparar en la ciudad de ordenamiento
 * se compara en base al tamaño
 */
public class Caja extends Objeto implements Comparable<Caja>{
	//ATRIBUTOS----------------------------------------------------------------------
	private int tamanio;
	//CONSTRUCTORES-----------------------------------------------------------------
	/**
     * pre:
     *
     * @param nombre diferente de null, almenos len de 2
     * @param tamanio mayor a 0
     *               post: crea la caja con el tamaño y nombre ingresados
     */
	public Caja(String nombre, int tamanio,  boolean colision) {
		super(nombre, colision);
		this.setTamaño(tamanio);
	}
	
	//METODOS DE COMPORTAMIENTO------------------------------------------------------
	
	//METODOS DE CLASES-------------------------------------------------------------
	//METODOS GENERALES------------------------------------------------------------
	@Override
    public int compareTo(Caja otra) {
        return Integer.compare(this.getTamanio(), otra.getTamanio());
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
		return this.tamanio==other.getTamanio();
	}

   
    //GETTER SIMPLES-----------------------------------------------------------------
	/**
	 * devuelve el tamaño
	 * @return
	 */
	public int getTamanio() {
		return this.tamanio;
	}
	
	
	//SETTERS SIMPLES---------------------------------------------------------------
	/**
	 * modifica el tamaño
	 * @param tamanio, debe ser mayor a 0
	 */
	private void setTamaño(int tamanio) {
		ValidacionesUtiles.validarMayorACero(tamanio, "El tamaño no puede ser menor a 0");
		this.tamanio=tamanio;
	}
	
	
}
