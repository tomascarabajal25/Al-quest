package juego.ciudades.ordenamientos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import juego.ciudades.ordenamientos.ui.FabricaMinijuegoOrdenamiento;
import juego.ciudades.ordenamientos.ui.MinijuegoOrdenamiento;
import modelos.Jugador;
import modelos.Partida;
import modelosVista.Vista;
import utils.ValidacionesUtiles;

/**
 * Partida de la ciudad de ordenamientos en el mundo 2D.
 *
 * Responsabilidades:
 * - Mantener el constructor liviano en estado Creado.
 * - Solicitar de forma interactiva (en iniciar) el algoritmo, la cantidad de cajas y sus tamaños.
 * - Inicializar la infraestructura de Vista, la ventana JFrame y el hilo de renderizado.
 * - Gestionar el cierre seguro liberando la ventana y notificando al mapa global.
 */
public class PartidaOrdenamientos extends Partida {

    // ── Constantes de Configuración de Spawn (Próximamente archivo de config) ─
    private static final int FILA_BASE  = 48;
    private static final int COL_INICIO = 1;

    // ── Atributos Dinámicos ───────────────────────────────────────────────────
    
    /** Cajas en su orden inicial configurado por el usuario */
    private List<Caja>          cajasIniciales;

    /** Algoritmo seleccionado interactivamente por el usuario */
    private Ordenador<Caja>      ordenador;

    /** Referencia a la Vista del juego donde se va a montar el minijuego */
    private Vista               vista;
    
    public JFrame               ventana;

    /** El minijuego activo (se crea en iniciar()) */
    private MinijuegoOrdenamiento minijuego;

    // ── Constructor ───────────────────────────────────────────────────────────

    /**
     * PRE:  nombreCiudad != null | jugador != null
     * POST: Crea la partida de forma ultra liviana en estado "Creado".
     * No reserva memoria para mapas ni colecciones pesadas todavía.
     */
    public PartidaOrdenamientos(String nombreCiudad, Jugador jugador) {
        super(nombreCiudad, jugador);
        setEstado(EstadoDePartida.Creado);
    }

    // ── Comportamiento de Ciclo de Vida ───────────────────────────────────────

    /**
     * PRE:  La partida debe estar en estado Creado (no iniciada).
     * POST: Solicita los parámetros al usuario mediante ventanas emergentes, 
     * construye el mapa gráfico, las cajas físicas, vincula los callbacks,
     * despliega la ventana y arranca el hilo de ejecución a 60 FPS.
     */
    @Override
    public void iniciar() {
        ValidacionesUtiles.validarFalso(estaIniciada(), "La partida ya ha sido iniciada");
        
        // 1. Configuración interactiva mediante cuadros de diálogo (Input del usuario)
        configurarPartidaInteractivamente();

        // Cambiamos el estado una vez que pasó exitosamente las configuraciones
        setEstado(EstadoDePartida.Iniciado);

        // 2. Creación diferida de la Vista utilizando las constantes de posición
        this.vista = new Vista("/maps/world02.txt", getJugador(), COL_INICIO, FILA_BASE, "/assets/jugador/boy");

        // 3. Construir y registrar el controlador del minijuego en el mundo
        this.minijuego = FabricaMinijuegoOrdenamiento.crear(
                vista,
                cajasIniciales,
                ordenador
        );

        // 4. Configurar el Callback de Victoria
        minijuego.setOnVictoria(() -> {
            setPuntaje(calcularPuntaje());
            finalizar();
        });

        // 5. Despliegue de la Interfaz Gráfica de la Ciudad
        ventana = new JFrame();
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setTitle("Ciudad de Ordenamientos - " + getNombreAlgoritmo());
        ventana.add(vista);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        // Asegurar foco del teclado para mover al personaje inmediatamente
        vista.requestFocusInWindow();

        // 6. Arranca el bucle principal (run -> actualizar -> repaint a 60 fps)
        vista.startGameThread();
    }

    /**
     * PRE:  La partida debe estar en estado Iniciado.
     * POST: Detiene el hilo del juego de forma segura, destruye la ventana gráfica,
     * establece el puntaje definitivo y notifica al mapa global para abrir caminos.
     */
    @Override
    public void finalizar() {
        ValidacionesUtiles.validarVerdadero(estaIniciada(), "La partida no está iniciada");
        setEstado(EstadoDePartida.Creado);
        
        // Detener el bucle principal de renderizado
        if (vista != null) {
            vista.detenerHilo();
        }

        // CORRECCIÓN: Destruir y cerrar la ventana de forma segura
        if (ventana != null) {
            ventana.dispose();
            ventana = null;
        }

        // Sincronización obligatoria con el grafo del mapa global
        notificarFinalizacion();
    }

    // ── Métodos Auxiliares de Configuración Dinámica ─────────────────────────

    /**
     * Se encarga de pedirle al usuario de forma secuencial y guiada las opciones de juego.
     */
    private void configurarPartidaInteractivamente() {
        try {
            // A. Selección del Ordenador (Algoritmo)
            String[] algoritmosDisponibles = {"Bubble Sort", "Selection Sort"};
            int seleccionAlgoritmo = JOptionPane.showOptionDialog(
                    null,
                    "Seleccione el método de ordenamiento para el desafío:",
                    "Configuración de la Ciudad",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    algoritmosDisponibles,
                    algoritmosDisponibles[0]
            );

            // Si cierra la ventana se cancela o toma el primero por defecto de manera segura
            if (seleccionAlgoritmo == JOptionPane.CLOSED_OPTION) seleccionAlgoritmo = 0;
            
            this.ordenador = seleccionAlgoritmo==0?new OrdenadorBubble<Caja>("ordenador bubble"): 
            	new OrdenadorSelection<Caja>("Ordenador Selection"); 

            // B. Selección de cantidad de cajas
            String cantStr = JOptionPane.showInputDialog(null, "¿Cuántas cajas desea ordenar? (Mínimo 2, Máximo 8):", "Cantidad de Cajas", JOptionPane.QUESTION_MESSAGE);
            int cantidadCajas = (cantStr == null || cantStr.isEmpty()) ? 4 : Integer.parseInt(cantStr);
            ValidacionesUtiles.validarRangoNumerico(cantidadCajas, 2, 8, "Cantidad de cajas fuera de rango permitido");

            // C. Selección del tamaño de cada caja respectiva
            this.cajasIniciales = new ArrayList<>();
            for (int i = 0; i < cantidadCajas; i++) {
                String tamStr = JOptionPane.showInputDialog(null, "Ingrese el tamaño numérico para la caja " + (i + 1) + ":", "Tamaño de Caja", JOptionPane.QUESTION_MESSAGE);
                int tamaño = (tamStr == null || tamStr.isEmpty()) ? (i + 1) * 10 : Integer.parseInt(tamStr);
                ValidacionesUtiles.validarMayorACero(tamaño, "Tamaño de caja " + (i + 1));
                
                this.cajasIniciales.add(new Caja(""+i,tamaño,true));
            }

        } catch (Exception e) {
            // Manejo defensivo en caso de ingresos erróneos o cancelaciones imprevistas
            JOptionPane.showMessageDialog(null, "Configuración inválida detectada. Se usarán valores iniciales por defecto.");
            cargarConfiguracionPorDefecto();
        }
    }

    private void cargarConfiguracionPorDefecto() {
        // Fallback seguro en caso de errores en los inputs gráficos
        this.ordenador = new OrdenadorBubble<Caja>("");
        this.cajasIniciales = new ArrayList<>();
        this.cajasIniciales.add(new Caja("A", 40, true));
        this.cajasIniciales.add(new Caja("B", 10, true));
        this.cajasIniciales.add(new Caja("C", 30, true));
        this.cajasIniciales.add(new Caja("D", 20, true));
        this.cajasIniciales.add(new Caja("E", 45, true));
        this.cajasIniciales.add(new Caja("F", 22, true));
    }

    // ── Lógica de Puntaje ─────────────────────────────────────────────────────

    public int calcularPuntaje() {
    	//validar si gano
        return 1000;
    }

    // ── Getters ───────────────────────────────────────────────────────────────

    public String getNombreAlgoritmo() {
        return (ordenador != null) ? ordenador.getNombre() : "Sin configurar";
    }

    public MinijuegoOrdenamiento getMinijuego() {
        return minijuego;
    }

    // ── Métodos Generales Reescritos ──────────────────────────────────────────

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
        return "PartidaOrdenamientos [cajas=" + cajasIniciales + ", ordenador=" + ordenador + "]";
    }
}