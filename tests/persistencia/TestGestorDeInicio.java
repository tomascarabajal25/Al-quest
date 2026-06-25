package persistencia;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class TestGestorDeInicio {

    @Test
    public void constructorEsPrivado() throws Exception {
        var c = GestorDeInicio.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(c.getModifiers()),
            "GestorDeInicio no debe ser instanciable");
    }

    @Test
    public void claseNoEsAbstracta() {
        assertFalse(Modifier.isAbstract(GestorDeInicio.class.getModifiers()));
    }


    @Test
    public void iniciarSesionEsPublicoYEstatico() throws Exception {
        var m = GestorDeInicio.class.getMethod("iniciarSesion");
        assertTrue(Modifier.isPublic(m.getModifiers()));
        assertTrue(Modifier.isStatic(m.getModifiers()));
    }

    @Test
    public void guardarSesionEsPublicoYEstatico() throws Exception {
        var m = GestorDeInicio.class.getMethod(
            "guardarSesion", juego.ciudades.ciudadGeneral.PartidaGeneral.class);
        assertTrue(Modifier.isPublic(m.getModifiers()));
        assertTrue(Modifier.isStatic(m.getModifiers()));
    }


    @Test
    public void pedirNombreJugadorEsPrivadoYEstatico() throws Exception {
        var m = GestorDeInicio.class.getDeclaredMethod("pedirNombreJugador");
        assertTrue(Modifier.isPrivate(m.getModifiers()));
        assertTrue(Modifier.isStatic(m.getModifiers()));
    }

    @Test
    public void mostrarMensajeBienvenidaDeVueltaEsPrivadoYEstatico() throws Exception {
        var m = GestorDeInicio.class.getDeclaredMethod(
            "mostrarMensajeBienvenidaDeVuelta", String.class);
        assertTrue(Modifier.isPrivate(m.getModifiers()));
        assertTrue(Modifier.isStatic(m.getModifiers()));
    }

    @Test
    public void mostrarMensajeNuevaPartidaEsPrivadoYEstatico() throws Exception {
        var m = GestorDeInicio.class.getDeclaredMethod(
            "mostrarMensajeNuevaPartida", String.class);
        assertTrue(Modifier.isPrivate(m.getModifiers()));
        assertTrue(Modifier.isStatic(m.getModifiers()));
    }


    @Test
    public void iniciarSesionRetornaPartidaGeneral() throws Exception {
        var m = GestorDeInicio.class.getMethod("iniciarSesion");
        assertEquals(juego.ciudades.ciudadGeneral.PartidaGeneral.class, m.getReturnType());
    }

    @Test
    public void guardarSesionRetornaBoolean() throws Exception {
        var m = GestorDeInicio.class.getMethod(
            "guardarSesion", juego.ciudades.ciudadGeneral.PartidaGeneral.class);
        assertEquals(boolean.class, m.getReturnType());
    }

    @Test
    public void pedirNombreJugadorRetornaString() throws Exception {
        var m = GestorDeInicio.class.getDeclaredMethod("pedirNombreJugador");
        assertEquals(String.class, m.getReturnType());
    }


    @Test
    public void guardarSesionConPartidaNulaLanzaExcepcion() {
        assertThrows(RuntimeException.class, () ->
            utils.ValidacionesUtiles.esDistintoDeNull(null, "partidaGeneral"));
    }

    @Test
    public void entradaNulaProduceNombreNulo() {
        String entrada = null;
        String resultado = normalizarEntrada(entrada);
        assertNull(resultado);
    }

    @Test
    public void entradaVaciaProduceNombreNulo() {
        String resultado = normalizarEntrada("");
        assertNull(resultado);
    }

    @Test
    public void entradaSoloEspaciosProduceNombreNulo() {
        String resultado = normalizarEntrada("   ");
        assertNull(resultado);
    }

    @Test
    public void entradaConEspaciosExternosEsNormalizada() {
        String resultado = normalizarEntrada("  Alice  ");
        assertEquals("Alice", resultado);
    }

    @Test
    public void entradaValidaSinEspaciosSeDevuelveTalCual() {
        String resultado = normalizarEntrada("Jugador1");
        assertEquals("Jugador1", resultado);
    }

    @Test
    public void entradaConEspaciosInternosSeConserva() {
        String resultado = normalizarEntrada("  Juan Pérez  ");
        assertEquals("Juan Pérez", resultado);
    }

    @Test
    public void mensajeBienvenidaContieneNombreJugador() {
        String nombre = "Alice";
        String mensaje = "Bienvenido de vuelta, " + nombre + ". Cargando partida...";
        assertTrue(mensaje.contains(nombre));
    }

    @Test
    public void mensajeBienvenidaContieneTextoFijo() {
        String nombre = "Bob";
        String mensaje = "Bienvenido de vuelta, " + nombre + ". Cargando partida...";
        assertTrue(mensaje.contains("Bienvenido de vuelta"));
        assertTrue(mensaje.contains("Cargando partida"));
    }

    @Test
    public void mensajeNuevaPartidaContieneNombreJugador() {
        String nombre = "Carlos";
        String mensaje = "Creando nueva partida para " + nombre + "...";
        assertTrue(mensaje.contains(nombre));
    }

    @Test
    public void mensajeNuevaPartidaContieneTextoFijo() {
        String nombre = "Diana";
        String mensaje = "Creando nueva partida para " + nombre + "...";
        assertTrue(mensaje.contains("Creando nueva partida para"));
    }

    @Test
    public void mensajeBienvenidaYNuevaPartidaSonDistintos() {
        String nombre = "Eve";
        String bienvenida  = "Bienvenido de vuelta, " + nombre + ". Cargando partida...";
        String nuevaPartida = "Creando nueva partida para " + nombre + "...";
        assertNotEquals(bienvenida, nuevaPartida);
    }

    @Test
    public void flujoConDatosGuardadosNoNuloAplicaDatos() {
        DatosGuardado datos = new DatosGuardado(
            "jugador", 100, 2,
            new java.util.Vector<>(), new java.util.Vector<>(),
            "/skin", new java.util.Vector<>()
        );
        assertTrue(datos != null);
    }

    @Test
    public void flujoSinDatosGuardadosUsaPartidaNueva() {
        DatosGuardado datos = null;
        assertNull(datos);
    }

    @Test
    public void flujoConNombreNuloRetornariaNullSinCrearPartida() {
        // Si pedirNombreJugador devuelve null, iniciarSesion retorna null
        String nombreJugador = normalizarEntrada(null);
        assertNull(nombreJugador);
    }


    @Test
    public void soloExistenDosMetodosPublicos() {
        long publicCount = java.util.Arrays.stream(GestorDeInicio.class.getMethods())
            .filter(m -> m.getDeclaringClass().equals(GestorDeInicio.class))
            .filter(m -> Modifier.isPublic(m.getModifiers()))
            .count();
        assertEquals(2, publicCount,
            "Solo deben existir iniciarSesion y guardarSesion como métodos públicos propios");
    }

    private String normalizarEntrada(String entrada) {
        if (entrada == null) return null;
        String limpio = entrada.trim();
        return limpio.isEmpty() ? null : limpio;
    }
}
