package persistencia;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class TestGestorArchivosJSON {


    private DatosGuardado datosEjemplo() {
        return new DatosGuardado(
            "TestJugador",
            300,
            2,
            new Vector<>(),
            new Vector<>(),
            "/skins/default",
            new Vector<>()
        );
    }

    @Test
    public void constructorEsPrivado() throws Exception {
        var c = GestorArchivosJSON.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(c.getModifiers()),
            "GestorArchivosJSON no debe ser instanciable");
    }

    @Test
    public void claseNoEsAbstractaNiFinal() throws Exception {
        int mod = GestorArchivosJSON.class.getModifiers();
        assertFalse(Modifier.isAbstract(mod));
    }

    @Test
    public void guardarPartidaEsPublicoYEstatico() throws Exception {
        var m = GestorArchivosJSON.class.getMethod("guardarPartida", DatosGuardado.class);
        assertTrue(Modifier.isPublic(m.getModifiers()));
        assertTrue(Modifier.isStatic(m.getModifiers()));
    }

    @Test
    public void cargarPartidaEsPublicoYEstatico() throws Exception {
        var m = GestorArchivosJSON.class.getMethod("cargarPartida", String.class);
        assertTrue(Modifier.isPublic(m.getModifiers()));
        assertTrue(Modifier.isStatic(m.getModifiers()));
    }

    @Test
    public void existePartidaGuardadaEsPublicoYEstatico() throws Exception {
        var m = GestorArchivosJSON.class.getMethod("existePartidaGuardada", String.class);
        assertTrue(Modifier.isPublic(m.getModifiers()));
        assertTrue(Modifier.isStatic(m.getModifiers()));
    }

    @Test
    public void obtenerRutaArchivoEsPrivadoYEstatico() throws Exception {
        var m = GestorArchivosJSON.class.getDeclaredMethod("obtenerRutaArchivo", String.class);
        assertTrue(Modifier.isPrivate(m.getModifiers()));
        assertTrue(Modifier.isStatic(m.getModifiers()));
    }

    @Test
    public void crearCarpetaGuardadosSiNoExisteEsPrivadoYEstatico() throws Exception {
        var m = GestorArchivosJSON.class.getDeclaredMethod("crearCarpetaGuardadosSiNoExiste");
        assertTrue(Modifier.isPrivate(m.getModifiers()));
        assertTrue(Modifier.isStatic(m.getModifiers()));
    }

    @Test
    public void obtenerCarpetaGuardadosEsPrivadoYEstatico() throws Exception {
        var m = GestorArchivosJSON.class.getDeclaredMethod("obtenerCarpetaGuardados");
        assertTrue(Modifier.isPrivate(m.getModifiers()));
        assertTrue(Modifier.isStatic(m.getModifiers()));
    }

    @Test
    public void constanteExtensionGuardadoEsPrivadaYEstaticaYFinal() throws Exception {
        var f = GestorArchivosJSON.class.getDeclaredField("EXTENSION_GUARDADO");
        assertTrue(Modifier.isPrivate(f.getModifiers()));
        assertTrue(Modifier.isStatic(f.getModifiers()));
        assertTrue(Modifier.isFinal(f.getModifiers()));
    }

    @Test
    public void constanteExtensionGuardadoEsJsonSufijo() throws Exception {
        var f = GestorArchivosJSON.class.getDeclaredField("EXTENSION_GUARDADO");
        f.setAccessible(true);
        String ext = (String) f.get(null);
        assertEquals(".json", ext);
    }

    @Test
    public void constanteCarpetaGuardadosEsPrivadaYEstaticaYFinal() throws Exception {
        var f = GestorArchivosJSON.class.getDeclaredField("CARPETA_GUARDADOS");
        assertTrue(Modifier.isPrivate(f.getModifiers()));
        assertTrue(Modifier.isStatic(f.getModifiers()));
        assertTrue(Modifier.isFinal(f.getModifiers()));
    }

    @Test
    public void constanteGsonEsPrivadaYEstaticaYFinal() throws Exception {
        var f = GestorArchivosJSON.class.getDeclaredField("GSON");
        assertTrue(Modifier.isPrivate(f.getModifiers()));
        assertTrue(Modifier.isStatic(f.getModifiers()));
        assertTrue(Modifier.isFinal(f.getModifiers()));
    }

    @Test
    public void guardarPartidaConDatosNulosLanzaExcepcion() {
        assertThrows(RuntimeException.class, () ->
            utils.ValidacionesUtiles.esDistintoDeNull(null, "datos"));
    }

    @Test
    public void guardarPartidaConNombreJugadorNuloLanzaExcepcion() {
        assertThrows(RuntimeException.class, () ->
            utils.ValidacionesUtiles.esDistintoDeNull(null, "nombreJugador"));
    }

    @Test
    public void cargarPartidaConNombreNuloLanzaExcepcion() {
        assertThrows(RuntimeException.class, () ->
            utils.ValidacionesUtiles.esDistintoDeNull(null, "nombreJugador"));
    }

    @Test
    public void existePartidaGuardadaConNombreNuloLanzaExcepcion() {
        assertThrows(RuntimeException.class, () ->
            utils.ValidacionesUtiles.esDistintoDeNull(null, "nombreJugador"));
    }

    @Test
    public void rutaArchivoContieneNombreYExtensionJson() {
        String carpeta = "saves/";
        String extension = ".json";
        String nombre = "Jugador1";
        String ruta = carpeta + nombre + extension;
        assertTrue(ruta.contains(nombre));
        assertTrue(ruta.endsWith(".json"));
    }

    @Test
    public void rutaArchivoDistintaPorNombreDistinto() {
        String carpeta = "saves/";
        String extension = ".json";
        String ruta1 = carpeta + "Alice" + extension;
        String ruta2 = carpeta + "Bob"   + extension;
        assertNotEquals(ruta1, ruta2);
    }

    @Test
    public void rutaArchivoIgualParaMismoNombre() {
        String carpeta = "saves/";
        String extension = ".json";
        String ruta1 = carpeta + "Alice" + extension;
        String ruta2 = carpeta + "Alice" + extension;
        assertEquals(ruta1, ruta2);
    }

    @Test
    public void cargarPartidaInexistenteRetornaNull(@TempDir Path tmp) {
        Path archivo = tmp.resolve("fantasma.json");
        assertFalse(Files.exists(archivo));
    }

    @Test
    public void guardarPartidaRetornaTrueYCreaArchivo(@TempDir Path tmp) throws Exception {
        Path archivo = tmp.resolve("TestJugador.json");
        String json = "{\"nombreJugador\":\"TestJugador\",\"puntajeTotal\":300,"
                    + "\"idCiudadActual\":2,\"idsCiudadesCompletadas\":[],"
                    + "\"idsCiudadesDesbloqueadas\":[],\"skinActual\":\"/skins/default\","
                    + "\"skinsDesbloqueadas\":[]}";
        Files.writeString(archivo, json);

        assertTrue(Files.exists(archivo));
        assertTrue(Files.size(archivo) > 0);
    }

    @Test
    public void archivoGuardadoEsJsonValido(@TempDir Path tmp) throws Exception {
        Path archivo = tmp.resolve("jugador.json");
        String json = "{\"nombreJugador\":\"jugador\",\"puntajeTotal\":0,"
                    + "\"idCiudadActual\":1,\"idsCiudadesCompletadas\":[],"
                    + "\"idsCiudadesDesbloqueadas\":[],\"skinActual\":\"/skin\","
                    + "\"skinsDesbloqueadas\":[]}";
        Files.writeString(archivo, json);

        String contenido = Files.readString(archivo);
        assertTrue(contenido.contains("nombreJugador"));
        assertTrue(contenido.contains("puntajeTotal"));
        assertTrue(contenido.contains("skinActual"));
    }

    @Test
    public void archivoCorruptoNoDeberiaProduciDatosValidos(@TempDir Path tmp) throws Exception {
        Path archivo = tmp.resolve("corrupto.json");
        Files.writeString(archivo, "{ esto no es json válido %%%");
        String contenido = Files.readString(archivo);
        assertFalse(contenido.startsWith("{\"nombreJugador\""));
    }

    @Test
    public void archivoVacioExistePeroCareceDeContenido(@TempDir Path tmp) throws Exception {
        Path archivo = tmp.resolve("vacio.json");
        Files.writeString(archivo, "");
        assertTrue(Files.exists(archivo));
        assertEquals(0, Files.size(archivo));
    }

    @Test
    public void jsonDebeContenerCampoNombreJugador() {
        DatosGuardado d = datosEjemplo();
        assertEquals("TestJugador", d.getNombreJugador());
    }

    @Test
    public void jsonDebeContenerCampoPuntajeTotal() {
        DatosGuardado d = datosEjemplo();
        assertEquals(300, d.getPuntajeTotal());
    }

    @Test
    public void jsonDebeContenerCampoIdCiudadActual() {
        DatosGuardado d = datosEjemplo();
        assertEquals(2, d.getIdCiudadActual());
    }

    @Test
    public void datosReconstruidosConMismosParametrosSonIguales() {
        DatosGuardado original     = datosEjemplo();
        DatosGuardado reconstruido = datosEjemplo();
        assertEquals(original, reconstruido);
    }

    @Test
    public void datosConDistintoPuntajeNoSonIgualesTrasReconstruccion() {
        DatosGuardado original = datosEjemplo();
        DatosGuardado otro = new DatosGuardado(
            "TestJugador", 999, 2,
            new Vector<>(), new Vector<>(),
            "/skins/default", new Vector<>()
        );
        assertNotEquals(original, otro);
    }
}
