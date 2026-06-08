package Juego.ciudades.torresDeHanoi;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.JFrame;

import Juego.ciudades.ordenamientos.EstadoDePartida;
import modelos.Jugador;
import modelos.Partida;
import modelosVista.Vista;
import utils.ValidacionesUtiles;

/**
 * Partida de Torres de Hanoi integrada en la Vista del juego.
 *
 * Unifica en una sola clase lo que antes estaba repartido entre
 * PartidaHanoi (ciclo de vida + ventana) y PartidaDeHanoi (modelo del puzzle).
 *
 * Responsabilidades:
 *  - Gestionar el ciclo de vida: Creado → Iniciado → Creado (finalizado).
 *  - Crear y mostrar la ventana con Vista.
 *  - Contener y exponer el motor lógico CiudadHanoi.
 *  - Calcular y guardar el puntaje al terminar.
 *
 * INVARIANTES:
 *  - 3 <= cantidadDiscos <= 10
 *  - juego != null después de iniciar()
 *
 * Ciclo de vida:
 *   new PartidaHanoi(discos, nombre, jugador) → iniciar() → [juego corre] → finalizar()
 */
public class PartidaHanoi extends Partida {

    // ── Modelo del puzzle ─────────────────────────────────────────────────────
    private CiudadHanoi juego;
    private final int   cantidadDiscos;

    // ── Infraestructura de vista ──────────────────────────────────────────────
    private Vista          vista;
    private JFrame         ventana;
    private MinijuegoHanoi minijuego;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * PRE:  3 <= discos <= 10 | jugador != null
     * POST: crea Vista y MinijuegoHanoi; no abre ventana ni arranca hilo todavía.
     */
    public PartidaHanoi(int discos, String nombre, Jugador jugador) {
        super(nombre, jugador);
        ValidacionesUtiles.validarRangoNumerico(discos, 3, 10, "Cantidad de discos inválida");

        this.cantidadDiscos = discos;

        // Vista con el mapa de la ciudad Hanoi
        this.vista     = new Vista("/maps/world03.txt", getJugador(), 24,21,"/assets/jugador/boy");

        // MinijuegoHanoi recibe 'this' para poder llamar a iniciar()/finalizar()
        this.minijuego = new MinijuegoHanoi(jugador, vista.tamaño, this);

        // Inyectar en Vista
        vista.setMinijuego(minijuego);

        // KeyListener para las teclas del puzzle (1/2/3/R/ESC)
        vista.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (minijuego.isActivo()) {
                    minijuego.procesarTecla(e.getKeyChar());
                    e.consume();
                }
            }
        });

        // Callback al finalizar: el minijuego avisa cuando el jugador gana/sale
        minijuego.setOnFinalizadoCallback(this::finalizar);

        setEstado(EstadoDePartida.Creado);
    }

    // ── Ciclo de vida ─────────────────────────────────────────────────────────

    /**
     * PRE:  estado == Creado
     * POST: crea CiudadHanoi, abre ventana, arranca hilo de Vista.
     */
    @Override
    public void iniciar() {
        ValidacionesUtiles.validarFalso(estaIniciada(), "La partida ya está iniciada");
        setEstado(EstadoDePartida.Iniciado);

        // Crear el motor del puzzle recién aquí (igual que hacía PartidaDeHanoi)
        this.juego = new CiudadHanoi(cantidadDiscos);

        ventana = new JFrame();
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setTitle("Torres de Hanoi");
        ventana.add(vista);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        vista.startGameThread();
    }

    /**
     * PRE:  estado == Iniciado
     * POST: calcula puntaje, detiene hilo, cierra ventana.
     */
    @Override
    public void finalizar() {
        ValidacionesUtiles.validarVerdadero(estaIniciada(), "La partida no está iniciada");
        setEstado(EstadoDePartida.Creado);
        setPuntaje(calcularPuntaje());

        vista.detenerHilo();

        if (ventana != null) {
            ventana.dispose();
            ventana = null;
        }
    }

    /**
     * Permite que clases externas (p.ej. MinijuegoHanoi) registren el puntaje.
     */
    public void actualizarPuntaje(int puntos) {
        this.setPuntaje(puntos);
    }

    // ── Lógica de puntaje ─────────────────────────────────────────────────────

    private int calcularPuntaje() {
        if (juego == null || !juego.haGanado()) return 0;
        int multiplicador = juego.getObjetivo();
        int puntos        = juego.esPerfecto() ? 150 : 100;
        return puntos * multiplicador;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    /**
     * Devuelve el motor lógico del puzzle.
     * PRE: iniciar() ya fue llamado.
     */
    public CiudadHanoi getJuego() {
        return juego;
    }

    public int getCantidadDeDiscos() {
        return cantidadDiscos;
    }

    // ── equals / hashCode / toString ─────────────────────────────────────────

    @Override
    public String toString() {
        return "PartidaHanoi [cantidadDiscos=" + cantidadDiscos + ", juego=" + juego + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(cantidadDiscos, juego);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)                    return true;
        if (obj == null)                    return false;
        if (!super.equals(obj))             return false;
        if (getClass() != obj.getClass())   return false;
        PartidaHanoi other = (PartidaHanoi) obj;
        return cantidadDiscos == other.cantidadDiscos
            && Objects.equals(juego, other.juego);
    }
}