package com.aiquest;

import com.aiquest.juego.ciudades.reinas.ui.VentanaPrincipal;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal(() -> {

                // acá se conectará con la siguiente ciudad cuando exista, es un print temporal
                System.out.println("Ciudad completada");

            });
        });
    }
}
