package Juego.ciudades.recoleccionEnMatriz;

import modelos.Elemento;
import utils.ValidacionesUtiles;

public class CartaPuntos extends Elemento {
//INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private String descripcion = null;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    /**
     * Constructor del TDA Elemento
     *
     * PRE:
     * -El nombre no debe ser null
     * POST:
     * -Se crea una instancia de Elemento con nombre
     *
     * @param nombre : nombre del elemento
     */
    public CartaPuntos(String nombre, String descripcion) {
        super(nombre);
        ValidacionesUtiles.esDistintoDeNull(descripcion, "Descripcion");
        setDescripcion(descripcion);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return "CartaPuntos{}";
    }

    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Efecto de la carta
     *
     * PRE:
     * -Juego no debe ser null
     *
     * @param juego: Juego correspondiente a cada ciudad
     */
    @Override
    public void aplicarEfecto(CiudadRecoleccion juego){
        juego.aumentarPuntos();
    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Getter del atributo descripcion
     *
     * @return: Devuelve la descripcion de la carta
     */
    public String getDescripcion(){
        return this.descripcion;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Setter del atributo descripcion
     *
     * PRE:
     * -Descripcion no debe ser nulo
     */
    private void setDescripcion(String descripcion){
        ValidacionesUtiles.esDistintoDeNull(descripcion, "Descripcion");
        this.descripcion = descripcion;
    }
}
