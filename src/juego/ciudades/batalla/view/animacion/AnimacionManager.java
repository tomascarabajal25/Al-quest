package juego.ciudades.batalla.view.animacion;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class AnimacionManager {
    private final List<Animacion> animaciones = new ArrayList<>();

    public void agregar(Animacion anim) {
        anim.iniciar();
        animaciones.add(anim);
    }

    public void tick() {
        animaciones.removeIf(a -> {
            a.tick();
            return a.terminada();
        });
    }

    public void dibujar(Graphics2D g) {
        for (Animacion a : animaciones) {
            if (!a.terminada()) a.dibujar(g);
        }
    }

    public boolean hayAnimaciones() {
        return !animaciones.isEmpty();
    }
}
