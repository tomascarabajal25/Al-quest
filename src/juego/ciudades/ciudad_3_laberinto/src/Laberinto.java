package juego.ciudades.ciudad_3_laberinto.src;

import java.util.ArrayList;
import java.util.List;

public class Laberinto {

    private Celda[][] grilla;
    private int filas;
    private int columnas;
    private Celda celdaInicio;
    private Celda celdaFin;
    
    //Constructor del laberinto
    public Laberinto(Celda[][] grilla, int filas, int columnas, Celda celdaInicio, Celda celdaFin) {
        this.grilla = grilla;
        this.filas = filas;
        this.columnas = columnas;
        this.celdaInicio = celdaInicio;
        this.celdaFin = celdaFin;
    }

    /**
     * Obtiene las celdas transitables en forma de cruz.
     * Verifica la validez de las posiciones en cada direccion
     */
    public List<Celda> obtenerCeldasTransitables(Celda celda) {
        List<Celda> vecinos = new ArrayList<>();
        int f = celda.getFila();
        int c = celda.getColumna();

        int[][] direcciones = {
            {f-1, c},
            {f+1, c},
            {f, c-1},
            {f, c+1}
        };

        for (int[] dir : direcciones) {
            if (posicionValida(dir[0], dir[1])) {
                Celda vecino = grilla[dir[0]][dir[1]];
                if (vecino.esTransitable()) {
                    vecinos.add(vecino);
                } 
            }
        }
        return vecinos;
    }

    /**
     * Reinicia el laberinto a su estado inicial.
     * Establece las celdas de inicio y fin en sus posiciones originales.
     */
    public void reiniciarLaberinto() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                EstadoCelda estado = grilla[i][j].getEstadoCelda();
                if (estado == EstadoCelda.DESCARTADA
                    || estado == EstadoCelda.EN_CAMINO
                    || estado == EstadoCelda.SOLUCION) {
                        grilla[i][j].setEstadoCelda(EstadoCelda.LIBRE);
                }
                getCeldaInicio().setEstadoCelda(EstadoCelda.INICIO);
                getCeldaFin().setEstadoCelda(EstadoCelda.FIN);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                switch (grilla[i][j].getEstadoCelda()) {
                    case PARED: sb.append("# "); break;
                    case LIBRE: sb.append(". "); break;
                    case INICIO: sb.append("+ "); break;
                    case FIN: sb.append("- "); break;
                    case DESCARTADA: sb.append("x "); break;
                    case EN_CAMINO: sb.append("o "); break;
                    case SOLUCION: sb.append("V "); break;
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Verifica que el laberinto se encuentra dentro de laberinto.
     * @param fila
     * @param columna
     * @return
     */
    public boolean posicionValida(int fila, int columna) {
        return fila >= 0 && fila < filas && columna >= 0 && columna < columnas;
    }

    //  GETTERS
    
    public Celda getCelda(int fila, int columna) {
        return grilla[fila][columna];
    }

    public Celda getCeldaInicio() {
        return celdaInicio;
    }

    public Celda getCeldaFin() {
        return celdaFin;
    }

    public int getFilas() {
        return filas;
    }

    public int getColumnas() {
        return columnas;
    }
}