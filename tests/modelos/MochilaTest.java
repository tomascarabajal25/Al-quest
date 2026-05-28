package tests.modelos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modelos.Elemento;
import modelos.Mochila;

import static org.junit.jupiter.api.Assertions.*;

public class MochilaTest {

    private Mochila mochila;
    private Elemento espada;
    private Elemento pocion;
    private Elemento llave;

    @BeforeEach
    public void setUp() {

        mochila = new Mochila(3);

        espada = new Elemento("Espada");
        pocion = new Elemento("Pocion");
        llave = new Elemento("Llave");
    }

    @Test
    public void constructorCreaMochilaVacia() {

        assertEquals(0, mochila.getElementos().size());
    }

    @Test
    public void agregarElementoAgregaCorrectamente() {

        mochila.agregarElemento(espada);

        assertTrue(mochila.getElementos().contains(espada));
    }

    @Test
    public void agregarElementoAumentaCantidad() {

        mochila.agregarElemento(espada);

        assertEquals(1, mochila.getElementos().size());
    }

    @Test
    public void eliminarElementoEliminaCorrectamente() {

        mochila.agregarElemento(espada);

        mochila.eliminarElemento(espada);

        assertFalse(mochila.getElementos().contains(espada));
    }

    @Test
    public void eliminarElementoReduceCantidad() {

        mochila.agregarElemento(espada);

        mochila.eliminarElemento(espada);

        assertEquals(0, mochila.getElementos().size());
    }

    @Test
    public void getElementoPorNombreDevuelveElementoCorrecto() {

        mochila.agregarElemento(espada);

        Elemento resultado = mochila.getElementoPorNombre(espada);

        assertEquals(espada, resultado);
    }

    @Test
    public void getElementoPorNombreDevuelveNullSiNoExiste() {

        Elemento resultado = mochila.getElementoPorNombre(espada);

        assertNull(resultado);
    }

    @Test
    public void equalsDevuelveTrueParaMochilasIguales() {

        Mochila mochila2 = new Mochila(3);

        mochila.agregarElemento(espada);
        mochila2.agregarElemento(espada);

        assertEquals(mochila, mochila2);
    }

    @Test
    public void hashCodeEsIgualParaMochilasIguales() {

        Mochila mochila2 = new Mochila(3);

        mochila.agregarElemento(espada);
        mochila2.agregarElemento(espada);

        assertEquals(mochila.hashCode(), mochila2.hashCode());
    }

    @Test
    public void toStringNoDevuelveNull() {

        assertNotNull(mochila.toString());
    }

    @Test
    public void agregarElementoNullLanzaExcepcion() {

        assertThrows(RuntimeException.class, () -> {
            mochila.agregarElemento(null);
        });
    }

    @Test
    public void eliminarElementoInexistenteLanzaExcepcion() {

        assertThrows(RuntimeException.class, () -> {
            mochila.eliminarElemento(espada);
        });
    }

    @Test
    public void mochilaLlenaLanzaExcepcion() {

        mochila.agregarElemento(espada);
        mochila.agregarElemento(pocion);
        mochila.agregarElemento(llave);

        Elemento arco = new Elemento("Arco");

        assertThrows(RuntimeException.class, () -> {
            mochila.agregarElemento(arco);
        });
    }
}
