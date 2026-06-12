package juego.ciudades.torresDeHanoi;

/**
 * TDA ConfiguracionDeHanoi — constantes de configuración de la ciudad de
 * Torres de Hanoi.
 *
 * Responsabilidad: centralizar los valores fijos utilizados por
 * CiudadHanoi, PartidaHanoi y MinijuegoHanoi (reglas del puzzle, posiciones
 * en el mundo, tiempos de interfaz, puntajes y recursos gráficos), para
 * evitar números y textos mágicos repetidos y facilitar el ajuste del
 * balance de la ciudad desde un solo lugar.
 *
 * Esta clase no debe instanciarse: todos sus miembros son constantes
 * estáticas.
 */
public final class ConfiguracionDeHanoi {

    // CONSTANTES

    // ── Reglas del puzzle ───────────────────────────────────────────────────

    /** Cantidad mínima de discos permitida para una partida. */
    public static final int DISCOS_MINIMOS = 3;

    /** Cantidad máxima de discos permitida para una partida. */
    public static final int DISCOS_MAXIMOS = 10;

    /** Cantidad de discos con la que se reinicia el puzzle al presionar 'R'. */
    public static final int DISCOS_REINICIO = 3;

    // ── Zona de activación en el mapa ───────────────────────────────────────

    /** Columna del mapa donde comienza la zona de activación del desafío. */
    public static final int ZONA_ACTIVACION_COLUMNA = 34;

    /** Fila del mapa donde comienza la zona de activación del desafío. */
    public static final int ZONA_ACTIVACION_FILA = 4;

    /** Ancho (en celdas) de la zona de activación del desafío. */
    public static final int ZONA_ACTIVACION_ANCHO = 5;

    /** Alto (en celdas) de la zona de activación del desafío. */
    public static final int ZONA_ACTIVACION_ALTO = 3;

    // ── Posición inicial del jugador ────────────────────────────────────────

    /** Columna inicial (en celdas) donde aparece el jugador al entrar a la ciudad. */
    public static final int SPAWN_JUGADOR_COLUMNA = 8;

    /** Fila inicial (en celdas) donde aparece el jugador al entrar a la ciudad. */
    public static final int SPAWN_JUGADOR_FILA = 48;

    // ── Tiempos de la interfaz ───────────────────────────────────────────────

    /** Milisegundos que se muestra un mensaje de feedback de movimiento. */
    public static final long DURACION_FEEDBACK_MS = 1500;

    /** Milisegundos que se muestra la pantalla de victoria antes de finalizar. */
    public static final long DURACION_VICTORIA_MS = 3000;

    // ── Puntaje ──────────────────────────────────────────────────────────────

    /** Puntaje base otorgado al ganar con el mínimo de movimientos posible. */
    public static final int PUNTAJE_BASE_PERFECTO = 1000;

    /** Puntaje base otorgado al ganar sin lograr el mínimo de movimientos. */
    public static final int PUNTAJE_BASE_IMPERFECTO = 500;

    // ── Recursos y textos de la interfaz ────────────────────────────────────

    /** Ruta relativa (recurso) del mapa de la ciudad de Torres de Hanoi. */
    public static final String RUTA_MAPA = "/maps/world03.txt";

    /** Ruta relativa (recurso) del sprite del jugador. */
    public static final String RUTA_SPRITE_JUGADOR = "/assets/jugador/boy";

    /** Título de la ventana principal de la ciudad. */
    public static final String TITULO_VENTANA = "Torres de Hanoi";

    /** Título del diálogo de configuración inicial. */
    public static final String TITULO_DIALOGO_CONFIGURACION = "Configuración de Torres de Hanoi";

    /** Mensaje mostrado al pedirle al jugador la dificultad. */
    public static final String MENSAJE_SELECCION_DIFICULTAD =
            "Selecciona la cantidad de discos (Dificultad):";

    // CONSTRUCTORES

    /**
     * Constructor privado: esta clase no debe instanciarse, ya que todos
     * sus miembros son constantes estáticas.
     */
    private ConfiguracionDeHanoi() {
    }
}