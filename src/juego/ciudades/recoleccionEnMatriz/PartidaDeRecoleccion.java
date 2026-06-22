package juego.ciudades.recoleccionEnMatriz;

import juego.configuracion.ConfiguracionDeRecoleccion;
import juego.ciudades.recoleccionEnMatriz.ui.KeyHandlerRecoleccion;
import juego.ciudades.recoleccionEnMatriz.ui.MinijuegoRecoleccion;
import modelos.Jugador;
import modelos.Partida;
import juego.ciudades.ordenamientos.EstadoDePartida;
import modelos.Sonido;
import modelosVista.Vista;
import utils.ValidacionesUtiles;

import javax.swing.*;

import java.util.Objects;

public class PartidaDeRecoleccion extends Partida {
	//ATRIBUTOS -----------------------------------------------------------------------------------------------
    private CiudadRecoleccion juegoRecoleccion = null;
    private Vista vista;
    private JFrame ventana;
    private int ultimoNivel;

    //CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * Constructor del TDA partidaDeRecoleccion simplificado.
     * El TDA se crea listo para ser configurado dinámicamente en iniciar().
     * @param sonido 
     *
     * @param nombre: Nombre de la ciudad
     * @param jugador: Jugador de la ciudad
     */
    public PartidaDeRecoleccion(String nombre, Jugador jugador, Sonido sonido) {
        super(nombre, jugador);
        setSonido(sonido);
        setJuego(ConfiguracionDeRecoleccion.FILAS_MAPA, ConfiguracionDeRecoleccion.COLUMNAS_MAPA, ConfiguracionDeRecoleccion.NIVELES_MAPA, ConfiguracionDeRecoleccion.CAPACIDAD_MAXIMA_MOCHILA, getJugador());

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
        return Objects.equals(juegoRecoleccion, that.juegoRecoleccion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), juegoRecoleccion);
    }

    @Override
    public String toString() {
        return "PartidaDeRecoleccion{" +
                "juego=" + juegoRecoleccion +
                '}';
    }

    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    @Override
    public void iniciar() {
        ValidacionesUtiles.validarFalso(estaIniciada(), "La partida ya está iniciada");
        setEstado(EstadoDePartida.Iniciado);
        this.ultimoNivel = juegoRecoleccion.getNivelActual();
        KeyHandlerRecoleccion key = new KeyHandlerRecoleccion();
        this.vista = new Vista(obtenerMapaPorNivel(this.juegoRecoleccion.getNivelActual()), getJugador(), 24, 21, getRutaSprites(), key, this.sonido);

        this.ventana = new JFrame("Ciudad de Recolección");
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setResizable(false);
        ventana.add(vista);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
        // Llamar a finalizar() si el usuario cierra la ventana con la X
        attachCloseHandler(ventana);

        MinijuegoRecoleccion minijuego = new MinijuegoRecoleccion(juegoRecoleccion, this, vista, key, ventana);
        vista.establecerMinijuego(minijuego);
        minijuego.setOnFinalizadoCallback(this::finalizar);

        vista.startGameThread();
         if (this.sonido != null) {
		 	 this.sonido.playMusica(juego.configuracion.ConstantesSonido.RECOLECCION);			
		  }
    }

    /**
     * Actualiza la vista del juego
     */
    public void actualizar() {
        int nivelActual = juegoRecoleccion.getNivelActual();

        if (nivelActual != ultimoNivel) {
            ultimoNivel = nivelActual;

            vista.cargarMapa(obtenerMapaPorNivel(nivelActual));
        }
    }

    @Override
    public void finalizar() {
        ValidacionesUtiles.validarVerdadero(estaIniciada(), "La partida no está iniciada");
        setEstado(EstadoDePartida.Creado);

        int puntos = 0;

        if (juegoRecoleccion != null) {
            puntos = juegoRecoleccion.finalizar();
        }
        this.setPuntaje(puntos);

        if (vista != null) {
            vista.detenerHilo();
        }

        if (ventana != null) {
            ventana.dispose();
            ventana = null;
        }
        if (this.sonido != null) {
        	this.sonido.stopMusica();
        	this.sonido.playMusica(juego.configuracion.ConstantesSonido.GLOBAL_AVENTURA);
        }

        notificarFinalizacion();
    }
    
    

    /**
     * Devuelve el mapa correspondiente al nivel
     *
     * PRE:
     * -Nivel debe ser mayor a cero
     *
     * @param nivel
     * @return
     */
    private String obtenerMapaPorNivel(int nivel) {
        ValidacionesUtiles.validarMayorACero(nivel, "nivel");

        return switch (nivel) {
            case 1 -> "/maps/recoleccion/world_recoleccion_1.txt";
            case 2 -> "/maps/recoleccion/world_recoleccion_2.txt";
            case 3 -> "/maps/recoleccion/world_recoleccion_3.txt";
            default -> "/maps/recoleccion/world_recoleccion_1.txt";
        };
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
        return this.juegoRecoleccion;
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

        this.juegoRecoleccion = new CiudadRecoleccion(filas, columnas, niveles, maximoMochila, jugador);
    }

}