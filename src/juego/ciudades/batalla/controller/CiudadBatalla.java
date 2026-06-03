package com.aiquest.juego.ciudades.batalla.controller;

import com.aiquest.estructuras.pilas.Pila;
import com.aiquest.juego.ciudades.batalla.model.Accion;
import com.aiquest.juego.ciudades.batalla.model.Enemigo;
import com.aiquest.juego.ciudades.batalla.model.HabilidadEspecial;
import com.aiquest.juego.ciudades.batalla.model.Heroe;
import com.aiquest.juego.ciudades.batalla.model.TipoEnemigo;

import java.util.Scanner;

public class CiudadBatalla {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		HabilidadEspecial ninguna = (personaje, objetivo) -> {};

		Heroe heroe = new Heroe("Heroe", 100, 15, 5, ninguna);
		Enemigo enemigo = new Enemigo("Goblin", TipoEnemigo.DUENDE, 50, 10, 2, ninguna);

		Pila<Accion> pila = new Pila<>();

		Batalla batalla = new Batalla(scanner, heroe, enemigo, pila);
		boolean victoria = batalla.startCombat();

		System.out.println(victoria ? "\nVictoria!" : "\nDerrota...");
		scanner.close();
	}
}
