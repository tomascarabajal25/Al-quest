package juego.ciudades.ciudad_3_laberinto.src;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 * Carga y provee los sprites BMP correspondientes a cada estado de celda.
 * Utiliza un Map para asociar cada EstadoCelda con su imagen.
 */
public class GestorSprites {
    
    private static final int TAMANIO_CELDA = 60;

    private String carpetaSprites;

    private BufferedImage iconoJugador;

    private Map<EstadoCelda, BufferedImage> sprites;

    public GestorSprites(String carpetaSprites) throws IOException {
        this.carpetaSprites = carpetaSprites;
        sprites = new HashMap<>();
        cargarSprites();
    }

    /**
     * Carga todos los sprites desde la carpeta indicada.
     * @throws IOException si algun archivo no se puede leer
     */
    private void cargarSprites() throws IOException {
        sprites.put(EstadoCelda.PARED,      cargarImagen("Pared.bmp"));
        sprites.put(EstadoCelda.LIBRE,      cargarImagen("Libre.bmp"));
        sprites.put(EstadoCelda.EN_CAMINO,  cargarImagen("EnCamino.bmp"));
        sprites.put(EstadoCelda.DESCARTADA, cargarImagen("Descartada.bmp"));
        sprites.put(EstadoCelda.SOLUCION,   cargarImagen("EnCamino.bmp"));
        sprites.put(EstadoCelda.INICIO,     cargarImagen("Inicio.bmp"));
        sprites.put(EstadoCelda.FIN,        cargarImagen("Fin.bmp"));
        iconoJugador = cargarImagen("Jugador2.png");
    }

    /**
     * Carga una imagen desde disco y la escala al tamanio de celda.
     * @param nombreArchivo nombre del archivo dentro de la carpeta de sprites
     * @return imagen escalada lista para dibujar
     * @throws IOException si el archivo no existe o no se puede leer
     */
    private BufferedImage cargarImagen(String nombreArchivo) throws IOException {
        String ruta = carpetaSprites + File.separator + nombreArchivo;
        BufferedImage original = ImageIO.read(new File(ruta));

        if (original == null) {
            throw new IOException("No se pudo leer la imagen: " + ruta);
        }

        return escalarImagen(original);
    }

    /**
     * Escala una imagen al tamanio de celda definido.
     * @param original imagen original a escalar
     * @return imagen escalada
     */
    private BufferedImage escalarImagen(BufferedImage original) {
        Image escalada = original.getScaledInstance(
            TAMANIO_CELDA, TAMANIO_CELDA, Image.SCALE_SMOOTH
        );

        BufferedImage resultado = new BufferedImage(
            TAMANIO_CELDA, TAMANIO_CELDA, BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = resultado.createGraphics();
        g.drawImage(escalada, 0, 0, null);
        g.dispose();

        return resultado;
    }

    /**
     * Retorna el sprite correspondiente a un estado de celda.
     * @param estado estado de la celda
     * @return imagen asociada a ese estado
     */
    public BufferedImage obtenerSprite(EstadoCelda estado) {
        return sprites.get(estado);
    }

    /**
     * @return el icono del jugador 
     */
    public BufferedImage getIconoJugador() {
        return iconoJugador;
    }

    public String getCarpetaSprites() {
        return carpetaSprites;
    }
}
