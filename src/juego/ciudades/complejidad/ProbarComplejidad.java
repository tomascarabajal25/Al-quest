package juego.ciudades.complejidad;

import javax.swing.*;
import juego.ciudades.complejidad.ui.VentanaComplejidad;
import modelos.Jugador;

public class ProbarComplejidad {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Jugador jugador = new Jugador("Jugador 1");
            new VentanaComplejidad(jugador, () -> {
                System.out.println("Ciudad completada");
            });
        });
    }
}
