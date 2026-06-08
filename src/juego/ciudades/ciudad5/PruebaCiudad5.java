package juego.ciudades.ciudad5;

import modelos.Jugador;
import modelos.Mapa;
import utils.Teclado;

public class PruebaCiudad5 {

    public static void main(String[] args) {
        System.out.println("--- ENTORNO DE PRUEBA DE CIUDAD 5 (CON NULOS CORREGIDOS) ---");
        
        Teclado.inicializar();
        Jugador jugadorPrueba = new Jugador("Tester");
        
        int ancho = 4;
        int alto = 4;
        
        // Ahora esto NO explota, crea el mapa con celdas internas en null gracias al arreglo en Celda
        Mapa mapaPrueba = new Mapa(ancho, alto); 
        
        // Simulamos el texto. Dejamos algunas celdas vacías como "" para ver que las ignore
        String[][] matrizPalabras = {
            {"hola",  "mundo", "java",  ""},
            {"",      "hola",  "arbol", "estructuras"},
            {"java",  "",      "lista", "hola"},
            {"match", "parcial", "",     "java"}
        };
        
        for (int i = 0; i < ancho; i++) {
            for (int j = 0; j < alto; j++) {
                String palabra = matrizPalabras[i][j];
                if (palabra != null && !palabra.isEmpty()) {
                    mapaPrueba.ocuparCelda(palabra.trim(), i, j);
                    System.out.println("Insertado con éxito en (" + i + "," + j + ") -> " + palabra);
                }
            }
        }

        System.out.println("Mapa inicializado de forma nativa.");
        
        // Lanzamos la partida
        PartidaBusqueda partida = new PartidaBusqueda("Nivel de Prueba", jugadorPrueba, mapaPrueba);
        partida.iniciar();
        
        Teclado.finalizar();
        System.out.println("\n--- PRUEBA FINALIZADA ---");
    }
}