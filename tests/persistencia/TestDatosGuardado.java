package persistencia;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

public class TestDatosGuardado {

    private DatosGuardado datosEjemplo() {
        return new DatosGuardado(
            "Jugador1",
            500,
            3,
            new Vector<>(),
            new Vector<>(),
            "/skins/default",
            new Vector<>(),
            new Vector<>()
        );
    }

    @Test
    public void constructorExisteYEsPublico() throws Exception {
        var c = DatosGuardado.class.getConstructor(
            String.class, int.class, int.class,
            Vector.class, Vector.class,
            String.class, Vector.class,
            Vector.class
        );
        assertTrue(Modifier.isPublic(c.getModifiers()));
    }

    @Test
    public void constructorAsignaNombreJugadorCorrectamente() {
        DatosGuardado d = datosEjemplo();
        assertEquals("Jugador1", d.getNombreJugador());
    }

    @Test
    public void constructorAsignaPuntajeTotalCorrectamente() {
        DatosGuardado d = datosEjemplo();
        assertEquals(500, d.getPuntajeTotal());
    }

    @Test
    public void constructorAsignaIdCiudadActualCorrectamente() {
        DatosGuardado d = datosEjemplo();
        assertEquals(3, d.getIdCiudadActual());
    }

    @Test
    public void constructorAsignaIdsCiudadesCompletadasCorrectamente() {
        Vector<Integer> completadas = new Vector<>();
        completadas.add(1);
        DatosGuardado d = new DatosGuardado(
            "test", 0, 1, completadas, new Vector<>(), "/skin", new Vector<>(),
            new Vector<>()
        );
        assertEquals(completadas, d.getIdsCiudadesCompletadas());
    }

    @Test
    public void constructorAsignaIdsCiudadesDesbloqueadasCorrectamente() {
        Vector<Integer> desbloqueadas = new Vector<>();
        desbloqueadas.add(2);
        DatosGuardado d = new DatosGuardado(
            "test", 0, 1, new Vector<>(), desbloqueadas, "/skin", new Vector<>(),
            new Vector<>()
        );
        assertEquals(desbloqueadas, d.getIdsCiudadesDesbloqueadas());
    }

    @Test
    public void constructorAsignaSkinActualCorrectamente() {
        DatosGuardado d = datosEjemplo();
        assertEquals("/skins/default", d.getSkinActual());
    }

    @Test
    public void constructorAsignaSkinsDesbloqueadasCorrectamente() {
        Vector<String> skins = new Vector<>();
        skins.add("/skins/default");
        skins.add("/skins/blue");
        DatosGuardado d = new DatosGuardado(
            "test", 0, 1, new Vector<>(), new Vector<>(), "/skins/default", skins,
            new Vector<>()
        );
        assertEquals(skins, d.getSkinsDesbloqueadas());
    }

    @Test
    public void constructorAlmacenaVectorPorReferencia() {
        Vector<Integer> completadas = new Vector<>();
        DatosGuardado d = new DatosGuardado(
            "test", 0, 1, completadas, new Vector<>(), "/skin", new Vector<>(),
            new Vector<>()
        );
        completadas.add(99);
        assertTrue(d.getIdsCiudadesCompletadas().contains(99));
    }

    @Test
    public void puntajeCeroEsValido() {
        DatosGuardado d = new DatosGuardado(
            "test", 0, 1, new Vector<>(), new Vector<>(), "/skin", new Vector<>(),
            new Vector<>()
        );
        assertEquals(0, d.getPuntajeTotal());
    }

    @Test
    public void puntajePositivoEsValido() {
        DatosGuardado d = new DatosGuardado(
            "test", 9999, 1, new Vector<>(), new Vector<>(), "/skin", new Vector<>(),
            new Vector<>()
        );
        assertEquals(9999, d.getPuntajeTotal());
    }

    @Test
    public void equalsConMismoObjetoRetornaTrue() {
        DatosGuardado d = datosEjemplo();
        assertEquals(d, d);
    }

    @Test
    public void equalsConObjetoIgualRetornaTrue() {
        DatosGuardado d1 = datosEjemplo();
        DatosGuardado d2 = datosEjemplo();
        assertEquals(d1, d2);
    }

    @Test
    public void equalsConNombreDistintoRetornaFalse() {
        DatosGuardado d1 = datosEjemplo();
        DatosGuardado d2 = new DatosGuardado(
            "Otro", 500, 3, new Vector<>(), new Vector<>(), "/skins/default", new Vector<>(),
            new Vector<>()
        );
        assertNotEquals(d1, d2);
    }

    @Test
    public void equalsConPuntajeDistintoRetornaFalse() {
        DatosGuardado d1 = datosEjemplo();
        DatosGuardado d2 = new DatosGuardado(
            "Jugador1", 999, 3, new Vector<>(), new Vector<>(), "/skins/default", new Vector<>(),
            new Vector<>()
        );
        assertNotEquals(d1, d2);
    }

    @Test
    public void equalsConIdCiudadDistintoRetornaFalse() {
        DatosGuardado d1 = datosEjemplo();
        DatosGuardado d2 = new DatosGuardado(
            "Jugador1", 500, 7, new Vector<>(), new Vector<>(), "/skins/default", new Vector<>(),
            new Vector<>()
        );
        assertNotEquals(d1, d2);
    }

    @Test
    public void equalsConSkinDistintaRetornaFalse() {
        DatosGuardado d1 = datosEjemplo();
        DatosGuardado d2 = new DatosGuardado(
            "Jugador1", 500, 3, new Vector<>(), new Vector<>(), "/skins/blue", new Vector<>(),
            new Vector<>()
        );
        assertNotEquals(d1, d2);
    }

    @Test
    public void equalsConNuloRetornaFalse() {
        assertNotEquals(datosEjemplo(), null);
    }

    @Test
    public void equalsConTipoDistintoRetornaFalse() {
        assertNotEquals(datosEjemplo(), "cadena");
    }

    @Test
    public void equalsConCiudadesCompletadasDistintasRetornaFalse() {
        Vector<Integer> unas = new Vector<>();
        unas.add(1);
        DatosGuardado d1 = new DatosGuardado(
            "Jugador1", 500, 3, unas, new Vector<>(), "/skins/default", new Vector<>(),
            new Vector<>()
        );
        DatosGuardado d2 = datosEjemplo();
        assertNotEquals(d1, d2);
    }

    @Test
    public void hashCodeIgualParaObjetosIguales() {
        DatosGuardado d1 = datosEjemplo();
        DatosGuardado d2 = datosEjemplo();
        assertEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    public void hashCodeDistintoParaObjetosDiferentes() {
        DatosGuardado d1 = datosEjemplo();
        DatosGuardado d2 = new DatosGuardado(
            "Otro", 500, 3, new Vector<>(), new Vector<>(), "/skins/default", new Vector<>(),
            new Vector<>()
        );
        assertNotEquals(d1.hashCode(), d2.hashCode());
    }

    @Test
    public void toStringContieneNombreJugador() {
        DatosGuardado d = datosEjemplo();
        assertTrue(d.toString().contains("Jugador1"));
    }

    @Test
    public void toStringContienePuntajeTotal() {
        DatosGuardado d = datosEjemplo();
        assertTrue(d.toString().contains("500"));
    }

    @Test
    public void toStringContieneIdCiudadActual() {
        DatosGuardado d = datosEjemplo();
        assertTrue(d.toString().contains("3"));
    }

    @Test
    public void toStringContieneSkinActual() {
        DatosGuardado d = datosEjemplo();
        assertTrue(d.toString().contains("/skins/default"));
    }

    @Test
    public void toStringNoEsNuloNiVacio() {
        String s = datosEjemplo().toString();
        assertNotNull(s);
        assertFalse(s.isEmpty());
    }

    @Test
    public void getNombreJugadorEsPublico() throws Exception {
        var m = DatosGuardado.class.getMethod("getNombreJugador");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void getPuntajeTotalEsPublico() throws Exception {
        var m = DatosGuardado.class.getMethod("getPuntajeTotal");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void getIdCiudadActualEsPublico() throws Exception {
        var m = DatosGuardado.class.getMethod("getIdCiudadActual");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void getIdsCiudadesCompletadasEsPublico() throws Exception {
        var m = DatosGuardado.class.getMethod("getIdsCiudadesCompletadas");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void getIdsCiudadesDesbloqueadasEsPublico() throws Exception {
        var m = DatosGuardado.class.getMethod("getIdsCiudadesDesbloqueadas");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void getSkinActualEsPublico() throws Exception {
        var m = DatosGuardado.class.getMethod("getSkinActual");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void getSkinsDesbloqueadasEsPublico() throws Exception {
        var m = DatosGuardado.class.getMethod("getSkinsDesbloqueadas");
        assertTrue(Modifier.isPublic(m.getModifiers()));
    }

    @Test
    public void atributoNombreJugadorEsPrivado() throws Exception {
        var f = DatosGuardado.class.getDeclaredField("nombreJugador");
        assertTrue(Modifier.isPrivate(f.getModifiers()));
    }

    @Test
    public void atributoPuntajeTotalEsPrivado() throws Exception {
        var f = DatosGuardado.class.getDeclaredField("puntajeTotal");
        assertTrue(Modifier.isPrivate(f.getModifiers()));
    }

    @Test
    public void atributoIdCiudadActualEsPrivado() throws Exception {
        var f = DatosGuardado.class.getDeclaredField("idCiudadActual");
        assertTrue(Modifier.isPrivate(f.getModifiers()));
    }

    @Test
    public void atributoIdsCiudadesCompletadasEsPrivado() throws Exception {
        var f = DatosGuardado.class.getDeclaredField("idsCiudadesCompletadas");
        assertTrue(Modifier.isPrivate(f.getModifiers()));
    }

    @Test
    public void atributoIdsCiudadesDesbloqueadasEsPrivado() throws Exception {
        var f = DatosGuardado.class.getDeclaredField("idsCiudadesDesbloqueadas");
        assertTrue(Modifier.isPrivate(f.getModifiers()));
    }

    @Test
    public void atributoSkinActualEsPrivado() throws Exception {
        var f = DatosGuardado.class.getDeclaredField("skinActual");
        assertTrue(Modifier.isPrivate(f.getModifiers()));
    }

    @Test
    public void atributoSkinsDesbloqueadasEsPrivado() throws Exception {
        var f = DatosGuardado.class.getDeclaredField("skinsDesbloqueadas");
        assertTrue(Modifier.isPrivate(f.getModifiers()));
    }
}
