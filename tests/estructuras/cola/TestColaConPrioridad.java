package estructuras.cola;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class TestColaConPrioridad {

    private ColaConPrioridad<String> cola;

    // 5 niveles: prioridades 0 (más alta) a 4 (más baja)
    @BeforeEach
    void setUp() {
        cola = new ColaConPrioridad<>(5);
    }

    // ──────────────────────────────────────────────
    // Constructor
    // ──────────────────────────────────────────────

    @Test
    void constructorConMaxPrioridadValidoCreaColaVacia() {
        ColaConPrioridad<Integer> c = new ColaConPrioridad<>(3);
        assertTrue(c.isEmpty());
        assertEquals(0, c.size());
    }

    @Test
    void constructorConMaxPrioridadCeroLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new ColaConPrioridad<>(0));
    }

    @Test
    void constructorConMaxPrioridadNegativaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> new ColaConPrioridad<>(-1));
    }

    @Test
    void constructorConMaxPrioridadUnoEsValido() {
        ColaConPrioridad<String> c = new ColaConPrioridad<>(1);
        assertTrue(c.isEmpty());
    }

    // ──────────────────────────────────────────────
    // isEmpty() y size()
    // ──────────────────────────────────────────────

    @Test
    void colaRecienCreadaEstaVacia() {
        assertTrue(cola.isEmpty());
        assertEquals(0, cola.size());
    }

    @Test
    void despuesDeEnqueueNoEstaVacia() {
        cola.enqueue("A", 0);
        assertFalse(cola.isEmpty());
    }

    @Test
    void sizeAumentaConCadaEnqueue() {
        cola.enqueue("A", 0);
        assertEquals(1, cola.size());
        cola.enqueue("B", 2);
        assertEquals(2, cola.size());
        cola.enqueue("C", 4);
        assertEquals(3, cola.size());
    }

    @Test
    void sizeDisminuyeConCadaDequeue() {
        cola.enqueue("A", 0);
        cola.enqueue("B", 1);
        cola.dequeue();
        assertEquals(1, cola.size());
    }

    @Test
    void dequeueDeUnicoElementoDejaColaVacia() {
        cola.enqueue("X", 2);
        cola.dequeue();
        assertTrue(cola.isEmpty());
        assertEquals(0, cola.size());
    }

    // ──────────────────────────────────────────────
    // enqueue()
    // ──────────────────────────────────────────────

    @Test
    void enqueueConPrioridadCeroEsValido() {
        assertDoesNotThrow(() -> cola.enqueue("alta", 0));
    }

    @Test
    void enqueueConMaxPrioridadMenosUnoEsValido() {
        assertDoesNotThrow(() -> cola.enqueue("baja", 4));
    }

    @Test
    void enqueueConPrioridadNegativaLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> cola.enqueue("X", -1));
    }

    @Test
    void enqueueConPrioridadFueraDeRangoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> cola.enqueue("X", 5));
    }

    @Test
    void enqueuePermiteDuplicadosEnMismaPrioridad() {
        cola.enqueue("A", 1);
        cola.enqueue("A", 1);
        assertEquals(2, cola.size());
    }

    @Test
    void enqueuePermiteNullComoElemento() {
        assertDoesNotThrow(() -> cola.enqueue(null, 0));
        assertEquals(1, cola.size());
    }

    // ──────────────────────────────────────────────
    // dequeue() — orden por prioridad
    // ──────────────────────────────────────────────

    @Test
    void dequeueEnColaVaciaLanzaExcepcion() {
        assertThrows(NoSuchElementException.class, () -> cola.dequeue());
    }

    @Test
    void dequeueDevuelveElElementoDeMayorPrioridad() {
        cola.enqueue("media", 2);
        cola.enqueue("alta", 0);
        cola.enqueue("baja", 4);
        assertEquals("alta", cola.dequeue());
    }

    @Test
    void dequeueSiHayEmpateEnPrioridadRespetaFIFODentroDelBucket() {
        cola.enqueue("primero", 1);
        cola.enqueue("segundo", 1);
        cola.enqueue("tercero", 1);
        assertEquals("primero", cola.dequeue());
        assertEquals("segundo", cola.dequeue());
        assertEquals("tercero", cola.dequeue());
    }

    @Test
    void dequeueConTodasLasPrioridadesDevuelveEnOrden() {
        cola.enqueue("p4", 4);
        cola.enqueue("p2", 2);
        cola.enqueue("p0", 0);
        cola.enqueue("p3", 3);
        cola.enqueue("p1", 1);
        assertEquals("p0", cola.dequeue());
        assertEquals("p1", cola.dequeue());
        assertEquals("p2", cola.dequeue());
        assertEquals("p3", cola.dequeue());
        assertEquals("p4", cola.dequeue());
    }

    @Test
    void dequeueIgnoraBucketsVaciosYAvanzaAlSiguiente() {
        // prioridades 0, 1 y 2 vacías; elemento en prioridad 3
        cola.enqueue("rezagado", 3);
        assertEquals("rezagado", cola.dequeue());
    }

    @Test
    void dequeueMezclaDePrioridadesYFIFO() {
        cola.enqueue("A", 2);
        cola.enqueue("B", 0);
        cola.enqueue("C", 2);
        cola.enqueue("D", 0);
        // orden esperado: B, D (prioridad 0), luego A, C (prioridad 2)
        assertEquals("B", cola.dequeue());
        assertEquals("D", cola.dequeue());
        assertEquals("A", cola.dequeue());
        assertEquals("C", cola.dequeue());
    }

    @Test
    void dequeueReduceSizeCorrectamente() {
        cola.enqueue("X", 0);
        cola.enqueue("Y", 1);
        cola.enqueue("Z", 2);
        cola.dequeue();
        assertEquals(2, cola.size());
        cola.dequeue();
        assertEquals(1, cola.size());
        cola.dequeue();
        assertEquals(0, cola.size());
    }

    @Test
    void dequeueSeguidoDeEnqueueMantienePrioridad() {
        cola.enqueue("A", 1);
        cola.dequeue();                  // saca A
        cola.enqueue("B", 3);
        cola.enqueue("C", 0);           // mayor prioridad
        assertEquals("C", cola.dequeue());
        assertEquals("B", cola.dequeue());
    }

    // ──────────────────────────────────────────────
    // peek()
    // ──────────────────────────────────────────────

    @Test
    void peekEnColaVaciaLanzaExcepcion() {
        assertThrows(NoSuchElementException.class, () -> cola.peek());
    }

    @Test
    void peekDevuelveElElementoDeMayorPrioridadSinEliminarlo() {
        cola.enqueue("media", 2);
        cola.enqueue("alta", 0);
        assertEquals("alta", cola.peek());
        assertEquals(2, cola.size()); // no modificó
    }

    @Test
    void peekRepetidasVecesDevuelveSiempreElMismo() {
        cola.enqueue("único", 1);
        assertEquals("único", cola.peek());
        assertEquals("único", cola.peek());
        assertEquals("único", cola.peek());
        assertEquals(1, cola.size());
    }

    @Test
    void peekEsConsistenteConDequeue() {
        cola.enqueue("X", 0);
        cola.enqueue("Y", 2);
        String vistoPorPeek = cola.peek();
        String eliminadoPorDequeue = cola.dequeue();
        assertEquals(vistoPorPeek, eliminadoPorDequeue);
    }

    @Test
    void peekNoCambiaCuandoSoloHayElementosEnPrioridadesBajas() {
        cola.enqueue("unico", 4);
        assertEquals("unico", cola.peek());
        assertEquals(1, cola.size());
    }

    // ──────────────────────────────────────────────
    // Propiedades de invariante
    // ──────────────────────────────────────────────

    @Test
    void vaciarPorDequeueDejaColaVacia() {
        cola.enqueue("A", 0);
        cola.enqueue("B", 1);
        cola.enqueue("C", 2);
        cola.dequeue();
        cola.dequeue();
        cola.dequeue();
        assertTrue(cola.isEmpty());
        assertEquals(0, cola.size());
    }

    @Test
    void dequeueTrasPeekNoAlteraElOrden() {
        cola.enqueue("primero", 0);
        cola.enqueue("segundo", 1);
        cola.peek(); // no debe alterar nada
        assertEquals("primero", cola.dequeue());
        assertEquals("segundo", cola.dequeue());
    }

    @Test
    void colaConUnicoNivelDePrioridadSeComportaComoFIFO() {
        ColaConPrioridad<Integer> c = new ColaConPrioridad<>(1);
        c.enqueue(10, 0);
        c.enqueue(20, 0);
        c.enqueue(30, 0);
        assertEquals(10, c.dequeue());
        assertEquals(20, c.dequeue());
        assertEquals(30, c.dequeue());
    }

    @Test
    void reusoDeLaColaTrasClearPorDequeueEsCorrecto() {
        cola.enqueue("A", 0);
        cola.dequeue();
        cola.enqueue("B", 2);
        cola.enqueue("C", 0);
        assertEquals("C", cola.peek());
        assertEquals(2, cola.size());
    }
}
