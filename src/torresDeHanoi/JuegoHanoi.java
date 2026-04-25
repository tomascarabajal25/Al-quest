package torresDeHanoi;

import java.util.Objects;

import materialesUtiles.ValidacionesUtiles;
/**
 * TDA que representa el juego de las Torres de Hanoi.
 * 
 * Modela el estado del juego mediante tres pilas (torres),
 * la cantidad de movimientos realizados y el objetivo (número de discos).
 * 
 * INVARIANTES:
 * - 3 <= objetivo <= 10
 * - movimientos >= 0
 * - Las torres solo contienen discos válidos (Strings con "#")
 * - En cada torre, los discos están ordenados de mayor a menor (de abajo hacia arriba)
 */
public class JuegoHanoi {
	//ATRIBUTOS----------------------------------------------------------------------
	
		
    private Pila torreA;
    private Pila torreB;
    private Pila torreC;
    private int movimientos;
    private int objetivo;
    
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
    public JuegoHanoi(int discos) {
    	ValidacionesUtiles.validarRangoNumerico(discos, 3, 10, "No es una cantidad de discos valida");
        this.objetivo = discos;
        inicializar();
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
    private void inicializar() {
        torreA = new Pila();
        torreB = new Pila();
        torreC = new Pila();
        movimientos = 0;

        for (int i = objetivo; i >= 1; i--) {
            torreA.push(new Nodo<String>("#".repeat(i)));
        }
    }
    /**
     * Reinicia el juego con un nuevo objetivo.
     * 
     * PRE:
     * - 3 <= nuevoObjetivo <= 10
     * 
     * POST:
     * - Se vacían todas las torres.
     * - torreA vuelve a contener todos los discos.
     * - movimientos = 0
     */
    protected void reiniciar(int nuevoObjetivo) {
    	ValidacionesUtiles.esDistintoDeNull(nuevoObjetivo, "el objetivo no puede ser nulo");
    	ValidacionesUtiles.validarRangoNumerico(nuevoObjetivo, 3, 10, "No es un objetivo valido");
        this.setObjetivo(nuevoObjetivo); 
        this.setMovimientos(0); 
        
        // vaciar todas las pilas para limpiar cualquier estado previo
        vaciarPila(torreA);
        vaciarPila(torreB);
        vaciarPila(torreC);
        
        // volver a llenar la torre A
        for (int i = objetivo; i >= 1; i--) {
            torreA.push(new Nodo<String>("#".repeat(i)));
        }
    }

    /**
     * Vacía completamente una pila.
     * 
     * PRE:
     * - p != null
     * 
     * POST:
     * - p queda vacía
     */
    private void vaciarPila(Pila p) {
    	ValidacionesUtiles.esDistintoDeNull(p, "no se puede vaciar pila nula");
        while (p.getContNodo() > 0) {
            p.pop();
        }
    }
    /**
     * Realiza un movimiento entre torres.
     * 
     * PRE:
     * - origen != null
     * - destino != null
     * 
     * POST:
     * - Si el movimiento es válido:
     *      - Se mueve un disco de origen a destino
     *      - movimientos se incrementa en 1
     *      - Retorna true
     * - Si el movimiento no es válido:
     *      - El estado no cambia
     *      - Retorna false
     */
    public boolean mover(Pila origen, Pila destino) {
    	ValidacionesUtiles.esDistintoDeNull(origen, "no se puede mover pila a una pila nula");
    	ValidacionesUtiles.esDistintoDeNull(destino, "no se puede mover una pila nula");
    	
        if (origen.getContNodo() == 0) return false;

        Nodo<String> plataforma = new Nodo<String> (origen.peek());

        if (destino.getContNodo() > 0 &&
            plataforma.getDato().compareTo(destino.peek()) > 0) {
            return false;
        }

        origen.pop();
        destino.push(plataforma);
        setMovimientos(++movimientos);;
        return true;
    }
    
    /*
     * retorna si el juego ha sido ganado
     */
    public boolean haGanado() {
        return torreC.getContNodo() == objetivo;
    }
    /*
     * retorna si el juego ha sido ganado con el minimo de movimientos
     */
    public boolean esPerfecto() {
        return haGanado() && movimientos == getMinMovimientos();
    }
  //METODOS DE CLASES-------------------------------------------------------------
  //METODOS GENERALES------------------------------------------------------------
    @Override
	public String toString() {
		return "JuegoHanoi [movimientos=" + movimientos + ", objetivo=" + objetivo + "]";
	}
    @Override
	public int hashCode() {
		return Objects.hash(movimientos, objetivo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JuegoHanoi other = (JuegoHanoi) obj;
		return movimientos == other.movimientos && objetivo == other.objetivo;
	}
  //GETTER SIMPLES-----------------------------------------------------------------
	/**
     * Calcula el mínimo número de movimientos necesarios.
     * 
     * POST:
     * - Retorna 2^objetivo - 1
     */
	public double getMinMovimientos() {
        return Math.pow(2, objetivo) - 1;
    }

	/**
     * Devuelve la cantidad de movimientos realizados.
     */
	public int getMovimientos() {
        return movimientos;
    }
	/**
     * Devuelve los discos de una torre en forma de arreglo.
     * 
     * PRE:
     * - torre != null
     * 
     * POST:
     * - Retorna un arreglo con los discos (de abajo hacia arriba)
     * - Las posiciones vacías contienen null
     */
    public String[] getDiscosDeTorre(Pila torre) {
    	ValidacionesUtiles.esDistintoDeNull(torre, "no se puede obtener los discos de una pila nula");
        String[] discos = new String[10]; 
        int i = 0; // Empezamos desde la última fila (abajo)
        
        Nodo<String> actual = torre.getCabeza();
        
        while (actual != null && i < discos.length) {
            discos[i] = actual.getDato();
            actual = actual.getAbajo();
            i++;
        }
        return discos;
    }
  
    public Pila getTorreA() { 
    	return torreA; 
    	}
    public Pila getTorreB() { 
    	return torreB; 
    	}
    public Pila getTorreC() { 
    	return torreC; 
    	}
  //SETTERS SIMPLES---------------------------------------------------------------
    /**
     * PRE:
     * - movimientos >= 0
     */
    private void setMovimientos(int objetivoNuevo) {
        this.movimientos = objetivoNuevo;
     }
    /**
     * PRE:
     * - 3 <= objetivo <= 10
     */
    private void setObjetivo(int objetivoNuevo) {
       this.objetivo = objetivoNuevo;
    }
}