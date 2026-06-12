package persistencia;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import utils.ValidacionesUtiles;

/**
 * TDA GestorArchivosJSON — gestor estático de persistencia de partidas.
 *
 * Responsabilidad: serializar y deserializar objetos DatosGuardado en
 * formato JSON utilizando Gson, dentro de la carpeta relativa de guardados.
 *
 * REGLA DE ORO — Rutas relativas:
 *   Todas las rutas usadas por esta clase son relativas al directorio de
 *   ejecución del programa (carpeta "saves/"), nunca rutas absolutas.
 *
 * Esta clase no mantiene estado propio: todos sus métodos son estáticos
 * (clase de utilidades, no instanciable).
 */
public class GestorArchivosJSON {

    // ── Constantes ────────────────────────────────────────────────────────────

    /** Carpeta relativa donde se almacenan los archivos de guardado. */
    private static final String CARPETA_GUARDADOS = "saves/";

    /** Extensión usada para los archivos de guardado. */
    private static final String EXTENSION_GUARDADO = ".json";

    /** Instancia de Gson configurada con formato legible (pretty printing). */
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Constructor privado: esta clase no debe instanciarse, ya que todos
     * sus métodos son estáticos.
     */
    private GestorArchivosJSON() {
    }

    // ── Métodos de clase ──────────────────────────────────────────────────────

    /**
     * Guarda el estado de la partida en un archivo JSON.
     *
     * pre:  datos != null, datos.getNombreJugador() != null
     * post: se crea (o sobrescribe) el archivo "saves/{nombreJugador}.json"
     *       con la representación JSON de datos. Si la carpeta "saves/" no
     *       existe, se crea automáticamente.
     *
     * @param datos estado de la partida a persistir
     * @return true si el guardado fue exitoso, false si ocurrió un error de E/S
     */
    public static boolean guardarPartida(DatosGuardado datos) {
        ValidacionesUtiles.esDistintoDeNull(datos, "datos");
        ValidacionesUtiles.esDistintoDeNull(datos.getNombreJugador(), "nombreJugador");

        crearCarpetaGuardadosSiNoExiste();

        String rutaArchivo = obtenerRutaArchivo(datos.getNombreJugador());

        try (FileWriter escritor = new FileWriter(rutaArchivo)) {
            GSON.toJson(datos, escritor);
            System.out.println("Partida guardada correctamente en " + rutaArchivo);
            return true;

        } catch (IOException excepcion) {
            System.out.println("Error al guardar la partida: " + excepcion.getMessage());
            return false;
        }
    }

    /**
     * Carga el estado de una partida previamente guardada.
     *
     * pre:  nombreJugador != null
     * post: si "saves/{nombreJugador}.json" existe y tiene contenido válido,
     *       devuelve el DatosGuardado reconstruido a partir de su contenido.
     *       Si el archivo no existe, no se puede leer o su contenido no es
     *       un JSON válido, devuelve null.
     *
     * @param nombreJugador nombre del jugador cuya partida se quiere cargar
     * @return los datos guardados, o null si no hay partida previa o hubo un error
     */
    public static DatosGuardado cargarPartida(String nombreJugador) {
        ValidacionesUtiles.esDistintoDeNull(nombreJugador, "nombreJugador");

        String rutaArchivo = obtenerRutaArchivo(nombreJugador);
        Path pathArchivo = Paths.get(rutaArchivo);

        if (!Files.exists(pathArchivo)) {
            System.out.println("No existe una partida guardada para " + nombreJugador + ".");
            return null;
        }

        try (FileReader lector = new FileReader(rutaArchivo)) {
            DatosGuardado datos = GSON.fromJson(lector, DatosGuardado.class);
            System.out.println("Partida cargada correctamente desde " + rutaArchivo);
            return datos;

        } catch (IOException excepcion) {
            System.out.println("Error al cargar la partida: " + excepcion.getMessage());
            return null;

        } catch (JsonSyntaxException excepcion) {
            System.out.println("El archivo de guardado está corrupto o mal formado: "
                    + excepcion.getMessage());
            return null;
        }
    }

    /**
     * pre:  nombreJugador != null
     * post: devuelve true si existe un archivo de guardado para ese jugador.
     *
     * @param nombreJugador nombre del jugador a verificar
     * @return true si existe "saves/{nombreJugador}.json"
     */
    public static boolean existePartidaGuardada(String nombreJugador) {
        ValidacionesUtiles.esDistintoDeNull(nombreJugador, "nombreJugador");
        return Files.exists(Paths.get(obtenerRutaArchivo(nombreJugador)));
    }

    // ── Métodos privados de apoyo ─────────────────────────────────────────────

    /**
     * post: devuelve la ruta relativa del archivo de guardado correspondiente
     *       al nombre de jugador indicado, con el formato
     *       "saves/{nombreJugador}.json".
     */
    private static String obtenerRutaArchivo(String nombreJugador) {
        return CARPETA_GUARDADOS + nombreJugador + EXTENSION_GUARDADO;
    }

    /**
     * post: garantiza que la carpeta "saves/" exista, creándola si es
     *       necesario. Si la creación falla, se informa por consola pero no
     *       se interrumpe la ejecución (guardarPartida reportará el error
     *       al intentar escribir el archivo).
     */
    private static void crearCarpetaGuardadosSiNoExiste() {
        Path carpeta = Paths.get(CARPETA_GUARDADOS);
        if (!Files.exists(carpeta)) {
            try {
                Files.createDirectories(carpeta);
            } catch (IOException excepcion) {
                System.out.println("Error al crear la carpeta de guardados: "
                        + excepcion.getMessage());
            }
        }
    }
}