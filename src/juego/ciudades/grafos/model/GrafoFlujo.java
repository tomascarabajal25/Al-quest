package juego.ciudades.grafos.model;

import estructuras.grafos.Arista;
import estructuras.grafos.Grafo;
import estructuras.grafos.ObservadorDijkstra;
import estructuras.grafos.algoritmos.fordFulkerson.FordFulkerson;
import estructuras.grafos.algoritmos.fordFulkerson.ObservadorFlujo;

import java.util.*;

public class GrafoFlujo {
    private Grafo<String, Integer> grafo;
    private List<String> vertices;
    private String fuente;
    private String sumidero;
    private EstadoCiudad7 estado;

    public GrafoFlujo() {
        this.grafo = new Grafo<>();
        this.vertices = new ArrayList<>();
        this.estado = EstadoCiudad7.CONSTRUYENDO;
    }

    public void reset() {
        this.grafo = new Grafo<>();
        this.vertices = new ArrayList<>();
        this.fuente = null;
        this.sumidero = null;
        this.estado = EstadoCiudad7.CONSTRUYENDO;
    }

    public void agregarVertice(String nombre) {
        if (nombre == null || nombre.isBlank()) return;
        if (!grafo.existeVertice(nombre)) {
            grafo.agregarVertice(nombre);
            vertices.add(nombre);
        }
    }

    public void agregarArista(String origen, String destino, int capacidad) {
        if (capacidad <= 0) return;
        if (!grafo.existeVertice(origen) || !grafo.existeVertice(destino)) return;
        grafo.agregarArista(origen, destino, capacidad);
    }

    public void setFuente(String fuente) {
        if (grafo.existeVertice(fuente)) this.fuente = fuente;
    }

    public void setSumidero(String sumidero) {
        if (grafo.existeVertice(sumidero)) this.sumidero = sumidero;
    }

    public String getFuente() { return fuente; }
    public String getSumidero() { return sumidero; }
    public Grafo<String, Integer> getGrafo() { return grafo; }
    public List<String> getVertices() { return Collections.unmodifiableList(vertices); }
    public EstadoCiudad7 getEstado() { return estado; }
    public void setEstado(EstadoCiudad7 estado) { this.estado = estado; }

    public int[][] getMatrizCapacidades() {
        int n = vertices.size();
        int[][] matriz = new int[n][n];
        for (int i = 0; i < n; i++) {
            String u = vertices.get(i);
            for (Arista<String, Integer> arista : grafo.getAdyacentes(u)) {
                int j = vertices.indexOf(arista.getDestino().getValor());
                if (j >= 0) {
                    matriz[i][j] = arista.getPeso();
                }
            }
        }
        return matriz;
    }

    public int indiceDe(String vertice) {
        return vertices.indexOf(vertice);
    }

    public String verticeEn(int indice) {
        if (indice < 0 || indice >= vertices.size()) return null;
        return vertices.get(indice);
    }

    public boolean estaCompleto() {
        return vertices.size() >= 2
                && fuente != null
                && sumidero != null
                && !fuente.equals(sumidero);
    }

    public ResultadoFlujo resolverFlujoMaximo() {
        if (!estaCompleto()) return null;

        int s = indiceDe(fuente);
        int t = indiceDe(sumidero);
        int[][] capacidad = getMatrizCapacidades();

        List<PasoFlujo> pasos = new ArrayList<>();
        final int[] flujoAcumulado = {0};

        ObservadorFlujo observador = (camino, cuelloDeBotella, flujoActual) -> {
            flujoAcumulado[0] += cuelloDeBotella;

            List<String> caminoNombres = new ArrayList<>();
            for (int idx : camino) {
                caminoNombres.add(verticeEn(idx));
            }

            String desc = "Camino aumentante: " + String.join(" → ", caminoNombres)
                    + " | Cuello de botella: " + cuelloDeBotella
                    + " | Flujo acumulado: " + flujoAcumulado[0];

            pasos.add(new PasoFlujo(caminoNombres, cuelloDeBotella, flujoActual, desc));
            return true;
        };

        int flujoMaximo = FordFulkerson.fordFulkerson(capacidad, s, t, observador);

        estado = EstadoCiudad7.EJECUTANDO_FLUJO;
        int[][] flujoFinal = pasos.isEmpty() ? new int[capacidad.length][capacidad.length] : pasos.get(pasos.size() - 1).getFlujoDespues();
        return new ResultadoFlujo(flujoMaximo, pasos, flujoFinal);
    }

    public ResultadoCamino resolverCaminoMinimo(String origen, String destino) {
        if (!grafo.existeVertice(origen) || !grafo.existeVertice(destino)) return null;

        List<PasoCamino> pasos = new ArrayList<>();
        Set<String> visitados = new LinkedHashSet<>();
        Map<String, String> predecesores = new HashMap<>();

        Map<String, Double> distanciasInicial = new HashMap<>();
        for (String v : vertices) {
            distanciasInicial.put(v, Double.MAX_VALUE);
        }
        distanciasInicial.put(origen, 0.0);

        pasos.add(new PasoCamino(origen, new ArrayList<>(visitados),
                distanciasInicial, predecesores, null,
                "Inicialización: distancia(" + origen + ") = 0, resto = ∞"));

        ObservadorDijkstra<String> observador = new ObservadorDijkstra<String>() {
            @Override
            public void onVerticeExtraido(String vertice, Map<String, Double> distancias) {
                visitados.add(vertice);
                pasos.add(new PasoCamino(vertice, new ArrayList<>(visitados),
                        distancias, predecesores, null,
                        "Extraer " + vertice + " (dist=" + formatoDist(distancias.get(vertice)) + ")"));
            }

            @Override
            public void onAristaRelajada(String origen, String destino, double nuevaDist) {
                predecesores.put(destino, origen);
                Map<String, Double> distSnapshot = new HashMap<>();
                for (String v : vertices) {
                    distSnapshot.put(v, Double.MAX_VALUE);
                }
                if (pasos.size() > 0) {
                    distSnapshot.putAll(pasos.get(pasos.size() - 1).getDistancias());
                }
                distSnapshot.put(destino, nuevaDist);

                pasos.add(new PasoCamino(origen, new ArrayList<>(visitados),
                        distSnapshot, predecesores, List.of(origen, destino),
                        "Relajar arista " + origen + " → " + destino + ": dist(" + destino + ") = " + nuevaDist));
            }
        };

        List<String> camino = grafo.dijkstra(origen, destino, observador);

        double costoTotal = camino.isEmpty() ? Double.MAX_VALUE : 0.0;
        if (!camino.isEmpty()) {
            for (int i = 0; i < camino.size() - 1; i++) {
                for (Arista<String, Integer> arista : grafo.getAdyacentes(camino.get(i))) {
                    if (arista.getDestino().getValor().equals(camino.get(i + 1))) {
                        costoTotal += arista.getPeso();
                        break;
                    }
                }
            }
        }

        estado = EstadoCiudad7.EJECUTANDO_CAMINO;
        return new ResultadoCamino(camino, costoTotal, pasos);
    }

    private String formatoDist(double d) {
        if (d == Double.MAX_VALUE) return "∞";
        if (d == (int) d) return String.valueOf((int) d);
        return String.valueOf(d);
    }
}
