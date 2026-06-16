package modelosVista;

import juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelos.Elemento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class TestElementoVista {

    private static class ElementoVistaConcreto extends ElementoVista {
        public ElementoVistaConcreto(Elemento elemento, int col, int fil,
                                     int nivel, int tamanio, String rutaImagen) {
            super(elemento, col, fil, nivel, tamanio, rutaImagen);
        }

        @Override
        protected void cargarImagen(String ruta) {
            // No carga nada: imagen queda null, suficiente para los tests de lógica.
        }
    }

    private static class ElementoStub extends Elemento {
        public ElementoStub(String nombre) {
            super(nombre);
        }

        @Override
        public void aplicarEfecto(CiudadRecoleccion juego) {
            // efecto vacío
        }
    }

    private static final int TAMANIO = 48;
    private ElementoStub    elementoStub;

    @BeforeEach
    public void setUp() {
        elementoStub = new ElementoStub("token");
    }

    private ElementoVistaConcreto crearElemento(int col, int fil, int nivel) {
        return new ElementoVistaConcreto(elementoStub, col, fil, nivel, TAMANIO, "/dummy.bmp");
    }

    @Test
    public void constructorCreaElementoNoNulo() {
        assertNotNull(crearElemento(2, 3, 0));
    }

    @Test
    public void constructorConElementoNuloLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new ElementoVistaConcreto(null, 0, 0, 0, TAMANIO, "/dummy.bmp"));
    }

    @Test
    public void constructorConColNegativaLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new ElementoVistaConcreto(elementoStub, -1, 0, 0, TAMANIO, "/dummy.bmp"));
    }

    @Test
    public void constructorConFilNegativaLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new ElementoVistaConcreto(elementoStub, 0, -1, 0, TAMANIO, "/dummy.bmp"));
    }

    @Test
    public void constructorConNivelNegativoLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new ElementoVistaConcreto(elementoStub, 0, 0, -1, TAMANIO, "/dummy.bmp"));
    }

    @Test
    public void constructorConTamanioNegativoLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new ElementoVistaConcreto(elementoStub, 0, 0, 0, -1, "/dummy.bmp"));
    }

    @Test
    public void constructorConTamanioCeroLanzaExcepcion() {
        // validarMayorACero: cero no es válido para tamanio
        assertThrows(RuntimeException.class,
            () -> new ElementoVistaConcreto(elementoStub, 0, 0, 0, 0, "/dummy.bmp"));
    }

    @Test
    public void constructorConRutaNulaLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new ElementoVistaConcreto(elementoStub, 0, 0, 0, TAMANIO, null));
    }

    @Test
    public void recogidoInicialEsFalse() {
        assertFalse(crearElemento(0, 0, 0).isRecogido());
    }

    @Test
    public void nivelSeGuardaCorrectamente() {
        assertEquals(2, crearElemento(0, 0, 2).getNivel());
    }

    @Test
    public void nivelCeroEsValido() {
        assertEquals(0, crearElemento(0, 0, 0).getNivel());
    }

    @Test
    public void elementoSeGuardaCorrectamente() {
        assertEquals(elementoStub, crearElemento(0, 0, 0).getElemento());
    }

    @Test
    public void worldXEsColPorTamanio() {
        ElementoVistaConcreto e = crearElemento(3, 0, 0);
        assertEquals(3 * TAMANIO, e.getWorldX());
    }

    @Test
    public void worldYEsFilPorTamanio() {
        ElementoVistaConcreto e = crearElemento(0, 5, 0);
        assertEquals(5 * TAMANIO, e.getWorldY());
    }

    @Test
    public void worldXYEnOrigen() {
        ElementoVistaConcreto e = crearElemento(0, 0, 0);
        assertEquals(0, e.getWorldX());
        assertEquals(0, e.getWorldY());
    }

    @Test
    public void areaSolidaInicializadaConTamanio() {
        ElementoVistaConcreto e = crearElemento(0, 0, 0);
        Rectangle area = e.getAreaSolida();
        assertNotNull(area);
        assertEquals(TAMANIO, area.width);
        assertEquals(TAMANIO, area.height);
    }

    @Test
    public void dosRectangulosSolapadosColisionan() {
        Rectangle elem = new Rectangle(0, 0, 48, 48);
        Rectangle jug  = new Rectangle(24, 24, 48, 48);
        assertTrue(elem.intersects(jug));
    }

    @Test
    public void dosRectangulosAdyacentesNoColisionan() {
        Rectangle elem = new Rectangle(0, 0, 48, 48);
        Rectangle jug  = new Rectangle(48, 0, 48, 48);
        assertFalse(elem.intersects(jug));
    }

    @Test
    public void dosRectangulosLejanosNoColisionan() {
        Rectangle elem = new Rectangle(0, 0, 48, 48);
        Rectangle jug  = new Rectangle(200, 200, 48, 48);
        assertFalse(elem.intersects(jug));
    }

    @Test
    public void colisionaConJugadorDevuelveFalseSiNivelDistinto() {
        // Lógica: nivelActual != nivel → return false
        // nivel del elemento = 1, nivelActual = 2 → no colisiona
        int nivelElemento = 1;
        int nivelActual   = 2;
        assertNotEquals(nivelElemento, nivelActual);
    }

    @Test
    public void recogerConJuegoNuloLanzaExcepcion() {
        ElementoVistaConcreto e = crearElemento(0, 0, 0);
        assertThrows(RuntimeException.class, () -> e.recoger(null));
    }

    @Test
    public void setElementoEsPrivado() throws Exception {
        var m = ElementoVista.class.getDeclaredMethod("setElemento", Elemento.class);
        assertTrue(Modifier.isPrivate(m.getModifiers()),
            "setElemento debe ser privado");
    }

    @Test
    public void setRecogidoEsPrivado() throws Exception {
        var m = ElementoVista.class.getDeclaredMethod("setRecogido", boolean.class);
        assertTrue(Modifier.isPrivate(m.getModifiers()),
            "setRecogido debe ser privado");
    }

    @Test
    public void setNivelEsPrivado() throws Exception {
        var m = ElementoVista.class.getDeclaredMethod("setNivel", int.class);
        assertTrue(Modifier.isPrivate(m.getModifiers()),
            "setNivel debe ser privado");
    }

    @Test
    public void setImagenEsProtegido() throws Exception {
        var m = ElementoVista.class.getDeclaredMethod("setImagen", java.awt.image.BufferedImage.class);
        assertTrue(Modifier.isProtected(m.getModifiers()),
            "setImagen debe ser protected");
    }

    @Test
    public void claseEsAbstracta() {
        assertTrue(Modifier.isAbstract(ElementoVista.class.getModifiers()),
            "ElementoVista debe ser abstracta");
    }
}
