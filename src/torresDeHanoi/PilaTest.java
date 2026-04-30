package torresDeHanoi;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PilaTest {

    private Pila pila;

    @BeforeEach
    public void setUp() {
        pila = new Pila();
    }

    @Test
    public void testPilaVacia() {
        assertEquals(0, pila.getContNodo(), "La pila debería inicializarse con 0 nodos");
        assertNull(pila.getCabeza(), "La cabeza de una pila vacía debe ser null");
    }

    @Test
    public void testPushYPeek() {
        pila.push(new Nodo<>("###"));
        pila.push(new Nodo<>("#"));
        
        assertEquals(2, pila.getContNodo(), "El contador de nodos no se actualizó bien tras los push");
        assertEquals("#", pila.peek(), "El peek no devolvió el último elemento insertado (LIFO)");
    }

    @Test
    public void testPop() {
        pila.push(new Nodo<>("###"));
        pila.push(new Nodo<>("#"));
        
        pila.pop(); // Sacamos "#"
        
        assertEquals(1, pila.getContNodo(), "El contador no se restó tras el pop");
        assertEquals("###", pila.peek(), "La cabeza no se actualizó tras el pop");
        
        pila.pop(); // Sacamos "###", pila queda vacía
        assertEquals(0, pila.getContNodo());
        assertNull(pila.getCabeza());
    }
}