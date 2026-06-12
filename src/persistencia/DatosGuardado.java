package persistencia;

import java.util.Objects;
import java.util.Vector;

/**
 * TDA DatosGuardado — Objeto de Transferencia de Datos (DTO) que representa
 * el estado persistible de una partida de Al-Quest.
 *
 * Responsabilidad: contener únicamente los datos necesarios para reconstruir
 * el progreso del jugador (jugador, puntaje, ciudad actual, ciudades
 * completadas y ciudades desbloqueadas), sin ninguna referencia a elementos
 * gráficos (Swing) ni a lógica transitoria de las sub-partidas.
 *
 * Esta clase es serializada y deserializada directamente por Gson, por lo
 * que su estructura define el formato del archivo JSON de guardado.
 *
 * Invariante de clase:
 *   - nombreJugador != null
 *   - puntajeTotal >= 0
 *   - idCiudadActual ∈ [1, GrafoCiudades.MAX_CIUDADES]
 *   - idsCiudadesCompletadas != null
 *   - idsCiudadesDesbloqueadas != null
 */
public class DatosGuardado {

    // ── Atributos ─────────────────────────────────────────────────────────────

    /** Nombre del jugador. Se usa además como nombre del archivo de guardado. */
    private String nombreJugador;

    /** Puntaje acumulado a lo largo de todas las ciudades completadas. */
    private int puntajeTotal;

    /** Id de la ciudad en la que se encontraba el jugador al guardar la partida. */
    private int idCiudadActual;

    /** Ids de las ciudades cuyo desafío ya fue completado. */
    private Vector<Integer> idsCiudadesCompletadas;

    /**
     * Ids de las ciudades actualmente accesibles para el jugador (incluye
     * las completadas y las recién desbloqueadas a partir de ellas).
     *
     * Se guarda como información derivada para facilitar la reconstrucción
     * y depuración del mapa; al cargar la partida, la accesibilidad real se
     * vuelve a calcular en base a idsCiudadesCompletadas, ya que es la
     * fuente de verdad según la regla de accesibilidad del grafo.
     */
    private Vector<Integer> idsCiudadesDesbloqueadas;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * pre:  nombreJugador != null, puntajeTotal >= 0,
     *       idsCiudadesCompletadas != null, idsCiudadesDesbloqueadas != null
     * post: crea un DatosGuardado con los valores recibidos. Los Vector se
     *       almacenan por referencia tal cual se reciben.
     *
     * @param nombreJugador            nombre del jugador
     * @param puntajeTotal             puntaje acumulado
     * @param idCiudadActual           ciudad en la que está parado el jugador
     * @param idsCiudadesCompletadas   ids de ciudades completadas
     * @param idsCiudadesDesbloqueadas ids de ciudades actualmente accesibles
     */
    public DatosGuardado(
            String nombreJugador,
            int puntajeTotal,
            int idCiudadActual,
            Vector<Integer> idsCiudadesCompletadas,
            Vector<Integer> idsCiudadesDesbloqueadas) {

        this.nombreJugador             = nombreJugador;
        this.puntajeTotal               = puntajeTotal;
        this.idCiudadActual             = idCiudadActual;
        this.idsCiudadesCompletadas     = idsCiudadesCompletadas;
        this.idsCiudadesDesbloqueadas   = idsCiudadesDesbloqueadas;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    /** @return nombre del jugador propietario de esta partida guardada. */
    public String getNombreJugador() {
        return nombreJugador;
    }

    /** @return puntaje total acumulado al momento de guardar. */
    public int getPuntajeTotal() {
        return puntajeTotal;
    }

    /** @return id de la ciudad donde estaba parado el jugador al guardar. */
    public int getIdCiudadActual() {
        return idCiudadActual;
    }

    /** @return ids de las ciudades completadas al momento de guardar. */
    public Vector<Integer> getIdsCiudadesCompletadas() {
        return idsCiudadesCompletadas;
    }

    /** @return ids de las ciudades accesibles al momento de guardar. */
    public Vector<Integer> getIdsCiudadesDesbloqueadas() {
        return idsCiudadesDesbloqueadas;
    }

    // ── Object overrides ──────────────────────────────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DatosGuardado)) {
            return false;
        }
        DatosGuardado otro = (DatosGuardado) o;
        return puntajeTotal == otro.puntajeTotal
                && idCiudadActual == otro.idCiudadActual
                && Objects.equals(nombreJugador, otro.nombreJugador)
                && Objects.equals(idsCiudadesCompletadas, otro.idsCiudadesCompletadas)
                && Objects.equals(idsCiudadesDesbloqueadas, otro.idsCiudadesDesbloqueadas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                nombreJugador,
                puntajeTotal,
                idCiudadActual,
                idsCiudadesCompletadas,
                idsCiudadesDesbloqueadas);
    }

    @Override
    public String toString() {
        return "DatosGuardado{"
                + "nombreJugador='" + nombreJugador + '\''
                + ", puntajeTotal=" + puntajeTotal
                + ", idCiudadActual=" + idCiudadActual
                + ", idsCiudadesCompletadas=" + idsCiudadesCompletadas
                + ", idsCiudadesDesbloqueadas=" + idsCiudadesDesbloqueadas
                + '}';
    }
}