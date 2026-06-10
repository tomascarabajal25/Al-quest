package modelosVista;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import Juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelos.Elemento;
import utils.ValidacionesUtiles;

public abstract class ElementoVista extends EntidadVista {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private Elemento elemento = null;
    private BufferedImage  imagen = null;
    private boolean recogido;
    private int nivel;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    /**
     * Constructor del TDA ElementoVista
     *
     * PRE:
     * -Elemento no debe ser nulo
     * -Col , fila y nivel deben ser mayores o iguales a cero
     * -Tamanio debe ser mayor a cero
     *
     * POST:
     * -Crea el elemento visual en la posición col/fila del nivel dado
     *
     * @param elemento: Modelo de dominio
     * @param col: Columna del mapa (base 0)
     * @param fil: Fila del mapa (base 0)
     * @param nivel: Nivel del mapa al que pertenece (base 1)
     * @param tamanio: Tamaño de tile en px
     * @param rutaImagen: Ruta al .bmp, null si la subclase la carga después
     */
    public ElementoVista(Elemento elemento, int col, int fil, int nivel, int tamanio, String rutaImagen) {
        super(elemento.getNombre());

        ValidacionesUtiles.esDistintoDeNull(elemento, "elemento");
        ValidacionesUtiles.validarMayorOIgualACero(col, "col");
        ValidacionesUtiles.validarMayorOIgualACero(fil, "fil");
        ValidacionesUtiles.validarMayorOIgualACero(nivel, "nivel");
        ValidacionesUtiles.validarMayorACero(tamanio, "tamanio");
        ValidacionesUtiles.esDistintoDeNull(rutaImagen, "rutaImagen");

        setElemento(elemento);
        setNivel(nivel);
        setRecogido(false);

        setWorldX(col * tamanio);
        setWorldY(fil * tamanio);
        setAreaSolida(new Rectangle(0, 0, tamanio, tamanio));

        cargarImagen(rutaImagen);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    /**
     * Dibuja el elemento sin filtro de nivel. Usar en ciudades con un solo nivel.
     *
     * PRE:
     * -G2 y vista no deben ser nulos
     */
    public void draw(Graphics2D g2, Vista vista) {
        ValidacionesUtiles.esDistintoDeNull(g2, "g2");
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");

        if (recogido || !estaEnPantalla(vista) || imagen == null){
            return;
        }
        g2.drawImage(imagen, getScreenX(vista), getScreenY(vista), vista.getTamanio(), vista.getTamanio(), null);
    }

    /**
     * Dibuja el elemento solo si pertenece al nivelActual dado.
     * Usar en ciudades multinivel; las subclases pueden sobreescribir
     * para agregar lógica de visibilidad propia.
     *
     * PRE:
     * -G2 y vista no deben ser nulos
     * -NivelActual debe ser mayor o igual a cero
     *
     * @param g2:
     * @param vista:
     * @param nivelActual nivel actual del jugador
     */
    public void draw(Graphics2D g2, Vista vista, int nivelActual) {
        ValidacionesUtiles.esDistintoDeNull(g2, "g2");
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");
        ValidacionesUtiles.validarMayorOIgualACero(nivelActual, "nivelActual");

        if (nivelActual != nivel){
            return;
        };
        draw(g2, vista);
    }

    /**
     * Verifica que el elemento dado este en la pantalla
     *
     * PRE:
     * -Elemento no debe ser nulo
     *
     * @param vista: vista donde se verifica si esta el elemento
     * @return: True si el elemento esta en la vista, false si no
     */
    protected boolean estaEnPantalla(Vista vista) {
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");

        int jx = vista.getJugadorVista().getWorldX();
        int jy = vista.getJugadorVista().getWorldY();
        int sx = vista.getJugadorVista().getScreenX();
        int sy = vista.getJugadorVista().getScreenY();

        return getWorldX() + vista.getTamanio() > jx - sx &&
               getWorldX() - vista.getTamanio() < jx + sx &&
               getWorldY() + vista.getTamanio() > jy - sy &&
               getWorldY() - vista.getTamanio() < jy + sy;
    }

    /**
     * Verifica si un elemento/entidad esta colisionando con el jugador
     *
     * PRE:
     * -Jugador no debe ser nulo
     * -Nivel actual debe ser mayor o igual a cero
     *
     * @return: true si el jugador está pisando este elemento y en el mismo nivel
     */
    public boolean colisionaConJugador(JugadorVista jugador, int nivelActual) {
        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");
        ValidacionesUtiles.validarMayorOIgualACero(nivelActual, "nivelActual");

        if (recogido || nivelActual != nivel) {
            return false;
        }

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

    /**
     * Aplica el efecto del elemento al receptor y lo marca como recogido.
     *
     * PRE:
     * -Jugador no nulo
     */
    public void recoger(CiudadRecoleccion juego) {
        ValidacionesUtiles.esDistintoDeNull(juego, "juego");

        if (!this.recogido) {
            elemento.aplicarEfecto(juego);
            this.recogido = true;
        }
    }

    /**
     *
     * @param ruta
     */
    protected void cargarImagen(String ruta) {
        try {
            this.imagen = ImageIO.read(getClass().getResourceAsStream(ruta));
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("No se pudo cargar imagen: " + ruta);
            this.imagen = null;
        }
    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Devuelve las posiciones en X de la vista
     *
     * PRE:
     * -Vista no debe ser nulo
     *
     * @param vista: Vista de la que se obtienen las posiciones en x
     * @return
     */
    protected int getScreenX(Vista vista) {
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");

        return getWorldX() - vista.getJugadorVista().getWorldX() + vista.getJugadorVista().getScreenX();
    }

    /**
     * Devuelve las posiciones en Y de la vista
     *
     * PRE:
     * -Vista no debe ser nulo
     *
     * @param vista: Vista de la que se obtienen las posiciones en x
     * @return
     */
    protected int getScreenY(Vista vista) {
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");

        return getWorldY() - vista.getJugadorVista().getWorldY() + vista.getJugadorVista().getScreenY();
    }

    /**
     * Getter del atributo elemento
     * @return: valor del atributo elemento
     */
    public Elemento getElemento() {
        return this.elemento;
    }

    /**
     * Getter del atributo imagen
     * @return: Devuelve la imagen guardada en el atributo
     */
    public BufferedImage getImagen(){
        return this.imagen;
    }

    /**
     * Getter del atributo recogido
     * @return: Devuelve el estado del atributo recogido
     */
    public boolean isRecogido(){
        return this.recogido;
    }

    /**
     * Getter del atributo nivel
     * @return: Devuelve el valor del atributo nivel
     */
    public int getNivel(){
        return this.nivel;
    }


    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Setter del atributo elemento
     *
     * PRE
     * -Elemento no debe ser nulo
     *
     * @param elemento: elemento a guardar en el atributo
     */
    private void setElemento(Elemento elemento){
        ValidacionesUtiles.esDistintoDeNull(elemento, "elemento");
        this.elemento = elemento;
    }

    /**
     * Setter del atributo imagen
     *
     * PRE:
     * -Img no debe ser nulo
     *
     * @param img: imagen a setear en el atributo imagen
     */
    protected void setImagen(BufferedImage img){
        ValidacionesUtiles.esDistintoDeNull(img, "img");
        this.imagen = img;
    }

    /**
     * Setter del atributo recogido
     *
     * PRE:
     * -Recogido no debe ser nulo
     *
     * @param recogido
     */
    private void setRecogido(boolean recogido){
        ValidacionesUtiles.esDistintoDeNull(recogido, "recogido");
        this.recogido = recogido;
    }

    /**
     * Setter del atributo nivel
     *
     * PRE:
     * -Nivel debe ser mayor o igual a cero
     *
     * @param nivel: Nivel del jugador
     */
    private void setNivel(int nivel){
        ValidacionesUtiles.validarMayorOIgualACero(nivel, "nivel");
        this.nivel = nivel;
    }


}