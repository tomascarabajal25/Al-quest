package juego.ciudades.ordenamientos.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import juego.ciudades.ordenamientos.AdministradorDePasos;
import juego.ciudades.ordenamientos.Caja;
import juego.ciudades.ordenamientos.Ordenador;
import juego.ciudades.ordenamientos.PasoOrdenamiento;
import modelos.Minijuego;
import modelosVista.JugadorVista;
import modelosVista.Vista;
import utils.ValidacionesUtiles;

/**
 * Minijuego de ordenamiento integrado al mundo del juego.
 *
 * El jugador camina entre las CajaVista y puede:
 *   1. Ordenarlas MANUALMENTE presionando ESPACIO sobre dos cajas para intercambiarlas.
 *   2. Activar el RESOLVER AUTOMÁTICO (tecla R) que anima los pasos del algoritmo
 *      configurado (Bubble o Selection), actualizando el mundo en cada swap.
 *
 * Flujo manual:
 *   · Jugador se acerca a una caja → se resalta la más cercana
 *   · ESPACIO sobre caja libre     → queda "seleccionada"
 *   · ESPACIO sobre otra caja      → intercambio inmediato
 *   · ESCAPE                       → deselecciona
 *
 * Flujo resolver:
 *   · R                            → arranca/pausa la animación
 *   · Cada TICK_POR_PASO frames    → aplica el siguiente swap del historial
 *   · Al finalizar, marca cajas correctas y muestra victoria
 *
 * Pre-condición de creación:
 *   · Lista de CajaVista con posiciones ya asignadas en el mundo
 *   · Ordenador<Caja> configurado
 *   · Vista activa con jugadorVista inicializado
 */
public class MinijuegoOrdenamiento implements Minijuego {
	
	private Runnable onVictoria;

    // ── Configuración ──────────────────────────────────────────────────────
    /** Frames entre cada paso del resolver automático */
    private static final int TICK_POR_PASO = 45;
    /** Radio en píxeles para detectar "cercanía" del jugador a una caja */
    private static final int RADIO_INTERACCION = 60;

    // ── Estado del minijuego ───────────────────────────────────────────────
    private enum Estado { ESPERANDO, MANUAL, RESOLVIENDO, FINALIZADO }

    private Estado estado = Estado.ESPERANDO;

    // ── Datos ──────────────────────────────────────────────────────────────
    private final List<CajaVista>           cajasVista;
    private final Ordenador<Caja>           ordenador;
    private final KeyHandlerOrdenamiento    keyOrd;

    /** Índice lógico de la caja seleccionada por el jugador (-1 = ninguna) */
    private int indiceCajaSeleccionada = -1;

    // ── Resolver automático ────────────────────────────────────────────────
    private List<PasoOrdenamiento<Caja>> pasos;
    private int indicePasoActual   = 0;
    private int tickDesdeUltimoPaso = 0;
    private boolean resolverPausado = false;

    // ── HUD ───────────────────────────────────────────────────────────────
    private static final Font FONT_HUD      = new Font("Monospaced", Font.BOLD,  11);
    private static final Font FONT_TITULO   = new Font("Monospaced", Font.BOLD,  16);
    private static final Font FONT_MENSAJE  = new Font("Monospaced", Font.PLAIN, 10);
    private static final Color COLOR_HUD_BG = new Color(0, 0, 0, 160);
    private static final Color COLOR_HUD_FG = new Color(200, 230, 255);
    private static final Color COLOR_WARN   = new Color(255, 200,  50);
    private static final Color COLOR_OK     = new Color( 80, 220, 100);

    private String mensajeEstado = "Acércate a una caja y presiona ESPACIO";
    private int    intercambiosManuales = 0;

    // ── Constructor ────────────────────────────────────────────────────────

    /**
     * Pre:
     * @param cajasVista  lista no nula, al menos 2 cajas, ya posicionadas en el mundo
     * @param ordenador   no nulo
     * @param vista       no nula (para registrar el KeyListener)
     */
    public MinijuegoOrdenamiento(List<CajaVista> cajasVista,
                                  Ordenador<Caja> ordenador,
                                  Vista vista) {
        if (cajasVista == null || cajasVista.size() < 2)
            throw new IllegalArgumentException("Se necesitan al menos 2 cajas");
        if (ordenador == null) throw new IllegalArgumentException("Ordenador no puede ser nulo");

        this.cajasVista = new ArrayList<>(cajasVista);
        this.ordenador  = ordenador;

        this.keyOrd = new KeyHandlerOrdenamiento();
        vista.addKeyListener(keyOrd);

        prepararPasosResolver();
        agregarCajasAlMundo(vista);
    }

    // ── MinijuegoDesafio ───────────────────────────────────────────────────

    /**
     * Post: actualiza lógica del minijuego según el estado actual
     */
    public void actualizar(JugadorVista jugador) {
        if (estado == Estado.FINALIZADO) return;

        // Animaciones de cada caja
        for (CajaVista cv : cajasVista) cv.actualizar();

        procesarEscape();

        switch (estado) {
            case ESPERANDO:
            case MANUAL:
                procesarInteraccionManual(jugador);
                if (keyOrd.resolverPresionado) {
                    keyOrd.resolverPresionado = false;
                    activarResolver();
                }
                break;
            case RESOLVIENDO:
                if (keyOrd.resolverPresionado) {
                    keyOrd.resolverPresionado = false;
                    togglePausaResolver();
                }
                if (keyOrd.espacioPresionado) {
                    keyOrd.espacioPresionado = false;
                    togglePausaResolver();
                }
                if (!resolverPausado) avanzarPasoResolver();
                break;
            default:
                break;
        }
    }

    /**
     * Post: dibuja el HUD del minijuego (controles, estado, progreso)
     */
    public void draw(Graphics2D g2, JugadorVista jugador) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        dibujarHUD(g2, jugador);
        dibujarIndicadorCercana(g2, jugador);
    }

    // ── Lógica manual ─────────────────────────────────────────────────────

    private void procesarInteraccionManual(JugadorVista jugador) {
        int cercanaIdx = encontrarCajaCercana(jugador);

        // Resaltar la caja más cercana (solo si no hay seleccionada)
        for (int i = 0; i < cajasVista.size(); i++) {
            CajaVista cv = cajasVista.get(i);
            if (i == indiceCajaSeleccionada) continue; // no tocar la seleccionada
            cv.setDestacada(i == cercanaIdx && indiceCajaSeleccionada == -1);
        }

        if (!keyOrd.espacioPresionado) return;
        keyOrd.espacioPresionado = false;

        if (cercanaIdx == -1) {
            desseleccionar();
            mensajeEstado = "No hay caja cercana";
            return;
        }

        if (indiceCajaSeleccionada == -1) {
            // Primera selección
            indiceCajaSeleccionada = cercanaIdx;
            cajasVista.get(cercanaIdx).setSeleccionada(true);
            cajasVista.get(cercanaIdx).setDestacada(false);
            estado = Estado.MANUAL;
            mensajeEstado = "Caja \"" + cajasVista.get(cercanaIdx).getCaja().getNombre()
                          + "\" seleccionada → ve a otra y presiona ESPACIO";
        } else if (cercanaIdx == indiceCajaSeleccionada) {
            // Segunda pulsación en la misma → deseleccionar
            desseleccionar();
            mensajeEstado = "Selección cancelada";
        } else {
            // Intercambio
            intercambiarCajas(indiceCajaSeleccionada, cercanaIdx);
            intercambiosManuales++;
            mensajeEstado = "¡Intercambio! (" + intercambiosManuales + " movimientos)";
            desseleccionar();
            estado = Estado.ESPERANDO;
            verificarVictoriaManual();
        }
    }

    private void desseleccionar() {
        if (indiceCajaSeleccionada != -1) {
            cajasVista.get(indiceCajaSeleccionada).setSeleccionada(false);
            indiceCajaSeleccionada = -1;
        }
        if (estado == Estado.MANUAL) estado = Estado.ESPERANDO;
    }

    private void procesarEscape() {
        if (!keyOrd.escapePresionado) return;
        keyOrd.escapePresionado = false;
        if (estado == Estado.MANUAL) {
            desseleccionar();
            mensajeEstado = "Selección cancelada";
        } else if (estado == Estado.RESOLVIENDO) {
            detenerResolver();
            mensajeEstado = "Resolver detenido";
        }
    }

    // ── Resolver automático ────────────────────────────────────────────────

    /**
     * Post: calcula todos los pasos del algoritmo sobre una copia del estado actual
     *       y arranca la animación desde el inicio
     */
    private void activarResolver() {
        prepararPasosResolver();
        indicePasoActual    = 0;
        tickDesdeUltimoPaso = 0;
        resolverPausado     = false;
        estado              = Estado.RESOLVIENDO;
        mensajeEstado       = "Resolver activo — " + ordenador.getNombre()
                            + "  [R o ESPACIO = pausar]";
        limpiarEstadosVisuales();
    }

    private void togglePausaResolver() {
        resolverPausado = !resolverPausado;
        mensajeEstado   = resolverPausado
            ? "Pausado — R para continuar"
            : "Resolviendo — " + ordenador.getNombre();
    }

    private void detenerResolver() {
        estado          = Estado.ESPERANDO;
        resolverPausado = false;
        limpiarEstadosVisuales();
    }

    private void avanzarPasoResolver() {
        tickDesdeUltimoPaso++;
        if (tickDesdeUltimoPaso < TICK_POR_PASO) return;
        tickDesdeUltimoPaso = 0;

        if (indicePasoActual >= pasos.size()) {
            finalizarResolver();
            return;
        }

        PasoOrdenamiento<Caja> paso = pasos.get(indicePasoActual);
        indicePasoActual++;

        int i1 = paso.getIndice1();
        int i2 = paso.getIndice2();

        limpiarEstadosVisuales();

        if (i1 >= 0 && i2 >= 0 && i1 != i2) {
            // Aplicar el swap en las cajas vista
            intercambiarCajas(i1, i2);
            cajasVista.get(i1).setDestacada(true);
            cajasVista.get(i2).setDestacada(true);
            mensajeEstado = "Paso " + indicePasoActual + "/" + pasos.size()
                          + " — " + paso.getAccion()
                          + " [" + cajasVista.get(i1).getCaja().getNombre()
                          + " ↔ " + cajasVista.get(i2).getCaja().getNombre() + "]";
        } else {
            mensajeEstado = "Paso " + indicePasoActual + "/" + pasos.size()
                          + " — " + paso.getAccion();
        }
    }

    private void finalizarResolver() {
    	if (onVictoria != null) {
    		onVictoria.run();
    	}
        estado = Estado.FINALIZADO;
        limpiarEstadosVisuales();
        for (CajaVista cv : cajasVista) cv.setCorrecta(true);
        mensajeEstado = "Ordenamiento completado con " + ordenador.getNombre() + "!";
    }

    private void verificarVictoriaManual() {
        // Verifica si el orden actual coincide con el orden correcto (creciente por tamaño)
        for (int i = 0; i < cajasVista.size() - 1; i++) {
            if (cajasVista.get(i).getCaja().getTamaño()
                    > cajasVista.get(i + 1).getCaja().getTamaño()) return;
        }
        if (onVictoria != null) {
        	onVictoria.run();
        }
        estado = Estado.FINALIZADO;
        for (CajaVista cv : cajasVista) cv.setCorrecta(true);
        mensajeEstado = "¡Ordenado manualmente en " + intercambiosManuales + " movimientos!";
    }

    // ── Helpers ────────────────────────────────────────────────────────────

    /**
     * Post: calcula los pasos del algoritmo sobre el estado ACTUAL de las cajas
     */
    private void prepararPasosResolver() {
        List<Caja> copiaActual = new ArrayList<>();
        for (CajaVista cv : cajasVista) copiaActual.add(cv.getCaja());

        AdministradorDePasos<Caja> admin = new AdministradorDePasos<>();
        ordenador.ordenar(copiaActual, admin);
        pasos = admin.getPasos();
    }

    /**
     * Post: intercambia las posiciones worldX/worldY y los índices lógicos
     *       de las dos cajasVista indicadas
     */
    private void intercambiarCajas(int i, int j) {
        CajaVista a = cajasVista.get(i);
        CajaVista b = cajasVista.get(j);

        int tmpX = a.getWorldX();
        int tmpY = a.getWorldY();
        a.setWorldX(b.getWorldX());
        a.setWorldY(b.getWorldY());
        b.setWorldX(tmpX);
        b.setWorldY(tmpY);

        int tmpIdx = a.getIndiceLogico();
        a.setIndiceLogico(b.getIndiceLogico());
        b.setIndiceLogico(tmpIdx);

        cajasVista.set(i, b);
        cajasVista.set(j, a);
    }

    private int encontrarCajaCercana(JugadorVista jugador) {
        int jCx = jugador.getWorldX() + jugador.getAreaSolida().x + jugador.getAreaSolida().width  / 2;
        int jCy = jugador.getWorldY() + jugador.getAreaSolida().y + jugador.getAreaSolida().height / 2;

        int    mejorIdx  = -1;
        double mejorDist = RADIO_INTERACCION;

        for (int i = 0; i < cajasVista.size(); i++) {
            CajaVista cv = cajasVista.get(i);
            double dist = Math.hypot(cv.getWorldX() - jCx, cv.getWorldY() - jCy);
            if (dist < mejorDist) { mejorDist = dist; mejorIdx = i; }
        }
        return mejorIdx;
    }

    private void limpiarEstadosVisuales() {
        for (CajaVista cv : cajasVista) {
            cv.setDestacada(false);
            cv.setSeleccionada(false);
            cv.setCorrecta(false);
        }
    }

    private void agregarCajasAlMundo(Vista vista) {
        for (CajaVista cv : cajasVista) {
            vista.agregarObjeto(cv);
        }
    }

    // ── HUD ───────────────────────────────────────────────────────────────

    private void dibujarHUD(Graphics2D g2, JugadorVista jugador) {
        // Panel semitransparente en la esquina superior izquierda de la pantalla
        int panelX = 10, panelY = 10, panelW = 500, panelH = 130;

        Composite orig = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));
        g2.setColor(COLOR_HUD_BG);
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 12, 12);
        g2.setComposite(orig);

        // Borde
        g2.setColor(new Color(100, 150, 220));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 12, 12);
        g2.setStroke(new BasicStroke(1f));

        int tx = panelX + 12;
        int ty = panelY + 20;

        // Título
        g2.setFont(FONT_TITULO);
        g2.setColor(COLOR_WARN);
        g2.drawString("⬡ Ciudad Ordenamientos — " + ordenador.getNombre(), tx, ty);
        ty += 18;

        // Estado
        g2.setFont(FONT_HUD);
        g2.setColor(estado == Estado.FINALIZADO ? COLOR_OK : COLOR_HUD_FG);
        g2.drawString(mensajeEstado, tx, ty);
        ty += 16;

        // Movimientos manuales
        g2.setFont(FONT_MENSAJE);
        g2.setColor(new Color(160, 190, 220));
        g2.drawString("Intercambios manuales: " + intercambiosManuales, tx, ty);
        ty += 14;

        // Controles
        g2.setColor(new Color(130, 160, 200));
        if (estado != Estado.RESOLVIENDO) {
            g2.drawString("[ESPACIO] Seleccionar/Intercambiar   [R] Resolver auto   [ESC] Cancelar", tx, ty);
        } else {
            g2.drawString("[R] o [ESPACIO] Pausar/Reanudar   [ESC] Detener resolver", tx, ty);
        }
        ty += 14;

        // Barra de progreso del resolver
        if (estado == Estado.RESOLVIENDO && pasos != null && !pasos.isEmpty()) {
            dibujarBarraProgreso(g2, tx, ty, panelW - 24);
        }
    }

    private void dibujarBarraProgreso(Graphics2D g2, int x, int y, int ancho) {
        float progreso = (float) indicePasoActual / pasos.size();
        int barH = 8;

        g2.setColor(new Color(40, 60, 100));
        g2.fillRoundRect(x, y, ancho, barH, 4, 4);

        g2.setColor(new Color(80, 160, 255));
        g2.fillRoundRect(x, y, (int)(ancho * progreso), barH, 4, 4);

        g2.setColor(new Color(100, 130, 180));
        g2.drawRoundRect(x, y, ancho, barH, 4, 4);
    }

    private void dibujarIndicadorCercana(Graphics2D g2, JugadorVista jugador) {
        if (estado == Estado.RESOLVIENDO || estado == Estado.FINALIZADO) return;

        int cercanaIdx = encontrarCajaCercana(jugador);
        if (cercanaIdx == -1 || cercanaIdx == indiceCajaSeleccionada) return;

        // La Vista no está disponible aquí directamente, pero CajaVista ya maneja el highlight
        // Este método es un hook para efectos adicionales si se desea
    }
    
    /**
     * Pre:  callback no nulo
     * Post: registra una acción a ejecutar cuando el jugador gane
     */
    public void setOnVictoria(Runnable callback) {
    	ValidacionesUtiles.esDistintoDeNull(callback, "Callback no puede ser nulo");
      	this.onVictoria = callback;
    }
	
}