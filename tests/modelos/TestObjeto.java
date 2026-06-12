package modelos;

/**
 * TDA Test para Objeto.
 *
 * Objeto extiende Entidad (abstracta), por lo que es directamente instanciable.
 *
 * Cubre:
 *   - Constructor: nombre y colisión almacenados correctamente
 *   - getColision: true y false según lo pasado al constructor
 *   - Herencia de Entidad: getNombre, toString, equals, hashCode
 *   - Precondición heredada: nombre null (según ValidacionesUtiles en Entidad)
 */
public class TestObjeto {

    // ------------------------------------------------------------------ //
    //  Helper de reporte
    // ------------------------------------------------------------------ //

    private static void reportar(String nombre, boolean ok) {
        System.out.println((ok ? "[OK]  " : "[FAIL]") + " " + nombre);
    }

    // ================================================================== //
    //  Tests: Constructor / getColision
    // ================================================================== //

    /** Constructor con colision=true: getColision devuelve true. */
    private static void testConstructorColisionTrue() {
        Objeto obj = new Objeto("Espada", true);
        reportar("Constructor: colision=true → getColision devuelve true",
                obj.getColision());
    }

    /** Constructor con colision=false: getColision devuelve false. */
    private static void testConstructorColisionFalse() {
        Objeto obj = new Objeto("Fantasma", false);
        reportar("Constructor: colision=false → getColision devuelve false",
                !obj.getColision());
    }

    /** El nombre queda accesible a través de getNombre (heredado de Entidad). */
    private static void testConstructorNombreHeredado() {
        Objeto obj = new Objeto("Escudo", false);
        reportar("Constructor: nombre accesible via getNombre (herencia)",
                "Escudo".equals(obj.getNombre()));
    }

    // ================================================================== //
    //  Tests: Herencia de Entidad — toString
    // ================================================================== //

    /** toString sigue el formato definido en Entidad: "Entidad [nombre=X]". */
    private static void testToStringFormatoEntidad() {
        Objeto obj = new Objeto("Poción", true);
        String esperado = "Entidad [nombre=Poción]";
        reportar("toString: hereda formato de Entidad 'Entidad [nombre=X]'",
                esperado.equals(obj.toString()));
    }

    // ================================================================== //
    //  Tests: Herencia de Entidad — equals
    // ================================================================== //

    /** Dos objetos con el mismo nombre son iguales (igualdad por nombre heredada). */
    private static void testEqualsIgualNombre() {
        Objeto o1 = new Objeto("Llave", true);
        Objeto o2 = new Objeto("Llave", false); // distinta colisión, mismo nombre
        reportar("equals: mismo nombre (distinta colisión) → true",
                o1.equals(o2));
    }

    /** Dos objetos con distinto nombre no son iguales. */
    private static void testEqualsDistintoNombre() {
        Objeto o1 = new Objeto("Llave", true);
        Objeto o2 = new Objeto("Cofre", true);
        reportar("equals: distinto nombre → false",
                !o1.equals(o2));
    }

    /** Un objeto no es igual a null. */
    private static void testEqualsConNull() {
        Objeto obj = new Objeto("Mapa", false);
        reportar("equals: comparación con null → false",
                !obj.equals(null));
    }

    /** Reflexividad: un objeto es igual a sí mismo. */
    private static void testEqualsReflexivo() {
        Objeto obj = new Objeto("Antorcha", true);
        reportar("equals: reflexividad (obj.equals(obj))",
                obj.equals(obj));
    }

    /** Un objeto no es igual a una instancia de otra clase con el mismo nombre. */
    private static void testEqualsClaseDistinta() {
        Objeto obj = new Objeto("Roca", false);
        reportar("equals: clase distinta → false",
                !obj.equals("Roca"));
    }

    // ================================================================== //
    //  Tests: Herencia de Entidad — hashCode
    // ================================================================== //

    /** Dos objetos con el mismo nombre tienen el mismo hashCode. */
    private static void testHashCodeMismoNombre() {
        Objeto o1 = new Objeto("Gema", true);
        Objeto o2 = new Objeto("Gema", false);
        reportar("hashCode: mismo nombre → mismo hashCode",
                o1.hashCode() == o2.hashCode());
    }

    /** El hashCode es consistente entre llamadas sucesivas. */
    private static void testHashCodeConsistente() {
        Objeto obj = new Objeto("Pergamino", false);
        reportar("hashCode: consistente entre llamadas sucesivas",
                obj.hashCode() == obj.hashCode());
    }

    // ================================================================== //
    //  Tests: Precondición — nombre null
    // ================================================================== //

    /**
     * El constructor debe lanzar excepción si el nombre es null
     * (precondición validada por Entidad vía ValidacionesUtiles).
     */
    private static void testConstructorNombreNullLanzaExcepcion() {
        boolean lanzó = false;
        try {
            new Objeto(null, false);
        } catch (Exception e) {
            lanzó = true;
        }
        reportar("Constructor: nombre null → lanza excepción", lanzó);
    }

    // ================================================================== //
    //  Punto de entrada
    // ================================================================== //

    public static void main(String[] args) {
        System.out.println("=== Tests Objeto ===\n");

        System.out.println("-- Constructor / getColision --");
        testConstructorColisionTrue();
        testConstructorColisionFalse();
        testConstructorNombreHeredado();

        System.out.println("\n-- toString (herencia Entidad) --");
        testToStringFormatoEntidad();

        System.out.println("\n-- equals (herencia Entidad) --");
        testEqualsIgualNombre();
        testEqualsDistintoNombre();
        testEqualsConNull();
        testEqualsReflexivo();
        testEqualsClaseDistinta();

        System.out.println("\n-- hashCode (herencia Entidad) --");
        testHashCodeMismoNombre();
        testHashCodeConsistente();

        System.out.println("\n-- Precondición: nombre null --");
        testConstructorNombreNullLanzaExcepcion();

        System.out.println("\n=== Fin de tests ===");
    }
}
