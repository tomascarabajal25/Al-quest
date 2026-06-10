package Juego.ciudades.torresDeHanoi;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

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
    private int   cantidadDiscos;

    // ── Infraestructura de vista ──────────────────────────────────────────────
    private Vista          vista;
    private JFrame         ventana;
    private MinijuegoHanoi minijuego;

    // ── Constructor ───────────────────────────────────────────────────────────

 // El constructor ya no pide la cantidad de discos de forma estática
    public PartidaHanoi(String nombre, Jugador jugador) {
        super(nombre, jugador);
    }

    @Override
    public void iniciar() {
    	ValidacionesUtiles.validarFalso(estaIniciada(), "La partida ya está iniciada");
        setEstado(EstadoDePartida.Iniciado);

        // 1. PEDIR LA DIFICULTAD AL JUGADOR
        Integer[] opcionesDiscos = {3, 4, 5, 6, 7, 8, 9, 10};
        Integer discosElegidos = (Integer) JOptionPane.showInputDialog(
            null, 
            "Selecciona la cantidad de discos (Dificultad):", 
            "Configuración de Torres de Hanoi",
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            opcionesDiscos, 
            opcionesDiscos[0]
        );

<<<<<<< HEAD
        // MinijuegoHanoi recibe 'this' para poder llamar a iniciar()/finalizar()
        this.minijuego = new MinijuegoHanoi(jugador, vista.getTamanio(), this);

        // Inyectar en Vista
        vista.establecerMinijuego(minijuego);
=======
        if (discosElegidos == null) {
            finalizar();
            return;
        }

        this.cantidadDiscos = discosElegidos;

        // 1. Creación del motor lógico con la cantidad de discos elegida
        this.juego = new CiudadHanoi(cantidadDiscos);

        // 2. Creación de la infraestructura de vista (Mundo 3 de Hanoi)
        this.vista = new Vista("/maps/world03.txt", getJugador(), 24, 21, "/assets/jugador/boy");

        // 3. Creación del controlador del minijuego pasándole la vista ya creada
        this.minijuego = new MinijuegoHanoi(getJugador(), vista.tamaño, this);

        // 4. Inyecciones y vinculaciones de comportamiento
        vista.setMinijuego(minijuego);
>>>>>>> fix/VistaAiQuest

        // KeyListener para capturar el control de teclas del puzzle (1/2/3/R/ESC)
        vista.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (minijuego.isActivo()) {
                    minijuego.procesarTecla(e.getKeyChar());
                    e.consume();
                }
            }
        });

        // Configuración del callback para que el minijuego avise al terminar
        minijuego.setOnFinalizadoCallback(this::finalizar);

        // 5. Despliegue de la interfaz gráfica
        ventana = new JFrame();
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setTitle("Torres de Hanoi");
        ventana.add(vista);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        // Asegura que la ventana tome el foco del teclado inmediatamente
        vista.requestFocusInWindow();

        // 6. Arranca el bucle principal de renderizado (60 FPS)
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