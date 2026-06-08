package Juego.ciudades.ordenamientos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JFrame;

import Juego.ciudades.ordenamientos.ui.FabricaMinijuegoOrdenamiento;
import Juego.ciudades.ordenamientos.ui.MinijuegoOrdenamiento;
import modelos.Jugador;
import modelos.Partida;
import modelosVista.Vista;
import utils.ValidacionesUtiles;

/**
 * Partida de la ciudad de ordenamientos en el mundo 2D.
 *
 * Cambio respecto a la versión anterior:
 *   - Ya NO maneja una ventana BMP separada ni un bucle bloqueante.
 *   - iniciar() configura el MinijuegoOrdenamiento en la Vista y arranca
 *     el hilo del juego. El loop de Vista.run() se encarga del resto.
 *   - La partida se puede marcar como finalizada desde afuera cuando el
 *     minijuego notifica victoria (via onVictoria).
 *
 * Tipo T está fijado a Caja porque el juego trabaja con objetos físicos
 * en el mundo. Si en el futuro se generalizan otros elementos, se puede
 * volver a parametrizar.
 */
public class PartidaOrdenamientos extends Partida {

    // ── Atributos ─────────────────────────────────────────────────────────────

    /** Cajas en su orden inicial (no se modifican) */
    private List<Caja>          cajasIniciales;

    /** Algoritmo seleccionado antes de iniciar */
    private Ordenador<Caja>     ordenador;

    /** Referencia a la Vista del juego donde se va a montar el minijuego */
    private Vista               vista;
    
    public JFrame ventana;

    /** Posición en el mapa donde se colocan las cajas (en celdas) */
    private final int           filaBase;
    private final int           colInicio;

    /** El minijuego activo (se crea en iniciar()) */
    private MinijuegoOrdenamiento minijuego;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * Pre:
     * @param nombreCiudad  no nulo
     * @param jugador       no nulo
     * @param cajas         lista no nula con al menos 2 cajas
     * @param ordenador     no nulo (ya seleccionado: Bubble, Selection, etc.)
     * @param vista         Vista activa donde vive el jugador
     * @param filaBase      fila del mundo donde se spawnean las cajas (en celdas)
     * @param colInicio     columna inicial de la primera caja (en celdas)
     *
     * Post: crea la partida en estado "Creado", sin iniciarla todavía
     */
    public PartidaOrdenamientos(String nombreCiudad,
                                 Jugador jugador,
                                 List<Caja> cajas,
                                 Ordenador<Caja> ordenador,
                                 Vista vista,
                                 int filaBase,
                                 int colInicio) {
        super(nombreCiudad, jugador);
        setCajasIniciales(new ArrayList<>(cajas));
        setOrdenador(ordenador);
        setVista(vista);
        this.filaBase  = filaBase;
        this.colInicio = colInicio;
    }

    // ── Comportamiento ────────────────────────────────────────────────────────

    /**
     * Pre:  la partida no debe estar iniciada
     * Post: crea las CajaVista en el mundo, registra el MinijuegoOrdenamiento
     *       en la Vista y arranca el hilo del juego.
     *       El jugador ya puede caminar e interactuar con las cajas.
     */
    @Override
    public void iniciar() {
        ValidacionesUtiles.validarFalso(estaIniciada(), "La partida ya ha sido iniciada");
        setEstado(EstadoDePartida.Iniciado);

        // Construir y registrar el minijuego en el mundo
        minijuego = FabricaMinijuegoOrdenamiento.crear(
                vista,
                cajasIniciales,
                ordenador,
                filaBase,
                colInicio
        );

        // Callback: cuando el jugador gane, esta partida se finaliza
        minijuego.setOnVictoria(() -> {
            setPuntaje(100);
            finalizar();
        });

        // Arrancar el loop visual (si no estaba corriendo ya)
        ventana = new JFrame();
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setTitle("Ciudad de Ordenamientos");
        ventana.add(vista);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        // Arranca el bucle (run → actualizar → repaint a 60 fps)
        vista.startGameThread();
    }

    /**
     * Pre:  la partida debe estar iniciada
     * Post: detiene el hilo del juego y cambia el estado a Creado
     */
    @Override
    public void finalizar() {
        ValidacionesUtiles.validarVerdadero(estaIniciada(), getNombreAlgoritmo());
        vista.detenerHilo();
        setEstado(EstadoDePartida.Creado);
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    /** Post: devuelve el nombre del algoritmo configurado */
    public String getNombreAlgoritmo() {
        return ordenador.getNombre();
    }

    /** Post: devuelve el minijuego activo (null si no se inició todavía) */
    public MinijuegoOrdenamiento getMinijuego() {
        return minijuego;
    }

    // ── Métodos generales ─────────────────────────────────────────────────────

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(cajasIniciales, ordenador);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        PartidaOrdenamientos other = (PartidaOrdenamientos) obj;
        return Objects.equals(cajasIniciales, other.cajasIniciales)
                && Objects.equals(ordenador, other.ordenador);
    }

    @Override
    public String toString() {
        return "PartidaOrdenamientos [cajas=" + cajasIniciales
                + ", ordenador=" + ordenador + "]";
    }

    // ── Setters privados ──────────────────────────────────────────────────────

    private void setCajasIniciales(List<Caja> cajas) {
        ValidacionesUtiles.esDistintoDeNull(cajas, "Las cajas no pueden ser nulas");
        this.cajasIniciales = cajas;
    }

    private void setOrdenador(Ordenador<Caja> ordenador) {
        ValidacionesUtiles.esDistintoDeNull(ordenador, "El ordenador no puede ser nulo");
        this.ordenador = ordenador;
    }

    private void setVista(Vista vista) {
        ValidacionesUtiles.esDistintoDeNull(vista, "La vista no puede ser nula");
        this.vista = vista;
    }
}