package com.aiquest.juego.ciudades.batalla.controller;

import com.aiquest.utils.Utils;
import com.aiquest.modelos.Jugador;
import com.aiquest.juego.ciudades.batalla.model.Enemigo;

import java.util.Scanner;

public class Batalla {
	private final Scanner scanner;
	private final Jugador jugador;
	private final Enemigo enemigo;


	public Batalla(Scanner scanner, Jugador jugador, Enemigo enemigo) {
		this.scanner = scanner;
		this.jugador = jugador;
		this.enemigo = enemigo;
	}

	// Start combat (For each turn)
	public boolean startCombat() {
		System.out.println("\nUn " + this.enemigo.getNombre() + " aparece!");

		Utils.sleep(1000);

		while (this.enemigo.estaVivo() && this.jugador.estaVivo()) {

			imprimirEstado(jugador, enemigo);
			Utils.sleep(1000);
			imprimirAcciones();
			int accion = scanner.nextInt();
			scanner.nextLine();
			try {
				ejecutarAccion(accion, this.jugador, this.enemigo);
			} catch (InvalidChoiceException e) {
				System.out.println("\u274C Error: " + e.getMessage());
			}

			Utils.sleep(3000);
		}
		return this.jugador.estaVivo();
	}
}