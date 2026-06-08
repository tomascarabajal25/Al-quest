package Juego.ciudades.recoleccionEnMatriz.ui;
 
import Juego.ciudades.recoleccionEnMatriz.CartaPuntos;
 
public class CartaPuntosVista extends CartaVista {
    public CartaPuntosVista(CartaPuntos carta, int col, int fila,
                            int nivel, int tamaño) {
        super(carta, col, fila, nivel, tamaño,
              "/assets/cartas/carta_puntos.bmp");
    }
}
 