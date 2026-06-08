package juego.ciudades.reinas;

public class Paso {
    private final int fila;
    private final int columna;
    private final Accion accion;

    public Paso (int fila, int columna, Accion accion){
        this.fila = fila;
        this.columna = columna;
        this.accion = accion;
    }

    public int getFila() {
        return fila;
    }

    public int getColumna (){
        return columna;
    }

    public Accion getAccion(){
        return accion;
    }
}
