package juego.ciudades.recoleccionEnMatriz.ui;

import juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelosVista.JugadorVista;
import modelos.Minijuego;
import modelosVista.Vista;
import utils.ValidacionesUtiles;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MinijuegoRecoleccion implements Minijuego {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private CiudadRecoleccion juego = null;
    private KeyHandlerRecoleccion key = null;
    private Runnable onFinalizadoCallback = null;
    private JFrame ventana = null;
    private List<CartaVista> cartas = new ArrayList<>();

    private boolean finalizado     = false;
    private boolean mochilaVisible = false;
    public int cartaPresionada = 0;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * Constructor del TDA MinijuegoRecoleccion
     *
     * PRE:
     * -Juego, vista, key y ventana no deben ser nulo
     *
     * @param juego: Juego asocioado a la UI
     * @param vista: Vista general de la UI
     * @param key: Manejador de controles propios del juego
     * @param ventana: Vista propia del juego
     */
    public MinijuegoRecoleccion(CiudadRecoleccion juego, Vista vista, KeyHandlerRecoleccion key, JFrame ventana) {
        ValidacionesUtiles.esDistintoDeNull(juego, "juego");
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");
        ValidacionesUtiles.esDistintoDeNull(key, "key");
        ValidacionesUtiles.esDistintoDeNull(ventana, "ventana");

        setJuego(juego);
        setKey(key);
        setVentana(ventana);
        inyectarCartas(vista);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Guarda todas las vistas de las cartas
     *
     * PRE:
     * -Vista no debe ser nulo
     *
     * @param vista: vista general de la UI
     */
    private void inyectarCartas(Vista vista) {
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");
        this.cartas.addAll(juego.getCartasVista(vista.getTamanio()));
    }

    /**
     * Actualiza el estado del minijuego frame a frame
     *
     * PRE:
     * -Jugador no debe ser nulo
     *
     * @param jugador: Jugador
     */
    @Override
    public void actualizar(JugadorVista jugador) {
        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");

        int tamaño = jugador.getVistaDelJuego().getTamanio();
        int col  = jugador.getWorldX() / tamaño + 1;
        int fila = jugador.getWorldY() / tamaño + 1;
        boolean colisionaCarta = false;

        if (finalizado){
            return;
        }

        // Colisión con cartas — chequear posición futura
        for (CartaVista carta : cartas) {
            if (carta.isRecogido() || carta.getNivel() != obtenerNivelActual()){
                continue;
            }

            int cartaTileX = carta.getWorldX() / tamaño; // columna 0-based
            int cartaTileY = carta.getWorldY() / tamaño; // fila 0-based

            int futuroX = jugador.getWorldX();
            int futuroY = jugador.getWorldY();
            int vel = jugador.getVelocidad();

            switch (jugador.getDireccion()) {
                case ARRIBA    -> futuroY -= vel;
                case ABAJO     -> futuroY += vel;
                case IZQUIERDA -> futuroX -= vel;
                case DERECHA   -> futuroX += vel;
            }

            int futuroTileX = futuroX / tamaño;
            int futuroTileY = futuroY / tamaño;

            // Si el tile futuro del jugador coincide con el tile de la carta, bloquear
            if (futuroTileX == cartaTileX && futuroTileY == cartaTileY) {
                colisionaCarta = true;
                break;
            }
        }
        if (!colisionaCarta) {
            juego.actualizarPosicionJugador(col, fila);
        }

        // Recoger carta
        if (key.getEPressed()) {
            int nivelAntes = obtenerNivelActual();
            int nivelDespues = obtenerNivelActual();

            juego.recogerCarta();
            key.modificarEstadoEPressed(false);

            if (nivelDespues != nivelAntes) {
                int t = jugador.getVistaDelJuego().getTamanio();
                jugador.setWorldX(2 * t);
                jugador.setWorldY(2 * t);
            }
        }

        // Abrir/cerrar mochila
        if (key.getPPressed()) {
            mochilaVisible = !mochilaVisible;
            key.modificarEstadoPPressed(false);
        }

        // Usar carta de la mochila
        if (mochilaVisible && key.getCartaPresionada() > 0) {
            try {
                juego.usarCartaMochila(key.getCartaPresionada());
            } catch (RuntimeException ex) {}
            key.restablecerCartaPresionada();
        }

        // Sincronizar velocidad visual con desplazamiento del modelo
        jugador.setVelocidad(4 * juego.getDesplazamiento());

        // Verificar fin de juego
        if (juego.estaFinalizado()) {
            this.finalizado = true;
            if (onFinalizadoCallback != null){
                onFinalizadoCallback.run();
            }

            javax.swing.SwingUtilities.invokeLater(() -> {javax.swing.JOptionPane.showMessageDialog(
                    ventana,
           "¡Ciudad completada!\nPuntos obtenidos: " + juego.getPuntos(),
               "Fin del juego",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

            if (ventana != null){
                ventana.dispose();
            }
            }
         );
      }
    }

    /**
     * Dibuja el rango de visibilidad limitado del jugador
     *
     * PRE:
     * -G2 y jugador no deben ser nulos
     *
     * @param g2: Vista del juego donde se limita la visibilidad
     * @param jugador: Jugador
     */
    private void dibujarOverlayVisibilidad(Graphics2D g2, JugadorVista jugador) {
        ValidacionesUtiles.esDistintoDeNull(g2, "g2");
        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");

        modelosVista.Vista vista = jugador.getVistaDelJuego();
        int tamaño = vista.getTamanio();
        int visibilidad = juego.getVisibilidad();

        int jugadorTileX = jugador.getWorldX() / tamaño;
        int jugadorTileY = jugador.getWorldY() / tamaño;

        int mitadCols  = vista.getAnchoDePantalla()  / tamaño / 2 + 1;
        int mitadFilas = vista.getLargoDePantalla() / tamaño / 2 + 1;

        for (int col = jugadorTileX - mitadCols; col <= jugadorTileX + mitadCols; col++) {
            for (int fila = jugadorTileY - mitadFilas; fila <= jugadorTileY + mitadFilas; fila++) {
                if (col < 0 || fila < 0 || col >= vista.getColumnasDelMundo() || fila >= vista.getFilasDelMundo()){
                    continue;
                }

                int distCol  = Math.abs(col  - jugadorTileX);
                int distFila = Math.abs(fila - jugadorTileY);

                if (distCol > visibilidad || distFila > visibilidad) {
                    int screenX = col  * tamaño - jugador.getWorldX() + jugador.getScreenX();
                    int screenY = fila * tamaño - jugador.getWorldY() + jugador.getScreenY();

                    int dist  = Math.max(distCol, distFila);
                    int alpha = Math.min(210, 150 + (dist - visibilidad) * 15);
                    g2.setColor(new Color(0, 0, 0, alpha));
                    g2.fillRect(screenX, screenY, tamaño, tamaño);
                }
            }
        }
    }

    /**
     * Dibuja todos los elementos o entidades en la vista de la UI
     *
     * PRE:
     * -G2 y jugador no debe ser nulos
     *
     * @param g2: Vista del juego donde se dibujan los elementos/entidades
     * @param jugador: Jugador
     */
    public void draw(Graphics2D g2, JugadorVista jugador) {
        ValidacionesUtiles.esDistintoDeNull(g2, "g2");
        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");

        int[] pos = juego.getPosicionJugador();

        int nivelActual = 0;
        if (pos != null) {
            nivelActual = pos[2];
        } else {
            nivelActual = 1;
        }

        for (CartaVista carta : cartas) {
            carta.draw(g2, jugador.getVistaDelJuego(), nivelActual);
        }

        dibujarOverlayVisibilidad(g2, jugador);
        dibujarHUD(g2);
        if (mochilaVisible) dibujarMochila(g2);
    }

    /**
     * Dibuja el panel HUD con los datos del juego
     *
     * PRE:
     * -G2 no debe ser nulo
     *
     * @param g2
     */
    private void dibujarHUD(Graphics2D g2) {
        ValidacionesUtiles.esDistintoDeNull(g2, "g2");
        int x = 10, y = 20;
        g2.setColor(new Color(0, 0, 0, 160));
        g2.fillRoundRect(x, y, 200, 90, 12, 12);

        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(new Color(255, 220, 50));
        g2.drawString("Puntos: "   + juego.getPuntos(),         x + 10, y + 22);
        g2.setColor(new Color(100, 200, 255));
        g2.drawString("Visión: "   + juego.getVisibilidad(),    x + 10, y + 42);
        g2.setColor(new Color(150, 255, 150));
        g2.drawString("Desplaz.: " + juego.getDesplazamiento(), x + 10, y + 62);
        g2.setColor(Color.WHITE);
        g2.drawString("Mochila: [P]",                           x + 10, y + 82);
    }

    /**
     * Dibuja la vista de la mochila
     *
     * PRE:
     * -G2 no debe ser nulo
     *
     * @param g2
     */
    private void dibujarMochila(Graphics2D g2) {
        ValidacionesUtiles.esDistintoDeNull(g2, "g2");

        int x = 10, y = 120;
        var items = juego.getItemsMochila();
        int cantidad = (items != null) ? items.size() : 0;
        int alto = 30 + cantidad * 20 + 10;

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(x, y, 220, alto, 12, 12);
        g2.setColor(new Color(200, 200, 200));
        g2.drawRoundRect(x, y, 220, alto, 12, 12);

        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(Color.WHITE);
        g2.drawString("── Mochila ──", x + 10, y + 20);

        if (cantidad == 0) {
            g2.setFont(new Font("Arial", Font.ITALIC, 12));
            g2.setColor(new Color(160, 160, 160));
            g2.drawString("(vacía)", x + 10, y + 40);
            return;
        }

        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(220, 220, 100));
        int ly = y + 40;
        for (int i = 0; i < items.size(); i++) {        // ← 0-based
            g2.drawString((i + 1) + ". " + items.get(i).getNombre(), x + 10, ly);  // ← get(i)

            ly += 20; 	
           }
    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------

    /**
     * Metodo para obtener el nivel actual del jugador
     *
     * @return: Devuelve el nivel
     */
    private int obtenerNivelActual() {
        int[] pos = juego.getPosicionJugador();
        return (pos != null) ? pos[2] : 1;
    }
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Getter del atributo juego
     * @return: Devuelve el valor del atributo juego
     */
    public CiudadRecoleccion getJuego(){
        return this.juego;
    }

    /**
     * Getter del atributo key
     * @return: Devuelve el valor del atributo key
     */
    public KeyHandlerRecoleccion getKey(){
        return this.key;
    }

    /**
     * Getter del atributo onFinalizadoCallback
     * @return: Devuelve el valor del atributo onFinalizadoCallback
     */
    public Runnable getOnFinalizadoCallback(){
        return this.onFinalizadoCallback;
    }

    /**
     * Getter del atributo ventana
     * @return: Devuelve el valor del atributo ventana
     */
    public JFrame getVentana(){
        return this.ventana;
    }

    /**
     * Getter del atributo cartas
     * @return: Devuelve la lista del atributo cartas
     */
    public List<CartaVista> getCartas(){
        return this.cartas;
    }

    /**
     * Getter del atributo finalizado
     * @return: Devuelve el estado del atriuto finalizado
     */
    public boolean getFinalizado(){
        return this.finalizado;
    }

    /**
     * Getter del atributo mochilaVisible
     * @return: Devuelve el estado del atriuto mochilaVisible
     */
    public boolean getMochilaVisible(){
        return this.mochilaVisible;
    }

    /**
     * Getter del atributo cartaPresionada
     * @return: Devuelve el valor del atributo cartaPresionada
     */
    public int getCartasPresionadas(){
        return this.cartaPresionada;
    }

    /**
     * Getter del puntaje del juego
     * @return: puntaje del objeto juego
     */
    public int getPuntaje(){
        return this.juego.getPuntos();
    }

    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Setter del atributo juego
     *
     * PRE:
     * -Juego no debe ser nulo
     *
     * @param juego: Objeto juego nuevo
     */
    private void setJuego(CiudadRecoleccion juego){
        ValidacionesUtiles.esDistintoDeNull(juego, "juego");
        this.juego = juego;
    }

    /**
     * Setter del atributo key
     *
     * PRE:
     * -Key no debe ser nulo
     *
     * @param key: Objeto key nuevo
     */
    private void setKey(KeyHandlerRecoleccion key){
        ValidacionesUtiles.esDistintoDeNull(key, "key");
        this.key = key;
    }

    /**
     * Setter del atributo onFinalizadoCallback
     *
     * PRE:
     * -Callback no debe ser nulo
     *
     * @param callback: Objeto callback nuevo
     */
    public void setOnFinalizadoCallback(Runnable callback){
        ValidacionesUtiles.esDistintoDeNull(callback, "callback");
        this.onFinalizadoCallback = callback;
    }

    /**
     * Setter del atributo ventana
     *
     * PRE:
     * -Ventana no debe ser nulo
     *
     * @param ventana: Objeto ventana nuevo
     */
    private void setVentana(JFrame ventana) {
        ValidacionesUtiles.esDistintoDeNull(ventana, "ventana");
        this.ventana =  ventana;
    }

    /**
     * Setter del atributo cartas
     *
     * PRE:
     * -Cartas no debe ser nulo
     *
     * @param cartas: Nueva lista de cartas
     */
    private void setCartas(List<CartaVista> cartas){
        this.cartas = cartas;
    }

    /**
     * Setter del atributo finalizado
     *
     * PRE:
     * -Estado no debe ser nulo
     *
     * @param estado: Estado nuevo del atributo
     */
    private void setFinalizado(boolean estado){
        ValidacionesUtiles.esDistintoDeNull(estado, "estado");
        this.finalizado =  estado;
    }

    /**
     * Setter del atributo mochilaVisible
     *
     * Pre:
     * -Estado no debe ser nulo
     *
     * @param estado: Nuevo estado
     */
    private void setMochilaVisible(boolean estado){
        ValidacionesUtiles.esDistintoDeNull(estado, "estado");
        this.mochilaVisible = estado;
    }

    /**
     * Setter del atributo cartaPresionada
     *
     * PRE:
     * -Valor debe ser mayor o igual a cero
     *
     * @param valor: Nuevo valor
     */
    private void setCartasPresionadas(int valor){
        ValidacionesUtiles.validarMayorOIgualACero(valor, "valor");
        this.cartaPresionada =  valor;
    }

}