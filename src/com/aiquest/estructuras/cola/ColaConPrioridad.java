package com.aiquest.estructuras.cola;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.NoSuchElementException;

/**
 * Implementación de una Cola con Prioridad utilizando una Lista de Colas (Bucket Queue).
 * * Los elementos de MAYOR prioridad se encuentran en los ÍNDICES MÁS BAJOS.
 * @param <E> El tipo de elementos a almacenar.
 */
public class ColaConPrioridad<E> {

    // La lista de colas (buckets). El índice 0 es la prioridad más alta.
    private final List<Queue<E>> colasDePrioridad;
    private int size; // Contador para saber el número total de elementos

    /**
     * Constructor.
     * @param maxPrioridad El número de niveles de prioridad disponibles (ej: si es 5, 
     * hay prioridades de 0 a 4).
     */
    public ColaConPrioridad(int maxPrioridad) {
        if (maxPrioridad <= 0) {
            throw new IllegalArgumentException("La prioridad máxima debe ser positiva.");
        }
        this.colasDePrioridad = new ArrayList<>(maxPrioridad);
        // Inicializa cada "bucket" (nivel de prioridad) con una nueva cola vacía
        for (int i = 0; i < maxPrioridad; i++) {
            colasDePrioridad.add(new LinkedList<>());
        }
        this.size = 0;
    }

    /**
     * Inserta un elemento en la cola según su nivel de prioridad.
     * @param elemento El elemento a insertar.
     * @param prioridad El nivel de prioridad (debe ser un índice válido: 0 hasta maxPrioridad - 1).
     */
    public void enqueue(E elemento, int prioridad) {
        if (prioridad < 0 || prioridad >= colasDePrioridad.size()) {
            throw new IllegalArgumentException("Prioridad inválida. Debe estar entre 0 y " + (colasDePrioridad.size() - 1));
        }
        
        // Agrega el elemento a la cola que corresponde a su prioridad
        colasDePrioridad.get(prioridad).offer(elemento);
        size++;
    }

    /**
     * Remueve y devuelve el elemento de mayor prioridad (el primero que encuentre 
     * empezando desde la prioridad 0).
     * @return El elemento de mayor prioridad.
     * @throws NoSuchElementException Si la cola está vacía.
     */
    public E dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("La cola con prioridad está vacía.");
        }

        // Itera desde la prioridad más alta (índice 0) hacia abajo
        for (Queue<E> cola : colasDePrioridad) {
            if (!cola.isEmpty()) {
                // Encontró el primer elemento de mayor prioridad
                size--;
                return cola.poll();
            }
        }
        // Este punto solo se alcanzaría si size > 0 pero todas las colas están vacías,
        // lo cual es una inconsistencia lógica (y se previene por la comprobación inicial).
        throw new IllegalStateException("Error interno: inconsistencia de tamaño.");
    }

    /**
     * Devuelve (sin remover) el elemento de mayor prioridad.
     * @return El elemento de mayor prioridad.
     * @throws NoSuchElementException Si la cola está vacía.
     */
    public E peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("La cola con prioridad está vacía.");
        }

        // Itera desde la prioridad más alta (índice 0)
        for (Queue<E> cola : colasDePrioridad) {
            if (!cola.isEmpty()) {
                // Devuelve el elemento en la cabeza de la cola
                return cola.peek();
            }
        }
        // De nuevo, inconsistencia lógica si se llega aquí.
        throw new IllegalStateException("Error interno: inconsistencia de tamaño.");
    }

    /**
     * Verifica si la cola con prioridad está vacía.
     * @return true si no contiene elementos, false en caso contrario.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Devuelve el número total de elementos en la cola.
     */
    public int size() {
        return size;
    }
}