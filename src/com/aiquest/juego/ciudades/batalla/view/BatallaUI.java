package com.aiquest.juego.ciudades.batalla.view;

import com.aiquest.estructuras.pilas.Pila;
import com.aiquest.juego.ciudades.batalla.model.Accion;
import com.aiquest.juego.ciudades.batalla.model.Combatiente;
import com.aiquest.juego.ciudades.batalla.model.Enemigo;
import com.aiquest.juego.ciudades.batalla.model.acciones.Atacar;
import com.aiquest.juego.ciudades.batalla.model.acciones.Defender;
import com.aiquest.juego.ciudades.batalla.view.models.enemies.Enemy;
import com.aiquest.juego.ciudades.batalla.view.models.enemies.EnemyFactory;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BatallaUI {

	private static final Color BG = Color.WHITE;
	private static final Color BORDER = Color.BLACK;
	private static final Font FONT_HUD = new Font("Monospaced", Font.BOLD, 14);
	private static final Font FONT_SMALL = new Font("Monospaced", Font.PLAIN, 11);
	private static final Font FONT_LOG = new Font("Monospaced", Font.PLAIN, 12);
	private static final int MAX_LOG_LINES = 5;

	private final Combatiente heroe;
	private final List<Enemigo> enemigos;
	private final List<Enemy> viewEnemies;
	private final BlockingQueue<String> colaComandos = new LinkedBlockingQueue<>();
	private final List<String> actionLog = new ArrayList<>();

	private final int[] vidaInicialEnemigos;
	private final int vidaInicialHeroe;
	private volatile int enemigoActivoIdx = 0;

	private JFrame frame;
	private BattleCanvas canvas;
	private JPanel buttonPanel;

	public BatallaUI(Combatiente heroe, List<Enemigo> enemigos) {
		this.heroe = heroe;
		this.enemigos = enemigos;
		this.vidaInicialHeroe = heroe.getVida();
		this.vidaInicialEnemigos = new int[enemigos != null ? enemigos.size() : 0];
		this.viewEnemies = new ArrayList<>();

		if (enemigos != null) {
			for (int i = 0; i < enemigos.size(); i++) {
				vidaInicialEnemigos[i] = enemigos.get(i).getVida();
				viewEnemies.add(EnemyFactory.fromEnemigo(enemigos.get(i)));
			}
		}

		SwingUtilities.invokeLater(this::createAndShowGUI);
	}

	private void createAndShowGUI() {
		frame = new JFrame("Batalla");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		canvas = new BattleCanvas();
		canvas.setPreferredSize(new Dimension(736, 414));
		frame.add(canvas, BorderLayout.CENTER);

		buttonPanel = new JPanel(new FlowLayout());
		instalarMenuPrincipal();
		frame.add(buttonPanel, BorderLayout.SOUTH);

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		actualizarEstado("");
	}

	public void actualizarEstado(String mensaje) {
		actualizarEstado(mensaje, null);
	}

	public void actualizarEstado(String mensaje, Combatiente actual) {
		normalizarIndiceActivo();
		if (actual instanceof Enemigo) {
			int idx = enemigos.indexOf(actual);
			if (idx >= 0) enemigoActivoIdx = idx;
		}

		sincronizarVida();

		if (mensaje != null && !mensaje.isEmpty()) {
			actionLog.add(mensaje);
			if (actionLog.size() > MAX_LOG_LINES) actionLog.remove(0);
		}

		SwingUtilities.invokeLater(() -> canvas.repaint());
	}

	public Pila<Accion> solicitarAcciones(int cantidad) throws InterruptedException {
		Pila<Accion> pila = new Pila<>();
		SwingUtilities.invokeLater(this::instalarMenuPrincipal);
		for (int i = 0; i < cantidad; i++) {
			String cmd = colaComandos.take();
			if ("PASAR".equals(cmd)) break;
			if ("DEFENDER".equals(cmd)) {
				pila.push(new Defender(heroe, heroe));
			} else if (cmd.startsWith("ATACAR:")) {
				int idx;
				try {
					idx = Integer.parseInt(cmd.substring(7));
				} catch (NumberFormatException ex) {
					i--;
					continue;
				}
				Enemigo e = (idx >= 0 && idx < enemigos.size()) ? enemigos.get(idx) : null;
				if (e != null && e.estaVivo()) {
					enemigoActivoIdx = idx;
					pila.push(new Atacar(heroe, e));
				} else {
					i--;
				}
			}
		}
		return pila;
	}

	public void cerrar() {
		if (frame != null) {
			SwingUtilities.invokeLater(() -> frame.dispose());
		}
	}

	private void instalarMenuPrincipal() {
		buttonPanel.removeAll();
		JButton btnAtacar = new JButton("ATACAR");
		btnAtacar.addActionListener(e -> instalarMenuEnemigos());
		buttonPanel.add(btnAtacar);

		JButton btnDefender = new JButton("DEFENDER");
		btnDefender.addActionListener(e -> colaComandos.offer("DEFENDER"));
		buttonPanel.add(btnDefender);

		JButton btnPasar = new JButton("PASAR");
		btnPasar.addActionListener(e -> colaComandos.offer("PASAR"));
		buttonPanel.add(btnPasar);

		buttonPanel.revalidate();
		buttonPanel.repaint();
	}

	private void instalarMenuEnemigos() {
		buttonPanel.removeAll();
		for (int i = 0; i < enemigos.size(); i++) {
			Enemigo e = enemigos.get(i);
			if (!e.estaVivo()) continue;
			final int idx = i;
			JButton btn = new JButton("→ " + e.getNombre());
			btn.addActionListener(ev -> {
				colaComandos.offer("ATACAR:" + idx);
				enemigoActivoIdx = idx;
				SwingUtilities.invokeLater(this::instalarMenuPrincipal);
			});
			buttonPanel.add(btn);
		}
		JButton btnVolver = new JButton("← VOLVER");
		btnVolver.addActionListener(e -> instalarMenuPrincipal());
		buttonPanel.add(btnVolver);

		buttonPanel.revalidate();
		buttonPanel.repaint();
	}

	private void normalizarIndiceActivo() {
		if (enemigos == null || enemigos.isEmpty()) { enemigoActivoIdx = 0; return; }
		if (enemigoActivoIdx < 0 || enemigoActivoIdx >= enemigos.size()) {
			enemigoActivoIdx = primerVivo();
			return;
		}
		if (!enemigos.get(enemigoActivoIdx).estaVivo()) {
			enemigoActivoIdx = primerVivo();
		}
	}

	private int primerVivo() {
		if (enemigos == null) return 0;
		for (int i = 0; i < enemigos.size(); i++) {
			if (enemigos.get(i).estaVivo()) return i;
		}
		return 0;
	}

	private int contarVivos() {
		if (enemigos == null) return 0;
		int c = 0;
		for (Enemigo e : enemigos) { if (e.estaVivo()) c++; }
		return c;
	}

	private void sincronizarVida() {
		if (enemigos == null) return;
		for (int i = 0; i < enemigos.size() && i < viewEnemies.size(); i++) {
			viewEnemies.get(i).setHp(enemigos.get(i).getVida());
		}
	}

	private class BattleCanvas extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

			int w = getWidth();
			int h = getHeight();

			g2d.setColor(BG);
			g2d.fillRect(0, 0, w, h);

			drawEnemyStatusPanel(g2d, w, h);
			drawEnemySprites(g2d, w, h);
			drawHero(g2d, w, h);
			drawActionLog(g2d, w, h);
			drawHeroStatus(g2d, w, h);
		}

		private void drawEnemyStatusPanel(Graphics2D g2d, int w, int h) {
			if (viewEnemies.isEmpty()) return;

			int panelX = 10;
			int panelY = 10;
			int panelW = 185;
			int lineH = 22;

			g2d.setFont(FONT_HUD);
			FontMetrics fmHud = g2d.getFontMetrics();

			for (int i = 0; i < viewEnemies.size(); i++) {
				Enemy e = viewEnemies.get(i);
				boolean isActive = (i == enemigoActivoIdx);
				boolean isFainted = e.isFainted();
				int lineY = panelY + (i + 1) * lineH;

				if (isActive && !isFainted) {
					g2d.setColor(new Color(220, 240, 220));
					g2d.fillRect(panelX, lineY - fmHud.getAscent(), panelW, lineH);
				}

				g2d.setFont(FONT_HUD);
				g2d.setColor(isFainted ? Color.GRAY : BORDER);

				String prefix = isActive ? "> " : "  ";
				String name = e.getName();
				if (name.length() > 8) name = name.substring(0, 8);

				g2d.drawString(prefix + name, panelX + 4, lineY);

				String hpStr = e.getHp() + "/" + e.getMaxHp();
				int hpX = panelX + panelW - fmHud.stringWidth(hpStr) - 8;
				g2d.drawString(hpStr, hpX, lineY);

				if (isFainted) {
					g2d.setFont(FONT_SMALL);
					g2d.drawString("KO", panelX + panelW - 30, lineY);
				}
			}
		}

		private void drawEnemySprites(Graphics2D g2d, int w, int h) {
			if (viewEnemies.isEmpty()) return;

			int count = viewEnemies.size();
			int spriteSize = 96;
			int gap = 14;
			int totalW = count * spriteSize + (count - 1) * gap;
			int startX = w - totalW - 10;
			int spriteY = 20;

			g2d.setFont(FONT_SMALL);
			FontMetrics fmSmall = g2d.getFontMetrics();

			for (int i = 0; i < count; i++) {
				Enemy e = viewEnemies.get(i);
				int x = startX + i * (spriteSize + gap);
				boolean isActive = (i == enemigoActivoIdx);
				boolean isFainted = e.isFainted();

				e.draw(g2d, x, spriteY, 1);

				if (isFainted) {
					Composite orig = g2d.getComposite();
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
					g2d.setColor(Color.LIGHT_GRAY);
					g2d.fillRect(x, spriteY, spriteSize, spriteSize);
					g2d.setComposite(orig);
					g2d.setFont(FONT_HUD);
					FontMetrics fmHud = g2d.getFontMetrics();
					g2d.setColor(Color.RED);
					String ko = "KO";
					g2d.drawString(ko, x + (spriteSize - fmHud.stringWidth(ko)) / 2, spriteY + spriteSize / 2 + 6);
				}

				if (isActive && !isFainted) {
					g2d.setColor(BORDER);
					g2d.setStroke(new BasicStroke(3));
					g2d.drawRect(x - 3, spriteY - 3, spriteSize + 6, spriteSize + 6);
					g2d.setStroke(new BasicStroke(1));
				}
			}
		}

		private void drawHero(Graphics2D g2d, int w, int h) {
			int heroX = 20;
			int heroY = (int) (h * 0.52);
			int size = 96;
			g2d.setColor(new Color(80, 128, 224));
			g2d.fillRect(heroX, heroY, size, size);
			g2d.setColor(BORDER);
			g2d.drawRect(heroX, heroY, size, size);
			g2d.setColor(new Color(56, 40, 32));
			g2d.fillRect(heroX + 8, heroY + 4, size - 16, 20);
			g2d.setColor(new Color(40, 64, 112));
			g2d.fillRect(heroX + 12, heroY + 24, size - 24, 16);
		}

		private void drawActionLog(Graphics2D g2d, int w, int h) {
			int logX = 140;
			int logY = (int) (h * 0.55);
			int logW = w - logX - 10;
			int logH = (int) (h * 0.40);

			g2d.setColor(BG);
			g2d.fillRoundRect(logX, logY, logW, logH, 8, 8);
			g2d.setColor(BORDER);
			g2d.setStroke(new BasicStroke(3));
			g2d.drawRoundRect(logX, logY, logW, logH, 8, 8);
			g2d.setStroke(new BasicStroke(1));

			g2d.setFont(FONT_LOG);
			g2d.setColor(BORDER);

			int lineH = 18;
			int startY = logY + 20;
			for (int i = 0; i < actionLog.size(); i++) {
				g2d.drawString(actionLog.get(i), logX + 10, startY + i * lineH);
			}

			if (actionLog.isEmpty()) {
				g2d.setColor(Color.GRAY);
				g2d.drawString("...", logX + 10, startY);
			}
		}

		private void drawHeroStatus(Graphics2D g2d, int w, int h) {
			int statusX = 20;
			int statusY = (int) (h * 0.52) + 96 + 10;

			g2d.setFont(FONT_HUD);
			g2d.setColor(BORDER);

			String name = heroe.getNombre();
			int hpNow = heroe.getVida();
			int hpMax = vidaInicialHeroe;

			g2d.drawString(name, statusX, statusY);
			g2d.drawString("HP " + hpNow + "/" + hpMax, statusX, statusY + 20);
		}
	}
}