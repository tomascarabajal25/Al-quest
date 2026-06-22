package juego.ciudades.complejidad;

import modelos.Jugador;
import modelos.Sonido;

public class ProbarComplejidad {
    public static Sonido sonido = new Sonido();

    public static void main(String[] args) {
        PartidaComplejidad partida = new PartidaComplejidad(new Jugador(""), sonido);
        partida.iniciar();
    }
}
