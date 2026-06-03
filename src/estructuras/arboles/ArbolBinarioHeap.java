package com.aiquest.estructuras.arboles;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * Implementación de un Heap Binario (Min o Max) usando un ArrayList.
 * El comportamiento (Min o Max) se define por el Comparator proporcionado.
 * @param <E> El tipo de elementos, debe ser comparable.
 */
public class ArbolBinarioHeap<E> {

	// El arreglo (vector) que simula la estructura de árbol.
	private E[] heapArray;
	private int size;
	private final Comparator<E> comparator;
	private static final int DEFAULT_CAPACITY = 100; // Capacidad inicial

	/**
	 * Constructor.
	 * @param comparator Define el orden de prioridad (Min-Heap o Max-Heap).
	 * @param capacity Capacidad inicial del arreglo.
	 */
	@SuppressWarnings("unchecked")
	public ArbolBinarioHeap(Comparator<E> comparator, int capacity) {
		this.comparator = comparator;
		this.size = 0;
		// La restricción de Java: no se puede hacer 'new E[capacity]'.
		// La solución es crear un Object[] y hacer un 'cast' inseguro.
		this.heapArray = (E[]) new Object[capacity];
	}

	// Constructor por defecto con capacidad predeterminada
	public ArbolBinarioHeap(Comparator<E> comparator) {
		this(comparator, DEFAULT_CAPACITY);
	}

	// --- Funciones de Navegación y Mantenimiento (Iguales a la versión ArrayList) ---

	// ... (Métodos getParentIndex, getLeftChildIndex, getRightChildIndex, swap, siftUp, siftDown)

	private int getParentIndex(int i) { return (i - 1) / 2; }
	private int getLeftChildIndex(int i) { return 2 * i + 1; }
	private int getRightChildIndex(int i) { return 2 * i + 2; }

	private void swap(int i, int j) {
		E temp = heapArray[i];
		heapArray[i] = heapArray[j];
		heapArray[j] = temp;
	}

	private void siftUp(int i) {
		int current = i;
		int parent = getParentIndex(current);
		while (current > 0 && comparator.compare(heapArray[current], heapArray[parent]) < 0) {
			swap(current, parent);
			current = parent;
			parent = getParentIndex(current);
		}
	}

	private void siftDown(int i) {
		int current = i;
		while (getLeftChildIndex(current) < size) {
			int left = getLeftChildIndex(current);
			int right = getRightChildIndex(current);
			int betterChild = left;

			if (right < size && comparator.compare(heapArray[right], heapArray[left]) < 0) {
				betterChild = right;
			}

			if (comparator.compare(heapArray[current], heapArray[betterChild]) > 0) {
				swap(current, betterChild);
				current = betterChild;
			} else {
				break;
			}
		}
	}

	/**
	 * Asegura que el arreglo tenga espacio suficiente. Si no, lo redimensiona.
	 */
	@SuppressWarnings("unchecked")
	private void ensureCapacity() {
		if (size == heapArray.length) {
			int newCapacity = heapArray.length * 2;
			E[] newArray = (E[]) new Object[newCapacity];
			System.arraycopy(heapArray, 0, newArray, 0, size);
			this.heapArray = newArray;
		}
	}


	// --- Operaciones de Cola de Prioridad ---

	/**
	 * Inserta un nuevo elemento en el Heap.
	 * Complejidad: O(log n) (o O(n) si se requiere redimensionar el arreglo)
	 */
	public void insert(E element) {
		ensureCapacity(); // Redimensiona si es necesario

		heapArray[size] = element;
		size++;
		siftUp(size - 1);
	}

	/**
	 * Remueve y devuelve el elemento raíz (el de mayor/menor prioridad).
	 * Complejidad: O(log n)
	 */
	public E extract() {
		if (isEmpty()) {
			throw new NoSuchElementException("El Heap está vacío.");
		}

		E root = heapArray[0];

		// Mover el último elemento a la raíz
		heapArray[0] = heapArray[size - 1];
		heapArray[size - 1] = null; // Evitar memory leak (opcional, pero buena práctica)
		size--;

		// Restaurar la propiedad de Heap
		if (!isEmpty()) {
			siftDown(0);
		}

		return root;
	}

	/**
	 * Devuelve el elemento raíz sin removerlo.
	 * Complejidad: O(1)
	 */
	public E peek() {
		if (isEmpty()) {
			throw new NoSuchElementException("El Heap está vacío.");
		}
		return heapArray[0];
	}

	// --- Utilidades ---

	public boolean isEmpty() {
		return size == 0;
	}

	public int size() {
		return size;
	}
}