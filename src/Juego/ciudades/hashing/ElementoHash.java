package juego.ciudades.hashing;

import juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelos.Elemento;


/**
 * importo CiudadRecoleccion porque el contrato heredado de Elemento
 * me lo exige, pero ene sta ciudad no necesito usarlo.
 * 
 * Elemento en especifico que inserto y busco adentro de la tabla hash.
 * 
 * modelos.Elemento obliga a implementar aplicarEfecto, entonces por eso 
 * la ciudad de hashing (esta) necesita su propia clase en especifico.
 * cada ElementoHash es un objeto del juego que viaja junto a una clave
 * numerica que se hashea.
 */

public class ElementoHash extends Elemento {
    //ATRIBUTOS
    /**
     * Clave numerica fija con la voy a insertar el elemento en la tabla hash
     */
    private final int clave;


    //CONSTRUCTORES
    /**
     * Constructor del TDA ElementoHash, clave es el codigo que
     * el jugador debe hashear. nombre es el nombre del elemento. 
     * PRE: nombre no puede ser null (me lo valida Elemento).
     * POST: se crea el elemento con clave y nombre. 
     * 
     * @param clave
     * @param nombre
     */
    public ElementoHash(int clave, String nombre) {
        super(nombre);
        this.clave = clave;
    }


    //METODOS DE COMPORTAMIENTO
    /**
     * En esta ciudad (de hashing) los elementos no producen efecto sobre
     * la ciudad de recoleccion. Entonces implemento vacio para cumplir Elemento. 
     * 
     * @param juego juego de recoleccion (en esta ciudad no lo utilizo)
     */
    @Override
    public void aplicarEfecto(CiudadRecoleccion juego) {
        // Sin efecto.
    }


    
    //GETTERS SIMPLES
    /**
     * @return la clave numerica asociada a este elemento. 
     */

    public int getClave() {
        return this.clave;
    }
}
