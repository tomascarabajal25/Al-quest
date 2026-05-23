package material.estructuras.grafos.algoritmos.floyd;

public class AlgoritmoFloydMarshall {
    static final int INFINITO = 99999; // Representa infinito

    public static void floydWarshall(int[][] grafo) {
        int n = grafo.length;
        int[][] distancias = new int[n][n];

        // Inicializamos la matriz de distancias con los valores del grafo
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                distancias[i][j] = grafo[i][j];
            }
        }

        // Aplicamos el algoritmo
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    // Si el camino a través de k es más corto, actualizamos
                    if (distancias[i][k] + distancias[k][j] < distancias[i][j]) {
                        distancias[i][j] = distancias[i][k] + distancias[k][j];
                    }
                }
            }
        }

        // Imprimimos el resultado
        imprimirSoluciones(distancias);
    }

    public static void imprimirSoluciones(int[][] dist) {
        System.out.println("Matriz de distancias más cortas:");
        for (int[] row : dist) {
            for (int val : row) {
                if (val == INFINITO) {
                    System.out.print("INF ");
                } else {
                    System.out.print(val + " ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][] graph = {
            {0, 3, INFINITO, 7},
            {8, 0, 2, INFINITO},
            {5, INFINITO, 0, 1},
            {2, INFINITO, INFINITO, 0}
        };

        floydWarshall(graph);
    }
}