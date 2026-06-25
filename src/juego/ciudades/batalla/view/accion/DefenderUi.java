package juego.ciudades.batalla.view.accion;

import juego.ciudades.batalla.model.Combatiente;
import juego.ciudades.batalla.view.animacion.Animacion;
import juego.ciudades.batalla.view.animacion.FlashAnimacion;

public class DefenderUi implements ActionUi {
    private final String actor;

    public DefenderUi(String actor) {
        this.actor = actor;
    }

    @Override
    public String getMensaje() {
        return actor + " usó defensa!";
    }

    @Override
    public Animacion crearAnimacion(Combatiente actor, Combatiente objetivo) {
        return new FlashAnimacion();
    }
}
