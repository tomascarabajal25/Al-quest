package modelos;

import java.util.Collections;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;

/**
 * TDA GrafoCiudades — grafo dirigido que modela la red de ciudades de Al-Quest.
 *
 * Responsabilidad: gestionar los 10 nodos-ciudad, sus conexiones dirigidas
 * y la regla de accesibilidad que controla la progresión del jugador.
 *
 * Regla de accesibilidad (condición del TP):
 *   Una ciudad es accesible si:
 *     a) es la Ciudad 1 (punto de entrada inicial), O
 *     b) existe al menos un nodo vecino que ya apunte a ella Y ese vecino
 *        está marcado como completada.
 *
 * Modularización: esta clase no conoce ningún componente Swing.
 * Solo opera sobre NodoCiudad y tipos primitivos.
 *
 * Invariante de clase:
 *   - nodos != null
 *   - nodos.size() <= MAX_CIUDADES
 *   - todas las claves coinciden con el id del nodo almacenado
 */
public class GrafoCiudades {

    // ── Constantes ────────────────────────────────────────────────────────────

    /** Cantidad máxima de ciudades en el mapa del juego. */
    public static final int MAX_CIUDADES = 10;

    /** ID de la ciudad inicial, siempre accesible sin condiciones previas. */
    public static final int ID_CIUDAD_INICIAL = 1;

    // ── Atributos ─────────────────────────────────────────────────────────────

    /**
     * Diccionario de nodos indexado por ID de ciudad.
     * Permite acceso O(1) para consultas del loop de 60 FPS.
     */
    private final Map<Integer, NodoCiudad> nodos;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * post: crea el grafo vacío listo para recibir ciudades y conexiones.
     */
    public GrafoCiudades() {
        this.nodos = new HashMap<>();
    }

    // ── Métodos de construcción del grafo ─────────────────────────────────────

    /**
     * Agrega una ciudad al grafo.
     *
     * pre:  ciudad != null, ciudad.getId() ∈ [1, MAX_CIUDADES],
     *       no existe ya un nodo con ese id
     * post: el nodo queda registrado y es consultable por id.
     *
     * @param ciudad nodo que representa la ciudad a agregar
     */
    public void agregarCiudad(NodoCiudad ciudad) {
        if (ciudad == null) {
            return;
        }
        if (nodos.size() >= MAX_CIUDADES) {
            throw new IllegalStateException(
                "El grafo ya tiene el máximo de " + MAX_CIUDADES + " ciudades.");
        }
        nodos.put(ciudad.getId(), ciudad);
    }

    /**
     * Conecta dos ciudades con un camino dirigido: idOrigen → idDestino.
     * El grafo es dirigido: recorrer el camino en sentido contrario requiere
     * una llamada separada.
     *
     * pre:  idOrigen e idDestino existen en el grafo y son distintos
     * post: idDestino queda en la lista de adyacentes de idOrigen.
     *       No se modifica idDestino.
     *
     * @param idOrigen  ciudad desde la que parte el camino
     * @param idDestino ciudad a la que llega el camino
     */
    public void conectarCiudades(int idOrigen, int idDestino) {
        NodoCiudad origen  = nodos.get(idOrigen);
        NodoCiudad destino = nodos.get(idDestino);

        if (origen == null || destino == null) {
            throw new IllegalArgumentException(
                "No se puede conectar: ciudad no encontrada ("
                + idOrigen + " → " + idDestino + ").");
        }
        origen.agregarCamino(destino);
    }

    // ── Consultas del grafo ───────────────────────────────────────────────────

    /**
     * Determina si el jugador puede ingresar a la ciudad indicada.
     *
     * Regla (condición del TP):
     *   - Ciudad 1: siempre accesible.
     *   - Cualquier otra: accesible si existe al menos un nodo del grafo
     *     que (a) tenga a idCiudad en su lista de adyacentes Y
     *          (b) esté marcado como completada.
     *
     * pre:  idCiudad ∈ [1, MAX_CIUDADES] y existe en el grafo
     * post: no modifica el estado del grafo; solo consulta.
     *
     * @param  idCiudad id de la ciudad que se quiere verificar
     * @return true si el jugador puede ingresar, false en caso contrario
     */
    public boolean esCiudadAccesible(int idCiudad) {
        if (idCiudad == ID_CIUDAD_INICIAL) {
            return true; // punto de entrada siempre desbloqueado
        }

        NodoCiudad candidata = nodos.get(idCiudad);
        if (candidata == null) {
            return false; // ciudad inexistente → inaccesible
        }

        // Recorrer todos los nodos y buscar alguno completado que apunte a candidata
        for (NodoCiudad nodo : nodos.values()) {
            if (nodo.isCompletada() && nodo.getAdyacentes().contains(candidata)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Recupera un nodo-ciudad por su identificador.
     *
     * pre:  idCiudad ∈ [1, MAX_CIUDADES]
     * post: devuelve el nodo o null si no existe.
     *
     * @param  idCiudad id de la ciudad buscada
     * @return el NodoCiudad correspondiente, o null si no se registró
     */
    public NodoCiudad obtenerCiudad(int idCiudad) {
        return nodos.get(idCiudad);
    }

    /**
     * post: devuelve una vista no modificable del diccionario de nodos.
     *       Útil para la vista del mapa que itera sobre las ciudades para
     *       decidir cómo renderizar cada ícono (completada / accesible / bloqueada).
     */
    public Map<Integer, NodoCiudad> getNodos() {
        return Collections.unmodifiableMap(nodos);
    }
  

    /**
    * post: marca la ciudad indicada como completada, si existe en el grafo.
    *       No tiene efecto si idCiudad no corresponde a ningún nodo registrado.
    *
    * @param idCiudad id de la ciudad a marcar como completada
    */
    public void marcarCiudadCompletada(int idCiudad) {
     NodoCiudad nodo = nodos.get(idCiudad);
     if (nodo != null) {
         nodo.setCompletada(true);
     }
    }

    /**
    * post: devuelve los ids de todas las ciudades marcadas como completadas.
    *
    * @return ids de ciudades completadas
    */
    public Vector<Integer> obtenerIdsCompletadas() {
     Vector<Integer> idsCompletadas = new Vector<>();
     for (NodoCiudad nodo : nodos.values()) {
         if (nodo.isCompletada()) {
             idsCompletadas.add(nodo.getId());
         }
     }
     return idsCompletadas;
    }

    /**
    * post: devuelve los ids de todas las ciudades actualmente accesibles
    *       según esCiudadAccesible().
    *
    * @return ids de ciudades accesibles
    */
    public Vector<Integer> obtenerIdsAccesibles() {
     Vector<Integer> idsAccesibles = new Vector<>();
     for (Integer idCiudad : nodos.keySet()) {
         if (esCiudadAccesible(idCiudad)) {
             idsAccesibles.add(idCiudad);
         }
     }
     return idsAccesibles;
    }

    // ── Object overrides ──────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "GrafoCiudades{ciudades=" + nodos.size()
                + "/" + MAX_CIUDADES + '}';
    }
}
