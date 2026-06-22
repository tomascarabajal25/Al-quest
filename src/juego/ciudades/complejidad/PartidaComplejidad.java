package juego.ciudades.complejidad;

import javax.swing.SwingUtilities;
import juego.ciudades.complejidad.ui.VentanaComplejidad;
import modelos.EstadoDePartida;
import modelos.Jugador;
import modelos.Partida;
import modelos.Sonido;

public class PartidaComplejidad extends Partida {

    private final CiudadComplejidad ciudad;
    private VentanaComplejidad ventana; 

    /**
     * @param jugador jugador que participa en la partida
     * @param sonido 
     */
    public PartidaComplejidad(Jugador jugador, Sonido sonido) {
        super("Complejidad Algorítmica", jugador);
        this.ciudad = new CiudadComplejidad();
        setSonido(sonido);
    }

    @Override
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);
        SwingUtilities.invokeLater(() -> {
            this.ventana = new VentanaComplejidad(this); 
        });
         if (this.sonido != null) {
			this.sonido.playMusica(juego.configuracion.ConstantesSonido.COMPLEJIDAD);
		}
    }

    @Override
    public void finalizar() {
        setEstado(EstadoDePartida.Creado);
   
        if (this.ventana != null) {
            this.ventana.dispose();
            this.ventana = null;
        }
        if (this.sonido != null) {
			this.sonido.stopMusica();
			this.sonido.playMusica(juego.configuracion.ConstantesSonido.GLOBAL_AVENTURA);
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