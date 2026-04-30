package torresDeHanoi;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JuegoHanoiTest {

    private JuegoHanoi juego;

    @BeforeEach
    public void setUp() {
        juego = new JuegoHanoi(3); // Iniciamos con 3 discos
    }

    @Test
    public void testInicializacion() {
        assertEquals(3, juego.getTorreA().getContNodo(), "La Torre A debería tener todos los discos");
        assertEquals(0, juego.getTorreB().getContNodo(), "La Torre B debería estar vacía");
        assertEquals(0, juego.getTorreC().getContNodo(), "La Torre C debería estar vacía");
        assertEquals(0, juego.getMovimientos(), "Los movimientos deben iniciar en 0");
        
        // El mínimo para 3 discos es (2^3) - 1 = 7
        assertEquals(7.0, juego.getMinMovimientos(), "El cálculo de movimientos mínimos falló");
    }

    @Test
    public void testMovimientoValido() {
        // Mover disco pequeño de A a B
        boolean resultado = juego.mover(juego.getTorreA(), juego.getTorreB());
        
        assertTrue(resultado, "El movimiento debería haber sido aceptado");
        assertEquals(2, juego.getTorreA().getContNodo(), "A debería tener 2 discos");
        assertEquals(1, juego.getTorreB().getContNodo(), "B debería tener 1 disco");
        assertEquals(1, juego.getMovimientos(), "El contador de movimientos debió sumar 1");
    }

    @Test
    public void testMovimientoInvalidoDiscoMasGrande() {
        // Mover disco pequeño de A a B
        juego.mover(juego.getTorreA(), juego.getTorreB());
        
        // Intentar mover disco mediano de A sobre el pequeño en B
        boolean resultado = juego.mover(juego.getTorreA(), juego.getTorreB());
        
        assertFalse(resultado, "No se puede apilar un disco más grande sobre uno más chico");
        assertEquals(2, juego.getTorreA().getContNodo(), "La Torre A no debió alterarse");
        assertEquals(1, juego.getMovimientos(), "Los movimientos inválidos no deben sumar");
    }

    @Test
    public void testMovimientoDesdeTorreVacia() {
        boolean resultado = juego.mover(juego.getTorreC(), juego.getTorreA());
        assertFalse(resultado, "No se puede mover un disco desde una torre vacía");
    }

    @Test
    public void testReinicio() {
        juego.mover(juego.getTorreA(), juego.getTorreB()); // Hacemos un movimiento
        
        juego.reiniciar(4); // Reiniciamos el juego con 4 discos
        
        assertEquals(4, juego.getTorreA().getContNodo(), "La Torre A debería tener 4 discos");
        assertEquals(0, juego.getTorreB().getContNodo(), "La Torre B debería haberse vaciado");
        assertEquals(0, juego.getMovimientos(), "Los movimientos deberían haber vuelto a 0");
        assertEquals(15.0, juego.getMinMovimientos(), "El mínimo para 4 discos es 15");
    }
}