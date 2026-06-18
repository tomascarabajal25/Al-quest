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

public class PartidaGeneral extends Partida {
    //INTERFACES ----------------------------------------------------------------------------------------------
    //ENUMERADOS ----------------------------------------------------------------------------------------------
    //CONSTANTES ----------------------------------------------------------------------------------------------
    private static final String RUTA_MAPA_GLOBAL    = "/maps/mapa_global.txt";
    private static final String RUTA_SPRITE_JUGADOR = "/assets/jugador/boy";
    //ATRIBUTOS DE CLASE --------------------------------------------------------------------------------------
    //ATRIBUTOS -----------------------------------------------------------------------------------------------
    private final GrafoCiudades mapaMundi;
    private NodoCiudad ciudadActual;
    private JFrame ventanaGlobal;
    private VistaGlobal vistaGlobal;
    private int puntajeTotal;
    private String skinActual;
    private final List<String> skinsDesbloqueadas;
    //ATRIBUTOS TRANSITORIOS ----------------------------------------------------------------------------------
    //CONSTRUCTORES -------------------------------------------------------------------------------------------
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
    //METODOS ABSTRACTOS --------------------------------------------------------------------------------------
    //METODOS HEREDADOS (CLASE)--------------------------------------------------------------------------------
    //METODOS HEREDADOS (INTERFACE)----------------------------------------------------------------------------
    //METODOS DE CLASE ----------------------------------------------------------------------------------------
    //METODOS GENERALES ---------------------------------------------------------------------------------------
    //METODOS DE COMPORTAMIENTO -------------------------------------------------------------------------------
    /**
     * Construye el grafo que une las ciudades del juego
     *
     * POST:
     * -Las ciudades quedan unidas
     */
    private void construirGrafo() {
        Jugador jugador = getJugador();

//        NodoCiudad ciudad1 = crearNodo(1, "Ciudad de Recoleccion",
//                new PartidaDeRecoleccion("Partida De Recoleccion", jugador));
//
//        NodoCiudad ciudad2 = crearNodo(2, "Ciudad De Reinas",
//                new PartidaReinas(jugador));
//
//        NodoCiudad ciudad3 = crearNodo(3, "Ciudad De Laberinto",
//                new PartidaLaberinto(jugador));
//
//        NodoCiudad ciudad4 = crearNodo(4, "Ciudad de Ordenamiento",
//                new PartidaOrdenamientos("Ordenamiento", jugador));
//
//        NodoCiudad ciudad5 = crearNodo(5, "Ciudad de Búsqueda",
//                new PartidaBusqueda("Búsqueda", jugador));
//
//        NodoCiudad ciudad6 = crearNodo(6, "Ciudad de Hashing",
//                new PartidaHashing("Hash", jugador));
//
//        NodoCiudad ciudad7 = crearNodo(7, "Ciudad de Grafos",
//                new PartidaGrafos("Grafos", jugador));
//
//        NodoCiudad ciudad8 = crearNodo(8, "Torres de Hanoi",
//                new PartidaHanoi("Torres de Hanoi", jugador));

        NodoCiudad ciudad9 = crearNodo(9, "Ciudad De Pilas Y Colas",
                new PartidaBatalla("Batalla de Pilas, Colas y Listas", jugador));
//
//        NodoCiudad ciudad10 = crearNodo(10, "Ciudad De Complejidad",
//                new PartidaComplejidad(jugador));

//        mapaMundi.agregarCiudad(ciudad1);
//        mapaMundi.agregarCiudad(ciudad2);
//        mapaMundi.agregarCiudad(ciudad3);
//        mapaMundi.agregarCiudad(ciudad4);
//        mapaMundi.agregarCiudad(ciudad5);
//        mapaMundi.agregarCiudad(ciudad6);
//        mapaMundi.agregarCiudad(ciudad7);
//        mapaMundi.agregarCiudad(ciudad8);
        mapaMundi.agregarCiudad(ciudad9);
//        mapaMundi.agregarCiudad(ciudad10);
//
//        mapaMundi.conectarCiudades(1, 2);
//        mapaMundi.conectarCiudades(2, 3);
//        mapaMundi.conectarCiudades(3, 4);
//        mapaMundi.conectarCiudades(4, 5);
//        mapaMundi.conectarCiudades(5, 6);
//        mapaMundi.conectarCiudades(6, 7);
//        mapaMundi.conectarCiudades(7, 8);
//        mapaMundi.conectarCiudades(8, 9);
//        mapaMundi.conectarCiudades(9, 10);
    }

    /**
     * Crea un nodo ciudad para el grafo
     *
     * PRE:
     * -Nombre y partida no deben ser nulos
     * -Id debe ser mayor o igual a cero
     *
     * @param id: id del nodo
     * @param nombre: Nombre de la ciudad
     * @param partida: Partida de la ciudad
     * @return: Devuelve el nodo creado
     */
    private NodoCiudad crearNodo(int id, String nombre, Partida partida) {
        ValidacionesUtiles.validarMayorOIgualACero(id, "id");
        ValidacionesUtiles.esDistintoDeNull(nombre, "nombre");
        ValidacionesUtiles.esDistintoDeNull(partida, "partida");

        NodoCiudad nodo = new NodoCiudad(id, nombre, partida);
        partida.setOnFinalizadoCallback(() -> alTerminarCiudad(id));
        return nodo;
    }

    /**
     * Inicia la ventana asociado a la partida general del juego
     */
    @Override
    public void iniciar() {
        setEstado(EstadoDePartida.Iniciado);

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
     * Inicia la ventana asociado a la partida general del juego
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
     * PRE:
     * -Datos no debe ser nulo
     * POST:
     * -Se inicia la partida con los datos restaurados
     *
     * @param datos: estado previamente guardado de la partida
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

    /**
     * Activa el minijuego de la ciudad a la que el jugador entra
     *
     * PRE:
     * -Id debe ser mayor o igual a cero y debe tener una ciudad asociado al mismo
     * POST:
     * -Si la ciudad es accesible:
     *         - detiene el hilo de vistaGlobal
     *         - oculta ventanaGlobal
     *         - invoca partidaAsociada.iniciar()
     * -Si no es accesible, no ocurre nada (VistaGlobal ya mostró el mensaje).
     *
     * @param idCiudad: id de la ciudad a ingresar
     */
    public void entrarACiudad(int idCiudad) {
        if (!mapaMundi.esCiudadAccesible(idCiudad)) return;

        NodoCiudad nodo = mapaMundi.obtenerCiudad(idCiudad);
        if (nodo == null || nodo.getPartidaAsociada() == null) return;

        ciudadActual = nodo;

        nodo.getPartidaAsociada().setRutaSprites(skinActual);

        vistaGlobal.detenerHilo();
        ventanaGlobal.setVisible(false);

        nodo.getPartidaAsociada().iniciar();
    }

    /**
     * Finaliza el minijuego de la ciudad y vuelve a la partida general, trayendo los puntdos y cambiando el estado de la ciudad finalizada
     *
     * PRE:
     * -Id debe ser mayor o igual a cero y debe tener una ciudad asociado al mismo
     * POST:
     * -Se cargan los puntos del jugador obtenidos en la ciudad finalizada
     * -Cambia el estado d ela ciudad
     * -Vuelve a la partida general
     *
     * @param idCiudad id de la ciudad cuya sub-partida terminó
     */
    public void alTerminarCiudad(int idCiudad) {
        ValidacionesUtiles.validarMayorOIgualACero(idCiudad, "idCiudad");


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
     *
     * PRE:
     * -RutaSkin no puede ser null
     * -costo debe ser mayor o igual a cero
     *
     * POST:
     * -PuntajeTotal >= costo (y skin no fue comprada antes):
     *                      -puntajeTotal -= costo
     *                      -rutaSkin se agrega a skinsDesbloqueadas
     *                      -se persiste la sesión (GestorDeInicio.guardarSesion)
     *                      -return true
     * -En otro caso, return false, no modifica nada.
     *
     * @param rutaSkin ruta base de la skin a comprar
     * @param costo se descuenta de puntaje
     * @return: Devuelve true si compro, false si no
     */
    public boolean comprarSkin(String rutaSkin, int costo) {
        ValidacionesUtiles.esDistintoDeNull(rutaSkin, "rutaSkin");
        ValidacionesUtiles.validarMayorOIgualACero(costo, "costo");

        if ((puntajeTotal < costo) || (skinsDesbloqueadas.contains(rutaSkin))) {
            return false;
        }
        puntajeTotal -=costo;
        skinsDesbloqueadas.add(rutaSkin);
        persistencia.GestorDeInicio.guardarSesion(this);
        return true;
    }
    public void reiniciar() {
		mapaMundi.reiniciarTodasLasCiudades();
		persistencia.GestorDeInicio.guardarSesion(this);
	}
    //METODOS DE CONSULTA DE ESTADO ---------------------------------------------------------------------------
    //GETTERS REDEFINIDOS -------------------------------------------------------------------------------------
    //GETTERS INICIALIZADOS -----------------------------------------------------------------------------------
    //GETTERS COMPLEJOS ---------------------------------------------------------------------------------------
    //GETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Getter del grafo
     * @return el grafo del mundo (solo lectura desde la vista). */
    public GrafoCiudades getMapaMundi() {
        return mapaMundi;
    }
    
    /**
     * post:devuelve si todas las ciudades fueron completadas
     * @return
     */
    public boolean estaTerminado() {
    	return mapaMundi.obtenerIdsCompletadas().size()==mapaMundi.getNodos().size();
    }

    /**
     * Getter del puntaje ideal
     * @return puntaje total acumulado. */
    public int getPuntajeTotal(){
        return puntajeTotal;
    }

    /**
     * Getter de la ciudad actual
     * @return ciudad activa (null si el jugador está en el mapa global). */
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
    //SETTERS COMPLEJOS----------------------------------------------------------------------------------------
    //SETTERS SIMPLES -----------------------------------------------------------------------------------------
    /**
     * Cambia la skin activa registrada en el modelo
     * PRE:
     * -rutaSkin no puede ser null
     * POST:
     * -skinActual queda actualizado. El cambio visual lo aplica TiendaSkins justo despues de este setter
     * -Se persiste la sesión (GestorDeInicio.guardarSesion) para que el cambio de skin sobreviva aunque se cierre el juego sin completar otra ciudad.
     *
     * @param rutaSkin: Devuelve la ruta base del skin a equipar
     */

    public void setSkinActual(String rutaSkin) {
        ValidacionesUtiles.esDistintoDeNull(rutaSkin, "rutaSkin");

        this.skinActual = rutaSkin;
        persistencia.GestorDeInicio.guardarSesion(this);
    }
	

}
