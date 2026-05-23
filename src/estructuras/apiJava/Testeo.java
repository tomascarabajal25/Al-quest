package material.estructuras.apiJava;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Vector;

import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Testeo {

	public static void main(String[] args) {
		
	}
	
	public static void vector() {
		{
			Vector<String> vector = new Vector<>();
			vector.add("Manzana");     // O(1) amortizado
			vector.add(0, "Pera");
	        String fruta = vector.get(1); // O(1)
	        System.out.println(fruta);
	        for(String item: vector) {
	        	System.out.println(item);	
	        }
		}
		
		{
			List<String> lista = new ArrayList<>();
	        lista.add("Manzana");     // O(1) amortizado
	        lista.add(0, "Pera");     // O(n)
	        String fruta = lista.get(1); // O(1)
	        System.out.println(fruta); // Salida: Manzana
		}
	}
	
	public static void linkedList() {

        Scanner scanner = new Scanner(System.in);
        LinkedList<Integer> numeros = new LinkedList<>();

        // 1. Cargar números hasta que llegue uno negativo
        System.out.println("Ingrese números enteros (negativo para terminar):");
        while (true) {
            int n = scanner.nextInt();
            if (n < 0) break;
            numeros.add(n);
        }

        // 2. Mostrar todos los números ingresados
        System.out.println("Lista original:");
        System.out.println(numeros);

        // 3. Eliminar números pares (usando ListIterator)
        ListIterator<Integer> it = numeros.listIterator();
        while (it.hasNext()) {
            int valor = it.next();
            if (valor % 2 == 0) {
                it.remove();
            }
        }

        // 4. Mostrar la lista final
        System.out.println("Lista sin números pares:");
        System.out.println(numeros);

        // 5. Calcular promedio
        if (numeros.isEmpty()) {
            System.out.println("No quedan números impares para calcular promedio.");
        } else {
            int suma = 0;
            for (int v : numeros) {
                suma += v;
            }
            double promedio = (double) suma / numeros.size();
            System.out.println("Promedio de los valores restantes: " + promedio);
        }
    }
	
	public static void hashSet() {
		HashSet<String> colores = new HashSet<>();
        colores.add("Rojo");
        colores.add("Azul");
        colores.add("Rojo"); // Ignorado (duplicado)
        boolean existe = colores.contains("Azul"); // O(1)
        System.out.println(existe + ", Tamaño: " + colores.size()); // Salida: true, Tamaño: 2
    }
	
	public static void TreeMap() {

        // 1. Crear el TreeMap
        // LocalDate se ordena naturalmente (compareTo)
        TreeMap<LocalDate, Integer> ventas = new TreeMap<>();

        // 2. Cargar datos (más típico en ejercicios)
        ventas.put(LocalDate.of(2025, 1, 10), 120);
        ventas.put(LocalDate.of(2025, 1, 8), 80);
        ventas.put(LocalDate.of(2025, 1, 12), 150);
        ventas.put(LocalDate.of(2025, 1, 9), 60);

        // 3. Mostrar ventas ordenadas automáticamente
        System.out.println("Ventas ordenadas por fecha:");
        for (Map.Entry<LocalDate, Integer> entry : ventas.entrySet()) {
            System.out.println(entry.getKey() + " → " + entry.getValue());
        }

        // 4. Obtener la venta de una fecha específica
        LocalDate fechaConsulta = LocalDate.of(2025, 1, 9);
        System.out.println("\nVenta del " + fechaConsulta + ": " + ventas.get(fechaConsulta));

        // 5. Obtener primera y última fecha
        System.out.println("\nPrimera fecha registrada: " + ventas.firstKey());
        System.out.println("Última fecha registrada:   " + ventas.lastKey());

        // 6. Obtener rango de fechas: subMap(desde, hastaIncluida)
        System.out.println("\nVentas entre el 09/01 y el 11/01:");
        Map<LocalDate, Integer> rango = ventas.subMap(
                LocalDate.of(2025, 1, 9),
                true,
                LocalDate.of(2025, 1, 11),
                true
        );

        for (Map.Entry<LocalDate, Integer> e : rango.entrySet()) {
            System.out.println(e.getKey() + " → " + e.getValue());
        }

        // 7. Eliminar un registro
        ventas.remove(LocalDate.of(2025, 1, 10));
        System.out.println("\nDespués de eliminar 10/01:");
        System.out.println(ventas);
    }
	
	/**
	 * La libreria se descarga de: https://mvnrepository.com/artifact/org.jgrapht/jgrapht-core
	 */
	public static void Graph() {

        // 1. Crear grafo ponderado no dirigido
        Graph<String, DefaultWeightedEdge> grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        // 2. Agregar vértices (ciudades)
        grafo.addVertex("Buenos Aires");
        grafo.addVertex("Rosario");
        grafo.addVertex("Cordoba");
        grafo.addVertex("Mendoza");

        // 3. Agregar aristas con pesos (distancias)
        grafo.setEdgeWeight(grafo.addEdge("Buenos Aires", "Rosario"), 300);
        grafo.setEdgeWeight(grafo.addEdge("Rosario", "Cordoba"), 400);
        grafo.setEdgeWeight(grafo.addEdge("Cordoba", "Mendoza"), 650);
        grafo.setEdgeWeight(grafo.addEdge("Buenos Aires", "Cordoba"), 700);

        // 4. Mostrar el grafo
        System.out.println("Aristas del grafo:");
        for (DefaultWeightedEdge e : grafo.edgeSet()) {
            System.out.println(e + " = " + grafo.getEdgeWeight(e));
        }

        // 5. Camino más corto usando Dijkstra
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra =
                new DijkstraShortestPath<>(grafo);

        var path = dijkstra.getPath("Buenos Aires", "Mendoza");

        System.out.println("\nCamino más corto Buenos Aires → Mendoza:");
        System.out.println(path.getVertexList());
        System.out.println("Distancia total: " + path.getWeight());
    }

}
