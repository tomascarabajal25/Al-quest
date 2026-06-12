package juego.ciudades.ciudad_3_laberinto.src;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * Ventana principal del modulo Laberinto.
 * Contiene el panel de visualizacion, botones de control y etiquetas de estado.
 * La logica es manejada por ControladorLaberinto.
 */
public class VentanaLaberinto extends JFrame {
    private PanelLaberinto panelLaberinto;

    private JButton botonIniciar;
    private JButton botonPausar;
    private JButton botonResetear;
    private JLabel labelEstado;
    private JLabel labelPaso;

    public VentanaLaberinto(PanelLaberinto panelLaberinto) {
        this.panelLaberinto = panelLaberinto;
        configurarVentana();
        agregarComponentes();
    }

    private void configurarVentana() {
        setTitle ("Ciudad 3 - Laberinto");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout(10, 10));
    }

    private void agregarComponentes() {
        add(crearPanelTitulo(), BorderLayout.NORTH);
        add(panelLaberinto, BorderLayout.CENTER);
        add(crearPanelControles(), BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel crearPanelTitulo() {
        JPanel panel = new JPanel();
        panel.setBorder(new EmptyBorder(10, 10, 0, 10));
        JLabel titulo = new JLabel("Resolucion por backtracking", SwingConstants.CENTER);
        panel.add(titulo);
        return panel;
    }

    private JPanel crearPanelControles() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(new EmptyBorder(0, 10, 10, 10));

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        botonIniciar = new JButton("Iniciar");
        botonPausar = new JButton("Pausar");
        botonResetear = new JButton("Resetear");

        // Pausar y Resetear deshabilitados hasta que empiece
        botonPausar.setEnabled(false);
        botonResetear.setEnabled(false);

        panelBotones.add(botonIniciar);
        panelBotones.add(botonPausar);
        panelBotones.add(botonResetear);

        // Etiquetas de estado
        JPanel panelEstado = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        labelEstado = new JLabel("Estado: Listo");
        labelPaso = new JLabel("Paso: 0");
        panelEstado.add(labelEstado);
        panelEstado.add(labelPaso);

        panel.add(panelBotones, BorderLayout.CENTER);
        panel.add(panelEstado, BorderLayout.SOUTH);

        return panel;
    }

    /** Muestra la ventana en pantalla. */
    public void mostrar() {
        setVisible(true);
    }

    /** @return boton para iniciar la animacion */
    public JButton getBotonIniciar() {
        return botonIniciar;
    }

    /** @return boton para pausar y reanudar la animacion */
    public JButton getBotonPausar() {
        return botonPausar;
    }

    /** @return boton para resetear el laberinto */
    public JButton getBotonResetear() {
        return botonResetear;
    }

    /**
     * Actualiza el texto de la etiqueta de estado.
     * @param estado texto a mostrar
     */
    public void setLabelEstado(String estado) {
        labelEstado.setText("Estado: " + estado);
    }

    /**
     * Actualiza el texto de la etiqueta de paso.
     * @param paso numero de paso actual
     */
    public void setLabelPaso(int paso) {
        labelPaso.setText("Paso: " + paso);
    }
}