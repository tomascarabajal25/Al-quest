package juego.ciudades.complejidad.ui;

import juego.ciudades.complejidad.CiudadComplejidad;
import juego.ciudades.complejidad.PasoTeoremaMaestro;
import juego.ciudades.reinas.VictoriaListener;
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
    private JButton btnListo;
    private JButton btnReiniciar;

    private ButtonGroup grupoOpciones;
    private JRadioButton[] opcionesComplejidad;

    private boolean solucionRevelada = false;
    private boolean juegoTerminado = false;

    private static final String[] COMPLEJIDADES = {
        "O(1)", "O(log n)", "O(n)", "O(n log n)", "O(n^2)", "O(n^3)", "O(2^n)"
    };

    /**
     * @param ciudad referencia a la lógica del juego
     * @param victoriaListener callback que se ejecuta cuando el jugador acierta
     */
    public PanelComplejidad(CiudadComplejidad ciudad, VictoriaListener victoriaListener) {
        this.ciudad = ciudad;
        this.victoriaListener = victoriaListener;

        configurarLayout();
        configurarBotones();
    }

    /**
     * Configura el layout del panel:
     * norte: campo de ecuación, este: opciones de complejidad,
     * centro: área de pasos, sur: botones.
     */
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
        add(crearPanelOpciones(), BorderLayout.EAST);
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

    /**
     * Construye el panel de radio buttons con las opciones de complejidad.
     * Al seleccionar una opción se habilita el botón Listo,
     * siempre que no se haya revelado la solución ni terminado el juego.
     *
     * @return panel configurado con los radio buttons
     */
    private JPanel crearPanelOpciones() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        panel.setPreferredSize(new Dimension(180, 0));

        JLabel titulo = new JLabel("¿Qué complejidad tiene?");
        titulo.setFont(new Font("Arial", Font.BOLD, 13));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createVerticalStrut(10));

        grupoOpciones = new ButtonGroup();
        opcionesComplejidad = new JRadioButton[COMPLEJIDADES.length];

        for (int i = 0; i < COMPLEJIDADES.length; i++) {
            opcionesComplejidad[i] = new JRadioButton(COMPLEJIDADES[i]);
            opcionesComplejidad[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            grupoOpciones.add(opcionesComplejidad[i]);
            panel.add(opcionesComplejidad[i]);
            panel.add(Box.createVerticalStrut(5));

            opcionesComplejidad[i].addActionListener(e -> {
                if (!solucionRevelada && !juegoTerminado) btnListo.setEnabled(true);
            });

        }

        return panel;
    }

    /**
     * Crea y agrega los botones Resolver, Listo y Reiniciar.
     * Listo arranca deshabilitado hasta que se seleccione una opción.
     * Reiniciar arranca oculto.
     */
    private void configurarBotones() {
        JPanel panelBotones = new JPanel();

        btnResolver = new JButton("Resolver");
        btnListo = new JButton("Listo");
        btnReiniciar = new JButton("Reiniciar");

        btnListo.setEnabled(false);
        btnReiniciar.setVisible(false);

        btnResolver.addActionListener(e -> resolver());
        btnListo.addActionListener(e -> verificarRespuesta());
        btnReiniciar.addActionListener(e -> reiniciar());

        panelBotones.add(btnResolver);
        panelBotones.add(btnListo);
        panelBotones.add(btnReiniciar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Revela la solución animando el paso a paso del teorema maestro.
     * Marca la partida como solucionRevelada — el jugador ya no puede ganar.
     */
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
        solucionRevelada = true;
        btnListo.setEnabled(false);
        btnReiniciar.setVisible(true);

        ciudad.procesarEcuacion(entrada);
        pasos = ciudad.getPasos();
        pasoActual = 0;
        areaPasos.setText("");
        campoEcuacion.setEditable(false);
        btnResolver.setEnabled(false);

        timerAnimacion = new Timer(600, e -> {
            if (pasoActual >= pasos.size()) {
                timerAnimacion.stop();
                return;
            }
            areaPasos.append(pasos.get(pasoActual).getDescripcion() + "\n\n");
            pasoActual++;
        });

        timerAnimacion.start();
    }

    /**
     * Verifica si la complejidad seleccionada es correcta.
     * Anima el paso a paso y muestra el resultado al terminar.
     * Si acertó notifica la victoria, si no permite reiniciar.
     */
    private void verificarRespuesta() {
        String seleccion = obtenerSeleccion();

        if (seleccion == null) {
            JOptionPane.showMessageDialog(this,
                "Elegí una complejidad antes de continuar.",
                "Sin selección",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String respuestaCorrecta = ciudad.getResultado();
        boolean acerto = seleccion.equals(respuestaCorrecta);

        btnListo.setEnabled(false);
        btnReiniciar.setVisible(true);

        // mostrar paso a paso
        pasos = ciudad.getPasos();
        pasoActual = 0;

        timerAnimacion = new Timer(600, e -> {
            if (pasoActual >= pasos.size()) {
                timerAnimacion.stop();

                if (acerto) {
                    juegoTerminado = true;
                    btnListo.setEnabled(false);
                    JOptionPane.showMessageDialog(PanelComplejidad.this,
                        "¡Correcto! La complejidad es " + respuestaCorrecta,
                        "Victoria",
                        JOptionPane.INFORMATION_MESSAGE
                    );

                    if (victoriaListener != null) victoriaListener.onVictoria();

                } else {
                    juegoTerminado = true;
                    btnListo.setEnabled(false);
                    JOptionPane.showMessageDialog(PanelComplejidad.this,
                        "Incorrecto. La complejidad es " + respuestaCorrecta + "\nPodés reiniciar e intentarlo de nuevo.",
                        "Incorrecto",
                        JOptionPane.ERROR_MESSAGE
                    );
                }

                return;
            }

            areaPasos.append(pasos.get(pasoActual).getDescripcion() + "\n\n");
            pasoActual++;
        });

        timerAnimacion.start();
    }

    /**
     * @return texto del radio button seleccionado, o null si ninguno está seleccionado
     */
    private String obtenerSeleccion() {
        for (JRadioButton opcion : opcionesComplejidad) {
            if (opcion.isSelected()) return opcion.getText();
        }
        return null;
    }

    /**
     * Reinicia el panel al estado inicial.
     * Detiene la animación, limpia el área de pasos y resetea todos los flags.
     */
    private void reiniciar() {
        if (timerAnimacion != null) timerAnimacion.stop();

        areaPasos.setText("");
        campoEcuacion.setText("T(n) = 2T(n/2) + n");
        campoEcuacion.setEditable(true);
        pasoActual = 0;

        btnResolver.setEnabled(true);
        btnReiniciar.setVisible(false);

        grupoOpciones.clearSelection();
        btnListo.setEnabled(false);

        solucionRevelada = false;
        juegoTerminado = false;
    }
}
