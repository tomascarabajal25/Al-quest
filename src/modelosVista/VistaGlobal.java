package modelosVista;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.SwingUtilities;

import modelos.GrafoCiudades;
import modelos.Jugador;
import modelos.NodoCiudad;
import modelos.PartidaGeneral;
import utils.ValidacionesUtiles;

/**
 * VistaGlobal — capa de presentación del mapa mundial de Al-Quest.
 *
 * Extiende {@link Vista} para heredar:
 *   - el loop de 60 FPS (run / startGameThread / detenerHilo)
 *   - el renderizado de tiles (ManejadorDeConstruccion)
 *   - el movimiento del jugador (JugadorVista)
 *   - la gestión de objetos (AdministradorDeObjetos)
 *
 * Responsabilidades propias de VistaGlobal:
 *   1. Conocer las posiciones de las 10 ciudades en el mapa de tiles.
 *   2. En cada frame, detectar si el jugador se acerca a una ciudad y
 *      presiona la tecla de acción (ENTER).
 *   3. Consultar al grafo si la ciudad es accesible y actuar en consecuencia:
 *        - Accesible   → llama a partidaGeneral.entrarACiudad(id)
 *        - Bloqueada   → muestra un mensaje flotante en pantalla
 *        - Completada  → muestra un mensaje de ciudad ya superada
 *   4. Renderizar el HUD: puntaje total y etiquetas sobre cada ciudad
 *      (BLOQUEADA / ACCESIBLE / COMPLETADA).
 *
 * ⚠️ REGLA DE ORO — Separación de capas:
 *   Esta clase SÍ puede importar modelos.PartidaGeneral y modelos.GrafoCiudades
 *   para CONSULTAR el estado, pero nunca modifica el grafo ni llama a lógica
 *   de negocio directamente. Toda acción se delega a PartidaGeneral.
 *
 * ⚠️ REGLA DE ORO — Sin rutas absolutas:
 *   El mapa se carga vía ruta relativa del classpath, igual que en Vista.
 */
public class VistaGlobal extends Vista {

    // ── Constantes de spawn ───────────────────────────────────────────────────

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Columna de spawn del jugador en el mapa global (en tiles). */
    public static final int SPAWN_COL  = 3;

    /** Fila de spawn del jugador en el mapa global (en tiles). */
    public static final int SPAWN_FILA = 25;

    // ── Constantes de interacción ─────────────────────────────────────────────

    /** Radio en píxeles para considerar que el jugador "está sobre" una ciudad. */
    private static final int RADIO_INTERACCION_PX = 56;

    /** Duración del mensaje flotante de ciudad bloqueada/completada (ms). */
    private static final long DURACION_MENSAJE_MS = 2500;

    // ── Posiciones de las ciudades en el mapa global (col, fila en tiles) ────
    //
    // Ajustá estos valores para que coincidan con los tiles donde dibujaste
    // cada ícono de ciudad en tu archivo /maps/mapa_global.txt.
    //
    // Formato: { idCiudad, columna, fila }
    private static final int[][] POSICIONES_CIUDADES = {
    	    {  1, 46, 25 },   // Ciudad de reco
    	    {  2, 46, 14 },   // Ciudad 2
    	    {  3, 22, 3 },   // Ciudad 3
    	    {  4, 3, 9 },   // Ciudad 4
    	    {  5, 46, 40 },   // Ciudad de Búsqueda
    	    {  6, 36, 46 },   // Ciudad 6
    	    {  7, 19, 35 },   // Ciudad 7
    	    {  8, 4, 39 },   // Torres de Hanoi
    	    {  9, 15, 47 },   // Ciudad 9
    	    { 10,  9, 30 },   // Ciudad 10
    	};

    // ── Colores de estado de ciudad ───────────────────────────────────────────

    private static final Color COLOR_COMPLETADA  = new Color(  80, 220, 100, 210);
    private static final Color COLOR_ACCESIBLE   = new Color( 255, 200,  50, 210);
    private static final Color COLOR_BLOQUEADA   = new Color( 180,  50,  50, 160);
    private static final Color COLOR_LABEL_BG    = new Color(   0,   0,   0, 160);
    private static final Color COLOR_HUD_BG      = new Color(   0,   0,   0, 180);

    private static final Font FONT_LABEL   = new Font("Arial", Font.BOLD,  11);
    private static final Font FONT_HUD     = new Font("Arial", Font.BOLD,  14);
    private static final Font FONT_MENSAJE = new Font("Arial", Font.BOLD,  16);

    // ── Estado de la vista ────────────────────────────────────────────────────

    /** Referencia al orquestador central; solo se usa para consultas y callbacks. */
    private final PartidaGeneral partidaGeneral;

    /** Mensaje flotante que se muestra al intentar entrar a ciudad bloqueada/completada. */
    private String  mensajeFlotante   = "";

    /** Timestamp en que empezó a mostrarse el mensaje flotante. */
    private long    tiempoMensaje     = 0;

    /** ID de ciudad sobre la que se detectó colisión en el frame actual (-1 si ninguna). */
    private int     ciudadCercanaId   = -1;

    /** Cooldown para evitar disparar entrarACiudad múltiples veces con la misma pulsación. */
    private boolean teclaEnterConsumed = false;
    // atributo nuevo
    private final Map<Integer, BufferedImage> iconosCiudad = new HashMap<>();

    //Gestion de SKINS
    //Cooldown para la tecla T de la tienda de SKINS
    private boolean teclaTiendaConsumed = false;
    
    private boolean teclaReiniciarConsumed = false;
    
    
    

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Crea la vista global del mapa mundial.
     *
     * PRE:  rutaMapa != null y el archivo existe en el classpath.
     *       jugador != null.
     *       partidaGeneral != null.
     * POST: Vista lista para añadirse a un JFrame y llamar startGameThread().
     *       El mapa de tiles queda cargado en memoria.
     *       No se inicia ningún hilo hasta llamar a startGameThread().
     *
     * @param rutaMapa        ruta relativa del mapa, ej: "/maps/mapa_global.txt"
     * @param jugador         datos del jugador (nombre, stats)
     * @param rutaSprites     ruta relativa de los sprites del jugador
     * @param partidaGeneral  orquestador central, usado para consultar el grafo
     *                        y disparar transiciones de ciudad
     */
    public VistaGlobal(String rutaMapa,
                       Jugador jugador,
                       String rutaSprites,
                       PartidaGeneral partidaGeneral) {

        // Delega la inicialización base (tiles, jugadorVista, keyhandler, etc.)
        super(rutaMapa, jugador, SPAWN_COL, SPAWN_FILA, rutaSprites,new KeyhandlerGlobal());
        ValidacionesUtiles.esDistintoDeNull(partidaGeneral, "la partida no puede ser nula");
        this.partidaGeneral = partidaGeneral;
        System.out.println(partidaGeneral.getMapaMundi().getNodos().size());

        cargarIconosCiudades();

        
        
        

    }

    // ── Loop de actualización ─────────────────────────────────────────────────

    /**
     * Se ejecuta en cada frame (≈60 veces por segundo).
     *
     * PRE:  startGameThread() ya fue llamado.
     * POST: detecta ciudades cercanas, evalúa pulsación de ENTER y dispara
     *       la transición si corresponde. El jugador se mueve normalmente.
     *
     * Orden intencionado: primero mover al jugador, luego evaluar si está
     * sobre una ciudad, para que la posición sea la más actualizada del frame.
     */
    @Override
    public void actualizar() {
        super.actualizar();           // mueve al jugador y actualiza el minijuego base

        ciudadCercanaId = detectarCiudadCercana();

        // Limpiar el cooldown cuando se suelta ENTER
        // (KeyHandler expone los booleans directamente, igual que en Vista)
        if (!this.keyhandler.enterPresionado) {
            teclaEnterConsumed = false;
        }

        if (ciudadCercanaId != -1 && keyhandler.enterPresionado && !teclaEnterConsumed) {
            teclaEnterConsumed = true;
            procesarEntradaCiudad(ciudadCercanaId);
        }
        //casteo el keyhandler al q tiene la vista global
        KeyhandlerGlobal khGlobal = (KeyhandlerGlobal) this.keyhandler;
        if (!khGlobal.getTiendaPressed()) {
        	teclaTiendaConsumed = false;
        }
        
        if (khGlobal.getTiendaPressed() && !teclaTiendaConsumed) {
            teclaTiendaConsumed = true;
            abrirTienda();
        }
        
        // reiniciar las ciudades en caso de apretar la x y tener todas las ciudades completadas
     
        
		if (!khGlobal.getReiniciarPressed()) {
            teclaReiniciarConsumed  = false; 
        }
       
        if (khGlobal.getReiniciarPressed() && !teclaReiniciarConsumed) {
            teclaReiniciarConsumed = true; // Consumimos el pulso
            evaluarYReiniciarJuego();
        }
       
    }


    /**
     * Evalúa si se cumplen los requisitos para reiniciar y procesa los cuadros
     * de diálogo informativos y de confirmación utilizando JOptionPane.
     */
    private void evaluarYReiniciarJuego() {
        // Pausamos momentáneamente el hilo del juego para evitar que el jugador se mueva de fondo
        detenerHilo();

        SwingUtilities.invokeLater(() -> {
            // 1. Validar si realmente completó todo el mapa mundi
            if (!partidaGeneral.estaTerminado()) {
                javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "No podés reiniciar el mapa todavía.\nEs necesario completar las 10 ciudades primero.",
                    "Progreso Insuficiente",
                    javax.swing.JOptionPane.WARNING_MESSAGE
                );
            } else {
                // 2. Si está terminado, pedir confirmación explícita
                int respuesta = javax.swing.JOptionPane.showConfirmDialog(
                    this,
                    "¿Estás seguro de que querés reiniciar el juego?\nSe borrará todo tu progreso de ciudades superadas.",
                    "Confirmar Reinicio de Partida",
                    javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE
                );

                // 3. Si responde que SÍ, ejecutamos el reinicio en el modelo
                if (respuesta == javax.swing.JOptionPane.YES_OPTION) {
                    partidaGeneral.getMapaMundi().reiniciarTodasLasCiudades();
                    // Forzamos un guardado instantáneo para persistir el mapa vacío en disco
                    persistencia.GestorDeInicio.guardarSesion(partidaGeneral);
                    
                    javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "¡El mapa mundial ha sido reiniciado con éxito!\nBuena suerte en tu nueva aventura.",
                        "Partida Reiniciada",
                        javax.swing.JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }

            // Al cerrar los cuadros de diálogo, limpiamos los estados del teclado y reanudamos el bucle
            if (this.keyhandler != null) {
                ((KeyhandlerGlobal) this.keyhandler).reset();
            }
            teclaReiniciarConsumed = true;
            startGameThread();
        });
    }

	/**
     * Renderiza el mapa, los tiles, el jugador y el HUD propio de VistaGlobal.
     *
     * PRE:  El grafo de ciudades está inicializado en partidaGeneral.
     * POST: Dibuja en orden: tiles → jugador → etiquetas de ciudad → HUD → mensaje flotante.
     *       No modifica ningún estado del modelo.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);          // dibuja tiles, objetos y jugador base

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        dibujarEtiquetasCiudades(g2);
        dibujarHUD(g2);
        dibujarMensajeFlotante(g2);

        g2.dispose();
    }
    private void cargarIconosCiudades() {
        int[] ids = {1, 2,3, 4, 5,6,7, 8,9,10};
        for (int id : ids) {
            try {
                BufferedImage img = ImageIO.read(
                    Objects.requireNonNull(
                        getClass().getResourceAsStream("/assets/ciudades/ciudad_" + id + ".bmp")
                    )
                );
                iconosCiudad.put(id, img);
            } catch (Exception e) {
                System.out.println("No se encontró ícono para ciudad " + id);
            }
        }
    }


    //Tienda de SKINS / GESTION DE SKINS
    /**
     * Pausa el loop del juego, abre TiendaSkins como dialogo modal y reanuda el loop
     * cuando la persona cierre la tienda
     * 
     * pre: el hilo del juego corriendo
     * post: loop se detiene durante la tienda y se reanuda al cerrar
     *           la skin del jugador posiblemente cambio
     */
    private void abrirTienda() {
        detenerHilo(); 
        
        SwingUtilities.invokeLater(() -> {
            TiendaSkins tienda = new TiendaSkins(partidaGeneral, this);
            tienda.setVisible(true); 
            
            // AL CERRAR LA TIENDA:
            // Reseteamos el manejador de eventos usando polimorfismo.
            // Si es un KeyhandlerGlobal, llamará a su reset() específico (apagando la T y flechas).
            if (this.keyhandler != null) {
                if (this.keyhandler instanceof KeyhandlerGlobal) {
                    ((KeyhandlerGlobal) this.keyhandler).reset();
                } else {
                    this.keyhandler.reset();
                }
            }
            
            teclaTiendaConsumed = true; 
            teclaEnterConsumed = false; 
            
            startGameThread(); 
        });
    }
    
    public void reiniciarCiudades() {
    	if (partidaGeneral.estaTerminado()) {
    		partidaGeneral.reiniciar();
    	}
    }


    // ── Detección de ciudades ─────────────────────────────────────────────────

    /**
     * Recorre las posiciones de las 10 ciudades y devuelve el id de la más
     * cercana dentro de RADIO_INTERACCION_PX, o -1 si ninguna está cerca.
     *
     * PRE:  jugadorVista inicializado.
     * POST: no modifica estado; es una consulta pura O(10).
     */
    private int detectarCiudadCercana() {
        // 1. Obtener el centro del jugador utilizando estrictamente coordenadas del MUNDO
        int jCx = getJugadorVista().getWorldX() 
                + getJugadorVista().getAreaSolida().x
                + getJugadorVista().getAreaSolida().width / 2;
                
        int jCy = getJugadorVista().getWorldY()
                + getJugadorVista().getAreaSolida().y
                + getJugadorVista().getAreaSolida().height / 2;

        // 2. Comparar contra la posición absoluta de cada ciudad en el mundo
        for (int[] pos : POSICIONES_CIUDADES) {
            int id  = pos[0];
            int cx  = pos[1] * getTamanio() + getTamanio() / 2;
            int cy  = pos[2] * getTamanio() + getTamanio() / 2;
            
            double dist = Math.hypot(cx - jCx, cy - jCy);
            if (dist <= RADIO_INTERACCION_PX) {
                return id;
            }
        }
        return -1;
    }

    // ── Lógica de transición ──────────────────────────────────────────────────

    /**
     * Evalúa si la ciudad es accesible y dispara la acción correspondiente.
     *
     * PRE:  idCiudad ∈ [1, GrafoCiudades.MAX_CIUDADES].
     * POST:
     *   - Si la ciudad ya fue completada → muestra mensaje "Ya completada".
     *   - Si es accesible pero no completada → llama a
     *     {@code partidaGeneral.entrarACiudad(idCiudad)}, que se encarga de
     *     ocultar esta ventana y pausar el hilo.
     *   - Si está bloqueada → muestra mensaje flotante con el motivo.
     *
     * @param idCiudad identificador de la ciudad con la que interactuó el jugador
     */
    private void procesarEntradaCiudad(int idCiudad) {
        GrafoCiudades grafo = partidaGeneral.getMapaMundi();
        NodoCiudad    nodo  = grafo.obtenerCiudad(idCiudad);

        if (nodo == null) {
            // Ciudad no registrada en el grafo todavía (comentada en PartidaGeneral)
            mostrarMensaje("Ciudad " + idCiudad + " aún no disponible en esta versión.");
            return;
        }

        if (nodo.isCompletada()) {
            mostrarMensaje("✓ " + nodo.getNombre() + " ya fue completada.");
            return;
        }

        if (!grafo.esCiudadAccesible(idCiudad)) {
            mostrarMensaje("✗ " + nodo.getNombre() + " está bloqueada. "
                + "Completá una ciudad vecina primero.");
            return;
        }

        // Ciudad accesible y no completada → PartidaGeneral gestiona la transición.
        // Esto detendrá el hilo de esta vista y ocultará la ventana global.
        partidaGeneral.entrarACiudad(idCiudad);
    }

    // ── Dibujo de etiquetas de ciudad ─────────────────────────────────────────

    /**
     * Dibuja un indicador visual sobre cada ciudad registrada en el grafo,
     * mostrando su estado (BLOQUEADA / ACCESIBLE / COMPLETADA) y un ícono de color.
     *
     * PRE:  partidaGeneral.getMapaMundi() != null.
     * POST: no modifica el estado del grafo; solo renderiza.
     *
     * Las ciudades comentadas en PartidaGeneral no aparecen en el grafo, por lo que
     * se renderizan con color gris y etiqueta "NO DISPONIBLE".
     */
    private void dibujarEtiquetasCiudades(Graphics2D g2) {
        GrafoCiudades         grafo = partidaGeneral.getMapaMundi();
        Map<Integer, NodoCiudad> nodos = grafo.getNodos();

        for (int[] pos : POSICIONES_CIUDADES) {
            int id  = pos[0];
            int cx  = pos[1] * getTamanio() + getTamanio() / 2 - getJugadorVista().getWorldX() + getJugadorVista().getScreenX();
            int cy  = pos[2] * getTamanio()  + getTamanio() / 2  - getJugadorVista().getWorldY() + getJugadorVista().getScreenY();

            NodoCiudad nodo = nodos.get(id);

            // ── Determinar color y etiqueta ────────────────────────────────
            Color  colorEstado;
            String etiqueta;

            if (nodo == null) {
                colorEstado = new Color(100, 100, 100, 140);
                etiqueta    = "C" + id + " — PRÓXIMAMENTE";
            } else if (nodo.isCompletada()) {
                colorEstado = COLOR_COMPLETADA;
                etiqueta    = "C" + id + " - " + nodo.getNombre();
            } else if (grafo.esCiudadAccesible(id)) {
                colorEstado = COLOR_ACCESIBLE;
                etiqueta    = "C" + id + " - " + nodo.getNombre();
            } else {
                colorEstado = COLOR_BLOQUEADA;
                etiqueta    = "C" + id + " - " + nodo.getNombre();
            }

            // ── Ícono de ciudad ────────────────────────────────────────────
            int lado = getTamanio()*3; // tamaño del tile (48px con tu escala)
            BufferedImage icono = iconosCiudad.get(id);

            Composite orig = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f));

            if (icono != null) {
                // Dibujar bitmap centrado en (cx, cy)
                g2.drawImage(icono, cx - lado / 2, cy - lado / 2, lado, lado, null);
            } else {
                // Fallback: círculo si no hay imagen (ciudades próximamente, etc.)
                int radio = lado / 2 - 2;
                g2.setColor(colorEstado);
                g2.fillOval(cx - radio, cy - radio, radio * 2, radio * 2);
            }

            g2.setComposite(orig);

            // Borde de color según estado 
            int radio = lado / 2 - 2;
            g2.setColor(colorEstado.darker());
            g2.setStroke(new BasicStroke(2f));
            g2.drawOval(cx - radio, cy - radio, radio * 2, radio * 2);
            g2.setStroke(new BasicStroke(1f));
            
            // ── Etiqueta de texto ──────────────────────────────────────────
            g2.setFont(FONT_LABEL);
            int anchoTexto = g2.getFontMetrics().stringWidth(etiqueta);
            int labelX = cx - anchoTexto / 2;
            int labelY = cy - radio - 6;

            // Fondo de la etiqueta
            g2.setColor(COLOR_LABEL_BG);
            g2.fillRoundRect(labelX - 4, labelY - 13, anchoTexto + 8, 16, 6, 6);

            // Texto de la etiqueta
            g2.setColor(colorEstado.brighter());
            g2.drawString(etiqueta, labelX, labelY);

            // Indicador [ENTER] si el jugador está cerca
            if (id == ciudadCercanaId && nodo != null && !nodo.isCompletada()) {
                g2.setFont(new Font("Arial", Font.ITALIC, 10));
                g2.setColor(Color.WHITE);
                g2.drawString("[ENTER]", cx - 18, cy + radio + 14);
            }
        }
    }

    // ── HUD principal ─────────────────────────────────────────────────────────

    /**
     * Dibuja el panel HUD en la esquina superior izquierda con el puntaje
     * acumulado del jugador y el nombre de la ciudad cercana (si aplica).
     * Tambien, muestra el "T" para la Tienda de Skins
     *
     * PRE:  partidaGeneral != null.
     * POST: renderiza el HUD; no modifica estado del modelo.
     */
    private void dibujarHUD(Graphics2D g2) {
        int px = 10, py = 10, pw = 420, ph = 56;

        // Fondo semitransparente
        Composite orig = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.82f));
        g2.setColor(COLOR_HUD_BG);
        g2.fillRoundRect(px, py, pw, ph, 12, 12);
        g2.setComposite(orig);

        g2.setColor(new Color(80, 160, 255));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(px, py, pw, ph, 12, 12);
        g2.setStroke(new BasicStroke(1f));
        
        
        
        
        // Puntaje
        g2.setFont(FONT_HUD);
        g2.setColor(new Color(255, 220, 60));
        g2.drawString("AI-QUEST - Los Compiladores -", px + 12, py + 22);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.drawString("Puntaje total: " + partidaGeneral.getPuntajeTotal() +
        		"    |    [T] Tienda de Skins    |    [X] Reiniciar Mapa ", px + 12, py + 40);
        
        
        // Ciudad cercana
        if (ciudadCercanaId != -1) {
            NodoCiudad nodo = partidaGeneral.getMapaMundi().obtenerCiudad(ciudadCercanaId);
            if (nodo != null) {
                g2.setColor(new Color(200, 230, 255));
                g2.drawString("Cerca: " + nodo.getNombre() + "  [ENTER para ingresar]",
                    px + pw + 10, py + 22);
            }
        }
    }

    // ── Mensaje flotante ──────────────────────────────────────────────────────

    /**
     * Registra un mensaje flotante para mostrar en pantalla durante
     * DURACION_MENSAJE_MS milisegundos.
     *
     * PRE:  mensaje != null.
     * POST: mensajeFlotante y tiempoMensaje quedan actualizados.
     *
     * @param mensaje texto a mostrar (ej: "✗ Ciudad bloqueada")
     */
    private void mostrarMensaje(String mensaje) {
        this.mensajeFlotante = mensaje;
        this.tiempoMensaje   = System.currentTimeMillis();
    }

    /**
     * Dibuja el mensaje flotante centrado en pantalla si aún no expiró.
     *
     * PRE:  —
     * POST: si el mensaje expiró, lo borra del estado.
     */
    private void dibujarMensajeFlotante(Graphics2D g2) {
        if (mensajeFlotante.isEmpty()) return;

        long ahora = System.currentTimeMillis();
        if (ahora - tiempoMensaje > DURACION_MENSAJE_MS) {
            mensajeFlotante = "";
            return;
        }

        // Fade-out progresivo en el último 40% de la duración
        float tiempoRestante = (DURACION_MENSAJE_MS - (ahora - tiempoMensaje))
                               / (float) DURACION_MENSAJE_MS;
        float alpha = Math.min(1f, tiempoRestante * 2.5f);

        g2.setFont(FONT_MENSAJE);
        int anchoTexto = g2.getFontMetrics().stringWidth(mensajeFlotante);
        int screenW    = getWidth();
        int screenH    = getHeight();
        int msgX       = (screenW - anchoTexto) / 2;
        int msgY       = screenH / 2 - 20;

        Composite orig = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha * 0.75f));
        g2.setColor(Color.BLACK);
        g2.fillRoundRect(msgX - 12, msgY - 22, anchoTexto + 24, 32, 10, 10);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.setColor(Color.WHITE);
        g2.drawString(mensajeFlotante, msgX, msgY);
        g2.setComposite(orig);
    }

    // ── Getters de soporte ────────────────────────────────────────────────────

    /**
     * @return id de la ciudad más cercana al jugador en este frame, o -1 si ninguna.
     */
    public int getCiudadCercanaId() {
        return ciudadCercanaId;
    }
}