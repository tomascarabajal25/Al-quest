package com.aiquest.juego.ciudades.ciudad5;
/**
     * Interfaz funcional auxiliar para abstraer el comportamiento de indexación. ->ayudin
     */
    @FunctionalInterface 
    public interface AccionPalabra {
    	void ejecutar(String palabra, int fila, int columna);
  }