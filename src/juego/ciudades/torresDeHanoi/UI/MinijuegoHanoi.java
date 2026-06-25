package juego.ciudades.torresDeHanoi.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import juego.ciudades.torresDeHanoi.CiudadHanoi;
import juego.ciudades.torresDeHanoi.EstadoHanoi;
import juego.ciudades.torresDeHanoi.PartidaHanoi;
import juego.ciudades.torresDeHanoi.Pila;
import juego.configuracion.ConfiguracionDeHanoi;
import modelos.Jugador;
import modelos.Minijuego;
import modelosVista.JugadorVista;

/**
 * Controlador visual del puzzle de Torres de Hanoi en la Vista del juego.
 *
 * Integra el motor lógico (CiudadHanoi) con la presentación gráfica (draw) y
 * procesa la entrada del jugador (procesarTecla). Gestiona el ciclo de vida
 * del minijuego mediante máquina de estados (INACTIVO → ACTIVO → GANADO).
 *
 * El modelo (CiudadHanoi) trabaja con Pila<Integer>; cada entero es el tamaño
 * del disco. EstadoHanoi expone los datos en un formato que la vista puede
 * dibujar sin acceder directamente a la estructura interna de la pila.
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
 *
 * INVARIANTES:
 * - partida != null
 * - zonaWorldX, zonaWorldY, zonaAncho, zonaAlto >= 0
 * - estado pertenece al enum Estado {INACTIVO, ACTIVO, GANADO}
 * - tiempoInicioVictoria == -1 hasta detectar la victoria
 */
public class MinijuegoHanoi implements Minijuego {

    // CONSTANTES

    /** Código de la tecla ESCAPE recibido en procesarTecla(). */
    private static final char TECLA_ESCAPE = 27;

    // ATRIBUTOS DE CLASE

    // No hay atributos de clase

    // ATRIBUTOS

    /** Enumeración de los estados posibles del minijuego. */
    private enum Estado {
        /** Minijuego inactivo; el jugador aún no ha entrado a la zona. */
        INACTIVO,
        /** Minijuego activo; el jugador está resolviendo el puzzle. */
        ACTIVO,
        /** Jugador ha ganado; se muestra pantalla de victoria. */
        GANADO
    }

    /** Estado actual del minijuego. */
    private Estado estado = Estado.INACTIVO;

    /** Partida a la que pertenece este minijuego (motor lógico + puntaje). */
    private final PartidaHanoi partida;

    /** Callback ejecutado cuando termina la pantalla de victoria. */
    private Runnable onFinalizadoCallback;

    /** Posición X de la zona de activación en coordenadas del mundo. */
    private final int zonaWorldX;

    /** Posición Y de la zona de activación en coordenadas del mundo. */
    private final int zonaWorldY;

    /** Ancho de la zona de activación en píxeles. */
    private final int zonaAncho;

    /** Alto de la zona de activación en píxeles. */
    private final int zonaAlto;

    /** Torre elegida como origen del próximo movimiento ("A", "B", "C" o null). */
    private String torreOrigen = null;

    /** Mensaje de feedback temporal a mostrar en pantalla. */
    private String mensajeFeedback = "";

    /** Momento (en ms) cuando se registró el último feedback. */
    private long tiempoFeedback = 0;

    /** Momento en que se detectó la victoria. -1 indica que todavía no ocurrió. */
    private long tiempoInicioVictoria = -1;

    // CONSTRUCTORES

    /**
     * Construye un nuevo controlador visual para el puzzle de Torres de Hanoi.
     *
     * Pre:
     * - jugador != null
     * - tamaño > 0
     * - partida != null con motor lógico inicializado
     *
     * Post:
     * - Calcula la posición y el tamaño de la zona de activación en
     *   coordenadas del mundo a partir de ConfiguracionDeHanoi.
     * - el minijuego comienza en estado INACTIVO.
     * - No hay callback registrado (onFinalizadoCallback == null).
     *
     * @param jugador jugador actual (se mantiene por consistencia con la
     *               firma esperada por PartidaHanoi)
     * @param tamaño tamaño de celda en píxeles (Vista.getTamanio())
     * @param partida partida de Hanoi asociada a este minijuego
     */
    public MinijuegoHanoi(Jugador jugador, int tamaño, PartidaHanoi partida) {
        this.partida = partida;
        this.zonaWorldX = ConfiguracionDeHanoi.ZONA_ACTIVACION_COLUMNA * tamaño;
        this.zonaWorldY = ConfiguracionDeHanoi.ZONA_ACTIVACION_FILA * tamaño;
        this.zonaAncho  = ConfiguracionDeHanoi.ZONA_ACTIVACION_ANCHO * tamaño;
        this.zonaAlto   = ConfiguracionDeHanoi.ZONA_ACTIVACION_ALTO * tamaño;
    }

    // METODOS DE CLASE

    // No hay métodos de clase

    // METODOS GENERALES

    // Los métodos generales (toString, etc.) están al final de la clase

    // METODOS DE COMPORTAMIENTO

    /**
     * Actualiza la lógica del minijuego según el estado actual.
     *
     * Responsabilidades según estado:
     * - INACTIVO: detecta si el jugador entró a la zona de activación.
     * - ACTIVO: detecta si ganó, hace expirar el feedback.
     * - GANADO: temporiza el vencimiento de la pantalla de victoria.
     *
     * Post:
     * - Puede cambiar de estado dependiendo de eventos detectados.
     * - Puede expirar el mensaje de feedback.
     * - Puede ejecutar el callback de finalización (una sola vez).
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
     * Actualiza lógica en estado INACTIVO.
     *
     * Post:
     * - Si el jugador entra a la zona de activación, cambia a estado ACTIVO.
     *
     * @param jugador jugador cuya posición se evalúa
     */
    private void actualizarInactivo(JugadorVista jugador) {
        if (jugadorEnZona(jugador)) {
            estado = Estado.ACTIVO;
        }
    }

    /**
     * Actualiza lógica en estado ACTIVO.
     *
     * Post:
     * - Si el motor lógico reporta victoria, cambia a estado GANADO.
     * - Si el feedback ha vencido (transcurrió más que ConfiguracionDeHanoi.DURACION_FEEDBACK_MS),
     *   lo borra.
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
     * Actualiza lógica en estado GANADO.
     *
     * Post:
     * - La primera vez que se entra a este estado, registra el momento de la victoria.
     * - Transcurrido ConfiguracionDeHanoi.DURACION_VICTORIA_MS desde la victoria,
     *   ejecuta el callback de finalización una sola vez (si existe).
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
     * Dibuja el estado visual del minijuego.
     *
     * Post:
     * - Si estado == INACTIVO: dibuja la zona de activación como un rectángulo indicador.
     * - Si estado == ACTIVO o GANADO: dibuja el overlay completo del puzzle
     *   (torres, contadores, instrucciones, mensajes).
     *
     * @param g2 contexto gráfico sobre el que dibujar
     * @param jugador jugador cuya posición se usa para transformar coordenadas de mundo a pantalla
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
     * Procesa una entrada de teclado del jugador.
     *
     * Pre:
     * - El minijuego debe estar en estado ACTIVO para procesar la mayoría de las teclas
     *   (si no lo está, se ignora la mayoría de eventos, excepto cambios de estado).
     *
     * Post:
     * - 'R' o 'r': reinicia el puzzle con ConfiguracionDeHanoi.DISCOS_REINICIO discos.
     * - ESC: vuelve el minijuego a estado INACTIVO.
     * - '1'/'2'/'3': selecciona torre origen o intenta mover disco a torre destino.
     * - Otras teclas: se ignoran.
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
     * Procesa la selección de una torre (1, 2 o 3).
     *
     * Pre:
     * - teclaMayuscula debe ser '1', '2', '3' o algún otro carácter válido.
     *
     * Post:
     * - Si teclaMayuscula no corresponde a una torre válida, no hace nada.
     * - Si torreOrigen == null: registra la torre elegida como origen.
     * - Si torreElegida == torreOrigen: cancela la selección (setea torreOrigen = null).
     * - Si torreElegida != torreOrigen: intenta mover disco de origen a destino,
     *   muestra feedback correspondiente y reinicia (torreOrigen = null).
     *
     * @param teclaMayuscula tecla ya normalizada a mayúscula ('1', '2' o '3')
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

    /**
     * Dibuja la zona de activación como un rectángulo indicador.
     *
     * Post:
     * - Dibuja un rectángulo semitransparente que marca dónde el jugador puede entrar.
     *
     * @param g2 contexto gráfico
     * @param jugador jugador para transformar coordenadas
     */
    private void dibujarZonaIndicadora(Graphics2D g2, JugadorVista jugador) {
        int sx = zonaWorldX - jugador.getWorldX() + jugador.getScreenX();
        int sy = zonaWorldY - jugador.getWorldY() + jugador.getScreenY();
        g2.setColor(new Color(255, 200, 0, 50));
        g2.fillRect(sx, sy, zonaAncho, zonaAlto);
        g2.setColor(new Color(255, 200, 0, 160));
        g2.drawRect(sx, sy, zonaAncho, zonaAlto);
    }

    /**
     * Dibuja el overlay completo del puzzle.
     *
     * Post:
     * - Dibuja panel de fondo del puzzle.
     * - Dibuja cabecera con título y contadores de movimientos.
     * - Dibuja las tres torres (A, B, C) con sus discos.
     * - Dibuja instrucciones de teclado.
     * - Si hay feedback activo, lo dibuja.
     * - Si estado == GANADO, dibuja el mensaje de victoria.
     *
     * @param g2 contexto gráfico
     */
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
        int maxDiscos = partida.getJuego().getObjetivo();
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
     * Dibuja el panel de victoria sobre el overlay del puzzle.
     *
     * Post:
     * - Dibuja un rectángulo oscuro centrado con el mensaje de victoria.
     * - Si la resolución fue perfecta, muestra: "¡Perfecto! N movimientos exactos"
     * - Si la resolución no fue perfecta, muestra: "¡Ganaste! N movimientos"
     *
     * @param g2 contexto gráfico
     * @param px posición X de la esquina izquierda del overlay
     * @param py posición Y de la esquina superior del overlay
     * @param pw ancho del overlay
     * @param ph alto del overlay
     * @param estadoActual estado actual del puzzle
     */
    private void dibujarMensajeVictoria(Graphics2D g2, int px, int py, int pw, int ph, EstadoHanoi estadoActual) {

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
     * Dibuja una torre con sus discos.
     *
     * Pre:
     * - discos != null; cada valor > 0 es el tamaño del disco, 0 = slot vacío.
     * - maxDiscos > 0; sirve para escalar el ancho proporcional del disco.
     *
     * Post:
     * - Dibuja el palo vertical, la base, el nombre de la torre y todos sus discos.
     * - Los discos se dibujan de mayor a menor (fondo al tope) para composición correcta.
     * - Si 'seleccionada' == true, cambia colores para indicar que es la torre origen.
     *
     * @param g2 contexto gráfico
     * @param centroX coordenada X del centro de la torre
     * @param baseY coordenada Y de la base de la torre
     * @param nombre nombre/etiqueta de la torre (p.ej. "A  [1]")
     * @param discos arreglo donde discos[0] = tope, discos[n] = fondo
     * @param maxDiscos cantidad máxima de discos (para escalar)
     * @param seleccionada verdadero si esta torre es la torre origen elegida
     */
    private void dibujarTorre(Graphics2D g2, int centroX, int baseY,
                               String nombre, int[] discos,
                               int maxDiscos, boolean seleccionada) {

        final int ALTO_TORRE = 200;
        final int ANCHO_PALO = 6;
        final int ALTO_BASE = 10;
        final int ANCHO_BASE = 140;
        final int ALTO_DISCO = 16;
        final int MAX_ANCHO_DISCO = 120;
        final int MARGEN_DISCO = 20;

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
                continue;
            }

            // Ancho proporcional al tamaño
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
     * Detecta si el jugador está dentro de la zona de activación del puzzle.
     *
     * Post:
     * - No modifica el estado; solo consulta si el área sólida del jugador
     *   intersecta la zona de activación del desafío.
     *
     * @param jugador jugador cuya posición se evalúa
     * @return verdadero si el jugador está dentro de la zona de activación
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
     * Realiza un movimiento entre dos torres.
     *
     * Pre:
     * - origen y destino tienen valores "A", "B" o "C".
     *
     * Post:
     * - Delega en CiudadHanoi.mover() el movimiento entre las torres indicadas.
     * - Devuelve el resultado del movimiento (válido o inválido).
     *
     * @param origen nombre de la torre origen ("A", "B" o "C")
     * @param destino nombre de la torre destino ("A", "B" o "C")
     * @return verdadero si el movimiento se realizó, falso si era inválido
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
     * Construye una instantánea inmutable del estado actual del motor lógico.
     *
     * Post:
     * - Crea un EstadoHanoi que captura los datos del puzzle en este momento.
     * - La instantánea está lista para ser dibujada sin acceder más al motor.
     *
     * @return instantánea del estado actual del puzzle
     */
    private EstadoHanoi getEstado() {
        CiudadHanoi juego = partida.getJuego();
        return new EstadoHanoi(
            convertirVectorAArray(juego.getDiscosDelTorre(juego.getTorreA())),
            convertirVectorAArray(juego.getDiscosDelTorre(juego.getTorreB())),
            convertirVectorAArray(juego.getDiscosDelTorre(juego.getTorreC())),
            juego.getMovimientos(),
            juego.getMinimosMovimientos()
        );
    }

    /**
     * Convierte un Vector<Integer> a un arreglo int[].
     *
     * Pre:
     * - vector != null
     *
     * Post:
     * - Devuelve un arreglo int[] con todos los elementos del vector.
     * - Los elementos vacíos (slots) se rellenan con 0.
     *
     * @param vector vector de discos
     * @return arreglo int[] equivalente
     */
    private int[] convertirVectorAArray(java.util.Vector<Integer> vector) {
        int tamanoMaximo = partida.getJuego().getObjetivo();
        int[] resultado = new int[tamanoMaximo];
        for (int i = 0; i < vector.size(); i++) {
            resultado[i] = vector.get(i);
        }
        return resultado;
    }

    /**
     * Actualiza el mensaje de feedback y reinicia su temporizador de vencimiento.
     *
     * Post:
     * - Establece mensajeFeedback al valor proporcionado.
     * - Reinicia el contador tiempoFeedback al tiempo actual.
     * - El feedback expirará luego de ConfiguracionDeHanoi.DURACION_FEEDBACK_MS ms.
     *
     * @param mensaje texto a mostrar como feedback
     */
    private void setFeedback(String mensaje) {
        this.mensajeFeedback = mensaje;
        this.tiempoFeedback  = System.currentTimeMillis();
    }

    // GETTERS

    /**
     * Post: no modifica el estado; solo consulta.
     *
     * @return verdadero si el minijuego está en estado ACTIVO
     */
    public boolean isActivo() {
        return estado == Estado.ACTIVO;
    }

    /**
     * Post: no modifica el estado; solo consulta.
     *
     * @return verdadero si el minijuego está en estado GANADO
     */
    public boolean isGanado() {
        return estado == Estado.GANADO;
    }

    /**
     * Post: no modifica el estado; solo consulta.
     *
     * @return la partida de Hanoi asociada a este minijuego
     */
    public PartidaHanoi getPartida() {
        return partida;
    }

    // SETTERS

    /**
     * Registra el callback a ejecutar cuando finalice la pantalla de victoria.
     *
     * Pre:
     * - callback != null
     *
     * Post:
     * - Memoriza la acción a ejecutar cuando se cumple la duración de victoria.
     * - El callback se invoca una única vez, luego se borra (para evitar múltiples ejecuciones).
     *
     * @param callback acción (Runnable) a ejecutar al finalizar
     */
    public void setOnFinalizadoCallback(Runnable callback) {
        this.onFinalizadoCallback = callback;
    }

    // METODOS GENERALES

    @Override
    public String toString() {
        return "MinijuegoHanoi{" +
                "estado=" + estado +
                ", partida=" + partida.getNombre() +
                ", torreOrigen=" + torreOrigen +
                ", activo=" + isActivo() +
                ", ganado=" + isGanado() +
                "}";
    }
}