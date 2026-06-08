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
