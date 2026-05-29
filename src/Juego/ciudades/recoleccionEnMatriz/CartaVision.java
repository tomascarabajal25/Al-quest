package Juego.ciudades.recoleccionEnMatriz;

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
    public CartaVision(String nombre, int puntos) {
        super(nombre);
        setPuntos(puntos);
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
    @Override
    public void aplicarEfecto(CiudadRecoleccion juego){
        juego.aumentarVision(this.puntos);
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
}
