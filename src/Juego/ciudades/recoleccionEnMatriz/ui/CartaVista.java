package Juego.ciudades.recoleccionEnMatriz.ui;

import modelosVista.ElementoVista;
import modelos.Elemento;

/**
 * Clase base para todas las cartas visuales de la ciudad de recolección.
 * Cada tipo de carta tiene su propio BMP y hereda de esta.
 *
 * Jerarquía:
 *   ElementoVista
 *     └── CartaVista          ← esta clase
 *           ├── CartaVisionVista
 *           ├── CartaPuntosVista
 *           └── CartaDesplazamientoVista
 *
 * CartaVista no se instancia directamente — es abstracta.
 * Cada subclase solo aporta la ruta del BMP en su constructor.
 */
public abstract class CartaVista extends ElementoVista {

    /**
     * @param elemento    modelo de dominio (CartaVision, CartaPuntos, etc.)
     * @param col         columna del mapa (base 0)
     * @param fila        fila del mapa (base 0)
     * @param nivel       nivel del mapa (base 1)
     * @param tamaño      tamaño de tile en px
     * @param rutaImagen  ruta al .bmp de la carta
     */
    public CartaVista(Elemento elemento, int col, int fila,
                      int nivel, int tamaño, String rutaImagen) {
        super(elemento, col, fila, nivel, tamaño, rutaImagen);
    }
}