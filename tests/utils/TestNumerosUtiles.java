package utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestNumerosUtiles {

    @Test
    void toInt_valorPositivo_debeRetornarParteEntera() {
        assertEquals(3, NumerosUtiles.toInt(3.9));
    }

    @Test
    void toInt_valorNegativo_debeRetornarParteEntera() {
        // intValue() trunca hacia cero, no hacia abajo
        assertEquals(-3, NumerosUtiles.toInt(-3.9));
    }

    @Test
    void toInt_valorExacto_debeRetornarMismoEntero() {
        assertEquals(5, NumerosUtiles.toInt(5.0));
    }

    @Test
    void toInt_cero_debeRetornarCero() {
        assertEquals(0, NumerosUtiles.toInt(0.0));
    }

    @Test
    void toInt_valorMuyPequeno_debeRetornarCero() {
        assertEquals(0, NumerosUtiles.toInt(0.9999));
    }

    @Test
    void toInt_valorNull_debeLanzarExcepcion() {
        assertThrows(NullPointerException.class, () -> NumerosUtiles.toInt(null));
    }

    @Test
    void limitarRango_valorDentroDelRango_debeRetornarMismoValor() {
        assertEquals(128, NumerosUtiles.limitarRango(0, 255, 128));
    }

    @Test
    void limitarRango_valorPorDebajoDelMinimo_debeRetornarMinimo() {
        assertEquals(0, NumerosUtiles.limitarRango(0, 255, -50));
    }

    @Test
    void limitarRango_valorPorEncimaDelMaximo_debeRetornarMaximo() {
        assertEquals(255, NumerosUtiles.limitarRango(0, 255, 300));
    }

    @Test
    void limitarRango_valorIgualAlMinimo_debeRetornarMinimo() {
        assertEquals(0, NumerosUtiles.limitarRango(0, 255, 0));
    }

    @Test
    void limitarRango_valorIgualAlMaximo_debeRetornarMaximo() {
        assertEquals(255, NumerosUtiles.limitarRango(0, 255, 255));
    }

    @Test
    void limitarRango_minimoYMaximoIguales_debeRetornarEseValor() {
        assertEquals(10, NumerosUtiles.limitarRango(10, 10, 99));
    }

    @Test
    void limitarRango_rangoNegativo_valorDentro_debeRetornarMismoValor() {
        assertEquals(-5, NumerosUtiles.limitarRango(-10, -1, -5));
    }

    @Test
    void limitarRango_rangoNegativo_valorPorDebajo_debeRetornarMinimo() {
        assertEquals(-10, NumerosUtiles.limitarRango(-10, -1, -50));
    }

    @Test
    void limitarRango_rangoNegativo_valorPorEncima_debeRetornarMaximo() {
        assertEquals(-1, NumerosUtiles.limitarRango(-10, -1, 0));
    }
}
