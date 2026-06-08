package juego.ciudades.reinas;

import java.util.Arrays;

public class Tablero {
    private int tamanio; // tamaño del tablero (N x N)
    private int[] reinas;   // reinas[fila] = columna donde está la reina
                            // usa -1 para indicar casilla vacía
                            // puede cambiarse a Integer[] para usar null en lugar de -1 (más legible)

    public Tablero() {
        // vacío por ahora, setTamanio hace el trabajo
    }

    public void colocarReina(int fila, int columna) {
        setReinas(fila, columna);
    }

    public void quitarReina(int fila) {
        if (esValido(fila)){
            reinas[fila] = -1; // marca la fila como vacía

        }
    }

    public boolean esValido(int fila){
        if (fila < 0 || fila > tamanio - 1){
            throw new IllegalArgumentException("Fila fuera de los límites del tablero");
        }
        return true;
    }

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

    public int[] getTodasLasReinas(){
        return Arrays.copyOf (reinas,tamanio);
    }

    public int getTamanio() {
        return tamanio;
    }

    public void setTamanio(int tamanio) {
        if (tamanio < 1) throw new IllegalArgumentException("El tamaño debe ser al menos 1");
        this.tamanio = tamanio;
        this.reinas = new int[tamanio]; // inicializa el arreglo de reinas
        Arrays.fill(reinas, -1);  // sin esto todas las posiciones arrancan en 0
    }

    public int getReinas (int fila) {
        return reinas[fila]; // devuelve la columna donde está la reina en la fila dada
    }

    public void setReinas (int fila, int columna) {
        if (esValido(fila, columna)){
            reinas[fila] = columna;
        }
        
    }

}


