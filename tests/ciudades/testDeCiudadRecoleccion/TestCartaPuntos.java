package tests.ciudades.testDeCiudadRecoleccion;

import juego.ciudades.recoleccionEnMatriz.CartaPuntos;
import juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelos.Jugador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCartaPuntos {

    @Test
    public void crearCartaValida() {
        CartaPuntos carta = new CartaPuntos("CartaPuntos", "Aumenta los puntos");
        assertEquals("CartaPuntos", carta.getNombre());
        assertEquals("Aumenta los puntos", carta.getDescripcion());
    }

    @Test
    public void crearCartaConNombreNuloLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> new CartaPuntos(null, "Descripcion"));
    }

    @Test
    public void crearCartaConDescripcionNulaLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> new CartaPuntos("Carta", null));
    }

    @Test
    public void getDescripcionDevuelveValorCorrecto() {
        CartaPuntos carta = new CartaPuntos("Carta", "Descripcion de prueba");
        assertEquals("Descripcion de prueba", carta.getDescripcion());
    }

    @Test
    public void dosCartasIgualesSonEquals() {
        CartaPuntos carta1 = new CartaPuntos("Carta", "Descripcion");
        CartaPuntos carta2 = new CartaPuntos("Carta", "Descripcion");
        assertEquals(carta1, carta2);
    }

    @Test
    public void dosCartasIgualesTienenMismoHashCode() {
        CartaPuntos carta1 = new CartaPuntos("Carta", "Descripcion");
        CartaPuntos carta2 = new CartaPuntos("Carta", "Descripcion");
        assertEquals(carta1.hashCode(), carta2.hashCode());
    }

    @Test
    public void cartasConDistintaDescripcionNoSonIguales() {
        CartaPuntos carta1 = new CartaPuntos("Carta", "Descripcion1");
        CartaPuntos carta2 = new CartaPuntos("Carta", "Descripcion2");
        assertNotEquals(carta1, carta2);
    }

    @Test
    public void cartasConDistintoNombreNoSonIguales() {
        CartaPuntos carta1 = new CartaPuntos("Carta1", "Descripcion");
        CartaPuntos carta2 = new CartaPuntos("Carta2", "Descripcion");
        assertNotEquals(carta1, carta2);
    }

    @Test
    public void toStringNoDevuelveNull() {
        CartaPuntos carta = new CartaPuntos("Carta", "Descripcion");
        assertNotNull(carta.toString());
    }

    @Test
    public void toStringContieneNombreDeLaClase() {
        CartaPuntos carta = new CartaPuntos("Carta", "Descripcion");
        assertTrue(carta.toString().contains("CartaPuntos"));
    }

    @Test
    public void aplicarEfectoAumentaLosPuntosDelJuego() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion ciudad = new CiudadRecoleccion(20, 35, 3, 10, jugador);
        CartaPuntos carta = new CartaPuntos("Carta", "Aumenta puntos");
        int puntosIniciales = ciudad.getPuntos();
        carta.aplicarEfecto(ciudad);

        assertTrue(ciudad.getPuntos() > puntosIniciales);
    }
}
