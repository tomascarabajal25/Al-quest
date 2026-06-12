package utils;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import javax.imageio.ImageIO;

public class convertidosSprites {

    public static void main(String[] args) {
        String carpetaOrigen  = "assets/objetos/";
        String carpetaDestino = "assets/objetos/";

        File dirDestino = new File(carpetaDestino);
        if (!dirDestino.exists()) dirDestino.mkdirs();

        String[] archivos = {
            "door"
        };

        for (String nombre : archivos) {
            File archivoOrigen  = new File(carpetaOrigen  + nombre + ".png");
            File archivoDestino = new File(carpetaDestino + nombre + ".bmp");

            if (!archivoOrigen.exists()) {
                System.out.println("ERROR: No se encontró -> " + archivoOrigen.getAbsolutePath());
                continue;
            }

            try {
                // 1. Leer PNG original
                BufferedImage imgOriginal = ImageIO.read(archivoOrigen);

                // 2. Convertir a TYPE_INT_ARGB asegurándonos de preservar la transparencia
                BufferedImage imgArgb = new BufferedImage(
                    imgOriginal.getWidth(),
                    imgOriginal.getHeight(),
                    BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D g2d = imgArgb.createGraphics();
                g2d.drawImage(imgOriginal, 0, 0, null);
                g2d.dispose();

                // 3. Escribir BMP 32-bit con alfa manualmente
                escribirBmp32(imgArgb, archivoDestino);

                System.out.println("Convertido: " + nombre + ".bmp");
                System.out.println(" -> Ruta:   " + archivoDestino.getAbsolutePath());
                System.out.println(" -> Tamaño: " + archivoDestino.length() + " bytes\n");

            } catch (IOException e) {
                System.out.println("ERROR al procesar: " + nombre);
                e.printStackTrace();
            }
        }
    }

    /**
     * Escribe un BMP de 32 bits (BGRA) con canal alfa.
     * Usa BITMAPV4HEADER (108 bytes) para que el canal alfa sea reconocido
     * correctamente por la mayoría de aplicaciones y engines.
     */
    private static void escribirBmp32(BufferedImage img, File destino) throws IOException {
        int w = img.getWidth();
        int h = img.getHeight();

        // En BMP las filas van de abajo hacia arriba
        int rowSize    = w * 4;           // 4 bytes por pixel (BGRA)
        int pixelData  = rowSize * h;
        int headerSize = 108;             // BITMAPV4HEADER
        int fileSize   = 14 + headerSize + pixelData;

        ByteBuffer buf = ByteBuffer.allocate(fileSize);
        buf.order(ByteOrder.LITTLE_ENDIAN);

        // ── BITMAPFILEHEADER (14 bytes) ──────────────────────────────────
        buf.put((byte)'B');
        buf.put((byte)'M');
        buf.putInt(fileSize);             // Tamaño total del archivo
        buf.putShort((short) 0);          // Reservado 1
        buf.putShort((short) 0);          // Reservado 2
        buf.putInt(14 + headerSize);      // Offset al inicio de los pixels

        // ── BITMAPV4HEADER (108 bytes) ───────────────────────────────────
        buf.putInt(headerSize);           // Tamaño del header
        buf.putInt(w);                    // Ancho
        buf.putInt(-h);                   // Alto negativo = top-down (más fácil de leer)
        buf.putShort((short) 1);          // Planos de color
        buf.putShort((short) 32);         // Bits por pixel
        buf.putInt(3);                    // Compresión: BI_BITFIELDS
        buf.putInt(pixelData);            // Tamaño de los datos de pixel
        buf.putInt(2835);                 // Pixels por metro X (~72 dpi)
        buf.putInt(2835);                 // Pixels por metro Y
        buf.putInt(0);                    // Colores en la tabla
        buf.putInt(0);                    // Colores importantes

        // Máscaras RGBA (orden BGRA en memoria → máscaras invertidas)
        buf.putInt(0x00FF0000);           // Máscara Rojo
        buf.putInt(0x0000FF00);           // Máscara Verde
        buf.putInt(0x000000FF);           // Máscara Azul
        buf.putInt(0xFF000000);           // Máscara Alfa  ← clave para transparencia

        // Espacio de color: LCS_sRGB
        buf.putInt(0x73524742);           // 'sRGB' en little-endian
        // 36 bytes de endpoints (ceros para sRGB)
        for (int i = 0; i < 36; i++) buf.put((byte) 0);
        // Gamma RGB (ceros = ignorar)
        buf.putInt(0);
        buf.putInt(0);
        buf.putInt(0);

        // ── DATOS DE PIXEL (BGRA, top-down porque pusimos alto negativo) ─
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int argb = img.getRGB(x, y);
                byte a = (byte)((argb >> 24) & 0xFF);
                byte r = (byte)((argb >> 16) & 0xFF);
                byte g = (byte)((argb >>  8) & 0xFF);
                byte b = (byte)( argb        & 0xFF);
                // BMP almacena en orden BGRA
                buf.put(b);
                buf.put(g);
                buf.put(r);
                buf.put(a);
            }
        }

        // Escribir todo al archivo de una sola vez
        try (FileOutputStream fos = new FileOutputStream(destino)) {
            fos.write(buf.array());
        }
    }
}