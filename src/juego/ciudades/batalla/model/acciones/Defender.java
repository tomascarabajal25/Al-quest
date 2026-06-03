package com.aiquest.juego.ciudades.batalla.model.acciones;

import com.aiquest.juego.ciudades.batalla.model.Accion;
import com.aiquest.juego.ciudades.batalla.model.Combatiente;
import com.aiquest.juego.ciudades.batalla.model.TipoAccion;

public class Defender extends Accion {

	public Defender(Combatiente combatiente, Combatiente objetivo) {
		super(combatiente, objetivo, TipoAccion.DEFENSA);
	}

	@Override
	public void ejecutar() {
		Combatiente combatiente = getCombatiente();
		System.out.println(combatiente.getNombre() + " se defiende!");
		// TODO: implementar buff temporal de armadura
	}
}
