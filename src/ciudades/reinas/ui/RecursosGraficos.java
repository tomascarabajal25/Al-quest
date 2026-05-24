package ciudades.reinas.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RecursosGraficos {

    private BufferedImage bordeClaroImg;
    private BufferedImage bordeOscuroImg;
    private BufferedImage esquinaClaraImg;
    private BufferedImage esquinaOscuraImg;
    private BufferedImage casillaClaraImg;
    private BufferedImage casillaOscuraImg;
    private BufferedImage reinaFondoClaroImg;
    private BufferedImage reinaFondoOscuroImg;

    public RecursosGraficos() {

        try {
            String ruta = "resources/imagenes/";
            bordeClaroImg     = ImageIO.read(new File(ruta + "borde-claro.bmp"));
            bordeOscuroImg    = ImageIO.read(new File(ruta + "borde-oscuro.bmp"));
            esquinaClaraImg   = ImageIO.read(new File(ruta + "esquina-clara.bmp"));
            esquinaOscuraImg  = ImageIO.read(new File(ruta + "esquina-oscura.bmp"));
            casillaClaraImg   = ImageIO.read(new File(ruta + "casilla-clara.bmp"));
            casillaOscuraImg  = ImageIO.read(new File(ruta + "casilla-oscura.bmp"));
            reinaFondoClaroImg = ImageIO.read(new File(ruta + "reina-fondoClaro.bmp"));
            reinaFondoOscuroImg = ImageIO.read(new File(ruta + "reina-fondoOscuro.bmp"));
            
        } catch (IOException e) {
            System.out.println("Error al cargar recursos graficos: " + e.getMessage());
        }
    }

    public BufferedImage getBordeClaroImg()      { return bordeClaroImg; }
    public BufferedImage getBordeOscuroImg()     { return bordeOscuroImg; }
    public BufferedImage getEsquinaClaraImg()    { return esquinaClaraImg; }
    public BufferedImage getEsquinaOscuraImg()   { return esquinaOscuraImg; }
    public BufferedImage getCasillaClaraImg()    { return casillaClaraImg; }
    public BufferedImage getCasillaOscuraImg()   { return casillaOscuraImg; }
    public BufferedImage getReinaFondoClaroImg() { return reinaFondoClaroImg; }
    public BufferedImage getReinaFondoOscuroImg(){ return reinaFondoOscuroImg; }
    
}
