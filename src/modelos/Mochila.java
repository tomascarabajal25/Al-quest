package modelos;

import utils.ValidacionesUtiles;

import java.util.Objects;
import java.util.Vector;

public class Mochila {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    private Vector<Elemento> elementos;
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    /**
     * Constructor del TDA Mochila
     *
     * PRE:
     * -El maximo debe ser mayor a 0
     * POST:
     * -Se crea una instancia de Mochila
     *
     * @param maximo: maximo de elementos
     */
    public Mochila(int maximo){
        ValidacionesUtiles.validarMayorACero(maximo, "maximo");
        elementos = new Vector<Elemento>(maximo);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------

    /**
     * Valida si las instancias son iguales en base a los elementos
     * @param o   the reference object with which to compare.
     * @return: True si son iguales, False si no lo son
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Mochila mochila = (Mochila) o;
        return Objects.deepEquals(elementos, mochila.elementos);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(elementos);
    }

    @Override
    public String toString() {
        return "Mochila{" +
                "elementos=" + elementos +
                '}';
    }

    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Agregar un elemento nuevo a la mochila
     *
     * PRE:
     * -El elemento no debe ser null
     * -La capacidad de la mochila no debe estar en su limite
     * POST
     * -El elemento se agrega a la mochila
     *
     * @param elemento: Elemento a agregar
     */
    public void agregarElemento(Elemento elemento){
        ValidacionesUtiles.esDistintoDeNull(elemento, "elemento");
        if(!validarEstaLlena()){
            throw new RuntimeException("La mochila esta llena");
        }
        this.elementos.add(elemento);
    }

    /**
     * Eliminar un elemento
     *
     * PRE:
     * -El elemento no debe ser nulo
     * -El elemento debe estar en la mochila
     * POST:
     * -El elemento es eliminado de la mochila
     *
     * @param elemento: Elemento a eliminar
     */
    public void eliminarElemento(Elemento elemento){
        ValidacionesUtiles.esDistintoDeNull(elemento, "elemento");
        if(!this.elementos.contains(elemento)){
            throw new RuntimeException("El elemento dado no se encuentra en la mochila");
        }
        this.elementos.remove(elemento);
    }

    /**
     * Vacial la mochila
     *
     * POST:
     * -La mochila queda sin elementos
     */
    public void vaciarMochila(){
        for(Elemento elemento : this.elementos){
            this.elementos.remove(elemento);
        }
    }

    /**
     * Valida la capacidad de la mochila
     *
     * @return: true si no esta llena, false si lo esta
     */
    private boolean validarEstaLlena(){
        if (this.getElementos().size() == 3){
            return false;
        }
        return true;
    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Getter del atributo Elementos
     *
     * @return: Devuelve el vector elementos
     */
    public Vector<Elemento> getElementos() {
        return this.elementos;
    }

    /**
     * Getter para obtener elemento por nombre
     *
     * PRE:
     * -El elemento no debe ser null
     *
     * @return: Devuelve un elemento
     */
    public Elemento getElementoPorNombre(Elemento elemento){
        for(Elemento elmt : this.elementos){
            if(elmt.getNombre().equals(elemento.getNombre())){
                return elmt;
            }
        }
        return null;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
}
