package ciudades.testsDeLaCiudadDeHashing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import juego.ciudades.hashing.ElementoHash;
import modelos.Elemento;



/**
 * Verifica que hereda de Elemento, tambien que efectivamente este guardando
 * su clave y su nombre de manera correcta.
 */
public class ElementoHashTest {
    
    @Test
    public void testGuardaClaveYNombre() {
        ElementoHash elemento = new ElementoHash(10, "Pocion");
        assertEquals(10, elemento.getClave(), "guarda la clave");
        assertEquals("Pocion", elemento.getNombre(), "guarda el nombre heredado de Elemento");
    }

    @Test 
    public void testEsUnElemento() {
        ElementoHash elemento = new ElementoHash(5, "Gema");
        assertTrue(elemento instanceof Elemento, "ElementoHash debe ser un Elemento (subclase concreta)");
    }

    @Test
    /**
     * En ElementoHash.java, hice hincapie en esto. 
    */
    public void testAplicarEfectoNoLanzaExcepcion () {
        ElementoHash elemento = new ElementoHash(5,"Gema");
        //En esta ciudad, los elementos no producen efecto, metodo vacio.
        assertDoesNotThrow(() -> elemento.aplicarEfecto(null), "aplicarEfecto no produce efecto ni tira error en la ciudad hashing");
    }

    
}
