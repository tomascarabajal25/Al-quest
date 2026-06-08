package juego.ciudades.reinas;

import java.util.ArrayList;
import java.util.List;

/**
 * Resuelve el problema de las N-Reinas usando backtracking.
 * Respeta las reinas ya colocadas en el tablero.
 */
public class SolverReinas {

    /**
     * Resuelve el tablero desde la fila indicada usando backtracking.
     * Saltea las filas que ya tienen reina colocada.
     *
     * @param tablero tablero sobre el que se resuelve
     * @param fila fila desde la que empieza a resolver
     * @return true si encontró solución, false si no
     */
    public boolean resolver(Tablero tablero, int fila) {

        if (fila == tablero.getTamanio()){
            return true; // solución encontrada
        } 

        if (tablero.getReinas(fila) != -1){
            return resolver(tablero, fila + 1);
        }

        for (int col = 0; col < tablero.getTamanio(); col++) {

            if (tablero.esValido(fila, col)) {

                tablero.colocarReina(fila, col);

                if (resolver(tablero, fila + 1)){
                    return true;
                }

                tablero.quitarReina(fila); // backtrack
            }
        }
        return false;
    }

    /**
     * Obtiene una solución válida sin modificar el tablero original.
     * Trabaja sobre una copia para preservar el estado.
     *
     * @param tableroOriginal tablero con las reinas ya colocadas por el jugador
     * @return arreglo con la columna de cada reina por fila, o null si no hay solución
     */
    public int[] obtenerSolucion (Tablero tableroOriginal){
        Tablero copia = tableroOriginal.copiar();
        if (resolver (copia, 0)){
            return copia.getTodasLasReinas(); //devuelve el arreglo reinas[]
        }

        return null; //sin solucion
    }

    /**
     * Resuelve el tablero grabando cada acción como un paso.
     * Usado para animar el backtracking en la UI.
     *
     * @param tablero tablero con las reinas ya colocadas por el jugador
     * @return lista de pasos COLOCAR/QUITAR en orden de ejecución
     */
    public List<Paso> grabarPasos (Tablero tablero){
        List<Paso> pasos = new ArrayList<>();
        resolverGrabando (tablero, 0, pasos);
        return pasos;
    }

    /**
     * Versión recursiva de grabarPasos que acumula los pasos en la lista.
     * Saltea las filas que ya tienen reina colocada.
     *
     * @param tablero tablero sobre el que se resuelve
     * @param fila fila actual del backtracking
     * @param pasos lista donde se acumulan los pasos
     * @return true si encontró solución, false si no
     */
    private boolean resolverGrabando (Tablero tablero, int fila, List<Paso> pasos){
        if (fila == tablero.getTamanio()){
            return true;
        }

        if (tablero.getReinas(fila) != -1){
            return resolverGrabando(tablero, fila + 1, pasos);
        }

        for (int col = 0; col < tablero.getTamanio(); col ++){
            if (tablero.esValido(fila, col)){
                tablero.colocarReina(fila,col);
                pasos.add(new Paso (fila, col, Accion.COLOCAR));

                if (resolverGrabando(tablero, fila + 1, pasos)){
                    return true;
                }

                tablero.quitarReina(fila);
                pasos.add(new Paso (fila, col, Accion.QUITAR));
            }
        }

        return false;
    }
}
