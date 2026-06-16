package estructuras.arboles;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestNodoB {

    @Test
    void constructor_debeAsignarGradoMinimo() {
        NodoB nodo = new NodoB(3, true);
        assertEquals(3, nodo.gradoMinimo);
    }

    @Test
    void constructor_conT2_debeAsignarGradoMinimo() {
        NodoB nodo = new NodoB(2, true);
        assertEquals(2, nodo.gradoMinimo);
    }

    @Test
    void constructor_conT3_clavesDebeTener5Posiciones() {
        NodoB nodo = new NodoB(3, true);
        assertEquals(5, nodo.claves.length); // 2*3 - 1 = 5
    }

    @Test
    void constructor_conT3_hijosDebeTener6Posiciones() {
        NodoB nodo = new NodoB(3, true);
        assertEquals(6, nodo.hijos.length); // 2*3 = 6
    }

    @Test
    void constructor_conT2_clavesDebeTener3Posiciones() {
        NodoB nodo = new NodoB(2, true);
        assertEquals(3, nodo.claves.length); // 2*2 - 1 = 3
    }

    @Test
    void constructor_conT2_hijosDebeTener4Posiciones() {
        NodoB nodo = new NodoB(2, true);
        assertEquals(4, nodo.hijos.length); // 2*2 = 4
    }

    @Test
    void constructor_conT1_casoBorde_clavesDebeTener1Posicion() {
        NodoB nodo = new NodoB(1, true);
        assertEquals(1, nodo.claves.length); // 2*1 - 1 = 1
    }

    @Test
    void constructor_conT1_casoBorde_hijosDebeTener2Posiciones() {
        NodoB nodo = new NodoB(1, true);
        assertEquals(2, nodo.hijos.length); // 2*1 = 2
    }

    @Test
    void constructor_gradoDebeInicializarEnCero() {
        NodoB nodo = new NodoB(3, true);
        assertEquals(0, nodo.grado);
    }

    @Test
    void constructor_gradoDebeInicializarEnCero_siendoNoHoja() {
        NodoB nodo = new NodoB(3, false);
        assertEquals(0, nodo.grado);
    }

    @Test
    void constructor_conHojaTrue_debeSerHoja() {
        NodoB nodo = new NodoB(3, true);
        assertTrue(nodo.hoja);
    }

    @Test
    void constructor_conHojaFalse_noDebeSerHoja() {
        NodoB nodo = new NodoB(3, false);
        assertFalse(nodo.hoja);
    }


    @Test
    void constructor_hijosDebenInicializarseEnNull() {
        NodoB nodo = new NodoB(3, false);
        for (NodoB hijo : nodo.hijos) {
            assertNull(hijo);
        }
    }

    @Test
    void constructor_clavesDebenInicializarseEnCero() {
        NodoB nodo = new NodoB(3, true);
        for (int clave : nodo.claves) {
            assertEquals(0, clave);
        }
    }

    @Test
    void asignarClave_debeReflejarseEnElArray() {
        NodoB nodo = new NodoB(3, true);
        nodo.claves[0] = 42;
        assertEquals(42, nodo.claves[0]);
    }

    @Test
    void asignarGrado_debeActualizarseCorrectamente() {
        NodoB nodo = new NodoB(3, true);
        nodo.grado = 3;
        assertEquals(3, nodo.grado);
    }

    @Test
    void asignarHijo_debeReflejarseEnElArray() {
        NodoB padre = new NodoB(3, false);
        NodoB hijo  = new NodoB(3, true);
        padre.hijos[0] = hijo;
        assertSame(hijo, padre.hijos[0]);
    }

    @Test
    void asignarHoja_debeActualizarseCorrectamente() {
        NodoB nodo = new NodoB(3, true);
        nodo.hoja = false;
        assertFalse(nodo.hoja);
    }
}
