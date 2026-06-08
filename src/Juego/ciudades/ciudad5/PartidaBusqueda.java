package Juego.ciudades.ciudad5;

import javax.swing.JFrame;

import Juego.ciudades.ciudad5.UI.MinijuegoDesafio;
import Juego.ciudades.ordenamientos.EstadoDePartida;
import modelos.Jugador;
import modelos.Mapa;
import modelos.Partida;
import modelosVista.Vista;

public class PartidaBusqueda extends Partida {

    private Vista    vista;
    private JFrame   ventana;
    private MinijuegoDesafio minijuego;

    /**
     * pre:  nombre y jugador no nulos, mapa no nulo
     * post: crea la partida e indexa el mapa.
     *       La ventana y el bucle se crean en iniciar().
     */
    public PartidaBusqueda(String nombre, Jugador jugador, Mapa mapa) {
        super(nombre, jugador);

        // Crear vista y minijuego (sin arrancar el hilo todavía)
        this.vista     = new Vista("/maps/world01.txt", getJugador(), 24,21,"/assets/jugador/boy");
        this.minijuego = new MinijuegoDesafio(mapa, vista.tamaño);
        vista.setMinijuego(minijuego);
        vista.adminObjt.setObjetos(minijuego.getPuertaLista(),
                                   minijuego.getPuertaArbol());
        // Cuando el minijuego termine, llamar a finalizar() automáticamente
        minijuego.setOnFinalizadoCallback(this::finalizar);

        setEstado(EstadoDePartida.Creado);
    }

    /**
     * post: abre la ventana, arranca el bucle del juego y cambia el estado.
     *       El control vuelve inmediatamente al llamador;
     *       el juego corre en su propio hilo (igual que en Princi).
     */
    @Override
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);

        // Igual que Princi.main() — la ventana vive aquí en vez de en main
        ventana = new JFrame();
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setTitle("Ciudad de Búsqueda");
        ventana.add(vista);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        // Arranca el bucle (run → actualizar → repaint a 60 fps)
        vista.startGameThread();
    }

    /**
     * post: cierra la ventana, detiene el hilo y guarda el puntaje.
     *       Puede llamarse desde el callback del minijuego o desde afuera.
     */
    @Override
    public void finalizar() {
        setEstado(EstadoDePartida.Creado);
        setPuntaje(calcularPuntaje());

        // Detener el hilo del juego
        vista.detenerHilo();

        // Cerrar la ventana
        if (ventana != null) {
            ventana.dispose();
            ventana = null;
        }

        // TODO: notificar a la partida contenedora que esta partida terminó
    }

    /**
     * post: devuelve 100 si ganó todas las rondas, 0 si salió antes.
     */
    private int calcularPuntaje() {
        return minijuego != null && minijuego.isGanado() ? 100 : 0;
    }
}