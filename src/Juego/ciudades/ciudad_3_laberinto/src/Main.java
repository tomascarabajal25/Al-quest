package juego.ciudades.ciudad_3_laberinto.src;

import javax.swing.SwingUtilities;
import modelos.Jugador;
public class Main {
    public static void main(String[] args) {
        PartidaLaberinto partidaLaberinto = new PartidaLaberinto(new Jugador(""));
        partidaLaberinto.iniciar();
    }
}