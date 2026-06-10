package tests.ciudades.testDeCiudadRecoleccion;

import juego.ciudades.recoleccionEnMatriz.CartaVision;
import juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelos.Jugador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCartaVision {

    @Test
    public void crearCartaValida() {
        CartaVision carta = new CartaVision("Vision", "Aumenta la visibilidad", 5);
        assertEquals("Vision", carta.getNombre());
        assertEquals("Aumenta la visibilidad", carta.getDescripcion());
        assertEquals(5, carta.getPuntos());
    }

    @Test
    public void crearCartaConNombreNuloLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> new CartaVision(null, "Descripcion", 5));
    }

    @Test
    public void crearCartaConDescripcionNulaLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> new CartaVision("Vision", null, 5));
    }

    @Test
    public void crearCartaConPuntosNegativosLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> new CartaVision("Vision", "Descripcion", -1));
    }

    @Test
    public void getPuntosDevuelveValorCorrecto() {
        CartaVision carta = new CartaVision("Vision", "Descripcion", 10);
        assertEquals(10, carta.getPuntos());
    }

    @Test
    public void getDescripcionDevuelveValorCorrecto() {
        CartaVision carta = new CartaVision("Vision", "Descripcion", 10);
        assertEquals("Descripcion", carta.getDescripcion());
    }

    @Test
    public void dosCartasIgualesSonEquals() {

        CartaVision carta1 = new CartaVision("Vision", "Descripcion", 10);
        CartaVision carta2 = new CartaVision("Vision", "Descripcion", 10);
        assertEquals(carta1, carta2);
    }

    @Test
    public void dosCartasIgualesTienenMismoHashCode() {

        CartaVision carta1 = new CartaVision("Vision", "Descripcion", 10);
        CartaVision carta2 = new CartaVision("Vision", "Descripcion", 10);

        assertEquals(carta1.hashCode(), carta2.hashCode());
    }

    @Test
    public void cartasConDistintosPuntosNoSonIguales() {

        CartaVision carta1 = new CartaVision("Vision", "Descripcion", 10);
        CartaVision carta2 = new CartaVision("Vision", "Descripcion", 20);
        assertNotEquals(carta1, carta2);
    }

    @Test
    public void cartasConDistintoNombreNoSonIguales() {
        CartaVision carta1 = new CartaVision("Vision1", "Descripcion", 10);
        CartaVision carta2 = new CartaVision("Vision2", "Descripcion", 10);
        assertNotEquals(carta1, carta2);
    }

    @Test
    public void toStringNoDevuelveNull() {
        CartaVision carta = new CartaVision("Vision", "Descripcion", 10);
        assertNotNull(carta.toString());
    }

    @Test
    public void toStringContieneNombreDeClase() {
        CartaVision carta = new CartaVision("Vision", "Descripcion", 10);
        assertTrue(carta.toString().contains("CartaVision"));
    }

    @Test
    public void aplicarEfectoAumentaLaVisionDelJuego() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion ciudad = new CiudadRecoleccion(20, 35, 3, 10, jugador);
        CartaVision carta = new CartaVision("Vision", "Descripcion", 1);
        int visionInicial = ciudad.getVisibilidad();
        carta.aplicarEfecto(ciudad);

        assertTrue(ciudad.getVisibilidad() > visionInicial);
    }
}