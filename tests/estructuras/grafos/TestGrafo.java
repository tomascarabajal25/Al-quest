package estructuras.grafos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

class TestGrafo {

    private Grafo<String, Integer> grafo;

    @BeforeEach
    void setUp() {
        grafo = new Grafo<>();
    }

    @Test
    void constructor_getVerticesDebeEstarVacio() {
        assertTrue(grafo.getVertices().isEmpty());
    }

    @Test
    void agregarVertice_debeAgregarloCorrectamente() {
        grafo.agregarVertice("A");
        assertTrue(grafo.existeVertice("A"));
    }

    @Test
    void agregarVertice_duplicado_noDebeAgregarDosVeces() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("A");
        assertEquals(1, grafo.getVertices().size());
    }

    @Test
    void existeVertice_conVerticeInexistente_debeRetornarFalse() {
        assertFalse(grafo.existeVertice("Z"));
    }

    @Test
    void getVertice_conVerticeExistente_debeRetornarloCorrectamente() {
        grafo.agregarVertice("A");
        assertNotNull(grafo.getVertice("A"));
        assertEquals("A", grafo.getVertice("A").getValor());
    }

    @Test
    void getVertice_conVerticeInexistente_debeLanzarExcepcion() {
        assertThrows(NoSuchElementException.class, () -> grafo.getVertice("Z"));
    }

    @Test
    void agregarArista_debeCrearAdyacenciaCorrectamente() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 5);

        List<Arista<String, Integer>> adyacentes = grafo.getAdyacentes("A");
        assertEquals(1, adyacentes.size());
        assertEquals("B", adyacentes.get(0).getDestino().getValor());
        assertEquals(5, adyacentes.get(0).getPeso());
    }

    @Test
    void agregarArista_esDirigida_noDebeCrearArcoEnSentidoContrario() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 5);

        assertTrue(grafo.getAdyacentes("B").isEmpty());
    }

    @Test
    void getAdyacentes_sinAristas_debeRetornarListaVacia() {
        grafo.agregarVertice("A");
        assertTrue(grafo.getAdyacentes("A").isEmpty());
    }

    @Test
    void build_debeCrearVerticesCorrectamente() {
        Grafo<String, Integer> g = Grafo.build(
            "{\"A\", \"B\", \"C\"}",
            "{}"
        );
        assertTrue(g.existeVertice("A"));
        assertTrue(g.existeVertice("B"));
        assertTrue(g.existeVertice("C"));
    }

    @Test
    void build_debeCrearAristasCorrectamente() {
        Grafo<String, Integer> g = Grafo.build(
            "{\"A\", \"B\"}",
            "{(\"A\",\"B\",10)}"
        );
        List<Arista<String, Integer>> ady = g.getAdyacentes("A");
        assertEquals(1, ady.size());
        assertEquals("B", ady.get(0).getDestino().getValor());
        assertEquals(10, ady.get(0).getPeso());
    }

    @Test
    void build_conVerticesVacios_debeCrearSoloLosDeAristas() {
        Grafo<String, Integer> g = Grafo.build(
            "{}",
            "{(\"X\",\"Y\",3)}"
        );
        assertTrue(g.existeVertice("X"));
        assertTrue(g.existeVertice("Y"));
    }

    @Test
    void recorridoAnchura_grafoLineal_debeVisitarEnOrden() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "C", 1);

        List<String> resultado = grafo.recorridoAnchura("A");
        assertEquals(List.of("A", "B", "C"), resultado);
    }

    @Test
    void recorridoAnchura_nodoAislado_debeRetornarSoloEseNodo() {
        grafo.agregarVertice("A");
        List<String> resultado = grafo.recorridoAnchura("A");
        assertEquals(List.of("A"), resultado);
    }

    @Test
    void recorridoAnchura_debeVisitarTodosLosNodosAlcanzables() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("A", "C", 1);

        List<String> resultado = grafo.recorridoAnchura("A");
        assertEquals(3, resultado.size());
        assertTrue(resultado.contains("A"));
        assertTrue(resultado.contains("B"));
        assertTrue(resultado.contains("C"));
    }

    @Test
    void recorridoProfundidad_grafoLineal_debeVisitarEnOrden() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "C", 1);

        List<String> resultado = grafo.recorridoProfundidad("A");
        assertEquals(List.of("A", "B", "C"), resultado);
    }

    @Test
    void recorridoProfundidad_nodoAislado_debeRetornarSoloEseNodo() {
        grafo.agregarVertice("A");
        List<String> resultado = grafo.recorridoProfundidad("A");
        assertEquals(List.of("A"), resultado);
    }

    @Test
    void recorridoProfundidad_debeVisitarCadaNodoUnaVez() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("A", "C", 1);
        grafo.agregarArista("B", "C", 1);

        List<String> resultado = grafo.recorridoProfundidad("A");
        assertEquals(3, resultado.size());
        // Sin repetidos
        assertEquals(resultado.size(), resultado.stream().distinct().count());
    }

    @Test
    void tieneCiclo_grafoSinCiclo_debeRetornarFalse() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "C", 1);

        assertFalse(grafo.tieneCiclo());
    }

    @Test
    void tieneCiclo_grafoConCiclo_debeRetornarTrue() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "C", 1);
        grafo.agregarArista("C", "A", 1); // ciclo

        assertTrue(grafo.tieneCiclo());
    }

    @Test
    void tieneCiclo_autolazo_debeRetornarTrue() {
        grafo.agregarVertice("A");
        grafo.agregarArista("A", "A", 1);

        assertTrue(grafo.tieneCiclo());
    }

    @Test
    void tieneCiclo_grafoVacio_debeRetornarFalse() {
        assertFalse(grafo.tieneCiclo());
    }


    @Test
    void caminoMinimoBFS_caminoExistente_debeRetornarCaminoCorrecto() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "C", 1);

        List<String> camino = grafo.caminoMinimoBFS("A", "C");
        assertEquals(List.of("A", "B", "C"), camino);
    }

    @Test
    void caminoMinimoBFS_sinCamino_debeRetornarListaVacia() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");

        List<String> camino = grafo.caminoMinimoBFS("A", "B");
        assertTrue(camino.isEmpty());
    }

    @Test
    void caminoMinimoBFS_mismoNodo_debeRetornarListaConUnElemento() {
        grafo.agregarVertice("A");

        List<String> camino = grafo.caminoMinimoBFS("A", "A");
        assertEquals(List.of("A"), camino);
    }

    @Test
    void caminoMinimoBFS_eligeCaminoMasCortoEnAristas() {
        // A->B->C (2 aristas) vs A->C (1 arista)
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 100);
        grafo.agregarArista("B", "C", 100);
        grafo.agregarArista("A", "C", 1);

        List<String> camino = grafo.caminoMinimoBFS("A", "C");
        assertEquals(List.of("A", "C"), camino);
    }

    @Test
    void dijkstra_caminoSimple_debeRetornarCaminoCorrecto() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "C", 2);

        List<String> camino = grafo.dijkstra("A", "C");
        assertEquals(List.of("A", "B", "C"), camino);
    }

    @Test
    void dijkstra_eligeCaminoPorPeso_noSoloPorAristas() {
        // A->B->C (costo 2) vs A->C (costo 10)
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "C", 1);
        grafo.agregarArista("A", "C", 10);

        List<String> camino = grafo.dijkstra("A", "C");
        assertEquals(List.of("A", "B", "C"), camino);
    }

    @Test
    void dijkstra_sinCamino_debeRetornarListaVacia() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");

        List<String> camino = grafo.dijkstra("A", "B");
        assertTrue(camino.isEmpty());
    }

    @Test
    void dijkstra_conPesoNegativo_debeLanzarExcepcion() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", -5);

        assertThrows(IllegalArgumentException.class, () -> grafo.dijkstra("A", "B"));
    }

    @Test
    void recorridoTopologicoDFS_grafoDag_cadaNodoAntesQueSusSucesores() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "C", 1);

        List<String> orden = grafo.recorridoTopologicoDFS();
        assertTrue(orden.indexOf("A") < orden.indexOf("B"));
        assertTrue(orden.indexOf("B") < orden.indexOf("C"));
    }

    @Test
    void recorridoTopologicoDFS_conCiclo_debeLanzarExcepcion() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "A", 1);

        assertThrows(IllegalStateException.class, () -> grafo.recorridoTopologicoDFS());
    }

    @Test
    void recorridoTopologicoBFS_grafoDag_cadaNodoAntesQueSusSucesores() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "C", 1);

        List<String> orden = grafo.recorridoTopologicoBFS();
        assertTrue(orden.indexOf("A") < orden.indexOf("B"));
        assertTrue(orden.indexOf("B") < orden.indexOf("C"));
    }

    @Test
    void recorridoTopologicoBFS_conCiclo_debeLanzarExcepcion() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "A", 1);

        assertThrows(IllegalStateException.class, () -> grafo.recorridoTopologicoBFS());
    }


    @Test
    void getPuntosArticulacion_grafoPuente_debeDetectarPuntoMedio() {
        // A - B - C (B es punto de articulación)
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        // No dirigido: aristas en ambos sentidos
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "A", 1);
        grafo.agregarArista("B", "C", 1);
        grafo.agregarArista("C", "B", 1);

        Set<String> pa = grafo.getPuntosArticulacion();
        assertTrue(pa.contains("B"));
    }

    @Test
    void getPuntosArticulacion_grafoCiclo_noDebeHaberPuntosArticulacion() {
        // Triángulo: A-B-C-A (sin puntos de articulación)
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1); grafo.agregarArista("B", "A", 1);
        grafo.agregarArista("B", "C", 1); grafo.agregarArista("C", "B", 1);
        grafo.agregarArista("C", "A", 1); grafo.agregarArista("A", "C", 1);

        Set<String> pa = grafo.getPuntosArticulacion();
        assertTrue(pa.isEmpty());
    }

    @Test
    void getComponentesFuertementeConexas_cadaNodoEsSuPropiaSCC() {
        // Grafo sin ciclos: cada nodo es su propia SCC
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "C", 1);

        List<List<String>> sccs = grafo.getComponentesFuertementeConexas();
        assertEquals(3, sccs.size());
    }

    @Test
    void getComponentesFuertementeConexas_cicloCompleto_esUnaSolaSCC() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "C", 1);
        grafo.agregarArista("C", "A", 1);

        List<List<String>> sccs = grafo.getComponentesFuertementeConexas();
        assertEquals(1, sccs.size());
        assertEquals(3, sccs.get(0).size());
    }

    @Test
    void getComponentesFuertementeConexas_grafoDosComponentes() {
        // A<->B  y  C<->D  (dos SCCs separadas)
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarVertice("D");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "A", 1);
        grafo.agregarArista("C", "D", 1);
        grafo.agregarArista("D", "C", 1);

        List<List<String>> sccs = grafo.getComponentesFuertementeConexas();
        assertEquals(2, sccs.size());
    }
}
