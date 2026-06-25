package juego.ciudades.batalla.view.animacion;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

public class FlashAnimacion extends Animacion {
    private static final long DURACION_MS = 1000;
    private double alpha;

    @Override
    public void tick() {
        alpha = Math.max(0, 1.0 - (double) elapsed() / DURACION_MS);
        if (alpha <= 0) terminada = true;
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (alpha <= 0) return;
        Composite orig = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 736, 414);
        g.setComposite(orig);
    }
}
