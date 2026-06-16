package estructuras.grafos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

class TestVertice {

    @Test
    void constructor_debeAsignarValorCorrectamente() {
        Vertice<String, Integer> v = new Vertice<>("A");
        assertEquals("A", v.getValor());
    }

    @Test
    void constructor_adyacenciasDebeEstarVacia() {
        Vertice<String, Integer> v = new Vertice<>("A");
        assertTrue(v.getAdyacencias().isEmpty());
    }

    @Test
    void constructor_conValorInteger_debeAsignarCorrectamente() {
        Vertice<Integer, Double> v = new Vertice<>(42);
        assertEquals(42, v.getValor());
    }

    @Test
    void constructor_conValorNull_debePermitirlo() {
        Vertice<String, Integer> v = new Vertice<>(null);
        assertNull(v.getValor());
    }

    @Test
    void agregarArista_debeAumentarTamañoDeAdyacencias() {
        Vertice<String, Integer> origen = new Vertice<>("A");
        Vertice<String, Integer> destino = new Vertice<>("B");
        origen.agregarArista(destino, 5);
        assertEquals(1, origen.getAdyacencias().size());
    }

    @Test
    void agregarArista_debeAsignarDestinoCorrectamente() {
        Vertice<String, Integer> origen = new Vertice<>("A");
        Vertice<String, Integer> destino = new Vertice<>("B");
        origen.agregarArista(destino, 5);
        assertSame(destino, origen.getAdyacencias().get(0).getDestino());
    }

    @Test
    void agregarArista_debeAsignarPesoCorrectamente() {
        Vertice<String, Integer> origen = new Vertice<>("A");
        Vertice<String, Integer> destino = new Vertice<>("B");
        origen.agregarArista(destino, 99);
        assertEquals(99, origen.getAdyacencias().get(0).getPeso());
    }

    @Test
    void agregarArista_variasAristas_debeMantenerTodas() {
        Vertice<String, Integer> origen  = new Vertice<>("A");
        Vertice<String, Integer> destino1 = new Vertice<>("B");
        Vertice<String, Integer> destino2 = new Vertice<>("C");
        origen.agregarArista(destino1, 1);
        origen.agregarArista(destino2, 2);
        assertEquals(2, origen.getAdyacencias().size());
    }

    @Test
    void agregarArista_alMismoDestino_debeAgregarDuplucado() {
        // Vertice no impide aristas duplicadas; eso es responsabilidad del Grafo
        Vertice<String, Integer> origen  = new Vertice<>("A");
        Vertice<String, Integer> destino = new Vertice<>("B");
        origen.agregarArista(destino, 1);
        origen.agregarArista(destino, 1);
        assertEquals(2, origen.getAdyacencias().size());
    }

    @Test
    void agregarArista_noAfectaAlDestino() {
        Vertice<String, Integer> origen  = new Vertice<>("A");
        Vertice<String, Integer> destino = new Vertice<>("B");
        origen.agregarArista(destino, 5);
        assertTrue(destino.getAdyacencias().isEmpty());
    }

    @Test
    void equals_mismaReferencia_debeRetornarTrue() {
        Vertice<String, Integer> v = new Vertice<>("A");
        assertEquals(v, v);
    }

    @Test
    void equals_mismoValor_debeRetornarTrue() {
        Vertice<String, Integer> v1 = new Vertice<>("A");
        Vertice<String, Integer> v2 = new Vertice<>("A");
        assertEquals(v1, v2);
    }

    @Test
    void equals_distintoValor_debeRetornarFalse() {
        Vertice<String, Integer> v1 = new Vertice<>("A");
        Vertice<String, Integer> v2 = new Vertice<>("B");
        assertNotEquals(v1, v2);
    }

    @Test
    void equals_conNull_debeRetornarFalse() {
        Vertice<String, Integer> v = new Vertice<>("A");
        assertNotEquals(v, null);
    }

    @Test
    void equals_conOtroTipoDeObjeto_debeRetornarFalse() {
        Vertice<String, Integer> v = new Vertice<>("A");
        assertNotEquals(v, "A");
    }

    @Test
    void equals_mismoValorConDistintasAristas_debeRetornarTrue() {
        // La igualdad depende solo del valor, no de las aristas
        Vertice<String, Integer> v1 = new Vertice<>("A");
        Vertice<String, Integer> v2 = new Vertice<>("A");
        v1.agregarArista(new Vertice<>("B"), 10);
        assertEquals(v1, v2);
    }

    @Test
    void hashCode_mismoValor_debeTenerMismoHashCode() {
        Vertice<String, Integer> v1 = new Vertice<>("A");
        Vertice<String, Integer> v2 = new Vertice<>("A");
        assertEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    void hashCode_distintoValor_generalmenteDistintoHashCode() {
        Vertice<String, Integer> v1 = new Vertice<>("A");
        Vertice<String, Integer> v2 = new Vertice<>("B");
        assertNotEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    void hashCode_funcionaCorrectamenteEnHashSet() {
        Vertice<String, Integer> v1 = new Vertice<>("A");
        Vertice<String, Integer> v2 = new Vertice<>("A"); // igual a v1
        Vertice<String, Integer> v3 = new Vertice<>("B");

        Set<Vertice<String, Integer>> set = new HashSet<>();
        set.add(v1);
        set.add(v2); // no debe agregar, ya existe v1
        set.add(v3);

        assertEquals(2, set.size());
        assertTrue(set.contains(v1));
        assertTrue(set.contains(v3));
    }

    @Test
    void toString_debeContenerElValor() {
        Vertice<String, Integer> v = new Vertice<>("A");
        assertTrue(v.toString().contains("A"));
    }

    @Test
    void toString_formatoCorrecto() {
        Vertice<String, Integer> v = new Vertice<>("A");
        assertEquals("Vertice(A)", v.toString());
    }

    @Test
    void toString_conValorInteger_formatoCorrecto() {
        Vertice<Integer, Integer> v = new Vertice<>(42);
        assertEquals("Vertice(42)", v.toString());
    }
}
