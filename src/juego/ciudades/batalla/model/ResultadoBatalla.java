package juego.ciudades.batalla.model;

/**
 * Snapshot inmutable con el resultado final de una batalla.
 */
public class ResultadoBatalla {

	private final boolean victoria;
	private final int enemigosEliminados;
	private final int enemigosTotales;
	private final int puntaje;

	/**
	 * Crea un resultado de batalla.
	 *
	 * @param victoria indica si el héroe ganó
	 * @param enemigosEliminados cantidad de enemigos derrotados
	 * @param enemigosTotales cantidad total de enemigos al iniciar
	 * @param puntaje puntaje obtenido en la batalla
	 */
	public ResultadoBatalla(boolean victoria, int enemigosEliminados, int enemigosTotales, int puntaje) {
		this.victoria = victoria;
		this.enemigosEliminados = enemigosEliminados;
		this.enemigosTotales = enemigosTotales;
		this.puntaje = puntaje;
	}

	/**
	 * @return {@code true} si la batalla terminó en victoria
	 */
	public boolean esVictoria() {
		return victoria;
	}

	/**
	 * @return cantidad de enemigos eliminados
	 */
	public int getEnemigosEliminados() {
		return enemigosEliminados;
	}

	/**
	 * @return cantidad total de enemigos al comenzar
	 */
	public int getEnemigosTotales() {
		return enemigosTotales;
	}

	/**
	 * @return puntaje obtenido por la batalla
	 */
	public int getPuntaje() {
		return puntaje;
	}
}
