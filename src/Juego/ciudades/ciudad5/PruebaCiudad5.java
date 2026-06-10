package Juego.ciudades.ciudad5;

import modelos.Jugador;


public class PruebaCiudad5 {

    public static void main(String[] args) {
        System.out.println("--- PRUEBA CIUDAD 5 ---");

        Jugador jugador = new Jugador("Tester");

        // ── Lanzar partida ────────────────────────────────────────────────
        PartidaBusqueda partida = new PartidaBusqueda("Nivel de Prueba", jugador);
        partida.iniciar();

        System.out.println("--- PRUEBA FINALIZADA ---");
    }

}