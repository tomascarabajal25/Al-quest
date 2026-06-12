package modelos;

/**
 * TDA Test para PartidaGeneral.
 *
 * PartidaGeneral depende de Swing (JFrame, SwingUtilities), GrafoCiudades,
 * NodoCiudad y sub-partidas concretas. Para aislar la lógica pura del
 * orquestador se definen stubs mínimos al final del archivo.
 *
 * Estrategia:
 *   - Los tests que solo ejercen lógica de modelo (puntaje, grafo, callbacks)
 *     NO llaman a iniciar() ni a finalizar(), evitando la creación de ventanas.
 *   - Se accede al estado interno a través de los getters públicos.
 *
 * Cubre:
 *   - Constructor: estado inicial (puntajeTotal=0, ciudadActual=null, grafo construido)
 *   - getMapaMundi / getPuntajeTotal / getCiudadActual
 *   - alTerminarCiudad: puntaje > 0 → completada + acumulación
 *   - alTerminarCiudad: puntaje = 0 → no completada, no acumula
 *   - alTerminarCiudad: acumulación de múltiples ciudades
 *   - alTerminarCiudad: id inexistente → sin excepción
 *   - Herencia de Partida: getNombre, getJugador
 */
public class TestPartidaGeneral {

    // ------------------------------------------------------------------ //
    //  Stubs
    // ------------------------------------------------------------------ //

    /** Jugador mínimo para construir PartidaGeneral. */
    private static Jugador jugadorStub() {
        // Ajustar según el constructor real de Jugador.
        return new Jugador("TestPlayer");
    }

    // ------------------------------------------------------------------ //
    //  Helper de reporte
    // ------------------------------------------------------------------ //

    private static void reportar(String nombre, boolean ok) {
        System.out.println((ok ? "[OK]  " : "[FAIL]") + " " + nombre);
    }

    // ------------------------------------------------------------------ //
    //  Helper: crea una PartidaGeneral sin abrir ventanas.
    //  iniciar() NO se llama — solo se construye el objeto.
    // ------------------------------------------------------------------ //

    private static PartidaGeneral crear() {
        return new PartidaGeneral(jugadorStub());
    }

    // ================================================================== //
    //  Tests: Constructor / estado inicial
    // ================================================================== //

    /** puntajeTotal arranca en 0. */
    private static void testConstructorPuntajeInicialCero() {
        PartidaGeneral pg = crear();
        reportar("Constructor: puntajeTotal inicial es 0",
                pg.getPuntajeTotal() == 0);
    }

    /** ciudadActual arranca en null (jugador en mapa global). */
    private static void testConstructorCiudadActualNull() {
        PartidaGeneral pg = crear();
        reportar("Constructor: ciudadActual inicial es null",
                pg.getCiudadActual() == null);
    }

    /** mapaMundi no es null después de construir. */
    private static void testConstructorMapaMundiNoNull() {
        PartidaGeneral pg = crear();
        reportar("Constructor: mapaMundi no es null",
                pg.getMapaMundi() != null);
    }

    /** El grafo tiene las ciudades activas declaradas en construirGrafo(). */
    private static void testConstructorGrafoTieneCiudadesActivas() {
        PartidaGeneral pg = crear();
        GrafoCiudades grafo = pg.getMapaMundi();
        // Ciudades activas según construirGrafo: 1, 2, 4, 5, 8, 10
        boolean todas =
                grafo.obtenerCiudad(1)  != null &&
                grafo.obtenerCiudad(2)  != null &&
                grafo.obtenerCiudad(4)  != null &&
                grafo.obtenerCiudad(5)  != null &&
                grafo.obtenerCiudad(8)  != null &&
                grafo.obtenerCiudad(10) != null;
        reportar("Constructor: grafo contiene las 6 ciudades activas (1,2,4,5,8,10)",
                todas);
    }

    /** Las ciudades no activas no están en el grafo. */
    private static void testConstructorGrafoNoCiudadesInactivas() {
        PartidaGeneral pg = crear();
        GrafoCiudades grafo = pg.getMapaMundi();
        boolean ninguna =
                grafo.obtenerCiudad(3)  == null &&
                grafo.obtenerCiudad(6)  == null &&
                grafo.obtenerCiudad(7)  == null &&
                grafo.obtenerCiudad(9)  == null;
        reportar("Constructor: grafo no contiene ciudades inactivas (3,6,7,9)",
                ninguna);
    }

    /** Las conexiones dirigidas del grafo están configuradas correctamente. */
    private static void testConstructorConexionesDirigidas() {
        PartidaGeneral pg = crear();
        GrafoCiudades grafo = pg.getMapaMundi();

        // Topología: 1→2→4→5→8→10
        // Para verificar accesibilidad: completar el predecesor activa al sucesor.
        grafo.obtenerCiudad(1).setCompletada(true);
        boolean c2_accesible  = grafo.esCiudadAccesible(2);

        grafo.obtenerCiudad(2).setCompletada(true);
        boolean c4_accesible  = grafo.esCiudadAccesible(4);

        reportar("Constructor: conexiones 1→2→4 configuradas correctamente",
                c2_accesible && c4_accesible);
    }

    /** El nombre de la partida es "Al-Quest — Mapa Mundial". */
    private static void testConstructorNombrePartida() {
        PartidaGeneral pg = crear();
        reportar("Constructor: nombre es 'Al-Quest — Mapa Mundial'",
                "Al-Quest — Mapa Mundial".equals(pg.getNombre()));
    }

    /** getJugador devuelve el jugador pasado al constructor. */
    private static void testConstructorGetJugador() {
        Jugador j = jugadorStub();
        PartidaGeneral pg = new PartidaGeneral(j);
        reportar("Constructor: getJugador devuelve el jugador correcto",
                pg.getJugador() == j);
    }

    // ================================================================== //
    //  Tests: alTerminarCiudad
    // ================================================================== //

    /**
     * Cuando puntaje > 0: el nodo queda marcado completada=true
     * y puntajeTotal acumula el puntaje.
     */
    private static void testAlTerminarCiudadPuntajePositivo() throws InterruptedException {
        PartidaGeneral pg = crear();
        NodoCiudad nodo = pg.getMapaMundi().obtenerCiudad(1);

        // Simulamos que la sub-partida arrojó puntaje 50
        nodo.getPartidaAsociada().setPuntaje(50);

        pg.alTerminarCiudad(1);

        // alTerminarCiudad usa invokeLater; esperamos que el EDT procese el evento.
        Thread.sleep(200);

        reportar("alTerminarCiudad (puntaje>0): nodo marcado como completada",
                nodo.isCompletada());
        reportar("alTerminarCiudad (puntaje>0): puntajeTotal acumula el puntaje",
                pg.getPuntajeTotal() == 50);
    }

    /**
     * Cuando puntaje = 0: el nodo NO queda marcado y puntajeTotal no cambia.
     */
    private static void testAlTerminarCiudadPuntajeCero() throws InterruptedException {
        PartidaGeneral pg = crear();
        NodoCiudad nodo = pg.getMapaMundi().obtenerCiudad(1);

        nodo.getPartidaAsociada().setPuntaje(0);

        pg.alTerminarCiudad(1);
        Thread.sleep(200);

        reportar("alTerminarCiudad (puntaje=0): nodo NO queda completada",
                !nodo.isCompletada());
        reportar("alTerminarCiudad (puntaje=0): puntajeTotal sigue en 0",
                pg.getPuntajeTotal() == 0);
    }

    /**
     * Acumulación de múltiples ciudades completadas con éxito.
     */
    private static void testAlTerminarCiudadAcumulaMultiples() throws InterruptedException {
        PartidaGeneral pg = crear();

        pg.getMapaMundi().obtenerCiudad(1).getPartidaAsociada().setPuntaje(30);
        pg.getMapaMundi().obtenerCiudad(2).getPartidaAsociada().setPuntaje(70);

        pg.alTerminarCiudad(1);
        pg.alTerminarCiudad(2);
        Thread.sleep(300);

        reportar("alTerminarCiudad: acumula puntaje de múltiples ciudades (30+70=100)",
                pg.getPuntajeTotal() == 100);
    }

    /**
     * alTerminarCiudad con un id inexistente no lanza excepción.
     */
    private static void testAlTerminarCiudadIdInexistente() throws InterruptedException {
        PartidaGeneral pg = crear();
        boolean ok = false;
        try {
            pg.alTerminarCiudad(99);
            Thread.sleep(200);
            ok = true;
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("alTerminarCiudad: id inexistente → sin excepción", ok);
    }

    /**
     * Después de alTerminarCiudad, ciudadActual vuelve a null.
     */
    private static void testAlTerminarCiudadResetaCiudadActual() throws InterruptedException {
        PartidaGeneral pg = crear();
        pg.getMapaMundi().obtenerCiudad(1).getPartidaAsociada().setPuntaje(10);

        pg.alTerminarCiudad(1);
        Thread.sleep(200);

        reportar("alTerminarCiudad: ciudadActual vuelve a null",
                pg.getCiudadActual() == null);
    }

    // ================================================================== //
    //  Tests: getPuntajeTotal
    // ================================================================== //

    /** getPuntajeTotal devuelve 0 si no se completó ninguna ciudad. */
    private static void testGetPuntajeTotalInicial() {
        PartidaGeneral pg = crear();
        reportar("getPuntajeTotal: 0 si no se completó ninguna ciudad",
                pg.getPuntajeTotal() == 0);
    }

    // ================================================================== //
    //  Punto de entrada
    // ================================================================== //

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Tests PartidaGeneral ===\n");

        System.out.println("-- Constructor / estado inicial --");
        testConstructorPuntajeInicialCero();
        testConstructorCiudadActualNull();
        testConstructorMapaMundiNoNull();
        testConstructorGrafoTieneCiudadesActivas();
        testConstructorGrafoNoCiudadesInactivas();
        testConstructorConexionesDirigidas();
        testConstructorNombrePartida();
        testConstructorGetJugador();

        System.out.println("\n-- alTerminarCiudad --");
        testAlTerminarCiudadPuntajePositivo();
        testAlTerminarCiudadPuntajeCero();
        testAlTerminarCiudadAcumulaMultiples();
        testAlTerminarCiudadIdInexistente();
        testAlTerminarCiudadResetaCiudadActual();

        System.out.println("\n-- getPuntajeTotal --");
        testGetPuntajeTotalInicial();

        System.out.println("\n=== Fin de tests ===");
        System.exit(0);
    }
}
