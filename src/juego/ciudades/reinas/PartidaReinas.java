package juego.ciudades.reinas;

import juego.ciudades.ordenamientos.EstadoDePartida;
import modelos.Jugador;
import modelos.Partida;

public class PartidaReinas extends Partida {

    private final CiudadReinas ciudad;
    private final int tamanio;

    /**
     * Crea una partida de N-Reinas para el jugador dado.
     *
     * @param jugador jugador que participa en la partida
     * @param tamanio dimensión del tablero (N x N)
     */
    public PartidaReinas(Jugador jugador, int tamanio) {
        super("N-Reinas", jugador);
        this.ciudad = new CiudadReinas();
        this.tamanio = tamanio;
    }

    /**
     * Inicia la partida cambiando su estado a Iniciado.
     */
    @Override
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);
    }

    /**
     * Finaliza la partida. Se llama cuando el jugador gana o abandona.
     */
    @Override
    public void finalizar() {
        // se puede extender cuando EstadoDePartida tenga Ganado/Abandonado
    }

    /** @return la ciudad de reinas asociada a esta partida */
    public CiudadReinas getCiudad() {
        return ciudad;
    }

    /** @return dimensión del tablero */
    public int getTamanio() {
        return tamanio;
    }
}
