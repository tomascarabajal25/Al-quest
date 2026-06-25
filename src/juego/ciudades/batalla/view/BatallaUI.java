package juego.ciudades.batalla.view;

import juego.ciudades.batalla.model.Accion;
import juego.ciudades.batalla.model.Combatiente;
import juego.ciudades.batalla.model.Enemigo;
import juego.ciudades.batalla.model.ResultadoBatalla;
import juego.ciudades.batalla.view.animacion.Animacion;
import juego.ciudades.batalla.view.animacion.AnimacionManager;
import juego.ciudades.batalla.view.menu.EstadoMenu;
import juego.ciudades.batalla.view.menu.MenuPrincipal;
import juego.ciudades.batalla.view.menu.SeleccionEnemigo;
import juego.ciudades.batalla.view.models.Enemy;
import juego.ciudades.batalla.view.models.EnemyFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.imageio.ImageIO;
import javax.swing.Timer;

public class BatallaUI {

	private static final Color BG_SKY         = new Color(120, 180, 240);
	private static final Color BG_GROUND      = new Color( 80, 160,  80);
	private static final Color BG_GROUND_DARK = new Color( 50, 110,  40);
	private static final Color HP_GREEN       = new Color( 48, 200,  80);
	private static final Color HP_YELLOW      = new Color(240, 200,  40);
	private static final Color HP_RED         = new Color(220,  40,  40);
	private static final Color HP_BG          = new Color( 30,  30,  40);
	private static final Color HP_BORDER      = new Color(180, 180, 200);
	private static final Color PANEL_BG       = new Color( 30,  30,  50, 220);
	private static final Color PANEL_BORDER   = new Color(140, 140, 180);
	private static final Color PANEL_TEXT     = new Color(240, 240, 255);
	private static final Color DIALOG_BG      = new Color( 28,  28,  48, 235);
	private static final Color DIALOG_BORDER  = new Color(180, 180, 210);
	private static final Color DIALOG_TEXT    = new Color(240, 240, 255);
	private static final Color MENU_DISABLED  = new Color(100, 100, 120);
	private static final Color SELECTED_BORDER= new Color(255, 220,  40);

	private Runnable onCloseCallback;

	private static final Font FONT_HUD      = new Font("Monospaced", Font.BOLD, 13);
	private static final Font FONT_HP_NUM   = new Font("Monospaced", Font.BOLD, 14);
	private static final Font FONT_DIALOG   = new Font("Monospaced", Font.PLAIN, 15);
	private static final Font FONT_MENU     = new Font("Monospaced", Font.BOLD, 16);
	private static final Font FONT_MENU_SM  = new Font("Monospaced", Font.BOLD, 12);
	private static final Font FONT_TITLE    = new Font("Monospaced", Font.BOLD, 28);

	private static final int HERO_X        = BatallaLayout.HERO_X;
	private static final int HERO_Y        = BatallaLayout.HERO_Y;
	private static final int HERO_SIZE     = BatallaLayout.HERO_SIZE;
	private static final int HERO_STATUS_W = BatallaLayout.HERO_STATUS_W;
	private static final int ENEMY_Y       = BatallaLayout.ENEMY_Y;
	private static final int ENEMY_SIZE    = BatallaLayout.ENEMY_SIZE;
	private static final int ENEMY_GAP     = BatallaLayout.ENEMY_GAP;
	private static final int STATUS_BOX_Y  = 17;
	private static final int STATUS_BOX_W  = 96;
	private static final int STATUS_BOX_H  = 34;
	private static final int HUD_Y         = BatallaLayout.HUD_Y;
	private static final int HUD_H         = BatallaLayout.HUD_H;
	private static final int DIALOG_Y      = BatallaLayout.DIALOG_Y;

	private final Combatiente heroe;
	private final List<Enemigo> enemigos;
	private final List<Enemy> viewEnemies;
	private final List<String> actionLog = new ArrayList<>();
	private final int[] vidaInicialEnemigos;
	private final int vidaInicialHeroe;
	private volatile int enemigoActivoIdx;
	private final String rutaSprites;

	private JFrame frame;
	private BattleCanvas canvas;
	private BufferedImage heroSprite;

	private double displayedHeroHp;
	private final double[] displayedEnemyHp;
	private Timer animTimer;

	private final AnimacionManager animManager = new AnimacionManager();
	private final BlockingQueue<Accion> colaAcciones = new LinkedBlockingQueue<>();
	private EstadoMenu estadoMenu;
	private volatile String indicadorAccion;
	private volatile ResultadoBatalla resultado;
	private volatile Runnable onResultadoCerrado;
	private Set<Integer> dificultadesGanadas;

	public BatallaUI(Combatiente heroe, List<Enemigo> enemigos, String rutaSprites, int dificultad) {
		this.heroe = heroe;
		this.enemigos = enemigos;
		this.vidaInicialHeroe = heroe.getVida();
		this.vidaInicialEnemigos = new int[enemigos != null ? enemigos.size() : 0];
		this.viewEnemies = new ArrayList<>();
		this.enemigoActivoIdx = (enemigos != null && !enemigos.isEmpty()) ? 0 : 0;
		this.rutaSprites = rutaSprites;

		if (enemigos != null) {
			for (int i = 0; i < enemigos.size(); i++) {
				vidaInicialEnemigos[i] = enemigos.get(i).getVida();
				viewEnemies.add(EnemyFactory.fromEnemigo(enemigos.get(i), dificultad));
			}
		}

		displayedHeroHp = heroe.getVida();
		displayedEnemyHp = new double[enemigos != null ? enemigos.size() : 0];
		for (int i = 0; i < displayedEnemyHp.length; i++) {
			displayedEnemyHp[i] = enemigos.get(i).getVida();
		}

		loadHeroSprite();

		SwingUtilities.invokeLater(this::createAndShowGUI);
	}

	private void createAndShowGUI() {
		frame = new JFrame("Batalla");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		if (onCloseCallback != null) {
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					if (animTimer != null) animTimer.stop();
					try {
						onCloseCallback.run();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			});
		}
		frame.setLayout(new BorderLayout());

		canvas = new BattleCanvas();
		canvas.setPreferredSize(new Dimension(BatallaLayout.CANVAS_W, BatallaLayout.CANVAS_H));
		canvas.setFocusable(true);
		frame.add(canvas, BorderLayout.CENTER);

		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				handleClick(e.getX(), e.getY());
			}
		});

		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		animTimer = new Timer(30, e -> tick());
		animTimer.start();

		actualizarEstado("");
	}

	private void handleClick(int mx, int my) {
		if (resultado != null) {
			ResultadoBatalla resultadoActual = resultado;
			Runnable callback = onResultadoCerrado;
			resultado = null;
			onResultadoCerrado = null;
			dificultadesGanadas = null;
			if (resultadoActual != null && callback != null) {
				callback.run();
			}
			return;
		}

		if (estadoMenu == null) return;
		Accion accion = estadoMenu.onClick(mx, my);
		if (accion != null) {
			colaAcciones.offer(accion);
		}
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
		}

		SwingUtilities.invokeLater(() -> canvas.repaint());
	}

	public Accion solicitarAccion() throws InterruptedException {
		Accion accion = colaAcciones.take();
		return accion;
	}

	public void registrarAnimacion(Animacion anim) {
		animManager.agregar(anim);
	}

	public void setEstadoMenu(EstadoMenu nuevo) {
		if (estadoMenu != null) estadoMenu.onExit();
		this.estadoMenu = nuevo;
		if (nuevo != null) nuevo.onEnter();
		if (canvas != null) canvas.repaint();
	}

	public void mostrarMenuPrincipal() {
		MenuPrincipal menu = new MenuPrincipal(heroe, enemigos, () -> {
			SeleccionEnemigo sel = new SeleccionEnemigo(heroe, enemigos, () -> {
				mostrarMenuPrincipal();
			});
			sel.setEnemigoActivoIdx(enemigoActivoIdx);
			setEstadoMenu(sel);
		});
		setEstadoMenu(menu);
	}

	public void mostrarIndicadorAccion(int numero) {
		this.indicadorAccion = "ELEGIR ACCION Nº" + numero;
		SwingUtilities.invokeLater(() -> canvas.repaint());
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		this.indicadorAccion = null;
		SwingUtilities.invokeLater(() -> canvas.repaint());
	}

	public void cerrar() {
		if (animTimer != null) animTimer.stop();
		if (frame != null) {
			SwingUtilities.invokeLater(() -> frame.dispose());
		}
	}

	/**
	 * Muestra el overlay final de victoria/derrota y ejecuta un callback al primer click.
	 *
	 * @param resultadoBatalla resultado final de la batalla
	 * @param onCerrado callback a ejecutar al cerrar el overlay
	 */
	public void mostrarResultado(ResultadoBatalla resultadoBatalla, Set<Integer> dificultadesGanadas, Runnable onCerrado) {
		this.resultado = resultadoBatalla;
		this.dificultadesGanadas = dificultadesGanadas;
		this.onResultadoCerrado = onCerrado;
		setEstadoMenu(null);
		if (canvas != null) {
			canvas.repaint();
		}
	}

	/**
	 * Register a callback that will be invoked when the user closes the window
	 * using the window manager (pressing the X). The callback should call
	 * partida.finalizar() or equivalent cleanup logic.
	 */
	public void setOnClose(Runnable onClose) {
		this.onCloseCallback = onClose;
		if (frame != null && onClose != null) {
			frame.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					if (animTimer != null) animTimer.stop();
					try { onClose.run(); } catch (Exception ex) { ex.printStackTrace(); }
				}
			});
		}
	}

	private void tick() {
		double heroTarget = heroe.getVida();
		displayedHeroHp += (heroTarget - displayedHeroHp) * 0.15;
		if (Math.abs(displayedHeroHp - heroTarget) < 1) displayedHeroHp = heroTarget;

		if (enemigos != null) {
			for (int i = 0; i < enemigos.size() && i < displayedEnemyHp.length; i++) {
				double target = enemigos.get(i).getVida();
				displayedEnemyHp[i] += (target - displayedEnemyHp[i]) * 0.15;
				if (Math.abs(displayedEnemyHp[i] - target) < 1) displayedEnemyHp[i] = target;
			}
		}

		animManager.tick();
		canvas.repaint();
	}

	private int getDisplayedHeroHp() {
		return (int) Math.round(displayedHeroHp);
	}

	private int getDisplayedEnemyHp(int idx) {
		if (idx < 0 || idx >= displayedEnemyHp.length) return 0;
		return (int) Math.round(displayedEnemyHp[idx]);
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

	private void sincronizarVida() {
		if (enemigos == null) return;
		for (int i = 0; i < enemigos.size() && i < viewEnemies.size(); i++) {
			viewEnemies.get(i).setHp(enemigos.get(i).getVida());
		}
	}

	private void loadHeroSprite() {
		String path = (rutaSprites != null ? rutaSprites : "/assets/jugador/boy") + "_up_1.bmp";
		try {
			var stream = getClass().getResourceAsStream(path);
			if (stream != null) {
				heroSprite = ImageIO.read(stream);
			} else {
				System.err.println("Could not load hero sprite at " + path);
			}
		} catch (IOException e) {
			System.err.println("Could not load hero sprite: " + e.getMessage());
		}
	}

	private Color hpColor(int current, int max) {
		if (max <= 0) return HP_RED;
		double pct = (double) current / max;
		if (pct > 0.50) return HP_GREEN;
		if (pct > 0.25) return HP_YELLOW;
		return HP_RED;
	}

	private String currentDialogText() {
		if (estadoMenu != null) {
			return "¿Qué hará " + heroe.getNombre() + "?";
		}
		if (!actionLog.isEmpty()) {
			return actionLog.get(actionLog.size() - 1);
		}
		return "";
	}

	private class BattleCanvas extends JPanel {

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

			int w = getWidth();
			int h = getHeight();

			drawBattleBackground(g2d, w, h);
			drawEnemySprites(g2d, w);
			drawEnemyStatus(g2d, w);
			drawHero(g2d, w);
			drawHeroStatus(g2d, w);

			animManager.dibujar(g2d);

			drawDialogBox(g2d, w, h);
			if (estadoMenu != null) {
				estadoMenu.dibujar(g2d, w, h);
			}
			if (resultado != null) {
				drawResultadoOverlay(g2d, w, h);
			}
		}

		private void drawBattleBackground(Graphics2D g2d, int w, int h) {
			GradientPaint sky = new GradientPaint(0, 0, BG_SKY, 0, h * 0.55f, BG_GROUND);
			g2d.setPaint(sky);
			g2d.fillRect(0, 0, w, h);

			int groundY = (int)(h * 0.52);
			g2d.setColor(BG_GROUND);
			g2d.fillRect(0, groundY, w, h - groundY);

			g2d.setColor(BG_GROUND_DARK);
			g2d.fillRect(0, groundY, w, 12);

			g2d.setColor(new Color(100, 180, 100, 80));
			for (int i = 0; i < 6; i++) {
				int y = groundY + 20 + i * 22;
				g2d.drawLine(0, y, w, y);
			}
		}

		private void drawEnemySprites(Graphics2D g2d, int w) {
			if (viewEnemies.isEmpty()) return;

			int totalCount = viewEnemies.size();
			int totalW = totalCount * ENEMY_SIZE + (totalCount - 1) * ENEMY_GAP;
			int startX = (w - totalW) / 2 + BatallaLayout.ENEMY_OFFSET_X;

			int drawn = 0;
			for (int i = 0; i < viewEnemies.size(); i++) {
				Enemy e = viewEnemies.get(i);
				boolean isFainted = e.isFainted();
				boolean isActive = (i == enemigoActivoIdx);

				int x = startX + drawn * (ENEMY_SIZE + ENEMY_GAP);

				if (isFainted) {
					Composite orig = g2d.getComposite();
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
					e.draw(g2d, x, ENEMY_Y, ENEMY_SIZE);
					g2d.setComposite(orig);

					g2d.setFont(FONT_MENU);
					FontMetrics fm = g2d.getFontMetrics();
					g2d.setColor(Color.RED);
					String ko = "KO";
					g2d.drawString(ko, x + (ENEMY_SIZE - fm.stringWidth(ko)) / 2,
							ENEMY_Y + ENEMY_SIZE / 2 + 6);
				} else {
					e.draw(g2d, x, ENEMY_Y, ENEMY_SIZE);
				}

				if (isActive && !isFainted) {
					g2d.setColor(SELECTED_BORDER);
					g2d.setStroke(new BasicStroke(3));
					g2d.drawRect(x - 3, ENEMY_Y - 3, ENEMY_SIZE + 6, ENEMY_SIZE + 6);
					g2d.setStroke(new BasicStroke(1));

					g2d.setFont(FONT_MENU);
					FontMetrics fm = g2d.getFontMetrics();
					String arrow = "▼";
					g2d.drawString(arrow, x + (ENEMY_SIZE - fm.stringWidth(arrow)) / 2,
							ENEMY_Y + ENEMY_SIZE + 16);
				}

				drawn++;
			}
		}

		private void drawEnemyStatus(Graphics2D g2d, int w) {
			if (enemigos == null || enemigos.isEmpty()) return;

			int totalCount = enemigos.size();
			int totalW = totalCount * ENEMY_SIZE + (totalCount - 1) * ENEMY_GAP;
			int startX = (w - totalW) / 2 + BatallaLayout.ENEMY_OFFSET_X;

			FontMetrics fmName = g2d.getFontMetrics(FONT_MENU_SM);
			FontMetrics fmHp = g2d.getFontMetrics(FONT_MENU_SM);

			for (int i = 0; i < totalCount; i++) {
				Enemigo e = enemigos.get(i);
				boolean isActive = (i == enemigoActivoIdx);
				boolean isFainted = !e.estaVivo();

				int sx = startX + i * (ENEMY_SIZE + ENEMY_GAP);
				int sy = STATUS_BOX_Y;
				int sw = ENEMY_SIZE;
				int sh = STATUS_BOX_H;

				Composite orig = g2d.getComposite();
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, isFainted ? 0.4f : 0.88f));
				g2d.setColor(PANEL_BG);
				g2d.fillRoundRect(sx, sy, sw, sh, 6, 6);
				g2d.setComposite(orig);

				g2d.setColor(isActive && !isFainted ? SELECTED_BORDER : PANEL_BORDER);
				g2d.setStroke(new BasicStroke(isActive && !isFainted ? 2.5f : 1.5f));
				g2d.drawRoundRect(sx, sy, sw, sh, 6, 6);
				g2d.setStroke(new BasicStroke(1));

				g2d.setFont(FONT_MENU_SM);
				g2d.setColor(isFainted ? MENU_DISABLED : PANEL_TEXT);
				String name = e.getNombre();
				int maxNameW = sw - 8;
				while (fmName.stringWidth(name) > maxNameW && name.length() > 2) {
					name = name.substring(0, name.length() - 1);
				}
				if (!name.equals(e.getNombre())) name = name + "…";
				g2d.drawString(name, sx + 4, sy + 12);

				int barX = sx + 4;
				int barY = sy + 16;
				int barW = sw - 8;
				int barH = 7;
				drawHPBar(g2d, barX, barY, barW, barH, getDisplayedEnemyHp(i), vidaInicialEnemigos[i]);

				if (isFainted) {
					g2d.setFont(FONT_MENU_SM);
					g2d.setColor(Color.RED);
					String ko = "KO";
					int koW = g2d.getFontMetrics().stringWidth(ko);
					g2d.drawString(ko, sx + (sw - koW) / 2, barY + barH + 10);
				} else {
					g2d.setFont(FONT_MENU_SM);
					g2d.setColor(PANEL_TEXT);
					String hpStr = getDisplayedEnemyHp(i) + "/" + vidaInicialEnemigos[i];
					int numX = sx + sw - fmHp.stringWidth(hpStr) - 4;
					g2d.drawString(hpStr, numX, barY + barH + 10);
				}
			}
		}

		private void drawHero(Graphics2D g2d, int w) {
			if (heroSprite != null) {
				g2d.drawImage(heroSprite, HERO_X, HERO_Y, HERO_SIZE, HERO_SIZE, null);
			} else {
				g2d.setColor(new Color(80, 128, 224));
				g2d.fillRect(HERO_X, HERO_Y, HERO_SIZE, HERO_SIZE);
				g2d.setColor(Color.BLACK);
				g2d.drawRect(HERO_X, HERO_Y, HERO_SIZE, HERO_SIZE);
			}
		}

		private void drawHeroStatus(Graphics2D g2d, int w) {
			int sw = HERO_STATUS_W;
			int sx = HERO_X + (HERO_SIZE - sw) / 2;
			int sy = HUD_Y;
			int sh = HUD_H;

			Composite orig = g2d.getComposite();
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.88f));
			g2d.setColor(PANEL_BG);
			g2d.fillRoundRect(sx, sy, sw, sh, 8, 8);
			g2d.setComposite(orig);

			g2d.setColor(PANEL_BORDER);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawRoundRect(sx, sy, sw, sh, 8, 8);
			g2d.setStroke(new BasicStroke(1));

			g2d.setFont(FONT_HUD);
			g2d.setColor(PANEL_TEXT);
			String name = heroe.getNombre();
			if (name.length() > 12) name = name.substring(0, 11) + "…";
			g2d.drawString(name, sx + 14, sy + 22);

			g2d.setFont(FONT_MENU_SM);
			g2d.setColor(new Color(180, 180, 210));
			g2d.drawString("Lv.1", sx + sw - 45, sy + 22);

			int barX = sx + 14;
			int barY = sy + 34;
			int barW = sw - 28;
			int barH = 12;
			drawHPBar(g2d, barX, barY, barW, barH, getDisplayedHeroHp(), vidaInicialHeroe);

			g2d.setFont(FONT_HP_NUM);
			g2d.setColor(PANEL_TEXT);
			String hpStr = getDisplayedHeroHp() + "/" + vidaInicialHeroe;
			int numW = g2d.getFontMetrics().stringWidth(hpStr);
			g2d.drawString(hpStr, sx + sw - numW - 14, barY + 26);
		}

		private void drawHPBar(Graphics2D g2d, int x, int y, int w, int h,
				int current, int max) {
			g2d.setColor(HP_BG);
			g2d.fillRoundRect(x, y, w, h, 3, 3);

			if (max > 0) {
				double ratio = Math.max(0, Math.min(1, (double) current / max));
				int fillW = (int) (w * ratio);
				if (fillW > 0) {
					g2d.setColor(hpColor(current, max));
					g2d.fillRoundRect(x, y, fillW, h, 3, 3);
				}
			}

			g2d.setColor(HP_BORDER);
			g2d.setStroke(new BasicStroke(1.5f));
			g2d.drawRoundRect(x, y, w, h, 3, 3);
			g2d.setStroke(new BasicStroke(1));

			g2d.setColor(new Color(255, 255, 255, 40));
			int segments = 8;
			for (int i = 1; i < segments; i++) {
				int sx = x + (w * i / segments);
				g2d.drawLine(sx, y + 2, sx, y + h - 2);
			}
		}

		private void drawDialogBox(Graphics2D g2d, int w, int h) {
			int dx = 12;
			int dy = DIALOG_Y;
			int dw = w - 24;
			int dh = h - dy - 6;

			Composite orig = g2d.getComposite();
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.92f));
			g2d.setColor(DIALOG_BG);
			g2d.fillRoundRect(dx, dy, dw, dh, 6, 6);
			g2d.setComposite(orig);

			g2d.setColor(DIALOG_BORDER);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawRoundRect(dx, dy, dw, dh, 6, 6);
			g2d.setStroke(new BasicStroke(1));

			String text = indicadorAccion != null ? indicadorAccion : currentDialogText();
			if (!text.isEmpty()) {
				if (indicadorAccion != null) {
					g2d.setFont(FONT_MENU);
					g2d.setColor(SELECTED_BORDER);
				} else {
					g2d.setFont(FONT_DIALOG);
					g2d.setColor(DIALOG_TEXT);
				}

				FontMetrics fm = g2d.getFontMetrics();
				int maxWidth = dw - 20;
				List<String> lines = wrapText(text, fm, maxWidth);
				int lineY = dy + 24;
				if (lines.size() == 1) lineY = dy + (dh + fm.getAscent()) / 2 - 4;

				for (String line : lines) {
					g2d.drawString(line, dx + 14, lineY);
					lineY += fm.getHeight();
				}
			}

			if (estadoMenu != null && indicadorAccion == null) {
				g2d.setFont(FONT_MENU_SM);
				g2d.setColor(new Color(255, 255, 255, 120));
				g2d.drawString("▶", dx + dw - 22, dy + dh - 10);
			}
		}

		private void drawResultadoOverlay(Graphics2D g2d, int w, int h) {
			ResultadoBatalla r = resultado;
			if (r == null) return;

			Composite orig = g2d.getComposite();
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));
			g2d.setColor(Color.BLACK);
			g2d.fillRect(0, 0, w, h);
			g2d.setComposite(orig);

			int panelW = 390;
			int panelH = 260;
			int px = (w - panelW) / 2;
			int py = (h - panelH) / 2;

			g2d.setColor(PANEL_BG);
			g2d.fillRoundRect(px, py, panelW, panelH, 12, 12);
			g2d.setColor(PANEL_BORDER);
			g2d.setStroke(new BasicStroke(3));
			g2d.drawRoundRect(px, py, panelW, panelH, 12, 12);
			g2d.setStroke(new BasicStroke(1));

			String titulo = r.esVictoria() ? "¡VICTORIA!" : "DERROTA";
			Color colorTitulo = r.esVictoria() ? new Color(255, 215, 0) : new Color(220, 20, 60);
			g2d.setFont(FONT_TITLE);
			g2d.setColor(colorTitulo);
			FontMetrics fmTitle = g2d.getFontMetrics();
			int tx = px + (panelW - fmTitle.stringWidth(titulo)) / 2;
			g2d.drawString(titulo, tx, py + 52);

			g2d.setFont(FONT_DIALOG);
			g2d.setColor(PANEL_TEXT);
			String estadistica1 = "Enemigos derrotados: " + r.getEnemigosEliminados() + "/" + r.getEnemigosTotales();
			String estadistica2 = "Puntaje obtenido: " + r.getPuntaje();
			FontMetrics fmStats = g2d.getFontMetrics();
			g2d.drawString(estadistica1, px + (panelW - fmStats.stringWidth(estadistica1)) / 2, py + 102);
			g2d.drawString(estadistica2, px + (panelW - fmStats.stringWidth(estadistica2)) / 2, py + 132);

			g2d.setFont(FONT_DIALOG);
			g2d.setColor(PANEL_TEXT);
			StringBuilder sb = new StringBuilder("Progreso: ");
			for (int d = 1; d <= 3; d++) {
				sb.append(dificultadesGanadas != null && dificultadesGanadas.contains(d) ? "★" : "☆");
				if (d < 3) sb.append(" ");
			}
			sb.append("   (").append(dificultadesGanadas != null ? dificultadesGanadas.size() : 0).append("/3)");
			String progreso = sb.toString();
			FontMetrics fmProg = g2d.getFontMetrics();
			g2d.drawString(progreso, px + (panelW - fmProg.stringWidth(progreso)) / 2, py + 162);

			boolean mostrarHint = ((System.currentTimeMillis() / 400) % 2) == 0;
			if (mostrarHint) {
				String hint = "Click para volver al mapa";
				g2d.setFont(FONT_MENU_SM);
				FontMetrics fmHint = g2d.getFontMetrics();
				g2d.drawString(hint, px + (panelW - fmHint.stringWidth(hint)) / 2, py + panelH - 24);
			}
		}

		private List<String> wrapText(String text, FontMetrics fm, int maxWidth) {
			List<String> lines = new ArrayList<>();
			if (text == null || text.isEmpty()) return lines;

			String[] words = text.split(" ");
			StringBuilder line = new StringBuilder();

			for (String word : words) {
				if (fm.stringWidth(line + word) < maxWidth) {
					if (line.length() > 0) line.append(" ");
					line.append(word);
				} else {
					if (line.length() > 0) {
						lines.add(line.toString());
						line = new StringBuilder(word);
					} else {
						lines.add(word);
					}
				}
			}
			if (line.length() > 0) lines.add(line.toString());

			return lines.isEmpty() ? List.of(text) : lines;
		}
	}
}
