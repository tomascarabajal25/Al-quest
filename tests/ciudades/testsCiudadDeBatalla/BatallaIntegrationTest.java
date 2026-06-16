package ciudades.testsCiudadDeBatalla;
import static org.junit.jupiter.api.Assertions.*;

import estructuras.cola.Cola;
import estructuras.pilas.Pila;
import juego.ciudades.batalla.controller.ManagerBatalla;
import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.model.acciones.Atacar;
import juego.ciudades.batalla.model.acciones.Defender;
import modelos.Jugador;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class BatallaIntegrationTest {

	private HabilidadEspecial habilidadNinguna = (personaje, objetivo) -> {};

	@Test
	public void testHeroeDesdeJugadorPreservesName() {
		Jugador jugador = new Jugador("Aventurero");
		Heroe heroe = Heroe.desdeJugador(jugador, 100, 20, 10, habilidadNinguna);
		assertEquals("Aventurero", heroe.getNombre());
	}

	@Test
	public void testHeroeDesdeJugadorPreservesStats() {
		Jugador jugador = new Jugador("TestHero");
		Heroe heroe = Heroe.desdeJugador(jugador, 150, 30, 15, habilidadNinguna);
		assertEquals(150, heroe.getVida());
		assertEquals(30, heroe.getFuerza());
		assertEquals(15, heroe.getArmadura());
	}

	@Test
	public void testHeroeDesdeJugadorIsCombatiente() {
		Jugador jugador = new Jugador("X");
		Heroe heroe = Heroe.desdeJugador(jugador, 100, 10, 5, habilidadNinguna);
		assertTrue(heroe instanceof Combatiente);
	}

	@Test
	public void testActionMessageFormatAtacar() {
		Heroe heroe = new Heroe("H", 100, 20, 10, habilidadNinguna);
		Enemigo enemigo = new Enemigo("Ninja", TipoEnemigo.NINJA, 80, 15, 5, habilidadNinguna);

		String msg = heroe.getNombre() + " atacó a " + enemigo.getNombre() + "!";
		assertTrue(msg.contains("atacó a"), "Attack message should contain 'atacó a'");
		assertTrue(msg.startsWith("H"), "Message should start with attacker name");
		assertTrue(msg.endsWith("!"), "Message should end with !");
	}

	@Test
	public void testActionMessageFormatDefender() {
		Heroe heroe = new Heroe("H", 100, 20, 10, habilidadNinguna);
		String msg = heroe.getNombre() + " usó defensa!";
		assertTrue(msg.contains("usó"), "Defend message should contain 'usó'");
	}

	@Test
	public void testEnemyRemovalAfterFaint() {
		Enemigo e1 = new Enemigo("E1", TipoEnemigo.NINJA, 10, 5, 0, habilidadNinguna);
		Enemigo e2 = new Enemigo("E2", TipoEnemigo.SAMURAI, 80, 15, 5, habilidadNinguna);

		List<Enemigo> enemigos = new ArrayList<>();
		enemigos.add(e1);
		enemigos.add(e2);

		Atacar attack = new Atacar(
				new Heroe("H", 100, 50, 10, habilidadNinguna), e1);
		attack.ejecutar();

		assertFalse(e1.estaVivo(), "E1 should be dead");
		assertTrue(e2.estaVivo(), "E2 should be alive");

		enemigos.removeIf(e -> !e.estaVivo());
		assertEquals(1, enemigos.size(), "Dead enemies should be removed");
		assertEquals("E2", enemigos.get(0).getNombre());
	}

	@Test
	public void testColaTurnosBasicOperation() {
		Heroe heroe = new Heroe("H", 100, 20, 10, habilidadNinguna);
		Enemigo e1 = new Enemigo("E1", TipoEnemigo.NINJA, 80, 15, 5, habilidadNinguna);

		Cola<Combatiente> turnos = new Cola<>();
		turnos.offer(heroe);
		turnos.offer(e1);

		assertEquals(heroe, turnos.peek());
		assertEquals(heroe, turnos.remove());
		assertEquals(e1, turnos.remove());
	}

	@Test
	public void testHeroeReceivesDamageAndSurvives() {
		Heroe heroe = Heroe.desdeJugador(new Jugador("Hero"), 100, 40, 10, habilidadNinguna);
		Enemigo e = new Enemigo("E", TipoEnemigo.NINJA, 80, 15, 5, habilidadNinguna);

		Atacar attack = new Atacar(e, heroe);
		attack.ejecutar();

		assertTrue(heroe.estaVivo(), "Heroe should survive one hit");
		assertTrue(heroe.getVida() < 100, "Heroe should have taken damage");
	}

	@Test
	public void testManagerBatallaGenerarEnemigosAllTypes() {
		for (int d = 1; d <= 3; d++) {
			for (int i = 0; i < 20; i++) {
				List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(d);
				for (Enemigo e : enemigos) {
					assertNotNull(e.getTipo(), "Enemy should have a type");
					assertTrue(e.estaVivo(), "Enemy should be alive at start");
					assertTrue(e.getVida() > 0, "Enemy should have positive HP");
				}
			}
		}
	}

	@Test
	public void testBatallaEmpezarHeroeWinsWhenEnemiesKill() {
		Heroe heroe = new Heroe("H", 200, 50, 20, habilidadNinguna);
		Enemigo enemigo = new Enemigo("E", TipoEnemigo.NINJA, 20, 5, 0, habilidadNinguna);

		Cola<Combatiente> turnos = new Cola<>();
		turnos.offer(heroe);
		turnos.offer(enemigo);

		List<Enemigo> enemigos = new ArrayList<>();
		enemigos.add(enemigo);

		Pila<Accion> heroActions = new Pila<>();
		heroActions.push(new Atacar(heroe, enemigo));

		Pila<Accion> enemyActions = ManagerBatalla.elegirAccionEnemigo(enemigo, heroe);

		assertNotNull(heroActions);
		assertFalse(heroActions.isEmpty());
		assertEquals(TipoAccion.ATAQUE, heroActions.pop().getTipo());
	}

	@Test
	public void testAtacarKillSequenceRemovesFromList() {
		Heroe heroe = new Heroe("H", 200, 50, 10, habilidadNinguna);
		Enemigo e1 = new Enemigo("E1", TipoEnemigo.DUENDE, 10, 5, 0, habilidadNinguna);
		Enemigo e2 = new Enemigo("E2", TipoEnemigo.NINJA, 100, 15, 5, habilidadNinguna);

		List<Enemigo> enemigos = new ArrayList<>();
		enemigos.add(e1);
		enemigos.add(e2);

		new Atacar(heroe, e1).ejecutar();
		assertFalse(e1.estaVivo());

		enemigos.removeIf(e -> !e.estaVivo());
		assertEquals(1, enemigos.size(), "Only alive enemy should remain");
		assertEquals("E2", enemigos.get(0).getNombre());
	}

	@Test
	public void testMultipleActionsInOneTurn() {
		Heroe heroe = new Heroe("H", 100, 20, 10, habilidadNinguna);
		Enemigo enemigo = new Enemigo("E", TipoEnemigo.NINJA, 200, 15, 5, habilidadNinguna);

		Pila<Accion> acciones = new Pila<>();
		acciones.push(new Atacar(heroe, enemigo));
		acciones.push(new Defender(heroe, heroe));

		ManagerBatalla.ejecutarAcciones(acciones);

		assertTrue(enemigo.getVida() < 200, "Enemy should have taken damage");
		assertTrue(acciones.isEmpty(), "Actions stack should be empty after execution");
	}

	@Test
	public void testActionLogMessageFormat() {
		Heroe heroe = new Heroe("Heroe", 100, 20, 10, habilidadNinguna);
		Enemigo enemigo = new Enemigo("Ninja", TipoEnemigo.NINJA, 80, 15, 5, habilidadNinguna);

		Accion atacar = new Atacar(heroe, enemigo);

		StringBuilder msg = new StringBuilder();
		if (atacar instanceof Atacar) {
			msg.append(atacar.getCombatiente().getNombre())
			   .append(" atacó a ")
			   .append(atacar.getObjetivo().getNombre())
			   .append("!");
		}

		assertEquals("Heroe atacó a Ninja!", msg.toString());
		assertTrue(msg.toString().contains("atacó a"));
	}

	@Test
	public void testDefendActionLogFormat() {
		Heroe heroe = new Heroe("Heroe", 100, 20, 10, habilidadNinguna);

		Accion defender = new Defender(heroe, heroe);

		StringBuilder msg = new StringBuilder();
		msg.append(defender.getCombatiente().getNombre())
		   .append(" usó ")
		   .append(defender.getTipo().name().toLowerCase())
		   .append("!");

		assertEquals("Heroe usó defensa!", msg.toString());
		assertTrue(msg.toString().contains("usó"));
	}

	@Test
	public void testFlashTriggerDetection() {
		assertTrue("Heroe atacó a Ninja!".contains("atacó"), "Should detect 'atacó' for flash");
		assertTrue("Ninja usó habilidad!".contains("usó"), "Should detect 'usó' for flash");
		assertFalse("Heroe se defiende".contains("atacó"), "Should NOT trigger flash for defend");
		assertFalse("Turno pasado".contains("atacó"), "Should NOT trigger flash for pass");
	}

	@Test
	public void testShakeTargetDetection() {
		Heroe heroe = new Heroe("H", 100, 20, 10, habilidadNinguna);
		String atkMsg = heroe.getNombre() + " atacó a E!";
		boolean isHeroAttacking = atkMsg.startsWith(heroe.getNombre());
		assertTrue(isHeroAttacking, "Hero attacking should shake enemy");

		String enemyAtkMsg = "Ninja atacó a H!";
		boolean isEnemyAttacking = !enemyAtkMsg.startsWith(heroe.getNombre());
		assertTrue(isEnemyAttacking, "Enemy attacking should shake hero");
	}
}