package modelos;

import java.util.Objects;

/**
 * TDA Test para Entidad (clase abstracta).
 *
 * Como Entidad es abstracta se usa una subclase concreta mínima —
 * EntidadConcreta— definida al final de este archivo, que no agrega
 * ningún comportamiento propio para no contaminar los tests.
 *
 * Cubre:
 *   - Constructor: nombre válido almacenado correctamente
 *   - getNombre: devuelve el nombre con el que fue creada
 *   - toString: formato esperado "Entidad [nombre=X]"
 *   - hashCode: misma instancia, instancias con igual nombre e instancias distintas
 *   - equals: reflexividad, simetría, transitividad, null, clase distinta,
 *             mismo nombre y distinto nombre
 */
public class TestEntidad {

    // ------------------------------------------------------------------ //
    //  Subclase concreta mínima (sólo para poder instanciar Entidad)
    // ------------------------------------------------------------------ //

    private static class EntidadConcreta extends Entidad {
        public EntidadConcreta(String nombre) {
            super(nombre);
        }
    }

    // ------------------------------------------------------------------ //
    //  Helper de reporte
    // ------------------------------------------------------------------ //

    private static void reportar(String nombre, boolean ok) {
        System.out.println((ok ? "[OK]  " : "[FAIL]") + " " + nombre);
    }

    // ------------------------------------------------------------------ //
    //  Tests: Constructor / getNombre
    // ------------------------------------------------------------------ //

    /** getNombre devuelve el nombre con el que fue construida la entidad. */
    private static void testGetNombreDevuelveNombreCorrect() {
        Entidad e = new EntidadConcreta("Personaje");
        reportar("getNombre: devuelve el nombre correcto",
                "Personaje".equals(e.getNombre()));
    }

    /** El nombre se conserva intacto (sin transformaciones). */
    private static void testGetNombreConservaCasoYEspacios() {
        Entidad e = new EntidadConcreta("  Nombre Con Espacios  ");
        reportar("getNombre: conserva mayúsculas y espacios",
                "  Nombre Con Espacios  ".equals(e.getNombre()));
    }

    // ------------------------------------------------------------------ //
    //  Tests: toString
    // ------------------------------------------------------------------ //

    /** toString sigue el formato "Entidad [nombre=X]". */
    private static void testToStringFormatoEsperado() {
        Entidad e = new EntidadConcreta("Héroe");
        String esperado = "Entidad [nombre=Héroe]";
        reportar("toString: formato 'Entidad [nombre=X]'",
                esperado.equals(e.toString()));
    }

    /** toString con nombre vacío mantiene el formato. */
    private static void testToStringNombreVacio() {
        Entidad e = new EntidadConcreta("");
        String esperado = "Entidad [nombre=]";
        reportar("toString: nombre vacío → 'Entidad [nombre=]'",
                esperado.equals(e.toString()));
    }

    // ------------------------------------------------------------------ //
    //  Tests: hashCode
    // ------------------------------------------------------------------ //

    /** Una misma instancia produce el mismo hashCode en llamadas sucesivas. */
    private static void testHashCodeConsistente() {
        Entidad e = new EntidadConcreta("A");
        reportar("hashCode: consistente entre llamadas sucesivas",
                e.hashCode() == e.hashCode());
    }

    /** Dos instancias con el mismo nombre tienen el mismo hashCode. */
    private static void testHashCodeIgualNombreIgualHash() {
        Entidad e1 = new EntidadConcreta("Vilano");
        Entidad e2 = new EntidadConcreta("Vilano");
        reportar("hashCode: mismo nombre → mismo hashCode",
                e1.hashCode() == e2.hashCode());
    }

    /** El hashCode coincide con el calculado mediante Objects.hash(nombre). */
    private static void testHashCodeValorEsperado() {
        String nombre = "Guerrero";
        Entidad e = new EntidadConcreta(nombre);
        int esperado = Objects.hash(nombre);
        reportar("hashCode: valor igual a Objects.hash(nombre)",
                e.hashCode() == esperado);
    }

    // ------------------------------------------------------------------ //
    //  Tests: equals
    // ------------------------------------------------------------------ //

    /** Reflexividad: una entidad es igual a sí misma. */
    private static void testEqualsReflexivo() {
        Entidad e = new EntidadConcreta("X");
        reportar("equals: reflexividad (e.equals(e))",
                e.equals(e));
    }

    /** Simetría: si e1.equals(e2) entonces e2.equals(e1). */
    private static void testEqualsSimetrico() {
        Entidad e1 = new EntidadConcreta("Dragón");
        Entidad e2 = new EntidadConcreta("Dragón");
        reportar("equals: simetría (e1=e2 ↔ e2=e1)",
                e1.equals(e2) && e2.equals(e1));
    }

    /** Transitividad: e1=e2 y e2=e3 implica e1=e3. */
    private static void testEqualsTransitivo() {
        Entidad e1 = new EntidadConcreta("Orco");
        Entidad e2 = new EntidadConcreta("Orco");
        Entidad e3 = new EntidadConcreta("Orco");
        reportar("equals: transitividad (e1=e2, e2=e3 → e1=e3)",
                e1.equals(e2) && e2.equals(e3) && e1.equals(e3));
    }

    /** equals con null siempre devuelve false. */
    private static void testEqualsConNull() {
        Entidad e = new EntidadConcreta("Elfo");
        reportar("equals: comparación con null → false",
                !e.equals(null));
    }

    /** equals con un objeto de clase distinta devuelve false. */
    private static void testEqualsClaseDistinta() {
        Entidad e = new EntidadConcreta("Enano");
        reportar("equals: clase distinta → false",
                !e.equals("Enano"));
    }

    /** Dos instancias con el mismo nombre son iguales. */
    private static void testEqualsIgualNombre() {
        Entidad e1 = new EntidadConcreta("Mago");
        Entidad e2 = new EntidadConcreta("Mago");
        reportar("equals: mismo nombre → true",
                e1.equals(e2));
    }

    /** Dos instancias con distinto nombre no son iguales. */
    private static void testEqualsDistintoNombre() {
        Entidad e1 = new EntidadConcreta("Mago");
        Entidad e2 = new EntidadConcreta("Hechicero");
        reportar("equals: distinto nombre → false",
                !e1.equals(e2));
    }

    /**
     * Consistencia equals/hashCode:
     * si equals devuelve true, hashCode debe ser igual.
     */
    private static void testEqualsHashCodeConsistentes() {
        Entidad e1 = new EntidadConcreta("Paladin");
        Entidad e2 = new EntidadConcreta("Paladin");
        boolean iguales = e1.equals(e2);
        boolean hashIgual = e1.hashCode() == e2.hashCode();
        reportar("equals/hashCode: si equals→true entonces hashCode igual",
                iguales && hashIgual);
    }

    // ------------------------------------------------------------------ //
    //  Punto de entrada
    // ------------------------------------------------------------------ //

    public static void main(String[] args) {
        System.out.println("=== Tests Entidad ===\n");

        System.out.println("-- Constructor / getNombre --");
        testGetNombreDevuelveNombreCorrect();
        testGetNombreConservaCasoYEspacios();

        System.out.println("\n-- toString --");
        testToStringFormatoEsperado();
        testToStringNombreVacio();

        System.out.println("\n-- hashCode --");
        testHashCodeConsistente();
        testHashCodeIgualNombreIgualHash();
        testHashCodeValorEsperado();

        System.out.println("\n-- equals --");
        testEqualsReflexivo();
        testEqualsSimetrico();
        testEqualsTransitivo();
        testEqualsConNull();
        testEqualsClaseDistinta();
        testEqualsIgualNombre();
        testEqualsDistintoNombre();
        testEqualsHashCodeConsistentes();

        System.out.println("\n=== Fin de tests ===");
    }
}
