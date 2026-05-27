package Juego.ordenamientos.ui;

import utils.bitmap.Bitmap;
import utils.bitmap.BitmapViewerConMenu;
import utils.bitmap.BitmapViewerConMenu.MenuAction;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

import java.util.ArrayList;
import java.util.List;

import Juego.ordenamientos.Caja;
import Juego.ordenamientos.PartidaOrdenamientos;
import Juego.ordenamientos.PasoOrdenamiento;


public class RenderOrdenamiento {

    private static final int ANCHO_PANTALLA = 1080;
    private static final int ALTO_PANTALLA = 600;
    private static final int MARGEN_INFERIOR = 120;

    private static Bitmap lienzoUnico;
    private static boolean animacionEnProgreso = false;

    // 🌟 Atributos de clase para retener los Bitmaps de tus imágenes
    private static Bitmap bitmapCajaNormal;
    private static Bitmap bitmapCajaRoja;

    /**
     * Inicializa las texturas de las cajas y monta la UI fija.
     */
    public static void visualizarSimulacion(PartidaOrdenamientos<Caja> partida) {
        List<PasoOrdenamiento<Caja>> historial = partida.getHistorialDePasos();
        if (historial.isEmpty()) return;

        // 🌟 LA SOLUCIÓN: Instanciamos tu clase de recursos para que maneje la carga
        RecursosOrdenamiento recursos = new RecursosOrdenamiento();
        
        // Le asignamos los Bitmaps cargados (o los de auxilio si falla el archivo) a los atributos del Render
        bitmapCajaNormal = recursos.getCajaNormal();
        bitmapCajaRoja   = recursos.getCajaRoja();

        // 2. Preparar el lienzo único donde se va a animar todo
        lienzoUnico = new Bitmap(ANCHO_PANTALLA, ALTO_PANTALLA);
        
        // Dibujamos el estado desordenado inicial para que la ventana no inicie en negro
        dibujarPasoEnBitmap(lienzoUnico, historial.get(0), 0, historial.size(), "¡Listo para empezar!");

        // 3. Crear el menú sin controles manuales de pasos (Evita trampas)
        List<MenuAction> acciones = new ArrayList<>();
        acciones.add(new MenuAction("🚀 Iniciar Animación", () -> {
            if (!animacionEnProgreso) {
                reproducirAnimacion(historial);
            }
        }));

        // Abrimos la ventana pasando el único lienzo reactivo
        BitmapViewerConMenu.showBitmapsWithMenu(acciones, new Bitmap[]{ lienzoUnico });
    }

    /**
     * Hilo en segundo plano que muta el lienzo original secuencialmente
     */
    private static void reproducirAnimacion(List<PasoOrdenamiento<Caja>> historial) {
        animacionEnProgreso = true;
        new Thread(() -> {
            try {
                for (int i = 0; i < historial.size(); i++) {
                    dibujarPasoEnBitmap(lienzoUnico, historial.get(i), i, historial.size(), "Corriendo algoritmo...");
                    Thread.sleep(800); // Velocidad de la animación (800 milisegundos por paso)
                }
                dibujarPasoEnBitmap(lienzoUnico, historial.get(historial.size() - 1), 
                                    historial.size() - 1, historial.size(), "✨ ¡Proceso Terminado!");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                animacionEnProgreso = false;
            }
        }).start();
    }

    /**
     * Borra el canvas y estampa las imágenes correspondientes para este paso
     */
    private static void dibujarPasoEnBitmap(Bitmap bmp, PasoOrdenamiento<Caja> paso, int nroPaso, int totalPasos, String estadoAnim) {
        synchronized (bmp) {
            bmp.rellenar(new Color(30, 30, 30)); // Fondo gris oscuro

            List<Caja> cajas = paso.getCopiasEnEstePaso();
            int cantCajas = cajas.size();
            
            int anchoCaja = (ANCHO_PANTALLA - 100) / cantCajas;
            int espacioEntreCajas = 15;
            anchoCaja -= espacioEntreCajas;

            int maxTam = 1;
            for (Caja c : cajas) {
                if (c.getTamaño() > maxTam) maxTam = c.getTamaño();
            }

            // 🌟 OBTENEMOS EL CONTEXTO GRÁFICO DEL CANVAS Y LE CONFIGURAMOS CALIDAD DE TEXTO
            Graphics2D gTextos = bmp.getImage().createGraphics();
            gTextos.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING, 
                                     java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            gTextos.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING, 
                                     java.awt.RenderingHints.VALUE_RENDER_QUALITY);

            // Dibujado de elementos (Cajas)
            for (int i = 0; i < cantCajas; i++) {
                Caja caja = cajas.get(i);
                int alturaMaximaDibujable = ALTO_PANTALLA - MARGEN_INFERIOR - 70;
                int altoBarra = (caja.getTamaño() * alturaMaximaDibujable) / maxTam;

                int x = 50 + i * (anchoCaja + espacioEntreCajas);
                int y = ALTO_PANTALLA - MARGEN_INFERIOR - altoBarra;

                // SELECCIÓN DE RECURSO
                Bitmap imagenCajaAEstampar = bitmapCajaNormal;
                if (i == paso.getIndice1() || i == paso.getIndice2()) {
                    imagenCajaAEstampar = bitmapCajaRoja;
                }

                // ESTAMPADO de la imagen de la caja escalada
                Graphics2D gCaja = bmp.getImage().createGraphics();
                gCaja.drawImage(imagenCajaAEstampar.getImage(), x, y, anchoCaja, altoBarra, null);
                gCaja.dispose();

                // Marco estético para delimitar el borde de la imagen de la caja
                bmp.drawRectangle(x, y, anchoCaja, altoBarra, Color.WHITE);

                // 🌟 TEXTO DE LA CAJA CENTRADO Y SUAVIZADO
                Font fontCaja = new Font("Segoe UI", Font.BOLD, 14);
                gTextos.setFont(fontCaja);
                gTextos.setColor(Color.WHITE);
                
                String textoCaja = caja.getNombre() + " (" + caja.getTamaño() + ")";
                
                // Calculamos los píxeles exactos de ancho que ocupa la cadena de texto
                java.awt.FontMetrics metricsCaja = gTextos.getFontMetrics(fontCaja);
                int anchoTextoCaja = metricsCaja.stringWidth(textoCaja);
                
                // Restamos la mitad del texto al centro de la caja actual
                int xTextoCentrado = x + (anchoCaja / 2) - (anchoTextoCaja / 2);
                int yTextoCaja = ALTO_PANTALLA - MARGEN_INFERIOR + 25;
                
                gTextos.drawString(textoCaja, xTextoCentrado, yTextoCaja);
            }

            // 🌟 TEXTOS SUPERIORES DIRECTAMENTE DIBUJADOS CON GTEXTOS (ALTA CALIDAD)
            Font fontInfo = new Font("Segoe UI", Font.BOLD, 16);
            gTextos.setFont(fontInfo);

            // Paso en Pantalla
            gTextos.setColor(Color.YELLOW);
            gTextos.drawString("Paso en Pantalla: " + nroPaso + " / " + (totalPasos - 1), 20, 40);

            // Acción del Algoritmo
            gTextos.setColor(Color.GREEN);
            gTextos.drawString("Acción: " + paso.getAccion(), 20, 70);

            // Estado de la Animación (derecha)
            gTextos.setColor(Color.LIGHT_GRAY);
            gTextos.drawString("[ " + estadoAnim + " ]", ANCHO_PANTALLA - 220, 40);

            // 🌟 MUY IMPORTANTE: Liberamos el objeto gráfico de textos al finalizar todo
            gTextos.dispose();
        }
    }

   
}