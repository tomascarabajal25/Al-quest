package juego.ciudades.batalla.view.models;

import juego.ciudades.batalla.model.Enemigo;

public class EnemyFactory {

	public static Enemy fromEnemigo(Enemigo e, int dificultad) {
		Enemy view;
		switch (e.getTipo()) {
			case VIKINGO:   view = new Vikingo(dificultad);   break;
			case MAGO:      view = new Mago(dificultad);   break;
			case CABALLERO: view = new Caballero(dificultad); break;
			case BUFON:     view = new Bufon(dificultad);     break;
			case DUENDE:    view = new Duende(dificultad);    break;
			case ROBOT:     view = new Robot(dificultad);      break;
			default:        view = new Ninja(dificultad);     break;
		}
		view.setHp(e.getVida());
		view.setMaxHp(e.getVida());
		return view;
	}
}