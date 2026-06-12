package modelos;

import java.util.List;

/**
 * TDA Test para NodoCiudad.
 *
 * Como NodoCiudad recibe una Partida en su constructor (tipo abstracto/interfaz),
 * se define una implementación mínima PartidaStub al final de este archivo.
 *
 * Cubre:
 *   - Constructor: estado inicial (id, nombre, completada=false, adyacentes vacíos)
 *   - getId / getNombre / getPartidaAsociada / isCompletada / getAdyacentes
 *   - setNombre / setPartidaAsociada / setCompletada
 *   - agregarCamino: caso normal, null ignorado, self-loop ignorado, no duplicados
 *   - getAdyacentes: vista no modificable
 *   - toString: formato esperado
 */
public class TestNodoCiudad {

    // ------------------------------------------------------------------ //
    //  Stub mínimo de Partida
    // ------------------------------------------------------------------ //

    /** Implementación vacía de Partida para poder instanciar NodoCiudad. */
    private static class PartidaStub extends Partida {
        // No es necesario sobrescribir nada; sólo necesitamos una instancia concreta.
    }

    // ------------------------------------------------------------------ //
    //  Helpers
    // ------------------------------------------------------------------ //

    private static void reportar(String nombre, boolean ok) {
        System.out.println((ok ? "[OK]  " : "[FAIL]") + " " + nombre);
    }

    private static PartidaStub partida() {
        return new PartidaStub();
    }

    private static NodoCiudad nodo(int id, String nombre) {
        return new NodoCiudad(id, nombre, partida());
    }

    // ================================================================== //
    //  Tests: Constructor / estado inicial
    // ================================================================== //

    /** getId devuelve el id con el que fue creado el nodo. */
    private static void testConstructorGetId() {
        NodoCiudad n = nodo(3, "Ciudad de Búsqueda");
        reportar("Constructor: getId devuelve el id correcto",
                n.getId() == 3);
    }

    /** getNombre devuelve el nombre con el que fue creado el nodo. */
    private static void testConstructorGetNombre() {
        NodoCiudad n = nodo(1, "Ciudad Inicial");
        reportar("Constructor: getNombre devuelve el nombre correcto",
                "Ciudad Inicial".equals(n.getNombre()));
    }

    /** getPartidaAsociada devuelve la partida pasada al constructor. */
    private static void testConstructorGetPartidaAsociada() {
        PartidaStub p = partida();
        NodoCiudad n = new NodoCiudad(2, "Ciudad2", p);
        reportar("Constructor: getPartidaAsociada devuelve la instancia correcta",
                n.getPartidaAsociada() == p);
    }

    /** completada arranca en false. */
    private static void testConstructorCompletadaInicialFalse() {
        NodoCiudad n = nodo(5, "Ciudad5");
        reportar("Constructor: completada inicial es false",
                !n.isCompletada());
    }

    /** adyacentes arranca vacía (no null). */
    private static void testConstructorAdyacentesInicialVacia() {
        NodoCiudad n = nodo(4, "Ciudad4");
        List<NodoCiudad> ady = n.getAdyacentes();
        reportar("Constructor: lista de adyacentes inicial es vacía y no null",
                ady != null && ady.isEmpty());
    }

    // ================================================================== //
    //  Tests: setNombre
    // ================================================================== //

    /** setNombre actualiza el nombre correctamente. */
    private static void testSetNombreActualiza() {
        NodoCiudad n = nodo(1, "Viejo");
        n.setNombre("Nuevo");
        reportar("setNombre: actualiza el nombre",
                "Nuevo".equals(n.getNombre()));
    }

    // ================================================================== //
    //  Tests: setPartidaAsociada
    // ================================================================== //

    /** setPartidaAsociada reemplaza la referencia. */
    private static void testSetPartidaAsociadaReemplaza() {
        NodoCiudad n = nodo(1, "C1");
        PartidaStub nueva = partida();
        n.setPartidaAsociada(nueva);
        reportar("setPartidaAsociada: reemplaza la partida correctamente",
                n.getPartidaAsociada() == nueva);
    }

    // ================================================================== //
    //  Tests: setCompletada / isCompletada
    // ================================================================== //

    /** setCompletada(true) marca la ciudad como completada. */
    private static void testSetCompletadaTrue() {
        NodoCiudad n = nodo(2, "C2");
        n.setCompletada(true);
        reportar("setCompletada(true): isCompletada devuelve true",
                n.isCompletada());
    }

    /** setCompletada(false) desmarca la ciudad. */
    private static void testSetCompletadaFalse() {
        NodoCiudad n = nodo(2, "C2");
        n.setCompletada(true);
        n.setCompletada(false);
        reportar("setCompletada(false): isCompletada devuelve false",
                !n.isCompletada());
    }

    // ================================================================== //
    //  Tests: agregarCamino
    // ================================================================== //

    /** agregarCamino agrega un destino válido a la lista de adyacentes. */
    private static void testAgregarCaminoValido() {
        NodoCiudad origen  = nodo(1, "C1");
        NodoCiudad destino = nodo(2, "C2");
        origen.agregarCamino(destino);
        reportar("agregarCamino: destino válido queda en adyacentes",
                origen.getAdyacentes().contains(destino));
    }

    /** agregarCamino con null no lanza excepción y no agrega nada. */
    private static void testAgregarCaminoNullIgnorado() {
        NodoCiudad n = nodo(1, "C1");
        boolean ok = false;
        try {
            n.agregarCamino(null);
            ok = n.getAdyacentes().isEmpty();
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("agregarCamino: null ignorado sin excepción",  ok);
    }

    /** agregarCamino con el mismo nodo (self-loop) no lo agrega. */
    private static void testAgregarCaminoSelfLoopIgnorado() {
        NodoCiudad n = nodo(1, "C1");
        n.agregarCamino(n);
        reportar("agregarCamino: self-loop ignorado",
                n.getAdyacentes().isEmpty());
    }

    /** agregarCamino no agrega duplicados. */
    private static void testAgregarCaminoNoDuplicados() {
        NodoCiudad origen  = nodo(1, "C1");
        NodoCiudad destino = nodo(2, "C2");
        origen.agregarCamino(destino);
        origen.agregarCamino(destino); // segunda llamada con el mismo destino
        reportar("agregarCamino: no se agregan duplicados",
                origen.getAdyacentes().size() == 1);
    }

    /** agregarCamino puede agregar múltiples destinos distintos. */
    private static void testAgregarCaminoVariosDestinos() {
        NodoCiudad origen = nodo(1, "C1");
        NodoCiudad d2     = nodo(2, "C2");
        NodoCiudad d3     = nodo(3, "C3");
        NodoCiudad d4     = nodo(4, "C4");
        origen.agregarCamino(d2);
        origen.agregarCamino(d3);
        origen.agregarCamino(d4);
        reportar("agregarCamino: múltiples destinos distintos → tamaño 3",
                origen.getAdyacentes().size() == 3);
    }

    // ================================================================== //
    //  Tests: getAdyacentes (vista no modificable)
    // ================================================================== //

    /** getAdyacentes devuelve una vista no modificable. */
    private static void testGetAdyacentesNoModificable() {
        NodoCiudad origen  = nodo(1, "C1");
        NodoCiudad destino = nodo(2, "C2");
        origen.agregarCamino(destino);
        boolean lanzó = false;
        try {
            origen.getAdyacentes().add(nodo(3, "C3"));
        } catch (UnsupportedOperationException e) {
            lanzó = true;
        }
        reportar("getAdyacentes: vista no modificable → UnsupportedOperationException",
                lanzó);
    }

    /** getAdyacentes refleja los cambios hechos via agregarCamino. */
    private static void testGetAdyacentesReflejaAgregarCamino() {
        NodoCiudad origen = nodo(1, "C1");
        NodoCiudad d2     = nodo(2, "C2");
        origen.agregarCamino(d2);
        reportar("getAdyacentes: refleja el estado actual de adyacentes",
                origen.getAdyacentes().contains(d2));
    }

    // ================================================================== //
    //  Tests: toString
    // ================================================================== //

    /** toString sigue el formato NodoCiudad{id=X, nombre='Y', completada=Z, caminos=N}. */
    private static void testToStringFormatoEsperado() {
        NodoCiudad n = nodo(7, "Ciudad Mágica");
        String esperado = "NodoCiudad{id=7, nombre='Ciudad Mágica', completada=false, caminos=0}";
        reportar("toString: formato esperado con 0 caminos y completada=false",
                esperado.equals(n.toString()));
    }

    /** toString refleja el conteo de caminos después de agregarCamino. */
    private static void testToStringReflejaCaminos() {
        NodoCiudad origen = nodo(1, "C1");
        origen.agregarCamino(nodo(2, "C2"));
        origen.agregarCamino(nodo(3, "C3"));
        String resultado = origen.toString();
        reportar("toString: caminos=2 tras agregar dos destinos",
                resultado.contains("caminos=2"));
    }

    /** toString refleja completada=true después de setCompletada(true). */
    private static void testToStringReflejaCompletada() {
        NodoCiudad n = nodo(2, "C2");
        n.setCompletada(true);
        reportar("toString: refleja completada=true",
                n.toString().contains("completada=true"));
    }

    // ================================================================== //
    //  Punto de entrada
    // ================================================================== //

    public static void main(String[] args) {
        System.out.println("=== Tests NodoCiudad ===\n");

        System.out.println("-- Constructor / estado inicial --");
        testConstructorGetId();
        testConstructorGetNombre();
        testConstructorGetPartidaAsociada();
        testConstructorCompletadaInicialFalse();
        testConstructorAdyacentesInicialVacia();

        System.out.println("\n-- setNombre --");
        testSetNombreActualiza();

        System.out.println("\n-- setPartidaAsociada --");
        testSetPartidaAsociadaReemplaza();

        System.out.println("\n-- setCompletada / isCompletada --");
        testSetCompletadaTrue();
        testSetCompletadaFalse();

        System.out.println("\n-- agregarCamino --");
        testAgregarCaminoValido();
        testAgregarCaminoNullIgnorado();
        testAgregarCaminoSelfLoopIgnorado();
        testAgregarCaminoNoDuplicados();
        testAgregarCaminoVariosDestinos();

        System.out.println("\n-- getAdyacentes --");
        testGetAdyacentesNoModificable();
        testGetAdyacentesReflejaAgregarCamino();

        System.out.println("\n-- toString --");
        testToStringFormatoEsperado();
        testToStringReflejaCaminos();
        testToStringReflejaCompletada();

        System.out.println("\n=== Fin de tests ===");
    }
}
