package ciudades.testsCiudadDeBatalla;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.model.acciones.Defender;
import juego.ciudades.batalla.model.estados.Defendiendo;

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

	@Test
	public void testDefenderAplicaEstadoDefendiendo() {
		Defender defender = new Defender(heroe, heroe);
		defender.ejecutar();
		assertTrue(heroe.getEstados().containsKey(EstadoCombatiente.DEFENDIENDO));
		assertTrue(heroe.getEstados().get(EstadoCombatiente.DEFENDIENDO) instanceof Defendiendo);
	}

	@Test
	public void testDefenderApilaConDefensaExistente() {
		heroe.setEstado(new Defendiendo(heroe));
		int turnosAntes = heroe.getEstados().get(EstadoCombatiente.DEFENDIENDO).getTurnosRestantes();
		Defender defender = new Defender(heroe, heroe);
		defender.ejecutar();
		int turnosDespues = heroe.getEstados().get(EstadoCombatiente.DEFENDIENDO).getTurnosRestantes();
		assertEquals(turnosAntes + 1, turnosDespues);
	}

	@Test
	public void testDefenderEnemigoAplicaEstado() {
		Defender defender = new Defender(enemigo, enemigo);
		defender.ejecutar();
		assertTrue(enemigo.getEstados().containsKey(EstadoCombatiente.DEFENDIENDO));
	}

	@Test
	public void testDefenderGetEstadoEsDefendiendo() {
		Defendiendo def = new Defendiendo(heroe);
		assertEquals(EstadoCombatiente.DEFENDIENDO, def.getEstado());
	}

	@Test
	public void testDefenderTurnosRestantesEsUno() {
		Defendiendo def = new Defendiendo(heroe);
		assertEquals(1, def.getTurnosRestantes());
	}
}
