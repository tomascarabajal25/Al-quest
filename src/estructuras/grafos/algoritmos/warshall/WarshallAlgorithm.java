package material.estructuras.grafos.algoritmos.warshall;

public class WarshallAlgorithm {
	
    public static void warshall(int[][] grafo) {
        int n = grafo.length;
        int[][] caminos = new int[n][n];

        // Inicializamos la matriz de cierre transitivo con la matriz de adyacencia
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                caminos[i][j] = grafo[i][j];
            }
        }

        // Aplicamos el algoritmo
        for (int k = 0; k < n; k++) { // Nodo intermedio
            for (int i = 0; i < n; i++) { // Nodo de origen
                for (int j = 0; j < n; j++) { // Nodo de destino
                    caminos[i][j] = caminos[i][j] | (caminos[i][k] & caminos[k][j]);
                }
            }
        }

        // Imprimimos la matriz de cierre transitivo
        imprimirMatriz(caminos);
    }

    public static void imprimirMatriz(int[][] matrix) {
        System.out.println("Cerradura transitiva:");
        for (int[] row : matrix) {
            for (int val : row) {
                System.out.print(val + " ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        int[][] graph = {
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1},
            {0, 0, 0, 0}
        };

        warshall(graph);
    }
}