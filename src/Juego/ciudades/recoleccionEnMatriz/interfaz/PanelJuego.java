package Juego.ciudades.recoleccionEnMatriz.interfaz;

import Juego.Constantes;
import Juego.ciudades.recoleccionEnMatriz.CartaDesplazamiento;
import Juego.ciudades.recoleccionEnMatriz.CartaPuntos;
import Juego.ciudades.recoleccionEnMatriz.CartaVision;
import Juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelos.Celda;
import modelos.Elemento;
import modelos.Jugador;
import modelos.Mapa;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Panel principal del juego. Renderiza la grilla del mapa usando bitmaps
 * generados programáticamente con Graphics2D.
 *
 * Cada celda es un BufferedImage (bitmap) de TILE_SIZE x TILE_SIZE píxeles.
 * Los bitmaps se generan una sola vez y se cachean en un Map.
 */
public class PanelJuego extends JPanel {

    // -------------------------------------------------------------------------
    // CONSTANTES DE RENDERIZADO
    // -------------------------------------------------------------------------
    private static final int TILE_SIZE    = 32;   // px por celda
    private static final int PADDING      = 12;   // margen interior del panel

    // Paleta de colores del juego
    private static final Color COLOR_FONDO         = new Color(15, 20, 35);
    private static final Color COLOR_CELDA          = new Color(28, 38, 60);
    private static final Color COLOR_CELDA_BORDE    = new Color(45, 60, 90);
    private static final Color COLOR_JUGADOR        = new Color(80, 200, 120);
    private static final Color COLOR_CARTA_VISION   = new Color(100, 180, 255);
    private static final Color COLOR_CARTA_DESPLAZ  = new Color(255, 200, 60);
    private static final Color COLOR_CARTA_PUNTOS   = new Color(255, 100, 100);
    private static final Color COLOR_VACIO          = new Color(28, 38, 60);
    private static final Color COLOR_MENSAJE        = new Color(255, 230, 100);
    private static final Color COLOR_MOCHILA_FONDO  = new Color(10, 14, 28, 220);

    // Tipos de tile para el cache
    private static final String TILE_VACIO      = "vacio";
    private static final String TILE_JUGADOR    = "jugador";
    private static final String TILE_VISION     = "vision";
    private static final String TILE_DESPLAZ    = "desplaz";
    private static final String TILE_PUNTOS     = "puntos";

    // -------------------------------------------------------------------------
    // ATRIBUTOS
    // -------------------------------------------------------------------------
    private final CiudadRecoleccion juego;
    private final int filas;
    private final int columnas;

    // Cache de bitmaps: tipo -> imagen
    private final Map<String, BufferedImage> tileCache = new HashMap<>();

    private PanelHUD hud;
    private boolean mostrarMochila = false;
    private String mensajeTemp = null;

    // -------------------------------------------------------------------------
    // CONSTRUCTOR
    // -------------------------------------------------------------------------

    public PanelJuego(CiudadRecoleccion juego, int filas, int columnas) {
        this.juego   = juego;
        this.filas   = filas;
        this.columnas = columnas;

        setOpaque(true);  // ← agregá esto
        setBackground(COLOR_FONDO);

        int ancho = columnas * TILE_SIZE + PADDING * 2;
        int alto  = filas   * TILE_SIZE + PADDING * 2 + 40;
        setPreferredSize(new Dimension(ancho, alto));

        generarBitmaps();
    }

    // -------------------------------------------------------------------------
    // GENERACION DE BITMAPS
    // -------------------------------------------------------------------------

    /**
     * Genera y cachea todos los bitmaps del juego.
     * Se llama una sola vez en el constructor.
     */
    private void generarBitmaps() {
        tileCache.put(TILE_VACIO,   crearTileVacio());
        tileCache.put(TILE_JUGADOR, crearTileJugador());
        tileCache.put(TILE_VISION,  crearTileVision());
        tileCache.put(TILE_DESPLAZ, crearTileDesplazamiento());
        tileCache.put(TILE_PUNTOS,  crearTilePuntos());
    }

    /** Celda vacía: fondo oscuro con borde sutil */
    private BufferedImage crearTileVacio() {
        BufferedImage img = nuevaImagen();
        Graphics2D g = setup(img);
        g.setColor(COLOR_VACIO);
        g.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
        g.setColor(COLOR_CELDA_BORDE);
        g.drawRect(0, 0, TILE_SIZE - 1, TILE_SIZE - 1);
        g.dispose();
        return img;
    }

    /** Jugador: círculo verde con sombra */
    private BufferedImage crearTileJugador() {
        BufferedImage img = nuevaImagen();
        Graphics2D g = setup(img);

        // Fondo de celda
        g.setColor(COLOR_CELDA);
        g.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
        g.setColor(COLOR_CELDA_BORDE);
        g.drawRect(0, 0, TILE_SIZE - 1, TILE_SIZE - 1);

        // Sombra del círculo
        g.setColor(new Color(0, 0, 0, 80));
        g.fillOval(5, 7, TILE_SIZE - 8, TILE_SIZE - 8);

        // Cuerpo del jugador
        GradientPaint gp = new GradientPaint(
                6, 6, COLOR_JUGADOR.brighter(),
                TILE_SIZE - 6, TILE_SIZE - 6, COLOR_JUGADOR.darker()
        );
        g.setPaint(gp);
        g.fillOval(4, 4, TILE_SIZE - 8, TILE_SIZE - 8);

        // Punto central
        g.setColor(Color.WHITE);
        g.fillOval(TILE_SIZE / 2 - 3, TILE_SIZE / 2 - 3, 6, 6);

        g.dispose();
        return img;
    }

    /** Carta Visión: ojo estilizado en azul */
    private BufferedImage crearTileVision() {
        BufferedImage img = nuevaImagen();
        Graphics2D g = setup(img);

        g.setColor(COLOR_CELDA);
        g.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
        g.setColor(COLOR_CELDA_BORDE);
        g.drawRect(0, 0, TILE_SIZE - 1, TILE_SIZE - 1);

        // Rombo / forma de ojo
        int cx = TILE_SIZE / 2, cy = TILE_SIZE / 2;
        int[] xp = {cx, cx + 10, cx, cx - 10};
        int[] yp = {cy - 7, cy, cy + 7, cy};
        g.setColor(COLOR_CARTA_VISION.darker());
        g.fillPolygon(xp, yp, 4);
        g.setColor(COLOR_CARTA_VISION);
        g.drawPolygon(xp, yp, 4);

        // Pupila
        g.setColor(new Color(20, 60, 120));
        g.fillOval(cx - 4, cy - 4, 8, 8);
        g.setColor(Color.WHITE);
        g.fillOval(cx - 2, cy - 4, 3, 3);

        g.dispose();
        return img;
    }

    /** Carta Desplazamiento: rayo en amarillo */
    private BufferedImage crearTileDesplazamiento() {
        BufferedImage img = nuevaImagen();
        Graphics2D g = setup(img);

        g.setColor(COLOR_CELDA);
        g.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
        g.setColor(COLOR_CELDA_BORDE);
        g.drawRect(0, 0, TILE_SIZE - 1, TILE_SIZE - 1);

        // Rayo (zigzag)
        int[] xp = {18, 13, 16, 11, 18, 14, 20};
        int[] yp = { 4, 14, 14, 28, 17, 17,  4};
        g.setColor(COLOR_CARTA_DESPLAZ.darker());
        g.fillPolygon(xp, yp, 7);
        g.setColor(COLOR_CARTA_DESPLAZ);
        g.setStroke(new BasicStroke(1.2f));
        g.drawPolygon(xp, yp, 7);

        g.dispose();
        return img;
    }

    /** Carta Puntos: estrella en rojo/dorado */
    private BufferedImage crearTilePuntos() {
        BufferedImage img = nuevaImagen();
        Graphics2D g = setup(img);

        g.setColor(COLOR_CELDA);
        g.fillRect(0, 0, TILE_SIZE, TILE_SIZE);
        g.setColor(COLOR_CELDA_BORDE);
        g.drawRect(0, 0, TILE_SIZE - 1, TILE_SIZE - 1);

        // Estrella de 5 puntas
        int cx = TILE_SIZE / 2, cy = TILE_SIZE / 2;
        Polygon star = crearEstrella(cx, cy, 12, 5, 5);
        g.setColor(COLOR_CARTA_PUNTOS.darker());
        g.fillPolygon(star);
        g.setColor(COLOR_CARTA_PUNTOS);
        g.setStroke(new BasicStroke(1f));
        g.drawPolygon(star);

        g.dispose();
        return img;
    }

    // -------------------------------------------------------------------------
    // PINTADO PRINCIPAL
    // -------------------------------------------------------------------------

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo general
        g.setColor(COLOR_FONDO);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Obtener nivel actual del jugador
        int[] posJugador = juego.getPosicionJugador();
        if (posJugador == null) return;

        int nivelActual = posJugador[2];

        // Barra superior: indicador de nivel
        dibujarBarraNivel(g, nivelActual);

        // Grilla del mapa
        dibujarMapa(g, nivelActual, posJugador);

        // Overlay de mochila (si está visible)
        if (mostrarMochila) {
            dibujarPanelMochila(g);
        }

        // Mensaje temporal
        if (mensajeTemp != null) {
            dibujarMensaje(g, mensajeTemp);
        }

        // Pantalla de fin
        if (juego.estaFinalizado()) {
            dibujarPantallaFin(g);
        }
    }

    // -------------------------------------------------------------------------
    // SUBCOMPONENTES DE PINTADO
    // -------------------------------------------------------------------------

    private void dibujarBarraNivel(Graphics2D g, int nivel) {
        g.setColor(new Color(40, 55, 85));
        g.fillRoundRect(PADDING, 8, columnas * TILE_SIZE, 26, 8, 8);

        g.setFont(new Font("Monospaced", Font.BOLD, 13));
        g.setColor(new Color(160, 200, 255));
        String txt = "  NIVEL " + nivel + " / " + Constantes.NIVELES_MAPA
                + "      [WASD] mover    [P] mochila";
        g.drawString(txt, PADDING + 8, 26);
    }

    private void dibujarMapa(Graphics2D g, int nivelActual, int[] posJugador) {
        Mapa mapa = juego.getMapaNivel(nivelActual);
        if (mapa == null) return;

        int offsetY = PADDING + 40;
        int visibilidad = juego.getVisibilidad();

        for (int fila = 1; fila <= filas; fila++) {
            for (int col = 1; col <= columnas; col++) {
                int px = PADDING + (col - 1) * TILE_SIZE;
                int py = offsetY + (fila - 1) * TILE_SIZE;

                // Verificar si la celda está dentro del rango de visibilidad
                boolean esVisible = Math.abs(fila - posJugador[0]) <= visibilidad
                        && Math.abs(col  - posJugador[1]) <= visibilidad;

                try {
                    Celda<?> celda = mapa.getCeldaConPosicion(fila, col);
                    if (esVisible) {
                        BufferedImage tile = resolverTile(celda, posJugador);
                        g.drawImage(tile, px, py, TILE_SIZE, TILE_SIZE, this);
                    } else {
                        // Celda fuera de visibilidad: dibujar oscura
                        g.drawImage(tileCache.get(TILE_VACIO), px, py, TILE_SIZE, TILE_SIZE, this);
                        g.setColor(new Color(0, 0, 0, 180)); // overlay oscuro
                        g.fillRect(px, py, TILE_SIZE, TILE_SIZE);
                    }
                } catch (RuntimeException e) {
                    g.drawImage(tileCache.get(TILE_VACIO), px, py, TILE_SIZE, TILE_SIZE, this);
                }
            }
        }
    }

    /**
     * Determina qué bitmap corresponde a cada celda.
     */
    private BufferedImage resolverTile(Celda<?> celda, int[] posJugador) {
        if (celda == null) return tileCache.get(TILE_VACIO);

        Object contenido = celda.getContenido();

        if (contenido instanceof Jugador)             return tileCache.get(TILE_JUGADOR);
        if (contenido instanceof CartaVision)         return tileCache.get(TILE_VISION);
        if (contenido instanceof CartaDesplazamiento) return tileCache.get(TILE_DESPLAZ);
        if (contenido instanceof CartaPuntos)         return tileCache.get(TILE_PUNTOS);

        return tileCache.get(TILE_VACIO);
    }

    private void dibujarPanelMochila(Graphics2D g) {
        // Fondo semi-transparente
        g.setColor(COLOR_MOCHILA_FONDO);
        g.fillRoundRect(PADDING + 20, PADDING + 50, columnas * TILE_SIZE - 40, 160, 12, 12);

        g.setColor(new Color(100, 160, 255));
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.drawString("── MOCHILA ──", PADDING + 40, PADDING + 80);

        var items = juego.getItemsMochila();
        g.setFont(new Font("Monospaced", Font.PLAIN, 13));
        int i = 1;
        for (Elemento el : items) {
            g.setColor(new Color(200, 220, 255));
            g.drawString("[" + i + "] " + el.getNombre(), PADDING + 40, PADDING + 80 + i * 22);
            i++;
        }

        if (i == 1) {
            g.setColor(new Color(120, 120, 160));
            g.drawString("(vacía)", PADDING + 40, PADDING + 102);
        }

        g.setColor(new Color(160, 160, 200));
        g.setFont(new Font("Monospaced", Font.ITALIC, 11));
        g.drawString("[1-3] usar carta    [Q] cerrar", PADDING + 40, PADDING + 200);
    }

    private void dibujarMensaje(Graphics2D g, String msg) {
        g.setFont(new Font("Monospaced", Font.BOLD, 13));
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(PADDING + 10, getHeight() - 50, columnas * TILE_SIZE - 20, 30, 8, 8);
        g.setColor(COLOR_MENSAJE);
        g.drawString(msg, PADDING + 20, getHeight() - 30);
    }

    private void dibujarPantallaFin(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setFont(new Font("Monospaced", Font.BOLD, 22));
        g.setColor(COLOR_CARTA_PUNTOS);
        String txt = "¡NIVEL COMPLETADO!";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(txt, (getWidth() - fm.stringWidth(txt)) / 2, getHeight() / 2);
    }

    // -------------------------------------------------------------------------
    // UTILIDADES
    // -------------------------------------------------------------------------

    private BufferedImage nuevaImagen() {
        return new BufferedImage(TILE_SIZE, TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
    }

    private Graphics2D setup(BufferedImage img) {
        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return g;
    }

    /** Genera un polígono de estrella de N puntas */
    private Polygon crearEstrella(int cx, int cy, int rExt, int rInt, int puntas) {
        Polygon p = new Polygon();
        double paso = Math.PI / puntas;
        for (int i = 0; i < 2 * puntas; i++) {
            double ang = i * paso - Math.PI / 2;
            int r = (i % 2 == 0) ? rExt : rInt;
            p.addPoint(cx + (int)(r * Math.cos(ang)), cy + (int)(r * Math.sin(ang)));
        }
        return p;
    }

    // -------------------------------------------------------------------------
    // GETTERS / SETTERS USADOS POR GameWindow
    // -------------------------------------------------------------------------

    public void setHUD(PanelHUD hud)               { this.hud = hud; }
    public PanelHUD getHUD()                        { return hud; }
    public void setMostrarMochila(boolean v)        { this.mostrarMochila = v; }
    public void mostrarMensaje(String msg)          { this.mensajeTemp = msg; repaint(); }
}
