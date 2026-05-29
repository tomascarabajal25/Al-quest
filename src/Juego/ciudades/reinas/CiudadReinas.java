package Juego.ciudades.reinas;

import java.util.List;

/**
 * Lógica principal de la Ciudad Reinas.
 * Coordina el tablero, el solver y la validación del jugador.
 */
public class CiudadReinas {
    
    private SolverReinas solver = new SolverReinas();
    private Tablero tablero = new Tablero();
    private boolean tieneSolucion;
    
    /**
     * Inicializa el tablero con el tamaño dado y coloca la reina inicial.
     * Verifica si existe al menos una solución posible desde esa posición.
     *
     * @param tamanio dimensión del tablero (N x N)
     * @param fila fila de la reina inicial
     * @param columna columna de la reina inicial
     * @return true si existe solución posible, false si no
     */
    public boolean iniciarCiudad(int tamanio, int fila, int columna){
        
        tablero.setTamanio(tamanio);
        tablero.setReinas(fila, columna);
        tieneSolucion = solver.obtenerSolucion(tablero.copiar()) != null;
        return tieneSolucion;
    }

    /**
     * Valida si el tablero del jugador es una solución correcta.
     * Verifica que haya exactamente una reina por fila y que ninguna se ataque.
     *
     * @param tableroJugador matriz NxN donde 1 indica reina y 0 casilla vacía
     * @return true si el tablero es una solución válida, false si no
     */
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

    /**
     * Sincroniza el tablero interno con el estado actual del jugador.
     * Garantiza que la reina inicial siempre se procese primero.
     * Las reinas que generen conflicto son ignoradas.
     *
     * @param tableroJugador matriz NxN con el estado actual del jugador
     * @param filaInicial fila de la reina inicial fija
     * @param colInicial columna de la reina inicial fija
     */
    public void actualizarTableroJugador(int[][] tableroJugador, int filaInicial, int colInicial) {
        tablero.setTamanio(tablero.getTamanio());
        tablero.setReinas(filaInicial, colInicial);

        for (int fila = 0; fila < tablero.getTamanio(); fila++) {
            if (fila == filaInicial) continue;      
            for (int col = 0; col < tablero.getTamanio(); col++) {
                if (tableroJugador[fila][col] == 1) {
                    tablero.setReinas(fila, col);
                }
            }
        }
    }

    /**
     * Graba y devuelve los pasos del backtracking para animar la solución.
     * Respeta las reinas ya colocadas en el tablero.
     *
     * @return lista de pasos para animar, o null si no hay solución posible
     */
    public List<Paso> obtenerPasos() {
        if (solver.obtenerSolucion(tablero.copiar()) == null) {
            return null; // no hay solución posible desde este estado
        }
        return solver.grabarPasos(tablero.copiar());
    }

    /**
     * @return arreglo con la columna de cada reina por fila, o -1 si la fila está vacía
     */
    public int[] getReinasTablero(){
        return tablero.getTodasLasReinas();
    }

}
