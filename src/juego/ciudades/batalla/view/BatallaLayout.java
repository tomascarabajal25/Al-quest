package juego.ciudades.batalla.view;

/**
 * Constantes de layout compartidas para la UI de la ciudad Batalla.
 *
 * Ajustar tamaños/posiciones aquí evita desalineaciones entre
 * {@code BatallaUI} y menús de entrada.
 */
public final class BatallaLayout {

	private BatallaLayout() {
		// Utility class
	}

	public static final int CANVAS_W = 736;
	public static final int CANVAS_H = 414;

	public static final int HERO_X = 160;
	public static final int HERO_Y = 155;
	public static final int HERO_SIZE = 128;
	public static final int HERO_STATUS_W = 280;

	public static final int ENEMY_Y = 55;
	public static final int ENEMY_SIZE = 125;
	public static final int ENEMY_GAP = 12;
	public static final int ENEMY_OFFSET_X = 24;

	public static final int HUD_Y = 282;
	public static final int HUD_H = 68;
	public static final int DIALOG_Y = 354;
}
