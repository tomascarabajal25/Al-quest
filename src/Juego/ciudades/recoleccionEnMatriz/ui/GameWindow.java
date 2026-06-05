package Juego.ciudades.recoleccionEnMatriz.ui;

import Juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelos.Jugador;
import utils.ValidacionesUtiles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;

public class GameWindow extends JFrame implements KeyListener {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private static final String TITULO = "Al-Quest - Recolección en Matriz";
    private CiudadRecoleccion juego;
    private PanelJuego panelJuego;
    private boolean mochilaVisible = false;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * Constructor del TDA GameWindow. Crea la ventana e inicia el juego
     * @param jugador
     * @param filas
     * @param columnas
     * @param niveles
     * @param maxMochila
     */
    public GameWindow(Jugador jugador, int filas, int columnas, int niveles, int maxMochila) {
        super(TITULO);

        CiudadRecoleccion juego = new CiudadRecoleccion(filas, columnas, niveles, maxMochila, jugador);
        setJuego(juego);
        setPanelJuego(juego, filas, columnas);

        configurarVentana();
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        setVisible(true);
        panelJuego.repaint();
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GameWindow that = (GameWindow) o;
        return mochilaVisible == that.mochilaVisible && Objects.equals(juego, that.juego) && Objects.equals(panelJuego, that.panelJuego);
    }

    @Override
    public int hashCode() {
        return Objects.hash(juego, panelJuego, mochilaVisible);
    }

    @Override
    public String toString() {
        return "GameWindow{" +
                "juego=" + juego +
                ", panelJuego=" + panelJuego +
                ", mochilaVisible=" + mochilaVisible +
                '}';
    }

    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    /**
     * Configura la vetana del juego
     */
    private void configurarVentana() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // Agregá estas dos líneas:
        getContentPane().setBackground(new Color(15, 20, 35));
        ((JPanel) getContentPane()).setOpaque(true);

        add(panelJuego, BorderLayout.CENTER);

        PanelHUD hud = new PanelHUD(juego);
        panelJuego.setHUD(hud);
        add(hud, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Lee la entrada de teclado
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (juego.estaFinalizado()) return;

        char tecla = Character.toUpperCase(e.getKeyChar());

        switch (tecla) {
            case 'W', 'S', 'A', 'D' -> {
                juego.moverJugador(tecla);
                panelJuego.repaint();
                panelJuego.getHUD().repaint();
                verificarFinDeJuego();
            }
            case 'P' -> {
                mochilaVisible = !mochilaVisible;
                panelJuego.setMostrarMochila(mochilaVisible);
                panelJuego.repaint();
            }
            case 'E' -> {
                juego.recogerCarta();
                panelJuego.repaint();
                panelJuego.getHUD().repaint();
                verificarFinDeJuego();
            }
            default -> {
                // Si la mochila está abierta y se presiona un número, usar carta
                if (mochilaVisible && Character.isDigit(tecla) && tecla != '0') {
                    int slot = tecla - '0';
                    try {
                        juego.usarCartaMochila(slot);
                    } catch (RuntimeException ex) {
                        panelJuego.mostrarMensaje("Carta inválida: " + ex.getMessage());
                    }
                    panelJuego.repaint();
                    panelJuego.getHUD().repaint();
                }
                // Q cierra la mochila
                if (tecla == 'Q' && mochilaVisible) {
                    mochilaVisible = false;
                    panelJuego.setMostrarMochila(false);
                    panelJuego.repaint();
                }
            }
        }
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    /**
     * Muestra la pantalla final con los puntos
     */
    private void mostrarPantallaFin() {
        int puntos = juego.finalizar();
        JOptionPane.showMessageDialog(
                this,
                "¡Juego terminado!\nPuntos obtenidos: " + puntos,
                "Fin del juego",
                JOptionPane.INFORMATION_MESSAGE
        );
        dispose();
    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    /**
     * Verifica si el juego termino
     */
    private void verificarFinDeJuego() {
        if (juego.estaFinalizado()) {
            panelJuego.repaint();
            Timer timer = new Timer(800, ev -> mostrarPantallaFin());
            timer.setRepeats(false);
            timer.start();
        }
    }
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Getter del atributo panelJuego
     *
     * @return: Devuelve el atributo panelJuego
     */
    public PanelJuego getPanelJuego() {
        return panelJuego;
    }

    /**
     * Getter del atributo mochilaVisible
     *
     * @return: Devuelve true si la mochila esta visible, false si no lo esta
     */
    public boolean isMochilaVisible() {
        return mochilaVisible;
    }

    /**
     * Getter del atributo juego
     *
     * @return: Devuelve el atributo juego
     */
    public CiudadRecoleccion getJuego() {
        return juego;
    }

    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Setter del atributo juego
     *
     * PRE:
     * -Juego no debe ser nulo
     *
     * @param juego: juego de la ciudad
     */
    private void setJuego(CiudadRecoleccion juego) {
        ValidacionesUtiles.esDistintoDeNull(juego, "juego");
        this.juego = juego;
    }

    /**
     * Setter del atributo panelJuego
     *
     * PRE:
     * -Juego no debe ser nulo
     * -Filas y columnas deben ser mayores a cero
     *
     * @param juego: juego
     * @param filas: Filas del juego
     * @param columnas: columnas del juego
     */
    private void setPanelJuego(CiudadRecoleccion juego, int filas, int columnas) {
        ValidacionesUtiles.esDistintoDeNull(juego, "juego");
        ValidacionesUtiles.validarMayorACero(filas, "filas");
        ValidacionesUtiles.validarMayorACero(columnas, "columnas");
        this.panelJuego = new PanelJuego(juego, filas, columnas);
    }
}
