package juego.ciudades.batalla.controller;

import estructuras.cola.Cola;
import estructuras.listas.ListaSimplementeEnlazada;
import estructuras.pilas.Pila;
import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.model.acciones.Atacar;

import java.awt.Color;
import java.util.*;

public class ManagerBatalla {

	public static void ejecutarAcciones(Pila<Accion> pilaAcciones) {
		while (!pilaAcciones.isEmpty()) {
			Accion accion = pilaAcciones.pop();
			accion.ejecutar();
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

		HabilidadEspecial ninguna = (personaje, objetivo) -> {};
		HabilidadEspecial danioBonus = (personaje, objetivo) -> objetivo.setVida(Math.max(0, objetivo.getVida() - 5));
		HabilidadEspecial veneno = (personaje, objetivo) -> objetivo.setVida(Math.max(0, objetivo.getVida() - 3));
		HabilidadEspecial roboDeVida = (personaje, objetivo) -> {
			int danio = 4;
			objetivo.setVida(Math.max(0, objetivo.getVida() - danio));
			personaje.setVida(personaje.getVida() + danio);
		};
		HabilidadEspecial[] habilidades = {danioBonus, veneno, roboDeVida};

		List<TipoEnemigo> tiposDisponibles = new ArrayList<>();
		Collections.addAll(tiposDisponibles, TipoEnemigo.values());
		Collections.shuffle(tiposDisponibles, rand);

		for (int i = 0; i < cantidad; i++) {
			TipoEnemigo tipo = tiposDisponibles.get(i);
			String nombre = "" + tipo;

			int vida = rand.nextInt(rango[1] - rango[0] + 1) + rango[0];
			int fuerza = rand.nextInt(rango[3] - rango[2] + 1) + rango[2];
			int armadura = rand.nextInt(rango[5] - rango[4] + 1) + rango[4];

			HabilidadEspecial habilidad = conHabilidad
					? habilidades[rand.nextInt(habilidades.length)]
					: ninguna;

			lista.add(new Enemigo(nombre, tipo, vida, fuerza, armadura, habilidad));
		}

		return lista;
	}

	static public boolean todosVivos(List<Enemigo> enemigos) {
		if (enemigos == null) { return false; }
		return enemigos.stream().anyMatch(c -> !c.estaVivo());
	}

	public static Pila<Accion> elegirAccionEnemigo(Enemigo enemigo, Combatiente heroe) {
		Pila<Accion> acciones = new Pila<>();
		acciones.push(new Atacar(enemigo, heroe));
		return acciones;
	}

	public static List<EstadoAplicado> aplicarEstados(Combatiente combatiente) {
		Map<EstadoCombatiente, EstadoActivo> estados = combatiente.getEstados();
		List<EstadoAplicado> resultados = new ArrayList<>();
		if (estados.isEmpty()) {
			return resultados;
		}

		List<EstadoCombatiente> terminados = new ArrayList<>();
		estados.values().forEach(e -> {
			e.aplicar();
			e.usado();
			String desc = e.getUi().getDescripcion(e);
			if (desc != null && !desc.isEmpty()) {
				resultados.add(new EstadoAplicado(desc, e.getUi().getBadgeColor()));
			}
			if (e.terminado()) {
				terminados.add(e.getEstado());
			}
		});
		for (EstadoCombatiente estado : terminados) {
			estados.remove(estado);
		}
		return resultados;
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
}
