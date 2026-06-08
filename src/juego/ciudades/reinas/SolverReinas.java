package juego.ciudades.reinas;

import java.util.ArrayList;
import java.util.List;

public class SolverReinas {

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

    public int[] obtenerSolucion (Tablero tableroOriginal){
        Tablero copia = tableroOriginal.copiar();
        if (resolver (copia, 0)){
            return copia.getTodasLasReinas(); //devuelve el arreglo reinas[]
        }

        return null; //sin solucion
    }

    public List<Paso> grabarPasos (Tablero tablero){
        List<Paso> pasos = new ArrayList<>();
        resolverGrabando (tablero, 0, pasos);
        return pasos;
    }

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
