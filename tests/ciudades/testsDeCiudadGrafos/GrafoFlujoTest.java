package ciudades.testsDeCiudadGrafos;

import juego.ciudades.grafos.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class GrafoFlujoTest {

    private GrafoFlujo grafo;

    @BeforeEach
    public void setUp() {
        grafo = new GrafoFlujo();
    }

    @Test
    public void testAgregarVertice() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        assertEquals(2, grafo.getVertices().size());
        assertTrue(grafo.getGrafo().existeVertice("A"));
        assertTrue(grafo.getGrafo().existeVertice("B"));
    }

    @Test
    public void testAgregarVerticeDuplicadoNoDuplica() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("A");
        assertEquals(1, grafo.getVertices().size());
    }

    @Test
    public void testAgregarVerticeVacioNoHaceNada() {
        grafo.agregarVertice("");
        grafo.agregarVertice(null);
        assertEquals(0, grafo.getVertices().size());
    }

    @Test
    public void testAgregarArista() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 10);
        assertFalse(grafo.getGrafo().getAdyacentes("A").isEmpty());
    }

    @Test
    public void testAgregarAristaConCapacidadInvalidaNoAgrega() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 0);
        grafo.agregarArista("A", "B", -5);
        assertTrue(grafo.getGrafo().getAdyacentes("A").isEmpty());
    }

    @Test
    public void testAgregarAristaConVerticeInexistenteNoAgrega() {
        grafo.agregarVertice("A");
        grafo.agregarArista("A", "Z", 10);
        assertTrue(grafo.getGrafo().getAdyacentes("A").isEmpty());
    }

    @Test
    public void testSetFuenteYSumidero() {
        grafo.agregarVertice("S");
        grafo.agregarVertice("T");
        grafo.setFuente("S");
        grafo.setSumidero("T");
        assertEquals("S", grafo.getFuente());
        assertEquals("T", grafo.getSumidero());
    }

    @Test
    public void testSetFuenteInexistenteNoSetea() {
        grafo.setFuente("X");
        assertNull(grafo.getFuente());
    }

    @Test
    public void testEstaCompleto() {
        assertFalse(grafo.estaCompleto());
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.setFuente("A");
        grafo.setSumidero("B");
        assertTrue(grafo.estaCompleto());
    }

    @Test
    public void testEstaCompletoFalsoSiFuenteIgualSumidero() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.setFuente("A");
        grafo.setSumidero("A");
        assertFalse(grafo.estaCompleto());
    }

    @Test
    public void testMatrizCapacidades() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 10);
        grafo.agregarArista("B", "C", 5);

        int[][] matriz = grafo.getMatrizCapacidades();
        assertEquals(3, matriz.length);
        assertEquals(10, matriz[0][1]);
        assertEquals(5, matriz[1][2]);
        assertEquals(0, matriz[0][2]);
    }

    @Test
    public void testReset() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.setFuente("A");
        grafo.setSumidero("B");
        grafo.reset();

        assertEquals(0, grafo.getVertices().size());
        assertNull(grafo.getFuente());
        assertNull(grafo.getSumidero());
        assertEquals(EstadoCiudad7.CONSTRUYENDO, grafo.getEstado());
    }

    @Test
    public void testResolverFlujoMaximoSimple() {
        grafo.agregarVertice("S");
        grafo.agregarVertice("T");
        grafo.agregarArista("S", "T", 10);
        grafo.setFuente("S");
        grafo.setSumidero("T");

        ResultadoFlujo resultado = grafo.resolverFlujoMaximo();
        assertNotNull(resultado);
        assertEquals(10, resultado.getFlujoMaximo());
        assertEquals(1, resultado.getPasos().size());
    }

    @Test
    public void testResolverFlujoMaximoDosCaminos() {
        grafo.agregarVertice("S");
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("T");
        grafo.agregarArista("S", "A", 10);
        grafo.agregarArista("S", "B", 10);
        grafo.agregarArista("A", "T", 10);
        grafo.agregarArista("B", "T", 10);
        grafo.setFuente("S");
        grafo.setSumidero("T");

        ResultadoFlujo resultado = grafo.resolverFlujoMaximo();
        assertNotNull(resultado);
        assertEquals(20, resultado.getFlujoMaximo());
        assertEquals(2, resultado.getPasos().size());
    }

    @Test
    public void testResolverFlujoMaximoConCuelloDeBotella() {
        grafo.agregarVertice("S");
        grafo.agregarVertice("A");
        grafo.agregarVertice("T");
        grafo.agregarArista("S", "A", 100);
        grafo.agregarArista("A", "T", 5);
        grafo.setFuente("S");
        grafo.setSumidero("T");

        ResultadoFlujo resultado = grafo.resolverFlujoMaximo();
        assertNotNull(resultado);
        assertEquals(5, resultado.getFlujoMaximo());
    }

    @Test
    public void testResolverFlujoMaximoSinCamino() {
        grafo.agregarVertice("S");
        grafo.agregarVertice("T");
        grafo.setFuente("S");
        grafo.setSumidero("T");

        ResultadoFlujo resultado = grafo.resolverFlujoMaximo();
        assertNotNull(resultado);
        assertEquals(0, resultado.getFlujoMaximo());
        assertTrue(resultado.getPasos().isEmpty());
    }

    @Test
    public void testResolverFlujoMaximoGrafoComplejo() {
        grafo.agregarVertice("S");
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarVertice("D");
        grafo.agregarVertice("T");
        grafo.agregarArista("S", "A", 16);
        grafo.agregarArista("S", "B", 13);
        grafo.agregarArista("A", "C", 12);
        grafo.agregarArista("B", "A", 4);
        grafo.agregarArista("B", "D", 14);
        grafo.agregarArista("C", "B", 9);
        grafo.agregarArista("C", "T", 20);
        grafo.agregarArista("D", "C", 7);
        grafo.agregarArista("D", "T", 4);
        grafo.setFuente("S");
        grafo.setSumidero("T");

        ResultadoFlujo resultado = grafo.resolverFlujoMaximo();
        assertNotNull(resultado);
        assertEquals(23, resultado.getFlujoMaximo());
    }

    @Test
    public void testResolverCaminoMinimoSimple() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarArista("A", "B", 5);

        ResultadoCamino resultado = grafo.resolverCaminoMinimo("A", "B");
        assertNotNull(resultado);
        assertEquals(5.0, resultado.getCostoTotal());
        assertEquals(List.of("A", "B"), resultado.getCamino());
    }

    @Test
    public void testResolverCaminoMinimoConEscala() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 3);
        grafo.agregarArista("B", "C", 4);
        grafo.agregarArista("A", "C", 10);

        ResultadoCamino resultado = grafo.resolverCaminoMinimo("A", "C");
        assertNotNull(resultado);
        assertEquals(7.0, resultado.getCostoTotal());
        assertEquals(List.of("A", "B", "C"), resultado.getCamino());
    }

    @Test
    public void testResolverCaminoMinimoSinCamino() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");

        ResultadoCamino resultado = grafo.resolverCaminoMinimo("A", "B");
        assertNotNull(resultado);
        assertTrue(resultado.getCamino().isEmpty());
    }

    @Test
    public void testResolverCaminoMinimoVerticeInexistente() {
        grafo.agregarVertice("A");
        ResultadoCamino resultado = grafo.resolverCaminoMinimo("A", "Z");
        assertNull(resultado);
    }

    @Test
    public void testResolverCaminoMinimoRegistraPasos() {
        grafo.agregarVertice("A");
        grafo.agregarVertice("B");
        grafo.agregarVertice("C");
        grafo.agregarArista("A", "B", 1);
        grafo.agregarArista("B", "C", 1);

        ResultadoCamino resultado = grafo.resolverCaminoMinimo("A", "C");
        assertNotNull(resultado);
        assertFalse(resultado.getPasos().isEmpty());
        assertTrue(resultado.getPasos().size() >= 3);
    }
}
