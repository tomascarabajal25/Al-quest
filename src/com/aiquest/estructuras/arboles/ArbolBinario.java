package com.aiquest.estructuras.arboles;


class ArbolBinario {
    NodoB raiz;
    int grado;

    public ArbolBinario(int orden) {
        this.grado = orden;
        this.raiz = null;
    }

    // Insertar una clave
    public void insertar(int clave) {
        if (raiz == null) {
            raiz = new NodoB(grado, true);
            raiz.claves[0] = clave;
            raiz.grado = 1;
        } else {
            // Si la raíz está llena, dividirla
            if (raiz.grado == 2 * grado - 1) {
                NodoB nuevaRaiz = new NodoB(grado, false);
                nuevaRaiz.hijos[0] = raiz;
                dividir(nuevaRaiz, 0, raiz);
                raiz = nuevaRaiz;
            }
            insertarNoLleno(raiz, clave);
        }
    }

    // Método auxiliar para insertar en un nodo no lleno
    private void insertarNoLleno(NodoB nodo, int clave) {
        int i = nodo.grado - 1;
        if (nodo.hoja) {
            while (i >= 0 && clave < nodo.claves[i]) {
                nodo.claves[i + 1] = nodo.claves[i];
                i--;
            }
            nodo.claves[i + 1] = clave;
            nodo.grado++;
        } else {
            while (i >= 0 && clave < nodo.claves[i]) {
                i--;
            }
            i++;
            if (nodo.hijos[i].grado == 2 * grado - 1) {
                dividir(nodo, i, nodo.hijos[i]);
                if (clave > nodo.claves[i]) {
                    i++;
                }
            }
            insertarNoLleno(nodo.hijos[i], clave);
        }
    }

    // Dividir un nodo
    private void dividir(NodoB padre, int i, NodoB nodo) {
        NodoB nuevoNodo = new NodoB(grado, nodo.hoja);
        nuevoNodo.grado = grado - 1;

        System.arraycopy(nodo.claves, grado, nuevoNodo.claves, 0, grado - 1);

        if (!nodo.hoja) {
            System.arraycopy(nodo.hijos, grado, nuevoNodo.hijos, 0, grado);
        }

        nodo.grado = grado - 1;

        for (int j = padre.grado; j >= i + 1; j--) {
            padre.hijos[j + 1] = padre.hijos[j];
        }

        padre.hijos[i + 1] = nuevoNodo;

        for (int j = padre.grado - 1; j >= i; j--) {
            padre.claves[j + 1] = padre.claves[j];
        }

        padre.claves[i] = nodo.claves[grado - 1];
        padre.grado++;
    }

    public void imprimir(NodoB nodo) {
        if (nodo != null) {
            for (int i = 0; i < nodo.grado; i++) {
                System.out.print(nodo.claves[i] + " ");
            }
            System.out.println();
            for (int i = 0; i <= nodo.grado; i++) {
                imprimir(nodo.hijos[i]);
            }
        }
    }

    public void imprimir() {
        imprimir(raiz);
    }

    public static void main(String[] args) {
        ArbolBinario arbol = new ArbolBinario(3);

        arbol.insertar(10);
        arbol.insertar(20);
        arbol.insertar(5);
        arbol.insertar(6);
        arbol.insertar(12);
        arbol.insertar(30);
        arbol.insertar(7);
        arbol.insertar(17);

        System.out.println("Árbol B:");
        arbol.imprimir();
    }
}
