package juego.ciudades.ciudad5.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import juego.configuracion.ConfiguracionBusqueda;
import modelosVista.JugadorVista;
import modelosVista.ObjetoVista;
import modelosVista.Vista;

/**
 * Puerta física en el mapa del desafío.
 * Hereda de ObjetoVista → se registra en AdministradorDeObjetos
 * y la Vista la dibuja automáticamente junto al resto de objetos.
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
        super(
            col * tamaño,
            fila * tamaño,
            tipo == TipoPuerta.LISTA ? "PuertaLista" : "PuertaArbol",
            false,              // sin colisión de mapa; la maneja MinijuegoDesafio
            cargarSprite(tipo)
        );

        this.tipo   = tipo;
        this.tamaño = tamaño;
    }

    // ── Imagen ────────────────────────────────────────────────────────────────

    private static BufferedImage cargarSprite(TipoPuerta tipo) {
        String ruta = tipo == TipoPuerta.LISTA
            ? ConfiguracionBusqueda.SPRITE_PUERTA_LISTA
            : ConfiguracionBusqueda.SPRITE_PUERTA_ARBOL;
        try {
            return ImageIO.read(PuertaDesafio.class.getResourceAsStream(ruta));
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Advertencia: no se pudo cargar sprite de puerta: " + ruta);
            return null;
        }
    }

    // ── Draw ──────────────────────────────────────────────────────────────────

    /**
     * La Vista llama este método automáticamente a través de adminObjt.
     */
    @Override
    public void draw(Graphics2D g2, Vista vista) {
        if (!estaEnPantalla(vista)) {
            return;
        }

        int screenX = getScreenX(vista);
        int screenY = getScreenY(vista);
        int anchoVisual = tamaño * ConfiguracionBusqueda.PUERTA_ANCHO_TILES;

        if (getImagen() != null) {
            g2.drawImage(getImagen(), screenX, screenY, anchoVisual, tamaño, null);
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
            g2.fillRect(screenX, screenY, anchoVisual, tamaño);
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            String label = tipo == TipoPuerta.LISTA ? "← LISTA" : "ÁRBOL →";
            g2.drawString(label, screenX + 6, screenY + tamaño / 2 + 5);
        }
    }

    // ── Colisión ──────────────────────────────────────────────────────────────

    /**
     * post: devuelve true si el jugador está cruzando esta puerta.
     */
    public boolean colisionaConJugador(JugadorVista jugador) {
        Rectangle puertaRect = new Rectangle(
            getWorldX(), getWorldY(),
            tamaño * ConfiguracionBusqueda.PUERTA_ANCHO_TILES, tamaño
        );
        Rectangle jugadorRect = new Rectangle(
            jugador.getWorldX() + jugador.getAreaSolida().x,
            jugador.getWorldY() + jugador.getAreaSolida().y,
            jugador.getAreaSolida().width,
            jugador.getAreaSolida().height
        );
        return puertaRect.intersects(jugadorRect);
    }

    // ── Getters / setters ─────────────────────────────────────────────────────

    public TipoPuerta   getTipo()                    { return tipo; }
    public EstadoPuerta getEstado()                  { return estado; }
    public void         setEstado(EstadoPuerta e)    { this.estado = e; }
}