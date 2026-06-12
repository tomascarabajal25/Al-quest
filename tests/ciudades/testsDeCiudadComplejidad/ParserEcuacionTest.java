package ciudades.testsDeCiudadComplejidad;

import juego.ciudades.complejidad.EcuacionRecurrencia;
import juego.ciudades.complejidad.ParserEcuacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserEcuacionTest {

    private ParserEcuacion parser;

    @BeforeEach
    public void setUp() {
        parser = new ParserEcuacion();
    }

    // --- parsear ---

    @Test
    public void testParsearEcuacionSimple() {
        EcuacionRecurrencia ec = parser.parsear("T(n) = 2T(n/2) + n");
        assertNotNull(ec);
        assertEquals(2, ec.getA());
        assertEquals(2, ec.getB());
        assertEquals("n", ec.getFn());
    }

    @Test
    public void testParsearEcuacionCuadratica() {
        EcuacionRecurrencia ec = parser.parsear("T(n) = 4T(n/2) + n^2");
        assertNotNull(ec);
        assertEquals(4, ec.getA());
        assertEquals(2, ec.getB());
        assertEquals("n^2", ec.getFn());
    }

    @Test
    public void testParsearEcuacionConstante() {
        EcuacionRecurrencia ec = parser.parsear("T(n) = 3T(n/3) + 1");
        assertNotNull(ec);
        assertEquals(3, ec.getA());
        assertEquals(3, ec.getB());
        assertEquals("1", ec.getFn());
    }

    @Test
    public void testParsearFormatoInvalidoDevuelveNull() {
        assertNull(parser.parsear("2T(n/2) + n"));
        assertNull(parser.parsear("T(n) = n"));
        assertNull(parser.parsear(""));
        assertNull(parser.parsear(null));
    }

    @Test
    public void testParsearConEspaciosExtra() {
        EcuacionRecurrencia ec = parser.parsear("T(n) =  2T(n/2) +  n");
        assertNotNull(ec, "Debe tolerar espacios extra");
    }

    // --- esValido ---

    @Test
    public void testEsValidoConFormatoCorrecto() {
        assertTrue(parser.esValido("T(n) = 2T(n/2) + n"));
        assertTrue(parser.esValido("T(n) = 4T(n/2) + n^2"));
    }

    @Test
    public void testEsValidoConFormatoIncorrecto() {
        assertFalse(parser.esValido("2T(n/2) + n"));
        assertFalse(parser.esValido("T(n) = n"));
        assertFalse(parser.esValido(""));
        assertFalse(parser.esValido(null));
    }
}
