package ciudad_3_laberinto;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GestorImagenes {
    
    private static final int TAMANIO_CELDA = 60;
    private static final Color COLOR_PARED = Color.BLACK;
    private static final Color COLOR_LIBRE = Color.WHITE;
    private static final Color COLOR_EN_CAMINO = Color.BLUE;
    private static final Color COLOR_DESCARTADA = Color.YELLOW;
    private static final Color COLOR_SOLUCION = Color.GREEN;
    private static final Color COLOR_INICIO_FIN = Color.RED;

    private String carpetaSalida;
    private int numeroPaso;

    public GestorImagenes(String carpetaSalida) {
        this.carpetaSalida = carpetaSalida;
        this.numeroPaso = 0;
        crearCarpeta();
    }

    private void crearCarpeta() {
        File carpeta = new File(carpetaSalida);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }

    public void guardarPaso(Laberinto laberinto) throws IOException {
        BufferedImage imagen = generarImagen(laberinto);
        numeroPaso++;
        String nombreArchivo = String.format("paso_%03d.bpm", numeroPaso);
        File archivo = new File(carpetaSalida + File.separator + nombreArchivo);
        ImageIO.write(imagen, "BPM", archivo);
    }

    private BufferedImage generarImagen(Laberinto laberinto) {
        int ancho = laberinto.getColumnas() * TAMANIO_CELDA;
        int alto = laberinto.getFilas() * TAMANIO_CELDA;

        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = imagen.createGraphics();

        for (int i = 0; i < laberinto.getFilas(); i++) {
            for (int j = 0; j < laberinto.getColumnas(); j++) {
                Celda celda = laberinto.getCelda(i, j);
                Color color = obtenerColor(celda.getEstadoCelda());

                int x = j * TAMANIO_CELDA;
                int y = i * TAMANIO_CELDA;

                g.setColor(color);
                g.fillRect(x, y, TAMANIO_CELDA, TAMANIO_CELDA);

                g.setColor(Color.GRAY);
                g.drawRect(x, y, TAMANIO_CELDA, TAMANIO_CELDA);
            }
        }

        g.dispose();
        return imagen;
    }

    private Color obtenerColor(EstadoCelda estado) {
        switch (estado) {
            case PARED:      return COLOR_PARED;
            case LIBRE:      return COLOR_LIBRE;
            case EN_CAMINO:  return COLOR_EN_CAMINO;
            case DESCARTADA: return COLOR_DESCARTADA;
            case SOLUCION:   return COLOR_SOLUCION;
            case INICIO:     return COLOR_INICIO_FIN;
            case FIN:        return COLOR_INICIO_FIN;
            default:         return COLOR_LIBRE;
        }
    }

    public void resetearContador() {
        numeroPaso = 0;
    }

    public int getNumeroPaso() {
        return numeroPaso;
    }

    public String getCarpetaSalida() {
        return carpetaSalida;
    }
}