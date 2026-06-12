package ciudades.testsDeCiudadLaberinto;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import juego.ciudades.ciudad_3_laberinto.src.*;

/**
 * Tests unitarios para CargadorLaberinto.
 */
public class CargadorLaberintoTest {

    private static final String RUTA_VALIDA = 
        "src/juego/ciudades/ciudad_3_laberinto/resources/laberinto.txt";

    /** Verifica que el laberinto se carga correctamente desde el archivo */
    @Test
    public void testCargarLaberintoValido() throws IOException {
        CargadorLaberinto cargador = new CargadorLaberinto();
        Laberinto laberinto = cargador.cargar(RUTA_VALIDA);

        assertNotNull(laberinto);
        assertNotNull(laberinto.getCeldaInicio());
        assertNotNull(laberinto.getCeldaFin());
        assertTrue(laberinto.getFilas() > 0);
        assertTrue(laberinto.getColumnas() > 0);
    }

    /** Verifica que se lanza excepcion si el archivo no existe */
    @Test(expected = IOException.class)
    public void testArchivoInexistente() throws IOException {
        CargadorLaberinto cargador = new CargadorLaberinto();
        cargador.cargar("ruta/inexistente.txt");
    }
}