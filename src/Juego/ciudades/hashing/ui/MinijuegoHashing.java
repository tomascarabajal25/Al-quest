package juego.ciudades.hashing.ui;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;

import juego.ciudades.hashing.CiudadHashing;
import juego.ciudades.hashing.ElementoHash;
import juego.ciudades.hashing.PasoHash;
import modelosVista.JugadorVista;
import modelos.Minijuego;
import modelosVista.Vista;
import utils.ValidacionesUtiles;


/**
 * Minijuego ciudad 6 Hashing integrado al mundo del juego.
 * 
 * Misma logica que MinijuegoORdenamiento:
 * Implementa minijuego (actualizar + draw), lee teclado
 * mediante keyHandler propio, manipula objetos del mundo
 * (los SlotVista) y dibuja un HUD.
 * Tecnicamente, la logica real esta en CiudadHashing, pero
 * este minijuego la maneja y la meustra.
 * 
 * El juego tiene dos fases, cada una con modo "manual" y "resolver" automatico:
 * 
 * 1) Fase Insertar:
 * El jugador camina hasta el slot donde cree que cae la clave del elemento actual y toca espacio.
 * Si acierta, se inserta y se ve crecer la cadena (las colisiones se encadenan).
 * Si se equivoca, el sistema le avisa que slot iba realmente.
 * Con R, se activa el resolvedor automatico, inserta los elementos paso a paso.
 * 
 * 2) Fase buscar:
 * El jugador camina al slot donde piensa que va a estar la clave buscada y toca ESPACIO.
 * El sistema por consola muestra si la encontro recorriendo la cadena en ese slot.
 * Con R, resolver automatico busca las claves restantes paso a paso.
 * 
 * Al terminar ambas fases, se notifica la victoria con el callback onVictoria.
*/

public class MinijuegoHashing implements Minijuego {

    //ENUMERADOS
    private enum Estado { INSERTAR_MANUAL, RESOLVIENDO_INSERCION, BUSCAR_MANUAL, RESOLVIENDO_BUSQUEDA, FINALIZADO }


    //CONSTANTES
    private static final int TICK_POR_PASO = 50; //Frames entre cada paso del resolver automatico.
    private static final int RADIO_INTERACCION = 80; // Radio en px para detectar el slot mas cercano al jugador.
    private static final int MAX_CARACTERES_LINEA = 64; //Maximo de caracteres por linea del mensaje del HUD (si es mas largo, se parte en varias).
    private static final int ALTO_LINEA_MENSAJE = 14; //Alto en px de cada linea del mensaje del HUD. 

    private static final Font FONT_TITULO    = new Font("Monospaced", Font.BOLD, 16);
    private static final Font FONT_HUD       = new Font("Monospaced", Font.BOLD, 12);
    private static final Font FONT_MENSAJE   = new Font("Monospaced", Font.PLAIN, 11);

    private static final Color COLOR_HUD_BG  = new Color(0, 0, 0, 170);
    private static final Color COLOR_HUD_FG  = new Color(200, 230, 255);
    private static final Color COLOR_TITULO  = new Color(255, 200, 50);
    private static final Color COLOR_OK      = new Color(80, 220, 100);
    private static final Color COLOR_ERROR   = new Color(235, 90, 90);
    private static final Color COLOR_CONTROL = new Color(130, 160, 200);


    //ATRIBUTOS
    private Runnable onVictoria;

    private Estado estado = Estado.INSERTAR_MANUAL;

    private final CiudadHashing         ciudad;
    private final List<SlotVista>       slotsVista;
    private final List<ElementoHash>    elementosAInsertar;
    private final List<Integer>         clavesABuscar;
    private final KeyHandlerHashing     keyHash;

    private int indiceInsercion = 0;
    private int indiceBusqueda  = 0;

    private int tickDesdeUltimoPaso = 0;
    private boolean resolverPausado = false;

    private int aciertos = 0;
    private int errores  = 0;

    private String mensajeEstado = "¡Llego un elemento! Caminá al slot correcto y presioná ESPACIO.";
    private boolean ultimaAccionFueError = false;
 

    //CONSTRUCTORES
    /**
     * PRE:
     * @param ciudad             no puede ser nula
     * @param slotsVista         no nula, al menos 1 slot
     * @param elementosAInsertar no nula, al menos 1 elemento
     * @param clavesABuscar      no nula (no hay problema si esta vacia)
     * @param vista              no nula (para registrar el KeyListener)
     * POST: crea el minijuego en fase INSERTAR_MANUAL y queda a la espera del jugador.
     */
    public MinijuegoHashing(CiudadHashing ciudad, List<SlotVista> slotsVista, List<ElementoHash> elementosAInsertar,
                            List<Integer> clavesABuscar, Vista vista) {
        ValidacionesUtiles.esDistintoDeNull(ciudad, "ciudad");
        ValidacionesUtiles.esDistintoDeNull(vista, "vista");
        if (slotsVista == null || slotsVista.isEmpty()) {
            throw new IllegalArgumentException("ERROR: se necesita al menos 1 slot.");
        }
        if (elementosAInsertar == null || elementosAInsertar.isEmpty()) {
            throw new IllegalArgumentException("ERROR: se necesita al menos 1 elemento para insertar.");
        }
        ValidacionesUtiles.esDistintoDeNull(clavesABuscar, "claves a buscar");

        this.ciudad             = ciudad;
        this.slotsVista         = new ArrayList<>(slotsVista);
        this.elementosAInsertar = new ArrayList<>(elementosAInsertar);
        this.clavesABuscar      = new ArrayList<>(clavesABuscar);

        this.keyHash = new KeyHandlerHashing();
        vista.addKeyListener(keyHash);
    }



    //MINIJUEGO
    /**
     * POST: Actualiza la logica del minijuego segun la fase actual. 
     */
    @Override
    public void actualizar(JugadorVista jugador){
        if (estado == Estado.FINALIZADO) {
            return;
        }

        for (SlotVista sv : slotsVista) {
            sv.actualizar();
        }

        procesarEscape();

        switch (estado) {

            case INSERTAR_MANUAL:
                procesarInsercionManual(jugador);
                if (keyHash.resolverPresionado) {
                    keyHash.resolverPresionado = false;
                    activarResolverInsercion();
                }
                break;

            case RESOLVIENDO_INSERCION:
                procesarPausaResolver();
                if (!resolverPausado) {
                    avanzarInsercionAutomatica();
                }
                break;

            case BUSCAR_MANUAL:
                procesarBusquedaManual(jugador);
                if (keyHash.resolverPresionado) {
                    keyHash.resolverPresionado = false;
                    activarResolverBusqueda();
                }
                break;

            case RESOLVIENDO_BUSQUEDA:
                procesarPausaResolver();
                if (!resolverPausado) {
                    avanzarBusquedaAutomatica();
                }
                break;

            default:
                break;
        }
    }


    /**
     * POST: Dibuja el HUD del minijuego (titulo, objetivo, mensaje, controles y progreso)
     */
    @Override
    public void draw(Graphics2D g2, JugadorVista jugador) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        dibujarHUD(g2);
    }



    //FASE INSERTAR (MANUAL)
    /**
     * POST: resalta el slot mas cercano al personaje, y si tocas ESPACIO intenta insertar el elemento
     *       actual en ese slot. 
     */
    private void procesarInsercionManual(JugadorVista jugador) {
        
        int cercano = encontrarSlotCercano(jugador);
        resaltarSoloA(cercano);

        if (!keyHash.espacioPresionado) {
            return;
        }

        keyHash.espacioPresionado = false;

        if (cercano == -1) {
            mensajeEstado = "No hay ningun slot cerca. Acercate mas. ";
            return;
        }


        ElementoHash elemento   =   elementosAInsertar.get(indiceInsercion);
        int clave               =   elemento.getClave();
        int indiceCorrecto      =   ciudad.calcularIndice(clave);
        SlotVista slotElegido   =   slotsVista.get(cercano);

        if (slotElegido.getIndice() == indiceCorrecto) {
            PasoHash paso = ciudad.insertar(clave, elemento);
            slotElegido.marcarOk();
            aciertos++;
            ultimaAccionFueError = false;
            mensajeEstado = paso.getDescripcion();
            indiceInsercion++;

            if (insercionTerminada()) {
                pasarAFaseBusqueda();
            }

        } else {
            slotElegido.marcarError();
            errores++;
            ultimaAccionFueError = true;
            mensajeEstado = "¡¡Slot erroneo!! - La clave " + clave + " va al slot " + indiceCorrecto
                             + "  (" + clave + " % " + ciudad.getCantidadSlots() + ")";
        }

    }



    //FASE INSERTAR (RESOLVER AUTOMATICAMENTE)
    /**
     * POST: cambia a modo resolver automatico de insercion y reinicia el temporizador de pasos. 
     */
    private void activarResolverInsercion() {
        estado = Estado.RESOLVIENDO_INSERCION;
        tickDesdeUltimoPaso = 0;
        resolverPausado = false;
        mensajeEstado = "¡¡MODO NIKOLA TESLA ACTIVADO!!\nResolver AUTOMATICO de inserciones  [R o ESPACIO = pausar | ESC = detener]";
    }

    /**
     * POST: cada TICK_POR_PASO frames inserta el siguiente elemento pendiente y lo muestra. 
     */
    private void avanzarInsercionAutomatica() {
        tickDesdeUltimoPaso++;

        if (tickDesdeUltimoPaso < TICK_POR_PASO) {
            return;
        }

        tickDesdeUltimoPaso = 0;

        if (insercionTerminada()) {
            pasarAFaseBusqueda();
            return;
        }

        ElementoHash elemento = elementosAInsertar.get(indiceInsercion);
        PasoHash paso = ciudad.insertar(elemento.getClave(), elemento);
        destacarSlotDeTabla(paso.getIndiceSlot());
        mensajeEstado = paso.getDescripcion();
        ultimaAccionFueError = false;
        indiceInsercion++;
    }



    //FASE BUSCAR (MANUAL)
    /**
     * POST: Resalta el slot mas cercano.
     *       Si el jugador presiona ESPACIO, busca la clave actual en ese slot 
     *       (siempre que sea el slot donde realmente caeria).
     */
    private void procesarBusquedaManual(JugadorVista jugador) {
        int cercano = encontrarSlotCercano(jugador);
        resaltarSoloA(cercano);

        if (!keyHash.espacioPresionado) {
            return;
        }

        keyHash.espacioPresionado = false;

        if (cercano == -1) {
            mensajeEstado = "No hay ningun slot cerca. Acercate mas.";
            return;
        }

        int clave                = clavesABuscar.get(indiceBusqueda);
        int indiceCorrecto       = ciudad.calcularIndice(clave);
        SlotVista slotElegido    = slotsVista.get(cercano);

        if (slotElegido.getIndice() == indiceCorrecto) {
            PasoHash paso = ciudad.buscar(clave);

            if (paso.isExito()) {
                slotElegido.marcarOk();
                aciertos++;
                ultimaAccionFueError = false;
            } else {
                slotElegido.marcarError();
                ultimaAccionFueError = true;
            }

            mensajeEstado = paso.getDescripcion();
            indiceBusqueda++;
            
            if (busquedaTerminada()) {
                finalizar();
            }
        } else {
            slotElegido.marcarError();
            errores++;
            ultimaAccionFueError = true;
            mensajeEstado = "¡¡Slot erroneo!! - La clave " + clave + " se busca en el slot " + indiceCorrecto
                            + "  (" + clave + " % " + ciudad.getCantidadSlots() + ")";
        }
    }



    //FASE BUSCAR (RESOLVER AUTOMATICO)
    /**
     * POST: Cambia al modo de resolver automaticamente de busqueda y reinicia el temporizador de pasos. 
     */
    private void activarResolverBusqueda() {
        estado = Estado.RESOLVIENDO_BUSQUEDA;
        tickDesdeUltimoPaso = 0;
        resolverPausado = false;
        mensajeEstado = "¡¡MODO EINSTEIN ACTIVADO!!\nResolver AUTOMATICO de busquedas  [R o ESPACIO = pausar | ESC = detener]";
    }


    /**
     * POST: cada TICK_POR_PASO frames busca la siguiente clave pendiente y la muestra. 
     */
    private void avanzarBusquedaAutomatica() {
        tickDesdeUltimoPaso++;

        if (tickDesdeUltimoPaso < TICK_POR_PASO) {
            return;
        }

        tickDesdeUltimoPaso = 0;

        if (busquedaTerminada()) {
            finalizar();
            return;
        }

        int clave = clavesABuscar.get(indiceBusqueda);
        PasoHash paso = ciudad.buscar(clave);
        destacarSlotDeTabla(paso.getIndiceSlot());
        mensajeEstado = paso.getDescripcion();
        ultimaAccionFueError = !paso.isExito();
        indiceBusqueda++;
    }


    //TRANSICIONES Y CONTROL
    /**
     * POST: Si quedan claves para buscar pasa a la fase de busqueda, de lo contrario, finaliza. 
     */
    private void pasarAFaseBusqueda() {
        limpiarResaltados();

        if (clavesABuscar.isEmpty()) {
            finalizar();
            return;
        }

        estado = Estado.BUSCAR_MANUAL;
        ultimaAccionFueError = false;
        mensajeEstado = " ¡Felicitaciones!\nTodo insertado. Ahora a BUSCAR: caminá al slot correcto y tocá ESPACIO.";
    }


    /**
     * POST: Marca el minijuego como finalizado y dispara el callback de victoria. 
     */
    private void finalizar() {
        estado = Estado.FINALIZADO;
        limpiarResaltados();
        ultimaAccionFueError = false;

        mensajeEstado = "¡¡¡¡¡¡¡¡Ciudad completada!!!!!!!!\nAciertos: " + aciertos + "  Errores: " + errores;
        
        if (onVictoria != null) {
            onVictoria.run();
        }
    }


    /**
     * POST: Si esta resolviendo y se preciona R o ESPACIO, alterna pause. 
     */
    private void procesarPausaResolver() {
        if (keyHash.resolverPresionado) {
            keyHash.resolverPresionado = false;
            alternarPausa();
        }

        if (keyHash.espacioPresionado) {
            keyHash.espacioPresionado = false;
            alternarPausa();
        }
    }

    private void alternarPausa() {
        resolverPausado = !resolverPausado;

        mensajeEstado = resolverPausado ? "PAUSADO — R o ESPACIO para continuar"
                                        : "Resolviendo automaticamente...";
    }


    /**
     * POST: Si se presiona ESCAPE durante un resolver, lo detiene y vuelve al modo manual de esa fase. 
     */
    private void procesarEscape() {
        if (!keyHash.escapePresionado) {
            return;
        }

        keyHash.escapePresionado = false;

        if (estado == Estado.RESOLVIENDO_INSERCION) {
            estado = Estado.INSERTAR_MANUAL;
            resolverPausado = false;
            mensajeEstado = "Resolver detenido. Segui insertando a mano o R para reanudar.";
        } else if (estado == Estado.RESOLVIENDO_BUSQUEDA) {
            estado = Estado.BUSCAR_MANUAL;
            resolverPausado = false;
            mensajeEstado = "Resolver detenido. Segui buscando a mano o R para reanudar.";
        }

    }




    //HELPERS
    /**
     * POST: devuelve el indice (dentro de slotsVista) del slot mas cercano al jugador, o -1 si no hay
     *       ningun slot dentro del radio para interactuar con el. 
     */
    private int encontrarSlotCercano(JugadorVista jugador) {
        int jugadorCx = jugador.getWorldX() + jugador.getAreaSolida().x + jugador.getAreaSolida().width  / 2;
        int jugadorCy = jugador.getWorldY() + jugador.getAreaSolida().y + jugador.getAreaSolida().height / 2;

        int    mejorIndice   = -1;
        double mejorDistancia = RADIO_INTERACCION;

        for (int i = 0; i < slotsVista.size(); i++) {
            SlotVista sv = slotsVista.get(i);
            double distancia = Math.hypot(sv.getWorldX() - jugadorCx, sv.getWorldY() - jugadorCy);
            
            if (distancia < mejorDistancia) {
                mejorDistancia = distancia;
                mejorIndice = i;
            }
        }
        return mejorIndice;
    }


    /**
     * POST: deja destacado unicamente el slot indicado (por posicion en slotsVista),
     *       -1 = ninguno. 
     */
    private void resaltarSoloA(int indiceEnLista) {
        for (int i = 0; i < slotsVista.size(); i++) {
            slotsVista.get(i).setDestacado(i == indiceEnLista);
        }
    }


    /**
     * POST: quita el resaltado de todos los slots. 
     */
    private void limpiarResaltados() {
        for (SlotVista sv : slotsVista) {
            sv.setDestacado(false);
        }
    }


    /**
     * POST: marca con flash verde el SlotVista cuyo indice de tabla coincide con el dado
     *       Lo usan los resolvedores automaticos, asi marcan donde interactuaron. 
     */
    private void destacarSlotDeTabla(int indiceTabla) {
        for (SlotVista sv : slotsVista) {
            if (sv.getIndice() == indiceTabla) {
                sv.marcarOk();
                return;
            }
        }
    }

    private boolean insercionTerminada() {
        return indiceInsercion >= elementosAInsertar.size();
    }

    private boolean busquedaTerminada() {
        return indiceBusqueda >= clavesABuscar.size();
    }



    //HUD
    /**
     * POST: Dibuja el panel informativo en la esquina superior izquierda de la pantalla.
     */
    private void dibujarHUD(Graphics2D g2) {
        int panelX = 10, panelY = 10, panelW = 470;

        // El mensaje puede ocupar varias lineas: se calcula primero para ajustar el alto del panel.
        List<String> lineasMensaje = envolver(mensajeEstado, MAX_CARACTERES_LINEA);
        int panelH = 104 + lineasMensaje.size() * ALTO_LINEA_MENSAJE;

        Composite original = g2.getComposite();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
        g2.setColor(COLOR_HUD_BG);
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 12, 12);
        g2.setComposite(original);

        g2.setColor(new Color(100, 150, 220));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 12, 12);
        g2.setStroke(new BasicStroke(1f));

        int tx = panelX + 14;
        int ty = panelY + 22;

        // Titulo
        g2.setFont(FONT_TITULO);
        g2.setColor(COLOR_TITULO);
        g2.drawString("# Ciudad Hashing — " + nombreFase(), tx, ty);
        ty += 22;

        // Objetivo actual
        g2.setFont(FONT_HUD);
        g2.setColor(COLOR_HUD_FG);
        g2.drawString(textoObjetivo(), tx, ty);
        ty += 18;

        // Mensaje del ultimo paso / feedback (se muestra completo, en varias lineas si hace falta)
        g2.setFont(FONT_MENSAJE);
        g2.setColor(colorMensaje());
        for (String linea : lineasMensaje) {
            g2.drawString(linea, tx, ty);
            ty += ALTO_LINEA_MENSAJE;
        }
        ty += 4;

        // Contadores
        g2.setColor(new Color(170, 195, 225));
        g2.drawString("Aciertos: " + aciertos + "    Errores: " + errores
                + "    Elementos en tabla: " + ciudad.getTabla().getCantidadElementos(), tx, ty);
        ty += 18;

        // Controles
        g2.setColor(COLOR_CONTROL);
        if (estado == Estado.RESOLVIENDO_INSERCION || estado == Estado.RESOLVIENDO_BUSQUEDA) {
            g2.drawString("[R] o [ESPACIO] Pausar/Reanudar    [ESC] Detener resolver", tx, ty);
        } else {
            g2.drawString("[ESPACIO] Confirmar slot    [R] Resolver automatico", tx, ty);
        }
    }


    /**
     * POST: devuelve el nombre legible de la fase actual. 
     */
    private String nombreFase() {
        switch (estado) {
            case INSERTAR_MANUAL:
            case RESOLVIENDO_INSERCION:
                return "INSERTAR";
            case BUSCAR_MANUAL:
            case RESOLVIENDO_BUSQUEDA:
                return "BUSCAR";
            default:
                return "FIN";
        }
    }


    /**
     * POST: devuelve el texto que describe el objetivo actual del juego
     */
    private String textoObjetivo() {
        if (estado == Estado.FINALIZADO) {
            return "Juego terminado.";
        }

        if (estado == Estado.INSERTAR_MANUAL || estado == Estado.RESOLVIENDO_INSERCION) {
            if (insercionTerminada()) {
                return "Inserciones completas.";
            }

            ElementoHash e = elementosAInsertar.get(indiceInsercion);
            
            return "Insertar elemento " + (indiceInsercion + 1) + "/" + elementosAInsertar.size()
                    + ":  clave=" + e.getClave() + "  (" + e.getNombre() + ")";
        }


        // Fase buscar
        if (busquedaTerminada()) {
            return "Busquedas completas.";
        }

        int clave = clavesABuscar.get(indiceBusqueda);
        
        return "Buscar clave " + (indiceBusqueda + 1) + "/" + clavesABuscar.size() + ":  clave=" + clave;
    }


    /**
     * POST: Color del mensaje segun el resultado de la ultima accion o fin del juego. 
     */
    private Color colorMensaje() {
        if (estado == Estado.FINALIZADO) {
            return COLOR_OK;
        }
        return ultimaAccionFueError ? COLOR_ERROR : COLOR_HUD_FG;
    }


    /**
     * POST: parte un texto en varias lineas para que cada una entre en el ancho del HUD,
     *       corta por palabra, no va a cortar una palabra a la mitad. retorna al menos una linea. 
     */
    private List<String> envolver(String texto, int maxCaracteres) {
        List<String> lineas = new ArrayList<>();

        if (texto == null || texto.isEmpty()) {
            lineas.add("");
            return lineas;
        }

        StringBuilder lineaActual = new StringBuilder();
        
        for (String palabra : texto.split(" ")) {
            
            if (lineaActual.length() == 0) {
                lineaActual.append(palabra);
            } else if (lineaActual.length() + 1 + palabra.length() <= maxCaracteres) {
                lineaActual.append(' ').append(palabra);
            } else {
                lineas.add(lineaActual.toString());
                lineaActual = new StringBuilder(palabra);
            }
        }
        lineas.add(lineaActual.toString());
        return lineas;
    }




    //SETTERS
    /**
     * PRE: callback no null
     * POST: registra la accion a ejecutar cuando el jugador completa la ciudad. 
     */
    public void setOnVictoria(Runnable callback) {
        ValidacionesUtiles.esDistintoDeNull(callback, "callback");
        this.onVictoria = callback;
    }



    
}
