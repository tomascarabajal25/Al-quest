package juego.ciudades.ciudad5.UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;

import juego.ciudades.ciudad5.ciudadBusqueda;
import juego.ciudades.ciudad5.UI.PuertaDesafio.EstadoPuerta;
import juego.ciudades.ciudad5.UI.PuertaDesafio.TipoPuerta;
import juego.configuracion.ConfiguracionBusqueda;
import modelos.Minijuego;
import modelos.Mapa;
import modelosVista.JugadorVista;

/**
 * Controlador del minijuego de búsqueda integrado en la Vista de Ciudad 5.
 *
 * Responsabilidades:
 *  - Mantener el estado del minijuego (EstadoMinijuego)
 *  - Detectar colisión con ZonaDesafio para activarse
 *  - Mostrar overlay con la palabra actual y los tiempos
 *  - Detectar colisión con PuertaDesafio para evaluar la respuesta
 *  - Dar feedback visual y contar puntaje
 *
 * Cómo usarlo desde Vista:
 *   1. Crear:  minijuego = new MinijuegoDesafio(mapa, tamaño);
 *   2. Update: minijuego.actualizar(jugadorVista);
 *   3. Draw:   minijuego.draw(g2, jugadorVista);
 */
public class MinijuegoDesafio implements Minijuego {

    // ── Dependencias ─────────────────────────────────────────────────────────
    private final ciudadBusqueda buscador;
    private final List<String>   palabras;
    private final Random         random = new Random();

    private final ZonaDesafio   zona;
    private final PuertaDesafio puertaLista;
    private final PuertaDesafio puertaArbol;
    private Runnable            onFinalizadoCallback;

    // ── Estado ────────────────────────────────────────────────────────────────
    private EstadoMinijuego estado = EstadoMinijuego.INACTIVO;

    // Ronda actual
    private String palabraActual;
    private long   tiempoLista;
    private long   tiempoArbol;
    private String respuestaCorrecta; // "LISTA" o "ARBOL"

    // Feedback
    private boolean ultimaRespuestaCorrecta;
    private long    tiempoInicioFeedback;

    // Victoria
    /** -1 indica que todavía no se registró el inicio de la pantalla de victoria. */
    private long tiempoInicioVictoria = -1;

    // Puntaje
    private int aciertos    = 0;
    private int rondaActual = 0;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * @param mapa   mapa de palabras ya indexado
     * @param tamaño tamaño de tile en px (Vista.tamaño)
     */
    public MinijuegoDesafio(Mapa mapa, int tamaño) {
        this.buscador = new ciudadBusqueda(mapa);
        this.palabras = buscador.getPalabras();

        this.zona = new ZonaDesafio(
            ConfiguracionBusqueda.ZONA_COL,
            ConfiguracionBusqueda.ZONA_FILA,
            ConfiguracionBusqueda.ZONA_ANCHO,
            ConfiguracionBusqueda.ZONA_ALTO,
            tamaño
        );

        this.puertaLista = new PuertaDesafio(
            ConfiguracionBusqueda.PUERTA_LISTA_COL,
            ConfiguracionBusqueda.PUERTA_LISTA_FILA,
            TipoPuerta.LISTA,
            tamaño
        );

        this.puertaArbol = new PuertaDesafio(
            ConfiguracionBusqueda.PUERTA_ARBOL_COL,
            ConfiguracionBusqueda.PUERTA_ARBOL_FILA,
            TipoPuerta.ARBOL,
            tamaño
        );
    }

    // ── Loop principal ────────────────────────────────────────────────────────

    /**
     * Llamar desde Vista.actualizar() en cada frame.
     */
    @Override
    public void actualizar(JugadorVista jugador) {
        switch (estado) {

            case INACTIVO -> {
                if (!palabras.isEmpty() && zona.colisionaConJugador(jugador)) {
                    iniciarRonda();
                }
            }

            case MOSTRANDO -> {
                // Transición directa a ESPERANDO; se puede agregar un timer acá si se quiere
                estado = EstadoMinijuego.ESPERANDO;
            }

            case ESPERANDO -> {
                if (puertaLista.colisionaConJugador(jugador)) {
                    evaluarRespuesta("LISTA", jugador);
                } else if (puertaArbol.colisionaConJugador(jugador)) {
                    evaluarRespuesta("ARBOL", jugador);
                }
            }

            case FEEDBACK -> {
                long ahora = System.currentTimeMillis();
                if (ahora - tiempoInicioFeedback >= ConfiguracionBusqueda.DURACION_FEEDBACK_MS) {
                    puertaLista.setEstado(EstadoPuerta.NORMAL);
                    puertaArbol.setEstado(EstadoPuerta.NORMAL);

                    if (rondaActual >= ConfiguracionBusqueda.RONDAS_PARA_GANAR) {
                        estado = EstadoMinijuego.GANADO;
                    } else {
                        teleportarJugadorAEntrada(jugador);
                        iniciarRonda();
                    }
                }
            }

            case GANADO -> {
                // Registrar momento de entrada al estado GANADO (solo una vez)
                if (tiempoInicioVictoria == -1) {
                    tiempoInicioVictoria = System.currentTimeMillis();
                }

                long transcurrido = System.currentTimeMillis() - tiempoInicioVictoria;
                if (transcurrido >= ConfiguracionBusqueda.DURACION_VICTORIA_MS
                        && onFinalizadoCallback != null) {
                    onFinalizadoCallback.run();
                    onFinalizadoCallback = null; // evita dispararlo más de una vez
                }
            }
        }
    }

    // ── Dibujo ────────────────────────────────────────────────────────────────

    /**
     * Llamar desde Vista.paintComponent() después de dibujar construcciones y jugador.
     */
    @Override
    public void draw(Graphics2D g2, JugadorVista jugador) {
        if (estado == EstadoMinijuego.INACTIVO) {
            zona.draw(g2, jugador);
        } else {
            dibujarOverlay(g2);
        }
    }

    private void dibujarOverlay(Graphics2D g2) {
        final int panelX = ConfiguracionBusqueda.OVERLAY_X;
        final int panelY = ConfiguracionBusqueda.OVERLAY_Y;
        final int panelW = ConfiguracionBusqueda.OVERLAY_ANCHO;
        final int panelH = ConfiguracionBusqueda.OVERLAY_ALTO;

        // Fondo semitransparente
        g2.setColor(new Color(0, 0, 0, 190));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        // Encabezado de ronda
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString(
            "Ronda " + rondaActual + "/" + ConfiguracionBusqueda.RONDAS_PARA_GANAR
            + "   Aciertos: " + aciertos,
            panelX + 16, panelY + 22
        );

        // Palabra actual
        g2.setFont(new Font("Arial", Font.BOLD, 28));
        g2.setColor(Color.YELLOW);
        g2.drawString("\"" + palabraActual + "\"", panelX + 16, panelY + 62);

        

        // Instrucción
        g2.setFont(new Font("Arial", Font.ITALIC, 13));
        g2.setColor(Color.WHITE);
        g2.drawString("← Lista  |  elegí la más rápida  |  Árbol →", panelX + 16, panelY + 142);

        // Feedback (solo en estado FEEDBACK)
        if (estado == EstadoMinijuego.FEEDBACK) {
            g2.setFont(new Font("Arial", Font.BOLD, 22));
            if (ultimaRespuestaCorrecta) {
                g2.setColor(new Color(0, 230, 0));
                g2.drawString("¡Correcto!", panelX + 330, panelY + 62);
                g2.setFont(new Font("Arial", Font.PLAIN, 14));
                g2.setColor(new Color(100, 180, 255));
                g2.drawString("Lista:  " + tiempoLista + " ns", panelX + 16, panelY + 92);
                g2.setColor(new Color(180, 130, 255));
                g2.drawString("Árbol:  " + tiempoArbol + " ns", panelX + 16, panelY + 112);
            } else {
                g2.setColor(new Color(230, 50, 50));
                g2.drawString("Incorrecto. Era: " + respuestaCorrecta, panelX + 270, panelY + 62);
                g2.setFont(new Font("Arial", Font.PLAIN, 14));
                g2.setColor(new Color(100, 180, 255));
                g2.drawString("Lista:  " + tiempoLista + " ns", panelX + 16, panelY + 92);
                g2.setColor(new Color(180, 130, 255));
                g2.drawString("Árbol:  " + tiempoArbol + " ns", panelX + 16, panelY + 112);
            }
        }

        // Pantalla de victoria (solo en estado GANADO)
        if (estado == EstadoMinijuego.GANADO) {
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);

            g2.setFont(new Font("Arial", Font.BOLD, 32));
            g2.setColor(Color.YELLOW);
            g2.drawString(
                "¡GANASTE! " + aciertos + "/" + ConfiguracionBusqueda.RONDAS_PARA_GANAR,
                panelX + 60, panelY + 60
            );

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.WHITE);
            g2.drawString(
                "Puntos obtenidos: +" + ConfiguracionBusqueda.PUNTOS_VICTORIA,
                panelX + 60, panelY + 100
            );
        }
    }

    // ── Lógica interna ────────────────────────────────────────────────────────

    private void iniciarRonda() {
        rondaActual++;
        palabraActual     = palabras.get(random.nextInt(palabras.size()));
        tiempoLista       = buscador.medirTiempoPorLista(palabraActual);
        tiempoArbol       = buscador.medirTiempoPorArbol(palabraActual);
        respuestaCorrecta = tiempoArbol < tiempoLista ? "ARBOL" : "LISTA";
        estado            = EstadoMinijuego.MOSTRANDO;
    }

    private void evaluarRespuesta(String elegida, JugadorVista jugador) {
        ultimaRespuestaCorrecta = elegida.equals(respuestaCorrecta);

        if (ultimaRespuestaCorrecta) {
            aciertos++;
            (elegida.equals("LISTA") ? puertaLista : puertaArbol)
                .setEstado(EstadoPuerta.CORRECTA);
        } else {
            (elegida.equals("LISTA") ? puertaLista : puertaArbol)
                .setEstado(EstadoPuerta.INCORRECTA);
            // Resaltar la puerta correcta
            (elegida.equals("LISTA") ? puertaArbol : puertaLista)
                .setEstado(EstadoPuerta.RESALTADA);
        }

        tiempoInicioFeedback = System.currentTimeMillis();
        estado = EstadoMinijuego.FEEDBACK;
    }

    /**
     * Mueve al jugador de vuelta a la entrada de la sala para la siguiente ronda.
     */
    private void teleportarJugadorAEntrada(JugadorVista jugador) {
        int tileW = jugador.getAreaSolida().width  + jugador.getAreaSolida().x;
        int tileH = jugador.getAreaSolida().height + jugador.getAreaSolida().y;
        jugador.setWorldX(ConfiguracionBusqueda.TELEPORT_COL  * tileW);
        jugador.setWorldY(ConfiguracionBusqueda.TELEPORT_FILA * tileH);
    }

    // ── Getters / setters ─────────────────────────────────────────────────────

    public EstadoMinijuego getEstado()   { return estado; }
    public int             getAciertos() { return aciertos; }

    /** post: devuelve true si el jugador completó todas las rondas. */
    public boolean isGanado() { return estado == EstadoMinijuego.GANADO; }

    public void setOnFinalizadoCallback(Runnable callback) {
        this.onFinalizadoCallback = callback;
    }

    public PuertaDesafio getPuertaLista() { return puertaLista; }
    public PuertaDesafio getPuertaArbol() { return puertaArbol; }
}