package modelos;


import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;

import javax.swing.SwingUtilities;

import juego.ciudades.reinas.PartidaReinas;
import juego.ciudades.ciudad5.PartidaBusqueda;
import juego.ciudades.ciudad_3_laberinto.src.PartidaLaberinto;
import juego.ciudades.complejidad.PartidaComplejidad;
import juego.ciudades.hashing.PartidaHashing;
import juego.ciudades.torresDeHanoi.PartidaHanoi;
import juego.ciudades.grafos.controller.PartidaGrafos;
import persistencia.DatosGuardado;
import utils.ValidacionesUtiles;
import juego.ciudades.ordenamientos.PartidaOrdenamientos;
import juego.ciudades.recoleccionEnMatriz.PartidaDeRecoleccion;
import juego.ciudades.batalla.controller.PartidaBatalla;

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

    /**
     * Ruta de la skin que esta equipada actualmente en el juego.
     * Lista de skins que el jugador ya compro 
     */
    private String skinActual;
    private final List<String> skinsDesbloqueadas;


 
    
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

        //Manejo de skins, personaje nace con skin base desbloqueada
        this.skinActual = RUTA_SPRITE_JUGADOR;
        this.skinsDesbloqueadas = new ArrayList<>();
        this.skinsDesbloqueadas.add(RUTA_SPRITE_JUGADOR);

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

        NodoCiudad ciudad3 = crearNodo(3, "Ciudad De Laberinto",
        		new PartidaLaberinto(jugador));

        NodoCiudad ciudad4 = crearNodo(4, "Ciudad de Ordenamiento",
            new PartidaOrdenamientos("Ordenamiento", jugador));

        NodoCiudad ciudad5 = crearNodo(5, "Ciudad de Búsqueda",
            new PartidaBusqueda("Búsqueda", jugador));

        NodoCiudad ciudad6 = crearNodo(6, "Ciudad de Hashing",
                new PartidaHashing("Hash", jugador));
        
        NodoCiudad ciudad7 = crearNodo(7, "Ciudad de Grafos",
            new PartidaGrafos("Grafos", jugador));

        NodoCiudad ciudad8 = crearNodo(8, "Torres de Hanoi",
            new PartidaHanoi("Torres de Hanoi", jugador));

        NodoCiudad ciudad9 = crearNodo(9, "Ciudad De Pilas Y Colas",
            new PartidaBatalla("Batalla de Pilas, Colas y Listas", jugador));

        NodoCiudad ciudad10 = crearNodo(10, "Ciudad De Complejidad",
            new PartidaComplejidad(jugador));

          mapaMundi.agregarCiudad(ciudad1);
          mapaMundi.agregarCiudad(ciudad2);
          mapaMundi.agregarCiudad(ciudad3);
          mapaMundi.agregarCiudad(ciudad4);
          mapaMundi.agregarCiudad(ciudad5);
          mapaMundi.agregarCiudad(ciudad6);
          mapaMundi.agregarCiudad(ciudad7);
          mapaMundi.agregarCiudad(ciudad8);
          mapaMundi.agregarCiudad(ciudad9);
          mapaMundi.agregarCiudad(ciudad10);
  
          mapaMundi.conectarCiudades(1, 2);
          mapaMundi.conectarCiudades(2, 3);
          mapaMundi.conectarCiudades(3, 4);
          mapaMundi.conectarCiudades(4, 5);
          mapaMundi.conectarCiudades(5, 6);
          mapaMundi.conectarCiudades(6, 7);
          mapaMundi.conectarCiudades(7, 8);
          mapaMundi.conectarCiudades(8, 9);
          mapaMundi.conectarCiudades(9, 10);
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
            skinActual,
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


    /**
     * Genera una "fotografía" del estado actual de la partida, apta para
     * persistencia en disco.
     *
     * pre:  ninguna.
     * post: no modifica el estado de la partida; solo lo consulta.
     *
     * @return los datos de la partida actual, listos para ser guardados
     */
    public DatosGuardado generarDatosGuardado() {
        int idCiudadActual = (ciudadActual != null)
                ? ciudadActual.getId()
                : GrafoCiudades.ID_CIUDAD_INICIAL;

        return new DatosGuardado(
                getJugador().getNombre(),
                puntajeTotal,
                idCiudadActual,
                mapaMundi.obtenerIdsCompletadas(),
                mapaMundi.obtenerIdsAccesibles(),
                skinActual,
                new Vector<>(skinsDesbloqueadas));
    }

    /**
     * Restaura el estado de la partida a partir de un DatosGuardado leído
     * desde disco.
     *
     * pre:  datos != null
     * post: - puntajeTotal pasa a ser datos.getPuntajeTotal().
     *       - todas las ciudades cuyo id figura en
     *         datos.getIdsCiudadesCompletadas() quedan marcadas como
     *         completadas en mapaMundi (esto recalcula automáticamente la
     *         accesibilidad del resto de las ciudades, vía
     *         esCiudadAccesible()).
     *       - ciudadActual pasa a apuntar al nodo cuyo id es
     *         datos.getIdCiudadActual(), si existe en el grafo.
     *       - skinsDesbloqueadas incorpora todas las rutas guardadas en
     *         datos.getSkinsDesbloqueadas() (si el guardado las incluye).
     *       - skinActual pasa a ser datos.getSkinActual() (si el guardado
     *         lo incluye).
     *
     * @param datos estado previamente guardado de la partida
     */
    public void aplicarDatosGuardado(DatosGuardado datos) {
        ValidacionesUtiles.esDistintoDeNull(datos, "datos");

        this.puntajeTotal = datos.getPuntajeTotal();

        for (int idCompletada : datos.getIdsCiudadesCompletadas()) {
            mapaMundi.marcarCiudadCompletada(idCompletada);
        }

        NodoCiudad nodoActual = mapaMundi.obtenerCiudad(datos.getIdCiudadActual());
        if (nodoActual != null) {
            this.ciudadActual = nodoActual;
        }

        // Restaura las skins desbloqueadas (si el guardado es de una versión
        // anterior sin este campo, Gson lo deja en null y se conserva el
        // estado inicial: solo la skin por defecto desbloqueada).
        if (datos.getSkinsDesbloqueadas() != null) {
            for (String ruta : datos.getSkinsDesbloqueadas()) {
                if (!this.skinsDesbloqueadas.contains(ruta)) {
                    this.skinsDesbloqueadas.add(ruta);
                }
            }
        }

        // Restaura la skin equipada (si existe en el guardado).
        if (datos.getSkinActual() != null) {
            this.skinActual = datos.getSkinActual();
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

        //Para gestion de skins, propago la skin equipada a la sub partida antes de iniciar
        //Con esto se soluciona el bug que al entrar a la ciudad se me volvia a la skin default
        nodo.getPartidaAsociada().setRutaSprites(skinActual);

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
                persistencia.GestorDeInicio.guardarSesion(this);
            }

            ciudadActual = null;
            ventanaGlobal.setVisible(true);
            vistaGlobal.startGameThread();
        });
    }


    //GESTION DE SKINS
    /**
     * Comprar una skin, descuenta el costo del puntaje
     * pre: rutaSkin no puede ser null, costo >= 0
     * POST: puntajeTotal >= costo (y skin no fue comprada antes):
     *                      -puntajeTotal -= costo
     *                      -rutaSkin se agrega a skinsDesbloqueadas
     *                      -se persiste la sesión (GestorDeInicio.guardarSesion)
     *                      -return true
     *      En otro caso, return false, no modifica nada.
     * 
     * @param rutaSkin ruta base de la skin a comprar
     * @param costo se descuenta de puntaje
     * @return retorna true si compro
     */
    public boolean comprarSkin(String rutaSkin, int costo) {
        if ((puntajeTotal < costo) || (skinsDesbloqueadas.contains(rutaSkin))) {
            return false;
        } 
        puntajeTotal -=costo;
        skinsDesbloqueadas.add(rutaSkin);
        persistencia.GestorDeInicio.guardarSesion(this);
        return true;
    }

 
    
    // ── Getters ───────────────────────────────────────────────────────────────

    /** @return el grafo del mundo (solo lectura desde la vista). */
    public GrafoCiudades getMapaMundi() {
        return mapaMundi;
    }

    /** @return puntaje total acumulado. */
    public int getPuntajeTotal(){ 
        return puntajeTotal;
    }

    /** @return ciudad activa (null si el jugador está en el mapa global). */
    public NodoCiudad getCiudadActual(){
        return ciudadActual; 
    }


    //Gestion de skins
    /**
     * @return ruta de la skin equipada en el momento
     */
    public String getSkinActual(){
        return skinActual;
    }

    /**
     * @return lista de rutas de skins desbloqueadas
     */
    public List<String> getSkinsDesbloqueadas() {
        return skinsDesbloqueadas;
    }




    //SETTERS
    //Por ahora, solamente lo estamos usando para gestion de skins
    /**
     * Cambia la skin activa registrada en el modelo
     * PRE: rutaSkin no puede ser null
     * POST: skinActual queda actualizado. El cambio visual lo aplica TiendaSkins
     *                                     justo despues de este setter
     *       Se persiste la sesión (GestorDeInicio.guardarSesion) para que el
     *       cambio de skin sobreviva aunque se cierre el juego sin completar
     *       otra ciudad.
     * @param rutaSkin ruta base del skin a equipar
     */

    public void setSkinActual(String rutaSkin) {
        this.skinActual = rutaSkin;
        persistencia.GestorDeInicio.guardarSesion(this);
    }

}
