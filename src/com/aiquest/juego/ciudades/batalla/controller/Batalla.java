package com.aiquest.juego.ciudades.batalla.controller;

import com.aiquest.utils.Utils;
import com.aiquest.estructuras.pilas.Pila;
import com.aiquest.juego.ciudades.batalla.model.Accion;
import com.aiquest.juego.ciudades.batalla.model.Enemigo;
import com.aiquest.juego.ciudades.batalla.model.acciones.Atacar;
import com.aiquest.juego.ciudades.batalla.model.acciones.Defender;
import com.aiquest.juego.ciudades.batalla.view.UiManager;
import com.aiquest.juego.ciudades.batalla.model.Combatiente;

import java.util.Scanner;

public class Batalla {
	private final Scanner scanner;
	private final Combatiente combatiente;
	private final Enemigo enemigo;
	private final Pila<Accion> pilaAcciones;

	public Batalla(Scanner scanner, Combatiente combatiente, Enemigo enemigo, Pila<Accion> pila) {
		this.scanner = scanner;
		this.combatiente = combatiente;
		this.enemigo = enemigo;
		this.pilaAcciones = pila;
	}

	// Start combat (For each turn)
	public boolean startCombat() {
		System.out.println("\nUn " + this.enemigo.getNombre() + " aparece!");

		Utils.sleep(1000);

		while (this.enemigo.estaVivo() && this.combatiente.estaVivo()) {

			UiManager.imprimirEstado(combatiente, enemigo);
			Utils.sleep(1000);
			UiManager.imprimirAcciones();
			int accion = scanner.nextInt();
			scanner.nextLine();
			switch (accion) {
				case 1:
					pilaAcciones.push(new Atacar(combatiente, enemigo));
					break;
				case 2:
					pilaAcciones.push(new Defender(combatiente, enemigo));
					break;
				default:
					System.out.println("Opcion invalida");
					continue;
			}

			System.out.println("----------------------------");
			ManagerBatalla.ejecutarAcciones(this.pilaAcciones);

			if (enemigo.estaVivo() && combatiente.estaVivo()) {
				System.out.println("\nTurno del enemigo...");
				Utils.sleep(1000);
				pilaAcciones.push(new Atacar(enemigo, combatiente));
				ManagerBatalla.ejecutarAcciones(this.pilaAcciones);
				Utils.sleep(1000);
				System.out.println("----------------------------\n");
			}

			Utils.sleep(1000);
		}
		return this.combatiente.estaVivo();
	}
}