package estructuras.nodos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestNodoSimplementeEnlazado {

    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    private NodoSimplementeEnlazado<Integer> nodoInt;
    private NodoSimplementeEnlazado<String>  nodoStr;
    private NodoSimplementeEnlazado<Integer> nodoNull;

    @BeforeEach
    public void setUp() {
        nodoInt  = new NodoSimplementeEnlazado<>(10);
        nodoStr  = new NodoSimplementeEnlazado<>("hola");
        nodoNull = new NodoSimplementeEnlazado<>(null);
    }

    // =========================================================================
    // CONSTRUCTOR  (hereda Nodo)
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

    // =========================================================================
    // TIENE SIGUIENTE
    // =========================================================================

    @Test
    public void tieneSiguiente_retorna_false_recien_creado() {
        assertFalse(nodoInt.tieneSiguiente());
    }

    @Test
    public void tieneSiguiente_retorna_true_despues_de_setSiguiente() {
        nodoInt.setSiguiente(new NodoSimplementeEnlazado<>(99));
        assertTrue(nodoInt.tieneSiguiente());
    }

    @Test
    public void tieneSiguiente_retorna_false_despues_de_setSiguiente_null() {
        nodoInt.setSiguiente(new NodoSimplementeEnlazado<>(99));
        nodoInt.setSiguiente(null);
        assertFalse(nodoInt.tieneSiguiente());
    }

    // =========================================================================
    // GET SIGUIENTE
    // =========================================================================

    @Test
    public void getSiguiente_retorna_null_sin_enlace() {
        assertNull(nodoInt.getSiguiente());
    }

    @Test
    public void getSiguiente_retorna_el_nodo_enlazado() {
        NodoSimplementeEnlazado<Integer> siguiente = new NodoSimplementeEnlazado<>(20);
        nodoInt.setSiguiente(siguiente);
        assertSame(siguiente, nodoInt.getSiguiente());
    }

    @Test
    public void getSiguiente_retorna_el_dato_del_nodo_enlazado() {
        nodoInt.setSiguiente(new NodoSimplementeEnlazado<>(55));
        assertEquals(55, nodoInt.getSiguiente().getDato());
    }

    // =========================================================================
    // SET SIGUIENTE
    // =========================================================================

    @Test
    public void setSiguiente_enlaza_correctamente() {
        NodoSimplementeEnlazado<Integer> sig = new NodoSimplementeEnlazado<>(30);
        nodoInt.setSiguiente(sig);
        assertSame(sig, nodoInt.getSiguiente());
    }

    @Test
    public void setSiguiente_null_desenlaza_el_nodo() {
        nodoInt.setSiguiente(new NodoSimplementeEnlazado<>(1));
        nodoInt.setSiguiente(null);
        assertNull(nodoInt.getSiguiente());
    }

    @Test
    public void setSiguiente_sobreescribe_enlace_anterior() {
        NodoSimplementeEnlazado<Integer> primero = new NodoSimplementeEnlazado<>(1);
        NodoSimplementeEnlazado<Integer> segundo = new NodoSimplementeEnlazado<>(2);
        nodoInt.setSiguiente(primero);
        nodoInt.setSiguiente(segundo);
        assertSame(segundo, nodoInt.getSiguiente());
    }

    // =========================================================================
    // CADENA DE NODOS
    // =========================================================================

    @Test
    public void cadena_tres_nodos_navegacion_correcta() {
        NodoSimplementeEnlazado<Integer> n1 = new NodoSimplementeEnlazado<>(1);
        NodoSimplementeEnlazado<Integer> n2 = new NodoSimplementeEnlazado<>(2);
        NodoSimplementeEnlazado<Integer> n3 = new NodoSimplementeEnlazado<>(3);
        n1.setSiguiente(n2);
        n2.setSiguiente(n3);

        assertEquals(2, n1.getSiguiente().getDato());
        assertEquals(3, n1.getSiguiente().getSiguiente().getDato());
        assertNull(n1.getSiguiente().getSiguiente().getSiguiente());
    }

    @Test
    public void cadena_ultimo_nodo_no_tiene_siguiente() {
        NodoSimplementeEnlazado<Integer> n1 = new NodoSimplementeEnlazado<>(1);
        NodoSimplementeEnlazado<Integer> n2 = new NodoSimplementeEnlazado<>(2);
        n1.setSiguiente(n2);
        assertFalse(n2.tieneSiguiente());
    }

    // =========================================================================
    // HERENCIA: métodos de Nodo
    // =========================================================================

    @Test
    public void getDato_herencia_retorna_valor_correcto() {
        assertEquals(10, nodoInt.getDato());
    }

    @Test
    public void setDato_herencia_actualiza_el_valor() {
        nodoInt.setDato(77);
        assertEquals(77, nodoInt.getDato());
    }

    @Test
    public void tieneDato_herencia_retorna_true_con_dato() {
        assertTrue(nodoInt.tieneDato());
    }

    @Test
    public void tieneDato_herencia_retorna_false_con_null() {
        assertFalse(nodoNull.tieneDato());
    }
}
