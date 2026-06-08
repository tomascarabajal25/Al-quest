package modelosVista;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import Juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelos.Elemento;

/**
 * Representación visual de un Elemento de dominio en el mapa 2D.
 * Hereda de EntidadVista → tiene worldX, worldY, areaSolida.
 * Contiene un Elemento → tiene lógica de efecto.
 *
 * Subclases concretas:
 *   CartaVisionVista, CartaPuntosVista, CartaDesplazamientoVista
 */
public abstract class ElementoVista extends EntidadVista {

    private final Elemento elemento;
    private BufferedImage  imagen;
    private boolean        recogido = false;

    // Nivel del mapa al que pertenece — Vista solo lo dibuja en ese nivel
    private final int nivel;

    /**
     * pre:  elemento no nulo, col y fila >= 0, tamaño > 0, nivel >= 1
     * post: crea el elemento visual en la posición col/fila del nivel dado
     *
     * @param elemento    modelo de dominio
     * @param col         columna del mapa (base 0)
     * @param fila        fila del mapa (base 0)
     * @param nivel       nivel del mapa al que pertenece (base 1)
     * @param tamaño      tamaño de tile en px
     * @param rutaImagen  ruta al .bmp, null si la subclase la carga después
     */
    public ElementoVista(Elemento elemento, int col, int fila,
                         int nivel, int tamaño, String rutaImagen) {
        super(elemento.getNombre());
        this.elemento = elemento;
        this.nivel    = nivel;

        setWorldX(col * tamaño);
        setWorldY(fila * tamaño);
        setAreaSolida(new Rectangle(0, 0, tamaño, tamaño));

        if (rutaImagen != null) cargarImagen(rutaImagen);
    }

    // ── Draw ──────────────────────────────────────────────────────────────

    /**
     * Dibuja el elemento sin filtro de nivel.
     * Usar en ciudades con un solo nivel.
     */
    public void draw(Graphics2D g2, Vista vista) {
        if (recogido)               return;
        if (!estaEnPantalla(vista)) return;
        if (imagen == null)         return;

        g2.drawImage(imagen,
                getScreenX(vista), getScreenY(vista),
                vista.tamaño, vista.tamaño, null);
    }

    /**
     * Dibuja el elemento solo si pertenece al nivelActual dado.
     * Usar en ciudades multinivel; las subclases pueden sobreescribir
     * para agregar lógica de visibilidad propia.
     *
     * @param nivelActual nivel donde está el jugador ahora
     */
    public void draw(Graphics2D g2, Vista vista, int nivelActual) {
        if (nivelActual != nivel) return;
        draw(g2, vista); // delega al draw base si el nivel coincide
    }

    // ── Helpers de pantalla ───────────────────────────────────────────────

    protected boolean estaEnPantalla(Vista vista) {
        int jx = vista.jugadorVista.getWorldX();
        int jy = vista.jugadorVista.getWorldY();
        int sx = vista.jugadorVista.getScreenX();
        int sy = vista.jugadorVista.getScreenY();

        return getWorldX() + vista.tamaño > jx - sx &&
               getWorldX() - vista.tamaño < jx + sx &&
               getWorldY() + vista.tamaño > jy - sy &&
               getWorldY() - vista.tamaño < jy + sy;
    }

    protected int getScreenX(Vista vista) {
        return getWorldX() - vista.jugadorVista.getWorldX()
                           + vista.jugadorVista.getScreenX();
    }

    protected int getScreenY(Vista vista) {
        return getWorldY() - vista.jugadorVista.getWorldY()
                           + vista.jugadorVista.getScreenY();
    }

    // ── Colisión con jugador ──────────────────────────────────────────────

    /**
     * post: true si el jugador está pisando este elemento y en el mismo nivel
     */
    public boolean colisionaConJugador(JugadorVista jugador, int nivelActual) {
        if (recogido || nivelActual != nivel) return false;

        Rectangle elemRect = new Rectangle(
            getWorldX() + getAreaSolida().x,
            getWorldY() + getAreaSolida().y,
            getAreaSolida().width,
            getAreaSolida().height
        );
        Rectangle jugRect = new Rectangle(
            jugador.getWorldX() + jugador.getAreaSolida().x,
            jugador.getWorldY() + jugador.getAreaSolida().y,
            jugador.getAreaSolida().width,
            jugador.getAreaSolida().height
        );
        return elemRect.intersects(jugRect);
    }

    // ── Recoger ───────────────────────────────────────────────────────────

    /**
     * Aplica el efecto del elemento al receptor y lo marca como recogido.
     * pre: receptor no nulo
     */
    public void recoger(CiudadRecoleccion juego) {
        if (!recogido) {
            elemento.aplicarEfecto(juego);
            recogido = true;
        }
    }

    // ── Imagen ────────────────────────────────────────────────────────────

    protected void cargarImagen(String ruta) {
        try {
            imagen = ImageIO.read(getClass().getResourceAsStream(ruta));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudo cargar imagen: " + ruta);
            imagen = null;
        }
    }

    // ── Getters ───────────────────────────────────────────────────────────

    public Elemento      getElemento()           { return elemento; }
    public int           getNivel()               { return nivel; }
    public boolean       isRecogido()             { return recogido; }
    public BufferedImage getImagen()              { return imagen; }
    protected void       setImagen(BufferedImage img) { this.imagen = img; }
}