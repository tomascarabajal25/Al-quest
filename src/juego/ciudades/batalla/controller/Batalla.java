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

import java.util.Scanner;

public class Batalla {
	private final Scanner scanner;
	private final Cola<Combatiente> combatientes;

	public Batalla(Scanner scanner, Cola<Combatiente> combatientes) {
		this.scanner = scanner;
		this.combatientes = combatientes;
		this.pilaAcciones =  null;
	}

	// Start combat (For each turn)
	public boolean empezar() {
		// siempre arranca el jugador
		Combatiente heroe = combatientes.peek();

		while (heroe.estaVivo() && !combatientes.isEmpty() && ManagerBatalla.todosVivos(combatientes)) {
			UiManager.imprimirEstado(heroe);
			combatientes.forEach(c -> UiManager.imprimirEstado(c));

			// eliminamos al combatiente actual de la cola
			Combatiente actual = combatientes.remove();

			Utils.sleep(1000);

			Accion accion = (actual instanceof Heroe)
					? ManagerBatalla.elegirAccionJugador()
					: ManagerBatalla.elegirAccionEnemigo();

			Pila<Accion> pilaAcciones = new Pila<>();                // NUEVA por turno, no field
			pilaAcciones.push(accion);                               // spec: pasa por la pila
			ManagerBatalla.ejecutarAcciones(pilaAcciones);           // pop + ejecutar
			cola.offer(actor);

			Utils.sleep(1000);
		}
		return heroe.estaVivo();
	}
}