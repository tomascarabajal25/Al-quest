package estructuras.grafos;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestArista {

    // ─────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────

    @Test
    void constructor_debeAsignarPesoCorrectamente() {
        Vertice<String, Integer> destino = new Vertice<>("B");
        Arista<String, Integer> arista = new Arista<>(5, destino);
        assertEquals(5, arista.getPeso());
    }

    @Test
    void constructor_debeAsignarDestinoCorrectamente() {
        Vertice<String, Integer> destino = new Vertice<>("B");
        Arista<String, Integer> arista = new Arista<>(5, destino);
        assertSame(destino, arista.getDestino());
    }

    @Test
    void constructor_conPesoDouble_debeAsignarCorrectamente() {
        Vertice<String, Double> destino = new Vertice<>("C");
        Arista<String, Double> arista = new Arista<>(3.14, destino);
        assertEquals(3.14, arista.getPeso());
    }

    @Test
    void constructor_conPesoCero_debePermitirlo() {
        Vertice<String, Integer> destino = new Vertice<>("B");
        Arista<String, Integer> arista = new Arista<>(0, destino);
        assertEquals(0, arista.getPeso());
    }

    @Test
    void constructor_conPesoNegativo_debePermitirlo() {
        // Arista no valida el peso; eso es responsabilidad del grafo
        Vertice<String, Integer> destino = new Vertice<>("B");
        Arista<String, Integer> arista = new Arista<>(-10, destino);
        assertEquals(-10, arista.getPeso());
    }

    @Test
    void constructor_conPesoNull_debePermitirlo() {
        Vertice<String, Integer> destino = new Vertice<>("B");
        Arista<String, Integer> arista = new Arista<>(null, destino);
        assertNull(arista.getPeso());
    }

    // ─────────────────────────────────────────────
    // getPeso
    // ─────────────────────────────────────────────

    @Test
    void getPeso_conTipoString_debeRetornarCorrectamente() {
        Vertice<Integer, String> destino = new Vertice<>(1);
        Arista<Integer, String> arista = new Arista<>("alto", destino);
        assertEquals("alto", arista.getPeso());
    }

    // ─────────────────────────────────────────────
    // getDestino
    // ─────────────────────────────────────────────

    @Test
    void getDestino_debeRetornarElMismoObjeto() {
        Vertice<String, Integer> destino = new Vertice<>("X");
        Arista<String, Integer> arista = new Arista<>(1, destino);
        assertSame(destino, arista.getDestino());
    }

    @Test
    void getDestino_valorDelDestinoDebeSerAccesible() {
        Vertice<String, Integer> destino = new Vertice<>("Z");
        Arista<String, Integer> arista = new Arista<>(7, destino);
        assertEquals("Z", arista.getDestino().getValor());
    }

    // ─────────────────────────────────────────────
    // toString
    // ─────────────────────────────────────────────

    @Test
    void toString_debeContenerElValorDelDestino() {
        Vertice<String, Integer> destino = new Vertice<>("B");
        Arista<String, Integer> arista = new Arista<>(5, destino);
        assertTrue(arista.toString().contains("B"));
    }

    @Test
    void toString_debeContenerElPeso() {
        Vertice<String, Integer> destino = new Vertice<>("B");
        Arista<String, Integer> arista = new Arista<>(5, destino);
        assertTrue(arista.toString().contains("5"));
    }

    @Test
    void toString_formatoCorrecto() {
        Vertice<String, Integer> destino = new Vertice<>("B");
        Arista<String, Integer> arista = new Arista<>(5, destino);
        assertEquals("-> [B](5)", arista.toString());
    }

    @Test
    void toString_conPesoDouble_formatoCorrecto() {
        Vertice<String, Double> destino = new Vertice<>("C");
        Arista<String, Double> arista = new Arista<>(2.5, destino);
        assertEquals("-> [C](2.5)", arista.toString());
    }
}
