package juego;

import java.util.ArrayList;
import java.util.List;

import juego.ciudades.ordenamientos.Caja;
import juego.ciudades.ordenamientos.OrdenadorBubble;
import juego.ciudades.ordenamientos.OrdenadorSelection;
import juego.ciudades.ordenamientos.PartidaOrdenamientos;
import juego.ciudades.recoleccionEnMatriz.PartidaDeRecoleccion;
import modelos.Jugador;
import modelos.Partida;

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
        // Ciudad 1: Recoleccion
        ciudades.add(new PartidaDeRecoleccion(
                "Recolección",
                getJugador(),
                Constantes.FILAS_MAPA,
                Constantes.COLUMNAS_MAPA,
                Constantes.NIVELES_MAPA,
                Constantes.CAPACIDAD_MAXIMA_MOCHILA
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
        // Nota: Asegurate de tener un metodo público o protegido en Partida para añadir puntos,
        // o usar lo que maneje tu atributo puntajeActual.
        
        this.indiceCiudadActual++;
    }

    @Override
    public void iniciar() {
        for (Partida ciudad : ciudades) {
            ciudad.iniciar();
        }
    }

    @Override
    public void finalizar() {
        // Finaliza el juego completo
    }
}