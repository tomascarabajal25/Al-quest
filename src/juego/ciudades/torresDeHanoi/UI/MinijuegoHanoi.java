package juego.ciudades.torresDeHanoi.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import juego.ciudades.torresDeHanoi.CiudadHanoi;
import juego.ciudades.torresDeHanoi.ConfiguracionDeHanoi;
import juego.ciudades.torresDeHanoi.EstadoHanoi;
import juego.ciudades.torresDeHanoi.PartidaHanoi;
import juego.ciudades.torresDeHanoi.Pila;
import modelos.Jugador;
import modelos.Minijuego;
import modelosVista.JugadorVista;

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
 *
 * Flujo de estados:
 *   INACTIVO  → el jugador entra a la zona de activación → ACTIVO
 *   ACTIVO    → el jugador resuelve el puzzle (1/2/3, R, ESC)
 *   GANADO    → se muestra el mensaje de victoria durante
 *               ConfiguracionDeHanoi.DURACION_VICTORIA_MS y luego se
 *               ejecuta onFinalizadoCallback
 */
public class MinijuegoHanoi implements Minijuego {

    // CONSTANTES

    /** Código de la tecla ESCAPE recibido en procesarTecla(). */
    private static final char TECLA_ESCAPE = 27;

    // ATRIBUTOS

    private enum Estado { INACTIVO, ACTIVO, GANADO }

    private Estado estado = Estado.INACTIVO;

    /** Partida a la que pertenece este minijuego (motor lógico + puntaje). */
    private final PartidaHanoi partida;

    /** Callback ejecutado cuando termina la pantalla de victoria. */
    private Runnable onFinalizadoCallback;

    private final int zonaWorldX;
    private final int zonaWorldY;
    private final int zonaAncho;
    private final int zonaAlto;

    /** Torre elegida como origen del próximo movimiento ("A", "B", "C" o null). */
    private String torreOrigen = null;

    private String mensajeFeedback = "";
    private long tiempoFeedback = 0;

    /** Momento en que se detectó la victoria. -1 indica que todavía no ocurrió. */
    private long tiempoInicioVictoria = -1;

    // CONSTRUCTORES

    /**
     * Pre:
     * - jugador != null
     * - tamaño > 0
     * - partida != null
     *
     * Post:
     * - Calcula la posición y el tamaño de la zona de activación en
     *   coordenadas del mundo, a partir de ConfiguracionDeHanoi.
     *
     * @param jugador jugador actual (se mantiene por consistencia con la
     *                firma esperada por PartidaHanoi)
     * @param tamaño  tamaño de celda en píxeles (Vista.getTamanio())
     * @param partida partida de Hanoi asociada a este minijuego
     */
    public MinijuegoHanoi(Jugador jugador, int tamaño, PartidaHanoi partida) {
        this.partida = partida;
        this.zonaWorldX = ConfiguracionDeHanoi.ZONA_ACTIVACION_COLUMNA * tamaño;
        this.zonaWorldY = ConfiguracionDeHanoi.ZONA_ACTIVACION_FILA * tamaño;
        this.zonaAncho  = ConfiguracionDeHanoi.ZONA_ACTIVACION_ANCHO * tamaño;
        this.zonaAlto   = ConfiguracionDeHanoi.ZONA_ACTIVACION_ALTO * tamaño;
    }

    // METODOS DE COMPORTAMIENTO

    /**
     * Post: actualiza la lógica del minijuego según el estado actual
     *       (detección de zona, detección de victoria, vencimiento del
     *       feedback y temporización del cierre).
     *
     * @param jugador jugador cuya posición se usa para las detecciones de colisión
     */
    @Override
    public void actualizar(JugadorVista jugador) {
        switch (estado) {
            case INACTIVO -> actualizarInactivo(jugador);
            case ACTIVO -> actualizarActivo();
            case GANADO -> actualizarGanado();
        }
    }

    /**
     * Post: si el jugador entra a la zona de activación, pasa a estado ACTIVO.
     */
    private void actualizarInactivo(JugadorVista jugador) {
        if (jugadorEnZona(jugador)) {
            estado = Estado.ACTIVO;
        }
    }

    /**
     * Post: si el juego está ganado, pasa a estado GANADO. Además, vence el
     *       mensaje de feedback si ya transcurrió DURACION_FEEDBACK_MS.
     */
    private void actualizarActivo() {
        if (partida.getJuego().haGanado()) {
            estado = Estado.GANADO;
        }

        long tiempoDesdeFeedback = System.currentTimeMillis() - tiempoFeedback;
        boolean feedbackVencido = tiempoDesdeFeedback > ConfiguracionDeHanoi.DURACION_FEEDBACK_MS;

        if (!mensajeFeedback.isEmpty() && feedbackVencido) {
            mensajeFeedback = "";
        }
    }

    /**
     * Post: la primera vez que se entra a este estado, registra el momento
     *       de la victoria. Luego de DURACION_VICTORIA_MS, ejecuta el
     *       callback de finalización una sola vez, sin bloquear el hilo.
     */
    private void actualizarGanado() {
        if (tiempoInicioVictoria == -1) {
            tiempoInicioVictoria = System.currentTimeMillis();
        }

        long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioVictoria;

        if (tiempoTranscurrido >= ConfiguracionDeHanoi.DURACION_VICTORIA_MS
                && onFinalizadoCallback != null) {
            onFinalizadoCallback.run();
            onFinalizadoCallback = null; // evitar llamarlo más de una vez
        }
    }

    /**
     * Post: dibuja la zona de activación si el minijuego está inactivo, o el
     *       overlay completo del puzzle (torres, HUD y mensajes) en caso contrario.
     *
     * @param g2      contexto gráfico sobre el que dibujar
     * @param jugador jugador cuya posición se usa para posicionar la zona
     */
    @Override
    public void draw(Graphics2D g2, JugadorVista jugador) {
        if (estado == Estado.INACTIVO) {
            dibujarZonaIndicadora(g2, jugador);
            return;
        }
        dibujarOverlay(g2);
    }

    // ── Input ─────────────────────────────────────────────────────────────────

    /**
     * Pre:
     * - el minijuego debe estar en estado ACTIVO para procesar la tecla
     *   (si no lo está, la tecla se ignora).
     *
     * Post:
     * - 'R' reinicia el puzzle con ConfiguracionDeHanoi.DISCOS_REINICIO discos.
     * - ESC vuelve el minijuego a estado INACTIVO.
     * - '1'/'2'/'3' seleccionan torre origen/destino y mueven discos según
     *   corresponda.
     *
     * @param tecla carácter de la tecla presionada
     */
    public void procesarTecla(char tecla) {
        if (estado != Estado.ACTIVO) {
            return;
        }

        char teclaMayuscula = Character.toUpperCase(tecla);

        if (teclaMayuscula == 'R') {
            partida.getJuego().reiniciar(ConfiguracionDeHanoi.DISCOS_REINICIO);
            torreOrigen = null;
            setFeedback("Reiniciado");
            return;
        }

        if (tecla == TECLA_ESCAPE) {
            estado = Estado.INACTIVO;
            torreOrigen = null;
            return;
        }

        procesarSeleccionDeTorre(teclaMayuscula);
    }

    /**
     * Post: si la tecla corresponde a una torre ('1', '2' o '3'):
     *       - si no había torre origen, la registra como origen.
     *       - si la torre elegida es la misma que el origen, cancela la selección.
     *       - en caso contrario, intenta mover un disco de origen a la torre
     *         elegida y muestra el feedback correspondiente.
     *       Si la tecla no corresponde a ninguna torre, no hace nada.
     *
     * @param teclaMayuscula tecla ya normalizada a mayúscula
     */
    private void procesarSeleccionDeTorre(char teclaMayuscula) {
        String torreElegida = switch (teclaMayuscula) {
            case '1' -> "A";
            case '2' -> "B";
            case '3' -> "C";
            default -> null;
        };

        if (torreElegida == null) {
            return;
        }

        if (torreOrigen == null) {
            torreOrigen = torreElegida;
            setFeedback("Origen: Torre " + torreOrigen + "  → elegí destino (1/2/3)");
            return;
        }

        if (torreElegida.equals(torreOrigen)) {
            torreOrigen = null;
            setFeedback("Movimiento cancelado");
            return;
        }

        boolean movimientoValido = mover(torreOrigen, torreElegida);
        setFeedback(movimientoValido
                ? "Torre " + torreOrigen + " → Torre " + torreElegida
                : "Movimiento inválido");
        torreOrigen = null;
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

        EstadoHanoi estadoActual = getEstado();
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.setColor(new Color(180, 180, 180));
        g2.drawString(
            "Movimientos: " + estadoActual.getMovimientos() +
            "   Mínimo: "   + (int) estadoActual.getMinMovimientos(),
            px + 200, py + 24);

        // Torres
        int maxDiscos = partida.getJuego().getObjetivo(); // para escalar el ancho
        int[] centros  = { px + 120, px + 324, px + 528 };
        String[] nombres = { "A  [1]", "B  [2]", "C  [3]" };
        int[][] datos  = { estadoActual.getTorreA(), estadoActual.getTorreB(), estadoActual.getTorreC() };
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
            dibujarMensajeVictoria(g2, px, py, pw, ph, estadoActual);
        }
    }

    /**
     * Post: dibuja el panel de victoria sobre el overlay del puzzle, con el
     *       mensaje correspondiente según si la resolución fue perfecta.
     */
    private void dibujarMensajeVictoria(
            Graphics2D g2,
            int px,
            int py,
            int pw,
            int ph,
            EstadoHanoi estadoActual) {

        g2.setColor(new Color(0, 0, 0, 180));
        g2.fillRoundRect(px + 100, py + 100, pw - 200, 100, 16, 16);
        g2.setFont(new Font("Arial", Font.BOLD, 26));

        String mensaje = partida.getJuego().esPerfecto()
            ? "¡Perfecto! " + (int) estadoActual.getMinMovimientos() + " movimientos exactos"
            : "¡Ganaste! " + estadoActual.getMovimientos() + " movimientos";

        g2.setColor(new Color(80, 255, 140));
        g2.drawString(mensaje, px + 120, py + 160);
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
        int ultimoIndiceConDisco = -1;
        for (int i = 0; i < discos.length; i++) {
            if (discos[i] > 0) {
                ultimoIndiceConDisco = i;
            }
        }

        // Iteramos de fondo a tope para dibujar el grande abajo y el chico arriba.
        int slot = 0;
        for (int i = ultimoIndiceConDisco; i >= 0; i--) {
            int tamano = discos[i];
            if (tamano == 0) {
                continue; // slot vacío (no debería ocurrir en este rango)
            }

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

    /**
     * Post: no modifica el estado; solo consulta si el área sólida del
     *       jugador intersecta la zona de activación del desafío.
     *
     * @param jugador jugador cuya posición se evalúa
     * @return true si el jugador está dentro de la zona de activación
     */
    private boolean jugadorEnZona(JugadorVista jugador) {
        Rectangle zona  = new Rectangle(zonaWorldX, zonaWorldY, zonaAncho, zonaAlto);
        Rectangle areaJugador = new Rectangle(
            jugador.getWorldX() + jugador.getAreaSolida().x,
            jugador.getWorldY() + jugador.getAreaSolida().y,
            jugador.getAreaSolida().width,
            jugador.getAreaSolida().height
        );
        return zona.intersects(areaJugador);
    }

    /**
     * Post: delega en CiudadHanoi.mover() el movimiento entre las torres
     *       indicadas por sus nombres ("A", "B" o "C").
     *
     * @param origen  nombre de la torre origen
     * @param destino nombre de la torre destino
     * @return true si el movimiento se realizó, false si era inválido
     */
    private boolean mover(String origen, String destino) {
        CiudadHanoi juego = partida.getJuego();

        Pila<Integer> pilaOrigen = switch (origen) {
            case "A" -> juego.getTorreA();
            case "B" -> juego.getTorreB();
            default  -> juego.getTorreC();
        };
        Pila<Integer> pilaDestino = switch (destino) {
            case "A" -> juego.getTorreA();
            case "B" -> juego.getTorreB();
            default  -> juego.getTorreC();
        };

        return juego.mover(pilaOrigen, pilaDestino);
    }

    /**
     * Post: construye una instantánea inmutable (EstadoHanoi) del estado
     *       actual del motor lógico, lista para ser dibujada.
     *
     * @return instantánea del estado actual del puzzle
     */
    private EstadoHanoi getEstado() {
        CiudadHanoi juego = partida.getJuego();
        return new EstadoHanoi(
            juego.getDiscosDeTorre(juego.getTorreA()),
            juego.getDiscosDeTorre(juego.getTorreB()),
            juego.getDiscosDeTorre(juego.getTorreC()),
            juego.getMovimientos(),
            juego.getMinMovimientos()
        );
    }

    /**
     * Post: actualiza mensajeFeedback y reinicia su temporizador de vencimiento.
     *
     * @param mensaje texto a mostrar como feedback
     */
    private void setFeedback(String mensaje) {
        mensajeFeedback = mensaje;
        tiempoFeedback  = System.currentTimeMillis();
    }

    // GETTERS

    /** @return true si el minijuego está en estado ACTIVO */
    public boolean isActivo() {
        return estado == Estado.ACTIVO;
    }

    /** @return true si el minijuego está en estado GANADO */
    public boolean isGanado() {
        return estado == Estado.GANADO;
    }

    /** @return la partida de Hanoi asociada a este minijuego */
    public PartidaHanoi getPartida() {
        return partida;
    }

    // SETTERS

    /**
     * Pre:
     * - callback != null
     *
     * Post:
     * - registra la acción a ejecutar cuando finalice la pantalla de victoria.
     *
     * @param callback acción a ejecutar al finalizar
     */
    public void setOnFinalizadoCallback(Runnable callback) {
        this.onFinalizadoCallback = callback;
    }
}