package juego;

import java.awt.*;

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
    // --- UI ---
    public static final int ANCHO  = 220;
    public static final int ALTO   = 720;
    public static final Color COLOR_FONDO     = new Color(22, 22, 22);
    public static final Color COLOR_TITULO    = new Color(190, 150, 65);
    public static final Color COLOR_LABEL     = new Color(135, 120, 100);
    public static final Color COLOR_VALOR     = new Color(205, 185, 150);
    public static final Color COLOR_SEPARADOR = new Color(60, 55, 48);
    public static final Color COLOR_VISION       = new Color(90, 150, 200);
    public static final Color COLOR_DESPLAZ       = new Color(185, 150, 50);
    public static final Color COLOR_PUNTOS_C      = new Color(170, 45, 45);
    public static final Color COLOR_JUGADOR       = new Color(200, 200, 200);

    public static final int TILE_SIZE    = 32;   // px por celda
    public static final int PADDING      = 12;   // margen interior del panel
    // Paleta de colores del juego
    public static final Color COLOR_CELDA     = new Color(45, 43, 40);
    public static final Color COLOR_CELDA_BORDE = new Color(30, 28, 26);
    public static final Color COLOR_CARTA_VISION = new Color(90, 150, 200);
    public static final Color COLOR_CARTA_DESPLAZ = new Color(185, 150, 50);
    public static final Color COLOR_CARTA_PUNTOS  = new Color(170, 45, 45);
    public static final Color COLOR_VACIO     = new Color(45, 43, 40);
    public static final Color COLOR_MENSAJE       = new Color(210, 175, 60);
    public static final Color COLOR_MOCHILA_FONDO = new Color(15, 15, 15, 220);

    // Tipos de tile para el cache
    public static final String TILE_VACIO      = "vacio";
    public static final String TILE_JUGADOR    = "jugador";
    public static final String TILE_VISION     = "vision";
    public static final String TILE_DESPLAZ    = "desplaz";
    public static final String TILE_PUNTOS     = "puntos";

}
