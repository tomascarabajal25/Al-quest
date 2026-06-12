package juego.ciudades.hashing;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import modelos.Jugador;

/**
 * Main de prueba INDEPENDIENTE de la Ciudad 6 HASHING con interfaz grafica.
 * as
 * Sirve para probar la ciudad por separado, no depende del resto del juego.
 * Crea un jugador, una Vista con un mapa, define los elementos a insertar y las claves
 * a buscar, y arranca tambien la PartidaHAshing (que abre la ventana).
 * 
 * Cuando se integre al juego completo, sera PartidaAiQuest quien cree la
 * PartidaHashing, como dije antes, este Principal es solo un banco de pruebas.
 * 
 */

public class Principal {


    public static void main(String[] args) {

        Jugador jugador = new Jugador("Tester");

        PartidaHashing partida = new PartidaHashing("Ciudad Hashing", jugador);

                        
        //Iniciar, monta el minijuego en el mundo, abre la ventana y arranca hilo
        partida.iniciar();
    }
    



    
}
