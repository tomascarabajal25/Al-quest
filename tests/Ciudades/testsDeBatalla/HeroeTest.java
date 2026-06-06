package tests.Ciudades.testsDeBatalla;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.aiquest.juego.ciudades.batalla.model.*;

public class HeroeTest {

	private Heroe heroe;
	private HabilidadEspecial habilidadNinguna;

	@BeforeEach
	public void setUp() {
		habilidadNinguna = (personaje, objetivo) -> {};
		heroe = new Heroe("Aventurero", 100, 25, 10, habilidadNinguna);
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
	public void testHeroeConHabilidadEspecialRoboDeVida() {
		Enemigo enemigo = new Enemigo("E", TipoEnemigo.NINJA, 80, 10, 5, habilidadNinguna);
		heroe.setVida(50);
		HabilidadEspecial roboDeVida = (personaje, objetivo) -> {
			int danio = 4;
			objetivo.setVida(Math.max(0, objetivo.getVida() - danio));
			personaje.setVida(personaje.getVida() + danio);
		};
		Heroe h = new Heroe("Ladron", 50, 10, 5, roboDeVida);
		h.usarHabilidadEspecial(enemigo);
		assertEquals(54, h.getVida());
		assertEquals(76, enemigo.getVida());
	}

	@Test
	public void testHeroeConHabilidadEspecialNinguna() {
		Enemigo enemigo = new Enemigo("E", TipoEnemigo.NINJA, 80, 10, 5, habilidadNinguna);
		heroe.usarHabilidadEspecial(enemigo);
		assertEquals(100, heroe.getVida());
		assertEquals(80, enemigo.getVida());
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