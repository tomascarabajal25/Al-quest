package estructuras.listas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestListaConCursor {

    private ListaConCursor<Integer> lista;

    @BeforeEach
    public void setUp() {
        lista = new ListaConCursor<>();
    }

    @Test
    public void constructor_lista_nueva_esta_vacia() {
        assertTrue(lista.estaVacia());
    }

    @Test
    public void constructor_longitud_inicial_es_cero() {
        assertEquals(0, lista.getLongitud());
    }

    @Test
    public void constructor_cursor_retorna_null() {
        assertNull(lista.getCursor());
    }

    @Test
    public void estaVacia_retorna_false_despues_de_agregar() throws Exception {
        lista.agregar(1);
        assertFalse(lista.estaVacia());
    }

    @Test
    public void estaVacia_retorna_true_despues_de_remover_todo() throws Exception {
        lista.agregar(1);
        lista.remover(1);
        assertTrue(lista.estaVacia());
    }

    @Test
    public void agregar_elemento_agrega_al_final() throws Exception {
        lista.agregar(1);
        lista.agregar(2);
        assertEquals(2, lista.obtener(2));
    }

    @Test
    public void agregar_elemento_incrementa_longitud() throws Exception {
        lista.agregar(1);
        lista.agregar(2);
        assertEquals(2, lista.getLongitud());
    }

    @Test
    public void agregar_elemento_en_lista_vacia_queda_en_posicion_uno() throws Exception {
        lista.agregar(42);
        assertEquals(42, lista.obtener(1));
    }

    @Test
    public void agregar_posicion_uno_inserta_al_inicio() throws Exception {
        lista.agregar(2);
        lista.agregar(1, 1);
        assertEquals(1, lista.obtener(1));
        assertEquals(2, lista.obtener(2));
    }

    @Test
    public void agregar_posicion_final_inserta_al_final() throws Exception {
        lista.agregar(1);
        lista.agregar(2, 2);
        assertEquals(2, lista.obtener(2));
    }

    @Test
    public void agregar_posicion_medio_desplaza_elementos() throws Exception {
        lista.agregar(1);
        lista.agregar(3);
        lista.agregar(2, 2);
        assertEquals(1, lista.obtener(1));
        assertEquals(2, lista.obtener(2));
        assertEquals(3, lista.obtener(3));
    }

    @Test
    public void agregar_posicion_cero_lanza_excepcion() {
        assertThrows(Exception.class, () -> lista.agregar(0, 1));
    }

    @Test
    public void agregar_posicion_mayor_a_longitud_mas_uno_lanza_excepcion() throws Exception {
        lista.agregar(1);
        assertThrows(Exception.class, () -> lista.agregar(3, 99));
    }

    @Test
    public void remover_posicion_uno_elimina_el_primero() throws Exception {
        lista.agregar(1);
        lista.agregar(2);
        lista.remover(1);
        assertEquals(2, lista.obtener(1));
    }

    @Test
    public void remover_posicion_ultima_elimina_el_ultimo() throws Exception {
        lista.agregar(1);
        lista.agregar(2);
        lista.remover(2);
        assertEquals(1, lista.getLongitud());
        assertEquals(1, lista.obtener(1));
    }

    @Test
    public void remover_posicion_medio_reencadena_correctamente() throws Exception {
        lista.agregar(1);
        lista.agregar(2);
        lista.agregar(3);
        lista.remover(2);
        assertEquals(1, lista.obtener(1));
        assertEquals(3, lista.obtener(2));
    }

    @Test
    public void remover_decrementa_longitud() throws Exception {
        lista.agregar(1);
        lista.agregar(2);
        lista.remover(1);
        assertEquals(1, lista.getLongitud());
    }

    @Test
    public void remover_posicion_invalida_lanza_excepcion() throws Exception {
        lista.agregar(1);
        assertThrows(Exception.class, () -> lista.remover(0));
        assertThrows(Exception.class, () -> lista.remover(2));
    }

    @Test
    public void remover_posicion_resetea_el_cursor() throws Exception {
        lista.agregar(1);
        lista.agregar(2);
        lista.iniciarCursor();
        lista.avanzarCursor();
        lista.remover(1);
        assertNull(lista.getCursor());
    }

    @Test
    public void remover_valor_existente_lo_elimina() throws Exception {
        lista.agregar(1);
        lista.agregar(2);
        lista.agregar(3);
        lista.remover((Integer) 2);
        assertFalse(lista.existe(2));
        assertEquals(2, lista.getLongitud());
    }

    @Test
    public void remover_valor_elimina_solo_primera_ocurrencia() throws Exception {
        lista.agregar(1);
        lista.agregar(2);
        lista.agregar(2);
        lista.remover((Integer) 2);
        assertEquals(2, lista.getLongitud());
        assertTrue(lista.existe(2));
    }

    @Test
    public void remover_valor_inexistente_lanza_excepcion() throws Exception {
        lista.agregar(1);
        assertThrows(Exception.class, () -> lista.remover((Integer) 99));
    }

    @Test
    public void obtener_posicion_valida_retorna_dato_correcto() throws Exception {
        lista.agregar(10);
        lista.agregar(20);
        assertEquals(20, lista.obtener(2));
    }

    @Test
    public void obtener_posicion_cero_lanza_excepcion() {
        assertThrows(Exception.class, () -> lista.obtener(0));
    }

    @Test
    public void obtener_posicion_mayor_a_longitud_lanza_excepcion() throws Exception {
        lista.agregar(1);
        assertThrows(Exception.class, () -> lista.obtener(2));
    }

    @Test
    public void cambiar_reemplaza_el_elemento_en_la_posicion() throws Exception {
        lista.agregar(1);
        lista.agregar(2);
        lista.cambiar(99, 2);
        assertEquals(99, lista.obtener(2));
    }

    @Test
    public void cambiar_posicion_invalida_lanza_excepcion() throws Exception {
        lista.agregar(1);
        assertThrows(Exception.class, () -> lista.cambiar(99, 0));
        assertThrows(Exception.class, () -> lista.cambiar(99, 2));
    }

    @Test
    public void cursor_recorre_todos_los_elementos_en_orden() throws Exception {
        lista.agregar(1);
        lista.agregar(2);
        lista.agregar(3);
        lista.iniciarCursor();
        assertTrue(lista.avanzarCursor());  assertEquals(1, lista.obtenerCursor());
        assertTrue(lista.avanzarCursor());  assertEquals(2, lista.obtenerCursor());
        assertTrue(lista.avanzarCursor());  assertEquals(3, lista.obtenerCursor());
        assertFalse(lista.avanzarCursor());
    }

    @Test
    public void cursor_lista_vacia_avanzar_retorna_false() {
        lista.iniciarCursor();
        assertFalse(lista.avanzarCursor());
    }

    @Test
    public void obtenerCursor_sin_avanzar_retorna_null() throws Exception {
        lista.agregar(1);
        lista.iniciarCursor();
        assertNull(lista.obtenerCursor());
    }

    @Test
    public void iniciarCursor_reinicia_el_recorrido() throws Exception {
        lista.agregar(1);
        lista.agregar(2);
        lista.iniciarCursor();
        lista.avanzarCursor();
        lista.iniciarCursor();
        lista.avanzarCursor();
        assertEquals(1, lista.obtenerCursor());
    }

    @Test
    public void getCursor_es_equivalente_a_obtenerCursor() throws Exception {
        lista.agregar(5);
        lista.iniciarCursor();
        lista.avanzarCursor();
        assertEquals(lista.obtenerCursor(), lista.getCursor());
    }

    @Test
    public void existe_retorna_true_si_el_valor_esta_en_la_lista() throws Exception {
        lista.agregar(7);
        assertTrue(lista.existe(7));
    }

    @Test
    public void existe_retorna_false_si_el_valor_no_esta() throws Exception {
        lista.agregar(1);
        assertFalse(lista.existe(99));
    }

    @Test
    public void existe_retorna_false_en_lista_vacia() {
        assertFalse(lista.existe(1));
    }


    @Test
    public void contarOcurrencias_sin_apariciones_retorna_cero() throws Exception {
        lista.agregar(1);
        assertEquals(0, lista.contarOcurrencias(99));
    }

    @Test
    public void contarOcurrencias_una_aparicion_retorna_uno() throws Exception {
        lista.agregar(1);
        lista.agregar(2);
        assertEquals(1, lista.contarOcurrencias(2));
    }

    @Test
    public void contarOcurrencias_multiples_apariciones_cuenta_correctamente() throws Exception {
        lista.agregar(1);
        lista.agregar(2);
        lista.agregar(2);
        lista.agregar(2);
        assertEquals(3, lista.contarOcurrencias(2));
    }

    @Test
    public void contarOcurrencias_lista_vacia_retorna_cero() {
        assertEquals(0, lista.contarOcurrencias(5));
    }

    @Test
    public void contiene_retorna_true_si_existe() throws Exception {
        lista.agregar(3);
        assertTrue(lista.contiene(3));
    }

    @Test
    public void contiene_retorna_false_si_no_existe() throws Exception {
        lista.agregar(1);
        assertFalse(lista.contiene(99));
    }

    @Test
    public void contiene_null_lanza_excepcion() {
        assertThrows(Exception.class, () -> lista.contiene(null));
    }

    @Test
    public void contieneS3_retorna_true_si_existe() throws Exception {
        lista.agregar(5);
        assertTrue(lista.contieneS3(5));
    }

    @Test
    public void contieneS3_null_lanza_excepcion() {
        assertThrows(Exception.class, () -> lista.contieneS3(null));
    }

    @Test
    public void contieneS4_retorna_true_comparando_toString() throws Exception {
        lista.agregar(42);
        assertTrue(lista.contieneS4(42));
    }

    @Test
    public void contieneS4_null_lanza_excepcion() {
        assertThrows(Exception.class, () -> lista.contieneS4(null));
    }


    @Test
    public void getLongitud_crece_con_cada_agregar() throws Exception {
        lista.agregar(1);
        assertEquals(1, lista.getLongitud());
        lista.agregar(2);
        assertEquals(2, lista.getLongitud());
    }

    @Test
    public void getLongitud_decrece_con_cada_remover() throws Exception {
        lista.agregar(1);
        lista.agregar(2);
        lista.remover(1);
        assertEquals(1, lista.getLongitud());
    }

    @Test
    public void getLongitud_no_cambia_con_obtener_ni_cursor() throws Exception {
        lista.agregar(1);
        lista.obtener(1);
        lista.iniciarCursor();
        lista.avanzarCursor();
        assertEquals(1, lista.getLongitud());
    }
}
