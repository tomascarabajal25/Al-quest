package ciudad_3_laberinto.src;

import javax.swing.SwingUtilities;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new ControladorLaberinto();
            }
        });
    }
}