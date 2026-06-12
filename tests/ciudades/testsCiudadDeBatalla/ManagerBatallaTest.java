package ciudades.testsCiudadDeBatalla;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import estructuras.pilas.Pila;
import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.model.acciones.Atacar;
import juego.ciudades.batalla.model.acciones.Defender;
import juego.ciudades.batalla.controller.ManagerBatalla;

import java.util.List;

public class ManagerBatallaTest {

	private HabilidadEspecial habilidadNinguna = (personaje, objetivo) -> {};

	@Test
	public void testGenerarEnemigosDificultad1Devuelve1Enemigo() {
		List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(1);
		assertNotNull(enemigos);
		assertEquals(1, enemigos.size());
	}

	@Test
	public void testGenerarEnemigosDificultad1SinHabilidadEspecial() {
		List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(1);
		for (Enemigo e : enemigos) {
			int vidaAntes = e.getVida();
			e.usarHabilidadEspecial(e);
			assertEquals(vidaAntes, e.getVida());
		}
	}

	@Test
	public void testGenerarEnemigosDificultad1RangoVida() {
		for (int i = 0; i < 50; i++) {
			List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(1);
			for (Enemigo e : enemigos) {
				assertTrue(e.getVida() >= 30 && e.getVida() <= 50,
						"Vida fuera de rango: " + e.getVida());
			}
		}
	}

	@Test
	public void testGenerarEnemigosDificultad1RangoFuerza() {
		for (int i = 0; i < 50; i++) {
			List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(1);
			for (Enemigo e : enemigos) {
				assertTrue(e.getFuerza() >= 5 && e.getFuerza() <= 8,
						"Fuerza fuera de rango: " + e.getFuerza());
			}
		}
	}

	@Test
	public void testGenerarEnemigosDificultad1RangoArmadura() {
		for (int i = 0; i < 50; i++) {
			List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(1);
			for (Enemigo e : enemigos) {
				assertTrue(e.getArmadura() >= 0 && e.getArmadura() <= 2,
						"Armadura fuera de rango: " + e.getArmadura());
			}
		}
	}

	@Test
	public void testGenerarEnemigosDificultad2Devuelve3Enemigos() {
		List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(2);
		assertNotNull(enemigos);
		assertEquals(3, enemigos.size());
	}

	@Test
	public void testGenerarEnemigosDificultad2RangoVida() {
		for (int i = 0; i < 50; i++) {
			List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(2);
			for (Enemigo e : enemigos) {
				assertTrue(e.getVida() >= 60 && e.getVida() <= 90,
						"Vida fuera de rango: " + e.getVida());
			}
		}
	}

	@Test
	public void testGenerarEnemigosDificultad2RangoFuerza() {
		for (int i = 0; i < 50; i++) {
			List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(2);
			for (Enemigo e : enemigos) {
				assertTrue(e.getFuerza() >= 10 && e.getFuerza() <= 15,
						"Fuerza fuera de rango: " + e.getFuerza());
			}
		}
	}

	@Test
	public void testGenerarEnemigosDificultad3Devuelve5Enemigos() {
		List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(3);
		assertNotNull(enemigos);
		assertEquals(5, enemigos.size());
	}

	@Test
	public void testGenerarEnemigosDificultad3RangoVida() {
		for (int i = 0; i < 50; i++) {
			List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(3);
			for (Enemigo e : enemigos) {
				assertTrue(e.getVida() >= 100 && e.getVida() <= 150,
						"Vida fuera de rango: " + e.getVida());
			}
		}
	}

	@Test
	public void testGenerarEnemigosDificultadInvalidaDevuelveNull() {
		assertNull(ManagerBatalla.generarEnemigos(0));
		assertNull(ManagerBatalla.generarEnemigos(4));
		assertNull(ManagerBatalla.generarEnemigos(-1));
		assertNull(ManagerBatalla.generarEnemigos(99));
	}

	@Test
	public void testGenerarEnemigosTiposValidos() {
		for (int d = 1; d <= 3; d++) {
			List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(d);
			for (Enemigo e : enemigos) {
				assertNotNull(e.getTipo());
				assertTrue(e.getTipo().name().matches("NINJA|SAMURAI|VIKINGO|CABALLERO|BUFON|DUENDE|ROBOT"));
			}
		}
	}

	@Test
	public void testGenerarEnemigosNoTiposRepetidos() {
		for (int d = 1; d <= 3; d++) {
			for (int i = 0; i < 50; i++) {
				List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(d);
				java.util.Set<TipoEnemigo> tiposVistos = new java.util.HashSet<>();
				for (Enemigo e : enemigos) {
					assertTrue(tiposVistos.add(e.getTipo()),
							"Tipo repetido: " + e.getTipo() + " en dificultad " + d);
				}
			}
		}
	}

	@Test
	public void testGenerarEnemigosTodosVivosAlInicio() {
		for (int d = 1; d <= 3; d++) {
			List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(d);
			for (Enemigo e : enemigos) {
				assertTrue(e.estaVivo());
			}
		}
	}

	@Test
	public void testEjecutarAccionesVaciaNoCrashea() {
		Pila<Accion> pila = new Pila<>();
		assertDoesNotThrow(() -> ManagerBatalla.ejecutarAcciones(pila));
	}

	@Test
	public void testEjecutarAccionesConUnaAccion() {
		Heroe heroe = new Heroe("H", 100, 20, 10, habilidadNinguna);
		Enemigo enemigo = new Enemigo("E", TipoEnemigo.NINJA, 80, 15, 5, habilidadNinguna);
		Pila<Accion> pila = new Pila<>();
		pila.push(new Atacar(heroe, enemigo));
		ManagerBatalla.ejecutarAcciones(pila);
		assertTrue(pila.isEmpty());
		assertTrue(enemigo.getVida() < 80);
	}

	@Test
	public void testEjecutarAccionesConMultiplesAcciones() {
		Heroe heroe = new Heroe("H", 100, 20, 10, habilidadNinguna);
		Enemigo enemigo = new Enemigo("E", TipoEnemigo.NINJA, 80, 15, 5, habilidadNinguna);
		Pila<Accion> pila = new Pila<>();
		pila.push(new Atacar(heroe, enemigo));
		pila.push(new Atacar(heroe, enemigo));
		int vidaAntes = enemigo.getVida();
		ManagerBatalla.ejecutarAcciones(pila);
		assertTrue(enemigo.getVida() < vidaAntes);
	}

	@Test
	public void testElegirAccionEnemigoDevuelveAtacar() {
		Heroe heroe = new Heroe("H", 100, 20, 10, habilidadNinguna);
		Enemigo enemigo = new Enemigo("E", TipoEnemigo.NINJA, 80, 15, 5, habilidadNinguna);
		Pila<Accion> acciones = ManagerBatalla.elegirAccionEnemigo(enemigo, heroe);
		assertNotNull(acciones);
		assertFalse(acciones.isEmpty());

		Accion accion = acciones.pop();
		assertEquals(TipoAccion.ATAQUE, accion.getTipo());
		assertEquals(enemigo, accion.getCombatiente());
		assertEquals(heroe, accion.getObjetivo());
	}

	@Test
	public void testTodosVivosConNull() {
		assertFalse(ManagerBatalla.todosVivos(null));
	}

	@Test
	public void testTodosVivosConTodosVivos() {
		Enemigo e1 = new Enemigo("A", TipoEnemigo.NINJA, 80, 10, 5, habilidadNinguna);
		Enemigo e2 = new Enemigo("B", TipoEnemigo.SAMURAI, 110, 15, 10, habilidadNinguna);
		List<Enemigo> lista = new java.util.ArrayList<>();
		lista.add(e1);
		lista.add(e2);
		assertFalse(ManagerBatalla.todosVivos(lista));
	}

	@Test
	public void testTodosVivosConAlgunoMuerto() {
		Enemigo e1 = new Enemigo("A", TipoEnemigo.NINJA, 80, 10, 5, habilidadNinguna);
		Enemigo e2 = new Enemigo("B", TipoEnemigo.SAMURAI, 110, 15, 10, habilidadNinguna);
		e1.setVida(0);
		List<Enemigo> lista = new java.util.ArrayList<>();
		lista.add(e1);
		lista.add(e2);
		assertTrue(ManagerBatalla.todosVivos(lista));
	}

	@Test
	public void testTodosVivosConTodosMuertos() {
		Enemigo e1 = new Enemigo("A", TipoEnemigo.NINJA, 80, 10, 5, habilidadNinguna);
		Enemigo e2 = new Enemigo("B", TipoEnemigo.SAMURAI, 110, 15, 10, habilidadNinguna);
		e1.setVida(0);
		e2.setVida(0);
		List<Enemigo> lista = new java.util.ArrayList<>();
		lista.add(e1);
		lista.add(e2);
		assertTrue(ManagerBatalla.todosVivos(lista));
	}
}