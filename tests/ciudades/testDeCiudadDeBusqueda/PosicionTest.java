package tests.ciudades.testDeCiudadDeBusqueda;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import juego.ciudades.ciudad5.Posicion;

/**
 * Clase de prueba para verificar el comportamiento del TDA Posicion.
 */
public class PosicionTest {

    /**
     * Propósito: Verificar que el constructor almacene la línea y la columna
     * de forma correcta y que los getters expongan dichos valores.
     */
    @Test
    public void testConstructorYGettersGuardanValoresCorrectamente() {
        // Arrange
        int lineaEsperada = 5;
        int columnaEsperada = 10;

        // Act
        Posicion posicion = new Posicion(lineaEsperada, columnaEsperada);

        // Assert
        assertEquals(lineaEsperada, posicion.getLinea(), "La línea guardada debe coincidir con la ingresada.");
        assertEquals(columnaEsperada, posicion.getIndice(), "La columna (índice) guardada debe coincidir con la ingresada.");
    }

    /**
     * Propósito: Asegurar que dos posiciones con idénticas coordenadas 
     * sean consideradas iguales al utilizar el método equals.
     */
    @Test
    public void testEqualsMismasCoordenadasSonIguales() {
        // Arrange
        Posicion posicion1 = new Posicion(3, 4);
        Posicion posicion2 = new Posicion(3, 4);

        // Act & Assert
        assertEquals(posicion1, posicion2, "Dos posiciones con iguales coordenadas deben ser iguales.");
    }

    /**
     * Propósito: Comprobar que el método equals devuelva false cuando
     * cambie alguna de las dimensiones individuales de la coordenada.
     */
    @Test
    public void testEqualsDiferentesCoordenadasNoSonIguales() {
        // Arrange
        Posicion posicionBase = new Posicion(3, 4);
        Posicion posicionDiferenteLinea = new Posicion(5, 4);
        Posicion posicionDiferenteColumna = new Posicion(3, 8);

        // Act & Assert
        assertNotEquals(posicionBase, posicionDiferenteLinea, "Posiciones con diferente línea no deben ser iguales.");
        assertNotEquals(posicionBase, posicionDiferenteColumna, "Posiciones con diferente columna no deben ser iguales.");
    }

    /**
     * Propósito: Validar que una posición no sea igual a null ni a objetos
     * pertenecientes a clases distintas.
     */
    @Test
    public void testEqualsConNuloYOtroTipoDevuelveFalse() {
        // Arrange
        Posicion posicion = new Posicion(1, 1);

        // Act & Assert
        assertNotNull(posicion, "La posición no debe ser igual a null.");
        assertNotEquals(posicion, "Un String cualquiera", "La posición no debe ser igual a un objeto de otro tipo.");
    }

    /**
     * Propósito: Garantizar que dos objetos lógicamente iguales devuelvan
     * el mismo valor hash, cumpliendo con el contrato de Java.
     */
    @Test
    public void testHashCodeMismasCoordenadasTienenMismoHash() {
        // Arrange
        Posicion posicion1 = new Posicion(12, 15);
        Posicion posicion2 = new Posicion(12, 15);

        // Act & Assert
        assertEquals(posicion1.hashCode(), posicion2.hashCode(), "Posiciones iguales deben generar el mismo hashCode.");
    }

    /**
     * Propósito: Verificar que la representación textual del objeto cumpla 
     * con el formato esperado ("Posicion [linea=X, columna=Y]").
     */
    @Test
    public void testToStringFormatoCorrecto() {
        // Arrange
        Posicion posicion = new Posicion(2, 3);
        String formatoEsperado = "Posicion [linea=2, columna=3]";

        // Act
        String resultado = posicion.toString();

        // Assert
        assertEquals(formatoEsperado, resultado, "El formato de toString() debe coincidir con el esperado.");
    }
}