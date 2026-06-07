package Juego.ciudades.torresDeHanoi;

import modelos.Jugador;

public class Principal {

    public static void main(String... strings) {
        // PartidaHanoi ahora recibe (discos, nombre, jugador) — todo en una clase.
        PartidaHanoi partida = new PartidaHanoi(3, "ciudad ordenamiento", new Jugador("h"));
        partida.iniciar();
    }
}