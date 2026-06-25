package juego.ciudades.batalla.view.estado;

import java.awt.Color;
import juego.ciudades.batalla.model.EstadoActivo;

public class DefendiendoUi implements StateUi {
    @Override
    public String getBadgeText() { return "DEF"; }

    @Override
    public Color getBadgeColor() { return new Color(100, 180, 255); }

    @Override
    public String getDescripcion(EstadoActivo estado) {
        return estado.getOrigen().getNombre() + " se está defendiendo!";
    }
}
