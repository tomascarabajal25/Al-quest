package modelosVista;

import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class TestChequeadorDeColision {

    @Test
    public void constructorConGpNuloLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new ChequeadorDeColision(null));
    }

    @Test
    public void constructorPublicoExisteYRecibeVista() {
        try {
            var constructor = ChequeadorDeColision.class.getConstructor(Vista.class);
            assertNotNull(constructor);
        } catch (NoSuchMethodException e) {
            fail("ChequeadorDeColision debe tener un constructor público que reciba Vista");
        }
    }

    @Test
    public void metodoGetGpEsPublicoYDevuelveVista() throws Exception {
        var metodo = ChequeadorDeColision.class.getMethod("getGp");
        assertNotNull(metodo);
        assertEquals(Vista.class, metodo.getReturnType());
    }

    @Test
    public void metodoSetGpEsPrivado() throws Exception {
        var metodo = ChequeadorDeColision.class.getDeclaredMethod("setGp", Vista.class);
        assertTrue(Modifier.isPrivate(metodo.getModifiers()),
            "setGp debe ser privado para respetar el encapsulamiento");
    }

    @Test
    public void atributoGpEsPrivado() throws Exception {
        var campo = ChequeadorDeColision.class.getDeclaredField("gp");
        assertTrue(Modifier.isPrivate(campo.getModifiers()),
            "El atributo gp debe ser privado");
    }

    @Test
    public void metodoChequearConstruccionEsPublico() throws Exception {
        var metodo = ChequeadorDeColision.class.getMethod("chequearConstruccion", EntidadVista.class);
        assertTrue(Modifier.isPublic(metodo.getModifiers()));
    }

    private Rectangle calcularRectanguloFuturo(int worldX, int worldY,
                                                Direccion direccion,
                                                int velocidad, int tamanio) {
        int futuroX = worldX;
        int futuroY = worldY;

        switch (direccion) {
            case ARRIBA    -> futuroY -= velocidad;
            case ABAJO     -> futuroY += velocidad;
            case IZQUIERDA -> futuroX -= velocidad;
            case DERECHA   -> futuroX += velocidad;
        }

        return new Rectangle(futuroX, futuroY, tamanio, tamanio);
    }

    @Test
    public void rectanguloFuturoMoverseArribaDecrementaY() {
        Rectangle r = calcularRectanguloFuturo(100, 100, Direccion.ARRIBA, 4, 48);
        assertEquals(96, r.y);
        assertEquals(100, r.x);
    }

    @Test
    public void rectanguloFuturoMoverseAbajoIncrementaY() {
        Rectangle r = calcularRectanguloFuturo(100, 100, Direccion.ABAJO, 4, 48);
        assertEquals(104, r.y);
        assertEquals(100, r.x);
    }

    @Test
    public void rectanguloFuturoMoverseIzquierdaDecrementaX() {
        Rectangle r = calcularRectanguloFuturo(100, 100, Direccion.IZQUIERDA, 4, 48);
        assertEquals(96, r.x);
        assertEquals(100, r.y);
    }

    @Test
    public void rectanguloFuturoMoverseDerechaIncrementaX() {
        Rectangle r = calcularRectanguloFuturo(100, 100, Direccion.DERECHA, 4, 48);
        assertEquals(104, r.x);
        assertEquals(100, r.y);
    }

    @Test
    public void rectanguloFuturoTieneTamanioDeUnTile() {
        Rectangle r = calcularRectanguloFuturo(0, 0, Direccion.ABAJO, 4, 48);
        assertEquals(48, r.width);
        assertEquals(48, r.height);
    }

    @Test
    public void dosRectangulosSolapados_intersectan() {
        Rectangle a = new Rectangle(0, 0, 48, 48);
        Rectangle b = new Rectangle(24, 24, 48, 48);
        assertTrue(a.intersects(b));
    }

    @Test
    public void dosRectangulosAdyacentes_noIntersectan() {
        Rectangle a = new Rectangle(0, 0, 48, 48);
        Rectangle b = new Rectangle(48, 0, 48, 48);
        assertFalse(a.intersects(b));
    }

    @Test
    public void dosRectangulosLejos_noIntersectan() {
        Rectangle a = new Rectangle(0, 0, 48, 48);
        Rectangle b = new Rectangle(200, 200, 48, 48);
        assertFalse(a.intersects(b));
    }

    @Test
    public void rectanguloContenidoDentroDeOtro_intersecta() {
        Rectangle grande = new Rectangle(0, 0, 96, 96);
        Rectangle chico  = new Rectangle(24, 24, 10, 10);
        assertTrue(grande.intersects(chico));
    }

    @Test
    public void interseccionEsSimetrica() {
        Rectangle a = new Rectangle(0, 0, 48, 48);
        Rectangle b = new Rectangle(30, 30, 48, 48);
        assertEquals(a.intersects(b), b.intersects(a));
    }

    @Test
    public void coordenadaDeTileCalculaCorrectamenteConTamanio48() {
        int tamanio = 48;
        // worldX = 96 → col = 96 / 48 = 2
        assertEquals(2, 96 / tamanio);
    }

    @Test
    public void coordenadaDeTileEnBordeDeUnTileEsExacta() {
        int tamanio = 48;
        // worldX = 48 → col = 48 / 48 = 1 (borde exacto del tile 1)
        assertEquals(1, 48 / tamanio);
    }

    @Test
    public void coordenadaDeTileEnCeroEsCero() {
        int tamanio = 48;
        assertEquals(0, 0 / tamanio);
    }

    @Test
    public void coordenadaDeTileConDesplazamientoDeVelocidad() {
        int tamanio   = 48;
        int worldY    = 96;
        int velocidad = 4;
        // Simulación de ARRIBA: (worldY - velocidad) / tamanio
        int fila = (worldY - velocidad) / tamanio;
        assertEquals(1, fila); // (96 - 4) / 48 = 92 / 48 = 1
    }

    @Test
    public void coordenadaDeTileAbajaSumaVelocidad() {
        int tamanio   = 48;
        int worldY    = 92;
        int velocidad = 4;
        // Simulación de ABAJO: (worldY + velocidad) / tamanio
        int fila = (worldY + velocidad) / tamanio;
        assertEquals(2, fila); // (92 + 4) / 48 = 96 / 48 = 2
    }
}
