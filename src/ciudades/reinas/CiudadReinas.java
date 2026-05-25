package ciudades.reinas;

import java.util.List;

public class CiudadReinas {
    
    private SolverReinas solver = new SolverReinas();
    private Tablero tablero = new Tablero();
    private int filajugador;
    private boolean tieneSolucion;
    
    public boolean iniciarCiudad(int tamanio, int fila, int columna){
        
        tablero.setTamanio(tamanio);
        tablero.setReinas(fila, columna);
        this.filajugador = fila;

        tieneSolucion = solver.obtenerSolucion(tablero.copiar(), fila) != null;
        return tieneSolucion;
    }

    public boolean validarTableroJugador(int[][] tableroJugador) {
    int[] columnas = new int[tablero.getTamanio()];

    // exactamente una reina por fila
    for (int fila = 0; fila < tablero.getTamanio(); fila++) {
        int reinasEnFila = 0;
        for (int col = 0; col < tablero.getTamanio(); col++) {
            if (tableroJugador[fila][col] == 1) {
                reinasEnFila++;
                columnas[fila] = col;
            }
        }
        if (reinasEnFila != 1) return false;
    }

    // ninguna se ataca entre sí
    for (int i = 0; i < tablero.getTamanio(); i++) {
        for (int j = i + 1; j < tablero.getTamanio(); j++) {
            if (columnas[i] == columnas[j]) return false;
            if (Math.abs(i - j) == Math.abs(columnas[i] - columnas[j])) return false;
        }
    }

    return true;
}

    public List<Paso> obtenerPasos(){
        Tablero copia = tablero.copiar();
        return solver.grabarPasos(tablero.copiar(), filajugador);
    }
    
    public void mostrarTablero(){   //metodo provisorio, se puede quitar mas adelante
        tablero.imprimirTablero();
    }

}
