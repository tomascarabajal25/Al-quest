package juego.ciudades.grafos.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PasoCamino {
    private final String verticeActual;
    private final List<String> visitados;
    private final Map<String, Double> distancias;
    private final Map<String, String> predecesores;
    private final List<String> aristaRelajada;
    private final String descripcion;

    public PasoCamino(String verticeActual, List<String> visitados,
                      Map<String, Double> distancias, Map<String, String> predecesores,
                      List<String> aristaRelajada, String descripcion) {
        this.verticeActual = verticeActual;
        this.visitados = Collections.unmodifiableList(visitados);
        this.distancias = Collections.unmodifiableMap(new HashMap<>(distancias));
        this.predecesores = Collections.unmodifiableMap(new HashMap<>(predecesores));
        this.aristaRelajada = aristaRelajada != null ? Collections.unmodifiableList(aristaRelajada) : null;
        this.descripcion = descripcion;
    }

    public String getVerticeActual() {
        return verticeActual;
    }

    public List<String> getVisitados() {
        return visitados;
    }

    public Map<String, Double> getDistancias() {
        return distancias;
    }

    public Map<String, String> getPredecesores() {
        return predecesores;
    }

    public List<String> getAristaRelajada() {
        return aristaRelajada;
    }

    public String getDescripcion() {
        return descripcion;
    }
}
