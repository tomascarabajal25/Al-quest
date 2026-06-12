package juego.ciudades.complejidad.ui;

import java.awt.*;
import javax.swing.*;
import juego.ciudades.complejidad.PartidaComplejidad;



/**
 * Ventana principal de la Ciudad Complejidad Algorítmica.
 */
public class VentanaComplejidad extends JFrame {

    private PanelComplejidad panel;

    /**
     * @param partida Referencia a la partida real que inició este minijuego
     */
    public VentanaComplejidad(PartidaComplejidad partida) {
        super("Complejidad Algorítmica");

        // CORRECCIÓN: Ya no creamos una nueva partida, usamos la que viene por parámetro
        panel = new PanelComplejidad(partida.getCiudad(), () -> partida.ganar());

        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(panel, BorderLayout.CENTER);
        contenedor.add(crearPanelDerecho(), BorderLayout.EAST);

        add(contenedor);
        pack();
        
        // CORRECCIÓN: Usar DISPOSE para liberar los recursos de esta ventana sin matar el juego principal
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel crearPanelDerecho() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 20, 24, 20));
        panel.setPreferredSize(new Dimension(220, 0));

        JLabel titulo = new JLabel("Instrucciones");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea instrucciones = new JTextArea(
            "Ingresá una ecuación de\n" +
            "recurrencia con el formato:\n\n" +
            "T(n) = aT(n/b) + f(n)\n\n" +
            "Donde:\n" +
            "• a: cantidad de subproblemas\n" +
            "• b: factor de división\n" +
            "• f(n): función de costo\n\n" +
            "Ejemplos válidos:\n" +
            "T(n) = 2T(n/2) + n\n" +
            "T(n) = 4T(n/2) + n^2\n" +
            "T(n) = 3T(n/3) + 1"
        );
        instrucciones.setEditable(false);
        instrucciones.setBackground(panel.getBackground());
        instrucciones.setFont(new Font("Arial", Font.PLAIN, 12));
        instrucciones.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(12));
        panel.add(instrucciones);

        return panel;
    }
}