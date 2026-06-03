package Juego.ciudades.recoleccionEnMatriz;

import utils.ValidacionesUtiles;

import estructuras.vector.Vector;
import modelos.Celda;
import modelos.Mapa;

public class Mapa3D {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private Vector<Mapa> mapa = null;
    private int niveles;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    public Mapa3D(int filas, int columnas, int niveles) {
        ValidacionesUtiles.validarMayorACero(filas, "filas");
        ValidacionesUtiles.validarMayorACero(columnas, "columnas");
        ValidacionesUtiles.validarMayorACero(niveles, "niveles");
        setMapa(filas, columnas, niveles);
        setNiveles(niveles);
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Ocupar celda con el contenido dado
     *
     * PRE:
     * -El parametro contenido no debe ser null
     * -Los parametros fila, columna y nivel deben ser mayores a cero y estar en el rango del mapa
     * POST:
     * -La celda pasa a almacenar el contenido dado
     *
     * @param contenido: Contenido a almacenar
     * @param fila: Fila donde se almacena el contenido
     * @param columna: Columna donde se almacena el contenido
     * @param nivel: : Nivel donde se almacena el contenido
     */
    public void ocuparCelda(Object contenido, int fila, int columna, int nivel){
        ValidacionesUtiles.validarMayorACero(niveles, "niveles");
        validarFueraDeRango(fila, columna, nivel);
        this.mapa.obtener(nivel).ocuparCelda(contenido, fila, columna);

    }

    /**
     * Vaciar el contenido de una celda
     *
     * PRE:
     * -Los parametros fila, columna y nivel deben ser mayores a cero
     * POST
     * -La celda queda vacia, almacena null
     *
     * @param fila: Fila donde se almacena el contenido
     * @param columna : Columna donde se almacena el contenido
     * @param nivel: Nivel donde se almacena el contenido
     */
    public void VaciarCelda(int fila, int columna, int nivel){
        ValidacionesUtiles.validarMayorACero(niveles, "niveles");
        validarFueraDeRango(fila, columna, nivel);
        this.mapa.obtener(nivel).vaciarCelda(fila, columna);
    }

    /**
     * Validacion de posicion fuera de rango
     *
     * @param fila: Fila del mapa a validar
     * @param columna: Columna del mapa a validar
     * @param nivel: Nivel del mapa a validar
     */
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    public void validarFueraDeRango(int fila, int columna, int nivel) {
        ValidacionesUtiles.validarMayorACero(fila, "fila");
        ValidacionesUtiles.validarMayorACero(columna, "columna");
        ValidacionesUtiles.validarMayorACero(niveles, "nivel");

        this.mapa.obtener(1).validarFueraDeRango(fila, columna);
        if (niveles > this.mapa.getLongitud()) {
            throw new RuntimeException("Posicion fuera de rango");
        }
    }
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Getter de nivel
     *
     * PRE
     * -El nivel debe ser mayor a 0 y estar en el rango del mapa
     *
     * @return: Devuelve el nivel del mapa solicitado
     */
    public Mapa getNivel(int nivel) {
        ValidacionesUtiles.validarMayorACero(nivel, "nivel");
        return this.mapa.obtener(nivel);
    }

    /**
     * Getter del atributo niveles
     *
     * @return: Devuelve la cantidad de niveles del mapa
     */
    public int getNiveles() {
        return this.niveles;
    }

    /**
     * Getter de la cantidad de celdas
     * @return: Devuelve la cantidad de celdas del mapa
     */
    public int getPosicionCeldaConContenido(int fila, int columna, int nivel) {
        Mapa mapAux = this.mapa.obtener(1);
        int auxNiveles = this.niveles;
        return mapAux.getCantidadCeldas() * auxNiveles;
    }

    /**
     * Getter de celda que se encuentra en la posicion dada
     *
     * PRE:
     * -Los parametros fila, columna y nivel deben ser mayores a cero y estar en el rango del mapa
     *
     * @param fila: Fila de la celda
     * @param columna: Columna de la celda
     * @param nivel: Nivel de la celda
     * @return: Devuelve la celda de la posicion
     */
    public Celda<?> getCeldaConPosicion(int fila, int columna, int nivel) {
        ValidacionesUtiles.validarMayorACero(nivel, "nivel");
        return this.mapa.obtener(nivel).getCeldaConPosicion(fila, columna);
    }

    public int[] getPosicionCeldaConContenido(Object contenido){
        ValidacionesUtiles.esDistintoDeNull(contenido, "contenido");

        int[] posicion = new int[3];
        int[] posicionAux = new int[2];
        for(int i = 0; i < this.niveles; i++) {
            if (this.mapa.obtener(i+1).getPosicionCeldaConContenido(contenido) != null){
                posicionAux = this.mapa.obtener(i+1).getPosicionCeldaConContenido(contenido);
                posicion[0] = posicionAux[0];
                posicion[1] = posicionAux[1];
                posicion[2] = i+1;
                return posicion;
            }
        }
        return null;
    }
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Setter del atributo mapa
     *
     * PRE:
     * -Los atributos filas, columnas y niveles deben ser mayores a 0
     */
    private void setMapa(int filas, int columnas, int niveles) {
        ValidacionesUtiles.validarMayorACero(filas, "filas");
        ValidacionesUtiles.validarMayorACero(columnas, "columnas");
        ValidacionesUtiles.validarMayorACero(niveles, "niveles");

        Mapa mapaInicial = new Mapa(filas, columnas);
        this.mapa = new Vector<>(niveles, mapaInicial);

        for (int i = 1; i <= niveles; i++) {
            Mapa mapaAux = new Mapa(filas, columnas);
            this.mapa.agregar(i, mapaAux);
        }
    }

    /**
     * Setter del atributo niveles
     *
     * PRE:
     * -El atributo niveles debe ser mayor a 0
     */
    private void setNiveles(int niveles) {
        ValidacionesUtiles.validarMayorACero(niveles, "niveles");
        this.niveles = niveles;
    }
}
