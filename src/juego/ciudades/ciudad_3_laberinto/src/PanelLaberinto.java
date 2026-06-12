package juego.ciudades.ciudad_3_laberinto.src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Componente Swing que dibuja el estado actual del laberinto.
 * Muestra los sprites de cada celda y el icono del jugador en la posicion actual.
 * Se actualiza llamando a repaint() cada vez que el algoritmo avanza un paso.
 */
public class PanelLaberinto extends JPanel {
    
    private static final int TAMANIO_CELDA = 60;

    private Laberinto laberinto;

    private BacktrackingLaberinto backtracking;

    private GestorSprites gestorSprites;

    public PanelLaberinto(Laberinto laberinto,
                        GestorSprites gestorSprites,
                        BacktrackingLaberinto backtracking) {
        this.laberinto = laberinto;
        this.gestorSprites = gestorSprites;
        this.backtracking = backtracking;
        int ancho = laberinto.getColumnas() * TAMANIO_CELDA;
        int alto = laberinto.getFilas() * TAMANIO_CELDA;
        setPreferredSize(new Dimension(ancho, alto));
        setBackground(Color.DARK_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < laberinto.getFilas(); i++) {
            for (int j = 0; j < laberinto.getColumnas(); j++) {
                dibujarCelda(g, laberinto.getCelda(i, j), i, j);
            }
        }
    }

    /**
     * Dibuja una celda individual con su sprite y el icono del jugador si corresponde.
     * @param g       contexto grafico
     * @param celda   celda a dibujar
     * @param fila    fila de la celda en la grilla
     * @param columna columna de la celda en la grilla
     */
    private void dibujarCelda(Graphics g, Celda celda, int fila, int columna) {
        int x = columna * TAMANIO_CELDA;
        int y = fila * TAMANIO_CELDA;

        BufferedImage sprite = gestorSprites.obtenerSprite(celda.getEstadoCelda());
        g.drawImage(sprite, x, y, null);

        Celda celdaActual = backtracking.getPila().obtener();
        if (celdaActual != null
            && celdaActual.getFila() == fila
            && celdaActual.getColumna() == columna) {
            g.drawImage(gestorSprites.getIconoJugador(), x, y, null);
        }
    }

    public void setLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;
        repaint();
    }
}