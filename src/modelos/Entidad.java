package modelos;

import utils.ValidacionesUtiles;

import java.util.Objects;

public abstract class Entidad {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private String nombre;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * Constructor del TDA Entidad
     *
     * PRE:
     * -Nombre no debe ser nulo
     *
     * @param nombre: Nombre de la entidad
     */
    public Entidad(String nombre) {
        setNombre(nombre);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "Entidad [nombre=" + nombre + "]";
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Entidad other = (Entidad) obj;
        return Objects.equals(nombre, other.nombre);
    }
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Getter del atributo nombre
     * @return: Devuelve el valor del atributo
     */
    public String getNombre() {
        return this.nombre;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Setter del atributo nombre
     *
     * PRE:
     * -Nombre no debe ser nulo
     *
     * @param nombre: nombre de la entidad
     */
    private void setNombre(String nombre) {
        ValidacionesUtiles.esDistintoDeNull(nombre, "nombre");
        this.nombre=nombre;
    }
    }







