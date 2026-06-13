package estructuras.arboles;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestArbolAVL {

    @Test
    public void insertarUnElemento() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(10);

        assertTrue(arbol.buscar(10));
    }

    @Test
    public void insertarVariosElementos() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(10);
        arbol.insertar(20);
        arbol.insertar(5);

        assertTrue(arbol.buscar(10));
        assertTrue(arbol.buscar(20));
        assertTrue(arbol.buscar(5));
    }

    @Test
    public void insertarElementoDuplicadoNoModificaElArbol() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(10);
        arbol.insertar(10);

        // El duplicado no se agrega: sigue habiendo una sola hoja
        assertEquals(1, arbol.contarCantidadDeHojas());
        assertTrue(arbol.buscar(10));
    }

    @Test
    public void arbolVacioEstaVacio() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        assertTrue(arbol.estaVacio());
    }

    @Test
    public void arbolConElementosNoEstaVacio() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(10);

        assertFalse(arbol.estaVacio());
    }

    @Test
    public void buscarElementoInexistente() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(10);

        assertFalse(arbol.buscar(99));
    }

    @Test
    public void buscarEnArbolVacio() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        assertFalse(arbol.buscar(10));
    }

    @Test
    public void alturaArbolVacioEsCero() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        assertEquals(0, arbol.calcularAltura());
    }

    @Test
    public void alturaArbolConUnElementoEsUno() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(10);

        assertEquals(1, arbol.calcularAltura());
    }

    @Test
    public void alturaBalanceadaTrasTresInserciones() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        // Sin AVL la altura sería 3; con balanceo debe quedar en 2
        arbol.insertar(10);
        arbol.insertar(20);
        arbol.insertar(30);

        assertEquals(2, arbol.calcularAltura());
    }

    @Test
    public void alturaSeMantieneBalanceadaConMuchosElementos() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        // 7 elementos en un AVL deben tener altura <= 3
        arbol.insertar(10);
        arbol.insertar(20);
        arbol.insertar(30);
        arbol.insertar(40);
        arbol.insertar(50);
        arbol.insertar(25);
        arbol.insertar(35);

        assertTrue(arbol.calcularAltura() <= 3);
    }

    @Test
    public void rotacionDerechaMantieneElementos() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        // Insertar en orden descendente fuerza rotación LL
        arbol.insertar(30);
        arbol.insertar(20);
        arbol.insertar(10);

        assertTrue(arbol.buscar(10));
        assertTrue(arbol.buscar(20));
        assertTrue(arbol.buscar(30));
    }

    @Test
    public void rotacionDerechaBalanceaElArbol() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(30);
        arbol.insertar(20);
        arbol.insertar(10);

        // Tras rotación LL la raíz debe ser 20 y la altura 2
        assertEquals(20, arbol.getRaiz().getValor());
        assertEquals(2, arbol.calcularAltura());
    }

    @Test
    public void rotacionIzquierdaMantieneElementos() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        // Insertar en orden ascendente fuerza rotación RR
        arbol.insertar(10);
        arbol.insertar(20);
        arbol.insertar(30);

        assertTrue(arbol.buscar(10));
        assertTrue(arbol.buscar(20));
        assertTrue(arbol.buscar(30));
    }

    @Test
    public void rotacionIzquierdaBalanceaElArbol() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(10);
        arbol.insertar(20);
        arbol.insertar(30);

        // Tras rotación RR la raíz debe ser 20 y la altura 2
        assertEquals(20, arbol.getRaiz().getValor());
        assertEquals(2, arbol.calcularAltura());
    }

    @Test
    public void rotacionIzquierdaDerechaMantieneElementos() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        // Insertar 30, 10, 20 fuerza rotación LR
        arbol.insertar(30);
        arbol.insertar(10);
        arbol.insertar(20);

        assertTrue(arbol.buscar(10));
        assertTrue(arbol.buscar(20));
        assertTrue(arbol.buscar(30));
    }

    @Test
    public void rotacionIzquierdaDerechaBalanceaElArbol() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(30);
        arbol.insertar(10);
        arbol.insertar(20);

        // Tras rotación LR la raíz debe ser 20 y la altura 2
        assertEquals(20, arbol.getRaiz().getValor());
        assertEquals(2, arbol.calcularAltura());
    }

    @Test
    public void rotacionDerechaIzquierdaMantieneElementos() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        // Insertar 10, 30, 20 fuerza rotación RL
        arbol.insertar(10);
        arbol.insertar(30);
        arbol.insertar(20);

        assertTrue(arbol.buscar(10));
        assertTrue(arbol.buscar(20));
        assertTrue(arbol.buscar(30));
    }

    @Test
    public void rotacionDerechaIzquierdaBalanceaElArbol() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(10);
        arbol.insertar(30);
        arbol.insertar(20);

        // Tras rotación RL la raíz debe ser 20 y la altura 2
        assertEquals(20, arbol.getRaiz().getValor());
        assertEquals(2, arbol.calcularAltura());
    }

    @Test
    public void contarHojasArbolEquilibrado() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        // Tras rotación RR: raíz=20, hijos=10 y 30 (ambos hojas)
        arbol.insertar(10);
        arbol.insertar(20);
        arbol.insertar(30);

        assertEquals(2, arbol.contarCantidadDeHojas());
    }

    @Test
    public void contarNodosConDosHijos() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        // Raíz con dos hijos: un nodo con dos hijos
        arbol.insertar(10);
        arbol.insertar(20);
        arbol.insertar(30);

        assertEquals(1, arbol.contarCantidadDeNodosConDosHijos());
    }

    @Test
    public void eliminarElementoExistente() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(10);
        arbol.insertar(20);
        arbol.insertar(5);

        arbol.eliminar(20);

        assertFalse(arbol.buscar(20));
        assertTrue(arbol.buscar(10));
        assertTrue(arbol.buscar(5));
    }

    @Test
    public void eliminarElementoInexistenteNoLanzaExcepcion() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(10);

        assertDoesNotThrow(() -> arbol.eliminar(99));
    }

    @Test
    public void eliminarUnicoElementoDejaArbolVacio() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(10);
        arbol.eliminar(10);

        assertTrue(arbol.estaVacio());
        assertFalse(arbol.buscar(10));
    }

    @Test
    public void eliminarNodoHoja() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(20);
        arbol.insertar(10);
        arbol.insertar(30);

        arbol.eliminar(10);

        assertFalse(arbol.buscar(10));
        assertTrue(arbol.buscar(20));
        assertTrue(arbol.buscar(30));
        assertEquals(1, arbol.contarCantidadDeHojas());
    }

    @Test
    public void eliminarNodoConDosHijos() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(20);
        arbol.insertar(10);
        arbol.insertar(30);
        arbol.insertar(25);
        arbol.insertar(35);

        arbol.eliminar(30);

        assertFalse(arbol.buscar(30));
        assertTrue(arbol.buscar(20));
        assertTrue(arbol.buscar(25));
        assertTrue(arbol.buscar(35));
    }

    @Test
    public void eliminarRaizConDosHijos() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(20);
        arbol.insertar(10);
        arbol.insertar(30);

        arbol.eliminar(20);

        assertFalse(arbol.buscar(20));
        assertTrue(arbol.buscar(10));
        assertTrue(arbol.buscar(30));
        assertFalse(arbol.estaVacio());
    }

    @Test
    public void eliminarMantieneLaAlturaBalanceada() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(20);
        arbol.insertar(10);
        arbol.insertar(30);
        arbol.insertar(5);

        // Eliminar 30 desbalancea → debe rebalancear
        arbol.eliminar(30);

        assertTrue(arbol.buscar(20));
        assertTrue(arbol.buscar(10));
        assertTrue(arbol.buscar(5));
        assertTrue(arbol.calcularAltura() <= 2);
    }

    @Test
    public void insertarYEliminarTodosLosElementos() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        arbol.insertar(10);
        arbol.insertar(20);
        arbol.insertar(30);

        arbol.eliminar(10);
        arbol.eliminar(20);
        arbol.eliminar(30);

        assertTrue(arbol.estaVacio());
        assertFalse(arbol.buscar(10));
        assertFalse(arbol.buscar(20));
        assertFalse(arbol.buscar(30));
    }

    @Test
    public void insertarMuchosElementosMantieneTodosLosValores() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        int[] valores = {50, 25, 75, 10, 30, 60, 80, 5, 15, 27};

        for (int v : valores) {
            arbol.insertar(v);
        }

        for (int v : valores) {
            assertTrue(arbol.buscar(v));
        }
    }

    @Test
    public void insertarMuchosElementosMantieneLaAlturaBalanceada() {

        ArbolAVL<Integer> arbol = new ArbolAVL<>();

        // 10 elementos en un AVL deben tener altura <= 4
        int[] valores = {50, 25, 75, 10, 30, 60, 80, 5, 15, 27};

        for (int v : valores) {
            arbol.insertar(v);
        }

        assertTrue(arbol.calcularAltura() <= 4);
    }
}

