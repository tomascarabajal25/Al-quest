package ciudad_3_laberinto.src;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class PanelLaberinto extends JPanel {
    
    private static final int TAMANIO_CELDA = 60;

    private Laberinto laberinto;

    private GestorSprites gestorSprites;

    public PanelLaberinto(Laberinto laberinto, GestorSprites gestorSprites) {
        this.laberinto = laberinto;
        this.gestorSprites = gestorSprites;
        int ancho = laberinto.getColumnas() * TAMANIO_CELDA;
        int alto = laberinto.getFilas() * TAMANIO_CELDA;
        setPreferredSize(new Dimension(ancho, alto));
        setBackground(Color.DARK_GRAY);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < laberinto.getFilas(); i++) {
            for (int j = 0; j < laberinto.getColumnas(); j++) {
                dibujarCelda(g, laberinto.getCelda(i, j), i, j);
            }
        }
    }

    private void dibujarCelda(Graphics g, Celda celda, int fila, int columna) {
        int x = columna * TAMANIO_CELDA;
        int y = fila * TAMANIO_CELDA;

        BufferedImage sprite = gestorSprites.obtenerSprite(celda.getEstadoCelda());
        g.drawImage(sprite, x, y, null);
    }

    public void setLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;
        repaint();
    }
}