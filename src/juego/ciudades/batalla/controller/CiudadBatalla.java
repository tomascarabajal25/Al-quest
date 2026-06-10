package juego.ciudades.batalla.controller;

import estructuras.cola.Cola;
import juego.ciudades.batalla.model.*;
import juego.ciudades.batalla.view.BatallaUI;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class CiudadBatalla {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(CiudadBatalla::run);
	}

	private static void run() {
		final int dificultad = pedirDificultad();
		if (dificultad < 1) return;

		HabilidadEspecial ninguna = (personaje, objetivo) -> {};
		Heroe heroe = new Heroe("Heroe", 100, 40, 5, ninguna);
		final List<Enemigo> enemigos = ManagerBatalla.generarEnemigos(dificultad);
		Cola<Combatiente> turnos = new Cola<>();
		turnos.add(heroe);
		if (enemigos != null) { turnos.addAll(enemigos); }

		final BatallaUI ui = new BatallaUI(heroe, enemigos);

		new Thread(() -> {
			boolean victoria = new Batalla(ui, turnos, enemigos, dificultad).empezar();
			SwingUtilities.invokeLater(() -> {
				ui.cerrar();
				JOptionPane.showMessageDialog(
						null,
						victoria ? "Victoria!" : "Derrota...",
						"Ciudad Batalla",
						victoria ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE
				);
			});
		}, "batalla-game-loop").start();
	}

	private static int pedirDificultad() {
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
