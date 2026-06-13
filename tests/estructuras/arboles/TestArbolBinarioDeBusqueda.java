package estructuras.arboles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestArbolBinarioDeBusqueda {

    @Test
    public void buscarElementoInsertadoDevuelveTrue() {
        ArbolBinarioDeBusqueda<String> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.insertar("40");
        assertTrue(arbol.buscar("40"));
    }

    @Test
    public void buscarElementoNoInsertadoDevuelveFalse() {
        ArbolBinarioDeBusqueda<String> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.insertar("50");
        assertFalse(arbol.buscar("99"));
    }

    @Test
    public void buscarEnArbolVacioDevuelveFalse() {
        ArbolBinarioDeBusqueda<String> arbol = new ArbolBinarioDeBusqueda<>();
        assertFalse(arbol.buscar("50"));
    }

    @Test
    public void inordenNoLanzaExcepcionConArbolLleno() {
        ArbolBinarioDeBusqueda<String> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.insertar("50");
        arbol.insertar("30");
        arbol.insertar("70");
        arbol.insertar("20");
        arbol.insertar("40");
        arbol.insertar("60");
        arbol.insertar("80");

        assertDoesNotThrow(arbol::inorden);
    }

    @Test
    public void inordenNoLanzaExcepcionConArbolVacio() {
        ArbolBinarioDeBusqueda<String> arbol = new ArbolBinarioDeBusqueda<>();
        assertDoesNotThrow(arbol::inorden);
    }

    @Test
    public void inordenNoLanzaExcepcionConUnSoloElemento() {
        ArbolBinarioDeBusqueda<String> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.insertar("50");
        assertDoesNotThrow(arbol::inorden);
    }

    @Test
    public void preordenNoLanzaExcepcionConArbolLleno() {
        ArbolBinarioDeBusqueda<String> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.insertar("50");
        arbol.insertar("30");
        arbol.insertar("70");
        arbol.insertar("20");
        arbol.insertar("40");
        arbol.insertar("60");
        arbol.insertar("80");

        assertDoesNotThrow(arbol::preorden);
    }

    @Test
    public void preordenNoLanzaExcepcionConArbolVacio() {
        ArbolBinarioDeBusqueda<String> arbol = new ArbolBinarioDeBusqueda<>();
        assertDoesNotThrow(arbol::preorden);
    }

    @Test
    public void preordenNoLanzaExcepcionConUnSoloElemento() {
        ArbolBinarioDeBusqueda<String> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.insertar("50");
        assertDoesNotThrow(arbol::preorden);
    }

    @Test
    public void postordenNoLanzaExcepcionConArbolLleno() {
        ArbolBinarioDeBusqueda<String> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.insertar("50");
        arbol.insertar("30");
        arbol.insertar("70");
        arbol.insertar("20");
        arbol.insertar("40");
        arbol.insertar("60");
        arbol.insertar("80");

        assertDoesNotThrow(arbol::postorden);
    }

    @Test
    public void postordenNoLanzaExcepcionConArbolVacio() {
        ArbolBinarioDeBusqueda<String> arbol = new ArbolBinarioDeBusqueda<>();
        assertDoesNotThrow(arbol::postorden);
    }

    @Test
    public void postordenNoLanzaExcepcionConUnSoloElemento() {
        ArbolBinarioDeBusqueda<String> arbol = new ArbolBinarioDeBusqueda<>();
        arbol.insertar("50");
        assertDoesNotThrow(arbol::postorden);
    }
}
