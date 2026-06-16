package estructuras.arboles;

import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TestArbolBinarioHeap {

    private static final Comparator<Integer> MIN = Integer::compareTo;
    private static final Comparator<Integer> MAX = (a, b) -> Integer.compare(b, a);

    @Test
    public void constructorIsEmptyEsTrueAlCrearElHeap() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        assertTrue(h.isEmpty());
    }

    @Test
    public void constructorSizeEsCeroAlCrearElHeap() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        assertEquals(0, h.size());
    }

    @Test
    public void constructorConCapacidadExplicitaCreaHeapVacioSinExcepcion() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN, 5);
        assertTrue(h.isEmpty());
    }

    @Test
    public void isEmptyEsFalseDespuesDeUnInsert() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(10);
        assertFalse(h.isEmpty());
    }

    @Test
    public void sizeDevuelveTresTrasTreeInserts() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(1);
        h.insert(2);
        h.insert(3);
        assertEquals(3, h.size());
    }

    @Test
    public void isEmptyEsTrueDespuesDeExtraerElUnicoElemento() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(42);
        h.extract();
        assertTrue(h.isEmpty());
    }

    @Test
    public void sizeDecraceAUnoDespuesDeUnExtractDeDosElementos() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(1);
        h.insert(2);
        h.extract();
        assertEquals(1, h.size());
    }

    @Test
    public void peekEnMinHeapDevuelveElMinimo() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(5);
        h.insert(2);
        h.insert(8);
        assertEquals(2, h.peek());
    }

    @Test
    public void peekEnMaxHeapDevuelveElMaximo() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MAX);
        h.insert(5);
        h.insert(2);
        h.insert(9);
        assertEquals(9, h.peek());
    }

    @Test
    public void peekNoModificaElTamanio() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(3);
        h.insert(1);
        h.peek();
        assertEquals(2, h.size());
    }

    @Test
    public void peekEnHeapVacioLanzaNoSuchElementException() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        assertThrows(NoSuchElementException.class, h::peek);
    }

    @Test
    public void extractEnMinHeapDevuelveElMinimo() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(7);
        h.insert(3);
        h.insert(5);
        assertEquals(3, h.extract());
    }

    @Test
    public void extractEnMaxHeapDevuelveElMaximo() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MAX);
        h.insert(7);
        h.insert(3);
        h.insert(10);
        assertEquals(10, h.extract());
    }

    @Test
    public void extractRestaurapropiedadMinHeap() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(4);
        h.insert(1);
        h.insert(7);
        h.insert(2);
        h.extract(); // extrae el 1
        assertEquals(2, h.peek());
    }

    @Test
    public void extractRestaurapropiedadMaxHeap() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MAX);
        h.insert(4);
        h.insert(9);
        h.insert(3);
        h.insert(6);
        h.extract(); // extrae el 9
        assertEquals(6, h.peek());
    }

    @Test
    public void extractEnHeapVacioLanzaNoSuchElementException() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        assertThrows(NoSuchElementException.class, h::extract);
    }

    @Test
    public void extractConUnSoloElementoDejaElHeapVacio() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        h.insert(99);
        h.extract();
        assertTrue(h.isEmpty());
    }

    @Test
    public void extractMinHeapProduceOrdenAscendente() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN);
        for (int v : new int[]{5, 2, 8, 1, 9, 3}) h.insert(v);

        int[] esperado = {1, 2, 3, 5, 8, 9};
        for (int exp : esperado) {
            assertEquals(exp, h.extract());
        }
    }

    @Test
    public void extractMaxHeapProduceOrdenDescendente() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MAX);
        for (int v : new int[]{5, 2, 8, 1, 9, 3}) h.insert(v);

        int[] esperado = {9, 8, 5, 3, 2, 1};
        for (int exp : esperado) {
            assertEquals(exp, h.extract());
        }
    }

    @Test
    public void insertarMasElementosQueLaCapacidadInicialRedimensionaSinExcepcion() {
        ArbolBinarioHeap<Integer> h = new ArbolBinarioHeap<>(MIN, 4);
        for (int i = 10; i >= 1; i--) h.insert(i);
        assertEquals(10, h.size());
        assertEquals(1, h.peek());
    }

    @Test
    public void minHeapConStringsDevuelveElMenorLexicograficamente() {
        ArbolBinarioHeap<String> h = new ArbolBinarioHeap<>(String::compareTo);
        h.insert("manzana");
        h.insert("banana");
        h.insert("cereza");
        assertEquals("banana", h.peek());
    }
}
