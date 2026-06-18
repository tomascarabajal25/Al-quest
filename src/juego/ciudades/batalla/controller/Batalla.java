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

			List<ManagerBatalla.EstadoAplicado> estadoDescs = ManagerBatalla.aplicarEstados(actual);
			for (ManagerBatalla.EstadoAplicado ea : estadoDescs) {
				ui.actualizarEstado(ea.getDescripcion(), actual);
				Animacion glow = new GlowAnimacion(ea.getColor());
				ui.registrarAnimacion(glow);
				while (!glow.terminada()) {
					try { Thread.sleep(16); } catch (InterruptedException e) { break; }
				}
			}

			Pila<Accion> acciones = new Pila<>();

			if (actual instanceof Heroe) {
				try {
					for (int i = 0; i < dificultad; i++) {
						ui.mostrarIndicadorAccion(i + 1);
						ui.mostrarMenuPrincipal();
						Accion accion = ui.solicitarAccion();
						ui.setEstadoMenu(null);
						if (accion == null) break; // PASAR
						acciones.push(accion);
					}
					ui.setEstadoMenu(null);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					break;
				}
			} else {
				acciones = ManagerBatalla.elegirAccionEnemigo((Enemigo) actual, heroe);
				ui.setEstadoMenu(null);
			}

			if (!acciones.isEmpty()) {
				for (Accion a : acciones) {
					a.ejecutar();

					ActionUi actionUi = a.getUi();
					String msg = actionUi.getMensaje();
					ui.actualizarEstado(msg, actual);

					try { Thread.sleep(16); } catch (InterruptedException e) { break; }

					Animacion anim = actionUi.crearAnimacion(a.getCombatiente(), a.getObjetivo());
					ui.registrarAnimacion(anim);
					while (!anim.terminada()) {
						try { Thread.sleep(16); } catch (InterruptedException e) { break; }
					}

					enemigos.removeIf(e -> !e.estaVivo());
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
