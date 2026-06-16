package modelosVista;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

public class TestConstruccion {

    private Construccion construccion;
    private BufferedImage imagenValida;

    @BeforeEach
    public void setUp() {
        construccion = new Construccion();
        imagenValida = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    }

    @Test
    public void imagenInicialEsNula() {
        assertNull(construccion.getImagen());
    }

    @Test
    public void colisionInicialEsFalse() {
        assertFalse(construccion.getColision());
    }

    @Test
    public void setImagenGuardaLaImagenCorrectamente() {
        construccion.setImagen(imagenValida);
        assertEquals(imagenValida, construccion.getImagen());
    }

    @Test
    public void setImagenNulaLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> construccion.setImagen(null));
    }

    @Test
    public void setImagenReemplazaLaImagenAnterior() {
        construccion.setImagen(imagenValida);
        BufferedImage nueva = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        construccion.setImagen(nueva);
        assertEquals(nueva, construccion.getImagen());
    }

    @Test
    public void setColisionTrueActualizaElEstado() {
        construccion.setColision(true);
        assertTrue(construccion.getColision());
    }

    @Test
    public void setColisionFalseActualizaElEstado() {
        construccion.setColision(true);
        construccion.setColision(false);
        assertFalse(construccion.getColision());
    }

    @Test
    public void setColisionEsIdempotenteSiSeRepite() {
        construccion.setColision(true);
        construccion.setColision(true);
        assertTrue(construccion.getColision());
    }

    @Test
    public void setImagenEsProtegido() throws Exception {
        var metodo = Construccion.class.getDeclaredMethod("setImagen", BufferedImage.class);
        assertTrue(Modifier.isProtected(metodo.getModifiers()),
            "setImagen debe ser protected");
    }

    @Test
    public void setColisionEsProtegido() throws Exception {
        var metodo = Construccion.class.getDeclaredMethod("setColision", boolean.class);
        assertTrue(Modifier.isProtected(metodo.getModifiers()),
            "setColision debe ser protected");
    }

    @Test
    public void getColisionEsProtegido() throws Exception {
        var metodo = Construccion.class.getDeclaredMethod("getColision");
        assertTrue(Modifier.isProtected(metodo.getModifiers()),
            "getColision debe ser protected");
    }

    @Test
    public void getImagenEsPublico() throws Exception {
        var metodo = Construccion.class.getMethod("getImagen");
        assertTrue(Modifier.isPublic(metodo.getModifiers()),
            "getImagen debe ser public");
    }

    @Test
    public void atributoImagenEsPrivado() throws Exception {
        var campo = Construccion.class.getDeclaredField("imagen");
        assertTrue(Modifier.isPrivate(campo.getModifiers()),
            "El atributo imagen debe ser privado");
    }

    @Test
    public void atributoColisionEsPrivado() throws Exception {
        var campo = Construccion.class.getDeclaredField("colision");
        assertTrue(Modifier.isPrivate(campo.getModifiers()),
            "El atributo colision debe ser privado");
    }
}
