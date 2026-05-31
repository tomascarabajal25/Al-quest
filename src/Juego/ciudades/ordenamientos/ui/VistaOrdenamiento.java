package Juego.ciudades.ordenamientos.ui;

import utils.bitmap.Bitmap;
import utils.bitmap.BitmapViewerConMenu;
import utils.bitmap.BitmapViewerConMenu.MenuAction;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import Juego.ciudades.ordenamientos.PasoOrdenamiento;

/**
 * Vista BMP genérica de la Ciudad 4 – Ordenamientos.
 *
 * No conoce el tipo T concreto. Recibe dos lambdas del caller:
 *   - dibujador:  sabe pintar T y calcular su alto proporcional
 *   - etiqueta:   convierte T en el String a mostrar debajo de cada caja
 *
 * La ventana se abre UNA sola vez en el constructor y nunca se vuelve a abrir.
 * El canvas se muta directamente; el BitmapViewer lo refresca cada 500ms.
 *
 * @param <T> tipo de elemento comparable que se ordena
 */
public class VistaOrdenamiento<T extends Comparable<T>> {

    // ── Dimensiones ───────────────────────────────────────────────────────────
    private static final int W          = 1080;
    private static final int H          = 600;
    private static final int MARGEN_INF = 130;   // espacio inferior para etiquetas

    // ── Paleta ────────────────────────────────────────────────────────────────
    private static final Color BG        = new Color(30,  30,  30);
    private static final Color GOLD      = new Color(0xC9A84C);
    private static final Color GOLD_DIM  = new Color(0x7A6028);
    private static final Color TEAL      = new Color(0x3ECFCF);
    private static final Color TEXT      = new Color(0xEDE8D5);
    private static final Color TEXT_DIM  = new Color(0x7A7565);
    private static final Color GREEN     = new Color(0x4CAF50);
    private static final Color RED       = new Color(0xE05050);
    private static final Color PANEL_BOR = new Color(0x3A3050);

    // ── Fuentes ───────────────────────────────────────────────────────────────
    private static final Font F_TITLE = new Font("Segoe UI", Font.BOLD,  20);
    private static final Font F_LABEL = new Font("Segoe UI", Font.BOLD,  15);
    private static final Font F_BODY  = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font F_SMALL = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font F_BIG   = new Font("Segoe UI", Font.BOLD,  36);

    // ── Lambdas externos ─────────────────────────────────────────────────────
    private final DibujarElemento<T>  dibujador;
    private final Function<T, String> etiqueta;

    // ── Canvas (una sola ventana, nunca se reabre) ────────────────────────────
    private final Bitmap canvas;

    // ── Sincronización ────────────────────────────────────────────────────────
    private volatile String  inputJugador       = null;
    private volatile boolean animacionSolicitada = false;
    private final Object lockInput  = new Object();
    private final Object lockBoton  = new Object();

    // ─────────────────────────────────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * pre:  dibujador != null, etiqueta != null
     * post: abre la ventana BMP UNA sola vez con el botón de inicio
     *
     * @param dibujador lambda que pinta T y devuelve el alto real usado
     * @param etiqueta  lambda que convierte T en texto legible (ej: "A (40)")
     */
    public VistaOrdenamiento(DibujarElemento<T> dibujador, Function<T, String> etiqueta) {
        this.dibujador = dibujador;
        this.etiqueta  = etiqueta;
        this.canvas    = new Bitmap(W, H);

        dibujarMensajeCentrado("Presiona 'Iniciar animacion' cuando estes listo.");

        List<MenuAction> acciones = new ArrayList<>();
        acciones.add(new MenuAction("Iniciar animacion", () -> {
            synchronized (lockBoton) {
                animacionSolicitada = true;
                lockBoton.notifyAll();
            }
        }));
        acciones.add(new MenuAction("Ingresar orden", () -> pedirInputSwing()));

        // UNA sola apertura de ventana — el canvas se refresca automáticamente
        BitmapViewerConMenu.showBitmapsWithMenu(acciones, new Bitmap[]{ canvas });
    }

    // ─────────────────────────────────────────────────────────────────────────
    // API pública
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Bloquea hasta que el jugador presiona "Iniciar animacion",
     * luego reproduce la animación a 1500ms por paso.
     *
     * pre:  historial no nulo y no vacío
     */
    public void animarOrdenamiento(List<PasoOrdenamiento<T>> historial,
                                   String nombreAlgoritmo) {
        synchronized (lockBoton) {
            while (!animacionSolicitada) {
                try { lockBoton.wait(); } catch (InterruptedException ignored) {}
            }
        }
        try {
            for (int i = 0; i < historial.size(); i++) {
                dibujarPaso(historial.get(i), i, historial.size(), nombreAlgoritmo, "Animando...");
                Thread.sleep(1500);
            }
            // Último frame: mensaje de memorización
            dibujarPaso(historial.get(historial.size() - 1),
                    historial.size() - 1, historial.size(),
                    nombreAlgoritmo, "Memoriza el orden!");
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
    }

    /**
     * Muestra las cajas mezcladas del paso desafío y bloquea
     * hasta que el jugador ingresa su respuesta con "Ingresar orden".
     *
     * pre:  paso != null
     * post: devuelve la lista en el orden elegido, o null si el input es inválido
     */
    public List<T> mostrarDesafioYEsperar(PasoOrdenamiento<T> paso,
                                          int nroPaso, int totalPasos) {
        List<T> mezcladas = new ArrayList<>(paso.getCopiasEnEstePaso());
        Collections.shuffle(mezcladas);

        // Solo mutamos el canvas — NO abrimos otra ventana
        dibujarPantallaDesafio(mezcladas, nroPaso);

        synchronized (lockInput) {
            inputJugador = null;
            while (inputJugador == null) {
                try { lockInput.wait(); } catch (InterruptedException ignored) {}
            }
        }

        return parsearRespuesta(inputJugador, mezcladas);
    }

    /**
     * Muestra feedback y espera 2.5s antes de retornar.
     *
     * pre:  pasoReal != null, respuestaJugador != null
     */
    public void mostrarFeedback(boolean acerto,
                                 PasoOrdenamiento<T> pasoReal,
                                 List<T> respuestaJugador) {
        dibujarPantallaFeedback(acerto, pasoReal, respuestaJugador);
        try { Thread.sleep(2500); } catch (InterruptedException ignored) {}
    }

    /**
     * post: pinta la pantalla de victoria
     */
    public void mostrarVictoria(int aciertos, int rondas) {
        dibujarPantallaVictoria(aciertos, rondas);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Input del jugador
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Abre un JOptionPane sin tocar ni reabrir la ventana BMP.
     * Notifica al hilo de juego cuando el jugador confirma.
     */
    private void pedirInputSwing() {
        // Obtenemos la cantidad de cajas del canvas actual de forma segura
        // pasando un mensaje genérico — el jugador ya ve los números en el canvas
        String input = javax.swing.JOptionPane.showInputDialog(
                null,
                "Ingresa los numeros en orden separados por coma.\nEj: 2,4,1,3",
                "Tu respuesta",
                javax.swing.JOptionPane.QUESTION_MESSAGE);

        if (input != null && !input.trim().isEmpty()) {
            synchronized (lockInput) {
                inputJugador = input.trim();
                lockInput.notifyAll();
            }
        }
    }

    /**
     * Convierte "2,1,3" en la lista de elementos en ese orden.
     *
     * pre:  input != null, mezcladas != null
     * post: devuelve la lista reordenada, o null si el formato es inválido
     */
    private List<T> parsearRespuesta(String input, List<T> mezcladas) {
        try {
            String[] partes  = input.split(",");
            List<T>  resultado = new ArrayList<>();
            for (String parte : partes) {
                int idx = Integer.parseInt(parte.trim()) - 1; // 1-based → 0-based
                if (idx < 0 || idx >= mezcladas.size()) { return null; }
                resultado.add(mezcladas.get(idx));
            }
            return resultado.size() == mezcladas.size() ? resultado : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Dibujo — pantallas
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Dibuja un paso de la animación. El dibujador calcula el alto proporcional.
     *
     * pre:  paso != null, nroPaso >= 0
     */
    private void dibujarPaso(PasoOrdenamiento<T> paso, int nroPaso,
                              int totalPasos, String algoritmo, String estado) {
        synchronized (canvas) {
            limpiar();

            List<T> elementos  = paso.getCopiasEnEstePaso();
            int cant           = elementos.size();
            int anchoCaja      = (W - 100) / cant - 15;
            int altoMaximo     = H - MARGEN_INF - 60;

            Graphics2D g = canvas.getImage().createGraphics();
            aplicarCalidad(g);

            for (int i = 0; i < cant; i++) {
                T    elem     = elementos.get(i);
                boolean dest  = (i == paso.getIndice1() || i == paso.getIndice2());
                int x         = 50 + i * (anchoCaja + 15);

                // El lambda calcula su propio alto y dibuja desde la base
                int altoReal  = dibujador.dibujar(g, elem, elementos,
                        x, H - MARGEN_INF, anchoCaja, altoMaximo, dest);

                // Marco encima del dibujador para destacar bordes
                canvas.drawRectangle(x, H - MARGEN_INF - altoReal, anchoCaja, altoReal,
                        dest ? RED : Color.WHITE);

                // Etiqueta legible debajo
                dibujarCentrado(g, etiqueta.apply(elem), F_BODY, TEXT,
                        x + anchoCaja / 2, H - MARGEN_INF + 25);
            }

            // Info superior
            g.setFont(F_LABEL);
            g.setColor(Color.YELLOW);
            g.drawString(algoritmo + "  —  Paso " + nroPaso + "/" + (totalPasos - 1), 20, 35);
            g.setColor(GREEN);
            g.drawString("Accion: " + paso.getAccion(), 20, 60);
            g.setColor(Color.LIGHT_GRAY);
            g.drawString("[ " + estado + " ]", W - 260, 35);

            g.dispose();
        }
    }

    /**
     * Pantalla de desafío: cajas mezcladas numeradas para que el jugador ordene.
     *
     * pre:  mezcladas != null y no vacía
     */
    private void dibujarPantallaDesafio(List<T> mezcladas, int nroPaso) {
        synchronized (canvas) {
            limpiar();
            dibujarMarco();

            Graphics2D g = canvas.getImage().createGraphics();
            aplicarCalidad(g);

            g.setFont(F_TITLE);
            g.setColor(GOLD);
            g.drawString("DESAFIO: paso " + nroPaso
                    + " — ordena las cajas como estaban", 40, 48);

            g.setFont(F_SMALL);
            g.setColor(TEXT_DIM);
            g.drawString("Presiona 'Ingresar orden' y escribi los numeros separados"
                    + " por coma. Ej: 2,4,1,3", 40, 70);

            int anchoCaja = 110;
            int sep       = 20;
            int altoFijo  = 140;   // alto fijo para el desafío (no proporcional)
            int yBase     = 110;

            for (int i = 0; i < mezcladas.size(); i++) {
                T   elem = mezcladas.get(i);
                int x    = 40 + i * (anchoCaja + sep);

                // Dibujamos con alto fijo (altoMaximo == altoFijo, el lambda decide)
                dibujador.dibujar(g, elem, mezcladas, x, yBase + altoFijo,
                        anchoCaja, altoFijo, false);
                canvas.drawRectangle(x, yBase, anchoCaja, altoFijo, Color.WHITE);

                // Etiqueta del elemento (nombre + tamaño)
                dibujarCentrado(g, etiqueta.apply(elem), F_BODY, TEXT,
                        x + anchoCaja / 2, yBase + altoFijo + 20);

                // Número de referencia para el jugador
                dibujarCentrado(g, String.valueOf(i + 1), F_LABEL, GOLD,
                        x + anchoCaja / 2, yBase + altoFijo + 40);
            }

            g.setFont(F_LABEL);
            g.setColor(TEAL);
            g.drawString("Escribe el orden usando los numeros debajo de cada caja.",
                    40, yBase + altoFijo + 75);

            g.dispose();
        }
    }

    /**
     * Pantalla de feedback: respuesta del jugador vs orden real.
     *
     * pre:  pasoReal != null, respuestaJugador != null
     */
    private void dibujarPantallaFeedback(boolean acerto,
                                          PasoOrdenamiento<T> pasoReal,
                                          List<T> respuestaJugador) {
        synchronized (canvas) {
            limpiar();
            dibujarMarco();

            Graphics2D g = canvas.getImage().createGraphics();
            aplicarCalidad(g);

            g.setFont(F_BIG);
            g.setColor(acerto ? GREEN : RED);
            dibujarCentrado(g, acerto ? "CORRECTO!" : "INCORRECTO", F_BIG,
                    acerto ? GREEN : RED, W / 2, 75);

            g.setFont(F_LABEL);
            g.setColor(TEAL);
            g.drawString("Tu respuesta:", 40, 125);
            dibujarFilaFija(g, respuestaJugador, 40, 135, acerto ? GREEN : RED);

            canvas.drawLine(30, H / 2 + 10, W - 30, H / 2 + 10, GOLD_DIM);

            g.setFont(F_LABEL);
            g.setColor(GOLD);
            g.drawString("Orden correcto:", 40, H / 2 + 38);
            dibujarFilaFija(g, pasoReal.getCopiasEnEstePaso(), 40, H / 2 + 48, GREEN);

            g.dispose();
        }
    }

    /** Pantalla de victoria */
    private void dibujarPantallaVictoria(int aciertos, int rondas) {
        synchronized (canvas) {
            limpiar();
            dibujarMarco();

            Graphics2D g = canvas.getImage().createGraphics();
            aplicarCalidad(g);

            g.setFont(F_BIG);
            g.setColor(GOLD);
            dibujarCentrado(g, "CIUDAD CONQUISTADA!", F_BIG, GOLD, W / 2, 160);

            g.setFont(F_LABEL);
            g.setColor(TEAL);
            dibujarCentrado(g, "Aciertos: " + aciertos + " de " + rondas + " rondas",
                    F_LABEL, TEAL, W / 2, 230);

            g.setFont(F_BODY);
            g.setColor(TEXT);
            dibujarCentrado(g, "Ya dominas el ordenamiento!", F_BODY, TEXT, W / 2, 285);

            g.setFont(F_SMALL);
            g.setColor(TEXT_DIM);
            dibujarCentrado(g, "Presiona Salir para volver al mapa.", F_SMALL, TEXT_DIM, W / 2, 335);

            g.dispose();
        }
    }

    /**
     * Dibuja una fila de cajas con alto fijo (para feedback/desafío).
     * Usa el lambda dibujador con alto fijo y etiqueta legible.
     *
     * pre:  elementos != null
     */
    private void dibujarFilaFija(Graphics2D g, List<T> elementos,
                                  int xOrigen, int yOrigen, Color borde) {
        int anchoCaja = 90;
        int altoFijo  = 90;
        int sep       = 15;

        for (int i = 0; i < elementos.size(); i++) {
            T   elem = elementos.get(i);
            int x    = xOrigen + i * (anchoCaja + sep);

            dibujador.dibujar(g, elem, elementos, x, yOrigen + altoFijo,
                    anchoCaja, altoFijo, false);
            canvas.drawRectangle(x, yOrigen, anchoCaja, altoFijo, borde);
            dibujarCentrado(g, etiqueta.apply(elem), F_BODY, TEXT,
                    x + anchoCaja / 2, yOrigen + altoFijo - 15);
        }
    }

    private void dibujarMensajeCentrado(String mensaje) {
        limpiar();
        dibujarMarco();
        Graphics2D g = canvas.getImage().createGraphics();
        aplicarCalidad(g);
        g.setFont(F_LABEL);
        g.setColor(TEXT);
        dibujarCentrado(g, mensaje, F_LABEL, TEXT, W / 2, H / 2);
        g.dispose();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private void limpiar() { canvas.rellenar(BG); }

    private void dibujarMarco() {
        canvas.drawRectangle(4,  4,  W - 10, H - 10, GOLD_DIM);
        canvas.drawRectangle(10, 10, W - 22, H - 22, PANEL_BOR);
    }

    private void dibujarCentrado(Graphics2D g, String texto, Font f, Color c,
                                  int xCentro, int y) {
        g.setFont(f);
        g.setColor(c);
        int ancho = g.getFontMetrics(f).stringWidth(texto);
        g.drawString(texto, xCentro - ancho / 2, y);
    }

    private void aplicarCalidad(Graphics2D g) {
        g.setRenderingHint(java.awt.RenderingHints.KEY_TEXT_ANTIALIASING,
                           java.awt.RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(java.awt.RenderingHints.KEY_RENDERING,
                           java.awt.RenderingHints.VALUE_RENDER_QUALITY);
    }
}