package modelos;

import utils.ValidacionesUtiles;

import java.util.Objects;

public class Elemento {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private String nombre;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    /**
     * Constructor del TDA Elemento
     *
     * PRE:
     * -El nombre no debe ser null
     * POST:
     * -Se crea una instancia de Elemento con nombre
     *
     * @param nombre: nombre del elemento
     */
    public Elemento(String nombre) {
        ValidacionesUtiles.esDistintoDeNull(nombre, "nombre");
        setNombre(nombre);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------

    /**
     * Valida si las instancias son iguales en base al nombre
     * @param o   the reference object with which to compare.
     * @return: True si son iguales, False si no lo son
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Elemento elemento = (Elemento) o;
        return Objects.equals(nombre, elemento.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nombre);
    }

    @Override
    public String toString() {
        return "Elemento{" +
                "nombre='" + nombre + '\'' +
                '}';
    }

    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Getter del parametro nombre
     *
     * @return: Devuelve el valor del parametro nombre
     */
    public String getNombre(){
        return this.nombre;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Setter del parametro nombre
     *
     * POST:
     * -Establece el valor del parametro nombre
     */
    private void setNombre(String nombre) {
        ValidacionesUtiles.esDistintoDeNull(nombre, "nombre");
        this.nombre = nombre;
    }
}
