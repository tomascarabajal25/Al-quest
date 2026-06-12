package juego.ciudades.batalla.view;

import estructuras.pilas.Pila;
import juego.ciudades.batalla.model.Accion;
import juego.ciudades.batalla.model.Combatiente;
import juego.ciudades.batalla.model.Enemigo;
import juego.ciudades.batalla.model.acciones.Atacar;
import juego.ciudades.batalla.model.acciones.Defender;
import juego.ciudades.batalla.view.models.Enemy;
import juego.ciudades.batalla.view.models.EnemyFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.imageio.ImageIO;
import javax.swing.Timer;

public class BatallaUI {

	// ── Colours ──────────────────────────────────────────────────────────────
	private static final Color BG_SKY           = new Color(120, 180, 240);
	private static final Color BG_GROUND        = new Color( 80, 160,  80);
	private static final Color BG_GROUND_DARK   = new Color( 50, 110,  40);
	private static final Color HP_GREEN         = new Color( 48, 200,  80);
	private static final Color HP_YELLOW        = new Color(240, 200,  40);
	private static final Color HP_RED           = new Color(220,  40,  40);
	private static final Color HP_BG            = new Color( 30,  30,  40);
	private static final Color HP_BORDER        = new Color(180, 180, 200);
	private static final Color PANEL_BG         = new Color( 30,  30,  50, 220);
	private static final Color PANEL_BORDER     = new Color(140, 140, 180);
	private static final Color PANEL_TEXT       = new Color(240, 240, 255);
	private static final Color DIALOG_BG        = new Color( 28,  28,  48, 235);
	private static final Color DIALOG_BORDER    = new Color(180, 180, 210);
	private static final Color DIALOG_TEXT      = new Color(240, 240, 255);
	private static final Color MENU_BG          = new Color( 30,  30,  50, 235);
	private static final Color MENU_BORDER      = new Color(130, 130, 180);
	private static final Color MENU_TEXT        = new Color(240, 240, 255);
	private static final Color MENU_HIGHLIGHT   = new Color( 70,  70, 120, 220);
	private static final Color MENU_DISABLED    = new Color(100, 100, 120);
	private static final Color ACTIVE_HIGHLIGHT = new Color(220, 240, 220, 80);
	private static final Color SELECTED_BORDER  = new Color(255, 220,  40);

	// ── Fonts ────────────────────────────────────────────────────────────────
	private static final Font FONT_HUD      = new Font("Monospaced", Font.BOLD, 13);
	private static final Font FONT_HP_NUM   = new Font("Monospaced", Font.BOLD, 14);
	private static final Font FONT_DIALOG   = new Font("Monospaced", Font.PLAIN, 15);
	private static final Font FONT_MENU     = new Font("Monospaced", Font.BOLD, 16);
	private static final Font FONT_MENU_SM  = new Font("Monospaced", Font.BOLD, 12);

// ── Layout constants ─────────────────────────────────────────────────────
	private static final int HERO_X         = 290;
	private static final int HERO_Y         = 155;
	private static final int HERO_SIZE      = 128;
	private static final int HERO_STATUS_W  = 280;
	private static final int ENEMY_Y        = 55;
	private static final int ENEMY_SIZE     = 96;
	private static final int ENEMY_GAP      = 14;
	private static final int STATUS_BOX_Y   = 17;
	private static final int STATUS_BOX_W   = 96;
	private static final int STATUS_BOX_H   = 34;
	private static final int HUD_Y          = 282;
	private static final int HUD_H          = 68;
	private static final int DIALOG_Y       = 354;

	// ── Model ────────────────────────────────────────────────────────────────
	private final Combatiente heroe;
	private final List<Enemigo> enemigos;
	private final List<Enemy> viewEnemies;
	private final BlockingQueue<String> colaComandos = new LinkedBlockingQueue<>();
	private final List<String> actionLog = new ArrayList<>();
	private final int[] vidaInicialEnemigos;
	private final int vidaInicialHeroe;
	private volatile int enemigoActivoIdx;

	// ── View state ───────────────────────────────────────────────────────────
	private JFrame frame;
	private BattleCanvas canvas;
	private BufferedImage heroSprite;
	private boolean showMainMenu;
	private boolean showSubMenu;
	private String currentDialog = "";

	// ── Animation state ────────────────────────────────────────────────────
	private double displayedHeroHp;
	private final double[] displayedEnemyHp;
	private double flashAlpha;
	private long flashStartTime;
	private static final long FLASH_DURATION_MS = 300;
	private int shakeOffsetX;
	private int shakeOffsetY;
	private long shakeStartTime;
	private boolean shakeTargetIsEnemy;
	private static final long SHAKE_DURATION_MS = 200;
	private Timer animTimer;

	// Menu rectangles (recalculated on paint)
	private Rectangle atacarRect;
	private Rectangle defenderRect;
	private Rectangle pasarRect;
	private transient List<Integer> subMenuEnemyIndices = new ArrayList<>();
	private transient List<Rectangle> subMenuRects = new ArrayList<>();
	private Rectangle volverRect;

	// ── Constructor ──────────────────────────────────────────────────────────
	public BatallaUI(Combatiente heroe, List<Enemigo> enemigos) {
		this.heroe = heroe;
		this.enemigos = enemigos;
		this.vidaInicialHeroe = heroe.getVida();
		this.vidaInicialEnemigos = new int[enemigos != null ? enemigos.size() : 0];
		this.viewEnemies = new ArrayList<>();
		this.enemigoActivoIdx = (enemigos != null && !enemigos.isEmpty()) ? 0 : 0;

		if (enemigos != null) {
			for (int i = 0; i < enemigos.size(); i++) {
				vidaInicialEnemigos[i] = enemigos.get(i).getVida();
				viewEnemies.add(EnemyFactory.fromEnemigo(enemigos.get(i)));
			}
		}

		displayedHeroHp = heroe.getVida();
		displayedEnemyHp = new double[enemigos != null ? enemigos.size() : 0];
		for (int i = 0; i < displayedEnemyHp.length; i++) {
			displayedEnemyHp[i] = enemigos.get(i).getVida();
		}

		loadHeroSprite();

		showMainMenu = true;
		showSubMenu = false;
		flashAlpha = 0;
		shakeStartTime = 0;

		SwingUtilities.invokeLater(this::createAndShowGUI);
	}

	// ── Window creation ──────────────────────────────────────────────────────
	private void createAndShowGUI() {
		frame = new JFrame("Batalla");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		canvas = new BattleCanvas();
		canvas.setPreferredSize(new Dimension(736, 414));
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

	// ── Mouse click handling ─────────────────────────────────────────────────
	private void handleClick(int mx, int my) {
		if (showSubMenu) {
			handleSubMenuClick(mx, my);
			return;
		}
		if (!showMainMenu) return;

		if (atacarRect != null && atacarRect.contains(mx, my)) {
			showMainMenu = false;
			showSubMenu = true;
			canvas.repaint();
		} else if (defenderRect != null && defenderRect.contains(mx, my)) {
			showMainMenu = false;
			colaComandos.offer("DEFENDER");
			canvas.repaint();
		} else if (pasarRect != null && pasarRect.contains(mx, my)) {
			showMainMenu = false;
			colaComandos.offer("PASAR");
			canvas.repaint();
		}
	}

	private void handleSubMenuClick(int mx, int my) {
		for (int i = 0; i < subMenuRects.size() && i < subMenuEnemyIndices.size(); i++) {
			Rectangle r = subMenuRects.get(i);
			if (r != null && r.contains(mx, my)) {
				int realIdx = subMenuEnemyIndices.get(i);
				colaComandos.offer("ATACAR:" + realIdx);
				enemigoActivoIdx = realIdx;
				showSubMenu = false;
				showMainMenu = true;
				canvas.repaint();
				return;
			}
		}
		if (volverRect != null && volverRect.contains(mx, my)) {
			showSubMenu = false;
			showMainMenu = true;
			canvas.repaint();
		}
	}

	// ── Public API (called from Batalla game loop) ───────────────────────────
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
			// Trigger visual effects for attacks
			if (mensaje.contains("atacó") || mensaje.contains("usó")) {
				final boolean isHeroAttacking = heroe != null && mensaje.startsWith(heroe.getNombre());
				SwingUtilities.invokeLater(() -> {
					triggerFlash();
					triggerShake(!isHeroAttacking);
				});
			}
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
		if (animTimer != null) animTimer.stop();
		if (frame != null) {
			SwingUtilities.invokeLater(() -> frame.dispose());
		}
	}

	// ── Menu state helpers ───────────────────────────────────────────────────
	private void instalarMenuPrincipal() {
		showMainMenu = true;
		showSubMenu = false;
		canvas.repaint();
	}

	// ── Animation tick ──────────────────────────────────────────────────────
	private void tick() {
		long now = System.currentTimeMillis();

		// Interpolate displayed HP towards actual HP
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

		// Decay flash
		if (flashAlpha > 0) {
			long elapsed = now - flashStartTime;
			flashAlpha = Math.max(0, 1.0 - (double) elapsed / FLASH_DURATION_MS);
		}

		// Decay shake
		if (shakeStartTime > 0) {
			long shakeElapsed = now - shakeStartTime;
			if (shakeElapsed > SHAKE_DURATION_MS) {
				shakeStartTime = 0;
				shakeOffsetX = 0;
				shakeOffsetY = 0;
			} else {
				double intensity = 1.0 - (double) shakeElapsed / SHAKE_DURATION_MS;
				shakeOffsetX = (int) (Math.sin(shakeElapsed * 0.05) * 4 * intensity);
				shakeOffsetY = (int) (Math.cos(shakeElapsed * 0.07) * 3 * intensity);
			}
		}

		canvas.repaint();
	}

	// ── Visual effects triggers ───────────────────────────────────────────────
	public void triggerFlash() {
		flashStartTime = System.currentTimeMillis();
		flashAlpha = 1.0;
	}

	public void triggerShake(boolean targetIsEnemy) {
		shakeStartTime = System.currentTimeMillis();
		shakeTargetIsEnemy = targetIsEnemy;
		shakeOffsetX = 0;
		shakeOffsetY = 0;
	}

	private int getDisplayedHeroHp() {
		return (int) Math.round(displayedHeroHp);
	}

	private int getDisplayedEnemyHp(int idx) {
		if (idx < 0 || idx >= displayedEnemyHp.length) return 0;
		return (int) Math.round(displayedEnemyHp[idx]);
	}

	// ── Internal helpers ─────────────────────────────────────────────────────
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
		try {
			var stream = getClass().getResourceAsStream("/assets/jugador/boy_up_1.bmp");
			if (stream != null) {
				heroSprite = ImageIO.read(stream);
			} else {
				System.err.println("Could not load hero sprite at /assets/jugador/boy_up_1.bmp");
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
		if (showMainMenu || showSubMenu) {
			return "¿Qué hará " + heroe.getNombre() + "?";
		}
		if (!actionLog.isEmpty()) {
			return actionLog.get(actionLog.size() - 1);
		}
		return "";
	}

	// ══════════════════════════════════════════════════════════════════════════
	//  BattleCanvas — every draw call happens here
	// ══════════════════════════════════════════════════════════════════════════
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

			// Flash overlay for attacks
			if (flashAlpha > 0) {
				Composite orig = g2d.getComposite();
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) flashAlpha));
				g2d.setColor(Color.WHITE);
				g2d.fillRect(0, 0, w, h);
				g2d.setComposite(orig);
			}

			drawDialogBox(g2d, w, h);
			drawActionMenu(g2d, w);
		}

		// ── Battle background ──────────────────────────────────────────────
		private void drawBattleBackground(Graphics2D g2d, int w, int h) {
			// Sky gradient
			GradientPaint sky = new GradientPaint(0, 0, BG_SKY, 0, h * 0.55f, BG_GROUND);
			g2d.setPaint(sky);
			g2d.fillRect(0, 0, w, h);

			// Ground plane (dark band at bottom)
			int groundY = (int)(h * 0.52);
			g2d.setColor(BG_GROUND);
			g2d.fillRect(0, groundY, w, h - groundY);

			// Ground plane shadow
			g2d.setColor(BG_GROUND_DARK);
			g2d.fillRect(0, groundY, w, 12);

			// Simple terrain detail lines
			g2d.setColor(new Color(100, 180, 100, 80));
			for (int i = 0; i < 6; i++) {
				int y = groundY + 20 + i * 22;
				g2d.drawLine(0, y, w, y);
			}
		}

		// ── Enemy sprites ──────────────────────────────────────────────────
		private void drawEnemySprites(Graphics2D g2d, int w) {
			if (viewEnemies.isEmpty()) return;

			int totalCount = viewEnemies.size();
			int totalW = totalCount * ENEMY_SIZE + (totalCount - 1) * ENEMY_GAP;
			int startX = (w - totalW) / 2;

			int drawn = 0;
			for (int i = 0; i < viewEnemies.size(); i++) {
				Enemy e = viewEnemies.get(i);
				boolean isFainted = e.isFainted();
				boolean isActive = (i == enemigoActivoIdx);

				int x = startX + drawn * (ENEMY_SIZE + ENEMY_GAP);

				// Apply shake offset if this enemy is the target
				if (shakeStartTime > 0 && shakeTargetIsEnemy && i == enemigoActivoIdx) {
					x += shakeOffsetX;
				}

			if (isFainted) {
				// Dimmed overlay
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

				// Selection highlight for active enemy
				if (isActive && !isFainted) {
					g2d.setColor(SELECTED_BORDER);
					g2d.setStroke(new BasicStroke(3));
					g2d.drawRect(x - 3, ENEMY_Y - 3, ENEMY_SIZE + 6, ENEMY_SIZE + 6);
					g2d.setStroke(new BasicStroke(1));

					// Yellow arrow under active enemy
					g2d.setFont(FONT_MENU);
					FontMetrics fm = g2d.getFontMetrics();
					String arrow = "▼";
					g2d.drawString(arrow, x + (ENEMY_SIZE - fm.stringWidth(arrow)) / 2,
							ENEMY_Y + ENEMY_SIZE + 16);
				}

				drawn++;
			}
		}

		// ── Enemy status box (top-right) ────────────────────────────────────
		private void drawEnemyStatus(Graphics2D g2d, int w) {
			if (enemigos == null || enemigos.isEmpty()) return;

			int totalCount = enemigos.size();
			int totalW = totalCount * ENEMY_SIZE + (totalCount - 1) * ENEMY_GAP;
			int startX = (w - totalW) / 2;

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

				if (isFainted) {
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
				} else {
					g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.88f));
				}

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

		// ── Hero sprite ─────────────────────────────────────────────────────
		private void drawHero(Graphics2D g2d, int w) {
			int hx = HERO_X;
			int hy = HERO_Y;

			// Apply shake offset if hero is the target
			if (shakeStartTime > 0 && !shakeTargetIsEnemy) {
				hx += shakeOffsetX;
				hy += shakeOffsetY;
			}

			if (heroSprite != null) {
				g2d.drawImage(heroSprite, hx, hy, HERO_SIZE, HERO_SIZE, null);
			} else {
				g2d.setColor(new Color(80, 128, 224));
				g2d.fillRect(hx, hy, HERO_SIZE, HERO_SIZE);
				g2d.setColor(Color.BLACK);
				g2d.drawRect(hx, hy, HERO_SIZE, HERO_SIZE);
			}
		}

		// ── Hero status panel (centered under hero sprite) ───────────────────────────
		private void drawHeroStatus(Graphics2D g2d, int w) {
			int sw = HERO_STATUS_W;
			int sx = HERO_X + (HERO_SIZE - sw) / 2;
			int sy = HUD_Y;
			int sh = HUD_H;

			// Panel background
			Composite orig = g2d.getComposite();
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.88f));
			g2d.setColor(PANEL_BG);
			g2d.fillRoundRect(sx, sy, sw, sh, 8, 8);
			g2d.setComposite(orig);

			// Panel border
			g2d.setColor(PANEL_BORDER);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawRoundRect(sx, sy, sw, sh, 8, 8);
			g2d.setStroke(new BasicStroke(1));

			// Hero name
			g2d.setFont(FONT_HUD);
			g2d.setColor(PANEL_TEXT);
			String name = heroe.getNombre();
			if (name.length() > 12) name = name.substring(0, 11) + "…";
			g2d.drawString(name, sx + 14, sy + 22);

			// Level label
			g2d.setFont(FONT_MENU_SM);
			g2d.setColor(new Color(180, 180, 210));
			String lv = "Lv.1";
			g2d.drawString(lv, sx + sw - 45, sy + 22);

			// HP bar (bigger than enemy bar)
			int barX = sx + 14;
			int barY = sy + 34;
			int barW = sw - 28;
			int barH = 12;
			drawHPBar(g2d, barX, barY, barW, barH, getDisplayedHeroHp(), vidaInicialHeroe);

			// HP numbers
			g2d.setFont(FONT_HP_NUM);
			g2d.setColor(PANEL_TEXT);
			String hpStr = getDisplayedHeroHp() + "/" + vidaInicialHeroe;
			int numW = g2d.getFontMetrics().stringWidth(hpStr);
			g2d.drawString(hpStr, sx + sw - numW - 14, barY + 26);
		}

		// ── Shared HP bar drawer ────────────────────────────────────────────
		private void drawHPBar(Graphics2D g2d, int x, int y, int w, int h,
				int current, int max) {
			// Background
			g2d.setColor(HP_BG);
			g2d.fillRoundRect(x, y, w, h, 3, 3);

			// Filled portion
			if (max > 0) {
				double ratio = Math.max(0, Math.min(1, (double) current / max));
				int fillW = (int) (w * ratio);
				if (fillW > 0) {
					g2d.setColor(hpColor(current, max));
					g2d.fillRoundRect(x, y, fillW, h, 3, 3);
				}
			}

			// Border
			g2d.setColor(HP_BORDER);
			g2d.setStroke(new BasicStroke(1.5f));
			g2d.drawRoundRect(x, y, w, h, 3, 3);
			g2d.setStroke(new BasicStroke(1));

			// Segments (subtle vertical lines)
			g2d.setColor(new Color(255, 255, 255, 40));
			int segments = 8;
			for (int i = 1; i < segments; i++) {
				int sx = x + (w * i / segments);
				g2d.drawLine(sx, y + 2, sx, y + h - 2);
			}
		}

		// ── Dialog box (bottom) ─────────────────────────────────────────────
		private void drawDialogBox(Graphics2D g2d, int w, int h) {
			int dx = 12;
			int dy = DIALOG_Y;
			int dw = w - 24;
			int dh = h - dy - 6;

			// Background
			Composite orig = g2d.getComposite();
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.92f));
			g2d.setColor(DIALOG_BG);
			g2d.fillRoundRect(dx, dy, dw, dh, 6, 6);
			g2d.setComposite(orig);

			// Border
			g2d.setColor(DIALOG_BORDER);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawRoundRect(dx, dy, dw, dh, 6, 6);
			g2d.setStroke(new BasicStroke(1));

			// Text
			String text = currentDialogText();
			if (!text.isEmpty()) {
				g2d.setFont(FONT_DIALOG);
				g2d.setColor(DIALOG_TEXT);

				// Word-wrap if needed
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

			// Blinking cursor prompt at bottom-right
			if (showMainMenu || showSubMenu) {
				g2d.setFont(FONT_MENU_SM);
				g2d.setColor(new Color(255, 255, 255, 120));
				g2d.drawString("▶", dx + dw - 22, dy + dh - 10);
			}
		}

		// ── Action menu (right of hero status) ─────────────────────────────
		private void drawActionMenu(Graphics2D g2d, int w) {
			if (!showMainMenu && !showSubMenu) return;

			int heroStatusX = HERO_X + (HERO_SIZE - HERO_STATUS_W) / 2;
			int mx = heroStatusX + HERO_STATUS_W + 10;
			int mw = w - mx - 16;
			int my = HUD_Y;
			int mh = HUD_H;

			// Panel background
			Composite orig = g2d.getComposite();
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.88f));
			g2d.setColor(MENU_BG);
			g2d.fillRoundRect(mx, my, mw, mh, 8, 8);
			g2d.setComposite(orig);

			// Panel border
			g2d.setColor(MENU_BORDER);
			g2d.setStroke(new BasicStroke(2));
			g2d.drawRoundRect(mx, my, mw, mh, 8, 8);
			g2d.setStroke(new BasicStroke(1));

			if (showSubMenu) {
				drawSubMenuEnemies(g2d, mx, my, mw, mh);
			} else {
				drawMainMenuButtons(g2d, mx, my, mw, mh);
			}
		}

		// ── Main menu (ATACAR / DEFENDER / PASAR) ───────────────────────────
		private void drawMainMenuButtons(Graphics2D g2d, int mx, int my, int mw, int mh) {
			int btnW = (mw - 30) / 2;
			int btnH = 30;
			int gap = 6;
			int bx1 = mx + 10;
			int bx2 = mx + 10 + btnW + gap;
			int by1 = my + 8;
			int by2 = my + 8 + btnH + gap;

			// ATACAR
			atacarRect = new Rectangle(bx1, by1, btnW, btnH);
			drawMenuButton(g2d, bx1, by1, btnW, btnH, "LUCHAR");

			// DEFENDER
			defenderRect = new Rectangle(bx2, by1, btnW, btnH);
			drawMenuButton(g2d, bx2, by1, btnW, btnH, "DEFENDER");

			// PASAR (full width)
			pasarRect = new Rectangle(bx1, by2, mw - 20, btnH);
			drawMenuButton(g2d, bx1, by2, mw - 20, btnH, "PASAR");
		}

		// ── Sub menu (enemy selection) ──────────────────────────────────────
		private void drawSubMenuEnemies(Graphics2D g2d, int mx, int my, int mw, int mh) {
			subMenuEnemyIndices.clear();
			subMenuRects.clear();

			g2d.setFont(FONT_MENU_SM);
			FontMetrics fm = g2d.getFontMetrics();
			int lineH = fm.getHeight() + 4;
			int startY = my + 8;

			int visibleCount = 0;
			for (int i = 0; i < enemigos.size(); i++) {
				Enemigo e = enemigos.get(i);
				if (!e.estaVivo()) continue;

				int lineY = startY + visibleCount * lineH;
				String label = "→ " + e.getNombre();
				Rectangle r = new Rectangle(mx + 6, lineY - 2, mw - 12, lineH);
				subMenuEnemyIndices.add(i);
				subMenuRects.add(r);

				boolean isActive = (i == enemigoActivoIdx);
				// Highlight if active
				if (isActive) {
					g2d.setColor(ACTIVE_HIGHLIGHT);
					g2d.fillRect(r.x, r.y, r.width, r.height);
				}

				g2d.setColor(MENU_TEXT);
				g2d.drawString(label, mx + 14, lineY + fm.getAscent());
				visibleCount++;
			}

			// VOLVER button at the bottom
			int volverY = startY + visibleCount * lineH + 4;
			volverRect = new Rectangle(mx + 6, volverY - 2, mw - 12, lineH);
			g2d.setColor(MENU_DISABLED);
			g2d.drawString("← VOLVER", mx + 14, volverY + fm.getAscent());
		}

		// ── Single menu button ──────────────────────────────────────────────
		private void drawMenuButton(Graphics2D g2d, int x, int y, int w, int h, String text) {
			// Button background
			g2d.setColor(new Color(50, 50, 70, 220));
			g2d.fillRect(x, y, w, h);

			// Button border
			g2d.setColor(MENU_BORDER);
			g2d.setStroke(new BasicStroke(1.5f));
			g2d.drawRect(x, y, w, h);
			g2d.setStroke(new BasicStroke(1));

			// Button text
			g2d.setFont(FONT_MENU);
			g2d.setColor(MENU_TEXT);
			FontMetrics fm = g2d.getFontMetrics();
			int tx = x + (w - fm.stringWidth(text)) / 2;
			int ty = y + (h + fm.getAscent()) / 2 - 3;
			g2d.drawString(text, tx, ty);

			// Arrow indicator
			g2d.setFont(FONT_MENU_SM);
			g2d.setColor(MENU_TEXT);
			String arrow = "▸";
			g2d.drawString(arrow, x + 6, ty);
		}

		// ── Simple word-wrap ────────────────────────────────────────────────
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
						// Word too long, just add it
						lines.add(word);
					}
				}
			}
			if (line.length() > 0) lines.add(line.toString());

			return lines.isEmpty() ? List.of(text) : lines;
		}
	}
}
