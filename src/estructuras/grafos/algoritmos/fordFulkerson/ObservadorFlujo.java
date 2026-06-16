package estructuras.grafos.algoritmos.fordFulkerson;

public interface ObservadorFlujo {
    boolean onCaminoAumentante(int[] camino, int cuelloDeBotella, int[][] flujoActual);
}
