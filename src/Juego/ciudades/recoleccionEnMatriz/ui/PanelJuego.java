package Juego.ciudades.recoleccionEnMatriz.ui;

import Juego.Constantes;
import Juego.ciudades.recoleccionEnMatriz.CartaDesplazamiento;
import Juego.ciudades.recoleccionEnMatriz.CartaPuntos;
import Juego.ciudades.recoleccionEnMatriz.CartaVision;
import Juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelos.Celda;
import modelos.Elemento;
import modelos.Jugador;
import modelos.Mapa;
import utils.ValidacionesUtiles;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;

public class PanelJuego extends JPanel {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private CiudadRecoleccion juego;
    private int filas;
    private int columnas;
    private Map<String, BufferedImage> tileCache = new HashMap<>();
    private PanelHUD hud;
    private boolean mostrarMochila = false;
    private String mensajeTemp = null;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    /**
     * Constructor del TDA PanelJuego
     *
     * PRE:
     * -Juego no debe ser nulo
     * -Filas y columnas deben ser mayores a cero
     *
     * @param juego: juego
     * @param filas: filas del nivel
     * @param columnas: columnas del nivel
     */
    public PanelJuego(CiudadRecoleccion juego, int filas, int columnas) {
        ValidacionesUtiles.esDistintoDeNull(juego, "juego");
        ValidacionesUtiles.validarMayorACero(filas, "filas");
        ValidacionesUtiles.validarMayorACero(columnas, "columnas");

        setJuego(juego);
        setFilas(filas);
        setColumnas(columnas);

        setOpaque(true);
        setBackground(Constantes.COLOR_FONDO);

        int ancho = columnas * Constantes.TILE_SIZE + Constantes.PADDING * 2;
        int alto  = filas   * Constantes.TILE_SIZE + Constantes.PADDING * 2 + 40;
        setPreferredSize(new Dimension(ancho, alto));

        generarBitmaps();
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PanelJuego that = (PanelJuego) o;
        return filas == that.filas && columnas == that.columnas && mostrarMochila == that.mostrarMochila && Objects.equals(juego, that.juego) && Objects.equals(tileCache, that.tileCache) && Objects.equals(hud, that.hud) && Objects.equals(mensajeTemp, that.mensajeTemp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(juego, filas, columnas, tileCache, hud, mostrarMochila, mensajeTemp);
    }

    @Override
    public String toString() {
        return "PanelJuego{" +
                "juego=" + juego +
                ", filas=" + filas +
                ", columnas=" + columnas +
                ", tileCache=" + tileCache +
                ", hud=" + hud +
                ", mostrarMochila=" + mostrarMochila +
                ", mensajeTemp='" + mensajeTemp + '\'' +
                '}';
    }

    //METODOS GENERALES ---------------------------------------------------------------------------------------
    /**
     * Genera y cachea todos los bitmaps del juego.
     * Se llama una sola vez en el constructor.
     */
    private void generarBitmaps() {
        tileCache.put(Constantes.TILE_VACIO,   crearTileVacio());
        tileCache.put(Constantes.TILE_JUGADOR, crearTileJugador());
        tileCache.put(Constantes.TILE_VISION,  crearTileVision());
        tileCache.put(Constantes.TILE_DESPLAZ, crearTileDesplazamiento());
        tileCache.put(Constantes.TILE_PUNTOS,  crearTilePuntos());
    }

    /**
     * Crea una imagen de celda vacia
     * @return: Devuelve la imagen creada
     */
    private BufferedImage crearTileVacio() {
        BufferedImage img = nuevaImagen();
        Graphics2D g = setup(img);
        g.setColor(Constantes.COLOR_VACIO);
        g.fillRect(0, 0, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
        g.setColor(Constantes.COLOR_CELDA_BORDE);
        g.drawRect(0, 0, Constantes.TILE_SIZE - 1, Constantes.TILE_SIZE - 1);
        g.dispose();
        return img;
    }

    /**
     * Crea la imagen del jugador
     * @return: Devuelve la imagen del jugador
     */
    private BufferedImage crearTileJugador() {
        BufferedImage img = nuevaImagen();
        Graphics2D g = setup(img);

        // Fondo de celda
        g.setColor(Constantes.COLOR_CELDA);
        g.fillRect(0, 0, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
        g.setColor(Constantes.COLOR_CELDA_BORDE);
        g.drawRect(0, 0, Constantes.TILE_SIZE - 1, Constantes.TILE_SIZE - 1);

        // Sombra del círculo
        g.setColor(new Color(0, 0, 0, 80));
        g.fillOval(5, 7, Constantes.TILE_SIZE - 8, Constantes.TILE_SIZE - 8);

        // Cuerpo del jugador
        GradientPaint gp = new GradientPaint(6, 6, Constantes.COLOR_JUGADOR.brighter(), Constantes.TILE_SIZE - 6, Constantes.TILE_SIZE - 6, Constantes.COLOR_JUGADOR.darker());
        g.setPaint(gp);
        g.fillOval(4, 4, Constantes.TILE_SIZE - 8, Constantes.TILE_SIZE - 8);

        // Punto central
        g.setColor(Color.WHITE);
        g.fillOval(Constantes.TILE_SIZE / 2 - 3, Constantes.TILE_SIZE / 2 - 3, 6, 6);

        g.dispose();
        return img;
    }

    /**
     * Crea la imagen de la carta vision
     * @return: Devuelve la imagen de la carta vision
     */
    private BufferedImage crearTileVision() {
        BufferedImage img = nuevaImagen();
        Graphics2D g = setup(img);

        g.setColor(Constantes.COLOR_CELDA);
        g.fillRect(0, 0, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
        g.setColor(Constantes.COLOR_CELDA_BORDE);
        g.drawRect(0, 0, Constantes.TILE_SIZE - 1, Constantes.TILE_SIZE - 1);

        // Rombo / forma de ojo
        int cx = Constantes.TILE_SIZE / 2, cy = Constantes.TILE_SIZE / 2;
        int[] xp = {cx, cx + 10, cx, cx - 10};
        int[] yp = {cy - 7, cy, cy + 7, cy};
        g.setColor(Constantes.COLOR_CARTA_VISION.darker());
        g.fillPolygon(xp, yp, 4);
        g.setColor(Constantes.COLOR_CARTA_VISION);
        g.drawPolygon(xp, yp, 4);

        // Pupila
        g.setColor(new Color(20, 60, 120));
        g.fillOval(cx - 4, cy - 4, 8, 8);
        g.setColor(Color.WHITE);
        g.fillOval(cx - 2, cy - 4, 3, 3);

        g.dispose();
        return img;
    }

    /**
     * Crea la imagen de la carta desplazamiento
     * @return: Devuelve la imagen de la carta desplazamiento
     */
    private BufferedImage crearTileDesplazamiento() {
        BufferedImage img = nuevaImagen();
        Graphics2D g = setup(img);

        g.setColor(Constantes.COLOR_CELDA);
        g.fillRect(0, 0, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
        g.setColor(Constantes.COLOR_CELDA_BORDE);
        g.drawRect(0, 0, Constantes.TILE_SIZE - 1, Constantes.TILE_SIZE - 1);

        // Rayo (zigzag)
        int[] xp = {18, 13, 16, 11, 18, 14, 20};
        int[] yp = { 4, 14, 14, 28, 17, 17,  4};
        g.setColor(Constantes.COLOR_CARTA_DESPLAZ.darker());
        g.fillPolygon(xp, yp, 7);
        g.setColor(Constantes.COLOR_CARTA_DESPLAZ);
        g.setStroke(new BasicStroke(1.2f));
        g.drawPolygon(xp, yp, 7);

        g.dispose();
        return img;
    }

    /**
     * Crea la imagen de la carta puntos
     * @return: Devuelve la imagen de la carta puntos
     */
    private BufferedImage crearTilePuntos() {
        BufferedImage img = nuevaImagen();
        Graphics2D g = setup(img);

        g.setColor(Constantes.COLOR_CELDA);
        g.fillRect(0, 0, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
        g.setColor(Constantes.COLOR_CELDA_BORDE);
        g.drawRect(0, 0, Constantes.TILE_SIZE - 1, Constantes.TILE_SIZE - 1);

        // Estrella de 5 puntas
        int cx = Constantes.TILE_SIZE / 2, cy = Constantes.TILE_SIZE / 2;
        Polygon star = crearEstrella(cx, cy, 12, 5, 5);
        g.setColor(Constantes.COLOR_CARTA_PUNTOS.darker());
        g.fillPolygon(star);
        g.setColor(Constantes.COLOR_CARTA_PUNTOS);
        g.setStroke(new BasicStroke(1f));
        g.drawPolygon(star);

        g.dispose();
        return img;
    }

    /**
     * Dibuja toda la imagen de la interfaz grafica utilizando los elementos previamente creados
     *
     * PRE:
     * -g0 no debe ser nulo
     *
     * @param g0:
     */
    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Fondo general
        g.setColor(Constantes.COLOR_FONDO);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Obtener nivel actual del jugador
        int[] posJugador = juego.getPosicionJugador();
        if (posJugador == null) return;

        int nivelActual = posJugador[2];

        // Barra superior: indicador de nivel
        dibujarBarraNivel(g, nivelActual);

        // Grilla del mapa
        dibujarMapa(g, nivelActual, posJugador);

        // Overlay de mochila (si está visible)
        if (mostrarMochila) {
            dibujarPanelMochila(g);
        }

        // Mensaje temporal
        if (mensajeTemp != null) {
            dibujarMensaje(g, mensajeTemp);
        }

        // Pantalla de fin
        if (juego.estaFinalizado()) {
            dibujarPantallaFin(g);
        }
    }

    /**
     * Dibuja el indicador de nivel
     *
     * PRE:
     * -G no debe ser nulo
     * -Nivel debe ser mayor a cero
     *
     * @param g:
     * @param nivel: Nivel donde se encuentra el jugador
     */
    private void dibujarBarraNivel(Graphics2D g, int nivel) {
        ValidacionesUtiles.esDistintoDeNull(g, "g");
        ValidacionesUtiles.validarMayorACero(nivel, "nivel");

        g.setColor(new Color(40, 55, 85));
        g.fillRoundRect(Constantes.PADDING, 8, columnas * Constantes.TILE_SIZE, 26, 8, 8);

        g.setFont(new Font("Monospaced", Font.BOLD, 13));
        g.setColor(new Color(160, 200, 255));
        String txt = "  NIVEL " + nivel + " / " + Constantes.NIVELES_MAPA + "      [WASD] mover    [P] mochila";
        g.drawString(txt, Constantes.PADDING + 8, 26);
    }

    /**
     * Dibuja el mapa de la interfaz grafica
     *
     * PRE:
     * -G no debe ser nulo
     * -NivelActual debe ser mayor a cero
     * -PosJugador no debe ser nulo
     *
     * @param g:
     * @param nivelActual: Nivel actual del jugador
     * @param posJugador: Posicion actual del jugador
     */
    private void dibujarMapa(Graphics2D g, int nivelActual, int[] posJugador) {
        ValidacionesUtiles.esDistintoDeNull(g, "g");
        ValidacionesUtiles.validarMayorACero(nivelActual, "nivelActual");
        ValidacionesUtiles.esDistintoDeNull(posJugador, "posJugador");

        Mapa mapa = juego.getMapaNivel(nivelActual);
        if (mapa == null) return;

        int offsetY = Constantes.PADDING + 40;
        int visibilidad = juego.getVisibilidad();

        for (int fila = 1; fila <= filas; fila++) {
            for (int col = 1; col <= columnas; col++) {
                int px = Constantes.PADDING + (col - 1) * Constantes.TILE_SIZE;
                int py = offsetY + (fila - 1) * Constantes.TILE_SIZE;

                boolean esVisible = Math.abs(fila - posJugador[0]) <= visibilidad
                        && Math.abs(col  - posJugador[1]) <= visibilidad;

                try {
                    Celda<?> celda = mapa.getCeldaConPosicion(fila, col);
                    if (esVisible) {
                        BufferedImage tile = resolverTile(celda, posJugador);
                        g.drawImage(tile, px, py, Constantes.TILE_SIZE, Constantes.TILE_SIZE, this);
                    } else {
                        // Celda fuera de visibilidad: dibujar oscura
                        g.drawImage(tileCache.get(Constantes.TILE_VACIO), px, py, Constantes.TILE_SIZE, Constantes.TILE_SIZE, this);
                        g.setColor(new Color(0, 0, 0, 180)); // overlay oscuro
                        g.fillRect(px, py, Constantes.TILE_SIZE, Constantes.TILE_SIZE);
                    }
                } catch (RuntimeException e) {
                    g.drawImage(tileCache.get(Constantes.TILE_VACIO), px, py, Constantes.TILE_SIZE, Constantes.TILE_SIZE, this);
                }
            }
        }
        if (juego.getCartaDisponible() != null) {
            g.setFont(new Font("Monospaced", Font.BOLD, 13));
            g.setColor(new Color(255, 230, 100));
            g.drawString("[E] Recoger: " + juego.getCartaDisponible().getNombre(), Constantes.PADDING + 10, offsetY + filas * Constantes.TILE_SIZE + 20);
        }
    }

    /**
     * Verifica que contiene la celda
     *
     * PRE:
     * -PosJugador no debe ser nulo
     *
     * @param celda: Celda que verifica
     * @param posJugador: Posicion del jugador
     * @return: Devuelve el tile correspondiente
     */
    private BufferedImage resolverTile(Celda<?> celda, int[] posJugador) {
        ValidacionesUtiles.esDistintoDeNull(posJugador, "posJugador");

        if (celda == null) return tileCache.get(Constantes.TILE_VACIO);

        Object contenido = celda.getContenido();

        if (contenido instanceof Jugador)             return tileCache.get(Constantes.TILE_JUGADOR);
        if (contenido instanceof CartaVision)         return tileCache.get(Constantes.TILE_VISION);
        if (contenido instanceof CartaDesplazamiento) return tileCache.get(Constantes.TILE_DESPLAZ);
        if (contenido instanceof CartaPuntos)         return tileCache.get(Constantes.TILE_PUNTOS);

        return tileCache.get(Constantes.TILE_VACIO);
    }

    /**
     * Dibuja el panel de la mochila
     *
     * PRE:
     * -G no debe ser nulo
     *
     * @param g:
     */
    private void dibujarPanelMochila(Graphics2D g) {
        ValidacionesUtiles.esDistintoDeNull(g, "g");

        g.setColor(Constantes.COLOR_MOCHILA_FONDO);
        g.fillRoundRect(Constantes.PADDING + 20, Constantes.PADDING + 50, columnas * Constantes.TILE_SIZE - 40, 160, 12, 12);

        g.setColor(new Color(100, 160, 255));
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.drawString("── MOCHILA ──", Constantes.PADDING + 40, Constantes.PADDING + 80);

        var items = juego.getItemsMochila();
        g.setFont(new Font("Monospaced", Font.PLAIN, 13));
        int i = 1;
        for (Elemento el : items) {
            g.setColor(new Color(200, 220, 255));
            g.drawString("[" + i + "] " + el.getNombre(), Constantes.PADDING + 40, Constantes.PADDING + 80 + i * 22);
            i++;
        }

        if (i == 1) {
            g.setColor(new Color(120, 120, 160));
            g.drawString("(vacía)", Constantes.PADDING + 40, Constantes.PADDING + 102);
        }

        g.setColor(new Color(160, 160, 200));
        g.setFont(new Font("Monospaced", Font.ITALIC, 11));
        g.drawString("[1-3] usar carta    [Q] cerrar", Constantes.PADDING + 40, Constantes.PADDING + 200);
    }

    /**
     * Dibuja mensaje inferior, como cuando se encuentra una carta
     *
     * PRE:
     * -G no debe ser nulo
     * -Msg no debe ser nulo
     *
     * @param g:
     * @param msg: Mensaje a mostrar
     */
    private void dibujarMensaje(Graphics2D g, String msg) {
        ValidacionesUtiles.esDistintoDeNull(g, "g");
        ValidacionesUtiles.esDistintoDeNull(msg, "msg");

        g.setFont(new Font("Monospaced", Font.BOLD, 13));
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRoundRect(Constantes.PADDING + 10, getHeight() - 50, columnas * Constantes.TILE_SIZE - 20, 30, 8, 8);
        g.setColor(Constantes.COLOR_MENSAJE);
        g.drawString(msg, Constantes.PADDING + 20, getHeight() - 30);
    }

    /**
     * Dibuja el mensaje de final
     *
     * PRE:
     * -G no debe ser nulo
     *
     * @param g:
     */
    private void dibujarPantallaFin(Graphics2D g) {
        ValidacionesUtiles.esDistintoDeNull(g, "g");

        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setFont(new Font("Monospaced", Font.BOLD, 22));
        g.setColor(Constantes.COLOR_CARTA_PUNTOS);
        String txt = "¡NIVEL COMPLETADO!";
        FontMetrics fm = g.getFontMetrics();
        g.drawString(txt, (getWidth() - fm.stringWidth(txt)) / 2, getHeight() / 2);
    }

    /**
     * Crea una nueva imagen
     * @return: Devuelve la imagen cerada
     */
    private BufferedImage nuevaImagen() {
        return new BufferedImage(Constantes.TILE_SIZE, Constantes.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * Setea para dibujar la imagen
     *
     * PRE:
     * -Img no debe ser nulo
     *
     * @param img:
     * @return: Devuelve el objeto g, que permite dibujar la imagen
     */
    private Graphics2D setup(BufferedImage img) {
        ValidacionesUtiles.esDistintoDeNull(img, "img");

        Graphics2D g = img.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        return g;
    }

    /**
     * Crea la estrella asociada a la carta puntos
     *
     * PRE:
     * -Los parametros no deben ser menores a cero
     * -Puntas debe ser mayor a cero
     *
     * @param cx
     * @param cy
     * @param rExt
     * @param rInt
     * @param puntas
     * @return: Devuelve el poligono con forma de estrella
     */
    private Polygon crearEstrella(int cx, int cy, int rExt, int rInt, int puntas) {
        ValidacionesUtiles.validarMayorOIgualACero(cx, "cx");
        ValidacionesUtiles.validarMayorOIgualACero(cy, "cy");
        ValidacionesUtiles.validarMayorOIgualACero(rExt, "rExt");
        ValidacionesUtiles.validarMayorOIgualACero(rInt, "rInt");
        ValidacionesUtiles.validarMayorACero(puntas, "puntas");

        Polygon p = new Polygon();
        double paso = Math.PI / puntas;
        for (int i = 0; i < 2 * puntas; i++) {
            double ang = i * paso - Math.PI / 2;
            int r = (i % 2 == 0) ? rExt : rInt;
            p.addPoint(cx + (int)(r * Math.cos(ang)), cy + (int)(r * Math.sin(ang)));
        }
        return p;
    }

    /**
     * Muestra un mensaje y forza a que la interfaz se redibuje
     *
     * PRE:
     * -Msg no debe ser nulo
     *
     * @param msg:
     */
    public void mostrarMensaje(String msg){
        ValidacionesUtiles.esDistintoDeNull(msg, "msg");
        this.mensajeTemp = msg; repaint();
    }
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Getter del atributo juego
     * @return: Devuelve juego
     */
    public CiudadRecoleccion getJuego() {
        return this.juego;
    }

    /**
     * Getter del atributo filas
     * @return: Devuelve filas
     */
    public int getFilas() {
        return this.filas;
    }

    /**
     * Getter del atributo columnas
     * @return: Devuelve columnas
     */
    public int getColumnas() {
        return this.columnas;
    }

    /**
     * Getter del atributo tileCache
     * @return: devuelve el hashmap
     */
    public Map<String, BufferedImage> getTileCache() {
        return this.tileCache;
    }

    /**
     * Getter del atributo hud
     * @return: Devuelve el PanelHUD de hud
     */
    public PanelHUD getHUD(){
        return this.hud;
    }

    /**
     * Getter del atributo mostrarMochila
     * @return: Devuelve el estado del atributo (true o false)
     */
    public boolean isMostrarMochila() {
        return this.mostrarMochila;
    }

    /**
     * Getter del atributo mensajeTemp
     * @return: Devuelve el string del atributo
     */
    public String getMensajeTemp() {
        return this.mensajeTemp;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Setter del atributo juego
     *
     * PRE:
     * -Juego no debe ser nulo
     *
     * @param juego: juego de la ciudad
     */
    private void setJuego(CiudadRecoleccion juego) {
        ValidacionesUtiles.esDistintoDeNull(juego, "juego");
        this.juego = juego;
    }

    /**
     * Setter del atributo filas
     *
     * PRE:
     * -Filas debe ser mayor a cero
     *
     * @param filas: filas del nivel
     */
    private void setFilas(int filas) {
        ValidacionesUtiles.validarMayorACero(filas, "filas");
        this.filas = filas;
    }

    /**
     * Setter del atributo columnas
     *
     * PRE:
     * -Columnas debe ser mayor a cero
     *
     * @param columnas: filas del nivel
     */
    private void setColumnas(int columnas) {
        ValidacionesUtiles.validarMayorACero(columnas, "columnas");
        this.columnas = columnas;
    }

    /**
     * Setter del atributo hud
     *
     * PRE:
     * -Hud no debe ser nulo
     *
     * @param hud:
     */
    public void setHUD(PanelHUD hud) {
        ValidacionesUtiles.esDistintoDeNull(hud, "hud");
        this.hud = hud;
    }


    /**
     * Setter del atributo mostrarMochila
     *
     * PRE:
     * -V no debe ser nulo
     *
     * @param v:
     */
    public void setMostrarMochila(boolean v){
        ValidacionesUtiles.esDistintoDeNull(v, "v");
        this.mostrarMochila = v;
    }


}
