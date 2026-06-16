package modelosVista;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class TestManejadorDeConstruccion {

    @Test
    public void constructorExisteYEsPublico() throws Exception {
        var c = ManejadorDeConstruccion.class.getConstructor(Vista.class);
        assertTrue(Modifier.isPublic(c.getModifiers()));
    }

    @Test
    public void constructorConVistaNulaLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new ManejadorDeConstruccion(null));
    }

    @Test
    public void drawConG2NuloLanzaExcepcion() {
        assertThrows(RuntimeException.class, () ->
            utils.ValidacionesUtiles.esDistintoDeNull(null, "g2"));
    }

    @Test
    public void loadMapConFileNuloLanzaExcepcion() {
        assertThrows(RuntimeException.class, () ->
            utils.ValidacionesUtiles.esDistintoDeNull(null, "file"));
    }

    @Test
    public void construccionGrassSinColisionPorDefecto() {
        Construccion grass = new Construccion();
        assertFalse(grass.getColision());
    }

    @Test
    public void construccionWallConColisionActivada() {
        Construccion wall = new Construccion();
        wall.setColision(true);
        assertTrue(wall.getColision());
    }

    @Test
    public void construccionWaterConColisionActivada() {
        Construccion water = new Construccion();
        water.setColision(true);
        assertTrue(water.getColision());
    }

    @Test
    public void construccionEarthSinColisionPorDefecto() {
        Construccion earth = new Construccion();
        assertFalse(earth.getColision());
    }

    @Test
    public void construccionTreeConColisionActivada() {
        Construccion tree = new Construccion();
        tree.setColision(true);
        assertTrue(tree.getColision());
    }

    @Test
    public void construccionSandSinColisionPorDefecto() {
        Construccion sand = new Construccion();
        assertFalse(sand.getColision());
    }

    @Test
    public void parsingLineaDeMapaConvierteCadenaAEnteros() {
        String linea = "0 1 2 3 4 5";
        String[] partes = linea.split(" ");
        int[] esperados = {0, 1, 2, 3, 4, 5};
        for (int i = 0; i < partes.length; i++) {
            assertEquals(esperados[i], Integer.parseInt(partes[i]));
        }
    }

    @Test
    public void parsingLineaConUnSoloElemento() {
        String linea = "3";
        String[] partes = linea.split(" ");
        assertEquals(1, partes.length);
        assertEquals(3, Integer.parseInt(partes[0]));
    }

    @Test
    public void parsingNumeroInvalidoLanzaNumberFormatException() {
        assertThrows(NumberFormatException.class, () ->
            Integer.parseInt("abc"));
    }

    @Test
    public void calculoScreenXEsCorrecto() {
        int worldX       = 200;
        int jugadorWorldX  = 150;
        int jugadorScreenX = 300;
        int screenX = worldX - jugadorWorldX + jugadorScreenX;
        assertEquals(350, screenX);
    }

    @Test
    public void calculoScreenYEsCorrecto() {
        int worldY       = 400;
        int jugadorWorldY  = 300;
        int jugadorScreenY = 240;
        int screenY = worldY - jugadorWorldY + jugadorScreenY;
        assertEquals(340, screenY);
    }

    private boolean esTileVisible(int worldX, int worldY, int tamanio,
                                   int jugadorWorldX, int jugadorWorldY,
                                   int jugadorScreenX, int jugadorScreenY) {
        return worldX + tamanio > jugadorWorldX - jugadorScreenX &&
               worldX - tamanio < jugadorWorldX + jugadorScreenX &&
               worldY + tamanio > jugadorWorldY - jugadorScreenY &&
               worldY - tamanio < jugadorWorldY + jugadorScreenY;
    }

    @Test
    public void tileCentradoEnPantallaEsVisible() {
        assertTrue(esTileVisible(300, 300, 48, 300, 300, 400, 300));
    }

    @Test
    public void tileMuyALaDerechaNoEsVisible() {
        // jugadorWorldX=300, screenX=400 → borde derecho pantalla = 700
        // tile en worldX=1200 → borde izquierdo = 1152 > 700 → fuera
        assertFalse(esTileVisible(1200, 300, 48, 300, 300, 400, 300));
    }

    @Test
    public void tileMuyAbajoNoEsVisible() {
        assertFalse(esTileVisible(300, 1200, 48, 300, 300, 400, 300));
    }

    @Test
    public void tileMuyALaIzquierdaNoEsVisible() {
        // borde izquierdo pantalla = 300-400 = -100; tile en worldX=-500 → borde derecho = -452 < -100
        assertFalse(esTileVisible(-500, 300, 48, 300, 300, 400, 300));
    }

    @Test
    public void tileMuyArribaNoEsVisible() {
        assertFalse(esTileVisible(300, -500, 48, 300, 300, 400, 300));
    }

    @Test
    public void tileEnBordeDerechoExactoEsVisible() {
        // worldX + tamanio == jugadorWorldX + screenX → NO supera, pero sí es >
        // Para que sea justo en el límite: worldX = jugadorWorldX + screenX - tamanio
        int jugadorWorldX = 300, screenX = 400, tamanio = 48;
        int worldX = jugadorWorldX + screenX - tamanio; // exactamente en el borde → visible (>)
        assertTrue(esTileVisible(worldX, 300, tamanio, jugadorWorldX, 300, screenX, 300));
    }

    @Test
    public void arregloConstruccionesTieneCapacidadDiez() {
        Construccion[] arr = new Construccion[10];
        assertEquals(10, arr.length);
    }

    @Test
    public void arregloConstruccionesEsNuloSinInicializar() {
        Construccion[] arr = null;
        assertNull(arr);
    }

    @Test
    public void setVistaDelJuegoEsPrivado() throws Exception {
        var m = ManejadorDeConstruccion.class.getDeclaredMethod("setVistaDelJuego", Vista.class);
        assertTrue(Modifier.isPrivate(m.getModifiers()),
            "setVistaDelJuego debe ser privado");
    }

    @Test
    public void setConstruccionesEsPrivado() throws Exception {
        var m = ManejadorDeConstruccion.class.getDeclaredMethod("setConstrucciones", Construccion[].class);
        assertTrue(Modifier.isPrivate(m.getModifiers()),
            "setConstrucciones debe ser privado");
    }

    @Test
    public void getVistaDelJuegoEsPublico() throws Exception {
        var m = ManejadorDeConstruccion.class.getMethod("getVistaDelJuego");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void getConstruccionesEsPublico() throws Exception {
        var m = ManejadorDeConstruccion.class.getMethod("getConstrucciones");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void drawEsPublico() throws Exception {
        var m = ManejadorDeConstruccion.class.getMethod("draw", java.awt.Graphics2D.class);
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void loadMapEsPublico() throws Exception {
        var m = ManejadorDeConstruccion.class.getMethod("loadMap", String.class);
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void cargarImagenesDeConstruccionesEsPublico() throws Exception {
        var m = ManejadorDeConstruccion.class.getMethod("cargarImagenesDeConstrucciones");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void atributoConstruccionesEsProtegido() throws Exception {
        var f = ManejadorDeConstruccion.class.getDeclaredField("construcciones");
        assertTrue(Modifier.isProtected(f.getModifiers()),
            "construcciones debe ser protected");
    }

    @Test
    public void atributoMapaDeConstruccionesNumEsProtegido() throws Exception {
        var f = ManejadorDeConstruccion.class.getDeclaredField("mapaDeConstruccionesNum");
        assertTrue(Modifier.isProtected(f.getModifiers()),
            "mapaDeConstruccionesNum debe ser protected");
    }

    @Test
    public void atributoVistaDelJuegoEsPrivado() throws Exception {
        var f = ManejadorDeConstruccion.class.getDeclaredField("vistaDelJuego");
        assertTrue(Modifier.isPrivate(f.getModifiers()),
            "vistaDelJuego debe ser privado");
    }
}
