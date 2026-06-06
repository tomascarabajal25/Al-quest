package com.aiquest.juego.ciudades.batalla.controller;

import com.aiquest.estructuras.listas.ListaSimplementeEnlazada;
import com.aiquest.estructuras.pilas.Pila;
import com.aiquest.juego.ciudades.batalla.model.*;
import com.aiquest.juego.ciudades.batalla.model.acciones.Atacar;

import java.util.List;
import java.util.Random;

public class ManagerBatalla {

	public static void ejecutarAcciones(Pila<Accion> pilaAcciones) {
		while (!pilaAcciones.isEmpty()) {
			Accion accion = pilaAcciones.pop();
			accion.ejecutar();
		}
	}

	public static List<Enemigo> generarEnemigos(int dificultad) {
		// generar enemigos segun dificultad:
		//    - 1 Facil -----> 1 enemigo debilucho (sin habilidad especial)
		//    - 2 Media -----> 3 enemigos normales (con habilidades especiales)
		//    - 3 Dificil ---> 5 enemigos chetados (con habilidades especiales)

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

		TipoEnemigo[] tipos = TipoEnemigo.values();

		for (int i = 0; i < cantidad; i++) {
			TipoEnemigo tipo = tipos[rand.nextInt(tipos.length)];
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
}
