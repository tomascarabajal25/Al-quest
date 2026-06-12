package juego.ciudades.recoleccionEnMatriz.ui;

import modelosVista.ElementoVista;
import modelos.Elemento;

public abstract class CartaVista extends ElementoVista {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * Constructor del TDA CartaVista
     *
     * PRE:
     * -Elemento y rutaImagen no deben ser nulos
     * -Col, fil, nivel y tamanio deben ser mayores a cero
     *
     * POST:
     * -Se crea una nueva instancia de CartaVista para la UI
     *
     * @param elemento: Carta a la cual pertenece la vista
     * @param col: Columna de la posicion de la carta
     * @param fil: Fila de la posicion de la carta
     * @param nivel: Nivel de la posicion de la carta
     * @param tamanio: Tamanio de la carta
     * @param rutaImagen: Ruta de la imagen de la carta
     */
    public CartaVista(Elemento elemento, int col, int fil, int nivel, int tamanio, String rutaImagen) {
        super(elemento, col, fil, nivel, tamanio, rutaImagen);
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