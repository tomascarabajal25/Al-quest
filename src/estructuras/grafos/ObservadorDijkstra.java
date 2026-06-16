package estructuras.grafos;

import java.util.Map;

public interface ObservadorDijkstra<T> {
    void onVerticeExtraido(T vertice, Map<T, Double> distancias);
    void onAristaRelajada(T origen, T destino, double nuevaDist);
}
