package material.estructuras.grafos.algoritmos.kruskal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Kruskal {
    static int find(int[] parent, int v) {
        if (parent[v] != v)
            parent[v] = find(parent, parent[v]); // Compresión de camino
        return parent[v];
    }

    static void union(int[] parent, int[] rank, int u, int v) {
        int rootU = find(parent, u);
        int rootV = find(parent, v);

        if (rank[rootU] < rank[rootV]) {
            parent[rootU] = rootV;
        } else if (rank[rootU] > rank[rootV]) {
            parent[rootV] = rootU;
        } else {
            parent[rootV] = rootU;
            rank[rootU]++;
        }
    }

    static void kruskalMST(int[][] graph, int V) {
        List<Edge> edges = new ArrayList<Edge>();

        // Convertimos la matriz de adyacencia en una lista de aristas
        for (int i = 0; i < V; i++) {
            for (int j = i + 1; j < V; j++) {
                if (graph[i][j] != 0) {
                    edges.add(new Edge(i, j, graph[i][j]));
                }
            }
        }

        Collections.sort(edges); // Ordenar aristas por peso

        int[] parent = new int[V];
        int[] rank = new int[V];
        for (int i = 0; i < V; i++) {
            parent[i] = i; // Inicializar cada nodo como su propio conjunto
            rank[i] = 0;
        }

        List<Edge> mst = new ArrayList<>();
        for (Edge edge : edges) {
            int u = find(parent, edge.src);
            int v = find(parent, edge.dest);

            if (u != v) { // Si no forman ciclo
                mst.add(edge);
                union(parent, rank, u, v);
            }

            if (mst.size() == V - 1) break; // Detenerse cuando el MST está completo
        }

        // Imprimir el MST
        System.out.println("Árbol abarcador de coste mínimo:");
        for (Edge edge : mst) {
            System.out.println(edge.src + " - " + edge.dest + " : " + edge.weight);
        }
    }

    public static void main(String[] args) {
        int[][] graph = {
            {0, 2, 0, 6, 0},
            {2, 0, 3, 8, 5},
            {0, 3, 0, 0, 7},
            {6, 8, 0, 0, 9},
            {0, 5, 7, 9, 0}
        };

        int V = graph.length;
        kruskalMST(graph, V);
    }
}