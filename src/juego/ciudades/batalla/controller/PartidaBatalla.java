package juego.ciudades.batalla.controller;

import modelos.Partida;
import modelos.Jugador;
import juego.ciudades.ordenamientos.EstadoDePartida;
import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.view.BatallaUI;
import estructuras.cola.Cola;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class PartidaBatalla extends Partida {

	private BatallaUI ui;
	private boolean victoria;

	public PartidaBatalla(String nombre, Jugador jugador) {
		super(nombre, jugador);
	}

	@Override
	public void iniciar() {
		setEstado(EstadoDePartida.Iniciado);

		final int dificultad = pedirDificultad();
		if (dificultad < 1) {
			finalizar();
			return;
		}

		HabilidadEspecial ninguna = (personaje, objetivo) -> {};
		Heroe heroe = Heroe.desdeJugador(getJugador(), 100, 40, 5, ninguna);
		final List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(dificultad);
		Cola<Combatiente> turnos = new Cola<>();
		turnos.add(heroe);
		if (enemigos != null) {
			turnos.addAll(enemigos);
		}

		ui = new BatallaUI(heroe, enemigos);

		new Thread(() -> {
			victoria = new Batalla(ui, turnos, enemigos, dificultad).empezar();
			SwingUtilities.invokeLater(this::finalizar);
		}, "batalla-game-loop").start();
		if (this.sonido != null) {
            this.sonido.playMusica(juego.configuracion.ConstantesSonido.BATALLA);
        }
	}

	@Override
	public void finalizar() {
		setPuntaje(victoria ? 100 : 0);
		if (ui != null) {
			ui.cerrar();
		}
		setEstado(EstadoDePartida.Creado);
		if (this.sonido != null) {
            this.sonido.stopMusica();
            this.sonido.playMusica(juego.configuracion.ConstantesSonido.GLOBAL_AVENTURA);
        }
		notificarFinalizacion();
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
