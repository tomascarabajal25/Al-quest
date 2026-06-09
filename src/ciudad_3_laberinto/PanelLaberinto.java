package ciudad_3_laberinto;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

public class PanelLaberinto extends JPanel {
    
    private static final int TAMANIO_CELDA = 40;

    private static final Color COLOR_PARED = Color.BLACK;
    private static final Color COLOR_LIBRE = Color.WHITE;
    private static final Color COLOR_EN_CAMINO = new Color(70, 130, 255);
    private static final Color COLOR_DESCARTADA = new Color(255, 220, 50);
    private static final Color COLOR_SOLUCION = new Color(50, 200, 80);
    private static final Color COLOR_INICIO_FIN = new Color(220, 50, 50);
    private static final Color COLOR_BORDE = new Color(180, 180, 180);

    private Laberinto laberinto;

    public PanelLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;
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

        g.setColor(obtenerColor(celda.getEstadoCelda()));
        g.fillRect(x, y, TAMANIO_CELDA, TAMANIO_CELDA);

        g.setColor(COLOR_BORDE);
        g.drawRect(x, y, TAMANIO_CELDA, TAMANIO_CELDA);

        if (celda.getEstadoCelda() == EstadoCelda.INICIO) {
            dibujarEtiqueta(g, "S", x, y);
        } else if (celda.getEstadoCelda() == EstadoCelda.FIN) {
            dibujarEtiqueta(g, "E", x, y);
        }
    }

    private void dibujarEtiqueta(Graphics g, String texto, int x, int y) {
        g.setColor(Color.WHITE);
        int xTexto = x + (TAMANIO_CELDA / 2) - 4;
        int yTexto = y + (TAMANIO_CELDA / 2) + 5;
        g.drawString(texto, xTexto, yTexto);
    }

    private Color obtenerColor(EstadoCelda estado) {
        switch (estado) {
            case PARED: return COLOR_PARED;
            case LIBRE: return COLOR_LIBRE;
            case EN_CAMINO: return COLOR_EN_CAMINO;
            case DESCARTADA: return COLOR_DESCARTADA;
            case SOLUCION: return COLOR_SOLUCION;
            case INICIO: return COLOR_INICIO_FIN;
            case FIN: return COLOR_INICIO_FIN;
            default: return COLOR_LIBRE;
        }
    }

    public void setLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;
        repaint();
    }
}