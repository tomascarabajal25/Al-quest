package juego.ciudades.batalla.view.animacion;

import java.awt.Graphics2D;

public class ShakeAnimacion extends Animacion {
    private static final long DURACION_MS = 600;
    private final boolean targetIsEnemy;
    private int offsetX;
    private int offsetY;

    public ShakeAnimacion(boolean targetIsEnemy) {
        this.targetIsEnemy = targetIsEnemy;
    }

    @Override
    public void tick() {
        long e = elapsed();
        if (e > DURACION_MS) {
            terminada = true;
            offsetX = 0;
            offsetY = 0;
            return;
        }
        double intensity = 1.0 - (double) e / DURACION_MS;
        offsetX = (int) (Math.sin(e * 0.05) * 4 * intensity);
        offsetY = (int) (Math.cos(e * 0.07) * 3 * intensity);
    }

    @Override
    public void dibujar(Graphics2D g) {
        g.translate(offsetX, offsetY);
    }

    public boolean isTargetEnemy() { return targetIsEnemy; }
    public int getOffsetX() { return offsetX; }
    public int getOffsetY() { return offsetY; }
}
