package Juego.ciudades.ciudad5;

import modelos.Jugador;
import modelos.Mapa;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PruebaCiudad5 {

    public static void main(String[] args) {
        System.out.println("--- PRUEBA CIUDAD 5 ---");

        Jugador jugador = new Jugador("Tester");

        // ── Opción A: leer desde archivo de texto ─────────────────────────
        // El archivo debe tener una palabra por celda separadas por espacios,
        // una fila por línea. Ejemplo:
        //   hola mundo java arbol
        //   lista estructuras match parcial
        //
        // Descomentá este bloque y comentá el bloque B para usar archivo real.

        /*
        Mapa mapa = cargarDesdeArchivo("recursos/texto_prueba.txt");
        if (mapa == null) {
            System.out.println("No se pudo leer el archivo.");
            return;
        }
        */

        // ── Opción B: mapa hardcodeado para prueba rápida ─────────────────
        String[][] palabras = {
            {"hola",  "mundo",    "java",   "arbol"},
            {"lista", "busqueda", "nodo",   "raiz"},
            {"clave", "arbol",    "hoja",   "java"},
            {"mundo", "nodo",     "hola",   "lista"}
        };
        Mapa mapa = cargarDesdeMatriz(palabras);

        // ── Lanzar partida ────────────────────────────────────────────────
        PartidaBusqueda partida = new PartidaBusqueda("Nivel de Prueba", jugador, mapa);
        partida.iniciar();

        System.out.println("--- PRUEBA FINALIZADA ---");
    }

    /**
     * Carga un Mapa desde un archivo de texto plano.
     * Cada línea es una fila; las palabras dentro de la línea se separan por espacios.
     *
     * pre:  rutaArchivo no nula, el archivo existe y es legible
     * post: devuelve un Mapa con las palabras del archivo, o null si hubo error
     */
    private static Mapa cargarDesdeArchivo(String rutaArchivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {

            // Primera pasada: contar filas y columnas máximas
            java.util.List<String[]> filas = new java.util.ArrayList<>();
            String linea;
            int maxColumnas = 0;

            while ((linea = br.readLine()) != null) {
                String[] tokens = linea.trim().split("\\s+");
                filas.add(tokens);
                if (tokens.length > maxColumnas) {
                    maxColumnas = tokens.length;
                }
            }

            if (filas.isEmpty()) {
                System.out.println("El archivo está vacío.");
                return null;
            }

            Mapa mapa = new Mapa(filas.size(), maxColumnas);

            for (int i = 0; i < filas.size(); i++) {
                String[] fila = filas.get(i);
                for (int j = 0; j < fila.length; j++) {
                    String palabra = fila[j].trim();
                    if (!palabra.isEmpty() && !palabra.equals("-")) {
                        mapa.ocuparCelda(palabra, i, j);
                        System.out.println("Cargado (" + i + "," + j + ") -> " + palabra);
                    }
                }
            }

            System.out.println("Archivo cargado: " + filas.size()
                    + " filas x " + maxColumnas + " columnas.");
            return mapa;

        } catch (IOException e) {
            System.out.println("Error leyendo archivo: " + e.getMessage());
            return null;
        }
    }

    /**
     * Carga un Mapa desde una matriz de Strings hardcodeada (para prueba rápida).
     *
     * pre:  matriz no nula, al menos 1 fila y 1 columna
     * post: devuelve un Mapa con las palabras de la matriz
     */
    private static Mapa cargarDesdeMatriz(String[][] matriz) {
        int filas    = matriz.length;
        int columnas = matriz[0].length;
        Mapa mapa    = new Mapa(filas, columnas);

        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                String palabra = matriz[i][j];
                if (palabra != null && !palabra.isEmpty()) {
                    mapa.ocuparCelda(palabra.trim(), i + 1, j + 1);
                    System.out.println("Insertado (" + i + "," + j + ") -> " + palabra);
                }
            }
        }

        System.out.println("Mapa cargado: " + filas + " filas x " + columnas + " columnas.");
        return mapa;
    }
}