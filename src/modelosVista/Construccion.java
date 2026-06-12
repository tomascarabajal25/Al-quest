package modelosVista;

import utils.ValidacionesUtiles;

import java.awt.image.BufferedImage;

public class Construccion{
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private BufferedImage imagen = null;
    private boolean colision = false;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Getter del atributo imagen
     * @return: Devuelve el valor del atributo imagen
     */
    public BufferedImage getImagen() {
        return this.imagen;
    }

    /**
     * Getter del atributo colision
     * @return: Devuelve el estado del atributo colision
     */
    protected boolean getColision() {
        return this.colision;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Setter del atributo imagen
     *
     * PRE:
     * -Imagen no debe ser nulo
     *
     * @param imagen: imagen a guardar
     */
    protected void setImagen(BufferedImage imagen) {
        ValidacionesUtiles.esDistintoDeNull(imagen, "imagen");
        this.imagen = imagen;
    }

    /**
     * Setter del atributo colision
     *
     * PRE:
     * -El estado de colision no debe ser nulo
     *
     * @param estado: Estado nuevo de colision
     */
    protected void setColision(boolean estado) {
        ValidacionesUtiles.esDistintoDeNull(estado, "estado");
        this.colision = estado;
    }
	
	

	
	
	
}
