package estructuras.conjunto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class TestConjuntoBasico {

    private ConjuntoBasico<Integer> conjunto;

    @BeforeEach
    void setUp() {
        conjunto = new ConjuntoBasico<>();
    }

    // ──────────────────────────────────────────────
    // esVacio() y tamaño()
    // ──────────────────────────────────────────────

    @Test
    void conjuntoNuevoEstaVacio() {
        assertTrue(conjunto.esVacio());
    }

    @Test
    void conjuntoNuevoTieneTamanoCero() {
        assertEquals(0, conjunto.tamaño());
    }

    @Test
    void despuesDeAgregarUnElementoNoEstaVacio() {
        conjunto.agregar(1);
        assertFalse(conjunto.esVacio());
    }

    @Test
    void tamanoCreceCadaVezQueSeAgregaUnElementoNuevo() {
        conjunto.agregar(10);
        assertEquals(1, conjunto.tamaño());
        conjunto.agregar(20);
        assertEquals(2, conjunto.tamaño());
        conjunto.agregar(30);
        assertEquals(3, conjunto.tamaño());
    }

    // ──────────────────────────────────────────────
    // agregar()
    // ──────────────────────────────────────────────

    @Test
    void agregarRetornaTrueCuandoElementoEsNuevo() {
        assertTrue(conjunto.agregar(5));
    }

    @Test
    void agregarRetornaFalseCuandoElementoYaExiste() {
        conjunto.agregar(5);
        assertFalse(conjunto.agregar(5));
    }

    @Test
    void agregarNoDuplicaElementos() {
        conjunto.agregar(7);
        conjunto.agregar(7);
        conjunto.agregar(7);
        assertEquals(1, conjunto.tamaño());
    }

    @Test
    void agregarVariosElementosDistintos() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        conjunto.agregar(3);
        assertEquals(3, conjunto.tamaño());
    }

    @Test
    void agregarNullEsAceptado() {
        assertTrue(conjunto.agregar(null));
        assertEquals(1, conjunto.tamaño());
    }

    @Test
    void agregarNullNoDuplica() {
        conjunto.agregar(null);
        assertFalse(conjunto.agregar(null));
        assertEquals(1, conjunto.tamaño());
    }

    // ──────────────────────────────────────────────
    // contiene()
    // ──────────────────────────────────────────────

    @Test
    void contieneRetornaFalseEnConjuntoVacio() {
        assertFalse(conjunto.contiene(1));
    }

    @Test
    void contieneRetornaTrueSiElementoFueAgregado() {
        conjunto.agregar(42);
        assertTrue(conjunto.contiene(42));
    }

    @Test
    void contieneRetornaFalseSiElementoNoEsta() {
        conjunto.agregar(1);
        assertFalse(conjunto.contiene(99));
    }

    @Test
    void contieneRetornaTrueParaNull() {
        conjunto.agregar(null);
        assertTrue(conjunto.contiene(null));
    }

    // ──────────────────────────────────────────────
    // quitar()
    // ──────────────────────────────────────────────

    @Test
    void quitarRetornaFalseEnConjuntoVacio() {
        assertFalse(conjunto.quitar(1));
    }

    @Test
    void quitarRetornaTrueSiElementoExiste() {
        conjunto.agregar(7);
        assertTrue(conjunto.quitar(7));
    }

    @Test
    void quitarEliminaElElemento() {
        conjunto.agregar(7);
        conjunto.quitar(7);
        assertFalse(conjunto.contiene(7));
        assertEquals(0, conjunto.tamaño());
    }

    @Test
    void quitarNoAfectaLosOtrosElementos() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        conjunto.agregar(3);
        conjunto.quitar(2);
        assertTrue(conjunto.contiene(1));
        assertFalse(conjunto.contiene(2));
        assertTrue(conjunto.contiene(3));
        assertEquals(2, conjunto.tamaño());
    }

    @Test
    void quitarMismoElementoDosVecesRetornaFalseEnSegundaLlamada() {
        conjunto.agregar(5);
        conjunto.quitar(5);
        assertFalse(conjunto.quitar(5));
    }

    // ──────────────────────────────────────────────
    // vaciar()
    // ──────────────────────────────────────────────

    @Test
    void vaciarDejaElConjuntoVacio() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        conjunto.agregar(3);
        conjunto.vaciar();
        assertTrue(conjunto.esVacio());
        assertEquals(0, conjunto.tamaño());
    }

    @Test
    void vaciarSobreConjuntoVacioNoLanzaExcepcion() {
        assertDoesNotThrow(() -> conjunto.vaciar());
    }

    @Test
    void despuesDeVaciarSePuedeVolvereAgregar() {
        conjunto.agregar(1);
        conjunto.vaciar();
        assertTrue(conjunto.agregar(1));
        assertEquals(1, conjunto.tamaño());
    }

    // ──────────────────────────────────────────────
    // elementos()
    // ──────────────────────────────────────────────

    @Test
    void elementosDeConjuntoVacioEsColeccionVacia() {
        assertTrue(conjunto.elementos().isEmpty());
    }

    @Test
    void elementosDevuelveUnaCopiaNoDependienteDelOriginal() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        Collection<Integer> copia = conjunto.elementos();
        conjunto.agregar(3);
        // la copia no debe verse afectada por la modificación posterior
        assertEquals(2, copia.size());
    }

    @Test
    void elementosContieneExactamenteLosElementosAgregados() {
        conjunto.agregar(10);
        conjunto.agregar(20);
        Collection<Integer> elems = conjunto.elementos();
        assertEquals(2, elems.size());
        assertTrue(elems.contains(10));
        assertTrue(elems.contains(20));
    }

    // ──────────────────────────────────────────────
    // union()
    // ──────────────────────────────────────────────

    @Test
    void unionConConjuntoVacioEsElMismo() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        ConjuntoBasico<Integer> vacio = new ConjuntoBasico<>();
        ConjuntoBasico<Integer> resultado = conjunto.union(vacio);
        assertEquals(2, resultado.tamaño());
        assertTrue(resultado.contiene(1));
        assertTrue(resultado.contiene(2));
    }

    @Test
    void unionDeConjuntoVacioConOtroEsElOtro() {
        ConjuntoBasico<Integer> otro = new ConjuntoBasico<>();
        otro.agregar(3);
        otro.agregar(4);
        ConjuntoBasico<Integer> resultado = conjunto.union(otro);
        assertEquals(2, resultado.tamaño());
        assertTrue(resultado.contiene(3));
        assertTrue(resultado.contiene(4));
    }

    @Test
    void unionSinElementosComunes() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        ConjuntoBasico<Integer> otro = new ConjuntoBasico<>();
        otro.agregar(3);
        otro.agregar(4);
        ConjuntoBasico<Integer> resultado = conjunto.union(otro);
        assertEquals(4, resultado.tamaño());
        assertTrue(resultado.contiene(1));
        assertTrue(resultado.contiene(2));
        assertTrue(resultado.contiene(3));
        assertTrue(resultado.contiene(4));
    }

    @Test
    void unionNoDuplicaElementosCompartidos() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        conjunto.agregar(3);
        ConjuntoBasico<Integer> otro = new ConjuntoBasico<>();
        otro.agregar(2);
        otro.agregar(3);
        otro.agregar(4);
        ConjuntoBasico<Integer> resultado = conjunto.union(otro);
        // {1,2,3} ∪ {2,3,4} = {1,2,3,4}
        assertEquals(4, resultado.tamaño());
    }

    @Test
    void unionNoModificaLosOperandos() {
        conjunto.agregar(1);
        ConjuntoBasico<Integer> otro = new ConjuntoBasico<>();
        otro.agregar(2);
        conjunto.union(otro);
        assertEquals(1, conjunto.tamaño());
        assertEquals(1, otro.tamaño());
    }

    @Test
    void unionEsConmutativaEnContenido() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        ConjuntoBasico<Integer> otro = new ConjuntoBasico<>();
        otro.agregar(2);
        otro.agregar(3);
        ConjuntoBasico<Integer> ab = conjunto.union(otro);
        ConjuntoBasico<Integer> ba = otro.union(conjunto);
        assertEquals(ab.tamaño(), ba.tamaño());
        for (Integer e : ab.elementos()) {
            assertTrue(ba.contiene(e));
        }
    }

    // ──────────────────────────────────────────────
    // interseccion()
    // ──────────────────────────────────────────────

    @Test
    void interseccionConConjuntoVacioEsVacia() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        ConjuntoBasico<Integer> vacio = new ConjuntoBasico<>();
        ConjuntoBasico<Integer> resultado = conjunto.interseccion(vacio);
        assertTrue(resultado.esVacio());
    }

    @Test
    void interseccionSinElementosComunesEsVacia() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        ConjuntoBasico<Integer> otro = new ConjuntoBasico<>();
        otro.agregar(3);
        otro.agregar(4);
        ConjuntoBasico<Integer> resultado = conjunto.interseccion(otro);
        assertTrue(resultado.esVacio());
    }

    @Test
    void interseccionConElementosComunes() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        conjunto.agregar(3);
        ConjuntoBasico<Integer> otro = new ConjuntoBasico<>();
        otro.agregar(2);
        otro.agregar(3);
        otro.agregar(4);
        ConjuntoBasico<Integer> resultado = conjunto.interseccion(otro);
        // {1,2,3} ∩ {2,3,4} = {2,3}
        assertEquals(2, resultado.tamaño());
        assertTrue(resultado.contiene(2));
        assertTrue(resultado.contiene(3));
    }

    @Test
    void interseccionConSiMismoEsIgual() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        ConjuntoBasico<Integer> resultado = conjunto.interseccion(conjunto);
        assertEquals(2, resultado.tamaño());
    }

    @Test
    void interseccionNoModificaLosOperandos() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        ConjuntoBasico<Integer> otro = new ConjuntoBasico<>();
        otro.agregar(2);
        conjunto.interseccion(otro);
        assertEquals(2, conjunto.tamaño());
        assertEquals(1, otro.tamaño());
    }

    @Test
    void interseccionEsConmutativaEnContenido() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        conjunto.agregar(3);
        ConjuntoBasico<Integer> otro = new ConjuntoBasico<>();
        otro.agregar(2);
        otro.agregar(3);
        otro.agregar(4);
        ConjuntoBasico<Integer> ab = conjunto.interseccion(otro);
        ConjuntoBasico<Integer> ba = otro.interseccion(conjunto);
        assertEquals(ab.tamaño(), ba.tamaño());
        for (Integer e : ab.elementos()) {
            assertTrue(ba.contiene(e));
        }
    }

    // ──────────────────────────────────────────────
    // diferencia()
    // ──────────────────────────────────────────────

    @Test
    void diferenciaConConjuntoVacioEsElMismo() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        ConjuntoBasico<Integer> vacio = new ConjuntoBasico<>();
        ConjuntoBasico<Integer> resultado = conjunto.diferencia(vacio);
        assertEquals(2, resultado.tamaño());
        assertTrue(resultado.contiene(1));
        assertTrue(resultado.contiene(2));
    }

    @Test
    void diferenciaConSiMismoEsVacia() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        ConjuntoBasico<Integer> resultado = conjunto.diferencia(conjunto);
        assertTrue(resultado.esVacio());
    }

    @Test
    void diferenciaSinElementosComunesEsElOriginal() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        ConjuntoBasico<Integer> otro = new ConjuntoBasico<>();
        otro.agregar(3);
        otro.agregar(4);
        ConjuntoBasico<Integer> resultado = conjunto.diferencia(otro);
        assertEquals(2, resultado.tamaño());
        assertTrue(resultado.contiene(1));
        assertTrue(resultado.contiene(2));
    }

    @Test
    void diferenciaEliminaLosElementosCompartidos() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        conjunto.agregar(3);
        ConjuntoBasico<Integer> otro = new ConjuntoBasico<>();
        otro.agregar(2);
        otro.agregar(3);
        otro.agregar(4);
        ConjuntoBasico<Integer> resultado = conjunto.diferencia(otro);
        // {1,2,3} - {2,3,4} = {1}
        assertEquals(1, resultado.tamaño());
        assertTrue(resultado.contiene(1));
        assertFalse(resultado.contiene(2));
        assertFalse(resultado.contiene(3));
    }

    @Test
    void diferenciaNoEsConmutativa() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        conjunto.agregar(3);
        ConjuntoBasico<Integer> otro = new ConjuntoBasico<>();
        otro.agregar(2);
        otro.agregar(3);
        otro.agregar(4);
        ConjuntoBasico<Integer> ab = conjunto.diferencia(otro); // {1}
        ConjuntoBasico<Integer> ba = otro.diferencia(conjunto); // {4}
        assertEquals(1, ab.tamaño());
        assertTrue(ab.contiene(1));
        assertEquals(1, ba.tamaño());
        assertTrue(ba.contiene(4));
    }

    @Test
    void diferenciaNoModificaLosOperandos() {
        conjunto.agregar(1);
        conjunto.agregar(2);
        ConjuntoBasico<Integer> otro = new ConjuntoBasico<>();
        otro.agregar(2);
        conjunto.diferencia(otro);
        assertEquals(2, conjunto.tamaño());
        assertEquals(1, otro.tamaño());
    }

    // ──────────────────────────────────────────────
    // Propiedades algebraicas combinadas
    // ──────────────────────────────────────────────

    @Test
    void unionConInterseccionCumpleAbsorcion() {
        // A ∪ (A ∩ B) = A
        conjunto.agregar(1);
        conjunto.agregar(2);
        conjunto.agregar(3);
        ConjuntoBasico<Integer> otro = new ConjuntoBasico<>();
        otro.agregar(2);
        otro.agregar(3);
        otro.agregar(4);
        ConjuntoBasico<Integer> interseccion = conjunto.interseccion(otro);
        ConjuntoBasico<Integer> resultado = conjunto.union(interseccion);
        assertEquals(conjunto.tamaño(), resultado.tamaño());
        for (Integer e : conjunto.elementos()) {
            assertTrue(resultado.contiene(e));
        }
    }

    @Test
    void diferenciaYUnionCumplenRelacionFundamental() {
        // |A ∪ B| = |A| + |B| - |A ∩ B|
        conjunto.agregar(1);
        conjunto.agregar(2);
        conjunto.agregar(3);
        ConjuntoBasico<Integer> otro = new ConjuntoBasico<>();
        otro.agregar(2);
        otro.agregar(3);
        otro.agregar(4);
        int union = conjunto.union(otro).tamaño();
        int interseccion = conjunto.interseccion(otro).tamaño();
        assertEquals(union, conjunto.tamaño() + otro.tamaño() - interseccion);
    }
}
