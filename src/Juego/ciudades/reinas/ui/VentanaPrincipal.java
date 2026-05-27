package Juego.ciudades.reinas.ui;

import Juego.ciudades.reinas.CiudadReinas;
import Juego.ciudades.reinas.VictoriaListener;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

public class VentanaPrincipal extends JFrame{


    private JPanel contenedor;
    private TableroPanel tableroPanel;
    private VictoriaListener victoriaListener;

    public VentanaPrincipal (VictoriaListener victoriaListener) {
        super("N-Reinas");
        this.victoriaListener = victoriaListener;

        contenedor = new JPanel(new BorderLayout());
        add(contenedor);

        mostrarSelectorInicial();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }
    
    private void iniciarConTamanio(int tamanio) {
        CiudadReinas ciudad = new CiudadReinas();
        // no llama a iniciarCiudad — lo hará el primer click del jugador

        contenedor.removeAll(); // limpiar todo, incluyendo el selector inicial

        tableroPanel = new TableroPanel(ciudad, tamanio, victoriaListener);
        contenedor.add(tableroPanel, BorderLayout.CENTER);
        contenedor.add(crearPanelDerecho(tamanio), BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        revalidate();
        repaint();
    }

    private void mostrarSelectorInicial() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel titulo = new JLabel("N-Reinas");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instruccion = new JLabel("Elegí el tamaño del tablero:");
        instruccion.setFont(new Font("Arial", Font.PLAIN, 14));
        instruccion.setAlignmentX(Component.CENTER_ALIGNMENT);

        Integer[] opciones = {4, 5, 6, 7, 8};
        JComboBox<Integer> combo = new JComboBox<>(opciones);
        combo.setSelectedItem(4);
        combo.setMaximumSize(new Dimension(100, 30));
        combo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnCrear = new JButton("Crear tablero");
        btnCrear.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCrear.addActionListener(e -> {
            int tamanio = (Integer) combo.getSelectedItem();
            iniciarConTamanio(tamanio);
        });

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(20));
        panel.add(instruccion);
        panel.add(Box.createVerticalStrut(10));
        panel.add(combo);
        panel.add(Box.createVerticalStrut(16));
        panel.add(btnCrear);

        contenedor.add(panel, BorderLayout.CENTER);
    }

    private JPanel crearPanelDerecho(int tamanioActual) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 20, 24, 20));
        panel.setPreferredSize(new Dimension(220, 0));

        // --- Reglas ---
        JLabel titulo = new JLabel("Reglas");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea reglas = new JTextArea(
            "Objetivo: colocar exactamente " + tamanioActual + " reinas\n" +
            "en el tablero sin que se ataquen.\n\n" +
            "• El primer click coloca la reina\n" +
            "  inicial, que quedará fija.\n\n" +
            "• Una reina por fila.\n\n" +
            "• Las reinas no pueden compartir\n" +
            "  columna ni diagonal.\n\n" +
            "Click izquierdo → colocar reina\n" +
            "Click derecho   → quitar reina"
        );
        reglas.setEditable(false);
        reglas.setLineWrap(true);
        reglas.setWrapStyleWord(true);
        reglas.setOpaque(false);
        reglas.setFont(new Font("Arial", Font.PLAIN, 13));
        reglas.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- Separador ---
        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separador.setAlignmentX(Component.LEFT_ALIGNMENT);

        // --- Selector de tamaño ---
        JLabel lblTamanio = new JLabel("Cambiar tablero");
        lblTamanio.setFont(new Font("Arial", Font.BOLD, 14));
        lblTamanio.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea aviso = new JTextArea("Cambiar el tamaño reiniciará el progreso actual.");
        aviso.setEditable(false);
        aviso.setLineWrap(true);
        aviso.setWrapStyleWord(true);
        aviso.setOpaque(false);
        aviso.setFont(new Font("Arial", Font.ITALIC, 12));
        aviso.setAlignmentX(Component.LEFT_ALIGNMENT);

        Integer[] opciones = {4, 5, 6, 7, 8};
        JComboBox<Integer> combo = new JComboBox<>(opciones);
        combo.setSelectedItem(tamanioActual);
        combo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        combo.setAlignmentX(Component.LEFT_ALIGNMENT);

        combo.addActionListener(e -> {
            int nuevoTamanio = (Integer) combo.getSelectedItem();
            if (nuevoTamanio == tamanioActual) return;

            int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Cambiar a tablero " + nuevoTamanio + "x" + nuevoTamanio + "?\nSe perderá el progreso actual.",
                "Confirmar cambio",
                JOptionPane.YES_NO_OPTION
            );
            if (respuesta == JOptionPane.YES_OPTION) {
                iniciarConTamanio(nuevoTamanio);
            } else {
                combo.setSelectedItem(tamanioActual);
            }
        });

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(12));
        panel.add(reglas);
        panel.add(Box.createVerticalStrut(20));
        panel.add(separador);
        panel.add(Box.createVerticalStrut(20));
        panel.add(lblTamanio);
        panel.add(Box.createVerticalStrut(8));
        panel.add(aviso);
        panel.add(Box.createVerticalStrut(10));
        panel.add(combo);

        return panel;
    }
}

