package Juego.ciudades.ciudad5;

import java.util.List;
import java.util.Random;

import Juego.ciudades.ciudad5.UI.vistaBusqueda;
import Juego.ciudades.ordenamientos.EstadoDePartida;
import modelos.Jugador;
import modelos.Mapa;
import modelos.Partida;

public class PartidaBusqueda extends Partida {

    private ciudadBusqueda juego;
    private vistaBusqueda  vista;

    /**
     * pre:  nombre y jugador no nulos, mapa no nulo
     * post: crea la partida, indexa el mapa y abre la ventana BMP
     */
    public PartidaBusqueda(String nombre, Jugador jugador, Mapa mapa) {
        super(nombre, jugador);
        this.juego = new ciudadBusqueda(mapa);
        this.vista = new vistaBusqueda();
        setEstado(EstadoDePartida.Creado);
    }

    @Override
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);

        // Palabras reales que vienen del mapa ya indexado
        List<String> palabras = juego.getPalabras();

        if (palabras.isEmpty()) {
            finalizar();
            return;
        }

        Random random = new Random();

        while (getEstado() == EstadoDePartida.Iniciado) {

            // 1. Elegimos una palabra al azar del mapa
            String palabraAleatoria = palabras.get(random.nextInt(palabras.size()));

            // 2. Medimos tiempos en ambas estructuras
            long tiempoLista = juego.medirTiempoPorLista(palabraAleatoria);
            long tiempoArbol = juego.medirTiempoPorArbol(palabraAleatoria);

            // 3. Mostramos la ronda y esperamos que el jugador elija
            String respuesta = vista.mostrarRondaYEsperarRespuesta(
                    palabraAleatoria, tiempoLista, tiempoArbol);

            // 4. El jugador eligió salir
            if (respuesta.equals("SALIR")) {
                finalizar();
                continue;
            }

            // 5. Determinamos la respuesta correcta
            String correcta = tiempoArbol < tiempoLista ? "ARBOL" : "LISTA";
            boolean acerto  = respuesta.equals(correcta);

            // 6. Mostramos si acertó o no
            vista.mostrarFeedback(acerto, correcta);

            // 7. Verificamos si el jugador ya ganó
            if (vista.estaGanada()) {
                vista.mostrarVictoria();
                finalizar();
            }
        }
    }

    @Override
    public void finalizar() {
        setEstado(EstadoDePartida.Creado);
        setPuntaje(calcularPuntaje());
        //falta completar para cerrar la ventana y volver a partida aiquest
    }

    /**
     * post: devuelve 100 si ganó, 0 si salió antes
     */
    private int calcularPuntaje() {
        return vista.estaGanada() ? 100 : 0;
    }
}