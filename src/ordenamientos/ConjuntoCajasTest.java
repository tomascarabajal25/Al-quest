package ordenamientos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ConjuntoCajasTest {

    private ConjuntoCajas conjunto;

    @BeforeEach
    public void setUp() {
        // Esto se ejecuta antes de cada @Test, dejándote un conjunto limpio
        conjunto = new ConjuntoCajas();
    }

    @Test
    public void testAgregarCajaYGetCantidad() {
        assertEquals(0, conjunto.getCantidad(), "El conjunto debería arrancar vacío");
        
        conjunto.agregarCaja(new Caja(1.0, 1.0, 1.0));
        conjunto.agregarCaja(new Caja(2.0, 2.0, 2.0));
        
        assertEquals(2, conjunto.getCantidad(), "Debería haber 2 cajas en el conjunto");
    }

    
}