package material.estructuras.listas;

import java.util.Iterator;
import java.util.NoSuchElementException;

import material.estructuras.nodos.NodoDoblementeEnlazado;

public class ListaCircularDoblementeEnlazada<T> extends ListaDoblementeEnlazada<T> {

    public ListaCircularDoblementeEnlazada() {
        super();
    }

    /**
     * Reutilizamos la lógica de agregar linealmente, 
     * y luego forzamos la unión entre el último y el primero.
     */
    @Override
    public boolean add(T elemento) {
        super.add(elemento); // Agrega normal (como lineal)
        cerrarCirculo();     // Convierte en circular
        return true;
    }

    @Override
    public void add(int index, T elemento) {
        super.add(index, elemento);
        cerrarCirculo();
    }

    @Override
    public T remove(int index) {
        T dato = super.remove(index);
        // Si quedó vacía, no hay nada que cerrar
        if (size() > 0) {
            cerrarCirculo();
        }
        return dato;
    }
    
    // Método auxiliar para mantener la circularidad
    private void cerrarCirculo() {
        if (getPrimero() != null && getUltimo() != null) {
        	getUltimo().setSiguiente(getPrimero());
        	getPrimero().setAnterior(getUltimo());
        }
    }

    /**
     * IMPORTANTE: Debemos sobrescribir los iteradores.
     * El iterador del padre busca (actual != null), lo cual en una lista circular
     * crearía un bucle infinito, ya que nunca es null.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            NodoDoblementeEnlazado<T> actual = getPrimero();
            int cont = 0;

            public boolean hasNext() {
                return cont < size(); // Paramos por conteo, no por null
            }

            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T dato = actual.getDato();
                actual = actual.getSiguiente();
                cont++;
                return dato;
            }
        };
    }

    // También hay que tener cuidado con métodos que recorran buscando null
    // Por ejemplo, si tu padre tiene un toString o un indexOf basado en while(actual != null),
    // deberías sobrescribirlos aquí para usar un bucle for(i=0; i<tamanio).
}
