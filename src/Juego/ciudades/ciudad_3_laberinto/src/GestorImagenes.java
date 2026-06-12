package juego.ciudades.ciudad_3_laberinto.src;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GestorImagenes {
    
    private static final int TAMANIO_CELDA = 60;

    private String carpetaSalida;
    private GestorSprites gestorSprites;
    private int numeroPaso;

    public GestorImagenes(String carpetaSalida, GestorSprites gestorSprites) {
        this.carpetaSalida = carpetaSalida;
        this.gestorSprites = gestorSprites;
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
        String nombreArchivo = String.format("paso_%03d.bmp", numeroPaso);
        File archivo = new File(carpetaSalida + File.separator + nombreArchivo);
        ImageIO.write(imagen, "BMP", archivo);
    }

    private BufferedImage generarImagen(Laberinto laberinto) {
        int ancho = laberinto.getColumnas() * TAMANIO_CELDA;
        int alto = laberinto.getFilas() * TAMANIO_CELDA;

        BufferedImage imagen = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = imagen.createGraphics();

        for (int i = 0; i < laberinto.getFilas(); i++) {
            for (int j = 0; j < laberinto.getColumnas(); j++) {
                Celda celda = laberinto.getCelda(i, j);
                BufferedImage sprite = gestorSprites.obtenerSprite(celda.getEstadoCelda());

                int x = j * TAMANIO_CELDA;
                int y = i * TAMANIO_CELDA;

                g.drawImage(sprite, x, y, null);
            }
        }
        g.dispose();
        return imagen;
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