package juego.ciudades.batalla.model;

import modelos.Jugador;

public class Heroe extends Combatiente {

	public Heroe(String nombre, int vida, int fuerza, int armadura, HabilidadEspecial habilidad) {
		super(nombre, vida, fuerza, armadura, habilidad);
	}

	public static Heroe desdeJugador(Jugador jugador, int vida, int fuerza, int armadura, HabilidadEspecial habilidad) {
		return new Heroe(jugador.getNombre(), vida, fuerza, armadura, habilidad);
	}
}
