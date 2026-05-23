package estructuras.arboles;

import estructuras.listas.ListaConCursor;

public class ArbolBinarioDeBusqueda<T extends Comparable<T>> {
	
    private NodoDeArbol<T> raiz = null;

    public ArbolBinarioDeBusqueda() {
        raiz = null;
    }

    // Método para insertar un nuevo nodo en el árbol
    public void insertar(T valor) {
        raiz = insertarRecursivo(raiz, valor);
    }

    private NodoDeArbol<T> insertarRecursivo(NodoDeArbol<T> nodo, T valor) {
        if (nodo == null) {
            return new NodoDeArbol<T>(valor);
        }

        if (valor.compareTo(nodo.getValor()) == -1) {
            nodo.setIzquierdo( insertarRecursivo(nodo.getIzquierdo(), valor) );
        } else if (valor.compareTo(nodo.getValor()) == 1) {
            nodo.setDerecho( insertarRecursivo(nodo.getDerecho(), valor));
        }

        return nodo;
    }

    // Método para buscar un valor en el árbol
    public boolean buscar(T valor) {
        return buscarRecursivo(raiz, valor);
    }

    private boolean buscarRecursivo(NodoDeArbol<T> nodo, T valor) {
        if (nodo == null) {
            return false;
        }

        if (valor.equals(nodo.getValor())) {
            return true;
        }

        
        return valor.compareTo(nodo.getValor()) == -1
                ? buscarRecursivo(nodo.getIzquierdo(), valor)
                : buscarRecursivo(nodo.getDerecho(), valor);
    }

    public boolean estaVacio() {
    	return this.raiz == null;
    }
    
    //FIXME: agregar el eliminar en la guia
    
    // Recorridos en el árbol

    // Inorden (izquierda, raíz, derecha)
    public void inorden() {
        inordenRecursivo(raiz);
    }

    private ListaConCursor<T> inordenRecursivo(NodoDeArbol<T> nodo) {
        if (nodo != null) {
            inordenRecursivo(nodo.getIzquierdo());
            //FIXME: devolver como lista el recorrido. Tarea
            System.out.print(nodo.getValor() + " ");
            inordenRecursivo(nodo.getDerecho());
        }
        return null;
    }

    // Preorden (raíz, izquierda, derecha)
    public void preorden() {
        preordenRecursivo(raiz);
    }

    private void preordenRecursivo(NodoDeArbol<T> nodo) {
        if (nodo != null) {
            System.out.print(nodo.getValor() + " ");
            preordenRecursivo(nodo.getIzquierdo());
            preordenRecursivo(nodo.getDerecho());
        }
    }

    // Postorden (izquierda, derecha, raíz)
    public void postorden() {
        postordenRecursivo(raiz);
    }

    private void postordenRecursivo(NodoDeArbol<T> nodo) {
        if (nodo != null) {
            postordenRecursivo(nodo.getIzquierdo());
            postordenRecursivo(nodo.getDerecho());
            System.out.print(nodo.getValor() + " ");
        }
    }

	protected NodoDeArbol<T> getRaiz() {
		return raiz;
	}

	protected void setRaiz(NodoDeArbol<T> raiz) {
		this.raiz = raiz;
	}
    
	/**
	 * Devuelve la cantidad de nodos hojas que tenga el arbol o 0 si esta vacio
	 * @return
	 */
	public int contarCantidadDeHojas() {
		return contarCantidadDeHojas(this.raiz);
	}
    
	/**
	 * Dado un nodo, devuelve la cantidad de hojas que tiene ese subarbol
	 * @param nodo
	 * @return
	 */
	private int contarCantidadDeHojas(NodoDeArbol<T> nodo) {
		if (nodo == null) {
			return 0;
		}
		if (!nodo.tieneHijos()) {
			return 1;
		}
		return contarCantidadDeHojas(nodo.getIzquierdo()) + contarCantidadDeHojas(nodo.getDerecho()); 
	}
	
	/**
	 * Devuelve la cantidad de nodos cons 2 hijos que tenga el arbol o 0 si esta vacio
	 * @return
	 */
	public int contarCantidadDeNodosConDosHijos() {
		return contarCantidadDeNodosConDosHijos(this.raiz);
	}
	
	/**
	 * Dado un nodo, devuelve la cantidad de nodos que tienen 2 hijos de ese subarbol
	 * @param nodo
	 * @return
	 */
	private int contarCantidadDeNodosConDosHijos(NodoDeArbol<T> nodo) {
		if ((nodo == null) ||
		   (!nodo.tieneHijos())) {
			return 0;
		}
		if (nodo.tieneUnHijo()) {
			return contarCantidadDeNodosConDosHijos(nodo.getIzquierdo()) + contarCantidadDeNodosConDosHijos(nodo.getDerecho());
		}
		return 1 + contarCantidadDeNodosConDosHijos(nodo.getIzquierdo()) + contarCantidadDeNodosConDosHijos(nodo.getDerecho());
	}
	
	/**
	 * Devuelve la altura del arbol
	 * @return
	 */
	public int calcularAltura() {
		return calcularAltura(this.raiz);
	}
	
	/**
	 * Dado un nodo, devuelve la altura del mismo
	 * @param nodo
	 * @return
	 */
	private int calcularAltura(NodoDeArbol<T> nodo) {
		if (nodo == null) {
			return 0;
		}
		int alturaIzquierda = calcularAltura(nodo.getIzquierdo());
		int alturaDerecha = calcularAltura(nodo.getDerecho());
		return Math.max(alturaIzquierda, alturaDerecha) + 1;
	}
	
	/**
     * Método público para eliminar un valor del árbol.
     * Llama al método recursivo auxiliar comenzando desde la raíz.
     * * @param valor El valor (T) que se desea eliminar.
     */
    public void eliminar(T valor) {
        // La raíz se reasigna por si el nodo eliminado es la raíz original.
        raiz = eliminarRecursivo(raiz, valor);
    }

    /**
     * Método recursivo privado para encontrar y eliminar un nodo con el valor dado.
     * * @param nodo El nodo actual que se está inspeccionando.
     * @param valor El valor que se desea eliminar.
     * @return El nodo que debe reemplazar al nodo actual en el árbol 
     * (puede ser el mismo nodo, un hijo, o null).
     */
    private NodoDeArbol<T> eliminarRecursivo(NodoDeArbol<T> nodo, T valor) {
        
        // --- 1. Caso Base: Árbol vacío o no se encontró el valor ---
        if (nodo == null) {
            return null; // O return nodo; es lo mismo
        }

        // --- 2. Buscar el nodo a eliminar ---
        if (valor.compareTo(nodo.getValor()) < 0) {
            // El valor es menor, buscar en el subárbol izquierdo
            // Se re-asigna el hijo izquierdo con lo que devuelva la recursión
            nodo.setIzquierdo(eliminarRecursivo(nodo.getIzquierdo(), valor));

        } else if (valor.compareTo(nodo.getValor()) > 0) {
            // El valor es mayor, buscar en el subárbol derecho
            // Se re-asigna el hijo derecho con lo que devuelva la recursión
            nodo.setDerecho(eliminarRecursivo(nodo.getDerecho(), valor));

        } else {
            // --- 3. Nodo encontrado (nodo.getValor().equals(valor)) ---
            // Aquí manejamos los 3 casos de eliminación:

            // Caso 1: El nodo es una hoja (sin hijos)
            // Caso 2: El nodo tiene UN solo hijo (izquierdo o derecho)
            
            // Estas dos condiciones cubren ambos casos (1 y 2)
            if (nodo.getIzquierdo() == null) {
                // Si no tiene hijo izquierdo, se reemplaza por el hijo derecho.
                // Si el derecho también es null (Caso 1), devuelve null.
                return nodo.getDerecho(); 
            } else if (nodo.getDerecho() == null) {
                // Si no tiene hijo derecho, se reemplaza por el hijo izquierdo.
                return nodo.getIzquierdo();
            }

            // Caso 3: El nodo tiene DOS hijos
            // Para este caso, buscamos el "sucesor in-order" del nodo.
            // El sucesor es el nodo con el valor más pequeño en el subárbol derecho.

            // 1. Encontrar el valor mínimo del subárbol derecho (el sucesor)
            T sucesorValor = encontrarMinimo(nodo.getDerecho());

            // 2. Reemplazar el valor del nodo actual con el valor del sucesor
            nodo.setValor(sucesorValor);

            // 3. Eliminar el nodo sucesor (que ahora está duplicado) del subárbol derecho.
            // Esta llamada recursiva siempre caerá en el Caso 1 o Caso 2,
            // porque el sucesor (el nodo más a la izquierda) no puede tener hijo izquierdo.
            nodo.setDerecho(eliminarRecursivo(nodo.getDerecho(), sucesorValor));
        }

        // Devuelve el nodo actual (con sus hijos potencialmente modificados)
        // para que el padre en la pila de recursión se re-enlace correctamente.
        return nodo;
    }

    /**
     * Método auxiliar para encontrar el valor mínimo en un subárbol dado (el sucesor).
     * Se utiliza en el Caso 3 de la eliminación.
     * * @param nodo La raíz del subárbol donde buscar el mínimo.
     * @return El valor (T) más pequeño de ese subárbol.
     */
    private T encontrarMinimo(NodoDeArbol<T> nodo) {
        NodoDeArbol<T> actual = nodo;
        
        // El valor mínimo siempre es el nodo más a la izquierda.
        while (actual.getIzquierdo() != null) {
            actual = actual.getIzquierdo();
        }
        return actual.getValor();
    }
}