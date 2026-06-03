package com.aiquest.estructuras.grafos.algoritmos.ciclos;

public class CicloHamiltoniano {

    private final int V; // Número de vértices
    private final int[][] grafo; // Matriz de adyacencia
    private final int[] path; // Arreglo para almacenar el camino/ciclo

    /**
     * Constructor.
     * @param grafo Matriz de adyacencia del grafo.
     */
    public CicloHamiltoniano(int[][] grafo) {
        this.grafo = grafo;
        this.V = grafo.length;
        this.path = new int[V];
        // Inicializar el camino: -1 indica que no hay vértice asignado
        for (int i = 0; i < V; i++) {
            path[i] = -1;
        }
    }

    /**
     * Método principal para iniciar la búsqueda del Ciclo Hamiltoniano.
     * @return true si se encuentra un ciclo, false en caso contrario.
     */
    public boolean buscarCicloHamiltoniano() {
        // Empezamos siempre desde el vértice 0 (path[0])
        path[0] = 0; 
        
        // La búsqueda comienza desde la posición 1 del camino (path[1])
        if (!resolver(1)) {
            System.out.println("\nNo existe un Ciclo Hamiltoniano en el grafo.");
            return false;
        }
        
        imprimirSolucion();
        return true;
    }

    /**
     * Función recursiva de backtracking para construir el camino.
     * @param k La posición actual en el arreglo 'path' que se está intentando llenar.
     * @return true si se puede completar un ciclo desde esta posición, false si se necesita hacer backtracking.
     */
    private boolean resolver(int k) {
        // Caso base: El camino está completo (se han colocado V vértices).
        if (k == V) {
            // Comprobación final: el último vértice debe estar conectado al primero (0)
            if (grafo[path[k - 1]][path[0]] == 1) {
                return true; // ¡Ciclo encontrado!
            } else {
                return false; // Es un camino, pero no un ciclo
            }
        }

        // Probar todos los vértices como la próxima opción para path[k]
        for (int v = 1; v < V; v++) {
            if (esMovimientoSeguro(v, k)) {
                
                // 1. Asignar: Probar el vértice v en la posición k
                path[k] = v;

                // 2. Llamada recursiva: Intentar completar el resto del camino
                if (resolver(k + 1)) {
                    return true;
                }

                // 3. Backtrack: Si la llamada recursiva falla, deshacer la asignación.
                path[k] = -1;
            }
        }

        return false; // No se encontró ningún vértice válido para la posición k.
    }

    /**
     * Comprueba si el vértice 'v' puede ser el próximo en 'path[k]'.
     * Debe cumplir dos condiciones: adyacencia y no estar ya en el camino.
     * @param v El vértice candidato.
     * @param k La posición actual en el camino.
     * @return true si es seguro moverse a 'v', false en caso contrario.
     */
    private boolean esMovimientoSeguro(int v, int k) {
        // Condición 1: El vértice 'v' debe ser adyacente al vértice anterior (path[k-1])
        if (grafo[path[k - 1]][v] == 0) {
            return false;
        }

        // Condición 2: El vértice 'v' no debe estar ya en el camino path[0...k-1]
        for (int i = 0; i < k; i++) {
            if (path[i] == v) {
                return false;
            }
        }

        return true;
    }

    /**
     * Imprime el Ciclo Hamiltoniano encontrado.
     */
    private void imprimirSolucion() {
        System.out.println("✅ Se encontró un Ciclo Hamiltoniano:");
        System.out.print("Camino: ");
        for (int i = 0; i < V; i++) {
            System.out.print(path[i] + " -> ");
        }
        // Imprimir el vértice inicial de nuevo para cerrar el ciclo
        System.out.println(path[0]);
    }

    // --- Método Main de Ejemplo ---
    public static void main(String[] args) {
        
        // Ejemplo de un grafo con 5 vértices que SÍ tiene un Ciclo Hamiltoniano (0-1-4-3-2-0)
        int[][] ejemploGrafo1 = {
            // 0 1 2 3 4
            {0, 1, 0, 0, 1}, // 0
            {1, 0, 1, 0, 0}, // 1
            {0, 1, 0, 1, 0}, // 2
            {0, 0, 1, 0, 1}, // 3
            {1, 0, 0, 1, 0}  // 4
        };
        
        System.out.println("--- Búsqueda en Grafo 1 (Con Ciclo) ---");
        CicloHamiltoniano ch1 = new CicloHamiltoniano(ejemploGrafo1);
        ch1.buscarCicloHamiltoniano();

        System.out.println("\n----------------------------------------");

        // Ejemplo de un grafo con 4 vértices (que NO tiene un Ciclo Hamiltoniano)
        int[][] ejemploGrafo2 = {
            // 0 1 2 3
            {0, 1, 1, 0}, // 0
            {1, 0, 0, 1}, // 1
            {1, 0, 0, 1}, // 2
            {0, 1, 1, 0}  // 3
        };

        System.out.println("--- Búsqueda en Grafo 2 (Sin Ciclo) ---");
        CicloHamiltoniano ch2 = new CicloHamiltoniano(ejemploGrafo2);
        ch2.buscarCicloHamiltoniano();
    }
}