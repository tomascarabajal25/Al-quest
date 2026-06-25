package juego.ciudades.ciudad5;

import modelos.Jugador;
import modelos.Sonido;


public class PruebaCiudad5 {
    public static Sonido sonido = new Sonido();

    public static void main(String[] args) {
        System.out.println("--- PRUEBA CIUDAD 5 ---");

        Jugador jugador = new Jugador("Tester");

        // ── Lanzar partida ────────────────────────────────────────────────
        PartidaBusqueda partida = new PartidaBusqueda("Nivel de Prueba", jugador, sonido);
        partida.iniciar();

        System.out.println("--- PRUEBA FINALIZADA ---");
    }

}