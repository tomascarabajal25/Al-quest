package utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestValidacionesUtiles {

    @Test
    void validarMayorACero_valorPositivo_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarMayorACero(1.0, "valor"));
    }

    @Test
    void validarMayorACero_cero_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> ValidacionesUtiles.validarMayorACero(0, "valor"));
    }

    @Test
    void validarMayorACero_valorNegativo_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> ValidacionesUtiles.validarMayorACero(-5, "valor"));
    }

    @Test
    void validarMayorACero_mensajeContieneNombre() {
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.validarMayorACero(0, "precio"));
        assertTrue(ex.getMessage().contains("precio"));
    }

    @Test
    void validarMayorAUno_valorMayorAUno_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarMayorAUno(2.0, "valor"));
    }

    @Test
    void validarMayorAUno_uno_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> ValidacionesUtiles.validarMayorAUno(1.0, "valor"));
    }

    @Test
    void validarMayorAUno_cero_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> ValidacionesUtiles.validarMayorAUno(0, "valor"));
    }

    @Test
    void validarMayorAUno_valorNegativo_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> ValidacionesUtiles.validarMayorAUno(-3, "valor"));
    }


    @Test
    void validarMayorOIgualACero_cero_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarMayorOIgualACero(0, "valor"));
    }

    @Test
    void validarMayorOIgualACero_valorPositivo_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarMayorOIgualACero(10, "valor"));
    }

    @Test
    void validarMayorOIgualACero_valorNegativo_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> ValidacionesUtiles.validarMayorOIgualACero(-1, "valor"));
    }

    @Test
    void validarLongitudDeTexto_dentroDelRango_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarLongitudDeTexto("hola", 1, 10, "nombre"));
    }

    @Test
    void validarLongitudDeTexto_exactamenteElMinimo_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarLongitudDeTexto("h", 1, 10, "nombre"));
    }

    @Test
    void validarLongitudDeTexto_exactamenteElMaximo_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarLongitudDeTexto("hola", 1, 4, "nombre"));
    }

    @Test
    void validarLongitudDeTexto_menorAlMinimo_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.validarLongitudDeTexto("hi", 5, 10, "nombre"));
    }

    @Test
    void validarLongitudDeTexto_mayorAlMaximo_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.validarLongitudDeTexto("holamundo", 1, 5, "nombre"));
    }

    @Test
    void validarLongitudDeTexto_null_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.validarLongitudDeTexto(null, 1, 10, "nombre"));
    }

    @Test
    void validarLongitudDeTexto_soloEspaciosConMinimoMayorACero_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.validarLongitudDeTexto("   ", 1, 10, "nombre"));
    }

    @Test
    void validarLongitudDeTexto_sinLimiteMaximo_aceptaTextoLargo() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarLongitudDeTexto("texto muy largo sin limite", 1, null, "nombre"));
    }

    @Test
    void validarCaracteresAlfabeticos_soloLetras_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarCaracteresAlfabeticos("Hola", "nombre"));
    }

    @Test
    void validarCaracteresAlfabeticos_letrasYEspacios_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarCaracteresAlfabeticos("Juan Perez", "nombre"));
    }

    @Test
    void validarCaracteresAlfabeticos_conNumeros_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.validarCaracteresAlfabeticos("Juan123", "nombre"));
    }

    @Test
    void validarCaracteresAlfabeticos_conCaracterEspecial_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.validarCaracteresAlfabeticos("Juan@Perez", "nombre"));
    }

    @Test
    void validarCaracteresAlfabeticos_stringVacio_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.validarCaracteresAlfabeticos("", "nombre"));
    }

    @Test
    void validarFalso_conFalse_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarFalso(false, "error"));
    }

    @Test
    void validarFalso_conTrue_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> ValidacionesUtiles.validarFalso(true, "error"));
    }

    @Test
    void validarFalso_mensajeEnExcepcionEsElTexto() {
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.validarFalso(true, "condicion invalida"));
        assertEquals("condicion invalida", ex.getMessage());
    }

    @Test
    void validarVerdadero_conTrue_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarVerdadero(true, "error"));
    }

    @Test
    void validarVerdadero_conFalse_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> ValidacionesUtiles.validarVerdadero(false, "error"));
    }

    @Test
    void esDistintoDeNull_objetoNoNulo_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.esDistintoDeNull("texto", "campo"));
    }

    @Test
    void esDistintoDeNull_null_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> ValidacionesUtiles.esDistintoDeNull(null, "campo"));
    }

    @Test
    void esDistintoDeNull_mensajeContieneNombre() {
        RuntimeException ex = assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.esDistintoDeNull(null, "usuario"));
        assertTrue(ex.getMessage().contains("usuario"));
    }

    enum Color { ROJO, VERDE, AZUL }

    @Test
    void validarRangoDeEnum_valorEnLista_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarRangoDeEnum(Color.ROJO, Color.ROJO, Color.VERDE));
    }

    @Test
    void validarRangoDeEnum_valorFueraDeLista_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.validarRangoDeEnum(Color.AZUL, Color.ROJO, Color.VERDE));
    }

    @Test
    void validarRangoDeEnum_valorNull_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.validarRangoDeEnum(null, Color.ROJO));
    }

    @Test
    void validarRangoNumerico_valorDentroDelRango_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarRangoNumerico(5, 1, 10, "valor"));
    }

    @Test
    void validarRangoNumerico_bordeInferior_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarRangoNumerico(1, 1, 10, "valor"));
    }

    @Test
    void validarRangoNumerico_bordeSuperior_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarRangoNumerico(10, 1, 10, "valor"));
    }

    @Test
    void validarRangoNumerico_menorAlMinimo_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.validarRangoNumerico(0, 1, 10, "valor"));
    }

    @Test
    void validarRangoNumerico_mayorAlMaximo_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.validarRangoNumerico(11, 1, 10, "valor"));
    }

    @Test
    void validarRango_valorDentroDelRango_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarRango(5.0, 0.0, 10.0, "valor"));
    }

    @Test
    void validarRango_bordeInferior_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarRango(0.0, 0.0, 10.0, "valor"));
    }

    @Test
    void validarRango_bordeSuperior_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> ValidacionesUtiles.validarRango(10.0, 0.0, 10.0, "valor"));
    }

    @Test
    void validarRango_menorAlLimiteInicial_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.validarRango(-0.1, 0.0, 10.0, "valor"));
    }

    @Test
    void validarRango_mayorAlLimiteFinal_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class,
            () -> ValidacionesUtiles.validarRango(10.1, 0.0, 10.0, "valor"));
    }
}
