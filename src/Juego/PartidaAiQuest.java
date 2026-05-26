package Juego;

import java.util.ArrayList;
import java.util.List;

import modelos.Jugador;
import modelos.Partida;
import ordenamientos.Caja;
import ordenamientos.OrdenadorBubble;
import ordenamientos.OrdenadorSelection;
import ordenamientos.PartidaOrdenamientos;
/**
 * clase incompleta:
 * va a representar la partida principal del juego aiquest, maneja el todas las ciudad (las demas partidas)
 */
public class PartidaAiQuest extends Partida {
    private List<Partida> ciudades;
    private int indiceCiudadActual;

    public PartidaAiQuest(Jugador jugador) {
        super("Campaña Global AIQUEST", jugador);
        
        this.ciudades = new ArrayList<>();
        this.indiceCiudadActual = 0;
        
        inicializarCiudades();
    }

    private void inicializarCiudades() {
        // Ciudad 1: Wilde
        ciudades.add(new PartidaOrdenamientos<>(
            "Wilde", 
            getJugador(), // Heredado de Partida
            List.of(new Caja("Caja A", 20), new Caja("Caja B", 50)), 
            new OrdenadorBubble<>("Burbuja")
        ));

        // Ciudad 2: Tokio
        ciudades.add(new PartidaOrdenamientos<>(
            "Tokio", 
            getJugador(), 
            List.of(new Caja("Caja 1", 90), new Caja("Caja 2", 10), new Caja("Caja 3", 40)), 
            new OrdenadorSelection<>("Selección")
        ));
    }

    /**
     * Devuelve la partida específica de la ciudad en la que se está jugando ahora.
     */
    public Partida getPartidaCiudadActual() {
        return ciudades.get(indiceCiudadActual);
    }

    /**
     * Cuando la pantalla avisa que terminó la ciudad, sumamos su puntaje al global
     * y avanzamos el turno del mapa.
     */
    public void avanzarCiudad() {
        Partida ciudadTerminada = getPartidaCiudadActual();
        
        // Sumamos los puntos que hizo en esa ciudad al puntaje acumulado de la PartidaGeneral
        // Nota: Asegurate de tener un método público o protegido en Partida para añadir puntos,
        // o usar lo que maneje tu atributo puntajeActual.
        
        this.indiceCiudadActual++;
    }

    @Override
    public void iniciar() {
        // Inicia el juego global
    }

    @Override
    public void finalizar() {
        // Finaliza el juego completo
    }
}