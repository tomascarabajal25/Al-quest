package com.aiquest.juego.ciudades.reinas.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class RecursosGraficos {

    private BufferedImage casillaClaraImg;
    private BufferedImage casillaOscuraImg;
    private BufferedImage reinaFondoClaroImg;
    private BufferedImage reinaFondoOscuroImg;

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

    public BufferedImage getCasillaClaraImg()    { return casillaClaraImg; }
    public BufferedImage getCasillaOscuraImg()   { return casillaOscuraImg; }
    public BufferedImage getReinaFondoClaroImg() { return reinaFondoClaroImg; }
    public BufferedImage getReinaFondoOscuroImg(){ return reinaFondoOscuroImg; }
    
}
