package Juego.ciudades.recoleccionEnMatriz;

import modelos.*;
import utils.ValidacionesUtiles;

import estructuras.vector.Vector;

public class CiudadRecoleccion {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private Mapa3D mapa = null;
    private Mochila mochila = null;
    private Vector<Elemento> elementos;
    private Jugador jugador = null;

    private EstadoDeJuego estado = null;
    private int desplazamiento;
    private int visibilidad;
    private int puntos;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
    public CiudadRecoleccion(int filas, int columnas, int niveles, int maximoMochila, Jugador jugador) {
        ValidacionesUtiles.validarMayorACero(filas, "filas");
        ValidacionesUtiles.validarMayorACero(columnas, "columnas");
        ValidacionesUtiles.validarMayorACero(niveles, "niveles");
        ValidacionesUtiles.validarMayorACero(maximoMochila, "maximoMochila");

        setMochila(maximoMochila);
        setElementos(maximoMochila);
        setMapa(filas, columnas, niveles);
        setJugador(jugador);

        setDesplazamiento(1);
        setVisibilidad(1);
        setPuntos(0);
        iniciar();

    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    /**
     * Establece los recursos en su estado inicial para comenzar el juego
     */
    public void iniciar() {
        ubicarElementosEnMapa();
        ubicarJugadorEnMapa(1, 1, 1);
        setEstado(EstadoDeJuego.COMENZADO);
        comenzarJuego();
    }

    /**
     *Comienza el juego
     */
    public void comenzarJuego() {
        while (this.estado == EstadoDeJuego.COMENZADO) {
            Direccion direccion = nuevaDireccion();
            try{
                moverJugador(direccion);
            }
            catch(RuntimeException e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Finaliza el juego de la ciudad
     *
     * @return: Devuelve los puntos acumulados
     */
    public int finalizar() {
        return this.puntos;
    }

    /**
     * Mueve al jugador
     *
     * PRE:
     * -Jugador y la direccion no deben ser null
     *
     * @param direccion: Direccion en la que el jugador se mueve
     */
    public void moverJugador(Direccion direccion) {
        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");
        ValidacionesUtiles.esDistintoDeNull(direccion, "direccion");

        int[] posicionJugador = this.mapa.getPosicionCeldaConContenido(this.jugador);
        int nuevaFilaJugador = posicionJugador[0];
        int nuevaColumnaJugador = posicionJugador[1];

        switch (direccion){

            case ARRIBA:
                nuevaFilaJugador -= desplazamiento;
                break;

            case ABAJO:
                nuevaFilaJugador += desplazamiento;
                break;

            case IZQUIERDA:
                nuevaColumnaJugador -= desplazamiento;
                break;

            case DERECHA:
                nuevaColumnaJugador += desplazamiento;
                break;
        }
        this.mapa.validarFueraDeRango(nuevaFilaJugador, nuevaColumnaJugador, posicionJugador[2]);
        moverJugadorEnMapa(nuevaFilaJugador, nuevaColumnaJugador, posicionJugador[2]);
        verificarCartasVecinas();
        validarMochila();
    }

    /**
     * Mueve al jugador dentro del mapa
     *
     * PRE:
     * -Fila, columna y nivel deben ser mayores a cero
     *
     * @param fila: Nueva fila del jugador
     * @param columna: Nueva columna del jugador
     * @param nivel: Nuevo nivel del jugador
     */
    public void moverJugadorEnMapa(int fila, int columna, int nivel) {
        ValidacionesUtiles.validarMayorACero(fila, "fila");
        ValidacionesUtiles.validarMayorACero(columna, "columna");
        ValidacionesUtiles.validarMayorACero(nivel, "nivel");

        ubicarJugadorEnMapa(fila, columna, nivel);
    }


    /**
     * Ubica los elementos carta dentro del mapa
     */
    public void ubicarElementosEnMapa() {
        this.mapa.ocuparCelda(this.elementos.obtener(1), 5, 3, 1);
        this.mapa.ocuparCelda(this.elementos.obtener(2), 2, 9, 2);
        this.mapa.ocuparCelda(this.elementos.obtener(3), 1, 5, 3);
    }

    /**
     * Ubica al jugador en la posicion dada
     *
     * PRE:
     * -Fila, columna y nivel deben ser mayores a 0
     */
    public void ubicarJugadorEnMapa(int fila, int columna, int nivel) {
        ValidacionesUtiles.validarMayorACero(nivel, "nivel");
        this.mapa.ocuparCelda(this.jugador, fila, columna, nivel);
    }

    /**
     * Aumentar la vision del jugador
     */
    public void aumentarVision(int puntos) {
        this.visibilidad++;
        setPuntos(this.puntos + puntos);
    }

    /**
     * Aumentar el desplazamiento del jugador
     */
    public void aumentardesplazamiento(int puntos) {
        this.desplazamiento++;
        setPuntos(this.puntos + puntos);
    }

    /**
     * Multiplicar puntos
     */
    public void aumentarPuntos(int multiplicador) {
        ValidacionesUtiles.validarMayorACero(multiplicador, "multiplicador");
        setPuntos(this.puntos * multiplicador);

    }

    /**
     *
     */
    public boolean verificarCartasVecinas() {

        int[] posicionJugador = this.mapa.getPosicionCeldaConContenido(this.jugador);
        Vector<Vector<Celda<?>>> vecinos = this.mapa.getNivel(posicionJugador[2]).getCeldasVecinasRespectoPosicion(posicionJugador[0],  posicionJugador[1], 1);

        for (int i = 0; i < vecinos.getLongitud(); i++ ){
            for (int j = 0; j < vecinos.obtener(i).getLongitud(); j++){
                if (vecinos.obtener(i).obtener(j).getContenido() instanceof Elemento) {
                    Elemento carta = ((Elemento) vecinos.obtener(i).obtener(j).getContenido());
                    ubicarJugadorEnMapa(posicionJugador[0], posicionJugador[1], posicionJugador[2]+1);
                    this.mochila.agregarElemento(carta);
                }
            }
        }

        return false;
    }

    /**
     * Aplica el efecto de la carta dada
     *
     * PRE:
     * -Carta no debe ser nulo
     *
     * @param carta: Carta de la que se quiere aplicar el efecto
     */
    public void aplicarEfectoCarta(Elemento carta) {
        ValidacionesUtiles.esDistintoDeNull(carta, "carta");
        carta.aplicarEfecto(this);

    }


    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    /**
     *Valida si mochila guarda tres elementos
     */
    public void validarMochila(){
        if (this.mochila.getCantidadElementos() == 3){
            setEstado(EstadoDeJuego.FINALIZADO);
        }
    }
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Setter del atributo mochila
     *
     * PRE
     * -El parametro maximoMochila debe ser mayor a cero
     *
     * @param maximoMochila: Cantidad de elementos que guardara la mochila
     */
    private void setMochila(int maximoMochila){
        ValidacionesUtiles.validarMayorACero(maximoMochila, "maximoMochila");
        mochila = new Mochila(maximoMochila);
    }

    /**
     * Setter de los elementos
     *
     * PRE:
     * -El parametro maximoMochila debe ser mayor a cero
     * -Los elementos que se guarden no deben ser null
     */
    private void setElementos(int maximo){
        CartaVision cartaVision = new CartaVision("Carta Vision", 5000);
        CartaDesplazamiento cartaDesplazamiento = new CartaDesplazamiento("Carta Desplazamiento", 2000);
        CartaPuntos cartaPuntos = new CartaPuntos("Carta Puntos");

        this.elementos = new Vector<>(maximo, cartaVision);
        this.elementos.agregar(cartaDesplazamiento);
        this.elementos.agregar(cartaPuntos);

    }

    /**
     * Setter del atributo mapa
     *
     * PRE:
     * -Los atributos filas, columnas y niveles deben ser mayores a cero
     */
    private void setMapa(int filas, int columnas, int niveles){
        ValidacionesUtiles.validarMayorACero(filas, "filas");
        ValidacionesUtiles.validarMayorACero(columnas, "columnas");
        ValidacionesUtiles.validarMayorACero(niveles, "niveles");
        this.mapa = new Mapa3D(filas, columnas, niveles);
    }

    /**
     * Setter del atributo jugador
     *
     * PRE:
     * -El jugador no puede ser null
     */
    private void setJugador(Jugador jugador){
        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");
        this.jugador = jugador;
    }

    /**
     * Setter del atributo estado
     *
     * PRE:
     * -Estado no debe ser null
     */
    private void setEstado(EstadoDeJuego estado){
        ValidacionesUtiles.esDistintoDeNull(estado, "estado");
        this.estado = estado;
    }

    /**
     * Setter del atributo desplazamiento
     *
     * PRE:
     * -El desplazamiento debe ser mayor a cero
     */
    private void setDesplazamiento(int desplazamiento){
        ValidacionesUtiles.validarMayorACero(desplazamiento, "desplazamiento");
        this.desplazamiento = desplazamiento;
    }

    /**
     * Setter del atributo visibilidad
     *
     * PRE:
     * -La visibilidad debe ser mayor a cero
     */
    private void setVisibilidad(int visibilidad){
        ValidacionesUtiles.validarMayorACero(visibilidad, "visibilidad");
        this.visibilidad = visibilidad;
    }

    /**
     * Setter del atributo puntos
     *
     * PRE:
     * -Puntos debe ser mayor o igual a cero
     */
    private void setPuntos(int puntos){
        ValidacionesUtiles.validarMayorOIgualACero(puntos, "puntos");
        this.puntos = puntos;
    }

}
