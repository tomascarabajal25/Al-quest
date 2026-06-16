package modelos;


import utils.ValidacionesUtiles;

import java.util.Objects;

public class Celda<T> {
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private T contenido;
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    /**
     * Constructor del TDA Celda
     *
     * POST:
     * -Se crea una instancia de Celda con contenido
     *
     * @param contenido: nombre del elemento
     */
    public Celda(T contenido){
        setContenido(contenido);
    }
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------

    /**
     * Valida si las instancias son iguales en base al contenido
     * @param o   the reference object with which to compare.
     * @return: True si son iguales, False si no lo son
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Celda<?> celda = (Celda<?>) o;
        return Objects.equals(contenido, celda.contenido);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(contenido);
    }

    @Override
    public String toString() {
        return "Celda{" +
                "contenido=" + contenido +
                '}';
    }
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Getter del parametro contenido
     *
     * @return: Devuelve el valor del parametro contenido
     */
    public T getContenido() {
        return this.contenido;
    }
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Setter del parametro contenido
     *
     * PRE:
     * -Contenido no debe ser nulo
     *
     * POST:
     * -Establece el valor del parametro contenido
     */
    private void setContenido(T contenido) {
        this.contenido = contenido;
    }
}
