package modelos;

public class Objeto extends Entidad {
	private boolean colision =false;

    /**
     * Constructor del TDA Objeto
     *
     * PRE:
     * -Nombre no debe ser nulo
     *
     * @param nombre:
     * @param colision:
     */
	public Objeto(String nombre, boolean colision) {
		super(nombre);
		setColision(colision);
		
	}

    /**
     * Getter del atributo colision
     * @return: estado del atributo
     */
    public boolean getColision() {
		return this.colision;
	}

    /**
     * Setter del atributo colision
     *
     * @param colision: nuevo estado
     */
	private void setColision(boolean colision) {
		this.colision = colision;
	}
	


}
