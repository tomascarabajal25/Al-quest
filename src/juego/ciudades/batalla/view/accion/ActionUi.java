package juego.ciudades.batalla.view.accion;

import juego.ciudades.batalla.model.Combatiente;
import juego.ciudades.batalla.view.animacion.Animacion;

public interface ActionUi {
    String getMensaje();
    Animacion crearAnimacion(Combatiente actor, Combatiente objetivo);
}
