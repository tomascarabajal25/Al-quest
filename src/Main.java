import Juego.PartidaAiQuest;
import modelos.Jugador;

public class Main {
    public static void main(String[] args) {
        Jugador jugador = new Jugador("Héroe");
        PartidaAiQuest partida = new PartidaAiQuest(jugador);
        partida.iniciar();
    }
}