package juego.ciudades.torresDeHanoi;

import java.util.Objects;
import utils.ValidacionesUtiles;

/**
 * TDA que representa el juego de las Torres de Hanoi.
 *
 * Modela el estado del juego mediante tres pilas (torres) de enteros,
 * donde cada entero representa el TAMAÑO del disco (1 = más pequeño).
 *
 * INVARIANTES:
 * - ConfiguracionDeHanoi.DISCOS_MINIMOS <= objetivo <= ConfiguracionDeHanoi.DISCOS_MAXIMOS
 * - movimientos >= 0
 * - En cada torre, los discos están ordenados de mayor a menor
 *   (mayor en el fondo, menor en el tope).
 */
public class CiudadHanoi {

    // ATRIBUTOS

    private Pila<Integer> torreA;
    private Pila<Integer> torreB;
    private Pila<Integer> torreC;
    private int movimientos;
    private int objetivo;

    // CONSTRUCTORES

    /**
     * Crea una nueva partida de Torres de Hanoi.
     *
     * Pre:
     * - ConfiguracionDeHanoi.DISCOS_MINIMOS <= discos <= ConfiguracionDeHanoi.DISCOS_MAXIMOS
     *
     * Post:
     * - torreA contiene todos los discos ordenados; torreB y torreC vacías.
     * - movimientos = 0.
     *
     * @param discos cantidad de discos del puzzle
     */
    public CiudadHanoi(int discos) {
        setObjetivo(discos);
        iniciar();
    }

    // METODOS DE COMPORTAMIENTO

    /**
     * Inicializa el estado del juego.
     *
     * Post:
     * - torreA tiene todos los discos (objetivo en el fondo, 1 en el tope).
     * - torreB y torreC quedan vacías.
     * - movimientos = 0.
     */
    public void iniciar() {
        torreA = new Pila<Integer>();
        torreB = new Pila<Integer>();
        torreC = new Pila<Integer>();
        movimientos = 0;

        // objetivo = disco más grande; se apila de mayor a menor
        // → queda mayor en el fondo y 1 (el más chico) en el tope.
        for (int tamanioDisco = objetivo; tamanioDisco >= 1; tamanioDisco--) {
            torreA.push(new Nodo<Integer>(tamanioDisco));
        }
    }

    /**
     * Reinicia el juego con una nueva cantidad de discos.
     *
     * Pre:
     * - ConfiguracionDeHanoi.DISCOS_MINIMOS <= nuevoObjetivo <= ConfiguracionDeHanoi.DISCOS_MAXIMOS
     *
     * Post:
     * - El estado queda equivalente a haber creado una nueva instancia con
     *   ese objetivo (torreA con todos los discos, torreB y torreC vacías,
     *   movimientos = 0).
     *
     * @param nuevoObjetivo nueva cantidad de discos del puzzle
     */
    public void reiniciar(int nuevoObjetivo) {
        ValidacionesUtiles.validarRangoNumerico(
                nuevoObjetivo,
                ConfiguracionDeHanoi.DISCOS_MINIMOS,
                ConfiguracionDeHanoi.DISCOS_MAXIMOS,
                "No es un objetivo valido");

        setObjetivo(nuevoObjetivo);
        setMovimientos(0);
        vaciarPila(torreA);
        vaciarPila(torreB);
        vaciarPila(torreC);

        for (int tamanioDisco = objetivo; tamanioDisco >= 1; tamanioDisco--) {
            torreA.push(new Nodo<Integer>(tamanioDisco));
        }
    }

    /**
     * Vacía completamente una pila.
     *
     * Pre:
     * - pila != null
     *
     * Post:
     * - pila queda sin nodos (getContNodo() == 0).
     *
     * @param pila pila a vaciar
     */
    private void vaciarPila(Pila<Integer> pila) {
        ValidacionesUtiles.esDistintoDeNull(pila, "no se puede vaciar pila nula");
        while (pila.getContNodo() > 0) {
            pila.pop();
        }
    }

    /**
     * Realiza un movimiento entre torres.
     *
     * Pre:
     * - origen != null
     * - destino != null
     *
     * Post:
     * - Si el movimiento es válido (origen no vacía y el disco movido no es
     *   mayor que el tope de destino), mueve el disco del tope de origen al
     *   tope de destino, incrementa movimientos y devuelve true.
     * - Si no es válido, no modifica ninguna torre y devuelve false.
     *
     * @param origen  torre desde la que se quiere mover un disco
     * @param destino torre hacia la que se quiere mover el disco
     * @return true si el movimiento se realizó, false en caso contrario
     */
    public boolean mover(Pila<Integer> origen, Pila<Integer> destino) {
        ValidacionesUtiles.esDistintoDeNull(origen, "origen no puede ser null");
        ValidacionesUtiles.esDistintoDeNull(destino, "destino no puede ser null");

        if (origen.getContNodo() == 0) {
            return false;
        }

        int discoMovido = origen.peek();

        // No se puede poner un disco mayor sobre uno menor
        if (destino.getContNodo() > 0 && discoMovido > destino.peek()) {
            return false;
        }

        origen.pop();
        destino.push(new Nodo<Integer>(discoMovido));
        setMovimientos(movimientos + 1);
        return true;
    }

    /**
     * Post: no modifica el estado del juego; solo lo consulta.
     *
     * @return true si todos los discos están en la torre C
     */
    public boolean haGanado() {
        return torreC.getContNodo() == objetivo;
    }

    /**
     * Post: no modifica el estado del juego; solo lo consulta.
     *
     * @return true si el jugador ganó usando exactamente el mínimo de
     *         movimientos posible (2^objetivo - 1)
     */
    public boolean esPerfecto() {
        return haGanado() && movimientos == (int) getMinMovimientos();
    }

    // GETTERS

    /**
     * Devuelve los tamaños de los discos de una torre en un arreglo de enteros.
     *
     * El arreglo tiene longitud fija = objetivo (máximo discos posibles).
     * Posición 0 = tope de la pila (disco más pequeño presente).
     * Posiciones vacías = 0.
     *
     * Pre:
     * - torre != null
     *
     * Post:
     * - Devuelve un int[objetivo]; las posiciones con disco contienen su
     *   tamaño (mayor a 0), las posiciones vacías contienen 0.
     *
     * @param torre torre cuyos discos se quieren consultar
     * @return arreglo con los tamaños de los discos, desde el tope al fondo
     */
    public int[] getDiscosDeTorre(Pila<Integer> torre) {
        ValidacionesUtiles.esDistintoDeNull(torre, "torre no puede ser null");

        int[] discos = new int[objetivo]; // 0 significa "slot vacío"

        // Recorremos desde la cabeza (tope) hacia el fondo
        // y llenamos el arreglo desde el índice 0.
        // discos[0] = tope de pila (disco más pequeño presente)
        // discos[contNodo-1] = fondo de pila (disco más grande presente)
        Nodo<Integer> actual = torre.getCabeza();
        int indice = 0;
        while (actual != null && indice < discos.length) {
            discos[indice] = actual.getDato();
            actual = actual.getAbajo();
            indice++;
        }
        return discos;
    }

    /** @return cantidad mínima de movimientos para resolver el puzzle (2^objetivo - 1) */
    public double getMinMovimientos() {
        return Math.pow(2, objetivo) - 1;
    }

    /** @return cantidad de movimientos realizados hasta el momento */
    public int getMovimientos() {
        return movimientos;
    }

    /** @return cantidad de discos configurada para esta partida */
    public int getObjetivo() {
        return objetivo;
    }

    /** @return torre A */
    public Pila<Integer> getTorreA() {
        return torreA;
    }

    /** @return torre B */
    public Pila<Integer> getTorreB() {
        return torreB;
    }

    /** @return torre C */
    public Pila<Integer> getTorreC() {
        return torreC;
    }

    // SETTERS

    /**
     * Post: actualiza la cantidad de movimientos realizados.
     *
     * @param nuevaCantidadDeMovimientos nueva cantidad de movimientos
     */
    private void setMovimientos(int nuevaCantidadDeMovimientos) {
        this.movimientos = nuevaCantidadDeMovimientos;
    }

    /**
     * Pre:
     * - ConfiguracionDeHanoi.DISCOS_MINIMOS <= nuevoObjetivo <= ConfiguracionDeHanoi.DISCOS_MAXIMOS
     *
     * Post:
     * - actualiza la cantidad de discos objetivo del puzzle.
     *
     * @param nuevoObjetivo nueva cantidad de discos
     */
    private void setObjetivo(int nuevoObjetivo) {
        ValidacionesUtiles.validarRangoNumerico(
                nuevoObjetivo,
                ConfiguracionDeHanoi.DISCOS_MINIMOS,
                ConfiguracionDeHanoi.DISCOS_MAXIMOS,
                "No es una cantidad de discos valida");
        this.objetivo = nuevoObjetivo;
    }

    // METODOS GENERALES

    @Override
    public String toString() {
        return "CiudadHanoi [movimientos=" + movimientos + ", objetivo=" + objetivo + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(movimientos, objetivo);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CiudadHanoi otraCiudad = (CiudadHanoi) obj;
        return movimientos == otraCiudad.movimientos && objetivo == otraCiudad.objetivo;
    }
}