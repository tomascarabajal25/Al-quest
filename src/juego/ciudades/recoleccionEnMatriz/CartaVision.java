package juego.ciudades.recoleccionEnMatriz;

import modelos.Elemento;
import utils.ValidacionesUtiles;

import java.util.Objects;

public class CartaVision extends Elemento {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private int puntos;
    private String descripcion = null;
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
     * @param nombre : nombre del elemento
     */
    public CartaVision(String nombre, String descripcion, int puntos) {
        super(nombre);
        ValidacionesUtiles.esDistintoDeNull(descripcion, "descripcion");
        ValidacionesUtiles.validarMayorOIgualACero(puntos, "puntos");
        setPuntos(puntos);
        setDescripcion(descripcion);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CartaVision that = (CartaVision) o;
        return puntos == that.puntos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), puntos);
    }

    @Override
    public String toString() {
        return "CartaVision{" +
                "puntos=" + puntos +
                '}';
    }

    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Efecto de la carta
     *
     * PRE:
     * -Juego no debe ser null
     *
     * @param juego: Juego correspondiente a cada ciudad
     */
    @Override
    public void aplicarEfecto(CiudadRecoleccion juego){
        juego.aumentarVision();
    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Getter del atributo puntos
     *
     * @return: Devuelte los puntos de la carta
     */
    public int getPuntos(){
        return this.puntos;
    }

    /**
     * Getter del atributo descripcion
     *
     * @return: Devuelve la descripcion de la carta
     */
    public String getDescripcion(){
        return this.descripcion;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Setter del atributo puntos
     *
     * PRE:
     * -El atributo puntos debe ser mayor a cero
     *
     * @param puntos: Puntos de la carta
     */
    private void setPuntos(int puntos){
        ValidacionesUtiles.validarMayorACero(puntos, "Puntos");
        this.puntos = puntos;
    }

    /**
     * Setter del atributo descripcion
     *
     * PRE:
     * -Descripcion no debe ser nulo
     */
    private void setDescripcion(String descripcion){
        ValidacionesUtiles.esDistintoDeNull(descripcion, "Descripcion");
        this.descripcion = descripcion;
    }
}
