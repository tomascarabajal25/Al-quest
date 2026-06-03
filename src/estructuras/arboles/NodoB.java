package com.aiquest.estructuras.arboles;

class NodoB {
    int[] claves;
    int gradoMinimo; // Grado mínimo
    NodoB[] hijos;
    int grado; // Número actual de claves
    boolean hoja;

    public NodoB(int t, boolean hoja) {
        this.gradoMinimo = t;
        this.hoja = hoja;
        this.claves = new int[2 * t - 1];
        this.hijos = new NodoB[2 * t];
        this.grado = 0;
    }
}
