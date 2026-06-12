package juego.ciudades.complejidad;

import javax.swing.SwingUtilities;
import juego.ciudades.complejidad.ui.VentanaComplejidad;
import juego.ciudades.ordenamientos.EstadoDePartida;
import modelos.Jugador;
import modelos.Partida;

public class PartidaComplejidad extends Partida {

    private final CiudadComplejidad ciudad;
    private VentanaComplejidad ventana; 

    /**
     * @param jugador jugador que participa en la partida
     */
    public PartidaComplejidad(Jugador jugador) {
        super("Complejidad Algorítmica", jugador);
        this.ciudad = new CiudadComplejidad();
    }

    @Override
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);
        SwingUtilities.invokeLater(() -> {
            this.ventana = new VentanaComplejidad(this); 
        });
    }

    @Override
    public void finalizar() {
        setEstado(EstadoDePartida.Creado);
   
        if (this.ventana != null) {
            this.ventana.dispose();
            this.ventana = null;
        }

        notificarFinalizacion(); 
    }

    public void ganar() {
        setPuntaje(1000); 
        finalizar();
    }

    /** @return la ciudad de complejidad asociada a esta partida */
    public CiudadComplejidad getCiudad() {
        return ciudad;
    }
}