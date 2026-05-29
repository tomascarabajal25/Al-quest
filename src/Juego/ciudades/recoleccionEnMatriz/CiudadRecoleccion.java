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
    private Vector<Elemento> elementos = null;
    private Jugador jugador = null;

    private EstadoDeJuego estado = null;
    private int desplazamiento;
    private int visibilidad;
    private int puntos;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------

    /**
     * Constructor del TDA CiudadRecoleccion
     *
     * PRE:
     * -Filas, columnas, niveles y maximoMochila deben ser mayores a cero
     * -Jugador no debe ser nulo
     *
     * @param filas: Filas del mapa
     * @param columnas: Columnas del mapa
     * @param niveles: Niveles del mapa
     * @param maximoMochila: Cantidad de elementos que guardara la mochila
     * @param jugador: Jugador de la ciudad
     */
    public CiudadRecoleccion(int filas, int columnas, int niveles, int maximoMochila, Jugador jugador) {
        ValidacionesUtiles.validarMayorACero(filas, "filas");
        ValidacionesUtiles.validarMayorACero(columnas, "columnas");
        ValidacionesUtiles.validarMayorACero(niveles, "niveles");
        ValidacionesUtiles.validarMayorACero(maximoMochila, "maximoMochila");
        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");

        setMapa(filas, columnas, niveles);
        setMochila(maximoMochila);
        setJugador(jugador);
        setElementos(maximoMochila);

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
     * Mueve al jugador
     *
     * PRE:
     * -Direccion no debe ser null
     *
     * @param direccion: Direccion en la que el jugador se mueve
     */
    public void moverJugador(Direccion direccion) {
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
        //this.mapa.validarFueraDeRango(nuevaFilaJugador, nuevaColumnaJugador, posicionJugador[2]);
        moverJugadorEnMapa(nuevaFilaJugador, nuevaColumnaJugador, posicionJugador[2]);
        verificarCartasVecinas();
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
     * Aplica el efecto de la carta seleccionada de la mochila
     *
     * PRE:
     * -Opcion no debe ser nulo
     *
     * @param opcion: Carta de la que se quiere aplicar el efecto
     */
    public void usarCartaMochila(int opcion) {
        ValidacionesUtiles.validarMayorACero(opcion, "opcion");

        if(opcion > mochila.getCantidadElementos()){
            throw new RuntimeException("No existe esa carta");
        }

        Elemento carta = this.mochila.getElementoPorPosicion(opcion);
        carta.aplicarEfecto(this);
        this.mochila.eliminarElemento(carta);
    }

    private void sumarPuntosCarta(int[] posicionJugador){
        if (posicionJugador[2] == 1){
            sumarPuntosVision(5000);
        }
        if (posicionJugador[2] == 2){
            sumarPuntosDesplazamiento(3000);
        }
    }

    /**
     * Aumentar la vision del jugador
     */
    public void aumentarVision() {
        this.visibilidad++;
    }
    /**
     * Suma los puntos de la carta visibilidad al encontrarla
     *
     * PRE:
     * -Puntos debe ser mayor a cero
     *
     * @param puntos: Puntos a sumar
     */
    private void sumarPuntosVision(int puntos) {
        ValidacionesUtiles.validarMayorACero(puntos, "puntos");
        setPuntos(this.puntos + puntos);
    }

    /**
     * Aumentar el desplazamiento del jugador
     */
    public void aumentardesplazamiento() {
        this.desplazamiento++;
    }
    /**
     * Suma los puntos de la carta desplazamiento al encontrarla
     *
     * PRE:
     * -Puntos debe ser mayor a cero
     *
     * @param puntos: Puntos a sumar
     */
    private void sumarPuntosDesplazamiento(int puntos) {
        ValidacionesUtiles.validarMayorACero(puntos, "puntos");
        setPuntos(this.puntos + puntos);
    }

    /**
     * Multiplicar puntos
     */
    public void aumentarPuntos(int multiplicador) {
        ValidacionesUtiles.validarMayorACero(multiplicador, "multiplicador");
        setPuntos(this.puntos * multiplicador);

    }


    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    /**
     * Verifica si el jugador se cruzo con alguna carta en el mapa
     */
    public void verificarCartasVecinas() {
        int[] posicionJugador = this.mapa.getPosicionCeldaConContenido(this.jugador);
        Vector<Vector<Celda<?>>> vecinos = this.mapa.getNivel(posicionJugador[2]).getCeldasVecinasRespectoPosicion(posicionJugador[0],  posicionJugador[1], this.visibilidad);

        for (int i = 0; i < vecinos.getLongitud(); i++ ){
            for (int j = 0; j < vecinos.obtener(i).getLongitud(); j++){
                if (vecinos.obtener(i).obtener(j).getContenido() instanceof Elemento) {
                    Elemento carta = ((Elemento) vecinos.obtener(i).obtener(j).getContenido());
                    this.mochila.agregarElemento(carta);
                    sumarPuntosCarta(posicionJugador);
                    validarMochila();
                    if (posicionJugador[2] <= 3){
                        ubicarJugadorEnMapa(1, 1, posicionJugador[2]+1);
                    }
                }
            }
        }
    }

    /**
     *Valida si mochila guarda tres elementos
     */
    public void validarMochila(){
        if (this.mochila.getElementoPorNombre("Carta Puntos") != null){
            this.mochila.getElementoPorNombre("Carta Puntos").aplicarEfecto(this);
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
}
