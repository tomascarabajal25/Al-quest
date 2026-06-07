package Juego.ciudades.ordenamientos;

import java.util.List;
import modelos.Jugador;
import modelos.Vista;

public class Principal {

    public static void main(String[] args) {

        // 1. Jugador lógico
        Jugador jugador = new Jugador("Tester");

        // 2. Vista del juego (ruta al mapa de la ciudad de ordenamientos)
        Vista vista = new Vista("/maps/world02.txt");

        // 3. Cajas a ordenar
        List<Caja> cajas = List.of(
                new Caja("A", 40, true),
                new Caja("B", 10, true),
                new Caja("C", 30, true),
                new Caja("D", 25, true),
                new Caja("E", 35, true)
        );

        // 4. Algoritmo elegido (Bubble o Selection — ya seleccionado antes de iniciar)
        Ordenador<Caja> ordenador = new OrdenadorBubble<>("Bubble Sort");
        // Ordenador<Caja> ordenador = new OrdenadorSelection<>("Selection Sort");

        // 5. Crear la partida
        //    filaBase=20, colInicio=22 → posición en el mapa donde aparecen las cajas
        PartidaOrdenamientos partida = new PartidaOrdenamientos(
                "Ciudad Ordenamientos",
                jugador,
                cajas,
                ordenador,
                vista,
                20,   // fila del mundo
                22    // columna inicial
        );


        // 7. Iniciar — configura el minijuego en el mundo y arranca el hilo
        partida.iniciar();
    }
}