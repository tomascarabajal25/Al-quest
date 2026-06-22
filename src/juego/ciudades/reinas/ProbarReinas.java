package juego.ciudades.reinas;
import modelos.Jugador;
import modelos.Sonido;

public class ProbarReinas {
    public static Sonido sonido = new Sonido();
    public static void main(String[] args) {
        PartidaReinas partidaReinas=new PartidaReinas(new Jugador("hola"), sonido);
        partidaReinas.iniciar();
}}
