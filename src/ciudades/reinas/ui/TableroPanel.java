package ciudades.reinas.ui;

import ciudades.reinas.Accion;
import ciudades.reinas.CiudadReinas;
import ciudades.reinas.Paso;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class TableroPanel extends JPanel{
    
    public static final int CASILLA = 80;  //pixeles por casilla
    public static final int BORDE = 16;
    public static final int ESQUINA = 96; //CASILLA + BORDE

    private final CiudadReinas ciudad;
    private final RecursosGraficos recursos;

    private int[][] tableroJugador; //tableroJugador[fila][columna] = 1 si hay reina, 0 si no
    private int tamanio;
    private final int filaJugadorInicial;
    private final int columnaJugadorInicial;

    private boolean solucionRevelada = false;
    private boolean juegoTerminado = false;

    private List<Paso> pasos;
    private int pasoActual = 0;
    private Timer timerAnimacion;

    private JButton btnListo;
    private JButton btnMostrarSolucion;
    private JButton btnReiniciar;

    public TableroPanel (CiudadReinas ciudad, int tamanio, int filaJugador, int columnaJugador){
        this.ciudad = ciudad;
        this.recursos = new RecursosGraficos ();
        this.tamanio = tamanio;
        this.tableroJugador = new int[tamanio][tamanio];
        this.filaJugadorInicial = filaJugador;
        this.columnaJugadorInicial = columnaJugador;

        //colocar la reina inicial del jugador
        tableroJugador[filaJugador][columnaJugador] = 1;

        configurarLayout();
        configurarBotones();
        configurarMouse(filaJugador, columnaJugador);

    }

    private void configurarLayout(){
        setLayout (new BorderLayout());
        int dimensionTablero = ESQUINA * 2 + tamanio * CASILLA; //96 * 2 + 8 * 80 = 832 para tablero 8x8
        setPreferredSize(new Dimension (dimensionTablero, dimensionTablero + 50));    //espacio para botones
    }

    private void configurarBotones(){
        JPanel panelBotones = new JPanel();

        btnListo = new JButton("Listo");
        btnMostrarSolucion = new JButton("Mostrar Solucion");
        btnReiniciar = new JButton("Reiniciar");
        btnReiniciar.setVisible(false); //solo aparece al perder

        btnListo.addActionListener (e -> validarTablero());
        btnMostrarSolucion.addActionListener (e -> mostrarSolucion());
        btnReiniciar.addActionListener (e -> reiniciar());

        panelBotones.add(btnListo);
        panelBotones.add (btnMostrarSolucion);
        panelBotones.add (btnReiniciar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void configurarMouse (int filaJugador, int columnaJugador){
        addMouseListener(new MouseAdapter(){

            @Override
            public void mouseClicked(MouseEvent e){
                if (juegoTerminado || solucionRevelada) return;

                int col = (e.getX() - ESQUINA) / CASILLA;
                int fila = (e.getY() - ESQUINA) / CASILLA;

                if (fila < 0 || fila >= tamanio || col < 0 || col >= tamanio) return;

                if (fila == filaJugador && col == columnaJugador) return;
                
                if (SwingUtilities.isLeftMouseButton(e)){
                    tableroJugador[fila][col] = 1;

                } else if (SwingUtilities.isRightMouseButton(e)){
                    tableroJugador[fila][col] = 0;
                }

                repaint();
            }
        });

    }

    private void validarTablero(){
        if (solucionRevelada){
            JOptionPane.showMessageDialog(this, "Revelaste la solucion. Reinicia para poder ganar", "No valido", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (ciudad.validarTableroJugador(tableroJugador)){
            juegoTerminado = true;
            btnListo.setEnabled(false);
            btnMostrarSolucion.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Ganaste!!!", "Victoria", JOptionPane.WARNING_MESSAGE);
            //aca se puede notificar al juego principal para desbloquear la siguiente ciudad
        }else {
            juegoTerminado = true;
            btnListo.setEnabled(false);
            btnMostrarSolucion.setText("Ver solucion");
            btnReiniciar.setVisible(true);
            JOptionPane.showMessageDialog(this, "Incorrecto. Puedes ver la solucion o reiniciar", "Game Over", JOptionPane.ERROR_MESSAGE);

        }
    }

    private void mostrarSolucion(){
        solucionRevelada = true;
        juegoTerminado = true;
        btnListo.setEnabled(false);
        btnReiniciar.setVisible(true);

        pasos = ciudad.obtenerPasos();
        pasoActual = 0;

        //limpiar tablero del jugador para mostrar la animacion limpia
        tableroJugador = new int [tamanio][tamanio];

        timerAnimacion = new Timer (400, e -> {
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

    private void reiniciar (){
        tableroJugador = new int[tamanio][tamanio];
        tableroJugador[filaJugadorInicial][columnaJugadorInicial] = 1;
        solucionRevelada = false;
        juegoTerminado = false;
        pasoActual = 0;
        
        if (timerAnimacion != null){
            timerAnimacion.stop();
        }

        btnListo.setEnabled(true);
        btnMostrarSolucion.setEnabled(true);
        btnMostrarSolucion.setText("Mostrar solucion");
        btnReiniciar.setVisible(false);

        repaint();
    }

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
