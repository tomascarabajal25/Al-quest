package ciudad_3_laberinto.src;

import ordenamientos.EstadoDePartida;
import modelos.Partida;
import modelos.Jugador;

public class PartidaLaberinto extends Partida {

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
        new ControladorLaberinto(this);
    }

    /**
     * 
     */
    @Override
    public void finalizar() {
        setEstado(EstadoDePartida.Creado);
    }

    public void finalizar(int puntaje) {
        setPuntaje(puntaje);
        finalizar();
    }
}