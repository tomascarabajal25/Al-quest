package ordenamientos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import materialesUtiles.ValidacionesUtiles;

/**
 * administrador de pasos de elemeto T
 * @param <T>
 */
public class AdministradorDePasos<T> {
	//ATRIBUTOS----------------------------------------------------------------------
    private List<PasoOrdenamiento<T>> historialDePasos;
    //CONSTRUCTORES-----------------------------------------------------------------
    /**
     * crea el administrador de pasos
     */
    public AdministradorDePasos(){
        setHistorial(new ArrayList<PasoOrdenamiento<T>>());
    }

    //METODOS DE CLASES-------------------------------------------------------------
  	//METODOS GENERALES------------------------------------------------------------
    
    @Override
	public int hashCode() {
		return Objects.hash(historialDePasos);
	}

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        
        AdministradorDePasos<?> other = (AdministradorDePasos<?>) obj;
        
        return Objects.equals(historialDePasos, other.historialDePasos);
    }
	@Override
	public String toString() {
		return "AdministradorDePasos [historialDePasos=" + historialDePasos + "]";
	}
    
    //METODOS DE COMPORTAMIENTO------------------------------------------------------
  	
    
	/**
	 * Pre:
	 * @param paso diferente de nulo
	 * Post:
	 * guarda el paso en el historial
	 */
	public void guardarPaso(PasoOrdenamiento<T> paso){
		ValidacionesUtiles.esDistintoDeNull(paso, "Paso no puede ser nulo");
        historialDePasos.add(paso);
    }
	//GETTER SIMPLES-----------------------------------------------------------------
		
    /**
     * devuelve los pasos del historial
     * @return
     */
	public List<PasoOrdenamiento<T>> getPasos() {
        return new ArrayList<PasoOrdenamiento<T>>(historialDePasos);
    }
    
	//SETTERS SIMPLES---------------------------------------------------------------
	/**
	 * Pre:
	 * pasos distinto de null
	 * post:
	 * reemplaza el historial por el pasado por parametro
	 */
    protected void setHistorial(List<PasoOrdenamiento<T>> pasos) {
        ValidacionesUtiles.esDistintoDeNull(pasos, "pasos no pueden ser nulos");
        historialDePasos = pasos;
    }
}
