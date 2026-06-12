package ciudades.testsCiudadDeBatalla;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.model.acciones.Defender;

public class DefenderTest {

	private Heroe heroe;
	private Enemigo enemigo;
	private HabilidadEspecial habilidadNinguna;

	@BeforeEach
	public void setUp() {
		habilidadNinguna = (personaje, objetivo) -> {};
		heroe = new Heroe("Heroe", 100, 20, 10, habilidadNinguna);
		enemigo = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 15, 5, habilidadNinguna);
	}

	@Test
	public void testDefenderNoCambiaVidaDelDefensor() {
		int vidaAntes = heroe.getVida();
		Defender defender = new Defender(heroe, heroe);
		defender.ejecutar();
		assertEquals(vidaAntes, heroe.getVida());
	}

	@Test
	public void testDefenderNoCambiaVidaDelObjetivo() {
		int vidaAntes = enemigo.getVida();
		Defender defender = new Defender(heroe, enemigo);
		defender.ejecutar();
		assertEquals(vidaAntes, enemigo.getVida());
	}

	@Test
	public void testDefenderNoCrashea() {
		Defender defender = new Defender(heroe, heroe);
		assertDoesNotThrow(() -> defender.ejecutar());
	}

	@Test
	public void testDefenderTipoEsDefensa() {
		Defender defender = new Defender(heroe, heroe);
		assertEquals(TipoAccion.DEFENSA, defender.getTipo());
	}

	@Test
	public void testDefenderGetCombatiente() {
		Defender defender = new Defender(heroe, heroe);
		assertEquals(heroe, defender.getCombatiente());
	}

	@Test
	public void testDefenderGetObjetivo() {
		Defender defender = new Defender(heroe, enemigo);
		assertEquals(enemigo, defender.getObjetivo());
	}

	@Test
	public void testDefenderNoAfectaFuerzaNiArmadura() {
		int fuerzaAntes = heroe.getFuerza();
		int armaduraAntes = heroe.getArmadura();
		Defender defender = new Defender(heroe, heroe);
		defender.ejecutar();
		assertEquals(fuerzaAntes, heroe.getFuerza());
		assertEquals(armaduraAntes, heroe.getArmadura());
	}

	@Test
	public void testDefenderEnemigoSeDefiende() {
		int vidaAntes = enemigo.getVida();
		Defender defender = new Defender(enemigo, enemigo);
		defender.ejecutar();
		assertEquals(vidaAntes, enemigo.getVida());
	}
}