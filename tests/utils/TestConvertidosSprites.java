package utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Path;

class TestConvertidosSprites {

    private void invocarEscribirBmp32(BufferedImage img, File destino) throws Exception {
        Method metodo = convertidosSprites.class.getDeclaredMethod("escribirBmp32", BufferedImage.class, File.class);
        metodo.setAccessible(true);
        metodo.invoke(null, img, destino);
    }

    // Helper: crea una imagen ARGB simple de n x n pixeles con un color dado
    private BufferedImage crearImagen(int ancho, int alto, int colorArgb) {
        BufferedImage img = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                img.setRGB(x, y, colorArgb);
            }
        }
        return img;
    }

    @Test
    void escribirBmp32_debeCrearElArchivo(@TempDir Path tempDir) throws Exception {
        File destino = tempDir.resolve("test.bmp").toFile();
        BufferedImage img = crearImagen(2, 2, 0xFF_FF0000); // rojo opaco

        invocarEscribirBmp32(img, destino);

        assertTrue(destino.exists());
    }

    @Test
    void escribirBmp32_tamanioArchivoEsElEsperado(@TempDir Path tempDir) throws Exception {
        int w = 4, h = 3;
        File destino = tempDir.resolve("test.bmp").toFile();
        BufferedImage img = crearImagen(w, h, 0xFF_00FF00);

        invocarEscribirBmp32(img, destino);

        // fileSize = 14 (BITMAPFILEHEADER) + 108 (BITMAPV4HEADER) + w*h*4 (pixels)
        long esperado = 14L + 108L + (long) w * h * 4;
        assertEquals(esperado, destino.length());
    }

    @Test
    void escribirBmp32_archivoComienzaConFirmaBM(@TempDir Path tempDir) throws Exception {
        File destino = tempDir.resolve("test.bmp").toFile();
        invocarEscribirBmp32(crearImagen(1, 1, 0xFF_FFFFFF), destino);

        byte[] bytes = java.nio.file.Files.readAllBytes(destino.toPath());
        assertEquals('B', bytes[0]);
        assertEquals('M', bytes[1]);
    }

    @Test
    void escribirBmp32_offsetPixelesEsCorrecto(@TempDir Path tempDir) throws Exception {
        File destino = tempDir.resolve("test.bmp").toFile();
        invocarEscribirBmp32(crearImagen(1, 1, 0xFF_FFFFFF), destino);

        byte[] bytes = java.nio.file.Files.readAllBytes(destino.toPath());
        // Offset al pixel data está en bytes 10-13 (little-endian)
        int offset = ByteBuffer.wrap(bytes, 10, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        assertEquals(14 + 108, offset); // 122
    }

    @Test
    void escribirBmp32_tamañoHeaderEsCorrecto(@TempDir Path tempDir) throws Exception {
        File destino = tempDir.resolve("test.bmp").toFile();
        invocarEscribirBmp32(crearImagen(1, 1, 0xFF_FFFFFF), destino);

        byte[] bytes = java.nio.file.Files.readAllBytes(destino.toPath());
        // Tamaño del DIB header en bytes 14-17
        int headerSize = ByteBuffer.wrap(bytes, 14, 4).order(ByteOrder.LITTLE_ENDIAN).getInt();
        assertEquals(108, headerSize); // BITMAPV4HEADER
    }

    @Test
    void escribirBmp32_bitsPorPixelEsCorrecto(@TempDir Path tempDir) throws Exception {
        File destino = tempDir.resolve("test.bmp").toFile();
        invocarEscribirBmp32(crearImagen(1, 1, 0xFF_FFFFFF), destino);

        byte[] bytes = java.nio.file.Files.readAllBytes(destino.toPath());
        // Bits por pixel en bytes 28-29
        short bpp = ByteBuffer.wrap(bytes, 28, 2).order(ByteOrder.LITTLE_ENDIAN).getShort();
        assertEquals(32, bpp);
    }

    @Test
    void escribirBmp32_pixelTransparente_alfaEsCero(@TempDir Path tempDir) throws Exception {
        // Pixel completamente transparente: alfa = 0x00
        File destino = tempDir.resolve("test.bmp").toFile();
        BufferedImage img = crearImagen(1, 1, 0x00_FF0000); // rojo transparente

        invocarEscribirBmp32(img, destino);

        byte[] bytes = java.nio.file.Files.readAllBytes(destino.toPath());
        // Primer pixel está en el offset 122, orden BGRA
        // A está en posición 122 + 3
        assertEquals(0x00, bytes[122 + 3] & 0xFF);
    }

    @Test
    void escribirBmp32_pixelOpaco_alfaEs255(@TempDir Path tempDir) throws Exception {
        File destino = tempDir.resolve("test.bmp").toFile();
        BufferedImage img = crearImagen(1, 1, 0xFF_0000FF); // azul opaco

        invocarEscribirBmp32(img, destino);

        byte[] bytes = java.nio.file.Files.readAllBytes(destino.toPath());
        assertEquals(0xFF, bytes[122 + 3] & 0xFF);
    }

    @Test
    void escribirBmp32_ordenBgraEsCorrecto(@TempDir Path tempDir) throws Exception {
        // Color ARGB: A=FF, R=11, G=22, B=33
        int argb = 0xFF_112233;
        File destino = tempDir.resolve("test.bmp").toFile();
        invocarEscribirBmp32(crearImagen(1, 1, argb), destino);

        byte[] bytes = java.nio.file.Files.readAllBytes(destino.toPath());
        int pixelOffset = 122;
        assertEquals(0x33, bytes[pixelOffset]     & 0xFF); // B
        assertEquals(0x22, bytes[pixelOffset + 1] & 0xFF); // G
        assertEquals(0x11, bytes[pixelOffset + 2] & 0xFF); // R
        assertEquals(0xFF, bytes[pixelOffset + 3] & 0xFF); // A
    }

    @Test
    void escribirBmp32_imagen1x1_generaArchivoCorrecto(@TempDir Path tempDir) throws Exception {
        File destino = tempDir.resolve("1x1.bmp").toFile();
        invocarEscribirBmp32(crearImagen(1, 1, 0xFF_FFFFFF), destino);

        long esperado = 14L + 108L + 4L;
        assertEquals(esperado, destino.length());
    }

    @Test
    void escribirBmp32_imagenGrande_generaArchivoCorrecto(@TempDir Path tempDir) throws Exception {
        int w = 64, h = 64;
        File destino = tempDir.resolve("64x64.bmp").toFile();
        invocarEscribirBmp32(crearImagen(w, h, 0xFF_AABBCC), destino);

        long esperado = 14L + 108L + (long) w * h * 4;
        assertEquals(esperado, destino.length());
    }
}
