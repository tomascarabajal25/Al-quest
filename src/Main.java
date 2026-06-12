


import modelos.Jugador;
import modelos.PartidaGeneral;

public class Main {
    public static void main(String[] args) {
        Jugador jugador = new Jugador("Héroe");
        PartidaGeneral partida = new PartidaGeneral(jugador);
        partida.iniciar();
    }
}