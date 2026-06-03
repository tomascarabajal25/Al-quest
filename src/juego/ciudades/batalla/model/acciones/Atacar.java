package com.aiquest.juego.ciudades.batalla.model.acciones;

import com.aiquest.juego.ciudades.batalla.model.Accion;
import com.aiquest.juego.ciudades.batalla.model.Combatiente;
import com.aiquest.juego.ciudades.batalla.model.TipoAccion;

public class Atacar extends Accion {

	public Atacar(Combatiente combatiente, Combatiente objetivo) {
		super(combatiente, objetivo, TipoAccion.ATAQUE);
	}

	public void ejecutar() {
		// damage = max(1, actor.getFuerza() + potencia - objetivo.getArmadura())
		Combatiente combatiente = getCombatiente();
		Combatiente objetivo = getObjetivo();

		int danio = Math.max(1, combatiente.getFuerza() - objetivo.getArmadura());
		int vida = objetivo.getVida();
		objetivo.setVida(Math.max(0, vida - danio));

		System.out.println(combatiente.getNombre() + " ataca!");
		System.out.println(objetivo.getNombre() + " recibe " + danio + " de danio.");
	}
}