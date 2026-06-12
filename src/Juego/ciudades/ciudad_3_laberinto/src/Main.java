package juego.ciudades.ciudad_3_laberinto.src;

import javax.swing.SwingUtilities;
import modelos.Jugador;

/**
 * Clase de prueba para lanzar el modulo Laberinto de forma independiente.
 * No forma parte del juego final, solo sirve para desarrollo y pruebas.
 */
public class Main {

     /**
     * Punto de entrada de la aplicacion de prueba.
     * @param args argumentos de linea de comandos (no se usan)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                PartidaLaberinto partidaLaberinto = new PartidaLaberinto(new Jugador(""));
                partidaLaberinto.iniciar();
            }
        });
    }
}