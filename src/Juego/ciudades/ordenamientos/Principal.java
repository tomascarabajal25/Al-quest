package Juego.ciudades.ordenamientos;


import modelos.Jugador;


public class Principal {

    public static void main(String[] args) {

        Jugador jugador = new Jugador("Tester");

        PartidaOrdenamientos partida = new PartidaOrdenamientos(
                "Ciudad Ordenamientos",
                jugador
        );


        // 7. Iniciar — configura el minijuego en el mundo y arranca el hilo
        partida.iniciar();
    }
}