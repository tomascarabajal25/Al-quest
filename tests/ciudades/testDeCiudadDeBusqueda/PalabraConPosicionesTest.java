package tests.ciudades.testDeCiudadDeBusqueda;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import juego.ciudades.ciudad5.PalabraConPosiciones;
import juego.ciudades.ciudad5.Posicion;

/**
 * Clase de prueba para verificar el comportamiento del TDA PalabraConPosiciones.
 */
public class PalabraConPosicionesTest {

    /**
     * Propósito: Validar que al construir el objeto se asigne correctamente la palabra
     * y se agregue de manera automática la posición inicial pasada por parámetro.
     */
    @Test
    public void testConstructorYGettersInicializanCorrectamente() {
        // Arrange
        String palabraEsperada = "Algoritmos";
        int fila = 1;
        int columna = 2;

        // Act
        PalabraConPosiciones palabraConPos = new PalabraConPosiciones(palabraEsperada, fila, columna);

        // Assert
        assertEquals(palabraEsperada, palabraConPos.getPalabra(), "La palabra guardada debe ser la ingresada.");
        List<Posicion> posiciones = palabraConPos.getPosiciones();
        assertEquals(1, posiciones.size(), "Debe inicializarse con exactamente una posición.");
        assertEquals(new Posicion(fila, columna), posiciones.get(0), "La posición inicial debe coincidir con los parámetros pasados.");
    }

    /**
     * Propósito: Comprobar que el método agregarPosicion añade nuevos elementos
     * de coordenadas al historial interno de forma acumulativa.
     */
    @Test
    public void testAgregarPosicionAgregaElementosALaLista() {
        // Arrange
        PalabraConPosiciones palabraConPos = new PalabraConPosiciones("Estructuras", 0, 0);

        // Act
        palabraConPos.agregarPosicion(4, 5);

        // Assert
        List<Posicion> posiciones = palabraConPos.getPosiciones();
        assertEquals(2, posiciones.size(), "La lista de posiciones debe tener 2 elementos tras agregar uno nuevo.");
        assertEquals(new Posicion(4, 5), posiciones.get(1), "La segunda posición debe coincidir con la agregada.");
    }

    /**
     * Propósito: Asegurar el principio de encapsulamiento verificando que getPosiciones()
     * devuelva una copia defensiva y no permita alterar la lista interna original.
     */
    @Test
    public void testGetPosicionesDevuelveUnaCopiaYNoPermiteModificarElInterno() {
        // Arrange
        PalabraConPosiciones palabraConPos = new PalabraConPosiciones("Java", 1, 1);

        // Act
        List<Posicion> copiaPosiciones = palabraConPos.getPosiciones();
        copiaPosiciones.clear(); // Intentamos limpiar la lista devuelta

        // Assert
        assertEquals(1, palabraConPos.getPosiciones().size(), "Modificar la lista externa devuelta no debe alterar el estado interno.");
    }

    /**
     * Propósito: Validar que el ordenamiento natural (compareTo) se realice
     * de forma alfabética (lexicográfica) según la palabra contenida.
     */
    @Test
    public void testCompareToOrdenaLexicograficamentePorPalabra() {
        // Arrange
        PalabraConPosiciones palabraA = new PalabraConPosiciones("Arbol", 0, 0);
        PalabraConPosiciones palabraB = new PalabraConPosiciones("Grafo", 2, 2);
        PalabraConPosiciones palabraA2 = new PalabraConPosiciones("Arbol", 5, 5);

        // Act & Assert
        assertTrue(palabraA.compareTo(palabraB) < 0, "'Arbol' debe ser menor lexicográficamente que 'Grafo'.");
        assertTrue(palabraB.compareTo(palabraA) > 0, "'Grafo' debe ser mayor lexicográficamente que 'Arbol'.");
        assertEquals(0, palabraA.compareTo(palabraA2), "Palabras con el mismo texto deben dar 0, sin importar las posiciones.");
    }

    /**
     * Propósito: Verificar que la igualdad de estos objetos se determine únicamente
     * mediante el texto de la palabra, sin importar que posean coordenadas distintas.
     */
    @Test
    public void testEqualsMismaPalabraSonIguales() {
        // Arrange
        PalabraConPosiciones pal1 = new PalabraConPosiciones("Test", 1, 1);
        PalabraConPosiciones pal2 = new PalabraConPosiciones("Test", 9, 9);

        // Act & Assert
        assertEquals(pal1, pal2, "Dos objetos con idéntica palabra deben ser iguales independientemente de sus coordenadas.");
    }

    /**
     * Propósito: Confirmar que palabras distintas no sean evaluadas como iguales.
     */
    @Test
    public void testEqualsDiferentePalabraNoSonIguales() {
        // Arrange
        PalabraConPosiciones pal1 = new PalabraConPosiciones("Hola", 1, 1);
        PalabraConPosiciones pal2 = new PalabraConPosiciones("Chau", 1, 1);

        // Act & Assert
        assertNotEquals(pal1, pal2, "Palabras con diferente texto no deben ser iguales.");
    }

    /**
     * Propósito: Validar que el valor hash cumpla el criterio de igualdad, dependiendo
     * exclusivamente del texto interno de la palabra.
     */
    @Test
    public void testHashCodeMismaPalabraTieneMismoHash() {
        // Arrange
        PalabraConPosiciones pal1 = new PalabraConPosiciones("Mismo", 1, 2);
        PalabraConPosiciones pal2 = new PalabraConPosiciones("Mismo", 5, 6);

        // Act & Assert
        assertEquals(pal1.hashCode(), pal2.hashCode(), "Palabras iguales deben generar idéntico hashCode.");
    }

    /**
     * Propósito: Comprobar el formato correcto de la salida del método toString().
     */
    @Test
    public void testToStringFormatoCorrecto() {
        // Arrange
        PalabraConPosiciones pal = new PalabraConPosiciones("Mundo", 1, 2);
        String formatoEsperado = "PalabraConPosiciones [palabra=Mundo, posiciones=[Posicion [linea=1, columna=2]]]";

        // Act
        String resultado = pal.toString();

        // Assert
        assertEquals(formatoEsperado, resultado, "El formato de toString() debe coincidir con el esperado.");
    }
}