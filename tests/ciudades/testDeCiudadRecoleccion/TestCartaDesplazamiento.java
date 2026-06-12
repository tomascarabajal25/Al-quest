package ciudades.testDeCiudadRecoleccion;

import juego.ciudades.recoleccionEnMatriz.CartaDesplazamiento;
import juego.ciudades.recoleccionEnMatriz.CiudadRecoleccion;
import modelos.Jugador;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCartaDesplazamiento {

    @Test
    public void crearCartaValida() {
        CartaDesplazamiento carta = new CartaDesplazamiento("Turbo", "Aumenta desplazamiento", 5);

        assertEquals("Turbo", carta.getNombre());
        assertEquals("Aumenta desplazamiento", carta.getDescripcion());
        assertEquals(5, carta.getPuntos());
    }

    @Test
    public void crearCartaConNombreNuloLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> new CartaDesplazamiento(null, "Descripcion", 5));
    }

    @Test
    public void crearCartaConDescripcionNulaLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> new CartaDesplazamiento("Turbo", null, 5));
    }

    @Test
    public void crearCartaConPuntosNegativosLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> new CartaDesplazamiento("Turbo", "Descripcion", -1));
    }

    @Test
    public void getPuntosDevuelveValorCorrecto() {
        CartaDesplazamiento carta = new CartaDesplazamiento("Turbo", "Descripcion", 10);
        assertEquals(10, carta.getPuntos());
    }

    @Test
    public void getDescripcionDevuelveValorCorrecto() {
        CartaDesplazamiento carta = new CartaDesplazamiento("Turbo", "Descripcion", 10);
        assertEquals("Descripcion", carta.getDescripcion());
    }

    @Test
    public void dosCartasIgualesSonEquals() {
        CartaDesplazamiento carta1 = new CartaDesplazamiento("Turbo", "Descripcion", 10);
        CartaDesplazamiento carta2 = new CartaDesplazamiento("Turbo", "Descripcion", 10);
        assertEquals(carta1, carta2);
    }

    @Test
    public void dosCartasIgualesTienenMismoHashCode() {
        CartaDesplazamiento carta1 = new CartaDesplazamiento("Turbo", "Descripcion", 10);
        CartaDesplazamiento carta2 = new CartaDesplazamiento("Turbo", "Descripcion", 10);
        assertEquals(carta1.hashCode(), carta2.hashCode());
    }

    @Test
    public void cartasConDistintosPuntosNoSonIguales() {
        CartaDesplazamiento carta1 = new CartaDesplazamiento("Turbo", "Descripcion", 10);
        CartaDesplazamiento carta2 = new CartaDesplazamiento("Turbo", "Descripcion", 20);
        assertNotEquals(carta1, carta2);
    }

    @Test
    public void toStringNoDevuelveNull() {
        CartaDesplazamiento carta = new CartaDesplazamiento("Turbo", "Descripcion", 10);
        assertNotNull(carta.toString());
    }

    @Test
    public void aplicarEfectoAumentaElDesplazamientoDelJuego() {
        Jugador jugador = new Jugador("Tomas");
        CiudadRecoleccion ciudad = new CiudadRecoleccion(20, 35, 3, 10, jugador);
        CartaDesplazamiento carta = new CartaDesplazamiento("Turbo", "Descripcion", 10);
        int desplazamientoInicial = ciudad.getDesplazamiento();
        carta.aplicarEfecto(ciudad);

        assertEquals(desplazamientoInicial + 1, ciudad.getDesplazamiento());
    }
}
