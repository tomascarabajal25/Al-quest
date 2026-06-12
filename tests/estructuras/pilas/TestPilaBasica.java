package estructuras.pilas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestPilaBasica {

    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    private PilaBasica<Integer> pila;

    @BeforeEach
    public void setUp() {
        pila = new PilaBasica<>();
    }

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================

    @Test
    public void constructor_pila_nueva_esta_vacia() {
        assertTrue(pila.estaVacia());
    }

    @Test
    public void constructor_pila_nueva_contador_es_cero() {
        assertEquals(0, pila.contarElementos());
    }

    @Test
    public void constructor_pila_nueva_obtener_retorna_null() {
        assertNull(pila.obtener());
    }

    // =========================================================================
    // ESTA VACIA
    // =========================================================================

    @Test
    public void estaVacia_retorna_false_despues_de_apilar() {
        pila.apilar(1);
        assertFalse(pila.estaVacia());
    }

    @Test
    public void estaVacia_retorna_true_despues_de_desapilar_todo() {
        pila.apilar(1);
        pila.desapilar();
        assertTrue(pila.estaVacia());
    }

    // =========================================================================
    // APILAR (elemento)
    // =========================================================================

    @Test
    public void apilar_un_elemento_queda_en_el_tope() {
        pila.apilar(42);
        assertEquals(42, pila.obtener());
    }

    @Test
    public void apilar_incrementa_el_contador() {
        pila.apilar(1);
        pila.apilar(2);
        assertEquals(2, pila.contarElementos());
    }

    @Test
    public void apilar_multiple_ultimo_en_entrar_queda_en_tope() {
        pila.apilar(1);
        pila.apilar(2);
        pila.apilar(3);
        assertEquals(3, pila.obtener());
    }

    @Test
    public void apilar_null_lo_almacena_sin_excepcion() {
        PilaBasica<String> p = new PilaBasica<>();
        assertDoesNotThrow(() -> p.apilar((String) null));
        assertNull(p.obtener());
    }

    // =========================================================================
    // APILAR (lista)
    // =========================================================================

    @Test
    public void apilar_lista_agrega_todos_los_elementos() {
        pila.apilar(List.of(1, 2, 3));
        assertEquals(3, pila.contarElementos());
    }

    @Test
    public void apilar_lista_el_ultimo_elemento_queda_en_tope() {
        pila.apilar(List.of(1, 2, 3));
        // apilar recorre la lista en orden, así que el 3 es el último apilado
        assertEquals(3, pila.obtener());
    }

    @Test
    public void apilar_lista_orden_lifo_correcto() {
        pila.apilar(List.of(1, 2, 3));
        assertEquals(3, pila.desapilar());
        assertEquals(2, pila.desapilar());
        assertEquals(1, pila.desapilar());
    }

    @Test
    public void apilar_lista_vacia_no_modifica_la_pila() {
        pila.apilar(1);
        pila.apilar(List.of());
        assertEquals(1, pila.contarElementos());
        assertEquals(1, pila.obtener());
    }

    // =========================================================================
    // DESAPILAR
    // =========================================================================

    @Test
    public void desapilar_retorna_elemento_del_tope() {
        pila.apilar(10);
        assertEquals(10, pila.desapilar());
    }

    @Test
    public void desapilar_reduce_el_contador() {
        pila.apilar(1);
        pila.apilar(2);
        pila.desapilar();
        assertEquals(1, pila.contarElementos());
    }

    @Test
    public void desapilar_expone_el_siguiente_elemento() {
        pila.apilar(1);
        pila.apilar(2);
        pila.desapilar();
        assertEquals(1, pila.obtener());
    }

    @Test
    public void desapilar_orden_lifo_correcto() {
        pila.apilar(1);
        pila.apilar(2);
        pila.apilar(3);
        assertEquals(3, pila.desapilar());
        assertEquals(2, pila.desapilar());
        assertEquals(1, pila.desapilar());
    }

    @Test
    public void desapilar_unico_elemento_deja_pila_vacia() {
        pila.apilar(5);
        pila.desapilar();
        assertTrue(pila.estaVacia());
    }

    @Test
    public void desapilar_pila_vacia_retorna_null() {
        assertNull(pila.desapilar());
    }

    @Test
    public void desapilar_pila_vacia_no_modifica_contador() {
        pila.desapilar();
        assertEquals(0, pila.contarElementos());
    }

    // =========================================================================
    // OBTENER
    // =========================================================================

    @Test
    public void obtener_retorna_tope_sin_remover() {
        pila.apilar(7);
        pila.obtener();
        assertEquals(1, pila.contarElementos());
        assertEquals(7, pila.obtener());
    }

    @Test
    public void obtener_pila_vacia_retorna_null() {
        assertNull(pila.obtener());
    }

    @Test
    public void obtener_refleja_el_nuevo_tope_despues_de_apilar() {
        pila.apilar(1);
        pila.apilar(2);
        assertEquals(2, pila.obtener());
        pila.apilar(3);
        assertEquals(3, pila.obtener());
    }

    @Test
    public void obtener_refleja_el_nuevo_tope_despues_de_desapilar() {
        pila.apilar(1);
        pila.apilar(2);
        pila.desapilar();
        assertEquals(1, pila.obtener());
    }

    // =========================================================================
    // CONTAR ELEMENTOS
    // =========================================================================

    @Test
    public void contarElementos_pila_vacia_es_cero() {
        assertEquals(0, pila.contarElementos());
    }

    @Test
    public void contarElementos_crece_con_cada_apilar() {
        pila.apilar(1);
        assertEquals(1, pila.contarElementos());
        pila.apilar(2);
        assertEquals(2, pila.contarElementos());
    }

    @Test
    public void contarElementos_decrece_con_cada_desapilar() {
        pila.apilar(1);
        pila.apilar(2);
        pila.desapilar();
        assertEquals(1, pila.contarElementos());
    }

    @Test
    public void contarElementos_no_cambia_con_obtener() {
        pila.apilar(1);
        pila.obtener();
        assertEquals(1, pila.contarElementos());
    }
}
