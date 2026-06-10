package ciudades.testsDeLaCiudadDeHashing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import Juego.ciudades.hashing.CiudadHashing;
import Juego.ciudades.hashing.ElementoHash;
import Juego.ciudades.hashing.PasoHash;


/**
 * Tests de la loggica de la ciudad 6 (de HASHING).
 * Verifico que cada operacion produce el PasoHash correcto:
 * indice esperado, colision, exito/fallo al buscar y validacion de nulls.
 */
public class CiudadHashingTest {
   
    //CONSTANTES
    private static final int SLOTS = 7;


    @Test
    public void testCalcularIndice() {
        CiudadHashing ciudad = new CiudadHashing(SLOTS);
        assertEquals(3, ciudad.calcularIndice(10), "10 % 7 = 3");
        assertEquals(3, ciudad.calcularIndice(31), "31 % 7 = 3");
        assertEquals(0, ciudad.calcularIndice(14), "14 % 7 = 0");
    }


    @Test
    public void testInsercionSimpleDevuelvePasoCorrecto() {
        CiudadHashing ciudad = new CiudadHashing(SLOTS);
        PasoHash paso = ciudad.insertar(10, new ElementoHash(10, "Pocion"));

        assertEquals(PasoHash.TipoPaso.INSERTAR, paso.getTipo(), "El paso es de tipo INSERTAR");
        assertEquals(3, paso.getIndiceSlot(), "La clave 10 cae en el slot 3");
        assertFalse(paso.isHuboColision(), "No hubo colision en tabla vacia");
        assertEquals(1, ciudad.getSlot(3).size(), "El slot 3 tiene 1 elemento");
        assertEquals(1, ciudad.getTabla().getCantidadElementos(), "La tabla tiene 1 elemento");
    }


    @Test
    public void testColisionPorEncadenamientoSeDetecta() {
        CiudadHashing ciudad = new CiudadHashing(SLOTS);

        ciudad.insertar(10, new ElementoHash(10, "Pocion")); //Sera slot 3
        PasoHash paso = ciudad.insertar(17, new ElementoHash(17, "Gema")); // 17 % 7 = 3

        assertEquals(3, paso.getIndiceSlot(), "La clave 17 tambien cae en el slot 3");
        assertTrue(paso.isHuboColision(), "Se detecta que hubo colision");
        assertEquals(2, ciudad.getSlot(3).size(), "El slot 3 quedo con 2 elementos encadenados");
        assertEquals(2, ciudad.getTabla().getCantidadElementos(), "La tabla tiene 2 elementos");
    }


    @Test
    public void testSobrescrituraNoEsColision() {
        CiudadHashing ciudad = new CiudadHashing(SLOTS);

        ciudad.insertar(10, new ElementoHash(10, "Pocion"));
        
        PasoHash paso = ciudad.insertar(10, new ElementoHash(10, "PocionMejorada"));

        assertFalse(paso.isHuboColision(), "Reinsertar la misma clave no es colision");
        assertEquals(1, ciudad.getTabla().getCantidadElementos(), "Sigue habiendo 1 elemento");
        assertEquals("PocionMejorada", ciudad.getTabla().buscar(10).getNombre(),
                    "El valor quedo actualizado a PocionMejorada");
    }


    @Test
    public void testBusquedaExitosa() {
        CiudadHashing ciudad = new CiudadHashing(SLOTS);

        ciudad.insertar(10, new ElementoHash(10, "Pocion"));
        ciudad.insertar(17, new ElementoHash(17, "Gema")); // colisiona en slot 3

        PasoHash paso = ciudad.buscar(17);

        assertEquals(PasoHash.TipoPaso.BUSCAR, paso.getTipo(), "El paso es de tipo BUSCAR");
        assertTrue(paso.isExito(), "Encuentra la clave 17 dentro de la cadena del slot 3");
        assertEquals(3, paso.getIndiceSlot(), "Reporta el slot 3, que es donde estaba la clave");
    }

    @Test
    public void testBusquedaFallidaReportaSlot() {
        CiudadHashing ciudad = new CiudadHashing(SLOTS);

        ciudad.insertar(10, new ElementoHash(10, "Pocion"));

        PasoHash paso = ciudad.buscar(99);
        
        assertFalse(paso.isExito(), "No encuentra la clave 99 (ya que no fue insertada)");
        assertEquals(ciudad.calcularIndice(99), paso.getIndiceSlot(),
                    "La busqueda fallida igual reporta el slot donde habria caido");
    }


    @Test
    public void testValidacionesDeNulos() {
        CiudadHashing ciudad = new CiudadHashing(SLOTS);

        assertThrows(IllegalArgumentException.class, () -> ciudad.insertar(null, new ElementoHash(1,"X")),
                                                            "Insertar con clave null debe devolver exception");
        assertThrows(IllegalArgumentException.class, () -> ciudad.insertar(5, null),
                                                            "Insertar con valor null debe devolver exception");
        assertThrows(IllegalArgumentException.class, () -> ciudad.buscar(null),
                                                            "Buscar con clave null debe devolver exception");
        assertThrows(IllegalArgumentException.class, () -> ciudad.calcularIndice(null),
                                                            "calcularIndice con clave null debe devolver exception");
    }


    @Test
    public void testConstructorRechazaSlotsInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new CiudadHashing(0),
                            "crear la ciudad con 0 slots debe lanzar excepcion");
    }


    @Test
    public void testGetCantidadSlots() {
        CiudadHashing ciudad = new CiudadHashing(SLOTS);

        assertEquals(SLOTS, ciudad.getCantidadSlots(), "La ciudad reporta la cantidad de slots con la que fue creada");
    }



    
}
