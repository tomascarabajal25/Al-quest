package com.aiquest.juego.ciudades.batalla.view;

import com.aiquest.juego.ciudades.batalla.model.Combatiente;
import com.aiquest.juego.ciudades.batalla.model.Enemigo;

import java.util.List;
import java.util.Scanner;

public class UiManager {
	public static void imprimirEstado(Combatiente p1) {
		System.out.println("-----------Estado-----------");
		System.out.println(p1.toString());
		System.out.println("----------------------------\n");
	}

	public static void imprimirAcciones() {
		System.out.println("1. \u2694\uFE0F Atacar");
		System.out.println("2. \uD83D\uDEE1\uFE0F Defender");
//		System.out.println("3. \uD83E\uDDEA Use a healing potion");
//		System.out.println("4. Display your stats");
		System.out.print("Ingrese acción: ");
	}

	public static void imprimirDificultad() {
		System.out.println("Dificultades:");
		System.out.println("1. Facil");
		System.out.println("2. Media");
		System.out.println("3. Dificil");
		System.out.println("Seleccionar dificultad: ");
	}

	public static Enemigo seleccionarEnemigo(List<Enemigo> enemigos, Scanner scanner) {
		if (enemigos == null || enemigos.isEmpty()) return null;

		System.out.println("--- Elija objetivo ---");
		for (int i = 0; i < enemigos.size(); i++) {
			System.out.println((i + 1) + ". " + enemigos.get(i));
		}

		int opcion = 0;
		while (opcion < 1 || opcion > enemigos.size()) {
			System.out.print("Ingrese objetivo: ");
			if (!scanner.hasNextInt()) {
				scanner.nextLine();
				System.out.println("Opcion invalida");
				continue;
			}
			opcion = scanner.nextInt();
			scanner.nextLine();
			if (opcion < 1 || opcion > enemigos.size()) {
				System.out.println("Opcion invalida");
			}
		}
		return enemigos.get(opcion - 1);
	}

	public static int seleccionarAccion(Scanner scanner) {
		int opcion = 0;
		while (opcion < 1 || opcion > 2) {
			imprimirAcciones();
			if (!scanner.hasNextInt()) {
				scanner.nextLine();
				System.out.println("Opcion invalida");
				continue;
			}
			opcion = scanner.nextInt();
			scanner.nextLine();
			if (opcion < 1 || opcion > 2) {
				System.out.println("Opcion invalida");
			}
		}
		return opcion;
	}
}
