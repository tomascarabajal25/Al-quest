package juego.ciudades.grafos.view;

import javax.swing.*;
import java.awt.*;

public class PanelAlgoritmo extends JPanel {
    private JButton btnFlujoMaximo;
    private JButton btnCaminoMinimo;
    private JButton btnPasoAnterior;
    private JButton btnPasoSiguiente;
    private JButton btnAutoPlay;
    private JButton btnDetener;
    private JLabel lblEstado;
    private JSlider sliderVelocidad;
    private JTextField txtOrigenCamino;
    private JTextField txtDestinoCamino;

    public PanelAlgoritmo() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Algoritmos"));

        add(crearSeccionFlujo());
        add(Box.createVerticalStrut(8));
        add(crearSeccionCamino());
        add(Box.createVerticalStrut(8));
        add(crearSeccionNavegacion());
        add(Box.createVerticalStrut(8));
        add(crearSeccionVelocidad());
        add(Box.createVerticalStrut(8));
        add(crearSeccionEstado());
    }

    private JPanel crearSeccionFlujo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Ford-Fulkerson"));
        btnFlujoMaximo = new JButton("Resolver Flujo Maximo");
        panel.add(btnFlujoMaximo);
        return panel;
    }

    private JPanel crearSeccionCamino() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Dijkstra - Camino Minimo"));

        JPanel fila1 = new JPanel(new GridLayout(1, 4, 5, 0));
        txtOrigenCamino = new JTextField();
        txtDestinoCamino = new JTextField();
        fila1.add(new JLabel("Origen:"));
        fila1.add(txtOrigenCamino);
        fila1.add(new JLabel("Destino:"));
        fila1.add(txtDestinoCamino);

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnCaminoMinimo = new JButton("Resolver Camino Minimo");
        fila2.add(btnCaminoMinimo);

        panel.add(fila1);
        panel.add(fila2);
        return panel;
    }

    private JPanel crearSeccionNavegacion() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.setBorder(BorderFactory.createTitledBorder("Navegacion de Pasos"));
        btnPasoAnterior = new JButton("◀ Anterior");
        btnPasoSiguiente = new JButton("Siguiente ▶");
        btnAutoPlay = new JButton("▶ Auto");
        btnDetener = new JButton("■ Stop");
        btnPasoAnterior.setEnabled(false);
        btnPasoSiguiente.setEnabled(false);
        btnAutoPlay.setEnabled(false);
        btnDetener.setEnabled(false);
        panel.add(btnPasoAnterior);
        panel.add(btnPasoSiguiente);
        panel.add(btnAutoPlay);
        panel.add(btnDetener);
        return panel;
    }

    private JPanel crearSeccionVelocidad() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("Velocidad:"));
        sliderVelocidad = new JSlider(200, 2000, 800);
        sliderVelocidad.setMajorTickSpacing(600);
        sliderVelocidad.setPaintTicks(true);
        panel.add(sliderVelocidad);
        return panel;
    }

    private JPanel crearSeccionEstado() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblEstado = new JLabel("Estado: Construyendo grafo");
        panel.add(lblEstado);
        return panel;
    }

    public JButton getBtnFlujoMaximo() { return btnFlujoMaximo; }
    public JButton getBtnCaminoMinimo() { return btnCaminoMinimo; }
    public JButton getBtnPasoAnterior() { return btnPasoAnterior; }
    public JButton getBtnPasoSiguiente() { return btnPasoSiguiente; }
    public JButton getBtnAutoPlay() { return btnAutoPlay; }
    public JButton getBtnDetener() { return btnDetener; }
    public int getVelocidad() { return sliderVelocidad.getValue(); }
    public String getOrigenCamino() { return txtOrigenCamino.getText().trim(); }
    public String getDestinoCamino() { return txtDestinoCamino.getText().trim(); }

    public void setEstado(String texto) {
        lblEstado.setText("Estado: " + texto);
    }

    public void setNavegacionHabilitada(boolean habilitada) {
        btnPasoAnterior.setEnabled(habilitada);
        btnPasoSiguiente.setEnabled(habilitada);
        btnAutoPlay.setEnabled(habilitada);
        btnDetener.setEnabled(false);
    }

    public void setAutoPlayActivo(boolean activo) {
        btnAutoPlay.setEnabled(!activo);
        btnDetener.setEnabled(activo);
    }

    public void setAlgoritmosHabilitados(boolean habilitados) {
        btnFlujoMaximo.setEnabled(habilitados);
        btnCaminoMinimo.setEnabled(habilitados);
    }
}
