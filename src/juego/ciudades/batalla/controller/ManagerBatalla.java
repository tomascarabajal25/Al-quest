package juego.ciudades.batalla.controller;

import estructuras.listas.ListaSimplementeEnlazada;
import estructuras.pilas.Pila;
import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.model.acciones.Atacar;
import juego.ciudades.batalla.model.acciones.Defender;
import juego.ciudades.batalla.model.acciones.HabilidadAccion;
import juego.ciudades.batalla.model.estados.Defendiendo;
import juego.ciudades.batalla.model.estados.Envenenado;
import juego.ciudades.batalla.view.BatallaUI;
import juego.ciudades.batalla.view.accion.ActionUi;
import juego.ciudades.batalla.view.animacion.Animacion;
import juego.ciudades.batalla.view.animacion.GlowAnimacion;

import java.awt.Color;
import java.util.*;
import java.util.function.BiFunction;

public class ManagerBatalla {

	private static final Map<TipoEnemigo, BiFunction<Enemigo, Combatiente, Accion>> HABILIDADES;

	static {
		HABILIDADES = new HashMap<>();
		HABILIDADES.put(TipoEnemigo.NINJA, (enemigo, heroe) -> new HabilidadAccion(enemigo, heroe, "Cortar",
			(actor, objetivo) -> objetivo.setVida(Math.max(0, objetivo.getVida() - 6))));
		HABILIDADES.put(TipoEnemigo.SAMURAI, (enemigo, heroe) -> new HabilidadAccion(enemigo, heroe, "Golpe Fuerte",
			(actor, objetivo) -> {
				int danio = Math.max(1, (int) (actor.getFuerza() * 1.5) - objetivo.getArmadura());
				objetivo.setVida(Math.max(0, objetivo.getVida() - danio));
			}));
		HABILIDADES.put(TipoEnemigo.MAGO, (enemigo, heroe) -> new HabilidadAccion(enemigo, heroe, "Veneno",
			(actor, objetivo) -> objetivo.setEstado(new Envenenado(objetivo))));
		HABILIDADES.put(TipoEnemigo.CABALLERO, (enemigo, heroe) -> new HabilidadAccion(enemigo, heroe, "Golpe Escudo",
			(actor, objetivo) -> {
				int danio = Math.max(1, (int) (actor.getFuerza() * 0.5) - objetivo.getArmadura());
				objetivo.setVida(Math.max(0, objetivo.getVida() - danio));
				actor.setEstado(new Defendiendo(actor));
			}));
		HABILIDADES.put(TipoEnemigo.BUFON, (enemigo, heroe) -> new HabilidadAccion(enemigo, heroe, "Travesura",
			(actor, objetivo) -> objetivo.setVida(Math.max(0, objetivo.getVida() - (3 + new Random().nextInt(8))))));
		HABILIDADES.put(TipoEnemigo.DUENDE, (enemigo, heroe) -> new HabilidadAccion(enemigo, heroe, "Robo de Vida",
			(actor, objetivo) -> {
				objetivo.setVida(Math.max(0, objetivo.getVida() - 5));
				actor.setVida(actor.getVida() + 5);
			}));
		HABILIDADES.put(TipoEnemigo.ROBOT, (enemigo, heroe) -> new HabilidadAccion(enemigo, heroe, "Rayo Láser",
			(actor, objetivo) -> objetivo.setVida(Math.max(0, objetivo.getVida() - 7))));
	}

	public static void ejecutarAcciones(Pila<Accion> acciones, BatallaUI ui, List<Enemigo> enemigos, Combatiente actual) {
		if (acciones.isEmpty()) {
			ui.actualizarEstado(null, actual);
		}

		for (Accion a : acciones) {
			a.ejecutar();
			ActionUi actionUi = a.getUi();
			String msg = actionUi.getMensaje();
			ui.actualizarEstado(msg, actual);

			try {
				Thread.sleep(16);
			} catch (InterruptedException e) {
				break;
			}

			Animacion animacion = actionUi.crearAnimacion(a.getCombatiente(), a.getObjetivo());
			ui.registrarAnimacion(animacion);
			while (!animacion.terminada()) {
				try {
					Thread.sleep(16);
				} catch (InterruptedException e) {
					break;
				}
			}
			enemigos.removeIf(e -> !e.estaVivo());
		}
	}

	public static List<Enemigo> generarEnemigos(int dificultad) {
		Random rand = new Random();
		List<Enemigo> lista = new ListaSimplementeEnlazada<>();

		int cantidad;
		int[] rango;
		boolean conHabilidad;

		switch (dificultad) {
			case 1:
				cantidad = 1;
				rango = new int[]{30, 50, 5, 8, 0, 2};
				conHabilidad = false;
				break;
			case 2:
				cantidad = 3;
				rango = new int[]{60, 90, 10, 15, 3, 6};
				conHabilidad = true;
				break;
			case 3:
				cantidad = 5;
				rango = new int[]{100, 150, 18, 25, 6, 10};
				conHabilidad = true;
				break;
			default:
				return null;
		}

		List<TipoEnemigo> tiposDisponibles = new ArrayList<>();
		Collections.addAll(tiposDisponibles, TipoEnemigo.values());
		Collections.shuffle(tiposDisponibles, rand);

		for (int i = 0; i < cantidad; i++) {
			TipoEnemigo tipo = tiposDisponibles.get(i);
			String nombre = "" + tipo;

			int vida = rand.nextInt(rango[1] - rango[0] + 1) + rango[0];
			int fuerza = rand.nextInt(rango[3] - rango[2] + 1) + rango[2];
			int armadura = rand.nextInt(rango[5] - rango[4] + 1) + rango[4];


			lista.add(new Enemigo(nombre, tipo, vida, fuerza, armadura));
		}

		return lista;
	}

//	static public boolean todosVivos(List<Enemigo> enemigos) {
//		if (enemigos == null) { return false; }
//		return enemigos.stream().anyMatch(c -> !c.estaVivo());
//	}

	public static Pila<Accion> elegirAccionesHeroe(int dificultad, BatallaUI ui) {
		Pila<Accion> acciones = new Pila<>();
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
		}
		return acciones;
	}

	public static Pila<Accion> elegirAccionesEnemigo(Enemigo enemigo, Combatiente heroe) {
		Pila<Accion> acciones = new Pila<>();
		Random rand = new Random();
		int roll = rand.nextInt(100);

		Accion accion;
		if (roll < 60) {
			accion = new Atacar(enemigo, heroe);
		} else if (roll < 90) {
			accion = new Defender(enemigo, enemigo);
		} else {
			accion = HABILIDADES.get(enemigo.getTipo()).apply(enemigo, heroe);
		}

		acciones.push(accion);
		return acciones;
	}

	public static List<EstadoAplicado> aplicarEstados(Combatiente combatiente) {
		Map<EstadoCombatiente, EstadoActivo> estados = combatiente.getEstados();
		List<EstadoAplicado> aplicados = new ArrayList<>();
		if (estados.isEmpty()) {
			return aplicados;
		}

		List<EstadoCombatiente> terminados = new ArrayList<>();
		
		for (EstadoActivo activo : estados.values()) {
			if (activo.terminado()) {
				terminados.add(activo.getEstado());
				continue;
			}
			activo.aplicar();
			activo.usado();
			String descripcion = activo.getUi().getDescripcion(activo);
			if (descripcion != null && !descripcion.isEmpty()) {
				aplicados.add(new EstadoAplicado(descripcion, activo.getUi().getBadgeColor()));
			}
		};
		
		for (EstadoCombatiente estado : terminados) {
			estados.remove(estado);
		}
		return aplicados;
	}

	public static class EstadoAplicado {
		private final String descripcion;
		private final Color color;

		public EstadoAplicado(String descripcion, Color color) {
			this.descripcion = descripcion;
			this.color = color;
		}

		public String getDescripcion() { return descripcion; }
		public Color getColor() { return color; }
	}

	public static void registrarAnimaciones(List<ManagerBatalla.EstadoAplicado> estadoDescripciones, BatallaUI ui, Combatiente actual) {
		for (ManagerBatalla.EstadoAplicado estadoAplicado : estadoDescripciones) {
			ui.actualizarEstado(estadoAplicado.getDescripcion(), actual);
			Animacion glow = new GlowAnimacion(estadoAplicado.getColor());
			ui.registrarAnimacion(glow);
			while (!glow.terminada()) {
				try { Thread.sleep(16); } catch (InterruptedException e) { break; }
			}
		}
	}
}
