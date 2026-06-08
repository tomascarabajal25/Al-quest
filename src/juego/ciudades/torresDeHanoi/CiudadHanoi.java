package Juego.ciudades.torresDeHanoi;

import java.util.Objects;
import utils.ValidacionesUtiles;

/**
 * TDA que representa el juego de las Torres de Hanoi.
 *
 * Modela el estado del juego mediante tres pilas (torres) de enteros,
 * donde cada entero representa el TAMAÑO del disco (1 = más pequeño).
 *
 * INVARIANTES:
 * - 3 <= objetivo <= 10
 * - movimientos >= 0
 * - En cada torre, los discos están ordenados de mayor a menor
 *   (mayor en el fondo, menor en el tope).
 */
public class CiudadHanoi {

    // ── Atributos ─────────────────────────────────────────────────────────────
    private Pila<Integer> torreA;
    private Pila<Integer> torreB;
    private Pila<Integer> torreC;
    private int movimientos;
    private int objetivo;

    // ── Constructores ─────────────────────────────────────────────────────────

    /**
     * PRE:  3 <= discos <= 10
     * POST: torreA contiene todos los discos ordenados; torreB y torreC vacías;
     *       movimientos = 0.
     */
    public CiudadHanoi(int discos) {
        setObjetivo(discos);
        iniciar();
    }

    // ── Comportamiento ────────────────────────────────────────────────────────

    /**
     * Inicializa el estado del juego.
     * POST: torreA tiene todos los discos (objetivo en el fondo, 1 en el tope).
     */
    public void iniciar() {
        torreA = new Pila<Integer>();
        torreB = new Pila<Integer>();
        torreC = new Pila<Integer>();
        movimientos = 0;

        // objetivo = disco más grande; se apila de mayor a menor
        // → queda mayor en el fondo y 1 (el más chico) en el tope.
        for (int i = objetivo; i >= 1; i--) {
            torreA.push(new Nodo<Integer>(i));
        }
    }

    /**
     * PRE:  3 <= nuevoObjetivo <= 10
     * POST: estado equivalente a haber creado una nueva instancia con ese objetivo.
     */
    public void reiniciar(int nuevoObjetivo) {
        ValidacionesUtiles.validarRangoNumerico(nuevoObjetivo, 3, 10, "No es un objetivo valido");
        setObjetivo(nuevoObjetivo);
        setMovimientos(0);
        vaciarPila(torreA);
        vaciarPila(torreB);
        vaciarPila(torreC);
        for (int i = objetivo; i >= 1; i--) {
            torreA.push(new Nodo<Integer>(i));
        }
    }

    /** POST: p queda vacía. */
    private void vaciarPila(Pila<Integer> p) {
        ValidacionesUtiles.esDistintoDeNull(p, "no se puede vaciar pila nula");
        while (p.getContNodo() > 0) p.pop();
    }

    /**
     * Realiza un movimiento entre torres.
     *
     * PRE:  origen != null, destino != null
     * POST: si es válido, mueve el disco del tope de origen al tope de destino
     *       e incrementa movimientos; retorna true.
     *       Si no es válido (origen vacía o disco mayor sobre menor), no cambia
     *       nada y retorna false.
     */
    public boolean mover(Pila<Integer> origen, Pila<Integer> destino) {
        ValidacionesUtiles.esDistintoDeNull(origen,  "origen no puede ser null");
        ValidacionesUtiles.esDistintoDeNull(destino, "destino no puede ser null");

        if (origen.getContNodo() == 0) return false;

        int discoMovido = origen.peek();

        // No se puede poner un disco mayor sobre uno menor
        if (destino.getContNodo() > 0 && discoMovido > destino.peek()) {
            return false;
        }

        origen.pop();
        destino.push(new Nodo<Integer>(discoMovido));
        setMovimientos(++movimientos);
        return true;
    }

    /** Retorna true si todos los discos están en la torre C. */
    public boolean haGanado() {
        return torreC.getContNodo() == objetivo;
    }

    /** Retorna true si ganó con el mínimo de movimientos posible. */
    public boolean esPerfecto() {
        return haGanado() && movimientos == (int) getMinMovimientos();
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    /**
     * Devuelve los tamaños de los discos de una torre en un arreglo de enteros.
     *
     * El arreglo tiene longitud fija = objetivo (máximo discos posibles).
     * Posición 0 = fondo de la pila (disco más grande que haya).
     * Posiciones vacías = 0.
     *
     * PRE:  torre != null
     * POST: retorna int[objetivo]; posiciones con disco contienen su tamaño (>0),
     *       posiciones vacías contienen 0.
     */
    public int[] getDiscosDeTorre(Pila<Integer> torre) {
        ValidacionesUtiles.esDistintoDeNull(torre, "torre no puede ser null");

        int[] discos = new int[objetivo]; // 0 significa "slot vacío"

        // Recorremos desde la cabeza (tope) hacia el fondo
        // y llenamos el arreglo desde el índice 0.
        // discos[0] = tope de pila (disco más pequeño presente)
        // discos[contNodo-1] = fondo de pila (disco más grande presente)
        Nodo<Integer> actual = torre.getCabeza();
        int i = 0;
        while (actual != null && i < discos.length) {
            discos[i] = actual.getDato();
            actual = actual.getAbajo();
            i++;
        }
        return discos;
    }

    public double getMinMovimientos() { return Math.pow(2, objetivo) - 1; }
    public int    getMovimientos()    { return movimientos; }
    public int    getObjetivo()       { return objetivo; }

    public Pila<Integer> getTorreA() { return torreA; }
    public Pila<Integer> getTorreB() { return torreB; }
    public Pila<Integer> getTorreC() { return torreC; }

    // ── Setters privados ──────────────────────────────────────────────────────

    private void setMovimientos(int m) { this.movimientos = m; }

    private void setObjetivo(int o) {
        ValidacionesUtiles.validarRangoNumerico(o, 3, 10, "No es una cantidad de discos valida");
        this.objetivo = o;
    }

    // ── Object ────────────────────────────────────────────────────────────────

    @Override
    public String toString() {
        return "CiudadHanoi [movimientos=" + movimientos + ", objetivo=" + objetivo + "]";
    }

    @Override
    public int hashCode() { return Objects.hash(movimientos, objetivo); }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CiudadHanoi other = (CiudadHanoi) obj;
        return movimientos == other.movimientos && objetivo == other.objetivo;
    }
}