package juego.configuracion;

/**
 * Constantes relacionadas con las construcciones (tiles) y sus rutas de recurso.
 */
public final class ConstantesConstrucciones {

    private ConstantesConstrucciones() {
        // utility class
    }

    // Identificadores de tiles
    public static final int TILE_GRASS = 0;
    public static final int TILE_WALL = 1;
    public static final int TILE_WATER = 2;
    public static final int TILE_EARTH = 3;
    public static final int TILE_TREE = 4;
    public static final int TILE_SAND = 5;

    // Tamaño del array de construcciones (reservar espacio)
    public static final int NUM_TILES = 10;

    // Rutas a los recursos de imagen para cada tile
    public static final String PATH_GRASS = "/assets/construcciones/grass.bmp";
    public static final String PATH_WALL = "/assets/construcciones/wall.bmp";
    public static final String PATH_WATER = "/assets/construcciones/water.bmp";
    public static final String PATH_EARTH = "/assets/construcciones/earth.bmp";
    public static final String PATH_TREE = "/assets/construcciones/tree.bmp";
    public static final String PATH_SAND = "/assets/construcciones/sand.bmp";

}
