package torresDeHanoi;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class NodoTest {

    @Test
    public void testCreacionYGetters() {
        Nodo<String> nodo = new Nodo<>("###");
        
        assertEquals("###", nodo.getDato(), "El nodo no guardó el dato correctamente");
        assertNull(nodo.getArriba(), "Un nodo nuevo no debería tener enlace hacia arriba");
        assertNull(nodo.getAbajo(), "Un nodo nuevo no debería tener enlace hacia abajo");
    }

    @Test
    public void testEnlaces() {
        Nodo<String> nodo1 = new Nodo<>("#");
        Nodo<String> nodo2 = new Nodo<>("##");
        
        nodo1.setArriba(nodo2);
        nodo2.setAbajo(nodo1);
        
        assertEquals(nodo2, nodo1.getArriba(), "El enlace hacia arriba falló");
        assertEquals(nodo1, nodo2.getAbajo(), "El enlace hacia abajo falló");
    }
}