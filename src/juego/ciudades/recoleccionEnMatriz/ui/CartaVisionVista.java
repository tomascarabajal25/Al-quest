package Juego.ciudades.recoleccionEnMatriz.ui;
 
import Juego.ciudades.recoleccionEnMatriz.CartaVision;
 
public class CartaVisionVista extends CartaVista {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * Constructor del TDA CartaVistaVista
     *
     * PRE:
     * -Carta no debe ser nulo
     * -Col, fila, nivel y tamanio deben ser mayores a cero
     *
     * POST:
     * -Se crea una nueva instancia de CartaVistaVista para la UI
     *
     * @param carta: Carta a la que pertenece la vista
     * @param col: Columna de la posicion de la carta
     * @param fila: Fila de la posicion de la carta
     * @param nivel: Nivel de la posicion de la carta
     * @param tamanio: Tamañio de la carta
     */
    public CartaVisionVista(CartaVision carta, int col, int fila, int nivel, int tamanio) {
        super(carta, col, fila, nivel, tamanio, "/assets/cartas/carta_vision.bmp");
    }
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
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
}