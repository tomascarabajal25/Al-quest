package juego.ciudades.ciudad_3_laberinto.src;

public class Celda {

    /**
    * Representacion de una celda en una grilla y su estado
    */
    private int fila;
    private int columna;
    private EstadoCelda estado;

    /**
     * Constructor de Celda.
     * @param fila    fila en la grilla, base 0
     * @param columna columna en la grilla, base 0
     * @param estado  estado inicial de la celda
     */
    public Celda(int fila, int columna, EstadoCelda estado) {
        this.fila = fila;
        this.columna = columna;
        setEstadoCelda(estado);
    }

    /**
     * Verificador de estado
     * Indica si la celda es una pared infranqueable.
     * @return true si el estado es PARED
     */
    public boolean esPared() {
        return estado == EstadoCelda.PARED;
    }

    /**
     * Indica si el algoritmo puede pasar por esta celda.
     * Una celda es transitable si su estado es LIBRE, INICIO o FIN.
     * @return true si la celda es transitable
     */
    public boolean esTransitable() {
        return estado == EstadoCelda.LIBRE
        || estado == EstadoCelda.INICIO
        || estado == EstadoCelda.FIN;
    }

    // GETTERS

    /** @return la fila de esta celda en la grilla */
    public int getFila() {
        return fila;
    }

    /** @return la columna de esta celda en la grilla */
    public int getColumna() {
        return columna;
    }

    /** @return el estado actual de esta celda */
    public EstadoCelda getEstadoCelda() {
        return estado;
    }

    // SETTERS
    /**
     * Cambia el estado de la celda.
     * @param estado nuevo estado a asignar
     */
    public void setEstadoCelda(EstadoCelda estado) {
        this.estado = estado;
    }
}
