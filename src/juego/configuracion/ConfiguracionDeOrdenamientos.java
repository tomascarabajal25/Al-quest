package juego.configuracion;

/**
 * Constantes de configuración para la Ciudad de Ordenamientos.
 *
 * Centraliza todos los valores que antes estaban dispersos en
 * PartidaOrdenamientos, MinijuegoOrdenamiento, FabricaMinijuegoOrdenamiento
 * y CajaVista, facilitando su ajuste sin tocar la lógica de cada clase.
 */
public final class ConfiguracionDeOrdenamientos {

    // Constructor privado: clase de utilidad, no instanciable.
    private ConfiguracionDeOrdenamientos() {}

    // ── Mapa y posición inicial del jugador ───────────────────────────────────

    /** Ruta del mapa del mundo donde se desarrolla la ciudad. */
    public static final String RUTA_MAPA_MUNDO = "/maps/world02.txt";

    /** Columna inicial donde aparece el jugador al entrar a la ciudad. */
    public static final int COL_INICIO = 1;

    /** Fila inicial donde aparece el jugador al entrar a la ciudad. */
    public static final int FILA_BASE = 48;

    // ── Sprites ───────────────────────────────────────────────────────────────

    /** Ruta del sprite de las cajas. */
    public static final String SPRITE_CAJA = "/assets/objetos/caja.bmp";

    // ── Posición de las cajas en el mundo ─────────────────────────────────────

    /** Columna (tile) donde empieza la primera caja en el mundo. */
    public static final int CAJAS_COL_INICIO = 17;

    /** Fila (tile) donde se ubican las cajas en el mundo. */
    public static final int CAJAS_FILA_BASE = 23;

    /**
     * Separación en tiles entre cajas consecutivas.
     * Cada caja ocupa su tile + este espacio entre ellas.
     */
    public static final int CAJAS_SEPARACION_TILES = 3;

    // ── Cajas configurables por el usuario ────────────────────────────────────

    /** Cantidad mínima de cajas que el jugador puede configurar. */
    public static final int CANTIDAD_MINIMA_CAJAS = 2;

    /** Cantidad máxima de cajas que el jugador puede configurar. */
    public static final int CANTIDAD_MAXIMA_CAJAS = 8;

    /** Cantidad de cajas por defecto cuando el usuario cancela la configuración. */
    public static final int CANTIDAD_CAJAS_POR_DEFECTO = 4;

    // ── Algoritmos disponibles ────────────────────────────────────────────────

    /** Nombre para mostrar del algoritmo Bubble Sort. */
    public static final String NOMBRE_BUBBLE_SORT    = "Bubble Sort";

    /** Nombre para mostrar del algoritmo Selection Sort. */
    public static final String NOMBRE_SELECTION_SORT = "Selection Sort";

    /** Nombre interno del ordenador Bubble Sort. */
    public static final String ID_BUBBLE_SORT    = "ordenador bubble";

    /** Nombre interno del ordenador Selection Sort. */
    public static final String ID_SELECTION_SORT = "Ordenador Selection";

    /** Lista de algoritmos disponibles para el JOptionPane de selección. */
    public static final String[] ALGORITMOS_DISPONIBLES = {
        NOMBRE_BUBBLE_SORT,
        NOMBRE_SELECTION_SORT
    };

    // ── Lógica del minijuego ──────────────────────────────────────────────────

    /** Frames entre cada paso del resolver automático. */
    public static final int TICK_POR_PASO = 45;

    /** Radio en píxeles para detectar cercanía del jugador a una caja. */
    public static final int RADIO_INTERACCION = 60;

    /** Duración en ms de la pantalla de victoria antes de cerrar la ciudad. */
    public static final long DURACION_VICTORIA_MS = 3_000L;

    /** Puntaje otorgado al completar el desafío de ordenamiento. */
    public static final int PUNTAJE_VICTORIA = 1_000;

    // ── HUD ───────────────────────────────────────────────────────────────────

    /** Posición X del panel HUD en pantalla (píxeles). */
    public static final int HUD_X = 10;

    /** Posición Y del panel HUD en pantalla (píxeles). */
    public static final int HUD_Y = 10;

    /** Ancho del panel HUD en píxeles. */
    public static final int HUD_ANCHO = 500;

    /** Alto del panel HUD en píxeles. */
    public static final int HUD_ALTO = 130;

    /** Posición X del panel de victoria en pantalla (píxeles). */
    public static final int VICTORIA_PANEL_X = 140;

    /** Posición Y del panel de victoria en pantalla (píxeles). */
    public static final int VICTORIA_PANEL_Y = 60;

    /** Ancho del panel de victoria en píxeles. */
    public static final int VICTORIA_PANEL_ANCHO = 420;

    /** Alto del panel de victoria en píxeles. */
    public static final int VICTORIA_PANEL_ALTO = 110;

    // ── Animación de parpadeo (CajaVista) ─────────────────────────────────────

    /** Ticks totales de un ciclo de parpadeo (selección de caja). */
    public static final int PARPADEO_CICLO_TICKS = 45;

    /** Ticks dentro del ciclo en que la caja es visible (resto → invisible). */
    public static final int PARPADEO_VISIBLE_TICKS = 30;
}