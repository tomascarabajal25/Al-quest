package juego.ciudades.batalla.model;

import modelos.Jugador;

public class Heroe extends Combatiente {

	public Heroe(String nombre, int vida, int fuerza, int armadura) {
		super(nombre, vida, fuerza, armadura);
	}

	public static Heroe desdeJugador(Jugador jugador, int vida, int fuerza, int armadura) {
		return new Heroe(jugador.getNombre(), vida, fuerza, armadura);
	}
}
