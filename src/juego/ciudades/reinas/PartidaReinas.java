package juego.ciudades.reinas;


import juego.ciudades.ordenamientos.EstadoDePartida;
import juego.ciudades.reinas.ui.VentanaPrincipal;
import modelos.Jugador;
import modelos.Partida;

public class PartidaReinas extends Partida {

	private CiudadReinas ciudad;
    private int tamanio;
    private VentanaPrincipal ventana;

    /**
     * Crea una partida de N-Reinas para el jugador dado.
     *
     * @param jugador jugador que participa en la partida
     */
    public PartidaReinas(Jugador jugador) {
        super("N-Reinas", jugador);
        setEstado(EstadoDePartida.Creado);
    }

    /**
     * Inicia la partida abriendo la Ventana Principal, la cual 
     * mostrará primero el selector de tamaño.
     */
    @Override
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);

        // Instanciamos la ventana pasándole ESTA partida (this) y el listener de victoria
        this.ventana = new VentanaPrincipal(this, () -> {
            System.out.println("¡Ciudad completada!"); // Print temporal que tenías
            finalizar();
        });

        this.ventana.setVisible(true);
        if(sonido != null) {
			sonido.playMusica(juego.configuracion.ConstantesSonido.REINAS);
		}
    }

    /**
     * 
     *
     * @param tamanioElegido el tamaño (NxN) seleccionado en la UI
     */
    public void configurarModelo(int tamanioElegido) {
        this.tamanio = tamanioElegido;
        this.ciudad = new CiudadReinas();
    }

    /**
     * Finaliza la partida, destruyendo la ventana si sigue abierta.
     */
    @Override
    public void finalizar() {
        setEstado(EstadoDePartida.Creado);
        setPuntaje(300*tamanio);

        if (this.ventana != null) {
            this.ventana.dispose();
            this.ventana = null;
        }
        
        if(sonido != null) {
        	sonido.stopMusica();
        	sonido.playMusica(juego.configuracion.ConstantesSonido.GLOBAL_AVENTURA);
        }

        notificarFinalizacion();
    }

    /** @return la ciudad de reinas asociada a esta partida */
    public CiudadReinas getCiudad() {
        return ciudad;
    }

    /** @return dimensión del tablero */
    public int getTamanio() {
        return tamanio;
    }
}
