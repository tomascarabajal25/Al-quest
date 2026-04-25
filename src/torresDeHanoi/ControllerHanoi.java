package torresDeHanoi;


import java.util.Objects;

import materialesUtiles.ValidacionesUtiles;
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
    private JuegoHanoi juego;
    private HanoiSolver solver;
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
    public ControllerHanoi(int discos, VentanaPrincipalHanoi vista) {
    	ValidacionesUtiles.validarRango(discos, 3, 10, "el objetivo debe estar entre 3 y 10");
    	ValidacionesUtiles.esDistintoDeNull(vista, null);
        this.setJuego(new JuegoHanoi(discos));
        this.setSolver(new HanoiSolver(this));
        this.setVista(vista);
        actualizarVista();
        
    }
 
  //METODOS DE CLASES-------------------------------------------------------------
  //METODOS GENERALES------------------------------------------------------------
    @Override
	public int hashCode() {
		return Objects.hash(juego, vista);
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
		return Objects.equals(juego, other.juego) && Objects.equals(vista, other.vista);
	}
	
	@Override
	public String toString() {
		return "ControllerHanoi [juego=" + juego + ", vista=" + vista + "]";
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
        juego.mover(juego.getTorreA(), juego.getTorreB());
        actualizarVista();
    }

	public void moverA_C() {
        juego.mover(juego.getTorreA(), juego.getTorreC());
        actualizarVista();
        preguntarSiGano();
    }

    public void moverB_A() {
        juego.mover(juego.getTorreB(), juego.getTorreA());
        actualizarVista();
    }

    public void moverB_C() {
        juego.mover(juego.getTorreB(), juego.getTorreC());
        actualizarVista();
    }

    public void moverC_A() {
        juego.mover(juego.getTorreC(), juego.getTorreA());
        actualizarVista();
    }

    public void moverC_B() {
        juego.mover(juego.getTorreC(), juego.getTorreB());
        actualizarVista();
        preguntarSiGano();
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
    	return juego.esPerfecto();
    	}
    /*
     * pregunta si el juego ha sido ganado
     */
    private boolean testearGanador() {
    	return juego.haGanado();
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
    	solver = new HanoiSolver(this);
    	solver.resolverHanoi(
                juego.getTorreA().getContNodo(),
                juego.getTorreA(),
                juego.getTorreB(),
                juego.getTorreC()
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
    	this.juego.reiniciar(discos);
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
    public JuegoHanoi getJuego() {
        return juego;
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
        return juego.getDiscosDeTorre(juego.getTorreA());
    }

    public String[] getTorreB() {
        return juego.getDiscosDeTorre(juego.getTorreB());
    }

    public String[] getTorreC() {
        return juego.getDiscosDeTorre(juego.getTorreC());
    }
    /*
     * retorna el estado del juego
     */
    public EstadoHanoi getEstado() {
        EstadoHanoi e = new EstadoHanoi();
        
        e.torreA = juego.getDiscosDeTorre(juego.getTorreA());
        e.torreB = juego.getDiscosDeTorre(juego.getTorreB());
        e.torreC = juego.getDiscosDeTorre(juego.getTorreC());

        e.movimientos = juego.getMovimientos();
        e.minMovimientos = juego.getMinMovimientos();

        return e;
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
     * Establece el juego.
     * 
     * PRE:
     * - juego != null
     */
    private void setJuego(JuegoHanoi juego) {
    	ValidacionesUtiles.esDistintoDeNull(juego, null);
		this.juego=juego;
		
	}
    /**
     * Establece el solver.
     * 
     * PRE:
     * - solver != null
     */
    private void setSolver(HanoiSolver solver) {
    	ValidacionesUtiles.esDistintoDeNull(solver, null);
		this.solver=solver;
		
	}
    

	
  
    	
}