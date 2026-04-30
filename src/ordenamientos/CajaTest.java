package ordenamientos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class CajaTest {

    @Test
    public void testGetVolumen() {
        Caja caja = new Caja(2.0, 3.0, 4.0);
        // 2 * 3 * 4 = 24.0
        assertEquals(24.0, caja.getVolumen(), "El volumen calculado es incorrecto");
    }

    @Test
    public void testEquals() {
        Caja caja1 = new Caja(2.0, 2.0, 2.0);
        Caja caja2 = new Caja(2.0, 2.0, 2.0);
        Caja cajaDiferente = new Caja(1.0, 1.0, 1.0);

        assertTrue(caja1.equals(caja2), "Las cajas con las mismas dimensiones deberían ser iguales");
        assertFalse(caja1.equals(cajaDiferente), "Las cajas con distintas dimensiones no deberían ser iguales");
    }
}