package Juego.ciudades.recoleccionEnMatriz.ui;
 
import Juego.ciudades.recoleccionEnMatriz.CartaDesplazamiento;
 
public class CartaDesplazamientoVista extends CartaVista {
    public CartaDesplazamientoVista(CartaDesplazamiento carta, int col, int fila,
                                    int nivel, int tamaño) {
        super(carta, col, fila, nivel, tamaño,
              "/assets/cartas/carta_desplazamiento.bmp");
    }
}