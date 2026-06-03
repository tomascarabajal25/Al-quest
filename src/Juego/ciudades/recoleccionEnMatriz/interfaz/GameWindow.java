package Juego.ciudades.recoleccionEnMatriz.interfaz;

import Juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelos.Jugador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Ventana principal del juego. Contiene el panel de juego y maneja
 * la entrada del teclado, reemplazando el loop bloqueante de consola.
 *
 * USO: En lugar de new CiudadRecoleccion(...), instanciar GameWindow.
 *      GameWindow crea internamente la CiudadRecoleccion y el panel.
 */
public class GameWindow extends JFrame implements KeyListener {

    // -------------------------------------------------------------------------
    // CONSTANTES
    // -------------------------------------------------------------------------
    private static final String TITULO = "Al-Quest - Recolección en Matriz";

    // -------------------------------------------------------------------------
    // ATRIBUTOS
    // -------------------------------------------------------------------------
    private final CiudadRecoleccion juego;
    private final PanelJuego panelJuego;

    // Panel de mochila (se muestra/oculta con P)
    private boolean mochilaVisible = false;

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------

    /**
     * Crea la ventana y arranca el juego.
     *
     * @param jugador       Jugador de la partida
     * @param filas         Filas del mapa
     * @param columnas      Columnas del mapa
     * @param niveles       Niveles del mapa
     * @param maxMochila    Capacidad máxima de la mochila
     */
    public GameWindow(Jugador jugador, int filas, int columnas, int niveles, int maxMochila) {
        super(TITULO);

        // Crear el modelo del juego (sin arrancar el loop de consola)
        this.juego = new CiudadRecoleccion(filas, columnas, niveles, maxMochila, jugador);

        // Crear el panel de renderizado
        this.panelJuego = new PanelJuego(juego, filas, columnas);

        // Configurar ventana
        configurarVentana();

        // Registrar teclado
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();

        setVisible(true);

        // Primer dibujado
        panelJuego.repaint();
    }

    // -------------------------------------------------------------------------
    // CONFIGURACION DE VENTANA
    // -------------------------------------------------------------------------

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

    // -------------------------------------------------------------------------
    // KEYLISTENER
    // -------------------------------------------------------------------------

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

    // -------------------------------------------------------------------------
    // VERIFICACION FIN DE JUEGO
    // -------------------------------------------------------------------------

    private void verificarFinDeJuego() {
        if (juego.estaFinalizado()) {
            panelJuego.repaint();
            // Pequeño delay para que se vea el último estado
            Timer timer = new Timer(800, ev -> mostrarPantallaFin());
            timer.setRepeats(false);
            timer.start();
        }
    }

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

    // -------------------------------------------------------------------------
    // PUNTO DE ENTRADA (para prueba standalone)
    // -------------------------------------------------------------------------

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Jugador jugador = new Jugador("Héroe");
            new GameWindow(
                    jugador,
                    Juego.Constantes.FILAS_MAPA,
                    Juego.Constantes.COLUMNAS_MAPA,
                    Juego.Constantes.NIVELES_MAPA,
                    Juego.Constantes.CAPACIDAD_MAXIMA_MOCHILA
            );
        });
    }
}
