package estructuras.nodos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestNodoDoblementeEnlazado {

    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    private NodoDoblementeEnlazado<Integer> nodoInt;
    private NodoDoblementeEnlazado<String>  nodoStr;
    private NodoDoblementeEnlazado<Integer> nodoNull;

    @BeforeEach
    public void setUp() {
        nodoInt  = new NodoDoblementeEnlazado<>(10);
        nodoStr  = new NodoDoblementeEnlazado<>("hola");
        nodoNull = new NodoDoblementeEnlazado<>(null);
    }

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================

    @Test
    public void constructor_guarda_el_dato_correctamente() {
        assertEquals(10, nodoInt.getDato());
    }

    @Test
    public void constructor_acepta_string() {
        assertEquals("hola", nodoStr.getDato());
    }

    @Test
    public void constructor_acepta_null_como_dato() {
        assertNull(nodoNull.getDato());
    }

    @Test
    public void constructor_siguiente_es_null_al_crear() {
        assertNull(nodoInt.getSiguiente());
    }

    @Test
    public void constructor_anterior_es_null_al_crear() {
        assertNull(nodoInt.getAnterior());
    }

    // =========================================================================
    // GET ANTERIOR / SET ANTERIOR
    // =========================================================================

    @Test
    public void getAnterior_retorna_null_sin_enlace() {
        assertNull(nodoInt.getAnterior());
    }

    @Test
    public void setAnterior_enlaza_correctamente() {
        NodoDoblementeEnlazado<Integer> anterior = new NodoDoblementeEnlazado<>(5);
        nodoInt.setAnterior(anterior);
        assertSame(anterior, nodoInt.getAnterior());
    }

    @Test
    public void setAnterior_null_desenlaza_el_nodo() {
        nodoInt.setAnterior(new NodoDoblementeEnlazado<>(1));
        nodoInt.setAnterior(null);
        assertNull(nodoInt.getAnterior());
    }

    @Test
    public void setAnterior_sobreescribe_enlace_anterior() {
        NodoDoblementeEnlazado<Integer> primero = new NodoDoblementeEnlazado<>(1);
        NodoDoblementeEnlazado<Integer> segundo = new NodoDoblementeEnlazado<>(2);
        nodoInt.setAnterior(primero);
        nodoInt.setAnterior(segundo);
        assertSame(segundo, nodoInt.getAnterior());
    }

    @Test
    public void getAnterior_retorna_dato_del_nodo_enlazado() {
        NodoDoblementeEnlazado<Integer> anterior = new NodoDoblementeEnlazado<>(99);
        nodoInt.setAnterior(anterior);
        assertEquals(99, nodoInt.getAnterior().getDato());
    }

    // =========================================================================
    // GET SIGUIENTE (override con cast a NodoDoblementeEnlazado)
    // =========================================================================

    @Test
    public void getSiguiente_retorna_null_sin_enlace() {
        assertNull(nodoInt.getSiguiente());
    }

    @Test
    public void getSiguiente_retorna_NodoDoblementeEnlazado() {
        NodoDoblementeEnlazado<Integer> siguiente = new NodoDoblementeEnlazado<>(20);
        nodoInt.setSiguiente(siguiente);
        assertSame(siguiente, nodoInt.getSiguiente());
        assertInstanceOf(NodoDoblementeEnlazado.class, nodoInt.getSiguiente());
    }

    @Test
    public void getSiguiente_retorna_dato_del_nodo_enlazado() {
        nodoInt.setSiguiente(new NodoDoblementeEnlazado<>(77));
        assertEquals(77, nodoInt.getSiguiente().getDato());
    }

    // =========================================================================
    // CADENA DOBLEMENTE ENLAZADA
    // =========================================================================

    @Test
    public void cadena_tres_nodos_navegacion_hacia_adelante() {
        NodoDoblementeEnlazado<Integer> n1 = new NodoDoblementeEnlazado<>(1);
        NodoDoblementeEnlazado<Integer> n2 = new NodoDoblementeEnlazado<>(2);
        NodoDoblementeEnlazado<Integer> n3 = new NodoDoblementeEnlazado<>(3);

        n1.setSiguiente(n2); n2.setAnterior(n1);
        n2.setSiguiente(n3); n3.setAnterior(n2);

        assertEquals(2, n1.getSiguiente().getDato());
        assertEquals(3, n1.getSiguiente().getSiguiente().getDato());
    }

    @Test
    public void cadena_tres_nodos_navegacion_hacia_atras() {
        NodoDoblementeEnlazado<Integer> n1 = new NodoDoblementeEnlazado<>(1);
        NodoDoblementeEnlazado<Integer> n2 = new NodoDoblementeEnlazado<>(2);
        NodoDoblementeEnlazado<Integer> n3 = new NodoDoblementeEnlazado<>(3);

        n1.setSiguiente(n2); n2.setAnterior(n1);
        n2.setSiguiente(n3); n3.setAnterior(n2);

        assertEquals(2, n3.getAnterior().getDato());
        assertEquals(1, n3.getAnterior().getAnterior().getDato());
    }

    @Test
    public void cadena_extremos_sin_enlace() {
        NodoDoblementeEnlazado<Integer> n1 = new NodoDoblementeEnlazado<>(1);
        NodoDoblementeEnlazado<Integer> n2 = new NodoDoblementeEnlazado<>(2);

        n1.setSiguiente(n2); n2.setAnterior(n1);

        assertNull(n1.getAnterior());
        assertNull(n2.getSiguiente());
    }

    @Test
    public void cadena_enlace_bidireccional_consistente() {
        NodoDoblementeEnlazado<Integer> n1 = new NodoDoblementeEnlazado<>(1);
        NodoDoblementeEnlazado<Integer> n2 = new NodoDoblementeEnlazado<>(2);

        n1.setSiguiente(n2); n2.setAnterior(n1);

        assertSame(n1, n2.getAnterior());
        assertSame(n2, n1.getSiguiente());
    }

    // =========================================================================
    // HERENCIA: métodos de NodoSimplementeEnlazado y Nodo
    // =========================================================================

    @Test
    public void tieneSiguiente_herencia_false_sin_enlace() {
        assertFalse(nodoInt.tieneSiguiente());
    }

    @Test
    public void tieneSiguiente_herencia_true_con_enlace() {
        nodoInt.setSiguiente(new NodoDoblementeEnlazado<>(1));
        assertTrue(nodoInt.tieneSiguiente());
    }

    @Test
    public void setDato_herencia_actualiza_valor() {
        nodoInt.setDato(55);
        assertEquals(55, nodoInt.getDato());
    }

    @Test
    public void tieneDato_herencia_true_con_dato() {
        assertTrue(nodoInt.tieneDato());
    }

    @Test
    public void tieneDato_herencia_false_con_null() {
        assertFalse(nodoNull.tieneDato());
    }
}
