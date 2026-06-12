package juego.ciudades.batalla.controller;

import estructuras.cola.Cola;
import estructuras.pilas.Pila;
import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.view.*;

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

	private static final long ACTION_DELAY_MS = 900;
	private static final long TURN_END_DELAY_MS = 600;

	public boolean empezar() {
		Combatiente heroe = turnos.peek();

		while (
				heroe != null &&
				heroe.estaVivo() &&
				!enemigos.isEmpty()
		) {
			Combatiente actual = turnos.remove();

			if (!actual.estaVivo()) {
				enemigos.remove(actual);
				continue;
			}

			Pila<Accion> acciones;
			try {
				acciones = (actual instanceof Heroe)
						? ui.solicitarAcciones(dificultad)
						: ManagerBatalla.elegirAccionEnemigo((Enemigo) actual, heroe);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				break;
			}

			if (acciones != null && !acciones.isEmpty()) {
				List<Accion> lista = new java.util.ArrayList<>();
				while (!acciones.isEmpty()) {
					lista.add(acciones.pop());
				}

				for (int i = 0; i < lista.size(); i++) {
					Accion a = lista.get(i);
					a.ejecutar();

					String msg;
					if (a instanceof juego.ciudades.batalla.model.acciones.Atacar) {
						msg = a.getCombatiente().getNombre() + " atacó a " + a.getObjetivo().getNombre() + "!";
					} else {
						msg = a.getCombatiente().getNombre() + " usó " + a.getTipo().name().toLowerCase() + "!";
					}

					ui.actualizarEstado(msg, actual);

					enemigos.removeIf(e -> !e.estaVivo());

					try {
						Thread.sleep(ACTION_DELAY_MS);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
						break;
					}
				}
			} else {
				ui.actualizarEstado(null, actual);
			}

			enemigos.removeIf(e -> !e.estaVivo());

			if (!enemigos.isEmpty() && heroe.estaVivo()) {
				try {
					Thread.sleep(TURN_END_DELAY_MS);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}

			turnos.offer(actual);
		}
		return heroe != null && heroe.estaVivo();
	}
}
