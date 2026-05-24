package ciudades.reinas;

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

    public int[] obtenerSolucion (Tablero tableroOriginal, int filajugador){
        Tablero copia = tableroOriginal.copiar();
        if (resolver (copia, 0, filajugador)){
            return copia.getTodasLasReinas(); //devuelve el arreglo reinas[]
        }

        return null; //sin solucion
    }

}
