package modelos;

import utils.ValidacionesUtiles;

import java.util.Objects;

public class Jugador {
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private String nombre;
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    /**
     *Constructor de TDA Jugador
     * PRE:
     * -El nombre no debe ser null
     * POST:
     * -Se crea una instancia de Jugador con nombre
     *
     * @param nombre: nombre del jugador
     */
    public Jugador(String nombre) {
        ValidacionesUtiles.esDistintoDeNull(nombre, "nombre");
        setNombre(nombre);
    }
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------

    /**
     * Valida si las instancias son iguales en base al nombre
     * @param o   the reference object with which to compare.
     * @return: True si son iguales, False si no lo son
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Jugador jugador = (Jugador) o;
        return Objects.equals(nombre, jugador.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(nombre);
    }

    @Override
    public String toString() {
        return "Jugador{" +
                "nombre='" + nombre + '\'' +
                '}';
    }

    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Getter del parametro nombre
     *
     * @return : Devuelve el valor del parametro nombre
     */
    public String getNombre() {
        return nombre;
    }
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