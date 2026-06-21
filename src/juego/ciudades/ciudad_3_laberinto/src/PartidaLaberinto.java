package juego.ciudades.ciudad_3_laberinto.src;

import juego.ciudades.ordenamientos.EstadoDePartida;
import modelos.Partida;
import modelos.Jugador;

public class PartidaLaberinto extends Partida {

    private ControladorLaberinto controlador;

	/**
     * Crea el constructor de partida
     * Este se ocupara de ejecutar y finalizar la ciudad laberinto dentro del juego
     */
    public PartidaLaberinto(Jugador jugador) {
        super("Laberinto", jugador);
    }
    
    /**
     * Inicia la partida
    */
    @Override
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);
        this.controlador =new ControladorLaberinto(this);
        if (this.sonido != null) {
            this.sonido.playMusica(juego.configuracion.ConstantesSonido.LABERINTO);
        }
        
    }

    /**
     * 
     */
    @Override
    public void finalizar() {
        setEstado(EstadoDePartida.Creado);
        
        if (this.controlador != null) {
            this.controlador.cerrarVentana();
            this.controlador = null; 
        }
        if (this.sonido != null) {
            this.sonido.stopMusica();
            this.sonido.playMusica(juego.configuracion.ConstantesSonido.GLOBAL_AVENTURA);
        }
        notificarFinalizacion();
    }

    public void finalizar(int puntaje) {
        setPuntaje(puntaje);
        finalizar();
    }
}