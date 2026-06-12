package juego.ciudades.ciudad_3_laberinto.src;

public class Celda {

    /**
    * Representacion de una celda en una grilla y su estado
    */
    private int fila;
    private int columna;
    private EstadoCelda estado;

    /*
    * Constructor de la celda
    * Sin setters para fila y columna ya que estos no deben cambiar.
    * Hay setter de estado de celda pues este cambiara durante su recorrido.
    */
    
    public Celda(int fila, int columna, EstadoCelda estado) {
        this.fila = fila;
        this.columna = columna;
        setEstadoCelda(estado);
    }

    // VERIFICADOR DE ESTADO

    // true si la celda es una pared
    public boolean esPared() {
        return estado == EstadoCelda.PARED;
    }

    /*
    * Verifica que el estado de la celda este libre o forme parte de sus extremos.
    * true si la celda no es pared
    */
    public boolean esTransitable() {
        return estado == EstadoCelda.LIBRE
        || estado == EstadoCelda.INICIO
        || estado == EstadoCelda.FIN;
    }

    // GETTERS

    /* devuelve la fila de la celda */

    public int getFila() {
        return fila;
    }

    /* devuelve la columna de la celda */

    public int getColumna() {
        return columna;
    }

    /* devuelve el estado actual de la celda */

    public EstadoCelda getEstadoCelda() {
        return estado;
    }

    // SETTERS

    public void setEstadoCelda(EstadoCelda estado) {
        this.estado = estado;
    }
}
