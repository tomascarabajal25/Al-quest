package modelos;

import java.util.Map;

/**
 * TDA Test para GrafoCiudades.
 *
 * Cubre:
 *   - Constructor: grafo vacío, getNodos vacío
 *   - agregarCiudad: caso normal, null ignorado, límite MAX_CIUDADES
 *   - conectarCiudades: conexión válida, origen inexistente, destino inexistente
 *   - esCiudadAccesible: ciudad inicial, ciudad sin predecesores, ciudad con
 *       predecesor no completado, ciudad con predecesor completado, id inexistente
 *   - obtenerCiudad: id existente, id inexistente
 *   - getNodos: vista no modificable
 *   - toString: formato esperado
 */
public class TestGrafoCiudades {

    // ------------------------------------------------------------------ //
    //  Helper de reporte
    // ------------------------------------------------------------------ //

    private static void reportar(String nombre, boolean ok) {
        System.out.println((ok ? "[OK]  " : "[FAIL]") + " " + nombre);
    }

    // ------------------------------------------------------------------ //
    //  Factories de apoyo
    // ------------------------------------------------------------------ //

    /**
     * Crea un NodoCiudad con el id indicado.
     * Se asume constructor NodoCiudad(int id, String nombre).
     */
    private static NodoCiudad nodo(int id) {
        return new NodoCiudad(id, "Ciudad" + id);
    }

    /**
     * Construye un grafo con las ciudades 1..n ya agregadas (sin conexiones).
     */
    private static GrafoCiudades grafoConN(int n) {
        GrafoCiudades g = new GrafoCiudades();
        for (int i = 1; i <= n; i++) {
            g.agregarCiudad(nodo(i));
        }
        return g;
    }

    // ================================================================== //
    //  Tests: Constructor
    // ================================================================== //

    /** El grafo recién creado no contiene ningún nodo. */
    private static void testConstructorGrafoVacio() {
        GrafoCiudades g = new GrafoCiudades();
        reportar("Constructor: grafo recién creado está vacío",
                g.getNodos().isEmpty());
    }

    // ================================================================== //
    //  Tests: agregarCiudad
    // ================================================================== //

    /** Una ciudad agregada queda recuperable por su id. */
    private static void testAgregarCiudadQuedarRegistrada() {
        GrafoCiudades g = new GrafoCiudades();
        NodoCiudad c = nodo(1);
        g.agregarCiudad(c);
        reportar("agregarCiudad: ciudad queda registrada y es recuperable",
                g.obtenerCiudad(1) == c);
    }

    /** Agregar null no lanza excepción y no modifica el grafo. */
    private static void testAgregarCiudadNullIgnorado() {
        GrafoCiudades g = new GrafoCiudades();
        boolean ok = false;
        try {
            g.agregarCiudad(null);
            ok = g.getNodos().isEmpty();
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("agregarCiudad: null es ignorado sin excepción",  ok);
    }

    /** El tamaño del grafo crece con cada ciudad agregada. */
    private static void testAgregarCiudadTamanioCrece() {
        GrafoCiudades g = new GrafoCiudades();
        g.agregarCiudad(nodo(1));
        g.agregarCiudad(nodo(2));
        g.agregarCiudad(nodo(3));
        reportar("agregarCiudad: el tamaño crece correctamente",
                g.getNodos().size() == 3);
    }

    /** Superar MAX_CIUDADES lanza IllegalStateException. */
    private static void testAgregarCiudadSuperaMaxLanzaExcepcion() {
        GrafoCiudades g = grafoConN(GrafoCiudades.MAX_CIUDADES);
        boolean lanzó = false;
        try {
            g.agregarCiudad(nodo(GrafoCiudades.MAX_CIUDADES + 1));
        } catch (IllegalStateException e) {
            lanzó = true;
        }
        reportar("agregarCiudad: superar MAX_CIUDADES → IllegalStateException", lanzó);
    }

    /** Agregar exactamente MAX_CIUDADES ciudades no lanza excepción. */
    private static void testAgregarCiudadHastaMaxNoLanzaExcepcion() {
        boolean ok = false;
        try {
            grafoConN(GrafoCiudades.MAX_CIUDADES);
            ok = true;
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("agregarCiudad: agregar exactamente MAX_CIUDADES → sin excepción", ok);
    }

    // ================================================================== //
    //  Tests: conectarCiudades
    // ================================================================== //

    /** Conectar dos ciudades existentes no lanza excepción. */
    private static void testConectarCiudadesValido() {
        boolean ok = false;
        try {
            GrafoCiudades g = grafoConN(3);
            g.conectarCiudades(1, 2);
            ok = true;
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("conectarCiudades: conexión válida → sin excepción", ok);
    }

    /** Conectar con origen inexistente lanza IllegalArgumentException. */
    private static void testConectarOrigenInexistenteLanzaExcepcion() {
        boolean lanzó = false;
        try {
            GrafoCiudades g = grafoConN(2);
            g.conectarCiudades(99, 1);
        } catch (IllegalArgumentException e) {
            lanzó = true;
        }
        reportar("conectarCiudades: origen inexistente → IllegalArgumentException", lanzó);
    }

    /** Conectar con destino inexistente lanza IllegalArgumentException. */
    private static void testConectarDestinoInexistenteLanzaExcepcion() {
        boolean lanzó = false;
        try {
            GrafoCiudades g = grafoConN(2);
            g.conectarCiudades(1, 99);
        } catch (IllegalArgumentException e) {
            lanzó = true;
        }
        reportar("conectarCiudades: destino inexistente → IllegalArgumentException", lanzó);
    }

    /**
     * La conexión es dirigida: conectar 1→2 no implica que 2 apunte a 1.
     * Se verifica indirectamente: completar ciudad 2 no desbloquea ciudad 1.
     */
    private static void testConectarCiudadesEsDirigido() {
        GrafoCiudades g = grafoConN(2);
        g.conectarCiudades(1, 2); // 1 → 2
        g.obtenerCiudad(2).setCompletada(true);
        // Ciudad 1 siempre es accesible por ser la inicial, así que probamos
        // que ciudad 1 NO aparece en los adyacentes de ciudad 2.
        boolean noApunta = !g.obtenerCiudad(2).getAdyacentes()
                                               .contains(g.obtenerCiudad(1));
        reportar("conectarCiudades: el grafo es dirigido (1→2 no implica 2→1)",
                noApunta);
    }

    // ================================================================== //
    //  Tests: esCiudadAccesible
    // ================================================================== //

    /** La ciudad inicial (ID 1) siempre es accesible, incluso sin vecinos. */
    private static void testAccesibleCiudadInicialSiempre() {
        GrafoCiudades g = grafoConN(1);
        reportar("esCiudadAccesible: ciudad inicial (id=1) siempre accesible",
                g.esCiudadAccesible(GrafoCiudades.ID_CIUDAD_INICIAL));
    }

    /** Una ciudad sin predecesores completados no es accesible. */
    private static void testAccesibleSinPredecesorCompletado() {
        GrafoCiudades g = grafoConN(2);
        g.conectarCiudades(1, 2);
        // Ciudad 1 no está completada
        reportar("esCiudadAccesible: predecesor existe pero no completado → false",
                !g.esCiudadAccesible(2));
    }

    /** Una ciudad con un predecesor completado ES accesible. */
    private static void testAccesibleConPredecesorCompletado() {
        GrafoCiudades g = grafoConN(2);
        g.conectarCiudades(1, 2);
        g.obtenerCiudad(1).setCompletada(true);
        reportar("esCiudadAccesible: predecesor completado → true",
                g.esCiudadAccesible(2));
    }

    /** Una ciudad sin ningún predecesor en el grafo no es accesible. */
    private static void testAccesibleSinPredecesores() {
        GrafoCiudades g = grafoConN(3);
        g.conectarCiudades(1, 2); // 3 no tiene ningún predecesor
        reportar("esCiudadAccesible: ciudad sin predecesores → false",
                !g.esCiudadAccesible(3));
    }

    /** Una ciudad con múltiples predecesores: basta con que uno esté completado. */
    private static void testAccesibleUnPredecesorEntreVarios() {
        GrafoCiudades g = grafoConN(4);
        g.conectarCiudades(2, 4);
        g.conectarCiudades(3, 4);
        // Solo ciudad 3 completada
        g.obtenerCiudad(3).setCompletada(true);
        reportar("esCiudadAccesible: uno de varios predecesores completado → true",
                g.esCiudadAccesible(4));
    }

    /** Un id inexistente en el grafo devuelve false. */
    private static void testAccesibleIdInexistente() {
        GrafoCiudades g = grafoConN(2);
        reportar("esCiudadAccesible: id inexistente → false",
                !g.esCiudadAccesible(99));
    }

    // ================================================================== //
    //  Tests: obtenerCiudad
    // ================================================================== //

    /** obtenerCiudad devuelve el nodo correcto para un id existente. */
    private static void testObtenerCiudadExistente() {
        GrafoCiudades g = new GrafoCiudades();
        NodoCiudad c = nodo(5);
        g.agregarCiudad(c);
        reportar("obtenerCiudad: id existente → devuelve el nodo correcto",
                g.obtenerCiudad(5) == c);
    }

    /** obtenerCiudad devuelve null para un id no registrado. */
    private static void testObtenerCiudadNoExistente() {
        GrafoCiudades g = grafoConN(3);
        reportar("obtenerCiudad: id inexistente → null",
                g.obtenerCiudad(99) == null);
    }

    // ================================================================== //
    //  Tests: getNodos
    // ================================================================== //

    /** getNodos devuelve un mapa con todas las ciudades agregadas. */
    private static void testGetNodosContieneTodasLasCiudades() {
        GrafoCiudades g = grafoConN(4);
        Map<Integer, NodoCiudad> nodos = g.getNodos();
        reportar("getNodos: contiene todas las ciudades agregadas",
                nodos.size() == 4
                && nodos.containsKey(1)
                && nodos.containsKey(4));
    }

    /** El mapa devuelto por getNodos es no modificable. */
    private static void testGetNodosNoModificable() {
        GrafoCiudades g = grafoConN(2);
        boolean lanzó = false;
        try {
            g.getNodos().put(99, nodo(99));
        } catch (UnsupportedOperationException e) {
            lanzó = true;
        }
        reportar("getNodos: devuelve vista no modificable → UnsupportedOperationException",
                lanzó);
    }

    // ================================================================== //
    //  Tests: toString
    // ================================================================== //

    /** toString sigue el formato "GrafoCiudades{ciudades=N/MAX}". */
    private static void testToStringFormatoEsperado() {
        GrafoCiudades g = grafoConN(3);
        String esperado = "GrafoCiudades{ciudades=3/" + GrafoCiudades.MAX_CIUDADES + "}";
        reportar("toString: formato 'GrafoCiudades{ciudades=N/MAX}'",
                esperado.equals(g.toString()));
    }

    /** toString con grafo vacío muestra 0 ciudades. */
    private static void testToStringGrafoVacio() {
        GrafoCiudades g = new GrafoCiudades();
        String esperado = "GrafoCiudades{ciudades=0/" + GrafoCiudades.MAX_CIUDADES + "}";
        reportar("toString: grafo vacío → 'GrafoCiudades{ciudades=0/MAX}'",
                esperado.equals(g.toString()));
    }

    // ================================================================== //
    //  Punto de entrada
    // ================================================================== //

    public static void main(String[] args) {
        System.out.println("=== Tests GrafoCiudades ===\n");

        System.out.println("-- Constructor --");
        testConstructorGrafoVacio();

        System.out.println("\n-- agregarCiudad --");
        testAgregarCiudadQuedarRegistrada();
        testAgregarCiudadNullIgnorado();
        testAgregarCiudadTamanioCrece();
        testAgregarCiudadHastaMaxNoLanzaExcepcion();
        testAgregarCiudadSuperaMaxLanzaExcepcion();

        System.out.println("\n-- conectarCiudades --");
        testConectarCiudadesValido();
        testConectarOrigenInexistenteLanzaExcepcion();
        testConectarDestinoInexistenteLanzaExcepcion();
        testConectarCiudadesEsDirigido();

        System.out.println("\n-- esCiudadAccesible --");
        testAccesibleCiudadInicialSiempre();
        testAccesibleSinPredecesorCompletado();
        testAccesibleConPredecesorCompletado();
        testAccesibleSinPredecesores();
        testAccesibleUnPredecesorEntreVarios();
        testAccesibleIdInexistente();

        System.out.println("\n-- obtenerCiudad --");
        testObtenerCiudadExistente();
        testObtenerCiudadNoExistente();

        System.out.println("\n-- getNodos --");
        testGetNodosContieneTodasLasCiudades();
        testGetNodosNoModificable();

        System.out.println("\n-- toString --");
        testToStringFormatoEsperado();
        testToStringGrafoVacio();

        System.out.println("\n=== Fin de tests ===");
    }
}
