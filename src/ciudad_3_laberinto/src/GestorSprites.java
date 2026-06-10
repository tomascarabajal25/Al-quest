package ciudad_3_laberinto.src;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

public class GestorSprites {
    
    private static final int TAMANIO_CELDA = 60;

    private String carpetaSprites;

    private Map<EstadoCelda, BufferedImage> sprites;

    public GestorSprites(String carpetaSprites) throws IOException {
        this.carpetaSprites = carpetaSprites;
        sprites = new HashMap<>();
        cargarSprites();
    }

    private void cargarSprites() throws IOException {
        sprites.put(EstadoCelda.PARED,      cargarImagen("Pared.bmp"));
        sprites.put(EstadoCelda.LIBRE,      cargarImagen("Libre.bmp"));
        sprites.put(EstadoCelda.EN_CAMINO,  cargarImagen("EnCamino.bmp"));
        sprites.put(EstadoCelda.DESCARTADA, cargarImagen("Descartada.bmp"));
        sprites.put(EstadoCelda.SOLUCION,   cargarImagen("EnCamino.bmp"));
        sprites.put(EstadoCelda.INICIO,     cargarImagen("Inicio.bmp"));
        sprites.put(EstadoCelda.FIN,        cargarImagen("Fin.bmp"));
    }

    private BufferedImage cargarImagen(String nombreArchivo) throws IOException {
        String ruta = carpetaSprites + File.separator + nombreArchivo;
        BufferedImage original = ImageIO.read(new File(ruta));

        if (original == null) {
            throw new IOException("No se pudo leer la imagen: " + ruta);
        }

        return escalarImagen(original);
    }

    private BufferedImage escalarImagen(BufferedImage original) {
        Image escalada = original.getScaledInstance(
            TAMANIO_CELDA, TAMANIO_CELDA, Image.SCALE_SMOOTH
        );

        BufferedImage resultado = new BufferedImage(
            TAMANIO_CELDA, TAMANIO_CELDA, BufferedImage.TYPE_INT_RGB
        );

        Graphics2D g = resultado.createGraphics();
        g.drawImage(escalada, 0, 0, null);
        g.dispose();

        return resultado;
    }

    public BufferedImage obtenerSprite(EstadoCelda estado) {
        return sprites.get(estado);
    }

    public String getCarpetaSprites() {
        return carpetaSprites;
    }
}
