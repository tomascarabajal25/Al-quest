package Juego.ciudades.ordenamientos.ui;

import java.awt.Graphics2D;
import java.util.List;

/**
 * Interfaz funcional que abstrae cómo dibujar un elemento T en el canvas.
 *
 * El lambda recibe altoMaximo (espacio vertical disponible) y la lista completa
 * del paso para calcular proporciones. Devuelve el alto real usado.
 *
 * @param <T> tipo de elemento a dibujar
 */
@FunctionalInterface
public interface DibujarElemento<T> {

    /**
     * Dibuja el elemento en el canvas y devuelve el alto real utilizado.
     *
     * pre:  g != null, elemento != null, lista != null y no vacía
     * @param g           contexto gráfico del canvas
     * @param elemento    elemento a dibujar
     * @param lista       lista completa del paso (para calcular proporciones)
     * @param x           posición x en píxeles
     * @param yBase       y de la base desde donde crece hacia arriba
     * @param ancho       ancho en píxeles disponible
     * @param altoMaximo  alto máximo disponible en píxeles
     * @param destacado   true si el elemento está siendo comparado/intercambiado
     * @return            alto real que ocupó (para que la vista posicione etiquetas)
     */
    int dibujar(Graphics2D g, T elemento, List<T> lista,
                int x, int yBase, int ancho, int altoMaximo, boolean destacado);
}