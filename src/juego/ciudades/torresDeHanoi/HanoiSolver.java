package juego.ciudades.torresDeHanoi;

import utils.ValidacionesUtiles;

/**
 * TDA HanoiSolver — resuelve el puzzle de Torres de Hanoi de forma recursiva,
 * notificando cada movimiento a un ObservadorHanoi.
 *
 * Responsabilidad: aplicar el algoritmo clásico de Hanoi sobre tres pilas,
 * permitiendo que un observador externo decida, después de cada movimiento,
 * si la resolución debe continuar o detenerse (por ejemplo, para animarla
 * paso a paso en la vista).
 *
 * @param <T> tipo de dato almacenado en los discos de las pilas
 */
public class HanoiSolver<T> {

    // ATRIBUTOS

    /** true si el observador pidió detener la resolución. */
    private boolean detenido = false;

    /** Cantidad de movimientos realizados hasta el momento. */
    private int pasoActual = 0;

    /** Observador notificado después de cada movimiento. */
    private final ObservadorHanoi observador;

    // CONSTRUCTORES

    /**
     * Pre:
     * - observador != null
     *
     * Post:
     * - el solver queda listo para resolver, sin movimientos realizados.
     *
     * @param observador objeto notificado después de cada movimiento
     */
    public HanoiSolver(ObservadorHanoi observador) {
        ValidacionesUtiles.esDistintoDeNull(observador, "observador");
        this.observador = observador;
    }

    // METODOS DE COMPORTAMIENTO

    /**
     * Resuelve el puzzle de Hanoi de forma recursiva.
     *
     * Pre:
     * - cantidadDiscos > 0
     * - origen != null, auxiliar != null, destino != null
     *
     * Post:
     * - mueve cantidadDiscos discos de origen a destino utilizando auxiliar
     *   como torre intermedia, siguiendo el algoritmo recursivo clásico de
     *   Hanoi.
     * - se detiene antes de tiempo si el observador devuelve false en algún
     *   movimiento (no se realizan más movimientos a partir de ese punto).
     *
     * @param cantidadDiscos cantidad de discos a mover en este sub-problema
     * @param origen         pila desde la que se mueven los discos
     * @param auxiliar       pila utilizada como apoyo intermedio
     * @param destino        pila hacia la que se mueven los discos
     */
    public void resolverHanoi(
            int cantidadDiscos,
            Pila<T> origen,
            Pila<T> auxiliar,
            Pila<T> destino) {

        ValidacionesUtiles.esDistintoDeNull(origen, "origen");
        ValidacionesUtiles.esDistintoDeNull(auxiliar, "auxiliar");
        ValidacionesUtiles.esDistintoDeNull(destino, "destino");
        ValidacionesUtiles.validarMayorACero(cantidadDiscos, "cantidadDiscos");

        if (detenido) {
            return;
        }

        if (cantidadDiscos == 1) {
            moverPlataforma(origen, destino);
            return;
        }

        resolverHanoi(cantidadDiscos - 1, origen, destino, auxiliar);
        moverPlataforma(origen, destino);
        resolverHanoi(cantidadDiscos - 1, auxiliar, origen, destino);
    }

    /**
     * Mueve un disco entre dos pilas y notifica al observador.
     *
     * Pre:
     * - origen != null, destino != null
     * - origen no está vacía
     *
     * Post:
     * - mueve el disco del tope de origen al tope de destino.
     * - incrementa pasoActual.
     * - consulta al observador si se debe continuar; si responde false,
     *   marca el solver como detenido.
     * - si el solver ya estaba detenido, no hace nada.
     *
     * @param origen  pila desde la que se quita el disco
     * @param destino pila a la que se agrega el disco
     */
    private void moverPlataforma(Pila<T> origen, Pila<T> destino) {
        ValidacionesUtiles.esDistintoDeNull(origen, "origen");
        ValidacionesUtiles.esDistintoDeNull(destino, "destino");

        if (detenido) {
            return;
        }

        Nodo<T> discoMovido = new Nodo<>(origen.peek());
        origen.pop();
        destino.push(discoMovido);
        pasoActual++;

        boolean continuar = observador.onMovimiento(pasoActual);
        if (!continuar) {
            detenido = true;
        }
    }
}