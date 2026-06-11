package juego.ciudades.grafos.view;

import javax.swing.*;
import java.awt.*;

public class PanelResultado extends JPanel {
    private JTextArea txtLog;
    private JLabel lblResultado;

    public PanelResultado() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Resultado"));
        setPreferredSize(new Dimension(0, 180));

        lblResultado = new JLabel(" ");
        lblResultado.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblResultado.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        txtLog = new JTextArea();
        txtLog.setEditable(false);
        txtLog.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtLog.setBackground(new Color(40, 40, 40));
        txtLog.setForeground(new Color(200, 200, 200));
        txtLog.setCaretColor(Color.WHITE);

        JScrollPane scroll = new JScrollPane(txtLog);
        scroll.setPreferredSize(new Dimension(0, 130));

        add(lblResultado, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
    }

    public void agregarLinea(String linea) {
        txtLog.append(linea + "\n");
        txtLog.setCaretPosition(txtLog.getDocument().getLength());
    }

    public void setResultado(String texto) {
        lblResultado.setText(texto);
    }

    public void limpiar() {
        txtLog.setText("");
        lblResultado.setText(" ");
    }
}
