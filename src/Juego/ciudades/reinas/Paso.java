package Juego.ciudades.reinas;

/**
 * Representa un paso individual en la animación del backtracking.
 * Almacena la posición y la acción realizada (colocar o quitar una reina).
 */
public class Paso {
    private final int fila;
    private final int columna;
    private final Accion accion;

    /**
     * @param fila fila donde se realiza la acción
     * @param columna columna donde se realiza la acción
     * @param accion tipo de acción: COLOCAR o QUITAR
     */
    public Paso (int fila, int columna, Accion accion){
        this.fila = fila;
        this.columna = columna;
        this.accion = accion;
    }

    /** @return fila donde se realiza la acción */
    public int getFila() {
        return fila;
    }

    /** @return columna donde se realiza la acción */
    public int getColumna (){
        return columna;
    }

    /** @return acción realizada: COLOCAR o QUITAR */
    public Accion getAccion(){
        return accion;
    }
}
