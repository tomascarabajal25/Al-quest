package estructuras.hashing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import estructuras.hashing.HashTable.EntradaHash;
import estructuras.listas.ListaSimplementeEnlazada;

import static org.junit.jupiter.api.Assertions.*;

public class TestHashTable {

    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    private HashTable<String, Integer> tabla;

    @BeforeEach
    public void setUp() {
        tabla = new HashTable<>(11); // primo para minimizar colisiones
    }

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================

    @Test
    public void constructor_tabla_nueva_tiene_cero_elementos() {
        assertEquals(0, tabla.getCantidadElementos());
    }

    @Test
    public void constructor_cantidad_slots_correcta() {
        assertEquals(11, tabla.getCantidadSlots());
    }

    @Test
    public void constructor_todos_los_slots_estan_vacios() {
        for (int i = 0; i < tabla.getCantidadSlots(); i++) {
            assertTrue(tabla.getSlot(i).isEmpty());
        }
    }

    @Test
    public void constructor_cantidad_slots_cero_lanza_excepcion() {
        assertThrows(IllegalArgumentException.class, () -> new HashTable<>(0));
    }

    @Test
    public void constructor_cantidad_slots_negativa_lanza_excepcion() {
        assertThrows(IllegalArgumentException.class, () -> new HashTable<>(-5));
    }

    // =========================================================================
    // CALCULAR INDICE
    // =========================================================================

    @Test
    public void calcularIndice_retorna_valor_en_rango_valido() {
        int idx = tabla.calcularIndice("clave");
        assertTrue(idx >= 0 && idx < tabla.getCantidadSlots());
    }

    @Test
    public void calcularIndice_misma_clave_retorna_mismo_indice() {
        assertEquals(tabla.calcularIndice("abc"), tabla.calcularIndice("abc"));
    }

    @Test
    public void calcularIndice_clave_null_lanza_excepcion() {
        assertThrows(IllegalArgumentException.class, () -> tabla.calcularIndice(null));
    }

    @Test
    public void calcularIndice_nunca_retorna_indice_negativo() {
        // Claves cuyo hashCode podría ser negativo
        HashTable<Integer, String> t = new HashTable<>(7);
        int idx = t.calcularIndice(Integer.MIN_VALUE);
        assertTrue(idx >= 0);
    }

    // =========================================================================
    // INSERTAR
    // =========================================================================

    @Test
    public void insertar_agrega_elemento_y_incrementa_contador() {
        tabla.insertar("a", 1);
        assertEquals(1, tabla.getCantidadElementos());
    }

    @Test
    public void insertar_multiple_claves_distintas_incrementa_contador() {
        tabla.insertar("a", 1);
        tabla.insertar("b", 2);
        tabla.insertar("c", 3);
        assertEquals(3, tabla.getCantidadElementos());
    }

    @Test
    public void insertar_clave_existente_sobreescribe_valor_sin_incrementar() {
        tabla.insertar("a", 1);
        tabla.insertar("a", 99);
        assertEquals(1, tabla.getCantidadElementos());
        assertEquals(99, tabla.buscar("a"));
    }

    @Test
    public void insertar_clave_null_lanza_excepcion() {
        assertThrows(IllegalArgumentException.class, () -> tabla.insertar(null, 1));
    }

    @Test
    public void insertar_valor_null_lanza_excepcion() {
        assertThrows(IllegalArgumentException.class, () -> tabla.insertar("a", null));
    }

    @Test
    public void insertar_colision_ambas_claves_conviven_en_el_mismo_slot() {
        // Forzamos colisión usando una tabla de tamaño 1
        HashTable<String, Integer> pequeña = new HashTable<>(1);
        pequeña.insertar("x", 10);
        pequeña.insertar("y", 20);
        ListaSimplementeEnlazada<EntradaHash<String, Integer>> slot = pequeña.getSlot(0);
        assertEquals(2, slot.size());
    }

    // =========================================================================
    // BUSCAR
    // =========================================================================

    @Test
    public void buscar_clave_existente_retorna_valor_correcto() {
        tabla.insertar("clave", 42);
        assertEquals(42, tabla.buscar("clave"));
    }

    @Test
    public void buscar_clave_inexistente_retorna_null() {
        assertNull(tabla.buscar("noExiste"));
    }

    @Test
    public void buscar_clave_null_lanza_excepcion() {
        assertThrows(IllegalArgumentException.class, () -> tabla.buscar(null));
    }

    @Test
    public void buscar_despues_de_sobreescribir_retorna_valor_nuevo() {
        tabla.insertar("k", 1);
        tabla.insertar("k", 2);
        assertEquals(2, tabla.buscar("k"));
    }

    @Test
    public void buscar_en_colision_retorna_valor_correcto_de_cada_clave() {
        HashTable<String, Integer> pequeña = new HashTable<>(1);
        pequeña.insertar("x", 10);
        pequeña.insertar("y", 20);
        assertEquals(10, pequeña.buscar("x"));
        assertEquals(20, pequeña.buscar("y"));
    }

    // =========================================================================
    // CONTIENE
    // =========================================================================

    @Test
    public void contiene_retorna_true_si_clave_existe() {
        tabla.insertar("presente", 5);
        assertTrue(tabla.contiene("presente"));
    }

    @Test
    public void contiene_retorna_false_si_clave_no_existe() {
        assertFalse(tabla.contiene("ausente"));
    }

    @Test
    public void contiene_retorna_false_en_tabla_vacia() {
        assertFalse(tabla.contiene("cualquier"));
    }

    @Test
    public void contiene_retorna_true_para_ambas_claves_en_colision() {
        HashTable<String, Integer> pequeña = new HashTable<>(1);
        pequeña.insertar("x", 10);
        pequeña.insertar("y", 20);
        assertTrue(pequeña.contiene("x"));
        assertTrue(pequeña.contiene("y"));
    }

    // =========================================================================
    // GET SLOT
    // =========================================================================

    @Test
    public void getSlot_indice_valido_retorna_la_lista() {
        tabla.insertar("a", 1);
        int idx = tabla.calcularIndice("a");
        ListaSimplementeEnlazada<EntradaHash<String, Integer>> slot = tabla.getSlot(idx);
        assertNotNull(slot);
        assertFalse(slot.isEmpty());
    }

    @Test
    public void getSlot_indice_negativo_lanza_excepcion() {
        assertThrows(IllegalArgumentException.class, () -> tabla.getSlot(-1));
    }

    @Test
    public void getSlot_indice_igual_a_cantidadSlots_lanza_excepcion() {
        assertThrows(IllegalArgumentException.class, () -> tabla.getSlot(tabla.getCantidadSlots()));
    }

    // =========================================================================
    // GET CANTIDAD SLOTS / GET CANTIDAD ELEMENTOS
    // =========================================================================

    @Test
    public void getCantidadSlots_retorna_el_valor_del_constructor() {
        HashTable<String, String> t = new HashTable<>(7);
        assertEquals(7, t.getCantidadSlots());
    }

    @Test
    public void getCantidadElementos_tabla_vacia_es_cero() {
        assertEquals(0, tabla.getCantidadElementos());
    }

    @Test
    public void getCantidadElementos_no_crece_con_claves_duplicadas() {
        tabla.insertar("a", 1);
        tabla.insertar("a", 2);
        tabla.insertar("a", 3);
        assertEquals(1, tabla.getCantidadElementos());
    }

    // =========================================================================
    // ENTRADA HASH (clase interna)
    // =========================================================================

    @Test
    public void entradaHash_getClave_retorna_clave_correcta() {
        EntradaHash<String, Integer> entrada = new EntradaHash<>("k", 10);
        assertEquals("k", entrada.getClave());
    }

    @Test
    public void entradaHash_getValor_retorna_valor_correcto() {
        EntradaHash<String, Integer> entrada = new EntradaHash<>("k", 10);
        assertEquals(10, entrada.getValor());
    }

    @Test
    public void entradaHash_setValor_actualiza_el_valor() {
        EntradaHash<String, Integer> entrada = new EntradaHash<>("k", 10);
        entrada.setValor(99);
        assertEquals(99, entrada.getValor());
    }

    @Test
    public void entradaHash_toString_formato_correcto() {
        EntradaHash<String, Integer> entrada = new EntradaHash<>("clave", 42);
        assertEquals("clave -> 42", entrada.toString());
    }
}
