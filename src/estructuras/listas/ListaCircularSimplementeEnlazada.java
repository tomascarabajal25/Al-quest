package estructuras.listas;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

import estructuras.nodos.NodoSimplementeEnlazado;

public class ListaCircularSimplementeEnlazada<T> extends ListaSimplementeEnlazada<T> {

    public ListaCircularSimplementeEnlazada() {
        super();
    }

    /**
     * Sobrescribimos add (que llama a addLast) porque la implementación del padre
     * usa un while(tieneSiguiente) que buscaría null y entraría en bucle infinito.
     */
    @Override
    public boolean add(T elemento) {
        NodoSimplementeEnlazado<T> nuevo = new NodoSimplementeEnlazado<>(elemento);
        
        if (primero == null) {
            primero = nuevo;
            // En circular, el único nodo se apunta a sí mismo
            primero.setSiguiente(primero); 
        } else {
            // Buscamos el último nodo basándonos en el tamaño, no en null
            NodoSimplementeEnlazado<T> ultimo = getNodoPorIndice(tamanio - 1);
            
            // El último apunta al nuevo
            ultimo.setSiguiente(nuevo);
            // El nuevo (ahora último) apunta al primero para cerrar el círculo
            nuevo.setSiguiente(primero);
        }
        tamanio++;
        return true;
    }

    @Override
    public void addLast(T elemento) {
        add(elemento);
    }

    @Override
    public void add(int index, T elemento) {
        if (index < 0 || index > tamanio) throw new IndexOutOfBoundsException();

        // Si insertamos al final, es lo mismo que add(e)
        if (index == tamanio) {
            add(elemento);
            return;
        }

        NodoSimplementeEnlazado<T> nuevo = new NodoSimplementeEnlazado<>(elemento);
        
        if (index == 0) {
            if (primero == null) {
                // Lista vacía
                primero = nuevo;
                primero.setSiguiente(primero);
            } else {
                // Insertar al inicio en lista no vacía:
                // 1. Buscamos al último para actualizar su puntero
                NodoSimplementeEnlazado<T> ultimo = getNodoPorIndice(tamanio - 1);
                
                // 2. Configuramos el nuevo nodo
                nuevo.setSiguiente(primero);
                primero = nuevo;
                
                // 3. El último ahora debe apuntar al nuevo primero
                ultimo.setSiguiente(primero);
            }
        } else {
            // En el medio, la lógica del padre (recorrer con for) sirve, 
            // pero debemos asegurarnos de no romper la cadena.
            // Como el padre usa punteros privados, replicamos la lógica segura aquí:
            NodoSimplementeEnlazado<T> anterior = getNodoPorIndice(index - 1);
            nuevo.setSiguiente(anterior.getSiguiente());
            anterior.setSiguiente(nuevo);
        }
        tamanio++;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= tamanio) throw new IndexOutOfBoundsException();
        
        T datoEliminado;

        if (index == 0) {
            datoEliminado = primero.getDato();
            if (tamanio == 1) {
                primero = null;
            } else {
                NodoSimplementeEnlazado<T> ultimo = getNodoPorIndice(tamanio - 1);
                primero = primero.getSiguiente();
                // El último debe seguir apuntando al (nuevo) primero
                ultimo.setSiguiente(primero); 
            }
        } else {
            // Eliminación en medio o final
            NodoSimplementeEnlazado<T> anterior = getNodoPorIndice(index - 1);
            NodoSimplementeEnlazado<T> eliminar = anterior.getSiguiente();
            datoEliminado = eliminar.getDato();
            
            // Saltamos el nodo
            anterior.setSiguiente(eliminar.getSiguiente());
        }
        tamanio--;
        return datoEliminado;
    }

    /**
     * Sobrescribimos remove(Object) porque el del padre busca hasta encontrar null
     * o el objeto. Si el objeto no está, el padre entraría en loop infinito.
     */
    @Override
    public boolean remove(Object o) {
        if (primero == null) return false;

        // Caso especial: el elemento está en la cabeza
        if (Objects.equals(primero.getDato(), o)) {
            remove(0);
            return true;
        }

        NodoSimplementeEnlazado<T> anterior = primero;
        // Iteramos solo 'tamanio - 1' veces para no dar vueltas eternas
        for (int i = 0; i < tamanio - 1; i++) {
            NodoSimplementeEnlazado<T> actual = anterior.getSiguiente();
            if (Objects.equals(actual.getDato(), o)) {
                anterior.setSiguiente(actual.getSiguiente());
                tamanio--;
                return true;
            }
            anterior = anterior.getSiguiente();
        }

        return false;
    }

    /**
     * Método auxiliar para obtener nodos de forma segura sin iteradores
     * ni condiciones de parada por null.
     */
    private NodoSimplementeEnlazado<T> getNodoPorIndice(int index) {
        NodoSimplementeEnlazado<T> actual = primero;
        for (int i = 0; i < index; i++) {
            actual = actual.getSiguiente();
        }
        return actual;
    }

    /**
     * IMPORTANTE: El iterador del padre fallará porque busca null.
     * Aquí definimos uno que para al alcanzar 'tamanio'.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private NodoSimplementeEnlazado<T> actual = primero;
            private int vistos = 0;

            @Override
            public boolean hasNext() {
                return vistos < tamanio;
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                vistos++;
                return dato;
            }
            
            @Override
            public void remove() {
                // Implementación compleja en listas simples, se omite por brevedad
                // o se puede llamar a ListaCircularSimplementeEnlazada.this.remove(...)
                throw new UnsupportedOperationException("Remove no soportado en este iterador simple");
            }
        };
    }
    
    /**
     * Ajustamos addSorted para manejar la circularidad.
     */
    @Override
    public void addSorted(T item) {
        if (item == null) throw new NullPointerException();
        if (!(item instanceof Comparable)) throw new IllegalArgumentException();
        
        // Si está vacía o va antes del primero, es un insert en index 0 (que ya maneja la circularidad)
        if (primero == null || ((Comparable<T>) item).compareTo(primero.getDato()) <= 0) {
            add(0, item);
            return;
        }

        // Buscamos posición
        NodoSimplementeEnlazado<T> actual = primero;
        int index = 0;
        
        // Iteramos máximo hasta el final de la lista
        // Condición: mientras no hayamos dado la vuelta Y el elemento sea mayor al siguiente
        while (index < tamanio - 1 && 
               ((Comparable<T>) item).compareTo(actual.getSiguiente().getDato()) > 0) {
            actual = actual.getSiguiente();
            index++;
        }
        
        // Insertamos después de 'actual'
        NodoSimplementeEnlazado<T> nuevo = new NodoSimplementeEnlazado<>(item);
        nuevo.setSiguiente(actual.getSiguiente());
        actual.setSiguiente(nuevo);
        tamanio++;
    }
}