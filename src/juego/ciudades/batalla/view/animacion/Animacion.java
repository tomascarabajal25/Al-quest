package juego.ciudades.batalla.view.animacion;

import java.awt.Graphics2D;

public abstract class Animacion {
    protected long inicioMs;
    protected boolean terminada;

    public void iniciar() {
        this.inicioMs = System.currentTimeMillis();
        this.terminada = false;
    }

    public abstract void tick();
    public abstract void dibujar(Graphics2D g);
    public boolean terminada() { return terminada; }
    protected long elapsed() { return System.currentTimeMillis() - inicioMs; }
}
