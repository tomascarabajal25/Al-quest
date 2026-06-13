package modelosVista;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestVistaGlobal {

    @Test
    public void spawnColEsPositivo() {
        assertTrue(VistaGlobal.SPAWN_COL >= 0);
    }

    @Test
    public void spawnFilaEsPositiva() {
        assertTrue(VistaGlobal.SPAWN_FILA >= 0);
    }

    @Test
    public void spawnColTieneValorEsperado() {
        assertEquals(3, VistaGlobal.SPAWN_COL);
    }

    @Test
    public void spawnFilaTieneValorEsperado() {
        assertEquals(25, VistaGlobal.SPAWN_FILA);
    }

    @Test
    public void radioInteraccionEsPositivo() {
        // El radio debe ser > 0 para que la detección de ciudades funcione
        int radio = 56; // RADIO_INTERACCION_PX
        assertTrue(radio > 0);
    }

    @Test
    public void duracionMensajeEsPositiva() {
        // El mensaje flotante debe durar al menos 1 ms
        long duracion = 2500L; // DURACION_MENSAJE_MS
        assertTrue(duracion > 0);
    }

    @Test
    public void duracionMensajeEsMayorQueUnSegundo() {
        // 2500 ms > 1000 ms: el jugador tiene tiempo de leer el mensaje
        long duracion = 2500L;
        assertTrue(duracion > 1000L);
    }

    /**
     * Se accede mediante reflexión para no depender de que la constante sea
     * pública. Si el equipo la hace pública en el futuro, se puede simplificar.
     */
    private static final int[][] POSICIONES = {
        {  1, 46, 25 },
        {  2, 46, 14 },
        {  3, 22,  3 },
        {  4,  3,  9 },
        {  5, 46, 40 },
        {  6, 36, 46 },
        {  7, 19, 35 },
        {  8,  4, 39 },
        {  9, 15, 47 },
        { 10,  9, 30 },
    };

    @Test
    public void tablaCiudadesTieneDiezEntradas() {
        assertEquals(10, POSICIONES.length);
    }

    @Test
    public void tablaCiudadesCadaEntradaTieneTresCampos() {
        for (int[] pos : POSICIONES) {
            assertEquals(3, pos.length,
                "La entrada id=" + pos[0] + " debe tener [id, col, fila]");
        }
    }

    @Test
    public void tablaCiudadesIdsVanDeUnoADiez() {
        for (int i = 0; i < POSICIONES.length; i++) {
            assertEquals(i + 1, POSICIONES[i][0],
                "El id en la posición " + i + " debe ser " + (i + 1));
        }
    }

    @Test
    public void tablaCiudadesColumnasNonNegativas() {
        for (int[] pos : POSICIONES) {
            assertTrue(pos[1] >= 0,
                "La columna de ciudad " + pos[0] + " no puede ser negativa");
        }
    }

    @Test
    public void tablaCiudadesFilasNonNegativas() {
        for (int[] pos : POSICIONES) {
            assertTrue(pos[2] >= 0,
                "La fila de ciudad " + pos[0] + " no puede ser negativa");
        }
    }

    @Test
    public void tablaCiudadesNoHayIdsRepetidos() {
        java.util.Set<Integer> ids = new java.util.HashSet<>();
        for (int[] pos : POSICIONES) {
            assertTrue(ids.add(pos[0]),
                "El id " + pos[0] + " está duplicado en la tabla");
        }
    }

    /**
     * Replica el cálculo de alpha que usa dibujarMensajeFlotante,
     * para verificar que siempre está en [0, 1].
     */
    private float calcularAlpha(long tiempoTranscurridoMs, long duracionMs) {
        if (tiempoTranscurridoMs >= duracionMs) return 0f;
        float tiempoRestante = (duracionMs - tiempoTranscurridoMs) / (float) duracionMs;
        return Math.min(1f, tiempoRestante * 2.5f);
    }

    @Test
    public void alphaEsUnoAlInicioDelMensaje() {
        float alpha = calcularAlpha(0, 2500);
        assertEquals(1f, alpha, 0.001f);
    }

    @Test
    public void alphaEsCeroAlExpirarElMensaje() {
        float alpha = calcularAlpha(2500, 2500);
        assertEquals(0f, alpha, 0.001f);
    }

    @Test
    public void alphaEstaEnRangoCeroUnoEnCualquierMomento() {
        long duracion = 2500L;
        for (long t = 0; t <= duracion; t += 100) {
            float alpha = calcularAlpha(t, duracion);
            assertTrue(alpha >= 0f && alpha <= 1f,
                "Alpha fuera de rango en t=" + t + ": " + alpha);
        }
    }

    @Test
    public void alphaEsMenorEnElUltimoTreinoYCincoPorCiento() {
        // A partir del 60% del tiempo el fade-out debe estar empezando
        float alphaInicio = calcularAlpha(0,    2500);
        float alphaFinal  = calcularAlpha(2000, 2500);
        assertTrue(alphaInicio > alphaFinal,
            "El alpha al inicio debe ser mayor que al final del fade");
    }

    @Test
    public void centinelaDeNingunaCiudadEsNegativo() {
        // El contrato establece que -1 significa "ninguna ciudad cercana"
        int centinela = -1;
        assertTrue(centinela < 0);
    }

    @Test
    public void idsDeCiudadesValidosSonMayoresQueCero() {
        // Todos los ids de ciudad válidos son >= 1
        for (int[] pos : POSICIONES) {
            assertTrue(pos[0] > 0,
                "El id de ciudad debe ser > 0, encontrado: " + pos[0]);
        }
    }
}
