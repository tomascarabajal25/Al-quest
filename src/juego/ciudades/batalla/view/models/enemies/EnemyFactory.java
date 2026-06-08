package juego.ciudades.batalla.view.models.enemies;

import juego.ciudades.batalla.model.Enemigo;
import juego.ciudades.batalla.model.TipoEnemigo;

public class EnemyFactory {

	public static Enemy fromEnemigo(Enemigo e) {
		Enemy view;
		switch (e.getTipo()) {
			case NINJA:     view = new Ninja();     break;
			case SAMURAI:   view = new Samurai();   break;
			case VIKINGO:  view = new Vikingo();   break;
			case CABALLERO: view = new Caballero(); break;
			case BUFON:     view = new Bufon();     break;
			case DUENDE:    view = new Duende();    break;
			case ROBOT:     view = new Robot();      break;
			default:        view = new Ninja();     break;
		}
		view.setHp(e.getVida());
		view.setMaxHp(e.getVida());
		return view;
	}
}