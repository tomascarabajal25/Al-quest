package ciudadReinas;

import java.util.Arrays;

/*
    Ciudad 2 – Problema de las 4 Reinas

    Se deberá implementar una solución al problema de las N reinas utilizando backtracking o fuerza bruta.
    El jugador define la cantidad de reinas, una posición inicial y el sistema completa la solución,
    informando si existe o no una solución. El juego debe ser explicativo mediante los gráficos BMP -
    a medida que avance la solución.
*/

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

        for (int fil = 0; fil < fila; fil++) {
            int col = reinas[fil];

            if (col == columna) return false;     // misma columna

            if (Math.abs(fil - fila) == Math.abs(col - columna)){ // misma diagonal
                return false;
            }
        }
        return true;
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

    public void imprimirTablero() { //metodo solo para probar funcionalidad del tablero, se puede eliminar o modificar para mostrar el gráfico BMP
        for (int fila = 0; fila < tamanio; fila++) {
            for (int col = 0; col < tamanio; col++) {
                System.out.print(reinas[fila] == col ? " C " : " . ");
            }
            System.out.println();
        }
    }

    /*  EJEMPLO USO EN MAIN

            int tamanio = 8;    // tamaño del tablero (N x N)
            int fila = 2;       // fila inicial para colocar la primera reina
            int columna = 0;    // columna inicial para colocar la primera reina

            CiudadReinas ciudad = new CiudadReinas();
            ciudad.iniciarCiudad(tamanio, fila, columna);
            
            ciudad.ciudadIniciada(4);    // fila del jugador para que el solver la saltee
            ciudad.mostrarTablero();     // imprimir el tablero con la solución encontrada
    
    */

}


