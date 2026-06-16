package estructuras.listas;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TestListaDoblementeEnlazada {

    private ListaDoblementeEnlazada<Integer> lista;

    @BeforeEach
    public void setUp() {
        lista = new ListaDoblementeEnlazada<>();
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
    public void constructor_primero_y_ultimo_son_null() {
        assertNull(lista.getPrimero());
        assertNull(lista.getUltimo());
    }

    @Test
    public void add_agrega_al_final_y_retorna_true() {
        assertTrue(lista.add(1));
        assertEquals(1, lista.get(0));
    }

    @Test
    public void add_primer_elemento_actualiza_primero_y_ultimo() {
        lista.add(42);
        assertNotNull(lista.getPrimero());
        assertNotNull(lista.getUltimo());
        assertEquals(42, lista.getPrimero().getDato());
        assertEquals(42, lista.getUltimo().getDato());
    }

    @Test
    public void add_multiple_mantiene_orden_de_insercion() {
        lista.add(1); lista.add(2); lista.add(3);
        assertEquals(1, lista.get(0));
        assertEquals(2, lista.get(1));
        assertEquals(3, lista.get(2));
    }

    @Test
    public void add_actualiza_ultimo_correctamente() {
        lista.add(1); lista.add(2);
        assertEquals(2, lista.getUltimo().getDato());
    }

    @Test
    public void add_enlace_bidireccional_correcto() {
        lista.add(1); lista.add(2); lista.add(3);
        // navegación hacia atrás desde el último
        assertEquals(2, lista.getUltimo().getAnterior().getDato());
        assertEquals(1, lista.getUltimo().getAnterior().getAnterior().getDato());
    }

    @Test
    public void add_acepta_null() {
        assertDoesNotThrow(() -> lista.add(null));
        assertNull(lista.get(0));
    }

    @Test
    public void add_index_cero_inserta_al_inicio_y_actualiza_primero() {
        lista.add(2);
        lista.add(0, 1);
        assertEquals(1, lista.get(0));
        assertEquals(1, lista.getPrimero().getDato());
    }

    @Test
    public void add_index_final_delega_en_add() {
        lista.add(1);
        lista.add(1, 2);
        assertEquals(2, lista.get(1));
        assertEquals(2, lista.getUltimo().getDato());
    }

    @Test
    public void add_index_medio_mantiene_enlaces_bidireccionales() {
        lista.add(1); lista.add(3);
        lista.add(1, 2);
        assertEquals(1, lista.get(0));
        assertEquals(2, lista.get(1));
        assertEquals(3, lista.get(2));
        // anterior del nodo 2 debe ser el nodo 1
        assertEquals(1, lista.getPrimero().getSiguiente().getAnterior().getDato());
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
    public void get_retorna_elemento_correcto_desde_inicio() {
        lista.add(10); lista.add(20); lista.add(30);
        assertEquals(10, lista.get(0));
    }

    @Test
    public void get_retorna_elemento_correcto_desde_final() {
        lista.add(10); lista.add(20); lista.add(30);
        assertEquals(30, lista.get(2));
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
    public void set_reemplaza_y_retorna_el_valor_viejo() {
        lista.add(1); lista.add(2);
        assertEquals(2, lista.set(1, 99));
        assertEquals(99, lista.get(1));
    }

    @Test
    public void set_index_invalido_lanza_excepcion() {
        assertThrows(IndexOutOfBoundsException.class, () -> lista.set(0, 1));
    }

    @Test
    public void remove_index_retorna_elemento_eliminado() {
        lista.add(10); lista.add(20);
        assertEquals(10, lista.remove(0));
    }

    @Test
    public void remove_index_cero_actualiza_primero() {
        lista.add(1); lista.add(2);
        lista.remove(0);
        assertEquals(2, lista.getPrimero().getDato());
        assertNull(lista.getPrimero().getAnterior());
    }

    @Test
    public void remove_index_ultimo_actualiza_ultimo() {
        lista.add(1); lista.add(2);
        lista.remove(1);
        assertEquals(1, lista.getUltimo().getDato());
        assertNull(lista.getUltimo().getSiguiente());
    }

    @Test
    public void remove_index_medio_mantiene_enlaces_bidireccionales() {
        lista.add(1); lista.add(2); lista.add(3);
        lista.remove(1);
        assertEquals(1, lista.get(0));
        assertEquals(3, lista.get(1));
        assertEquals(3, lista.getPrimero().getSiguiente().getDato());
        assertEquals(1, lista.getUltimo().getAnterior().getDato());
    }

    @Test
    public void remove_index_unico_elemento_deja_lista_vacia() {
        lista.add(5);
        lista.remove(0);
        assertTrue(lista.isEmpty());
        assertNull(lista.getPrimero());
        assertNull(lista.getUltimo());
    }

    @Test
    public void remove_index_invalido_lanza_excepcion() {
        assertThrows(IndexOutOfBoundsException.class, () -> lista.remove(0));
        assertThrows(IndexOutOfBoundsException.class, () -> lista.remove(-1));
    }

    @Test
    public void remove_object_elimina_primera_ocurrencia_y_retorna_true() {
        lista.add(1); lista.add(2); lista.add(1);
        assertTrue(lista.remove((Object) 1));
        assertEquals(2, lista.size());
        assertEquals(2, lista.get(0));
    }

    @Test
    public void remove_object_primero_actualiza_primero() {
        lista.add(1); lista.add(2);
        lista.remove((Object) 1);
        assertEquals(2, lista.getPrimero().getDato());
        assertNull(lista.getPrimero().getAnterior());
    }

    @Test
    public void remove_object_ultimo_actualiza_ultimo() {
        lista.add(1); lista.add(2);
        lista.remove((Object) 2);
        assertEquals(1, lista.getUltimo().getDato());
        assertNull(lista.getUltimo().getSiguiente());
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
    public void indexOf_retorna_primer_indice_correcto() {
        lista.add(1); lista.add(2); lista.add(1);
        assertEquals(0, lista.indexOf(1));
    }

    @Test
    public void indexOf_retorna_menos_uno_si_no_existe() {
        lista.add(1);
        assertEquals(-1, lista.indexOf(99));
    }

    @Test
    public void lastIndexOf_retorna_ultimo_indice_correcto() {
        lista.add(1); lista.add(2); lista.add(1);
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
    public void clear_vacia_la_lista_y_anula_punteros() {
        lista.add(1); lista.add(2);
        lista.clear();
        assertTrue(lista.isEmpty());
        assertEquals(0, lista.size());
        assertNull(lista.getPrimero());
        assertNull(lista.getUltimo());
    }

    @Test
    public void clear_lista_ya_vacia_no_falla() {
        assertDoesNotThrow(() -> lista.clear());
    }

    @Test
    public void iterator_recorre_en_orden() {
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
    public void iterator_hasNext_false_lista_vacia() {
        assertFalse(lista.iterator().hasNext());
    }

    @Test
    public void toArray_retorna_elementos_en_orden() {
        lista.add(1); lista.add(2); lista.add(3);
        assertArrayEquals(new Object[]{1, 2, 3}, lista.toArray());
    }

    @Test
    public void toArray_tipado_retorna_arreglo_correcto() {
        lista.add(1); lista.add(2);
        assertArrayEquals(new Integer[]{1, 2}, lista.toArray(new Integer[0]));
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
        assertEquals(3, lista.getUltimo().getDato());
    }

    @Test
    public void addAll_index_inserta_en_posicion_correcta() {
        lista.add(1); lista.add(4);
        lista.addAll(1, List.of(2, 3));
        assertEquals(4, lista.size());
        assertEquals(2, lista.get(1));
        assertEquals(3, lista.get(2));
    }

    @Test
    public void removeAll_elimina_todos_los_de_la_coleccion() {
        lista.add(1); lista.add(2); lista.add(3);
        lista.removeAll(List.of(1, 3));
        assertFalse(lista.contains(1));
        assertFalse(lista.contains(3));
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
    public void listIterator_next_recorre_en_orden() {
        lista.add(1); lista.add(2); lista.add(3);
        ListIterator<Integer> it = lista.listIterator();
        assertEquals(1, it.next());
        assertEquals(2, it.next());
        assertEquals(3, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    public void listIterator_previous_navega_hacia_atras() {
        lista.add(1); lista.add(2); lista.add(3);
        ListIterator<Integer> it = lista.listIterator();
        it.next(); it.next(); it.next();
        assertEquals(3, it.previous());
        assertEquals(2, it.previous());
        assertEquals(1, it.previous());
        assertFalse(it.hasPrevious());
    }

    @Test
    public void listIterator_nextIndex_y_previousIndex_correctos() {
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
    public void listIterator_remove_sin_next_lanza_excepcion() {
        lista.add(1);
        assertThrows(IllegalStateException.class, () -> lista.listIterator().remove());
    }

    @Test
    public void listIterator_next_sin_elementos_lanza_excepcion() {
        assertThrows(NoSuchElementException.class, () -> lista.listIterator().next());
    }

    @Test
    public void addSorted_lista_vacia_inicializa_primero_y_ultimo() {
        lista.addSorted(5);
        assertEquals(5, lista.getPrimero().getDato());
        assertEquals(5, lista.getUltimo().getDato());
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
    public void addSorted_elemento_menor_queda_al_inicio_y_actualiza_primero() {
        lista.addSorted(5);
        lista.addSorted(1);
        assertEquals(1, lista.getPrimero().getDato());
        assertNull(lista.getPrimero().getAnterior());
    }

    @Test
    public void addSorted_elemento_mayor_queda_al_final_y_actualiza_ultimo() {
        lista.addSorted(1);
        lista.addSorted(9);
        assertEquals(9, lista.getUltimo().getDato());
        assertNull(lista.getUltimo().getSiguiente());
    }

    @Test
    public void addSorted_elementos_iguales_mantienen_orden() {
        lista.addSorted(2);
        lista.addSorted(2);
        lista.addSorted(1);
        assertEquals(1, lista.get(0));
        assertEquals(2, lista.get(1));
        assertEquals(2, lista.get(2));
    }

    @Test
    public void addSorted_enlace_bidireccional_correcto_en_medio() {
        lista.addSorted(1);
        lista.addSorted(3);
        lista.addSorted(2);
        // nodo 2 debe tener anterior=1 y siguiente=3
        assertEquals(1, lista.getPrimero().getSiguiente().getAnterior().getDato());
        assertEquals(3, lista.getPrimero().getSiguiente().getSiguiente().getDato());
    }

    @Test
    public void addSorted_tipo_no_comparable_lanza_excepcion() {
        ListaDoblementeEnlazada<Object> listaObj = new ListaDoblementeEnlazada<>();
        assertThrows(IllegalArgumentException.class, () -> listaObj.addSorted(new Object()));
    }

    @Test
    public void getPrimero_retorna_el_primer_nodo() {
        lista.add(10); lista.add(20);
        assertEquals(10, lista.getPrimero().getDato());
    }

    @Test
    public void getUltimo_retorna_el_ultimo_nodo() {
        lista.add(10); lista.add(20);
        assertEquals(20, lista.getUltimo().getDato());
    }

    @Test
    public void getPrimero_y_getUltimo_apuntan_al_mismo_con_un_elemento() {
        lista.add(42);
        assertSame(lista.getPrimero(), lista.getUltimo());
    }
}
