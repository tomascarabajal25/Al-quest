package persistencia;

import javax.swing.JOptionPane;

import modelos.Jugador;
import modelos.PartidaGeneral;
import utils.ValidacionesUtiles;

/**
 * TDA GestorDeInicio — punto de entrada del flujo de login de Al-Quest.
 *
 * Responsabilidad: pedirle el nombre al jugador, intentar cargar una
 * partida previa con ese nombre, y devolver una PartidaGeneral lista para
 * iniciar — ya sea reconstruida desde un guardado o creada desde cero.
 *
 * Esta clase no conoce el formato del JSON ni la estructura del grafo:
 * delega la persistencia en GestorArchivosJSON/DatosGuardado y la
 * reconstrucción del estado en PartidaGeneral.aplicarDatosGuardado(...).
 *
 * Todos sus métodos son estáticos (clase de utilidades, no instanciable).
 */
public class GestorDeInicio {

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Constructor privado: esta clase no debe instanciarse.
     */
    private GestorDeInicio() {
    }

    // ── Métodos de clase ──────────────────────────────────────────────────────

    /**
     * Ejecuta el flujo completo de login/creación de partida.
     *
     * pre:  debe ejecutarse antes de mostrar cualquier ventana del juego.
     * post: - le pide al jugador su nombre mediante un JOptionPane.
     *       - si existe un guardado para ese nombre (GestorArchivosJSON),
     *         muestra un mensaje de bienvenida y restaura el puntaje, las
     *         ciudades completadas y la ciudad actual sobre una
     *         PartidaGeneral recién creada.
     *       - si no existe guardado, muestra un mensaje de nueva partida y
     *         devuelve una PartidaGeneral con el jugador en su estado
     *         inicial (sin ciudades completadas, parado en la Ciudad 1).
     *
     * @return la PartidaGeneral lista para invocar iniciar(), o null si el
     *         usuario canceló el ingreso del nombre
     */
    public static PartidaGeneral iniciarSesion() {
        String nombreJugador = pedirNombreJugador();

        if (nombreJugador == null) {
            return null; // el usuario cerró o canceló el diálogo de ingreso
        }

        Jugador jugador = new Jugador(nombreJugador);
        PartidaGeneral partidaGeneral = new PartidaGeneral(jugador);

        DatosGuardado datosGuardados = GestorArchivosJSON.cargarPartida(nombreJugador);

        if (datosGuardados != null) {
            mostrarMensajeBienvenidaDeVuelta(nombreJugador);
            partidaGeneral.aplicarDatosGuardado(datosGuardados);
        } else {
            mostrarMensajeNuevaPartida(nombreJugador);
        }

        return partidaGeneral;
    }

    /**
     * Genera el DatosGuardado correspondiente al estado actual de la
     * partida y lo persiste mediante GestorArchivosJSON.
     *
     * pre:  partidaGeneral != null
     * post: se crea o actualiza "saves/{nombreJugador}.json" con el estado
     *       actual del jugador y del grafo. No modifica partidaGeneral.
     *
     * @param partidaGeneral partida cuyo estado actual se desea guardar
     * @return true si el guardado fue exitoso, false en caso contrario
     */
    public static boolean guardarSesion(PartidaGeneral partidaGeneral) {
        ValidacionesUtiles.esDistintoDeNull(partidaGeneral, "partidaGeneral");

        DatosGuardado datos = partidaGeneral.generarDatosGuardado();
        return GestorArchivosJSON.guardarPartida(datos);
    }

    // ── Métodos privados de apoyo ─────────────────────────────────────────────

    /**
     * post: muestra un diálogo pidiendo el nombre del jugador y devuelve el
     *       texto ingresado sin espacios al inicio/final, o null si el
     *       usuario canceló o ingresó un texto vacío.
     */
    private static String pedirNombreJugador() {
        String entrada = JOptionPane.showInputDialog(
                null,
                "Ingresá tu nombre de jugador:",
                "Al-Quest — Inicio de sesión",
                JOptionPane.QUESTION_MESSAGE);

        if (entrada == null) {
            return null;
        }

        String nombreLimpio = entrada.trim();
        if (nombreLimpio.isEmpty()) {
            return null;
        }
        return nombreLimpio;
    }

    /**
     * post: muestra el mensaje de bienvenida para un jugador con partida
     *       previa guardada.
     */
    private static void mostrarMensajeBienvenidaDeVuelta(String nombreJugador) {
        JOptionPane.showMessageDialog(
                null,
                "Bienvenido de vuelta, " + nombreJugador + ". Cargando partida...",
                "Al-Quest",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * post: muestra el mensaje de creación de partida para un jugador sin
     *       guardado previo.
     */
    private static void mostrarMensajeNuevaPartida(String nombreJugador) {
        JOptionPane.showMessageDialog(
                null,
                "Creando nueva partida para " + nombreJugador + "...",
                "Al-Quest",
                JOptionPane.INFORMATION_MESSAGE);
    }
}