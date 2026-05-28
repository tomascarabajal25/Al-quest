package Juego.ciudades.recoleccionEnMatriz;

import modelos.Mochila;
import modelos.Jugador;
import modelos.Partida;
import utils.ValidacionesUtiles;

public class PartidaDeRecoleccion extends Partida {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private CiudadRecoleccion juego = null;
    private Mapa3D mapa = null;
    private Mochila mochila = null;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * Constructor del TDA partidaDeRecoleccion
     *
     * @param nombre: Nombre de la ciudad
     * @param jugador: Jugador de la ciudad
     * @param filas: Filas del mapa
     * @param columnas: Columnas del mapa
     * @param niveles: Niveles del mapa
     * @param maximoMochila: Cantidad maxima de elementos de la mochila
     */
    public PartidaDeRecoleccion(String nombre, Jugador jugador, int filas, int columnas, int niveles, int maximoMochila) {
        super(nombre, jugador);
        ValidacionesUtiles.validarMayorACero(filas, "filas");
        ValidacionesUtiles.validarMayorACero(columnas, "columnas");
        ValidacionesUtiles.validarMayorACero(niveles, "niveles");
        ValidacionesUtiles.validarMayorACero(maximoMochila, "maximoMochila");
        mapa = new Mapa3D(filas, columnas, niveles);
        mochila = new Mochila(3);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    @Override
    public void iniciar() {

    }

    @Override
    public void finalizar() {

    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Setter del atributo mapa
     *
     * PRE:
     * -Los atributos filas, columnas y niveles deben ser mayores a cero
     */
    private void setMapa(int filas, int columnas, int niveles){
        ValidacionesUtiles.validarMayorACero(filas, "filas");
        ValidacionesUtiles.validarMayorACero(columnas, "columnas");
        ValidacionesUtiles.validarMayorACero(niveles, "niveles");
        mapa = new Mapa3D(filas, columnas, niveles);
    }

    /**
     * Setter del atributo mochila
     *
     * PRE:
     * -El atributo maximoMochila debe ser mayor a cero
     */
    private void setMochila(int maximoMochila){
        ValidacionesUtiles.validarMayorACero(maximoMochila, "maximoMochila");
        mochila = new Mochila(3);
    }
}
