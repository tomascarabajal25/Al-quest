package juego.ciudades.recoleccionEnMatriz;

import juego.ciudades.recoleccionEnMatriz.ui.KeyHandlerRecoleccion;
import juego.ciudades.recoleccionEnMatriz.ui.MinijuegoRecoleccion;
import modelos.Jugador;
import modelos.Partida;
import modelosVista.Vista;
import utils.ValidacionesUtiles;

import javax.swing.*;

import java.util.Objects;

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

        setJuego(filas, columnas, niveles, maximoMochila, jugador);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------

    /**
     * Equals del TDA PartidaDeRecoleccion. Compara en base al atributo juego
     * @param o   the reference object with which to compare.
     * @return: true si son iguales, false si no lo son
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PartidaDeRecoleccion that = (PartidaDeRecoleccion) o;
        return Objects.equals(juego, that.juego);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), juego);
    }

    @Override
    public String toString() {
        return "PartidaDeRecoleccion{" +
                "juego=" + juego +
                '}';
    }

    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    @Override
    public void iniciar() {
    	KeyHandlerRecoleccion key = new KeyHandlerRecoleccion();
        Vista vista = new Vista("/maps/world_recoleccion.txt", getJugador(), 24,21,"/assets/jugador/boy", key );

        // Igual que PartidaBusqueda
        JFrame ventana = new JFrame("Ciudad de Recolección");
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setResizable(false);
        ventana.add(vista);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        MinijuegoRecoleccion minijuego =new MinijuegoRecoleccion(juego, vista, key, ventana);
        vista.establecerMinijuego(minijuego);
        minijuego.setOnFinalizadoCallback(this::finalizar);

        vista.startGameThread();
    }

    @Override
    public void finalizar() {
        int puntos = juego.finalizar();
        this.setPuntaje( puntos);

    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Getter del atributo juego
     *
     * @return: Devuelve el atributo juego
     */
    public CiudadRecoleccion getJuego() {
        return this.juego;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Setter del atributo juego
     * @param filas: Filas del mapa del juego
     * @param columnas: Columnas del mapa del juego
     * @param niveles: Niveles del mapa del juego
     * @param maximoMochila: Maxima capacidad de la mochila del juego
     * @param jugador: Jugador del juego
     */
    private void setJuego(int filas, int columnas, int niveles, int maximoMochila, Jugador jugador){
        ValidacionesUtiles.validarMayorACero(filas, "filas");
        ValidacionesUtiles.validarMayorACero(columnas, "columnas");
        ValidacionesUtiles.validarMayorACero(niveles, "niveles");
        ValidacionesUtiles.validarMayorACero(maximoMochila, "maximoMochila");
        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");

        this.juego = new CiudadRecoleccion(filas, columnas, niveles, maximoMochila, jugador);
    }

}
