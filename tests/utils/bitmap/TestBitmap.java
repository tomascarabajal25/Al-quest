package utils.bitmap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.nio.file.Path;

class TestBitmap {

    private Bitmap bitmap;
    private static final int ANCHO  = 100;
    private static final int ALTO   = 100;

    @BeforeEach
    void setUp() {
        bitmap = new Bitmap(ANCHO, ALTO);
    }

    @Test
    void constructor_debeAsignarAnchoCorrectamente() {
        assertEquals(ANCHO, bitmap.getWidth());
    }

    @Test
    void constructor_debeAsignarAltoCorrectamente() {
        assertEquals(ALTO, bitmap.getHeight());
    }

    @Test
    void constructor_imagenNoDebeSerNula() {
        assertNotNull(bitmap.getImage());
    }

    @Test
    void constructor_imagenDebeTenerDimensionesCorrectas() {
        assertEquals(ANCHO, bitmap.getImage().getWidth());
        assertEquals(ALTO,  bitmap.getImage().getHeight());
    }

    @Test
    void constructor_anchoroCero_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> new Bitmap(0, 50));
    }

    @Test
    void constructor_altoCero_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> new Bitmap(50, 0));
    }

    @Test
    void constructor_anchoNegativo_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> new Bitmap(-1, 50));
    }

    @Test
    void constructor_altoNegativo_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> new Bitmap(50, -1));
    }

    @Test
    void drawPixel_debeEstablecerColorCorrectamente() {
        bitmap.drawPixel(10, 10, Color.RED);
        assertEquals(Color.RED.getRGB(), bitmap.getImage().getRGB(10, 10));
    }

    @Test
    void drawPixel_esquinaSuperiorIzquierda_debeEstablecerColor() {
        bitmap.drawPixel(0, 0, Color.BLUE);
        assertEquals(Color.BLUE.getRGB(), bitmap.getImage().getRGB(0, 0));
    }

    @Test
    void drawPixel_colorNull_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> bitmap.drawPixel(5, 5, null));
    }

    @Test
    void drawPixel_xFueraDeRango_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> bitmap.drawPixel(ANCHO + 1, 5, Color.RED));
    }

    @Test
    void drawPixel_yFueraDeRango_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> bitmap.drawPixel(5, ALTO + 1, Color.RED));
    }

    @Test
    void drawLine_horizontal_debePintarPixelesIntermedios() {
        bitmap.drawLine(10, 10, 20, 10, Color.GREEN);
        // Verificamos algunos puntos del segmento
        assertEquals(Color.GREEN.getRGB(), bitmap.getImage().getRGB(10, 10));
        assertEquals(Color.GREEN.getRGB(), bitmap.getImage().getRGB(15, 10));
        assertEquals(Color.GREEN.getRGB(), bitmap.getImage().getRGB(20, 10));
    }

    @Test
    void drawLine_vertical_debePintarPixelesIntermedios() {
        bitmap.drawLine(10, 5, 10, 15, Color.BLUE);
        assertEquals(Color.BLUE.getRGB(), bitmap.getImage().getRGB(10, 5));
        assertEquals(Color.BLUE.getRGB(), bitmap.getImage().getRGB(10, 10));
        assertEquals(Color.BLUE.getRGB(), bitmap.getImage().getRGB(10, 15));
    }

    @Test
    void drawLine_unSoloPunto_debePintarEsePunto() {
        bitmap.drawLine(30, 30, 30, 30, Color.RED);
        assertEquals(Color.RED.getRGB(), bitmap.getImage().getRGB(30, 30));
    }

    @Test
    void drawLine_colorNull_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> bitmap.drawLine(0, 0, 10, 10, null));
    }

    @Test
    void drawRectangle_debePintarLasCuatroEsquinas() {
        bitmap.drawRectangle(10, 10, 20, 20, Color.WHITE);
        assertEquals(Color.WHITE.getRGB(), bitmap.getImage().getRGB(10, 10)); // sup-izq
        assertEquals(Color.WHITE.getRGB(), bitmap.getImage().getRGB(30, 10)); // sup-der
        assertEquals(Color.WHITE.getRGB(), bitmap.getImage().getRGB(10, 30)); // inf-izq
        assertEquals(Color.WHITE.getRGB(), bitmap.getImage().getRGB(30, 30)); // inf-der
    }

    @Test
    void drawRectangle_colorNull_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> bitmap.drawRectangle(5, 5, 10, 10, null));
    }

    @Test
    void drawCircle_debePintarPuntosEnElEje() {
        // El punto más a la derecha del círculo debe estar pintado
        bitmap.drawCircle(50, 50, 10, Color.RED);
        assertEquals(Color.RED.getRGB(), bitmap.getImage().getRGB(60, 50)); // (cx+r, cy)
        assertEquals(Color.RED.getRGB(), bitmap.getImage().getRGB(50, 60)); // (cx, cy+r)
    }

    @Test
    void drawCircle_colorNull_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class, () -> bitmap.drawCircle(50, 50, 10, null));
    }

    @Test
    void rellenar_debePintarTodosLosPixeles() {
        bitmap.rellenar(Color.CYAN);
        for (int x = 0; x < ANCHO; x++) {
            for (int y = 0; y < ALTO; y++) {
                assertEquals(Color.CYAN.getRGB(), bitmap.getImage().getRGB(x, y),
                    "Pixel (" + x + "," + y + ") no tiene el color esperado");
            }
        }
    }

    @Test
    void rellenar_sobreescribeColorAnterior() {
        bitmap.drawPixel(5, 5, Color.RED);
        bitmap.rellenar(Color.BLACK);
        assertEquals(Color.BLACK.getRGB(), bitmap.getImage().getRGB(5, 5));
    }

    @Test
    void pasteBitmap_debeCopirarPixelesEnPosicionCorrecta() {
        Bitmap otro = new Bitmap(10, 10);
        otro.rellenar(Color.MAGENTA);

        bitmap.pasteBitmap(otro, 5, 5);

        assertEquals(Color.MAGENTA.getRGB(), bitmap.getImage().getRGB(5, 5));
        assertEquals(Color.MAGENTA.getRGB(), bitmap.getImage().getRGB(14, 14));
    }

    @Test
    void saveToFile_debeCrearArchivoEnDisco(@TempDir Path tempDir) throws IOException {
        String ruta = tempDir.resolve("test.bmp").toString();
        bitmap.rellenar(Color.RED);
        bitmap.saveToFile(ruta);

        assertTrue(new java.io.File(ruta).exists());
    }

    @Test
    void saveToFile_debeRetornarRutaAbsoluta(@TempDir Path tempDir) throws IOException {
        String ruta = tempDir.resolve("test.bmp").toString();
        String retorno = bitmap.saveToFile(ruta);
        assertNotNull(retorno);
        assertFalse(retorno.isBlank());
    }

    @Test
    void loadFromFile_debeCargarImagenConDimensionesCorrectas(@TempDir Path tempDir) throws IOException {
        String ruta = tempDir.resolve("test.bmp").toString();
        bitmap.saveToFile(ruta);

        Bitmap cargado = Bitmap.loadFromFile(ruta);
        assertEquals(ANCHO, cargado.getWidth());
        assertEquals(ALTO,  cargado.getHeight());
    }

    @Test
    void loadFromFile_debePreservarColores(@TempDir Path tempDir) throws IOException {
        bitmap.rellenar(Color.RED);
        String ruta = tempDir.resolve("test.bmp").toString();
        bitmap.saveToFile(ruta);

        Bitmap cargado = Bitmap.loadFromFile(ruta);
        assertEquals(Color.RED.getRGB(), cargado.getImage().getRGB(0, 0));
    }

    @Test
    void drawText_noDebeLanzarExcepcion() {
        Font fuente = new Font("Arial", Font.PLAIN, 12);
        assertDoesNotThrow(() -> bitmap.drawText("Hola", 5, 20, fuente, Color.WHITE, Color.BLACK));
    }

    @Test
    void drawText_fuenteNull_debeLanzarExcepcion() {
        assertThrows(RuntimeException.class,
            () -> bitmap.drawText("Hola", 5, 20, null, Color.WHITE, Color.BLACK));
    }

    @Test
    void drawText_colorNull_debeLanzarExcepcion() {
        Font fuente = new Font("Arial", Font.PLAIN, 12);
        assertThrows(RuntimeException.class,
            () -> bitmap.drawText("Hola", 5, 20, fuente, null, Color.BLACK));
    }

    @Test
    void dibujarTablero_noDebeLanzarExcepcion() {
        // 3 casillas de 10px cada una caben en el bitmap 100x100
        assertDoesNotThrow(() -> bitmap.dibujarTablero(0, 0, 3, 10, Color.WHITE));
    }

    @Test
    void dibujarTablero_debePintarEsquinaDePrimeraCasilla() {
        bitmap.dibujarTablero(0, 0, 2, 10, Color.WHITE);
        // La esquina superior izquierda de la primera casilla debe estar pintada
        assertEquals(Color.WHITE.getRGB(), bitmap.getImage().getRGB(0, 0));
    }
}
