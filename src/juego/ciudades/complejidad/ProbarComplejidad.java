package Juego.ciudades.complejidad;

import Juego.ciudades.complejidad.ui.VentanaComplejidad;
import javax.swing.*;

public class ProbarComplejidad {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaComplejidad(() -> {
                System.out.println("Ciudad completada");
            });
        });
    }
}
