package juego.ciudades.hashing;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import juego.ciudades.hashing.ui.FabricaMinijuegoHashing;
import juego.ciudades.hashing.ui.MinijuegoHashing;
import juego.ciudades.ordenamientos.EstadoDePartida;
import modelos.Jugador;
import modelos.Partida;
import modelosVista.Vista;
import utils.ValidacionesUtiles;

/**
 * Partida de la ciudad 6 Hashing dentro del mundo 2D.
 * 
 * Funcionamiento:
 * -iniciar() crea la CiudadHashing, monta el MinijuegoHashing en la Vista,
 *  abre la ventana y arranca el hilo del juego.
 * -El minijuego avisa la victoria mediante onVictoria,
 *  de ahi se suma el puntaje y se finaliza la partida.
 * 
 */

public class PartidaHashing extends Partida {
    //CONSTANTES
    private static final int PUNTAJE_VICTORIA = 100; //Puntaje que otorga al ganar la ciudad
    private static final String TITULO_VENTANA = "Ciudad de Hashing";
    private static final int FILA_SPAWN = 48;
    private static final int COL_SPAWN  = 1;
    private static final String RUTA_MAPA    = "/maps/world_hashing.txt";
    private static final String RUTA_SPRITES = "/assets/jugador/boy";


    //ATRIBUTOS    
    private final int cantidadSlots; //Cantidad de slots de la tabla (numero primo es mejor)
    private final List<ElementoHash> elementos; //Elementos que el jugador debe insertar
    private final List<Integer> clavesABuscar; //Claves que jugador va a buscar luego de insertar
    private final List<Point> posicionesSlots; //Posiciones donde se reparten los slots por el mapa


    /**
     * Logica de la ciudad y minijuego activos
     */
    private CiudadHashing ciudad;
    private MinijuegoHashing minijuego;
    private Vista vista;
    private JFrame ventana;



    //CONSTRUCTORES
    /**
     * PRE:
     * @param nombreCiudad    no nulo, de al menos 2 caracteres
     * @param jugador         no nulo
     * @param cantidadSlots   mayor a 0
     * @param elementos       no nula, al menos 1 elemento a insertar
     * @param clavesABuscar   no nula (puede estar vacia)
     * @param posicionesSlots no nula, una posicion (x = columna, y = fila, en celdas) por cada slot
     *
     * POST: crea la partida en estado "Creado", sin iniciarla todavia.
     */
    public PartidaHashing(String nombreCiudad, Jugador jugador, int cantidadSlots,
                          List<ElementoHash> elementos, List<Integer> clavesABuscar,
                          List<Point> posicionesSlots) {
    
        super(nombreCiudad, jugador);

        ValidacionesUtiles.validarMayorACero(cantidadSlots, "cantidad de slots");
        ValidacionesUtiles.esDistintoDeNull(elementos, "elementos");
        ValidacionesUtiles.esDistintoDeNull(clavesABuscar, "claves a buscar");
        ValidacionesUtiles.esDistintoDeNull(posicionesSlots, "posiciones de los slots");

        if (elementos.isEmpty()) {
            throw new IllegalArgumentException("ERROR: se necesita al menos 1 elemento para insertar.");
        }

        this.cantidadSlots   = cantidadSlots;
        this.elementos       = new ArrayList<>(elementos);
        this.clavesABuscar   = new ArrayList<>(clavesABuscar);
        this.posicionesSlots = new ArrayList<>(posicionesSlots);
    }



    //METODOS DE COMPORTAMIENTO
    /**
     * PRE: la partida no tiene que estar iniciada. 
     * POST: crea la ciudad y el minijuego, los monta en la Vista, abre la ventana
     *       y arranca el hilo del juego.
     */
    @Override
    public void iniciar() {
        ValidacionesUtiles.validarFalso(estaIniciada(), "La partida ya ha sido iniciada");
        setEstado(EstadoDePartida.Iniciado);

        this.vista = new Vista(RUTA_MAPA, getJugador(), COL_SPAWN, FILA_SPAWN, RUTA_SPRITES);

        this.ciudad = new CiudadHashing(cantidadSlots);

        this.minijuego = FabricaMinijuegoHashing.crear(vista, ciudad, elementos, clavesABuscar, posicionesSlots);

        // Cuando el jugador gane, esta partida suma puntaje y se finaliza
        minijuego.setOnVictoria(() -> {
            setPuntaje(PUNTAJE_VICTORIA);
            finalizar();
        });

        
        // Ventana del juego
        ventana = new JFrame();
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setTitle(TITULO_VENTANA);
        ventana.add(vista);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);
        vista.requestFocusInWindow();

        // Arranca el bucle del juego (run -> actualizar -> repaint a 60 fps)
        vista.startGameThread();
    }


    /**
     * PRE: la partida debe estar iniciada
     * POST: detiene el hilo del juego y vuelve el estado a "Creado"
     */
    @Override
    public void finalizar() {
        ValidacionesUtiles.validarVerdadero(estaIniciada(), getNombre());
        setEstado(EstadoDePartida.Creado);
        if (vista != null) {
            vista.detenerHilo();
        }
        if (ventana != null) {
        ventana.dispose();
        ventana = null;
        }
        
        notificarFinalizacion();
    }


    //GETTERS
    public CiudadHashing getCiudad() { //Devuelve la logica de la ciudad (null si todavia no inicio)
        return this.ciudad;
    }

    public MinijuegoHashing getMinijuego() {
        return this.minijuego; //Devuelve el minijuego activo (null si todavia no inicio)
    }


    //METODOS GENERALES
    @Override
    public String toString() {
        return "PartidaHashing [slots=" + cantidadSlots + ", elementos=" + elementos.size()
                + ", clavesABuscar=" + clavesABuscar.size() + "]";
    }



    //SETTERS PRIVADOS
    private void setVista(Vista vista) {
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");
        this.vista = vista;
    }



}
