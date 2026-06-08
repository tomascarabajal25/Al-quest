package Juego.ciudades.ciudad5.UI;

import java.awt.Color;
import modelos.Minijuego;
import modelosVista.JugadorVista;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.List;
import java.util.Random;

import Juego.ciudades.ciudad5.ciudadBusqueda;
import Juego.ciudades.ciudad5.UI.PuertaDesafio.EstadoPuerta;
import Juego.ciudades.ciudad5.UI.PuertaDesafio.TipoPuerta;
import modelos.Mapa;

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
 *   1. Crear: minijuego = new MinijuegoDesafio(mapa, tamaño);
 *   2. En actualizar(): minijuego.actualizar(jugadorCiudad5);
 *   3. En paintComponent(): minijuego.draw(g2, jugadorCiudad5);
 */
public class MinijuegoDesafio implements Minijuego {

    // ── Configuración del mapa (ajustar si cambia el diseño) ──────────────────
    // Zona de activación: entrada a la sala, cols 23-26, filas 28-30
    private static final int ZONA_COL   = 23;
    private static final int ZONA_FILA  = 28;
    private static final int ZONA_ANCHO = 4;   // tiles
    private static final int ZONA_ALTO  = 3;   // tiles

    // Puerta izquierda (LISTA): cols 18-20, fila 37
    private static final int PUERTA_LISTA_COL  = 18;
    private static final int PUERTA_LISTA_FILA = 37;

    // Puerta derecha (ÁRBOL): cols 29-31, fila 37
    private static final int PUERTA_ARBOL_COL  = 29;
    private static final int PUERTA_ARBOL_FILA = 37;

    // Rondas necesarias para ganar
    private static final int RONDAS_PARA_GANAR = 5;

    // Milisegundos que se muestra el feedback antes de la siguiente ronda
    private static final long DURACION_FEEDBACK_MS = 2000;
    // ──────────────────────────────────────────────────────────────────────────

    private final ciudadBusqueda buscador;
    private final List<String>   palabras;
    private final Random         random = new Random();

    private final ZonaDesafio  zona;
    private final PuertaDesafio puertaLista;
    private final PuertaDesafio puertaArbol;
    private Runnable onFinalizadoCallback;

    private EstadoMinijuego estado = EstadoMinijuego.INACTIVO;

    // Ronda actual
    private String palabraActual;
    private long   tiempoLista;
    private long   tiempoArbol;
    private String respuestaCorrecta; // "LISTA" o "ARBOL"

    // Feedback
    private boolean ultimaRespuestaCorrecta;
    private long    tiempoInicioFeedback;

    // Puntaje
    private int aciertos   = 0;
    private int rondaActual = 0;
    

    /**
     * @param mapa   el mapa de palabras que ya tenés indexado
     * @param tamaño tamaño de tile en px (Vista.tamaño)
     */
    public MinijuegoDesafio(Mapa mapa, int tamaño) {
        this.buscador   = new ciudadBusqueda(mapa);
        this.palabras   = buscador.getPalabras();

        this.zona        = new ZonaDesafio(ZONA_COL, ZONA_FILA, ZONA_ANCHO, ZONA_ALTO, tamaño);
        this.puertaLista = new PuertaDesafio(
                PUERTA_LISTA_COL, PUERTA_LISTA_FILA, TipoPuerta.LISTA, tamaño);
            this.puertaArbol = new PuertaDesafio(
                PUERTA_ARBOL_COL, PUERTA_ARBOL_FILA, TipoPuerta.ARBOL, tamaño);
      
    }

    // ── Loop principal ────────────────────────────────────────────────────────

    /**
     * Llamar desde Vista.actualizar() en cada frame.
     */
    public void actualizar(JugadorVista jugador) {
        switch (estado) {

            case INACTIVO -> {
                // Si el jugador entra a la zona, empezar ronda
                if (!palabras.isEmpty() && zona.colisionaConJugador(jugador)) {
                    iniciarRonda();
                }
            }

            case MOSTRANDO -> {
                // Pausa breve para que el jugador lea la palabra, luego esperar elección
                // (podría agregar un timer; por ahora pasa directo a ESPERANDO)
                estado = EstadoMinijuego.ESPERANDO;
            }

            case ESPERANDO -> {
                // Detectar qué puerta cruza el jugador
                if (puertaLista.colisionaConJugador(jugador)) {
                    evaluarRespuesta("LISTA", jugador);
                } else if (puertaArbol.colisionaConJugador(jugador)) {
                    evaluarRespuesta("ARBOL", jugador);
                }
            }

            case FEEDBACK -> {
                // Esperar DURACION_FEEDBACK_MS y luego resetear para nueva ronda
                long ahora = System.currentTimeMillis();
                if (ahora - tiempoInicioFeedback >= DURACION_FEEDBACK_MS) {
                    puertaLista.setEstado(EstadoPuerta.NORMAL);
                    puertaArbol.setEstado(EstadoPuerta.NORMAL);

                    if (rondaActual >= RONDAS_PARA_GANAR) {
                        estado = EstadoMinijuego.GANADO;
                    } else {
                        // Teleportar jugador de vuelta a la entrada de la sala
                        teleportarJugadorAEntrada(jugador);
                        iniciarRonda();
                    }
                }
            }

            case GANADO -> {
                if (onFinalizadoCallback != null) {
                    onFinalizadoCallback.run();
                    onFinalizadoCallback = null; // evitar llamarlo más de una vez
                } 
            }
        }
    }

    // ── Dibujo ───────────────────────────────────────────────────────────────

    /**
     * Llamar desde Vista.paintComponent() después de dibujar construcciones y jugador.
     */
    public void draw(Graphics2D g2, JugadorVista jugador) {
        // Siempre dibujar la zona (podés comentar esta línea en producción)
        if (estado == EstadoMinijuego.INACTIVO) {
            zona.draw(g2, jugador);
        }

        // texto
        if (estado != EstadoMinijuego.INACTIVO) {
        	dibujarOverlay(g2);
        }
    }

    private void dibujarOverlay(Graphics2D g2) {
        int panelW = 500, panelH = 160;
        int panelX = 134, panelY = 20; // esquina superior centro de pantalla

        // Fondo
        g2.setColor(new Color(0, 0, 0, 190));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);
        g2.setColor(Color.WHITE);
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 20, 20);

        // Ronda
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.setColor(Color.LIGHT_GRAY);
        g2.drawString("Ronda " + rondaActual + "/" + RONDAS_PARA_GANAR
                + "   Aciertos: " + aciertos, panelX + 16, panelY + 22);

        // Palabra
        g2.setFont(new Font("Arial", Font.BOLD, 28));
        g2.setColor(Color.YELLOW);
        g2.drawString("\"" + palabraActual + "\"", panelX + 16, panelY + 62);

        // Tiempos, se puede sacar para q tenga sentido el juego
        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(new Color(100, 180, 255));
        g2.drawString("Lista:  " + tiempoLista + " ns", panelX + 16, panelY + 92);
        g2.setColor(new Color(180, 130, 255));
        g2.drawString("Árbol:  " + tiempoArbol + " ns", panelX + 16, panelY + 112);

        // Instrucción
        g2.setFont(new Font("Arial", Font.ITALIC, 13));
        g2.setColor(Color.WHITE);
        g2.drawString("←Lista |elegi la mas rapida| arbol →", panelX + 16, panelY + 142);

        // Feedback
        if (estado == EstadoMinijuego.FEEDBACK) {
            g2.setFont(new Font("Arial", Font.BOLD, 22));
            if (ultimaRespuestaCorrecta) {
                g2.setColor(new Color(0, 230, 0));
                g2.drawString("✓ ¡Correcto!", panelX + 330, panelY + 62);
            } else {
                g2.setColor(new Color(230, 50, 50));
                g2.drawString("✗ Era " + respuestaCorrecta, panelX + 295, panelY + 62);
            }
        }

        // Pantalla de victoria
        if (estado == EstadoMinijuego.GANADO) {
            g2.setColor(new Color(0, 0, 0, 200));
            g2.fillRoundRect(panelX, panelY, panelW, panelH, 20, 20);
            g2.setFont(new Font("Arial", Font.BOLD, 32));
            g2.setColor(Color.YELLOW);
            g2.drawString("¡GANASTE! " + aciertos + "/" + RONDAS_PARA_GANAR,
                    panelX + 60, panelY + 90);
        }
    }

    // ── Lógica interna ────────────────────────────────────────────────────────

    private void iniciarRonda() {
        rondaActual++;
        palabraActual    = palabras.get(random.nextInt(palabras.size()));
        tiempoLista      = buscador.medirTiempoPorLista(palabraActual);
        tiempoArbol      = buscador.medirTiempoPorArbol(palabraActual);
        respuestaCorrecta = tiempoArbol < tiempoLista ? "ARBOL" : "LISTA";
        estado           = EstadoMinijuego.MOSTRANDO;
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
            // Marcar cuál era la correcta
            (elegida.equals("LISTA") ? puertaArbol : puertaLista)
                .setEstado(EstadoPuerta.RESALTADA);
        }
        tiempoInicioFeedback = System.currentTimeMillis();
        estado = EstadoMinijuego.FEEDBACK;
    }

    /**
     * Mueve al jugador de vuelta a la entrada de la sala para la siguiente ronda.
     * col=24, fila=28 → justo antes del divisor.
     */
    private void teleportarJugadorAEntrada(JugadorVista jugador) {
        jugador.setWorldX(29* (jugador.getAreaSolida().width + jugador.getAreaSolida().x)) ; // aprox col 24
        jugador.setWorldY(32* (jugador.getAreaSolida().height + jugador.getAreaSolida().y));
        // Nota: usá jugador.setX/setY si los hacés públicos; de lo contrario
        // accedés a worldX/worldY directamente desde el mismo package UI
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public EstadoMinijuego getEstado()   { return estado; }
    public int             getAciertos() { return aciertos; }
    public boolean         isGanado()    { return estado == EstadoMinijuego.GANADO; }

    public void setOnFinalizadoCallback(Runnable callback) {
        this.onFinalizadoCallback = callback;
    }

    public PuertaDesafio getPuertaLista() { 
    	return puertaLista;
    	}
    public PuertaDesafio getPuertaArbol() { 
    	return puertaArbol; 
    	}
}