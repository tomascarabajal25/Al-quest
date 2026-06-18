package juego.ciudades.batalla.view.estado;

import java.awt.Color;
import juego.ciudades.batalla.model.EstadoActivo;

public interface StateUi {
    String getBadgeText();
    Color getBadgeColor();
    String getDescripcion(EstadoActivo estado);
}
