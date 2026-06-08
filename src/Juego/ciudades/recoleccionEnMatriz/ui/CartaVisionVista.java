package Juego.ciudades.recoleccionEnMatriz.ui;
 
import Juego.ciudades.recoleccionEnMatriz.CartaVision;
 
public class CartaVisionVista extends CartaVista {
    public CartaVisionVista(CartaVision carta, int col, int fila,
                            int nivel, int tamaño) {
        super(carta, col, fila, nivel, tamaño,
              "/assets/cartas/carta_vision.bmp");
    }
}