package Juego.ciudades.recoleccionEnMatriz.interfaz;

import Juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;

import javax.swing.*;
import java.awt.*;

/**
 * Panel lateral derecho (HUD) que muestra:
 *  - Puntos actuales
 *  - Visibilidad y desplazamiento
 *  - Nivel actual
 *  - Leyenda de iconos
 *
 * Se actualiza llamando a repaint() desde GameWindow cada vez que cambia el estado.
 */
public class PanelHUD extends JPanel {

    // -------------------------------------------------------------------------
    // CONSTANTES
    // -------------------------------------------------------------------------
    private static final int ANCHO  = 220;
    private static final int ALTO   = 720;

    // Colores
    private static final Color COLOR_FONDO      = new Color(10, 14, 28);
    private static final Color COLOR_TITULO     = new Color(100, 160, 255);
    private static final Color COLOR_LABEL      = new Color(160, 180, 220);
    private static final Color COLOR_VALOR      = new Color(230, 240, 255);
    private static final Color COLOR_SEPARADOR  = new Color(40, 55, 85);
    private static final Color COLOR_VISION     = new Color(100, 180, 255);
    private static final Color COLOR_DESPLAZ    = new Color(255, 200, 60);
    private static final Color COLOR_PUNTOS_C   = new Color(255, 100, 100);
    private static final Color COLOR_JUGADOR    = new Color(80, 200, 120);

    // -------------------------------------------------------------------------
    // ATRIBUTOS
    // -------------------------------------------------------------------------
    private final CiudadRecoleccion juego;

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------

    public PanelHUD(CiudadRecoleccion juego) {
        this.juego = juego;
        setOpaque(true);  // ← agregá esto
        setPreferredSize(new Dimension(ANCHO, ALTO));
        setBackground(COLOR_FONDO);
    }

    // -------------------------------------------------------------------------
    // PINTADO
    // -------------------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int y = 0;

        // Fondo
        g.setColor(COLOR_FONDO);
        g.fillRect(0, 0, ANCHO, getHeight());

        // Línea separadora izquierda
        g.setColor(COLOR_SEPARADOR);
        g.fillRect(0, 0, 2, getHeight());

        y += 30;

        // Título del HUD
        y = dibujarTitulo(g, y);
        y += 10;

        // Separador
        y = dibujarSeparador(g, y);
        y += 15;

        // Stats del jugador
        y = dibujarStat(g, y, "PUNTOS",         String.valueOf(juego.getPuntos()),      COLOR_PUNTOS_C);
        y += 8;
        y = dibujarStat(g, y, "VISIBILIDAD",    String.valueOf(juego.getVisibilidad()), COLOR_VISION);
        y += 8;
        y = dibujarStat(g, y, "DESPLAZAMIENTO", String.valueOf(juego.getDesplazamiento()), COLOR_DESPLAZ);
        y += 8;
        y = dibujarStat(g, y, "NIVEL",          nivelTexto(),                           COLOR_VALOR);
        y += 15;

        // Separador
        y = dibujarSeparador(g, y);
        y += 15;

        // Mochila
        y = dibujarSeccionMochila(g, y);
        y += 15;

        // Separador
        y = dibujarSeparador(g, y);
        y += 15;

        // Leyenda de iconos
        y = dibujarLeyenda(g, y);
        y += 15;

        // Separador
        y = dibujarSeparador(g, y);
        y += 15;

        // Controles
        dibujarControles(g, y);
    }

    // -------------------------------------------------------------------------
    // SECCIONES DEL HUD
    // -------------------------------------------------------------------------

    private int dibujarTitulo(Graphics2D g, int y) {
        g.setFont(new Font("Monospaced", Font.BOLD, 15));
        g.setColor(COLOR_TITULO);
        g.drawString("AL-QUEST", 20, y);
        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        g.setColor(COLOR_LABEL);
        g.drawString("Recolección en Matriz", 20, y + 16);
        return y + 30;
    }

    private int dibujarSeparador(Graphics2D g, int y) {
        g.setColor(COLOR_SEPARADOR);
        g.fillRect(12, y, ANCHO - 24, 1);
        return y + 1;
    }

    private int dibujarStat(Graphics2D g, int y, String label, String valor, Color colorValor) {
        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g.setColor(COLOR_LABEL);
        g.drawString(label, 20, y);

        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.setColor(colorValor);
        g.drawString(valor, 20, y + 18);
        return y + 32;
    }

    private int dibujarSeccionMochila(Graphics2D g, int y) {
        g.setFont(new Font("Monospaced", Font.BOLD, 11));
        g.setColor(COLOR_TITULO);
        g.drawString("MOCHILA", 20, y);
        y += 18;

        var items = juego.getItemsMochila();
        int i = 1;
        for (var el : items) {
            g.setFont(new Font("Monospaced", Font.PLAIN, 11));
            g.setColor(COLOR_VALOR);
            g.drawString("[" + i + "] " + el.getNombre(), 20, y);
            y += 16;
            i++;
        }

        if (i == 1) {
            g.setFont(new Font("Monospaced", Font.ITALIC, 11));
            g.setColor(new Color(100, 110, 140));
            g.drawString("(vacía)", 20, y);
            y += 16;
        }

        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g.setColor(COLOR_LABEL);
        g.drawString("Capacidad: " + (i-1) + " / 3", 20, y);
        return y + 14;
    }

    private int dibujarLeyenda(Graphics2D g, int y) {
        g.setFont(new Font("Monospaced", Font.BOLD, 11));
        g.setColor(COLOR_TITULO);
        g.drawString("LEYENDA", 20, y);
        y += 16;

        y = dibujarItemLeyenda(g, y, COLOR_JUGADOR,  "Jugador");
        y = dibujarItemLeyenda(g, y, COLOR_VISION,   "Carta Visión");
        y = dibujarItemLeyenda(g, y, COLOR_DESPLAZ,  "Carta Desplaz.");
        y = dibujarItemLeyenda(g, y, COLOR_PUNTOS_C, "Carta Puntos");
        return y;
    }

    private int dibujarItemLeyenda(Graphics2D g, int y, Color color, String texto) {
        // Cuadradito de color
        g.setColor(color);
        g.fillRoundRect(20, y - 10, 14, 14, 4, 4);
        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        g.setColor(COLOR_LABEL);
        g.drawString(texto, 42, y);
        return y + 18;
    }

    private void dibujarControles(Graphics2D g, int y) {
        g.setFont(new Font("Monospaced", Font.BOLD, 11));
        g.setColor(COLOR_TITULO);
        g.drawString("CONTROLES", 20, y);
        y += 16;

        String[] controles = {
                "W / S / A / D  mover",
                "E              recoger carta",  // ← nuevo
                "P              mochila",
                "1 / 2 / 3      usar carta",
                "Q              cerrar mochila"
        };

        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g.setColor(COLOR_LABEL);
        for (String ctrl : controles) {
            g.drawString(ctrl, 20, y);
            y += 14;
        }
    }

    // -------------------------------------------------------------------------
    // UTIL
    // -------------------------------------------------------------------------

    private String nivelTexto() {
        int[] pos = juego.getPosicionJugador();
        if (pos == null) return "?";
        return pos[2] + " / " + juego.getNiveles();
    }
}
