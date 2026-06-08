package modelos;

import utils.ValidacionesUtiles;

import estructuras.vector.Vector;
import java.util.Arrays;
import java.util.Objects;

public class Mapa {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private final Vector<Vector<Celda<?>>> celdas;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * Constructor del TDA Mapa
     *
     * PRE:
     * -Los parametro ancho y alto deben ser mayor a cero
     * POST:
     * -Se crea una nueva instancia de Mapa con las dimensiones dadas y todas sus celdas inicializadas en null
     *
     * @param ancho: ancho del tablero
     * @param alto: ancho del tablero
     */
    public Mapa(int ancho, int alto) {
        ValidacionesUtiles.validarMayorACero(ancho, "ancho");
        ValidacionesUtiles.validarMayorACero(alto,  "alto");

        celdas = new Vector<>(ancho, null);
        setCeldas(ancho, alto);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------

    /**
     * Valida las celdas, una a una, son iguales
     * @param o   the reference object with which to compare.
     * @return: True si son iguales, False si no lo son
     */
    @Override
    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Mapa mapa = (Mapa) o;

        return Objects.equals(celdas, mapa.celdas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(celdas);
    }

    @Override
    public String toString() {
        return "Mapa{" +
                "celdas=" + celdas +
                '}';
    }
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Ocupar celda con el contenido dado
     *
     * PRE:
     * -El parametro contenido no debe ser null
     * -Los parametros ancho y alto deben ser mayores a cero y estar en el rango del mapa
     * POST:
     * -La celda pasa a almacenar el contenido dado
     *
     * @param contenido: Contenido a almacenarse en la celda
     * @param ancho: Posicion en ancho de la celda
     * @param alto: Posicion en ancho de la celda
     */
    public void ocuparCelda(Object contenido, int ancho, int alto) {
        ValidacionesUtiles.esDistintoDeNull(contenido, "contenido");
        ValidacionesUtiles.validarMayorACero(ancho, "ancho");
        ValidacionesUtiles.validarMayorACero(alto, "alto");
        validarFueraDeRango(ancho, alto);
        Celda<?> nuevaCelda = new Celda<>(contenido);
        this.celdas.obtener(ancho).agregar(alto, nuevaCelda);
    }

    /**
     * Vaciar el contenido de una celda
     *
     * PRE:
     * -Los parametros ancho y alto deben ser mayor o igual a cero y estar en el rango del mapa
     * POST:
     * -La celda queda vacia, almacenando null
     *
     * @param ancho: Posicion en ancho de la celda
     * @param alto: Posicion en ancho de la celda
     */
    public void vaciarCelda(int ancho, int alto) {
        ValidacionesUtiles.validarMayorACero(ancho, "ancho");
        ValidacionesUtiles.validarMayorACero(alto, "alto");
        validarFueraDeRango(ancho, alto);
        Celda<?> nuevaCelda = new Celda<>(null);
        this.celdas.obtener(ancho).agregar(alto, nuevaCelda);
    }
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    /**
     * Validacion de posicion fuera de rango
     *
     * @param ancho: Posicion en ancho
     * @param alto: Posicion en alto
     */
    public void validarFueraDeRango(int ancho, int alto) {
        ValidacionesUtiles.validarMayorOIgualACero(ancho, "ancho");
        ValidacionesUtiles.validarMayorOIgualACero(alto, "alto");
        if (ancho > this.celdas.getLongitud() || alto > this.celdas.obtener(1).getLongitud()) {
            throw new RuntimeException("Posicion fuera de rango");
        }
    }
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Getter del ancho
     * @return: Devuelve el ancho del mapa
     */
    public int getAncho() {
        return this.celdas.getLongitud();
    }

    /**
     * Getter del alto
     * @return: Devuelve el alto del mapa
     */
    public int getAlto() {
        return this.celdas.obtener(1).getLongitud();
    }

    /**
     * Getter de la cantidad de celdas
     * @return: Devuelve la cantidad de celdas del mapa
     */
    public int getCantidadCeldas() {
        return this.celdas.getLongitud()*this.celdas.obtener(1).getLongitud();
    }

    /**
     * Getter de celda que se encuentra en la posicion dada
     *
     * PRE:
     * -Los parametros ancho y alto deben ser mayor o igual a cero y estar en el rango del mapa
     *
     * @param ancho: Posicion en ancho de la celda
     * @param alto: Posicion en ancho de la celda
     * @return: Devuelve la celda de la posicion
     */
    public Celda<?> getCeldaConPosicion(int ancho, int alto) {
        ValidacionesUtiles.validarMayorOIgualACero(ancho, "ancho");
        ValidacionesUtiles.validarMayorOIgualACero(alto, "alto");
        validarFueraDeRango(ancho, alto);
        return this.celdas.obtener(ancho).obtener(alto);
    }

    /**
     * Getter de los vecinos a la posicion dada
     *
     * PRE:
     * -Los parametros ancho y alto deben ser mayores o iguales a cero
     * -El parametro cant debe ser mayor a cero
     *
     * @param ancho: Posicion en ancho de la celda
     * @param alto: Posicion en ancho de la celda
     * @param cant: Cantidad de vecinos en x e y direccion
     * @return: Devuelve una matriz con la posicion en el centro y los vecinos de la posicion dada, si la posicion no existe el valor es null
     */
    public Vector<Vector<Celda<?>>> getCeldasVecinasRespectoPosicion(int ancho, int alto, int cant) {
        ValidacionesUtiles.validarMayorACero(ancho, "ancho");
        ValidacionesUtiles.validarMayorACero(alto, "alto");
        ValidacionesUtiles.validarMayorACero(cant, "cant");
        validarFueraDeRango(ancho, alto);

        int tamanio = 2 * cant + 1;

        Vector<Vector<Celda<?>>> celdasAux = new Vector<>(tamanio, null);

        for (int i = 1; i <= tamanio; i++) {
            Vector<Celda<?>> fila = new Vector<>(tamanio, null);

            for (int j = 1; j <= tamanio; j++) {
                int anchoMapa = ancho - cant + (j - 1);
                int altoMapa = alto - cant + (i - 1);
                Celda<?> celda = null;

                if (anchoMapa >= 1 && anchoMapa <= getAncho() && altoMapa >= 1 && altoMapa <= getAlto()) {
                    celda = getCeldaConPosicion(anchoMapa, altoMapa);
                }
                fila.agregar(j, celda);
            }
            celdasAux.agregar(i, fila);
        }
        return celdasAux;
    }

    /**
     * Getter de celda que almacene el contenido dado
     *
     * PRE:
     * -El contenido no debe ser null
     *
     * @param contenido: Contenido que almacena la celda
     * @return: Devuelve la celda encontrada
     */
    public Celda<?> getCeldaConContenido(Object contenido) {
        ValidacionesUtiles.esDistintoDeNull(contenido, "contenido");
        for (int i = 1; i <= this.celdas.getLongitud(); i++) {
            for (int j = 1; j <= this.celdas.obtener(i).getLongitud(); j++) {
                Celda<?> celda = this.celdas.obtener(i).obtener(j);
                if (celda != null && Objects.equals(celda.getContenido(), contenido)) { // ← getContenido()
                    return celda;
                }
            }
        }
        return null;
    }

    /**
     * Devuelve la posicion de la celda que almacene el contenido dado
     *
     * PRE:
     * -Contenido no debe ser null
     *
     * @return: Devuelve la posicion de la celda
     */
    public int[] getPosicionCeldaConContenido(Object contenido) {
        ValidacionesUtiles.esDistintoDeNull(contenido, "contenido");

        int[] posicion = new int[2];
        for (int i = 1; i <= this.celdas.getLongitud(); i++) {
            for (int j = 1; j <= this.celdas.obtener(i).getLongitud(); j++) {
                Celda<?> celda = this.celdas.obtener(i).obtener(j);
                if (celda != null && Objects.equals(celda.getContenido(), contenido)) { // ← getContenido()
                    posicion[0] = i;
                    posicion[1] = j;
                    return posicion;
                }
            }
        }
        return null;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Setter de las celdas con valor null
     *
     * PRE:
     * -Los parametros alto y ancho deben ser mayores a cero
     * POST:
     * -Todas las celdas se inicializan con null
     *
     * @param ancho: ancho de la matriz
     * @param alto: alto de la matriz
     */
    public void setCeldas(int ancho, int alto) {
        ValidacionesUtiles.validarMayorACero(ancho, "ancho");
        ValidacionesUtiles.validarMayorACero(alto, "alto");
        for (int i = 1; i <= ancho; i++) {
            Vector<Celda<?>> columna = new Vector<>(alto, null);
            for (int j = 1; j <= alto; j++) {
                Celda<?> celda = new Celda<>(null);
                columna.agregar(j, celda);
            }
            this.celdas.agregar(i, columna);
        }
    }
}
