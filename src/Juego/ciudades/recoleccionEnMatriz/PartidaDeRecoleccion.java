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

        CiudadRecoleccion juego = new CiudadRecoleccion(filas, columnas, niveles, maximoMochila, jugador);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    @Override
    public void iniciar() {
        this.juego.iniciar();
    }

    @Override
    public void finalizar() {
        int puntos = juego.finalizar();
        int puntajeActual = this.getPuntajeActual();
        this.setPuntaje(puntajeActual + puntos);

    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

}
