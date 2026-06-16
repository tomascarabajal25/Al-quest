package estructuras.cola;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestColaBasica {

    private ColaBasica<Integer> cola;

    @BeforeEach
    void setUp() {
        cola = new ColaBasica<>();
    }

    @Test
    void colaNuevaEstaVacia() {
        assertTrue(cola.estaVacia());
    }

    @Test
    void colaNuevaTieneCeroElementos() {
        assertEquals(0, cola.contarElementos());
    }

    @Test
    void despuesDeAcolarUnElementoNoEstaVacia() {
        cola.acolar(1);
        assertFalse(cola.estaVacia());
    }

    @Test
    void contarElementosAumentaConCadaAcolar() {
        cola.acolar(10);
        assertEquals(1, cola.contarElementos());
        cola.acolar(20);
        assertEquals(2, cola.contarElementos());
        cola.acolar(30);
        assertEquals(3, cola.contarElementos());
    }

    @Test
    void contarElementosDisminuyeConCadaDesacolar() {
        cola.acolar(1);
        cola.acolar(2);
        cola.acolar(3);
        cola.desacolar();
        assertEquals(2, cola.contarElementos());
    }

    @Test
    void acolarUnSoloElemento() {
        cola.acolar(42);
        assertEquals(1, cola.contarElementos());
        assertEquals(42, cola.obtener());
    }

    @Test
    void acolarPermiteDuplicados() {
        cola.acolar(5);
        cola.acolar(5);
        assertEquals(2, cola.contarElementos());
    }

    @Test
    void acolarAgregaAlFinal_verificadoConDesacolar() {
        cola.acolar(1);
        cola.acolar(2);
        cola.acolar(3);
        assertEquals(1, cola.desacolar());
        assertEquals(2, cola.desacolar());
        assertEquals(3, cola.desacolar());
    }

    @Test
    void acolarListaEncolaEnOrden() {
        cola.acolar(List.of(10, 20, 30));
        assertEquals(3, cola.contarElementos());
        assertEquals(10, cola.desacolar());
        assertEquals(20, cola.desacolar());
        assertEquals(30, cola.desacolar());
    }

    @Test
    void acolarListaVaciaNoModificaLaCola() {
        cola.acolar(1);
        cola.acolar(List.of());
        assertEquals(1, cola.contarElementos());
    }

    @Test
    void acolarListaDespuesDeElementosPreexistentes() {
        cola.acolar(1);
        cola.acolar(List.of(2, 3));
        assertEquals(3, cola.contarElementos());
        assertEquals(1, cola.desacolar());
        assertEquals(2, cola.desacolar());
        assertEquals(3, cola.desacolar());
    }

    @Test
    void acolarAllEncolaEnOrden() {
        cola.acolarAll(List.of(7, 8, 9));
        assertEquals(3, cola.contarElementos());
        assertEquals(7, cola.desacolar());
        assertEquals(8, cola.desacolar());
        assertEquals(9, cola.desacolar());
    }

    @Test
    void acolarAllConListaVaciaNoModificaLaCola() {
        cola.acolar(99);
        cola.acolarAll(List.of());
        assertEquals(1, cola.contarElementos());
    }

    @Test
    void acolarAllEsEquivalenteAAcolarLista() {
        ColaBasica<Integer> cola2 = new ColaBasica<>();
        cola.acolar(List.of(1, 2, 3));
        cola2.acolarAll(List.of(1, 2, 3));
        assertEquals(cola.contarElementos(), cola2.contarElementos());
        while (!cola.estaVacia()) {
            assertEquals(cola.desacolar(), cola2.desacolar());
        }
    }

    @Test
    void desacolarEnColaVaciaRetornaNull() {
        assertNull(cola.desacolar());
    }

    @Test
    void desacolarDevuelveElFrenteYLoElimina() {
        cola.acolar(10);
        cola.acolar(20);
        assertEquals(10, cola.desacolar());
        assertEquals(1, cola.contarElementos());
    }

    @Test
    void desacolarRespetaOrdenFIFO() {
        cola.acolar(1);
        cola.acolar(2);
        cola.acolar(3);
        assertEquals(1, cola.desacolar());
        assertEquals(2, cola.desacolar());
        assertEquals(3, cola.desacolar());
    }

    @Test
    void desacolarUnicoElementoDejaColaVacia() {
        cola.acolar(55);
        cola.desacolar();
        assertTrue(cola.estaVacia());
        assertEquals(0, cola.contarElementos());
    }

    @Test
    void desacolarTodosLosElementosDejaColaVacia() {
        cola.acolar(1);
        cola.acolar(2);
        cola.desacolar();
        cola.desacolar();
        assertTrue(cola.estaVacia());
        assertNull(cola.desacolar()); // siguiente llamada sigue retornando null
    }

    @Test
    void desacolarSeguidoDeAcolarMantieneFIFO() {
        cola.acolar(1);
        cola.acolar(2);
        cola.desacolar();       // saca 1
        cola.acolar(3);         // agrega 3
        assertEquals(2, cola.desacolar());
        assertEquals(3, cola.desacolar());
    }

    @Test
    void obtenerEnColaVaciaRetornaNull() {
        assertNull(cola.obtener());
    }

    @Test
    void obtenerDevuelveElFrenteSinEliminarlo() {
        cola.acolar(77);
        cola.acolar(88);
        assertEquals(77, cola.obtener());
        assertEquals(2, cola.contarElementos()); // no modificó
    }

    @Test
    void obtenerRepetidasVecesDevuelveSiempreElMismo() {
        cola.acolar(42);
        assertEquals(42, cola.obtener());
        assertEquals(42, cola.obtener());
        assertEquals(42, cola.obtener());
    }

    @Test
    void obtenerEsConsistenteConDesacolar() {
        cola.acolar(100);
        cola.acolar(200);
        Integer vistoPorObtener = cola.obtener();
        Integer eliminadoPorDesacolar = cola.desacolar();
        assertEquals(vistoPorObtener, eliminadoPorDesacolar);
    }


    @Test
    void encolarYDesacolarListaCompletaPreservaOrden() {
        List<Integer> original = List.of(5, 3, 8, 1, 9, 2);
        cola.acolarAll(original);
        for (int esperado : original) {
            assertEquals(esperado, cola.desacolar());
        }
        assertTrue(cola.estaVacia());
    }

    @Test
    void intercalacionDeAcolarYDesacolarMantieneFIFO() {
        cola.acolar(1);
        cola.acolar(2);
        assertEquals(1, cola.desacolar());
        cola.acolar(3);
        assertEquals(2, cola.desacolar());
        assertEquals(3, cola.desacolar());
        assertTrue(cola.estaVacia());
    }

    @Test
    void reusoDeLaColaTrasClearPorDesacolarEsCorrecto() {
        cola.acolar(10);
        cola.desacolar();
        // después de quedar vacía, se puede volver a usar normalmente
        cola.acolar(20);
        cola.acolar(30);
        assertEquals(20, cola.obtener());
        assertEquals(2, cola.contarElementos());
    }
}
