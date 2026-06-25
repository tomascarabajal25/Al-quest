package ciudades.testsCiudadDeBatalla;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import juego.ciudades.batalla.model.*;

public class HeroeTest {

	private Heroe heroe;

	@BeforeEach
	public void setUp() {
		heroe = new Heroe("Aventurero", 100, 25, 10);
	}

	@Test
	public void testConstructorNombre() {
		assertEquals("Aventurero", heroe.getNombre());
	}

	@Test
	public void testConstructorVida() {
		assertEquals(100, heroe.getVida());
	}

	@Test
	public void testConstructorFuerza() {
		assertEquals(25, heroe.getFuerza());
	}

	@Test
	public void testConstructorArmadura() {
		assertEquals(10, heroe.getArmadura());
	}

	@Test
	public void testHeroeHeredaDeCombatiente() {
		assertTrue(heroe instanceof Combatiente);
	}

	@Test
	public void testHeroePuedeRecibirDano() {
		heroe.setVida(heroe.getVida() - 30);
		assertEquals(70, heroe.getVida());
	}

	@Test
	public void testHeroeSetVidaACeroMuere() {
		heroe.setVida(0);
		assertFalse(heroe.estaVivo());
	}

	@Test
	public void testHeroeVidaSeMantieneEnPositivo() {
		heroe.setVida(1);
		assertTrue(heroe.estaVivo());
	}
}