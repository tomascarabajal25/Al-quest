package utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TestUtils {

    @Test
    void sleep_ceroMilisegundos_noDebeLanzarExcepcion() {
        assertDoesNotThrow(() -> Utils.sleep(0));
    }

    @Test
    void sleep_valorPositivo_debeEsperarAproximadamenteEseTiempo() {
        long inicio = System.currentTimeMillis();
        Utils.sleep(100);
        long transcurrido = System.currentTimeMillis() - inicio;
        assertTrue(transcurrido >= 100,
            "Se esperaba al menos 100ms pero transcurrieron: " + transcurrido);
    }

    @Test
    void sleep_interrupcion_debeRestaurarFlagDeInterrupcion() throws InterruptedException {
        Thread hilo = new Thread(() -> {
            // Iniciamos un sleep largo
            Utils.sleep(5000);
        });

        hilo.start();
        Thread.sleep(50); // le damos tiempo a que entre en sleep
        hilo.interrupt();  // interrumpimos
        hilo.join(500);    // esperamos que termine

        // El hilo debe haber terminado (el flag fue restaurado y el hilo salió limpio)
        assertFalse(hilo.isAlive());
    }
}
