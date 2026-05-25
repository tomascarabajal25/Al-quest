package ciudades.reinas;

import java.util.ArrayList;
import java.util.List;

public class SolverReinas {

    public boolean resolver(Tablero tablero, int fila, int filaJugador) {

        if (fila == tablero.getTamanio()) return true; // solución encontrada

        if (fila == filaJugador) {
            return resolver(tablero, fila + 1, filaJugador); // saltear fila del jugador
        }

        for (int col = 0; col < tablero.getTamanio(); col++) {

            if (tablero.esValido(fila, col)) {
                tablero.colocarReina(fila, col);

                if (resolver(tablero, fila + 1, filaJugador)) return true;

                tablero.quitarReina(fila); // backtrack
            }
        }
        return false;
    }

    public int[] obtenerSolucion (Tablero tableroOriginal, int filaJugador){
        Tablero copia = tableroOriginal.copiar();
        if (resolver (copia, 0, filaJugador)){
            return copia.getTodasLasReinas(); //devuelve el arreglo reinas[]
        }

        return null; //sin solucion
    }

    public List<Paso> grabarPasos (Tablero tablero, int filaJugador){
        List<Paso> pasos = new ArrayList<>();
        resolverGrabando (tablero, 0, filaJugador, pasos);
        return pasos;
    }

    private boolean resolverGrabando (Tablero tablero, int fila, int filaJugador, List<Paso> pasos){
        if (fila == tablero.getTamanio()){
            return true;
        }

        if (fila == filaJugador){
            return resolverGrabando (tablero, fila + 1, filaJugador, pasos);
        }

        for (int col = 0; col < tablero.getTamanio(); col ++){
            if (tablero.esValido(fila, col)){
                tablero.colocarReina(fila,col);
                pasos.add(new Paso (fila, col, Accion.COLOCAR));

                if (resolverGrabando(tablero, fila + 1, filaJugador, pasos)){
                    return true;
                }

                tablero.quitarReina(fila);
                pasos.add(new Paso (fila, col, Accion.QUITAR));
            }
        }

        return false;
    }

}
