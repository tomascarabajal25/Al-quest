package estructuras.nodos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestNodo {

    private static class NodoConcreto<T> extends Nodo<T> {
        NodoConcreto(T dato) {
            super(dato);
        }
    }

    private Nodo<Integer> nodoInt;
    private Nodo<String>  nodoStr;
    private Nodo<Integer> nodoNull;

    @BeforeEach
    public void setUp() {
        nodoInt  = new NodoConcreto<>(42);
        nodoStr  = new NodoConcreto<>("hola");
        nodoNull = new NodoConcreto<>(null);
    }

    @Test
    public void constructor_guarda_el_dato_correctamente() {
        assertEquals(42, nodoInt.getDato());
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
    public void getDato_retorna_el_valor_almacenado() {
        assertEquals(42, nodoInt.getDato());
    }

    @Test
    public void getDato_retorna_null_si_dato_es_null() {
        assertNull(nodoNull.getDato());
    }

    @Test
    public void setDato_actualiza_el_valor() {
        nodoInt.setDato(99);
        assertEquals(99, nodoInt.getDato());
    }

    @Test
    public void setDato_permite_asignar_null() {
        nodoInt.setDato(null);
        assertNull(nodoInt.getDato());
    }

    @Test
    public void setDato_sobreescribe_valor_previo() {
        nodoStr.setDato("nuevo");
        assertEquals("nuevo", nodoStr.getDato());
    }

    @Test
    public void setDato_permite_asignar_desde_null_a_valor() {
        nodoNull.setDato(7);
        assertEquals(7, nodoNull.getDato());
    }

    @Test
    public void tieneDato_retorna_true_con_dato_no_null() {
        assertTrue(nodoInt.tieneDato());
    }

    @Test
    public void tieneDato_retorna_false_con_dato_null() {
        assertFalse(nodoNull.tieneDato());
    }

    @Test
    public void tieneDato_retorna_false_despues_de_setDato_null() {
        nodoInt.setDato(null);
        assertFalse(nodoInt.tieneDato());
    }

    @Test
    public void tieneDato_retorna_true_despues_de_setDato_con_valor() {
        nodoNull.setDato(1);
        assertTrue(nodoNull.tieneDato());
    }
}
