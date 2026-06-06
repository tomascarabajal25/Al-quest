package tests.Ciudades.testsDeBatalla;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aiquest.juego.ciudades.batalla.model.*;

public class CombatienteTest {

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
	public void testConstructorInicializaNombre() {
		assertEquals("Heroe", heroe.getNombre());
	}

	@Test
	public void testConstructorInicializaVida() {
		assertEquals(100, heroe.getVida());
	}

	@Test
	public void testConstructorInicializaFuerza() {
		assertEquals(20, heroe.getFuerza());
	}

	@Test
	public void testConstructorInicializaArmadura() {
		assertEquals(10, heroe.getArmadura());
	}

	@Test
	public void testSetVida() {
		heroe.setVida(50);
		assertEquals(50, heroe.getVida());
	}

	@Test
	public void testSetFuerza() {
		heroe.setFuerza(30);
		assertEquals(30, heroe.getFuerza());
	}

	@Test
	public void testSetArmadura() {
		heroe.setArmadura(15);
		assertEquals(15, heroe.getArmadura());
	}

	@Test
	public void testEstaVivoConVidaPositiva() {
		assertTrue(heroe.estaVivo());
	}

	@Test
	public void testEstaVivoConVidaCero() {
		heroe.setVida(0);
		assertFalse(heroe.estaVivo());
	}

	@Test
	public void testEstaVivoConVidaNegativa() {
		heroe.setVida(-10);
		assertFalse(heroe.estaVivo());
	}

	@Test
	public void testEstaVivoDespuesDeDano() {
		heroe.setVida(1);
		assertTrue(heroe.estaVivo());
		heroe.setVida(0);
		assertFalse(heroe.estaVivo());
	}

	@Test
	public void testUsarHabilidadEspecialDanioBonus() {
		HabilidadEspecial danioBonus = (personaje, objetivo) -> objetivo.setVida(Math.max(0, objetivo.getVida() - 5));
		Heroe h = new Heroe("H", 100, 10, 5, danioBonus);
		h.usarHabilidadEspecial(enemigo);
		assertEquals(75, enemigo.getVida());
	}

	@Test
	public void testUsarHabilidadEspecialNinguna() {
		heroe.usarHabilidadEspecial(enemigo);
		assertEquals(80, enemigo.getVida());
		assertEquals(100, heroe.getVida());
	}

	@Test
	public void testToStringContieneNombre() {
		String str = heroe.toString();
		assertTrue(str.contains("Heroe"));
	}

	@Test
	public void testToStringContieneStats() {
		String str = heroe.toString();
		assertTrue(str.contains("100"));
		assertTrue(str.contains("20"));
		assertTrue(str.contains("10"));
	}

	@Test
	public void testEnemigoEsCombatiente() {
		assertTrue(enemigo instanceof Combatiente);
	}

	@Test
	public void testHeroeEsCombatiente() {
		assertTrue(heroe instanceof Combatiente);
	}
}