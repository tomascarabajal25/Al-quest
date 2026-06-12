package ciudades.testDeCiudadRecoleccion;

import juego.ciudades.recoleccionEnMatriz.Mapa3D;
import modelos.Celda;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestMapa3D {

    @Test
    public void crearMapa3DValido() {
        Mapa3D mapa = new Mapa3D(10, 20, 3);
        assertEquals(3, mapa.getNiveles());
    }

    @Test
    public void crearMapaConFilasCeroLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> new Mapa3D(0, 20, 3));
    }

    @Test
    public void crearMapaConColumnasCeroLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> new Mapa3D(10, 0, 3));
    }

    @Test
    public void crearMapaConNivelesCeroLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> new Mapa3D(10, 20, 0));
    }

    @Test
    public void obtenerNivelValido() {
        Mapa3D mapa = new Mapa3D(10, 20, 3);
        assertNotNull(mapa.getNivel(1));
        assertNotNull(mapa.getNivel(2));
        assertNotNull(mapa.getNivel(3));
    }

    @Test
    public void obtenerNivelCeroLanzaExcepcion() {
        Mapa3D mapa = new Mapa3D(10, 20, 3);
        assertThrows(RuntimeException.class, () -> mapa.getNivel(0));
    }

    @Test
    public void ocuparCeldaGuardaContenido() {
        Mapa3D mapa = new Mapa3D(10, 20, 3);
        mapa.ocuparCelda("Hola", 2, 3, 1);
        Celda<?> celda = mapa.getCeldaConPosicion(2, 3, 1);
        assertEquals("Hola", celda.getContenido());
    }

    @Test
    public void vaciarCeldaEliminaContenido() {
        Mapa3D mapa = new Mapa3D(10, 20, 3);
        mapa.ocuparCelda("Hola", 2, 3, 1);
        mapa.VaciarCelda(2, 3, 1);
        Celda<?> celda = mapa.getCeldaConPosicion(2, 3, 1);
        assertNull(celda.getContenido());
    }

    @Test
    public void obtenerCeldaConPosicionDevuelveCeldaCorrecta() {
        Mapa3D mapa = new Mapa3D(10, 20, 3);
        mapa.ocuparCelda("Dato", 4, 5, 2);
        Celda<?> celda = mapa.getCeldaConPosicion(4, 5, 2);
        assertEquals("Dato", celda.getContenido());
    }

    @Test
    public void obtenerPosicionDeContenidoExistente() {
        Mapa3D mapa = new Mapa3D(10, 20, 3);
        String contenido = "Jugador";
        mapa.ocuparCelda(contenido, 7, 8, 2);
        int[] posicion = mapa.getPosicionCeldaConContenido(contenido);

        assertNotNull(posicion);
        assertEquals(7, posicion[0]);
        assertEquals(8, posicion[1]);
        assertEquals(2, posicion[2]);
    }

    @Test
    public void obtenerPosicionDeContenidoInexistenteDevuelveNull() {
        Mapa3D mapa = new Mapa3D(10, 20, 3);
        assertNull(mapa.getPosicionCeldaConContenido("NoExiste"));
    }

    @Test
    public void obtenerPosicionDeContenidoNuloLanzaExcepcion() {
        Mapa3D mapa = new Mapa3D(10, 20, 3);
        assertThrows(RuntimeException.class, () -> mapa.getPosicionCeldaConContenido(null));
    }

    @Test
    public void validarPosicionValidaNoLanzaExcepcion() {
        Mapa3D mapa = new Mapa3D(10, 20, 3);
        assertDoesNotThrow(() -> mapa.validarFueraDeRango(5, 5, 2));
    }

    @Test
    public void validarFilaCeroLanzaExcepcion() {
        Mapa3D mapa = new Mapa3D(10, 20, 3);
        assertThrows(RuntimeException.class, () -> mapa.validarFueraDeRango(0, 5, 2));
    }

    @Test
    public void validarColumnaCeroLanzaExcepcion() {
        Mapa3D mapa = new Mapa3D(10, 20, 3);
        assertThrows(RuntimeException.class, () -> mapa.validarFueraDeRango(5, 0, 2));
    }

    @Test
    public void cantidadTotalDeCeldasEsCorrecta() {
        Mapa3D mapa = new Mapa3D(10, 20, 3);
        int cantidad = mapa.getPosicionCeldaConContenido(1, 1, 1);
        assertEquals(600, cantidad);
    }

    @Test
    public void dosMapasIgualesSonEquals() {
        Mapa3D mapa1 = new Mapa3D(10, 20, 3);
        Mapa3D mapa2 = new Mapa3D(10, 20, 3);
        assertEquals(mapa1, mapa2);
    }

    @Test
    public void dosMapasIgualesTienenMismoHashCode() {
        Mapa3D mapa1 = new Mapa3D(10, 20, 3);
        Mapa3D mapa2 = new Mapa3D(10, 20, 3);
        assertEquals(mapa1.hashCode(), mapa2.hashCode());
    }

    @Test
    public void mapasConDistintaCantidadDeNivelesNoSonIguales() {
        Mapa3D mapa1 = new Mapa3D(10, 20, 3);
        Mapa3D mapa2 = new Mapa3D(10, 20, 4);
        assertNotEquals(mapa1, mapa2);
    }

    @Test
    public void toStringNoDevuelveNull() {
        Mapa3D mapa = new Mapa3D(10, 20, 3);
        assertNotNull(mapa.toString());
    }

    @Test
    public void toStringContieneNombreDeClase() {
        Mapa3D mapa = new Mapa3D(10, 20, 3);
        assertTrue(mapa.toString().contains("Mapa3D"));
    }
}