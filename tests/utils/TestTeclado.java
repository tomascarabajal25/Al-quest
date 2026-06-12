package utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.NoSuchElementException;

class TestTeclado {

    // Redirige System.in con el texto dado e inicializa el teclado
    private void simularEntrada(String texto) {
        System.setIn(new ByteArrayInputStream(texto.getBytes()));
        Teclado.inicializar();
    }

    @AfterEach
    void tearDown() {
        // Cerramos el scanner si quedó abierto para no ensuciar el siguiente test
        if (Teclado.teclado != null) {
            Teclado.finalizar();
            Teclado.teclado = null;
        }
    }

    // ─────────────────────────────────────────────
    // inicializar / finalizar
    // ─────────────────────────────────────────────

    @Test
    void inicializar_debeCrearElScanner() {
        simularEntrada("");
        assertNotNull(Teclado.teclado);
    }

    @Test
    void finalizar_debePoderLlamarseTrasCerrar() {
        simularEntrada("");
        assertDoesNotThrow(() -> Teclado.finalizar());
    }

    // ─────────────────────────────────────────────
    // leerTexto
    // ─────────────────────────────────────────────

    @Test
    void leerTexto_debeRetornarLineaCompleta() {
        simularEntrada("Hola Mundo\n");
        assertEquals("Hola Mundo", Teclado.leerTexto());
    }

    @Test
    void leerTexto_lineaVacia_debeRetornarStringVacio() {
        simularEntrada("\n");
        assertEquals("", Teclado.leerTexto());
    }

    @Test
    void leerTexto_conEspacios_debeRetornarTextoCompleto() {
        simularEntrada("   hola   \n");
        assertEquals("   hola   ", Teclado.leerTexto());
    }

    @Test
    void leerTexto_variasLineas_debeRetornarSoloPrimera() {
        simularEntrada("primera\nsegunda\n");
        assertEquals("primera", Teclado.leerTexto());
    }

    // ─────────────────────────────────────────────
    // leerCaracter
    // ─────────────────────────────────────────────

    @Test
    void leerCaracter_debeRetornarPrimerCaracter() {
        simularEntrada("A\n");
        assertEquals('A', Teclado.leerCaracter());
    }

    @Test
    void leerCaracter_conPalabraLarga_debeRetornarSoloPrimerCaracter() {
        simularEntrada("hola\n");
        assertEquals('h', Teclado.leerCaracter());
    }

    @Test
    void leerCaracter_conNumero_debeRetornarDigito() {
        simularEntrada("7\n");
        assertEquals('7', Teclado.leerCaracter());
    }

    @Test
    void leerCaracter_entradaVacia_debeLanzarExcepcion() {
        simularEntrada("");
        assertThrows(NoSuchElementException.class, () -> Teclado.leerCaracter());
    }

    // ─────────────────────────────────────────────
    // leerEntero
    // ─────────────────────────────────────────────

    @Test
    void leerEntero_valorPositivo_debeRetornarCorrectamente() {
        simularEntrada("42\n");
        assertEquals(42, Teclado.leerEntero());
    }

    @Test
    void leerEntero_valorNegativo_debeRetornarCorrectamente() {
        simularEntrada("-10\n");
        assertEquals(-10, Teclado.leerEntero());
    }

    @Test
    void leerEntero_cero_debeRetornarCero() {
        simularEntrada("0\n");
        assertEquals(0, Teclado.leerEntero());
    }

    @Test
    void leerEntero_entradaNoNumerica_debeLanzarExcepcion() {
        simularEntrada("abc\n");
        assertThrows(Exception.class, () -> Teclado.leerEntero());
    }

    @Test
    void leerEntero_entradaVacia_debeLanzarExcepcion() {
        simularEntrada("");
        assertThrows(NoSuchElementException.class, () -> Teclado.leerEntero());
    }
}
