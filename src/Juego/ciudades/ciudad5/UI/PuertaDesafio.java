package Juego.ciudades.ciudad5.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import modelos.JugadorVista;
import modelos.ObjetoVista;
import modelos.Vista;

/**
 * Puerta física en el mapa del desafío.
 * Hereda de ObjetoVista → se registra en AdministradorDeObjetos
 * y la Vista la dibuja automáticamente junto al resto de objetos.
 *
 * Uso:
 *   PuertaDesafio puertaLista = new PuertaDesafio(18, 37, TipoPuerta.LISTA, tamaño);
 *   PuertaDesafio puertaArbol = new PuertaDesafio(29, 37, TipoPuerta.ARBOL, tamaño);
 *   vista.adminObjt.setObjetos(puertaLista, puertaArbol);
 */
public class PuertaDesafio extends ObjetoVista {

    public enum TipoPuerta   { LISTA, ARBOL }
    public enum EstadoPuerta { NORMAL, RESALTADA, CORRECTA, INCORRECTA }

    private final TipoPuerta tipo;
    private final int        tamaño;
    private EstadoPuerta     estado = EstadoPuerta.NORMAL;

    /**
     * @param col    columna del tile donde empieza la puerta
     * @param fila   fila del tile donde está la puerta
     * @param tipo   LISTA o ARBOL
     * @param tamaño tamaño de tile en px (Vista.tamaño)
     */
    public PuertaDesafio(int col, int fila, TipoPuerta tipo, int tamaño) {
        super(col * tamaño, fila * tamaño,
              tipo == TipoPuerta.LISTA ? "PuertaLista" : "PuertaArbol",
              false,   // sin colisión de mapa — la colisión la maneja MinijuegoDesafio
              null);   // imagen null → cargarImagenPuerta() la asigna abajo

        this.tipo   = tipo;
        this.tamaño = tamaño;
        cargarImagenPuerta();
    }

    // ── Imagen ────────────────────────────────────────────────────────────

    private void cargarImagenPuerta() {
        try {
            String ruta = tipo == TipoPuerta.LISTA
                ? "/assets/objetos/door.bmp"
                : "/assets/objetos/door_iron.bmp";
            BufferedImage img = ImageIO.read(
                getClass().getResourceAsStream(ruta));
            setImagen(img);
        } catch (IOException | IllegalArgumentException e) {
            setImagen(null); // sin BMP → draw() usa placeholder de color
        }
    }

    // ── Draw ──────────────────────────────────────────────────────────────

    /**
     * La Vista llama este método automáticamente a través de adminObjt.
     * PuertaDesafio personaliza la apariencia según su estado.
     */
    @Override
    public void draw(Graphics2D g2, Vista vista) {
        if (!estaEnPantalla(vista)) {
        	return;
        }

        int screenX = getScreenX(vista);
        int screenY = getScreenY(vista);

        if (getImagen() != null) {
            g2.drawImage(getImagen(), screenX, screenY,
                         tamaño * 3, tamaño, null);
        } else {
            // Placeholder de color según estado
            Color color = switch (estado) {
                case CORRECTA   -> new Color(0, 200, 0, 180);
                case INCORRECTA -> new Color(200, 0, 0, 180);
                case RESALTADA  -> new Color(255, 200, 0, 180);
                default         -> tipo == TipoPuerta.LISTA
                                    ? new Color(80, 120, 200, 180)
                                    : new Color(120, 80, 200, 180);
            };
            g2.setColor(color);
            g2.fillRect(screenX, screenY, tamaño * 3, tamaño);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            String label = tipo == TipoPuerta.LISTA ? "← LISTA" : "ÁRBOL →";
            g2.drawString(label, screenX + 6, screenY + tamaño / 2 + 5);
        }
    }

    // ── Colisión ──────────────────────────────────────────────────────────

    /**
     * post: devuelve true si el jugador está cruzando esta puerta
     */
    public boolean colisionaConJugador(JugadorVista jugador) {
        Rectangle puertaRect  = new Rectangle(
            getWorldX(), getWorldY(), tamaño * 3, tamaño);
        Rectangle jugadorRect = new Rectangle(
            jugador.getWorldX() + jugador.getAreaSolida().x,
            jugador.getWorldY() + jugador.getAreaSolida().y,
            jugador.getAreaSolida().width,
            jugador.getAreaSolida().height
        );
        return puertaRect.intersects(jugadorRect);
    }

    // ── Getters / Setters ─────────────────────────────────────────────────

    public TipoPuerta   getTipo()              { return tipo; }
    public EstadoPuerta getEstado()            { return estado; }
    public void setEstado(EstadoPuerta estado) { this.estado = estado; }
}