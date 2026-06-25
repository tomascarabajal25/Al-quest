package juego.ciudades.batalla.view.animacion;

public class AnimacionCompuesta extends Animacion {
    private final Animacion[] hijos;

    public AnimacionCompuesta(Animacion... hijos) {
        this.hijos = hijos;
    }

    @Override
    public void iniciar() {
        super.iniciar();
        for (Animacion h : hijos) h.iniciar();
    }

    @Override
    public void tick() {
        boolean todosTerminados = true;
        for (Animacion h : hijos) {
            if (!h.terminada()) h.tick();
            if (!h.terminada()) todosTerminados = false;
        }
        if (todosTerminados) terminada = true;
    }

    @Override
    public void dibujar(java.awt.Graphics2D g) {
        for (Animacion h : hijos) {
            if (!h.terminada()) h.dibujar(g);
        }
    }
}
