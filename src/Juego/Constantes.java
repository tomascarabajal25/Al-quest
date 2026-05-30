package Juego;

public class Constantes {
    // PARTIDA
    /** Puntos iniciales de la partida */
    public static final int PUNTOS_INICIALES_PARTIDA = 0;

    // OBJETOS
    /** Caracter correspondiente a celda vacia */
    public static final char CARACTER_VACIO = '-';
    /** Caracter correspondiente al jugador */
    public static final char CARACTER_JUGADOR = '♟';
    /** Caracter correspondiente a la carta visibilidad */
    public static final char CARACTER_CARTA_VISIBILIDAD = '☯';
    /** Caracter correspondiente a la carta desplazamiento */
    public static final char CARACTER_CARTA_DESPLAZAMIENTO = '⚡';
    /** Caracter correspondiente a la carta puntos */
    public static final char CARACTER_CARTA_PUNTOS = '★';

    // CONTROLES
    /** Tecla para mover hacia arriba */
    public static final char MOVER_ARRIBA = 'W';
    /** Tecla para mover hacia abajo */
    public static final char MOVER_ABAJO = 'S';
    /** Tecla para mover hacia la izquierda */
    public static final char MOVER_IZQUIERDA = 'A';
    /** Tecla para mover hacia la derecha */
    public static final char MOVER_DERECHA = 'D';
    /** Tecla para entrar a la mochila */
    public static final char MENU_MOCHILA = 'P';



    // CONFIGURACIONES DE CIUDAD RECOLECCION
    // --- MAPA ---
    /** Filas del mapa */
    public static final int FILAS_MAPA = 20;
    /** Columnas del mapa */
    public static final int COLUMNAS_MAPA = 35;
    /** Niveles del mapa */
    public static final int NIVELES_MAPA = 3;

    // --- JUGADOR
    /** Visibilidad inicial del jugador */
    public static final int VISIBILIDAD_INICIAL = 1;
    /** Desplazamiento inicial del jugador */
    public static final int DESPLAZAMIENTO_INICIAL = 1;
    // --- MOCHILA
    /** Capacidad maxima de la mochila */
    public static final int CAPACIDAD_MAXIMA_MOCHILA = 3;
    // --- CARTAS ---
    /** Puntos entregados al encontrar la carta de visibilidad */
    public static final int PUNTAJE_VISIBILIDAD = 5000;
    /** Puntos entregados al encontrar la carta de visibilidad */
    public static final int PUNTAJE_DESPLAZAMIENTO = 3000;
    /** Aumento de visibilidad por CartaVision */
    public static final int CANTIDAD_AUMENTO_VISIBILIDAD = 1;
    /** Aumento de desplazamiento por CartaDesplazamiento */
    public static final int CANTIDAD_AUMENTO_DESPLAZAMIENTO = 1;
    /** Aumento de puntos por CartaPuntos */
    public static final int CANTIDAD_AUMENTO_PUNTOS = 3;
}
