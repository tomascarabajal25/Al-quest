package juego.ciudades.recoleccionEnMatriz;

import modelos.*;
import juego.ciudades.recoleccionEnMatriz.ui.CartaVista;
import utils.ValidacionesUtiles;
import juego.configuracion.ConfiguracionDeRecoleccion;
import juego.ciudades.recoleccionEnMatriz.ui.CartaDesplazamientoVista;
import juego.ciudades.recoleccionEnMatriz.ui.CartaPuntosVista;
import juego.ciudades.recoleccionEnMatriz.ui.CartaVisionVista;
import estructuras.vector.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private Elemento cartaDisponible = null;
    private int desplazamiento;
    private int visibilidad;
    private int puntos;
    private int nivelActual;
    private String ultimoMensaje = null;
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

        setDesplazamiento(ConfiguracionDeRecoleccion.DESPLAZAMIENTO_INICIAL);
        setVisibilidad(ConfiguracionDeRecoleccion.VISIBILIDAD_INICIAL);
        setPuntos(ConfiguracionDeRecoleccion.PUNTOS_INICIALES_PARTIDA);
        iniciar();
    }
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------

    /**
     * Metodo equals del TDA CiudadRecoleccion.
     * @param o   the reference object with which to compare.
     * @return: true si son iguales, false si no lo son.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CiudadRecoleccion that = (CiudadRecoleccion) o;
        return desplazamiento == that.desplazamiento && visibilidad == that.visibilidad && Objects.equals(mapa, that.mapa) && Objects.equals(mochila, that.mochila) && Objects.equals(elementos, that.elementos) && Objects.equals(jugador, that.jugador);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mapa, mochila, elementos, jugador, desplazamiento, visibilidad);
    }

    @Override
    public String toString() {
        return "CiudadRecoleccion{" +
                "mapa=" + mapa +
                ", mochila=" + mochila +
                ", elementos=" + elementos +
                ", jugador=" + jugador +
                ", estado=" + estado +
                ", cartaDisponible=" + cartaDisponible +
                ", desplazamiento=" + desplazamiento +
                ", visibilidad=" + visibilidad +
                ", puntos=" + puntos +
                ", ultimoMensaje='" + ultimoMensaje + '\'' +
                '}';
    }

    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------

    /**
     * Establece los recursos en su estado inicial para comenzar el juego
     */
    public void iniciar() {
        ubicarElementosEnMapa();
        ubicarJugadorEnMapa(1, 1, 1);
        setEstado(EstadoDeJuego.COMENZADO);
    }

    /**
     * Finaliza el juego de la ciudad
     *
     * @return: Devuelve los puntos acumulados
     */
    public int finalizar() {
        return this.puntos;
    }
    public List<CartaVista> getCartasVista(int tamaño) {
        List<CartaVista> resultado = new ArrayList<>();

        // mapa es Mapa3D: iterar cada nivel, y dentro de él cada fila/columna.
        // getAncho()/getAlto() los tiene Mapa (cada nivel), no Mapa3D.
        for (int nivel = 1; nivel <= mapa.getNiveles(); nivel++) {
            modelos.Mapa mapaDeNivel = mapa.getNivel(nivel);
            for (int fila = 1; fila <= mapaDeNivel.getAncho(); fila++) {
                for (int col = 1; col <= mapaDeNivel.getAlto(); col++) {
                    Celda<?> celda = mapaDeNivel.getCeldaConPosicion(fila, col);
                    if (celda == null || celda.getContenido() == null) continue;

                    Object contenido = celda.getContenido();
                    // Se pasa el nivel para que ElementoVista.draw(g2,vista,nivel) filtre correctamente
                    if (contenido instanceof CartaPuntos c)
                        resultado.add(new CartaPuntosVista(c, col, fila, nivel, tamaño));
                    else if (contenido instanceof CartaVision c)
                        resultado.add(new CartaVisionVista(c, col, fila, nivel, tamaño));
                    else if (contenido instanceof CartaDesplazamiento c)
                        resultado.add(new CartaDesplazamientoVista(c, col, fila, nivel, tamaño));
                }
            }
        }
        return resultado;
    }

    /**
     * Ubica los elementos carta dentro del mapa.
     *
     * PRE:  mapa y elementos ya inicializados.
     * POST: cada carta queda en una celda válida dentro de sus respectivos niveles,
     *       calculando las posiciones como proporciones del tamaño real del mapa
     *       para que nunca superen getAncho() / getAlto() sin importar las dimensiones
     *       que el usuario haya ingresado.
     *
     * Distribución proporcional:
     *   - Carta 1 (nivel 1): fila ≈ 75% del alto, col ≈ 75% del ancho
     *   - Carta 2 (nivel 2, si existe): fila ≈ 50% del alto, col ≈ 50% del ancho
     *   - Carta 3 (nivel 3, si existe): fila ≈ 25% del alto, col ≈ 25% del ancho
     */
    public void ubicarElementosEnMapa() {
        this.mapa.ocuparCelda(this.elementos.obtener(1), 5, 5, 1);
        this.mapa.ocuparCelda(this.elementos.obtener(2), 38, 25, 2);
        this.mapa.ocuparCelda(this.elementos.obtener(3), 19, 40, 3);
    }

    /**
     * Ubica al jugador en la posicion dada
     *
     * PRE:
     * -Fila, columna y nivel deben ser mayores a 0
     */
    public void ubicarJugadorEnMapa(int fila, int columna, int nivel) {
        ValidacionesUtiles.validarMayorACero(nivel, "nivel");
        this.nivelActual = nivel;
        this.mapa.ocuparCelda(this.jugador, fila, columna, nivel);
    }

    /**
     * Mueve al jugador
     *
     * PRE:
     * -Direccion no debe ser null
     *
     * @param opcion: Direccion en la que el jugador se mueve
     */
    public void moverJugador(char opcion) {
        ValidacionesUtiles.esDistintoDeNull(opcion, "opcion");

        int[] posicionJugador = this.mapa.getPosicionCeldaConContenido(this.jugador);
        int nuevaFilaJugador = posicionJugador[0];
        int nuevaColumnaJugador = posicionJugador[1];

        switch (opcion) {
            case 'W':
                nuevaFilaJugador -= desplazamiento;
                break;
            case 'S':
                nuevaFilaJugador += desplazamiento;
                break;
            case 'A':
                nuevaColumnaJugador -= desplazamiento;
                break;
            case 'D':
                nuevaColumnaJugador += desplazamiento;
                break;
        }

        // Verifica que, cuando el desplazamiento es mayor a 1, el jugador no quede
        // en una posicion fuera de rango con un movimiento
        Mapa nivelActual = this.mapa.getNivel(posicionJugador[2]);
        nuevaFilaJugador    = Math.max(1, Math.min(nuevaFilaJugador,    nivelActual.getAncho()));
        nuevaColumnaJugador = Math.max(1, Math.min(nuevaColumnaJugador, nivelActual.getAlto()));

        moverJugadorEnMapa(nuevaFilaJugador, nuevaColumnaJugador, posicionJugador[2]);
        this.nivelActual = posicionJugador[2];
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

        // Verificar si la celda destino tiene una carta
        Celda<?> celdaDestino = this.mapa.getNivel(nivel).getCeldaConPosicion(fila, columna);
        if (celdaDestino != null && celdaDestino.getContenido() instanceof Elemento) {
            return; // no se mueve, la carta bloquea el paso
        }

        // Borrar jugador de la posición anterior
        int[] posAnterior = this.mapa.getPosicionCeldaConContenido(this.jugador);
        if (posAnterior != null) {
            this.mapa.VaciarCelda(posAnterior[0], posAnterior[1], posAnterior[2]);
        }

        ubicarJugadorEnMapa(fila, columna, nivel);
    }

    /**
     * Sube de nivel al jugador
     */
    private void avanzarNivel() {
        if (this.nivelActual < this.mapa.getNiveles()) {
            this.nivelActual++;

            int[] spawn = getPosicionSpawnSiguienteNivel();
            moverJugadorEnMapa(
                    spawn[0],
                    spawn[1],
                    this.nivelActual
            );
        }
    }

    public void recogerCarta() {
        if (this.cartaDisponible == null) return;

        int[] posicionJugador = this.mapa.getPosicionCeldaConContenido(this.jugador);

        // Vaciar la celda de la carta
        int[] posCarta = this.mapa.getPosicionCeldaConContenido(this.cartaDisponible);
        if (posCarta != null) {
            this.mapa.VaciarCelda(posCarta[0], posCarta[1], posCarta[2]);
        }

        procesarCarta(this.cartaDisponible);
        this.mochila.agregarElemento(this.cartaDisponible);
        sumarPuntosCarta(posicionJugador);
        validarMochila();

        if (posicionJugador[2] < this.mapa.getNiveles()) {
            avanzarNivel();
        }

        this.cartaDisponible = null;
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

        if (opcion > mochila.getCantidadElementos()) {
            throw new RuntimeException("No existe esa carta");
        }

        Elemento carta = this.mochila.getElementoPorPosicion(opcion);
        carta.aplicarEfecto(this);
        this.mochila.eliminarElemento(carta);
    }

    /**
     * Suma los puntos correspondientes segun el nivel donde se encontro la carta
     *
     * @param posicionJugador: Posicion actual del jugador [fila, columna, nivel]
     */
    private void sumarPuntosCarta(int[] posicionJugador) {
        if (posicionJugador[2] == 1) {
            sumarPuntosVision(ConfiguracionDeRecoleccion.PUNTAJE_VISIBILIDAD);
        }
        if (posicionJugador[2] == 2) {
            sumarPuntosDesplazamiento(ConfiguracionDeRecoleccion.PUNTAJE_DESPLAZAMIENTO);
        }
    }

    /**
     * Aumentar la vision del jugador
     */
    public void aumentarVision() {
        this.visibilidad += ConfiguracionDeRecoleccion.CANTIDAD_AUMENTO_VISIBILIDAD;
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
        this.desplazamiento += ConfiguracionDeRecoleccion.CANTIDAD_AUMENTO_DESPLAZAMIENTO;
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
    public void aumentarPuntos() {
        setPuntos(this.puntos * ConfiguracionDeRecoleccion.CANTIDAD_AUMENTO_PUNTOS);
    }

    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------

    /**
     * Verifica si el jugador se cruzo con alguna carta en el mapa
     */
    public void verificarCartasVecinas() {
        this.cartaDisponible = null; // resetear cada vez que se mueve

        int[] posicionJugador = this.mapa.getPosicionCeldaConContenido(this.jugador);
        // Siempre buscar adyacentes con distancia 1, independientemente de visibilidad
        Vector<Vector<Celda<?>>> adyacentes = this.mapa.getNivel(posicionJugador[2])
                .getCeldasVecinasRespectoPosicion(posicionJugador[0], posicionJugador[1], 1);

        for (int i = 1; i <= adyacentes.getLongitud(); i++) {
            for (int j = 1; j <= adyacentes.obtener(i).getLongitud(); j++) {
                Celda<?> celda = adyacentes.obtener(i).obtener(j);
                if (celda != null && celda.getContenido() instanceof Elemento) {
                    this.cartaDisponible = (Elemento) celda.getContenido();
                    return;
                }
            }
        }
    }

    /**
     * Procesa la carta, identifica su tipo y guarda el mensaje
     * correspondiente para que la GUI lo muestre.
     *
     * PRE:
     * -La carta no debe ser null
     *
     * @param carta: Carta a procesar
     */
    public void procesarCarta(Elemento carta) {
        ValidacionesUtiles.esDistintoDeNull(carta, "carta");

        if (carta instanceof CartaVision cartaV) {
            this.ultimoMensaje = "¡Carta encontrada! " + cartaV.getNombre() + " - " + cartaV.getDescripcion();
        }
        else if (carta instanceof CartaDesplazamiento cartaD) {
            this.ultimoMensaje = "¡Carta encontrada! " + cartaD.getNombre() + " - " + cartaD.getDescripcion();
        }
        else if (carta instanceof CartaPuntos cartaP) {
            this.ultimoMensaje = "¡Carta encontrada! " + cartaP.getNombre() + " - " + cartaP.getDescripcion();
        }
    }

    /**
     * Valida si la mochila contiene la Carta Puntos y finaliza el juego
     */
    public void validarMochila() {
        if (this.mochila.getElementoPorNombre("Carta Puntos") != null) {
            this.mochila.getElementoPorNombre("Carta Puntos").aplicarEfecto(this);
            setEstado(EstadoDeJuego.FINALIZADO);
        }
    }

    /**
     * Limpia el ultimo mensaje para que no se muestre dos veces.
     */
    public void limpiarUltimoMensaje() {
        this.ultimoMensaje = null;
    }

    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Devuelve la posicion actual del jugador en el mapa 3D.
     * [0]=fila, [1]=columna, [2]=nivel
     *
     * @return int[3] con la posicion, o null si no se encontro
     */
    public int[] getPosicionJugador() {
        return this.mapa.getPosicionCeldaConContenido(this.jugador);
    }

    /**
     * Devuelve el Mapa (2D) correspondiente a un nivel dado.
     *
     * @param nivel nivel a obtener (1-based)
     * @return Mapa del nivel
     */
    public modelos.Mapa getMapaNivel(int nivel) {
        return this.mapa.getNivel(nivel);
    }

    /**
     * Devuelve los puntos actuales del juego.
     */
    public int getPuntos() {
        return this.puntos;
    }

    /**
     * Devuelve la visibilidad actual del jugador.
     */
    public int getVisibilidad() {
        return this.visibilidad;
    }

    /**
     * Devuelve el desplazamiento actual del jugador.
     */
    public int getDesplazamiento() {
        return this.desplazamiento;
    }

    /**
     * Devuelve la cantidad de niveles del mapa.
     */
    public int getNiveles() {
        return this.mapa.getNiveles();
    }

    /**
     * Devuelve true si el juego esta en estado FINALIZADO.
     */
    public boolean estaFinalizado() {
        return this.estado == EstadoDeJuego.FINALIZADO;
    }

    /**
     * Devuelve los elementos actualmente en la mochila.
     *
     * @return ListaSimplementeEnlazada con los elementos de la mochila
     */
    public estructuras.listas.ListaSimplementeEnlazada<modelos.Elemento> getItemsMochila() {
        return this.mochila.getElementos();
    }

    /**
     * Devuelve la carta disponible para guardar en la mochila
     *
     * @return: Carta disponible en el mapa
     */
    public Elemento getCartaDisponible() {
        return this.cartaDisponible;
    }

    /**
     * Devuelve el ultimo mensaje generado por procesarCarta().
     * La GUI lo consume y lo muestra en pantalla.
     * Despues de leerlo se recomienda llamar a limpiarUltimoMensaje().
     *
     * @return String con el mensaje, o null si no hay mensaje pendiente
     */
    public String getUltimoMensaje() {
        return this.ultimoMensaje;
    }

    /**
     * Getter de posicion de spawn del jugador
     * @return: Devuelve la posicion inicial del jugador
     */
    public int[] getPosicionSpawnSiguienteNivel() {
        return new int[]{1, 1}; // fila, columna del spawn
    }

    /**
     * Getter del atributo nivelActual
     * @return: Devuelve el valor del atributo
     * @return
     */
    public int getNivelActual() {
        return this.nivelActual;
    }


    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------

    /**
     * Setter del atributo mapa
     *
     * PRE:
     * -Los atributos filas, columnas y niveles deben ser mayores a cero
     *
     * @param filas: Filas del mapa
     * @param columnas: Columnas del mapa
     * @param niveles: Niveles del mapa
     */
    private void setMapa(int filas, int columnas, int niveles) {
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
    private void setMochila(int maximoMochila) {
        ValidacionesUtiles.validarMayorACero(maximoMochila, "maximoMochila");
        mochila = new Mochila(maximoMochila);
    }

    /**
     * Setter del atributo jugador
     *
     * PRE:
     * -El jugador no puede ser null
     *
     * @param jugador: Jugador de la ciudad
     */
    private void setJugador(Jugador jugador) {
        ValidacionesUtiles.esDistintoDeNull(jugador, "jugador");
        this.jugador = jugador;
    }

    /**
     * Setter de los elementos
     *
     * PRE:
     * -El parametro maximoMochila debe ser mayor a cero
     * -Los elementos que se guarden no deben ser null
     *
     * @param maximo: Cantidad de elementos que guardara la mochila
     */
    private void setElementos(int maximo) {
        ValidacionesUtiles.validarMayorACero(maximo, "maximo");

        CartaVision cartaVision = new CartaVision(
                "Carta Vision",
                "Esta carta tiene el efecto de aumentar la visibilidad en una celda",
                5000);
        CartaDesplazamiento cartaDesplazamiento = new CartaDesplazamiento(
                "Carta Desplazamiento",
                "Esta carta tiene el efecto de aumentar el desplazamiento en una celda",
                2000);
        CartaPuntos cartaPuntos = new CartaPuntos(
                "Carta Puntos",
                "Esta carta tiene el efecto de aumentar exponencialmente los puntos");

        this.elementos = new Vector<>(3, null);          // datoInicial = null
        this.elementos.agregar(1, cartaVision);           // posición 1 → CartaVision
        this.elementos.agregar(2, cartaDesplazamiento);   // posición 2 → CartaDesplazamiento
        this.elementos.agregar(3, cartaPuntos);           // posición 3 → CartaPuntos
    }

    /**
     * Setter del atributo desplazamiento
     *
     * PRE:
     * -El desplazamiento debe ser mayor a cero
     */
    private void setDesplazamiento(int desplazamiento) {
        ValidacionesUtiles.validarMayorACero(desplazamiento, "desplazamiento");
        this.desplazamiento = desplazamiento;
    }

    /**
     * Setter del atributo visibilidad
     *
     * PRE:
     * -La visibilidad debe ser mayor a cero
     */
    private void setVisibilidad(int visibilidad) {
        ValidacionesUtiles.validarMayorACero(visibilidad, "visibilidad");
        this.visibilidad = visibilidad;
    }

    /**
     * Setter del atributo puntos
     *
     * PRE:
     * -Puntos debe ser mayor o igual a cero
     */
    private void setPuntos(int puntos) {
        ValidacionesUtiles.validarMayorOIgualACero(puntos, "puntos");
        this.puntos = puntos;
    }

    /**
     * Setter del atributo estado
     *
     * PRE:
     * -Estado no debe ser null
     */
    private void setEstado(EstadoDeJuego estado) {
        ValidacionesUtiles.esDistintoDeNull(estado, "estado");
        this.estado = estado;
    }

 // En CiudadRecoleccion.java
    public void actualizarPosicionJugador(int colVisual, int filaVisual) {
        // El mapa lógico puede ser más chico que el mundo visual.
        // Clampear para no salir de rango.
        modelos.Mapa nivel1 = this.mapa.getNivel(1);
        
        // IMPORTANTE: moverJugadorEnMapa espera (fila, columna) — no al revés
        int filaLogica = Math.max(1, Math.min(filaVisual, nivel1.getAncho()));
        int colLogica  = Math.max(1, Math.min(colVisual,  nivel1.getAlto()));
        
        moverJugadorEnMapa(filaLogica, colLogica, 
                           obtenerNivelActual()); // en vez de hardcodear 1
        verificarCartasVecinas();
    }

    // Helper para no perder el nivel actual del jugador al sincronizar
    private int obtenerNivelActual() {
        int[] pos = this.mapa.getPosicionCeldaConContenido(this.jugador);
        return (pos != null) ? pos[2] : 1;
    }
}