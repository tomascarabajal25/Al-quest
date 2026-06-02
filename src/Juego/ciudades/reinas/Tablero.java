package Juego.ciudades.reinas;

import java.util.Arrays;

/**
 * Representa el tablero del problema de las N-Reinas.
 * Internamente usa un arreglo donde reinas[fila] = columna de la reina,
 * o -1 si la fila está vacía.
 */
public class Tablero {
    private int tamanio; // tamaño del tablero (N x N)
    private int[] reinas;   // reinas[fila] = columna donde está la reina
                            // usa -1 para indicar casilla vacía
                            // puede cambiarse a Integer[] para usar null en lugar de -1 (más legible)

    /**
     * Coloca una reina en la posición dada si es válida.
     *
     * @param fila fila donde colocar la reina
     * @param columna columna donde colocar la reina
     */
    public void colocarReina(int fila, int columna) {
        setReinas(fila, columna);
    }

    /**
     * Quita la reina de la fila indicada marcándola como vacía (-1).
     *
     * @param fila fila de la que quitar la reina
     */
    public void quitarReina(int fila) {
        if (esValido(fila)){
            reinas[fila] = -1; // marca la fila como vacía

        }
    }

    /**
     * Verifica que la fila esté dentro de los límites del tablero.
     *
     * @param fila fila a verificar
     * @return true si la fila es válida
     * @throws IllegalArgumentException si la fila está fuera de los límites
     */
    public boolean esValido(int fila){
        if (fila < 0 || fila > tamanio - 1){
            throw new IllegalArgumentException("Fila fuera de los límites del tablero");
        }
        return true;
    }

    /**
     * Verifica si colocar una reina en (fila, columna) es válido.
     * Chequea que no haya conflictos de columna ni diagonal con las demás reinas.
     *
     * @param fila fila a verificar
     * @param columna columna a verificar
     * @return true si la posición es válida, false si hay conflicto
     * @throws IllegalArgumentException si la posición está fuera de los límites
     */
    public boolean esValido(int fila, int columna) {
        if (fila < 0 || fila > tamanio - 1 || columna < 0 || columna > tamanio - 1)
            throw new IllegalArgumentException("Posición fuera de los límites del tablero");

        for (int fil = 0; fil < tamanio; fil++) {  
            if (fil == fila) continue;

            int col = reinas[fil];

            if (col == -1) continue;    //saltear filas vacias

            if (col == columna) return false;     // misma columna

            if (Math.abs(fil - fila) == Math.abs(col - columna)){ // misma diagonal
                return false;
            }
        }
        return true;
    }

    /**
     * Crea una copia independiente del tablero con el mismo estado.
     *
     * @return nuevo tablero con las mismas reinas colocadas
     */
    public Tablero copiar(){
        Tablero copia = new Tablero();
        copia.setTamanio(this.tamanio);
        for (int i = 0; i < tamanio; i++){
            if (reinas[i] != -1){
                copia.setReinas(i, reinas[i]);
            }
        }
        return copia;
    }

    /**
     * @return copia del arreglo interno con la columna de cada reina por fila,
     *         o -1 en las filas vacías
     */
    public int[] getTodasLasReinas(){
        return Arrays.copyOf (reinas,tamanio);
    }

    /** @return dimensión N del tablero */
    public int getTamanio() {
        return tamanio;
    }

    /**
     * Establece el tamaño del tablero y reinicia todas las filas a vacío (-1).
     *
     * @param tamanio dimensión N del tablero, debe ser al menos 1
     * @throws IllegalArgumentException si tamanio es menor a 1
     */
    public void setTamanio(int tamanio) {
        if (tamanio < 1) throw new IllegalArgumentException("El tamaño debe ser al menos 1");
        this.tamanio = tamanio;
        this.reinas = new int[tamanio]; // inicializa el arreglo de reinas
        Arrays.fill(reinas, -1);  // sin esto todas las posiciones arrancan en 0
    }

    /**
     * @param fila fila a consultar
     * @return columna donde está la reina en esa fila, o -1 si está vacía
     */
    public int getReinas (int fila) {
        return reinas[fila]; // devuelve la columna donde está la reina en la fila dada
    }

    /**
     * Coloca una reina en (fila, columna) si la posición es válida.
     * No hace nada si hay conflicto con otra reina.
     *
     * @param fila fila donde colocar
     * @param columna columna donde colocar
     */
    public void setReinas (int fila, int columna) {
        if (esValido(fila, columna)){
            reinas[fila] = columna;
        }
        
    }

}


