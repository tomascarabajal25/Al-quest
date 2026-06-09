package ciudad_3_laberinto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CargadorLaberinto {
    
    private static final char PARED = '1';

    private static final char LIBRE = '0';

    private static final char INICIO = 'I';

    private static final char FINAL = 'F';

    /**
     * Lee el archivo del laberinto
     * @return el laberinto cargado
     * @throws IOException
     */
    public Laberinto cargar(String ruta) throws IOException {
        List<String[]> lineas = leerLineas(ruta);

        int filas = lineas.size();
        int columnas = lineas.get(0).length;

        validarDimensiones(lineas, columnas);

        Celda[][] grilla = new Celda[filas][columnas];
        Celda celdaInicio = null;
        Celda celdaFin = null;

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                char simbolo = lineas.get(i)[j].charAt(0);
                EstadoCelda estado = parsearSimbolo(simbolo, i, j);
                grilla[i][j] = new Celda(i, j, estado);

                if (estado == EstadoCelda.INICIO) {
                    celdaInicio = grilla[i][j];
                } else if (estado == EstadoCelda.FIN) {
                    celdaFin = grilla[i][j];
                }
            }
        }

        validarInicioYFin(celdaInicio, celdaFin);

        return new Laberinto(grilla, filas, columnas, celdaInicio, celdaFin);
    }


    /**
     * Metodo encargado de leer el archivo .txt
     * @param ruta acceso al laberinto
     * @return las lineas del archivo a procesar
     * @throws IOException si el archivo esta vacio
     */
    private List<String[]> leerLineas(String ruta) throws IOException {
        List<String[]> lineas = new ArrayList<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = lector.readLine()) != null) {
                linea = linea.trim();
                if (!linea.isEmpty()) {
                    lineas.add(linea.split(" "));
                }
            }
        }

        if (lineas.isEmpty()) {
            throw new IllegalArgumentException("El archivo esta vacio: " + ruta);
        }

        return lineas;
    }

    /**
     * Valida que las dimensiones del laberinto cargado coincide con los valores leidos del archivo
     */
    private void validarDimensiones(List<String[]> lineas, int columnas) {
        for (int i = 0; i < lineas.size(); i++) {
            if (lineas.get(i).length != columnas) {
                throw new IllegalArgumentException(
                    "La fila " + i + " tiene " + lineas.get(i).length 
                    + " columnas, se esperaban " + columnas
                );
            }
        }
    }

    /**
     * Interpreta los simbolos del laberinto para representar cada casilla
     * @throws IOException si se usa un simbolo invalido o desconocido en el archivo
     */
    private EstadoCelda parsearSimbolo(char simbolo, int fila, int columna) {
        switch (simbolo) {
            case PARED:  return EstadoCelda.PARED;
            case LIBRE:  return EstadoCelda.LIBRE;
            case INICIO: return EstadoCelda.INICIO;
            case FINAL:    return EstadoCelda.FIN;
            default:
                throw new IllegalArgumentException(
                    "Simbolo desconocido '" + simbolo 
                    + "' en posicion [" + fila + "," + columna + "]"
                );
        }
    }

    /**
     * Verifica que exista en el laberinto una celda de inicio como una celda final
     * @param celdaInicio
     * @param celdaFin
     */
    private void validarInicioYFin(Celda celdaInicio, Celda celdaFin) {
        if (celdaInicio == null) {
            throw new IllegalArgumentException(
                "El laberinto no tiene celda de inicio (S)"
            );
        }
        if (celdaFin == null) {
            throw new IllegalArgumentException(
                "El laberinto no tiene celda de fin (E)"
            );
        }
    }
}
