package ciudades.testsDeCiudadComplejidad;

import juego.ciudades.complejidad.EcuacionRecurrencia;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EcuacionRecurrenciaTest {

    // --- constructor y getters ---

    @Test
    public void testConstructorGuardaValores() {
        EcuacionRecurrencia ec = new EcuacionRecurrencia(2, 2, "n");
        assertEquals(2, ec.getA());
        assertEquals(2, ec.getB());
        assertEquals("n", ec.getFn());
    }

    // --- toString ---

    @Test
    public void testToStringFormatoCorrecto() {
        EcuacionRecurrencia ec = new EcuacionRecurrencia(2, 2, "n");
        assertEquals("T(n) = 2T(n/2) + n", ec.toString());
    }

    @Test
    public void testToStringConFuncionCuadratica() {
        EcuacionRecurrencia ec = new EcuacionRecurrencia(4, 2, "n^2");
        assertEquals("T(n) = 4T(n/2) + n^2", ec.toString());
    }

    // --- equals ---

    @Test
    public void testEqualsIguales() {
        EcuacionRecurrencia ec1 = new EcuacionRecurrencia(2, 2, "n");
        EcuacionRecurrencia ec2 = new EcuacionRecurrencia(2, 2, "n");
        assertEquals(ec1, ec2);
    }

    @Test
    public void testEqualsDistintoA() {
        EcuacionRecurrencia ec1 = new EcuacionRecurrencia(2, 2, "n");
        EcuacionRecurrencia ec2 = new EcuacionRecurrencia(3, 2, "n");
        assertNotEquals(ec1, ec2);
    }

    @Test
    public void testEqualsDistintoB() {
        EcuacionRecurrencia ec1 = new EcuacionRecurrencia(2, 2, "n");
        EcuacionRecurrencia ec2 = new EcuacionRecurrencia(2, 3, "n");
        assertNotEquals(ec1, ec2);
    }

    @Test
    public void testEqualsDistintoFn() {
        EcuacionRecurrencia ec1 = new EcuacionRecurrencia(2, 2, "n");
        EcuacionRecurrencia ec2 = new EcuacionRecurrencia(2, 2, "n^2");
        assertNotEquals(ec1, ec2);
    }

    @Test
    public void testEqualsConNull() {
        EcuacionRecurrencia ec = new EcuacionRecurrencia(2, 2, "n");
        assertNotEquals(ec, null);
    }

    @Test
    public void testEqualsConOtroTipo() {
        EcuacionRecurrencia ec = new EcuacionRecurrencia(2, 2, "n");
        assertNotEquals(ec, "T(n) = 2T(n/2) + n");
    }
}
