package Juego.ciudades.complejidad.ui;

import Juego.ciudades.complejidad.CiudadComplejidad;
import Juego.ciudades.complejidad.PasoTeoremaMaestro;
import Juego.ciudades.reinas.VictoriaListener;
import java.awt.*;
import java.util.List;
import javax.swing.*;

public class PanelComplejidad extends JPanel {

    private final CiudadComplejidad ciudad;
    private final VictoriaListener victoriaListener;

    private List<PasoTeoremaMaestro> pasos;
    private int pasoActual = 0;
    private Timer timerAnimacion;

    private JTextField campoEcuacion;
    private JTextArea areaPasos;
    private JButton btnResolver;
    private JButton btnReiniciar;

    /**
     * @param ciudad referencia a la lógica del juego
     * @param victoriaListener callback que se ejecuta cuando el jugador resuelve correctamente
     */
    public PanelComplejidad(CiudadComplejidad ciudad, VictoriaListener victoriaListener) {
        this.ciudad = ciudad;
        this.victoriaListener = victoriaListener;

        configurarLayout();
        configurarBotones();
    }

    private void configurarLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setPreferredSize(new Dimension(600, 500));

        // campo de entrada
        JPanel panelEntrada = new JPanel(new BorderLayout(10, 0));
        JLabel lblEcuacion = new JLabel("Ecuación:");
        campoEcuacion = new JTextField("T(n) = 2T(n/2) + n");
        panelEntrada.add(lblEcuacion, BorderLayout.WEST);
        panelEntrada.add(campoEcuacion, BorderLayout.CENTER);
        add(panelEntrada, BorderLayout.NORTH);

        // área de pasos
        areaPasos = new JTextArea();
        areaPasos.setEditable(false);
        areaPasos.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaPasos.setLineWrap(true);
        areaPasos.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(areaPasos);
        add(scroll, BorderLayout.CENTER);
    }

    private void configurarBotones() {
        JPanel panelBotones = new JPanel();

        btnResolver = new JButton("Resolver");
        btnReiniciar = new JButton("Reiniciar");
        btnReiniciar.setVisible(false);

        btnResolver.addActionListener(e -> resolver());
        btnReiniciar.addActionListener(e -> reiniciar());

        panelBotones.add(btnResolver);
        panelBotones.add(btnReiniciar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void resolver() {
        String entrada = campoEcuacion.getText();

        if (!ciudad.esEntradaValida(entrada)) {
            JOptionPane.showMessageDialog(this,
                "Formato inválido. Usá: T(n) = aT(n/b) + f(n)\nEjemplo: T(n) = 2T(n/2) + n",
                "Error de formato",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        ciudad.procesarEcuacion(entrada);
        pasos = ciudad.getPasos();
        pasoActual = 0;
        areaPasos.setText("");

        btnResolver.setEnabled(false);
        btnReiniciar.setVisible(true);
        campoEcuacion.setEditable(false);

        timerAnimacion = new Timer(600, e -> {
            if (pasoActual >= pasos.size()) {
                timerAnimacion.stop();
                if (victoriaListener != null) victoriaListener.onVictoria();
                return;
            }

            areaPasos.append(pasos.get(pasoActual).getDescripcion() + "\n\n");
            pasoActual++;
        });

        timerAnimacion.start();
    }

    private void reiniciar() {
        if (timerAnimacion != null) timerAnimacion.stop();

        areaPasos.setText("");
        campoEcuacion.setText("T(n) = 2T(n/2) + n");
        campoEcuacion.setEditable(true);
        pasoActual = 0;

        btnResolver.setEnabled(true);
        btnReiniciar.setVisible(false);
    }
}
