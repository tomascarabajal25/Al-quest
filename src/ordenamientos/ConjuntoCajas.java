package ordenamientos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import materialesUtiles.ValidacionesUtiles;
/*
* conjunto de cajas
*/
public class ConjuntoCajas {
    private List<Caja> cajas;
  //CONSTRUCTORES-----------------------------------------------------------------
    public ConjuntoCajas() {
        this.cajas = new ArrayList<>();
    }
   //METODOS DE COMPORTAMIENTO------------------------------------------------------
    /*
    * agrega una caja 
    */
    public void agregarCaja(Caja c) {
    	ValidacionesUtiles.esDistintoDeNull(c, null);
    	this.cajas.add(c);
        
    }
    /*

    * post: retorna si las cajas estan ordenadas

    */
    public boolean estaOrdenado() {
        for (int i = 0; i < cajas.size() - 1; i++) {
            if (cajas.get(i).getVolumen() > cajas.get(i + 1).getVolumen()) {
                return false;
            }
        }
        return true;
    }

    /*
     * POST:
     * -deja desordenadas las cajas de la lista
     */
    public void desordenar() {
        Collections.shuffle(cajas);
    }
  	//METODOS DE CLASES-------------------------------------------------------------
  	//METODOS GENERALES------------------------------------------------------------
    @Override
    public String toString() {
        return "ConjuntoCajas [Cajas=" + cajas + "]";
    }
	@Override
	public int hashCode() {
		return Objects.hash(cajas);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConjuntoCajas other = (ConjuntoCajas) obj;
		return Objects.equals(cajas, other.cajas);
	}
	//GETTER SIMPLES-----------------------------------------------------------------
    /*
     * retorna la cantidad de cajas
     */
    public int getCantidad() {
        return cajas.size();
    }

    
    
}