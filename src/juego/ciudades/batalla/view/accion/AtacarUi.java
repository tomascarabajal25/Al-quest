package juego.ciudades.batalla.view.accion;

import juego.ciudades.batalla.model.Combatiente;
import juego.ciudades.batalla.view.animacion.Animacion;
import juego.ciudades.batalla.view.animacion.AnimacionCompuesta;
import juego.ciudades.batalla.view.animacion.FlashAnimacion;
import juego.ciudades.batalla.view.animacion.ShakeAnimacion;

public class AtacarUi implements ActionUi {
    private final String origen;
    private final String objetivo;
    private final int danio;

    public AtacarUi(String origen, String objetivo, int danio) {
        this.origen = origen;
        this.objetivo = objetivo;
        this.danio = danio;
    }

    @Override
    public String getMensaje() {
        return
            origen + " atacó a " + objetivo + "!" + " | " +
            objetivo + " recibe " + danio + " de daño.";
    }

    @Override
    public Animacion crearAnimacion(Combatiente actor, Combatiente objetivo) {
        return new AnimacionCompuesta(
            new FlashAnimacion(),
            new ShakeAnimacion(true)
        );
    }
}
