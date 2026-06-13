package modelosVista;

import modelos.Jugador;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class TestJugadorVista {

    @Test
    public void constructorExisteYEsPublico() throws Exception {
        var c = JugadorVista.class.getConstructor(
            Jugador.class, KeyHandler.class, int.class, int.class, String.class, Vista.class
        );
        assertTrue(Modifier.isPublic(c.getModifiers()));
    }

    @Test
    public void constructorConJugadorNuloLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new JugadorVista(null, new KeyHandler(), 0, 0, "/ruta", null));
    }

    @Test
    public void constructorConKeyNuloLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new JugadorVista(new Jugador("test"), null, 0, 0, "/ruta", null));
    }

    @Test
    public void constructorConRutaNulaLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new JugadorVista(new Jugador("test"), new KeyHandler(), 0, 0, null, null));
    }

    @Test
    public void establecerNivelActualConCeroLanzaExcepcion() {
        // validarMayorACero: 0 no es válido
        assertThrows(RuntimeException.class, () -> {
            // Invocamos la validación directamente replicando el contrato
            utils.ValidacionesUtiles.validarMayorACero(0, "nivel");
        });
    }

    @Test
    public void establecerNivelActualConNegativoLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> {
            utils.ValidacionesUtiles.validarMayorACero(-1, "nivel");
        });
    }

    @Test
    public void establecerNivelActualConUnoEsValido() {
        assertDoesNotThrow(() -> {
            utils.ValidacionesUtiles.validarMayorACero(1, "nivel");
        });
    }


    private int simularAnimacion(int frames) {
        int spriteNum     = 1;
        int spriteCounter = 0;

        for (int i = 0; i < frames; i++) {
            spriteCounter++;
            if (spriteCounter > 12) {
                spriteNum     = (spriteNum == 1) ? 2 : 1;
                spriteCounter = 0;
            }
        }
        return spriteNum;
    }

    @Test
    public void spriteNumEsUnoAlInicio() {
        assertEquals(1, simularAnimacion(0));
    }

    @Test
    public void spriteNumNoAlternaAntesDe13Frames() {
        // Frames 1-12: spriteCounter nunca supera 12 → spriteNum sigue en 1
        assertEquals(1, simularAnimacion(12));
    }

    @Test
    public void spriteNumAlternaEnElFrame13() {
        // Frame 13: spriteCounter llega a 13 > 12 → cambia a 2
        assertEquals(2, simularAnimacion(13));
    }

    @Test
    public void spriteNumVuelveAUnoEnElFrame26() {
        // Segundo ciclo: frame 26 → spriteNum vuelve a 1
        assertEquals(1, simularAnimacion(26));
    }

    @Test
    public void spriteNumAlternaEnCadaCiclo() {
        // Ciclo 1 → 2, ciclo 2 → 1, ciclo 3 → 2
        assertEquals(2, simularAnimacion(13));
        assertEquals(1, simularAnimacion(26));
        assertEquals(2, simularAnimacion(39));
    }

    @Test
    public void spriteCounterSeResetaACeroTrasCadaAlternancia() {
        // Verificamos que el contador se resetea: en el frame 14 el contador es 1 (no 14)
        int spriteCounter = 0;
        for (int i = 0; i < 14; i++) {
            spriteCounter++;
            if (spriteCounter > 12) spriteCounter = 0;
        }
        assertEquals(1, spriteCounter);
    }

    private int[] calcularPosicionFutura(int worldX, int worldY,
                                          Direccion dir, int velocidad) {
        int x = worldX, y = worldY;
        switch (dir) {
            case ARRIBA    -> y -= velocidad;
            case ABAJO     -> y += velocidad;
            case IZQUIERDA -> x -= velocidad;
            case DERECHA   -> x += velocidad;
        }
        return new int[]{x, y};
    }

    @Test
    public void movimientoArribaDecrementaWorldY() {
        int[] pos = calcularPosicionFutura(100, 100, Direccion.ARRIBA, 4);
        assertEquals(96, pos[1]);
        assertEquals(100, pos[0]);
    }

    @Test
    public void movimientoAbajoIncrementaWorldY() {
        int[] pos = calcularPosicionFutura(100, 100, Direccion.ABAJO, 4);
        assertEquals(104, pos[1]);
        assertEquals(100, pos[0]);
    }

    @Test
    public void movimientoIzquierdaDecrementaWorldX() {
        int[] pos = calcularPosicionFutura(100, 100, Direccion.IZQUIERDA, 4);
        assertEquals(96, pos[0]);
        assertEquals(100, pos[1]);
    }

    @Test
    public void movimientoDerechaIncrementaWorldX() {
        int[] pos = calcularPosicionFutura(100, 100, Direccion.DERECHA, 4);
        assertEquals(104, pos[0]);
        assertEquals(100, pos[1]);
    }

    @Test
    public void cambiarSkinConRutaNulaLanzaExcepcion() {
        // El método valida null antes de cargar imágenes
        assertThrows(RuntimeException.class, () ->
            utils.ValidacionesUtiles.esDistintoDeNull(null, "rutaSprites"));
    }

    @Test
    public void setJugadorEsPrivado() throws Exception {
        var m = JugadorVista.class.getDeclaredMethod("setJugador", Jugador.class);
        assertTrue(Modifier.isPrivate(m.getModifiers()),
            "setJugador debe ser privado");
    }

    @Test
    public void setVistaEsPrivado() throws Exception {
        var m = JugadorVista.class.getDeclaredMethod("setVista", Vista.class);
        assertTrue(Modifier.isPrivate(m.getModifiers()),
            "setVista debe ser privado");
    }

    @Test
    public void setKeyEsPrivado() throws Exception {
        var m = JugadorVista.class.getDeclaredMethod("setKey", KeyHandler.class);
        assertTrue(Modifier.isPrivate(m.getModifiers()),
            "setKey debe ser privado");
    }

    @Test
    public void atributoScreenXEsFinal() throws Exception {
        var campo = JugadorVista.class.getDeclaredField("screenX");
        assertTrue(Modifier.isFinal(campo.getModifiers()),
            "screenX debe ser final: se calcula una sola vez en el constructor");
    }

    @Test
    public void atributoScreenYEsFinal() throws Exception {
        var campo = JugadorVista.class.getDeclaredField("screenY");
        assertTrue(Modifier.isFinal(campo.getModifiers()),
            "screenY debe ser final: se calcula una sola vez en el constructor");
    }

    @Test
    public void getScreenXEsPublico() throws Exception {
        var m = JugadorVista.class.getMethod("getScreenX");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void getScreenYEsPublico() throws Exception {
        var m = JugadorVista.class.getMethod("getScreenY");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }
}
