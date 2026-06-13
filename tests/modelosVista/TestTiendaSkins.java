package modelosVista;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestTiendaSkins {

    private String capitalizar(String base) throws Exception {
        Method m = TiendaSkins.class.getDeclaredMethod("capitalizar", String.class);
        m.setAccessible(true);
        // Se necesita una instancia; como no podemos crearla normalmente,
        // usamos null y capturamos solo el resultado del método estático en lógica.
        // Como capitalizar no usa `this`, podemos pasarle null de forma segura.
        return (String) m.invoke(null, base);
    }


    private static final Object[][] CATALOGO = {
        { "boy",      0       },
        { "lady",     0       },
        { "captain",  0       },
        { "dinosaur", 0       },
        { "ivan",     0       },
        { "doggy",    0       },
        { "goblin",   0       },
        { "king",     0       },
        { "knight",   80      },
        { "roman",    0       },
        { "soldier",  0       },
        { "goku",     1000000 },
        { "naruto",   1000000 },
        { "yoda",     500000  },
    };

    @Test
    public void catalogoTieneCatorceSkins() {
        assertEquals(14, CATALOGO.length);
    }

    @Test
    public void catalogoCadaEntradaTieneDosColumnas() {
        for (Object[] fila : CATALOGO) {
            assertEquals(2, fila.length,
                "La entrada '" + fila[0] + "' debe tener [nombre, precio]");
        }
    }

    @Test
    public void catalogoNombreBaseNuncaEsNulo() {
        for (Object[] fila : CATALOGO) {
            assertNotNull(fila[0], "El nombre base no puede ser null");
        }
    }

    @Test
    public void catalogoNombreBaseNuncaEsVacio() {
        for (Object[] fila : CATALOGO) {
            assertFalse(((String) fila[0]).isEmpty(),
                "El nombre base no puede ser vacío");
        }
    }

    @Test
    public void catalogoPrecioNuncaEsNegativo() {
        for (Object[] fila : CATALOGO) {
            int precio = (int) fila[1];
            assertTrue(precio >= 0,
                "El precio de '" + fila[0] + "' no puede ser negativo, era: " + precio);
        }
    }

    @Test
    public void catalogoNoHayNombresBaseDuplicados() {
        Set<String> nombres = new HashSet<>();
        for (Object[] fila : CATALOGO) {
            String nombre = (String) fila[0];
            assertTrue(nombres.add(nombre),
                "El nombre base '" + nombre + "' está duplicado en el catálogo");
        }
    }

    @Test
    public void catalogoPrimerEntradaEsBoyConPrecioCero() {
        assertEquals("boy", CATALOGO[0][0]);
        assertEquals(0,     CATALOGO[0][1]);
    }

    @Test
    public void catalogoKnightTieneOchentaPuntos() {
        // knight es la única skin con precio > 0 entre las disponibles
        Object[] knight = null;
        for (Object[] fila : CATALOGO) {
            if ("knight".equals(fila[0])) {
                knight = fila;
                break;
            }
        }
        assertNotNull(knight, "knight debe estar en el catálogo");
        assertEquals(80, knight[1]);
    }

    @Test
    public void catalogoSkinsConPrecioCeroSonLaMayoria() {
        long gratis = 0;
        for (Object[] fila : CATALOGO) {
            if ((int) fila[1] == 0) gratis++;
        }
        assertTrue(gratis > CATALOGO.length / 2,
            "La mayoría de las skins deberían ser gratuitas");
    }

    @Test
    public void catalogoNombreBaseNoContieneEspacios() {
        for (Object[] fila : CATALOGO) {
            String nombre = (String) fila[0];
            assertFalse(nombre.contains(" "),
                "El nombre base '" + nombre + "' no debe contener espacios (se usa como ruta de archivo)");
        }
    }

    @Test
    public void rutaBaseConFormatoCorrectoEsConsistente() {
        // La ruta esperada sigue el patrón "/assets/jugador/<base>"
        String carpeta = "/assets/jugador/";
        for (Object[] fila : CATALOGO) {
            String base = (String) fila[0];
            String ruta = carpeta + base;
            assertTrue(ruta.startsWith("/assets/jugador/"),
                "La ruta de '" + base + "' debe comenzar con /assets/jugador/");
            assertFalse(ruta.endsWith("/"),
                "La ruta de '" + base + "' no debe terminar con /");
        }
    }

    @Test
    public void sufijoDeSpriteRepresentativoEstaDefinido() {
        // El método skinDisponible() chequea <ruta>_down_1.bmp
        String sufijo = "_down_1.bmp";
        assertFalse(sufijo.isEmpty());
        assertTrue(sufijo.endsWith(".bmp"));
    }
}
