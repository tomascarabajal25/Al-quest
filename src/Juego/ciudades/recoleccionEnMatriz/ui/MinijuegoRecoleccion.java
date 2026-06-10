package Juego.ciudades.recoleccionEnMatriz.ui;

import Juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelosVista.JugadorVista;
import modelos.Minijuego;
import modelosVista.Vista;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MinijuegoRecoleccion implements Minijuego {

    private final CiudadRecoleccion     juego;
    private final KeyHandlerRecoleccion key;
    private Runnable                    onFinalizadoCallback;
    private final List<CartaVista>      cartas = new ArrayList<>();

    private boolean finalizado     = false;
    private boolean mochilaVisible = false;

    public MinijuegoRecoleccion(CiudadRecoleccion juego,
                                 Vista vista,
                                 KeyHandlerRecoleccion key) {
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
        // píxeles → tile (base 1)
        int tamaño = jugador.getVistaDelJuego().tamaño;
        int col  = jugador.getWorldX() / tamaño + 1;  // columna = X
        int fila = jugador.getWorldY() / tamaño + 1;  // fila    = Y
        juego.actualizarPosicionJugador(col, fila);    // (col, fila) → el método los clampea

        // Notificar al modelo dónde está el jugador visualmente
        juego.actualizarPosicionJugador(col, fila);
        if (key.ePressed) {
            juego.recogerCarta();
            key.ePressed = false;
        }
        if (key.pPressed) {
            mochilaVisible = !mochilaVisible;
            key.pPressed   = false;
        }
        if (juego.estaFinalizado()) {
            finalizado = true;
            if (onFinalizadoCallback != null) onFinalizadoCallback.run();
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
    @Override
    public void draw(Graphics2D g2, JugadorVista jugador) {
        int[] pos = juego.getPosicionJugador();
        int nivelActual = (pos != null) ? pos[2] : 1;

        // Dibujamos las cartas con la sobrecarga que filtra por nivel.
        // Vista no las dibuja porque no están en adminObjt.
        for (CartaVista carta : cartas) {
            carta.draw(g2, jugador.getVistaDelJuego(), nivelActual);
        }

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
        // base 0 — si tu lista es base 1, cambiá a i=1; i<=cantidad
        for (int i = 0; i < cantidad; i++) {
            g2.drawString((i + 1) + ". " + items.get(i).getNombre(), x + 10, ly);
            ly += 20;
        }
    }

    public void setOnFinalizadoCallback(Runnable cb) { this.onFinalizadoCallback = cb; }
    public boolean isFinalizado() { return finalizado; }
    public int     getPuntaje()   { return juego.getPuntos(); }
}