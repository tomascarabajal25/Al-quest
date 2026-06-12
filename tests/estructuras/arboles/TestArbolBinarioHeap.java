package estructuras.arboles;

import java.util.Comparator;
import java.util.NoSuchElementException;

/**
 * TDA Test para ArbolBinarioHeap.
 *
 * Se prueba tanto como Min-Heap (Comparator natural) como Max-Heap (Comparator inverso)
 * para verificar que el comportamiento depende únicamente del Comparator inyectado.
 *
 * Cubre:
 *   - Constructor: estado inicial (isEmpty=true, size=0)
 *   - Constructor con capacidad explícita
 *   - insert: un elemento, múltiples elementos, propiedad de orden (Min y Max)
 *   - peek: devuelve la raíz sin extraer, lanza excepción en heap vacío
 *   - extract: devuelve y elimina la raíz, mantiene propiedad tras extracción,
 *              extracción total (heap queda vacío), lanza excepción en heap vacío
 *   - isEmpty / size: después de insert y extract
 *   - ensureCapacity: insertar más allá de la capacidad inicial sin excepción
 *   - Orden de extracción completo: los N elementos salen ordenados
 */
public class TestArbolBinarioHeap {

    // ------------------------------------------------------------------ //
    //  Comparators de uso general
    // ------------------------------------------------------------------ //

    /** Comparator para Min-Heap de enteros. */
    private static final Comparator<Integer> MIN = Integer::compareTo;

    /** Comparator para Max-Heap de enteros (orden inverso). */
    private static final Comparator<Integer> MAX = (a, b) -> Integer.compare(b, a);

    // ------------------------------------------------------------------ //
    //  Helper de reporte
    // ------------------------------------------------------------------ //

    private static void reportar(String nombre, boolean ok) {
        System.out.println((ok ? "[OK]  " : "[FAIL]") + " " + nombre);
    }

    // ================================================================== //
    //  Tests: Constructor / estado inicial
    // ================================================================== //

    /** Heap recién creado está vacío. */
    private static void testConstructorIsEmpty() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        reportar("Constructor: isEmpty() es true al crear el heap", h.isEmpty());
    }

    /** Heap recién creado tiene tamaño 0. */
    private static void testConstructorSizeCero() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        reportar("Constructor: size() es 0 al crear el heap", h.size() == 0);
    }

    /** Constructor con capacidad explícita no lanza excepción. */
    private static void testConstructorCapacidadExplicita() {
        boolean ok = false;
        try {
            ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN, 5);
            ok = h.isEmpty();
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("Constructor(capacity): crea heap vacío sin excepción", ok);
    }

    // ================================================================== //
    //  Tests: isEmpty / size
    // ================================================================== //

    /** isEmpty devuelve false después de un insert. */
    private static void testIsEmptyDespuesDeInsert() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(10);
        reportar("isEmpty: false después de un insert", !h.isEmpty());
    }

    /** size refleja la cantidad de elementos insertados. */
    private static void testSizeRefleja() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(1);
        h.insert(2);
        h.insert(3);
        reportar("size: devuelve 3 tras tres inserts", h.size() == 3);
    }

    /** isEmpty vuelve a true después de extraer el único elemento. */
    private static void testIsEmptyDespuesDeExtractTotal() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(42);
        h.extract();
        reportar("isEmpty: true después de extraer el único elemento", h.isEmpty());
    }

    /** size decrece en 1 por cada extract. */
    private static void testSizeDecreceConExtract() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(1);
        h.insert(2);
        h.extract();
        reportar("size: decrece a 1 tras un extract de dos elementos", h.size() == 1);
    }

    // ================================================================== //
    //  Tests: peek
    // ================================================================== //

    /** peek devuelve el mínimo en un Min-Heap sin modificar el tamaño. */
    private static void testPeekMinHeap() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(5);
        h.insert(2);
        h.insert(8);
        int raiz = h.peek();
        reportar("peek (Min-Heap): devuelve el mínimo (2)", raiz == 2);
    }

    /** peek devuelve el máximo en un Max-Heap sin modificar el tamaño. */
    private static void testPeekMaxHeap() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MAX);
        h.insert(5);
        h.insert(2);
        h.insert(9);
        int raiz = h.peek();
        reportar("peek (Max-Heap): devuelve el máximo (9)", raiz == 9);
    }

    /** peek no modifica el tamaño del heap. */
    private static void testPeekNoModificaTamanio() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(3);
        h.insert(1);
        h.peek();
        reportar("peek: no modifica el tamaño (sigue siendo 2)", h.size() == 2);
    }

    /** peek en heap vacío lanza NoSuchElementException. */
    private static void testPeekVacioLanzaExcepcion() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        boolean lanzó = false;
        try {
            h.peek();
        } catch (NoSuchElementException e) {
            lanzó = true;
        }
        reportar("peek: heap vacío → NoSuchElementException", lanzó);
    }

    // ================================================================== //
    //  Tests: extract
    // ================================================================== //

    /** extract devuelve el mínimo y lo elimina del Min-Heap. */
    private static void testExtractMinHeap() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(7);
        h.insert(3);
        h.insert(5);
        int extraido = h.extract();
        reportar("extract (Min-Heap): devuelve el mínimo (3)", extraido == 3);
    }

    /** extract devuelve el máximo y lo elimina del Max-Heap. */
    private static void testExtractMaxHeap() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MAX);
        h.insert(7);
        h.insert(3);
        h.insert(10);
        int extraido = h.extract();
        reportar("extract (Max-Heap): devuelve el máximo (10)", extraido == 10);
    }

    /** Tras extract, peek devuelve el nuevo mínimo (propiedad del heap restaurada). */
    private static void testExtractRestaurapropiedadMinHeap() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(4);
        h.insert(1);
        h.insert(7);
        h.insert(2);
        h.extract(); // extrae el 1
        reportar("extract: propiedad Min-Heap restaurada (nueva raíz = 2)",
                h.peek() == 2);
    }

    /** Tras extract, peek devuelve el nuevo máximo (propiedad del heap restaurada). */
    private static void testExtractRestaurapropiedadMaxHeap() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MAX);
        h.insert(4);
        h.insert(9);
        h.insert(3);
        h.insert(6);
        h.extract(); // extrae el 9
        reportar("extract: propiedad Max-Heap restaurada (nueva raíz = 6)",
                h.peek() == 6);
    }

    /** extract en heap vacío lanza NoSuchElementException. */
    private static void testExtractVacioLanzaExcepcion() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        boolean lanzó = false;
        try {
            h.extract();
        } catch (NoSuchElementException e) {
            lanzó = true;
        }
        reportar("extract: heap vacío → NoSuchElementException", lanzó);
    }

    /** extract en heap con un solo elemento deja el heap vacío. */
    private static void testExtractUnicoElementoQuedaVacio() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(99);
        h.extract();
        reportar("extract: heap con un elemento → queda vacío", h.isEmpty());
    }

    // ================================================================== //
    //  Tests: orden de extracción completo
    // ================================================================== //

    /**
     * Extraer todos los elementos de un Min-Heap debe producirlos en orden
     * ascendente (HeapSort).
     */
    private static void testOrdenAscendenteMinHeap() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        int[] valores = {5, 2, 8, 1, 9, 3};
        for (int v : valores) h.insert(v);

        int[] esperado = {1, 2, 3, 5, 8, 9};
        boolean ordenCorrecto = true;
        for (int exp : esperado) {
            int extraido = h.extract();
            if (extraido != exp) {
                ordenCorrecto = false;
                System.out.println("  esperado=" + exp + " obtenido=" + extraido);
            }
        }
        reportar("extract (Min-Heap): orden ascendente completo", ordenCorrecto);
    }

    /**
     * Extraer todos los elementos de un Max-Heap debe producirlos en orden
     * descendente.
     */
    private static void testOrdenDescendenteMaxHeap() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MAX);
        int[] valores = {5, 2, 8, 1, 9, 3};
        for (int v : valores) h.insert(v);

        int[] esperado = {9, 8, 5, 3, 2, 1};
        boolean ordenCorrecto = true;
        for (int exp : esperado) {
            int extraido = h.extract();
            if (extraido != exp) {
                ordenCorrecto = false;
                System.out.println("  esperado=" + exp + " obtenido=" + extraido);
            }
        }
        reportar("extract (Max-Heap): orden descendente completo", ordenCorrecto);
    }

    // ================================================================== //
    //  Tests: ensureCapacity (redimensionamiento automático)
    // ================================================================== //

    /**
     * Insertar más elementos que la capacidad inicial no lanza excepción
     * y todos los elementos quedan accesibles en orden.
     */
    private static void testEnsureCapacityRedimensiona() {
        // Capacidad inicial = 4; insertamos 10 elementos
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN, 4);
        boolean ok = false;
        try {
            for (int i = 10; i >= 1; i--) h.insert(i);
            // Si se redimensionó bien, el mínimo debe ser 1
            ok = h.size() == 10 && h.peek() == 1;
        } catch (Exception e) {
            System.out.println("  excepción inesperada: " + e.getMessage());
        }
        reportar("ensureCapacity: insertar > capacidad inicial → redimensiona sin excepción",
                ok);
    }

    // ================================================================== //
    //  Tests: con Strings (tipo genérico distinto)
    // ================================================================== //

    /** El heap funciona con Strings usando orden lexicográfico (Min-Heap). */
    private static void testMinHeapConStrings() {
        ArbolBinarioHeap<String> h = new ArbolBinarioHeap<>(String::compareTo);
        h.insert("manzana");
        h.insert("banana");
        h.insert("cereza");
        reportar("Min-Heap con Strings: raíz es 'banana' (menor lexicográfico)",
                "banana".equals(h.peek()));
    }

    // ================================================================== //
    //  Punto de entrada
    // ================================================================== //

    public static void main(String[] args) {
        System.out.println("=== Tests ArbolBinarioHeap ===\n");

        System.out.println("-- Constructor / estado inicial --");
        testConstructorIsEmpty();
        testConstructorSizeCero();
        testConstructorCapacidadExplicita();

        System.out.println("\n-- isEmpty / size --");
        testIsEmptyDespuesDeInsert();
        testSizeRefleja();
        testIsEmptyDespuesDeExtractTotal();
        testSizeDecreceConExtract();

        System.out.println("\n-- peek --");
        testPeekMinHeap();
        testPeekMaxHeap();
        testPeekNoModificaTamanio();
        testPeekVacioLanzaExcepcion();

        System.out.println("\n-- extract --");
        testExtractMinHeap();
        testExtractMaxHeap();
        testExtractRestaurapropiedadMinHeap();
        testExtractRestaurapropiedadMaxHeap();
        testExtractVacioLanzaExcepcion();
        testExtractUnicoElementoQuedaVacio();

        System.out.println("\n-- Orden de extracción completo --");
        testOrdenAscendenteMinHeap();
        testOrdenDescendenteMaxHeap();

        System.out.println("\n-- ensureCapacity --");
        testEnsureCapacityRedimensiona();

        System.out.println("\n-- Tipo genérico (String) --");
        testMinHeapConStrings();

        System.out.println("\n=== Fin de tests ===");
    }
}
