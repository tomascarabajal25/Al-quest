package ciudades.testsCiudadDeBatalla;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import juego.ciudades.batalla.model.*;

public class EnemigoTest {

	private HabilidadEspecial habilidadNinguna = (personaje, objetivo) -> {};

	@Test
	public void testConstructorTipoNinja() {
		Enemigo e = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 45, 20, habilidadNinguna);
		assertEquals(TipoEnemigo.NINJA, e.getTipo());
	}

	@Test
	public void testConstructorTipoSamurai() {
		Enemigo e = new Enemigo("SAMURAI", TipoEnemigo.SAMURAI, 110, 55, 45, habilidadNinguna);
		assertEquals(TipoEnemigo.SAMURAI, e.getTipo());
	}

	@Test
	public void testConstructorTipoVikingo() {
		Enemigo e = new Enemigo("VIKINGO", TipoEnemigo.VIKINGO, 140, 65, 30, habilidadNinguna);
		assertEquals(TipoEnemigo.VIKINGO, e.getTipo());
	}

	@Test
	public void testConstructorTipoCaballero() {
		Enemigo e = new Enemigo("CABALLERO", TipoEnemigo.CABALLERO, 160, 35, 80, habilidadNinguna);
		assertEquals(TipoEnemigo.CABALLERO, e.getTipo());
	}

	@Test
	public void testConstructorTipoBufon() {
		Enemigo e = new Enemigo("BUFON", TipoEnemigo.BUFON, 95, 40, 35, habilidadNinguna);
		assertEquals(TipoEnemigo.BUFON, e.getTipo());
	}

	@Test
	public void testConstructorTipoDuende() {
		Enemigo e = new Enemigo("DUENDE", TipoEnemigo.DUENDE, 70, 30, 25, habilidadNinguna);
		assertEquals(TipoEnemigo.DUENDE, e.getTipo());
	}

	@Test
	public void testConstructorTipoRobot() {
		Enemigo e = new Enemigo("ROBOT", TipoEnemigo.ROBOT, 120, 50, 60, habilidadNinguna);
		assertEquals(TipoEnemigo.ROBOT, e.getTipo());
	}

	@Test
	public void testEnemigoHeredaDeCombatiente() {
		Enemigo e = new Enemigo("E", TipoEnemigo.NINJA, 50, 10, 5, habilidadNinguna);
		assertTrue(e instanceof Combatiente);
	}

	@Test
	public void testEnemigoNombreEsCorrecto() {
		Enemigo e = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 45, 20, habilidadNinguna);
		assertEquals("NINJA", e.getNombre());
	}

	@Test
	public void testEnemigoVida() {
		Enemigo e = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 45, 20, habilidadNinguna);
		assertEquals(80, e.getVida());
	}

	@Test
	public void testEnemigoFuerza() {
		Enemigo e = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 45, 20, habilidadNinguna);
		assertEquals(45, e.getFuerza());
	}

	@Test
	public void testEnemigoArmadura() {
		Enemigo e = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 45, 20, habilidadNinguna);
		assertEquals(20, e.getArmadura());
	}

	@Test
	public void testEnemigoEstaVivoAlInicio() {
		Enemigo e = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 45, 20, habilidadNinguna);
		assertTrue(e.estaVivo());
	}

	@Test
	public void testEnemigoMuereAlQuedarSinVida() {
		Enemigo e = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 45, 20, habilidadNinguna);
		e.setVida(0);
		assertFalse(e.estaVivo());
	}

	@Test
	public void testTipoEnemigoValores() {
		TipoEnemigo[] tipos = TipoEnemigo.values();
		assertEquals(7, tipos.length);
		assertEquals(TipoEnemigo.NINJA, tipos[0]);
		assertEquals(TipoEnemigo.SAMURAI, tipos[1]);
		assertEquals(TipoEnemigo.VIKINGO, tipos[2]);
		assertEquals(TipoEnemigo.CABALLERO, tipos[3]);
		assertEquals(TipoEnemigo.BUFON, tipos[4]);
		assertEquals(TipoEnemigo.DUENDE, tipos[5]);
		assertEquals(TipoEnemigo.ROBOT, tipos[6]);
	}

	@Test
	public void testTipoEnemigoValueOf() {
		assertEquals(TipoEnemigo.NINJA, TipoEnemigo.valueOf("NINJA"));
		assertEquals(TipoEnemigo.ROBOT, TipoEnemigo.valueOf("ROBOT"));
	}

	@Test
	public void testEnemigoConHabilidadDanioBonus() {
		HabilidadEspecial danioBonus = (personaje, objetivo) -> objetivo.setVida(Math.max(0, objetivo.getVida() - 5));
		Enemigo atacante = new Enemigo("A", TipoEnemigo.NINJA, 80, 45, 20, danioBonus);
		Enemigo objetivo = new Enemigo("B", TipoEnemigo.SAMURAI, 110, 55, 45, habilidadNinguna);
		atacante.usarHabilidadEspecial(objetivo);
		assertEquals(105, objetivo.getVida());
		assertEquals(80, atacante.getVida());
	}

	@Test
	public void testEnemigoConHabilidadVeneno() {
		HabilidadEspecial veneno = (personaje, objetivo) -> objetivo.setVida(Math.max(0, objetivo.getVida() - 3));
		Enemigo atacante = new Enemigo("A", TipoEnemigo.BUFON, 95, 40, 35, veneno);
		Enemigo objetivo = new Enemigo("B", TipoEnemigo.DUENDE, 70, 30, 25, habilidadNinguna);
		atacante.usarHabilidadEspecial(objetivo);
		assertEquals(67, objetivo.getVida());
	}

	@Test
	public void testEnemigoConHabilidadRoboDeVida() {
		HabilidadEspecial roboDeVida = (personaje, objetivo) -> {
			int danio = 4;
			objetivo.setVida(Math.max(0, objetivo.getVida() - danio));
			personaje.setVida(personaje.getVida() + danio);
		};
		Enemigo atacante = new Enemigo("A", TipoEnemigo.DUENDE, 50, 30, 25, roboDeVida);
		Enemigo objetivo = new Enemigo("B", TipoEnemigo.ROBOT, 120, 50, 60, habilidadNinguna);
		atacante.usarHabilidadEspecial(objetivo);
		assertEquals(116, objetivo.getVida());
		assertEquals(54, atacante.getVida());
	}
}