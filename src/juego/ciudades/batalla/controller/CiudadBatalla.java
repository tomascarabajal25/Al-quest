package com.aiquest.juego.ciudades.batalla.controller;

import com.aiquest.estructuras.cola.Cola;
import com.aiquest.estructuras.pilas.Pila;
import com.aiquest.juego.ciudades.batalla.model.*;
import com.aiquest.juego.ciudades.batalla.view.UiManager;

import java.util.List;
import java.util.Scanner;

public class CiudadBatalla {
	public static void main(String[] args) {
		// TODO: recibir al jugador y encajarlo en la batalla

		Scanner scanner = new Scanner(System.in);
		int dificultad = 0;
		while (dificultad < 1 || dificultad > 3) {
			UiManager.imprimirDificultad();
			dificultad = scanner.nextInt();
			scanner.nextLine();
			if (dificultad < 1 || dificultad > 3) {
				System.out.println("Invalid dificultad");
			}
		}

		// TODO: crear habilidades especiales
		HabilidadEspecial ninguna = (personaje, objetivo) -> {};
		Heroe heroe = new Heroe("Heroe", 100, 15, 5, ninguna);
		List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(dificultad);
		Cola<Combatiente> turnos = new Cola<Combatiente>();
		turnos.add(heroe);
		turnos.addAll(enemigos);

		boolean victoria = new Batalla(scanner, turnos).empezar();
		System.out.println(victoria ? "\nVictoria!" : "\nDerrota...");
		scanner.close();
	}
}
