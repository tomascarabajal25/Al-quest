package juego.ciudades.complejidad;

import juego.ciudades.ordenamientos.EstadoDePartida;
import modelos.Jugador;
import modelos.Partida;

public class PartidaComplejidad extends Partida {

    private final CiudadComplejidad ciudad;

    /**
     * @param jugador jugador que participa en la partida
     */
    public PartidaComplejidad(Jugador jugador) {
        super("Complejidad Algorítmica", jugador);
        this.ciudad = new CiudadComplejidad();
    }

    @Override
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);
    }

    @Override
    public void finalizar() {
        setEstado(EstadoDePartida.Creado);
        notificarFinalizacion();
    }

    /** @return la ciudad de complejidad asociada a esta partida */
    public CiudadComplejidad getCiudad() {
        return ciudad;
    }
}
