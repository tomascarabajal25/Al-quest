package juego.ciudades.torresDeHanoi;

import java.util.Objects;
import java.util.Vector;

import juego.configuracion.ConfiguracionDeHanoi;
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

    // CONSTANTES

    // No hay constantes locales; se usan las de ConfiguracionDeHanoi

    // ATRIBUTOS DE CLASE

    // No hay atributos de clase

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

    // METODOS DE CLASE

    // No hay métodos de clase

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
        return haGanado() && movimientos == (int) getMinimosMovimientos();
    }

    // GETTERS

    /**
     * Devuelve los tamaños de los discos de una torre en un vector de enteros.
     *
     * El vector contiene los discos presentes, comenzando desde el tope de la pila
     * (disco más pequeño presente) hasta el fondo (disco más grande).
     *
     * Pre:
     * - torre != null
     *
     * Post:
     * - Devuelve un Vector<Integer> con los tamaños de los discos.
     * - La posición 0 corresponde al tope de la pila.
     * - La posición (tamaño - 1) corresponde al fondo de la pila.
     *
     * @param torre torre cuyos discos se quieren consultar
     * @return vector con los tamaños de los discos en orden de tope a fondo
     */
    public Vector<Integer> getDiscosDelTorre(Pila<Integer> torre) {
        ValidacionesUtiles.esDistintoDeNull(torre, "torre no puede ser null");

        Vector<Integer> discos = new Vector<>();

        // Recorremos desde la cabeza (tope) hacia el fondo
        // y agregamos cada disco al vector.
        Nodo<Integer> actual = torre.getCabeza();
        while (actual != null) {
            discos.add(actual.getDato());
            actual = actual.getAbajo();
        }

        return discos;
    }

    /**
     * Post: no modifica el estado del juego; solo lo consulta.
     *
     * @return cantidad mínima de movimientos para resolver el puzzle (2^objetivo - 1)
     */
    public double getMinimosMovimientos() {
        return Math.pow(2, objetivo) - 1;
    }

    /** 
     * Post: no modifica el estado del juego; solo lo consulta.
     *
     * @return cantidad de movimientos realizados hasta el momento
     */
    public int getMovimientos() {
        return movimientos;
    }

    /** 
     * Post: no modifica el estado del juego; solo lo consulta.
     *
     * @return cantidad de discos configurada para esta partida
     */
    public int getObjetivo() {
        return objetivo;
    }

    /** 
     * Devuelve una referencia a la torre A.
     *
     * Pre:
     * - La torre ha sido inicializada mediante iniciar() o reiniciar().
     *
     * Post:
     * - Devuelve la referencia a torreA.
     * - No devuelve copia defensiva (la pila es controlada internamente).
     *
     * @return torre A del puzzle
     */
    public Pila<Integer> getTorreA() {
        return torreA;
    }

    /** 
     * Devuelve una referencia a la torre B.
     *
     * Pre:
     * - La torre ha sido inicializada mediante iniciar() o reiniciar().
     *
     * Post:
     * - Devuelve la referencia a torreB.
     * - No devuelve copia defensiva (la pila es controlada internamente).
     *
     * @return torre B del puzzle
     */
    public Pila<Integer> getTorreB() {
        return torreB;
    }

    /** 
     * Devuelve una referencia a la torre C.
     *
     * Pre:
     * - La torre ha sido inicializada mediante iniciar() o reiniciar().
     *
     * Post:
     * - Devuelve la referencia a torreC.
     * - No devuelve copia defensiva (la pila es controlada internamente).
     *
     * @return torre C del puzzle
     */
    public Pila<Integer> getTorreC() {
        return torreC;
    }

    // SETTERS

    /**
     * Actualiza la cantidad de movimientos realizados.
     *
     * Pre:
     * - nuevaCantidadDeMovimientos >= 0
     *
     * Post:
     * - Actualiza el contador de movimientos al valor proporcionado.
     *
     * @param nuevaCantidadDeMovimientos nueva cantidad de movimientos
     */
    private void setMovimientos(int nuevaCantidadDeMovimientos) {
        this.movimientos = nuevaCantidadDeMovimientos;
    }

    /**
     * Actualiza la cantidad de discos objetivo del puzzle.
     *
     * Pre:
     * - ConfiguracionDeHanoi.DISCOS_MINIMOS <= nuevoObjetivo <= ConfiguracionDeHanoi.DISCOS_MAXIMOS
     *
     * Post:
     * - Actualiza la cantidad de discos objetivo.
     * - Valida el rango mediante ValidacionesUtiles.
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
        StringBuilder representacion = new StringBuilder();
        representacion.append("CiudadHanoi{");
        representacion.append("objetivo=").append(objetivo);
        representacion.append(", movimientos=").append(movimientos);
        representacion.append(", minimosMovimientos=").append(String.format("%.0f", getMinimosMovimientos()));
        representacion.append(", torreA=[");
        representacion.append(construirRepresentacionTorre(torreA));
        representacion.append("], torreB=[");
        representacion.append(construirRepresentacionTorre(torreB));
        representacion.append("], torreC=[");
        representacion.append(construirRepresentacionTorre(torreC));
        representacion.append("], ganador=").append(haGanado());
        representacion.append(", perfecto=").append(esPerfecto());
        representacion.append("}");
        return representacion.toString();
    }

    /**
     * Construye una representación visual de los discos de una torre.
     *
     * Pre:
     * - torre != null
     *
     * Post:
     * - Devuelve un String con los tamaños de los discos separados por comas,
     *   del tope (izquierda) al fondo (derecha).
     *
     * @param torre torre a representar
     * @return string con los discos de la torre
     */
    private String construirRepresentacionTorre(Pila<Integer> torre) {
        StringBuilder representacion = new StringBuilder();
        Nodo<Integer> actual = torre.getCabeza();
        boolean primero = true;

        while (actual != null) {
            if (!primero) {
                representacion.append(", ");
            }
            representacion.append(actual.getDato());
            actual = actual.getAbajo();
            primero = false;
        }

        return representacion.toString();
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