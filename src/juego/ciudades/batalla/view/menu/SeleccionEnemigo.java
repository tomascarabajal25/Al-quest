package juego.ciudades.batalla.view.menu;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import juego.ciudades.batalla.model.Accion;
import juego.ciudades.batalla.model.Combatiente;
import juego.ciudades.batalla.model.Enemigo;
import juego.ciudades.batalla.model.acciones.Atacar;
import juego.ciudades.batalla.view.BatallaLayout;

public class SeleccionEnemigo implements EstadoMenu {
    private static final Color MENU_BG          = new Color( 30,  30,  50, 235);
    private static final Color MENU_BORDER      = new Color(130, 130, 180);
    private static final Color MENU_TEXT        = new Color(240, 240, 255);
    private static final Color MENU_DISABLED    = new Color(100, 100, 120);
    private static final Color ACTIVE_HIGHLIGHT = new Color(220, 240, 220, 80);
    private static final Font  FONT_MENU_SM     = new Font("Monospaced", Font.BOLD, 12);

    private final Combatiente heroe;
    private final List<Enemigo> enemigos;
    private final Runnable onVolver;
    private final List<Integer> enemyIndices = new ArrayList<>();
    private final List<Rectangle> enemyRects = new ArrayList<>();
    private Rectangle volverRect;

    private int enemigoActivoIdx = 0;

    public SeleccionEnemigo(Combatiente heroe, List<Enemigo> enemigos, Runnable onVolver) {
        this.heroe = heroe;
        this.enemigos = enemigos;
        this.onVolver = onVolver;
    }

    public void setEnemigoActivoIdx(int idx) { this.enemigoActivoIdx = idx; }

    @Override
    public void onEnter() {}

    @Override
    public void onExit() {}

    @Override
    public Accion onClick(int mx, int my) {
        for (int i = 0; i < enemyRects.size(); i++) {
            if (enemyRects.get(i).contains(mx, my)) {
                int realIdx = enemyIndices.get(i);
                return new Atacar(heroe, enemigos.get(realIdx));
            }
        }
        if (volverRect != null && volverRect.contains(mx, my)) {
            onVolver.run();
        }
        return null;
    }

    @Override
    public void dibujar(Graphics2D g, int w, int h) {
        enemyIndices.clear();
        enemyRects.clear();

        int heroStatusX = BatallaLayout.HERO_X + (BatallaLayout.HERO_SIZE - BatallaLayout.HERO_STATUS_W) / 2;
        int mx = heroStatusX + BatallaLayout.HERO_STATUS_W + 10;
        int mw = w - mx - 16;
        int my = BatallaLayout.HUD_Y;
        int mh = BatallaLayout.HUD_H;

        Composite orig = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.88f));
        g.setColor(MENU_BG);
        g.fillRoundRect(mx, my, mw, mh, 8, 8);
        g.setComposite(orig);

        g.setColor(MENU_BORDER);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(mx, my, mw, mh, 8, 8);
        g.setStroke(new BasicStroke(1));

        g.setFont(FONT_MENU_SM);
        FontMetrics fm = g.getFontMetrics();
        int lineH = fm.getHeight() + 4;
        int startY = my + 8;

        int visibleCount = 0;
        for (int i = 0; i < enemigos.size(); i++) {
            Enemigo e = enemigos.get(i);
            if (!e.estaVivo()) continue;

            int lineY = startY + visibleCount * lineH;
            String label = "→ " + e.getNombre();
            Rectangle r = new Rectangle(mx + 6, lineY - 2, mw - 12, lineH);
            enemyIndices.add(i);
            enemyRects.add(r);

            if (i == enemigoActivoIdx) {
                g.setColor(ACTIVE_HIGHLIGHT);
                g.fillRect(r.x, r.y, r.width, r.height);
            }

            g.setColor(MENU_TEXT);
            g.drawString(label, mx + 14, lineY + fm.getAscent());
            visibleCount++;
        }

        int volverY = startY + visibleCount * lineH + 4;
        volverRect = new Rectangle(mx + 6, volverY - 2, mw - 12, lineH);
        g.setColor(MENU_DISABLED);
        g.drawString("← VOLVER", mx + 14, volverY + fm.getAscent());
    }
}
