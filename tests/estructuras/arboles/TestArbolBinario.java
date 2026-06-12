package estructuras.arboles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestArbolBinario {

    private ArbolBinario arbol;

    @BeforeEach
    void setUp() {
        arbol = new ArbolBinario(3); // grado mínimo t=3 → max 5 claves por nodo
    }

    // ─────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────

    @Test
    void constructor_raizDebeSerNull() {
        assertNull(arbol.raiz);
    }

    @Test
    void constructor_debeAsignarGradoCorrectamente() {
        assertEquals(3, arbol.grado);
    }

    // ─────────────────────────────────────────────
    // Insertar: árbol vacío
    // ─────────────────────────────────────────────

    @Test
    void insertar_enArbolVacio_raizNoDebeSerNull() {
        arbol.insertar(10);
        assertNotNull(arbol.raiz);
    }

    @Test
    void insertar_enArbolVacio_raizDebeSerHoja() {
        arbol.insertar(10);
        assertTrue(arbol.raiz.hoja);
    }

    @Test
    void insertar_enArbolVacio_raizDebeTenerUnaClaveConValorCorrecto() {
        arbol.insertar(10);
        assertEquals(1, arbol.raiz.grado);
        assertEquals(10, arbol.raiz.claves[0]);
    }

    // ─────────────────────────────────────────────
    // Insertar: múltiples claves sin split
    // ─────────────────────────────────────────────

    @Test
    void insertar_variasClavesSinLlenarNodo_gradoCorrecto() {
        arbol.insertar(10);
        arbol.insertar(20);
        arbol.insertar(5);
        assertEquals(3, arbol.raiz.grado);
    }

    @Test
    void insertar_variasClaves_ordenadoEnRaiz() {
        arbol.insertar(20);
        arbol.insertar(5);
        arbol.insertar(10);
        // Deben quedar ordenadas: 5, 10, 20
        assertEquals(5,  arbol.raiz.claves[0]);
        assertEquals(10, arbol.raiz.claves[1]);
        assertEquals(20, arbol.raiz.claves[2]);
    }

    // ─────────────────────────────────────────────
    // Insertar: split (nodo lleno → crecimiento en altura)
    // ─────────────────────────────────────────────

    @Test
    void insertar_alLlenarNodo_siguienteClaveForzaSplit() {
        // t=3 → max 5 claves. La 6ta fuerza split
        for (int clave : new int[]{10, 20, 5, 6, 12}) {
            arbol.insertar(clave);
        }
        // Aún sin split
        assertTrue(arbol.raiz.hoja);

        arbol.insertar(30); // fuerza split
        assertFalse(arbol.raiz.hoja);
    }

    @Test
    void insertar_luegoDeSplit_raizDebeTenerUnaClaveYDosHijos() {
        for (int clave : new int[]{10, 20, 5, 6, 12, 30}) {
            arbol.insertar(clave);
        }
        assertFalse(arbol.raiz.hoja);
        assertEquals(1, arbol.raiz.grado);
        assertNotNull(arbol.raiz.hijos[0]);
        assertNotNull(arbol.raiz.hijos[1]);
    }

    @Test
    void insertar_luegoDeSplit_hijosDebenSerHojas() {
        for (int clave : new int[]{10, 20, 5, 6, 12, 30}) {
            arbol.insertar(clave);
        }
        assertTrue(arbol.raiz.hijos[0].hoja);
        assertTrue(arbol.raiz.hijos[1].hoja);
    }

    // ─────────────────────────────────────────────
    // Propiedades estructurales del árbol B
    // ─────────────────────────────────────────────

    @Test
    void propiedades_todosLosNodosDebenTenerALoSumoMaxClaves() {
        for (int clave : new int[]{10, 20, 5, 6, 12, 30, 7, 17}) {
            arbol.insertar(clave);
        }
        verificarMaxClaves(arbol.raiz);
    }

    @Test
    void propiedades_nodosNoRaizDebenTenerAlMenosMinClaves() {
        for (int clave : new int[]{10, 20, 5, 6, 12, 30, 7, 17}) {
            arbol.insertar(clave);
        }
        verificarMinClaves(arbol.raiz, true);
    }

    @Test
    void propiedades_clavesEnCadaNodoDebenEstarOrdenadas() {
        for (int clave : new int[]{30, 10, 50, 5, 20, 40, 60}) {
            arbol.insertar(clave);
        }
        verificarOrden(arbol.raiz);
    }

    @Test
    void propiedades_todasLasHojasDebenEstarAlMismoNivel() {
        for (int clave : new int[]{10, 20, 5, 6, 12, 30, 7, 17, 3, 1}) {
            arbol.insertar(clave);
        }
        int profundidadPrimeraHoja = profundidadPrimeraHoja(arbol.raiz, 0);
        verificarProfundidadHojas(arbol.raiz, 0, profundidadPrimeraHoja);
    }

    @Test
    void propiedades_nodoInternoConKClavesDebeTenerKMasUnHijos() {
        for (int clave : new int[]{10, 20, 5, 6, 12, 30, 7, 17}) {
            arbol.insertar(clave);
        }
        verificarCantidadHijos(arbol.raiz);
    }

    // ─────────────────────────────────────────────
    // Insertar: orden inverso
    // ─────────────────────────────────────────────

    @Test
    void insertar_enOrdenDecreciente_estructuraSigueSiendoValida() {
        for (int clave = 20; clave >= 1; clave--) {
            arbol.insertar(clave);
        }
        verificarOrden(arbol.raiz);
        verificarMaxClaves(arbol.raiz);
    }

    // ─────────────────────────────────────────────
    // Insertar: clave duplicada
    // ─────────────────────────────────────────────

    @Test
    void insertar_claveDuplicada_noRompeEstructura() {
        arbol.insertar(10);
        arbol.insertar(10);
        // El árbol no debe lanzar excepción y la estructura sigue siendo válida
        verificarOrden(arbol.raiz);
    }

    // ─────────────────────────────────────────────
    // Helpers de verificación estructural
    // ─────────────────────────────────────────────

    private void verificarMaxClaves(NodoB nodo) {
        if (nodo == null) return;
        int maxClaves = 2 * arbol.grado - 1;
        assertTrue(nodo.grado <= maxClaves,
            "Nodo tiene más claves de las permitidas: " + nodo.grado);
        for (int i = 0; i <= nodo.grado; i++) {
            verificarMaxClaves(nodo.hijos[i]);
        }
    }

    private void verificarMinClaves(NodoB nodo, boolean esRaiz) {
        if (nodo == null) return;
        int minClaves = arbol.grado - 1;
        if (!esRaiz) {
            assertTrue(nodo.grado >= minClaves,
                "Nodo no raíz tiene menos claves de las permitidas: " + nodo.grado);
        }
        for (int i = 0; i <= nodo.grado; i++) {
            verificarMinClaves(nodo.hijos[i], false);
        }
    }

    private void verificarOrden(NodoB nodo) {
        if (nodo == null) return;
        for (int i = 0; i < nodo.grado - 1; i++) {
            assertTrue(nodo.claves[i] <= nodo.claves[i + 1],
                "Claves desordenadas en nodo: " + nodo.claves[i] + " > " + nodo.claves[i + 1]);
        }
        for (int i = 0; i <= nodo.grado; i++) {
            verificarOrden(nodo.hijos[i]);
        }
    }

    private int profundidadPrimeraHoja(NodoB nodo, int nivel) {
        if (nodo == null || nodo.hoja) return nivel;
        return profundidadPrimeraHoja(nodo.hijos[0], nivel + 1);
    }

    private void verificarProfundidadHojas(NodoB nodo, int nivelActual, int nivelEsperado) {
        if (nodo == null) return;
        if (nodo.hoja) {
            assertEquals(nivelEsperado, nivelActual,
                "Hoja encontrada a nivel incorrecto: " + nivelActual);
            return;
        }
        for (int i = 0; i <= nodo.grado; i++) {
            verificarProfundidadHojas(nodo.hijos[i], nivelActual + 1, nivelEsperado);
        }
    }

    private void verificarCantidadHijos(NodoB nodo) {
        if (nodo == null || nodo.hoja) return;
        int hijosNoNull = 0;
        for (int i = 0; i <= nodo.grado; i++) {
            if (nodo.hijos[i] != null) hijosNoNull++;
        }
        assertEquals(nodo.grado + 1, hijosNoNull,
            "Nodo con " + nodo.grado + " claves debe tener " + (nodo.grado + 1) + " hijos");
        for (int i = 0; i <= nodo.grado; i++) {
            verificarCantidadHijos(nodo.hijos[i]);
        }
    }
}
