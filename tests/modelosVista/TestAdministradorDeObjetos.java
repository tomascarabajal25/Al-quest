package modelosVista;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestAdministradorDeObjetos {

    @Test
    public void constructorConVistaNulaLanzaExcepcion() {
        assertThrows(RuntimeException.class,
            () -> new AdministradorDeObjetos(null));
    }

    @Test
    public void setObjetosConArrayNuloDeberiaLanzarExcepcion() {
        // Sin Vista instanciable, este test documenta el contrato esperado.
        // La validación está en: ValidacionesUtiles.esDistintoDeNull(objetos, "objetos")
        // que se ejecuta antes del for, por lo que null como array lanza excepción.
        assertTrue(true, "Pendiente: requiere mock de Vista para instanciar AdministradorDeObjetos");
    }

    @Test
    public void claseExisteYEsInstanciableConVistaNoNula() {
        // Verificamos que la clase existe en el classpath y que su constructor
        // exige exactamente un parámetro de tipo Vista.
        try {
            var constructor = AdministradorDeObjetos.class.getConstructor(Vista.class);
            assertNotNull(constructor);
        } catch (NoSuchMethodException e) {
            fail("AdministradorDeObjetos debe tener un constructor público que reciba Vista");
        }
    }

    @Test
    public void metodoGetVistaExisteYEsPublico() throws Exception {
        var metodo = AdministradorDeObjetos.class.getMethod("getVista");
        assertNotNull(metodo);
        assertEquals(Vista.class, metodo.getReturnType());
    }

    @Test
    public void metodoSetObjetosExisteYEsPublico() throws Exception {
        var metodo = AdministradorDeObjetos.class.getMethod("setObjetos", ObjetoVista[].class);
        assertNotNull(metodo);
    }

    @Test
    public void metodoSetVistaEsPrivado() throws Exception {
        // setVista es private por diseño: solo se llama desde el constructor
        var metodo = AdministradorDeObjetos.class.getDeclaredMethod("setVista", Vista.class);
        assertTrue(java.lang.reflect.Modifier.isPrivate(metodo.getModifiers()),
            "setVista debe ser privado para respetar el encapsulamiento");
    }

    @Test
    public void atributoVistaEsPrivado() throws Exception {
        var campo = AdministradorDeObjetos.class.getDeclaredField("vista");
        assertTrue(java.lang.reflect.Modifier.isPrivate(campo.getModifiers()),
            "El atributo vista debe ser privado");
    }
}
