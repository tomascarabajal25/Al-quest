package juego.ciudades.reinas.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RecursosGraficos {

    private BufferedImage casillaClaraImg;
    private BufferedImage casillaOscuraImg;
    private BufferedImage reinaFondoClaroImg;
    private BufferedImage reinaFondoOscuroImg;

    /**
     * Carga las imágenes BMP usadas para dibujar el tablero y las reinas.
     * Si alguna imagen no se encuentra, imprime el error por consola.
     */

    public RecursosGraficos() {

        try {
            String ruta = "src/Juego/ciudades/reinas/resources/imagenes/";
            casillaClaraImg   = ImageIO.read(new File(ruta + "casilla-clara.bmp"));
            casillaOscuraImg  = ImageIO.read(new File(ruta + "casilla-oscura.bmp"));
            reinaFondoClaroImg = ImageIO.read(new File(ruta + "reina-fondoClaro.bmp"));
            reinaFondoOscuroImg = ImageIO.read(new File(ruta + "reina-fondoOscuro.bmp"));
            
        } catch (IOException e) {
            System.out.println("Error al cargar recursos graficos: " + e.getMessage());
        }
    }

    /** @return imagen de casilla clara (sin reina) */
    public BufferedImage getCasillaClaraImg()    { return casillaClaraImg; }

    /** @return imagen de casilla oscura (sin reina) */
    public BufferedImage getCasillaOscuraImg()   { return casillaOscuraImg; }

    /** @return imagen de reina sobre fondo claro */
    public BufferedImage getReinaFondoClaroImg() { return reinaFondoClaroImg; }

    /** @return imagen de reina sobre fondo oscuro */
    public BufferedImage getReinaFondoOscuroImg(){ return reinaFondoOscuroImg; }
    
}
