package Juego.ciudades.ordenamientos.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import Juego.ciudades.ordenamientos.Caja;
import modelosVista.ObjetoVista;
import modelosVista.Vista;

/**
 * Representación visual de una Caja en el mundo del juego.
 * Cada caja ocupa una celda del mundo y tiene una altura visual
 * proporcional a su tamaño lógico.
 *
 * Estados visuales:
 *   - Normal     → color base según altura
 *   - Seleccionada → borde amarillo parpadeante
 *   - Destacada  → borde rojo (involucrada en swap del resolver)
 *   - Correcta   → borde verde (ya en posición final)
 */
public class CajaVista extends ObjetoVista {

    // ── Constantes visuales ────────────────────────────────────────────────
    private static final Color COLOR_NORMAL_TOP    = new Color(70, 130, 180);
    private static final Color COLOR_NORMAL_BOT    = new Color(30,  80, 140);
    private static final Color COLOR_SELECCIONADA  = new Color(255, 220,  50);
    private static final Color COLOR_DESTACADA     = new Color(220,  50,  50);
    private static final Color COLOR_CORRECTA      = new Color( 60, 200,  90);
    private static final Color COLOR_SOMBRA        = new Color(  0,   0,   0, 80);
    private static final Color COLOR_TEXTO         = Color.WHITE;
    private static final Font  FONT_ETIQUETA       = new Font("Monospaced", Font.BOLD, 9);

    // ── Datos lógicos ──────────────────────────────────────────────────────
    private final Caja caja;
    /** Índice lógico dentro del arreglo de cajas (columna en el mundo) */
    private int indiceLogico;

    // ── Estado visual ──────────────────────────────────────────────────────
    private boolean seleccionada  = false;
    private boolean destacada     = false;
    private boolean correcta      = false;

    /** Contador para animación de parpadeo (selección) */
    private int tickParpadeo = 0;

    // ── Imagen de caja (opcional, si hay sprite) ──────────────────────────
    private BufferedImage spriteCaja;

    // ── Constructor ────────────────────────────────────────────────────────

    /**
     * Pre:
     * @param caja          distinto de null
     * @param worldX        posición X en el mundo (píxeles)
     * @param worldY        posición Y en el mundo (píxeles)
     * @param indiceLogico  posición en el arreglo lógico
     */
    public CajaVista(Caja caja, int worldX, int worldY, int indiceLogico) {
        super(worldX, worldY, caja.getNombre(), false, null);
        this.caja         = caja;
        this.indiceLogico = indiceLogico;
        intentarCargarSprite();
    }

    // ── Ciclo de juego ─────────────────────────────────────────────────────

    /**
     * Post: avanza las animaciones internas (parpadeo de selección)
     */
    public void actualizar() {
        tickParpadeo++;
    }

    // ── Dibujo ─────────────────────────────────────────────────────────────

    /**
     * Post: dibuja la caja en pantalla con su estado visual actual.
     *       La altura de la barra es proporcional al tamaño lógico de la caja
     *       respecto al tamaño de celda de la Vista.
     */
    @Override
    public void draw(Graphics2D g2, Vista vista) {
        if (!estaEnPantalla(vista)) return;

        // Guardar estado gráfico para no contaminar el render global
        java.awt.Paint    paintOrig  = g2.getPaint();
        java.awt.Stroke   strokeOrig = g2.getStroke();
        Object            aaOrig     = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int tamaño  = vista.getTamanio();
        int sx      = getScreenX(vista);
        int sy      = getScreenY(vista);

        // Altura proporcional: tamaño lógico / 50 * celda (máx = celda completa)
        int altoMax  = tamaño - 4;
        int altoReal = Math.max(8, Math.min(altoMax, (caja.getTamanio() * altoMax) / 50));
        int yDibujo  = sy + (tamaño - altoReal);

        // ── Sombra ────────────────────────────────────────────────────────
        g2.setColor(COLOR_SOMBRA);
        g2.fillRoundRect(sx + 3, yDibujo + 3, tamaño - 6, altoReal, 6, 6);

        // ── Cuerpo de la caja ─────────────────────────────────────────────
        if (spriteCaja != null) {
            g2.drawImage(spriteCaja, sx + 2, yDibujo, tamaño - 4, altoReal, null);
        } else {
            dibujarCajaVectorial(g2, sx, yDibujo, tamaño, altoReal);
        }

        // ── Borde de estado ───────────────────────────────────────────────
        Color colorBorde = obtenerColorBorde();
        if (colorBorde != null) {
            g2.setColor(colorBorde);
            g2.setStroke(new java.awt.BasicStroke(2.5f));
            g2.drawRoundRect(sx + 2, yDibujo, tamaño - 4, altoReal, 6, 6);
            g2.setStroke(new java.awt.BasicStroke(1f));
        }

        // ── Etiqueta ──────────────────────────────────────────────────────
        dibujarEtiqueta(g2, sx, sy, tamaño);

        // Restaurar estado gráfico global
        g2.setPaint(paintOrig);
        g2.setStroke(strokeOrig);
        if (aaOrig != null)
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, aaOrig);
    }

    // ── Helpers de dibujo ─────────────────────────────────────────────────

    private void dibujarCajaVectorial(Graphics2D g2, int sx, int y, int tam, int alto) {
        // Gradiente vertical
        java.awt.GradientPaint grad = new java.awt.GradientPaint(
                sx, y, COLOR_NORMAL_TOP,
                sx, y + alto, COLOR_NORMAL_BOT);
        g2.setPaint(grad);
        g2.fillRoundRect(sx + 2, y, tam - 4, alto, 6, 6);

        // Brillo superior
        g2.setColor(new Color(255, 255, 255, 40));
        g2.fillRoundRect(sx + 4, y + 2, tam - 8, alto / 3, 4, 4);
    }

    private void dibujarEtiqueta(Graphics2D g2, int sx, int sy, int tam) {
        g2.setFont(FONT_ETIQUETA);
        g2.setColor(COLOR_TEXTO);
        FontMetrics fm = g2.getFontMetrics();
        String txt = caja.getNombre() + "\n" + caja.getTamanio();

        // Nombre
        String linea1 = caja.getNombre();
        int x1 = sx + (tam - fm.stringWidth(linea1)) / 2;
        g2.drawString(linea1, x1, sy + tam - 14);

        // Tamaño
        String linea2 = String.valueOf(caja.getTamanio());
        int x2 = sx + (tam - fm.stringWidth(linea2)) / 2;
        g2.drawString(linea2, x2, sy + tam - 4);
    }

    private Color obtenerColorBorde() {
        if (correcta)     return COLOR_CORRECTA;
        if (destacada)    return COLOR_DESTACADA;
        if (seleccionada) {
            // parpadeo: visible 30 ticks, invisible 15 ticks
            return (tickParpadeo % 45 < 30) ? COLOR_SELECCIONADA : null;
        }
        return null;
    }

    private void intentarCargarSprite() {
        try {
            spriteCaja = ImageIO.read(
                getClass().getResourceAsStream("/assets/objetos/caja.bmp"));
        } catch (Exception e) {
            spriteCaja = null; // fallback a dibujo vectorial
        }
    }

    // ── Getters / Setters ──────────────────────────────────────────────────

    public Caja getCaja()                        { return caja; }
    public int  getIndiceLogico()                { return indiceLogico; }
    public void setIndiceLogico(int i)           { indiceLogico = i; }
    public boolean isSeleccionada()              { return seleccionada; }
    public void setSeleccionada(boolean v)       { seleccionada = v; tickParpadeo = 0; }
    public boolean isDestacada()                 { return destacada; }
    public void setDestacada(boolean v)          { destacada = v; }
    public boolean isCorrecta()                  { return correcta; }
    public void setCorrecta(boolean v)           { correcta = v; }
}