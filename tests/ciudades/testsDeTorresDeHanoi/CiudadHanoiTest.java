package tests.ciudades.testsDeTorresDeHanoi;

import org.junit.jupiter.api.Test;

import juego.ciudades.torresDeHanoi.CiudadHanoi;

import static org.junit.jupiter.api.Assertions.*;

public class CiudadHanoiTest {

    @Test
    public void constructor_alCrearCiudadConCantidadValida_debeInicializarMovimientosEnCero() {
        // Arrange
        int discosSolicitados = 4;

        // Act
        CiudadHanoi ciudad = new CiudadHanoi(discosSolicitados);
        

        // Assert
        assertEquals(0, ciudad.getMovimientos(), "Una ciudad recién creada debe empezar con 0 movimientos.");
    }

    @Test
    public void constructor_alIngresarCantidadDeDiscosInvalida_debeLanzarExcepcion() {
        // Arrange
        int cantidadInvalida = 1; // Menos del mínimo permitido (3)

        // Act & Assert
        // SE CORRIGE: Se espera RuntimeException que es el que suele usar ValidacionesUtiles
        RuntimeException excepcion = assertThrows(
            RuntimeException.class, 
            () -> new CiudadHanoi(cantidadInvalida)
        );
        
        assertNotNull(excepcion.getMessage(), "La excepción debe tener un mensaje descriptivo según ValidacionesUtiles.");
    }
}