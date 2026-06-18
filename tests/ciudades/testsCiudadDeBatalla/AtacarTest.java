package ciudades.testsCiudadDeBatalla;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.model.acciones.Atacar;
import juego.ciudades.batalla.model.estados.Defendiendo;

public class AtacarTest {

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
	public void testAtacarReduceVidaDelObjetivo() {
		Atacar atacar = new Atacar(heroe, enemigo);
		atacar.ejecutar();
		int danioEsperado = heroe.getFuerza() - enemigo.getArmadura();
		assertEquals(80 - danioEsperado, enemigo.getVida());
	}

	@Test
	public void testAtacarDanioMinimoEsUno() {
		Enemigo tanque = new Enemigo("TANQUE", TipoEnemigo.CABALLERO, 200, 10, 100, habilidadNinguna);
		Atacar atacar = new Atacar(heroe, tanque);
		atacar.ejecutar();
		assertEquals(199, tanque.getVida());
	}

	@Test
	public void testAtacarConFuerzaMenorQueArmadura() {
		Enemigo fortachon = new Enemigo("FUERTE", TipoEnemigo.CABALLERO, 200, 5, 50, habilidadNinguna);
		Atacar atacar = new Atacar(heroe, fortachon);
		atacar.ejecutar();
		assertEquals(199, fortachon.getVida());
	}

	@Test
	public void testAtacarNoReduceVidaBajoCero() {
		Enemigo debil = new Enemigo("DEBIL", TipoEnemigo.DUENDE, 10, 5, 2, habilidadNinguna);
		Atacar atacar = new Atacar(heroe, debil);
		atacar.ejecutar();
		assertEquals(0, debil.getVida());
		assertFalse(debil.estaVivo());
	}

	@Test
	public void testAtacarConArmaduraCero() {
		Enemigo sinArmadura = new Enemigo("SINARM", TipoEnemigo.DUENDE, 80, 10, 0, habilidadNinguna);
		Atacar atacar = new Atacar(heroe, sinArmadura);
		atacar.ejecutar();
		assertEquals(60, sinArmadura.getVida());
	}

	@Test
	public void testAtacarMataAlObjetivo() {
		Enemigo debil = new Enemigo("DEBIL", TipoEnemigo.DUENDE, 10, 5, 2, habilidadNinguna);
		int danio = heroe.getFuerza() - debil.getArmadura();
		assertTrue(danio >= 10, "El danio debe ser suficiente para matar");
		Atacar atacar = new Atacar(heroe, debil);
		atacar.ejecutar();
		assertEquals(0, debil.getVida());
		assertFalse(debil.estaVivo());
	}

	@Test
	public void testAtacarMultipleVecesAcumulaDano() {
		Atacar atacar = new Atacar(heroe, enemigo);
		int vidaInicial = enemigo.getVida();
		int danioPorAtaque = heroe.getFuerza() - enemigo.getArmadura();

		atacar.ejecutar();
		assertEquals(vidaInicial - danioPorAtaque, enemigo.getVida());

		atacar.ejecutar();
		assertEquals(vidaInicial - 2 * danioPorAtaque, enemigo.getVida());
	}

	@Test
	public void testAtacarEnemigoAtacaHeroe() {
		Atacar atacar = new Atacar(enemigo, heroe);
		int danioEsperado = enemigo.getFuerza() - heroe.getArmadura();
		atacar.ejecutar();
		assertEquals(100 - danioEsperado, heroe.getVida());
	}

	@Test
	public void testAtacarTipoEsAtaque() {
		Atacar atacar = new Atacar(heroe, enemigo);
		assertEquals(TipoAccion.ATAQUE, atacar.getTipo());
	}

	@Test
	public void testAtacarGetCombatiente() {
		Atacar atacar = new Atacar(heroe, enemigo);
		assertEquals(heroe, atacar.getCombatiente());
	}

	@Test
	public void testAtacarGetObjetivo() {
		Atacar atacar = new Atacar(heroe, enemigo);
		assertEquals(enemigo, atacar.getObjetivo());
	}

	@Test
	public void testAtacarNoAfectaAlAtacante() {
		int vidaHeroeAntes = heroe.getVida();
		Atacar atacar = new Atacar(heroe, enemigo);
		atacar.ejecutar();
		assertEquals(vidaHeroeAntes, heroe.getVida());
	}

	@Test
	public void testAtacarConMismaFuerzaYArmadura() {
		Heroe h = new Heroe("H", 100, 10, 10, habilidadNinguna);
		Enemigo e = new Enemigo("E", TipoEnemigo.NINJA, 80, 10, 10, habilidadNinguna);
		Atacar atacar = new Atacar(h, e);
		atacar.ejecutar();
		assertEquals(79, e.getVida());
	}

	@Test
	public void testAtacarContraDefensorReduceDanioAMitad() {
		heroe.setEstado(new Defendiendo(enemigo));
		Atacar atacar = new Atacar(heroe, enemigo);
		atacar.ejecutar();
		int danioBase = heroe.getFuerza() - enemigo.getArmadura();
		int danioReducido = Math.max(1, danioBase / 2);
		assertEquals(80 - danioReducido, enemigo.getVida());
	}

	@Test
	public void testAtacarContraDefensorConArmaduraCero() {
		Enemigo sinArmadura = new Enemigo("SINARM", TipoEnemigo.DUENDE, 80, 10, 0, habilidadNinguna);
		sinArmadura.setEstado(new Defendiendo(sinArmadura));
		Atacar atacar = new Atacar(heroe, sinArmadura);
		atacar.ejecutar();
		int danioBase = heroe.getFuerza() - sinArmadura.getArmadura();
		int danioReducido = Math.max(1, danioBase / 2);
		assertEquals(80 - danioReducido, sinArmadura.getVida());
	}

	@Test
	public void testAtacarContraDefensorNoBajaDanioBajoCero() {
		Heroe debil = new Heroe("DEBIL", 100, 5, 0, habilidadNinguna);
		Enemigo tanque = new Enemigo("TANQUE", TipoEnemigo.CABALLERO, 200, 10, 100, habilidadNinguna);
		tanque.setEstado(new Defendiendo(tanque));
		Atacar atacar = new Atacar(debil, tanque);
		atacar.ejecutar();
		assertEquals(199, tanque.getVida());
	}

	@Test
	public void testAtacarContraDefensorConsumeEstado() {
		enemigo.setEstado(new Defendiendo(enemigo));
		Atacar atacar = new Atacar(heroe, enemigo);
		atacar.ejecutar();
		// After usado() decrements turnosRestantes from 1 to 0,
		// the state survives until the next aplicarEstados() tick
		// which removes it via terminado() == true
		assertNotNull(enemigo.getEstados().get(EstadoCombatiente.DEFENDIENDO));
		assertEquals(0, enemigo.getEstados().get(EstadoCombatiente.DEFENDIENDO).getTurnosRestantes());
	}

	@Test
	public void testAtacarContraDefensorSegundoAtaqueSinReduccion() {
		enemigo.setEstado(new Defendiendo(enemigo));
		Atacar atacar = new Atacar(heroe, enemigo);
		atacar.ejecutar();
		// Need to tick once to remove the consumed state from the map
		juego.ciudades.batalla.controller.ManagerBatalla.aplicarEstados(enemigo);
		atacar.ejecutar();
		int danioBase = heroe.getFuerza() - enemigo.getArmadura();
		int danioReducido = Math.max(1, danioBase / 2);
		assertEquals(80 - danioReducido - danioBase, enemigo.getVida());
		assertFalse(enemigo.getEstados().containsKey(EstadoCombatiente.DEFENDIENDO));
	}

	@Test
	public void testAtacarContraDefensorDobleDefensaUnAtaque() {
		enemigo.setEstado(new Defendiendo(enemigo));
		enemigo.setEstado(new Defendiendo(enemigo));
		Atacar atacar = new Atacar(heroe, enemigo);
		atacar.ejecutar();
		int danioBase = heroe.getFuerza() - enemigo.getArmadura();
		int danioReducido = Math.max(1, danioBase / 2);
		assertEquals(80 - danioReducido, enemigo.getVida());
	}

	@Test
	public void testAtacarContraDefensorEstadoRemovidoDespuesDeTick() {
		enemigo.setEstado(new Defendiendo(enemigo));
		Atacar atacar = new Atacar(heroe, enemigo);
		atacar.ejecutar();
		// State still in map with turnos=0
		assertTrue(enemigo.getEstados().containsKey(EstadoCombatiente.DEFENDIENDO));
		// After aplicarEstados tick, terminado() is true, state removed
		juego.ciudades.batalla.controller.ManagerBatalla.aplicarEstados(enemigo);
		assertFalse(enemigo.getEstados().containsKey(EstadoCombatiente.DEFENDIENDO));
	}
}
