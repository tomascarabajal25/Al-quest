package estructuras.grafos.algoritmos.prim;

import java.util.Arrays;

public class Prim {
    static int findMinVertex(int[] key, boolean[] mstSet, int V) {
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int v = 0; v < V; v++) {
            if (!mstSet[v] && key[v] < min) {
                min = key[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    static void primMST(int[][] graph, int V) {
        int[] parent = new int[V]; // Para almacenar el MST
        int[] key = new int[V];   // Para rastrear los pesos mínimos
        boolean[] mstSet = new boolean[V]; // Nodos incluidos en el MST

        // Inicializar todas las claves como infinito y el MST como falso
        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(mstSet, false);

        key[0] = 0;     // Comenzar con el primer nodo
        parent[0] = -1; // El nodo raíz no tiene padre

        for (int count = 0; count < V - 1; count++) {
            int u = findMinVertex(key, mstSet, V); // Obtener el nodo con la clave mínima
            mstSet[u] = true;                      // Incluir este nodo en el MST

            // Actualizar las claves y padres de los nodos adyacentes
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && !mstSet[v] && graph[u][v] < key[v]) {
                    parent[v] = u;
                    key[v] = graph[u][v];
                }
            }
        }

        // Imprimir el MST
        printMST(parent, graph, V);
    }

    static void printMST(int[] parent, int[][] graph, int V) {
        System.out.println("Árbol abarcador de coste mínimo (Prim):");
        System.out.println("Arista\tPeso");
        for (int i = 1; i < V; i++) {
            System.out.println(parent[i] + " - " + i + "\t" + graph[i][parent[i]]);
        }
    }

    public static void main(String[] args) {
        int[][] graph = {
            { 0,  2, 10,  0, 0,  0},
            { 2,  0, 60,  4, 0,  0},
            {10, 60,  0, 50, 5, 20},
            { 0,  4, 50,  0, 0, 30},
            { 0,  0,  5,  0, 0, 15},
            { 0,  0, 20, 30, 15, 0}
        };

        int V = graph.length;
        primMST(graph, V);
    }
}
