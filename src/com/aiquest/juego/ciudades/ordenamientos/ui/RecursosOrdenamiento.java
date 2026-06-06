package com.aiquest.juego.ciudades.ordenamientos.ui;

import com.aiquest.utils.bitmap.Bitmap;
import java.io.IOException;

public class RecursosOrdenamiento {

    private Bitmap cajaNormal;
    private Bitmap cajaRoja;

    public RecursosOrdenamiento() {
        
        try {
            // 2. Probamos cargar usando la ruta con barras normales (sirve tanto para Windows como Linux/Mac)
            // Probá primero con "src/com.aiquest.com.aiquest.imagenesDeOrdenamiento/caja_azul.bmp"
            cajaNormal = Bitmap.loadFromFile("src/imagenesDeOrdenamiento/caja_azul.bmp");
            cajaRoja   = Bitmap.loadFromFile("src/imagenesDeOrdenamiento/caja_roja.bmp");

            System.out.println("[Recursos] ¡Felicidades! Las imágenes cargaron de verdad de los archivos físicos.");

        } catch (IOException e) {
            System.out.println("[ALERTA] Falló la carga física. Usando cajas de auxilio generadas por código.");
            System.out.println("Detalle del error: " + e.getMessage());
            
            // Tu lógica de auxilio para que el com.aiquest.com.aiquest.juego no se rompa si no encuentra el archivo
            cajaNormal = new Bitmap(50, 50); cajaNormal.rellenar(java.awt.Color.BLUE);
            cajaRoja   = new Bitmap(50, 50); cajaRoja.rellenar(java.awt.Color.RED);
        }
    }

    public Bitmap getCajaNormal() { return cajaNormal; }
    public Bitmap getCajaRoja()   { return cajaRoja; }
}