package ciudad_3_laberinto.src;

import java.util.List;

import Juego.ordenamientos.EstadoDePartida;

public class BacktrackingLaberinto {
    private Laberinto laberinto;
    private PilaLaberinto<Celda> pila;

    private boolean terminado;
    private ResultadoPaso resultado;

    public BacktrackingLaberinto(Laberinto laberinto) {
        this.laberinto = laberinto;
        this.pila = new PilaLaberinto<>();
        this.terminado = false;
        this.resultado = ResultadoPaso.EN_PROGRESO;
        inicializar();
    }

    private void inicializar() {
        Celda celdaInicio = laberinto.getCeldaInicio();
        pila.apilar(celdaInicio);
    }

    public ResultadoPaso avanzarPaso() {
        if (terminado) {
            return resultado;
        }

        if (pila.estaVacia()) {
            terminado = true;
            resultado = ResultadoPaso.SIN_SOLUCION;
            return resultado;
        }

        Celda celdaActual = pila.verCima();

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
    
    private Celda obtenerPrimerVecino(Celda celda) {
        List<Celda> vecinos = laberinto.obtenerCeldasTransitables(celda);
        if (vecinos.isEmpty()) {
            return null;
        }
        return vecinos.get(0);
    }

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
        PilaLaberinto<Celda> pilaAux = new PilaLaberinto<>();

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

        // Marcar tambien el FIN
        //laberinto.getCeldaFin().setEstadoCelda(EstadoCelda.SOLUCION);
    }

    public boolean isTerminado() {
        return terminado;
    }

    public ResultadoPaso getResultado() {
        return resultado;
    }

    public PilaLaberinto<Celda> getPila() {
        return pila;
    }
}