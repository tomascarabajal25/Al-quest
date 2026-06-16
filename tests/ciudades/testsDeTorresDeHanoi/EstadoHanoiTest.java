package tests.ciudades.testsDeTorresDeHanoi;

import org.junit.jupiter.api.Test;

import juego.ciudades.torresDeHanoi.EstadoHanoi;

import static org.junit.jupiter.api.Assertions.*;

public class EstadoHanoiTest {

    @Test
    public void estadoHanoi_alCrearEstado_debeClonarLosArreglosParaSerInmutable() {
        // Arrange
        int[] torreAInicial = {3, 2, 1};
        int[] torreBInicial = {0, 0, 0};
        int[] torreCInicial = {0, 0, 0};
        int movimientos = 5;
        double minMovimientos = 7.0;

        // Act
        EstadoHanoi estado = new EstadoHanoi(torreAInicial, torreBInicial, torreCInicial, movimientos, minMovimientos);
        
        // Modificamos el arreglo original después de crear el estado
        torreAInicial[0] = 99; 

        // Assert
        int[] torreADelEstado = estado.getTorreA();
        assertNotEquals(99, torreADelEstado[0], "El estado debe clonar el arreglo para garantizar la inmutabilidad.");
        assertEquals(3, torreADelEstado[0], "El estado debe mantener el valor original con el que fue instanciado.");
    }

    @Test
    public void getTorre_alPedirUnaTorre_debeDevolverUnClonParaEvitarModificacionesExternas() {
        // Arrange
        int[] torreA = {1, 0, 0};
        int[] torreB = {0, 0, 0};
        int[] torreC = {0, 0, 0};
        EstadoHanoi estado = new EstadoHanoi(torreA, torreB, torreC, 0, 1.0);

        // Act
        int[] torreObtenida = estado.getTorreA();
        torreObtenida[0] = 50; // Intentamos corromper el estado desde afuera

        // Assert
        assertNotEquals(50, estado.getTorreA()[0], "El getter debe devolver un clon, no la referencia interna.");
        assertEquals(1, estado.getTorreA()[0], "El estado interno debe permanecer intacto.");
    }
}