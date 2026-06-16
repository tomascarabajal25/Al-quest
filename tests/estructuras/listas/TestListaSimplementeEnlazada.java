package estructuras.listas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TestListaSimplementeEnlazada {

    private ListaSimplementeEnlazada<Integer> lista;

    @BeforeEach
    public void setUp() {
        lista = new ListaSimplementeEnlazada<>();
    }

    @Test
    public void constructor_lista_nueva_esta_vacia() {
        assertTrue(lista.isEmpty());
    }

    @Test
    public void constructor_lista_nueva_size_es_cero() {
        assertEquals(0, lista.size());
    }

    @Test
    public void add_agrega_al_final_y_retorna_true() {
        assertTrue(lista.add(1));
        assertEquals(1, lista.get(0));
    }

    @Test
    public void add_multiple_mantiene_orden_de_insercion() {
        lista.add(1);
        lista.add(2);
        lista.add(3);
        assertEquals(1, lista.get(0));
        assertEquals(2, lista.get(1));
        assertEquals(3, lista.get(2));
    }

    @Test
    public void addLast_agrega_al_final() {
        lista.add(1);
        lista.addLast(99);
        assertEquals(99, lista.get(lista.size() - 1));
    }

    @Test
    public void add_incrementa_size() {
        lista.add(1);
        lista.add(2);
        assertEquals(2, lista.size());
    }

    @Test
    public void add_acepta_null() {
        assertDoesNotThrow(() -> lista.add(null));
        assertNull(lista.get(0));
    }

    @Test
    public void add_index_cero_inserta_al_inicio() {
        lista.add(2);
        lista.add(0, 1);
        assertEquals(1, lista.get(0));
        assertEquals(2, lista.get(1));
    }

    @Test
    public void add_index_final_inserta_al_final() {
        lista.add(1);
        lista.add(1, 2);
        assertEquals(2, lista.get(1));
    }

    @Test
    public void add_index_medio_desplaza_elementos() {
        lista.add(1);
        lista.add(3);
        lista.add(1, 2);
        assertEquals(1, lista.get(0));
        assertEquals(2, lista.get(1));
        assertEquals(3, lista.get(2));
    }

    @Test
    public void add_index_negativo_lanza_excepcion() {
        assertThrows(IndexOutOfBoundsException.class, () -> lista.add(-1, 1));
    }

    @Test
    public void add_index_mayor_a_size_lanza_excepcion() {
        assertThrows(IndexOutOfBoundsException.class, () -> lista.add(1, 1));
    }

    @Test
    public void get_retorna_elemento_correcto() {
        lista.add(10);
        lista.add(20);
        assertEquals(20, lista.get(1));
    }

    @Test
    public void get_index_negativo_lanza_excepcion() {
        lista.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> lista.get(-1));
    }

    @Test
    public void get_index_igual_a_size_lanza_excepcion() {
        lista.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> lista.get(1));
    }

    @Test
    public void set_reemplaza_elemento_y_retorna_el_viejo() {
        lista.add(1);
        lista.add(2);
        assertEquals(2, lista.set(1, 99));
        assertEquals(99, lista.get(1));
    }

    @Test
    public void set_index_invalido_lanza_excepcion() {
        assertThrows(IndexOutOfBoundsException.class, () -> lista.set(0, 1));
    }

    @Test
    public void remove_index_retorna_elemento_eliminado() {
        lista.add(10);
        lista.add(20);
        assertEquals(10, lista.remove(0));
    }

    @Test
    public void remove_index_reduce_size() {
        lista.add(1);
        lista.add(2);
        lista.remove(0);
        assertEquals(1, lista.size());
    }

    @Test
    public void remove_index_cero_elimina_primero() {
        lista.add(1);
        lista.add(2);
        lista.remove(0);
        assertEquals(2, lista.get(0));
    }

    @Test
    public void remove_index_ultimo_elemento() {
        lista.add(1);
        lista.add(2);
        lista.remove(1);
        assertEquals(1, lista.size());
        assertEquals(1, lista.get(0));
    }

    @Test
    public void remove_index_invalido_lanza_excepcion() {
        assertThrows(IndexOutOfBoundsException.class, () -> lista.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> lista.remove(-1));
    }

    @Test
    public void remove_object_elimina_primera_ocurrencia_y_retorna_true() {
        lista.add(1);
        lista.add(2);
        lista.add(1);
        assertTrue(lista.remove((Object) 1));
        assertEquals(2, lista.size());
        assertEquals(2, lista.get(0));
    }

    @Test
    public void remove_object_retorna_false_si_no_existe() {
        lista.add(1);
        assertFalse(lista.remove((Object) 99));
    }

    @Test
    public void remove_object_lista_vacia_retorna_false() {
        assertFalse(lista.remove((Object) 1));
    }

    @Test
    public void contains_retorna_true_si_existe() {
        lista.add(5);
        assertTrue(lista.contains(5));
    }

    @Test
    public void contains_retorna_false_si_no_existe() {
        assertFalse(lista.contains(99));
    }

    @Test
    public void contains_null_retorna_true_si_hay_null() {
        lista.add(null);
        assertTrue(lista.contains(null));
    }

    @Test
    public void indexOf_retorna_indice_correcto() {
        lista.add(1);
        lista.add(2);
        lista.add(1);
        assertEquals(0, lista.indexOf(1));
    }

    @Test
    public void indexOf_retorna_menos_uno_si_no_existe() {
        lista.add(1);
        assertEquals(-1, lista.indexOf(99));
    }

    @Test
    public void lastIndexOf_retorna_ultimo_indice() {
        lista.add(1);
        lista.add(2);
        lista.add(1);
        assertEquals(2, lista.lastIndexOf(1));
    }

    @Test
    public void lastIndexOf_retorna_menos_uno_si_no_existe() {
        assertEquals(-1, lista.lastIndexOf(99));
    }

    @Test
    public void isEmpty_retorna_false_con_elementos() {
        lista.add(1);
        assertFalse(lista.isEmpty());
    }

    @Test
    public void clear_vacia_la_lista() {
        lista.add(1);
        lista.add(2);
        lista.clear();
        assertTrue(lista.isEmpty());
        assertEquals(0, lista.size());
    }

    @Test
    public void clear_lista_ya_vacia_no_falla() {
        assertDoesNotThrow(() -> lista.clear());
    }


    @Test
    public void cursor_recorre_todos_los_elementos_en_orden() {
        lista.add(1);
        lista.add(2);
        lista.add(3);
        lista.iniciarCursor();
        assertTrue(lista.avanzarCursor()); assertEquals(1, lista.obtenerCursor());
        assertTrue(lista.avanzarCursor()); assertEquals(2, lista.obtenerCursor());
        assertTrue(lista.avanzarCursor()); assertEquals(3, lista.obtenerCursor());
        assertFalse(lista.avanzarCursor());
    }

    @Test
    public void cursor_lista_vacia_avanzar_retorna_false() {
        lista.iniciarCursor();
        assertFalse(lista.avanzarCursor());
    }

    @Test
    public void obtenerCursor_sin_avanzar_retorna_null() {
        lista.add(1);
        lista.iniciarCursor();
        assertNull(lista.obtenerCursor());
    }

    @Test
    public void iniciarCursor_reinicia_el_recorrido() {
        lista.add(1);
        lista.add(2);
        lista.iniciarCursor();
        lista.avanzarCursor();
        lista.iniciarCursor();
        lista.avanzarCursor();
        assertEquals(1, lista.obtenerCursor());
    }

    @Test
    public void equals_listas_iguales_retorna_true() {
        ListaSimplementeEnlazada<Integer> otra = new ListaSimplementeEnlazada<>();
        lista.add(1); lista.add(2);
        otra.add(1);  otra.add(2);
        assertEquals(lista, otra);
    }

    @Test
    public void equals_listas_distintas_retorna_false() {
        ListaSimplementeEnlazada<Integer> otra = new ListaSimplementeEnlazada<>();
        lista.add(1);
        otra.add(2);
        assertNotEquals(lista, otra);
    }

    @Test
    public void equals_distinto_tamanio_retorna_false() {
        ListaSimplementeEnlazada<Integer> otra = new ListaSimplementeEnlazada<>();
        lista.add(1);
        assertNotEquals(lista, otra);
    }

    @Test
    public void equals_mismo_objeto_retorna_true() {
        assertEquals(lista, lista);
    }

    @Test
    public void equals_null_retorna_false() {
        assertNotEquals(null, lista);
    }

    @Test
    public void hashCode_listas_iguales_mismo_hash() {
        ListaSimplementeEnlazada<Integer> otra = new ListaSimplementeEnlazada<>();
        lista.add(1); lista.add(2);
        otra.add(1);  otra.add(2);
        assertEquals(lista.hashCode(), otra.hashCode());
    }

    @Test
    public void toString_formato_correcto() {
        lista.add(1);
        lista.add(2);
        lista.add(3);
        assertEquals("[1, 2, 3]", lista.toString());
    }

    @Test
    public void toString_lista_vacia() {
        assertEquals("[]", lista.toString());
    }

    @Test
    public void iterator_recorre_elementos_en_orden() {
        lista.add(10); lista.add(20); lista.add(30);
        Iterator<Integer> it = lista.iterator();
        assertEquals(10, it.next());
        assertEquals(20, it.next());
        assertEquals(30, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void iterator_next_sin_elementos_lanza_excepcion() {
        assertThrows(NoSuchElementException.class, () -> lista.iterator().next());
    }

    @Test
    public void iterator_remove_elimina_elemento_actual() {
        lista.add(1); lista.add(2); lista.add(3);
        Iterator<Integer> it = lista.iterator();
        it.next();
        it.remove();
        assertEquals(2, lista.size());
        assertEquals(2, lista.get(0));
    }

    @Test
    public void iterator_remove_sin_next_lanza_excepcion() {
        lista.add(1);
        Iterator<Integer> it = lista.iterator();
        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    public void toArray_retorna_elementos_en_orden() {
        lista.add(1); lista.add(2); lista.add(3);
        assertArrayEquals(new Object[]{1, 2, 3}, lista.toArray());
    }

    @Test
    public void toArray_tipado_retorna_arreglo_correcto() {
        lista.add(1); lista.add(2);
        Integer[] arr = lista.toArray(new Integer[0]);
        assertArrayEquals(new Integer[]{1, 2}, arr);
    }

    @Test
    public void toArray_lista_vacia_retorna_arreglo_vacio() {
        assertEquals(0, lista.toArray().length);
    }

    @Test
    public void containsAll_retorna_true_si_contiene_todos() {
        lista.add(1); lista.add(2); lista.add(3);
        assertTrue(lista.containsAll(List.of(1, 3)));
    }

    @Test
    public void containsAll_retorna_false_si_falta_alguno() {
        lista.add(1);
        assertFalse(lista.containsAll(List.of(1, 99)));
    }

    @Test
    public void addAll_agrega_coleccion_al_final() {
        lista.add(0);
        lista.addAll(List.of(1, 2, 3));
        assertEquals(4, lista.size());
        assertEquals(3, lista.get(3));
    }

    @Test
    public void addAll_index_inserta_en_posicion_correcta() {
        lista.add(1); lista.add(4);
        lista.addAll(1, List.of(2, 3));
        assertEquals(List.of(1, 2, 3, 4), List.copyOf(lista));
    }

    @Test
    public void removeAll_elimina_elementos_de_la_coleccion() {
        lista.add(1); lista.add(2); lista.add(3);
        lista.removeAll(List.of(1, 3));
        assertFalse(lista.contains(1));
        assertFalse(lista.contains(3));
        assertTrue(lista.contains(2));
    }

    @Test
    public void retainAll_conserva_solo_elementos_de_la_coleccion() {
        lista.add(1); lista.add(2); lista.add(3);
        lista.retainAll(List.of(2));
        assertEquals(1, lista.size());
        assertTrue(lista.contains(2));
    }

    @Test
    public void subList_retorna_sublista_correcta() {
        lista.add(1); lista.add(2); lista.add(3); lista.add(4);
        List<Integer> sub = lista.subList(1, 3);
        assertEquals(2, sub.size());
        assertEquals(2, sub.get(0));
        assertEquals(3, sub.get(1));
    }

    @Test
    public void subList_indices_invalidos_lanza_excepcion() {
        lista.add(1);
        assertThrows(IndexOutOfBoundsException.class, () -> lista.subList(-1, 1));
        assertThrows(IndexOutOfBoundsException.class, () -> lista.subList(1, 0));
    }

    @Test
    public void listIterator_next_y_previous_navegan_correctamente() {
        lista.add(1); lista.add(2); lista.add(3);
        ListIterator<Integer> it = lista.listIterator();
        assertEquals(1, it.next());
        assertEquals(2, it.next());
        assertEquals(2, it.previous());
        assertEquals(1, it.previous());
        assertFalse(it.hasPrevious());
    }

    @Test
    public void listIterator_nextIndex_y_previousIndex_son_correctos() {
        lista.add(1); lista.add(2);
        ListIterator<Integer> it = lista.listIterator();
        assertEquals(0, it.nextIndex());
        assertEquals(-1, it.previousIndex());
        it.next();
        assertEquals(1, it.nextIndex());
        assertEquals(0, it.previousIndex());
    }

    @Test
    public void listIterator_set_reemplaza_ultimo_retornado() {
        lista.add(1); lista.add(2);
        ListIterator<Integer> it = lista.listIterator();
        it.next();
        it.set(99);
        assertEquals(99, lista.get(0));
    }

    @Test
    public void listIterator_add_inserta_en_posicion_actual() {
        lista.add(1); lista.add(3);
        ListIterator<Integer> it = lista.listIterator(1);
        it.add(2);
        assertEquals(3, lista.size());
        assertEquals(2, lista.get(1));
    }

    @Test
    public void listIterator_remove_sin_next_lanza_excepcion() {
        lista.add(1);
        ListIterator<Integer> it = lista.listIterator();
        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    public void listIterator_index_invalido_lanza_excepcion() {
        assertThrows(IndexOutOfBoundsException.class, () -> lista.listIterator(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> lista.listIterator(1));
    }

    @Test
    public void addSorted_lista_vacia_inserta_correctamente() {
        lista.addSorted(5);
        assertEquals(1, lista.size());
        assertEquals(5, lista.get(0));
    }

    @Test
    public void addSorted_mantiene_orden_ascendente() {
        lista.addSorted(3);
        lista.addSorted(1);
        lista.addSorted(4);
        lista.addSorted(2);
        assertEquals(1, lista.get(0));
        assertEquals(2, lista.get(1));
        assertEquals(3, lista.get(2));
        assertEquals(4, lista.get(3));
    }

    @Test
    public void addSorted_elemento_menor_queda_al_inicio() {
        lista.addSorted(5);
        lista.addSorted(1);
        assertEquals(1, lista.get(0));
    }

    @Test
    public void addSorted_elemento_mayor_queda_al_final() {
        lista.addSorted(1);
        lista.addSorted(9);
        assertEquals(9, lista.get(1));
    }

    @Test
    public void addSorted_elementos_iguales_no_rompe_orden() {
        lista.addSorted(2);
        lista.addSorted(2);
        lista.addSorted(1);
        assertEquals(1, lista.get(0));
        assertEquals(2, lista.get(1));
        assertEquals(2, lista.get(2));
    }

    @Test
    public void addSorted_null_lanza_NullPointerException() {
        assertThrows(NullPointerException.class, () -> lista.addSorted(null));
    }

    @Test
    public void addSorted_tipo_no_comparable_lanza_IllegalArgumentException() {
        ListaSimplementeEnlazada<Object> listaObj = new ListaSimplementeEnlazada<>();
        assertThrows(IllegalArgumentException.class, () -> listaObj.addSorted(new Object()));
    }
}
