package juego.ciudades.torresDeHanoi;


import java.util.Objects;

import utils.ValidacionesUtiles;
/**
 * Controlador del juego Torres de Hanoi.
 * 
 * Actúa como intermediario entre la vista (VentanaPrincipalHanoi)
 * y el modelo (JuegoHanoi), gestionando las acciones del usuario
 * y la resolución automática del problema.
 * 
 * Implementa el patrón observador para recibir notificaciones
 * durante la resolución recursiva.
 */
public class ControllerHanoi implements ObservadorHanoi {
	//ATRIBUTOS----------------------------------------------------------------------
	private PartidaDeHanoi partida;
    private HanoiSolver<String> solver;
    private VentanaPrincipalHanoi vista;
  
	
  	//CONSTRUCTORES-----------------------------------------------------------------
    /**
     * Construye un controlador con una cantidad inicial de discos.
     * 
     * PRE:
     * - 10>discos > 3
     * - vista != null
     * 
     * POST:
     * - Se inicializa un nuevo juego con la cantidad de discos indicada.
     * - Se crea un solver asociado al controlador.
     * - Se establece la vista.
     */
    public ControllerHanoi(PartidaDeHanoi partidaSeleccionada,int discos, VentanaPrincipalHanoi vista) {
    	ValidacionesUtiles.validarRango(discos, 3, 10, "el objetivo debe estar entre 3 y 10");
    	ValidacionesUtiles.esDistintoDeNull(vista, null);
    	setPartida(partidaSeleccionada) ;
        
        if (!this.partida.estaIniciada()) {
            this.partida.iniciar(); 
        }
        this.setSolver(new HanoiSolver<String>(this));
        this.setVista(vista);
        actualizarVista();
        
    }
 
    public void registrarFinDelJuego() {
        if (partida.getJuego().haGanado()) {        
            vista.mostrarVictoria();
        }
        this.partida.actualizarPuntaje(calcularPuntaje());
        this.partida.finalizar();
    }
    
    
    private int calcularPuntaje() {
    	CiudadHanoi juego= partida.getJuego();
        int multiplicador=juego.getObjetivo();
        int puntos= juego.esPerfecto()?150:0;
        if(puntos == 0) {
        	puntos=juego.haGanado()?100:0;
        }
        return puntos*multiplicador;
    }

  //METODOS DE CLASES-------------------------------------------------------------
  //METODOS GENERALES------------------------------------------------------------
    @Override
	public int hashCode() {
		return Objects.hash(partida.getJuego(), vista);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ControllerHanoi other = (ControllerHanoi) obj;
		return Objects.equals(partida.getJuego(), other.partida.getJuego()) && Objects.equals(vista, other.vista);
	}
	
	@Override
	public String toString() {
		return "ControllerHanoi [juego=" + partida.getJuego() + ", vista=" + vista + "]";
	}

	//METODOS DE COMPORTAMIENTO------------------------------------------------------
	/**
     * metodos para mover de una torre a la otra
     * 
     * PRE:
     * - El juego está inicializado.
     * - El movimiento es válido según reglas de Hanoi.
     * 
     * POST:
     * - Se modifica el estado interno del juego.
     */
    public void moverA_B() {
        partida.getJuego().mover(partida.getJuego().getTorreA(), partida.getJuego().getTorreB());
        actualizarVista();
    }

	public void moverA_C() {
        partida.getJuego().mover(partida.getJuego().getTorreA(), partida.getJuego().getTorreC());
        actualizarVista();
        preguntarSiGano();
    }

    public void moverB_A() {
        partida.getJuego().mover(partida.getJuego().getTorreB(), partida.getJuego().getTorreA());
        actualizarVista();
    }

    public void moverB_C() {
        partida.getJuego().mover(partida.getJuego().getTorreB(), partida.getJuego().getTorreC());
        actualizarVista();
        preguntarSiGano();
    }

    public void moverC_A() {
        partida.getJuego().mover(partida.getJuego().getTorreC(), partida.getJuego().getTorreA());
        actualizarVista();
    }

    public void moverC_B() {
        partida.getJuego().mover(partida.getJuego().getTorreC(), partida.getJuego().getTorreB());
        actualizarVista();
    }
    
    /*
     * post: si se gano con la cantidad min de movimientos o si se gano le pide a vista q muestre la alerta
     */
    public void preguntarSiGano() {
    	if (testearGanadorPerfecto()) {
        	vista.mostrarVictoriaPerfecta();
        }
        else if(this.testearGanador()) {
        	vista.mostrarVictoria();
        }
    }
    
    /*
     * pregunta si el juego ha sido ganado con la cantidad min de movimientos
     */
    private boolean testearGanadorPerfecto() {
    	return partida.getJuego().esPerfecto();
    	}
    /*
     * pregunta si el juego ha sido ganado
     */
    private boolean testearGanador() {
    	return partida.getJuego().haGanado();
    	}
    /**
     * Resuelve automáticamente el problema de Torres de Hanoi.
     * 
     * PRE:
     * - El juego está inicializado.
     * 
     * POST:
     * - Se ejecutan los movimientos necesarios para resolver el juego.
     * - La vista puede actualizarse progresivamente según el observador.
     */
    public void resolver() {
    	ValidacionesUtiles.validarVerdadero(vista.getJuegoIniciado(), "no se puede resolver un juego no iniciado");
    	solver = new HanoiSolver<String>(this);
    	solver.resolverHanoi(
                partida.getJuego().getTorreA().getContNodo(),
                partida.getJuego().getTorreA(),
                partida.getJuego().getTorreB(),
                partida.getJuego().getTorreC()
            );
    }
    
    /**
     * Actualiza la vista con el estado actual del juego.
     * 
     * PRE:
     * - vista != null
     * 
     * POST:
     * - La vista refleja el estado actual del modelo.
     */
    protected void actualizarVista() {
    	vista.actualizar(this.getEstado());
    }
    /**
     * Reinicia el juego con una nueva cantidad de discos.
     * 
     * PRE:
     * - 10>discos > 3
     * 
     * POST:
     * - El juego vuelve a su estado inicial con los discos indicados.
     * - El contador de movimientos se reinicia.
     */
    protected void reiniciar(int discos) {
    	ValidacionesUtiles.validarRango(discos, 3, 10, null);
    	this.partida.getJuego().reiniciar(discos);
    	actualizarVista();
    }
    
    /**
     * Método del observador que se ejecuta en cada movimiento
     * durante la resolución automática.
     * 
     * PRE:
     * - paso >= 0
     * 
     * POST:
     * - La vista se actualiza.
     * - Se consulta al usuario si desea continuar.
     * 
     * @param paso número de movimiento actual
     * @return true si el usuario desea continuar, false en caso contrario
     */
    @Override
    public boolean onMovimiento(int paso) {
        vista.actualizar(this.getEstado());
        return vista.preguntarContinuar(paso);
    }   
    
  //GETTER SIMPLES-----------------------------------------------------------------
    /**
     * Devuelve el modelo del juego.
     */
    public CiudadHanoi getJuego() {
        return partida.getJuego();
    }
    
    /**
     * Obtiene los discos de una torre en forma de arreglo.
     * 
     * PRE:
     * - torre != null
     * 
     * POST:
     * - Retorna un arreglo con los discos de la torre.
     */
    public String[] getTorreA() {
        return partida.getJuego().getDiscosDeTorre(partida.getJuego().getTorreA());
    }

    public String[] getTorreB() {
        return partida.getJuego().getDiscosDeTorre(partida.getJuego().getTorreB());
    }

    public String[] getTorreC() {
        return partida.getJuego().getDiscosDeTorre(partida.getJuego().getTorreC());
    }
    /*
     * retorna el estado del juego
     */
    public EstadoHanoi getEstado() {
        
        return new EstadoHanoi(
            partida.getJuego().getDiscosDeTorre(partida.getJuego().getTorreA()),
            partida.getJuego().getDiscosDeTorre(partida.getJuego().getTorreB()),
            partida.getJuego().getDiscosDeTorre(partida.getJuego().getTorreC()),
            partida.getJuego().getMovimientos(),
            partida.getJuego().getMinMovimientos()
        );
    }
     /**
      * Devuelve la vista asociada.
      */
     public VentanaPrincipalHanoi getVista() {
    	 return this.vista;
     }
    
  //SETTERS SIMPLES---------------------------------------------------------------
     /**
      * Establece la vista.
      * 
      * PRE:
      * - vista != null
      */
    private void setVista(VentanaPrincipalHanoi vista2) {
    	ValidacionesUtiles.esDistintoDeNull(vista2, null);
		this.vista=vista2;
		
	}
    /**
     * Establece la partida.
     * 
     * PRE:
     * - partida != null
     */
    private void setPartida(PartidaDeHanoi partidaSeleccionada) {
    	ValidacionesUtiles.esDistintoDeNull(partidaSeleccionada, null);
		this.partida=partidaSeleccionada;
		
	}
    /**
     * Establece el solver.
     * 
     * PRE:
     * - solver != null
     */
    private void setSolver(HanoiSolver<String> solver) {
    	ValidacionesUtiles.esDistintoDeNull(solver, null);
		this.solver=solver;
		
	}
 
    	
}