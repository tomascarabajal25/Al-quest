package ciudad_3_laberinto.src;

import ordenamientos.EstadoDePartida;
import modelos.Partida;
import modelos.Jugador;

public class PartidaLaberinto extends Partida {

    public PartidaLaberinto(Jugador jugador) {
        super("Laberinto", jugador);
    }
    
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);
        new ControladorLaberinto(this);
    }

    public void finalizar(int puntaje) {
        setPuntaje(puntaje);
        finalizar();
    }
}
