package juego.ciudades.grafos.controller;

import juego.ciudades.ordenamientos.EstadoDePartida;
import modelos.Jugador;
import modelos.Partida;

public class PartidaGrafos extends Partida {
    private CiudadGrafos ciudadGrafos;

    public PartidaGrafos(String nombre, Jugador jugador) {
        super(nombre, jugador);
    }

    @Override
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);
        ciudadGrafos = new CiudadGrafos(this);
        ciudadGrafos.iniciar();
    }

    @Override
    public void finalizar() {
        setEstado(EstadoDePartida.Creado);
        setPuntaje(200);
        if (ciudadGrafos != null) {
            ciudadGrafos.detenerAutoPlay();
            if (ciudadGrafos.getVentana() != null) {
                ciudadGrafos.getVentana().dispose();
            }
        }
        notificarFinalizacion();
    }
}
