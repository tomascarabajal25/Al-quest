package com.aiquest.estructuras.arboles;

public class ArbolAVL<T extends Comparable<T>> extends ArbolBinarioDeBusqueda<T> {

    // Método para insertar un nuevo nodo en el árbol AVL
    @Override
    public void insertar(T valor) {
        setRaiz( insertarAVL((NodoAVL<T>) getRaiz(), valor) );
    }

    private NodoAVL<T> insertarAVL(NodoAVL<T> nodo, T valor) {
        // Inserción estándar de un árbol binario de búsqueda
        if (nodo == null) {
            return new NodoAVL<T>(valor);
        }

        if (valor.compareTo(nodo.getValor()) == -1) {
            nodo.setIzquierdo( insertarAVL((NodoAVL<T>) nodo.getIzquierdo(), valor));
        } else if (valor.compareTo(nodo.getValor()) == 1) {
            nodo.setDerecho( insertarAVL((NodoAVL<T>) nodo.getDerecho(), valor) );
        } else {
            // No se permiten valores duplicados en el árbol AVL
            return nodo;
        }

        // Actualizar la altura del nodo actual
        nodo.setAltura( 1 + Math.max(altura((NodoAVL<T>) nodo.getIzquierdo()), altura((NodoAVL<T>) nodo.getDerecho())));

        // Obtener el factor de balance para este nodo
        int balance = obtenerBalance(nodo);

        // Rotaciones para mantener el árbol balanceado

        // Caso Izquierda-Izquierda
        if (balance > 1 && 
        	valor.compareTo( nodo.getIzquierdo().getValor()) == -1) {
            return rotacionDerecha(nodo);
        }

        // Caso Derecha-Derecha
        if (balance < -1 && 
        	valor.compareTo( nodo.getDerecho().getValor()) == 1) {
            return rotacionIzquierda(nodo);
        }

        // Caso Izquierda-Derecha
        if (balance > 1 && 
        	valor.compareTo( nodo.getIzquierdo().getValor()) == 1) {
            nodo.setIzquierdo( rotacionIzquierda((NodoAVL<T>) nodo.getIzquierdo()));
            return rotacionDerecha(nodo);
        }

        // Caso Derecha-Izquierda
        if (balance < -1 && 
        	valor.compareTo( nodo.getDerecho().getValor()) == -1) {
            nodo.setDerecho( rotacionDerecha((NodoAVL<T>) nodo.getDerecho()) );
            return rotacionIzquierda(nodo);
        }

        return nodo;
    }

    // Rotación hacia la derecha
    private NodoAVL<T> rotacionDerecha(NodoAVL<T> y) {
        NodoAVL<T> x = (NodoAVL<T>) y.getIzquierdo();
        NodoAVL<T> T2 = (NodoAVL<T>) x.getDerecho();

        // Realizar la rotación
        x.setDerecho(y);
        y.setIzquierdo(T2);

        // Actualizar alturas
        y.setAltura( Math.max(altura((NodoAVL<T>) y.getIzquierdo()), altura((NodoAVL<T>) y.getDerecho())) + 1 );
        x.setAltura( Math.max(altura((NodoAVL<T>) x.getIzquierdo()), altura((NodoAVL<T>) x.getDerecho())) + 1);

        // Retornar nueva raíz
        return x;
    }

    // Rotación hacia la izquierda
    private NodoAVL<T> rotacionIzquierda(NodoAVL<T> x) {
        NodoAVL<T> y = (NodoAVL<T>) x.getDerecho();
        NodoAVL<T> T2 = (NodoAVL<T>) y.getIzquierdo();

        // Realizar la rotación
        y.setIzquierdo(x);
        x.setDerecho(T2);

        // Actualizar alturas
        x.setAltura( Math.max(altura((NodoAVL<T>) x.getIzquierdo()), altura((NodoAVL<T>) x.getDerecho())) + 1);
        y.setAltura( Math.max(altura((NodoAVL<T>) y.getIzquierdo()), altura((NodoAVL<T>) y.getDerecho())) + 1);

        // Retornar nueva raíz
        return y;
    }

    // Obtener la altura de un nodo
    private int altura(NodoAVL<T> nodo) {
        return (nodo == null) ? 0 : nodo.getAltura();
    }

    // Obtener el factor de balance de un nodo
    private int obtenerBalance(NodoAVL<T> nodo) {
        return (nodo == null) ? 0 : altura((NodoAVL<T>) nodo.getIzquierdo()) - altura((NodoAVL<T>) nodo.getDerecho());
    }

    /**
     * Método público para eliminar un valor del árbol AVL.
     * Sobrescribe el método de la clase padre.
     * Llama al método recursivo auxiliar 'eliminarAVL'.
     *
     * @param valor El valor (T) que se desea eliminar.
     */
    @Override
    public void eliminar(T valor) {
        setRaiz(eliminarAVL((NodoAVL<T>) getRaiz(), valor));
    }

    /**
     * Método recursivo privado para eliminar un nodo y luego rebalancear el árbol.
     *
     * @param nodo  El nodo actual en la recursión.
     * @param valor El valor a eliminar.
     * @return El nodo que reemplaza al nodo actual (puede ser el mismo o uno nuevo
     * después de la rotación).
     */
    private NodoAVL<T> eliminarAVL(NodoAVL<T> nodo, T valor) {
        
        // --- 1. ELIMINACIÓN ESTÁNDAR DE ABB ---

        if (nodo == null) {
            return null; // Valor no encontrado
        }

        // Buscar el nodo a eliminar
        if (valor.compareTo(nodo.getValor()) == -1) { // valor < nodo.getValor()
            nodo.setIzquierdo(eliminarAVL((NodoAVL<T>) nodo.getIzquierdo(), valor));
        } else if (valor.compareTo(nodo.getValor()) == 1) { // valor > nodo.getValor()
            nodo.setDerecho(eliminarAVL((NodoAVL<T>) nodo.getDerecho(), valor));
        } else {
            // --- Nodo encontrado ---
            
            // Caso 1 o 2: Nodo con 0 o 1 hijo
            if (nodo.getIzquierdo() == null) {
                return (NodoAVL<T>) nodo.getDerecho(); // Devuelve el hijo derecho (o null)
            } else if (nodo.getDerecho() == null) {
                return (NodoAVL<T>) nodo.getIzquierdo(); // Devuelve el hijo izquierdo
            }

            // Caso 3: Nodo con 2 hijos
            // Encontrar el sucesor in-order (el valor más pequeño del subárbol derecho)
            T sucesorValor = encontrarMinimo((NodoAVL<T>) nodo.getDerecho());

            // Reemplazar el valor del nodo actual con el del sucesor
            nodo.setValor(sucesorValor);

            // Eliminar el nodo sucesor del subárbol derecho
            nodo.setDerecho(eliminarAVL((NodoAVL<T>) nodo.getDerecho(), sucesorValor));
        }

        // Si el árbol se quedó vacío (ej. eliminamos el único nodo), no hay nada que balancear
        if (nodo == null) {
            return null;
        }

        // --- 2. ACTUALIZACIÓN Y REBALANCEO AVL ---
        // (Este código se ejecuta "hacia arriba" en la recursión)

        // Actualizar la altura del nodo actual
        nodo.setAltura(1 + Math.max(altura((NodoAVL<T>) nodo.getIzquierdo()),
                                     altura((NodoAVL<T>) nodo.getDerecho())));

        // Obtener el factor de balance
        int balance = obtenerBalance(nodo);

        // --- 3. Casos de Rotación (Hay 4 casos) ---

        // Caso Izquierda-Izquierda (LL)
        if (balance > 1 && obtenerBalance((NodoAVL<T>) nodo.getIzquierdo()) >= 0) {
            return rotacionDerecha(nodo);
        }

        // Caso Izquierda-Derecha (LR)
        if (balance > 1 && obtenerBalance((NodoAVL<T>) nodo.getIzquierdo()) < 0) {
            nodo.setIzquierdo(rotacionIzquierda((NodoAVL<T>) nodo.getIzquierdo()));
            return rotacionDerecha(nodo);
        }

        // Caso Derecha-Derecha (RR)
        if (balance < -1 && obtenerBalance((NodoAVL<T>) nodo.getDerecho()) <= 0) {
            return rotacionIzquierda(nodo);
        }

        // Caso Derecha-Izquierda (RL)
        if (balance < -1 && obtenerBalance((NodoAVL<T>) nodo.getDerecho()) > 0) {
            nodo.setDerecho(rotacionDerecha((NodoAVL<T>) nodo.getDerecho()));
            return rotacionIzquierda(nodo);
        }

        // Devolver el nodo (potencialmente modificado o rotado)
        return nodo;
    }

    /**
     * Método auxiliar para encontrar el valor mínimo en un subárbol (el sucesor).
     * Se utiliza en el Caso 3 de la eliminación.
     *
     * @param nodo La raíz del subárbol (subárbol derecho del nodo a eliminar).
     * @return El valor (T) más pequeño de ese subárbol.
     */
    private T encontrarMinimo(NodoAVL<T> nodo) {
        NodoAVL<T> actual = nodo;
        
        // El valor mínimo siempre es el nodo más a la izquierda.
        while (actual.getIzquierdo() != null) {
            actual = (NodoAVL<T>) actual.getIzquierdo();
        }
        return actual.getValor();
    }
}