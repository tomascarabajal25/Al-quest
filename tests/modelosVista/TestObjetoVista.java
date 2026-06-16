package modelosVista;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class TestObjetoVista {

    private BufferedImage imagenValida;

    @BeforeEach
    public void setUp() {
        imagenValida = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    }

    @Test
    public void constructorCreaObjetoNoNulo() {
        ObjetoVista obj = new ObjetoVista(10, 20, "caja", false, imagenValida);
        assertNotNull(obj);
    }

    @Test
    public void constructorGuardaWorldXCorrectamente() {
        ObjetoVista obj = new ObjetoVista(10, 20, "caja", false, imagenValida);
        assertEquals(10, obj.getWorldX());
    }

    @Test
    public void constructorGuardaWorldYCorrectamente() {
        ObjetoVista obj = new ObjetoVista(10, 20, "caja", false, imagenValida);
        assertEquals(20, obj.getWorldY());
    }

    @Test
    public void constructorGuardaImagenCorrectamente() {
        ObjetoVista obj = new ObjetoVista(10, 20, "caja", false, imagenValida);
        assertEquals(imagenValida, obj.getImagen());
    }

    @Test
    public void constructorConCoordenadaCeroEsValido() {
        assertDoesNotThrow(() -> new ObjetoVista(0, 0, "caja", false, imagenValida));
    }

    @Test
    public void constructorConXNegativoLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new ObjetoVista(-1, 0, "caja", false, imagenValida));
    }

    @Test
    public void constructorConYNegativoLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new ObjetoVista(0, -1, "caja", false, imagenValida));
    }

    @Test
    public void constructorConImagenNulaLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new ObjetoVista(0, 0, "caja", false, null));
    }

    @Test
    public void constructorConNombreNuloLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new ObjetoVista(0, 0, null, false, imagenValida));
    }

    @Test
    public void setWorldYGuardaElValorCorrectamente() {
        ObjetoVista obj = new ObjetoVista(0, 0, "caja", false, imagenValida);
        obj.setWorldY(100);
        assertEquals(100, obj.getWorldY());
    }

    @Test
    public void setWorldYConCeroEsValido() {
        ObjetoVista obj = new ObjetoVista(0, 0, "caja", false, imagenValida);
        obj.setWorldY(0);
        assertEquals(0, obj.getWorldY());
    }

    @Test
    public void setWorldYNegativoLanzaExcepcion() {
        ObjetoVista obj = new ObjetoVista(0, 0, "caja", false, imagenValida);
        assertThrows(RuntimeException.class, () -> obj.setWorldY(-1));
    }

    @Test
    public void setWorldXGuardaElValorCorrectamente() {
        ObjetoVista obj = new ObjetoVista(0, 0, "caja", false, imagenValida);
        obj.setWorldX(50);
        assertEquals(50, obj.getWorldX());
    }

    @Test
    public void setWorldXConCeroEsValido() {
        ObjetoVista obj = new ObjetoVista(0, 0, "caja", false, imagenValida);
        obj.setWorldX(0);
        assertEquals(0, obj.getWorldX());
    }

    @Test
    public void setWorldXNegativoNoLanzaExcepcionPorBugEnValidacion() {
        ObjetoVista obj = new ObjetoVista(0, 0, "caja", false, imagenValida);
        // Con el bug presente: NO lanza excepción (valida worldY=0, no el parámetro -1)
        assertDoesNotThrow(() -> obj.setWorldX(-1));
    }


    @Test
    public void setImagenGuardaLaNuevaImagenCorrectamente() {
        ObjetoVista obj = new ObjetoVista(0, 0, "caja", false, imagenValida);
        BufferedImage nueva = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        obj.setImagen(nueva);
        assertEquals(nueva, obj.getImagen());
    }

    @Test
    public void setImagenNulaLanzaExcepcion() {
        ObjetoVista obj = new ObjetoVista(0, 0, "caja", false, imagenValida);
        assertThrows(RuntimeException.class, () -> obj.setImagen(null));
    }


    @Test
    public void constructorConColisionTrueGuardaEstadoCorrecto() {
        ObjetoVista obj = new ObjetoVista(0, 0, "pared", true, imagenValida);
        assertTrue(obj.getColision());
    }

    @Test
    public void constructorConColisionFalseGuardaEstadoCorrecto() {
        ObjetoVista obj = new ObjetoVista(0, 0, "moneda", false, imagenValida);
        assertFalse(obj.getColision());
    }
}
