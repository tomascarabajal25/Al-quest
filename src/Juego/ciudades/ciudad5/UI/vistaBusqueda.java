package Juego.ciudades.ciudad5.UI;

import utils.bitmap.Bitmap;
import utils.bitmap.BitmapViewerConMenu;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

/**
 * Vista BMP de la Ciudad 5 – Búsqueda: Lista vs Árbol.
 *
 * Mecánica: se muestra una palabra al azar con sus tiempos de búsqueda
 * (lista y árbol) y el jugador debe adivinar qué estructura fue más rápida.
 * Gana cuando acumula {@code ACIERTOS_PARA_GANAR} respuestas correctas.
 *
 * Usa exclusivamente la librería Bitmap provista por la cátedra.
 * La sincronización con el hilo de juego se hace mediante Object.wait/notifyAll.
 */
public class vistaBusqueda {

    // ── Configuración ─────────────────────────────────────────────────────────
    private static final int ACIERTOS_PARA_GANAR = 3;

    // ── Dimensiones del canvas ────────────────────────────────────────────────
    private static final int W = 700;
    private static final int H = 500;

    // ── Paleta ────────────────────────────────────────────────────────────────
    private static final Color BG        = new Color(0x0D0A1A);
    private static final Color GOLD      = new Color(0xC9A84C);
    private static final Color GOLD_DIM  = new Color(0x7A6028);
    private static final Color TEAL      = new Color(0x3ECFCF);
    private static final Color TEXT      = new Color(0xEDE8D5);
    private static final Color TEXT_DIM  = new Color(0x7A7565);
    private static final Color GREEN     = new Color(0x4CAF50);
    private static final Color RED       = new Color(0xE05050);
    private static final Color PANEL_BG  = new Color(0x1A1528);
    private static final Color PANEL_BOR = new Color(0x3A3050);

    // ── Fuentes ───────────────────────────────────────────────────────────────
    private static final Font F_TITLE = new Font("Serif",      Font.BOLD,  22);
    private static final Font F_LABEL = new Font("Monospaced", Font.BOLD,  14);
    private static final Font F_BODY  = new Font("Monospaced", Font.PLAIN, 13);
    private static final Font F_SMALL = new Font("Monospaced", Font.PLAIN, 11);
    private static final Font F_BIG   = new Font("Serif",      Font.BOLD,  32);

    // ── Estado del juego ──────────────────────────────────────────────────────
    private int  aciertos    = 0;
    private int  rondas      = 0;
    private long tiempoLista = 0;   // guardados para mostrar en el feedback
    private long tiempoArbol = 0;

    // ── Comunicación con el hilo de PartidaBusqueda ───────────────────────────
    /** Valores posibles: "LISTA", "ARBOL" o "SALIR" */
    private volatile String respuestaJugador = null;
    private final Object lock = new Object();

    // ── Bitmap ────────────────────────────────────────────────────────────────
    private final Bitmap canvas;

    // ─────────────────────────────────────────────────────────────────────────
    // Constructor
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * post: abre la ventana BMP con pantalla de inicio
     */
    public vistaBusqueda() {
        this.canvas = new Bitmap(W, H);
        dibujarPantallaInicio();

        List<BitmapViewerConMenu.MenuAction> acciones = new ArrayList<>();
        acciones.add(new BitmapViewerConMenu.MenuAction(
                "LISTA fue mas rapida",  () -> notificarRespuesta("LISTA")));
        acciones.add(new BitmapViewerConMenu.MenuAction(
                "ARBOL fue mas rapido",  () -> notificarRespuesta("ARBOL")));
        acciones.add(new BitmapViewerConMenu.MenuAction(
                "Salir",                 () -> notificarRespuesta("SALIR")));

        BitmapViewerConMenu.showBitmapsWithMenu(acciones, canvas);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // API publica — consumida por PartidaBusqueda
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Muestra los tiempos de la ronda actual y bloquea hasta que el jugador elige.
     *
     * pre:  palabra != null, tiempos en nanosegundos >= 0
     * post: devuelve "LISTA", "ARBOL" o "SALIR"
     */
    public String mostrarRondaYEsperarRespuesta(String palabra,
                                                long tiempoLista,
                                                long tiempoArbol) {
        rondas++;
        this.tiempoLista = tiempoLista;
        this.tiempoArbol = tiempoArbol;
        dibujarRonda(palabra);

        synchronized (lock) {
            respuestaJugador = null;
            while (respuestaJugador == null) {
                try { lock.wait(); } catch (InterruptedException ignored) {}
            }
            return respuestaJugador;
        }
    }

    /**
     * Muestra si el jugador acertó o no, y cuál era la respuesta correcta.
     * Espera 1.8 s para que el jugador lo lea antes de pasar a la siguiente ronda.
     *
     * pre:  estructuraGanadora es "LISTA" o "ARBOL"
     * post: actualiza aciertos si acerto == true
     */
    public void mostrarFeedback(boolean acerto, String estructuraGanadora) {
        if (acerto) {
            aciertos++;
        }
        dibujarFeedback(acerto, estructuraGanadora);
        try { Thread.sleep(1800); } catch (InterruptedException ignored) {}
    }

    /**
     * post: pinta la pantalla de victoria final
     */
    public void mostrarVictoria() {
        dibujarVictoria();
    }

    /**
     * post: devuelve true si el jugador acumuló los aciertos necesarios para ganar
     */
    public boolean estaGanada() {
        return aciertos >= ACIERTOS_PARA_GANAR;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Dibujo — pantallas
    // ─────────────────────────────────────────────────────────────────────────

    /** Pantalla de bienvenida inicial */
    private void dibujarPantallaInicio() {
        limpiar();
        dibujarMarco();

        canvas.drawText("EL GRIMORIO DE PALABRAS",
                centrarX("EL GRIMORIO DE PALABRAS", F_TITLE), 80,
                F_TITLE, GOLD, PANEL_BG);

        canvas.drawText("Ciudad 5  -  Lista vs Arbol Binario",
                centrarX("Ciudad 5  -  Lista vs Arbol Binario", F_BODY), 115,
                F_BODY, TEXT_DIM, PANEL_BG);

        canvas.drawLine(100, 135, W - 100, 135, GOLD_DIM);

        canvas.drawText("Se te mostrara una palabra y los tiempos de busqueda",
                centrarX("Se te mostrara una palabra y los tiempos de busqueda", F_BODY), 200,
                F_BODY, TEXT, PANEL_BG);

        canvas.drawText("en Lista y en Arbol. Adivina cual fue mas rapido!",
                centrarX("en Lista y en Arbol. Adivina cual fue mas rapido!", F_BODY), 225,
                F_BODY, TEXT, PANEL_BG);

        canvas.drawText("Necesitas " + ACIERTOS_PARA_GANAR + " aciertos para ganar.",
                centrarX("Necesitas " + ACIERTOS_PARA_GANAR + " aciertos para ganar.", F_LABEL), 270,
                F_LABEL, TEAL, PANEL_BG);

        canvas.drawText("Usa los botones de abajo para responder.",
                centrarX("Usa los botones de abajo para responder.", F_SMALL), 315,
                F_SMALL, TEXT_DIM, PANEL_BG);
    }

    /**
     * Pantalla de ronda: muestra la palabra pero NO los tiempos.
     *
     * pre:  palabra != null
     */
    private void dibujarRonda(String palabra) {
        limpiar();
        dibujarMarco();
        dibujarProgreso();

        canvas.drawText("Cual estructura fue mas rapida?",
                centrarX("Cual estructura fue mas rapida?", F_LABEL), 65,
                F_LABEL, GOLD, PANEL_BG);

        canvas.drawLine(80, 80, W - 80, 80, GOLD_DIM);

        // Palabra consultada
        canvas.drawText("Palabra buscada:", 80, 125, F_SMALL, TEXT_DIM, PANEL_BG);

        String palabraDisplay = "\"" + palabra + "\"";
        canvas.drawText(palabraDisplay,
                centrarX(palabraDisplay, F_BIG), 185,
                F_BIG, TEAL, PANEL_BG);

        canvas.drawLine(80, 205, W - 80, 205, PANEL_BOR);

        // Paneles ciegos: el jugador no ve los tiempos todavía
        dibujarPanelCiego(80,  225, 240, 120, "LISTA");
        dibujarPanelCiego(380, 225, 240, 120, "ARBOL");

        canvas.drawText("-> Elegi con los botones de abajo",
                centrarX("-> Elegi con los botones de abajo", F_SMALL), 400,
                F_SMALL, TEXT_DIM, PANEL_BG);
    }

    /**
     * Panel sin tiempo visible — para la pantalla de pregunta.
     *
     * pre:  nombre != null, coordenadas dentro del canvas
     */
    private void dibujarPanelCiego(int x, int y, int w, int h, String nombre) {
        canvas.drawRectangle(x, y, w, h, PANEL_BOR);
        int centroX = x + w / 2;
        canvas.drawText(nombre,
                centroX - nombre.length() * 4,
                y + 30, F_LABEL, GOLD, PANEL_BG);
        canvas.drawText("???",
                centroX - 12,
                y + 65, F_LABEL, TEXT_DIM, PANEL_BG);
    }

    /**
     * Dibuja un panel con el nombre de la estructura y su tiempo medido.
     *
     * pre:  nombre != null, tiempo >= 0, coordenadas dentro del canvas
     */
    private void dibujarPanelTiempo(int x, int y, int w, int h,
                                     String nombre, long tiempo) {
        canvas.drawRectangle(x, y, w, h, PANEL_BOR);

        int centroX = x + w / 2;

        canvas.drawText(nombre,
                centroX - nombre.length() * 4,
                y + 30, F_LABEL, GOLD, PANEL_BG);

        String ns = tiempo + " ns";
        canvas.drawText(ns,
                centroX - ns.length() * 4,
                y + 62, F_BODY, TEXT, PANEL_BG);

        canvas.drawLine(x + 10, y + 80, x + w - 10, y + 80, GOLD_DIM);

        String ms = String.format("(%.4f ms)", tiempo / 1_000_000.0);
        canvas.drawText(ms,
                centroX - ms.length() * 3,
                y + 105, F_SMALL, TEXT_DIM, PANEL_BG);
    }

    /**
     * Feedback tras la respuesta del jugador.
     *
     * pre:  estructuraGanadora es "LISTA" o "ARBOL"
     */
    private void dibujarFeedback(boolean acerto, String estructuraGanadora) {
        limpiar();
        dibujarMarco();
        dibujarProgreso();

        if (acerto) {
            canvas.drawText("CORRECTO!",
                    centrarX("CORRECTO!", F_BIG), 170,
                    F_BIG, GREEN, PANEL_BG);
            canvas.drawText("El " + estructuraGanadora + " fue mas rapido.",
                    centrarX("El " + estructuraGanadora + " fue mas rapido.", F_LABEL), 220,
                    F_LABEL, TEXT, PANEL_BG);
        } else {
            canvas.drawText("INCORRECTO",
                    centrarX("INCORRECTO", F_BIG), 170,
                    F_BIG, RED, PANEL_BG);
            canvas.drawText("Era el " + estructuraGanadora + ".",
                    centrarX("Era el " + estructuraGanadora + ".", F_LABEL), 220,
                    F_LABEL, TEXT, PANEL_BG);
        }

        String progreso = "Aciertos: " + aciertos + " / " + ACIERTOS_PARA_GANAR;
        canvas.drawText(progreso,
                centrarX(progreso, F_BODY), 280,
                F_BODY, TEAL, PANEL_BG);

        // Ahora sí mostramos los tiempos reales
        canvas.drawLine(80, 300, W - 80, 300, PANEL_BOR);
        dibujarPanelTiempo(80,  315, 240, 120, "LISTA", tiempoLista);
        dibujarPanelTiempo(380, 315, 240, 120, "ARBOL", tiempoArbol);
    }

    /** Pantalla de victoria final */
    private void dibujarVictoria() {
        limpiar();
        dibujarMarco();

        canvas.drawText("CIUDAD CONQUISTADA!",
                centrarX("CIUDAD CONQUISTADA!", F_BIG), 160,
                F_BIG, GOLD, PANEL_BG);

        canvas.drawLine(80, 180, W - 80, 180, GOLD);

        String stats = "Aciertos: " + aciertos + " de " + rondas + " rondas";
        canvas.drawText(stats,
                centrarX(stats, F_LABEL), 235,
                F_LABEL, TEAL, PANEL_BG);

        canvas.drawText("Ya sabes cuando el Arbol supera a la Lista!",
                centrarX("Ya sabes cuando el Arbol supera a la Lista!", F_BODY), 285,
                F_BODY, TEXT, PANEL_BG);

        canvas.drawText("Presiona Salir para volver al mapa.",
                centrarX("Presiona Salir para volver al mapa.", F_SMALL), 335,
                F_SMALL, TEXT_DIM, PANEL_BG);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers de dibujo
    // ─────────────────────────────────────────────────────────────────────────

    /** Rellena el fondo con el color base */
    private void limpiar() {
        canvas.rellenar(BG);
    }

    /** Marco decorativo doble */
    private void dibujarMarco() {
        canvas.drawRectangle(4,  4,  W - 10, H - 10, GOLD_DIM);
        canvas.drawRectangle(10, 10, W - 22, H - 22, PANEL_BOR);
    }

    /** Barra de progreso en esquina superior derecha */
    private void dibujarProgreso() {
        int llenos  = Math.min(aciertos, ACIERTOS_PARA_GANAR);
        int vacios  = Math.max(0, ACIERTOS_PARA_GANAR - llenos);
        String barra = "[" + repetir("#", llenos) + repetir(".", vacios) + "]";
        String txt   = "Aciertos " + aciertos + "/" + ACIERTOS_PARA_GANAR + " " + barra;
        canvas.drawText(txt, W - 260, 28, F_SMALL, TEAL, BG);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Comunicación entre hilos
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Notifica al hilo de juego que el jugador eligió una respuesta.
     * pre:  respuesta es "LISTA", "ARBOL" o "SALIR"
     */
    private void notificarRespuesta(String respuesta) {
        synchronized (lock) {
            respuestaJugador = respuesta;
            lock.notifyAll();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Utilidades
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Aproxima el x para centrar texto horizontalmente.
     * pre:  texto != null, fuente != null
     */
    private int centrarX(String texto, Font fuente) {
        int charWidth = fuente.getFamily().contains("Monospaced") ? 8 : 11;
        int estimado  = texto.length() * charWidth;
        return Math.max(20, (W - estimado) / 2);
    }

    /**
     * Repite un caracter n veces (reemplazo de String.repeat para compatibilidad).
     * pre:  n >= 0
     */
    private String repetir(String c, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) { sb.append(c); }
        return sb.toString();
    }
}