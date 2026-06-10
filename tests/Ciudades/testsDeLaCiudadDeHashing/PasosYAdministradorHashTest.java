package ciudades.testsDeLaCiudadDeHashing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Juego.ciudades.hashing.AdministradorPasosHash;
import Juego.ciudades.hashing.PasoHash;

/**
 * Tests de PasoHash y AdministradorPasosHash.
 * Verifica que el paso guarde sus datos de forma inmutable y
 * el administrador mantenga el historial en orden.
 */
public class PasosYAdministradorHashTest {
    //HELPERS
    /**
     * Crea un PAsoHash de insercion de prueba con descripcion
     */
    private PasoHash nuevoPasoInsertar(String clave, int indice, String descripcion) {
        PasoHash paso = new PasoHash(PasoHash.TipoPaso.INSERTAR, clave, clave.hashCode(), indice, false, true, descripcion);

        return paso; 
    }


    @Test 
    public void testPasoHashGuardaSusDatos() {
        PasoHash paso = new PasoHash(PasoHash.TipoPaso.BUSCAR, "17", 17, 3, false, true, "Encontrada");

        assertEquals(PasoHash.TipoPaso.BUSCAR, paso.getTipo());
        assertEquals("17", paso.getClaveTexto());
        assertEquals(17, paso.getHashCalculado());
        assertEquals(3, paso.getIndiceSlot());
        assertFalse(paso.isHuboColision());
        assertTrue(paso.isExito());
        assertEquals("Encontrada", paso.getDescripcion());
    }

    
    @Test
    public void testPasoHashRechazaParametrosNulos() {
        assertThrows(IllegalArgumentException.class, () -> new PasoHash(null, "1", 1, 0, false, true, "desc"),
                    "tipo null devuelve excepcion");
        assertThrows(IllegalArgumentException.class, () -> new PasoHash(PasoHash.TipoPaso.INSERTAR, null, 1, 0, false, true, "desc"),
                "si claveTexto es null debe lanzar exception");
        assertThrows(IllegalArgumentException.class, () -> new PasoHash(PasoHash.TipoPaso.INSERTAR, "1", 1, 0, false, true, null),
                "descripcion null tiene que devolver una excepcion");
    }

    
    @Test
    public void testAdministradorArrancaVacio() {
        AdministradorPasosHash admin = new AdministradorPasosHash();
        assertEquals(0, admin.getCantidadPasos(), "Un administrador nuevo no tiene pasos");
        assertTrue(admin.getPasos().isEmpty(), "La lista de pasos empieza vacia");
        assertNull(admin.getUltimoPaso(), "Sin pasos, el ultimo paso es null");
    }


    @Test
    public void testAdministradorGuardaYDevuelveUltimoPaso() {
        AdministradorPasosHash admin = new AdministradorPasosHash();
        PasoHash primero = nuevoPasoInsertar("10", 3, "Inserta 10");
        PasoHash segundo = nuevoPasoInsertar("22",1,"Inserta 22");

        admin.agregarPaso(primero);
        admin.agregarPaso(segundo);

        assertEquals(2, admin.getCantidadPasos(), " Se registraron 2 pasos");
        assertSame(segundo, admin.getUltimoPaso(), "El ultimo paso sera el ultimo agregado");
        assertSame(primero, admin.getPasos().get(0), "El primer paso conserva su orden");
    }


    @Test
    public void testAdministradorLimpiaElHistorial() {
        AdministradorPasosHash admin = new AdministradorPasosHash();

        admin.agregarPaso(nuevoPasoInsertar("10", 3, "Inserta 10"));
        admin.limpiar();

        assertEquals(0, admin.getCantidadPasos(), "Tras limpiar no deberian quedar pasos");
        assertNull(admin.getUltimoPaso(), "Tras limpiar el ultimo paso vuelve a ser null");
    }


    @Test
    public void testAdministradorRechazaPasoNulo() {
        AdministradorPasosHash admin = new AdministradorPasosHash();

        assertThrows(IllegalArgumentException.class, () -> admin.agregarPaso(null),
                    "Agregar un paso null va a devolver excepcion");
    }

}
