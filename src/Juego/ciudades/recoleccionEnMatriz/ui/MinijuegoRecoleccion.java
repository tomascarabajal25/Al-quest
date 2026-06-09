package Juego.ciudades.recoleccionEnMatriz.ui;

import Juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelosVista.JugadorVista;
import modelos.Minijuego;
import modelosVista.Vista;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MinijuegoRecoleccion implements Minijuego {

    private final CiudadRecoleccion juego;
    private final KeyHandlerRecoleccion key;
    private Runnable onFinalizadoCallback;
    private final List<CartaVista> cartas = new ArrayList<>();

    private boolean finalizado     = false;
    private boolean mochilaVisible = false;
    public int cartaPresionada = 0;

    public MinijuegoRecoleccion(CiudadRecoleccion juego, Vista vista, KeyHandlerRecoleccion key) {
        this.juego = juego;
        this.key   = key;
        inyectarCartas(vista);
    }

    private void inyectarCartas(Vista vista) {
        // Las cartas NO van en adminObjt porque ElementoVista no extiende ObjetoVista.
        // El minijuego las guarda localmente y las dibuja en draw() con la sobrecarga
        // draw(g2, vista, nivelActual) que filtra por nivel.
        for (CartaVista carta : juego.getCartasVista(vista.tamaño)) {
            cartas.add(carta);
        }
    }

    @Override
    public void actualizar(JugadorVista jugador) {
        if (finalizado) return;

        int tamaño = jugador.getVistaDelJuego().tamaño;
        int col  = jugador.getWorldX() / tamaño + 1;
        int fila = jugador.getWorldY() / tamaño + 1;

        // Colisión con cartas — empujar al jugador en dirección contraria
        for (CartaVista carta : cartas) {
            if (!carta.isRecogido() && carta.colisionaConJugador(jugador, obtenerNivelActual())) {
                int t = jugador.getVistaDelJuego().tamaño;
                switch (jugador.getDireccion()) {
                    case Arriba    -> jugador.setWorldY(jugador.getWorldY() + t);
                    case Abajo     -> jugador.setWorldY(jugador.getWorldY() - t);
                    case Izquierda -> jugador.setWorldX(jugador.getWorldX() + t);
                    case Derecha   -> jugador.setWorldX(jugador.getWorldX() - t);
                }
                return;
            }
        }

        juego.actualizarPosicionJugador(col, fila);

        // Recoger carta
        if (key.ePressed) {
            int nivelAntes = obtenerNivelActual();
            juego.recogerCarta();
            key.ePressed = false;

            int nivelDespues = obtenerNivelActual();
            if (nivelDespues != nivelAntes) {
                int t = jugador.getVistaDelJuego().tamaño;
                jugador.setWorldX(2 * t);
                jugador.setWorldY(2 * t);
            }
        }

        // Abrir/cerrar mochila
        if (key.pPressed) {
            mochilaVisible = !mochilaVisible;
            key.pPressed   = false;
        }

        // Usar carta de la mochila
        if (mochilaVisible && key.cartaPresionada > 0) {
            try {
                juego.usarCartaMochila(key.cartaPresionada);
            } catch (RuntimeException ex) {
                // slot inválido, ignorar
            }
            key.cartaPresionada = 0;
        }

        // Verificar fin de juego
        if (juego.estaFinalizado()) {
            finalizado = true;
            if (onFinalizadoCallback != null) onFinalizadoCallback.run();
        }
    }

    private int obtenerNivelActual() {
        int[] pos = juego.getPosicionJugador();
        return (pos != null) ? pos[2] : 1;
    }

    private void dibujarOverlayVisibilidad(Graphics2D g2, JugadorVista jugador) {
        modelosVista.Vista vista = jugador.getVistaDelJuego();
        int tamaño      = vista.tamaño;
        int visibilidad = juego.getVisibilidad();

        // Posición del jugador en pantalla (centro)
        int jScreenX = jugador.getScreenX();
        int jScreenY = jugador.getScreenY();

        // Recorrer todas las celdas visibles en pantalla
        for (int col = 0; col < vista.columnas + 2; col++) {
            for (int fila = 0; fila < vista.filas + 2; fila++) {
                // worldX/Y de esta celda
                int worldX = (jugador.getWorldX() / tamaño - vista.columnas / 2 + col) * tamaño;
                int worldY = (jugador.getWorldY() / tamaño - vista.filas   / 2 + fila) * tamaño;

                // screenX/Y de esta celda
                int screenX = worldX - jugador.getWorldX() + jScreenX;
                int screenY = worldY - jugador.getWorldY() + jScreenY;

                // Distancia en tiles al jugador
                int distCol = Math.abs(worldX / tamaño - jugador.getWorldX() / tamaño);
                int distFila = Math.abs(worldY / tamaño - jugador.getWorldY() / tamaño);

                if (distCol > visibilidad || distFila > visibilidad) {
                    int dist = Math.max(distCol, distFila);
                    int alpha = Math.min(190, 130 + (dist - visibilidad) * 15);
                    g2.setColor(new Color(0, 0, 0, alpha));
                    g2.fillRect(screenX, screenY, tamaño, tamaño);
                }
            }
        }
    }

    /**
     * draw() del minijuego se llama DESPUÉS de que Vista dibujó los objetos.
     * Las cartas ya fueron dibujadas por Vista con draw(g2, vista) — sin nivel.
     * Acá redibujamos solo las cartas usando la sobrecarga con nivel,
     * lo que oculta las del nivel incorrecto pintando encima... 
     *
     * MEJOR: Vista no dibuja las CartaVista directamente; el minijuego
     * las dibuja acá con la versión correcta. Para eso NO las inyectamos
     * en adminObjt sino que las guardamos en la lista local y las dibujamos
     * manualmente con el nivel correcto.
     */
    public void draw(Graphics2D g2, JugadorVista jugador) {
        int[] pos = juego.getPosicionJugador();
        int nivelActual = (pos != null) ? pos[2] : 1;

        for (CartaVista carta : cartas) {
            carta.draw(g2, jugador.getVistaDelJuego(), nivelActual);
        }

        dibujarOverlayVisibilidad(g2, jugador);  // ← nuevo
        dibujarHUD(g2);
        if (mochilaVisible) dibujarMochila(g2);
    }

    private void dibujarHUD(Graphics2D g2) {
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

    private void dibujarMochila(Graphics2D g2) {
        int x = 10, y = 120;
        var items = juego.getItemsMochila();
        int alto = 30 + (items != null ? items.size() * 20 : 0) + 10;

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(x, y, 220, alto, 12, 12);
        g2.setColor(new Color(200, 200, 200));
        g2.drawRoundRect(x, y, 220, alto, 12, 12);

        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(Color.WHITE);
        g2.drawString("── Mochila ──", x + 10, y + 20);

        if (items == null || items.size() == 0) {
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

    public void setOnFinalizadoCallback(Runnable cb) { this.onFinalizadoCallback = cb; }
    public boolean isFinalizado() { return finalizado; }
    public int     getPuntaje()   { return juego.getPuntos(); }
}