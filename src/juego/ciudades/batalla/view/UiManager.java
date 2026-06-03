package com.aiquest.juego.ciudades.batalla.view;

import com.aiquest.juego.ciudades.batalla.model.Combatiente;
import com.aiquest.juego.ciudades.batalla.model.Enemigo;

public class UiManager {
	public static void imprimirEstado(Combatiente p1, Combatiente p2) {
		System.out.println("-----------Estado-----------");
		System.out.println(p2.toString());
		System.out.println(p1.toString());
		System.out.println("----------------------------\n");
	}

	public static void imprimirAcciones() {
		System.out.println("1. \u2694\uFE0F Atacar");
		System.out.println("2. \uD83D\uDEE1\uFE0F Defender");
//		System.out.println("3. \uD83E\uDDEA Use a healing potion");
//		System.out.println("4. Display your stats");
		System.out.print("Ingrese acción: ");
	}
}
