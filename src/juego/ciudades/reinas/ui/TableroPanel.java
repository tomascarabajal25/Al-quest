package juego.ciudades.reinas.ui;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import juego.ciudades.reinas.Accion;
import juego.ciudades.reinas.CiudadReinas;
import juego.ciudades.reinas.Paso;

public class TableroPanel extends JPanel{
    private Runnable victoriaListener;
    
    public static final int CASILLA = 80;  //pixeles por casilla
    public static final int BORDE = 16;
    public static final int ESQUINA = 96; //CASILLA + BORDE

    private final CiudadReinas ciudad;
    private final RecursosGraficos recursos;

    private int[][] tableroJugador; //tableroJugador[fila][columna] = 1 si hay reina, 0 si no
    private int tamanio;
    private int filaJugadorInicial;
    private int columnaJugadorInicial;

    private boolean solucionRevelada = false;
    private boolean juegoTerminado = false;

    private List<Paso> pasos;
    private int pasoActual = 0;
    private boolean esperandoPrimeraReina = true;
    private Timer timerAnimacion;

    private JButton btnListo;
    private JButton btnMostrarSolucion;
    private JButton btnReiniciar;

    /**
     * Panel visual del tablero de N-Reinas.
     * Maneja la interacción del jugador, la animación de la solución y la validación.
     *
     * @param ciudad referencia a la lógica del juego
     * @param tamanio dimensión del tablero (N x N)
     * @param victoriaListener callback que se ejecuta cuando el jugador gana
     */
    public TableroPanel(CiudadReinas ciudad, int tamanio, Runnable victoriaListener) {
        this.ciudad = ciudad;
        this.recursos = new RecursosGraficos();
        this.tamanio = tamanio;
        this.tableroJugador = new int[tamanio][tamanio];
        this.victoriaListener = victoriaListener;
        // no se coloca reina todavía

        configurarLayout();
        configurarBotones();
        configurarMouse();
    }

    /**
     * Configura el tamaño preferido del panel según la dimensión del tablero.
     */
    private void configurarLayout(){
        setLayout (new BorderLayout());
        int dimensionTablero = ESQUINA * 2 + tamanio * CASILLA; //96 * 2 + 8 * 80 = 832 para tablero 8x8
        setPreferredSize(new Dimension (dimensionTablero, dimensionTablero));    //espacio para botones
    }

    /**
     * Crea y agrega los botones Listo, Mostrar Solución y Reiniciar.
     * Los botones arrancan ocultos hasta que se coloque la primera reina.
     */
    private void configurarBotones(){
        JPanel panelBotones = new JPanel();

        btnListo = new JButton("Listo");
        btnListo.setVisible(false);
        btnMostrarSolucion = new JButton("Mostrar solucion");
        btnMostrarSolucion.setVisible(false);
        btnReiniciar = new JButton("Reiniciar");
        btnReiniciar.setVisible(false);

        btnListo.addActionListener (e -> validarTablero());
        btnMostrarSolucion.addActionListener (e -> mostrarSolucion());
        btnReiniciar.addActionListener (e -> reiniciar());

        panelBotones.add(btnListo);
        panelBotones.add (btnMostrarSolucion);
        panelBotones.add (btnReiniciar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Registra el listener de mouse para colocar y quitar reinas.
     * El primer click izquierdo coloca la reina inicial y la bloquea.
     * Los clicks siguientes colocan (izquierdo) o quitan (derecho) reinas,
     * respetando el límite de N reinas en total.
     */
    private void configurarMouse() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (juegoTerminado || solucionRevelada){
                    return;
                }

                int col = (e.getX() - ESQUINA) / CASILLA;
                int fila = (e.getY() - ESQUINA) / CASILLA;

                if (fila < 0 || fila >= tamanio || col < 0 || col >= tamanio){
                    return;
                }

                if (esperandoPrimeraReina) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        // colocar y bloquear primera reina
                        filaJugadorInicial = fila;
                        columnaJugadorInicial = col;
                        tableroJugador[fila][col] = 1;
                        ciudad.iniciarCiudad(tamanio, fila, col);
                        esperandoPrimeraReina = false;
                        btnListo.setVisible(true);
                        btnMostrarSolucion.setVisible(true);
                        btnReiniciar.setVisible(true);
                        repaint();
                    }
                    return; // ignorar click derecho hasta que haya primera reina
                }

                // comportamiento normal
                if (fila == filaJugadorInicial && col == columnaJugadorInicial) {
                    return;
                }

                if (SwingUtilities.isLeftMouseButton(e)) {
                    int reinasActuales = 0;
                    for (int f = 0; f < tamanio; f++)
                        for (int c = 0; c < tamanio; c++)
                            if (tableroJugador[f][c] == 1) reinasActuales++;

                    if (reinasActuales < tamanio) {
                        tableroJugador[fila][col] = 1;
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    tableroJugador[fila][col] = 0;
                }

                repaint();
            }
        });
    }


    /**
     * Verifica si el tablero actual es una solución válida.
     * Si es correcto notifica la victoria. Si no, habilita ver la solución.
     */
    private void validarTablero(){
        if (solucionRevelada){
            JOptionPane.showMessageDialog(this, "Revelaste la solucion. Reinicia para poder ganar", "No valido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (ciudad.validarTableroJugador(tableroJugador)) {
            juegoTerminado = true;
            btnListo.setEnabled(false);
            btnMostrarSolucion.setEnabled(false);
            btnReiniciar.setVisible(false);
            JOptionPane.showMessageDialog(this, "Ganaste!!!", "Victoria", JOptionPane.INFORMATION_MESSAGE);

            if (victoriaListener != null) {
                victoriaListener.run();
            }

        } else {
            juegoTerminado = true;
            btnListo.setEnabled(false);
            btnMostrarSolucion.setText("Ver solucion");
            btnMostrarSolucion.setVisible(true);
            btnReiniciar.setVisible(true);
            JOptionPane.showMessageDialog(this, "Incorrecto. Puedes ver la solucion o reiniciar", "Game Over", JOptionPane.ERROR_MESSAGE);

        }
    }

    /**
     * Anima el proceso de resolución del backtracking sobre el tablero actual.
     * Respeta las reinas ya colocadas por el jugador.
     * Si el estado actual no tiene solución posible, muestra un aviso.
     */
    private void mostrarSolucion(){
        if (timerAnimacion != null && timerAnimacion.isRunning()){
            timerAnimacion.stop();
        }
    
        ciudad.actualizarTableroJugador(tableroJugador, filaJugadorInicial, columnaJugadorInicial);
        pasos = ciudad.obtenerPasos();

        if (pasos == null){
            JOptionPane.showMessageDialog(this, "Las reinas colocadas no permiten completar el tablero.\n\n" +
            "Reinicie e intente en otra posicion", "Sin solucion posible", JOptionPane.WARNING_MESSAGE
            );

            return; //no anima nada
        }

        int[] reinasAceptadas = ciudad.getReinasTablero();
        tableroJugador = new int[tamanio][tamanio];
        for (int i = 0; i < tamanio; i++){
            if (reinasAceptadas[i] != -1){
                tableroJugador[i][reinasAceptadas[i]] = 1;
            }
        }

        pasoActual = 0;

        solucionRevelada = true;
        juegoTerminado = true;
        btnListo.setEnabled(false);
        btnMostrarSolucion.setVisible(false);
        btnReiniciar.setVisible(true);

        timerAnimacion = new Timer (300, e -> {
            btnListo.setEnabled(false);
            btnMostrarSolucion.setEnabled(false);

            if (pasoActual >= pasos.size()) {
                timerAnimacion.stop();
                return;
            }
        
            Paso paso = pasos.get(pasoActual);
            if(paso.getAccion() == Accion.COLOCAR){
                tableroJugador [paso.getFila()][paso.getColumna()] = 1;

            } else {
                tableroJugador[paso.getFila()][paso.getColumna()] = 0;
            }

            pasoActual ++;
            repaint();
    
        });

        timerAnimacion.start();
    }

    /**
     * Reinicia el tablero al estado inicial: vacío, esperando la primera reina.
     * Detiene la animación si estaba en curso.
     */
    private void reiniciar() {
        if (timerAnimacion != null) timerAnimacion.stop();

        tableroJugador = new int[tamanio][tamanio];
        esperandoPrimeraReina = true;   // ← volver a esperar
        filaJugadorInicial = -1;
        columnaJugadorInicial = -1;
        solucionRevelada = false;
        juegoTerminado = false;
        pasoActual = 0;

        btnListo.setEnabled(true);
        btnListo.setVisible(false);
        btnMostrarSolucion.setEnabled(true);
        btnMostrarSolucion.setVisible(false);
        btnMostrarSolucion.setText("Mostrar solucion");
        btnReiniciar.setEnabled(true);

        repaint();
    }

    /**
     * Dibuja el tablero con sus casillas e imágenes de reinas.
     * Alterna casillas claras y oscuras según la paridad de fila+columna.
     *
     * @param g contexto gráfico provisto por Swing
     */
    @Override
    protected void paintComponent (Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // ---CASILLAS ---
        for (int fila = 0; fila < tamanio; fila++){
            for (int col = 0; col < tamanio; col++){
                boolean esClara = (fila + col) % 2 == 0;
                int x = ESQUINA + col * CASILLA;
                int y = ESQUINA + fila * CASILLA;

                Image img =tableroJugador[fila][col] == 1
                    ? (esClara ? recursos.getReinaFondoClaroImg() : recursos.getReinaFondoOscuroImg())
                    : (esClara ? recursos.getCasillaClaraImg() : recursos.getCasillaOscuraImg());

                g.drawImage(img, x, y, CASILLA, CASILLA, this);
            }
        }
    }
}
