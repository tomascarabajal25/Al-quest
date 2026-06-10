package ciudades.testsDeLaCiudadDeHashing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import estructuras.hashing.HashTable;

/**
 * Tests de la estructura HashTable (manejo de colisiones por encadenamiento).
 * Verifica el calculo del indice, la insercion, las colisiones, la sobreescritura,
 * la busqueda y las validaciones de parametros.
 */

public class HashTableTest {

    //CONSTANTES
    private static final int SLOTS = 7;

    @Test 
    public void testConstructorRechazaSlotsInvalidos(){
        assertThrows(IllegalArgumentException.class, () -> new HashTable<Integer, String>(0),
                        "Crear la tabla con 0 slots lanza excepcion");
        
        assertThrows(IllegalArgumentException.class, () -> new HashTable<Integer, String>(-3), 
                        "Crear la tabla con slots negativos me lanza exception");
    }

    
    @Test
    public void testCalcularIndiceEsClaveModSlots() {
        HashTable<Integer, String> tabla = new HashTable<>(SLOTS);
        assertEquals(3, tabla.calcularIndice(10), "10 % 7 = 3");
        assertEquals(0, tabla.calcularIndice(14), "14 % 7 = 0");
        assertEquals(0, tabla.calcularIndice(7), "7 % 7 = 0");
        assertEquals(3, tabla.calcularIndice(31), "31 % 7 = 3");
    }

    
    @Test
    public void testCalcularIndiceRechazaNull(){
        HashTable<Integer, String> tabla = new HashTable<>(SLOTS);
        assertThrows(IllegalArgumentException.class, () -> tabla.calcularIndice(null),
                        "calcularIndice con clave null deberia lanzar excepcion");
        
    }


    @Test 
    public void testInsercionSimpleSinColision() {
        HashTable<Integer, String> tabla = new HashTable<>(SLOTS);
        tabla.insertar(10, "Pocion");

        assertEquals(1, tabla.getSlot(3).size(), "La clave 10 cae en el slot 3");
        assertEquals(1, tabla.getCantidadElementos(), "La tabla tiene 1 elemento");
        assertTrue(tabla.contiene(10), "La tabla contiene la clave 10");
        assertEquals("Pocion", tabla.buscar(10), "buscar(10) devuelve el valor asociado");
    }

    
    @Test
    public void testColisionSeEncadena() {
        HashTable<Integer, String> tabla = new HashTable<>(SLOTS);
        tabla.insertar(10, "Pocion");   // iria en slot 3
        tabla.insertar(17, "Gema");     // 17 % 7 = 3, deberia haebr colision

        assertEquals(2, tabla.getSlot(3).size(), "El slot 3 quedo con 2 elementos encadenados");
        assertEquals(2, tabla.getCantidadElementos(), "La tabla tiene 2 elementos");
        assertEquals("Pocion", tabla.buscar(10), "Se sigue encontrando la primera clave de la cadena");
        assertEquals("Gema", tabla.buscar(17), "Se encuentra la segunda clave de la cadena");
    }


    @Test
    public void testSobrescrituraDeClaveNoSumaElementos() {
        HashTable<Integer, String> tabla = new HashTable<>(SLOTS);
        tabla.insertar(10, "Pocion");
        tabla.insertar(10, "PocionMejorada");

        assertEquals(1, tabla.getCantidadElementos(), "Reinsertar la misma clave no suma elementos");
        assertEquals("PocionMejorada", tabla.buscar(10), "El valor quedo actualizado");
    }


    @Test
    public void testBusquedaDeClaveInexistente() {
        HashTable<Integer, String> tabla = new HashTable<>(SLOTS);
        tabla.insertar(10, "Pocion");

        assertNull(tabla.buscar(99), "Buscar una clave ausente devuelve null");
        assertFalse(tabla.contiene(99), "Contiene devuelve false para una clave ausente");
    }


    @Test
    public void testInsertarRechazaParametrosNulos() {
        HashTable<Integer, String> tabla = new HashTable<>(SLOTS);
        assertThrows(IllegalArgumentException.class, () -> tabla.insertar(null, "X"),
                    "insertar con clave null debe devolver excepcion");

        assertThrows(IllegalArgumentException.class, () -> tabla.insertar(5, null),
                "insertar con valor null debe lanzar excepcion");
    }


    @Test
    public void testBuscarRechazaNull() {
        HashTable<Integer, String> tabla = new HashTable<>(SLOTS);
        assertThrows(IllegalArgumentException.class, () -> tabla.buscar(null),
                "buscar con clave null debe dar excepcion");
    }


    @Test
    public void testGetSlotFueraDeRango() {
        HashTable<Integer, String> tabla = new HashTable<>(SLOTS);

        assertThrows(IllegalArgumentException.class, () -> tabla.getSlot(-1),
                "getSlot con indice negativo tiene que dar excepcion");

        assertThrows(IllegalArgumentException.class, () -> tabla.getSlot(SLOTS),
                "Con indice igual a la cantidad de slots debe lanzar excepcion");
    }


    @Test
    public void testGetCantidadSlots() {
        HashTable<Integer, String> tabla = new HashTable<>(SLOTS);
        assertEquals(SLOTS, tabla.getCantidadSlots(), "La tabla reporta la cantidad de slots con la que fue creada.");
    }




    
}