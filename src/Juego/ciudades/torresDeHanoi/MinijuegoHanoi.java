package Juego.ciudades.torresDeHanoi;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import modelos.JugadorVista;
import modelos.Minijuego;

/**
 * Integra el puzzle de Torres de Hanoi en la Vista del juego.
 *
 * El modelo (CiudadHanoi) trabaja con Pila<Integer>; cada entero es el tamaño
 * del disco. EstadoHanoi expone int[] para que la vista pueda dibujar sin
 * conocer la estructura interna de la pila.
 *
 * Responsabilidad de la vista sobre el tamaño del disco:
 *   anchoDisco = (tamano / maxDiscos) * maxAnchoDisco + margenMinimo
 * donde tamano es el entero del arreglo y maxDiscos es el objetivo de la partida.
 */
public class MinijuegoHanoi implements Minijuego {

    // ── Zona de activación ────────────────────────────────────────────────────
    private static final int ZONA_COL   = 20;
    private static final int ZONA_FILA  = 15;
    private static final int ZONA_ANCHO = 5;
    private static final int ZONA_ALTO  = 3;

    private static final int DISCOS_INICIAL = 3;

    // ── Estado ───────────────────────────────────────────────────────────────
    private enum Estado { INACTIVO, ACTIVO, GANADO }
    private Estado estado = Estado.INACTIVO;

    // ── Dependencias ─────────────────────────────────────────────────────────
    private final PartidaHanoi partida;
    private Runnable onFinalizadoCallback;

    private final int zonaWorldX;
    private final int zonaWorldY;
    private final int zonaAncho;
    private final int zonaAlto;

    private String torreOrigen = null;

    private String mensajeFeedback = "";
    private long   tiempoFeedback  = 0;
    private static final long DURACION_FEEDBACK_MS = 1500;

    // ── Constructor ───────────────────────────────────────────────────────────

    public MinijuegoHanoi(modelos.Jugador jugador, int tamaño, PartidaHanoi partida) {
        this.partida    = partida;
        this.zonaWorldX = ZONA_COL   * tamaño;
        this.zonaWorldY = ZONA_FILA  * tamaño;
        this.zonaAncho  = ZONA_ANCHO * tamaño;
        this.zonaAlto   = ZONA_ALTO  * tamaño;
    }

    // ── Minijuego: actualizar ─────────────────────────────────────────────────

    @Override
    public void actualizar(JugadorVista jugador) {
        switch (estado) {
            case INACTIVO -> {
                if (jugadorEnZona(jugador)) estado = Estado.ACTIVO;
            }
            case ACTIVO -> {
                if (partida.getJuego().haGanado()) estado = Estado.GANADO;
                if (!mensajeFeedback.isEmpty() &&
                    System.currentTimeMillis() - tiempoFeedback > DURACION_FEEDBACK_MS) {
                    mensajeFeedback = "";
                }
            }
            case GANADO -> {
                if (onFinalizadoCallback != null) {
                    onFinalizadoCallback.run();
                    onFinalizadoCallback = null;
                }
            }
        }
    }

    // ── Minijuego: draw ───────────────────────────────────────────────────────

    @Override
    public void draw(Graphics2D g2, JugadorVista jugador) {
        if (estado == Estado.INACTIVO) {
            dibujarZonaIndicadora(g2, jugador);
            return;
        }
        dibujarOverlay(g2);
    }

    // ── Input ─────────────────────────────────────────────────────────────────

    public void procesarTecla(char tecla) {
        if (estado != Estado.ACTIVO) return;

        char t = Character.toUpperCase(tecla);

        if (t == 'R') {
            partida.getJuego().reiniciar(DISCOS_INICIAL);
            torreOrigen = null;
            setFeedback("Reiniciado");
            return;
        }
        if (tecla == 27) { // ESC
            estado = Estado.INACTIVO;
            torreOrigen = null;
            return;
        }

        String torreElegida = switch (t) {
            case '1' -> "A";
            case '2' -> "B";
            case '3' -> "C";
            default  -> null;
        };
        if (torreElegida == null) return;

        if (torreOrigen == null) {
            torreOrigen = torreElegida;
            setFeedback("Origen: Torre " + torreOrigen + "  → elegí destino (1/2/3)");
        } else {
            if (torreElegida.equals(torreOrigen)) {
                torreOrigen = null;
                setFeedback("Movimiento cancelado");
                return;
            }
            boolean ok = mover(torreOrigen, torreElegida);
            setFeedback(ok
                ? "Torre " + torreOrigen + " → Torre " + torreElegida
                : "Movimiento inválido");
            torreOrigen = null;
        }
    }

    // ── Dibujo ───────────────────────────────────────────────────────────────

    private void dibujarZonaIndicadora(Graphics2D g2, JugadorVista jugador) {
        int sx = zonaWorldX - jugador.getWorldX() + jugador.getScreenX();
        int sy = zonaWorldY - jugador.getWorldY() + jugador.getScreenY();
        g2.setColor(new Color(255, 200, 0, 50));
        g2.fillRect(sx, sy, zonaAncho, zonaAlto);
        g2.setColor(new Color(255, 200, 0, 160));
        g2.drawRect(sx, sy, zonaAncho, zonaAlto);
    }

    private void dibujarOverlay(Graphics2D g2) {
        int px = 60, py = 30, pw = 648, ph = 340;
        g2.setColor(new Color(10, 10, 20, 210));
        g2.fillRoundRect(px, py, pw, ph, 20, 20);
        g2.setColor(new Color(120, 100, 220));
        g2.drawRoundRect(px, py, pw, ph, 20, 20);

        // Cabecera
        g2.setFont(new Font("Arial", Font.BOLD, 15));
        g2.setColor(Color.WHITE);
        g2.drawString("Torres de Hanoi", px + 16, py + 24);

        EstadoHanoi est = getEstado();
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(180, 180, 180));
        g2.drawString(
            "Movimientos: " + est.getMovimientos() +
            "   Mínimo: "   + (int) est.getMinMovimientos(),
            px + 200, py + 24);

        // Torres
        int maxDiscos = partida.getJuego().getObjetivo(); // para escalar el ancho
        int[] centros  = { px + 120, px + 324, px + 528 };
        String[] nombres = { "A  [1]", "B  [2]", "C  [3]" };
        int[][] datos  = { est.getTorreA(), est.getTorreB(), est.getTorreC() };
        String[] letras = { "A", "B", "C" };

        for (int i = 0; i < 3; i++) {
            dibujarTorre(g2, centros[i], py + 55, nombres[i], datos[i],
                maxDiscos, torreOrigen != null && torreOrigen.equals(letras[i]));
        }

        // Instrucciones
        g2.setFont(new Font("Arial", Font.ITALIC, 12));
        g2.setColor(new Color(160, 160, 200));
        g2.drawString("1/2/3 = elegir torre   R = reiniciar   ESC = salir",
            px + 16, py + ph - 32);

        // Feedback
        if (!mensajeFeedback.isEmpty()) {
            g2.setFont(new Font("Arial", Font.BOLD, 13));
            g2.setColor(new Color(255, 230, 80));
            g2.drawString(mensajeFeedback, px + 16, py + ph - 14);
        }

        // Victoria
        if (estado == Estado.GANADO) {
            g2.setColor(new Color(0, 0, 0, 180));
            g2.fillRoundRect(px + 100, py + 100, pw - 200, 100, 16, 16);
            g2.setFont(new Font("Arial", Font.BOLD, 26));
            String msg = partida.getJuego().esPerfecto()
                ? "¡Perfecto! " + (int) est.getMinMovimientos() + " movimientos exactos"
                : "¡Ganaste! " + est.getMovimientos() + " movimientos";
            g2.setColor(new Color(80, 255, 140));
            g2.drawString(msg, px + 120, py + 160);
        }
    }

    /**
     * Dibuja una torre.
     *
     * @param discos     int[] donde cada valor > 0 es el tamaño del disco;
     *                   0 = slot vacío. Índice 0 = tope (más pequeño presente),
     *                   últimos índices = fondo (más grande).
     * @param maxDiscos  objetivo de la partida; sirve para escalar el ancho
     *                   proporcional del disco: ancho = (tamaño/maxDiscos)*MAX_ANCHO.
     *
     * Orden de dibujo: se itera de mayor a menor índice para que el disco
     * más grande (fondo de pila) quede en el slot más bajo, pegado a la base.
     */
    private void dibujarTorre(Graphics2D g2, int centroX, int baseY,
                               String nombre, int[] discos,
                               int maxDiscos, boolean seleccionada) {

        final int ALTO_TORRE      = 200;
        final int ANCHO_PALO      = 6;
        final int ALTO_BASE       = 10;
        final int ANCHO_BASE      = 140;
        final int ALTO_DISCO      = 16;
        final int MAX_ANCHO_DISCO = 120;
        final int MARGEN_DISCO    = 20; // ancho mínimo para discos pequeños

        // Palo
        g2.setColor(seleccionada ? new Color(255, 220, 80) : new Color(160, 140, 100));
        g2.fillRoundRect(centroX - ANCHO_PALO / 2, baseY,
                         ANCHO_PALO, ALTO_TORRE, 4, 4);

        // Base
        g2.fillRoundRect(centroX - ANCHO_BASE / 2, baseY + ALTO_TORRE,
                         ANCHO_BASE, ALTO_BASE, 4, 4);

        // Nombre
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(seleccionada ? new Color(255, 220, 80) : new Color(200, 200, 200));
        g2.drawString(nombre, centroX - 18, baseY + ALTO_TORRE + ALTO_BASE + 16);

        // ── Discos ────────────────────────────────────────────────────────────
        // discos[0] = tope (chico), discos[n] = fondo (grande).
        // Buscamos el último índice con valor > 0 (= fondo real de la pila).
        int ultimoIdx = -1;
        for (int i = 0; i < discos.length; i++) {
            if (discos[i] > 0) ultimoIdx = i;
        }

        // Iteramos de fondo a tope para dibujar el grande abajo y el chico arriba.
        int slot = 0;
        for (int i = ultimoIdx; i >= 0; i--) {
            int tamano = discos[i];
            if (tamano == 0) continue; // slot vacío (no debería ocurrir en este rango)

            // Ancho proporcional al tamaño: disco 1 es MARGEN_DISCO px, disco maxDiscos
            // es MAX_ANCHO_DISCO + MARGEN_DISCO px.
            int anchoDisco = (int) ((tamano / (double) maxDiscos) * MAX_ANCHO_DISCO)
                             + MARGEN_DISCO;

            int yDisco = baseY + ALTO_TORRE - (slot + 1) * ALTO_DISCO;

            // Color: disco grande → naranja/marrón; disco pequeño → azul
            float ratio = tamano / (float) maxDiscos;
            int r = (int) (80  + ratio * 160);
            int b = (int) (200 - ratio * 140);
            g2.setColor(seleccionada ? new Color(220, 180, 50) : new Color(r, 90, b));
            g2.fillRoundRect(centroX - anchoDisco / 2, yDisco,
                             anchoDisco, ALTO_DISCO - 2, 6, 6);

            // Número del disco
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 10));
            g2.drawString(String.valueOf(tamano),
                          centroX - 4, yDisco + ALTO_DISCO - 4);
            slot++;
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private boolean jugadorEnZona(JugadorVista jugador) {
        Rectangle zona  = new Rectangle(zonaWorldX, zonaWorldY, zonaAncho, zonaAlto);
        Rectangle jRect = new Rectangle(
            jugador.getWorldX() + jugador.getAreaSolida().x,
            jugador.getWorldY() + jugador.getAreaSolida().y,
            jugador.getAreaSolida().width,
            jugador.getAreaSolida().height
        );
        return zona.intersects(jRect);
    }

    private boolean mover(String origen, String destino) {
        CiudadHanoi juego = partida.getJuego();
        Pila<Integer> pOrigen = switch (origen) {
            case "A" -> juego.getTorreA();
            case "B" -> juego.getTorreB();
            default  -> juego.getTorreC();
        };
        Pila<Integer> pDestino = switch (destino) {
            case "A" -> juego.getTorreA();
            case "B" -> juego.getTorreB();
            default  -> juego.getTorreC();
        };
        return juego.mover(pOrigen, pDestino);
    }

    private EstadoHanoi getEstado() {
        CiudadHanoi j = partida.getJuego();
        return new EstadoHanoi(
            j.getDiscosDeTorre(j.getTorreA()),
            j.getDiscosDeTorre(j.getTorreB()),
            j.getDiscosDeTorre(j.getTorreC()),
            j.getMovimientos(),
            j.getMinMovimientos()
        );
    }

    private void setFeedback(String msg) {
        mensajeFeedback = msg;
        tiempoFeedback  = System.currentTimeMillis();
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public boolean isActivo()  { return estado == Estado.ACTIVO;  }
    public boolean isGanado()  { return estado == Estado.GANADO;  }
    public PartidaHanoi getPartida() { return partida; }
    public void setOnFinalizadoCallback(Runnable cb) { this.onFinalizadoCallback = cb; }
}