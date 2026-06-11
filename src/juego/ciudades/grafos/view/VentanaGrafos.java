package juego.ciudades.grafos.view;

import javax.swing.*;
import java.awt.*;

public class VentanaGrafos extends JFrame {
    private PanelGrafo panelGrafo;
    private PanelEntrada panelEntrada;
    private PanelAlgoritmo panelAlgoritmo;
    private PanelResultado panelResultado;

    public VentanaGrafos() {
        setTitle("Ciudad 7 - Grafos y Flujo");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);

        panelGrafo = new PanelGrafo();
        panelEntrada = new PanelEntrada();
        panelAlgoritmo = new PanelAlgoritmo();
        panelResultado = new PanelResultado();

        JPanel panelDerecho = new JPanel(new BorderLayout());
        panelDerecho.setPreferredSize(new Dimension(280, 0));
        panelDerecho.add(panelEntrada, BorderLayout.NORTH);
        panelDerecho.add(panelAlgoritmo, BorderLayout.CENTER);

        JPanel panelCentral = new JPanel(new BorderLayout());
        panelCentral.add(panelGrafo, BorderLayout.CENTER);
        panelCentral.add(panelResultado, BorderLayout.SOUTH);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelCentral, BorderLayout.CENTER);
        getContentPane().add(panelDerecho, BorderLayout.EAST);
    }

    public PanelGrafo getPanelGrafo() { return panelGrafo; }
    public PanelEntrada getPanelEntrada() { return panelEntrada; }
    public PanelAlgoritmo getPanelAlgoritmo() { return panelAlgoritmo; }
    public PanelResultado getPanelResultado() { return panelResultado; }

    public void mostrar() {
        setVisible(true);
    }
}
