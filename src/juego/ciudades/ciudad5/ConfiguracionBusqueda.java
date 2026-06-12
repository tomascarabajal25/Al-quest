package juego.ciudades.ciudad5;

/**
 * Constantes de configuración para la Ciudad de Búsqueda (Ciudad 5).
 *
 * Centraliza todos los valores "mágicos" que antes estaban dispersos en
 * MinijuegoDesafio, PartidaBusqueda y ciudadBusqueda, facilitando su ajuste
 * sin tener que tocar la lógica de cada clase.
 */
public final class ConfiguracionBusqueda {

    // Constructor privado: clase de utilidad, no instanciable.
    private ConfiguracionBusqueda() {}

    // ── Archivos de diccionario ───────────────────────────────────────────────

    /** Ruta relativa al diccionario de dificultad fácil. */
    public static final String ARCHIVO_FACIL   = "mapasBusqueda/diccionario_facil.txt";

    /** Ruta relativa al diccionario de dificultad media. */
    public static final String ARCHIVO_MEDIO   = "mapasBusqueda/diccionario_medio.txt";

    /** Ruta relativa al diccionario de dificultad difícil. */
    public static final String ARCHIVO_DIFICIL = "mapasBusqueda/diccionario_dificil.txt";

    /** Lista ordenada de archivos disponibles (para el JOptionPane de selección). */
    public static final String[] ARCHIVOS_DISPONIBLES = {
        ARCHIVO_FACIL,
        ARCHIVO_MEDIO,
        ARCHIVO_DIFICIL
    };

    // ── Mapa de la ciudad ────────────────────────────────────────────────────

    /** Ruta del mapa del mundo donde se desarrolla la ciudad. */
    public static final String RUTA_MAPA_MUNDO = "/maps/world01.txt";

    /** Ruta base de los sprites del jugador. */
    public static final String RUTA_SPRITES_JUGADOR = "/assets/jugador/boy";

    /** Tamaño de pantalla horizontal en tiles. */
    public static final int PANTALLA_ANCHO_TILES = 24;

    /** Tamaño de pantalla vertical en tiles. */
    public static final int PANTALLA_ALTO_TILES = 3;

    // ── Zona de activación del desafío ────────────────────────────────────────

    /** Columna (tile) de inicio de la zona del desafío. */
    public static final int ZONA_COL   = 23;

    /** Fila (tile) de inicio de la zona del desafío. */
    public static final int ZONA_FILA  = 28;

    /** Ancho en tiles de la zona del desafío. */
    public static final int ZONA_ANCHO = 4;

    /** Alto en tiles de la zona del desafío. */
    public static final int ZONA_ALTO  = 3;

    // ── Puertas ───────────────────────────────────────────────────────────────

    /** Columna (tile) de la puerta LISTA. */
    public static final int PUERTA_LISTA_COL  = 18;

    /** Fila (tile) de la puerta LISTA. */
    public static final int PUERTA_LISTA_FILA = 37;

    /** Columna (tile) de la puerta ÁRBOL. */
    public static final int PUERTA_ARBOL_COL  = 29;

    /** Fila (tile) de la puerta ÁRBOL. */
    public static final int PUERTA_ARBOL_FILA = 37;

    /** Ancho visual de cada puerta expresado en tiles. */
    public static final int PUERTA_ANCHO_TILES = 3;

    /** Ruta del sprite de la puerta LISTA. */
    public static final String SPRITE_PUERTA_LISTA  = "/assets/objetos/door.bmp";

    /** Ruta del sprite de la puerta ÁRBOL. */
    public static final String SPRITE_PUERTA_ARBOL  = "/assets/objetos/door_iron.bmp";

    // ── Teleportación entre rondas ────────────────────────────────────────────

    /**
     * Columna (tile) a la que se teletransporta al jugador entre rondas
     * (entrada de la sala de desafío).
     */
    public static final int TELEPORT_COL  = 29;

    /**
     * Fila (tile) a la que se teletransporta al jugador entre rondas.
     */
    public static final int TELEPORT_FILA = 32;

    // ── Lógica del minijuego ──────────────────────────────────────────────────

    /** Cantidad de rondas correctas necesarias para ganar. */
    public static final int RONDAS_PARA_GANAR = 5;

    /** Duración en ms del estado FEEDBACK (muestra si acertó o no). */
    public static final long DURACION_FEEDBACK_MS = 2_000L;

    /** Duración en ms de la pantalla de victoria antes de finalizar la partida. */
    public static final long DURACION_VICTORIA_MS = 3_000L;

    /** Puntaje otorgado al completar el desafío. */
    public static final int PUNTOS_VICTORIA = 1_000;

    // ── Overlay de texto ──────────────────────────────────────────────────────

    /** Ancho en píxeles del panel de overlay. */
    public static final int OVERLAY_ANCHO = 500;

    /** Alto en píxeles del panel de overlay. */
    public static final int OVERLAY_ALTO  = 160;

    /** Posición X del panel de overlay en pantalla. */
    public static final int OVERLAY_X     = 134;

    /** Posición Y del panel de overlay en pantalla. */
    public static final int OVERLAY_Y     = 20;
}