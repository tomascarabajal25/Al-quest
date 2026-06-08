package Juego.ciudades.ciudad5.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import modelosVista.JugadorVista;

/**
 * Zona invisible en el mapa que activa el minijuego cuando el jugador la pisa.
 * Colocarla en la entrada de la sala (cols 23-26, fila 30 aprox).
 *
 * Coordenadas mundo: worldX = col * tamaño, worldY = fila * tamaño
 */
public class ZonaDesafio {

    // Posición y tamaño en coordenadas mundo
    private final int worldX;
    private final int worldY;
    private final int ancho;
    private final int alto;

    private boolean activada = false;

    /**
     * @param col      columna del tile de inicio de la zona
     * @param fila     fila del tile de inicio de la zona
     * @param anchoTiles  cuántos tiles de ancho ocupa
     * @param altoTiles   cuántos tiles de alto ocupa
     * @param tamaño   tamaño de un tile en píxeles
     */
    public ZonaDesafio(int col, int fila, int anchoTiles, int altoTiles, int tamaño) {
        this.worldX = col * tamaño;
        this.worldY = fila * tamaño;
        this.ancho  = anchoTiles * tamaño;
        this.alto   = altoTiles  * tamaño;
    }

    /**
     * post: devuelve true si el jugador está pisando esta zona
     */
    public boolean colisionaConJugador(JugadorVista jugador) {
        Rectangle zonaRect   = new Rectangle(worldX, worldY, ancho, alto);
        Rectangle jugadorRect = new Rectangle(
            jugador.getWorldX() + jugador.getAreaSolida().x,
            jugador.getWorldY() + jugador.getAreaSolida().y,
            jugador.getAreaSolida().width,
            jugador.getAreaSolida().height
        );
        return zonaRect.intersects(jugadorRect);
    }

    /**
     * Dibuja un indicador visual opcional (útil en desarrollo, podés sacarlo luego).
     * Dibuja el texto "? DESAFÍO" flotando sobre la zona.
     */
    public void draw(Graphics2D g2, JugadorVista jugador) {
        int screenX = worldX - jugador.getWorldX() + jugador.getScreenX();
        int screenY = worldY - jugador.getWorldY() + jugador.getScreenY();

        // Fondo semitransparente opcional — comentar si no se quiere ver
        g2.setColor(new Color(255, 255, 0, 60));
        g2.fillRect(screenX, screenY, ancho, alto);

        // Texto indicador
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("? DESAFÍO", screenX + 4, screenY + alto / 2);
    }

    public boolean isActivada() {
        return activada;
    }

    public void setActivada(boolean activada) {
        this.activada = activada;
    }
}