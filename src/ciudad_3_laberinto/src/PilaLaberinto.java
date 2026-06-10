package ciudad_3_laberinto.src;

public class PilaLaberinto<T> {

    private Nodo cima;

    private int tamanio;

    private class Nodo {
        T dato;
        Nodo siguiente;

        Nodo(T dato) {
            this.dato = dato;
            this.siguiente = null;
        }
    }
    
    public PilaLaberinto() {
        this.cima = null;
        this.tamanio = 0;
    }

    public void apilar(T dato) {
        Nodo nuevoNodo = new Nodo(dato);
        nuevoNodo.siguiente = cima;
        cima = nuevoNodo;
        tamanio++;
    }

    public T desapilar() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila esta vacia, no se puede desapilar");
        }
        T dato = cima.dato;
        cima = cima.siguiente;
        tamanio--;
        return dato;
    }

    public T verCima() {
        if (estaVacia()) {
            throw new IllegalStateException("La pila esta vacia, no hay nada que ver");
        }
        return cima.dato;
    }

    public boolean estaVacia() {
        return tamanio == 0;
    }

    public int obtenerTamanio() {
        return tamanio;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Pila [cima -> fondo]: ");
        Nodo actual = cima;
        while (actual != null) {
            sb.append(actual.dato);
            if (actual.siguiente != null) {
                sb.append(" -> ");
            }
            actual = actual.siguiente;
        }
        return sb.toString();
    }
}
