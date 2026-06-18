package juego.ciudades.batalla.view.menu;

import java.awt.*;
import java.util.List;
import juego.ciudades.batalla.model.Accion;
import juego.ciudades.batalla.model.Combatiente;
import juego.ciudades.batalla.model.Enemigo;
import juego.ciudades.batalla.model.acciones.Defender;

public class MenuPrincipal implements EstadoMenu {
    private static final Color MENU_BG        = new Color( 30,  30,  50, 235);
    private static final Color MENU_BORDER    = new Color(130, 130, 180);
    private static final Color MENU_TEXT      = new Color(240, 240, 255);
    private static final Font  FONT_MENU      = new Font("Monospaced", Font.BOLD, 16);
    private static final Font  FONT_MENU_SM   = new Font("Monospaced", Font.BOLD, 12);

    private static final int HUD_Y = 282;
    private static final int HUD_H = 68;
    private static final int HERO_X = 290;
    private static final int HERO_SIZE = 128;
    private static final int HERO_STATUS_W = 280;

    private final Combatiente heroe;
    private final List<Enemigo> enemigos;
    private final Runnable onLuchar;

    private Rectangle atacarRect;
    private Rectangle defenderRect;
    private Rectangle pasarRect;

    public MenuPrincipal(Combatiente heroe, List<Enemigo> enemigos, Runnable onLuchar) {
        this.heroe = heroe;
        this.enemigos = enemigos;
        this.onLuchar = onLuchar;
    }

    @Override
    public void onEnter() {}

    @Override
    public void onExit() {}

    @Override
    public Accion onClick(int mx, int my) {
        if (atacarRect != null && atacarRect.contains(mx, my)) {
            onLuchar.run();
            return null;
        }
        if (defenderRect != null && defenderRect.contains(mx, my)) {
            return new Defender(heroe, heroe);
        }
        if (pasarRect != null && pasarRect.contains(mx, my)) {
            return null; // PASAR signal: controller interprets as "end turn"
        }
        return null;
    }

    @Override
    public void dibujar(Graphics2D g, int w, int h) {
        int heroStatusX = HERO_X + (HERO_SIZE - HERO_STATUS_W) / 2;
        int mx = heroStatusX + HERO_STATUS_W + 10;
        int mw = w - mx - 16;
        int my = HUD_Y;
        int mh = HUD_H;

        Composite orig = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.88f));
        g.setColor(MENU_BG);
        g.fillRoundRect(mx, my, mw, mh, 8, 8);
        g.setComposite(orig);

        g.setColor(MENU_BORDER);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(mx, my, mw, mh, 8, 8);
        g.setStroke(new BasicStroke(1));

        int btnW = (mw - 30) / 2;
        int btnH = 30;
        int gap = 6;
        int bx1 = mx + 10;
        int bx2 = mx + 10 + btnW + gap;
        int by1 = my + 8;
        int by2 = my + 8 + btnH + gap;

        atacarRect = new Rectangle(bx1, by1, btnW, btnH);
        dibujarBoton(g, bx1, by1, btnW, btnH, "LUCHAR");

        defenderRect = new Rectangle(bx2, by1, btnW, btnH);
        dibujarBoton(g, bx2, by1, btnW, btnH, "DEFENDER");

        pasarRect = new Rectangle(bx1, by2, mw - 20, btnH);
        dibujarBoton(g, bx1, by2, mw - 20, btnH, "PASAR");
    }

    private void dibujarBoton(Graphics2D g, int x, int y, int w, int h, String texto) {
        g.setColor(new Color(50, 50, 70, 220));
        g.fillRect(x, y, w, h);
        g.setColor(MENU_BORDER);
        g.setStroke(new BasicStroke(1.5f));
        g.drawRect(x, y, w, h);
        g.setStroke(new BasicStroke(1));

        g.setFont(FONT_MENU);
        g.setColor(MENU_TEXT);
        FontMetrics fm = g.getFontMetrics();
        int tx = x + (w - fm.stringWidth(texto)) / 2;
        int ty = y + (h + fm.getAscent()) / 2 - 3;
        g.drawString(texto, tx, ty);

        g.setFont(FONT_MENU_SM);
        g.setColor(MENU_TEXT);
        g.drawString("▸", x + 6, ty);
    }
}
