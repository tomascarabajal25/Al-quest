package Juego.ciudades.recoleccionEnMatriz.ui;

import Juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import Juego.Constantes;
import utils.ValidacionesUtiles;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class PanelHUD extends JPanel {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private CiudadRecoleccion juego;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     *
     * @param juego
     */
    public PanelHUD(CiudadRecoleccion juego) {
        setJuego(juego);
        setOpaque(true);  // ← agregá esto
        setPreferredSize(new Dimension(Constantes.ANCHO, Constantes.ALTO));
        setBackground(Constantes.COLOR_FONDO);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PanelHUD panelHUD = (PanelHUD) o;
        return Objects.equals(juego, panelHUD.juego);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(juego);
    }

    @Override
    public String toString() {
        return "PanelHUD{" +
                "juego=" + juego +
                '}';
    }

    //METODOS GENERALES ---------------------------------------------------------------------------------------

    /**
     *
     * @param g0 the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int y = 0;

        // Fondo
        g.setColor(Constantes.COLOR_FONDO);
        g.fillRect(0, 0, Constantes.ANCHO, getHeight());

        // Línea separadora izquierda
        g.setColor(Constantes.COLOR_SEPARADOR);
        g.fillRect(0, 0, 2, getHeight());

        y += 30;

        // Título del HUD
        y = dibujarTitulo(g, y);
        y += 10;

        // Separador
        y = dibujarSeparador(g, y);
        y += 15;

        // Stats del jugador
        y = dibujarStat(g, y, "PUNTOS", String.valueOf(juego.getPuntos()), Constantes.COLOR_PUNTOS_C);
        y += 8;
        y = dibujarStat(g, y, "VISIBILIDAD", String.valueOf(juego.getVisibilidad()), Constantes.COLOR_VISION);
        y += 8;
        y = dibujarStat(g, y, "DESPLAZAMIENTO", String.valueOf(juego.getDesplazamiento()), Constantes.COLOR_DESPLAZ);
        y += 8;
        y = dibujarStat(g, y, "NIVEL", nivelTexto(), Constantes.COLOR_VALOR);
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

    /**
     *
     * @param g
     * @param y
     * @return
     */
    private int dibujarTitulo(Graphics2D g, int y) {
        g.setFont(new Font("Monospaced", Font.BOLD, 15));
        g.setColor(Constantes.COLOR_TITULO);
        g.drawString("AL-QUEST", 20, y);
        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        g.setColor(Constantes.COLOR_LABEL);
        g.drawString("Recolección en Matriz", 20, y + 16);
        return y + 30;
    }

    /**
     *
     * @param g
     * @param y
     * @return
     */
    private int dibujarSeparador(Graphics2D g, int y) {
        g.setColor(Constantes.COLOR_SEPARADOR);
        g.fillRect(12, y, Constantes.ANCHO - 24, 1);
        return y + 1;
    }

    /**
     *
     * @param g
     * @param y
     * @param label
     * @param valor
     * @param colorValor
     * @return
     */
    private int dibujarStat(Graphics2D g, int y, String label, String valor, Color colorValor) {
        g.setFont(new Font("Monospaced", Font.PLAIN, 10));
        g.setColor(Constantes.COLOR_LABEL);
        g.drawString(label, 20, y);

        g.setFont(new Font("Monospaced", Font.BOLD, 16));
        g.setColor(colorValor);
        g.drawString(valor, 20, y + 18);
        return y + 32;
    }

    /**
     *
     * @param g
     * @param y
     * @return
     */
    private int dibujarSeccionMochila(Graphics2D g, int y) {
        g.setFont(new Font("Monospaced", Font.BOLD, 11));
        g.setColor(Constantes.COLOR_TITULO);
        g.drawString("MOCHILA", 20, y);
        y += 18;

        var items = juego.getItemsMochila();
        int i = 1;
        for (var el : items) {
            g.setFont(new Font("Monospaced", Font.PLAIN, 11));
            g.setColor(Constantes.COLOR_VALOR);
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
        g.setColor(Constantes.COLOR_LABEL);
        g.drawString("Capacidad: " + (i-1) + " / 3", 20, y);
        return y + 14;
    }

    /**
     *
     * @param g
     * @param y
     * @return
     */
    private int dibujarLeyenda(Graphics2D g, int y) {
        g.setFont(new Font("Monospaced", Font.BOLD, 11));
        g.setColor(Constantes.COLOR_TITULO);
        g.drawString("LEYENDA", 20, y);
        y += 16;

        y = dibujarItemLeyenda(g, y, Constantes.COLOR_JUGADOR,  "Jugador");
        y = dibujarItemLeyenda(g, y, Constantes.COLOR_VISION,   "Carta Visión");
        y = dibujarItemLeyenda(g, y, Constantes.COLOR_DESPLAZ,  "Carta Desplaz.");
        y = dibujarItemLeyenda(g, y, Constantes.COLOR_PUNTOS_C, "Carta Puntos");
        return y;
    }

    /**
     *
     * @param g
     * @param y
     * @param color
     * @param texto
     * @return
     */
    private int dibujarItemLeyenda(Graphics2D g, int y, Color color, String texto) {
        // Cuadradito de color
        g.setColor(color);
        g.fillRoundRect(20, y - 10, 14, 14, 4, 4);
        g.setFont(new Font("Monospaced", Font.PLAIN, 11));
        g.setColor(Constantes.COLOR_LABEL);
        g.drawString(texto, 42, y);
        return y + 18;
    }

    /**
     *
     * @param g
     * @param y
     */
    private void dibujarControles(Graphics2D g, int y) {
        g.setFont(new Font("Monospaced", Font.BOLD, 11));
        g.setColor(Constantes.COLOR_TITULO);
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
        g.setColor(Constantes.COLOR_LABEL);
        for (String ctrl : controles) {
            g.drawString(ctrl, 20, y);
            y += 14;
        }
    }

    /**
     *
     * @return
     */
    private String nivelTexto() {
        int[] pos = juego.getPosicionJugador();
        if (pos == null) return "?";
        return pos[2] + " / " + juego.getNiveles();
    }
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
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
}
