package utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestSistemaUtiles {

    @Test
    void esperar_ceroMilisegundos_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> SistemaUtiles.esperar(0));
    }

    @Test
    void esperar_valorPositivo_debeEsperarAproximadamenteEseTiempo() {
        long inicio = System.currentTimeMillis();
        SistemaUtiles.esperar(100);
        long transcurrido = System.currentTimeMillis() - inicio;
        assertTrue(transcurrido >= 100,
            "Se esperaba al menos 100ms pero transcurrieron: " + transcurrido);
    }

    @Test
    void esperar_valorNegativo_debeLanzarExcepcion() {
        // ValidacionesUtiles.validarMayorOIgualACero debe rechazarlo
        assertThrows(Exception.class, () -> SistemaUtiles.esperar(-1));
    }

    @Test
    void generarRutaAbsoluta_debeRetornarStringNoNulo() {
        String ruta = SistemaUtiles.generarRutaAbsoluta("src/test/resources/archivo.txt");
        assertNotNull(ruta);
    }

    @Test
    void generarRutaAbsoluta_debeRetornarStringNoVacio() {
        String ruta = SistemaUtiles.generarRutaAbsoluta("src/test/resources/archivo.txt");
        assertFalse(ruta.isBlank());
    }

    @Test
    void generarRutaAbsoluta_debeTerminarConLaRutaRelativa() {
        String relativa = "src/test/resources/archivo.txt";
        String absoluta = SistemaUtiles.generarRutaAbsoluta(relativa);
        assertTrue(absoluta.endsWith(relativa),
            "Se esperaba que terminara con '" + relativa + "' pero fue: " + absoluta);
    }

    @Test
    void generarRutaAbsoluta_noDebeEmpezarConBarra() {
        String ruta = SistemaUtiles.generarRutaAbsoluta("src/archivo.txt");
        assertFalse(ruta.startsWith("/"),
            "La ruta no debe empezar con '/' pero fue: " + ruta);
    }

    @Test
    void generarRutaAbsoluta_conRutaVacia_debeRetornarSoloLaBase() {
        String ruta = SistemaUtiles.generarRutaAbsoluta("");
        assertNotNull(ruta);
        assertFalse(ruta.isBlank());
    }
}
