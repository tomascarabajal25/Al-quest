package juego.ciudades.batalla.controller;

import modelos.Partida;
import modelos.Sonido;
import modelos.Jugador;
import juego.ciudades.ordenamientos.EstadoDePartida;
import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.view.BatallaUI;
import estructuras.cola.Cola;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class PartidaBatalla extends Partida {

	private BatallaUI ui;
	private boolean victoria;
	private final Set<Integer> dificultadesGanadas = new HashSet<>();
	private int puntajeAcumulado;
	private int puntajeTotal;

	public PartidaBatalla(String nombre, Jugador jugador, Sonido sonido) {
		super(nombre, jugador);
		setSonido(sonido);
	}

	@Override
	public void iniciar() {
		setEstado(EstadoDePartida.Iniciado);

		final int dificultad = pedirDificultad();
		if (dificultad < 1) {
			setPuntaje(0);
			finalizar();
			return;
		}

		int victorias = dificultadesGanadas.size();
		int puntajeGeneral = puntajeTotal / 5000;
		Heroe heroe = Heroe.desdeJugador(
				getJugador(),
				80 + victorias * 40 + puntajeGeneral * 10,
				25 + victorias * 10 + puntajeGeneral * 5,
				5 + victorias + puntajeGeneral
		);
		final List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(dificultad);
		Cola<Combatiente> turnos = new Cola<>();
		turnos.add(heroe);
		if (enemigos != null) {
			turnos.addAll(enemigos);
		}

		ui = new BatallaUI(heroe, enemigos, getRutaSprites(), dificultad);
		ui.setOnClose(this::finalizar);

		new Thread(() -> {
			ResultadoBatalla resultado = new Batalla(ui, turnos, enemigos, dificultad).empezar();
			SwingUtilities.invokeLater(() -> {
				if (ui == null) return;

				if (resultado.esVictoria() && !dificultadesGanadas.contains(dificultad)) {
					dificultadesGanadas.add(dificultad);
					puntajeAcumulado += resultado.getPuntaje();
				}

				ui.mostrarResultado(resultado, dificultadesGanadas, () -> {
					victoria = resultado.esVictoria();
					finalizar();
				});
			});
		}, "batalla-game-loop").start();
		if (this.sonido != null) {
      this.sonido.playMusica(juego.configuracion.ConstantesSonido.BATALLA);
		}
	}

	@Override
	public void finalizar() {
		boolean todasCompletadas = dificultadesGanadas.size() == 3;
		setPuntaje(todasCompletadas ? puntajeAcumulado : 0);

		if (ui != null) {
			ui.cerrar();
			ui = null;
		}
		setEstado(EstadoDePartida.Creado);
		if (this.sonido != null) {
            this.sonido.stopMusica();
            this.sonido.playMusica(juego.configuracion.ConstantesSonido.GLOBAL_AVENTURA);
        }
		notificarFinalizacion();
	}

	public Set<Integer> getDificultadesGanadas() {
		return dificultadesGanadas;
	}

	public void setPuntajeTotal(int puntajeTotal) {
		this.puntajeTotal = puntajeTotal;
	}

	public void restaurarDificultades(Vector<Integer> dificultades) {
		for (int d : dificultades) {
			if (d >= 1 && d <= 3 && !dificultadesGanadas.contains(d)) {
				dificultadesGanadas.add(d);
				puntajeAcumulado += puntajePorDificultad(d);
			}
		}
	}

	private static int puntajePorDificultad(int dificultad) {

		switch (dificultad) {
			case 1: return 1000;
			case 2: return 5000;
			case 3: return 15000;
			default: return 0;
		}
	}

	private int pedirDificultad() {
		Object seleccion = JOptionPane.showInputDialog(
				null,
				"Elija la dificultad:",
				"Ciudad Batalla",
				JOptionPane.QUESTION_MESSAGE,
				null,
				new Object[]{"1 - Facil", "2 - Normal", "3 - Dificil"},
				"1 - Facil"
		);
		if (seleccion == null) return -1;
		String texto = seleccion.toString();
		if (texto.startsWith("1")) return 1;
		if (texto.startsWith("2")) return 2;
		if (texto.startsWith("3")) return 3;
		return -1;
	}
}
