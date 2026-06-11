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
        ciudadGrafos = new CiudadGrafos();
        ciudadGrafos.iniciar();

        ciudadGrafos.getVentana().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                finalizar();
            }
        });
    }

    @Override
    public void finalizar() {
        setEstado(EstadoDePartida.Creado);
        setPuntaje(100);
    }
}
