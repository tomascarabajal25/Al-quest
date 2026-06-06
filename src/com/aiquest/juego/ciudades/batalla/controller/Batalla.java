package com.aiquest.juego.ciudades.batalla.controller;

import com.aiquest.juego.ciudades.batalla.model.Heroe;
import com.aiquest.utils.Utils;
import com.aiquest.estructuras.cola.Cola;
import com.aiquest.estructuras.pilas.Pila;
import com.aiquest.juego.ciudades.batalla.model.Accion;
import com.aiquest.juego.ciudades.batalla.model.Enemigo;
import com.aiquest.juego.ciudades.batalla.view.UiManager;
import com.aiquest.juego.ciudades.batalla.model.Combatiente;
import com.aiquest.juego.ciudades.batalla.model.acciones.Atacar;
import com.aiquest.juego.ciudades.batalla.model.acciones.Defender;

import java.util.List;
import java.util.Scanner;

public class Batalla {
	private final Scanner scanner;
	private final Cola<Combatiente> turnos;
	private final List<Enemigo> enemigos;

	public Batalla(Scanner scanner, Cola<Combatiente> turnos, List<Enemigo> enemigos) {
		this.scanner = scanner;
		this.turnos = turnos;
		this.enemigos = enemigos;
	}

	// Start combat (For each turn)
	public boolean empezar() {
		// siempre arranca el jugador
		Combatiente heroe = turnos.peek();

		while (
				heroe != null &&
				heroe.estaVivo() &&
				!enemigos.isEmpty()
		) {
			// obtenemos al combatiente que jugara su turno, y lo sacamos de la cola
			Combatiente actual = turnos.remove();

			if (!actual.estaVivo()) {
				// seleccionamos un enemigo que murio -> lo sacamos de la lista y no lo volvemos a encolar
				enemigos.remove(actual);
				continue;
			}

			if (actual instanceof Heroe) {
				UiManager.imprimirEstado(heroe);
				turnos.forEach(c -> UiManager.imprimirEstado(c));
			}

			Utils.sleep(1000);

			// si actual es heroe usamos el que ya teniamos, sino -> es enemigo
			Accion accion = (actual instanceof Heroe)
					? ManagerBatalla.elegirAccionHeroe(heroe, enemigos, scanner)  // objetivo -> enemigos
					: ManagerBatalla.elegirAccionEnemigo((Enemigo) actual, heroe);   // objetivo -> heroe

			Pila<Accion> pilaAcciones = new Pila<>();    // nueva pila por turno
			pilaAcciones.push(accion);
			ManagerBatalla.ejecutarAcciones(pilaAcciones);

			// encolamos al personaje que termino su turno
			turnos.offer(actual);

			Utils.sleep(1000);
		}
		return heroe.estaVivo();
	}
}