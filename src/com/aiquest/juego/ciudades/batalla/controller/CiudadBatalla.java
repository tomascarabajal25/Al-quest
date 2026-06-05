package com.aiquest.juego.ciudades.batalla.controller;

import com.aiquest.estructuras.pilas.Pila;
import com.aiquest.estructuras.cola.Cola;
import com.aiquest.juego.ciudades.batalla.model.Heroe;
import com.aiquest.juego.ciudades.batalla.model.Accion;
import com.aiquest.juego.ciudades.batalla.model.Enemigo;
import com.aiquest.juego.ciudades.batalla.view.UiManager;
import com.aiquest.juego.ciudades.batalla.model.HabilidadEspecial;

import java.util.Scanner;

public class CiudadBatalla {
	public static void main(String[] args) {
		// TODO: recibir al jugador y encajarlo en la batalla

		Scanner scanner = new Scanner(System.in);

		// TODO: crear habilidades especiales
		HabilidadEspecial ninguna = (personaje, objetivo) -> {};

		Heroe heroe = new Heroe("Heroe", 100, 15, 5, ninguna);

		int dificultad = 0;
		while (dificultad < 1 || dificultad > 3) {
			UiManager.imprimirDificultad();
			dificultad = scanner.nextInt();
			scanner.nextLine();
			if (dificultad < 1 || dificultad > 3) {
				System.out.println("Invalid dificultad");
			}
		}

		Cola<Enemigo> cola = ManagerBatalla.generarEnemigos(dificultad);
		Pila<Accion> pila = new Pila<>();

		Enemigo enemigo = cola.poll();
		boolean victoria = false;
		while (enemigo != null) {
			Batalla batalla = new Batalla(scanner, heroe, enemigo, pila);
			victoria = batalla.startCombat();
			enemigo = cola.poll();
		}

		System.out.println(victoria ? "\nVictoria!" : "\nDerrota...");
		scanner.close();
	}
}
