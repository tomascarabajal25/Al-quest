package com.aiquest.estructuras.arboles;

public class TestDeArbolBinarioDeBusqueda {

    public static void main(String[] args) {
        ArbolBinarioDeBusqueda<String> arbol = new ArbolBinarioDeBusqueda<String>();
        
        // Insertar nodos en el árbol
        arbol.insertar(new String("50"));
        arbol.insertar(new String("30"));
        arbol.insertar(new String("70"));
        arbol.insertar(new String("20"));
        arbol.insertar(new String("40"));
        arbol.insertar(new String("60"));
        arbol.insertar(new String("80"));

        // Buscar un nodo
        System.out.println("¿El valor 40 está en el árbol? " + arbol.buscar( new String("40")));

        // Recorridos
        System.out.print("Inorden: ");
        arbol.inorden();
        System.out.println();

        System.out.print("Preorden: ");
        arbol.preorden();
        System.out.println();

        System.out.print("Postorden: ");
        arbol.postorden();
        System.out.println();
    }
}
