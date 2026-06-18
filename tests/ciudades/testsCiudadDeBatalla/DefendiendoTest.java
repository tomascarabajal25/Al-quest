package ciudades.testsCiudadDeBatalla;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.model.estados.Defendiendo;

public class DefendiendoTest {

	private HabilidadEspecial habilidadNinguna = (p, o) -> {};

	@Test
	public void testConstructorAsignaTipoCorrecto() {
		Heroe h = new Heroe("H", 100, 20, 10, habilidadNinguna);
		Defendiendo d = new Defendiendo(h);
		assertEquals(EstadoCombatiente.DEFENDIENDO, d.getEstado());
	}

	@Test
	public void testConstructorAsignaTurnosRestantesUno() {
		Heroe h = new Heroe("H", 100, 20, 10, habilidadNinguna);
		Defendiendo d = new Defendiendo(h);
		assertEquals(1, d.getTurnosRestantes());
	}

	@Test
	public void testAplicarNoModificaVida() {
		Heroe h = new Heroe("H", 100, 20, 10, habilidadNinguna);
		int vidaAntes = h.getVida();
		Defendiendo d = new Defendiendo(h);
		d.aplicar();
		assertEquals(vidaAntes, h.getVida());
	}

	@Test
	public void testAplicarNoModificaTurnosRestantes() {
		Heroe h = new Heroe("H", 100, 20, 10, habilidadNinguna);
		Defendiendo d = new Defendiendo(h);
		int turnosAntes = d.getTurnosRestantes();
		d.aplicar();
		assertEquals(turnosAntes, d.getTurnosRestantes());
	}

	@Test
	public void testOrigenYDestinoSonElMismo() {
		Heroe h = new Heroe("H", 100, 20, 10, habilidadNinguna);
		Defendiendo d = new Defendiendo(h);
		assertEquals(h, d.getOrigen());
		assertEquals(h, d.getDestino());
	}

	@Test
	public void testTerminadoEsFalseInicialmente() {
		Heroe h = new Heroe("H", 100, 20, 10, habilidadNinguna);
		Defendiendo d = new Defendiendo(h);
		assertFalse(d.terminado());
	}

	@Test
	public void testEsEstadoActivo() {
		Heroe h = new Heroe("H", 100, 20, 10, habilidadNinguna);
		Defendiendo d = new Defendiendo(h);
		assertTrue(d instanceof EstadoActivo);
	}
}
