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

    //CONSTANTES
    private static final int CANTIDAD_SLOTS = 7; //Cantidad de slots de la tabla. Primo es mas comodo.


    public static void main(String[] args) {

        // Jugador logico
        Jugador jugador = new Jugador("Tester");

        // Elementos a insertar (clave + nombre).
        List<ElementoHash> elementos = new ArrayList<>();
        elementos.add(new ElementoHash(10, "Pocion"));
        elementos.add(new ElementoHash(22, "Escudo"));
        elementos.add(new ElementoHash(31, "Llave"));
        elementos.add(new ElementoHash(4,  "Mapa"));
        elementos.add(new ElementoHash(17, "Gema"));
        elementos.add(new ElementoHash(8,  "Antorcha"));

        // Claves a buscar
        List<Integer> clavesABuscar = new ArrayList<>();
        clavesABuscar.add(31);
        clavesABuscar.add(99);
        clavesABuscar.add(4);

        //Posiciones de los slots, repartidas por el mapa (x:columna , y:fila)
        //Asi el jugador tiene que pensar en que Slot va el elemento
        //Ademas, tendra que acordarse las ubicaciones de los slots
        //El mapa world02 es de 50x50, con celda 1 a 48 caminables.
        List<Point> posicionesSlots = new ArrayList<>();
        posicionesSlots.add(new Point(4,     4));   // slot 0 - arriba a la izquierda
        posicionesSlots.add(new Point(40,    40));   // slot 1 - abajo a la derecha
        posicionesSlots.add(new Point(11,    9));   // slot 2 - arriba a la izquierda
        posicionesSlots.add(new Point(6,     40));   // slot 3 - abajo a la izquierda
        posicionesSlots.add(new Point(23,    22));   // slot 4 - en el medio
        posicionesSlots.add(new Point(40,    6));   // slot 5 - arriba a la derecha
        posicionesSlots.add(new Point(38,    24));   // slot 6 - medio a la derecha

        //Crear la partida
        PartidaHashing partida = new PartidaHashing("Ciudad Hashing", jugador, CANTIDAD_SLOTS,
                                                    elementos, clavesABuscar, posicionesSlots);

                        
        //Iniciar, monta el minijuego en el mundo, abre la ventana y arranca hilo
        partida.iniciar();
    }
    



    
}
