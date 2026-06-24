package juego.ciudades.batalla.controller;

import estructuras.cola.Cola;
import estructuras.pilas.Pila;
import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.view.*;
import juego.ciudades.batalla.view.accion.ActionUi;
import juego.ciudades.batalla.view.animacion.Animacion;
import juego.ciudades.batalla.view.animacion.GlowAnimacion;

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

	private static final long TURN_END_DELAY_MS = 600;

	public ResultadoBatalla empezar() {
		int totalEnemigos = enemigos.size();

		// heroe siempre arrancara primero, asi que lo guardamos mirando al primero en la cola
		Combatiente heroe = turnos.peek();

		while (
				heroe != null &&
				heroe.estaVivo() &&
				!enemigos.isEmpty()
		) {
			Combatiente actual = turnos.remove(); // desencolamos

			if (!actual.estaVivo()) {
				enemigos.remove(actual);
				continue;
			}

			// aplicamos estados + ui al pj actual (veneno, sangrado, etc.)
			List<ManagerBatalla.EstadoAplicado> estadoDescripciones = ManagerBatalla.aplicarEstados(actual);
			ManagerBatalla.registrarAnimaciones(estadoDescripciones, ui, actual);

			Pila<Accion> acciones;
			if (actual instanceof Heroe) {
				acciones = ManagerBatalla.elegirAccionesHeroe(dificultad, ui);
			} else {
				acciones = ManagerBatalla.elegirAccionesEnemigo((Enemigo) actual, heroe);
				ui.setEstadoMenu(null);
			}
			ManagerBatalla.ejecutarAcciones(acciones, ui, enemigos, actual);

			enemigos.removeIf(e -> !e.estaVivo());

			if (!enemigos.isEmpty() && heroe.estaVivo()) {
				try {
					Thread.sleep(TURN_END_DELAY_MS);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			}

			// volvemos a encolar al pj que ejecuto su turno
			turnos.offer(actual);
		}
		boolean victoria = heroe != null && heroe.estaVivo();
		int eliminados = totalEnemigos - enemigos.size();
		int puntaje = victoria ? puntajePorDificultad(dificultad) : 0;
		return new ResultadoBatalla(victoria, eliminados, totalEnemigos, puntaje);
	}

	private static int puntajePorDificultad(int dificultad) {
		switch (dificultad) {
			case 1:
				return 1000;
			case 2:
				return 5000;
			case 3:
				return 15000;
			default:
				return 0;
		}
	}
}
