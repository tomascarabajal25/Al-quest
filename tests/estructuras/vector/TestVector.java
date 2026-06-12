package estructuras.vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class TestVector {

    // -------------------------------------------------------------------------
    // FIXTURES
    // -------------------------------------------------------------------------

    private Vector<Integer> vectorInt;
    private Vector<String>  vectorStr;

    @BeforeEach
    public void setUp() {
        vectorInt = new Vector<>(5, 0);
        vectorStr = new Vector<>(3, null);
    }

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================

    @Test
    public void constructor_longitud_valida_crea_vector_con_dato_inicial() {
        Vector<Integer> v = new Vector<>(3, 99);
        assertEquals(3, v.getLongitud());
        for (int i = 1; i <= 3; i++) {
            assertEquals(99, v.obtener(i));
        }
    }

    @Test
    public void constructor_longitud_uno_es_valida() {
        Vector<String> v = new Vector<>(1, "x");
        assertEquals(1, v.getLongitud());
        assertEquals("x", v.obtener(1));
    }

    @Test
    public void constructor_longitud_cero_lanza_excepcion() {
        assertThrows(RuntimeException.class, () -> new Vector<>(0, 0));
    }

    @Test
    public void constructor_longitud_negativa_lanza_excepcion() {
        assertThrows(RuntimeException.class, () -> new Vector<>(-5, 0));
    }

    // =========================================================================
    // CONSTRUCTOR COPIA
    // =========================================================================

    @Test
    public void constructorCopia_genera_vector_igual_al_original() {
        vectorInt.agregar(1, 10);
        vectorInt.agregar(2, 20);
        Vector<Integer> copia = new Vector<>(vectorInt);
        assertEquals(vectorInt, copia);
    }

    @Test
    public void constructorCopia_es_independiente_del_original() {
        vectorInt.agregar(1, 10);
        Vector<Integer> copia = new Vector<>(vectorInt);
        copia.agregar(1, 99);
        assertNotEquals(vectorInt, copia);
    }

    // =========================================================================
    // AGREGAR (posicion, dato)
    // =========================================================================

    @Test
    public void agregar_posicion_dato_guarda_correctamente() {
        vectorInt.agregar(3, 42);
        assertEquals(42, vectorInt.obtener(3));
    }

    @Test
    public void agregar_posicion_uno_es_valida() {
        vectorInt.agregar(1, 7);
        assertEquals(7, vectorInt.obtener(1));
    }

    @Test
    public void agregar_posicion_maxima_es_valida() {
        vectorInt.agregar(5, 77);
        assertEquals(77, vectorInt.obtener(5));
    }

    @Test
    public void agregar_posicion_cero_lanza_excepcion() {
        assertThrows(RuntimeException.class, () -> vectorInt.agregar(0, 1));
    }

    @Test
    public void agregar_posicion_mayor_a_longitud_lanza_excepcion() {
        assertThrows(RuntimeException.class, () -> vectorInt.agregar(6, 1));
    }

    // =========================================================================
    // AGREGAR (dato) – auto-posicion + redimensionado
    // =========================================================================

    @Test
    public void agregar_dato_devuelve_posicion_correcta() {
        int pos = vectorInt.agregar(55);
        assertEquals(55, vectorInt.obtener(pos));
    }

    @Test
    public void agregar_dato_llena_primera_posicion_libre() {
        vectorInt.agregar(1, 10);
        int pos = vectorInt.agregar(20);
        // La primera libre es la 2 (dato inicial = 0)
        assertEquals(2, pos);
    }

    @Test
    public void agregar_dato_redimensiona_cuando_vector_lleno() {
        Vector<Integer> pequeño = new Vector<>(2, 0);
        pequeño.agregar(1, 1);
        pequeño.agregar(2, 2);
        // El vector está lleno; agregar debe expandirlo
        int pos = pequeño.agregar(3);
        assertEquals(3, pequeño.obtener(pos));
        assertTrue(pequeño.getLongitud() >= 3);
    }

    // =========================================================================
    // OBTENER
    // =========================================================================

    @Test
    public void obtener_posicion_valida_retorna_dato() {
        vectorInt.agregar(2, 99);
        assertEquals(99, vectorInt.obtener(2));
    }

    @Test
    public void obtener_posicion_cero_lanza_excepcion() {
        assertThrows(RuntimeException.class, () -> vectorInt.obtener(0));
    }

    @Test
    public void obtener_posicion_negativa_lanza_excepcion() {
        assertThrows(RuntimeException.class, () -> vectorInt.obtener(-1));
    }

    @Test
    public void obtener_posicion_mayor_longitud_lanza_excepcion() {
        assertThrows(RuntimeException.class, () -> vectorInt.obtener(6));
    }

    // =========================================================================
    // REMOVER (posicion)
    // =========================================================================

    @Test
    public void remover_posicion_valida_restaura_dato_inicial() throws Exception {
        vectorInt.agregar(3, 55);
        vectorInt.remover(3);
        assertEquals(0, vectorInt.obtener(3));
    }

    @Test
    public void remover_posicion_invalida_lanza_excepcion() {
        assertThrows(Exception.class, () -> vectorInt.remover(0));
        assertThrows(Exception.class, () -> vectorInt.remover(6));
    }

    // =========================================================================
    // REMOVE (dato) – todas las apariciones
    // =========================================================================

    @Test
    public void remove_dato_elimina_todas_las_apariciones() {
        vectorStr = new Vector<>(5, null);
        vectorStr.agregar(1, "A");
        vectorStr.agregar(2, "B");
        vectorStr.agregar(3, "A");
        vectorStr.remove("A");
        assertFalse(vectorStr.contains("A"));
        assertTrue(vectorStr.contains("B"));
    }

    @Test
    public void remove_dato_null_lanza_excepcion() {
        assertThrows(RuntimeException.class, () -> vectorInt.remove(null));
    }

    // =========================================================================
    // REMOVE FIRST (dato) – solo la primera aparicion
    // =========================================================================

    @Test
    public void removeFirst_elimina_solo_primera_aparicion() {
        Vector<String> v = new Vector<>(4, null);
        v.agregar(1, "X");
        v.agregar(2, "X");
        v.removeFirst("X");
        // La segunda sigue presente
        assertTrue(v.contains("X"));
        // La primera fue eliminada (null = dato inicial)
        assertNull(v.obtener(1));
    }

    @Test
    public void removeFirst_dato_null_lanza_excepcion() {
        assertThrows(RuntimeException.class, () -> vectorInt.removeFirst(null));
    }

    // =========================================================================
    // CONTAINS
    // =========================================================================

    @Test
    public void contains_retorna_true_si_dato_presente() {
        vectorInt.agregar(1, 42);
        assertTrue(vectorInt.contains(42));
    }

    @Test
    public void contains_retorna_false_si_dato_ausente() {
        assertFalse(vectorInt.contains(999));
    }

    @Test
    public void contains_dato_null_lanza_excepcion() {
        assertThrows(RuntimeException.class, () -> vectorInt.contains(null));
    }

    // =========================================================================
    // GET CANTIDAD DE DATOS
    // =========================================================================

    @Test
    public void getCantidadDeDatos_vector_vacio_retorna_cero() {
        assertEquals(0, vectorInt.getCantidadDeDatos());
    }

    @Test
    public void getCantidadDeDatos_cuenta_solo_distintos_del_inicial() {
        vectorInt.agregar(1, 10);
        vectorInt.agregar(3, 30);
        assertEquals(2, vectorInt.getCantidadDeDatos());
    }

    @Test
    public void getCantidadDeDatos_despues_de_remover_decrementa() throws Exception {
        vectorInt.agregar(1, 10);
        vectorInt.agregar(2, 20);
        vectorInt.remover(1);
        assertEquals(1, vectorInt.getCantidadDeDatos());
    }

    // =========================================================================
    // GET LONGITUD
    // =========================================================================

    @Test
    public void getLongitud_retorna_longitud_inicial() {
        assertEquals(5, vectorInt.getLongitud());
    }

    @Test
    public void getLongitud_crece_al_redimensionar() {
        Vector<Integer> v = new Vector<>(2, 0);
        v.agregar(1, 1);
        v.agregar(2, 2);
        v.agregar(3); // fuerza redimensionado
        assertTrue(v.getLongitud() > 2);
    }

    // =========================================================================
    // EQUALS
    // =========================================================================

    @Test
    public void equals_vectores_iguales_retorna_true() {
        Vector<Integer> v1 = new Vector<>(3, 0);
        Vector<Integer> v2 = new Vector<>(3, 0);
        v1.agregar(1, 5); v2.agregar(1, 5);
        assertEquals(v1, v2);
    }

    @Test
    public void equals_vectores_distintos_retorna_false() {
        Vector<Integer> v1 = new Vector<>(3, 0);
        Vector<Integer> v2 = new Vector<>(3, 0);
        v1.agregar(1, 5);
        v2.agregar(1, 9);
        assertNotEquals(v1, v2);
    }

    @Test
    public void equals_distinta_longitud_retorna_false() {
        Vector<Integer> v1 = new Vector<>(3, 0);
        Vector<Integer> v2 = new Vector<>(4, 0);
        assertNotEquals(v1, v2);
    }

    @Test
    public void equals_mismo_objeto_retorna_true() {
        assertEquals(vectorInt, vectorInt);
    }

    @Test
    public void equals_null_retorna_false() {
        assertNotEquals(null, vectorInt);
    }

    // =========================================================================
    // HASHCODE
    // =========================================================================

    @Test
    public void hashCode_vectores_iguales_tienen_mismo_hash() {
        Vector<Integer> v1 = new Vector<>(3, 0);
        Vector<Integer> v2 = new Vector<>(3, 0);
        v1.agregar(2, 7); v2.agregar(2, 7);
        assertEquals(v1.hashCode(), v2.hashCode());
    }

    // =========================================================================
    // TO STRING
    // =========================================================================

    @Test
    public void toString_formato_correcto() {
        Vector<Integer> v = new Vector<>(3, 0);
        v.agregar(1, 1);
        v.agregar(2, 2);
        v.agregar(3, 3);
        assertEquals("[1, 2, 3]", v.toString());
    }

    @Test
    public void toString_vector_con_dato_inicial() {
        Vector<Integer> v = new Vector<>(2, 0);
        assertEquals("[0, 0]", v.toString());
    }

    // =========================================================================
    // ITERATOR
    // =========================================================================

    @Test
    public void iterator_recorre_todos_los_elementos() {
        Vector<Integer> v = new Vector<>(3, 0);
        v.agregar(1, 10);
        v.agregar(2, 20);
        v.agregar(3, 30);

        int[] esperados = {10, 20, 30};
        int idx = 0;
        for (int dato : v) {
            assertEquals(esperados[idx++], dato);
        }
        assertEquals(3, idx);
    }

    @Test
    public void iterator_hasNext_false_al_final() {
        Vector<Integer> v = new Vector<>(1, 0);
        Iterator<Integer> it = v.iterator();
        it.next();
        assertFalse(it.hasNext());
    }

    @Test
    public void iterator_next_sin_elementos_lanza_NoSuchElementException() {
        Vector<Integer> v = new Vector<>(1, 0);
        Iterator<Integer> it = v.iterator();
        it.next(); // consume el único elemento
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    public void iterator_remove_lanza_UnsupportedOperationException() {
        Iterator<Integer> it = vectorInt.iterator();
        assertThrows(UnsupportedOperationException.class, it::remove);
    }
}
