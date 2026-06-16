package juego.ciudades.ciudad_3_laberinto.src;

import java.util.List;

import estructuras.pilas.PilaBasica;

/**
 * Implementacion del algoritmo de backtracking con pila
 * Resuelve el laberinto paso a paso, ejecutando una sola
 * accion, permitiendo visualizar el proceso detenidamente.
 */
public class BacktrackingLaberinto {
    private Laberinto laberinto;
    private PilaBasica<Celda> pila;

    private boolean terminado;
    private ResultadoPaso resultado;

    public BacktrackingLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;
        this.pila = new PilaBasica<>();
        this.terminado = false;
        this.resultado = ResultadoPaso.EN_PROGRESO;
        inicializar();
    }

    private void inicializar() {
        Celda celdaInicio = laberinto.getCeldaInicio();
        pila.apilar(celdaInicio);
    }

    /**
     * Avanza un paso en la resolucion del laberinto.
     * Debe llamarse repetidamente hasta obtener un resultado distinto de EN_PROGRESO.
     * @return el resultado del paso actual
     */
    public ResultadoPaso avanzarPaso() {
        if (terminado) {
            return resultado;
        }

        if (pila.estaVacia()) {
            terminado = true;
            resultado = ResultadoPaso.SIN_SOLUCION;
            return resultado;
        }

        Celda celdaActual = pila.obtener();

        // Verificar si llegamos al fin
        if (celdaActual.getEstadoCelda() == EstadoCelda.FIN 
            || esAdyacenteAlFin(celdaActual)) {
            marcarSolucion();
            terminado = true;
            resultado = ResultadoPaso.SOLUCION_ENCONTRADA;
            return resultado;
        }

        // Buscar un vecino transitable
        Celda vecino = obtenerPrimerVecino(celdaActual);

        if (vecino != null) {
            // Avanzar hacia el vecino
            vecino.setEstadoCelda(EstadoCelda.EN_CAMINO);
            pila.apilar(vecino);
        } else {
            // No hay vecinos, retroceder
            pila.desapilar();
            // No descartar el inicio
            if (celdaActual.getEstadoCelda() != EstadoCelda.INICIO) {
                celdaActual.setEstadoCelda(EstadoCelda.DESCARTADA);
            }
        }

        return ResultadoPaso.EN_PROGRESO;
    }
    /**
     * Obtiene la primera celda a visitar
     * @param celda recibe la celda actual como parametro
     * @return la celda vecina
     */
    private Celda obtenerPrimerVecino(Celda celda) {
        List<Celda> vecinos = laberinto.obtenerCeldasTransitables(celda);
        if (vecinos.isEmpty()) {
            return null;
        }
        return vecinos.get(0);
    }

    /**
     * Busca si alguna celda adyacente es la casilla FIN
     * para determinar si ya se llego a la solucion.
     * @param celda
     * @return true si la celda es adyacente.
     */
    private boolean esAdyacenteAlFin(Celda celda) {
        List<Celda> vecinos = laberinto.obtenerCeldasTransitables(celda);
        for (Celda vecino : vecinos) {
            if (vecino.getEstadoCelda() == EstadoCelda.FIN) {
                return true;
            }
        }
        return false;
    }

    private void marcarSolucion() {
        PilaBasica<Celda> pilaAux = new PilaBasica<>();

        // Pasar todo a la pila auxiliar para no perder el orden
        while (!pila.estaVacia()) {
            pilaAux.apilar(pila.desapilar());
        }

        // Marcar cada celda como SOLUCION
        while (!pilaAux.estaVacia()) {
            Celda celda = pilaAux.desapilar();
            if (celda.getEstadoCelda() == EstadoCelda.INICIO) {
                break;
            } else {
                celda.setEstadoCelda(EstadoCelda.SOLUCION);
            }
            
        }
    }

    /**
     * Indica si el algoritmo termino, ya sea con solucion o sin ella.
     * @return true si el algoritmo termino
     */
    public boolean isTerminado() {
        return terminado;
    }

    /**
     * Retorna el resultado actual del algoritmo.
     * @return EN_PROGRESO, SOLUCION_ENCONTRADA o SIN_SOLUCION
     */
    public ResultadoPaso getResultado() {
        return resultado;
    }

    /**
     * Retorna la pila del camino actual.
     * La cima de la pila es siempre la celda donde esta el jugador.
     * @return pila con el camino actual
     */
    public PilaBasica<Celda> getPila() {
        return pila;
    }
}