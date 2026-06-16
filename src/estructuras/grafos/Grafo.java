package estructuras.grafos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * TDA Grafo (Dirigido y Ponderado)
 * Implementado con una "lista de vértices" (un Map para eficiencia)
 * donde cada vértice tiene su "sublista de aristas".
 *
 * @param <T> El tipo del valor almacenado en los vértices (ej. String, Integer)
 * @param <U> El tipo del peso almacenado en las aristas (ej. Integer, Double)
 */
public class Grafo<T, U> {

    // Esta es tu "lista de vértices", implementada como un Map
    // para búsquedas eficientes O(1) usando el valor T.
    private List<Vertice<T, U>> vertices;

 // Variables miembro para el algoritmo de Puntos de Articulación
    private int tiempoAP; 
    private Map<Vertice<T, U>, Integer> disc; // Tiempo de descubrimiento
    private Map<Vertice<T, U>, Integer> low;  // Low-link
    private Map<Vertice<T, U>, Vertice<T, U>> parent;
    private Set<Vertice<T, U>> puntosArticulacion;
    
    
    public Grafo() {
        this.vertices = new ArrayList<>();
    }

    /**
     * Construye un Grafo<String, Integer> a partir de representaciones en String de conjuntos.
     * Formatos esperados:
     * V = {"A", "B", ...}
     * A = {("A","B",1), ("C","D",2), ...}
     */
    public static Grafo<String, Integer> build(String cadenaParaVertices, String cadenaParaAristas) {
        Grafo<String, Integer> grafo = new Grafo<>();

        // -------------------------------------------------
        // 1. PROCESAR VÉRTICES
        // -------------------------------------------------
        // Limpiamos llaves externas y espacios
        String vLimpio = cadenaParaVertices.replace("{", "").replace("}", "").trim();
        
        // Si no está vacío, separamos por comas
        if (!vLimpio.isEmpty()) {
            String[] partes = vLimpio.split(",");
            for (String parte : partes) {
                // Quitamos comillas y espacios: "A" -> A
                String vertice = parte.trim().replace("\"", "");
                grafo.agregarVertice(vertice);
            }
        }

        // -------------------------------------------------
        // 2. PROCESAR ARISTAS
        // -------------------------------------------------
        // Usamos Regex para encontrar patrones del tipo: (algo, algo, algo)
        // Explicación del regex \(([^)]+)\): 
        // Busca un paréntesis de apertura, captura todo lo que NO sea paréntesis de cierre, y busca el cierre.
        Pattern patronTupla = Pattern.compile("\\(([^)]+)\\)");
        Matcher matcher = patronTupla.matcher(cadenaParaAristas);

        while (matcher.find()) {
            // contenidoTupla tendrá algo como: "A","B",1
            String contenidoTupla = matcher.group(1); 
            
            // Separamos por coma
            String[] datos = contenidoTupla.split(",");
            
            if (datos.length == 3) {
                // Limpiamos origen y destino (quitar espacios y comillas)
                String origen = datos[0].trim().replace("\"", "");
                String destino = datos[1].trim().replace("\"", "");
                
                // Parseamos el peso (asumiendo Integer)
                try {
                    Integer peso = Integer.parseInt(datos[2].trim());
                    
                    // Agregamos la arista (asegurando que los vértices existan por seguridad, 
                    // aunque agregarVertice ya maneja duplicados, es bueno llamar antes si el string V viniera incompleto)
                    grafo.agregarVertice(origen);
                    grafo.agregarVertice(destino);
                    
                    grafo.agregarArista(origen, destino, peso);
                } catch (NumberFormatException e) {
                    System.err.println("Error parseando peso en la tupla: " + contenidoTupla);
                }
            }
        }

        return grafo;
    }
    
    /**
     * Agrega un nuevo vértice al grafo.
     * @param valor El valor del vértice (su "ID" o "payload").
     */
    public void agregarVertice(T valor) {
        if (!existeVertice(valor)) {
            Vertice<T, U> nuevoVertice = new Vertice<>(valor);
            vertices.add(nuevoVertice);
        }
    }

    /**
     * Agrega una arista dirigida desde un vértice origen a uno destino.
     * @param origenValor El valor del vértice de origen.
     * @param destinoValor El valor del vértice de destino.
     * @param peso El peso (tipo U) de la arista.
     */
    public void agregarArista(T origenValor, T destinoValor, U peso) {
        Vertice<T, U> origen = getVertice(origenValor);
        Vertice<T, U> destino = getVertice(destinoValor);

        // El vértice origen mantiene su propia "sublista de aristas"
        origen.agregarArista(destino, peso);
    }

    /**
     * Devuelve la colección de todos los vértices del grafo.
     * (Esta es tu "lista de vértices").
     */
    public Collection<Vertice<T, U>> getVertices() {
        return vertices;
    }

    /**
     * Obtiene el objeto Vértice basado en su valor.
     */
    public Vertice<T, U> getVertice(T valor) {
    	for (Vertice<T, U> v : vertices) {
            if (v.getValor().equals(valor)) {
                return v;
            }
        }
        throw new NoSuchElementException("El vértice no existe: " + valor);
    }
    
    public boolean existeVertice(T valor) {
    	for (Vertice<T, U> v : vertices) {
            if (v.getValor().equals(valor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene la "sublista de aristas" (adyacencias) para un vértice dado.
     */
    public List<Arista<T, U>> getAdyacentes(T valor) {
        return getVertice(valor).getAdyacencias();
    }
    
    /**
     * Método para imprimir el grafo y ver su estructura.
     */
    public void imprimirGrafo() {
        System.out.println("--- Estructura del Grafo ---");
        for (Vertice<T, U> v : vertices) {
            System.out.print("Vertice " + v.getValor() + " tiene aristas a: ");
            if (v.getAdyacencias().isEmpty()) {
                System.out.println("(ninguna)");
            } else {
                for (Arista<T, U> a : v.getAdyacencias()) {
                    System.out.print(a + "  "); // Usa el toString() de Arista
                }
                System.out.println();
            }
        }
        System.out.println("----------------------------");
    }
    
    /**
     * Realiza un Recorrido en Anchura (BFS) desde un vértice inicial.
     * @param valorInicio El valor del vértice desde donde comenzar.
     * @return Una lista de valores de vértices en el orden de visita.
     */
    public List<T> recorridoAnchura(T valorInicio) {
        Vertice<T, U> inicio = getVertice(valorInicio);
        
        List<T> resultado = new ArrayList<>();
        Queue<Vertice<T, U>> cola = new LinkedList<>();
        Set<Vertice<T, U>> visitados = new HashSet<>();

        cola.add(inicio);
        visitados.add(inicio);

        while (!cola.isEmpty()) {
            Vertice<T, U> actual = cola.poll();
            resultado.add(actual.getValor());

            for (Arista<T, U> arista : actual.getAdyacencias()) {
                Vertice<T, U> vecino = arista.getDestino();
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.add(vecino);
                }
            }
        }
        return resultado;
    }
    
    /**
     * Realiza un Recorrido en Profundidad (DFS) desde un vértice inicial.
     * @param valorInicio El valor del vértice desde donde comenzar.
     * @return Una lista de valores de vértices en el orden de visita.
     */
    public List<T> recorridoProfundidad(T valorInicio) {
        Vertice<T, U> inicio = getVertice(valorInicio);
        
        List<T> resultado = new ArrayList<>();
        Set<Vertice<T, U>> visitados = new HashSet<>();

        // Inicia el helper recursivo
        dfsRecursivo(inicio, visitados, resultado);
        
        return resultado;
    }

    // Helper recursivo para DFS
    private void dfsRecursivo(Vertice<T, U> actual, Set<Vertice<T, U>> visitados, List<T> resultado) {
        visitados.add(actual);
        resultado.add(actual.getValor());

        for (Arista<T, U> arista : actual.getAdyacencias()) {
            Vertice<T, U> vecino = arista.getDestino();
            if (!visitados.contains(vecino)) {
                dfsRecursivo(vecino, visitados, resultado);
            }
        }
    }
    
    /**
     * Comprueba si el grafo dirigido contiene al menos un ciclo.
     * @return true si hay un ciclo, false en caso contrario.
     */
    public boolean tieneCiclo() {
        Set<Vertice<T, U>> visitados = new HashSet<>();
        // 'enPila' rastrea los nodos en el camino de recursión actual
        Set<Vertice<T, U>> enPilaRecursion = new HashSet<>();

        for (Vertice<T, U> v : vertices) {
            if (!visitados.contains(v)) {
                if (testAciclidadHelper(v, visitados, enPilaRecursion)) {
                    return true; // Ciclo detectado
                }
            }
        }
        return false; // No se encontraron ciclos
    }

    // Helper recursivo para detectar ciclos
    private boolean testAciclidadHelper(Vertice<T, U> actual, Set<Vertice<T, U>> visitados, Set<Vertice<T, U>> enPilaRecursion) {
        visitados.add(actual);
        enPilaRecursion.add(actual);

        for (Arista<T, U> arista : actual.getAdyacencias()) {
            Vertice<T, U> vecino = arista.getDestino();
            
            if (!visitados.contains(vecino)) {
                // Si se encuentra un ciclo en la llamada recursiva
                if (testAciclidadHelper(vecino, visitados, enPilaRecursion)) {
                    return true;
                }
            } else if (enPilaRecursion.contains(vecino)) {
                // Si el vecino ya está en la pila actual, es un "back-edge"
                // ¡CICLO DETECTADO!
                return true;
            }
        }

        // Al salir de la recursión, lo quitamos de la pila
        enPilaRecursion.remove(actual);
        return false;
    }
    
    /**
     * Encuentra todos los Puntos de Articulación (Cut Vertices).
     * ASUME UN GRAFO NO DIRIGIDO (aristas agregadas en ambos sentidos).
     * @return Un conjunto con los valores de los vértices que son P.A.
     */
    public Set<T> getPuntosArticulacion() {
        // Inicialización de variables
        tiempoAP = 0;
        disc = new HashMap<>();
        low = new HashMap<>();
        parent = new HashMap<>();
        puntosArticulacion = new HashSet<>();
        Set<Vertice<T, U>> visitados = new HashSet<>();

        for (Vertice<T, U> v : vertices) {
            if (!visitados.contains(v)) {
                findAPHelper(v, visitados);
            }
        }

        // Convertir el Set<Vertice> a Set<T>
        Set<T> resultado = new HashSet<>();
        for (Vertice<T, U> v : puntosArticulacion) {
            resultado.add(v.getValor());
        }
        return resultado;
    }

    // Helper recursivo para Tarjan's Articulation Points
    private void findAPHelper(Vertice<T, U> u, Set<Vertice<T, U>> visitados) {
        visitados.add(u);
        disc.put(u, tiempoAP);
        low.put(u, tiempoAP);
        tiempoAP++;
        int children = 0;

        for (Arista<T, U> arista : u.getAdyacencias()) {
            Vertice<T, U> v = arista.getDestino();

            if (!visitados.contains(v)) {
                children++;
                parent.put(v, u);
                findAPHelper(v, visitados);

                // Actualiza el low-link del padre
                low.put(u, Math.min(low.get(u), low.get(v)));

                // (1) 'u' es raíz del árbol DFS y tiene más de un hijo
                if (parent.get(u) == null && children > 1) {
                    puntosArticulacion.add(u);
                }

                // (2) 'u' no es raíz y el low-link de un hijo 'v'
                // es mayor o igual al tiempo de descubrimiento de 'u'
                if (parent.get(u) != null && low.get(v) >= disc.get(u)) {
                    puntosArticulacion.add(u);
                }
            } else if (!v.equals(parent.get(u))) {
                // Arista de retroceso (back-edge) a un ancestro
                // (que no es el padre inmediato)
                low.put(u, Math.min(low.get(u), disc.get(v)));
            }
        }
    }
    
    /**
     * Encuentra todas las Componentes Fuertemente Conexas (SCC).
     * Usa el algoritmo de Kosaraju.
     * @return Una lista de listas (cada lista interna es una SCC).
     */
    public List<List<T>> getComponentesFuertementeConexas() {
        Stack<Vertice<T, U>> stack = new Stack<>();
        Set<Vertice<T, U>> visitados = new HashSet<>();

        // --- 1. Primera Pasada (DFS en Grafo Original) ---
        // Llena la pila en orden de post-visita (finishing times)
        for (Vertice<T, U> v : vertices) {
            if (!visitados.contains(v)) {
                sccDfsPass1(v, visitados, stack);
            }
        }

        // --- 2. Transponer el Grafo ---
        // Creamos una lista de adyacencia inversa temporal
        Map<Vertice<T, U>, List<Vertice<T, U>>> grafoInverso = new HashMap<>();
        for (Vertice<T, U> v : vertices) {
            grafoInverso.put(v, new ArrayList<>());
        }
        for (Vertice<T, U> u : vertices) {
            for (Arista<T, U> arista : u.getAdyacencias()) {
                Vertice<T, U> v = arista.getDestino();
                grafoInverso.get(v).add(u); // Arista v -> u en el inverso
            }
        }

        // --- 3. Segunda Pasada (DFS en Grafo Inverso) ---
        // Procesamos los vértices en el orden de la pila
        visitados.clear();
        List<List<T>> todasLasSCCs = new ArrayList<>();
        
        while (!stack.isEmpty()) {
            Vertice<T, U> v = stack.pop();
            if (!visitados.contains(v)) {
                List<T> nuevaSCC = new ArrayList<>();
                sccDfsPass2(v, visitados, nuevaSCC, grafoInverso);
                todasLasSCCs.add(nuevaSCC);
            }
        }
        return todasLasSCCs;
    }

    // Helper para la Pasada 1 de Kosaraju
    private void sccDfsPass1(Vertice<T, U> u, Set<Vertice<T, U>> visitados, Stack<Vertice<T, U>> stack) {
        visitados.add(u);
        for (Arista<T, U> arista : u.getAdyacencias()) {
            Vertice<T, U> v = arista.getDestino();
            if (!visitados.contains(v)) {
                sccDfsPass1(v, visitados, stack);
            }
        }
        // Se añade a la pila DESPUÉS de visitar a todos sus hijos
        stack.push(u);
    }

    // Helper para la Pasada 2 de Kosaraju (en el grafo inverso)
    private void sccDfsPass2(Vertice<T, U> u, Set<Vertice<T, U>> visitados, List<T> nuevaSCC, Map<Vertice<T, U>, List<Vertice<T, U>>> grafoInverso) {
        visitados.add(u);
        nuevaSCC.add(u.getValor());
        
        for (Vertice<T, U> v : grafoInverso.get(u)) {
            if (!visitados.contains(v)) {
                sccDfsPass2(v, visitados, nuevaSCC, grafoInverso);
            }
        }
    }
    
    /**
     * Realiza un Ordenamiento Topológico usando DFS.
     * @return Una lista de valores de vértices en orden topológico.
     * @throws IllegalStateException si el grafo contiene un ciclo.
     */
    public List<T> recorridoTopologicoDFS() {
        if (tieneCiclo()) {
            throw new IllegalStateException("No se puede ordenar topológicamente un grafo con ciclos.");
        }

        Stack<Vertice<T, U>> stack = new Stack<>();
        Set<Vertice<T, U>> visitados = new HashSet<>();

        for (Vertice<T, U> v : vertices) {
            if (!visitados.contains(v)) {
                topoDfsHelper(v, visitados, stack);
            }
        }

        // El orden topológico es el inverso de la pila
        List<T> resultado = new ArrayList<>();
        while (!stack.isEmpty()) {
            resultado.add(stack.pop().getValor());
        }
        return resultado;
    }

    // Helper para el TopoSort DFS
    private void topoDfsHelper(Vertice<T, U> u, Set<Vertice<T, U>> visitados, Stack<Vertice<T, U>> stack) {
        visitados.add(u);
        for (Arista<T, U> arista : u.getAdyacencias()) {
            Vertice<T, U> v = arista.getDestino();
            if (!visitados.contains(v)) {
                topoDfsHelper(v, visitados, stack);
            }
        }
        // Se añade a la pila DESPUÉS de procesar a sus descendientes
        stack.push(u);
    }
    
    /**
     * Realiza un Ordenamiento Topológico usando el Algoritmo de Kahn (BFS).
     * @return Una lista de valores de vértices en orden topológico.
     * @throws IllegalStateException si el grafo contiene un ciclo.
     */
    public List<T> recorridoTopologicoBFS() {
        // 1. Calcular "grados de entrada" (in-degrees)
        Map<Vertice<T, U>, Integer> inDegree = new HashMap<>();
        for (Vertice<T, U> v : vertices) {
            inDegree.put(v, 0); // Inicializar todos en 0
        }
        
        for (Vertice<T, U> u : vertices) {
            for (Arista<T, U> arista : u.getAdyacencias()) {
                Vertice<T, U> v = arista.getDestino();
                inDegree.put(v, inDegree.get(v) + 1);
            }
        }

        // 2. Encolar todos los nodos con grado de entrada 0
        Queue<Vertice<T, U>> cola = new LinkedList<>();
        for (Vertice<T, U> v : vertices) {
            if (inDegree.get(v) == 0) {
                cola.add(v);
            }
        }

        // 3. Procesar la cola (Algoritmo de Kahn)
        List<T> resultado = new ArrayList<>();
        int nodosProcesados = 0;

        while (!cola.isEmpty()) {
            Vertice<T, U> u = cola.poll();
            resultado.add(u.getValor());
            nodosProcesados++;

            for (Arista<T, U> arista : u.getAdyacencias()) {
                Vertice<T, U> v = arista.getDestino();
                // "Remover" la arista conceptualmente
                inDegree.put(v, inDegree.get(v) - 1);
                
                // Si el vecino ahora tiene grado 0, encolarlo
                if (inDegree.get(v) == 0) {
                    cola.add(v);
                }
            }
        }

        // 4. Verificar si hubo un ciclo
        if (nodosProcesados != vertices.size()) {
            throw new IllegalStateException("No se puede ordenar: El grafo tiene un ciclo.");
        }

        return resultado;
    }
    
    /**
     * Encuentra el camino más corto (en número de aristas) entre dos vértices.
     * Usa BFS.
     * @param valorInicio El valor del vértice de inicio.
     * @param valorFin El valor del vértice de fin.
     * @return Una lista de valores (el camino) o una lista vacía si no hay camino.
     */
    public List<T> caminoMinimoBFS(T valorInicio, T valorFin) {
        Vertice<T, U> inicio = getVertice(valorInicio);
        Vertice<T, U> fin = getVertice(valorFin);

        Queue<Vertice<T, U>> cola = new LinkedList<>();
        Set<Vertice<T, U>> visitados = new HashSet<>();
        // Mapa para reconstruir el camino (hijo -> padre)
        Map<Vertice<T, U>, Vertice<T, U>> predecesores = new HashMap<>();

        cola.add(inicio);
        visitados.add(inicio);
        predecesores.put(inicio, null); // El inicio no tiene predecesor
        
        boolean encontrado = false;

        while (!cola.isEmpty() && !encontrado) {
            Vertice<T, U> actual = cola.poll();

            if (actual.equals(fin)) {
                encontrado = true;
                break;
            }

            for (Arista<T, U> arista : actual.getAdyacencias()) {
                Vertice<T, U> vecino = arista.getDestino();
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.add(vecino);
                    predecesores.put(vecino, actual); // Guardamos el camino
                }
            }
        }

        // Reconstruir el camino
        List<T> camino = new LinkedList<>(); // Usamos LinkedList por addFirst
        if (encontrado) {
            Vertice<T, U> paso = fin;
            while (paso != null) {
                camino.add(0, paso.getValor()); // add(0) es como addFirst
                paso = predecesores.get(paso);
            }
        }
        return camino; // Devuelve lista vacía si no se encontró
    }
    
 // Clase interna helper para la cola de prioridad de Dijkstra
    private class ParDijkstra implements Comparable<ParDijkstra> {
        Vertice<T, U> vertice;
        double distancia;

        ParDijkstra(Vertice<T, U> vertice, double distancia) {
            this.vertice = vertice;
            this.distancia = distancia;
        }

        @Override
        public int compareTo(ParDijkstra otro) {
            return Double.compare(this.distancia, otro.distancia);
        }
    }

    /**
     * Encuentra el camino más corto (ponderado) usando el Algoritmo de Dijkstra.
     * Asume que los pesos 'U' son subtipos de Number (Integer, Double...).
     * @param valorInicio El valor del vértice de inicio.
     * @param valorFin El valor del vértice de fin.
     * @return Una lista de valores (el camino) o una lista vacía si no hay camino.
     */
    public List<T> dijkstra(T valorInicio, T valorFin) {
        return dijkstra(valorInicio, valorFin, null);
    }

    public List<T> dijkstra(T valorInicio, T valorFin, ObservadorDijkstra<T> observador) {
        Vertice<T, U> inicio = getVertice(valorInicio);
        Vertice<T, U> fin = getVertice(valorFin);

        // Cola de prioridad para obtener el nodo con menor distancia
        PriorityQueue<ParDijkstra> pq = new PriorityQueue<>();
        
        // Mapa de distancias (infinitas al inicio)
        Map<Vertice<T, U>, Double> distancias = new HashMap<>();
        
        // Mapa para reconstruir el camino
        Map<Vertice<T, U>, Vertice<T, U>> predecesores = new HashMap<>();

        // Inicialización
        for (Vertice<T, U> v : vertices) {
            distancias.put(v, Double.MAX_VALUE);
            predecesores.put(v, null);
        }
        distancias.put(inicio, 0.0);
        pq.add(new ParDijkstra(inicio, 0.0));
        
        boolean encontrado = false;

        while (!pq.isEmpty()) {
            ParDijkstra parActual = pq.poll();
            Vertice<T, U> u = parActual.vertice;
            double distActual = parActual.distancia;

            if (distActual > distancias.get(u)) {
                continue;
            }

            if (observador != null) {
                Map<T, Double> distanciasPorValor = new HashMap<>();
                for (Map.Entry<Vertice<T, U>, Double> entry : distancias.entrySet()) {
                    distanciasPorValor.put(entry.getKey().getValor(), entry.getValue());
                }
                observador.onVerticeExtraido(u.getValor(), distanciasPorValor);
            }

            if (u.equals(fin)) {
                encontrado = true;
                break;
            }

            for (Arista<T, U> arista : u.getAdyacencias()) {
                Vertice<T, U> v = arista.getDestino();

                double peso = ((Number) arista.getPeso()).doubleValue();

                if (peso < 0) {
                     throw new IllegalArgumentException("Dijkstra no funciona con pesos negativos.");
                }

                double nuevaDist = distancias.get(u) + peso;

                if (nuevaDist < distancias.get(v)) {
                    distancias.put(v, nuevaDist);
                    predecesores.put(v, u);
                    pq.add(new ParDijkstra(v, nuevaDist));

                    if (observador != null) {
                        observador.onAristaRelajada(u.getValor(), v.getValor(), nuevaDist);
                    }
                }
            }
        }

        // Reconstruir el camino (igual que en BFS)
        List<T> camino = new LinkedList<>();
        if (encontrado) {
            Vertice<T, U> paso = fin;
            while (paso != null) {
                camino.add(0, paso.getValor());
                paso = predecesores.get(paso);
            }
        }
        
        // Si el camino no se encontró, 'encontrado' es falso Y
        // el primer elemento no será 'inicio' (a menos que inicio == fin)
        if (!encontrado || !camino.get(0).equals(valorInicio)) {
            return new LinkedList<>(); // No se encontró camino
        }
        
        return camino;
    }
    
    //Agregar el algoritmo de floyd
}