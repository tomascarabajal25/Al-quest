package ciudades.testsCiudadDeBatalla;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.model.estados.Defendiendo;

public class CombatienteTest {

	private Heroe heroe;
	private Enemigo enemigo;

	@BeforeEach
	public void setUp() {
		heroe = new Heroe("Heroe", 100, 20, 10);
		enemigo = new Enemigo("NINJA", TipoEnemigo.NINJA, 80, 15, 5);
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

	@Test
	public void testSetEstadoAgregaNuevoEstado() {
		heroe.setEstado(new Defendiendo(heroe));
		assertTrue(heroe.getEstados().containsKey(EstadoCombatiente.DEFENDIENDO));
	}

	@Test
	public void testSetEstadoApilaExistente() {
		heroe.setEstado(new Defendiendo(heroe));
		int turnosAntes = heroe.getEstados().get(EstadoCombatiente.DEFENDIENDO).getTurnosRestantes();
		heroe.setEstado(new Defendiendo(heroe));
		int turnosDespues = heroe.getEstados().get(EstadoCombatiente.DEFENDIENDO).getTurnosRestantes();
		assertEquals(turnosAntes + 1, turnosDespues);
		assertEquals(1, heroe.getEstados().size());
	}

	@Test
	public void testEstaDefendiendoEsTrue() {
		heroe.setEstado(new Defendiendo(heroe));
		assertTrue(heroe.estaDefendiendo());
	}

	@Test
	public void testEstaDefendiendoEsFalse() {
		assertFalse(heroe.estaDefendiendo());
	}

	@Test
	public void testEstaDefendiendoEsFalseDespuesDeConsumo() {
		heroe.setEstado(new Defendiendo(heroe));
		heroe.getEstados().remove(EstadoCombatiente.DEFENDIENDO);
		assertFalse(heroe.estaDefendiendo());
	}
}
