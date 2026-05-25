package ordenamientos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CajaTest {
	    @Test
	    public void testCajaHeredaCorrectamenteYGuardaTamaño() {
	        Caja caja = new Caja("Caja Mediana", 50);
	        assertEquals("Caja Mediana", caja.getNombre());
	        assertEquals(50, caja.getTamaño());
	    }

	    @Test
	    public void testCajaEqualsPorTamaño() {
	        Caja caja1 = new Caja("Caja A", 30);
	        Caja caja2 = new Caja("Caja B", 30);
	        Caja caja3 = new Caja("Caja C", 10);

	        assertEquals(caja1, caja2, "Cajas con mismo tamaño deben ser iguales");
	        assertNotEquals(caja1, caja3, "Cajas con distinto tamaño no deben ser iguales");
	    }

	    @Test
	    public void testCajaCompareTo() {
	        Caja chica = new Caja("Chica", 10);
	        Caja grande = new Caja("Grande", 100);

	        assertTrue(chica.compareTo(grande) < 0, "Chica debe ser menor que Grande");
	        assertTrue(grande.compareTo(chica) > 0, "Grande debe ser mayor que Chica");
	        assertEquals(0, chica.compareTo(new Caja("Otra", 10)), "Mismo tamaño debe dar 0");
	    }
	}