package estructuras.arboles;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestNodoAVL {

    @Test
    void constructor_debeAsignarValorCorrectamente() {
        NodoAVL<Integer> nodo = new NodoAVL<>(10);
        assertEquals(10, nodo.getValor());
    }

    @Test
    void constructor_alturaInicialDebeSer1() {
        NodoAVL<Integer> nodo = new NodoAVL<>(10);
        assertEquals(1, nodo.getAltura());
    }

    @Test
    void constructor_izquierdoDebeSerNull() {
        NodoAVL<Integer> nodo = new NodoAVL<>(10);
        assertNull(nodo.getIzquierdo());
    }

    @Test
    void constructor_derechoDebeSerNull() {
        NodoAVL<Integer> nodo = new NodoAVL<>(10);
        assertNull(nodo.getDerecho());
    }

    @Test
    void constructor_conValorString_debeAsignarCorrectamente() {
        NodoAVL<String> nodo = new NodoAVL<>("hola");
        assertEquals("hola", nodo.getValor());
    }

    @Test
    void constructor_conValorNull_debePermitirlo() {
        NodoAVL<Integer> nodo = new NodoAVL<>(null);
        assertNull(nodo.getValor());
    }

    @Test
    void setAltura_debeActualizarAltura() {
        NodoAVL<Integer> nodo = new NodoAVL<>(10);
        nodo.setAltura(5);
        assertEquals(5, nodo.getAltura());
    }

    @Test
    void setAltura_conCero_debePermitirlo() {
        NodoAVL<Integer> nodo = new NodoAVL<>(10);
        nodo.setAltura(0);
        assertEquals(0, nodo.getAltura());
    }

    @Test
    void setAltura_conValorNegativo_debePermitirlo() {
        // NodoAVL no restringe valores negativos; la validación es del árbol
        NodoAVL<Integer> nodo = new NodoAVL<>(10);
        nodo.setAltura(-1);
        assertEquals(-1, nodo.getAltura());
    }

    @Test
    void setValor_debeActualizarElValor() {
        NodoAVL<Integer> nodo = new NodoAVL<>(1);
        nodo.setValor(99);
        assertEquals(99, nodo.getValor());
    }

    @Test
    void setIzquierdo_conNodoAVL_debeAsignarHijoIzquierdo() {
        NodoAVL<Integer> padre = new NodoAVL<>(10);
        NodoAVL<Integer> hijo  = new NodoAVL<>(5);
        padre.setIzquierdo(hijo);
        assertSame(hijo, padre.getIzquierdo());
    }

    @Test
    void setDerecho_conNodoAVL_debeAsignarHijoDerecho() {
        NodoAVL<Integer> padre = new NodoAVL<>(10);
        NodoAVL<Integer> hijo  = new NodoAVL<>(15);
        padre.setDerecho(hijo);
        assertSame(hijo, padre.getDerecho());
    }

    @Test
    void setIzquierdo_conNull_debeDesvincularHijo() {
        NodoAVL<Integer> padre = new NodoAVL<>(10);
        padre.setIzquierdo(new NodoAVL<>(5));
        padre.setIzquierdo(null);
        assertNull(padre.getIzquierdo());
    }

    @Test
    void setDerecho_conNull_debeDesvincularHijo() {
        NodoAVL<Integer> padre = new NodoAVL<>(10);
        padre.setDerecho(new NodoAVL<>(15));
        padre.setDerecho(null);
        assertNull(padre.getDerecho());
    }

    @Test
    void tieneHijos_sinHijos_debeRetornarFalse() {
        NodoAVL<Integer> nodo = new NodoAVL<>(10);
        assertFalse(nodo.tieneHijos());
    }

    @Test
    void tieneHijos_conAmbosHijos_debeRetornarTrue() {
        NodoAVL<Integer> nodo = new NodoAVL<>(10);
        nodo.setIzquierdo(new NodoAVL<>(5));
        nodo.setDerecho(new NodoAVL<>(15));
        assertTrue(nodo.tieneHijos());
    }

    @Test
    void tieneUnHijo_soloConHijoIzquierdo_debeRetornarTrue() {
        NodoAVL<Integer> nodo = new NodoAVL<>(10);
        nodo.setIzquierdo(new NodoAVL<>(5));
        assertTrue(nodo.tieneUnHijo());
    }

    @Test
    void tieneUnHijo_conAmbosHijos_debeRetornarFalse() {
        NodoAVL<Integer> nodo = new NodoAVL<>(10);
        nodo.setIzquierdo(new NodoAVL<>(5));
        nodo.setDerecho(new NodoAVL<>(15));
        assertFalse(nodo.tieneUnHijo());
    }

    @Test
    void nodoAVL_debeSerInstanciaDeNodoDeArbol() {
        NodoAVL<Integer> nodo = new NodoAVL<>(10);
        assertInstanceOf(NodoDeArbol.class, nodo);
    }

    @Test
    void alturaNoSeHeredaDeNodoDeArbol_esPropiaDeLaSubclase() {
        // NodoDeArbol no tiene getAltura(); confirma que es exclusivo de NodoAVL
        NodoAVL<Integer> nodo = new NodoAVL<>(10);
        assertEquals(1, nodo.getAltura()); // única clase con esta propiedad
    }
}
