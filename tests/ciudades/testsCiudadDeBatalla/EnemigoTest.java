package ciudades.testsCiudadDeBatalla;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import juego.ciudades.batalla.model.*;

public class EnemigoTest {

	@Test
	public void testConstructorTipoNinja() {
		Enemigo e = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 45, 20);
		assertEquals(TipoEnemigo.NINJA, e.getTipo());
	}

	@Test
	public void testConstructorTipoSamurai() {
		Enemigo e = new Enemigo("SAMURAI", TipoEnemigo.SAMURAI, 110, 55, 45);
		assertEquals(TipoEnemigo.SAMURAI, e.getTipo());
	}

	@Test
	public void testConstructorTipoVikingo() {
		Enemigo e = new Enemigo("MAGO", TipoEnemigo.MAGO, 140, 65, 30);
		assertEquals(TipoEnemigo.MAGO, e.getTipo());
	}

	@Test
	public void testConstructorTipoCaballero() {
		Enemigo e = new Enemigo("CABALLERO", TipoEnemigo.CABALLERO, 160, 35, 80);
		assertEquals(TipoEnemigo.CABALLERO, e.getTipo());
	}

	@Test
	public void testConstructorTipoBufon() {
		Enemigo e = new Enemigo("BUFON", TipoEnemigo.BUFON, 95, 40, 35);
		assertEquals(TipoEnemigo.BUFON, e.getTipo());
	}

	@Test
	public void testConstructorTipoDuende() {
		Enemigo e = new Enemigo("DUENDE", TipoEnemigo.DUENDE, 70, 30, 25);
		assertEquals(TipoEnemigo.DUENDE, e.getTipo());
	}

	@Test
	public void testConstructorTipoRobot() {
		Enemigo e = new Enemigo("ROBOT", TipoEnemigo.ROBOT, 120, 50, 60);
		assertEquals(TipoEnemigo.ROBOT, e.getTipo());
	}

	@Test
	public void testEnemigoHeredaDeCombatiente() {
		Enemigo e = new Enemigo("E", TipoEnemigo.NINJA, 50, 10, 5);
		assertTrue(e instanceof Combatiente);
	}

	@Test
	public void testEnemigoNombreEsCorrecto() {
		Enemigo e = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 45, 20);
		assertEquals("NINJA", e.getNombre());
	}

	@Test
	public void testEnemigoVida() {
		Enemigo e = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 45, 20);
		assertEquals(80, e.getVida());
	}

	@Test
	public void testEnemigoFuerza() {
		Enemigo e = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 45, 20);
		assertEquals(45, e.getFuerza());
	}

	@Test
	public void testEnemigoArmadura() {
		Enemigo e = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 45, 20);
		assertEquals(20, e.getArmadura());
	}

	@Test
	public void testEnemigoEstaVivoAlInicio() {
		Enemigo e = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 45, 20);
		assertTrue(e.estaVivo());
	}

	@Test
	public void testEnemigoMuereAlQuedarSinVida() {
		Enemigo e = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 45, 20);
		e.setVida(0);
		assertFalse(e.estaVivo());
	}

	@Test
	public void testTipoEnemigoValores() {
		TipoEnemigo[] tipos = TipoEnemigo.values();
		assertEquals(7, tipos.length);
		assertEquals(TipoEnemigo.NINJA, tipos[0]);
		assertEquals(TipoEnemigo.SAMURAI, tipos[1]);
		assertEquals(TipoEnemigo.MAGO, tipos[2]);
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

}