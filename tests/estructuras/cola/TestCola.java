package estructuras.cola;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TestCola {

    private Cola<Integer> cola;

    @BeforeEach
    void setUp() {
        cola = new Cola<>();
    }

    @Test
    void colaNuevaEstaVacia() {
        assertTrue(cola.isEmpty());
    }

    @Test
    void colaNuevaTieneTamanoCero() {
        assertEquals(0, cola.size());
    }

    @Test
    void despuesDeEncolarUnElementoNoEstaVacia() {
        cola.offer(1);
        assertFalse(cola.isEmpty());
        assertEquals(1, cola.size());
    }

    @Test
    void tamanoAumentaConCadaEncolamiento() {
        cola.offer(1);
        cola.offer(2);
        cola.offer(3);
        assertEquals(3, cola.size());
    }

    @Test
    void tamanoDisminuyeConCadaDescolamiento() {
        cola.offer(1);
        cola.offer(2);
        cola.offer(3);
        cola.poll();
        assertEquals(2, cola.size());
    }

    @Test
    void offerRetornaSiempreTrue() {
        assertTrue(cola.offer(10));
    }

    @Test
    void addRetornaSiempreTrue() {
        assertTrue(cola.add(10));
    }

    @Test
    void offerYAddSonEquivalentes() {
        cola.offer(1);
        cola.add(2);
        assertEquals(2, cola.size());
    }

    @Test
    void offerPermiteEncolarNull() {
        assertTrue(cola.offer(null));
        assertEquals(1, cola.size());
    }

    @Test
    void offerPermiteEncolarDuplicados() {
        cola.offer(5);
        cola.offer(5);
        assertEquals(2, cola.size());
    }

    @Test
    void pollEnColaVaciaRetornaNull() {
        assertNull(cola.poll());
    }

    @Test
    void pollDevuelveYEliminaElPrimero() {
        cola.offer(10);
        cola.offer(20);
        assertEquals(10, cola.poll());
        assertEquals(1, cola.size());
    }

    @Test
    void pollRespetaOrdenFIFO() {
        cola.offer(1);
        cola.offer(2);
        cola.offer(3);
        assertEquals(1, cola.poll());
        assertEquals(2, cola.poll());
        assertEquals(3, cola.poll());
    }

    @Test
    void pollEnColaConUnElementoLaDejaVacia() {
        cola.offer(42);
        cola.poll();
        assertTrue(cola.isEmpty());
    }

    @Test
    void pollTodosLosElementosDejaColaVacia() {
        cola.offer(1);
        cola.offer(2);
        cola.poll();
        cola.poll();
        assertTrue(cola.isEmpty());
        assertNull(cola.poll());
    }

    @Test
    void removeEnColaVaciaLanzaExcepcion() {
        assertThrows(NoSuchElementException.class, () -> cola.remove());
    }

    @Test
    void removeDevuelveElPrimeroYLoElimina() {
        cola.offer(7);
        cola.offer(8);
        assertEquals(7, cola.remove());
        assertEquals(1, cola.size());
    }

    @Test
    void removeRespetaOrdenFIFO() {
        cola.offer(10);
        cola.offer(20);
        cola.offer(30);
        assertEquals(10, cola.remove());
        assertEquals(20, cola.remove());
        assertEquals(30, cola.remove());
    }

    @Test
    void peekEnColaVaciaRetornaNull() {
        assertNull(cola.peek());
    }

    @Test
    void peekDevuelveElPrimeroSinEliminarlo() {
        cola.offer(99);
        cola.offer(100);
        assertEquals(99, cola.peek());
        assertEquals(2, cola.size()); // no modificó
    }

    @Test
    void peekNoAlteraElOrden() {
        cola.offer(1);
        cola.offer(2);
        cola.peek();
        assertEquals(1, cola.poll());
        assertEquals(2, cola.poll());
    }

    @Test
    void elementEnColaVaciaLanzaExcepcion() {
        assertThrows(NoSuchElementException.class, () -> cola.element());
    }

    @Test
    void elementDevuelveElPrimeroSinEliminarlo() {
        cola.offer(55);
        cola.offer(66);
        assertEquals(55, cola.element());
        assertEquals(2, cola.size());
    }

    @Test
    void containsRetornaFalseEnColaVacia() {
        assertFalse(cola.contains(1));
    }

    @Test
    void containsRetornaTrueSiElementoEsta() {
        cola.offer(5);
        cola.offer(10);
        assertTrue(cola.contains(10));
    }

    @Test
    void containsRetornaFalseSiElementoNoEsta() {
        cola.offer(1);
        assertFalse(cola.contains(99));
    }

    @Test
    void containsRetornaTrueParaNull() {
        cola.offer(null);
        assertTrue(cola.contains(null));
    }

    @Test
    void removeObjectRetornaFalseEnColaVacia() {
        assertFalse(cola.remove((Object) 1));
    }

    @Test
    void removeObjectEliminaLaPrimeraOcurrencia() {
        cola.offer(1);
        cola.offer(2);
        cola.offer(1);
        assertTrue(cola.remove((Object) 1));
        assertEquals(2, cola.size());
        // el primero que queda ahora debe ser 2
        assertEquals(2, cola.peek());
    }

    @Test
    void removeObjectEliminaDesdeElMedio() {
        cola.offer(1);
        cola.offer(2);
        cola.offer(3);
        assertTrue(cola.remove((Object) 2));
        assertEquals(1, cola.poll());
        assertEquals(3, cola.poll());
    }

    @Test
    void removeObjectEliminaElUltimo() {
        cola.offer(1);
        cola.offer(2);
        cola.offer(3);
        assertTrue(cola.remove((Object) 3));
        assertEquals(2, cola.size());
        // poll debe llegar a 2 como último
        cola.poll();
        assertEquals(2, cola.poll());
    }

    @Test
    void removeObjectRetornaFalseSiNoEsta() {
        cola.offer(1);
        assertFalse(cola.remove((Object) 99));
    }

    @Test
    void clearDejaColaVacia() {
        cola.offer(1);
        cola.offer(2);
        cola.clear();
        assertTrue(cola.isEmpty());
        assertEquals(0, cola.size());
    }

    @Test
    void clearSobreColaVaciaNoLanzaExcepcion() {
        assertDoesNotThrow(() -> cola.clear());
    }

    @Test
    void despuesdeClearSePuedeVolvereAEncolar() {
        cola.offer(1);
        cola.clear();
        cola.offer(2);
        assertEquals(1, cola.size());
        assertEquals(2, cola.peek());
    }

    @Test
    void containsAllRetornaTrueSiTodosPresentes() {
        cola.offer(1);
        cola.offer(2);
        cola.offer(3);
        assertTrue(cola.containsAll(List.of(1, 2, 3)));
    }

    @Test
    void containsAllRetornaFalseSiFaltaAlguno() {
        cola.offer(1);
        cola.offer(2);
        assertFalse(cola.containsAll(List.of(1, 99)));
    }

    @Test
    void containsAllConColeccionVaciaRetornaTrue() {
        cola.offer(1);
        assertTrue(cola.containsAll(List.of()));
    }

    @Test
    void addAllEncolaTodosLosElementos() {
        cola.addAll(List.of(1, 2, 3));
        assertEquals(3, cola.size());
    }

    @Test
    void addAllPreservaOrdenFIFO() {
        cola.addAll(List.of(10, 20, 30));
        assertEquals(10, cola.poll());
        assertEquals(20, cola.poll());
        assertEquals(30, cola.poll());
    }

    @Test
    void addAllRetornaTrueSiColeccionNoVacia() {
        assertTrue(cola.addAll(List.of(1, 2)));
    }

    @Test
    void addAllConColeccionVaciaRetornaFalse() {
        assertFalse(cola.addAll(List.of()));
    }

    @Test
    void removeAllEliminaTodosLosElementosIndicados() {
        cola.addAll(List.of(1, 2, 3, 2, 1));
        cola.removeAll(List.of(1, 2));
        assertEquals(1, cola.size());
        assertEquals(3, cola.peek());
    }

    @Test
    void removeAllRetornaFalseSiNingunEliminado() {
        cola.addAll(List.of(1, 2, 3));
        assertFalse(cola.removeAll(List.of(99, 100)));
    }

    @Test
    void removeAllConColeccionVaciaNoModifica() {
        cola.addAll(List.of(1, 2, 3));
        assertFalse(cola.removeAll(List.of()));
        assertEquals(3, cola.size());
    }

    @Test
    void retainAllConservaInterseccion() {
        cola.addAll(List.of(1, 2, 3, 4, 5));
        cola.retainAll(List.of(2, 4));
        assertEquals(2, cola.size());
        assertTrue(cola.contains(2));
        assertTrue(cola.contains(4));
    }

    @Test
    void retainAllConColeccionVaciaDejaColaVacia() {
        cola.addAll(List.of(1, 2, 3));
        cola.retainAll(List.of());
        assertTrue(cola.isEmpty());
    }

    @Test
    void retainAllSinInterseccionDejaColaVacia() {
        cola.addAll(List.of(1, 2, 3));
        cola.retainAll(List.of(99, 100));
        assertTrue(cola.isEmpty());
    }

    @Test
    void iteratorDeColaVaciaNoTieneNext() {
        assertFalse(cola.iterator().hasNext());
    }

    @Test
    void iteratorRecorreTodosLosElementosEnOrden() {
        cola.addAll(List.of(10, 20, 30));
        int[] esperado = {10, 20, 30};
        int i = 0;
        for (Integer e : cola) {
            assertEquals(esperado[i++], e);
        }
        assertEquals(3, i);
    }

    @Test
    void iteratorNoModificaLaCola() {
        cola.addAll(List.of(1, 2, 3));
        for (Integer ignored : cola) { /* recorro sin hacer nada */ }
        assertEquals(3, cola.size());
    }

    @Test
    void toArrayDeColaVaciaEsArrayVacio() {
        assertEquals(0, cola.toArray().length);
    }

    @Test
    void toArrayContieneElementosEnOrdenFIFO() {
        cola.addAll(List.of(1, 2, 3));
        Object[] arr = cola.toArray();
        assertArrayEquals(new Object[]{1, 2, 3}, arr);
    }

    @Test
    void toArrayTipadoContieneElementosEnOrden() {
        cola.addAll(List.of(7, 8, 9));
        Integer[] arr = cola.toArray(new Integer[0]);
        assertArrayEquals(new Integer[]{7, 8, 9}, arr);
    }


    @Test
    void intercalacionDeEncolarYDescolaMantieneFIFO() {
        cola.offer(1);
        cola.offer(2);
        assertEquals(1, cola.poll());
        cola.offer(3);
        assertEquals(2, cola.poll());
        assertEquals(3, cola.poll());
    }

    @Test
    void encolarYDescolaTodosPreservaOrdenOriginal() {
        List<Integer> original = List.of(5, 3, 8, 1, 9, 2);
        cola.addAll(original);
        for (int esperado : original) {
            assertEquals(esperado, cola.poll());
        }
        assertTrue(cola.isEmpty());
    }

    @Test
    void peekNoCambiaElResultadoDeRemove() {
        cola.offer(100);
        cola.offer(200);
        Integer vistoPorPeek = cola.peek();
        Integer eliminadoPorRemove = cola.remove();
        assertEquals(vistoPorPeek, eliminadoPorRemove);
    }
}
