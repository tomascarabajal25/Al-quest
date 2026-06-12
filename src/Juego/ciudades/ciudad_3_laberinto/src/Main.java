package ciudad_3_laberinto.src;

import javax.swing.SwingUtilities;
import modelos.Jugador;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Jugador jugador = new Jugador("Jugador de prueba");
                PartidaLaberinto partida =  new PartidaLaberinto(jugador);
                partida.iniciar();
            }
        });
    }
}