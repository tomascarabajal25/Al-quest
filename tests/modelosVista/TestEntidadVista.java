package modelosVista;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Rectangle;

import static org.junit.jupiter.api.Assertions.*;

public class TestEntidadVista {

    private EntidadVista entidad;

    @BeforeEach
    public void setUp() {
        entidad = new EntidadVista("TestEntidad");
    }

    @Test
    public void constructorCreaEntidadNoNula() {
        assertNotNull(entidad);
    }

    @Test
    public void constructorConNombreNullLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> new EntidadVista(null));
    }

    @Test
    public void velocidadInicialEsCuatro() {
        assertEquals(4, entidad.getVelocidad());
    }

    @Test
    public void spriteNumInicialEsUno() {
        assertEquals(1, entidad.getSpriteNum());
    }

    @Test
    public void spriteCounterInicialEsCero() {
        assertEquals(0, entidad.getSpriteCounter());
    }

    @Test
    public void colisionOnInicialEsFalse() {
        assertFalse(entidad.isColisionOn());
    }

    @Test
    public void areaSolidaInicialEsNula() {
        assertNull(entidad.getAreaSolida());
    }

    @Test
    public void direccionInicialEsNula() {
        assertNull(entidad.getDireccion());
    }

    @Test
    public void imagenesInicialesUp1EsNula() {
        assertNull(entidad.getUp1());
    }

    @Test
    public void imagenesInicialesUp2EsNula() {
        assertNull(entidad.getUp2());
    }

    @Test
    public void imagenesInicialesDown1EsNula() {
        assertNull(entidad.getDown1());
    }

    @Test
    public void imagenesInicialesDown2EsNula() {
        assertNull(entidad.getDown2());
    }

    @Test
    public void imagenesInicialesRight1EsNula() {
        assertNull(entidad.getRight1());
    }

    @Test
    public void imagenesInicialesRight2EsNula() {
        assertNull(entidad.getRight2());
    }

    @Test
    public void imagenesInicialesLeft1EsNula() {
        assertNull(entidad.getLeft1());
    }

    @Test
    public void imagenesInicialesLeft2EsNula() {
        assertNull(entidad.getLeft2());
    }

    @Test
    public void setWorldXGuardaElValorCorrectamente() {
        entidad.setWorldX(100);
        assertEquals(100, entidad.getWorldX());
    }

    @Test
    public void setWorldXConCeroEsValido() {
        entidad.setWorldX(0);
        assertEquals(0, entidad.getWorldX());
    }

    @Test
    public void setWorldXNegativoLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> entidad.setWorldX(-1));
    }

    @Test
    public void setWorldYGuardaElValorCorrectamente() {
        entidad.setWorldY(200);
        assertEquals(200, entidad.getWorldY());
    }

    @Test
    public void setWorldYConCeroEsValido() {
        entidad.setWorldY(0);
        assertEquals(0, entidad.getWorldY());
    }

    @Test
    public void setWorldYNegativoLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> entidad.setWorldY(-1));
    }

    @Test
    public void cambiarVelocidadGuardaElNuevoValor() {
        entidad.cambiarVelocidad(8);
        assertEquals(8, entidad.getVelocidad());
    }

    @Test
    public void cambiarVelocidadConUnoEsValido() {
        entidad.cambiarVelocidad(1);
        assertEquals(1, entidad.getVelocidad());
    }

    @Test
    public void cambiarVelocidadConCeroLanzaExcepcion() {
        // cambiarVelocidad exige > 0 (validarMayorACero)
        assertThrows(RuntimeException.class, () -> entidad.cambiarVelocidad(0));
    }

    @Test
    public void cambiarVelocidadNegativoLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> entidad.cambiarVelocidad(-5));
    }

    @Test
    public void setAreaSolidaGuardaElRectanguloCorrectamente() {
        Rectangle rect = new Rectangle(0, 0, 32, 32);
        entidad.setAreaSolida(rect);
        assertEquals(rect, entidad.getAreaSolida());
    }

    @Test
    public void setAreaSolidaNulaLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> entidad.setAreaSolida(null));
    }

    @Test
    public void setDireccionGuardaLaDireccionCorrectamente() {
        entidad.setDireccion(Direccion.ARRIBA);
        assertEquals(Direccion.ARRIBA, entidad.getDireccion());
    }

    @Test
    public void setDireccionNulaLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> entidad.setDireccion(null));
    }

    @Test
    public void setDireccionCambiaDeDireccion() {
        entidad.setDireccion(Direccion.ARRIBA);
        entidad.setDireccion(Direccion.ABAJO);
        assertEquals(Direccion.ABAJO, entidad.getDireccion());
    }

    @Test
    public void setColisionOnTrueActualizaElEstado() {
        entidad.setColisionOn(true);
        assertTrue(entidad.isColisionOn());
    }

    @Test
    public void setColisionOnFalseActualizaElEstado() {
        entidad.setColisionOn(true);
        entidad.setColisionOn(false);
        assertFalse(entidad.isColisionOn());
    }

    @Test
    public void setSpriteNumGuardaElValorCorrectamente() {
        entidad.setSpriteNum(2);
        assertEquals(2, entidad.getSpriteNum());
    }

    @Test
    public void setSpriteNumConCeroEsValido() {
        entidad.setSpriteNum(0);
        assertEquals(0, entidad.getSpriteNum());
    }

    @Test
    public void setSpriteNumNegativoLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> entidad.setSpriteNum(-1));
    }


    @Test
    public void setSpriteCounterGuardaElValorCorrectamente() {
        entidad.setSpriteCounter(10);
        assertEquals(10, entidad.getSpriteCounter());
    }

    @Test
    public void setSpriteCounterConCeroEsValido() {
        entidad.setSpriteCounter(0);
        assertEquals(0, entidad.getSpriteCounter());
    }

    @Test
    public void setSpriteCounterNegativoLanzaExcepcion() {
        assertThrows(RuntimeException.class, () -> entidad.setSpriteCounter(-1));
    }
}
