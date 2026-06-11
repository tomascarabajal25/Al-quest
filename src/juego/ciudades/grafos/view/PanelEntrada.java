package juego.ciudades.grafos.view;

import javax.swing.*;
import java.awt.*;

public class PanelEntrada extends JPanel {
    private JTextField txtVertice;
    private JTextField txtOrigen;
    private JTextField txtDestino;
    private JTextField txtCapacidad;
    private JComboBox<String> cbFuente;
    private JComboBox<String> cbSumidero;
    private JButton btnAgregarVertice;
    private JButton btnAgregarArista;
    private JButton btnLimpiar;

    public PanelEntrada() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Construccion del Grafo"));
        setPreferredSize(new Dimension(250, 0));

        add(crearSeccionVertice());
        add(Box.createVerticalStrut(10));
        add(crearSeccionArista());
        add(Box.createVerticalStrut(10));
        add(crearSeccionFuenteSumidero());
        add(Box.createVerticalStrut(10));
        add(crearSeccionAcciones());
    }

    private JPanel crearSeccionVertice() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBorder(BorderFactory.createTitledBorder("Vertice"));
        txtVertice = new JTextField(6);
        btnAgregarVertice = new JButton("+ Vertice");
        panel.add(new JLabel("Nombre:"));
        panel.add(txtVertice);
        panel.add(btnAgregarVertice);
        return panel;
    }

    private JPanel crearSeccionArista() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Arista"));

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtOrigen = new JTextField(4);
        txtDestino = new JTextField(4);
        fila1.add(new JLabel("Origen:"));
        fila1.add(txtOrigen);
        fila1.add(new JLabel("Destino:"));
        fila1.add(txtDestino);

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtCapacidad = new JTextField(4);
        btnAgregarArista = new JButton("+ Arista");
        fila2.add(new JLabel("Capacidad:"));
        fila2.add(txtCapacidad);
        fila2.add(btnAgregarArista);

        panel.add(fila1);
        panel.add(fila2);
        return panel;
    }

    private JPanel crearSeccionFuenteSumidero() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Fuente y Sumidero"));

        cbFuente = new JComboBox<>();
        cbSumidero = new JComboBox<>();

        JPanel fila1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila1.add(new JLabel("Fuente:"));
        fila1.add(cbFuente);

        JPanel fila2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        fila2.add(new JLabel("Sumidero:"));
        fila2.add(cbSumidero);

        panel.add(fila1);
        panel.add(fila2);
        return panel;
    }

    private JPanel crearSeccionAcciones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnLimpiar = new JButton("Limpiar todo");
        panel.add(btnLimpiar);
        return panel;
    }

    public void actualizarCombos(java.util.List<String> vertices) {
        String fuenteSel = (String) cbFuente.getSelectedItem();
        String sumideroSel = (String) cbSumidero.getSelectedItem();

        cbFuente.removeAllItems();
        cbSumidero.removeAllItems();
        for (String v : vertices) {
            cbFuente.addItem(v);
            cbSumidero.addItem(v);
        }

        if (fuenteSel != null && vertices.contains(fuenteSel)) cbFuente.setSelectedItem(fuenteSel);
        if (sumideroSel != null && vertices.contains(sumideroSel)) cbSumidero.setSelectedItem(sumideroSel);
    }

    public String getVertice() { return txtVertice.getText().trim(); }
    public String getOrigen() { return txtOrigen.getText().trim(); }
    public String getDestino() { return txtDestino.getText().trim(); }
    public String getFuente() { return (String) cbFuente.getSelectedItem(); }
    public String getSumidero() { return (String) cbSumidero.getSelectedItem(); }

    public int getCapacidad() {
        try {
            return Integer.parseInt(txtCapacidad.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public JButton getBtnAgregarVertice() { return btnAgregarVertice; }
    public JButton getBtnAgregarArista() { return btnAgregarArista; }
    public JButton getBtnLimpiar() { return btnLimpiar; }

    public void limpiarCampos() {
        txtVertice.setText("");
        txtOrigen.setText("");
        txtDestino.setText("");
        txtCapacidad.setText("");
    }

    public void setEnabled(boolean enabled) {
        txtVertice.setEnabled(enabled);
        txtOrigen.setEnabled(enabled);
        txtDestino.setEnabled(enabled);
        txtCapacidad.setEnabled(enabled);
        cbFuente.setEnabled(enabled);
        cbSumidero.setEnabled(enabled);
        btnAgregarVertice.setEnabled(enabled);
        btnAgregarArista.setEnabled(enabled);
        btnLimpiar.setEnabled(enabled);
    }
}
