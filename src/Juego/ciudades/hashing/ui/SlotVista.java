package juego.ciudades.hashing.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import juego.ciudades.hashing.CiudadHashing;
import estructuras.hashing.HashTable.EntradaHash;
import estructuras.listas.ListaSimplementeEnlazada;
import modelos.Elemento;
import modelosVista.ObjetoVista;
import modelosVista.Vista;


/**
 * Representacion visual de un slot de la tabla hash dentro del mundo del juego.
 * 
 * Cada SlotVista se dibuja como una caja con su indice
 * debajo, tendra la cadena de entradas que contiene (encadenamiento por colision).
 * Lee su contenido en vivo desde la CiudadHashing, para nunca quedar desincronizado.
 * 
 * Estados Visuales:
 * -NORMAL      >>> Borde Azul
 * -DESTACADO   >>> Borde Amarillo (slot mas cercano al jugador)
 * -FLASH OK    >>> Borde Verde por unos frames (Insercion o Busqueda correcta)
 * -FLASH ERROR >>> Borde Rojo por unos frames (jugador eligio mal slot.)
 */
public class SlotVista extends ObjetoVista {

    //ENUMERADOS
    /**
     * Cuando se resalta brevemente luego de una accion
     */
    private enum Flash {NINGUNO, OK, ERROR}


    //CONSTANTES
    private static final int   DURACION_FLASH        = 50;   // frames que dura el flash
    private static final int   ALTO_ENTRADA          = 26;   // alto en px de cada eslabon de la cadena
    private static final int   ESPACIO_ENTRE_CAJAS   = 6;    // separacion vertical en px
    
    private static final Color COLOR_HEADER_TOP      = new Color( 60,   110,   170);
    private static final Color COLOR_HEADER_BOT      = new Color( 25,   60,    110);
    private static final Color COLOR_ENTRADA         = new Color( 45,   90,    140);
    private static final Color COLOR_VACIO           = new Color( 120,  140,   170);
    private static final Color COLOR_TEXTO           = Color.WHITE;
    private static final Color COLOR_BORDE_NORMAL    = new Color( 100,  150,   220);
    private static final Color COLOR_BORDE_DESTACADO = new Color( 255,  220,   50);
    private static final Color COLOR_BORDE_OK        = new Color( 60,   200,   90);
    private static final Color COLOR_BORDE_ERROR     = new Color( 220,  60,    60);

    private static final Font FONT_INDICE            = new Font("Monospaced", Font.BOLD, 14);
    private static final Font FONT_ENTRADA           = new Font("Monospaced", Font.BOLD, 11);
    private static final Font FONT_CHICA             = new Font("Monospaced", Font.PLAIN, 9);


    //ATRIBUTOS
    private final CiudadHashing ciudad;
    private final int indice;

    private boolean destacado = false;
    private Flash flash = Flash.NINGUNO;
    private int tickFlash = 0;


    //CONSTRUCTORES
    /**
     * PRE: ciudad no nula, el indice debe ser valido dentro de la tabla. 
     * POST: crea el slot visual en la posicion de mundo indicada. 
     * 
     * @param ciudad ciudad de hashing (fuente de verdad del contenido del slot)
     * @param indice indice del slot en la tabla
     * @param worldX posicion X en el mundo (px)
     * @param worldY posicion Y en el mundo (px)
     */
    public SlotVista(CiudadHashing ciudad, int indice, int worldX, int worldY) {
        super (worldX, worldY, "Slot " + indice, false, new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
        if (ciudad == null) {
            throw new IllegalArgumentException("ERROR: La ciudad no puede ser nula.");
        }
        this.ciudad = ciudad;
        this.indice = indice;
    }


    //METODOS DE COMPORTAMIENTO
    /**
     * POST: avanza el contador del flash y se apaga cuando se cumple la duracion. 
     */
    public void actualizar() {
        if (flash != Flash.NINGUNO) {
            tickFlash++;
            if (tickFlash >= DURACION_FLASH) {
                flash = Flash.NINGUNO;
                tickFlash = 0;
            }
        }
    }

    /**
     * POST: marca un flash verde ya que la accion fue correcta (mencionado en las primeras lineas).
     */
    public void marcarOk() {
        flash = Flash.OK;
        tickFlash = 0;
    }

    /**
     * POST: marca un flash rojo ya que la accion fue incorrecta.
     */
    public void marcarError() {
        flash = Flash.ERROR;
        tickFlash = 0;
    }



    //DIBUJO
    /**
     * POST: dibujo el slot y su cadena de entradas si esta dentro de la pantalla. 
     */
    @Override
    public void draw(Graphics2D g2, Vista vista) {
        if (!estaEnPantalla(vista)) {
            return;
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int tam = vista.getTamanio();
        int sx  = getScreenX(vista);
        int sy  = getScreenY(vista);

        dibujarHeader(g2, sx, sy, tam);
        dibujarCadena(g2, sx, sy, tam);
    }

    /**
     * POST: dibuja la caja superior del slot con su indice y el borde segun su estado. 
     */
    private void dibujarHeader(Graphics2D g2, int sx, int sy, int tam) {
        java.awt.GradientPaint grad = new java.awt.GradientPaint(
                                            sx, sy, COLOR_HEADER_TOP,
                                            sx, sy + tam, COLOR_HEADER_BOT);
        g2.setPaint(grad);
        g2.fillRoundRect(sx, sy, tam, tam, 8, 8);

        g2.setColor(obtenerColorBorde());
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawRoundRect(sx, sy, tam, tam, 8, 8);
        g2.setStroke(new BasicStroke(1f));

        g2.setFont(FONT_INDICE);
        g2.setColor(COLOR_TEXTO);
        FontMetrics fm = g2.getFontMetrics();
        String txt = String.valueOf(indice);
        int tx = sx + (tam - fm.stringWidth(txt)) / 2;
        g2.drawString(txt, tx, sy + tam / 2 + 5);

        g2.setFont(FONT_CHICA);
        g2.setColor(new Color(200, 220, 245));
        String etiqueta = "slot";
        int ex = sx + (tam - g2.getFontMetrics().stringWidth(etiqueta)) / 2;
        g2.drawString(etiqueta, ex, sy + 12);
    }

    /**
     * POST: dibuja debajo del header cada entrada encadenada del slot
     * va leyendo la tabla en vivo
     */
    
    private void dibujarCadena(Graphics2D g2, int sx, int sy, int tam) {
        ListaSimplementeEnlazada<EntradaHash<Integer, Elemento>> slot = ciudad.getSlot(indice);

        int y = sy + tam + ESPACIO_ENTRE_CAJAS;

        if (slot.size() == 0) {
            g2.setFont(FONT_CHICA);
            g2.setColor(COLOR_VACIO);
            g2.drawString("(vacio)", sx + 2, y + 12);
            return;
        }

        for (EntradaHash<Integer, Elemento> entrada : slot) {
            // Flecha de encadenamiento
            g2.setColor(COLOR_VACIO);
            g2.drawString("|", sx + tam / 2, y - 1);

            // Caja de la entrada
            g2.setColor(COLOR_ENTRADA);
            g2.fillRoundRect(sx, y, tam, ALTO_ENTRADA, 6, 6);
            g2.setColor(COLOR_BORDE_NORMAL);
            g2.drawRoundRect(sx, y, tam, ALTO_ENTRADA, 6, 6);

            // Texto: clave arriba, nombre abajo
            g2.setFont(FONT_ENTRADA);
            g2.setColor(COLOR_TEXTO);
            String clave = String.valueOf(entrada.getClave());
            g2.drawString(clave, sx + 4, y + 12);

            g2.setFont(FONT_CHICA);
            g2.setColor(new Color(200, 220, 245));
            String nombre = entrada.getValor().getNombre();
            g2.drawString(recortar(nombre, 7), sx + 4, y + 23);

            y += ALTO_ENTRADA + ESPACIO_ENTRE_CAJAS;
        }
    }

    /**
     * POST: devuelve el color de borde segun el estado actual. (flash va por encima de destacado)
     */
    private Color obtenerColorBorde() {
        if (flash == Flash.OK) {
            return COLOR_BORDE_OK;
        }
        if (flash == Flash.ERROR) {
            return COLOR_BORDE_ERROR;
        }
        if (destacado) {
            return COLOR_BORDE_DESTACADO;
        }
        return COLOR_BORDE_NORMAL;
    }

    /**
     * POST: Recorta un texto a una cantidad maxima de caracteres para que entre en la caja. 
     */
    private String recortar(String texto, int max) {
        if (texto.length() <= max) {
            return texto;
        }
        return texto.substring(0, max);
    }



    //GETTERS
    public int getIndice(){
        return this.indice;
    }


    //SETTERS
    public void setDestacado(boolean destacado) {
        this.destacado = destacado;
    }



}
