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
    private JTextArea txtListaAdyacencia;
    private JButton btnAgregarVertice;
    private JButton btnAgregarArista;
    private JButton btnCargarLista;
    private JButton btnLimpiar;

    public PanelEntrada() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Construccion del Grafo"));

        add(crearSeccionVertice());
        add(Box.createVerticalStrut(10));
        add(crearSeccionArista());
        add(Box.createVerticalStrut(10));
        add(crearSeccionListaAdyacencia());
        add(Box.createVerticalStrut(10));
        add(crearSeccionFuenteSumidero());
        add(Box.createVerticalStrut(10));
        add(crearSeccionAcciones());
    }

    private JPanel crearSeccionVertice() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 5, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Vertice"));
        txtVertice = new JTextField();
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

        JPanel fila1 = new JPanel(new GridLayout(1, 4, 5, 0));
        txtOrigen = new JTextField();
        txtDestino = new JTextField();
        fila1.add(new JLabel("Origen:"));
        fila1.add(txtOrigen);
        fila1.add(new JLabel("Destino:"));
        fila1.add(txtDestino);

        JPanel fila2 = new JPanel(new GridLayout(1, 3, 5, 0));
        txtCapacidad = new JTextField();
        btnAgregarArista = new JButton("+ Arista");
        fila2.add(new JLabel("Capacidad:"));
        fila2.add(txtCapacidad);
        fila2.add(btnAgregarArista);

        panel.add(fila1);
        panel.add(fila2);
        return panel;
    }

    private JPanel crearSeccionListaAdyacencia() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Lista de Adyacencia"));

        JLabel lblFormato = new JLabel("Formato: [origen, destino, peso]  (una arista por linea)");
        lblFormato.setFont(new Font("SansSerif", Font.PLAIN, 10));

        txtListaAdyacencia = new JTextArea(3, 20);
        txtListaAdyacencia.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollLista = new JScrollPane(txtListaAdyacencia);

        btnCargarLista = new JButton("Cargar Lista");
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBoton.add(btnCargarLista);

        panel.add(lblFormato, BorderLayout.NORTH);
        panel.add(scrollLista, BorderLayout.CENTER);
        panel.add(panelBoton, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel crearSeccionFuenteSumidero() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createTitledBorder("Fuente y Sumidero"));

        cbFuente = new JComboBox<>();
        cbSumidero = new JComboBox<>();

        JPanel fila1 = new JPanel(new GridLayout(1, 2, 5, 0));
        fila1.add(new JLabel("Fuente:"));
        fila1.add(cbFuente);

        JPanel fila2 = new JPanel(new GridLayout(1, 2, 5, 0));
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
    public String getListaAdyacencia() { return txtListaAdyacencia.getText().trim(); }

    public int getCapacidad() {
        try {
            return Integer.parseInt(txtCapacidad.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public JButton getBtnAgregarVertice() { return btnAgregarVertice; }
    public JButton getBtnAgregarArista() { return btnAgregarArista; }
    public JButton getBtnCargarLista() { return btnCargarLista; }
    public JButton getBtnLimpiar() { return btnLimpiar; }

    public void limpiarCampos() {
        txtVertice.setText("");
        txtOrigen.setText("");
        txtDestino.setText("");
        txtCapacidad.setText("");
        txtListaAdyacencia.setText("");
    }

    public void setEnabled(boolean enabled) {
        txtVertice.setEnabled(enabled);
        txtOrigen.setEnabled(enabled);
        txtDestino.setEnabled(enabled);
        txtCapacidad.setEnabled(enabled);
        txtListaAdyacencia.setEnabled(enabled);
        btnAgregarVertice.setEnabled(enabled);
        btnAgregarArista.setEnabled(enabled);
        btnCargarLista.setEnabled(enabled);
    }
}