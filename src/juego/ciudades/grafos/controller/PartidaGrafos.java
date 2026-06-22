package juego.ciudades.grafos.controller;

import juego.ciudades.ordenamientos.EstadoDePartida;
import modelos.Jugador;
import modelos.Partida;
import modelos.Sonido;

public class PartidaGrafos extends Partida {
    private CiudadGrafos ciudadGrafos;

    public PartidaGrafos(String nombre, Jugador jugador, Sonido sonido) {
        super(nombre, jugador);
        setSonido(sonido);
    }

    @Override
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);
        ciudadGrafos = new CiudadGrafos(this);
        ciudadGrafos.iniciar();
         if (this.sonido != null) {
			 this.sonido.playMusica(juego.configuracion.ConstantesSonido.GRAFOS);
		 }
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
        if (this.sonido != null) {
        	this.sonido.stopMusica();
        	this.sonido.playMusica(juego.configuracion.ConstantesSonido.GLOBAL_AVENTURA);
        				 
        }
        notificarFinalizacion();
    }
}
