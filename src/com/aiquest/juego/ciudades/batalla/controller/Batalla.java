package com.aiquest.juego.ciudades.batalla.controller;

import com.aiquest.juego.ciudades.batalla.model.Heroe;
import com.aiquest.estructuras.cola.Cola;
import com.aiquest.estructuras.pilas.Pila;
import com.aiquest.juego.ciudades.batalla.model.Accion;
import com.aiquest.juego.ciudades.batalla.model.Enemigo;
import com.aiquest.juego.ciudades.batalla.view.BatallaUI;
import com.aiquest.juego.ciudades.batalla.model.Combatiente;

import java.util.List;

public class Batalla {
	private final BatallaUI ui;
	private final Cola<Combatiente> turnos;
	private final List<Enemigo> enemigos;
	private final int dificultad;

	public Batalla(BatallaUI ui, Cola<Combatiente> turnos, List<Enemigo> enemigos, int dificultad) {
		this.ui = ui;
		this.turnos = turnos;
		this.enemigos = enemigos;
		this.dificultad = dificultad;
	}

	public boolean empezar() {
		Combatiente heroe = turnos.peek();

		while (
				heroe != null &&
				heroe.estaVivo() &&
				!enemigos.isEmpty()
		) {
			System.out.println("Comenzando batalla " + heroe);
			Combatiente actual = turnos.remove();
			System.out.println("Actualizando batalla " + actual);

			if (!actual.estaVivo()) {
				System.out.println("Eliminando de batalla " + actual);
				enemigos.remove(actual);
				continue;
			}

			System.out.println("Mostrando estado de " + actual);
			ui.actualizarEstado("Turno de " + actual.getNombre(), actual);

			Pila<Accion> acciones;
			try {
				acciones = (actual instanceof Heroe)
						? ui.solicitarAcciones(dificultad)
						: ManagerBatalla.elegirAccionEnemigo((Enemigo) actual, heroe);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}

			if (acciones != null) { ManagerBatalla.ejecutarAcciones(acciones); }

			turnos.offer(actual);

			ui.actualizarEstado(null, actual);
		}
		return heroe != null && heroe.estaVivo();
	}
}
