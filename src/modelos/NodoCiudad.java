package modelos;

import java.util.ArrayList;
import java.util.List;

/**
 * TDA NodoCiudad — vértice del grafo de ciudades del mundo Al-Quest.
 *
 * Responsabilidad: modelar una ciudad-desafío como nodo del grafo,
 * encapsulando su identidad, su lógica de juego asociada y la lista
 * de caminos salientes (adyacencia dirigida).
 *
 * Invariante de clase:
 *   - id ∈ [1, GrafoCiudades.MAX_CIUDADES]
 *   - nombre != null
 *   - adyacentes != null (puede estar vacía, nunca null)
 */
public class NodoCiudad {

    // ── Atributos ─────────────────────────────────────────────────────────────

    /** Identificador único de la ciudad (1-10). */
    private final int id;

    /** Nombre legible de la ciudad (ej: "Ciudad de Búsqueda"). */
    private String nombre;

    /**
     * Referencia polimórfica a la lógica de juego de esta ciudad.
     * Se usa Partida como tipo base para desacoplar el nodo del grafo
     * de implementaciones concretas (PartidaBusqueda, etc.).
     */
    private Partida partidaAsociada;

    /** true si el jugador completó exitosamente el desafío de esta ciudad. */
    private boolean completada;

    /**
     * Lista de adyacencia: ciudades a las que se puede llegar desde ésta.
     * Representa caminos salientes en el grafo dirigido.
     */
    private final List<NodoCiudad> adyacentes;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * pre:  id ∈ [1,10], nombre != null, partidaAsociada != null
     * post: crea el nodo con completada=false y lista de adyacentes vacía.
     *
     * @param id               identificador único de la ciudad
     * @param nombre           nombre legible de la ciudad
     * @param partidaAsociada  lógica de juego vinculada a esta ciudad
     */
    public NodoCiudad(int id, String nombre, Partida partidaAsociada) {
        this.id               = id;
        this.nombre           = nombre;
        this.partidaAsociada  = partidaAsociada;
        this.completada       = false;
        this.adyacentes       = new ArrayList<>();
    }

    // ── Métodos de comportamiento ─────────────────────────────────────────────

    /**
     * Agrega un camino saliente hacia la ciudad destino.
     *
     * pre:  destino != null y no es el mismo nodo (sin bucles directos)
     * post: destino queda registrado en adyacentes si no estaba ya.
     *       El grafo es dirigido: la relación inversa no se crea aquí.
     *
     * @param destino nodo de la ciudad a la que lleva este camino
     */
    public void agregarCamino(NodoCiudad destino) {
        if (destino == null || destino == this) {
            return; // pre-condición no cumplida: se ignora silenciosamente
        }
        if (!adyacentes.contains(destino)) {
            adyacentes.add(destino);
        }
    }

    // ── Getters y Setters ─────────────────────────────────────────────────────

    /** @return identificador único de la ciudad (inmutable). */
    public int getId() {
        return id;
    }

    /** @return nombre legible de la ciudad. */
    public String getNombre() {
        return nombre;
    }

    /**
     * pre:  nombre != null
     * post: actualiza el nombre de la ciudad.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /** @return referencia polimórfica a la lógica de juego. */
    public Partida getPartidaAsociada() {
        return partidaAsociada;
    }

    /**
     * pre:  partidaAsociada != null
     * post: reemplaza la lógica de juego vinculada a este nodo.
     */
    public void setPartidaAsociada(Partida partidaAsociada) {
        this.partidaAsociada = partidaAsociada;
    }

    /** @return true si el desafío de esta ciudad fue completado. */
    public boolean isCompletada() {
        return completada;
    }

    /**
     * post: marca o desmarca la ciudad como completada.
     *       PartidaGeneral llama a setCompletada(true) desde el callback.
     */
    public void setCompletada(boolean completada) {
        this.completada = completada;
    }

    /**
     * post: devuelve una vista no modificable de la lista de adyacentes
     *       para evitar modificaciones externas sin pasar por agregarCamino().
     *
     * @return lista de ciudades alcanzables desde ésta
     */
    public List<NodoCiudad> getAdyacentes() {
        return java.util.Collections.unmodifiableList(adyacentes);
    }

    // ── Object overrides ──────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "NodoCiudad{id=" + id
                + ", nombre='" + nombre + '\''
                + ", completada=" + completada
                + ", caminos=" + adyacentes.size() + '}';
    }
}
