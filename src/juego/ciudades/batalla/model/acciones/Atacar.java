package juego.ciudades.batalla.model.acciones;

import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.view.accion.ActionUi;
import juego.ciudades.batalla.view.accion.AtacarUi;

import java.util.Map;

public class Atacar extends Accion {
	private int danio;

	public Atacar(Combatiente combatiente, Combatiente objetivo) {
		super(combatiente, objetivo, TipoAccion.ATAQUE);
	}

	@Override
	public ActionUi getUi() {
		return new AtacarUi(combatiente.getNombre(), objetivo.getNombre(), danio);
	}

	public void ejecutar() {
		// damage = max(1, actor.getFuerza() + potencia - objetivo.getArmadura())
		Combatiente combatiente = getCombatiente();
		Combatiente objetivo = getObjetivo();
		int armadura = objetivo.getArmadura();
		Map<EstadoCombatiente, EstadoActivo> estadosObjetivo = objetivo.getEstados();

		int danio = Math.max(1, combatiente.getFuerza() - armadura);

		if (objetivo.estaDefendiendo()) {
			danio = Math.max(1, danio / 2);
			objetivo.defendido();
		}

		this.danio = danio;

		int vida = objetivo.getVida();
		objetivo.setVida(Math.max(0, vida - danio));

		System.out.println(combatiente.getNombre() + " ataca!");
		System.out.println(objetivo.getNombre() + " recibe " + danio + " de danio.");
	}
}