package modelos;


import javax.swing.JFrame;

import javax.swing.SwingUtilities;

import juego.ciudades.ordenamientos.PartidaOrdenamientos;
import juego.ciudades.recoleccionEnMatriz.PartidaDeRecoleccion;
import juego.ciudades.reinas.PartidaReinas;
import juego.ciudades.ciudad5.PartidaBusqueda;
import juego.ciudades.torresDeHanoi.PartidaHanoi;


import modelosVista.VistaGlobal;
import juego.ciudades.ordenamientos.EstadoDePartida;


/**
 * TDA PartidaGeneral — orquestador central de Al-Quest.
 *
 * Responsabilidades:
 *  - Construir el grafo de ciudades con sus conexiones dirigidas.
 *  - Gestionar el ciclo de vida de las sub-partidas mediante Callbacks.
 *  - Mantener el puntaje global acumulado.
 *  - Coordinar la transición entre el mapa global y cada sub-partida.
 *
 * Patrón de comunicación: Callback funcional (Runnable).
 *   Cada sub-partida recibe un lambda () -> alTerminarCiudad(id).
 *   La sub-partida no conoce a PartidaGeneral: desacoplamiento total.
 *
 * REGLA DE ORO — Modularización:
 *   La lógica del grafo (accesibilidad, caminos) reside en GrafoCiudades.
 *   PartidaGeneral solo coordina ventanas, hilos y callbacks.
 *
 * REGLA DE ORO — Gestión de memoria:
 *   Cada sub-partida libera su hilo y ventana en su propio finalizar().
 *   PartidaGeneral solo reactiva la ventana global.
 */
public class PartidaGeneral extends Partida {

    // ── Constantes ────────────────────────────────────────────────────────────

    /** Ruta relativa del mapa de tiles mundial. Sin rutas absolutas (Regla de Oro). */
    private static final String RUTA_MAPA_GLOBAL    = "/maps/mapa_global.txt";

    /** Ruta relativa de los sprites del jugador en el mapa global. */
    private static final String RUTA_SPRITE_JUGADOR = "/assets/jugador/boy";

    // ── Atributos ─────────────────────────────────────────────────────────────

    /** Grafo dirigido con los nodos-ciudad del mundo. */
    private final GrafoCiudades mapaMundi;

    /** Ciudad en la que el jugador está actualmente (null = mapa global). */
    private NodoCiudad ciudadActual;

    /** Ventana principal del mapa mundial. */
    private JFrame ventanaGlobal;

    /**
     * Vista global del mapa mundial (loop 60 FPS + detección de ciudades).
     * Se usa VistaGlobal en lugar de Vista para acceder a la lógica de
     * colisión con ciudades sin romper la separación de capas.
     */
    private VistaGlobal vistaGlobal;

    /** Puntaje acumulado a lo largo de todas las ciudades completadas. */
    private int puntajeTotal;
 
    
    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * PRE:  jugador != null.
     * POST: grafo construido con las ciudades activas y sus callbacks configurados.
     *       No se crea ninguna ventana ni hilo hasta llamar a iniciar().
     *
     * @param jugador el jugador que persistirá durante toda la sesión de juego
     */
    public PartidaGeneral(Jugador jugador) {
        super("Al-Quest — Mapa Mundial", jugador);
        this.mapaMundi    = new GrafoCiudades();
        this.puntajeTotal = 0;
        construirGrafo();
    }

    // ── Construcción del grafo ────────────────────────────────────────────────

    /**
     * Instancia las sub-partidas activas, las registra en el grafo y conecta
     * los caminos dirigidos que representan la progresión del jugador.
     *
     * POST: mapaMundi tiene los nodos activos y sus aristas configuradas.
     *       Cada nodo tiene su Runnable de callback apuntando a alTerminarCiudad(id).
     *
     * Topología activa:
     *   [1:Ordenamiento] ---> [5:Búsqueda]
     *   [1:Ordenamiento] ---> [8:Hanoi]
     *
     * Descomentá y conectá ciudades aquí a medida que las implementes.
     */
    private void construirGrafo() {
        Jugador jugador = getJugador();

        NodoCiudad ciudad1 = crearNodo(1, "Ciudad de Recoleccion",
                new PartidaDeRecoleccion("Partida De Recoleccion", jugador));
        NodoCiudad ciudad2 = crearNodo(2, "Ciudad De Reinas", 
        		new PartidaReinas(jugador));
        
        NodoCiudad ciudad4 = crearNodo(4, "Ciudad de Ordenamiento",
                new PartidaOrdenamientos("Ordenamiento", jugador));
        NodoCiudad ciudad5 = crearNodo(5, "Ciudad de Búsqueda",
                new PartidaBusqueda("Búsqueda", jugador));
        
        NodoCiudad ciudad8 = crearNodo(8, "Torres de Hanoi",
                new PartidaHanoi("Torres de Hanoi", jugador));
        //NodoCiudad ciudad9 = crearNodo(9, "Ciudad De Pilas Y Colas", 
        //		new partida);
        //NodoCiudad ciudad10= crearNodo(10, "Ciudad De Complejidad",
        //		new );
        mapaMundi.agregarCiudad(ciudad1);
        mapaMundi.agregarCiudad(ciudad2);
        mapaMundi.agregarCiudad(ciudad4);
        mapaMundi.agregarCiudad(ciudad5);
        mapaMundi.agregarCiudad(ciudad8);

        
        mapaMundi.conectarCiudades(1, 2);
        mapaMundi.conectarCiudades(2, 4);
        mapaMundi.conectarCiudades(4, 5);
        mapaMundi.conectarCiudades(5, 8);
    }

    /**
     * Factoría privada: crea un NodoCiudad y le inyecta el callback de finalización.
     *
     * PRE:  id en [1,10], nombre != null, partida != null.
     * POST: nodo con Runnable configurado, listo para agregar al grafo.
     *
     * El closure captura 'id' por valor: alTerminarCiudad siempre recibe
     * el id correcto aunque el objeto nodo sea reemplazado en el futuro.
     */
    private NodoCiudad crearNodo(int id, String nombre, Partida partida) {
        NodoCiudad nodo = new NodoCiudad(id, nombre, partida);
        partida.setOnFinalizadoCallback(() -> alTerminarCiudad(id));
        return nodo;
    }

    // ── Ciclo de vida principal ───────────────────────────────────────────────

    /**
     * Crea la ventana global, inyecta VistaGlobal y arranca el hilo de 60 FPS.
     *
     * PRE:  jugador != null, RUTA_MAPA_GLOBAL accesible en el classpath.
     * POST: ventanaGlobal visible, vistaGlobal corriendo a 60 FPS, estado = Iniciado.
     */
    @Override
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);

        // VistaGlobal recibe 'this' para consultar el grafo y disparar entrarACiudad()
        // cuando el jugador presiona ENTER. Solo consulta; nunca modifica el modelo.
        vistaGlobal = new VistaGlobal(
            RUTA_MAPA_GLOBAL,
            getJugador(),
            RUTA_SPRITE_JUGADOR,
            this
        );

        ventanaGlobal = new JFrame("Al-Quest");
        ventanaGlobal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventanaGlobal.setResizable(false);
        ventanaGlobal.add(vistaGlobal);
        ventanaGlobal.pack();
        ventanaGlobal.setLocationRelativeTo(null);
        ventanaGlobal.setVisible(true);

        vistaGlobal.startGameThread();
    }

    /**
     * Cierra la partida general (salida desde menú o cierre de ventana).
     *
     * POST: hilo detenido, ventana destruida, memoria liberada.
     */
    @Override
    public void finalizar() {
        setEstado(EstadoDePartida.Creado);
        if (vistaGlobal  != null) vistaGlobal.detenerHilo();
        if (ventanaGlobal != null) {
            ventanaGlobal.dispose();
            ventanaGlobal = null;
        }
    }

    // ── Interacción con ciudades ──────────────────────────────────────────────

    /**
     * Intenta que el jugador ingrese a la ciudad indicada.
     * Llamado desde VistaGlobal cuando detecta ENTER sobre una ciudad.
     *
     * PRE:  idCiudad en [1, MAX_CIUDADES], ventanaGlobal visible y activa.
     * POST: si la ciudad es accesible:
     *         - detiene el hilo de vistaGlobal
     *         - oculta ventanaGlobal
     *         - invoca partidaAsociada.iniciar()
     *       Si no es accesible, no ocurre nada (VistaGlobal ya mostró el mensaje).
     *
     * @param idCiudad id de la ciudad a ingresar
     */
    public void entrarACiudad(int idCiudad) {
        if (!mapaMundi.esCiudadAccesible(idCiudad)) return;

        NodoCiudad nodo = mapaMundi.obtenerCiudad(idCiudad);
        if (nodo == null || nodo.getPartidaAsociada() == null) return;

        ciudadActual = nodo;

        // Regla de Oro: gestión limpia de hilos antes de lanzar sub-partida
        vistaGlobal.detenerHilo();
        ventanaGlobal.setVisible(false);

        nodo.getPartidaAsociada().iniciar();
    }

    /**
     * Callback invocado por la sub-partida cuando el jugador termina el desafío.
     *
     * Se ejecuta en el hilo de la sub-partida; invokeLater garantiza que las
     * operaciones de Swing sobre ventanaGlobal ocurran en el EDT.
     *
     * PRE:  la sub-partida ya ejecutó finalizar() (hilo detenido, ventana dispuesta).
     * POST: si puntaje > 0:
     *         - nodo.setCompletada(true) desbloquea ciudades adyacentes en el grafo
     *         - puntajeTotal acumula el puntaje
     *       En cualquier caso:
     *         - ciudadActual = null
     *         - ventanaGlobal vuelve a ser visible
     *         - vistaGlobal.startGameThread() relanza el loop manteniendo la posición
     *           del jugador en el mapa global (no se reinicia el spawn).
     *
     * @param idCiudad id de la ciudad cuya sub-partida terminó
     */
    public void alTerminarCiudad(int idCiudad) {
        SwingUtilities.invokeLater(() -> {

            NodoCiudad nodo = mapaMundi.obtenerCiudad(idCiudad);
            if (nodo == null) return;

            int puntajeCiudad = nodo.getPartidaAsociada().getPuntaje();

            if (puntajeCiudad > 0) {
                nodo.setCompletada(true);
                puntajeTotal += puntajeCiudad;
            }

            ciudadActual = null;
            ventanaGlobal.setVisible(true);
            vistaGlobal.startGameThread();
        });
    }

 
    
    // ── Getters ───────────────────────────────────────────────────────────────

    /** @return el grafo del mundo (solo lectura desde la vista). */
    public GrafoCiudades getMapaMundi()      { return mapaMundi;    }

    /** @return puntaje total acumulado. */
    public int getPuntajeTotal()             { return puntajeTotal; }

    /** @return ciudad activa (null si el jugador está en el mapa global). */
    public NodoCiudad getCiudadActual()      { return ciudadActual; }
}