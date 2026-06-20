package juego.ciudades.ordenamientos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import juego.ciudades.ordenamientos.ui.FabricaMinijuegoOrdenamiento;
import juego.ciudades.ordenamientos.ui.MinijuegoOrdenamiento;
import juego.configuracion.ConfiguracionDeOrdenamientos;
import modelos.Jugador;
import modelos.Partida;
import modelosVista.Vista;
import utils.ValidacionesUtiles;

/**
 * TDA PartidaOrdenamientos — partida de la ciudad de ordenamientos en el mundo 2D.
 *
 * Responsabilidades:
 * - Mantener el constructor liviano en estado Creado.
 * - Solicitar de forma interactiva (en iniciar) el algoritmo, la cantidad de cajas y sus tamaños.
 * - Inicializar la infraestructura de Vista, la ventana JFrame y el hilo de renderizado.
 * - Gestionar el cierre seguro liberando la ventana y notificando al mapa global.
 */
public class PartidaOrdenamientos extends Partida {

    // ATRIBUTOS

    /** Cajas en su orden inicial configurado por el usuario. */
    private List<Caja> cajasIniciales;

    /** Algoritmo seleccionado interactivamente por el usuario. */
    private Ordenador<Caja> ordenador;

    /** Referencia a la Vista del juego donde se va a montar el minijuego. */
    private Vista vista;

    /** Ventana gráfica de la ciudad. */
    private JFrame ventana;

    /** El minijuego activo (se crea en iniciar()). */
    private MinijuegoOrdenamiento minijuego;

    // CONSTRUCTORES

    /**
     * Crea la partida de forma liviana, sin reservar memoria para mapas
     * ni colecciones pesadas.
     *
     * Pre:
     * - nombreCiudad != null
     * - jugador != null
     *
     * Post:
     * - La partida queda creada en estado Creado, lista para ser
     *   configurada e iniciada más adelante.
     *
     * @param nombreCiudad nombre de la ciudad asociada a esta partida
     * @param jugador      jugador que participa en la partida
     */
    public PartidaOrdenamientos(String nombreCiudad, Jugador jugador) {
        super(nombreCiudad, jugador);
        setEstado(EstadoDePartida.Creado);
    }

    // METODOS GENERALES

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        PartidaOrdenamientos otraPartida = (PartidaOrdenamientos) obj;
        return Objects.equals(cajasIniciales, otraPartida.cajasIniciales)
                && Objects.equals(ordenador, otraPartida.ordenador);
    }

    @Override
    public int hashCode() {
        final int numeroPrimo = 31;
        int resultado = super.hashCode();
        resultado = numeroPrimo * resultado + Objects.hash(cajasIniciales, ordenador);
        return resultado;
    }

    @Override
    public String toString() {
        return "PartidaOrdenamientos [cajas=" + cajasIniciales
                + ", ordenador=" + ordenador + "]";
    }

    // METODOS DE COMPORTAMIENTO

    /**
     * Inicia la partida de ordenamientos.
     *
     * Pre:
     * - La partida debe estar en estado Creado (no iniciada).
     *
     * Post:
     * - Se solicitan los parámetros al usuario mediante ventanas emergentes.
     * - Se construye el mapa gráfico y las cajas físicas.
     * - Se vincula el callback de victoria.
     * - Se despliega la ventana y arranca el hilo de ejecución a 60 FPS.
     */
    @Override
    public void iniciar() {
        ValidacionesUtiles.validarFalso(estaIniciada(), "La partida ya ha sido iniciada");

        configurarPartidaInteractivamente();

        setEstado(EstadoDePartida.Iniciado);

        this.vista = new Vista(
            ConfiguracionDeOrdenamientos.RUTA_MAPA_MUNDO,
            getJugador(),
            ConfiguracionDeOrdenamientos.COL_INICIO,
            ConfiguracionDeOrdenamientos.FILA_BASE,
            getRutaSprites(),
            this.sonido
        );

        this.minijuego = FabricaMinijuegoOrdenamiento.crear(
            vista,
            cajasIniciales,
            ordenador
        );

        minijuego.setOnVictoria(() -> {
            setPuntaje(calcularPuntaje());
            finalizar();
        });

        ventana = new JFrame("Ciudad de Ordenamientos - " + getNombreAlgoritmo());
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.setResizable(false);
        ventana.add(vista);
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        ventana.setVisible(true);

        vista.requestFocusInWindow();
        vista.startGameThread();
         if (this.sonido != null) {
        	 this.sonido.playMusica(juego.configuracion.ConstantesSonido.ORDENAMIENTO);
         }
    }

    /**
     * Finaliza la partida de ordenamientos.
     *
     * Pre:
     * - La partida debe estar en estado Iniciado.
     *
     * Post:
     * - Se detiene el hilo del juego de forma segura.
     * - Se destruye la ventana gráfica.
     * - Se notifica al mapa global para abrir los caminos correspondientes.
     */
    @Override
    public void finalizar() {
        ValidacionesUtiles.validarVerdadero(estaIniciada(), "La partida no está iniciada");
        setEstado(EstadoDePartida.Creado);

        if (vista != null) {
            vista.detenerHilo();
        }
        if (ventana != null) {
            ventana.dispose();
            ventana = null;
        }

         if (this.sonido != null) {
		 	this.sonido.stopMusica();
		 	this.sonido.playMusica(juego.configuracion.ConstantesSonido.GLOBAL_AVENTURA);
		 }
        notificarFinalizacion();
    }

    /**
     * Post: devuelve el puntaje fijo asignado a la victoria de esta ciudad.
     */
    public int calcularPuntaje() {
        return ConfiguracionDeOrdenamientos.PUNTAJE_VICTORIA;
    }

    /**
     * Pide al usuario, de forma secuencial y guiada, el algoritmo de
     * ordenamiento y las cajas a ordenar.
     *
     * Post:
     * - Se asignan los atributos ordenador y cajasIniciales.
     * - Si ocurre un error o cancelación, se cargan los valores por defecto.
     */
    private void configurarPartidaInteractivamente() {
        try {
            this.ordenador      = seleccionarOrdenador();
            int cantidadDeCajas = pedirCantidadDeCajas();
            this.cajasIniciales = generarCajasIniciales(cantidadDeCajas);
        } catch (Exception excepcion) {
            JOptionPane.showMessageDialog(
                null,
                "Configuración inválida detectada. Se usarán valores iniciales por defecto.");
            cargarConfiguracionPorDefecto();
        }
    }

    /**
     * Post: devuelve el Ordenador elegido por el usuario.
     *       Si cierra el diálogo sin elegir, usa Bubble Sort por defecto.
     */
    private Ordenador<Caja> seleccionarOrdenador() {
        int opcion = JOptionPane.showOptionDialog(
            null,
            "Seleccioná el método de ordenamiento para el desafío:",
            "Configuración de la Ciudad",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            ConfiguracionDeOrdenamientos.ALGORITMOS_DISPONIBLES,
            ConfiguracionDeOrdenamientos.ALGORITMOS_DISPONIBLES[0]
        );

        if (opcion == JOptionPane.CLOSED_OPTION) {
            opcion = 0;
        }

        return opcion == 0
            ? new OrdenadorBubble<Caja>(ConfiguracionDeOrdenamientos.ID_BUBBLE_SORT)
            : new OrdenadorSelection<Caja>(ConfiguracionDeOrdenamientos.ID_SELECTION_SORT);
    }

    /**
     * Post: devuelve la cantidad de cajas ingresada, validada en el rango permitido.
     *       Si el usuario cancela, devuelve CANTIDAD_CAJAS_POR_DEFECTO.
     */
    private int pedirCantidadDeCajas() {
        String texto = JOptionPane.showInputDialog(
            null,
            "¿Cuántas cajas querés ordenar? (Mínimo "
                + ConfiguracionDeOrdenamientos.CANTIDAD_MINIMA_CAJAS
                + ", Máximo "
                + ConfiguracionDeOrdenamientos.CANTIDAD_MAXIMA_CAJAS + "):",
            "Cantidad de Cajas",
            JOptionPane.QUESTION_MESSAGE
        );

        int cantidad = (texto == null || texto.isEmpty())
            ? ConfiguracionDeOrdenamientos.CANTIDAD_CAJAS_POR_DEFECTO
            : Integer.parseInt(texto);

        ValidacionesUtiles.validarRangoNumerico(
            cantidad,
            ConfiguracionDeOrdenamientos.CANTIDAD_MINIMA_CAJAS,
            ConfiguracionDeOrdenamientos.CANTIDAD_MAXIMA_CAJAS,
            "Cantidad de cajas fuera de rango permitido"
        );

        return cantidad;
    }

    /**
     * Pre:  cantidadDeCajas > 0
     * Post: devuelve la lista de cajas con los tamaños ingresados por el usuario.
     */
    private List<Caja> generarCajasIniciales(int cantidadDeCajas) {
        List<Caja> cajas = new ArrayList<>();
        for (int i = 0; i < cantidadDeCajas; i++) {
            int tamaño = pedirTamañoDeCaja(i);
            cajas.add(new Caja(String.valueOf(i), tamaño, true));
        }
        return cajas;
    }

    /**
     * Pre:  numeroDeCaja >= 0
     * Post: devuelve el tamaño ingresado por el usuario, validado como mayor a cero.
     *       Si cancela, usa (numeroDeCaja + 1) * 10 como valor por defecto.
     */
    private int pedirTamañoDeCaja(int numeroDeCaja) {
        String texto = JOptionPane.showInputDialog(
            null,
            "Ingresá el tamaño numérico para la caja " + (numeroDeCaja + 1) + ":",
            "Tamaño de Caja",
            JOptionPane.QUESTION_MESSAGE
        );

        int tamaño = (texto == null || texto.isEmpty())
            ? (numeroDeCaja + 1) * 10
            : Integer.parseInt(texto);

        ValidacionesUtiles.validarMayorACero(tamaño, "Tamaño de caja " + (numeroDeCaja + 1));
        return tamaño;
    }

    /**
     * Post: asigna configuración por defecto (BubbleSort + 6 cajas fijas)
     *       cuando ocurre un error durante la configuración interactiva.
     */
    private void cargarConfiguracionPorDefecto() {
        this.ordenador = new OrdenadorBubble<>(ConfiguracionDeOrdenamientos.ID_BUBBLE_SORT);
        this.cajasIniciales = new ArrayList<>(List.of(
            new Caja("A", 40, true),
            new Caja("B", 10, true),
            new Caja("C", 30, true),
            new Caja("D", 20, true),
            new Caja("E", 45, true),
            new Caja("F", 22, true)
        ));
    }

    // GETTERS

    /**
     * Post: devuelve el nombre del algoritmo configurado, o "Sin configurar" si es null.
     */
    public String getNombreAlgoritmo() {
        return ordenador != null ? ordenador.getNombre() : "Sin configurar";
    }

    /**
     * Post: devuelve el minijuego activo, o null si la partida no fue iniciada.
     */
    public MinijuegoOrdenamiento getMinijuego() {
        return minijuego;
    }
}