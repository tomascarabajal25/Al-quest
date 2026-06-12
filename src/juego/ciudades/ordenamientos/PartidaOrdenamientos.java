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
 * TDA PartidaOrdenamientos — partida de la ciudad de ordenamientos en el mundo 2D.
 *
 * Responsabilidades:
 * - Mantener el constructor liviano en estado Creado.
 * - Solicitar de forma interactiva (en iniciar) el algoritmo, la cantidad de cajas y sus tamaños.
 * - Inicializar la infraestructura de Vista, la ventana JFrame y el hilo de renderizado.
 * - Gestionar el cierre seguro liberando la ventana y notificando al mapa global.
 */
public class PartidaOrdenamientos extends Partida {

    // CONSTANTES

    /** Fila inicial donde aparece el jugador al entrar a la ciudad. */
    private static final int FILA_BASE = 48;

    /** Columna inicial donde aparece el jugador al entrar a la ciudad. */
    private static final int COL_INICIO = 1;

    /** Cantidad mínima de cajas que el jugador puede configurar. */
    private static final int CANTIDAD_MINIMA_CAJAS = 2;

    /** Cantidad máxima de cajas que el jugador puede configurar. */
    private static final int CANTIDAD_MAXIMA_CAJAS = 8;

    /** Puntaje otorgado al completar el desafío de ordenamiento. */
    private static final int PUNTAJE_VICTORIA = 1000;

    // ATRIBUTOS

    /** Cajas en su orden inicial configurado por el usuario. */
    private List<Caja> cajasIniciales;

    /** Algoritmo seleccionado interactivamente por el usuario. */
    private Ordenador<Caja> ordenador;

    /** Referencia a la Vista del juego donde se va a montar el minijuego. */
    private Vista vista;

    /** Ventana gráfica de la ciudad. */
    public JFrame ventana;

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
    public PartidaOrdenamientos(
            String nombreCiudad,
            Jugador jugador) {

        super(nombreCiudad, jugador);
        setEstado(EstadoDePartida.Creado);
    }

    // METODOS GENERALES

    /**
     * Compara esta partida con otro objeto en base a las cajas iniciales
     * y al ordenador configurado.
     *
     * @param obj objeto a comparar con esta partida
     * @return true si ambos objetos representan la misma partida,
     *         false en caso contrario
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        PartidaOrdenamientos otraPartida = (PartidaOrdenamientos) obj;
        return Objects.equals(cajasIniciales, otraPartida.cajasIniciales)
                && Objects.equals(ordenador, otraPartida.ordenador);
    }

    /**
     * Calcula el código hash de la partida en base a las cajas iniciales
     * y al ordenador configurado.
     *
     * @return código hash de la partida
     */
    @Override
    public int hashCode() {
        final int numeroPrimo = 31;
        int resultado = super.hashCode();
        resultado = numeroPrimo * resultado + Objects.hash(cajasIniciales, ordenador);
        return resultado;
    }

    /**
     * Devuelve una representación textual de la partida, útil para
     * depuración.
     *
     * @return texto con las cajas iniciales y el ordenador configurado
     */
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

        // 1. Configuración interactiva mediante cuadros de diálogo (Input del usuario)
        configurarPartidaInteractivamente();

        // Cambiamos el estado una vez que pasó exitosamente las configuraciones
        setEstado(EstadoDePartida.Iniciado);

        // 2. Creación diferida de la Vista utilizando las constantes de posición
        this.vista = new Vista(
                "/maps/world02.txt",
                getJugador(),
                COL_INICIO,
                FILA_BASE,
                "/assets/jugador/boy");

        // 3. Construir y registrar el controlador del minijuego en el mundo
        this.minijuego = FabricaMinijuegoOrdenamiento.crear(
                vista,
                cajasIniciales,
                ordenador);

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

        // Detener el bucle principal de renderizado
        if (vista != null) {
            vista.detenerHilo();
        }

        // Destruir y cerrar la ventana de forma segura
        if (ventana != null) {
            ventana.dispose();
            ventana = null;
        }

        // Sincronización obligatoria con el grafo del mapa global
        notificarFinalizacion();
    }

    /**
     * Calcula el puntaje obtenido al completar el desafío de ordenamiento.
     *
     * Post:
     * - Devuelve siempre el puntaje fijo asignado a la victoria de esta ciudad.
     *
     * @return puntaje obtenido por completar la ciudad
     */
    public int calcularPuntaje() {
        return PUNTAJE_VICTORIA;
    }

    /**
     * Pide al usuario, de forma secuencial y guiada mediante ventanas
     * emergentes, el algoritmo de ordenamiento y las cajas a ordenar.
     *
     * Post:
     * - Se asignan los atributos ordenador y cajasIniciales con los
     *   valores ingresados por el usuario.
     * - Si ocurre un error o una cancelación inesperada, se cargan los
     *   valores por defecto mediante cargarConfiguracionPorDefecto().
     */
    private void configurarPartidaInteractivamente() {
        try {
            this.ordenador = seleccionarOrdenador();

            int cantidadDeCajas = pedirCantidadDeCajas();
            this.cajasIniciales = generarCajasIniciales(cantidadDeCajas);

        } catch (Exception excepcion) {
            // Manejo defensivo en caso de ingresos erróneos o cancelaciones imprevistas
            JOptionPane.showMessageDialog(
                    null,
                    "Configuración inválida detectada. Se usarán valores iniciales por defecto.");
            cargarConfiguracionPorDefecto();
        }
    }

    /**
     * Pide al usuario que seleccione el algoritmo de ordenamiento mediante
     * un cuadro de diálogo.
     *
     * Post:
     * - Devuelve el Ordenador correspondiente a la opción elegida.
     * - Si el usuario cierra el diálogo sin elegir, se utiliza por defecto
     *   la primera opción de la lista (Bubble Sort).
     *
     * @return ordenador seleccionado por el usuario
     */
    private Ordenador<Caja> seleccionarOrdenador() {
        String[] algoritmosDisponibles = {"Bubble Sort", "Selection Sort"};

        int opcionSeleccionada = JOptionPane.showOptionDialog(
                null,
                "Seleccione el método de ordenamiento para el desafío:",
                "Configuración de la Ciudad",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                algoritmosDisponibles,
                algoritmosDisponibles[0]);

        // Si cierra la ventana, se toma la primera opción por defecto de manera segura
        if (opcionSeleccionada == JOptionPane.CLOSED_OPTION) {
            opcionSeleccionada = 0;
        }

        if (opcionSeleccionada == 0) {
            return new OrdenadorBubble<Caja>("ordenador bubble");
        }
        return new OrdenadorSelection<Caja>("Ordenador Selection");
    }

    /**
     * Pide al usuario la cantidad de cajas que desea ordenar mediante un
     * cuadro de diálogo.
     *
     * Post:
     * - Devuelve la cantidad de cajas ingresada, validada dentro del rango
     *   permitido (CANTIDAD_MINIMA_CAJAS a CANTIDAD_MAXIMA_CAJAS).
     * - Si el usuario cancela o deja el campo vacío, devuelve un valor por
     *   defecto de 4 cajas.
     *
     * @return cantidad de cajas a ordenar
     */
    private int pedirCantidadDeCajas() {
        String textoCantidadDeCajas = JOptionPane.showInputDialog(
                null,
                "¿Cuántas cajas desea ordenar? (Mínimo "
                        + CANTIDAD_MINIMA_CAJAS + ", Máximo " + CANTIDAD_MAXIMA_CAJAS + "):",
                "Cantidad de Cajas",
                JOptionPane.QUESTION_MESSAGE);

        int cantidadDeCajas;
        if (textoCantidadDeCajas == null || textoCantidadDeCajas.isEmpty()) {
            cantidadDeCajas = 4;
        } else {
            cantidadDeCajas = Integer.parseInt(textoCantidadDeCajas);
        }

        ValidacionesUtiles.validarRangoNumerico(
                cantidadDeCajas,
                CANTIDAD_MINIMA_CAJAS,
                CANTIDAD_MAXIMA_CAJAS,
                "Cantidad de cajas fuera de rango permitido");

        return cantidadDeCajas;
    }

    /**
     * Genera la lista de cajas iniciales, pidiendo al usuario el tamaño de
     * cada una mediante cuadros de diálogo.
     *
     * Pre:
     * - cantidadDeCajas > 0
     *
     * Post:
     * - Devuelve una lista con cantidadDeCajas cajas, cada una con el
     *   tamaño ingresado por el usuario (o un valor por defecto si se
     *   cancela el diálogo correspondiente).
     *
     * @param cantidadDeCajas cantidad de cajas a generar
     * @return lista de cajas iniciales configuradas por el usuario
     */
    private List<Caja> generarCajasIniciales(int cantidadDeCajas) {
        List<Caja> cajas = new ArrayList<>();

        for (int numeroDeCaja = 0; numeroDeCaja < cantidadDeCajas; numeroDeCaja++) {
            int tamañoDeCaja = pedirTamañoDeCaja(numeroDeCaja);
            cajas.add(new Caja("" + numeroDeCaja, tamañoDeCaja, true));
        }

        return cajas;
    }

    /**
     * Pide al usuario el tamaño de una caja específica mediante un cuadro
     * de diálogo.
     *
     * Pre:
     * - numeroDeCaja >= 0
     *
     * Post:
     * - Devuelve el tamaño ingresado por el usuario, validado como mayor a cero.
     * - Si el usuario cancela o deja el campo vacío, devuelve un valor por
     *   defecto basado en la posición de la caja.
     *
     * @param numeroDeCaja posición (0-based) de la caja dentro de la configuración
     * @return tamaño asignado a la caja
     */
    private int pedirTamañoDeCaja(int numeroDeCaja) {
        String textoTamañoDeCaja = JOptionPane.showInputDialog(
                null,
                "Ingrese el tamaño numérico para la caja " + (numeroDeCaja + 1) + ":",
                "Tamaño de Caja",
                JOptionPane.QUESTION_MESSAGE);

        int tamañoDeCaja;
        if (textoTamañoDeCaja == null || textoTamañoDeCaja.isEmpty()) {
            tamañoDeCaja = (numeroDeCaja + 1) * 10;
        } else {
            tamañoDeCaja = Integer.parseInt(textoTamañoDeCaja);
        }

        ValidacionesUtiles.validarMayorACero(tamañoDeCaja, "Tamaño de caja " + (numeroDeCaja + 1));

        return tamañoDeCaja;
    }

    /**
     * Carga una configuración por defecto, utilizada como respaldo seguro
     * cuando ocurre un error durante la configuración interactiva.
     *
     * Post:
     * - this.ordenador queda asignado a un OrdenadorBubble.
     * - this.cajasIniciales queda asignado a una lista fija de seis cajas
     *   con tamaños predefinidos.
     */
    private void cargarConfiguracionPorDefecto() {
        this.ordenador = new OrdenadorBubble<Caja>("");

        this.cajasIniciales = new ArrayList<>();
        this.cajasIniciales.add(new Caja("A", 40, true));
        this.cajasIniciales.add(new Caja("B", 10, true));
        this.cajasIniciales.add(new Caja("C", 30, true));
        this.cajasIniciales.add(new Caja("D", 20, true));
        this.cajasIniciales.add(new Caja("E", 45, true));
        this.cajasIniciales.add(new Caja("F", 22, true));
    }

    // GETTERS

    /**
     * Devuelve el nombre del algoritmo de ordenamiento configurado.
     *
     * Post:
     * - Si todavía no se configuró un ordenador, devuelve "Sin configurar".
     *
     * @return nombre del algoritmo de ordenamiento
     */
    public String getNombreAlgoritmo() {
        if (ordenador != null) {
            return ordenador.getNombre();
        }
        return "Sin configurar";
    }

    /**
     * Devuelve el minijuego de ordenamiento activo.
     *
     * Post:
     * - Devuelve null si la partida todavía no fue iniciada.
     *
     * @return minijuego de ordenamiento activo
     */
    public MinijuegoOrdenamiento getMinijuego() {
        return minijuego;
    }
}