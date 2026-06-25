package juego.ciudades.batalla.view.animacion;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

public class GlowAnimacion extends Animacion {
    private static final long DURACION_MS = 800;
    private final Color color;
    private double alpha;

    public GlowAnimacion(Color color) {
        this.color = color;
    }

    @Override
    public void tick() {
        long e = elapsed();
        if (e >= DURACION_MS) {
            alpha = 0;
            terminada = true;
            return;
        }
        double phase = (double) e / DURACION_MS;
        alpha = Math.sin(phase * Math.PI) * 0.35;
    }

    @Override
    public void dibujar(Graphics2D g) {
        if (alpha <= 0) return;
        Composite orig = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) alpha));
        g.setColor(color);
        g.fillRect(0, 0, 736, 414);
        g.setComposite(orig);
    }
}
