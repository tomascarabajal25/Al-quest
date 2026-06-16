package estructuras.conjunto;

import estructuras.conjuntos.Conjunto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestConjunto {

    private Conjunto<Integer> conjunto;

    @BeforeEach
    void setUp() {
        conjunto = new Conjunto<>();
    }

    @Test
    void conjuntoNuevoEstaVacio() {
        assertTrue(conjunto.isEmpty());
        assertEquals(0, conjunto.size());
    }

    @Test
    void despuesDeAgregarUnElementoNoEstaVacio() {
        conjunto.add(1);
        assertFalse(conjunto.isEmpty());
        assertEquals(1, conjunto.size());
    }

    @Test
    void addRetornaTrueCuandoElementoNoExiste() {
        assertTrue(conjunto.add(10));
    }

    @Test
    void addRetornaFalseCuandoElementoYaExiste() {
        conjunto.add(10);
        assertFalse(conjunto.add(10));
    }

    @Test
    void addNoDuplicaElementos() {
        conjunto.add(5);
        conjunto.add(5);
        conjunto.add(5);
        assertEquals(1, conjunto.size());
    }

    @Test
    void addVariosElementosDistintos() {
        conjunto.add(1);
        conjunto.add(2);
        conjunto.add(3);
        assertEquals(3, conjunto.size());
    }

    @Test
    void addAceptaNullSiListaLoPermite() {
        assertTrue(conjunto.add(null));
        assertEquals(1, conjunto.size());
        assertFalse(conjunto.add(null)); // no debe duplicar null
        assertEquals(1, conjunto.size());
    }

    @Test
    void containsRetornaFalseEnConjuntoVacio() {
        assertFalse(conjunto.contains(1));
    }

    @Test
    void containsRetornaTrueSiElementoFueAgregado() {
        conjunto.add(42);
        assertTrue(conjunto.contains(42));
    }

    @Test
    void containsRetornaFalseSiElementoNoEsta() {
        conjunto.add(1);
        assertFalse(conjunto.contains(99));
    }

    @Test
    void removeRetornaFalseEnConjuntoVacio() {
        assertFalse(conjunto.remove(1));
    }

    @Test
    void removeRetornaTrueSiElementoExiste() {
        conjunto.add(7);
        assertTrue(conjunto.remove(7));
    }

    @Test
    void removeBorraElElementoCorrectamente() {
        conjunto.add(7);
        conjunto.remove(7);
        assertFalse(conjunto.contains(7));
        assertEquals(0, conjunto.size());
    }

    @Test
    void removeNoAfectaOtrosElementos() {
        conjunto.add(1);
        conjunto.add(2);
        conjunto.add(3);
        conjunto.remove(2);
        assertTrue(conjunto.contains(1));
        assertFalse(conjunto.contains(2));
        assertTrue(conjunto.contains(3));
        assertEquals(2, conjunto.size());
    }

    @Test
    void containsAllRetornaTrueSiTodosPresentes() {
        conjunto.add(1);
        conjunto.add(2);
        conjunto.add(3);
        assertTrue(conjunto.containsAll(List.of(1, 2, 3)));
    }

    @Test
    void containsAllRetornaFalseSiFaltaAlguno() {
        conjunto.add(1);
        conjunto.add(2);
        assertFalse(conjunto.containsAll(List.of(1, 2, 99)));
    }

    @Test
    void containsAllConColeccionVaciaRetornaTrue() {
        conjunto.add(1);
        assertTrue(conjunto.containsAll(List.of()));
    }

    @Test
    void addAllAgregaElementosNuevos() {
        conjunto.addAll(List.of(1, 2, 3));
        assertEquals(3, conjunto.size());
        assertTrue(conjunto.containsAll(List.of(1, 2, 3)));
    }

    @Test
    void addAllNoDuplicaElementosExistentes() {
        conjunto.add(1);
        conjunto.addAll(List.of(1, 2, 3));
        assertEquals(3, conjunto.size());
    }

    @Test
    void addAllRetornaTrueSiHuboCambio() {
        assertTrue(conjunto.addAll(List.of(1, 2)));
    }

    @Test
    void addAllRetornaFalseSiNadaNuevo() {
        conjunto.addAll(List.of(1, 2));
        assertFalse(conjunto.addAll(List.of(1, 2)));
    }

    @Test
    void addAllConColeccionConDuplicadosInternos() {
        conjunto.addAll(Arrays.asList(5, 5, 5));
        assertEquals(1, conjunto.size());
    }

    @Test
    void removeAllEliminaLosElementosIndicados() {
        conjunto.addAll(List.of(1, 2, 3, 4));
        conjunto.removeAll(List.of(2, 4));
        assertEquals(2, conjunto.size());
        assertTrue(conjunto.containsAll(List.of(1, 3)));
    }

    @Test
    void removeAllRetornaFalseSiNingúnElementoEliminado() {
        conjunto.addAll(List.of(1, 2, 3));
        assertFalse(conjunto.removeAll(List.of(99, 100)));
    }

    @Test
    void removeAllConColeccionVaciaNoModifica() {
        conjunto.addAll(List.of(1, 2, 3));
        assertFalse(conjunto.removeAll(List.of()));
        assertEquals(3, conjunto.size());
    }

    @Test
    void retainAllConservaInterseccion() {
        conjunto.addAll(List.of(1, 2, 3, 4, 5));
        conjunto.retainAll(List.of(2, 4));
        assertEquals(2, conjunto.size());
        assertTrue(conjunto.containsAll(List.of(2, 4)));
    }

    @Test
    void retainAllConColeccionVaciaDejaConjuntoVacio() {
        conjunto.addAll(List.of(1, 2, 3));
        conjunto.retainAll(List.of());
        assertTrue(conjunto.isEmpty());
    }

    @Test
    void retainAllSinInterseccionDejaConjuntoVacio() {
        conjunto.addAll(List.of(1, 2, 3));
        conjunto.retainAll(List.of(99, 100));
        assertTrue(conjunto.isEmpty());
    }

    @Test
    void retainAllRetornaFalseSiNadaCambio() {
        conjunto.addAll(List.of(1, 2));
        assertFalse(conjunto.retainAll(List.of(1, 2, 3)));
    }

    @Test
    void clearDejaElConjuntoVacio() {
        conjunto.addAll(List.of(1, 2, 3));
        conjunto.clear();
        assertTrue(conjunto.isEmpty());
        assertEquals(0, conjunto.size());
    }

    @Test
    void clearSobreConjuntoVacioNoProduce_excepcion() {
        assertDoesNotThrow(() -> conjunto.clear());
    }

    @Test
    void iteratorDeConjuntoVacioNoTieneNext() {
        assertFalse(conjunto.iterator().hasNext());
    }

    @Test
    void iteratorRecorreTodosLosElementos() {
        conjunto.addAll(List.of(10, 20, 30));
        int count = 0;
        for (Integer e : conjunto) {
            count++;
        }
        assertEquals(3, count);
    }

    @Test
    void iteratorNoRepiteElementos() {
        conjunto.addAll(List.of(1, 2, 3));
        Conjunto<Integer> vistos = new Conjunto<>();
        for (Integer e : conjunto) {
            assertTrue(vistos.add(e), "Elemento repetido en el iterador: " + e);
        }
    }

    @Test
    void toArrayDeConjuntoVacioEsArrayVacio() {
        assertEquals(0, conjunto.toArray().length);
    }

    @Test
    void toArrayContieneExactamenteLosElementos() {
        conjunto.addAll(List.of(1, 2, 3));
        Object[] arr = conjunto.toArray();
        assertEquals(3, arr.length);
        assertTrue(conjunto.containsAll(Arrays.asList(arr)));
    }

    @Test
    void toArrayTipado_ContieneElementosCorrectos() {
        conjunto.addAll(List.of(7, 8, 9));
        Integer[] arr = conjunto.toArray(new Integer[0]);
        assertEquals(3, arr.length);
        assertTrue(conjunto.containsAll(Arrays.asList(arr)));
    }

    @Test
    void dosConjuntosConMismosElementosTienenIgualSize() {
        Conjunto<String> a = new Conjunto<>();
        Conjunto<String> b = new Conjunto<>();
        a.addAll(List.of("x", "y", "z"));
        b.addAll(List.of("z", "x", "y"));
        assertEquals(a.size(), b.size());
        assertTrue(a.containsAll(b) && b.containsAll(a));
    }

    @Test
    void unionConAddAll_sizeEsCardinalidadUnion() {
        conjunto.addAll(List.of(1, 2, 3));
        Conjunto<Integer> otro = new Conjunto<>();
        otro.addAll(List.of(3, 4, 5));
        conjunto.addAll(otro);
        // {1,2,3} ∪ {3,4,5} = {1,2,3,4,5}
        assertEquals(5, conjunto.size());
    }

    @Test
    void interseccionConRetainAll_sizeEsCardinalidadInterseccion() {
        conjunto.addAll(List.of(1, 2, 3, 4));
        conjunto.retainAll(List.of(2, 3, 99));
        // {1,2,3,4} ∩ {2,3,99} = {2,3}
        assertEquals(2, conjunto.size());
        assertTrue(conjunto.containsAll(List.of(2, 3)));
    }

    @Test
    void diferenciConRemoveAll_sizeEsCardinalidadDiferencia() {
        conjunto.addAll(List.of(1, 2, 3, 4, 5));
        conjunto.removeAll(List.of(2, 4));
        // {1,2,3,4,5} - {2,4} = {1,3,5}
        assertEquals(3, conjunto.size());
        assertTrue(conjunto.containsAll(List.of(1, 3, 5)));
    }
}
