package estructuras.arboles;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestNodoDeArbol {

    @Test
    void constructor_debeAsignarValorCorrectamente() {
        NodoDeArbol<Integer> nodo = new NodoDeArbol<>(10);
        assertEquals(10, nodo.getValor());
    }

    @Test
    void constructor_izquierdoDebeSerNull() {
        NodoDeArbol<Integer> nodo = new NodoDeArbol<>(10);
        assertNull(nodo.getIzquierdo());
    }

    @Test
    void constructor_derechoDebeSerNull() {
        NodoDeArbol<Integer> nodo = new NodoDeArbol<>(10);
        assertNull(nodo.getDerecho());
    }

    @Test
    void constructor_conValorString_debeAsignarCorrectamente() {
        NodoDeArbol<String> nodo = new NodoDeArbol<>("hola");
        assertEquals("hola", nodo.getValor());
    }

    @Test
    void constructor_conValorNull_debePermitirlo() {
        NodoDeArbol<Integer> nodo = new NodoDeArbol<>(null);
        assertNull(nodo.getValor());
    }

    @Test
    void setValor_debeActualizarElValor() {
        NodoDeArbol<Integer> nodo = new NodoDeArbol<>(1);
        nodo.setValor(99);
        assertEquals(99, nodo.getValor());
    }

    @Test
    void setIzquierdo_debeAsignarHijoIzquierdo() {
        NodoDeArbol<Integer> padre = new NodoDeArbol<>(10);
        NodoDeArbol<Integer> hijo  = new NodoDeArbol<>(5);
        padre.setIzquierdo(hijo);
        assertSame(hijo, padre.getIzquierdo());
    }

    @Test
    void setDerecho_debeAsignarHijoDerecho() {
        NodoDeArbol<Integer> padre = new NodoDeArbol<>(10);
        NodoDeArbol<Integer> hijo  = new NodoDeArbol<>(15);
        padre.setDerecho(hijo);
        assertSame(hijo, padre.getDerecho());
    }

    @Test
    void setIzquierdo_conNull_debeDesvinculiarHijo() {
        NodoDeArbol<Integer> padre = new NodoDeArbol<>(10);
        padre.setIzquierdo(new NodoDeArbol<>(5));
        padre.setIzquierdo(null);
        assertNull(padre.getIzquierdo());
    }

    @Test
    void setDerecho_conNull_debeDesvincularHijo() {
        NodoDeArbol<Integer> padre = new NodoDeArbol<>(10);
        padre.setDerecho(new NodoDeArbol<>(15));
        padre.setDerecho(null);
        assertNull(padre.getDerecho());
    }

    @Test
    void tieneHijos_sinHijos_debeRetornarFalse() {
        NodoDeArbol<Integer> nodo = new NodoDeArbol<>(10);
        assertFalse(nodo.tieneHijos());
    }

    @Test
    void tieneHijos_soloConHijoIzquierdo_debeRetornarTrue() {
        NodoDeArbol<Integer> nodo = new NodoDeArbol<>(10);
        nodo.setIzquierdo(new NodoDeArbol<>(5));
        assertTrue(nodo.tieneHijos());
    }

    @Test
    void tieneHijos_soloConHijoDerecho_debeRetornarTrue() {
        NodoDeArbol<Integer> nodo = new NodoDeArbol<>(10);
        nodo.setDerecho(new NodoDeArbol<>(15));
        assertTrue(nodo.tieneHijos());
    }

    @Test
    void tieneHijos_conAmbosHijos_debeRetornarTrue() {
        NodoDeArbol<Integer> nodo = new NodoDeArbol<>(10);
        nodo.setIzquierdo(new NodoDeArbol<>(5));
        nodo.setDerecho(new NodoDeArbol<>(15));
        assertTrue(nodo.tieneHijos());
    }

    @Test
    void tieneUnHijo_sinHijos_debeRetornarFalse() {
        NodoDeArbol<Integer> nodo = new NodoDeArbol<>(10);
        assertFalse(nodo.tieneUnHijo());
    }

    @Test
    void tieneUnHijo_soloConHijoIzquierdo_debeRetornarTrue() {
        NodoDeArbol<Integer> nodo = new NodoDeArbol<>(10);
        nodo.setIzquierdo(new NodoDeArbol<>(5));
        assertTrue(nodo.tieneUnHijo());
    }

    @Test
    void tieneUnHijo_soloConHijoDerecho_debeRetornarTrue() {
        NodoDeArbol<Integer> nodo = new NodoDeArbol<>(10);
        nodo.setDerecho(new NodoDeArbol<>(15));
        assertTrue(nodo.tieneUnHijo());
    }

    @Test
    void tieneUnHijo_conAmbosHijos_debeRetornarFalse() {
        NodoDeArbol<Integer> nodo = new NodoDeArbol<>(10);
        nodo.setIzquierdo(new NodoDeArbol<>(5));
        nodo.setDerecho(new NodoDeArbol<>(15));
        assertFalse(nodo.tieneUnHijo());
    }

    @Test
    void consistencia_sinHijos_ambosFalse() {
        NodoDeArbol<Integer> nodo = new NodoDeArbol<>(10);
        assertFalse(nodo.tieneHijos());
        assertFalse(nodo.tieneUnHijo());
    }

    @Test
    void consistencia_ambosHijos_tieneHijosTrueYtieneUnHijoFalse() {
        NodoDeArbol<Integer> nodo = new NodoDeArbol<>(10);
        nodo.setIzquierdo(new NodoDeArbol<>(5));
        nodo.setDerecho(new NodoDeArbol<>(15));
        assertTrue(nodo.tieneHijos());
        assertFalse(nodo.tieneUnHijo());
    }
}
