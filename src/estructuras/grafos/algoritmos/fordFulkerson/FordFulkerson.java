package material.estructuras.grafos.algoritmos.fordFulkerson;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class FordFulkerson {
    // Método para realizar una búsqueda en profundidad (DFS) para encontrar un camino aumentante
    private static boolean dfs(int[][] residualGraph, int source, int sink, int[] parent) {
        int numVertices = residualGraph.length;
        boolean[] visited = new boolean[numVertices];
        Arrays.fill(visited, false);

        // Utilizamos una pila para realizar el DFS
        Stack<Integer> stack = new Stack<>();
        stack.push(source);
        visited[source] = true;

        while (!stack.isEmpty()) {
            int u = stack.pop();

            // Recorremos todos los nodos adyacentes
            for (int v = 0; v < numVertices; v++) {
                // Si no está visitado y hay capacidad residual
                if (!visited[v] && residualGraph[u][v] > 0) {
                    parent[v] = u; // Guardamos el camino
                    if (v == sink) {
                        return true; // Llegamos al sumidero
                    }
                    stack.push(v);
                    visited[v] = true;
                }
            }
        }
        return false;
    }

    // Implementación del algoritmo de Ford-Fulkerson
    public static int fordFulkerson(int[][] graph, int source, int sink) {
        int numVertices = graph.length;

        // Crear el grafo residual
        int[][] residualGraph = new int[numVertices][numVertices];
        for (int u = 0; u < numVertices; u++) {
            for (int v = 0; v < numVertices; v++) {
                residualGraph[u][v] = graph[u][v];
            }
        }

        // Array para guardar el camino aumentante
        int[] parent = new int[numVertices];

        int maxFlow = 0; // Inicializamos el flujo máximo en 0

        // Mientras exista un camino aumentante
        while (dfs(residualGraph, source, sink, parent)) {
            // Determinar la capacidad mínima en el camino aumentante
            int pathFlow = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, residualGraph[u][v]);
            }

            // Actualizar las capacidades residuales
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residualGraph[u][v] -= pathFlow; // Reducimos en la dirección original
                residualGraph[v][u] += pathFlow; // Aumentamos en la dirección opuesta
            }

            // Añadir el flujo de este camino al flujo máximo
            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    public static void main(String[] args) {
        // Ejemplo de grafo representado como matriz de adyacencia
        int[][] graph = {
            {0, 16, 13, 0, 0, 0},
            {0, 0, 10, 12, 0, 0},
            {0, 4, 0, 0, 14, 0},
            {0, 0, 9, 0, 0, 20},
            {0, 0, 0, 7, 0, 4},
            {0, 0, 0, 0, 0, 0}
        };

        int source = 0; // Nodo fuente
        int sink = 5;   // Nodo sumidero
        System.out.println("El flujo máximo es: " + fordFulkerson(graph, source, sink));
    }
}
