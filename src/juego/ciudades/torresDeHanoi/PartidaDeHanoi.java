package juego.ciudades.torresDeHanoi;

import java.util.Objects;

import juego.ciudades.ordenamientos.EstadoDePartida;
import modelos.Jugador;
import modelos.Partida;
import utils.ValidacionesUtiles;
/**
 * Conecta el sistema general de partidas del juego con el puzzle específico de Hanoi.
 */
public class PartidaDeHanoi extends Partida {
    private CiudadHanoi juego; // El motor lógico del puzzle
    private int cantidadDiscos;        // Guardamos la configuración del nivel
  //CONSTRUCTORES-----------------------------------------------------------------
    /**
     * Crea un nuevo juego con una cantidad inicial de discos.
     * 
     * PRE:
     * - 3 <= discos <= 10
     * 
     * POST:
     * - Se inicializan las tres torres.
     * - Todos los discos se ubican en la torre A.
     * - movimientos = 0
     */
    public PartidaDeHanoi(int discos, String nombre, Jugador jugador) {
    	super(nombre, jugador);
    	setCantidadDeDiscos(discos);
        iniciar();
    }
    
  //METODOS DE COMPORTAMIENTO------------------------------------------------------
    /**
     * Inicializa el estado del juego.
     * 
     * POST:
     * - torreA contiene todos los discos.
     * - torreB y torreC están vacías.
     * - movimientos = 0
     */
    public void iniciar() {
    	ValidacionesUtiles.validarFalso(estaIniciada(), "La partida ya esta iniciada");
    	setEstado(EstadoDePartida.Iniciado);
    	this.juego = new CiudadHanoi(this.cantidadDiscos);
    	}
    public void finalizar() {
    	ValidacionesUtiles.validarVerdadero(estaIniciada(), "La partida no esta iniciada");
    	setEstado(EstadoDePartida.Creado);
		
        this.setPuntaje(getPuntajeActual());
    }
    /**
     * Metodo puente público para que clases externas (como el controlador)
     * puedan registrar el puntaje de esta partida específica.
     */
    protected void actualizarPuntaje(int puntos) {
        this.setPuntaje(puntos); 
    }
    
  //METODOS DE CLASES-------------------------------------------------------------
  //METODOS GENERALES------------------------------------------------------------
    
    @Override
	public String toString() {
		return "PartidaDeHanoi [juegoMecanico=" + juego + ", cantidadDiscos=" + cantidadDiscos + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(cantidadDiscos, juego);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PartidaDeHanoi other = (PartidaDeHanoi) obj;
		return cantidadDiscos == other.cantidadDiscos && Objects.equals(juego, other.juego);
	}
    
    
  //GETTER SIMPLES-----------------------------------------------------------------
	// Getter para que el controlador pueda acceder al motor del puzzle
    public CiudadHanoi getJuego() {
        return this.juego;
    }
    

	// Devuelve la cantidad de discos
    public int getCantidadDeDiscos() {
    	return cantidadDiscos;
    }
  
    
  //SETTERS SIMPLES---------------------------------------------------------------
   
    /**
     * PRE:
     * - 3 <= objetivo <= 10
     */
    private void setCantidadDeDiscos(int cantidad) {
    	ValidacionesUtiles.validarRangoNumerico(cantidad, 3, 10, "No es una cantidad de discos valida");
       this.cantidadDiscos = cantidad;
    }
}